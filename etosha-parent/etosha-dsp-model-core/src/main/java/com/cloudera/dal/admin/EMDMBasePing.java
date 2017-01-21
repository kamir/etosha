/*
 * 
 */
package com.cloudera.dal.admin;
 
import com.cloudera.dal.HBaseEMDMStoreAdapter;
import static com.cloudera.dal.admin.EMDMBaseTabAdmin.colFamName1;
import static com.cloudera.dal.admin.EMDMBaseTabAdmin.colFamName2;
import static com.cloudera.dal.admin.EMDMBaseTabAdmin.colFamName3;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

 /**
 * Simple Ping Test. 
 * 
 * Is the HBase cluster still running fine?
 * 
 * @author kamir
 */
public class EMDMBasePing {

    /**
     * Simple Ping Test. 
     * 
     * Is the HBase cluster still running fine?
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String ZK = args[0];
        String TN = args[1];
        
//        HBaseEMDMStoreAdapter.defaultZookeeperIP = "dl-mn04-d,dl-mn02-d,dl-mn03-d";
        
        HBaseEMDMStoreAdapter.defaultZookeeperIP = ZK;
        EMDMBaseTabAdmin.docTabName = TN;
        
        HBaseEMDMStoreAdapter adapter = HBaseEMDMStoreAdapter.init();
        
        String k = "Hey...";
        String v = "Ho!";
         
        try {
            
            // test putting something into HBase ...
            adapter.putDoc(k.getBytes(), v.getBytes() );

            // test getting something from HBase ...
            String r = new String( adapter.getDoc( k.getBytes() ) );
        
            System.out.println( ">>> EMDM-Ping : key=" + k + " -----> " + r );

            // test delete the sample doc from HBase ...
            adapter.deleteDoc(k.getBytes() );

            System.out.print( ">>> EMDM-Ping : key=" + k + " exist: " );

            boolean b = adapter.hasDocKey( k.getBytes() );
            
            System.out.println( b + "." );
            System.out.println( ">>>                (should be false, because the doc was deleted) " );

        } 
        catch (Exception ex) {
            Logger.getLogger(EMDMBasePing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 

    
    /**
     * 
     * CREATE TABLE is not working in QSVM currently.
     * 
     * @throws IOException 
     */
    private static void ct() throws IOException {

        HBaseAdmin hbase = new HBaseAdmin(HBaseEMDMStoreAdapter.config);

        HTableDescriptor desc = new HTableDescriptor(EMDMBaseTabAdmin.docTabName);
        
        HColumnDescriptor meta1 = new HColumnDescriptor(colFamName1.getBytes());
        HColumnDescriptor meta2 = new HColumnDescriptor(colFamName2.getBytes());
        HColumnDescriptor meta3 = new HColumnDescriptor(colFamName3.getBytes());

        desc.addFamily(meta1);
        desc.addFamily(meta2);
        desc.addFamily(meta3);

            
        System.out.println(">>> HBase table will be created ... (" + EMDMBaseTabAdmin.docTabName + ") " );

        hbase.createTable(desc);

        System.out.println(">>> DONE." );

    }
}
