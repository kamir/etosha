
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
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
    public Graph<Integer, String> getGraphOfTraces() {

        SimpleTraceGraphView sgtv = new SimpleTraceGraphView( new File( "simple-trace-graph.dat"), urlListe );
        sgtv.open();
        return sgtv.g;
        
    }
    
}
