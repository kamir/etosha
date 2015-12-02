package contextualizer.tools;


/**
 * We can run FUSEKI in single mode on any cluster node.
 *
 * This tool exposes a TTL file via Fuseki.
 *
 * The TripleStoreRunner uses the generated data to expose a TTL file via
 * FUSEKI.
 *
 * Whenever a DEFAULT-JENA-IN-RAM contextualizer is used, you can simply expose
 * its data via FUSEKI.
 *
 * Maybe it is also good to create an additional copy in this case, otherwise
 * you may loose data by running the next test.
 *
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mirko Kämpf
 */
public class TripleStoreRunner {
    
    public static int availablePort = 0; 
    
    public static String FUSEKI_SCRIPT = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-docworld/DISTRIBUTE/DocWorld.PARCEL/src/ETCS-1.0/scripts/control.sh";
    public static String FUSEKI_CMD = "start";

    public static int FUSEKI_PORT = 3070;
    public static String FUSEKI_DATA = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-contextualizer/test.ttl";

    public static boolean running = false;

    
    public static TripleStoreRunnable myRunnable = null;

    public static void runTS() {

        myRunnable = new TripleStoreRunnable();

        theFusekiThread = new Thread(myRunnable);

        theFusekiThread.start();

    }
    
    static Thread theFusekiThread = null;

    public static void main(String[] ARGS) throws IOException, InterruptedException {

        System.out.println("*** ATTEMPT *** TripleStoreRunner.runTS() ...");

        TripleStoreRunner.runTS();
        
        
        /**
         * Here we have to wait a bit, to see if the service is ready, but 
         * we can easily ignore this issue currently.
         */
        if (TripleStoreRunner.running) {
            System.out.println(">>> Triple Store is RUNNING in a new Thread.");
        } else {
            System.out.println("!!! Triple Store is NOT WORKING.");
        }
        
        System.out.println( "+++WARNING+++ FUSEKI-ADMIN-COMMAND POST	/$/server/shutdown	Not yet implemented" );
        
        // JUST KEEP the program running as long as the Widget is visible ...
        String nameOfNewDump = javax.swing.JOptionPane.showInputDialog("READY to CLOSE THE FUSEKI SERVER.");
        myRunnable.stop();
        
    }

}

class TripleStoreRunnable implements Runnable {

    public void stop() {

        // SHUTDOWN the store and dump the latest content to disc.

        process.destroy();
        

    }
    
    public void run() {

        try {

            runFusekiSingleMode();

        } catch (IOException ex) {

            TripleStoreRunner.running = false;

            Logger.getLogger(TripleStoreRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    Process process = null;

    public void runFusekiSingleMode() throws IOException {

        int port = getAvailablePort(TripleStoreRunner.FUSEKI_PORT);
        
        boolean hadToUnlock = doUnlock(TripleStoreRunner.FUSEKI_SCRIPT);

        ProcessBuilder pb = new ProcessBuilder(
                TripleStoreRunner.FUSEKI_SCRIPT,
                TripleStoreRunner.FUSEKI_CMD,
                TripleStoreRunner.FUSEKI_DATA,
                ""+port
        );

        File script = new File(TripleStoreRunner.FUSEKI_SCRIPT);
        pb.directory(script.getParentFile());

        process = pb.start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.println("[TripleStoreRunner.FUSEKI_SCRIPT] " + TripleStoreRunner.FUSEKI_SCRIPT);
        System.out.println("[TripleStoreRunner.FUSEKI_CMD   ] " + TripleStoreRunner.FUSEKI_CMD);
        System.out.println("[TripleStoreRunner.FUSEKI_DATA  ] " + TripleStoreRunner.FUSEKI_DATA);
        System.out.println("[TripleStoreRunner.FUSEKI_PORT  ] " + TripleStoreRunner.FUSEKI_PORT + " => " + port);

        TripleStoreRunner.running = true;

        System.out.println(">>> FUSEKI WAS started on port : " + port);

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }


    }

    int maxPortAttempts = 10;
    int usedPortAttempts = 0;

    
    private int getAvailablePort(int fp) {

        System.out.println("[Q:] IS PORT " + fp + " AVAILABLE?");

        if (available(fp)) {
            TripleStoreRunner.availablePort = fp;
            System.out.println("[A:] Yeah! The PORT " + TripleStoreRunner.availablePort + " is AVAILABLE!");
            return fp;
        } 
        else {
            usedPortAttempts++;
            if (usedPortAttempts < maxPortAttempts) {
                fp = fp + 1;
                getAvailablePort(fp);
            }
        }
        return TripleStoreRunner.availablePort;
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    int MIN_PORT_NUMBER = 3030;
    int MAX_PORT_NUMBER = 3090;
    
    public boolean available(int port) {
        
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    private boolean doUnlock(String SCRIPT) {
        
        File f = new File( SCRIPT );
        File folder = f.getParentFile();
        
        File lockFile = new File( folder.getAbsolutePath() + "/run/system/tdb.lock" );
       
        if ( lockFile.exists() ) {
            
             lockFile.delete();
             return true;
        }
        
        return false;
        
    }
}
