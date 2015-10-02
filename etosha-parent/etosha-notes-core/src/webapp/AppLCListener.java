package webapp;

import data.CFG;
import data.CollabProject;
import java.util.Date;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import tools.antscripts.SystemAntAdapter;
import tools.gnuplot.GnuplotCompilerToolAdapter;
import tools.latex.TexCompilerToolAdapter;


/**
 * Application Life Cycle Listener
 * 
 * @author kamir
 */
public class AppLCListener implements ServletContextListener	{

	public static long applicationInitialized = 0L;

	/* Application Startup Event */
	public void	contextInitialized(ServletContextEvent ce) {
		applicationInitialized = System.currentTimeMillis();
                Date d = new Date(applicationInitialized);
                System.out.println(">>> AppContext initialized ... at: " + d );
                
                String pfad = ce.getServletContext().getInitParameter("CE_TEMP_FOLDER");
                String drive = ce.getServletContext().getInitParameter("CE_TEMP_FOLDER_DRV");
                

                System.out.println(">>> use drive: " + drive + pfad);
                
                CFG.setDefaultBasePath( drive + pfad );
                
                CollabProject pro = CollabProject.getDefaultProjekt();
                ce.getServletContext().setAttribute( "PROJEKT", pro );
                
                CFG.TEMPLATE_BASE = ce.getServletContext().getInitParameter("VM_TEMPLATE_FOLDER");
                
                // CFG-Template Base ... muss auf WEB-INF/vm gesetzt werden ...
                
            
//                SystemAntAdapter.ANT_basePath = CFG.getDefaultBasePath() + "/ant/" ;
                
                TexCompilerToolAdapter.COMPILE_CMD = ce.getServletContext().getInitParameter("COMPILE_LATEX_CMD");
                GnuplotCompilerToolAdapter.COMPILE_CMD = ce.getServletContext().getInitParameter("COMPILE_GNUPLOT_CMD");

        }

	/* Application Shutdown	Event */
	public void	contextDestroyed(ServletContextEvent ce) {
            System.out.println(">>> AppContext destroyed ... " );
        }
 
    
}
