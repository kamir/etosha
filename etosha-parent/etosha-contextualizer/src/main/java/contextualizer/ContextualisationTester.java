package contextualizer;

/**
 * 
 * This tool overwrites the triple store data with test data.
 * 
 * The TripleStoreRunner uses the generated data to expose a TTL file
 * via FUSEKI.
 * 
 * Whenever a DEFAULT-JENA-IN-RAM contextualizer is used, you can simply 
 * expose its data via FUSEKI.
 * 
 * Maybe it is also good to create an additional copy in this case, otherwise
 * you may loose data by running the next test.
 * 
 */

import contextualizer.ContextIsReadOnlyException;
import contextualizer.ContextualizerFactory;
import org.etosha.vocab.EtoshaDatasetVocabulary;
import contextualizer.IContextualizer;
import org.apache.jena.rdf.model.Property;


public class ContextualisationTester {

	public static void main(String[] args) { 
		
		ContextualisationTester tester = new ContextualisationTester();
		
		IContextualizer contexter1 = ContextualizerFactory.getSimpleContextualizer("test.ttl");
		tester.testContexter(contexter1);
	
		IContextualizer contexter2 = ContextualizerFactory.getJenaInMemoryContextualizer("test.ttl");
		tester.testContexter(contexter2);

//		IContextualizer contexter3 = ContextualizerFactory.getFusekiContextualizer("test.ttl");
//		tester.testContexter(contexter3);

		IContextualizer contexter4 = ContextualizerFactory.getSMWContextualizer();
		tester.testContexter(contexter4);

		IContextualizer contexter5 = ContextualizerFactory.getBridgedContextualizer();
		tester.testContexter(contexter5);
		
	}		
	
	public void testContexter(IContextualizer contexter) {
		
            System.out.println( "[TEST] " + contexter.getName() );

		String s = "S";
                
                
                Property p = EtoshaDatasetVocabulary.isOwnedBy;
                // String p2 = "isOwnedBy";
		
                String o = "O";
		String n = "N";
		
		contexter.putSPO(s, p, o);
		// contexter.putNSPO(n, s, p2, o);
		
		try {

			// show all neighbors (outlink) 
			
			// show all neighbors (inlik) 

			// show categories of all neighbors

			// show all neighbors by category 
					
			// show all neighbors[nr outlinks] by category[nr of elements in this cat] 
	
                        contexter.close();
		
		} 
		catch (Exception e) {
                    e.printStackTrace();
		}
				

		

		
	}

}
