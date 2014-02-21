package mr;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * To define a reduce function for your MapReduce job, subclass 
 * the Reducer class and override the reduce method.
 * The class definition requires four parameters: 
 *   The data type of the input key (which is the output key type 
 *   from the mapper)
 *   The data type of the input value (which is the output value 
 *   type from the mapper)
 *   The data type of the output key
 *   The data type of the output value
 */   
public class MultiStatsReducer extends Reducer<Text, DoubleWritable, Text, Text> {

  /*
   * The reduce method runs once for each key received from
   * the shuffle and sort phase of the MapReduce framework.
   * The method receives a key of type Text, a set of values of type
   * IntWritable, and a Context object.
   */
  @Override
	public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
			throws IOException, InterruptedException {

	    String sep = "\t";
	    Text line = new Text();
	    
	    int valCount = 0;
		Double minVal = Double.MAX_VALUE;
		Double maxVal = Double.MIN_VALUE;
		Double sum = 0.0;
		
		/*
		 * For each value in the set of values passed to us by the mapper:
		 */
		for (DoubleWritable value : values) {
			double v = value.get();
			
			valCount += 1;
			sum += v;
			
			if( v < minVal ) minVal = v;
			if( v > maxVal ) maxVal = v;
		}
		
		/*
		 * Call the write method on the Context object to emit a key
		 * and a value from the reduce method. 
		 */
		String lineData = minVal + sep + maxVal  + sep + valCount + sep + sum + sep + ( sum / valCount ); 
		
		line.set( lineData );
		
		context.write(key, line);
	}
}