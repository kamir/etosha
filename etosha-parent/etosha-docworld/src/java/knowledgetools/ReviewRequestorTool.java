package knowledgetools;

import admin.DocTabAdmin;
import static admin.DocTabAdmin.colFamNameR; 
import admin.MailFactory;
import com.cloudera.mailer.MailerTool;
import com.cloudera.mailer.mails.ActionMail; 
import data.io.adapter.ObjectEncoder;
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
 *
 * @author kamir
 */
public class ReviewRequestorTool {

    ReviewRequestorTool rrt = null;
    HBaseDocWorld dw = null;

    /**
     * 
     * THE BOTH ARRAY can also be defined by a JSP.
     * 
     */
    public static int[] maxItemsPerUser = { 1, 3 };
    public static String[] adresses = { "mirko@cloudera.com", "mirko.kaempf@gmail.com" };
    
    /**
     * This tool has an HBaseDocWorld handler to talk to the DB.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ReviewRequestorTool rrt = new ReviewRequestorTool();
        rrt.runInvitationRound();
    
    }

    
    public ReviewRequestorTool() {
        dw = new HBaseDocWorld();
        dw.init();
        
        // load the list of email adresses
        initParticipants();
    
    }
    
    Vector<String> participantMailAdresses = new Vector<String>();
    
    public String runInvitationRound() {
        
        
        int z = participantMailAdresses.size();
        
        int maxItems = sum( maxItemsPerUser );

        //int maxItems = 10000;
        
        // SCAN HBASE FOR SOME non-valid items
        String[] r = dw.getScannResults(maxItems);
        
        int c = 0;  // user
        int c2 = 0;  // mails per user
        for( String s : r ) {
            System.out.println(">> " + s );
            try {
                
                Properties pr = (Properties) ObjectEncoder.fromString(s);
                pr.list(System.out);
                
                String mail = participantMailAdresses.elementAt(c);
                
                if ( c2 == maxItemsPerUser[c] ) {
                    // NÄCHST USER 
                    c++;
                    c2 = 0;
                }
                else {
                    c2++;
                }
                        
                if ( pr.getProperty("isValid") == null ) {
                    invalidateItem( pr.getProperty("id") );
                }
                
                requestAReviewForThisItem( mail, pr );

            } 
            catch (IOException ex) {
                Logger.getLogger(ReviewRequestorTool.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReviewRequestorTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return submitTheRequests();
        
    }

    private void initParticipants() {
        participantMailAdresses = new Vector<String>();
        int i = 0;
        for( String s : adresses ) {
            participantMailAdresses.add(s);
            System.out.println("> " + s + " => " + maxItemsPerUser[ i ]);
            i++;
        }
        System.out.println(">>> # of participants : " + participantMailAdresses.size() );
    }

    /**
     * 
     * Here we must call the WS to put the value 0 into the field raw:valid
     * with a simple put.
     * 
     * @param property 
     */
    private void invalidateItem(String key) {

        dw.putExtended(key, DocTabAdmin.colFamNameR, "isValid", "0");
    
    }

    Vector<ReviewRequest> requests = new Vector<ReviewRequest>();
    private void requestAReviewForThisItem(String mail, Properties pr) {

        String key = pr.getProperty("id");
        dw.putExtended(key, DocTabAdmin.colFamNameR, "isValid", "0");
        dw.putExtended(key, DocTabAdmin.colFamNameR, "underReview", "1");
        
        ReviewRequest rq = new ReviewRequest( mail, pr );
        requests.add(rq);
    
    }

    private void validateThisItem(String key) {

        dw.putExtended(key, DocTabAdmin.colFamNameR, "isValid", "1");
        
    }

    private String submitTheRequests() {

        String stus = "";
        for ( ReviewRequest rr : requests ) {
            
            System.out.println( rr );
            
            ActionMail m = MailFactory.getReviewRequestMail( rr.pr, rr.mail, DocTabAdmin.baseURL );
            stus = stus + MailerTool.sendMail(m) + "<br>";
            
            System.out.println( stus );
            
            // if mail not send invalidate again
            
            
        }
    
        return stus;
    }

    private int sum(int[] a) {
        int sum = 0;
        for( int i: a )
            sum = sum + i;
        return sum;    
    }

}
