package org.etosha.io.hdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileChecksum;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.AccessControlException;

public class HDFSWalker {

	Path path;
	Configuration conf;
	FileSystem fs;
	int depth = 5;
	
	public HDFSWalker( Path p, int d ) throws IOException {
 
		path = p;
		depth = d;
		
		conf = new Configuration();
		fs = FileSystem.get( conf );

        System.out.println( "> fs.defaultFS    : " + conf.get("fs.defaultFS") );
        System.out.println( "> PARAM.1.path    : " + p );
        System.out.println( "> PARAM.2.deptht  : " + depth );
        
	}
 
	public void walk() throws AccessControlException, FileNotFoundException, IOException {
		walk( path, 0 );
	}
	
	public void walk( Path p, int d ) throws AccessControlException, FileNotFoundException, IOException {
		FileStatus[] status = fs.listStatus( path );
		System.out.println( d + " " + getTab( d ) + p );
		
		for (int i = 0; i < status.length; i++) {
			
			FileStatus stat = status[i];
			
			if( !stat.isDir() ) {
				
				// uses the default config // which has your default FS
//			        FileContext myFContext = FileContext.getFileContext(); 
//				FileChecksum csum = myFContext.getFileChecksum( stat.getPath() );
//				
//				System.out.println( "FILE [" + stat.getPath() + "] {CS: " + csum + "}" );
//				
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(
//								fs.open(status[i].getPath())
//					    )
//				);
//				String line;
//				line = br.readLine();
//				while (line != null) {
//					System.out.println(line);
//					line = br.readLine();
//				}
			}
			else {
				d = d + 1;
				if ( d < depth ) walk( stat.getPath(), d );				
			}
//	        File root = new File( path );
//	        File[] list = root.listFiles();
//
//	        if (list == null) return;
//
//	        for ( File f : list ) {
//	            if ( f.isDirectory() ) {
//	                walk( f.getAbsolutePath() );
//	                System.out.println( "Dir:" + f.getAbsoluteFile() );
//	            }
//	            else {
//	                System.out.println( "File:" + f.getAbsoluteFile() );
//	            }
//	        }
			
			}
	    }

	private String getTab(int d) {
		String s = " ";
		int i = 0;
		while( i < d ) {
			s.concat( "\t" );	
			i++;
		}		
		return s;
	}
 
	
}