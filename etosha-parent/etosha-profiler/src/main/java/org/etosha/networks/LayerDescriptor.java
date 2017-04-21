/**
 *
 *
 **/
package org.etosha.networks;

import java.util.Comparator;

/**
 * This Layer-Descriptor holds relevant metadata to access the right layer
 * in multilayer correlation networks.
 * 
 * 
 * 
 * @author kamir
 */
public class LayerDescriptor {
   
    private Class linkType = null;
    
    private Class nodeType = null;
    
    private Comparator<Link> linkComparator = null;
    
    private int multiLinkLayerSelector = 0;

    /**
     * @return the linkType
     */
    public Class getLinkType() {
        return linkType;
    }

    /**
     * @param linkType the linkType to set
     */
    public void setLinkType(Class linkType) {
        this.linkType = linkType;
    }

    /**
     * @return the nodeType
     */
    public Class getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(Class nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the linkComparator
     */
    public Comparator<Link> getLinkComparator() {
        return linkComparator;
    }

    /**
     * @param linkComparator the linkComparator to set
     */
    public void setLinkComparator(Comparator<Link> linkComparator) {
        this.linkComparator = linkComparator;
    }

    /**
     * @return the multiLinkLayerSelector
     */
    public int getMultiLinkLayerSelector() {
        return multiLinkLayerSelector;
    }

    /**
     * @param multiLinkLayerSelector the multiLinkLayerSelector to set
     */
    public void setMultiLinkLayerSelector(int multiLinkLayerSelector) {
        this.multiLinkLayerSelector = multiLinkLayerSelector;
    }
    
}
