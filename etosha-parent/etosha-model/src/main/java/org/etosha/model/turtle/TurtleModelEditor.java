package org.etosha.model.turtle;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
 

import com.hp.hpl.jena.rdf.listeners.StatementListener;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * <p>A Swing-based GUI window that provides a simple Turtle-based
 * editor and inspector for Jena models. Useful for debugging GUI
 * and web applications. To open an editor window, pass the model
 * instance to the static {@link ModelEditor#open(Model)} method.</p>
 * 
 * <p>The editor has basic reporting of Turtle syntax errors.
 * It also updates the namespace prefixes of the model.
 * Several windows for different models may be open at the same
 * time. Concurrent changes to the model are reported.</p>
 * 
 * <p>The class has a {@link #main} method for demonstration purposes.
 * It loads one or more RDF files into Jena models and displays an editor
 * for each.</p>
 * 
 * @version $Id$
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class TurtleModelEditor {
	private final static int WINDOW_MIN_WIDTH = 400;
	private final static int WINDOW_MIN_HEIGHT = 200;
	
	// We randomize the position of new windows
	private final static Random random = new Random(19790715);

	/**
	 * Opens a new editor window and binds it to the given model.
	 * @param sourceModel A Jena model
	 * @return A reference to the new editor window
	 */
	public static TurtleModelEditor open(Model sourceModel) {
		return new TurtleModelEditor(sourceModel, "TurtleModelEditor");
	}
	
	/**
	 * Opens a new editor window and binds it to the given model.
	 * A custom title is useful to distinguish multiple editor
	 * windows for different models.
	 * @param sourceModel A Jena model
	 * @param title A custom title for the editor window
	 * @return A reference to the new editor window
	 */
	public static TurtleModelEditor open(Model sourceModel, String title) {
		return new TurtleModelEditor(sourceModel,
				title + " - Jena Model Editor");
	}

	/**
	 * Main method for demonstration purposes. Takes a number of
	 * filename or URL arguments. Reads them as RDF/XML or Turtle
	 * (if ends with ".n3" or ".ttl"). Displays an editor for
	 * each. If the same filename appears twice, then both editors
	 * will use the same model.
	 * @param args
	 */
	public static void main(String[] args) {
                
                String URL1 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:Ask/-5B-5BCategory:Fach-5D-5D/-3F%3DFach-23/-3FHas-20Abbreviation%3DAbk%C3%BCrzung/-3FHasSpecialAbbreviation%3DSpezialabk%C3%BCrzung-20(y-2Fn)/-3FIs-20taught-20at%3DHochschulart/format%3Drdf/mainlabel%3DFach/offset%3D0";
                args = new String[1];
                args[0] = URL1;
                
                
		if (args.length == 0) {
			System.out.println("Please specify one or more "
					+ "RDF filenames or URLs.");
			return;
		}
		Map filenamesToModels = new HashMap();
		for (int i = 0; i < args.length; i++) {
			if (!filenamesToModels.containsKey(args[i])) {
				String url = args[i];
				if (url.indexOf(":") == -1) {
					url = "file:" + url;
				}
				Model m = ModelFactory.createDefaultModel();
				String lang =
					(url.endsWith(".n3") || url.endsWith(".ttl"))
					? "N3" : "RDF/XML";
				m.read(url, lang);
				filenamesToModels.put(args[i], m);
			}
			Model m = (Model) filenamesToModels.get(args[i]);
			open(m, args[i]);
		}
                
                
	}
	
	/**
	 * The model bound to the editor.
	 */
	Model boundModel;
	
	JFrame window;
	JTextArea turtleTextArea;
	JLabel cursorPositionLabel;
	JPanel buttons;
	StatementListener listener;
	
	/**
	 * true when the model has been changed by another part of the
	 * system
	 */
	boolean outOfSync;
	
	/**
	 * true while we've locked the model. Assumption: All changes to
	 * the model reported by the listener during this period have
	 * been caused by us.
	 */
	boolean weAreEditingTheModel = false;

	/**
	 * Creates and displays new ModelEditor.
	 * @param sourceModel The model bound to the editor
	 * @param title The full title of the editor window
	 */
	private TurtleModelEditor(Model sourceModel, String title) {
		this.window = new JFrame(title) {
			// Remove listener from bound model when window is closed
			public void dispose() {
				TurtleModelEditor.this.boundModel.unregister(
						TurtleModelEditor.this.listener);
				super.dispose();
			}
		};
		this.boundModel = sourceModel;
		init();
		fetchTurtleFromModel();
		this.window.setVisible(true);
	}
	
	/**
	 * Parses the contents of the Turtle text area and adds
	 * all statements and namespace prefixes to the model.
	 * Displays an error message if the contents are invalid.
	 */
	protected synchronized void addTurtleToModel() {
		lockModel();
		Model contents = getContentsAsModel();
		if (contents == null) {	// Syntax error?
			return;
		}
		
		// Namespace prefixes
		Iterator it = contents.getNsPrefixMap().keySet().iterator();
		while (it.hasNext()) {
			String prefix = (String) it.next();
			String uri = contents.getNsPrefixURI(prefix);
			if (!uri.equals(this.boundModel.getNsPrefixURI(prefix))) {
				this.boundModel.setNsPrefix(prefix, uri);
			}
		}
		
		this.boundModel.add(contents);
		this.turtleTextArea.requestFocusInWindow();
		unlockModel();
	}
	
	/**
	 * Parses the contents of the Turtle text area and removes
	 * all statements and namespace prefixes from the model.
	 * Displays an error message if the contents are invalid.
	 */
	protected synchronized void removeTurtleFromModel() {
		lockModel();
		Model contents = getContentsAsModel();
		if (contents == null) {	// syntax error?
			return;
		}

		// Namespace prefixes
		Iterator it = contents.getNsPrefixMap().keySet().iterator();
		while (it.hasNext()) {
			String prefix = (String) it.next();
			this.boundModel.removeNsPrefix(prefix);
		}
		
		this.boundModel.remove(contents);
		this.turtleTextArea.requestFocusInWindow();
		unlockModel();
	}
	
	/**
	 * Parses the contents of the Turtle text area and replaces
	 * all statements and namespace prefixes in the model with
	 * those from the text area.
	 * Displays an error message if the contents are invalid.
	 */
	protected synchronized void replaceModelWithTurtle() {
		lockModel();
		Model contents = getContentsAsModel();
		if (contents == null) {	// syntax error?
			return;
		}
		
		// Namespace prefixes
		Iterator it = this.boundModel.getNsPrefixMap().keySet().iterator();
		while (it.hasNext()) {
			String prefix = (String) it.next();
			this.boundModel.removeNsPrefix(prefix);
		}
		this.boundModel.setNsPrefixes(contents);
		
		this.boundModel.removeAll();
		this.boundModel.add(contents);
		
		this.turtleTextArea.requestFocusInWindow();
		unlockModel();

		// Model contains the contents of the text area so we're synced
		this.outOfSync = false;
	}

	/**
	 * Replaces the contents of the text area with a Turtle
	 * serialization of the model. 
	 */
	protected void fetchTurtleFromModel() {
		
		// Serialize model and update text area
		StringWriter writer = new StringWriter();
		this.boundModel.write(writer, "N3");
		setContents(writer.toString());
		
		this.turtleTextArea.requestFocusInWindow();
		moveCursorTo(1, 1);
		
		// Text area now contains model so we're synced
		this.outOfSync = false;
	}
	
	/**
	 * @return The current contents of the Turtle text area
	 */
	protected String getContentsAsTurtle() {
		return this.turtleTextArea.getText();
	}

	/**
	 * Parses the current contents of the Turtle text area
	 * and returns them as a Jena model. Will show an error
	 * message to the user and return null if the contents
	 * are not syntactically valid.
	 * @return The parsed contents of the Turtle text area
	 */
	protected Model getContentsAsModel() {
		Model result = ModelFactory.createDefaultModel();
		StringReader reader = new StringReader(getContentsAsTurtle());
//		try {
			result.read(reader, "", "N3");
			return result;
//		} catch (N3Exception ex) {	// syntax error?
//			// Split error message into line, column, parser message
//			Pattern p = Pattern.compile(".*\\[([0-9]+):([0-9]+)\\] (.*)");
//			Matcher m = p.matcher(ex.getMessage());
//			if (!m.matches()) {
//				throw new RuntimeException(
//						"Unexpected error format: " + ex.getMessage());
//			}
//
//			// Show error message
//			JOptionPane.showMessageDialog(
//					this.window, m.group(3), "Parse Error", 
//					JOptionPane.ERROR_MESSAGE);
//
//			// Then highlight the offending character
//			int line = Integer.parseInt(m.group(1));
//			int column = Integer.parseInt(m.group(2));
//			highlightCharacter(line, column);
//			
//			// null return means there was a syntax error
//			return null;
//		}
	}

	/**
	 * Sets the contents of the Turtle text area.
	 * @param text The new contents
	 */
	protected void setContents(String text) {
		this.turtleTextArea.setText(text);
	}

	/**
	 * @return The line number of the current cursor position
	 */
	protected int getCurrentCursorLine() {
		try {
			return this.turtleTextArea.getLineOfOffset(
					this.turtleTextArea.getCaretPosition()) + 1;
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @return The column number of the current cursor position
	 */
	protected int getCurrentCursorColumn() {
		try {
			int line = this.turtleTextArea.getLineOfOffset(
					this.turtleTextArea.getCaretPosition());
			return this.turtleTextArea.getCaretPosition() -
					this.turtleTextArea.getLineStartOffset(line) + 1;
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Selects the character at the given position to indicate the
	 * location of a syntax error.
	 * @param line A cursor position
	 * @param column A cursor position
	 */
	protected void highlightCharacter(int line, int column) {
		try {
			this.turtleTextArea.requestFocusInWindow();
			int offset = this.turtleTextArea.getLineStartOffset(line - 1) +
					column - 1;
			this.turtleTextArea.setCaretPosition(offset + 1);
			this.turtleTextArea.moveCaretPosition(offset);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Moves the cursor to a given position.
	 * @param line A cursor position
	 * @param column A cursor position
	 */
	protected void moveCursorTo(int line, int column) {
		try {
			this.turtleTextArea.requestFocusInWindow();
			int offset = this.turtleTextArea.getLineStartOffset(line - 1) +
					column - 1;
			this.turtleTextArea.setCaretPosition(offset);
			notifyCursorPositionChanged();
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Updates the cursor position label.
	 */
	protected void notifyCursorPositionChanged() {
		this.cursorPositionLabel.setText(
				getCurrentCursorLine() + " : " + getCurrentCursorColumn());
	}

	/**
	 * Shows a message about a concurrent modification of the
	 * model to the user, except if we have locked the model
	 * (then we assume the change has been by ourselves), or if
	 * model and text area are already out of sync (then the
	 * user has already seen the message earlier).
	 */
	protected synchronized void notifyConcurrentChange() {
		if (this.weAreEditingTheModel || this.outOfSync) {
			return;
		}
		this.outOfSync = true;
		
		// This is called by the model's worker thread and
		// the error dialog blocks the thread until the user
		// clicks OK, so we better show the dialog in another
		// thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(TurtleModelEditor.this.window,
						"The model has been changed by another part of the system.\n" +
						"Re-fetch the contents of the model to get the latest changes.",
						"Concurrent Change", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	private void lockModel() {
		this.weAreEditingTheModel = true;
		this.boundModel.enterCriticalSection(false);
	}
	
	private void unlockModel() {
		this.weAreEditingTheModel = false;
		this.boundModel.leaveCriticalSection();
	}

	/**
	 * Sets up the window.
	 */
	private void init() {
		// Add listener to the model
		this.listener = new StatementListener() {
			public void addedStatement(Statement s) {
				notifyConcurrentChange();
			}
			public void removedStatement(Statement s) {
				notifyConcurrentChange();
			}
		};
		this.boundModel.register(this.listener);
		
		// set up Turtle text area
		this.turtleTextArea = new JTextArea();
		this.turtleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.turtleTextArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				// update cursor position label
				notifyCursorPositionChanged();
			}
		});
		// make text area scrollable
		JScrollPane scroller = new JScrollPane(this.turtleTextArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setPreferredSize(new Dimension(400, 300));
		
		// set up the right-side panel with the buttons
		this.buttons = new JPanel();
		this.buttons.setLayout(
				new BoxLayout(this.buttons, BoxLayout.PAGE_AXIS));
		this.buttons.setBorder(
				BorderFactory.createEmptyBorder(0, 12, 0, 16));

		// buttons
		makeButton("Add this to the model", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTurtleToModel();
			}
		});
		makeButton("Remove this from the model", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTurtleFromModel();
			}
		});
		makeButton("Fetch contents of the model", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetchTurtleFromModel();
			}
		});
		makeButton("Replace contents of the model", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceModelWithTurtle();
			}
		});

		// cursor position label
		this.cursorPositionLabel = new JLabel("Status bar");
		this.cursorPositionLabel.setFont(
				new Font("Dialog", Font.PLAIN, 10));
		this.buttons.add(Box.createVerticalGlue());
		this.buttons.add(this.cursorPositionLabel);

		// put pieces together and add some borders
		Container contentPane = this.window.getContentPane();
		contentPane.add(scroller, BorderLayout.CENTER);
		contentPane.add(this.buttons, BorderLayout.EAST);
		contentPane.add(Box.createRigidArea(
				new Dimension(16, 0)), BorderLayout.WEST);
		contentPane.add(Box.createRigidArea(
				new Dimension(0, 12)), BorderLayout.NORTH);
		contentPane.add(Box.createRigidArea(
				new Dimension(0, 12)), BorderLayout.SOUTH);

		// Hack to prevent resizing the window below a minimum size
		this.window.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int width = TurtleModelEditor.this.window.getWidth();
				int height = TurtleModelEditor.this.window.getHeight();
				boolean doResize = false;
				if (width < WINDOW_MIN_WIDTH) {
					width = WINDOW_MIN_WIDTH;
					doResize = true;
				}
				if (height < WINDOW_MIN_HEIGHT) {
					height = WINDOW_MIN_HEIGHT;
					doResize = true;
				}
				if (!doResize) {
					return;
				}
				TurtleModelEditor.this.window.setSize(width, height);
			}
		});
		
		// DISPOSE_ON_CLOSE so we can have multiple windows running
		this.window.setDefaultCloseOperation(
				WindowConstants.DISPOSE_ON_CLOSE);
		this.window.pack();

		// Randomly set initial window positions (otherwise they would
		// all sit overlapping in the top left corner)
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.window.setLocation(
				random.nextInt(screen.width - this.window.getWidth()),
				random.nextInt(screen.height - this.window.getHeight()));
	}

	/**
	 * Helper function that creates a button
	 * @param label The button's label
	 * @param action
	 */
	private void makeButton(String label,
			ActionListener action) {
		JButton newButton = new JButton(label) {
			// Try to cover the whole width of its parent container.
			// We use this because we want all buttons to have the
			// same width.
			public Dimension getMaximumSize() {
				Dimension dim = super.getMaximumSize();
				return new Dimension(
						Short.MAX_VALUE,
						(int) dim.getHeight());
			}
		};
		newButton.addActionListener(action);
		this.buttons.add(newButton);
		// 6 pixels spacing between this and next button
		this.buttons.add(Box.createRigidArea(new Dimension(0, 6)));
	}
}