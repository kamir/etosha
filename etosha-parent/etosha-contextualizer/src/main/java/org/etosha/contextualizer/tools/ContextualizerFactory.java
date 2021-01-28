package org.etosha.contextualizer.tools;

import org.etosha.contextualizer.IContextualizer;
import org.etosha.contextualizer.impl.*;

public class ContextualizerFactory {
	
//	public static IContextualizer getSimpleContextualizer( String fn ) {
//		SimpleContextualizer sc = new SimpleContextualizer();
//		sc.setFileName(fn);
//		sc.init();
//		sc.open();
//		return sc;
//	}

	/**
	 * Loads initial data into the InMemory-Model
	 * 
	 * @return
	 */
	public static IContextualizer getJenaInMemoryContextualizer(String s) {
            
		JenaInMemoryContextualizer cj = new JenaInMemoryContextualizer();
        cj.setDefaultFilename( s );

        return cj;

	}

 

//	public static IContextualizer getSMWContextualizer(String string) {
//		SMWContextualizer sc = new SMWContextualizer();
//
//		return sc;
//	}

	String DEFAULT_WIKI = null;
	
//	public static IContextualizer getSMWContextualizer() {
//		return getSMWContextualizer(SMWContextualizer.DEFAULT_WIKI);
//	}

	public static IContextualizer getBridgedContextualizer() {
//		BridgedContextualizer sc = new BridgedContextualizer();
//		return sc;
            
            return null;
	}

    public static IContextualizer getHBaseContextualizer() {

		HBaseContextualizer ch = new HBaseContextualizer();
		ch.init();
		return ch;

    }
}
