package org.etosha;

/**
 * This tool overwrites the triple store data with test data.
 * <p>
 * The TripleStoreRunner uses the generated data to expose a TTL file
 * via FUSEKI.
 * <p>
 * Whenever a DEFAULT-JENA-IN-RAM contextualizer is used, you can simply
 * expose its data via FUSEKI.
 * <p>
 * Maybe it is also good to create an additional copy in this case, otherwise
 * you may loose data by running the next test.
 */

import org.apache.jena.rdf.model.Property;
import org.etosha.contextualizer.IContextualizer;
import org.etosha.contextualizer.tools.ContextualizerFactory;
import org.etosha.gmail.GmailCollector;
import org.etosha.gmail.gui.GmailLabelSelectorFrame;
import org.etosha.vocab.EtoshaDatasetVocabulary;

import java.io.IOException;
import java.net.URISyntaxException;


public class ContextualisationTester {

    public static void main(String[] args) throws IOException, URISyntaxException, Exception {

        ContextualisationTester tester = new ContextualisationTester();

//		IContextualizer contexter1 = ContextualizerFactory.getSimpleContextualizer("test.ttl");
//		tester.testContexter(contexter1);

//		IContextualizer contexter2 = ContextualizerFactory.getJenaInMemoryContextualizer("test.ttl");
//		tester.testContexter(contexter2);

//		IContextualizer contexter3 = ContextualizerFactory.getFusekiContextualizer("test.ttl");
//		tester.testContexter(contexter3);

//		IContextualizer contexter4 = ContextualizerFactory.getSMWContextualizer();
//		tester.testContexter(contexter4);

//		IContextualizer contexter5 = ContextualizerFactory.getBridgedContextualizer();
//		tester.testContexter(contexter5);

        IContextualizer contexter6 = ContextualizerFactory.getHBaseContextualizer();
        tester.testContexter(contexter6);

        runGMailCollector( contexter6 );

    }

    private static void runGMailCollector(IContextualizer contexter6) throws IOException, URISyntaxException, Exception {

        GmailCollector gmix = new GmailCollector();
        GmailLabelSelectorFrame.open(gmix);

        // GmailLabelSelectorFrame.openLOGEDOUT(gmix);

    }

    public void testContexter(IContextualizer contexter) {

        System.out.println("[TEST] " + contexter.getName());

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
