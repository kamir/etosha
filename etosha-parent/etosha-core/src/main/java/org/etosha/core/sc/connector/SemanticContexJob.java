package org.etosha.core.sc.connector;

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
import org.etosha.core.sc.connector.external.Wiki;

public class SemanticContexJob extends Configured implements Tool {

	/**
	 * 
	 * @throws FailedLoginException
	 * @throws IOException
	 */
	public SemanticContexJob() throws FailedLoginException, IOException {
		// nothing to do here, all initialisation goes into the init() method
		// ...
	};

	SemanticContextBridge scb = null;
	
	private void init() throws UnknownHostException {

		scb = new SemanticContextBridge( this.getConf() );
		
		scb.login();
		
		System.out.println("***init() # done! ***\n");

	}

	String pn = null;
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws LoginException
	 */
	public int run(String[] args) throws LoginException, IOException {

		init();

		doPreProcessLogging();
		
		int state = doRunJob(args);
		
		doPostProcessLogging();	

		return state;

	}

	/**
	 * 
	 * Here we find the implementation of the Hadoop MR Job ...
	 * 
	 * @param args
	 * @return
	 */
	private int doRunJob(String[] args) {
		System.out.println("*** The SemanticContextJob Base class does not run a job in a cluster.\n");
		System.out.println("*** Pleas implement your own job ... based in this class.\n");
		return 0;
	}

	private void doPreProcessLogging() throws LoginException, IOException {
		
		scb.jCont.jobPn = scb.createTheNewPage( this.getClass().getCanonicalName() );
		
		scb.logToJobPage( "semantic job log ... "  , "some new LOGGED DATA from: " + this.getClass().getCanonicalName() );
		
		System.out.println("***preProcessingLog() # done! ***\n");
	}

	private void doPostProcessLogging() throws LoginException, IOException {
		File f = new File("./images/Golden_gate2.jpg");
		String fn = "A." + f.getName();

		scb.logImageToPage(pn, f, fn);
		
		System.out.println("***postProcessingLog() # done! ***\n");
	}

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration();
		
		/** 
		 * according to:
		 * 
		 * 		http://stackoverflow.com/questions/11478036/hadoop-configuration-property-returns-null
		 * 
		 * we add the resource as a URI
		 */
		File cfgFile = new File("/home/training/etc/smw-site.xml");
		
		cfg.addResource( cfgFile.getAbsoluteFile().toURI().toURL() );

		int exitCode = ToolRunner.run(cfg, new SemanticContexJob(), args);

		System.exit(exitCode);
	}

}
