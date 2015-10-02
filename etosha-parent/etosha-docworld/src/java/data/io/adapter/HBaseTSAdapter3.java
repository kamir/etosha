/**
 *
 * High level adapter for storing and retrieving Time Series from HBase.
 *
 */
package data.io.adapter;

import admin.TSTabAdmin;
import data.io.adapter.HBaseAdapter;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseTSAdapter3 {

 
    

    private HBaseTSAdapter3() {
        // You need a configuration object to tell the client where to connect.
        // When you create a HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as these can
        // be found on the CLASSPATH
        config = HBaseConfiguration.create();

        config.set("hbase.zookeeper.quorum", defaultZookeeperIP);  // Here we are running zookeeper locally
        config.set("hbase.zookeeper.property.clientPort", "22181");  // Here we are running zookeeper locally





    }
    
    static Configuration config = null;
    static String defaultZookeeperIP = "192.168.3.171";
    static HBaseTSAdapter3 hba = null;
    static String tabName = TSTabAdmin.tsTabName;
    static HTable table = null;

    /**
     * Connect to a Zookeeper, who knows the location of an HBase Master.
     *
     * @param zk
     */
    public static HBaseTSAdapter3 init() {
        if (hba == null) {
            hba = new HBaseTSAdapter3();
            
            // initTable( config , tabName );
        try {
            // This instantiates an HTable object that connects you to
            // the "myLittleHBaseTable" table.
            hba.table = new HTable(config, tabName);

            String k = "Hi";
            String v = "Mirko!";

            System.out.println( table );
            
            putEditTS(k.getBytes(), v.getBytes());

            String r = new String(getEditTS(k.getBytes()));

            System.out.println(k + " " + r);

        } 
        catch (Exception ex) {
            Logger.getLogger(HBaseTSAdapter3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        }
        return hba;
    }

    /**
     *
     * the object can be a Messreihe.
     *
     * @param data
     * @param pageID
     */
    public static void putEditTS(byte[] k, byte[] v) throws IOException, Exception {

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
        p.add(Bytes.toBytes( TSTabAdmin.colFamNameE), Bytes.toBytes("raw"), v);

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

//    /** 
//     * 
//     * the object can be a Messreihe.
//     * 
//     * @param data
//     * @param pageID 
//     */
//    public static void putEditTS( Object data, String pageID ) {
//    
//    }
    public static byte[] getEditTS(byte[] key) throws IOException, Exception {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);
        Result r = hba.table.get(g);
        byte[] value = r.getValue(Bytes.toBytes( TSTabAdmin.colFamNameE), Bytes.toBytes("raw"));

        if ( value == null) value = "NULL".getBytes();
        return value;
    }
    
    
    //    /** 
//     * 
//     * the object can be a Messreihe.
//     * 
//     * @param data
//     * @param pageID 
//     */
//    public static void putEditTS( Object data, String pageID ) {
//    
//    }
    public static byte[] getEditTSKeyOnly(byte[] key) throws IOException, Exception {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);
        
        KeyOnlyFilter filter = new KeyOnlyFilter();
        g.setFilter(filter);
        
        Result r = hba.table.get(g);
        // byte[] value = r.getValue(Bytes.toBytes( TSTabAdmin.colFamNameE), Bytes.toBytes("raw"));
        int size = r.size();

        return Bytes.toBytes(size);
    }
    
    
    
//    public static Object getEditsTS( String pageID ) {
//        Object o = null;
//        return o;
//    }

    public boolean hasEditTSKey(byte[] k) throws IOException {
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(k);
        g.setMaxVersions(1);
        g.setFilter( new KeyOnlyFilter() );
        
        boolean ret = false;
        
        Result r = hba.table.get(g);
        
        byte[] value = r.getValue(Bytes.toBytes( TSTabAdmin.colFamNameE ), Bytes.toBytes("raw"));

        if ( value == null) value = "NULL".getBytes();
        return ret;
    }
}
