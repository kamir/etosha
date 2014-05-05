package org.etosha.smc.connector;

import io.wiki.WikiTableBuilder;
import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Date;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.etosha.core.context.SemanticDataSetContext;
import org.etosha.core.context.SemanticJobContext;
import org.etosha.core.context.SemanticUserContext;
import org.etosha.smc.connector.external.Wiki;

public class SemanticContextBridge {

    public static boolean overWriteEnvForLocaltest = true;
    // will be loaded from core-site.xml file ...
    String user = "";
    String pw = "";
    String url = "";
    public String project = "Etosha Process";
    public String clusterID = "CDH4.2.VM.adht";
    public String localEnv = "???";
    public String localUser = "default";
    public String localHostAdress = "127.0.0.1";
    public String localHostName = "localhost";
    public SemanticJobContext jCont = new SemanticJobContext();
    public SemanticUserContext uCont = new SemanticUserContext();
    public SemanticDataSetContext dsCont = new SemanticDataSetContext();

    public SemanticContextBridge(Configuration conf) throws UnknownHostException {

        localUser = System.getProperty("user.name");

        overWriteEnvForLocaltest = conf.getBoolean("smw.mode.local", false);

//        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
//        localHostName = localMachine.getHostName();
//        localHostAdress = localMachine.getHostName();
//        System.out.println("Hostname of local machine: " + localMachine.getHostName());

        String hostName = "127.0.0.1";
        
        System.out.println("Hostname of local machine: " + hostName );

        System.out.println("***Semantic Context Bridge Logger init() ***");

        conf.reloadConfiguration();

        System.out.println(conf);

        url = conf.getRaw("smw.url");
        pw = conf.get("smw.pw");
        user = conf.get("smw.user");   // for SMW account
        localEnv = conf.get("smw.env");

        /**
         * we overwrite the user for testing...
		 *
         */
        if (overWriteEnvForLocaltest) {

            localUser = "training"; // instead of training like in all VMs
            url = "127.0.0.1/wiki";

        }

        System.out.println("smw.env=" + localEnv);
        System.out.println("smw.url=" + url);
        System.out.println("smw.user=" + user);
        System.out.println("smw.pw=" + pw);



        w = getWiki();

    }
    Wiki w = null;

    public Wiki getWiki() {
        Wiki wiki = new Wiki(url);
        wiki.setUsingCompressedRequests(false);
        return wiki;
    }

    public void login() {
        try {
            w.login(user, pw.toCharArray());
        } catch (FailedLoginException e) {
            System.err.println("***SCB.init() ###ERROR###");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("***SCB.init() ###ERROR###");
            e.printStackTrace();

        }
    }

    /**
     * creates a new page in Semantik-Wiki if there is not such a page and
     * calculates the pagename which is used later in the procedure of logging
     * data.
     *
     * @param localUser2
     * @param localHostName2
     * @param string
     * @return
     * @throws IOException
     * @throws LoginException
     */
    public String createTheNewPage(
            String drivername) throws LoginException, IOException {

        String nPN = "PROJECT_" + project + "_BY_" + localUser + "_ON_" + localHostName + "_WITH.JOB.DRIVER_" + drivername;

        String[] pages = new String[1];
        pages[0] = nPN;

        boolean[] flag = w.exists(pages);

        if (flag[0]) {
        } else {
            w.edit(nPN, jCont.getJobCategories(project, localUser, localHostName, drivername, clusterID, localEnv) + "Semantic Job Log Page (created with v0.1 of SemanticContextJob @" + getTimeStamp() + " )", "Initialisation done.");
            System.out.println("***pageCreation() # done! ***\n");
        }

        return nPN;
    }

    public String _createTheNewUCPage(String uc) throws LoginException, IOException {

        String nPN = "USERCONTEXT_" + uc;

        String[] pages = new String[1];
        pages[0] = nPN;

        boolean[] flag = w.exists(pages);

        if (flag[0]) {
        } else {
            w.edit(nPN, "Semantic User Context Log Page (created with v0.1 of SemanticContextBridge @" + getTimeStamp() + ")", "Initialisation done.");

            System.out.println("***pageCreation() # done! ***\n");
        }

        return nPN;
    }

    public static String getTimeStamp() {
        Date d = new Date(System.currentTimeMillis());
        return d.toLocaleString();
    }

    /**
     * A special log to a JobPage.
     *
     * @param title
     * @param summary
     * @param log
     * @throws IOException
     * @throws LoginException
     */
    public void logToJobPage(String summary, String log)
            throws IOException, LoginException {

        String title = jCont.jobPn;

        System.out.println(">>> " + title);

        String cont = w.getPageText(title);

        String date = (new Date(System.currentTimeMillis())).toString();
        cont = cont.concat("===" + date + "===\n");
        cont = cont.concat("some stuff was done ... " + log + "\n\n");
        cont = cont.concat("a little more stuff was done ... \n\n");

        w.edit(title, cont, summary);

        System.out.println("***semantiLog() # done! ***\n");


    }

