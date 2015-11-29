
import java.net.URL;
import java.util.Vector; 

 

/**
 *
 * @author kamir
 */
class WebTrace {

    URL position = null;
    
    public void goTo( URL url ) {
        
        position = url;
        urlListe.add(url);
    }
    
    private Vector<URL> urlListe = new Vector<URL>();
    
    // WRITE TRACE TO WIKI
    public void dumpToWiki(Object o) {}
    
    // WRITE TRACE TO WIKI
    @Override
    public String toString() {
        
        StringBuffer sb = new StringBuffer("[WebTrace]");
        for( URL u : urlListe ) {
            sb.append("\t" + u + "\n");
        }
        return sb.toString();
    }

    // JUNG2 representation of graph ...
    Object getGraphOfTraces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
