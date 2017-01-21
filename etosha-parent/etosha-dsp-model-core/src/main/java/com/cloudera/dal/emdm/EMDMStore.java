/*
 * EMDMStore is a simple content handler for HBase.
 *
 * You can give it a "match result" and HBase will store it. No matter what
 * kind of metadata you want to relate to it later, just add new rows with more
 * dimensions. 
 * 
 * HBase allows us to scale out the matching engine across multiple machines. 
 */
package com.cloudera.dal.emdm;

import com.cloudera.dal.admin.EMDMBaseTabAdmin;
import com.cloudera.utils.ObjectEncoder;
import com.cloudera.dal.HBaseEMDMStoreAdapter;
import com.cloudera.dal.graph.TopicGraphLink;
import com.cloudera.utils.StatisticalSummary; 
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import javax.jws.WebService;
import javax.jws.WebMethod;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * EMDMStore is the component used in the WEB-UI and in the
 Spark-Matching-Engine
 *
 * @author kamir
 */
@WebService(serviceName = "MatchStore.v2")
public class EMDMStore implements Serializable {

    // TODO in parallel mode: use aggregator instead of this variable!!!
    public static int counter_z_emptyMatchObjects = 0;

    public static transient HBaseEMDMStoreAdapter adapter = null;

    static EMDMStore ms = null;

    /**
     * Just write one record to eliminate any access-issues.
     *
     * @throws IOException
     */
    public static void testWrite() throws IOException {
        ms.putMatchResults(MatchingResult.getTestMatchSamples3());
    }

    public static String getTableName() {
        return EMDMBaseTabAdmin.docTabName;
    }

    public static int getNrOfRows() {
        return adapter.getNrOfRows();
    }

    public static void reset() {
        ms = null;
        adapter = null;
    }

    @WebMethod
    public static EMDMStore init() {

        ms = new EMDMStore();

        if (ms.adapter == null) {
            adapter = HBaseEMDMStoreAdapter.init();
        }

        System.out.println("\n>>> MatchStore.init() : { STATUS: " + adapter.status() + ". }");

        return ms;
    }

