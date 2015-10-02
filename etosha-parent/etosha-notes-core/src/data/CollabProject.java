/**
 * 
 * 
 * 
 */
package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;
import tools.SVNTool;
import tools.gnuplot.GnuplotCompilerToolAdapter;
import tools.latex.TexCompilerToolAdapter;

/**
 *
 * @author kamir
 */
public class CollabProject implements Serializable {

    public static void initProps(Properties props, CollabProject project) {
        props.put("project.basePath", project.basePath ); 
        props.put("project.name", project.name ); 
        props.put("project.useGIT", project.useGIT ); 
        props.put("project.useSVN", project.useSVN ); 
        
        
    }

   
    public boolean useSVN = false;
    public boolean useGIT = false;
    
    public String name = null;
    public String basePath = null;
    String sessionId = null;

    public String REFINDER_URL;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
   
    public Hashtable<String,GoogleDocument> googleDocuments = null;
    public Hashtable<String,TemplateFile> templates = null;
    
    public CollabProject() { 
        
        useGIT = CFG.useGIT;
        useSVN = CFG.useSVN;
        REFINDER_URL = CFG.refinderURL;
        
        basePath = CFG.defaultBasePath; // aus initialisierung gelesen ...
        
        templates = new Hashtable<String,TemplateFile>();
        googleDocuments = new Hashtable<String,GoogleDocument>();
        
        templates.put( "ELEMENT_CHART_LATEX", 
                       new TemplateFile( TemplateFile.default_LATEX_FILE ) );
        templates.put( "ELEMENT_CHART_GNUPLOT", 
                       new TemplateFile( TemplateFile.default_GNUPLOT_FILE ) );
        
        googleDocuments.put( "WP", new GoogleDocument() );        
    };
    

    public void createTemplates() throws IOException { 
        File f1 = getElementChartLatexFile();
        File f2 = getElementChartGnuplotFile();
    };
    
    /**
     * Das File   /chart.cmd  ist ein Gnuplot-Template im Projekt.
     * 
     * @return File
     * 
     * @throws IOException 
     */
    public File getElementChartLatexFile() throws IOException { 
        File f = new File( this.basePath + name + "/imageLS.tex" );
        System.out.println(">>> use tex-File: " + f.getAbsolutePath() );
        if ( !f.exists() ) { 
            TemplateFile tf = templates.get( "ELEMENT_CHART_LATEX" );
            writeToFile(f, tf.content );
            SVNTool.addFile(f, this);
            SVNTool.commit(this, "autocommit");
        }
        return f;
    }
    
    /**
     * Das File   /chart.tex   ist ein Template im Projekt.
     * 
     * @return File
     * 
     * @throws IOException 
     */
    public File getElementChartGnuplotFile() throws IOException { 
        File f = new File( this.basePath + name + "/chart.cmd" );
        if ( !f.exists() ) { 
            TemplateFile tf = templates.get( "ELEMENT_CHART_GNUPLOT" );
            writeToFile(f, tf.content );
            SVNTool.addFile(f, this);
            SVNTool.commit(this, "autocommit");
            System.out.println(">>> created CMD-File: " + f.getAbsolutePath() );
        }
        else {
            System.out.println(">>> reuse CMD-File: " + f.getAbsolutePath() );        
        }
        return f;
    }
    
    public void writeToFile( File f, String data ) throws IOException { 
        FileWriter fw = new FileWriter(f);
        fw.write( data );
        fw.close();
    }
    
    public void storeLatexFile( String data ) throws IOException { 
        SVNTool.commit(this, "autosave-latex");
        writeToFile( this.getElementChartLatexFile(), data);
    }
    
    public void storeGnuplotFile( String data ) throws IOException { 
        SVNTool.commit(this, "autosave-gnuplot");
        writeToFile( this.getElementChartGnuplotFile(), data);
    }
    
    

    public File getBaseFolder() {
        File f = new File( basePath + name );
        System.out.println( ">>> nutze nun: " + f.getAbsolutePath() + " als BaseFolder." );
        return f;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    public static CollabProject getDefaultProjekt() {
        CollabProject p = new CollabProject();
        p.name = "P0" + File.separator + "default";
        p.basePath = CFG.defaultBasePath;
        return p;
    };
   
    public static void main( String[] args ) throws IOException { 
        CollabProject p = getDefaultProjekt();
        p.buildDocumentSRC();
        p.compileSinglePage();
    };
    
    public static void reCompile( CollabProject p ) throws IOException { 
        p.buildDocumentSRCWebApp();
        p.compileSinglePage();
    };

    public static void reCreateGnuplotChart( CollabProject projekt ) throws IOException { 
         System.out.println(">>> ERZEUGE Gnuplot-CHARTS neu ... ");
         File f = new File( projekt.basePath + projekt.name + "/chart.cmd" );
         GnuplotCompilerToolAdapter.compileTexFile(f, projekt);
    };

    public void createFolder() { 
        File f = new File( basePath + name );
        if ( f.exists() ) { 
            System.out.println(">>> init.jsp  -> Folder: " + f.getAbsolutePath() + " is there already ..."  );
        }
        else { 
            f.mkdirs();
            System.out.println(">>> init.jsp  -> CREATED Folder: " + f.getAbsolutePath()  );
        }
    }
    


    private void compileSinglePage() {
        File f = getSingleDocumentFile();
        TexCompilerToolAdapter.compileTexFile(f, this);
    }

    private void buildDocumentSRC() throws IOException {
        // mittels Velocity wird hier ein SRC-File erzeugt ...
        SimplePageDocument.doPrepareSingleDocument( this ); 
    }
    
    public String loadTextFromFile( File fn ) { 
        String latexData = "";
        try {
            BufferedReader br = new BufferedReader( new FileReader( fn.getAbsolutePath() ) );
            while( br.ready() ) { 
                String line = br.readLine();
                latexData = latexData.concat(line + "\n" );
            }
            br.close();
        }
        catch( Exception ex) { 
            latexData.concat( ex.getMessage() );
        }
        return latexData;
    }

    public File getSingleDocumentFile() {
        File fn = new File( getSingleDocumentFileName( this.basePath ) + ".tex" );
        return fn;
    }

    public String getBasePath() {
        return basePath;
    }
    
    public String getSingleDocumentFileName( String path ) { 
        System.out.println(">>> Name: " + name );
        String append = "";
        if( !name.endsWith("/") ) append = "/";
        String fn = path + this.name + append  + "SinglePage";
        
        return fn;
    }
    
    void writeSingleDocumentSRC(String s) throws IOException {
        File fn = getSingleDocumentFile();
        FileWriter fw = new FileWriter( fn.getAbsolutePath() );
        fw.write( s );
        fw.flush();
        fw.close();
    }

    private void buildDocumentSRCWebApp() throws IOException {
        System.out.println(">>> Velocity ... ");
            // mittels Velocity wird hier ein SRC-File erzeugt ...
        SimplePageDocument.doPrepareSingleDocumentWEBAPP( this ); 
    }
    
    public GoogleDocument getGoogleDoc(String key ) { 
        return googleDocuments.get(key);
    };


    public Hashtable<String, GoogleDocument> getGoogleDocuments() {
        return googleDocuments;
    }

    public void setGoogleDocuments(Hashtable<String, GoogleDocument> googleDocuments) {
        this.googleDocuments = googleDocuments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hashtable<String, TemplateFile> getTemplates() {
        return templates;
    }

    public void setTemplates(Hashtable<String, TemplateFile> templates) {
        this.templates = templates;
    }
    
    
}
