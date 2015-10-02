/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author kamir
 */
public class CFG {
    
    /**
     * The place in the webapp where the templates are.
     */
    static public String TEMPLATE_BASE = "/GITHUB/SEMANPIX.WS/bitOceanTB/build/web/WEB-INF/vm";
    // static public String TEMPLATE_BASE = "C:/DEV2013/semanpix/bitoceanTB/build/web/WEB-INF/vm";
    
    static String defaultBasePath = "/opt/semanpixCOPY";
    
    static String wikiURL = "http://www.semanpix.de/opendata/wiki";

    static String refinderURL = "?";

    public static void setDefaultBasePath(String pfad) {
        System.out.println(">>> defaultBasePath - old: " + defaultBasePath + " -> new: " + pfad );
        defaultBasePath = pfad;
    }
    
    public static String getDefaultBasePath() {
        return defaultBasePath;
    }

        
    static public boolean useSVN = false;
    static public boolean useGIT = false;
    
    
}
