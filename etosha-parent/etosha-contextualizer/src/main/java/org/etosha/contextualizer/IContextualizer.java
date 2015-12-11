package org.etosha.contextualizer;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

public interface IContextualizer {

	public abstract void open();

	public abstract void close();

	public abstract void putNSPO(String n, String s, String p, String o);

	public abstract void putSPO(String s, Property p, String o);

	public abstract void init();
 
        public abstract String getName();
	
        public abstract void addGraph( Model m );

    public Model getModel();

    public Model getNewPartition(String distGraphFolder, String label, String name);

    public void persistPartition();

}