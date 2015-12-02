
import org.etosha.vocab.EtoshaDatasetVocabulary;

import org.apache.jena.rdf.model.Property;
import contextualizer.ContextualizerFactory; 
import contextualizer.IContextualizer;

/**
 *
 * @author kamir
 */
class Exposer {

    // Bridged-Context is the default ...
    static IContextualizer contexter = null;

    static void init() {

        // Bridged-Context is the default ...
        contexter = ContextualizerFactory.getBridgedContextualizer();

    }

    static void close() {

        // Bridged-Context is the default ...
        contexter.close();

    }
    
    static void exposeToDefaultStore(Scrapelet currentSnippet) {
        try {

            System.out.println( "### " + currentSnippet );
            
            /**
             * ************
             *
             * Here we take the information about the snippet which has to be
             * exposed.
             */
            String s = "S";
            Property p = EtoshaDatasetVocabulary.isOwnedBy;
            String o = "O";
            String n = "N";

            // Here we process the snippet ...
            contexter.putSPO(s, p, o);

        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void persist() {
        
        // The contexter should do a persist instead of a close call .....
        
        contexter.close();
 
    }

}
