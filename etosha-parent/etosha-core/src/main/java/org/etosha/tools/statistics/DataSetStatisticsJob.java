package org.etosha.tools.statistics;

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileSystemUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.jobhistory.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.tools.clusteranalytics.ResultGrabberHelper;
import org.etosha.tools.statistics.jobstatistics.ExtractJobTaskTimeline;
import org.etosha.tools.statistics.jobstatistics.JobHistoryHelper;


public class DataSetStatisticsJob extends DataSetInspector {
	
	String resultFileFolder = null;
	String resultFolderName = null;

	public DataSetStatisticsJob() throws FailedLoginException, IOException {
		super();
	}

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration();
		
		// TODO: {MIDDLE} change static CFG file to a default file in /etc ...
		File cfgFile = new File("/home/training/etc/smw-site.xml");
		/**
		 * according to:
		 * 
		 * http://stackoverflow.com/questions/11478036/hadoop-configuration-
		 * property-returns-null
		 * 
		 * we add the resource as a URI
		 */
		cfg.addResource(cfgFile.getAbsoluteFile().toURI().toURL());

		int exitCode = ToolRunner.run(cfg, new DataSetStatisticsJob(), args);

		System.exit(exitCode);
	}

	@Override
	public int doRunJob(String[] args) {

		try {

			/*
			 * The expected command-line arguments are the paths containing
			 * input and output data. Terminate the job if the number of
			 * command-line arguments is not exactly 2.
			 */
			if (args.length != 2) {
				System.out
						.printf("Usage: DataSetStatistics <input dir> <output dir>\n");
				System.exit(-1);
			}

			/*
			 * Instantiate a Job object for your job's configuration and get the
			 * config object which has all data parsed from the commandline
			 * call.
			 */

			Job job = new Job(super.getConf());

			/*
			 * Specify the jar file that contains your driver, mapper, and
			 * reducer. Hadoop will transfer this jar file to nodes in your
			 * cluster running mapper and reducer tasks.
			 */
			job.setJarByClass(DataSetStatisticsJob.class);

			/*
			 * Specify an easily-decipherable name for the job. This job name
			 * will appear in reports and logs.
			 */
			job.setJobName("DataSetStatistics #2");

			/*
			 * Specify the paths to the input and output data based on the
			 * command-line arguments.
			 */
			Path pathOut = new Path(args[1]);
			
			FileInputFormat.setInputPaths(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, pathOut );
			
			resultFileFolder = pathOut.toUri().toString(); 
			resultFolderName = args[1];
			
			/*
			 * Specify the mapper and reducer classes.
			 */
			job.setMapperClass(mr.SimpleTableMapper.class);
			job.setReducerClass(mr.MultiStatsReducer.class);

			/*
			 * For the word count application, the input file and output files
			 * are in text format - the default format.
			 * 
			 * In text format files, each record is a line delineated by a by a
			 * line terminator.
			 * 
			 * When you use other input formats, you must call the
			 * SetInputFormatClass method. When you use other output formats,
			 * you must call the setOutputFormatClass method.
			 */
			
			/*
			 * For the word count application, the mapper's output keys and
			 * values have the same data types as the reducer's output keys and
			 * values: Text and IntWritable.
			 * 
			 * When they are not the same data types, you must call the
			 * setMapOutputKeyClass and setMapOutputValueClass methods.
			 */
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(DoubleWritable.class);

			/*
			 * Specify the job's output key and value classes.
			 */
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);

			/*
			 * Start the MapReduce job and wait for it to finish. If it finishes
			 * successfully, return 0. If not, return 1.
			 */
			boolean success = job.waitForCompletion(true);
			if (success)
				return 0;
			else
				return -1;

		} 
		catch (Exception ex) {
			return -1;
		}

	}
	
	public void doPostProcessLogging( String logText ) throws LoginException, IOException {

		File folder = new File( "/tmp/etosha.MR.Context" );
		if(!folder.exists()) folder.mkdir();

		FileSystem fileSystem = FileSystem.get( this.getConf() );
		Path srcPath = new Path(resultFileFolder);
		
		/**
		 * the Job-Log-File will be added to the wiki ...
		 */
		String HDFSCluster = "hdfs://0.0.0.0:8020/";
		Path jobConfFile = ResultGrabberHelper.getJobLogFile(HDFSCluster, resultFileFolder, this.getConf());
		File dest = new File( "/tmp/etosha.MR.Context/" + jobConfFile.getName() + ".zip" );
		ResultGrabberHelper.copyZipFileFromHDFS(jobConfFile, dest, this.getConf());
		scb.logJobConfToJobPage( resultFileFolder , dest );
		
		/**
		 * the implicit result-file will be added to the wiki ...
		 */
		Path jobResultFile = ResultGrabberHelper.getJobResultFile(HDFSCluster, resultFileFolder, this.getConf());
		File dest2 = new File( "/tmp/etosha.MR.Context/"+jobConfFile.getName()+"_simple_DS_statistics.dat" + ".zip" );
		ResultGrabberHelper.copyZipFileFromHDFS(jobResultFile, dest2, this.getConf());
		scb.logImplicitResultsToJobPage( resultFileFolder, dest2, resultFileFolder );

 		
 		
 		
 		/**
		 * the explicit result-file will be added to the wiki ...
		 */
		scb.logExplicitResultsToJobPage( resultFileFolder, HDFSCluster, this.getConf() );
		
		
		/**
		 * 
		 * log the job-benchmark to the Wiki ...
		 * 
		 * 
		 * 
		 */
		JobHistoryParser.JobInfo info = JobHistoryHelper.getJobInfoFromHdfsOutputDir( resultFileFolder, this.getConf() );
		
		try {

			// TODO: {TOP} convert the data to WIKITABLE
			String runtimeInfo = ExtractJobTaskTimeline.logTaskTimes( info );
			
			scb.logRuntimeResultsToJobPage( runtimeInfo, HDFSCluster, this.getConf() );
		
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("***postProcessingLog() # done! ***\n");
	}

}
