package org.etosha.cmd.jobprofiler;

/**
 * from: GITHUB
 *       
 *       https://github.com/alexholmes/hadoop-book
 *       
 *                      Apache License
 *                      Version 2.0, January 2004
 *                      http://www.apache.org/licenses/   
 **/

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.jobhistory.JobHistoryParser;
import org.apache.hadoop.mapreduce.jobhistory.JobHistoryParser.JobInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.hadoop.mapreduce.TaskCounter.*;

public final class JobHistoryHelper {

  public static JobHistoryParser.JobInfo getJobInfoFromCliArgs(String ... args)
      throws IOException {
    return getJobInfoFromCliArgs(new Configuration(), args);
  }

  public static JobHistoryParser.JobInfo getJobInfoFromCliArgs(Configuration conf, String ... args)
      throws IOException {
	  
	  for( String s : args) {
		  System.out.println(s);		  
	  }
    String usage = "Expected 2 arguments, either --hdfsdir <dir> or --localfile <path>";
    if(args.length != 2) {
      throw new IOException(usage);
    }
    if("--hdfsdir".equals(args[0])) {
      return getJobInfoFromHdfsOutputDir(args[1], conf);
    } else if("--localfile".equals(args[0])) {
      return getJobInfoFromLocalFile(args[1], conf);
    }
    throw new IOException("Unexpected option '" + args[0] + "' \n" + usage);
  }


  public static PathFilter jobLogFileFilter = new PathFilter() {
    public boolean accept(Path path) {
      return !(path.getName().endsWith(".xml"));
    }
  };

  public static JobHistoryParser.JobInfo getJobInfoFromHdfsOutputDir(String outputDir, Configuration conf)
      throws IOException {

/*
    Path output = new Path(outputDir);
    Path historyLogDir = new Path(output, "_logs/history");
      FileSystem fs = output.getFileSystem(conf);
      if (!fs.exists(output)) {
        throw new IOException("History directory " + historyLogDir.toString()
            + " does not exist");
      }
      Path[] jobFiles = FileUtil.stat2Paths(fs.listStatus(historyLogDir,
          jobLogFileFilter));
      if (jobFiles.length == 0) {
        throw new IOException("Not a valid history directory "
            + historyLogDir.toString());
      }
      String[] jobDetails =
          JobHistoryParser.JobInfo.decodeJobHistoryFileName(jobFiles[0].getName()).
              split("_");
      String jobId = jobDetails[2] + "_" + jobDetails[3] + "_" + jobDetails[4];
      JobHistoryParser.JobInfo job = new JobHistoryParser.JobInfo(jobId);
      DefaultJobHistoryParser.parseJobTasks(jobFiles[0].toString(), job, fs);
    
    return job; */

return null;
  }

  public static JobHistoryParser.JobInfo getJobInfoFromLocalFile(String outputFile, Configuration conf)
      throws IOException {
  /*  FileSystem fs = FileSystem.getLocal(conf);

    Path outputFilePath = new Path(outputFile);

    String[] jobDetails =
        JobHistoryParser.JobInfo.decodeJobHistoryFileName(outputFilePath.getName()).
            split("_");
    String jobId = jobDetails[2] + "_" + jobDetails[3] + "_" + jobDetails[4];
    JobHistoryParser.JobInfo job = new JobHistoryParser.JobInfo(jobId);
    DefaultJobHistoryParser.parseJobTasks(outputFile, job, fs);
    return job;
*/
return null;
  }

  public static List<TaskMetrics> getMapTaskMetrics(
      JobHistoryParser.JobInfo job)
      throws ParseException {
    List<TaskMetrics> metrics = new ArrayList<TaskMetrics>();
    //addTask(metrics, job, JobHistoryParser.Values.MAP.name());
    return metrics;
  }

  public static List<TaskMetrics> getReduceTaskMetrics(
      JobHistoryParser.JobInfo job)
      throws ParseException {
    List<TaskMetrics> metrics = new ArrayList<TaskMetrics>();
    //addTask(metrics, job, JobHistoryParser.Values.REDUCE.name());
    return metrics;
  }


