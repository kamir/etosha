/**
 * 
 * Eine Weiche fuepr die Aufrufe der Skripte im BS. 
 *  
 */
package tools;

import data.CFG;
import java.lang.String;

/**
 *
 * @author kamir
 */
public class ScriptSwitch {
    
    public static int mode_WIN_dev = 0;
    public static int mode_LINUX_dev = 1;

    public static int mode = mode_WIN_dev;
    
    static public String[] latexScriptCall= { 
        "cmd /c "
    }; 

    static public String[] latexScriptName = { 
        "compile_latex.bat"
    }; 
    
    static public String[] latexScriptPath = { 
        ""
    }; 
    
    public static String calcTexCompilerSkriptCall(String pfad, String name, String project_name) {
        String s = latexScriptCall[mode];
        
        String s2 = latexScriptPath[mode]
                   + pfad + "" + latexScriptName[mode] + " " + name + " " + project_name + "\\ " + CFG.getDefaultBasePath();
        
        s2 = s2.replace('/', '\\');

        s = s + " " + s2;
        
        System.out.println( "DEBUG: " + s );
        return s;
    }
    
    public static void main( String[] args ) { 
        System.out.println( calcTexCompilerSkriptCall("A:", "B", "C") );
    } 
    
    
    
    
}
