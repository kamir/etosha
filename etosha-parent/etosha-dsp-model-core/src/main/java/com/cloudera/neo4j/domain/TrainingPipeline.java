/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.neo4j.domain;

import java.util.ArrayList;
import java.util.List;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
 
//@NodeEntity
public class TrainingPipeline extends Flow {

    public TrainingPipeline() {
        super();
    }
    
//    @Relationship(type="CREATES_MODEL_DATA", direction = Relationship.OUTGOING) 
    public List<DataSet> models = new ArrayList<>();


    public void producesModel(DataSet dsMODEL) {
        models.add( dsMODEL );
    }

}