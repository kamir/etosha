package org.etosha.networks;

import scala.Serializable;

/**
 * For each individual node we calculate DFA(2) and extract the
 * correlation coefficient alpha for 3 ranges.
 * 
 * The DFANetNode is used to calculate a similarity network for nodes 
 * based on DFA coefficient.
 * 
 * @author kamir
 */
public class DFANetNode implements Serializable, Node  {

    /**
     * Node properies we use for the similarity measure.
     */
    String nodeid = null;
    double[] alphas = null;

    /**
     * Create a node from a textline.
     * 
     * @param line 
     */
    public DFANetNode(String line) {
        String[] seg = line.split("\t");
        nodeid = seg[0];
        alphas = new double[3];
        alphas[0] = Double.parseDouble(seg[2]);
        alphas[1] = Double.parseDouble(seg[3]);
        alphas[2] = Double.parseDouble(seg[4]);
    }

    public String toString() {
        return nodeid + "\t" + alphas[0] + "\t" + alphas[1] + "\t" + alphas[2];
    }

    public String toHeader() {
        return "#nodeid\talpha_0\talpha_1\talpha_2";
    }
}