    /**
     * This log can be done for all types of pages.
     *
     * @param title
     * @param f
     * @param fn
     * @throws LoginException
     * @throws IOException
     */
    public void logImageToPage(String title, File f, String fn)
            throws LoginException, IOException {

        String cont = w.getPageText(title);

        String date = (new Date(System.currentTimeMillis())).toString();
        cont = cont.concat("\n\n[[File:" + fn + "]]\n");

        w.edit(title, cont, "image file added");
        w.upload(f, fn, "...", "descriptor");

        System.out.println("***imageUpload() # done! ***\n");

        System.out.println(w.getFullURL(title));



    }

    /**
     * A special log to a JobPage.
     *
     * @param jobPn
     * @param f
     * @param fn
     * @param logText
     * @param descriptor
     * @throws IOException
     * @throws LoginException
     */
    public void logResultsToJobPage(File f, String fn, String logText, String descriptor) throws IOException, LoginException {

        String jobPn = jCont.jobPn;

        String cont = w.getPageText(jobPn);

        String date = (new Date(System.currentTimeMillis())).toString();
        cont = cont.concat("\n\n[[File:" + fn + "]]\n");

        w.edit(jobPn, cont, "result file added");
        w.upload(f, fn, logText, descriptor);

        System.out.println("***resultUpload() # done! ***\n");

    }

    /**
     * A special log to a JobPage.
     *
     * @param jobPn
     * @param resultFileFolder
     * @throws IOException
     * @throws LoginException
     */
    public void logExplicitResultsToJobPage(String resultFileFolder, String HDFSCluster, Configuration conf) throws IOException, LoginException {

        String jobPn = jCont.jobPn;

        String cont = w.getPageText(jobPn);

        String date = (new Date(System.currentTimeMillis())).toString();
        cont = cont.concat("\n\n[[DataSet URI::" + resultFileFolder + "]]\n");
        cont = cont.concat("\nResult table: \n" + WikiTableBuilder.getWikiTable(HDFSCluster, resultFileFolder, conf) + "\n");



        w.edit(jobPn, cont, "explicit result-link added");

        System.out.println("***resultUpload() # done! ***\n");

    }

    public void logImplicitResultsToJobPage(String resultFileFolder, File f, String fn) throws IOException, LoginException {

        String jobPn = jCont.jobPn;

        String cont = w.getPageText(jobPn);

        cont = cont.concat("\n[[DataSet URI::" + resultFileFolder + "| ]]\n");
        cont = cont.concat("\nDataset statistics: [[File:" + f.getName() + "]]\n");

        w.edit(jobPn, cont, "implicit result file added");

        try {
            w.upload(f, f.getName(), dsCont.getDataSetCategories(jCont, project, localUser, localHostName), "__descriptor__");
        } catch (Exception e) {
            System.out.println("###ImplicitResultUpload() was not possible ... please check the SemanticMeidaWiki-Configuration ... ! \n");
            //	e.printStackTrace();
        }

        System.out.println("***resultsUpload() # done! ***\n");

    }

    public void logJobConfToJobPage(String fn, File localFile) throws IOException, LoginException {

        String cont = w.getPageText(jCont.jobPn);

        String date = (new Date(System.currentTimeMillis())).toString();

        cont = cont.concat("\nJob-Log: [[File:" + localFile.getName() + "]]\n");
        cont = cont.concat("\nResult file: " + fn + "\n");

        w.edit(jCont.jobPn, cont, "image file added");

        System.out.println(">>> Upload local file now: " + localFile.getAbsolutePath());

        try {
            w.upload(localFile, localFile.getName(), "...", "[[is userlogfile:true| ]] ... descriptor");
        } catch (Exception e) {
            System.out.println("###JobLogUpload() was not possible ... please check the SemanticMeidaWiki-Configuration ... ! \n");
            // e.printStackTrace();
        }

        System.out.println("***JobLogUpload() # done! ***\n");

    }

    /**
     * Log data to a User-Context
     *
     * @param bashText
     * @throws LoginException
     * @throws IOException
     */
    public void _logBashHistoryToPage(String bashText) throws LoginException, IOException {

        uCont.ucPn = _createTheNewUCPage("username: " + uCont.user);

        String cont = w.getPageText(uCont.ucPn);

        cont = bashText.concat(cont);

        w.edit(uCont.ucPn, cont, "new bash_history added ... ");


    }

    public void logRuntimeResultsToJobPage(String runtimeStats, String HDFSCluster, Configuration conf) throws IOException, LoginException {

        String jobPn = jCont.jobPn;

        String cont = w.getPageText(jobPn);

        String date = (new Date(System.currentTimeMillis())).toString();
        cont = cont.concat("\n\n=====Runtime stats=====\n");
        cont = cont.concat(runtimeStats + "\n");

        w.edit(jobPn, cont, "runtime state added");

        System.out.println("***runtimResultsUpload() # done! ***\n");


    }

