
package com.cloudera.neo4j.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;

 
//@NodeEntity
public class DataSet implements DaDiEntity {
    
//    @GraphId Long id;
    
    String label = "DS";

    public String title = "???";
    
//    @Relationship(type="CREATED_BY", direction = Relationship.OUTGOING) 
    List<Flow> producers = new ArrayList<Flow>();

//    @Relationship(type="CONSUMED_BY", direction = Relationship.OUTGOING) 
    List<Flow> consumers = new ArrayList<Flow>();

    public void isDerivedFromDatasetViaFlow(DataSet ds, Flow flow2) {

        flow2.consumesData(ds);

        flow2.producesData(this);
        
        producers.add(flow2);
        
    }

    /**
     * DaDiEintity
     * 
     * @return 
     */
    @Override
    public Vector<String> getTags() {
        Vector<String> v = new Vector<String>();
        v.add("DATA_SET");
        return v;
    }
    
    /**
     * DaDiEintity
     * 
     * @return 
     */
    @Override
    public String getSubject() {
        return this.title;
    }

    /**
     * DaDiEintity
     * 
     * @return 
     */
    @Override
    public Properties getProperties() {
        
        Properties props = new Properties();
        props.put("label", "DS");
        props.put("TITLE", title);
        
        for( Flow f : producers ) {
            props.put("CREATED_BY", f.title );
        }

        for( Flow f : consumers ) {
            props.put("CONSUMED_BY", f.title );
        }

        return props;
    }

    @Override
    public String describe() {
        return "DESCRIPTION OF THE DATASET in human readable form ... ";
    }



}