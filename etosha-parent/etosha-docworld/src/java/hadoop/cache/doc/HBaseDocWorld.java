/*
 * HBaseDocWorld is a simple content handler for HBase.
 *
 * You can give it a document and HBase will stage it. No matter what
 * kind of metadata you care about. HBase allows you to scale out your
 * Application across multiple machines. HBaseDocWorld will handle your
 * Documents.
 *
 * http://de.wikipedia.org/wiki/Content_Repository_for_Java_Technology_API
 *
 *
 * Maybe we will be an implementation of the "JSR 170", known as Jackrabbit.
 *
 */
package hadoop.cache.doc;

import admin.DocTabAdmin;
import data.io.adapter.HBaseDocWorldAdapter;
import data.io.adapter.ObjectEncoder;
import hadoop.cache.ts.HBaseTimeSeriesCache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
@WebService(serviceName = "HBaseDocWorld.v1")
public class HBaseDocWorld {

    static HBaseDocWorldAdapter adapter = null;

    @WebMethod
    public String[] getScannResults(int limit) {

        String[] res = new String[limit];

        try {

            Configuration config = HBaseConfiguration.create();

            Scan s = new Scan();
            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Bytes.toBytes(DocTabAdmin.colFamNameR),
                    Bytes.toBytes("valid"),
                    CompareFilter.CompareOp.EQUAL, Bytes.toBytes("0"));
            // gives us all with the value and without that field

            s.addFamily(DocTabAdmin.colFamNameR.getBytes());
            s.setFilter(filter);

            String zkHostString = "training01.sjc.cloudera.com,training03.sjc.cloudera.com,training06.sjc.cloudera.com";
            config.set("hbase.zookeeper.quorum", zkHostString);
            config.set("hbase.zookeeper.property.clientPort", "2181");

            String tabName = DocTabAdmin.docTabName;

            // This instantiates an HTable object that connects you to
            // the "tabName" table.
            HTable table = new HTable(config, tabName);

            int LIMIT = limit;

            ResultScanner scanner = table.getScanner(s);

            try {
                int c = 0;
                // Scanners return Result instances.
                // Now, for the actual iteration. One way is to use a while loop like so:
                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

                    // print out the row we found and the columns we were looking for
                    System.out.println("Found row: " + rr);
                    if (c == LIMIT) {
                        break;
                    }

                    Properties pr = adapter.getStructuredDocument(rr);
                    String docObject = ObjectEncoder.toString(pr);
                    res[c] = docObject;

                    c++;

                }
            } finally {
                // Make sure you close your scanners when you are done!
                // Thats why we have it inside a try/finally clause
                scanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseDocWorld.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
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

            System.out.println("Lookup Key: " + sk);

            byte[] k = sk.getBytes();
            System.out.println("*** " + adapter);
            if (k != null) {
                ret = adapter.hasDocKey(k);
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     * Put this document into the store.
     *
     * @param sk
     * @param sv
     */
    @WebMethod
    public void put(String sk, String sv) {
        try {

            byte[] k = sk.getBytes();
            byte[] v = sv.getBytes();

            if (k != null && v != null) {
                adapter.putDoc(k, v);
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Put this property for a document into the store.
     *
     * @param key
     * @param fam
     * @param col
     * @param value
     */
    @WebMethod
    public void putExtended(String key, String fam, String col, String value) {

        adapter.put(key.getBytes(), fam.getBytes(), col.getBytes(), value.getBytes());

    }

    @WebMethod
    public boolean putDocStructured(String sk, String pString) {
        boolean back = true;
        try {
            if (sk != null && pString != null) {
                System.out.println("* putDocStructured() ... started ...");
                adapter.putDocStructured(sk, pString);
            }
        } catch (IOException ex) {
            back = false;
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            back = false;
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println( count(sk, "CH.DocSTRUCT") );

        System.out.println("* putDocStructured() ... done!");

        return back;
    }

    @WebMethod
    public String getDocStructured(String sk) {

        System.out.println("Found ADAPTER : " + adapter);

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
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println( count(sk, "REQ.DocSTRUCT") );

        return prop;
    }

    @WebMethod
    public int init() {
        adapter = HBaseDocWorldAdapter.init();
        return 42 + 1;
    }

    /**
     * Tell me when the document was used. I will remember and create an access
     * time series.
     */
    @WebMethod
    private void logUsage() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Give my the usage-history of this document
     */
    @WebMethod
    private Object getAccessHistory() {
        return null; //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Give my the editorial-history of this document
     */
    @WebMethod
    private Object getEditsgHistory() {
        return null; // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * We scan all items and reset individually ...
     *
     * @param string
     */
    @WebMethod
    public String resetItemsToState(String status) {

        int c = 0;
        try {

            List<Put> puts = new ArrayList<Put>();

            Configuration config = HBaseConfiguration.create();

            Scan s = new Scan();
            KeyOnlyFilter kof = new KeyOnlyFilter();

//            SingleColumnValueFilter filter = new SingleColumnValueFilter(
//                    Bytes.toBytes(DocTabAdmin.colFamNameR),
//                    Bytes.toBytes("valid"),
//                    CompareFilter.CompareOp.EQUAL, Bytes.toBytes("0"));
            // gives us all with the value and without that field
            s.addFamily(DocTabAdmin.colFamNameR.getBytes());
            s.setFilter(kof);

            String zkHostString = "training01.sjc.cloudera.com,training03.sjc.cloudera.com,training06.sjc.cloudera.com";
            config.set("hbase.zookeeper.quorum", zkHostString);
            config.set("hbase.zookeeper.property.clientPort", "2181");

            String tabName = DocTabAdmin.docTabName;

            HTable table = new HTable(config, tabName);

            ResultScanner scanner = table.getScanner(s);

            try {
                // Scanners return Result instances.
                // Now, for the actual iteration. One way is to use a while loop like so:
                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

                    // print out the row we found and the columns we were looking for
                    System.out.println("Found row: " + rr);

                    byte[] fam = Bytes.toBytes(DocTabAdmin.colFamNameR);
                    byte[] col = Bytes.toBytes("id");

                    String key = Bytes.toString(rr.getRow());

                    Put p = new Put(Bytes.toBytes(key));

                    p.add(DocTabAdmin.colFamNameR.getBytes(), "valid".getBytes(), status.getBytes());
                    //
                    // Here we change the value ... 

                    puts.add(p);

                    c++;

                }

                adapter.putAll(puts);

            } finally {
                // Make sure you close your scanners when you are done!
                // Thats why we have it inside a try/finally clause
                scanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HBaseDocWorld.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c + "";

    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "count")
    public String count(@WebParam(name = "resourceID") String resourceID, @WebParam(name = "metric") String metric) {

        return adapter.increment( resourceID, metric );

    }

}
