package admin;
 
import hadoop.cache.doc.HBaseDocWorld;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

/**
 * @author kamir
 * 
 * This tool is used to initialize see: 
 *     see: init() 
 * and clean up
 *     see: reset() 
 * time series cache tables in the HBase server. 
 * 
 * Tabel names:
 * 
 *     - Access-Rows
 *     - Edit-Rows
 * 
 * This is part of the raw document cache in HBase.
 * 
 */
public class SoleraFBCTabAdmin {

    public static final String baseURL = "http://training03.sjc.cloudera.com:9099/DocWorld/v1/editItem.jsp?id=";
    
    public static final String docTabName = "partsMatch"; 
    public static final String colFamNameR = "matcher_v1"; 
    
    public static String[] FIELDS = { "claim", "suggestions", "votes" };

    public static void main(String[] args) throws IOException {
        
        // You need a configuration object to tell the client where to connect.
        // When you create a HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as these can
        // be found on the CLASSPATH

        Configuration config = HBaseConfiguration.create();
        String server = "QSVM";
        
        if ( args != null && args.length > 0 ) {
            server = args[0];
        }
        else {
            server = javax.swing.JOptionPane.showInputDialog("SERVER-Code:", server);
        }
        
        /**
         * Where is the Zookeeper server?
         * 
         * QSVM cluster ...
         *
         **/
        // config.set("hbase.zookeeper.quorum", "192.168.3.171");  

        if ( server.equals("QSVM") ) {
            /**
             * QSVM ...   TODO
             */
            String zkHostString = "training01.sjc.cloudera.com,training03.sjc.cloudera.com,training06.sjc.cloudera.com";
            config.set( "hbase.zookeeper.quorum", zkHostString );  
            config.set( "hbase.zookeeper.property.clientPort", "2181");  
        }
        
        if ( server.equals("local") ) {
            /**
             * Local in VM ...
             */
            String zkHostString = "127.0.0.1";
            config.set( "hbase.zookeeper.quorum", zkHostString );  
            config.set( "hbase.zookeeper.property.clientPort", "2181");  
        }        
        
        String tabName = docTabName;

        /**
         * !!! Warning !!!
         * 
         * resetTable(  ) -> will delete the table and you loose all content!
         * 
         */
        //resetTable(  config , tabName  );  
        
        initTable( config , tabName );

        // This instantiates an HTable object that connects you to
        // the "tabName" table.
        HTable table = new HTable(config, tabName);
        
        int LIMIT = 10;

        // Sometimes, you won't know the row you're looking for. In this case, you
        // use a Scanner. This will give you cursor-like interface to the contents
        // of the table.  To set up a Scanner, do like you did above making a Put
        // and a Get, create a Scan.  Adorn it with column names, etc.
        System.out.println("\nSolera FBC SCAN");   
        System.out.println(  "***************");   
        System.out.println(  ">>> LIMIT = " + LIMIT );   
        Scan s = new Scan();
        
        s.addFamily(colFamNameR.getBytes());

        ResultScanner scanner = table.getScanner(s);
        try {
            int c = 0;
            // Scanners return Result instances.
            // Now, for the actual iteration. One way is to use a while loop like so:
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                // print out the row we found and the columns we were looking for
                System.out.println("Found row: " + rr);
                c++;
                if ( c == LIMIT ) break;
            }
        } 
        finally {
            // Make sure you close your scanners when you are done!
            // Thats why we have it inside a try/finally clause
            scanner.close();
        }
          
        HBaseDocWorld dw = new HBaseDocWorld();
        dw.init();
        
        System.out.println( ">>> Test the counters ... ");
        
        System.out.println(dw.count("CONTENT1", "USED"));
        System.out.println(dw.count("CONTENT1", "CHANGED"));
        System.out.println(dw.count("CONTENT1", "USED"));
        
        System.out.println(dw.count("CONTENT1", "USED"));
        System.out.println(dw.count("CONTENT2", "USED"));
 
    }

    private static void resetTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        String m = javax.swing.JOptionPane.showInputDialog("I want to reset the table: Confirm with [yes] OR CANCLE!");
        if ( m == null ) return;
        if ( m.equals( "yes" ) ) {
            HBaseAdmin hbase = new HBaseAdmin(config);
            hbase.disableTable(name);
            hbase.deleteTable(name);
            
            System.out.println(">>> HBase table (" + name + ") was deleted.");
            
            initTable( config, name );
        }

    }

    private static void addColfamToTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

    }
    
    private static void initTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        HBaseAdmin hbase = new HBaseAdmin(config);

        HTableDescriptor desc = new HTableDescriptor(name);
        
        HColumnDescriptor meta2 = new HColumnDescriptor(colFamNameR.getBytes());

        desc.addFamily(meta2);

        if (hbase.tableExists(name)) {
            resetTable( config, name );
        } 
        else {
            hbase.createTable(desc);
        };

    }
}
