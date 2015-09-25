import contextualizer.ContextIsReadOnlyException;
import contextualizer.ContextualizerFactory;
import contextualizer.IContextualizer;


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
		

		String s = "S";
		String p = "P";
		String o = "O";
		String n = "N";
		
		contexter.putSPO(s, p, o);
		contexter.putNSPO(n, s, p, o);
		
		try {
			contexter.initDEMO();
			
			// show all neighbors (outlink) 
			
			// show all neighbors (inlik) 

			// show categories of all neighbors

			// show all neighbors by category 
					
			// show all neighbors[nr outlinks] by category[nr of elements in this cat] 
	
		
		} 
		catch (ContextIsReadOnlyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

		

		
	}

}
