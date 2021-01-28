package org.etosha.contextualizer.impl;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.log4j.Logger;
import org.etosha.contextualizer.IContextualizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;

/**
 * We collect all facts in HBase first. From here we do background preprocessing.
 *
 * This method decouples data ingestion and data transformations.
 * We do not relay on transactions.
 * We reduce complexity.
 *
 * Alternatively, Kafka topics can be used to collect the facts.
 * HBase is prefered because we can simply run an embedded HBase service in Docker.
 *
 *
 *
 *
 * Some helper functions for managing the HBaseCluster.
 *
 */
public class HBaseContextualizer implements IContextualizer {


    static Model model = ModelFactory.createDefaultModel() ;


    public static String TABLE_NAME = "etosha_facts";
    public static String COLFAM_NAME = "etosha_stage";

    public static String HABSE_HOST = "127.0.0.1";
    public static String HBASE_REST_PORT = "8080";

    Logger logger = Logger.getLogger( HBaseContextualizer.class.getName() );

    private static String getHBaseREST_API_String_PUT() {
        String hbase_REST_API_PUT = "http://" + HABSE_HOST + ":" + HBASE_REST_PORT + "/" + TABLE_NAME + "/fakerow";
        return hbase_REST_API_PUT;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void putNSPO(String n, String s, String p, String o) {







    }

    /**
     * Put facts into table.
     *
     *

     RK=$(openssl enc -base64 <<< $1)
     COLFAM_COL=$(openssl enc -base64 <<< $2)
     VALUE=$(openssl enc -base64 <<< $3)

     echo "RK:   $1 : " $RK
     echo "COL:  $2 : " $COLFAM_COL
     echo "VALUE $3 : " $VALUE

     echo "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><CellSet><Row key=\"$RK\"><Cell column=\"$COLFAM_COL\">$VALUE</Cell></Row></CellSet>"

     curl -vi -X PUT \
     -H "Accept: text/xml" \
     -H "Content-Type: text/xml" \
     -d "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><CellSet><Row key=\"$RK\"><Cell column=\"$COLFAM_COL\">$VALUE</Cell></Row></CellSet>" \
     "http://$REST_SERVER:20550/$TABLE/fakerow"

     */
    @Override
    public void putSPO(String s, Property p, String o) {

        String url = getHBaseREST_API_String_PUT();

        String RK = Base64.getEncoder().encodeToString( s.getBytes() );
        String COLFAM_COL = Base64.getEncoder().encodeToString( (COLFAM_NAME + ":" + p.toString() ).getBytes() );
        String VALUE = Base64.getEncoder().encodeToString( o.getBytes() );

        String content = String.format( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><CellSet><Row key=\"%s\"><Cell column=\"%s\">%s</Cell></Row></CellSet>", RK, COLFAM_COL, VALUE );

        int responseCode = -1;
        HttpClient httpClient = new DefaultHttpClient();
        try {


            HttpPut request = new HttpPut( url );

            StringEntity params =new StringEntity(content,"UTF-8");

            params.setContentType("text/xml");
            request.addHeader("content-type", "text/xml");
            request.addHeader("Accept", "text/xml");
            request.addHeader("Accept-Encoding", "UTF-8");

            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

                String output;

                System.out.println("Output from HBase Rest-Server ...." + response.getStatusLine().getStatusCode() + "\n");

                while ((output = br.readLine()) != null) {
                    // System.out.println(output);
                }

            }
            else{

                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

        }
        catch (Exception ex) {

            logger.error("exeption sendPut : " + ex);
            logger.error("url              : " + url);
            logger.error("data             : \n" + content);

        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }

        // return responseCode;

    }

    @Override
    public void init() {

        // spindocker: run your own HBase
        //
        //    https://github.com/dajobe/hbase-docker
        runLoaclHBaseOnDocker();

        // table creation procedure
        createHBaseTable();

    }

    private void runLoaclHBaseOnDocker() {

        String DATA_DIR = "/GITHUB/ETOSHA.WS/etosha-workbench/docker-hbase/data";

        System.out.println( ">>> https://github.com/dajobe/hbase-docker" );

        System.out.println( ">>> please execute this docker command to start a single hbase server : " );

        String docker_cmd = "docker pull dajobe/hbase\n" +
                            "id=$(docker run --name=hbase-docker -p 8080:8080 -h hbase-docker -d -v " + DATA_DIR + ":/data dajobe/hbase)\n";

        System.out.println( ">>> " +docker_cmd );

    }

    private static void createHBaseTable() {

        System.out.println( ">>> please execute this command in hbase shell: " );

        System.out.println( ">>> create '" + TABLE_NAME + "', { NAME => 'etosha_stage', REPLICATION_SCOPE => 1 }\n " );

    };

    @Override
    public String getName() {
        String hbase_REST_API = "http://" + HABSE_HOST + ":" + HBASE_REST_PORT + "/" + TABLE_NAME + "/schema";

        return hbase_REST_API + " {COLFAM: " + COLFAM_NAME  + "}";
    }

    @Override
    public void addGraph(Model m) {

    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Model getNewPartition(String distGraphFolder, String label, String name) {
        return null;
    }

    @Override
    public void persistPartition() {

    }
}
