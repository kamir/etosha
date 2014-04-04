package org.etosha.tools.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.core.context.SemanticUserContext;
import org.etosha.hdgs.hive.tools.HiveTableInspector;
import org.etosha.smc.connector.SemanticContextBridge;
import org.etosha.smc.connector.external.GMailLoader;
import org.etosha.smc.connector.internal.ScreenSnappLoader;
import org.etosha.tools.gui.NoteTool;
import org.etosha.tools.statistics.DataSetInspector;

public class ContextLoggerTool extends Configured implements Tool {

    public SemanticContextBridge scb = null;
    String pn = null;

    public void initConnector() throws UnknownHostException {

        scb = new SemanticContextBridge(this.getConf());

        scb.login();

        System.out.println("***init() # done! ***\n");

    }

    /**
     * @param args
     * @throws IOException
     * @throws LoginException
     */
    public int run(String[] args) throws IOException, LoginException {

        // String user = "training";
        // File f = new File("/home/" + user + "/etc");
        
        String user = "kamir";
        File f = new File("/Users/" + user + "/etc");

        
        int status = 0;
        String cmd = null;

        if (args != null) {
            if (args.length > 0) {
                cmd = args[1];
            } 
            else {
                cmd = " --- no input --- ";
            }
        }

        System.out.println(">>> Etosha Context Logging (v 0.2.20) \n[" + cmd + "]");

        if (cmd.equals("list") || cmd.equals(" --- no input --- ")) {
            list();

            System.exit(0);
        }

        if (cmd.equals("init")) {
            System.out.println(">>> new configuration will be created in ...");

            if (!f.exists()) {
                f.mkdirs();
            }
            
            f = new File( f.getAbsolutePath() + "/smw-site.xml");

            FileWriter fw = new FileWriter(f);
            fw.write(getContentDefaultCFG());
            fw.close();
            System.out.println(">>> new configuration is created in: "
                    + f.getAbsolutePath());
            System.exit(0);
        }

        if (cmd.equals("cfg")) {

            System.out.println(">>> your current etosha-core configuration : ");

            f = new File( f.getAbsolutePath() + "/smw-site.xml");

            if (!f.exists()) {
                f.mkdirs();

                System.out.println(">>> new configuration will be created in ...");
            
                FileWriter fw = new FileWriter(f);
                fw.write(getContentDefaultCFG());
                fw.close();
                System.out.println(">>> new configuration is created in: "
                        + f.getAbsolutePath());

                System.out.println(getContentDefaultCFG());

            } 
            else {
                System.out.println(">>> location : " + f.getAbsolutePath());
                System.out.println(getLocalCFG(f));
            }
            System.exit(0);

        }

        initConnector();

        if (cmd.equals("inspectHiveMetastore")) {

            HiveTableInspector.cfg = cfg;
            HiveTableInspector.clt = this;

            try {

                HiveTableInspector.run(args);
            } catch (SQLException ex) {
                Logger.getLogger(ContextLoggerTool.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.exit(0);
        }

        if (cmd.equals("mail")) {

            GMailLoader.cfg = cfg;
            GMailLoader.clt = this;

            GMailLoader.run(args);

            System.exit(0);
        }

        if (cmd.equals("snap")) {

            ScreenSnappLoader.cfg = cfg;
            ScreenSnappLoader.clt = this;

            ScreenSnappLoader.run(args);

            System.exit(0);
        }

        if (cmd.equals("new")) {
            System.out.println("> create new context ... ");
            System.exit(0);
        }

        if (cmd.equals("log")) {
            System.out.println("> log current context ... ");

            SemanticUserContext uc = new SemanticUserContext();

            String text = uc.getUserContextBashHistory();
            text = text.concat(uc.getUserContextCategories());

            scb._logBashHistoryToPage(text);

            System.out.println("***userContextLogging() # done! ***\n");
            System.exit(0);
        }

        if (cmd.equals("note")) {
            System.out.println("> add a note to the current context ... ");

            SemanticUserContext uc = new SemanticUserContext();

            String text = NoteTool.getNote();

            scb._logNoteToPage(text);

            System.out.println("***userContextLogging() # done! ***\n");
            System.exit(0);
        }

        if (cmd.equals("notel")) {
            System.out.println("> add a note to the current context ... ");

            SemanticUserContext uc = new SemanticUserContext();

            String text = NoteTool.getNote();

            scb._logNoteLinkToPage(text);

            System.out.println("***userContextLogging() # done! ***\n");
            System.exit(0);
        }

        if (cmd.equals("inspectSEQ")) {

            Path p = new Path(args[1]);
            System.out.println("\n\n>>>> inspect SEQ file ... " + p.toString());
            SEQFileInspector.inspectFile(cfg, p);

            System.exit(0);
        }

        if (cmd.equals("inspectAVRO")) {

            Path p = new Path(args[1]);
            System.out.println("\n\n>>>> inspect AVRO file ... " + p.toString());
            AVROFileInspector.inspectFile(cfg, p);

            System.exit(0);
        }

        if (cmd.equals("put")) {
            System.out.println("> attache a file to current context ... ");

            SemanticUserContext uc = new SemanticUserContext();

            String fn = args[1];

            try {
                scb.logFileToHistoryToPage(fn);
                System.out.println("***userContextLogging() # done! ***\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("***userContextLogging() # failed! ***\n");
            }
            System.exit(0);
        }

        return status;

    }

    private String getContentDefaultCFG() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(
                ContextLoggerTool.class.getResourceAsStream("smw-site.xml")));

        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            String line = br.readLine();
            // System.out.println(line);
            sb.append(line + "\n");
        }

        return sb.toString();
    }

