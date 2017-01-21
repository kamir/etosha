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
package org.etosha.tools.profiler;
  
import org.etosha.tools.profiler.IGraphProfilerTool;
import org.etosha.tools.profiler.common.SNAProfiler;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import java.util.Vector;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.etosha.networks.LayerDescriptor;
import org.etosha.vocab.EtoshaNetworkVocabulary;
import org.openide.util.Exceptions;

public class EtoshaReferenceProfiler {
    
    
  public static void main(String[] args) throws Exception {
      
      EtoshaReferenceProfiler prof = new EtoshaReferenceProfiler();
      prof.run( new EtoshaModelHandler() );
      
  }  
  
       
  public void run(EtoshaModelHandler handler) throws Exception {
       
    String modelToLoad = handler.getStorageFiles()[0].getAbsolutePath() + ".ttl";  
    
    File fIn = new File( modelToLoad );
    System.out.println( "Model file            : " + fIn.getAbsolutePath() + " (r:" + fIn.canRead() + ")" );
    
    /**
     *   Where are input datasets stored?
     *   
     *   You simply clone the "etosha-data-calibration" project to that folder.
     * 
     */  
    //String BASE_FOLDER_GEXF = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/generator";
    //String BASE_FOLDER_GEXF = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/gexf";
    //String BASE_FOLDER_GEXF = "/TSBASE/networks";
    
    String BASE_FOLDER_GEXF = EtoshaModelHandler.BASE_FOLDER_GEXF;
    
    File folder = new File( BASE_FOLDER_GEXF );
    System.out.println( "Graph data folder     : " + folder.getAbsolutePath() + " (r:" + folder.canRead() + ")" );
    
    String PROFILE_OUTPUT_FOLDER = EtoshaModelHandler.PROFILE_OUTPUT_FOLDER;

    File outFolder = new File( BASE_FOLDER_GEXF );
    System.out.println( "PROFILE data folder   : " + outFolder.getAbsolutePath() + " (r:" + outFolder.canRead() + ")" );

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

    System.out.println("### Available Gephi files in :" + folder.getAbsolutePath());

    int i = 0;
    for( File f : list ) {
        i++;
        // here we register all files which should be processed ...
        if ( f.getName().endsWith( ".gexf" ) ) {
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
    
    
    /***************************************************************************
     * 
     * Define the graph metadata ...
     * 
     */
    LayerDescriptor descriptor = new LayerDescriptor();
    //
    // currently a descriptor for GEPHI files does not exist.
    //
    
 
    
    long TIME = System.currentTimeMillis();
    /**
     *   Now we can iterate on a set of network files.
     */
    for( File fn : toProcess ) {
        
        
        // the particular input
        System.out.println( ">>> " + fn.getAbsolutePath() + " => " + fn.exists() );

        File fileOut = new File( PROFILE_OUTPUT_FOLDER + "_" + fn.getName() + "_" + TIME );
        
        // Data is in an "EXTERNAL representation" 
        
        // We do not IMPORT this networks.
        
        // The MST and SNA IGraphProfilerTool work locally. A cluster import is not
        // required by this profiler types.
        
        /***
         *   We profile the network for several link thresholds.
         * 
         *   This configures the edge-filter.
         * 
         **/
        double[] LEVELS = { 1.9 };
        
        for( double TS : LEVELS) {

            //------------------------------------------------------------------
            //
            // A JUNG2 based profiler.
            // 
            // For plotting the MST
            // 
            
            
 
            
            
//            /***
//             * 
//             * This kind of MST works not with a weight, so it may not produce
//             * repeatable results.
//             */
//
//            IGraphProfilerTool pMST = MSTProfiler.profileLocaly(
//                    TS, 
//                    fileOut.getAbsolutePath(), 
//                    "MST_TS_LAYER=" + descriptor.multiLinkLayerSelector + "_TS=" + TS, 
//                    linksSORTED );
//            
//            pMST.storeImage( folderOut );
      

            //------------------------------------------------------------------
            //
            // A Gephi based profiler.
            // 
            // For diameter and cluster-coefficients 
            //
            String s = "SNA_GEPHI-File=" + fn.getName() + "_TS=" + TS;
            
            IGraphProfilerTool pSNA = SNAProfiler.profileGephiFileLocaly(
                    TS,  // filter threshold 
                    fn,  // input data file (usually GEXF)
                    fileOut.getAbsolutePath(),  // output location for profile
                    s );  // label
            
            pSNA.storeImage(new File( PROFILE_OUTPUT_FOLDER ), 5 );
            
            storeTriple( s, EtoshaNetworkVocabulary.TS, TS+"", "[New Threshold]\n" );
            storeTriple( s, EtoshaNetworkVocabulary.zEdges, pSNA.getNumberEdges() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.zNodes, pSNA.getNumberVertices() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.diameter, pSNA.getDiameter() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.globalClusterCoefficient, pSNA.getGlobalClusterCoefficient() + "", "     " );
            
            
            //##################################################################

            
//            MSTFrame f = new MSTFrame();
//            f.setGraph( pSNA.getGraph() );
            



            //##################################################################
            
            
            
            
            
            System.out.println( "   #zClusters   =" + pSNA.getDiameter());
            System.out.println( "   #sizeMaxCluster => (n=" + pSNA.getNrOfNodesInLargestCluster() + ",e=" + pSNA.getNrOfEdgesInLargestCluster() + ")" );
        }  // END LOOP LEVELS
        
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

