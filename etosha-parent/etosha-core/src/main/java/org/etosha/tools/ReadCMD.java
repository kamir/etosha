/**
 * A component which reads some input from command line.
 */
package org.etosha.tools;

import java.io.*;
 
/**
 *
 * @author kamir
 */
public class ReadCMD { 
   
    /**
     * Prompt a label and the default value.
     * 
     * Default is given back if an error occurs or no input was provided.
     * 
     * @param label
     * @param defaultValue
     * @return 
     */
    public static String read(String label, String defaultValue) {
 
      //  prompt the "LABEL"
      System.out.print( "> " + label + " ( " + defaultValue + " ) ");
 
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
      String val = null;

      try {
         val = br.readLine();
         if ( val.length() < 1 ) { 
             System.out.println("NO Input provided.");
             System.out.println("Use default value (" + defaultValue + ")" );
             val = defaultValue;
          } 
      } 
      catch (IOException ioe) {
         System.out.println("IO error trying to read input!");
         System.out.println("Use default value (" + defaultValue + ")" );
         val = defaultValue;
      }
      return val;
    }  
   
   
}