    public static EMDMStore _forceInit() {

        ms = new EMDMStore();

        try {

            adapter = HBaseEMDMStoreAdapter.init();

            EMDMBaseTabAdmin.initTable(HBaseEMDMStoreAdapter.config, EMDMBaseTabAdmin.docTabName);

        } catch (ZooKeeperConnectionException ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\n>>> MatchStore.forceInit() : { STATUS: " + adapter.status() + ". }");

        return ms;
    }

    private static void countEmptyResultSet() {
        EMDMStore.counter_z_emptyMatchObjects++;
    }

    @WebMethod
    public void close() {

        adapter = null;

        System.out.println(">>> MatchStore.close() : { A new MatchStore instance is required for future requests. }");

    }

    /**
     * Is a document with this particular key stored?
     *
     * @param sk
     * @return
     */
    @WebMethod
    public boolean hasKey(String sk) {

        boolean ret = false;

        try {

            System.out.println(">>> lookup key: " + sk);
            byte[] k = sk.getBytes();

            if (k != null) {
                ret = adapter.hasDocKey(k);
            }

        } catch (IOException ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;

    }

    /**
     * Put a single MatchingResult into HBase ...
     *
     * ... this means, we write individual claim-part-pairs into a columnfamily
     * and the JSON representation into a second one.
     *
     * Such a MatchingResult is typcally not ranked by a human.
     *
     * @param m
     */
    public void putMatchResults(MatchingResult r) throws IOException {

        try {

            String key = EMDMEntityKeyFactory.getKeyForClaim(r.claim, r.lang, EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD);

            Match m1 = null;

            int i = 0;
            for (Match m : r.matches) {

                System.out.println("[" + m.lang + "]" + " : " + m.claim + " * (" + m.score + ")[" + m.userRating + "] => " + m.part);

                putMatchResult(m, i);

                if (i == 0) {
                    m1 = m;
                }
                i++;

            }
            if (m1 != null) {
                putMatchingResultNrOfMatches(m1, r.matches.size());
                putMatchingResultAsJSON(key, MatchResultEncoder.getJSONObjectForClaimPartsMatches(r));
            } 
            else {
                EMDMStore.countEmptyResultSet();
            }

        } 
        catch (Exception ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Handle an individual claim-part item, called Match, and write it into the
     * expanded details column.
     *
     * @param m
     */
    public void putMatchResult(Match m, int i) {

        /**
         * Here we calculate a unique key for a claim per language.
         */
        String key = EMDMEntityKeyFactory.getKeyForClaim(m.claim, m.lang, EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD);
        String lang = m.lang;

        // expanded properties data store - CF 2
        putMatchResultsStructured(key, m, i);

    }

    /**
     * Put this "document" into the store.
     *
     * @param sk
     * @param sv
     */
//    @WebMethod
//    public void put(String sk, String sv, String lang) {
//        try {
//
//            byte[] k = sk.getBytes();
//            byte[] v = sv.getBytes();
//
//            if (k != null && v != null) {
//                
//                adapter.putDoc(k, v);
//                adapter.putDocLang(k, lang);
//                
//            }
//            
//        } 
//        catch (IOException ex) {
//            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//        catch (Exception ex) {
//            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    /**
     * In order to search / scan / query the individual fields of an item we
     * must store those in individual cells, not as an embedded JSON object.
     *
     * @param sk
     * @param pString
     * @return
     */
    @WebMethod
    public boolean putDocStructured(String sk, Properties p) {

        boolean back = true;

        try {
            if (sk != null && p != null) {

                System.out.println("\n\n* putDocStructured() ... started ...");
                //adapter.putDocStructured(sk, pString);
                adapter.putDocStructured(sk, p);

            }
        } catch (IOException ex) {
            back = false;
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            back = false;
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("* putDocStructured() ... done! (key:" + sk + ")");

        return back;

    }

    public MatchingResult getMatchesForAClaimInLanguage(String claim, String lang) throws IOException {

//        int KEYMODE = EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD;
        String key = EMDMEntityKeyFactory.getKeyForClaim(claim, lang, 1);

        boolean hasKey = adapter.hasDocKey(key.getBytes());

        if (hasKey) {

            System.out.println(">>> " + lang + "_" + claim + "  { hasKey(" + key + ") : [" + hasKey + "] }");

            MatchingResult mr = new MatchingResult();
            mr.claim = claim;
            mr.lang = lang;
            mr.matches = this.getMatchResultForKey(key);

            return mr;
        } else {

            System.out.println("!!! NO KEY >>> " + claim + "_" + lang + "  { hasKey(" + key + ") : [" + hasKey + "] }");

            return null;

        }

    }

//    public Vector<Match> getMatchesForLanguage(String lang, int z) {
//
//        Vector<Match> vn = new Vector<Match>();
//        int i = 0;
//        for( Match m : v ) {
//            if ( m.lang.equals(lang) ) {
//                vn.add(m);
//                i++;
//            }
//            if ( i > z ) return vn;
//        }
//        
//        return vn;
//        
////        System.out.println( ">>> " + lang );
////        
////        return null;
//    
//    }
    public Vector<MatchingResult> getMatchResults(int z) {
        System.out.println(">>> z=" + z);

        return null;
    }

    /**
     *
     *
     * @param z
     * @return
     */
    public Vector<MatchingResult> getMatchResultsUnranked(int z) {
        System.out.println(">>> z=" + z);

        return null;
    }

    public Vector<MatchingResult> _getMatchResultsWithLimit(int z) {

        System.out.println(">>> limit :  " + z);

        Vector<MatchingResult> mr = adapter.getFullMatchesWithLimit(z);

        return mr;
    }

    public Vector<MatchingResult> getMatchResultsForLanguage(String LANG, int z) {

        System.out.println(">>> lang=" + LANG + " : z=" + z);

        Vector<MatchingResult> mr = adapter._getFullMatchesForLang(LANG, z);

        return mr;
    }

    public Vector<MatchingResult> getMatchResultsForLanguageUnranked(String LANG, int z) {
        System.out.println(">>> " + LANG);

        return null;
    }

    @WebMethod
    public String getDocStructured(String sk) {

        if (adapter == null) {
            return "NO ADAPTER !!!";
        }

        String prop = null;
        Properties props = null;

        try {

            if (sk != null) {
                byte[] k = sk.getBytes();
                props = adapter.getDocStructured(k);
                prop = ObjectEncoder.toString(props);

            } else {
                sk = "1/6/2015 13:24:53_mirko.kaempf@cloudera.com_14";
                byte[] k = sk.getBytes();
                props = adapter.getDocStructured(k);
                prop = ObjectEncoder.toString(props);
            }
        } catch (IOException ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return prop;
    }

    private void putMatchResultsStructured(String key, Match m, int i) {

        putDocStructured(key, m.getPropertiesRepresentation(i));

    }

    /**
     * The number of matches is extracted from the data structure and stored in
     * a separate column.
     *
     * @param m
     * @param z
     * @throws IOException
     */
    private void putMatchingResultNrOfMatches(Match m, int z) throws IOException {

        String key = EMDMEntityKeyFactory.getKeyForClaim(m.claim, m.lang, EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD);
        String value = "" + z;

        // simple raw data store - CF 1
        adapter.putDocNrOfMatches(key.getBytes(), value);

    }

    private void putMatchingResultAsJSON(String key, String json) throws IOException {

        adapter.putClaimPartsMatchAsJSON(key.getBytes(), json);

    }

    /**
     * For each evaluated match we can store the user vote in HBase in the
     * expanded properties CF.
     *
     * @param rankedByUserD
     */
    void putRankedMatchResults(Vector<Match> rms) {

        for (Match rm : rms) {
            String key = EMDMEntityKeyFactory.getKeyForClaim(rm.claim, rm.lang, EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD);
            putMatchResultsStructured(key, rm, rm.indexInMatchResult);
        }

    }

    private Vector<Match> getMatchResultForKey(String key) throws IOException {

        int zM = adapter._getNrOfMatches(key.getBytes());

        System.out.println(">>> load props ... " + zM);

        if (adapter == null) {
            return null;
        }

        String prop = null;
        Properties props = null;

        try {

            if (key != null) {
                byte[] k = key.getBytes();
                props = adapter.getDocStructured(k);
                prop = ObjectEncoder.toString(props);
            }

        } catch (IOException ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EMDMStore.class.getName()).log(Level.SEVERE, null, ex);
        }

        Vector<Match> matches = new Vector<Match>();

        Enumeration enu = props.propertyNames();

        while (enu.hasMoreElements()) {

            System.out.println(enu.nextElement());

        }

        for (int i = 0; i < zM; i++) {
            Match m = new Match();
            m.part = (String) props.getProperty(i + "_p");
            m.score = Double.parseDouble(props.getProperty(i + "_s"));
            m.userRating = Double.parseDouble(props.getProperty(i + "_r"));
            matches.add(m);
        }

        System.out.println("# " + matches.size());
        return matches;

    }

    /**
     * For the connected table, we collect all
     *
     * @return
     */
    StatisticalSummary stats = null;
    public StatisticalSummary getHistogramForAllMatchesInAllRows() {
        stats = getHistogramForAllMatchesInAllRowsForCertainColumn("prop", "[0-9]+(_s)");
        return stats;
    }

    /**
     * Here we filter the CF with name "prop" and a RegexFilterPattern on the
     * columns in this CF.
     *
     * @param cf
     * @param pattern
     * @return
     */
    public StatisticalSummary getHistogramForAllMatchesInAllRowsForCertainColumn(String cf, String pattern) {

        System.out.println(">>> filter pattern: " + pattern);

        Filter f = new QualifierFilter(CompareOp.EQUAL, new RegexStringComparator(pattern));

        // Filter f = new ColumnPrefixFilter( Bytes.toBytes( pattern ) );
        return adapter.getHistogramForAllMatchesInAllRows(cf, f);

    }
    
    public Vector<TopicGraphLink> getTopicGraphLinks() {
        return adapter.getTopicGraphLinks();
    }

}
