/**
 * 
 * The network importer loads Gehphi files instead of node
 * and edge lists. 
 * 
 * An RDF model is also loaded. This model allows a continuous
 * extension of existing network profile data in an RDF graph. 
 * 
 * We apply the standard profiler to a given network file and collect all
 * results in this RDF model based on our default vocabulary.
 * 
 * 
 * EXTENSION:
 * 
 * - use a FUSEKI Server instead of local files
 * - dump the profile to a .metadata folder
 * - use plugable vocabularies and mappings
 * 
 **/
package org.etosha.tools.na;
  
import org.etosha.networks.profiler.MSTProfiler;
import org.etosha.networks.profiler.Profiler;
import org.etosha.networks.profiler.SNAProfiler;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import java.util.List;
import java.util.Vector;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DC;
import static org.apache.jena.vocabulary.DCTypes.Dataset;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.etosha.networks.CCLink;
import org.etosha.networks.CCNetNode;
import org.etosha.networks.LayerDescriptor;
import org.etosha.networks.Link;
import org.etosha.networks.LinkComparator;
import org.etosha.networks.rdf.EtoshaNetworkVocabulary;
import org.openide.util.Exceptions;

public class EtoshaReferenceProfiler {
    
 
       
  public static void main(String[] args) {
      
    String modelToLoad = "./data/out/JENA/model.trdf";  
    
    /**
     *   Where are reference datasets stored?
     *   
     *   You simply clone the "etosha-data-calibration".
     * 
     */  
    //String offset = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/generator";
    String offset = "/Users/kamir/GITHUB/etosha-data-calibration/GEPHI/gexf";
    File folder = new File( offset );
    
    String outputFolder = "./data/TEMP/";

    
    //--------------------------------------------------------------------------
    // Create a model and read into it from file 
    // "data.ttl" assumed to be Turtle.
    //
    model = RDFDataMgr.loadModel( modelToLoad ) ;
   
    //--------------------------------------------------------------------------
    // Iterate on files ... 
    //
    Vector<File> toProcess = new Vector<File>();
    File[] list = folder.listFiles();
   
    for( File f : list ) {
        // here we register all files which should be processed ...
        if ( f.getName().endsWith( ".gexf" ) ) {
        //if ( f.getName().endsWith( ".gephi" ) ) {
    
            toProcess.add(f);
        }
        System.out.println(">" + f.getAbsolutePath());
    }
 
    /***************************************************************************
     * 
     *   Some preparations for cluster mode.
     * 
     */
    SparkConf conf = new SparkConf().setAppName("Simple Graph Profiler (An Etosha Spark Application)");
    conf.setMaster("local");
    
    JavaSparkContext sc = new JavaSparkContext(conf);
    sc.addJar("/GITHUB/SparkNetworkCreator/target/SparkNetworkCreator-0.1.0-SNAPSHOT-jar-with-dependencies.jar");
    sc.addJar("/Users/kamir/NetBeansProjects/PlanarityTester-master/dist/lib/gephi-toolkit.jar" );
    sc.addJar("/Users/kamir/NetBeansProjects/PlanarityTester-master/dist/lib/a.jar" );
    
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

        File fileOut = new File( outputFolder + "_" + fn.getName() + "_" + TIME );

        
        // Data is in an "EXTERNAL representation" 
        
        // We do not IMPORT this networks.
        
        // The MST and SNA Profiler work locally. A cluster import is not
        // required by this profiler types.
        
        /***
         *   We profile the network for several link thresholds.
         * 
         *   This configures the edge-filter.
         * 
         **/
        double[] LEVELS = { 0.9 };
        
        for( double TS : LEVELS) {

            //------------------------------------------------------------------
            //
            // A JUNG2 based profiler.
            // 
            // For plotting the MST
            // 
            
            /***
             * 
             * This kind of MST works not with a weight, so it may not produce
             * repeatable results.
             */

//            Profiler pMST = MSTProfiler.profileLocaly(
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
            Profiler pSNA = SNAProfiler.profileGephiFileLocaly(
                    TS,  // filter threshold 
                    fn,  // input data file (usually GEXF)
                    fileOut.getAbsolutePath(),  // output location for profile
                    s );  // label
            
            // pSNA.storeImage( new File( outputFolder ), 5 );
         
            
            storeTriple( s, EtoshaNetworkVocabulary.TS, TS+"", "[New Threshold]\n" );
            storeTriple( s, EtoshaNetworkVocabulary.zEdges, pSNA.getNumberEdges() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.zNodes, pSNA.getNumberVertices() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.diameter, pSNA.getDiameter() + "", "     " );
            storeTriple( s, EtoshaNetworkVocabulary.globalClusterCoefficient, pSNA.getGlobalClusterCoefficient() + "", "     " );
            
//            System.out.println( "   #zClusters   =" + pSNA.getDiameter());
//            System.out.println( "   #sizeMaxCluster => (n=" + pSNA.getMaxCLusterNrNodes() + ",e=" + pSNA.getMaxCLusterNrEdges() + ")" );
        }
        
        
        try {

            FileOutputStream out1 = new FileOutputStream( "./data/out/JENA/model.json-ld" );
            model.write(out1, "JSON-LD");
            out1.close();

            FileOutputStream out2 = new FileOutputStream( "./data/out/JENA/model.ttl" );
            model.write(out2, "TTL");
            out2.close();

            
            FileOutputStream out = new FileOutputStream( modelToLoad );
            RDFDataMgr.write(out, model, Lang.RDFTHRIFT) ;
            out.close();

        } 
        catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    
    }
    
  }
  
  static Model model = ModelFactory.createDefaultModel() ;
  
  public static void storeTriple( String s, Property p, String o, String info ) {
        
        Resource r1 = model.createResource("http://semanpix.de/network#" + s);
        
        r1.addProperty( p, o );
        
        System.out.println( info + "=>(" + p + "=" + o + ")" );
  }
}

