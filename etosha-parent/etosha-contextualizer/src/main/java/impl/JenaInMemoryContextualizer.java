package impl;

 
import contextualizer.IContextualizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Just for demos and simple tests we experiment with an in Memory
 * contextualizer, which works stateless. All data is collected during runtime.
 * Therefore we use it in the Spark-Shell;
 *
 * @author training
 *
 */
public class JenaInMemoryContextualizer implements IContextualizer {
    
    static Model model = ModelFactory.createDefaultModel() ;

    public void storeTriple( String s, Property p, String o, String info ) {
        
        Resource r1 = model.createResource(s);
        
        r1.addProperty( p, o );
        
        System.out.println( info + "=>(" + p + "=" + o + ")" );
    }
    
    public static void storeTriple( String base, String s, Property p, String o, String info ) {
        
        Resource r1 = model.createResource(base + s);
        
        r1.addProperty( p, o );
        
        System.out.println( info + "=>(" + p + "=" + o + ")" );
    }

    @Override
    public void open() {
        try {
        
            model = ModelFactory.createDefaultModel();
            
            if ( f.exists() )
                model.read( new FileInputStream( f ), base);
        
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(JenaInMemoryContextualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        FileOutputStream out2;
        try {
            
            out2 = new FileOutputStream( f );
            model.write(out2, "TTL");
            out2.close();
            
            System.out.println( "MODEL is now in file: " + f.getAbsolutePath() );
            
        
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(JenaInMemoryContextualizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JenaInMemoryContextualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void putNSPO(String n, String s, String p, String o) {
		// TODO Auto-generated method stub

    }

    @Override
    public void putSPO(String s, Property p, String o) {
        
        storeTriple( s, p, o, "JENA_in_RAM_CONTEXTUALIZER");
	

    }

    @Override
    public void init() {
		// TODO Auto-generated method stub

    }

 
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    String base = null;
    File f = null;
    public void setDefaultFilename(String s, String b) {
        f = new File( s );
        base = b;
        System.out.println(">>> Load JENA model from: " +  f.getAbsolutePath() +  " (file exists: " + f.exists() + ")");
    }

    public void setDefaultFilename(String s) {
        setDefaultFilename( s, "BASE" );
    }

}
