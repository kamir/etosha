package com.computergodzilla.cosinesimilarity;

/**
 * Class to calculate cosine similarity
 * @author Mubin Shrestha
 */
public class CosineSimilarity {  
    
    public static double CosineSimilarity(DocVector d1,DocVector d2) {
        if ( d1==null) return 0.0;
        if ( d2==null) return 0.0;
        
        double cosinesimilarity;
        try {
            cosinesimilarity = (d1.vector.dotProduct(d2.vector))
                    / (d1.vector.getNorm() * d2.vector.getNorm());
        } catch (Exception e) {
            return 0.0;
        }
        return cosinesimilarity;
    }
    
}