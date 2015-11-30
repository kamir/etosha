
import contextualizer.ContextualizerFactory;
import contextualizer.IContextualizer;

/**
 *
 * @author kamir
 */
class Exposer {

    static void exposeToDefaultRepository(Scrapelet currentSnippet) {

        // Bridged-Context is the default ...
        IContextualizer contexter = ContextualizerFactory.getBridgedContextualizer();

        System.out.println("[TEST] " );
        System.out.println( contexter.getClass().getName() );

        String s = "S";
        String p = "P";
        String o = "O";
        String n = "N";

        // Here we process the snippet ...
        
        
        
        contexter.putSPO(s, p, o);
        contexter.putNSPO(n, s, p, o);

        try {
            contexter.initDEMO();

			// show all neighbors (outlink) 
			// show all neighbors (inlik) 
			// show categories of all neighbors
			// show all neighbors by category 
			// show all neighbors[nr outlinks] by category[nr of elements in this cat] 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
