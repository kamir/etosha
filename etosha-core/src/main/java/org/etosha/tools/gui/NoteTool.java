package org.etosha.tools.gui;

public class NoteTool {

	public static String getNote() {

		String note = javax.swing.JOptionPane.showInputDialog("Note:");
		
		return "\n====Note====\n" + note + "\n";
		
	}

}
