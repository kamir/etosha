/**
 *
 *
 **/
package org.etosha.networks;

import java.util.Comparator;

/**
 *
 * @author kamir
 */
public class LayerDescriptor {
   
    Class linkType = null;
    Class nodeType = null;
    
    Comparator<Link> linkComparator = null;
    
    int multiLinkLayerSelector = 0;
    
}
