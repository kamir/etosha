/**
 * A DFA Link is based on calculated Node Properties.
 * 
 * A DFA Link is created for a pair of fluctuation coefficients.
 * DFA(2) is used by default to calculate the node properties.
 * 
 * The result is given like this:
 *
 *   s  t  s_alphas[0]  s_alphas[1] a_alphas[2]  t_alphas[0] t_alphas[1] t_alphas[2] w[0] w[1] w[2] 
 *
 */
package org.etosha.networks;

import scala.Serializable;
import scala.Tuple2;

/**
 *
 * @author kamir
 */
public class DFALink implements Serializable, Link {

    static String getLinkTypeLabel() {
        return "DFA-Link";
    }

    // we use thre layers in this multilayer link ...
    public double w[] = new double[3];
    
    double a_alphas[] = new double[3];
    double b_alphas[] = new double[3];
    
    public String s = null;
    public String t = null;
   
    /**
     * For a pair of DFA coefficients we calculate the similarity link
     * strength on 3 LAYERs.
     * 
     * @param a
     * @param b 
     */
    public DFALink(DFANetNode a, DFANetNode b) {
        
        for( int i = 0; i < 3; i++ ) {
            
            double wa = a.alphas[i];
            double wb = b.alphas[i];
            
            a_alphas[i] = wa;
            b_alphas[i] = wb;
            
            w[i] = Math.abs( (wa - wb) / ( ( wa + wb ) * 0.5 ) ); 
            
//            if ( a.nodeid.equals(b.nodeid)) {
//            
//                System.out.println( i + " => " + w[i] + "wa=" + wa + "   wb=" + wb );
//                System.out.println( a );
//                System.out.println( b );
//                
//            }
            
        }
        s = a.nodeid;
        t = b.nodeid;
    }

    DFALink(Tuple2<DFANetNode, DFANetNode> p) {
        this( p._1, p._2 );
    }
    
    public String toString() {
//        return s + "\t" + t + "\t" + a_alphas[0] + "\t" + a_alphas[1] + "\t" + a_alphas[2] + "\t" + b_alphas[0] + "\t" + b_alphas[1] + "\t" + b_alphas[2] + "\t" + w[0] + "\t" + w[1] + "\t" + w[2]; 
        return getNodeID(s) + "\t" + getNodeID(t) + "\t" + w[0] + "\t" + w[1] + "\t" + w[2]; 
    }
    
    /**
     * Specific parsing behaviou for a DFA result data set.
     * May change in the future.
     * 
     * @param l
     * @return 
     */
    public String getNodeID( String l ) {
        String[] parts = l.split("accessrate_");
        return parts[1];
    }

    @Override
    public String getSource() {
        return s;
    }

    @Override
    public String getTarget() {
        return t;
    }

    @Override
    public double getWeight(int LAYER) {
        return w[LAYER];
    }
    
}
