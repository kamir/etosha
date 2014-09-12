
package org.apache.etosha.hdfsindexer;

import java.io.*;
 
/**
 *
 * @author kamir
 */
public class ReadCMD {
 
   public static String read(String label, String def) {
 
      //  prompt the "LABEL"
      System.out.print( label + " ( " + def + " ) ");
 
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
      String val = null;
 
      //  read the username from the command-line; need to use try/catch with the
      //  readLine() method
      try {
         val = br.readLine();
         if ( val.length() < 1 ) { 
             System.out.println("IO error trying to read input!");
             System.out.println("Use default value (" + def + ")" );
             val = def;
          } 
      } 
      catch (IOException ioe) {
         System.out.println("IO error trying to read input!");
         System.out.println("Use default value (" + def + ")" );
         val = def;
      }
      return val;
    }  
   
   
}
