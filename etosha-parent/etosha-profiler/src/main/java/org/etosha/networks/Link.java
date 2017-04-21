/**
 * We represent a Link in a functional network based on calculated
 * values derived from measured node properties.
 * 
 * 
 */
package org.etosha.networks;

/**
 *   What ever is relevant for all types of links in this packages
 *   comes here.
 */
public interface Link {
    
    // node IDs are good labels
    public String s = null;
    public String t = null;
    
    // we use 3 Layers by default.
    public double[] w = null;

    public String getSource();

    public String getTarget();

    public double getWeight(int LAYER);
    
}
