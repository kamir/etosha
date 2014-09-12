/**
 * 
 * The HDFS Metadata Importer Package works with Etosha.indexlib.
 * 
 * Fist you select and prepare an index from Etosha.libs
 * Then you configer your data collectors
 *    a) Flume, observes a directory, such as an SMB or NFS share
 *    b) CLI tool runs on a particular local directory
 *    c) CLI tool runs on a particular HDFS directory
 *    d) CLI tool imports from a particular Email box
 *
 *    ((e)) Nutch imports a website
 */
package org.apache.etosha.hdfsindexer;

import java.io.File;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kamir
 */
public class HDFSMetadataImport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        HDFSMetadataLoader.init();
        
        String mode = ReadCMD.read("Local / HDFS mode (l,r)", "l" );
        
        String topic = ReadCMD.read("What folder has to be imported?", "/Users/kamir/Documents/Cloudera/github" );
        File f = new File( topic ); 
        
        HDFSMetadataLoader.importHDFSMetadataForFolder( topic, mode );
        
    }
    
}
