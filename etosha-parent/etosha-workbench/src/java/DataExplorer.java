
<<<<<<< HEAD
import java.io.IOException;
=======
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
<<<<<<< HEAD
 * We browse the web. Step from URL to URL and explore the context of each
 * page. Available links, lists and tables are selected and presented.
 *  
=======
 *
 * 
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
 * 
 **/

/**
 *
 * @author kamir
 */
public class DataExplorer {

<<<<<<< HEAD
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

    
    public void move() throws IOException {

        list( "TARGETS", getLinks( trace.position ) );

        int i = Integer.parseInt( javax.swing.JOptionPane.showInputDialog("Where to go next? (FROM: " + trace.position + ")" ) );

        String[] urls = getLinks( trace.position );
        if (i > 0 && i < urls.length) {
            cd(urls[i]);
        }

    }

    Scrapelet currentSnippet = null;
    
    public void scrape() throws IOException {

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

    public void persist() {
        
        System.out.println( ">>> PERSIST the current trace of our journey here." );

        openTraceGraph();
        
        Exposer.persist();
    
    }

    public void stop() {
        
        System.out.println( ">>> STOP the journey here: " + this.trace.position );

    }
    
    
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

=======
    private static void list(String t, String[] links) {

        System.out.println("["+t+"]");
        StringBuffer sb = new StringBuffer();
        for( String u : links ) {
            sb.append("\t" + u + "\n");
        }
        System.out.println( sb.toString() );
    }
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
    
    WebTrace trace = new WebTrace();
    
    public void init(URL url) {
            trace.goTo(url);
    }
<<<<<<< HEAD
    public void init(String u) throws MalformedURLException {
=======
    private void init(String u) throws MalformedURLException {
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
        init( new URL( u ) );
    }

    
    // Jsoup inspection of current position
<<<<<<< HEAD
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

            
=======
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
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b

        }
        
        System.out.println( explorer.trace.toString() );
        
        explorer.openTraceGraph();
        
    }

    private void openTraceGraph() {
        
        Object graph = this.trace.getGraphOfTraces();
        
        // SHOW GRAPH AS IN MORPHMINER ...
        
<<<<<<< HEAD
        // store the trace ...
        
    }

    private void expose(Scrapelet currentSnippet) {
        if ( this.currentSnippet != null )
           Exposer.exposeToDefaultStore( currentSnippet );
=======
>>>>>>> d06fcbf7ea46d68ceb8674210db0b4cea0d12c6b
    }

    
    
}
