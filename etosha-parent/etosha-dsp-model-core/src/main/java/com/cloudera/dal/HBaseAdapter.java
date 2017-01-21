/**
 *  The HBaseAdapter is a class, which implements a direct link to 
 *  an HBase cluster.
 * 
 *  It gives us references to HBase tables in this HBase cluster.
 * 
 *  Just define the connection to the Zookeeper server and start putting or 
 *  getting data from HBase. As all data is just binary (content of each cell
 *  is just a byte-Array - we have to use some higher abstraction layers on top 
 *  of the adapter to map data structures the right way.
 * 
 *  So we can easily handle networks as adjacency-lists or adjacency-matrices,
 *  distributed or in on single cell, dependent on the algorithm which will be 
 *  applied to the data set.
 * 
 */
package com.cloudera.dal;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author kamir
 */
public class HBaseAdapter {
    
    String zookeeperIP = null;
    String zookeeperPort = "2181";
    
    public Configuration config = null;
    
    /**
     * Overwrites the zookeeperIP with the "localhost" name.
     * 
     * @param port
     * @return 
     */
    public static HBaseAdapter getLocalHBaseAdapter( String port ) {
        HBaseAdapter adapter = new HBaseAdapter( "localhost", port );
        return adapter;
    }

    /**
     * Create an adapter for HBase with a given Zookeeper ensemble.
     * 
     * @param theZookeeperIP 
     */
    public HBaseAdapter( String theZookeeperIP ) {
        zookeeperIP = theZookeeperIP;
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperIP );  
        config.set("hbase.zookeeper.property.clientPort", zookeeperPort);  // Here we are running zookeeper locally
    }
    
    /**
     * Create an adapter for HBase with a given Zookeeper ensemble.
     * 
     * @param theZookeeperIP
     * @param port 
     */
    public HBaseAdapter( String theZookeeperIP, String port ) {
        zookeeperIP = theZookeeperIP;
        zookeeperPort = port;
        
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperIP );  
        config.set("hbase.zookeeper.property.clientPort", zookeeperPort);  // Here we are running zookeeper locally
    }

    /**
     * Returns a handle to a table if the configuration is already loaded.
     * 
     * @param tableName
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public HTable getTable( String tableName ) throws IOException, Exception {
        if ( config != null ) {
            HTable table = new HTable(config, tableName );
            
            if ( table != null )
            return table;
            else throw new Exception("Could table " + tableName + " not access." );
        }
        else { 
            throw new Exception( "> No config object in " + this.getClass() );
        }
    }
    

}
