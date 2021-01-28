package org.etosha.gmail;

import java.io.File;

/**
 * Created by kamir on 29.08.17.
 */
public class ClientSecretFileManager {

    // Path to the client_secret.json file downloaded from the Developer Console
    private static String CLIENT_SECRET_PATH = "./etosha-contextualizer/client_secret_761864824458-tg536nkrif0rgitmo8ej2pmfbjsmh3os.apps.googleusercontent.com.json";


    public static String getPathName() {

        File f = new File(  CLIENT_SECRET_PATH );

        if ( f.canRead() && f.exists() ) {

            return  CLIENT_SECRET_PATH;

        }
        else {

            System.out.println( ">>> GmailCollector: Please provide a client-secret file in folder: " + f.getParent() );

            return null;
        }
    }
}
