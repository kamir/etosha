package org.etosha.sandbox.mr.corpusprofiler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

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

public class WordContextMapper extends Mapper<NullWritable, BytesWritable, Text, IntWritable> {

	
	
	
  /*
   * The map method runs once for each line of text in the input file.
   * The method receives a key of type LongWritable, a value of type
   * Text, and a Context object.
   */
  @Override
  public void map(NullWritable key, BytesWritable value, Context context)
      throws IOException, InterruptedException {

	  
	  
  	/** 
  	 * We work with jsoup, a HTML parser to navigate the DOM-tree
  	 * 
  	 * 
  	 * http://jsoup.org/cookbook/input/parse-document-from-string
  	 * 
  	 */
	  String bS = null;
	  
	  byte[] content = value.getBytes();
	    int size = content.length;
	    InputStream is = null;
	    byte[] b = new byte[size];
	    try {
	        is = new ByteArrayInputStream(content);
	        is.read(b);
	        bS = new String(b);
	        // System.out.println("Data Recovered: " + bS );
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try{
	            if(is != null) is.close();
	        } catch (Exception ex){

	        }
	    }

  	  
  	  Document doc = Jsoup.parse(bS, "UTF-8");

  	  Element contentHTML = doc.getElementById("content");
  	  Elements links = contentHTML.getElementsByTag("a");
  	  for (Element link : links) {
  	    String linkHref = link.attr("href");
  	    String linkText = link.text();
  	    
        /*
         * Call the write method on the Context object to emit a key
         * and a value from the map method.
         */
        context.write(new Text(linkHref), new IntWritable(1));

  	  }
 	  
  	  

    	  
    	  
    	  
        
    
  }
}