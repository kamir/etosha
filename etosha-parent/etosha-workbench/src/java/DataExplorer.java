
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * We browse the web. Step from URL to URL and explore the context of each
 * page. Available links, lists and tables are selected and presented.
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
        int c = 0;
        StringBuffer sb = new StringBuffer();
        for( String u : links ) {
            sb.append( "[" + c + "] \t" + u + "\n");
            c++;
        }
        System.out.println( sb.toString() );
    }

    private void move() throws IOException {

        list( "TARGETS", getLinks( trace.position ) );

        int i = Integer.parseInt( javax.swing.JOptionPane.showInputDialog("Where to go next? (FROM: " + trace.position + ")" ) );

        String[] urls = getLinks( trace.position );
        if (i > 0 && i < urls.length) {
            cd(urls[i]);
        }

    }

    Scrapelet currentSnippet = null;
    
    private void scrape() throws IOException {

        // HERE WE SCRAPE VIA JSOUP ...
        String[] OPTIONS = {"LIST", "TABLE" };            
        int j = javax.swing.JOptionPane.showOptionDialog(null, "What's next:", "Select a thing to scrape :", JOptionPane.DEFAULT_OPTION , 1, null, OPTIONS, 1);

        switch ( j ) {
            
            case 0 : {   

                        list( "LISTS to scrape from { " + trace.position + " } ", this.getDataLists(trace.position ) );
                        break;
            }
            case 1 : {

                        String selector = javax.swing.JOptionPane.showInputDialog( "Selector: " , "#table2") ;
                        
                        list( "TABLES to scrape from { " + trace.position + " } " , this.getDataLists( trace.position ) );

                        TableScraper.open(trace.position, selector );
                        
                        /**
                         * THIS MUST be provided by the TableScaper!!!
                         */
                        Scrapelet s = new Scrapelet();
                        currentSnippet = s;
                        
                        break;
                
            }
            
        }
        
        
    }

    private void persist() {
        
        System.out.println( ">>> PERSIST the current trace of our journey here." );

        openTraceGraph();
        
        Exposer.persist();
    
    }

    private void stop() {
        
        System.out.println( ">>> STOP the journey here: " + this.trace.position );

    }
    
    WebTrace trace = new WebTrace();
    
    public void init(URL url) {
            trace.goTo(url);
    }
    private void init(String u) throws MalformedURLException {
        init( new URL( u ) );
    }

    
    // Jsoup inspection of current position
    public String[] getDataTables(URL url) throws IOException {
        String[] t = ScraperTool.getDataTables(url);
        return t;
    };

    // Jsoup inspection of current position
    public String[] getDataLists(URL url) throws IOException{
        String[] l = ScraperTool.getDataLists(url);
        return l;
    };
    
    // Jsoup inspection of current position
    public String[] getLinks(URL url) throws IOException {
        String[] links = ScraperTool.getLinks( url );
        return links;
    };
    
    public void cd(String url) {
        
        System.out.println( ">>> CD to {"+ url + "}");
        int index = url.lastIndexOf(" => ");
        
        url = url.substring(index + 4);

        System.out.println( ">>> MOVE now to {"+ url + "}");
        
        try {
            URL u = new URL( url );
            trace.goTo(u);
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(DataExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    static SchemaRepository repo = null;
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        
        Exposer.init();
        
        repo = SchemaRepository.getSchemaRepository();
    
        String pageZeroA = "https://de.wikipedia.org/wiki/Stollberg/Erzgeb.";
        String pageZeroB = "http://finance.yahoo.com/q/cp?s=%5ESDAXI+Components";
        String pageZeroC = "https://stats.wikimedia.org/EN/Sitemap.htm";
        
        String[] OPTIONS0 = { pageZeroA , pageZeroB, pageZeroC }; 
        
        int k = javax.swing.JOptionPane.showOptionDialog(null, "Where to start:", "Select a data-site:", JOptionPane.DEFAULT_OPTION , 1, null, OPTIONS0, 1);
       
        String pageZero = OPTIONS0[k];
        
        DataExplorer explorer = new DataExplorer();
        explorer.init( pageZero );

        int i = 0;  
        
        while( i != -1 ) {
            
            list( "DATA-LISTS", explorer.getDataLists( explorer.trace.position ) );
            list( "DATA-TABLES", explorer.getDataTables( explorer.trace.position ) );

            String[] OPTIONS = {"MOVE", "SCRAPE", "PERSIST", "EXPOSE", "STOP"};
            
            int j = javax.swing.JOptionPane.showOptionDialog(null, "What's next:", "Select a task:", JOptionPane.DEFAULT_OPTION , 1, null, OPTIONS, 0);

            switch( j ) {
                
                case 0 : explorer.move();  // go on to another page ...
                         break;
                    
                case 1 : explorer.scrape();
                         // current snippet is not null any more !!!
                         break;
                    
                case 2 : explorer.persist();
                         break;
                    
                case 3 : { // METADATA about a snippet goes to the Exposer
                           explorer.expose( explorer.currentSnippet );
                           break;
                         }
                    
                case 4 : explorer.stop();
                         i = -1;
                         break;
            }

            

        }
        
        System.out.println( explorer.trace.toString() );
        
        explorer.openTraceGraph();
        
    }

    private void openTraceGraph() {
        
        Object graph = this.trace.getGraphOfTraces();
        
        // SHOW GRAPH AS IN MORPHMINER ...
        
        // store the trace ...
        
    }

    private void expose(Scrapelet currentSnippet) {
        if ( this.currentSnippet != null )
           Exposer.exposeToDefaultStore( currentSnippet );
    }

    
    
}
