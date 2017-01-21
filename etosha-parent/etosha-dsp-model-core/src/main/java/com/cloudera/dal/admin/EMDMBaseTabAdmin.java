package com.cloudera.dal.admin;


import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

/**
 * @author kamir
 * 
 * This tool is used to initialize the table in HBase to hold
 * the match results.
 * 
 *     init() 
 * 
 * and clean up
 *     
 *     reset() 
 * 
 * are used with care! 
 * 
 * Tabel names:
 * 
 *    docTabName ...
 * 
 * This is part of the raw document cache in HBase and uses a direct 
 * interaction with HBase.
 * 
 */
public class EMDMBaseTabAdmin {

    public static String default_server = "127.0.0.1";
     
    /**  
     * CREATE TABLE COMMAND ...
     *   create 'partsMatch', 'me', 'prop', 'fb'
     */
    public static String docTabName = "emdmBase"; 
    
    public static final String colFamName1 = "cn";    // matching engine 
    public static final String colFamName2 = "cm";  // expanded properties
    public static final String colFamName3 = "fast";    // feedback
    
    public static String[] FIELDS1 = { "raw" };
    public static String[] FIELDS3 = { "identity", "name", "owner", "created" };

    public static void main(String[] args) throws IOException {
        
        // You need a configuration object to tell the client where to connect.
        // When you create a HBaseConfiguration, it reads in whatever you've set
        // into your hbase-site.xml and in hbase-default.xml, as long as these can
        // be found on the CLASSPATH

        Configuration config = HBaseConfiguration.create();
        String server = default_server;
        
        /**
         * Where is the Zookeeper server?
         * 
         * QSVM cluster ...
         *
         **/

        if ( server.equals("QSVM") ) {
            /**
             * QSVM ...  
             */
            config.set("hbase.zookeeper.quorum", "quickstart.cloudera");  
            config.set( "hbase.zookeeper.property.clientPort", "2181");  
        }

        if ( server.equals("solera-dev") ) {
            /**
             * Local in VM ...
             */
            String zkHostString = "dl-mn04-d,dl-mn02-d,dl-mn03-d";
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
//        resetTable( config , tabName );     CURRENTLY NOT WORKING !!!
        
        initTable( config , tabName );

        // This instantiates an HTable object that connects you to
        // the "tabName" table.
        HTable table = new HTable(config, tabName);
        
        int LIMIT = 10;

        // Sometimes, you won't know the row you're looking for. In this case, you
        // use a Scanner. This will give you cursor-like interface to the contents
        // of the table.  To set up a Scanner, do like you did above making a Put
        // and a Get, create a Scan.  Adorn it with column names, etc.
        System.out.println("\nEMDM Base-Tab SCAN");   
        System.out.println(  "***************");   
        System.out.println(  ">>> LIMIT = " + LIMIT );   
        Scan s = new Scan();
        
        s.addFamily(colFamName1.getBytes());

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
 
    }

    /**
     * We remove the table and go on to he init function to create it new.
     * 
     * @param config
     * @param name
     * @throws MasterNotRunningException
     * @throws ZooKeeperConnectionException
     * @throws IOException 
     */
    private static void resetTable(Configuration config, String name, boolean force) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        String m = "no";
        
        if ( !force ) {
        
            m = javax.swing.JOptionPane.showInputDialog("I want to reset the table: Confirm with [yes] OR CANCLE!");
        
            if ( m == null ) return;
            else {
                System.out.println( " m="+m );
            }
        }
        else {
            m="yes";
        }
            
            
        if ( m.equals( "yes" ) ) {
            
            HBaseAdmin hbase = new HBaseAdmin(config);
            
            try {
                hbase.disableTable(name);
                System.out.println(">>> HBase table (" + name + ") was disabled. (table available: " + hbase.isTableAvailable(name) + ") ");
            }
            catch(Exception ex){
            }

            
            
            try {
                hbase.deleteTable(name);
                System.out.println(">>> HBase table (" + name + ") was deleted.");
            }
            catch(Exception ex){
            }

            
            
            try {
                hbase.deleteTable(name);
                initTable( config, name );
                System.out.println(">>> Create table (" + name + ") now.");
            }
            catch(Exception ex){
            }        
            
        }

    }
     
    /**
     * Delete and create or simply create.
     * 
     * Delete operation has a safety valve.
     * 
     * @param config
     * @param name
     * @throws MasterNotRunningException
     * @throws ZooKeeperConnectionException
     * @throws IOException 
     */
    public static void initTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {

        HBaseAdmin hbase = new HBaseAdmin(config);

        HTableDescriptor desc = new HTableDescriptor(name);
        
        HColumnDescriptor meta1 = new HColumnDescriptor(colFamName1.getBytes());
        HColumnDescriptor meta2 = new HColumnDescriptor(colFamName2.getBytes());
        HColumnDescriptor meta3 = new HColumnDescriptor(colFamName3.getBytes());

        desc.addFamily(meta1);
        desc.addFamily(meta2);
        desc.addFamily(meta3);

        if (hbase.tableExists(name)) {
            
            System.out.println(">>> HBase table will be reset ... (" + name + ") " );
            resetTable( config, name, true );
            System.out.println(">>> DONE." );
        
        } 
        
            
        System.out.println(">>> HBase table will be created ... (" + name + ") " );
        hbase.createTable(desc);
        System.out.println(">>> DONE." );

    }
 
 
}
