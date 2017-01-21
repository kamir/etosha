package com.cloudera.navigator.client;
//

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author kamir
 */
public class MetadataSink {

    public static final String BASEFOLDER_TO_SIMULATE_HDFS = "./data/HDFS_sim"; 
    public static final String BASEFOLDER_MODEL_REPO = "REPO"; 
    public static final String BASEFOLDER_STAGE = "STAGE"; 
    public static final String BASEFOLDER_RESULTS = "RESULTS"; 

    public static void putMDO(ObjectMetadata omd) {

        // Needs some real formatting ...
        Gson gson = new GsonBuilder().create();
        gson.toJson(omd.props, System.out);
        gson.toJson(omd.tags, System.out);
         
    }

    public static void putMDOLocal(ObjectMetadata omd, PrintWriter s) {
        
        // Needs real formatting for external storage ...
        Gson gson = new GsonBuilder().create();
        gson.toJson(omd.props, s);
        gson.toJson(omd.tags, s);

    }

    enum OpMode {
        ONLINE,
        OFFLINE
    }
    
    public static OpMode mode = MetadataSink.OpMode.OFFLINE;
    
    /**
     * Get the identity field from CN for a given entity.
     * 
     * @param id
     * @return 
     */
    public static ObjectMetadata getMDOForHDFSFolderByName(String id) {
        
        String response = "NOTHING THERE ... ";
        
        try {

            switch ( mode ) {
                case ONLINE : {
                    String phrase = "originalName:" + id + " AND type:HDFS";
                    response = callCN_for_id(phrase);
                    break;
                }
                
                case OFFLINE : {
                    response = readHDFSFolder_for_id(id);
                    break;
                }
                   
            }
            
            return new ObjectMetadata(response);

        } 
        catch (IOException ex) {
            Logger.getLogger(MetadataSink.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Add some more properties to the store ...
     * @return 
     */
//    public static ObjectMetadata putPropertiesAndTagsForIdToUnifiedStore() {
//        try {
//            
//            String response = callCN_for_id("*:*");
//           
////        Gson gson = new GsonBuilder().create();
////        gson.toJson("Hello", System.out);
////        gson.toJson(123, System.out);
//            
//            return new ObjectMetadata(response);
//            
//        } catch (IOException ex) {
//            Logger.getLogger(MetadataSink.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }

    /**
     * 
     * If no CN is available (in offline tests) or if passive mode is used by a client
     * with no connection to CN, we access files in HDFS. These files are picked up 
     * by CN automatically.
     * 
     * @param id
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static String readHDFSFolder_for_id(String id) throws FileNotFoundException, IOException {
        
        System.out.print( ">>> Load MD for (id: " + id + ") from ");
        
        File folder = new File( BASEFOLDER_TO_SIMULATE_HDFS + "/" + BASEFOLDER_MODEL_REPO + "/" + id );
        
        File fnav   = new File( BASEFOLDER_TO_SIMULATE_HDFS + "/" + BASEFOLDER_MODEL_REPO + "/" + id + "/" + ".navigator" );
        
        System.out.println( fnav.getAbsolutePath() + " (exists: " + fnav.exists() + ")" );
        
        String json = "";
        
        if ( fnav.canRead() ) {

            BufferedReader br = new BufferedReader( new FileReader( fnav ) );
            StringBuffer sb = new StringBuffer();

            while( br.ready() ) {
                sb.append( br.readLine() );
            }
            br.close();

            json = sb.toString();

        }
        return json;
        
    }
    
    /**
     * Docu: https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientAuthentication.java
     * 
     * For a given search phrase we search for the MD object and take
     * the first ID we can get. Because multiple IDs can be provided,
     * we list them. But by definition, the first one is given back.
     * 
     * @param phrase
     * @return
     * @throws IOException 
     */
    private static String callCN_for_id(String phrase) throws UnsupportedEncodingException, IOException  {
        
        // FROM HERE, we will use the Cloudera Navigator SDK to write 
        // into the CN API directly. 
        // 
        //     https://github.com/cloudera/navigator-sdk/tree/v2.0
        //
        
        String user = "admin";
        String pwd = "PWD";
        String host = "172.24.128.52";
        String port = "7187";

        String identity = "???";
        
        /**
         *
         * CURL EXAMPLE:
         *
         * curl http://172.24.128.52:7187/api/v8/entities/?query=*:*sketch2* -u
         *
         * USER:PWD -X GET
         */
        String URL = "http://" + host + ":" + port + "/api/v8/entities/?query=" + URLEncoder.encode(phrase, "UTF-8");;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, Integer.parseInt( port ) ),
                new UsernamePasswordCredentials(user, pwd));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        try {
            HttpGet httpget = new HttpGet( URL );

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println("########################################");

                String json = EntityUtils.toString(response.getEntity());

                // System.out.println(json);
                
                // ------------ extract identity -----------------
                // http://goessner.net/articles/JsonPath/
                // 
        
                // System.out.println( json );
                List<String> ids = JsonPath.read(json, "$.[*].identity");

                for( String idx : ids ) {
                     System.out.println( idx );
                }
                
                identity = ids.get(0);
                
            } 
            finally {
                response.close();
            }
        } 
        finally {
            httpclient.close();
        }
        
        return identity;
   }

}
