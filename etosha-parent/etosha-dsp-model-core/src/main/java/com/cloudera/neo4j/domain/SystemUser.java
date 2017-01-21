/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.neo4j.domain;

import com.cloudera.neo4j.domain.types.UserType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
 
//@NodeEntity
public class SystemUser implements DaDiEntity {
    
//    @GraphId Long id;

    public String name;
    
    public UserType userType;

//    @Relationship(type="OWNS_FLOW", direction = Relationship.OUTGOING) 
            List<Flow> ownedFlows = new ArrayList<>();

//    @Relationship(type="OWNS_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> ownedData = new ArrayList<>();
    
//    @Relationship(type="NEEDS_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> neededData = new ArrayList<>();
    
//    @Relationship(type="PROVIDES_DATA", direction = Relationship.OUTGOING) 
            List<DataSet> providedData = new ArrayList<>();

    public void ownsFlow(Flow tp) {
        ownedFlows.add( tp );
    }

    @Override
    public Vector<String> getTags() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSubject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String describe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}