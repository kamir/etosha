/**
 * 
 * This tool exposes the TTL file via Fuseki.
 * 
 * The TripleStoreRunner uses the generated data to expose a TTL file
 * via FUSEKI.
 * 
 * Whenever a DEFAULT-JENA-IN-RAM contextualizer is used, you can simply 
 * expose its data via FUSEKI.
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamir
 */
public class TripleStoreRunner {

    public static String FUSEKI_SCRIPT = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-docworld/DISTRIBUTE/DocWorld.PARCEL/src/ETCS-1.0/scripts/control.sh";
    public static String FUSEKI_CMD = "start";

    public static String FUSEKI_DATA = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-contextualizer/test.ttl";

    public static boolean running = false;

    public static void runTS() {

        TripleStoreRunnable myRunnable = new TripleStoreRunnable();

        Thread myThread = new Thread(myRunnable);

        myThread.start();

    }

    public static void main(String[] ARGS) throws IOException, InterruptedException {

        System.out.println("> TRY TO RUN TS ...");
        TripleStoreRunner.runTS();
        System.out.println("> TS is RUNNING in a new Thread.");

        // JUST KEEP the program running as long as the Widget is visible ...
        javax.swing.JOptionPane.showInputDialog("CLOSE");

    }

}

class TripleStoreRunnable implements Runnable {

    public void run() {

        try {
            runFuseki();
        } catch (IOException ex) {
            Logger.getLogger(TripleStoreRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TripleStoreRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void runFuseki() throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(
                TripleStoreRunner.FUSEKI_SCRIPT, TripleStoreRunner.FUSEKI_CMD, TripleStoreRunner.FUSEKI_DATA);

        File script = new File(TripleStoreRunner.FUSEKI_SCRIPT);
        pb.directory(script.getParentFile());

        Process process = pb.start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.println(">>> FUSEKI is running ... ");

        TripleStoreRunner.running = true;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }
}
