/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import data.CollabProject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *
 * @author kamir
 */
public class SVNTool {
    
    static String svnPfad = "C:/Program Files/Subversion/bin/svn.exe";
    
    public static void commit( CollabProject pro, String comment ) { 
        if ( pro.useSVN ) {
        System.out.println(">>> commit data ... [" + pro.name +"]");
        try {
          String line;
          Process p = Runtime.getRuntime().exec( svnPfad + " commit " + pro.getBaseFolder().getAbsolutePath() + " -m \""+comment+"\"");
          BufferedReader bri = new BufferedReader
            (new InputStreamReader(p.getInputStream()));
          BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));
          while ((line = bri.readLine()) != null) {
            System.out.println(line);
          }
          bri.close();
          while ((line = bre.readLine()) != null) {
            System.out.println(line);
          }
          bre.close();
          p.waitFor();
          System.out.println("Done.");
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
        }
        else { 
          System.out.println(">>> SVN is not activated ...");
        }
    }
    
    public static String getCurrentRevision( CollabProject pro ) { 
        
        String s = "";
        
        if ( pro.useSVN ) {
            { 
        
        System.out.println(">>> commit data ... [" + pro.name +"]");
        
        s = "<b>Letzte Revision:</b><br><hr> ";
        try {
          String line;
          Process p = Runtime.getRuntime().exec( svnPfad + " info " + pro.getBaseFolder().getAbsolutePath());
          BufferedReader bri = new BufferedReader
            (new InputStreamReader(p.getInputStream()));
          BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));
          while ((line = bri.readLine()) != null) {
            System.out.println(line);
            s = s.concat( line + "<br/>" );
          }
          bri.close();
          while ((line = bre.readLine()) != null) {
            System.out.println(line);
          }
          bre.close();
          p.waitFor();
          System.out.println("Done.");
        }
        catch (Exception err) {
          err.printStackTrace();
        }    
        
            }
  }          else { 
                s = "<b> SVN ist nicht aktiviert.</b></br>";
                System.out.println(">>> SVN deactivated.");
            
        }
        return s;
    }
    
    public static String createFolder( File folder, CollabProject project ) { 

        String s = "";

        if ( project.useSVN ) {
        System.out.println(">>> create folder ... [" + folder.getAbsolutePath() +"]");
        
        s = "<b>Letzte Revision:</b><br><hr> ";
        try {
          String line;
          Process p = Runtime.getRuntime().exec( svnPfad + " add " + folder.getAbsolutePath() );
          BufferedReader bri = new BufferedReader
            (new InputStreamReader(p.getInputStream()));
          BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));
          while ((line = bri.readLine()) != null) {
            System.out.println(line);
            s = s.concat( line + "<br/>" );
          }
          bri.close();
          while ((line = bre.readLine()) != null) {
            System.out.println(line);
          }
          bre.close();
          p.waitFor();
          
          SVNTool.commit(project, "auto-commit-after create");
          
          System.out.println("Done.");
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
        
    }
        else { 
                s = "<b> SVN ist nicht aktiviert.</b></br>";
                System.out.println(">>> SVN deactivated.");
            
        }
        
        return s;
    }
    
    public static void addFile( File file, CollabProject project ) { 
        System.out.println(">>> add file ... [" + file.getAbsolutePath() +"]");
        
        if ( project.useSVN ) {
        
        try {
          String line;
          Process p = Runtime.getRuntime().exec( svnPfad + " add " + file.getAbsolutePath() );
          BufferedReader bri = new BufferedReader
            (new InputStreamReader(p.getInputStream()));
          BufferedReader bre = new BufferedReader
            (new InputStreamReader(p.getErrorStream()));
          while ((line = bri.readLine()) != null) {
            System.out.println(line);
             
          }
          bri.close();
          while ((line = bre.readLine()) != null) {
            System.out.println(line);
          }
          bre.close();
          p.waitFor();
          
          SVNTool.commit(project, "auto-commit-after create");
          
          System.out.println("Done.");
        }
        catch (Exception err) {
          err.printStackTrace();
        }        
        
        }
        else { 
          System.out.println(">>> SVN ist nicht aktiviert. ");

        }
        
    }
    
}
