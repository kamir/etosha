package org.etosha.contextualizer.impl;

import org.etosha.contextualizer.ContextIsReadOnlyException;
import org.etosha.contextualizer.ContextualizerFactory;
import org.etosha.contextualizer.IContextualizer;
import static org.etosha.contextualizer.impl.JenaInMemoryContextualizer.model;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

/**
 * Human readable contextualization is important. We ue two backends in
 * parallel. One is the SMW and any of the others will allow us a direct access
 * via SPARQL instead of the SMW-ask syntax. But, SMW is not considered to be a
 * triple store only.
 *
 * We use it as a documentation-manager for our cluster activity.
 *
 * All data is collected during runtime, can be persisted, exported and even
 * imported from remote locations such as the SMW or other triple stores.
 *
 * We use it in the Spark-Shell and in the Etosha-cli;
 *
 * @author training
 *
 */
abstract public class BridgedContextualizer implements IContextualizer {

        @Override
    public Model getModel() {
        return model;
    }
    IContextualizer c1 = null;
    IContextualizer c2 = null;

    @Override
    public String getName() {
        
        String name = this.getClass().getName();
        name = name + "\n\t" + c1.getName();
        name = name + "\n\t" + c2.getName();
        
        return name;
    }

    public BridgedContextualizer() {
//        c1 = ContextualizerFactory.getJenaInMemoryContextualizer("test.ttl");
//        c2 = ContextualizerFactory.getSMWContextualizer(SMWContextualizer.DEFAULT_WIKI);
    }

    @Override
    public void open() {
        c1.open();
        c2.open();
    }

    @Override
    public void close() {
        c1.close();
        c2.close();
    }

    @Override
    public void putNSPO(String n, String s, String p, String o) {
        c1.putNSPO(n, s, p, o);
        c2.putNSPO(n, s, p, o);
    }

    @Override
    public void putSPO(String s, Property p, String o) {
        c1.putSPO(s, p, o);
        c2.putSPO(s, p, o);
    }

    @Override
    public void init() {
        c1.init();
        c2.init();
    }

    @Override
    public void addGraph(Model m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 

}
