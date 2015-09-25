package org.etosha.networks;

import scala.Serializable;

/**
 * For each individual node we collect access-activity.
 * 
 * The CCNetNode is used to represent a correlation network for nodes 
 * based on access time series.
 * 
 * @author kamir
 */
public class CCNetNode implements Serializable, Node  {

    /**
     * Node properies we use for the similarity measure.
     */
    String nodeid = null;
    int activity = 0;
    
    /**
     * Create a node from a textline.
     * 
     * @param line 
     */
    public CCNetNode(String line) {
        String[] seg = line.split("\t");
    }

    public String toString() {
        return nodeid + "\t" + activity;
    }

    public String toHeader() {
        return "#nodeid\tactivity";
    }
}
