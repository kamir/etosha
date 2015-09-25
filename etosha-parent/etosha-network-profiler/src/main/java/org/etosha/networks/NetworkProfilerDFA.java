/**
 * 
 * The simple network creator loads link properties and defines a network layer
 * according to specific functional aspects, represented by correlation
 * or similarity measures, which represent the link properties.
 *
 **/
package org.etosha.networks;
  
import org.etosha.networks.profiler.MSTProfiler;
import org.etosha.networks.profiler.Profiler;
import org.etosha.networks.profiler.SNAProfiler;
import java.io.File; 
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import java.util.List;

public class NetworkProfilerDFA {
    
//       static public String[] dfaResultsTEST = { 
//                        "b10"
//                       };  
       
       static public String[] dfaResults = { 
//                        "b10", 
//                        "b2",
                        "b9",  
//                        "c10",  
//                        "c2", 
//                        "c9"
       };  

       
  public static void main(String[] args) {
    
    /**
     *   Where is our data from previous calculations stored?
     */  
    String ext = ".csv";  
    String offset = "./data/in/DFA/";
  
    /**
     *   Some preparations for cluster mode.
     */
    SparkConf conf = new SparkConf().setAppName("Simple Graph Creator (A Spark Application)");
    conf.setMaster("local");
    
    JavaSparkContext sc = new JavaSparkContext(conf);
    sc.addJar("/GITHUB/SparkNetworkCreator/target/SparkNetworkCreator-0.1.0-SNAPSHOT-jar-with-dependencies.jar");
    sc.addJar("/Users/kamir/NetBeansProjects/PlanarityTester-master/dist/lib/gephi-toolkit.jar" );
    sc.addJar("/Users/kamir/NetBeansProjects/PlanarityTester-master/dist/lib/a.jar" );
   
    /**
     * Define the graph metadata ...
     * 
     */
    LayerDescriptor descriptor = new LayerDescriptor();
    int LAYER = 2;
    descriptor.linkComparator = new LinkComparator( LAYER );
    descriptor.linkType = DFALink.class;
    descriptor.nodeType = DFANetNode.class;
    descriptor.multiLinkLayerSelector = LAYER;
    
    /**
     * 
     *   We iterate on a set of result files.
     */
    for( String file : dfaResults ) {
    
        // the particular input
        File fn = new File( offset + file + ext );
        
        // a simple text-based RDD to load node properties from
        JavaRDD<String> dfaData = sc.textFile(fn.getAbsolutePath()).cache();

        // Because we want to handle all pairs of nodes, two Node RDDs are created.
        // split the string into ID and double[]
        JavaRDD<DFANetNode> nodesA = dfaData.map((String s) -> new DFANetNode(s)); 
        JavaRDD<DFANetNode> nodesB = dfaData.map((String s) -> new DFANetNode(s)); 

        // now we have all node pairs ...
        JavaPairRDD<DFANetNode,DFANetNode> pairs = nodesB.cartesian(nodesA);

        /**
         * Here we calculate the particular link strength
         * 
         * A DFANetNode contains the correlation coefficient-alpha 
         * for three scales.
         * 
         */
        JavaRDD<DFALink> links = pairs.map( (Tuple2<DFANetNode,DFANetNode> p) -> new DFALink(p) );
        
        // sort the values based on links on one particular scale ...
        JavaRDD<DFALink> sortedLinks = links.sortBy( (DFALink l) -> l.w[LAYER], false, 1);

        // convert for generic profiler ...
        JavaRDD<Link> linksToProfile = links.map((DFALink l) -> (Link)l);
        
        long time = System.currentTimeMillis();
        
        // Dump link-list for external processing 
        String label = descriptor.linkType.getName();
        String fnOut = "/GITHUB/SparkNetworkCreator/data/out/" + label + "-net-SORTED-" + fn.getName() + "_" + time;
        
        File fileOut = new File(fnOut );
        //File folderOut = new File( fileOut.getParentFile() + "/" + DFALink.getLinkTypeLabel() + "-" + time );
        File folderOut = new File( fileOut.getAbsolutePath() + "/" + DFALink.getLinkTypeLabel() + "-" + time );
        sortedLinks.saveAsTextFile( fnOut );
        
        // BRING links to the DRIVER and operate locally ...
        List<Link> linksALL = linksToProfile.collect();
        
        /***
         *   We profile the network for several link thresholds.
         **/
        double[] LEVELS = { 0.4, 0.7 };
        
        for( double TS : LEVELS) {

            //------------------------------------------------------------------
            //
            // A JUNG2 based profiler.
            // 
            // For plotting the MST
            // 
//            Profiler pMST = MSTProfiler.profileLocaly(
//                    TS, 
//                    fileOut.getAbsolutePath(), 
//                    "MST_TS_LAYER=" + descriptor.multiLinkLayerSelector + "_TS=" + TS, 
//                    linksALL );
//            
//            pMST.storeImage( folderOut, 20 );
      

            //------------------------------------------------------------------
            //
            // A Gephi based profiler.
            // 
            // For diameter and cluster-coefficients 
            //
            Profiler pSNA = SNAProfiler.profileLocaly(
                    TS, 
                    fileOut.getAbsolutePath(), 
                    "SNA_TS_LAYER=" + descriptor.multiLinkLayerSelector + "_TS=" + TS, 
                    linksALL );
         
            System.out.println( "TS=" +  TS );
            System.out.println( "   #e=" + pSNA.getNumberEdges() );
            System.out.println( "   #n=" + pSNA.getNumberVertices());
            System.out.println( "   #diameter    =" + pSNA.getDiameter());
            System.out.println( "   #clustering  =" + pSNA.getGlobalClusterCoefficient());

            //            pSNA.storeImage( folderOut, 20 );

            
//            System.out.println( "   #zClusters   =" + pSNA.getDiameter());
//            System.out.println( "   #sizeMaxCluster => (n=" + pSNA.getMaxCLusterNrNodes() + ",e=" + pSNA.getMaxCLusterNrEdges() + ")" );
//     
        }
    
    }
    
  }
}
