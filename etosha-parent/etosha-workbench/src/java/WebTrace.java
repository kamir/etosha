
<<<<<<< HEAD
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
=======
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
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
<<<<<<< HEAD
    public Graph<Integer, String> getGraphOfTraces() {

        SimpleTraceGraphView sgtv = new SimpleTraceGraphView( new File( "simple-trace-graph.dat"), urlListe );
        sgtv.open();
        return sgtv.g;
        
=======
    Object getGraphOfTraces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
    }
    
}
