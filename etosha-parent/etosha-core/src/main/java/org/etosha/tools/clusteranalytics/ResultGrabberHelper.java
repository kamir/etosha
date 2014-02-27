package org.etosha.tools.clusteranalytics;

/**
 * 
 * Result GRABBER loads the result logs from HDFS to a local temp direcotry
 * from where it can be uploaded to the semantic knowledge base.
 * 
 */

import java.io.*;


import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class ResultGrabberHelper {

	// http://linuxjunkies.wordpress.com/2011/11/21/a-hdfsclient-for-hadoop-using-the-native-java-api-a-tutorial/
	// http://www.slideshare.net/martyhall/hadoop-tutorial-hdfs-part-3-java-api

	static public Path getJobLogFile(String HDFSCluster, String resultFolder,
			Configuration conf) {

		Path back = null;
		boolean hasSuccess = false;

		try {
			FileSystem fs = FileSystem.get(conf);

			FileStatus[] status = fs.listStatus(new Path(HDFSCluster
					+ resultFolder));
			for (int i = 0; i < status.length; i++) {

				String fn = status[i].getPath().getName();
				System.out.println("> " + fn);
				if (fn.equals("_SUCCESS")) {
					hasSuccess = true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File not found: " + resultFolder);
		}

		if (hasSuccess) {
			try {

				String resultFolder2 = resultFolder + "/_logs/history";
				FileSystem fs = FileSystem.get(conf);
				FileStatus[] status = fs.listStatus(new Path(HDFSCluster
						+ resultFolder2));
				for (int i = 0; i < status.length; i++) {

					String fn = status[i].getPath().getName();
					if (fn.endsWith("_conf.xml")) {
						back = status[i].getPath();
					}

				}

			} catch (Exception e) {
				System.out.println("LOGFILE not found: " + resultFolder);
			}
		}

		return back;
	}
	
	static public Path getJobResultFile(String HDFSCluster, String resultFolder,
			Configuration conf) {

		Path back = null;
		boolean hasSuccess = false;

		try {
			FileSystem fs = FileSystem.get(conf);

			FileStatus[] status = fs.listStatus(new Path(HDFSCluster
					+ resultFolder));
			for (int i = 0; i < status.length; i++) {

				String fn = status[i].getPath().getName();
				System.out.println("> " + fn);
				if (fn.equals("_SUCCESS")) {
					hasSuccess = true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File not found: " + resultFolder);
		}

		if (hasSuccess) {
			try {

				String resultFolder2 = resultFolder;
				
				FileSystem fs = FileSystem.get(conf);
				FileStatus[] status = fs.listStatus(new Path(HDFSCluster
						+ resultFolder2));
				for (int i = 0; i < status.length; i++) {

					String fn = status[i].getPath().getName();
					if (fn.endsWith("part-r-00000")) {
						back = status[i].getPath();
					}

				}

			} catch (Exception e) {
				System.out.println("LOGFILE not found: " + resultFolder);
			}
		}

		return back;
	}


	static public void copyZipFileFromHDFS(Path sourcePath, File dest,
			Configuration conf) throws IOException {

		FileSystem fileSystem = FileSystem.get(conf);

		if (!fileSystem.exists(sourcePath)) {
			System.out.println("HDFS-File " + sourcePath + " does not exists");
			return;
		}

		FSDataInputStream in = fileSystem.open(sourcePath);

	    ZipOutputStream out = new ZipOutputStream(new FileOutputStream( dest ));
	    String n = dest.getName();
	    int endIndex = n.length()-4;
	    String eName = n.substring(0, endIndex);
	    		
	    out.putNextEntry(new ZipEntry( eName )); 
		
		//OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

		byte[] b = new byte[1024];
		int numBytes = 0;
		while ((numBytes = in.read(b)) > 0) {
			out.write(b, 0, numBytes);
		}

		in.close();
		out.close();
		fileSystem.close();
	}
	
	static public void copyFileFromHDFS(Path sourcePath, File dest,
			Configuration conf) throws IOException {

		FileSystem fileSystem = FileSystem.get(conf);

		if (!fileSystem.exists(sourcePath)) {
			System.out.println("HDFS-File " + sourcePath + " does not exists");
			return;
		}

		FSDataInputStream in = fileSystem.open(sourcePath);
		
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

		byte[] b = new byte[1024];
		int numBytes = 0;
		while ((numBytes = in.read(b)) > 0) {
			out.write(b, 0, numBytes);
		}

		in.close();
		out.close();
		fileSystem.close();
	}


}
