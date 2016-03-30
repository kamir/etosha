/**
 *
 * High level adapter for storing and retrieving arbitrary document in HBase.
 *
 * Versions allow us to derive time series data from each document.
 *
 */
package data.io.adapter;

import admin.DocTabAdmin;
import admin.TSTabAdmin;
import org.apache.hadoopts.data.io.adapter.HBaseAdapter;
import hadoop.cache.doc.HBaseDocWorld;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseDocWorldAdapter {

    public static Properties getStructuredDocument(Result r) {

        Properties props = new Properties();
        
        NavigableMap<byte[], byte[]> cells = r.getFamilyMap( Bytes.toBytes( DocTabAdmin.colFamNameR) );

        if ( cells != null ) {
        
        while( cells.size() > 0 ) {
        
            Map.Entry<byte[], byte[]> entry = cells.pollFirstEntry();
         
            String k = Bytes.toString( entry.getKey() );
            String v = Bytes.toString( entry.getValue() );

            props.put( k, v);
        }

        }

//        String isValid = props.getProperty("isValid");
//                            String isAnswered = props.getProperty("isAnswered");
//                            String isPublic = props.getProperty("isPublic");
//                            String isPublished = props.getProperty("isPublished");
                            
        if ( props.getProperty("isValid" ) == null )
            props.put( "isValid", "0" );

        if ( props.getProperty("isPublic" ) == null )
            props.put( "isPublic", "0" );
        
        if ( props.getProperty("isPublished" ) == null )
            props.put( "isPublished", "0" );

        if ( props.getProperty("answer" ) == null )
            props.put( "answer", "no answer" );

        if ( props.getProperty("illu" ) == null )
            props.put( "illu", "no illu" );
        
        if ( props.getProperty("question" ) == null )
            props.put( "question", "no question" );
 

//        
        return props;        
    }

 
 
    private HBaseDocWorldAdapter() {
        // You need a configuration object to tell the client where to connect to.
        // When you create an HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as those 
        // files can be found on the CLASSPATH
        config = HBaseConfiguration.create();

        config.set("hbase.zookeeper.quorum", defaultZookeeperIP);  // Here we are running zookeeper locally
        config.set("hbase.zookeeper.property.clientPort", "2181");  // Here we are running zookeeper locally

    }
    
    static Configuration config = null;
    static String defaultZookeeperIP = "training01.sjc.cloudera.com,training03.sjc.cloudera.com,training06.sjc.cloudera.com";

    static HBaseDocWorldAdapter hba = null;
    
    static String tabName = DocTabAdmin.docTabName;
    static HTable table = null;

    /**
     * Connect to a Zookeeper, who knows the location of an HBase Master.
     *
     * @param zk
     */
    public static HBaseDocWorldAdapter init() {
        
        if ( config == null ) {
            config = HBaseConfiguration.create();

            config.set("hbase.zookeeper.quorum", defaultZookeeperIP);  // Here we are running zookeeper locally
            config.set("hbase.zookeeper.property.clientPort", "2181");  // Here we are running zookeeper locally
        }
        
        if (hba == null) {
            hba = new HBaseDocWorldAdapter();
            
            // initTable( config , tabName );
        try {
            // This instantiates an HTable object that connects you to
            // the "myLittleHBaseTable" table.
            System.out.println( ">>> Init HBaseAdapter ... " + hba);
            hba.table = new HTable(config, tabName);

            String k = "Ask";
            String v = "A question!";

            System.out.println( table );
            
            putDoc(k.getBytes(), v.getBytes());

            String r = new String(getDoc(k.getBytes()));

            System.out.println(k + " " + r);

        } 
        catch (Exception ex) {
            Logger.getLogger(HBaseDocWorldAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        }
        return hba;
    }

    /**
     *
     * the object can be a everything.
     *
     * @param data
     * @param pageID
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
        
        p.add(Bytes.toBytes( DocTabAdmin.colFamNameR), Bytes.toBytes("raw"), v);

        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }

    /**
     *
     * the object can be a everything.
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
        for( String keyOfProp : props.stringPropertyNames() ) {
            String v = props.getProperty(keyOfProp);
            
            p.add(Bytes.toBytes( DocTabAdmin.colFamNameR), 
                                 Bytes.toBytes( keyOfProp ), 
                                 Bytes.toBytes( v ) );

        }
        
        // Once you've adorned your Put instance with all the updates you want to
        // make, to commit it do the following (The HTable#put method takes the
        // Put instance you've been building and pushes the changes you made into
        // hbase)
        hba.table.put(p);

    }
   
    public static void putDocStructured( String k, String pString) throws IOException, Exception {
        
        Properties props = (Properties)ObjectEncoder.fromString(pString);
        
        putDoc( k.getBytes(), props );
    
        System.out.println( "Put done ... " );
        props.list( System.out );
        
    };
    

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
    public static byte[] getDoc(byte[] key) throws IOException, Exception {

        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);
        Result r = hba.table.get(g);
        
        byte[] value = r.getValue(Bytes.toBytes( DocTabAdmin.colFamNameR), Bytes.toBytes("raw"));

        if ( value == null) value = "NULL".getBytes();
        return value;
    }
    
    public static Properties getDocStructured(byte[] key) throws IOException, Exception {

        Properties props = new Properties();
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(key);
        
        System.out.println( Bytes.toString(key) );
        
        Result r = hba.table.get(g);
        
        props = getStructuredDocument( r );
        
        return props;
    }
 
    public boolean hasDocKey(byte[] k) throws IOException {
 
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(k);
        g.setMaxVersions(1);
        g.setFilter( new KeyOnlyFilter() );
        
        boolean ret = false;
        
        Result r = hba.table.get(g);
        
        byte[] fam = Bytes.toBytes( DocTabAdmin.colFamNameR );
        byte[] col = Bytes.toBytes("id");
        
        
        ret = r.containsColumn( fam , col );
        
        return ret;    
    }

    public void putAll(List<Put> p) {
        try {
        
            hba.table.put(p);
        
        } 
        catch (Exception ex) {
            Logger.getLogger(HBaseDocWorldAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }    
    
    
    
    
    public void put(byte[] k, byte[] fam, byte[] col, byte[] value) {

        try {
            
            Put p = new Put(k);
            
            p.add( fam, col, value );
            
            hba.table.put(p);
        
        } 
        catch (Exception ex) {
            Logger.getLogger(HBaseDocWorldAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    
    }

    public String increment(String resourceID, String metric) {

        long cnt1 = 0;
        
        try {
            
            
            cnt1 = hba.table.incrementColumnValue(
                    Bytes.toBytes(resourceID), // co IncrementSingleExample-1-Incr1 Increase counter by one.
                    Bytes.toBytes(DocTabAdmin.colFamNameR),
                    Bytes.toBytes(metric), 1);
            
            
            
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseDocWorldAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resourceID + " => (" + metric + ") v=:" + cnt1 ;
    
    
    }
}
