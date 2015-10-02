/**
 *  The HBaseAdapter is a class, which will be instanziated in MATLAB or other
 *  systems, to implement a direct link to an HBase cluster.
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
package data.io.adapter;


import static data.io.adapter.HBaseTSAdapter3.config;
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
    
    String zookeeperIP = "192.168.3.171";
    String zookeeperPort = "22181";
    
    public Configuration config = null;
    
    public static HBaseAdapter getLocalHBaseAdapter( String port ) {
        HBaseAdapter adapter = new HBaseAdapter( "localhost", port );
        return adapter;
    }

    public HBaseAdapter( String theZookeeperIP ) {
        zookeeperIP = theZookeeperIP;
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperIP );  
        config.set("hbase.zookeeper.property.clientPort", zookeeperPort);  // Here we are running zookeeper locally
    }

    public HBaseAdapter( String theZookeeperIP, String port ) {
        zookeeperIP = theZookeeperIP;
        zookeeperPort = port;
        
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperIP );  
        config.set("hbase.zookeeper.property.clientPort", zookeeperPort);  // Here we are running zookeeper locally
    }

    /**
     * 
     * @param tableName
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public HTable getTable( String tableName ) throws IOException, Exception {
        if ( config != null ) {
            HTable table = new HTable(config, tableName );
            return table;
        }
        else { 
            throw new Exception( "> No config object in " + this.getClass() );
        }
    }
    

}
