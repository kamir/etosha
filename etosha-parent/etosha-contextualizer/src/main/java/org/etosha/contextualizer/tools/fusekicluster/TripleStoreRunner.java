package org.etosha.contextualizer.tools.fusekicluster;

/**
 * We use this tool to run a single FUSEKI-Server, in standalone or
 * simple scale out mode, for high throughput SPARQL queries. 
 * 
 * We offer an "embedded" single node FUSEKI service by running a CSD in CM.
 * 
 * The cluster mode is based on a YARN application, which starts multiple 
 * FUSEKI instances, a loadbalancer, and offers automatic scale out if query
 * number is to high.
 * 
 * This scale-out cluster is managed by a FUSEKI MASTER service.
 *
 * This tool exposes a set of TTL files via Fuseki. A graph can be prepared
 * as a set of partitions, each partition stored in an TTL file. The FUSEKI
 * server load the ttl files from a local folder.
 * 
 * Distribution of the the graph is done via HDFS. As soon as a FUSEKI node
 * takes over the control on such a partition it provides METADATA in ZOOKEEPER.
 * 
 * This way, zookeeper knows which partition is available on which SERVER AND 
 * PORT. Partitions can be seen as named graphs, even if the graph name is not
 * explicitely part of the ttl file. This way we automatically enrich all 
 * triples in a partition by a thorth triple, which is only known on the 
 * meta-layer (in the master and in the zookeeper).
 * 
 * We can run multiple local graph partitions on on physical / virtual host.
 * ZOOKEEPER holds the partition registry.
 *
 * A TripleStoreRunner uses generated data to expose it via SPARQL End-Point.
 *
 * Creation of such graphs is done via Contextualizer tools. E.g., the 
 * DEFAULT-JENA-IN-RAM contextualizer is used to triplify groups of emails. 
 * 
 * Flume will use the GRAPH-PARTITIONER-SINK to write ttl files into HDFS.
 */
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Mirko Kämpf
 */
public class TripleStoreRunner {

    public static int availablePort = 4099;

    public static String FUSEKI_SCRIPT = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-docworld/DISTRIBUTE/DocWorld.PARCEL/src/ETCS-1.0/scripts/control.sh";
    public static String FUSEKI_CMD = "start";

    public static int FUSEKI_MIN_PORT = 5051;

    /**
     * Must be an absolute path on the local disc of the runnging Fuseki-Node
     */
    public static String FUSEKI_DATA_FOLDER = "/GITHUB/ETOSHA.WS/etosha-workbench/fuseki-buffer";
    public static String FUSEKI_DATA = "/GITHUB/ETOSHA.WS/etosha-workbench/fuseki-buffer/default.ttl";

    public static boolean running = false;

    public static TripleStoreRunnable myRunnable = null;

    public static void runTS() {

        myRunnable = new TripleStoreRunnable();

        theFusekiThread = new Thread(myRunnable);

        theFusekiThread.start();

    }

    public static void stopTS() {

        if (myRunnable != null) {
            myRunnable.stop();
        }

    }

    static Thread theFusekiThread = null;

    public static void main(String[] ARGS) throws IOException, InterruptedException {

        System.out.println("*** ATTEMPT *** TripleStoreRunner.runTS() ...");

        TripleStoreRunner.runTS();

        /**
         * Here we have to wait a bit, to see if the service is ready, but we
         * can easily ignore this issue currently.
         */
        if (TripleStoreRunner.running) {
            System.out.println(">>> Triple Store is RUNNING in a new Thread.");
        } else {
            System.out.println("!!! Triple Store is NOT WORKING.");
        }

        System.out.println("+++WARNING+++ FUSEKI-ADMIN-COMMAND POST	/$/server/shutdown	Not yet implemented");

        // JUST KEEP the program running as long as the Widget is visible ...
        String nameOfNewDump = javax.swing.JOptionPane.showInputDialog("READY to CLOSE THE FUSEKI SERVER.");

        myRunnable.stop();

    }

    public static void openUI() throws IOException, URISyntaxException {
     
        myRunnable.openWebUI();

    }
    


    public static TripleStoreRunnable runControlledFUSEKIService(FUSEKIHostControlerPanel p) {
        
        TripleStoreRunnable r = new TripleStoreRunnable( p );

        return r;
    }


}

