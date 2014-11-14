package org.etosha.core.context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.etosha.core.sc.connector.SemanticContextBridge;

public class SemanticUserContext {
	
	public String ucPn = null;
	
	public String user = "training";
	public String host = "demoVM.webex";
	public String ip = "127.0.0.1";
	
	public String project = "Etosha dev";
	
	public String getUserContextBashHistory() throws IOException {
		
		StringBuffer data = new StringBuffer();
		
		data.append( "\n===" + SemanticContextBridge.getTimeStamp() + "===\n" );
		data.append( "     '''/home/training/.bash_history'''\n" );
		String fn = "/home/training/.bash_history";
		BufferedReader br = new BufferedReader( new FileReader( fn ) );
		while( br.ready() ) {
			String line = br.readLine();
			data.append("     " + line + "\n");
		}
		
		return data.toString();
	}

	
	
	public String getUserContextCategories() {
		String cat1 = "[[Category:" + project + "]]";	
		String cat2 = "[[Category:" + user + "]]";	
		String cat3 = "[[Category:" + host + "]]";	 // from where was the job submitted
		//String cat4 = "[[Category:" + drivername + "]]";
		//String cat5 = "[[Category:" + hadoopCluster + "]]";
		//String cat6 = "[[Category:" + localEnv + "]]";
		String cat = cat1.concat(cat2.concat(cat3)); //(cat4.concat(cat5.concat(cat6)))));
		return cat;
	}

}
