package io.hdfs;

import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

/**
 * 
 * http://blog.cloudera.com/blog/2012/03/authorization-and-authentication-in-hadoop/
 * 
 * 
 * @author training
 *
 */
public class FSTree {

	public static void main(String[] args) throws Exception {
		
		if ( args.length < 1 ) {
			
			System.out.println("Exit early, because no args found on commandline.");
			
			System.exit(-2);
		}
		
		String path = args[0];
		// String serverNN = "hdfs://elephant:8020";
		// String serverNN = "hdfs://elephant:8020";
		
		try {

            Configuration conf = new Configuration();

            System.out.println( "fs.defaultFS: " + conf.get("fs.defaultFS") );
            
			FileSystem fs = FileSystem.get( conf );
			FileStatus[] status = fs.listStatus(
					new Path( path )
		    );

			for (int i = 0; i < status.length; i++) {
				FileStatus stat = status[i];
				
				if( !stat.isDir() ) {
					System.out.println(stat.getPath());

					// uses the default config // which has your default FS
				    FileContext myFContext = FileContext.getFileContext(); 
					FileChecksum csum = myFContext.getFileChecksum( stat.getPath() );
					
					System.out.println( "CS: " + csum );
					
//					BufferedReader br = new BufferedReader(
//							new InputStreamReader(
//									fs.open(status[i].getPath())
//						    )
//					);
//					String line;
//					line = br.readLine();
//					while (line != null) {
//						System.out.println(line);
//						line = br.readLine();
//					}
				}
				else {
					
					
				}
			}
		} 
		catch (Exception e) {
			System.err.println( e.getMessage() );
			// System.out.println("File [" + serverNN + path + "] was not found in HDFS.");
			System.out.println("File [" + path + "] was not found in HDFS.");
	    }
	}
}