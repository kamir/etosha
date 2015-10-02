package hadoop.cache.ts;

import data.io.adapter.HBaseTSAdapter3;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import javax.jws.soap.SOAPBinding.*;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author User
 */
@WebService(name = "TSCache.v3")
@MTOM
public class HBaseTimeSeriesCache {
    
    static HBaseTSAdapter3 adapter = null;
   
    @WebMethod 
    public boolean hasKey(  String sk ) {
        boolean ret = false;
        try {
            byte[] k = sk.getBytes();
            System.out.println( "*** " + adapter );
            if ( k != null ) ret = adapter.hasEditTSKey(k);
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
        
    }
 
    @WebMethod 
    public void put( String sk, String sv) {
        try {
            
            byte[] k = sk.getBytes();
            byte[] v = sv.getBytes();

            if ( k != null && v != null ) adapter.putEditTS(k, v);
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    
    
    
    
    
    @WebMethod 
    public String get( String sk) {
        byte[] v = null;
        try {
            
            byte[] k = sk.getBytes();

            if ( k != null ) v = adapter.getEditTS( k );
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String( v );
    }
    
    @WebMethod 
    public int init() {
        adapter = HBaseTSAdapter3.init();
        return 42;
    }
    



    
}
