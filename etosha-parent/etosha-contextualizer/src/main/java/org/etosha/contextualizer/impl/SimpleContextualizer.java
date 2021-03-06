package org.etosha.contextualizer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.etosha.contextualizer.ContextIsReadOnlyException;
import org.etosha.contextualizer.IContextualizer;
import static org.etosha.contextualizer.impl.JenaInMemoryContextualizer.model;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

/**
 * The simplest contextualisation approach produces triples and stores them in a
 * file, locally.
 *
 * The file contains what is relevant to work with triples or quads in it.
 *
 * @author training
 *
 */
abstract public class SimpleContextualizer implements IContextualizer {

    private String fileName;
    OutputStream out = null;

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void init() {
        try {
            out = new FileOutputStream(new File(getFileName()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see impl.IContextualizer#open()
     */
    @Override
    public void open() {

    }

    /* (non-Javadoc)
     * @see impl.IContextualizer#close()
     */
    @Override
    public void close() {

    }

    /**
     * A subject is connected to an object via a predicate.
     *
     * @param s
     * @param p
     * @param o
     */
    @Override
    public void putSPO(String s, Property p, String o) {

    }

    /**
     * A subject is connected to an object via a predicate. The graph name is n.
     *
     * (see: https://en.wikipedia.org/wiki/Named_graph)
     *
     * @param s
     * @param p
     * @param o
     */
    @Override
    public void putNSPO(String n, String s, String p, String o) {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void addGraph(Model m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     

}
