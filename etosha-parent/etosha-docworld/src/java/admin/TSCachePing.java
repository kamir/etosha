/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import data.io.HBaseTester;
import data.io.adapter.HBaseTSAdapter3;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamir
 */
public class TSCachePing {

    /**
     * Simple Ping Test. 
     * 
     * Is the HBase cluster still running fine?
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        HBaseTester.main(args);
        
        HBaseTSAdapter3 a = HBaseTSAdapter3.init();
        
        String k = "Hi";
        String v = "Mirko!";
        try {
            a.putEditTS(k.getBytes(), v.getBytes() );
        
            String r = new String( a.getEditTS( k.getBytes() ) );
        
            System.out.println( k + " " + r );
        } 
        catch (Exception ex) {
            Logger.getLogger(TSCachePing.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
