/*
 * 
 */
package admin;
 
import data.io.adapter.HBaseDocWorldAdapter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**
 * Simple Ping Test. 
 * 
 * Is the HBase cluster still running fine?
 * 
 * @author kamir
 */
public class SoleraFBCPing {

    /**
     * Simple Ping Test. 
     * 
     * Is the HBase cluster still running fine?
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
   
        
        HBaseDocWorldAdapter a = HBaseDocWorldAdapter.init();
        
        String k = "Hi";
        String v = "Mirko!";
        try {
            
            a.putDoc(k.getBytes(), v.getBytes() );
        
            String r = new String( a.getDoc( k.getBytes() ) );
        
            System.out.println( k + " " + r );
        } 
        catch (Exception ex) {
            Logger.getLogger(SoleraFBCPing.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
