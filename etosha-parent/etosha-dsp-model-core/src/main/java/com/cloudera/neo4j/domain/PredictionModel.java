
package com.cloudera.neo4j.domain;

import java.util.ArrayList;
import java.util.List;
//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
 
//@NodeEntity
public class PredictionModel extends DataSet {
    
    public PredictionModel() {
        super();
    }
    
//    @GraphId Long id;
    
    String label = "PM";

    public String title;
    
    public String goal;
    
    public Double accuracy;
    
//    @Relationship(type="TRAIN_PARAMETERS", direction = Relationship.OUTGOING) 
            List<TrainingParameters> serializedTrainingParameters = new ArrayList<>();

//    @Relationship(type="MODEL_PARAMETERS", direction = Relationship.OUTGOING) 
            List<ModelParameters> serializedModelParameters = new ArrayList<>();

}