/**
 *
 * High level adapter for storing and retrieving arbitrary document in HBase.
 *
 * Versions allow us to derive time series data from each document.
 *
 */
package com.cloudera.dal;

import com.cloudera.utils.ObjectEncoder;
import com.cloudera.dal.admin.EMDMBaseTabAdmin;
import com.cloudera.dal.emdm.Match;
import com.cloudera.dal.emdm.MatchResultEncoder;
import com.cloudera.dal.emdm.MatchingResult;
import com.cloudera.utils.StatisticalSummary;
import com.cloudera.dal.graph.TopicGraphLink;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseEMDMStoreAdapter {

    public static String defaultZookeeperIP = "dl-mn04-d,dl-mn02-d,dl-mn03-d";

    private HBaseEMDMStoreAdapter() {
        // You need a configuration object to tell the client where to connect to.
        // When you create an HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as those 
        // files can be found on the CLASSPATH
        config = HBaseConfiguration.create();

        config.set("hbase.zookeeper.quorum", defaultZookeeperIP);  // Here we are running zookeeper locally
        config.set("hbase.zookeeper.property.clientPort", "2181");  // Here we are running zookeeper locally

    }

    public static Configuration config = null;

    public static HBaseEMDMStoreAdapter hba = null;
    static HTable table = null;

    /**
     * Connect to a Zookeeper, who knows the location of an HBase Master.
     *
     * @param zk
     */
    
    public static HBaseEMDMStoreAdapter init() {

        if (config == null) {

            config = HBaseConfiguration.create();

            config.set("hbase.zookeeper.quorum", defaultZookeeperIP);  // Here we are running zookeeper locally
            config.set("hbase.zookeeper.property.clientPort", "2181");  // Here we are running zookeeper locally

        }

        if (hba == null) {

            hba = new HBaseEMDMStoreAdapter();

            // initTable( config , tabName );
            try {
                // This instantiates an HTable object that connects you to
                // the "myLittleHBaseTable" table.
                System.out.println(">>> Init HBaseAdapter : " + hba);
                System.out.println(">>> Point to table    : " + EMDMBaseTabAdmin.docTabName);
                System.out.flush();

                hba.table = new HTable(config, EMDMBaseTabAdmin.docTabName);

//                String k = "FUNCTIONAL.TEST";
//                String v = new Date(System.currentTimeMillis()).toString();
//
//                System.out.println(table);
//
//                putDoc(k.getBytes(), v.getBytes());
//
//                String r = new String(getDoc(k.getBytes()));
//
//                System.out.println(k + " >>> " + r);
            } catch (Exception ex) {
                Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
                hba = null;
            }

        } else {
            System.out.println(">>> HBaseAdapter was ready! " + hba);

        }
        return hba;
    }

    /**
     * We write the "JSON-serialized object" into the "raw-column" in
 columnfamily with name EMDMBaseTabAdmin.colFamName1=me.
     *
     */
    public static void putDoc(byte[] k, byte[] v) throws IOException, Exception {

        // To add to a row, use Put.  A Put constructor takes the name of the row
        // you want to insert into as a byte array.  In HBase, the Bytes class has
        // utility for converting all kinds of java types to byte arrays.  In the
        // below, we are converting the String "pageID" into a byte array to
        // use as a row key for our update. Once you have a Put instance, you can
        // adorn it by setting the names of columns you want to update on the row,
        // the timestamp to use in your update, etc.If no timestamp, the server
        // applies current time to the edits.
//        System.out.println(">> k     " + new String( k ) );
//        System.out.println(">> v     " + new String( v ) );
//        System.out.println(">> hba   " + hba == null);
//        System.out.println(">> table " + hba.table == null);
        Put p = new Put(k);

        // To set the value you'd like to update in the row "pageID", specify
        // the column family, column qualifier, and value of the table cell you'd
        // like to update.  The column family must already exist in your table
        // schema.  The qualifier can be anything.  All must be specified as byte
        // arrays as hbase is all about byte arrays.  Lets pretend the table
        // 'wikinodes' was created with a family 'accessts'.
        p.add(Bytes.toBytes(EMDMBaseTabAdmin.colFamName1), Bytes.toBytes("raw"), v);

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

    /**
     * For a particular claim_lang keyed item, we get the full JSON-serialized
     * MatchResult-object.
     *
     * @param key
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static byte[] getDoc(byte[] key) throws IOException, Exception {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);
        Result r = hba.table.get(g);

        byte[] value = r.getValue(Bytes.toBytes(EMDMBaseTabAdmin.colFamName1), Bytes.toBytes("raw"));

        if (value == null) {
            value = "NULL".getBytes();
        }

        return value;
    }

    /**
     * We write the MatchResult in an expanded form for faster access to fields
     * in to the second column family.
     *
     * @param data
     * @param pageID
     */
    public static void putDoc(byte[] k, Properties props) throws IOException, Exception {

        // To add to a row, use Put.  A Put constructor takes the name of the row
        // you want to insert into as a byte array.  In HBase, the Bytes class has
        // utility for converting all kinds of java types to byte arrays.  In the
        // below, we are converting the String "pageID" into a byte array to
        // use as a row key for our update. Once you have a Put instance, you can
        // adorn it by setting the names of columns you want to update on the row,
        // the timestamp to use in your update, etc.If no timestamp, the server
        // applies current time to the edits.
//        System.out.println(">> k     " + new String( k ) );
//        System.out.println(">> v     " + new String( v ) );
//        System.out.println(">> hba   " + hba == null);
//        System.out.println(">> table " + hba.table == null);
        Put p = new Put(k);

        // To set the value you'd like to update in the row "pageID", specify
        // the column family, column qualifier, and value of the table cell you'd
        // like to update.  The column family must already exist in your table
        // schema.  The qualifier can be anything.  All must be specified as byte
        // arrays as hbase is all about byte arrays.  Lets pretend the table
        // 'wikinodes' was created with a family 'accessts'.
        for (String keyOfProp : props.stringPropertyNames()) {

            String v = props.getProperty(keyOfProp);

            p.add(Bytes.toBytes(EMDMBaseTabAdmin.colFamName2),
                    Bytes.toBytes(keyOfProp),
                    Bytes.toBytes(v));

        }

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

    public static void putDocStructured(String k, String pString) throws IOException, Exception {

        Properties props = (Properties) ObjectEncoder.fromString(pString);

        putDoc(k.getBytes(), props);

        System.out.println("Structured String  => PUT done ... " + k);

    }

    public static void putDocStructured(String k, Properties props) throws IOException, Exception {

        putDoc(k.getBytes(), props);

        System.out.println("Structured Properties => PUT done ... " + k);

    }

    /**
     * The elements stored in HBase can be converted into a Property object
     * which is a Map. Elements are accessable via key.
     *
     * @param key
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static Properties getDocStructured(byte[] key) throws IOException, Exception {

        Properties props = new Properties();
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);

        System.out.println(Bytes.toString(key));

        Result r = hba.table.get(g);

        props = getStructuredDocument(r);

        return props;
    }

    public boolean hasDocKey(byte[] k) throws IOException {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(k);
        g.setMaxVersions(1);
        g.setFilter(new KeyOnlyFilter());

        boolean ret = false;

        Result r = hba.table.get(g);

        byte[] fam = Bytes.toBytes("me");
        byte[] col = Bytes.toBytes("zMatches");

        ret = r.containsColumn(fam, col);

        return ret;
    }

    public void putAll(List<Put> p) {
        try {

            hba.table.put(p);

        } catch (Exception ex) {
            Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void put(byte[] k, byte[] fam, byte[] col, byte[] value) {

        try {

            Put p = new Put(k);

            p.add(fam, col, value);

            hba.table.put(p);

        } catch (Exception ex) {
            Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * We can increment a value directly in an atomar operation.
     *
     * @param resourceID
     * @param metric
     * @return
     */
    public String increment(String resourceID, String metric, int value) {

        long cnt1 = 0;

        try {

            cnt1 = hba.table.incrementColumnValue(Bytes.toBytes(resourceID), // co IncrementSingleExample-1-Incr1 Increase counter by one.
Bytes.toBytes(EMDMBaseTabAdmin.colFamName1),
                    Bytes.toBytes(metric), value);

        } catch (IOException ex) {
            Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resourceID + " => (" + metric + ") v=:" + cnt1;

    }

    /**
     * We can decompose a Properties object into individual HBase columns ...
     *
     * @param r
     * @return
     */
    public static Properties getStructuredDocument(Result r) {

        Properties props = new Properties();

        NavigableMap<byte[], byte[]> cells = r.getFamilyMap(Bytes.toBytes(EMDMBaseTabAdmin.colFamName2));

        if (cells != null) {

            while (cells.size() > 0) {

                Map.Entry<byte[], byte[]> entry = cells.pollFirstEntry();

                String k = Bytes.toString(entry.getKey());
                String v = Bytes.toString(entry.getValue());

                props.put(k, v);
            }

        }

        if (props.getProperty("isValid") == null) {
            props.put("isValid", "0");
        }

        return props;
    }

    String status = "normal";

    public String status() {
        return status;
    }

    /**
     * REFERENCE CODE für SCANN Funktionen ...
     *
     * @param LANG
     * @param limit
     * @return
     */
    @WebMethod
    public Vector<MatchingResult> _getFullMatchesForLang(String LANG, int limit) {

        Vector<MatchingResult> res = new Vector<MatchingResult>();

        try {

            Scan s = new Scan();

            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Bytes.toBytes(EMDMBaseTabAdmin.colFamName1),
                    Bytes.toBytes("lang"),
                    CompareFilter.CompareOp.EQUAL, Bytes.toBytes(LANG));

            s.addFamily(EMDMBaseTabAdmin.colFamName1.getBytes());
            s.setFilter(filter);

            int LIMIT = limit;

            ResultScanner scanner = hba.table.getScanner(s);

            try {
                int c = 0;

                // Scanners return Result instances.
                // Now, for the actual iteration. One way is to use a while loop like so:
                MatchingResult mr = new MatchingResult();

                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

                    // loop over n-cells with embeded fields ...
                    int z = 0;
                    for (Cell cell : rr.listCells()) {

                        String row = new String(CellUtil.cloneRow(cell));
                        String family = new String(CellUtil.cloneFamily(cell));
                        String column = new String(CellUtil.cloneQualifier(cell));
                        String value = new String(CellUtil.cloneValue(cell));
                        long timestamp = cell.getTimestamp();

                        System.out.printf("%-20s column=%s:%s, timestamp=%s, value=%s\n", row, family, column, timestamp, value);

                        if (column.equals("zMatches")) {

                        }

                        if (column.equals("raw")) {
                            Match m = MatchResultEncoder.getMatchFromString(value);
                        }

                    }

                    if (c == LIMIT) {
                        break;
                    }

//                    Properties pr = adapter.getStructuredDocument(rr);
//                    String docObject = ObjectEncoder.toString(pr);
                    c++;

                }
            } finally {
                // Make sure you close your scanners when you are done!
                // Thats why we have it inside a try/finally clause
                scanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    /**
     * REFERENCE CODE für SCANN Funktionen ...
     *
     * @param LANG
     * @param limit
     * @return
     */
    @WebMethod
    public Vector<MatchingResult> getFullMatchesWithLimit(int limit) {

        Vector<MatchingResult> res = new Vector<MatchingResult>();

        try {

            Scan s = new Scan();

            Filter filter = new PageFilter(limit);
            s.addFamily(EMDMBaseTabAdmin.colFamName1.getBytes()); // just "me" CF
            s.setFilter(filter);

            int LIMIT = limit;

            ResultScanner scanner = hba.table.getScanner(s);

            try {
                int c = 0;

                // Scanners return Result instances.
                // Now, for the actual iteration. One way is to use a while loop like so:
                MatchingResult mr = new MatchingResult();

                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

                    // loop over n-cells with embeded fields ...
                    int z = 0;
                    for (Cell cell : rr.listCells()) {

                        String row = new String(CellUtil.cloneRow(cell));
                        String family = new String(CellUtil.cloneFamily(cell));
                        String column = new String(CellUtil.cloneQualifier(cell));
                        String value = new String(CellUtil.cloneValue(cell));
                        long timestamp = cell.getTimestamp();

                        System.out.printf("%-20s column=%s:%s, timestamp=%s, value=%s\n", row, family, column, timestamp, value);
//                    
//                        if ( column.equals( "zMatches" ) ) {
//                            
//                        }
//                        
//                        if ( column.equals( "raw" ) ) {
//                            
//                            Match m = MatchResultEncoder.getMatchFromString( value );
//                            
//                            mr.matches.add( m );
//                            mr.claim = m.claim;
//                            mr.lang = m.lang;
//                            
//                        }

                        if (column.equals("json")) {

                            mr = MatchResultEncoder.getMatchingResultFromString(value);

                        }

                    }

                    if (c == LIMIT) {
                        break;
                    }

//                    Properties pr = adapter.getStructuredDocument(rr);
//                    String docObject = ObjectEncoder.toString(pr);
                    res.add(mr);
                    c++;

                }
            } finally {
                // Make sure you close your scanners when you are done!
                // Thats why we have it inside a try/finally clause
                scanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseEMDMStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public void putDocLang(byte[] k, String lang) throws IOException {

        Put p = new Put(k);

        p.add(Bytes.toBytes(EMDMBaseTabAdmin.colFamName1), Bytes.toBytes("lang"), lang.getBytes());

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

    public void putDocNrOfMatches(byte[] k, String value) throws IOException {

        Put p = new Put(k);

        p.add(Bytes.toBytes(EMDMBaseTabAdmin.colFamName1), Bytes.toBytes("zMatches"), value.getBytes());

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);
    }

    public void putClaimPartsMatchAsJSON(byte[] k, String value) throws IOException {

        Put p = new Put(k);

        p.add(Bytes.toBytes(EMDMBaseTabAdmin.colFamName1), Bytes.toBytes("json"), value.getBytes());

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

    public void deleteDoc(byte[] k) throws IOException {

        Delete d = new Delete(k);

        hba.table.delete(d);

    }

    public int _getNrOfMatches(byte[] k) throws IOException {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(k);
        g.setMaxVersions(1);

        Result r = hba.table.get(g);

        byte[] fam = Bytes.toBytes("me");
        byte[] col = Bytes.toBytes("zMatches");

        int value = 0;

        try {

            String v = new String(r.getValue(fam, col));

//            System.out.println("*["+v+"]");
            value = Integer.parseInt(v);

        } catch (Exception ex) {

        }

//        System.out.println("#["+value+"]");
        return value;

    }

    /**
     * Pretty slow implementation !
     *
     * If we need this more often, we should use the API to increment a counter
     * on each table update (see:
     * http://stackoverflow.com/questions/11375098/hbase-quickly-count-number-of-rows)
     *
     */
    public static int getNrOfRows() {
        int z = 0;

        try {

            Scan s = new Scan();
            FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
            s.setFilter(filter);

            ResultScanner scanner = hba.table.getScanner(s);

            for (Result rs = scanner.next(); rs != null; rs = scanner.next()) {
                z++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return z;

    }

    /**
     * Pretty slow implementation !
     *
     * If we need this more often, we should use the API to increment a counter
     * on each table update (see:
     * http://stackoverflow.com/questions/11375098/hbase-quickly-count-number-of-rows)
     *
     */
    double min = 0.0;
    double max = 50;
    double numBins = 1000;

    public SummaryStatistics getHistogramForAllMatchesInAllRows() {

        String pattern = ".*(_s)";

        int z = 0;
        SummaryStatistics sampleStats = new SummaryStatistics();

        byte[] family = EMDMBaseTabAdmin.colFamName2.getBytes();
        byte[] prefix = Bytes.toBytes(pattern);

        System.out.println(">>> pattern: " + pattern);

        double val = 0.0d;

        try {

            Scan s = new Scan();
            s.addFamily(family);

            Filter f = new ColumnPrefixFilter(prefix);
            s.setFilter(f);
            s.setBatch(10); // set this if there could be many columns returned
            ResultScanner rs = hba.table.getScanner(s);

            for (Result r = rs.next(); r != null; r = rs.next()) {

                CellScanner sc = r.cellScanner();
                while (sc.advance()) {
                    Cell c = sc.current();

                    System.out.print("row: " + c.getRow());

                    Double value = Bytes.toDouble(c.getValue());

                    System.out.println(" => " + value);

                    // each kv represents a column
                    sampleStats.addValue(value);
                }
            }

            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sampleStats;
    }

    /**
     *
     * @param cf
     * @param f
     * @return
     */
    public StatisticalSummary getHistogramForAllMatchesInAllRows(String cf, Filter f) {

        int z = 0;
        
        StatisticalSummary summary = new StatisticalSummary();

        byte[] family = cf.getBytes();

        System.out.println(f.toString());

        double val = 0.0d;

        try {

            Scan s = new Scan();
            s.addFamily(family);

            s.setFilter(f);
            s.setBatch(10); // set this if there could be many columns returned
            ResultScanner rs = hba.table.getScanner(s);

            for (Result r = rs.next(); r != null; r = rs.next()) {

                CellScanner sc = r.cellScanner();
                while (sc.advance()) {

                    Cell c = sc.current();

                    String row = new String(CellUtil.cloneRow(c)) + "# " + new String(CellUtil.cloneQualifier(c));

                    System.out.print("row: " + row);

                    Double value = Double.parseDouble(Bytes.toString(CellUtil.cloneValue(c)));

                    System.out.println(" => " + value);

                    // each kv represents a column
                    summary.addValue(value);
                }
            }

            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return summary;
    }

    /**
     *
     * @param cf
     * @param f
     * @return
     */
    public Vector<TopicGraphLink> getTopicGraphLinks() {

        int z = 0;
    
        String cf = "prop";
        
        StatisticalSummary summary = new StatisticalSummary();

        byte[] family = cf.getBytes();

        Vector<TopicGraphLink> links = new Vector<TopicGraphLink>();
        
        try {

            Scan s = new Scan();

            s.addFamily(family);

            // s.setBatch(10); // set this if there could be many columns returned
            
            ResultScanner rs = hba.table.getScanner(s);

            for (Result r = rs.next(); r != null; r = rs.next()) {

                Hashtable<Integer, TopicGraphLink> linkSet = TopicGraphLink.getTopicGraphLinksFromHBaseRow( r );
                
                Enumeration<Integer> en = linkSet.keys();

                while( en.hasMoreElements() ) {
                    
                    TopicGraphLink item = linkSet.get( en.nextElement() );
                    
                    if( item != null )
                        links.add( item );
                    
                }

            }

            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return links;
    }

}



/**
 * REFERENCE CODE für SCANN Funktionen ...
 * 
 *
   
 
    public String[] getFullMatchesForLang(String LANG, int limit) {

        String[] res = new String[limit];

        try {

            Scan s = new Scan();

            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Bytes.toBytes(SoleraFBCTabAdmin.colFamName1),
                    Bytes.toBytes("lang"),
                    CompareFilter.CompareOp.EQUAL, Bytes.toBytes(LANG));

            s.addFamily(SoleraFBCTabAdmin.colFamName2.getBytes());
            s.setFilter(filter);

            int LIMIT = limit;

            ResultScanner scanner = hba.table.getScanner(s);

            try {
                int c = 0;

                // Scanners return Result instances.
                // Now, for the actual iteration. One way is to use a while loop like so:
                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

                    for (Cell cell : rr.listCells()) {
                        
                        String row = new String(CellUtil.cloneRow(cell));
                        String family = new String(CellUtil.cloneFamily(cell));
                        String column = new String(CellUtil.cloneQualifier(cell));
                        String value = new String(CellUtil.cloneValue(cell));
                        long timestamp = cell.getTimestamp();

                        System.out.printf("%-20s column=%s:%s, timestamp=%s, value=%s\n", row, family, column, timestamp, value);
                    }

                    if (c == LIMIT) {
                        break;
                    }

//                    Properties pr = adapter.getStructuredDocument(rr);
//                    String docObject = ObjectEncoder.toString(pr);
                    res[c] = rr.toString();

                    c++;

                }
            } finally {
                // Make sure you close your scanners when you are done!
                // Thats why we have it inside a try/finally clause
                scanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseMatchStoreAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
     
 *
 * 
 * 
 **/
