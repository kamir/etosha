package org.etosha.tools.gui;

public class NoteTool {

        /**
         * 
         * Request the NOTE data to store it in Etosha metastore.
         * 
         * The return value is the WIKI Markup code which will be added 
         * to an existing page as a "Note".
         * 
         * @return 
         */
	public static String getNote() {

		String note = javax.swing.JOptionPane.showInputDialog("Note:");
		
		return "\n====Note====\n" + note + "\n";
		
	}
        
        

}