  public static long extractLongFieldValue(TaskMetrics m,
                                           String fieldName)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    return (Long) MethodUtils.invokeMethod(m, fieldName, null);
  }

  public static void addTask(List<TaskMetrics> metrics,
                             JobHistoryParser.JobInfo job,
                             String taskType)
      throws ParseException {


/*
    Map<String, JobHistoryParser.Task> tasks = job.getAllTasks();
    for (JobHistoryParser.Task task : tasks.values()) {
      for (JobHistoryParser.TaskAttempt attempt : task.getTaskAttempts()
          .values()) {
        if (taskType.equals(task.get(JobHistoryParser.Keys.TASK_TYPE))) {

          TaskMetrics metric = new TaskMetrics();
          metrics.add(metric);
          metric.setType(taskType)
              .setTaskId(attempt.get(JobHistoryParser.Keys.TASK_ATTEMPT_ID))
              .setHost(attempt.get(JobHistoryParser.Keys.HOSTNAME))
              .setStatus(attempt.get(JobHistoryParser.Keys.TASK_STATUS));

          long taskOverallTime =
              attempt.getLong(JobHistoryParser.Keys.FINISH_TIME) -
                  attempt.getLong(JobHistoryParser.Keys.START_TIME);

          metric.setOverallTimeMillis(taskOverallTime);

          metric.setInputBytes(
              extractNumericCounter(
                  attempt.get(JobHistoryParser.Keys.COUNTERS),
                  MAP_INPUT_BYTES.name(),
                  REDUCE_SHUFFLE_BYTES.name()));

          metric.setOutputBytes(
              extractNumericCounter(
                  attempt.get(JobHistoryParser.Keys.COUNTERS),
                  MAP_OUTPUT_BYTES.name(),
                  "HDFS_BYTES_WRITTEN"));

          metric.setInputRecords(
              extractNumericCounter(
                  attempt.get(JobHistoryParser.Keys.COUNTERS),
                  MAP_INPUT_RECORDS.name(),
                  REDUCE_INPUT_RECORDS.name()));

          metric.setOutputRecords(
              extractNumericCounter(
                  attempt.get(JobHistoryParser.Keys.COUNTERS),
                  MAP_OUTPUT_RECORDS.name(),
                  REDUCE_OUTPUT_RECORDS.name()));

          if (JobHistoryParser.Values.REDUCE.name()
              .equals(task.get(JobHistoryParser.Keys.TASK_TYPE))) {
            long shuffleTime =
                attempt.getLong(JobHistoryParser.Keys.SHUFFLE_FINISHED) -
                    attempt.getLong(JobHistoryParser.Keys.START_TIME);
            long sortTime =
                attempt.getLong(JobHistoryParser.Keys.SORT_FINISHED) -
                    attempt
                        .getLong(JobHistoryParser.Keys.SHUFFLE_FINISHED);

            metric.setShuffleTimeMillis(shuffleTime);
            metric.setSortTimeMillis(sortTime);
          }

        }
      }
    }
*/
  }

  public static long extractNumericCounter(String counterFromHist,
                                           String... counterNames)
      throws ParseException {
    long result = -1;
    String s = extractCounter(counterFromHist, counterNames);
    if (s != null) {
      result = Long.valueOf(s);
    }
    return result;
  }

  public static String extractCounter(String counterFromHist,
                                      String... counterNames)
      throws ParseException {
/*
    Counters counters =
        Counters.fromEscapedCompactString(counterFromHist);
    for (Counters.Group group : counters) {
      for (Counters.Counter counter : group) {
        for (String counterName : counterNames) {
          if (counterName.equals(counter.getName())) {
            return String.valueOf(counter.getCounter());
          }
        }
      }
    }
*/
    return null;
  }

  public static String formatTime(long timeDiffMillis) {
    StringBuilder buf = new StringBuilder();
    long hours = timeDiffMillis / (60 * 60 * 1000);
    long rem = (timeDiffMillis % (60 * 60 * 1000));
    long minutes = rem / (60 * 1000);
    rem = rem % (60 * 1000);
    long seconds = rem / 1000;

    if (hours != 0) {
      buf.append(hours);
      buf.append("h");
    }
    if (hours != 0 || minutes != 0) {
      if (buf.length() > 0) {
        buf.append(" ");
      }
      buf.append(minutes);
      buf.append("m");
    }
    if (buf.length() > 0) {
      buf.append(" ");
    }
    buf.append(seconds)
        .append("s");
    return buf.toString();
  }


}
