/*
 * HadoopTripleCache is a "cluster-local" triple cache for large scale 
 * "data-local" analysis. 
 * 
 * Instead of sending recurring queries to remote services, data is collected,
 * and locally stored in a Hadoop cluster.
 * 
 * All literals are stored in HBase. The unique hash of the literal allows
 * Data deduplication. Literals are also indexed, first in a default "LITERALS"
 * collection. 
 *
 * The CRUD module stores the triples.
 * The QA module translates queries.
 */
package hadoop.cache.triples;

import data.io.adapter.HBaseTripleAdapter;
import hadoop.cache.doc.*; 
import hadoop.cache.ts.HBaseTimeSeriesCache;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod; 

/**
 *
 * @author kamir
 */
@WebService(serviceName = "HadoopTripleCache.v1")
public class HadoopTripleCache {

    static HBaseTripleAdapter adapter = null;
    
    /**
     * Is a document with this particular key stored?
     * 
     * @param sk
     * @return 
     */
    @WebMethod 
    public boolean hasTriple(  String sk ) {
        boolean ret = false;
        try {
            
            System.out.println( "Lookup Triple for Key: " + sk );
            
            
            byte[] k = sk.getBytes();
            System.out.println( "*** " + adapter );
            if ( k != null ) ret = adapter.hasTripleForKey(k);
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;        
    }
    
    /**
     * Put this document into the store.
     * 
     * @param sk
     * @param sv 
     */
    @WebMethod 
    public void putTriple( String s, String p, String o) {
        try {
            
            byte[] bs = s.getBytes();
            byte[] bp = p.getBytes();
            byte[] bo = o.getBytes();

            if ( bs != null && bp != null && bo != null ) {
                adapter.putTRIPLE( bs, bp, bo );
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(HBaseTimeSeriesCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 

    
 
    
 
    
    @WebMethod 
    public int init() {
        adapter = HBaseTripleAdapter.init();
        return 42+1;
    }

    /**
     * Tell me if the document was used. I will remember and count.
     */
    @WebMethod
    private void countUsage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Tell me when the document was used. I will remember and create an access time series.
     */
    @WebMethod
    private void logUsage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Give my the usage-history of this document
     */
    @WebMethod
    private Object getAccessHistory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Give my the editorial-history of this document
     */
    @WebMethod
    private Object getEditsgHistory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
