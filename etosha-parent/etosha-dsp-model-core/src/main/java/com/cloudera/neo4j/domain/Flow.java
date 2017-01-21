/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.neo4j.domain;

import com.cloudera.neo4j.domain.types.DataFlowType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
// 
//@NodeEntity
public class Flow implements DaDiEntity {
    
//    @GraphId Long id;

    public String title;
    
    public DataFlowType flowType;

//    @Relationship(type="CONSUMES_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> consumed = new ArrayList<>();
    
//    @Relationship(type="PRODUCES_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> created = new ArrayList<>();

//    @Relationship(type="ENRICHES_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> enriched = new ArrayList<>();

    
    public void consumesData(DataSet d) {
        consumed.add( d );
    }

    public void producesData(DataSet d) {
        created.add( d );
    }

    public void enrichesDataset(DataSet d) {
        enriched.add( d );
    }

    @Override
    public Vector<String> getTags() {
        Vector<String> tags = new Vector();
        return tags;
    }

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        props.put("title", title);
        props.put("flowType", flowType);
        return props;
    }

    @Override
    public String getSubject() {
        return this.title;
    }

    @Override
    public String describe() {
        return this.title + " => " + getProperties().size();
    }
    
}