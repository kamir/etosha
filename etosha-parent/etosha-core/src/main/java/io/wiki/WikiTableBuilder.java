package io.wiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.etosha.tools.clusteranalytics.ResultGrabberHelper;

/** 
 * 
 * The WikiTableLoader creates a String representation of the the
 * table data to publish this on a Wiki-page.
 * 
 * @author training
 *
 */

public class WikiTableBuilder {
	
	/**
	 * We create a WIKI table ...
	 * 
	   	    	{| class="wikitable"
				|-
				! Header 1
				! Header 2
				! Header 3
				|-
				| row 1, cell 1
				| row 1, cell 2
				| row 1, cell 3
				|-
				| row 2, cell 1
				| row 2, cell 2
				| row 2, cell 3
				|}
	 * @throws IOException 

	 * 
	 * 
	 * 
	 */
	static public String getWikiTable( String HDFSCluster, String resultFileFolder, Configuration conf ) throws IOException {
		String tab = "{| class=\"wikitable\"\n" + "|-";
		
		Path jobResultFile = ResultGrabberHelper.getJobResultFile(HDFSCluster, resultFileFolder, conf );
		
		File dest2 = new File( "/tmp/etosha.MR.Context/_wikitab_simple_DS_statistics.dat" );
		ResultGrabberHelper.copyFileFromHDFS(jobResultFile, dest2, conf );
		
		BufferedReader br = new BufferedReader( new FileReader( dest2 ) );
		while ( br.ready() ) {
			String line = "\n|-\n|";
			
			String l = br.readLine();
			l = l.replaceAll("\t", "\n|" );
			line = line.concat( l );
			tab = tab.concat(line);
		}

		 
		tab = tab + "\n|}";
		
		return tab;
	}

}
