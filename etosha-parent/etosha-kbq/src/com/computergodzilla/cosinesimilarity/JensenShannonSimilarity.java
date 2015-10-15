package com.computergodzilla.cosinesimilarity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Class to calculate cosine similarity
 *
 * @author Mubin Shrestha
 */
public class JensenShannonSimilarity {

    DocVector d1;
    DocVector d2;
    Map<String, Double> p1;
    Map<String, Double> p2;
    int n1 = 0;
    int n2 = 0;
    double mu1 = 0.0;
    double mu2 = 0.0;
    double[] P = null;
    double[] Q = null;
    double[][] PQJ = null;
    
    public double jsd = 0.0;
    
    double normFactor = 1.0;
    private double hPQ;
    private double hQ;
    private double hP;
    double Z = 0.0;
    
    String docpair = "";

    public JensenShannonSimilarity(DocVector _d1, DocVector _d2) throws Exception {

        d1 = _d1;
        d2 = _d2;

        // Verteilung d1
        p1 = _getTFVector(d1);
        // Verteilung d2
        p2 = _getTFVector(d2);

        for( String s : p1.keySet() ) {
            docpair = docpair.concat( s + " ");
        }
        docpair = docpair.concat( " ### " );
        for( String s : p2.keySet() ) {
            docpair = docpair.concat( s + " ");
        }        
        
        n1 = p1.size();
        n2 = p2.size();

        mu1 = (double)n1 / (double)(n1 + n2);
        mu2 = (double)n2 / (double)(n1 + n2);

        calcAllignedVectors(p1, p2);
        
        normFactor = - mu1 * Math.log(mu1)  -  mu2 * Math.log(mu2) ;
        
        jsd = calcJSD(P, Q);
        
//        if ( jsd < 0.0 ) throw new Exception( "WRONG MATH !!! ");
//        if ( jsd > 1.0 ) throw new Exception( "WRONG MATH !!! ");
        
        //dump();

    }

    public static Map<String, Double> _getTFVector(DocVector d) {

        Map<String, Double> termfreq = new HashMap<String, Double>();
        Map<String, Double> t2 = new HashMap<String, Double>();
        
        if ( d == null ) return t2;

        double sumCountTotal = 0.0;
        Set<String> keys = d.terms.keySet();
        for (String key : keys) {
            double count = d.vector2.getEntry(d.terms.get(key).intValue());
            if (count > 0) {
                //System.out.println( "###### " + d.id + " : *** " + key + " => " + d.terms.get(key).intValue() + " ===> " + count );
                termfreq.put(key, count);
                sumCountTotal=sumCountTotal+count;
            }
        }
        
        
        for (String key : termfreq.keySet()) {
            t2.put(key, termfreq.get(key)/ sumCountTotal );
        }

        return t2;

    }

    private void calcAllignedVectors(Map<String, Double> p1, Map<String, Double> p2) {
        Vector<String> allKeySorted = new Vector<String>();

        Set<String> k1 = p1.keySet();
        Set<String> k2 = p2.keySet();

        allKeySorted.addAll(k1);
        for( String s : k2) {
            if ( !allKeySorted.contains(s) )
                allKeySorted.addElement(s);
        }

        Collections.sort(allKeySorted);

        P = new double[allKeySorted.size()];
        Q = new double[allKeySorted.size()];
        PQJ = new double[allKeySorted.size()][2];
        
        int i = 0;
        for (String k : allKeySorted) {
            Double v1 = p1.get(k);
            Double v2 = p2.get(k);

            // System.out.println(i + " " + k + "  ***>>> " + v1 + " : " + v2);

            if (v1 == null) {
                v1 = 0.0;
            }
            if (v2 == null) {
                v2 = 0.0;
            }
        
            if ( v1 * v2 != 0.0 ) {
                PQJ[i][0] = v1;
                PQJ[i][1] = v2;
            }
            else {
                PQJ[i][0] = 0.0;
                PQJ[i][1] = 0.0;
            }
            
            P[i] = v1;
            Q[i] = v2;
            
            i++;
        }

    }
    
    private double H( double[] p1, double[] p2 , double m1, double m2) {double sum = 0.0;
        for( int i = 0; i < p1.length ; i++ ) {
            //System.out.println( "p1=" + p1[i] + "  p2=" + p2[i] + "  sum=" + sum );
            double v = mu1 * p1[i] + mu2 * p2[i];
            if ( ( v ) != 0.0  ) 
                sum = sum - v * Math.log(v);
        }
        return sum;
    }

    private double H( double[][] p, double m1, double m2) {double sum = 0.0;
        for( int i = 0; i < p.length ; i++ ) {
            //System.out.println( "p1=" + p1[i] + "  p2=" + p2[i] + "  sum=" + sum );
            double v = mu1 * p[i][0] + mu2 * p[i][1];
            if ( ( v ) != 0.0  ) 
                sum = sum - v * Math.log(v);
        }
        return sum;
    }

    
    private double H( double[] p) {
        
        double sum = 0.0;
        for( int i = 0; i < p.length ; i++ ) {
            double prop = p[i];
            //System.out.println( "                            prop=" + prop + " sum=" + sum );
            if ( prop != 0.0 ) 
                sum = sum - prop * Math.log(prop);
        }
        return sum;
    }

    private double calcJSD(double[] P, double[] Q) {

        hPQ = H(P,Q, mu1, mu2);
        hP = H(P);
        hQ = H(Q);
               
        Z = ( hPQ - mu1 * hP - mu2 * hQ );
        
        return Z / normFactor;
    
    }

    private void dump() {
        
        System.out.println( "DOC: " + this.docpair );

        
        System.out.println( "n1=" + this.n1 );
        System.out.println( "n2=" +this.n2 );
        
        System.out.println( "mu1=" + this.mu1 );
        System.out.println( "mu2=" + this.mu2 );
        
        
        
        
        System.out.println( "hPQ=" + hPQ );
        System.out.println( "hP=" + hP );
        System.out.println( "hQ=" + hQ );
        
        System.out.println( " Z=" + Z );
        
        
        System.out.println( "f=" +normFactor );
        System.out.println( "---------------"  );
        
        System.out.println( "JSD=" +jsd );
        
        System.out.println( "==============="  );
        
    }

 
}