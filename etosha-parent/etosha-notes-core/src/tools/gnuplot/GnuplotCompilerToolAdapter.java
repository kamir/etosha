/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.gnuplot;

import tools.latex.*;
import data.CollabProject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *
 * @author kamir
 */
public class GnuplotCompilerToolAdapter {
    public static String COMPILE_CMD;
    
        public static void compileTexFile( File file, CollabProject project ) { 
        
        String log = "";
         
        int endIndex = file.getName().lastIndexOf(".");
        
        String name = file.getName().substring(0, endIndex);
        
        String pfad = file.getParent() + File.separator;

        name = name.replace('/', '\\');
        String project_name = project.name.replace('/', '\\');
        System.out.println(">>> compile gnuplot-file ... {"+ pfad +"}[" + name +"]");
        
        try {
          String line;
          Process p = Runtime.getRuntime().exec( "cmd /c " + pfad + "compile_gnuplot.bat " + name +" " + pfad + " " + project_name );
          BufferedReader bri = new BufferedReader
            (new InputStreamReader(p.getInputStream()));
          BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));
          while ((line = bri.readLine()) != null) {
            System.out.println(line);
            log = log.concat( line + "\n" );
             
          }
          bri.close();
          while ((line = bre.readLine()) != null) {
            System.out.println(line);
          }
          bre.close();
          p.waitFor();
          
          
        
          
          System.out.println( log );
          
          System.out.println("Done.");
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
        
        
    }
        
    
    
}
