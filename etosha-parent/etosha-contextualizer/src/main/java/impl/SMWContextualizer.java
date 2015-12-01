package impl;

import contextualizer.IContextualizer;
import org.apache.jena.rdf.model.Property;

/**
 * Human readable contextualization is important. We ue two backends in
 * parallel. One is the SMW and any of the others will allow us a direct access
 * via SPARQL instead of the SMW-ask syntax. But, SMW is not considered to be a
 * triple store only.
 *
 * We use it as a documentation-manager for our cluster activity. * All data is
 * collected during runtime, can be persisted, exported and even imported from
 * remote locations such as the SMW or other triple stores.
 *
 * We use it in the Spark-Shell and in the Etosha-cli;
 *
 * @author training
 *
 */
public class SMWContextualizer implements IContextualizer {

    public static final String DEFAULT_WIKI = "www.semanpix.de/opendata/wiki";

    @Override
    public void open() {
		// TODO Auto-generated method stub

    }

    @Override
    public void close() {
		// TODO Auto-generated method stub

    }

    @Override
    public void putNSPO(String n, String s, String p, String o) {
		// TODO Auto-generated method stub

    }

    @Override
    public void putSPO(String s, Property p, String o) {
		// TODO Auto-generated method stub

    }

    @Override
    public void init() {
		// TODO Auto-generated method stub

    }

 

    @Override
    public String getName() {
        return this.getClass().getName();
    }

}
