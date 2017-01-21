/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.neo4j.domain;

import com.cloudera.neo4j.domain.types.DataFlowType;
import java.util.ArrayList;
import java.util.List;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
 
//@NodeEntity
public class ProcessingPipeline extends Flow {
    
    public ProcessingPipeline() {
        super();
    }

    public ProcessingPipeline(PredictionModel pm) {
        super();
        models.add(pm);
    }
    
//    @Relationship(type="USES_MODEL_DATA", direction = Relationship.OUTGOING) 
            List<PredictionModel> models = new ArrayList<>();

    
}