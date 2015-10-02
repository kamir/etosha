/**
 *
 *  Das Tool ruft den Latex-Compiler auf ...
 * 
 */
package tools.latex;

import data.CollabProject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;
import tools.ScriptSwitch;
import tools.antscripts.SystemAntAdapter;

/**
 *
 * @author kamir
 */
public class TexCompilerToolAdapter {

    public static String COMPILE_CMD;
    
    public static void compileTexFile( File file, CollabProject project ) { 
        
        String log = "";
         
        int endIndex = file.getName().lastIndexOf(".");
        
        String name = file.getName().substring(0, endIndex);
        
        String pfad = file.getParent() + File.separator;

        name = name.replace('/', '\\');
        String project_name = project.name.replace('/', '\\');
        System.out.println(">>> Compile LaTex-File ... {"+ pfad +"}[" + name +"]");
        
        try {
          String line;
          // Aufruf Spezifikation für Latex-Adapter Skript:
          // "cmd /c $PFAD/compile_latex.bat $NAME $PFAD $PROJEKTNAME
//          String scriptName = ScriptSwitch.calcTexCompilerSkriptCall( pfad, name, project_name );
//        
//        
//          System.out.println( ">>> call now: " + scriptName );
//          
//          Process p = Runtime.getRuntime().exec( scriptName );
//
//          BufferedReader bri = new BufferedReader
//            (new InputStreamReader(p.getInputStream()));
//          BufferedReader bre = new BufferedReader
//            (new InputStreamReader(p.getErrorStream()));
//          while ((line = bri.readLine()) != null) {
//            System.out.println(line);
//            log = log.concat( line + "\n" );
//          }
//          bri.close();
//          while ((line = bre.readLine()) != null) {
//            System.out.println(line);
//          }
//          bre.close();
//          p.waitFor();
//          
//          System.out.println( log );
//          
//          System.out.println("Done.");
//          
          
          /**
           * MAPPING der Properties für den ANT call ...
           * 
           * Somit entkoppelt das Antskript das WebGUI von
           * der Installationsumgebung.
           */

          
          Properties props = new Properties();
          props.put("SRC_File", "src");
          props.put("DEST_Folder", "dest");
          
          CollabProject.initProps( props, project );
          
          SystemAntAdapter.getSystemAntAdapter().runScript("publishPDF.xml", props );
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
        
        
    }
        
    
    
}
