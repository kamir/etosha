package admin;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

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
 * This is part of the time series cache in HBase.
 * 
 */
public class TSTabAdmin {

    public static final String tsTabName = "wikinodes.mk"; 
    public static final String colFamNameE = "edit.ts"; 
    public static final String colFamNameA = "access.ts"; 

    public static void main(String[] args) throws IOException {
        
        // You need a configuration object to tell the client where to connect.
        // When you create a HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as these can
        // be found on the CLASSPATH
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.property.clientPort", "2181");  

        /**
         * Where is the Zookeeper server?
         * 
         */
        // config.set("hbase.zookeeper.quorum", "172.20.63.21");  
        
        /**
         * Mirko's home cluster ...
         */
        config.set("hbase.zookeeper.quorum", "192.168.3.171");  

        String tabName = tsTabName;

        /**
         * Vorsicht !!!
         * 
         * resetTable(  )
         * 
         * ... löscht eine bestehende TABELLE !!!
         * 
         */
        // resetTable(  config , tabName  );  
        
        
        initTable( config , tabName );

        // This instantiates an HTable object that connects you to
        // the "myLittleHBaseTable" table.
        HTable table = new HTable(config, tabName);
        
        int LIMIT = 10;

        // Sometimes, you won't know the row you're looking for. In this case, you
        // use a Scanner. This will give you cursor-like interface to the contents
        // of the table.  To set up a Scanner, do like you did above making a Put
        // and a Get, create a Scan.  Adorn it with column names, etc.
        System.out.println("\nEDITS.TS SCAN");   
        System.out.println("*************");   
        Scan s = new Scan();
        s.addFamily(colFamNameE.getBytes());
        //s.addColumn(Bytes.toBytes(colFamNameE), Bytes.toBytes("raw"));
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

            // The other approach is to use a foreach loop. Scanners are iterable!
            // for (Result rr : scanner) {
            //   System.out.println("Found row: " + rr);
            // }
        } finally {
            // Make sure you close your scanners when you are done!
            // Thats why we have it inside a try/finally clause
            scanner.close();
        }
        
        
        System.out.println("\nACCESS.TS SCAN");   
        System.out.println("**************"); 
        s = new Scan();
        s.addFamily(colFamNameA.getBytes());
        //s.addColumn(Bytes.toBytes(colFamNameA), Bytes.toBytes("raw"));
        
        scanner = table.getScanner(s);
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

            // The other approach is to use a foreach loop. Scanners are iterable!
            // for (Result rr : scanner) {
            //   System.out.println("Found row: " + rr);
            // }
        } finally {
            // Make sure you close your scanners when you are done!
            // Thats why we have it inside a try/finally clause
            scanner.close();
        }

    }

    private static void resetTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

//        HBaseAdmin hbase = new HBaseAdmin(config);
//        hbase.disableTable(name);
//        hbase.deleteTable(name);

    }

    private static void initTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        HBaseAdmin hbase = new HBaseAdmin(config);

        HTableDescriptor desc = new HTableDescriptor(name);
        HColumnDescriptor meta1 = new HColumnDescriptor(colFamNameA.getBytes());
        HColumnDescriptor prefix1 = new HColumnDescriptor("raw".getBytes());
        HColumnDescriptor meta2 = new HColumnDescriptor(colFamNameE.getBytes());

        HColumnDescriptor prefix2 = new HColumnDescriptor("raw".getBytes());
        desc.addFamily(meta1);
        desc.addFamily(prefix1);

        desc.addFamily(meta2);
        desc.addFamily(prefix2);

        if (hbase.tableExists(name)) {
        } else {
            hbase.createTable(desc);
        };


    }
}
