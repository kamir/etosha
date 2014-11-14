package org.etosha.io.hdfs;

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
public class HDFSTree {

	public static void main(String[] args) throws Exception {
		
		if ( args.length < 1 ) {
			
			System.out.println("Exit early, because no args found on commandline.");
			
			System.exit(-2);
		}
		
		String path = args[0];
		int depth = Integer.parseInt( args[1] );
				
		try {
			
			HDFSWalker walker = new HDFSWalker( new Path( path ), depth );
			walker.walk();
	
		} 
		catch (Exception e) {
			System.err.println( e.getMessage() );
			// System.out.println("File [" + serverNN + path + "] was not found in HDFS.");
			System.out.println("File [" + path + "] was not found in HDFS.");
	    }
	}
}