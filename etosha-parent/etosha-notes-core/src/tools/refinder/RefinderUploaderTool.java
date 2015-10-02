/*
 *  Ein neues PDF wird manuell von der Hauptseite aus hochgeladen.
 * 
 */
package tools.refinder;

import data.CollabProject;

/**
 *
 * @author kamir
 */
public class RefinderUploaderTool {
    private static boolean isInitialized;
    
    public static void uploadPDF_to_Refinder( CollabProject projekt, String fileName ) { 
        // init()
        if ( !isInitialized ) { 
            init( projekt );
        }
        
        System.out.println(">>> lade das PDF: " + fileName + " zum Refinder ... ");
        
        
    };

    private static void init( CollabProject projekt) {
        // Refinder API besorgen ...
        
        String URL = projekt.REFINDER_URL;
        
        
    }
}
