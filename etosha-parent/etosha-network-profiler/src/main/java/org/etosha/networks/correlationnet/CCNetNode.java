package org.etosha.networks.correlationnet;

import org.etosha.networks.Node;
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
    private String nodeid = null;
    private int activity = 0;
    
    /**
     * Create a node from a textline.
     * 
     * @param line 
     */
    public CCNetNode(String line) {
        String[] seg = line.split("\t");
    }

    public String toString() {
        return getNodeid() + "\t" + getActivity();
    }

    public String toHeader() {
        return "#nodeid\tactivity";
    }

    /**
     * @return the nodeid
     */
    public String getNodeid() {
        return nodeid;
    }

    /**
     * @param nodeid the nodeid to set
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    /**
     * @return the activity
     */
    public int getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(int activity) {
        this.activity = activity;
    }
}