    private String getLocalCFG(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            String line = br.readLine();
            // System.out.println(line);
            sb.append(line + "\n");
        }

        return sb.toString();
    }

    Configuration cfg = null;

    static ContextLoggerTool clt = null;

    public static void main(String[] args) throws Exception {

        ContextLoggerTool clt = new ContextLoggerTool();
        clt.cfg = new Configuration();

        File cfgFile = new File("/home/training/etc/smw-site.xml");
        if (cfgFile.exists()) {
            /**
             * according to:
             *
             * http://stackoverflow.com/questions/11478036/hadoop-configuration-
             * property-returns-null
             *
             * we add the resource as a URI
             */
            clt.cfg.addResource(cfgFile.getAbsoluteFile().toURI().toURL());
        }
        int exitCode = ToolRunner.run(clt.cfg, clt, args);

        System.exit(exitCode);

    }

    public void list() {
        System.out.println("  list  : show the help");
        System.out.println("  snap  : take a screenshot ...");
        System.out.println("  new   : define a new context (role the .bash_history)");
        System.out.println("  log   : store the current .bash_history");
        System.out.println("  put   : embed a local file to the context");
        System.out.println("  note  : add a note to the current context");
        System.out.println("  notel : link a note to the current context");
        System.out.println("  init  : create a new configuration file in $user/etc/smw-site.xml");
        System.out.println("  mail  : load annotated mails from default mail box (see cfg)");
        System.out.println("* chat  : load annotated chat threads from default chat client (Skype)");
        System.out.println("  cfg   : show the client configuration, stored in $user/etc/smw-site.xml");

        System.out.println("\n  inspectSEQ      : inspect a SEQUENCE file and add meta-data to data set context");
        System.out.println("  inspectAVRO     : inspect an AVRO file and add meta-data to data set context");
        System.out.println("\n* inspectRC       : inspect an RC file and add meta-data to data set context");
        System.out.println("* inspectPARQUET  : inspect a PARQUET file and add meta-data to data set context");

        System.out.println("\n  inspectHiveMetastore      : inspect the Hive-Metastore and collect notes about the tables");

    }

}
