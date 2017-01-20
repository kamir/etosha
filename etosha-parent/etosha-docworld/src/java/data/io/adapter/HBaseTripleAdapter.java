/**
 *
 * High level adapter for storing and retrieving triples in HBase.
 *
 * Versions allow us to track time evolution of the data.
 *
 */
package data.io.adapter;

import admin.DocTabAdmin;
import admin.TSTabAdmin;
import admin.TripleTabAdmin; 
import java.io.*;
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
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseTripleAdapter {

 
 
    private HBaseTripleAdapter() {
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

    static HBaseTripleAdapter hba = null;
    
    static String tabName = DocTabAdmin.docTabName;
    static HTable table = null;

    /**
     * Connect to a Zookeeper, who knows the location of an HBase Master.
     *
     * @param zk
     */
    public static HBaseTripleAdapter init() {
        if (hba == null) {
            hba = new HBaseTripleAdapter();
            
            // initTable( config , tabName );
        try {
            // This instantiates an HTable object that connects you to
            // the "myLittleHBaseTable" table.
            hba.table = new HTable(config, tabName);

            String s1 = "Mirko";
            String p1 = "hasAskedTheQuestion";
            String o1 = "What time is it?";

            String s2 = "Ben";
            String p2 = "hasAskedTheQuestion";
            String o2 = "http://theQuestionIsAvailableHere.html";

            System.out.println( table );
            
            hba.putTRIPLE( s1.getBytes(), p1.getBytes(), o1.getBytes() );
            hba.putTRIPLE( s2.getBytes(), p2.getBytes(), o2.getBytes() );

            String r1 = new String( "T1: " + hba.hasTripleForKey( s1.getBytes() ));
            String r2 = new String( "T2: " + hba.hasTripleForKey( s2.getBytes() ));

        } 
        catch (Exception ex) {
            Logger.getLogger(HBaseTripleAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        }
        return hba;
    }

    
    /**
     * S = subject: = KEY
     * P = object: COLFAM: TripleTabAdmin.colFamNameS COL: p
     * O = object: COLFAM: TripleTabAdmin.colFamNameS COL: o
     * 
     * the predicate and object can be everything. Literals are also indexed.
     */
    public void putTRIPLE(byte[] s, byte[] p, byte[] o) throws IOException, Exception {
        
        String COLFAM = TripleTabAdmin.colFamNameS;
        
        Put pc = new Put( s );
        
        pc.add(Bytes.toBytes( COLFAM ), Bytes.toBytes( "p" ), p);
        pc.add(Bytes.toBytes( COLFAM ), Bytes.toBytes( "o" ), o);

        hba.table.put(pc);
    }
 
    public boolean hasTripleForKey(byte[] k) throws IOException {
 
        // Now, to retrieve the data we just wrote. The values that come back are
        // Result instances. Generally, a Result is an object that will package up
        // the hbase return into the form you find most palatable.
        Get g = new Get(k);
        g.setMaxVersions(1);
//        g.setFilter( new KeyOnlyFilter() );
        
        boolean ret = false;
        
        Result r = hba.table.get(g);
        
        String COLFAM = TripleTabAdmin.colFamNameS;
        
        byte[] fam = Bytes.toBytes( COLFAM );
        byte[] colP = Bytes.toBytes("p");
        byte[] colO = Bytes.toBytes("o");
        
        ret = r.containsColumn( fam , colP ) && r.containsColumn( fam , colO );
        
        return ret;    
    }
}
