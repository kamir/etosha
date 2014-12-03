package org.etosha.cmd;

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
import org.etosha.cmd.admintools.AVROFileInspector;
import org.etosha.cmd.admintools.SEQFileInspector;
import org.etosha.core.context.SemanticUserContext;
import org.etosha.tools.hivemetastore.HiveTableInspector;
import org.etosha.core.sc.connector.SemanticContextBridge;
import org.etosha.core.sc.connector.external.GMailLoader;
import org.etosha.core.sc.connector.internal.ScreenSnappLoader;
import org.etosha.tools.gui.NoteTool;

/**
 * This is the tool to interact with the Etosha Context Proxy (ECP).
 * 
 * Either a local ECP or the configured remote proxy is used.
 * The first proxy implementation is the Semantic Media Wiki.
 * 
 * @author kamir
 */

public class EtoshaContextLogger extends Configured implements Tool {

    /**
     * The Semantic Context Bridge (SCB) connects an electronic device
     * with an ECP.
     * 
     * One user login is related to dynamic evolving context. This context
     * is maintained in the context proxy and allows connecting the contexts
     * of many users.
     * 
     */
    public SemanticContextBridge scb = null;
    
    /**
     * The pagename to work with.
     */
    private String pn = null;
    public static boolean debug = false;

    /**
     * We have to connect to the ECP using the SCB implementation.
     * 
     * @throws UnknownHostException 
     */
    public void initConnector() throws UnknownHostException {
        scb = new SemanticContextBridge(this.getConf());
        scb.login();
        if ( debug )
            System.out.println("*** EtoshaContextLogger.init()  ***\n# done!");
    }

    /**
     * 
     * We use the Hadoop Tool Runner ...
     * 
     * @param args
     * 
     * @throws IOException
     * @throws LoginException
     */
    public int run(String[] args) throws IOException, LoginException {

        Configuration cfg = getConf();
        
        File f = getCFGFile();

        int status = 0;
        String cmd = null;

        if (args != null) {
            if (args.length > 0) {
                cmd = args[1];
            } 
            else {
                cmd = "list";
            }
        }

        System.out.println(">>> Etosha Context Logging (v 0.3.31) \ncmd: [" + cmd + "]");

        if (cmd.equals("list") ) {
            list();
            System.exit(0);
        }

        if (cmd.equals("init")) {
            System.out.println(">>> new configuration will be created in ...");

            if (!f.exists()) {
                f.getParentFile().mkdirs();
            }
            
            f = new File( f.getAbsolutePath() );

            FileWriter fw = new FileWriter(f);
            
            fw.write(getContentDefaultCFG());
            
            fw.close();
            
            System.out.println(">>> A new Etosha configuration was created in: "
                    + f.getAbsolutePath());
            System.exit(0);
        }

        if (cmd.equals("cfg")) {

            System.out.println(">>> Your current Etosha configuration is in: ");

            f = new File( f.getAbsolutePath() );

            if (!f.exists()) {
                f.getParentFile().mkdirs();

                System.out.println(">>> New configuration will be created in ...");
            
                FileWriter fw = new FileWriter(f);
                fw.write(getContentDefaultCFG());
                fw.close();
                
                System.out.println(">>> New configuration was created in: "
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
        
        if (cmd.equals("importImage")) {
            
            String fn = args[1];
            
            File f2 = new File( fn );
            
            System.out.println("> import an image to the current context ... ");
            scb.createTheNewPage( "ImagePage#" + f2.getName());
            scb.logImageToPage( "ImagePage#" + f2.getName() , f2, fn);

            System.out.println("***userContextLogging() # done! ***\n");
            System.exit(0);
            
        }


        if (cmd.equals("inspectHiveMetastore")) {

            HiveTableInspector.cfg = getConf();
            HiveTableInspector.clt = this;

            try {
                HiveTableInspector.run(args);
            } 
            catch (SQLException ex) {
                Logger.getLogger(EtoshaContextLogger.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.exit(0);
        }

        if (cmd.equals("mail")) {

            GMailLoader.cfg = getConf();
            GMailLoader.clt = this;

            GMailLoader.run(args);

            System.exit(0);
        }

        if (cmd.equals("snap")) {

            ScreenSnappLoader.cfg = getConf();
            ScreenSnappLoader.clt = this;

            ScreenSnappLoader.run(args);
            
            exit( args );

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
                EtoshaContextLogger.class.getResourceAsStream("smw-site.xml")));

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

    static public EtoshaContextLogger clt = null;

    public static void main(String[] args) throws Exception {

        EtoshaContextLogger clt = new EtoshaContextLogger();
        clt.setConf( new Configuration() );

        File cfgFile = clt.getCFGFile();
                
        if (cfgFile.exists()) {
            /**
             * according to:
             *
             * http://stackoverflow.com/questions/11478036/hadoop-configuration-
             * property-returns-null
             *
             * we add the resource as a URI
             */
            clt.getConf().addResource(cfgFile.getAbsoluteFile().toURI().toURL());
        }
        
        int exitCode = ToolRunner.run(clt.getConf(), clt, args);

        System.exit(exitCode);
    }

    public void list() {
        System.out.println("  list  : show the help");
        System.out.println("  snap  : take a screenshot ...");
        System.out.println("  new   : define a new context (role the .bash_history)");
        System.out.println("  log   : store the current .bash_history");
        System.out.println("  ping  : check connectivity to curent context");
        System.out.println("  put   : embed a local file to the context");
        System.out.println("  note  : add a note to the current context");
        System.out.println("  notel : link a note to the current context");
        System.out.println("  init  : create a new configuration file in $user/etc/smw-site.xml");
        System.out.println("  mail  : load annotated mails from default mail box (see cfg)");
        System.out.println("* chat  : load annotated chat threads from default chat client (Skype)");
        System.out.println("  cfg   : show the client configuration, stored in $user/etc/smw-site.xml");

        System.out.println("  importImage   : import an image and extract metadata from it ");
        
        
        System.out.println("\n  inspectSEQ      : inspect a SEQUENCE file and add meta-data to data set context");
        System.out.println("  inspectAVRO     : inspect an AVRO file and add meta-data to data set context");
        System.out.println("* inspectPARQUET  : inspect a PARQUET file and add meta-data to data set context");

        System.out.println("\n  profileSEQ      : inspect a SEQUENCE file and add meta-data to data set context");
        System.out.println("  profileAVRO     : inspect an AVRO file and add meta-data to data set context");
        System.out.println("  profilePARQUET  : inspect a PARQUET file and add meta-data to data set context");

        
        System.out.println("\n  inspectHiveMetastore      : inspect the Hive-Metastore and collect notes about the tables");

    }

            
    /**
     * only exit from JVM if on CMD-line mode ...
     * 
     * @param args 
     */
    private void exit(String[] args) {
        if( args[0].equals( this.getClass().getName() ) ) {
            System.exit( 0 );
        }
    }

    private File getCFGFile() {
       
        String userHome = System.getProperty("userhome");
        File f = new File(userHome + "/etc/etosha/smw-site.xml");
        
        return f;
    }

}
