
package com.cloudera.neo4j.domain;

import java.util.ArrayList;
import java.util.List;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
 
//@NodeEntity
public class MappingModel extends DataSet {
    
    public MappingModel() {
        super();
    }
    
//    @GraphId Long id;
    
    String label = "MM";

    public String title;
    
    public String goal;

    /** 
     * Percentage of available mappings
     * 
     * 0.0 : if only candidates exist.
     * 1.0 : if for all candidates a mapping is defined.
     * 
     * Simple model: 1:1 matching
     * 
     * Advanced: 1:n mappings with probabilities are possible.
     * 
     **/   
    public Double accuracy;
   
}