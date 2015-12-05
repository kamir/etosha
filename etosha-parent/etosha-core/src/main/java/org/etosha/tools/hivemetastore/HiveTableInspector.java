/*
 * The Hive-JDBC client is an example application which shows
 * how to load data from an Hadoop cluster via the 
 * Hive-JDBC interface.
 * 
 * The required JDBC driver: 
 *       org.apache.hadoop.hive.jdbc.HiveDriver 
 * 
 * So we have a dependency to Hive libs.
 * 
 * TODO: Add a kind of a fingerprint for a table-schema state which goes into
 *       the description and can be compared manually or later with a tool.
 * 
 *       Idea:   timestamp : checksum pair can indicate state changes. 
 * 
 */
package org.etosha.tools.hivemetastore;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import org.apache.hadoop.conf.Configuration;
import org.etosha.cmd.EtoshaContextLogger;

public class HiveTableInspector {
    
    public static EtoshaContextLogger clt = null;
    
    public static Configuration cfg = null;
    
  
    /**
     * 
     * The HiveTableInspector loads the table description and 
     * creates for each table a page with the name:
     *   
     * "hivetable_clustername_tablename" which is in a categories:
     *      HiveTableDataset 
     *      DataSet
     * 
     * It also adds a property which defines the relation between the dataset 
     * and the cluster, it is stored in. 
     * 
     */
    
  private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";

  /**
 * @param args
 * @throws SQLException
   */
  public static void run(String[] args) throws SQLException {
      
    try {
      Class.forName(driverName);
    } 
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println( ">>> The Hive-JDBC driver is not available!" );
      System.exit(1);
    }
//    String host = "localhost";  
    String host = "172.16.14.181";  
    
    String connectionStr = "jdbc:hive://"+host+":10000";
    System.out.println( ">>> " + connectionStr );
    
    Connection con = DriverManager.getConnection( connectionStr, "", "");
    
    Statement stmt = con.createStatement();
   
    Vector<String> tables = new Vector<String>();
    
    // show tables
    String sql = "show tables";
    System.out.println(">>> Execute : " + sql);
    ResultSet res = stmt.executeQuery(sql);
    while (res.next()) {
        
        String tn = res.getString(1);
        System.out.println("\t[tab] " +  tn);
        tables.add(tn);
    }
    
    for( String tableName : tables ) {
        
        HiveTableDataSetPage page = new HiveTableDataSetPage();
        page.cluster = "VM3";
        page.tablename = tableName;
        
        StringBuffer schema = new StringBuffer();
        
        // to track schema evolution we ignore new comments.
        StringBuffer schemaCore = new StringBuffer();

        // describe table
        sql = "describe " + tableName;
        System.out.println("\n>>>> Execute : " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
          System.out.println("\t[col]" + res.getString(1) + "\t" + res.getString(2) + "\t" + res.getString(3));
          schema.append( "  [col]" + res.getString(1) + " :: " + res.getString(2) + " :: " + res.getString(3) + "\n");
          schemaCore.append( "  [col]" + res.getString(1) + " :: " + res.getString(2) + "\n");
        }
        
        // describe table
        sql = "describe extended " + tableName;
        System.out.println("\n>>>> Execute : " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
          System.out.println("***" + res.getString(1) + "\n");
          //schema.append( "  [col]" + res.getString(1) + " :: " + res.getString(2) + " :: " + res.getString(3) + "\n");
          //schemaCore.append( "  [col]" + res.getString(1) + " :: " + res.getString(2) + "\n");
        }

        page.schema = schema.toString();
        page.schemaCore = schemaCore.toString();
        
        
        System.out.println( page.getPageName() );
        System.out.println( page.getPageContent() );
        System.out.println();
        
        page.update( clt );
    }

  }
}

class HiveTableDataSetPage {

    String cluster = "default";
    String tablename = "?";
    String schema = "?";
    String schemaCore = "?";
    String ts = null;
    
    public HiveTableDataSetPage() {
        Date d = new Date( System.currentTimeMillis() );
        ts = d.toString();
    }
            
    public String getPageContent(){
        
        String s = "===Hive Table Description===\n";
        s = s + "[[Category:HiveTableDataset]]\n";
        s = s + "[[Category:DataSet]]\n";
        s = s + "Stored in Cluster : [[Is available in cluster::"+cluster+"]]\n";
        s = s + "Checksum : [[has state tracking checksum::"+getChecksum( schemaCore )+"]] created @ " + ts  + "\n";
        s = s + schema + "\n";
        s = s + "(Snippet was created by: [[createdBy:" + this.getClass().getName() + ")]]";
        return s;
        
    }
    
    
    public String getPageName(){
        return "hivetable_" + cluster + "_" + tablename;
    }

    void update(EtoshaContextLogger clt) {
        try {
            
            clt.scb.createPageIfNotExists( this.getPageName() );
            
            clt.scb.logDataSetSchemaToDataSetPage( this.getPageName(), this.getPageContent() );
            
        } 
        catch (IOException ex) {
            Logger.getLogger(HiveTableDataSetPage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (LoginException ex) {
            Logger.getLogger(HiveTableDataSetPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String getChecksum(String t) {
        return "hashCode=" + t.hashCode();
    }
}
