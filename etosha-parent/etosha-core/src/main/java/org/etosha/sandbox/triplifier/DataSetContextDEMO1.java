package org.etosha.sandbox.triplifier;

//
// We implement a first Use-Case 
//
// Jane want to collect data from Twitter for
// market research.
//

import com.hp.hpl.jena.rdf.model.*;


import java.io.*;


/**
 *
 * @author kamir
 */
public class DataSetContextDEMO1 {

    
  public static void main(String[] args) throws IOException {
    
    String RDF_OutputLocation = "./data/rdf/demo1Dataset.rdf";
    
    System.out.println("The RDF outputpath is: " + RDF_OutputLocation + ")");

    DataSetContextDEMO1 demo = null;

    try { 
      
        demo = new DataSetContextDEMO1(RDF_OutputLocation);
      
        String json = demo.getTagsAndPropsAsJSON();
      
    } 
    catch (Exception ex) {
      System.out.println("Cannot write to RDF location. " + ex.getMessage());
      System.exit(-1);
    }

  }

  /**
   * Constructor
   * @param indexDir the name of the folder in which the index should be created
   * @throws java.io.IOException when exception creating index.
   */
  DataSetContextDEMO1(String modelFileName) throws IOException {

    // a temporary local model:
    Model m = ModelFactory.createDefaultModel();

    BufferedWriter bw = new BufferedWriter( new FileWriter( modelFileName ) );
    
    // etosha create dataset tweetsTTIP_CETA; // defines an entity (as page)
    DatasetContextualizer.createDataset( m, "tweetsTTIP_CETA" );  
    
    // etosha create datasource flumeAgent01; // defines an entity (as page)
    DatasetContextualizer.createDatasource( m, "flumeAgent01" );  
    
    // etosha link tweetsTTIP_CETA ‘hasSource’ flumeAgent01; // links S and O via P 
    DatasetContextualizer.linkDatasetToDatasource( m, "tweetsTTIP_CETA", "flumeAgent01" );  

    // etosha add tag to dataset tweetsTTIP_CETA to column col1 max=10000
    String column = "col1";
    String key = "typeOfIdsUsed";
    String value = "twitterUserAccounts";
    DatasetContextualizer.addTagToDatasetToColumn( m, "tweetsTTIP_CETA", column, key, value );  

    
    // etosha add tag to dataset tweetsTTIP_CETA isAbout TTIP
    key="isAbout";
    value="CETA";
    DatasetContextualizer.addTagToDataset( m, "tweetsTTIP_CETA", key, value );  
    
    
    // etosha add tag to dataset tweetsTTIP_CETA isAbout CETA
    value="TTIP";
    DatasetContextualizer.addTagToDataset( m, "tweetsTTIP_CETA", key, value );  
    
    m.write( bw, "N-TRIPLES" );

    m.write( System.out, "N-TRIPLES" );

    m.close();
    
  }

    private String getTagsAndPropsAsJSON() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 

 
}



class DatasetContextualizer {

    static void addTagToDataset(Model m, String dataset, String key, String value) {
        Resource s = m.createResource( dataset );
        Property p = m.createProperty( key );
        m.add( s, p, value ); 
    }

    static void addTagToDatasetToColumn(Model m, String dataset, String column, String key, String value) {

        Resource s1 = m.createResource( column );
        Property p1 = m.createProperty( key );
        m.add( s1, p1, value );

        Resource s2 = m.createResource( dataset );
        Property p2 = m.createProperty( "hasColumn" );
        m.add( s2, p2, column );
        
        Property p3 = m.createProperty( "isColumnIn" );
        m.add( s1, p3, dataset );
        
    }

    static void createDataset(Model m, String dataset) {
        Resource s1 = m.createResource( dataset );
        Property p1 = m.createProperty( "isOftype" );
        m.add( s1, p1, "etosha:dataset" ); 
    }
    
    static void createDatasource(Model m, String datasource) {
        Resource s1 = m.createResource( datasource );
        Property p1 = m.createProperty( "isOftype" );
        m.add( s1, p1, "unbounded:datasource" ); 
    }

    static void linkDatasetToDatasource(Model m, String dataset, String datasource) {
        Resource s1 = m.createResource( datasource );
        Property p1 = m.createProperty( "produces" );
        m.add( s1, p1, dataset );

        Resource s2 = m.createResource( dataset );
        Property p2 = m.createProperty( "isProducedby" );
        m.add( s2, p2, datasource ); 
    }


}
