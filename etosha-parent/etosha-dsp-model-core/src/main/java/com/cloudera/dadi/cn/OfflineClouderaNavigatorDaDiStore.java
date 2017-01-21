package com.cloudera.dadi.cn;

import com.cloudera.dadi.DaDiStore;
import com.cloudera.navigator.client.MetadataSink;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * The Offline Cloudera Manager DaDiStore writes the ".navigator" files
 * into HDFS, instead of using the CN-API.
 * 
 * If HDFS is not available, a local folder is used to simulate HDFS.
 * 
 * This allows testing and debugging.
 * 
 * @author kamir
 */
public class OfflineClouderaNavigatorDaDiStore implements DaDiStore {

    File file = null;
    public static PrintWriter pw = null;

    @Override
    public boolean init() throws Exception { 
        
        // access HDFS
        
        // use the local folder 
        File f = new File( MetadataSink.BASEFOLDER_TO_SIMULATE_HDFS + "/_access_test" );
        boolean canCreate = f.createNewFile();
        
        System.out.println( ">>> DICTIONARY StorePath: " + f.getAbsolutePath() + " => " + f.canWrite() );
        if( f.canWrite() ) {
            file = f;
            pw = new PrintWriter( new BufferedWriter( new FileWriter( f )));

            return true;
        }
        else return false;
        
    }

    @Override
    public boolean close() {
        try{
            pw.flush();
            pw.close();
            return true;
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void storeEntityWithContext(Object o) {
        
        CNViewHandler view = new CNViewHandler( o );
        view.persist();
        System.out.print( "> store in CN Handler: " + view.getInfo() );

        CNOfflineViewHandler view2 = new CNOfflineViewHandler( o );
        view2.persist();
        System.out.print( "> store in CNOfflineViewHandler: " + view2.getInfo() );

        
    }

    
    
}
