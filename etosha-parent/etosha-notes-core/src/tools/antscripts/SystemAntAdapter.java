package tools.antscripts;

import java.io.File;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/**
 * Quelle:  http://www.ibm.com/developerworks/websphere/library/techarticles/0502_gawor/0502_gawor.html
 *
 * @author kamir
 */
public class SystemAntAdapter {

    public static SystemAntAdapter getSystemAntAdapter() { 
        return new SystemAntAdapter();
    }
    
    public static String ANT_basePath = "C:/TEST/ant/";
    
    private static void initProps() {
        props = new Properties();
        props.put("name", "System" );
    }

    static Properties props = null;
    
    public static void main(String args[]) {

        initProps();
        
        SystemAntAdapter adapter = new SystemAntAdapter();
        adapter.runScript( "buildtest.xml" , null );
    
    }
    
    public void runScript( String name, Properties pro ) {
        
        if ( pro == null ) pro = props;
        
        
 
        File buildFile = new File( ANT_basePath + name );
        
        Project p = new Project();
        p.setUserProperty("ant.file", buildFile.getAbsolutePath());
        
        /**
         * Eine Property überschreiben ...
         */
        // p.setProperty("name", "Mirko");
        
        /**
         * alle Properties aus einem Properties-Object setzen ...
         */
        for( String key: pro.stringPropertyNames()) {
            p.setProperty( key , pro.getProperty(key));
            System.out.println( key + " \t " + pro.getProperty(key) );
        }

        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        p.addBuildListener(consoleLogger);


        try {
            p.fireBuildStarted();
            p.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            p.addReference("ant.projectHelper", helper);
            helper.parse(p, buildFile);
            p.executeTarget(p.getDefaultTarget());
            p.fireBuildFinished(null);
        } 
        catch (BuildException e) {
            p.fireBuildFinished(e);
        }
        
    }
}
