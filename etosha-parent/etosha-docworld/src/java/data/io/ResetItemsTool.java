package data.io;

import knowledgetools.*;
import admin.DocTabAdmin;
import static admin.DocTabAdmin.colFamNameR; 
import admin.MailFactory;
import com.cloudera.mailer.MailerTool;
import com.cloudera.mailer.mails.ActionMail; 
import org.apache.hadoopts.data.io.adapter.ObjectEncoder;
import hadoop.cache.doc.HBaseDocWorld;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

/**
 * Here we scan for all items with and reset their valid-state to 0
 * 
 * @author kamir
 */
public class ResetItemsTool {

    ResetItemsTool rrt = null;
        
    /**
     * This tool has an HBaseDocWorld handler to talk to the DB.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ResetItemsTool rrt = new ResetItemsTool();
        rrt.resetAllItems();
    
    }

    HBaseDocWorld dw = null;
    
    public ResetItemsTool() {
        dw = new HBaseDocWorld();
        dw.init();
    }
    
    private void resetAllItems() {
        
        // SCAN HBASE FOR to reset all valid-states to 0 
        String m = dw.resetItemsToState("0");
        System.out.println("Reset m=" + m + " items." );
        
    }


}
