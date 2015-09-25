package contextualizer;

import impl.BridgedContextualizer;
import impl.JenaInMemoryContextualizer;
import impl.SMWContextualizer;
import impl.SimpleContextualizer;

public class ContextualizerFactory {
	
	public static IContextualizer getSimpleContextualizer( String fn ) {
		SimpleContextualizer sc = new SimpleContextualizer();
		sc.setFileName(fn);
		sc.init();
		sc.open();
		return sc;
	}

	/**
	 * Loads initial data into the InMemory-Model
	 * 
	 * @param string
	 * @return
	 */
	public static IContextualizer getJenaInMemoryContextualizer(String string) {
		JenaInMemoryContextualizer cj = new JenaInMemoryContextualizer();
		
		return cj;
	}

	public static IContextualizer getSMWContextualizer(String string) {
		SMWContextualizer sc = new SMWContextualizer();

		return sc;
	}

	String DEFAULT_WIKI = null;
	
	public static IContextualizer getSMWContextualizer() {
		return getSMWContextualizer(SMWContextualizer.DEFAULT_WIKI);
	}

	public static IContextualizer getBridgedContextualizer() {
		BridgedContextualizer sc = new BridgedContextualizer();
		return sc;
	}

}
