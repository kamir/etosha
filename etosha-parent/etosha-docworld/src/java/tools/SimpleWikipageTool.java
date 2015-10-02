package tools;


import data.io.adapter.HBaseTSAdapter;
import data.io.adapter.HBaseWikiAdapter;
import data.ts.TSDataWrapper;
import data.wikipedia.dump.WikipediaNode;
import java.io.IOException;

/*
 *  Create / Load some Wikipages and store them in HBase.
 */

/**
 *
 * @author kamir
 */
public class SimpleWikipageTool {
    
    public static void main( String[] args ) throws IOException, Exception {
        
        String myIP = null;  // use default IP
        
        System.out.println( myIP );
        
        boolean doCreate = true;
        boolean doLoad = true;
        int nrOfRows = 25;
        
        HBaseWikiAdapter.init( myIP );

        if ( doCreate ) {
            for( int i = 0; i < nrOfRows ; i++ ) {
                WikipediaNode node = new WikipediaNode( (i+") Meine Seite").getBytes(), null );
                HBaseWikiAdapter.putArticle( "wikipages", node , ""+i );
            }

            System.out.println( "Done ..." );
        }
        
        if ( doLoad ) {
            System.out.println( "Start loading ... ");
            // load all data from DB ...
            for( int i = 0; i < nrOfRows ; i++ ) {
                Object o = HBaseWikiAdapter.getArticle( "wikipages", ""+i );
                WikipediaNode mapper = (WikipediaNode)o;
                // System.out.println( mapper.data.length );
            }
        }
        
    }
    
}
