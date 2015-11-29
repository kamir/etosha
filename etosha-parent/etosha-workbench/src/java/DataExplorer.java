
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * 
 * 
 **/

/**
 *
 * @author kamir
 */
public class DataExplorer {

    private static void list(String t, String[] links) {

        System.out.println("["+t+"]");
        StringBuffer sb = new StringBuffer();
        for( String u : links ) {
            sb.append("\t" + u + "\n");
        }
        System.out.println( sb.toString() );
    }
    
    WebTrace trace = new WebTrace();
    
    public void init(URL url) {
            trace.goTo(url);
    }
    private void init(String u) throws MalformedURLException {
        init( new URL( u ) );
    }

    
    // Jsoup inspection of current position
    public String[] getDataTables() {
        String[] dummy = {"tabA","tabB","tabE","tabF","tabD"};
        return dummy;
    };

    // Jsoup inspection of current position
    public String[] getDataLists(){
        String[] dummy = {"listA","listB","listE","listF","listD"};
        return dummy;
    };
    
    // Jsoup inspection of current position
    public String[] getLinks() {
        String[] dummy = {"http://www.URLA.de","http://www.URLURL-B.de","http://www.URLURL-E.de","http://www.URLF.de","http://www.URLD.de"};
        return dummy;
    };
    
    public void cd(String url) {
        try {
            URL u = new URL( url );
            trace.goTo(u);
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(DataExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static void main(String[] args) throws MalformedURLException {
    
        String start = "https://stats.wikimedia.org/wikisitemap.html";
        
        DataExplorer explorer = new DataExplorer();
        explorer.init( start );

        int i = 0;
        
        while( i != -1 ) {
            
            list( "DATA-LISTS", explorer.getDataLists() );
            list( "DATA-TABLES", explorer.getDataTables() );
            list( "TARGETS", explorer.getLinks() );

            String[] OPTIONS = {"SCRAPE list","SCRAPE table", "MOVE"};
            
            int j = javax.swing.JOptionPane.showOptionDialog(null, "What should we do?", "Select:", JOptionPane.DEFAULT_OPTION , 1, null, OPTIONS, 2);
            
            
            i = Integer.parseInt( javax.swing.JOptionPane.showInputDialog("Where to go?") );

            String[] urls = explorer.getLinks();
            if( i > 0 && i < urls.length )
                explorer.cd( urls[i] );

        }
        
        System.out.println( explorer.trace.toString() );
        
        explorer.openTraceGraph();
        
    }

    private void openTraceGraph() {
        
        Object graph = this.trace.getGraphOfTraces();
        
        // SHOW GRAPH AS IN MORPHMINER ...
        
    }

    
    
}
