/**
 * The network importer loads Gehphi files instead of node
 * and edge lists. 
 * 
 * An existing RDF model is also loaded. This model allows a continuous
 * extension of existing network profile data in an RDF graph. 
 * 
 * We apply the standard profiler to a given network file and collect all
 * results in this RDF model, using our default vocabulary.
 * 
 * @TODO 
 * 
 * EXTENSION:
 * 
 * - use a FUSEKI Server instead of local files
 * - dump the profile to a .metadata folder
 * - use plugable vocabularies and mappings
 * 
 **/
package org.etosha.mi;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.spark.api.java.JavaSparkContext;
import org.etosha.tools.profiler.EtoshaModelHandler;
import org.etosha.vocab.EtoshaNetworkVocabulary;
import org.openide.util.Exceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ModelInspector {

    
  public static void main(String[] args) throws Exception {
      
      ModelInspector prof = new ModelInspector();
      prof.run( new EtoshaModelHandler() );
      
  }

  public void run() throws Exception {

      run( new EtoshaModelHandler() );

  }

  public void run(EtoshaModelHandler handler) throws Exception {

    // this is the metadata model we contribute to ...
    String modelToLoad = handler.getStorageFiles()[0].getAbsolutePath() + ".ttl";  
    
    File fIn = new File( modelToLoad );
    System.out.println( "Meta-Model file       : " + fIn.getAbsolutePath() + " (r:" + fIn.canRead() + ")" );
    
    /**
     *   Where are input datasets stored?
     *   
     *   You simply clone the "etosha-data-calibration" project to that folder.
     * 
     */  
    //String BASE_FOLDER_GEXF = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/generator";
    //String BASE_FOLDER_GEXF = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/gexf";
    //String BASE_FOLDER_GEXF = "/TSBASE/networks";
    
    String BASE_FOLDER_DSM = EtoshaModelHandler.BASE_FOLDER_DSM;
    
    File folder = new File( BASE_FOLDER_DSM );
    System.out.println( "Model data folder     : " + folder.getAbsolutePath() + " (r:" + folder.canRead() + ")" );

    //--------------------------------------------------------------------------
    // Create a model and read into it from file 
    // "data.ttl" assumed to be Turtle.
    //
    try {
        if ( model == null )
            model = RDFDataMgr.loadModel( modelToLoad ) ;
        else {
            model = ModelFactory.createDefaultModel() ;
            System.out.println( ">> Use new model. "  );
            model = ModelFactory.createDefaultModel() ;
        }    
    }
    catch( Exception ex ) {
        System.out.println( ">> EX: " + ex.getMessage() );
        System.out.println( ">> Use new model. "  );
        model = ModelFactory.createDefaultModel() ;
        
    }
    //--------------------------------------------------------------------------
    // Iterate on files ... 
    //
    Vector<File> toProcess = new Vector<File>();
    
    /**
     * 
     * Create a file list ...
     * 
     */
    File[] list = folder.listFiles();

    System.out.println("### Available model folders in :" + folder.getAbsolutePath());

    int i = 0;
    for( File f : list ) {
        i++;
        // here we register all files which should be processed ...
        if ( f.isDirectory() ) {
        //if ( f.getName().endsWith( ".gephi" ) ) {
    
            toProcess.add(f);
        
        }
        
        System.out.println( i + " > " + f.getAbsolutePath());
    }
 
    /***************************************************************************
     *   Some preparations for cluster mode.
     */
//    SparkConf conf = new SparkConf().setAppName("Simple Graph Profiler (Etosha Spark Application)");
//    conf.setMaster("local");
//    
//    String fn1 = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-network-profiler/target/etosha-network-profiler-0.9.0-SNAPSHOT-jar-with-dependencies";
//    String fn2 = "/GITHUB/NetBeansProjects/PlanarityTester-master/dist/lib/gephi-toolkit.jar";
//    String fn3 = "/GITHUB/NetBeansProjects/PlanarityTester-master/dist/lib/a.jar";
//        
//    JavaSparkContext sc = new JavaSparkContext(conf);
//
//    checkAndAddJAR( fn1, sc );
//    checkAndAddJAR( fn2, sc );
//    checkAndAddJAR( fn3, sc );
    

 
    
    long TIME = System.currentTimeMillis();
    /**
     *   Now we can iterate on a set of network files.
     */
    for( File fn : toProcess ) {
        
        
        // the particular input
        System.out.println( ">>> " + fn.getAbsolutePath() + " => " + fn.exists() );

            String s = "MLLib-Model_" + fn.getName();

            storeTriple( s, EtoshaNetworkVocabulary.TS,"UNKNOWN TYPE", "[TYPE]\n" );
            

    } // END LOOP FILES
    
            try {

//            FileOutputStream out1 = new FileOutputStream(  handler.getStorageFiles()[0].getAbsolutePath() + ".json-ld" );
//            model.write(out1, "JSON-LD");
//            out1.close();

            FileOutputStream out2 = new FileOutputStream(   handler.getStorageFiles()[0].getAbsolutePath() + ".ttl" );
            model.write(out2, "TTL");
            out2.close();

            FileOutputStream out1 = new FileOutputStream(   handler.getStorageFiles()[1].getAbsolutePath() + ".ttl" );
            model.write(out1, "TTL");
            out1.close();

//            // trdf
//            FileOutputStream out = new FileOutputStream( modelToLoad );
//            RDFDataMgr.write(out, model, Lang.RDFTHRIFT) ;
//            out.close();

        } 
        catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    
  }
  
  static Model model = null;
  
  public static void storeTriple( String s, Property p, String o, String info ) {
        
        Resource r1 = model.createResource("http://semanpix.de/network#" + s);
        
        r1.addProperty( p, o );
        
        System.out.println( info + "=>(" + p + "=" + o + ")" );
  }

    private static void checkAndAddJAR(String fn1, JavaSparkContext sc) throws Exception {
        File f = new File( fn1 );
        
        if ( f.exists() )
                sc.addJar( fn1 ); 
        else 
            throw new Exception( fn1 + " ... is a required file for SC.");
    
    }
}

