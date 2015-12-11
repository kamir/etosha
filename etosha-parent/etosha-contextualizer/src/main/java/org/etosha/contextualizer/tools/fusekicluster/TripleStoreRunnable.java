/*
 * Copyright 2015 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.etosha.contextualizer.tools.fusekicluster;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TripleStoreRunnable implements Runnable {
    
    FUSEKIHostControlerPanel panel = null;

    TripleStoreRunnable(FUSEKIHostControlerPanel p) {
        panel = p;
        
        
    }

    TripleStoreRunnable() {
    }

    public void stop() {

        // SHUTDOWN the store and dump the latest content to disc.
        process.destroy();

        
        if ( panel != null )
            panel.appendLogLine( "> FUSEKI Triple Store was stoped." );
        else
            System.out.println("> FUSEKI Triple Store was stoped.");
            
       

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
    int port = -1;
    
    public void runFusekiSingleMode() throws IOException {

        if ( panel != null ) 
            port = panel.getPort();
        else 
            port = TripleStoreRunner.FUSEKI_MIN_PORT;

        port = getAvailablePort(port);

        boolean hadToUnlock = doUnlock(TripleStoreRunner.FUSEKI_SCRIPT);

        ProcessBuilder pb = new ProcessBuilder(
                TripleStoreRunner.FUSEKI_SCRIPT,
                TripleStoreRunner.FUSEKI_CMD,
                TripleStoreRunner.FUSEKI_DATA,
                "" + port
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
        System.out.println("[TripleStoreRunner.FUSEKI_PORT  ] " + TripleStoreRunner.FUSEKI_MIN_PORT + " => " + port);

        TripleStoreRunner.running = true;

        System.out.println(">>> FUSEKI WAS started on port : " + port);
        
        if ( panel != null )
            panel.setUsedPort( port );

        System.out.println("[WEB-UI] " + getWebUI());

        while ((line = br.readLine()) != null) {
            System.out.println(line);
            if ( panel != null )
                panel.appendLogLine( line );
        }

    }
    
    public void openWebUI() throws IOException, URISyntaxException {
          if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI( getWebUI() ));
        }

    }
    
    public String getWebUI() {
        return "http://localhost:" + port;
    }

    int maxPortAttempts = 10;
    int usedPortAttempts = 0;

    private int getAvailablePort(int fp) {

        System.out.println("[Q:] IS PORT " + fp + " AVAILABLE?");

        if (available(fp)) {
            TripleStoreRunner.availablePort = fp;
            System.out.println("[A:] Yeah! The PORT " + TripleStoreRunner.availablePort + " is AVAILABLE!");
            return fp;
        } else {
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
    int MIN_PORT_NUMBER = 5050;
    int MAX_PORT_NUMBER = 5090;

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

        File f = new File(SCRIPT);
        File folder = f.getParentFile();

        File lockFile = new File(folder.getAbsolutePath() + "/run/system/tdb.lock");

        if (lockFile.exists()) {

            lockFile.delete();
            return true;
        }

        return false;

    }

}

