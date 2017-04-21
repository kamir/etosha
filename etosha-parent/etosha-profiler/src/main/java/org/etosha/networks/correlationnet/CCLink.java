/**
 * A CC-Link is based on calculated link-property per pair of nodes.
 * 
 * s: source node
 * t: target node
 * 
 * w[]: array of weights for a link 
 * 
 */
package org.etosha.networks.correlationnet;
 
import org.etosha.networks.Link;
import org.json.simple.*;
import scala.Serializable;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openide.util.Exceptions;

/**
 *
 * @author kamir
 */
public class CCLink implements Serializable, Link {
    
    private static boolean debug = false;

    public static String getLinkTypeLabel() {
        return "CC-Link";
    }

    /**
     * @return the debug
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * @param aDebug the debug to set
     */
    public static void setDebug(boolean aDebug) {
        debug = aDebug;
    }

    // we use thre layers in this multilayer link ...
    private double w[] = new double[5];
    
    
    private String s = "s";
    private String t = "t";
 
    private String groupKey = null;

    /**
     * For a pair of Access-Time-Series we calculate a similarity link.
     *
     * @param a
     * @param b
     */
    public CCLink(String line) {
 
        try {
            
            if ( debug ) 
                System.out.println(line);
            
            String[] l = new String[5];
            JSONParser parser = new JSONParser();
            
            Object obj = parser.parse(line);
 
            JSONObject jsonObject = (JSONObject) obj;
 
            s = (String) jsonObject.get( "np_source" );  
            t = (String) jsonObject.get( "np_target" );
            
            s = s.replace(".accessrate_" ," ");
            s = s.replace("_BIN=24_log10"," ");
            
            t = t.replace(".accessrate_" ," ");
            t = t.replace("_BIN=24_log10"," ");
            
            l[0] = "" + jsonObject.get("cc_linkA");
            l[1] = "" + jsonObject.get("cc_linkB");
            l[2] = "" + jsonObject.get("cc_linkC");
            l[3] = "" + jsonObject.get("cc_linkD");
            l[4] = "" + jsonObject.get("mi_info");
            
            w[0] = Double.parseDouble( l[0] );
            w[1] = Double.parseDouble( l[1] );
            w[2] = Double.parseDouble( l[2] );
            w[3] = Double.parseDouble( l[3] );
            w[4] = Double.parseDouble( l[4] );
            
            if ( debug )
                System.out.println( this );
        
        } 
        catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public String toString() {
//        return s + "\t" + t + "\t" + a_alphas[0] + "\t" + a_alphas[1] + "\t" + a_alphas[2] + "\t" + b_alphas[0] + "\t" + b_alphas[1] + "\t" + b_alphas[2] + "\t" + w[0] + "\t" + w[1] + "\t" + w[2]; 
        return getS() + "\t" + getT() + "\t" + getW()[0] + "\t" + getW()[1] + "\t" + getW()[2] + "\t" + getW()[3]+ "\t" + getW()[4];
    }

    /**
     * Specific parsing behaviou for a DFA result data set. 
     * May change in the future.
     *
     * @param l
     * @return
     */
    public String _getNodeID(String l) {
        String[] parts = l.split("accessrate_");
        return parts[1];
    }
    
    private String target(String s) {
        return s.split( ".accessrate_" )[2];
    }

    private String source(String string) {
        return getS().split( ".accessrate_" )[1];
    }

    @Override
    public String getSource() {
        return getS();
    }

    @Override
    public String getTarget() {
        return getT();
    }

    @Override
    public double getWeight(int LAYER) {
        return getW()[LAYER];
    }

    /**
     * @return the w
     */
    public double[] getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(double[] w) {
        this.w = w;
    }

    /**
     * @return the s
     */
    public String getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(String s) {
        this.s = s;
    }

    /**
     * @return the t
     */
    public String getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(String t) {
        this.t = t;
    }

    /**
     * @return the groupKey
     */
    public String getGroupKey() {
        return groupKey;
    }

    /**
     * @param groupKey the groupKey to set
     */
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }



}