    public void logFileToHistoryToPage(String fn) throws Exception {
        File f = new File(fn);
        if (!f.canRead()) {
            throw new Exception("File: " + f.getAbsolutePath() + " not readable!!!");
        }

        uCont.ucPn = _createTheNewUCPage("username: " + uCont.user);
        String contOLD = w.getPageText(uCont.ucPn);


        String date = (new Date(System.currentTimeMillis())).toString();
        String fnUpload = date + "_" + uCont.user + "_" + f.getName();
        fnUpload = fnUpload.replace(" ", "_");
        fnUpload = fnUpload.replace(":", "_");
        fnUpload = fnUpload.replace(".", "_");
        fnUpload = fnUpload + ".txt";

        String cont = "\n\n===" + date + "===";
        cont = cont.concat("\nContext-Log: [[File:" + fnUpload + "]]\n");
        cont = cont.concat(contOLD);

        w.edit(uCont.ucPn, cont, "local file added");

        System.out.println(">>> Upload local file now: " + f.getAbsolutePath() + " as: " + f.getName() + "_" + date + "_");

        try {
            w.upload(f, fnUpload, "...", "user defined logfile");
        } catch (Exception e) {
            System.out.println("###LocalLogUpload() was not possible ... please check the SemanticMeidaWiki-Configuration ... ! \n");
            // e.printStackTrace();
        }

    }

    public void _logNoteToPage(String text) throws LoginException, IOException {

        uCont.ucPn = _createTheNewUCPage("username: " + uCont.user);

        String cont = w.getPageText(uCont.ucPn);

        cont = text.concat(cont);

        w.edit(uCont.ucPn, cont, "new note added ... ");

    }

    public void _logNoteLinkToPage(String text) throws LoginException, IOException {

        uCont.ucPn = _createTheNewUCPage("username: " + uCont.user);
        String nn = uCont.user + System.currentTimeMillis();

        String noteLink = "\n[[" + nn + " | linked note ]] ...\n";
        String cont = w.getPageText(uCont.ucPn);
        cont = noteLink.concat(cont);
        w.edit(uCont.ucPn, cont, "new note-link added ... ");

        w.edit(nn, text, "new note-text added to node link ... ");
    }

    public void createCategoryIfNotExists(String s) throws IOException, LoginException {
        String catName = "Category:" + s;
        String[] t = new String[1];
        t[0] = catName;

        boolean[] flag = w.exists(t);

        if (flag[0]) {
        } else {
            w.edit(catName, "auto imported category ... " + getTimeStamp() + ")", "auto import of category done.");

            System.out.println("***pageCreation() # done! ***\n");
        }

    }

    public void appendMailIfNotExists(Message m, String cat) throws MessagingException, IOException, LoginException {

        Address a = m.getFrom()[0];

        String pName = "MESSAGE_from_" + a.toString() + "_" + m.getSentDate();
        pName = pName.replace("<", "_");
        pName = pName.replace(">", "_");
        pName = pName.replace("[", "_");
        pName = pName.replace("]", "_");
        pName = pName.replace("{", "_");
        pName = pName.replace("}", "_");
        pName = pName.replace("@", "_");
        pName = pName.replace(":", "_");
        pName = pName.replace(" ", "_");
        pName = pName.replace(".", "_");

        String[] t = new String[1];
        t[0] = pName;

// 		boolean[] flag = w.exists(t);
//		
//		if( flag[0] ) {
//			
//		}
//		else {
        Multipart mp = (Multipart) m.getContent();
        BodyPart bp = mp.getBodyPart(0);
        System.out.println("SENT DATE:" + m.getSentDate());
        System.out.println("SUBJECT:" + m.getSubject());
        System.out.println("CONTENT:" + bp.getContent());

        String content = "==" + m.getSubject() + "==\n__SHOWFACTBOX__\n[[Category:" + cat + "]]\n" + bp.getContent();

        _logNoteToPage(content);

        System.out.println("***messageImport # done! ***\n");
//		}

    }

    public void createPageIfNotExists(String pageName) throws IOException, LoginException {

        String[] t = new String[1];
        t[0] = pageName;

        boolean[] flag = w.exists(t);

        if (flag[0]) {
        
        } 
        else {
            w.edit(pageName, "auto imported category ... " + getTimeStamp() + ")", "auto creation of a page done.");

            System.out.println("***pageCreation() # done! ***\n");
        }


    }

    public void logDataSetSchemaToDataSetPage(String page, String newText) throws IOException, LoginException {
        
        String cont = w.getPageText(page);

        cont = newText.concat(cont);

        w.edit(page, cont, "new schema information added ... ");
    }
}
