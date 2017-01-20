package tools;

 

import data.io.adapter.HBaseTSAdapter;
import java.io.IOException;

/*
 *  Create some Time-Series and store them in HBase.
 */

/**
 *
 * @author kamir
 */
public class SimpleTSTool {
    
    public static void main( String[] args ) throws IOException, Exception {
        
        String defaultIP = "192.168.3.170";
        
//        if ( args != null ) 
//            defaultIP = args[0];
        
        System.out.println( defaultIP );
        
        boolean doCreate = false;
        boolean doLoad = false;
        int nrOfRows = 25;
        
        HBaseTSAdapter.init( defaultIP );

        if ( doCreate ) {
            for( int i = 0; i < nrOfRows ; i++ ) {
                //TSDataMapper mapper = new TSDataMapper( 24*299 );
                //HBaseTSAdapter.putAccessTS( "wikinodes", mapper , ""+i );
            }

            System.out.println( "Done ..." );
        }
        
        if ( doLoad ) {
            System.out.println( "Start loading ... ");
            // load all data from DB ...
            for( int i = 0; i < nrOfRows ; i++ ) {
                Object o = HBaseTSAdapter.getAccessTS( "wikinodes", ""+i );
                //TSDataMapper mapper = (TSDataMapper)o;
                // System.out.println( mapper.data.length );
            }
        }
        
    }
    
}
