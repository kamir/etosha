package org.etosha.tools.statistics;


/**
 * The semantic context of a Job will be managed automatic.
 * 
 * What will be logged:
 * 
 * Initial JOB-LOG:
 * ================
 * name: Host+User+Driver+Timesamp which in Categories: Host, User, Driver and has the property timestamp.
 * This is a kind of a double encoding, but this is required a) for readability by human users and b) for
 * automatic queries running against the ASK API of the Semantic Media Wiki, or later on, via SPARQL.
 * 
 * CMD line Parameters: 
 * --------------------
 * All commandline parametes are written to a Wikipage ... 

 * 
 * 
 * 
 * 
 * 
 */

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.smc.connector.SemanticContextBridge;
import org.etosha.smc.connector.external.Wiki;

public class DataSetInspector extends Configured implements Tool {

	SemanticContextBridge scb = null;
	// String pn = null;

	/**
	 * @throws FailedLoginException
	 * @throws IOException
	 */
	public DataSetInspector() throws FailedLoginException, IOException {
		// nothing to do here, all initialisation goes into the init() method
		// ...
	};

	private void init() throws UnknownHostException {
		scb = new SemanticContextBridge( this.getConf() );
		scb.login();
		System.out.println("***init() # done! ***\n");
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws LoginException
	 */
	public int run(String[] args) throws LoginException, IOException {

		init();

		doPreProcessLogging( "some new LOGGED DATA" );
		
		int state = doRunJob(args);
		
		doPostProcessLogging( "a new file ...");	

		return state;
	}

	/**
	 * 
	 * Here we find the implementation of the Hadoop MR Job ...
	 * 
	 * @param args
	 * @return
	 */
	public int doRunJob(String[] args) {
		System.out.println(">>> The   D a t a S e t I n s p e c t o r   class does not run a job in a cluster.\n");
		System.out.println(">>> Please implement your own job ... based in this class and overwrite:" +
				           "       public int doRunJob(String[] args) .\n");
		return 0;
	}

	public void doPreProcessLogging( String logText ) throws LoginException, IOException {
		scb.jCont.jobPn = scb.createTheNewPage( this.getClass().getCanonicalName() );
		
		scb.logToJobPage( "semantic job log ... "  , logText );
		
		System.out.println("***preProcessingLog() # done! ***\n");
	}

	public void doPostProcessLogging( String logText ) throws LoginException, IOException {

		// TODO: {MIDDLE} import the resultfile if (MODE: IMPLICIT) or just create a link to the place in HDFS ...
		
		/**
		 * the result-file will be added to the wiki ...
		 */
		
		scb.dsCont.isExplicit = false; // upload the result file
		
		File f = new File("./data/result.xls");
		String fn = "C." + f.getName();

		String descriptor = "original-file:" + f.getAbsolutePath();
		
		scb.logResultsToJobPage( f, fn, logText, descriptor );
		
		System.out.println("***postProcessingLog() # done! ***\n");
	}

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration();
		
		File cfgFile = new File("/home/training/etc/smw-site.xml");
		/** 
		 * according to:
		 * 
		 * 		http://stackoverflow.com/questions/11478036/hadoop-configuration-property-returns-null
		 * 
		 * we add the resource as a URI
		 */
		cfg.addResource( cfgFile.getAbsoluteFile().toURI().toURL() );

		int exitCode = ToolRunner.run(cfg, new DataSetInspector(), args);

		System.exit(exitCode);
	}

}
