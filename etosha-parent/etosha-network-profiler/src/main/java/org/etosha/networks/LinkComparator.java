/**
 * 
 * A Link Comparator allows to order Links.
 * 
 * In case of multi-layer links we have to use a Layer value
 * to point to the right field in the weight array. 
 * 
 */
package org.etosha.networks;

import java.util.Comparator;
import scala.Serializable;

/**
 * 
 * A LinkComparator operates on a specific Layer only.
 * 
 * Multi-Layer handling requires a specific aggregation of or combination
 * of the data of each layer. We can handle each component as a dimension in
 * a multidimensional space.
 * 
 * Weighted or unweighted values can be handled in different way.
 * 
 * a) just sum up the squares and calc root of result. 
 * b) use representation like in a "base x" number system. It seems to be hard
 * to define the base, as Link strength values are not bound to a specific range.
 * One can scale the links into such a range, but this requires a bit more research.
 * 
 * 
 * @author kamir
 */
public class LinkComparator implements Comparator<Link>, Serializable {
    
    int LAYER = 0;
    public LinkComparator() {
    }
    
    public LinkComparator( int s ) {
        LAYER = s;
    }
    
    @Override
    public int compare(Link l1, Link l2) {
        return l1.w[LAYER] < l2.w[LAYER] ? 0 : 1;
    }

}
    
