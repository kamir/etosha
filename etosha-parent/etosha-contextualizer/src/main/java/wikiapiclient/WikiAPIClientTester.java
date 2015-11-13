/**
 * The Wiki-API CLient tester is used to validate the compatibility
 * of the used WIKI Connector.
 * 
 * We have to replace the WikiORIGINAL.java connector by one, which
 * is under Apache License. This one is already started as part 
 * of the MAG.net project.
 * 
 */
package wikiapiclient;

import java.io.IOException;
import java.util.HashMap;

public class WikiAPIClientTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
   
        /**
         * This is the "ORIGINAL" GNU Licensed Wiki-Client tool.
         */
        WikiORIGINAL w = new WikiORIGINAL();

        WikiORIGINAL w2 = new WikiORIGINAL( "www.semanpix.de/opendata/wiki","");
        w2.getPageText("Main");
        
        String pn = "Berlin";
        
        // Read a page ...
        String pageText = w.getPageText( pn );
        
        // What links are on this page?
        String[] links = w.getLinksOnPage( pn );
        System.out.println( "> pagename: (" + pn + ")\n> has " + links.length + " links." );
        
        HashMap<String,String> iwl = w.getInterWikiLinks(pn);
        System.out.println( "> pagename: (" + pn + ")\n> has " + iwl.size() + " interwiki-links." );
        
    }
    
}
