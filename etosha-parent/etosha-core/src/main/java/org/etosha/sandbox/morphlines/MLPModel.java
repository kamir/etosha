/**
 * Model for Morphline-projects
 *
 * Such a project has a:
 *
 * - morphline file - solr schema - flume config - test dataset - an avro schema
 * (optionally)
 */
package org.etosha.sandbox.morphlines;

import com.cloudera.cdk.morphline.api.MorphlineContext;
import com.cloudera.cdk.morphline.api.Record;
import com.cloudera.cdk.morphline.base.Fields;
import com.cloudera.cdk.morphline.base.Notifications;
import com.cloudera.cdk.morphline.base.Compiler;
import com.cloudera.cdk.morphline.api.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.solr.util.SimplePostTool.stringToStream;

/**
 * @author kamir
 */
public class MLPModel {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        // some testdata recors are defined
        Vector<String> messages = new Vector<String>();
        messages.add("S1 P1 O1 D1");
        messages.add("S2 P2 O2 D2");

        // the model instance is used to handle all configuration
        MLPModel model = new MLPModel();

        model.setTestData(messages);
        try {

            model.morphlineConfiguration = model.getTemplateConfig();

            model.dumpMorphlineCFG();

            model.runFullMorphlineTest(System.out);

        } catch (Exception ex) {
            Logger.getLogger(MLPModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * A default morphline configuration is bundled.
     *
     * @return
     */
    private String getTemplateConfig() {
        InputStream ins = getClass().getResourceAsStream("ml.conf");
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        StringBuffer sb = new StringBuffer();
        try {
            while (br.ready()) {
                sb.append(br.readLine() + "\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "NULL";
        }
    }

    /**
     *
     * A default morphline configuration is bundled.
     *
     * @return
     */
    private void dumpMorphlineCFG() {
        System.out.println(this.morphlineConfiguration);
    }

    public void runFullMorphlineTest(String hocon, PrintStream out) throws Exception {
        this.morphlineConfiguration = hocon;
        runFullMorphlineTest(out);
    }

    public void runFullMorphlineTest(String hocon) throws Exception {
        this.morphlineConfiguration = hocon;
        runFullMorphlineTest(System.out);
    }

    public void runFullMorphlineTest(PrintStream out) throws Exception {

        File folder = new File(".");
        File configFile = File.createTempFile("morphline-test-", ".conf", folder);
        FileWriter fw = new FileWriter(configFile);
        fw.write(this.morphlineConfiguration);
        fw.flush();
        fw.close();

        MorphlineContext context = new MorphlineContext.Builder().build();

        Command morphline = new Compiler().compile(configFile, null, context, null);

        // process each input record form the testdata file
        Notifications.notifyBeginTransaction(morphline);

        if (testdata != null) {

            int i = 0;

            for (String m : testdata) {

                Record record = new Record();
                record.put(Fields.ATTACHMENT_BODY, stringToStream(m));
                boolean status = morphline.process(record);

                out.println("\n[" + i + "] successfully tested record <String>: (" + m + ") : " + status);
                i++;
            }
        } else {

            if (testdataBytes != null) {

                int i = 0;

                for (byte[] m : testdataBytes) {

                    Record record = new Record();
                    record.put(Fields.ATTACHMENT_BODY, m);
                    boolean status = morphline.process(record);

                    out.println("\n[" + i + "] successfully tested record <byte[" + m.length + "]>: (" + m + ") : " + status);
                    i++;

                }

            }

        }

        Notifications.notifyCommitTransaction(morphline);

    }

    public Vector<String> testdata;
    public Vector<byte[]> testdataBytes;

    public String morphlineConfiguration;
    public String flumeConfiguration;
    public String avroSchema;
    public String solrSchema;

    public void setTestData(Vector<String> messages) {
        testdata = messages;
    }

    public void setTestDataBytes(Vector<byte[]> messages) {
        testdataBytes = messages;
    }

}
