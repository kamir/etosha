package org.etosha.data.profiles;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

/* 
 * To define a map function for your MapReduce job, subclass 
 * the Mapper class and override the map method.
 * The class definition requires four parameters: 
 *   The data type of the input key
 *   The data type of the input value
 *   The data type of the output key (which is the input key type 
 *   for the reducer)
 *   The data type of the output value (which is the input value 
 *   type for the reducer)
 */

public class SimpleTableMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

  /*
   * The map method runs once for each line of text in the input file.
   * The method receives a key of type LongWritable, a value of type
   * Text, and a Context object.
   */
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    /*
     * Convert the line, which is received as a Text object,
     * to a String object.
     */
    String line = value.toString();
    
    DoubleWritable dval = new DoubleWritable();
    Text emitKey = new Text();
    String s = "";
    
    int column = 0;
    for (String word : line.split("\t")) {
          emitKey.set( column + s );
    	  double val = 0.0;
    	  try {
    		  val = Double.parseDouble(word);
    	  } 
    	  catch (Exception ex) {
    		  val = 0.0;
    		  Counter c = context.getCounter("DATASET", "LOAD.ERROR");
    		  c.increment(1);
    	  }
    	  dval.set(val);
    	  context.write( emitKey, dval );  
    	  column = column + 1;
    }
  }
}