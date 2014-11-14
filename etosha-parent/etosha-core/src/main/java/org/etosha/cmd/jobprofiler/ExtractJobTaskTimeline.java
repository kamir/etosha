package org.etosha.cmd.jobprofiler;

/**
 *   This helper extracts the data from JobHistoryParser.JobInfo
 *   and creates a Wiki-Snippet or a plain output.
 *   
 *   
 *   TODO: {TOP} integrate Velocity templates ...
 */

/**

 * from: GITHUB
 *       
 *       https://github.com/alexholmes/hadoop-book
 *       
 *                      Apache License
 *                      Version 2.0, January 2004
 *                      http://www.apache.org/licenses/   
 **/

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.jobhistory.JobHistoryParser;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ExtractJobTaskTimeline {

	public static void main(String[] args) throws Exception {
		try {
			dumpTaskTimes(args);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static String logTaskTimes(JobHistoryParser.JobInfo info) throws Exception {

		StringBuilder sbuilder = new StringBuilder();

		return sbuilder.toString();
/*
		JobHistoryParser.JobInfo job = info;

// JobFinishEvent.Type

		long startTime = job.getLong(JobHistoryParser.Keys.LAUNCH_TIME);
		long endTime = job.getLong(JobHistoryParser.Keys.FINISH_TIME);

		List<TimeRange> mapRanges = new ArrayList<TimeRange>();
		List<TimeRange> reduceRanges = new ArrayList<TimeRange>();
		List<TimeRange> shuffleRanges = new ArrayList<TimeRange>();
		List<TimeRange> sortRanges = new ArrayList<TimeRange>();

		Map<String, JobHistoryParser.Task> tasks = job.getAllTasks();
		for (JobHistoryParser.Task task : tasks.values()) {
			for (JobHistoryParser.TaskAttempt attempt : task.getTaskAttempts()
					.values()) {

				String taskId = attempt.get(JobHistoryParser.Keys.TASK_ATTEMPT_ID);
				String taskType = task.get(JobHistoryParser.Keys.TASK_TYPE);
				String taskStatus = task.get(JobHistoryParser.Keys.TASK_STATUS);

				sbuilder.append(taskId + " " + taskType + " " + taskStatus + "\n");

				long taskStartTime = attempt
						.getLong(JobHistoryParser.Keys.START_TIME);
				long taskEndTime = attempt.getLong(JobHistoryParser.Keys.FINISH_TIME);

				TimeRange range = new TimeRange(TimeUnit.MILLISECONDS,
						taskStartTime, taskEndTime);

				if (JobHistoryParser.Values.MAP.name().equals(taskType)) {
					mapRanges.add(range);
				} else if (JobHistoryParser.Values.REDUCE.name().equals(taskType)) {

					long shuffleEndTime = attempt
							.getLong(JobHistoryParser.Keys.SHUFFLE_FINISHED);
					long sortEndTime = attempt
							.getLong(JobHistoryParser.Keys.SORT_FINISHED);

					shuffleRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							taskStartTime, shuffleEndTime));
					sortRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							shuffleEndTime, sortEndTime));
					reduceRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							sortEndTime, taskEndTime));
				}
			}
		}

		// output the data, tab-separated in the following order:
		// time-offset #-map-tasks #-reduce-tasks #-shuffle-tasks #-sort-tasks
		// #-waste-tasks
		// steps of 1 second
		sbuilder.append("time").append("\tmap").append("\treduce")
				.append("\tshuffle").append("\tsort").append("\n");

		System.err.print(sbuilder.toString());

		int timeOffset = 0;
		for (long i = startTime; i <= endTime; i += 1000) {
			sbuilder.append(timeOffset).append("\t")
					.append(countRangesForTime(mapRanges, i)).append("\t")
					.append(countRangesForTime(reduceRanges, i)).append("\t")
					.append(countRangesForTime(shuffleRanges, i)).append("\t")
					.append(countRangesForTime(sortRanges, i)).append("\n");

			System.err.print(sbuilder.toString());
			timeOffset++;

		}

		return sbuilder.toString();
*/

	}

	public static void dumpTaskTimes(String... args) throws Exception {

/*
		JobHistoryParser.JobInfo job = JobHistoryHelper.getJobInfoFromCliArgs(args);

		long startTime = job.getLong(JobHistoryParser.Keys.LAUNCH_TIME);
		long endTime = job.getLong(JobHistoryParser.Keys.FINISH_TIME);

		List<TimeRange> mapRanges = new ArrayList<TimeRange>();
		List<TimeRange> reduceRanges = new ArrayList<TimeRange>();
		List<TimeRange> shuffleRanges = new ArrayList<TimeRange>();
		List<TimeRange> sortRanges = new ArrayList<TimeRange>();

		Map<String, JobHistoryParser.Task> tasks = job.getAllTasks();
		for (JobHistoryParser.Task task : tasks.values()) {
			for (JobHistoryParser.TaskAttempt attempt : task.getTaskAttempts()
					.values()) {

				String taskId = attempt.get(JobHistoryParser.Keys.TASK_ATTEMPT_ID);
				String taskType = task.get(JobHistoryParser.Keys.TASK_TYPE);
				String taskStatus = task.get(JobHistoryParser.Keys.TASK_STATUS);

				System.out.println(taskId + " " + taskType + " " + taskStatus);

				long taskStartTime = attempt
						.getLong(JobHistoryParser.Keys.START_TIME);
				long taskEndTime = attempt.getLong(JobHistoryParser.Keys.FINISH_TIME);

				TimeRange range = new TimeRange(TimeUnit.MILLISECONDS,
						taskStartTime, taskEndTime);

				if (JobHistoryParser.Values.MAP.name().equals(taskType)) {
					mapRanges.add(range);
				} else if (JobHistoryParser.Values.REDUCE.name().equals(taskType)) {

					long shuffleEndTime = attempt
							.getLong(JobHistoryParser.Keys.SHUFFLE_FINISHED);
					long sortEndTime = attempt
							.getLong(JobHistoryParser.Keys.SORT_FINISHED);

					shuffleRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							taskStartTime, shuffleEndTime));
					sortRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							shuffleEndTime, sortEndTime));
					reduceRanges.add(new TimeRange(TimeUnit.MILLISECONDS,
							sortEndTime, taskEndTime));
				}
			}
		}

		// output the data, tab-separated in the following order:
		// time-offset #-map-tasks #-reduce-tasks #-shuffle-tasks #-sort-tasks
		// #-waste-tasks
		// steps of 1 second
		StringBuilder sb = new StringBuilder();
		sb.append("time").append("\tmap").append("\treduce")
				.append("\tshuffle").append("\tsort");
		System.err.println(sb);

		int timeOffset = 0;
		for (long i = startTime; i <= endTime; i += 1000) {
			sb = new StringBuilder();
			sb.append(timeOffset).append("\t")
					.append(countRangesForTime(mapRanges, i)).append("\t")
					.append(countRangesForTime(reduceRanges, i)).append("\t")
					.append(countRangesForTime(shuffleRanges, i)).append("\t")
					.append(countRangesForTime(sortRanges, i));

			System.err.println(sb);
			timeOffset++;

		}
*/

	}

	public static int countRangesForTime(List<TimeRange> ranges, long time) {
		int count = 0;
		for (TimeRange range : ranges) {
			if (range.inRange(TimeUnit.MILLISECONDS, time)) {
				count++;
			}
		}
		return count;
	}

	public static class TimeRange {
		final long startTimeMillis;
		final long endTimeMillis;

		public TimeRange(TimeUnit unit, long start, long end) {
			startTimeMillis = unit.toMillis(start);
			endTimeMillis = unit.toMillis(end);
		}

		public boolean inRange(TimeUnit unit, long value) {
			long millis = unit.toMillis(value);
			return millis >= startTimeMillis && millis <= endTimeMillis;
		}
	}

}
