package data.io;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * Simple API Tester.
 * 
 * @author kamir
 */
public class HBaseTester {



    public static void main(String[] args) throws IOException {
    // You need a configuration object to tell the client where to connect.
    // When you create a HBaseConfiguration, it reads in whatever you've set
    // into your hbase-site.xml and in hbase-default.xml, as long as these can
    // be found on the CLASSPATH
    Configuration config = HBaseConfiguration.create();
    
    config.set("hbase.zookeeper.quorum", "192.168.3.171");  // Here we are running zookeeper locally
    config.set("hbase.zookeeper.property.clientPort", "22181");  // Here we are running zookeeper locally
    
    
    String tabName = "wikinodes.2";
    
    initTable( config , tabName );
    
    // This instantiates an HTable object that connects you to
    // the "myLittleHBaseTable" table.
    HTable table = new HTable(config, tabName );

    // To add to a row, use Put.  A Put constructor takes the name of the row
    // you want to insert into as a byte array.  In HBase, the Bytes class has
    // utility for converting all kinds of java types to byte arrays.  In the
    // below, we are converting the String "myLittleRow" into a byte array to
    // use as a row key for our update. Once you have a Put instance, you can
    // adorn it by setting the names of columns you want to update on the row,
    // the timestamp to use in your update, etc.If no timestamp, the server
    // applies current time to the edits.
    Put p = new Put(Bytes.toBytes("myLittleRowKEY"));

    // To set the value you'd like to update in the row 'myLittleRow', specify
    // the column family, column qualifier, and value of the table cell you'd
    // like to update.  The column family must already exist in your table
    // schema.  The qualifier can be anything.  All must be specified as byte
    // arrays as hbase is all about byte arrays.  Lets pretend the table
    // 'myLittleHBaseTable' was created with a family 'myLittleFamily'.
    p.add(Bytes.toBytes("access.ts"), Bytes.toBytes("someQualifier"),
      Bytes.toBytes("Some cool value ..."));

    // Once you've adorned your Put instance with all the updates you want to
    // make, to commit it do the following (The HTable#put method takes the
    // Put instance you've been building and pushes the changes you made into
    // hbase)
    table.put(p);

    // Now, to retrieve the data we just wrote. The values that come back are
    // Result instances. Generally, a Result is an object that will package up
    // the hbase return into the form you find most palatable.
    Get g = new Get(Bytes.toBytes("myLittleRowKEY"));
    Result r = table.get(g);
    byte [] value = r.getValue(Bytes.toBytes("access.ts"),
      Bytes.toBytes("someQualifier"));
    // If we convert the value bytes, we should get back 'Some Value', the
    // value we inserted at this location.
    String valueStr = Bytes.toString(value);
    System.out.println("GET: " + valueStr);

    // Sometimes, you won't know the row you're looking for. In this case, you
    // use a Scanner. This will give you cursor-like interface to the contents
    // of the table.  To set up a Scanner, do like you did above making a Put
    // and a Get, create a Scan.  Adorn it with column names, etc.
    System.out.println("ACCESS.TS SCAN");    
    Scan s = new Scan();
    s.addColumn(Bytes.toBytes("access.ts"), Bytes.toBytes("someQualifier"));
    ResultScanner scanner = table.getScanner(s);
    int i = 0;
    try {
        i++;
      // Scanners return Result instances.
      // Now, for the actual iteration. One way is to use a while loop like so:
      for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
        // print out the row we found and the columns we were looking for
        System.out.println("[" + i + "] found rowkey: " + rr);
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

    private static void initTable(Configuration config, String name) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
            
        HBaseAdmin hbase = new HBaseAdmin( config );
        HTableDescriptor desc = new HTableDescriptor( name ); 
        HColumnDescriptor meta = new HColumnDescriptor("edit.ts".getBytes());
        HColumnDescriptor prefix = new HColumnDescriptor("access.ts".getBytes());
        desc.addFamily(meta);
        desc.addFamily(prefix);
        
        if ( hbase.tableExists(name) ) { 
            System.out.println( ">>> table: " + name + " already exists.");
        }
        else { 
            hbase.createTable(desc);
        }
        
    }
}

    
