package org.etosha.tools.profiler.usecase.wikipedia;
  
import org.etosha.networks.correlationnet.CCLink;
import org.etosha.networks.correlationnet.CCNetNode;
import org.etosha.tools.profiler.minimumspanningtree.MSTProfiler;
import org.etosha.tools.profiler.IGraphProfilerTool;
import org.etosha.tools.profiler.common.SNAProfiler;
import java.io.File; 
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import java.util.List;
import java.util.Vector;
import org.etosha.networks.LayerDescriptor;
import org.etosha.networks.Link;
import org.etosha.networks.LinkComparator;

public class NetworkProfilerCC {
    
 
       
  public static void main(String[] args) {
    
    /**
     *   Where is our data from previous calculations stored?
     */  
    String offset = "/Volumes/Macintosh HD/Users/kamir/tempData/CN_9_9_CN.accessrate_de___Illuminati_(Buch)_BIN=24_log10_WikiPaper_und_BA_2012_c_merged_MONTHLY/FINAL.CC.INFOFLOW";
    
    //
    // which time frame are we working with?
    //
    Vector<String> toProcess = new Vector<String>();
    int id = 0;
    while( id < 12 ) {
        String f1 = offset.concat( ".false.10-001." + id + ".20000.0.9_CN.accessrate_de___Illuminati_(Buch)_BIN=24_log10.json");
        File f = new File( f1 );
        // here we register all files which should be processed ...
        toProcess.add(f1);
        id++;
    }
 
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
     */
    LayerDescriptor descriptor = new LayerDescriptor();
    int LAYER = 0;
    descriptor.setLinkComparator(new LinkComparator( LAYER ));
    descriptor.setLinkType(CCLink.class);
    descriptor.setNodeType(CCNetNode.class);
    descriptor.setMultiLinkLayerSelector(LAYER);
    
    /**
     * 
     *   We iterate on a set of result files.
     */
    for( String file : toProcess ) {
    
        // the particular input
        File fn = new File( file );
        System.out.println( ">>> " + fn.getAbsolutePath() + " => " + fn.exists() );

        // a simple text-based RDD to load link properties from
        JavaRDD<String> linkData = sc.textFile(fn.getAbsolutePath()).cache();

        /**
         * Here we load the particular link strengths.
         * 
         */
        JavaRDD<CCLink> links = linkData.map( line -> new CCLink( line ) );

        // convert for generic profiler ...
        JavaRDD<Link> linksToProfile = links.map((CCLink l) -> (Link)l);

        // sort the values based on links on one particular layer ...
        JavaRDD<Link> sortedLinks = linksToProfile.sortBy( (Link l) -> l.getWeight(LAYER), false, 1);

        
        long time = System.currentTimeMillis();
        
        // Dump link-list for external processing 
        String label = descriptor.getLinkType().getName();
        String fnOut = "/TSBASE/profiles/" + label + "-net-SORTED-" + fn.getName() + "_" + time;
        
        File fileOut = new File(fnOut );
        
        if ( !fileOut.getParentFile().exists() )
            fileOut.getParentFile().mkdirs();
        
        //File folderOut = new File( fileOut.getParentFile() + "/" + CCLink.getLinkTypeLabel() + "-" + time );
        File folderOut = new File( fileOut.getAbsolutePath() + "/" + CCLink.getLinkTypeLabel() + "-" + time );
        sortedLinks.saveAsTextFile( fnOut );
        
        // BRING links to the DRIVER and operate locally ...
        List<Link> linksALL = linksToProfile.collect();
        List<Link> linksSORTED = sortedLinks.collect();
        
        /***
         *   We profile the network for several link thresholds.
         **/
        double[] LEVELS = { 5 };
        
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

//           IProfilerr pMST = MSTProfiler.profileLocaly(
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
          IGraphProfilerTool pSNA = SNAProfiler.profileLocaly(
                    TS, 
                    fileOut.getAbsolutePath(), 
                    "SNA_TS_LAYER=" + descriptor.getMultiLinkLayerSelector() + "_TS=" + TS, 
                    linksALL );
            
            pSNA.storeImage( folderOut, 20 );
         
            System.out.println( "TS=" +  TS );
            System.out.println( "   #e=" + pSNA.getNumberEdges() );
            System.out.println( "   #n=" + pSNA.getNumberVertices());
            System.out.println( "   #diameter    =" + pSNA.getDiameter());
            System.out.println( "   #clustering  =" + pSNA.getGlobalClusterCoefficient());
            
//            System.out.println( "   #zClusters   =" + pSNA.getDiameter());
//            System.out.println( "   #sizeMaxCluster => (n=" + pSNA.getNrOfNodesInLargestCluster() + ",e=" + pSNA.getNrOfEdgesInLargestCluster() + ")" );
//     
        }
    
    }
    
  }
}

