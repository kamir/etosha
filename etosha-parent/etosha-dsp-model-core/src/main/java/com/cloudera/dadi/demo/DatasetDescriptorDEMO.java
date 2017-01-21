package com.cloudera.dadi.demo;


//------------------------------------------------------------------------------
//
// !!! NEEDS REFACTORING !!!
//
//------------------------------------------------------------------------------
// This class is using the logic for our RDF view.
// 
// Views project the domain model, which initially was Neo4J centric,
// because of the OMG framework we used for visualization, to different 
// data representations.
// 
// We use: CN (for Cloduera Navigator)
//         Neo4J  
//         HBase 
//         Jena (for RDF)
//         Wiki (for Mediawiki with SemanticMediaWiki extension) 
//------------------------------------------------------------------------------

//
// We implement a first Use-Case 
//
// A dataset, which will be used for classification with a Random-Forrest
// classifier has to be labeled.
// 
import com.cloudera.descriptors.util.JSONEntityDescriptor;
import java.io.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource; 
/**
 *
 * @author kamir
 */
public class DatasetDescriptorDEMO {

    public static void main(String[] args) throws IOException {

        String RDF_OutputLocation = "./data/rdf/demo_dataset_1.rdf";

        System.out.println("The RDF outputpath is: " + RDF_OutputLocation + ")");

        DatasetDescriptorDEMO demo = null;

        try {

            demo = new DatasetDescriptorDEMO(RDF_OutputLocation);

            JSONEntityDescriptor descr = new JSONEntityDescriptor();
            descr.initDEMODESCRIPTOR();

            String json = descr.toJSON();

        } catch (Exception ex) {
            System.out.println("Cannot write to location. " + ex.getMessage());
            System.exit(-1);
        }

    }

    /**
     * Constructor
     *
     * @param indexDir the name of the folder in which the index should be
     * created
     * @throws java.io.IOException when exception creating index.
     */
    DatasetDescriptorDEMO(String modelFileName) throws IOException {

        // a temporary local model:
        Model m = ModelFactory.createDefaultModel();

        BufferedWriter bw = new BufferedWriter(new FileWriter(modelFileName));

        // etosha create dataset tweetsTTIP_CETA; // defines an entity (as page)
        DatasetContextualizer.createDataset(m, "tweetsTTIP_CETA");

        // etosha create datasource flumeAgent01; // defines an entity (as page)
        DatasetContextualizer.createDatasource(m, "flumeAgent01");

        // etosha link tweetsTTIP_CETA ‘hasSource’ flumeAgent01; // links S and O via P 
        DatasetContextualizer.linkDatasetToDatasource(m, "tweetsTTIP_CETA", "flumeAgent01");

        // etosha add tag to dataset tweetsTTIP_CETA to column col1 max=10000
        String column = "col1";
        String key = "typeOfIdsUsed";
        String value = "twitterUserAccounts";
        DatasetContextualizer.addTagToDatasetToColumn(m, "tweetsTTIP_CETA", column, key, value);

        // etosha add tag to dataset tweetsTTIP_CETA isAbout TTIP
        key = "isAbout";
        value = "CETA";
        DatasetContextualizer.addTagToDataset(m, "tweetsTTIP_CETA", key, value);

        // etosha add tag to dataset tweetsTTIP_CETA isAbout CETA
        value = "TTIP";
        DatasetContextualizer.addTagToDataset(m, "tweetsTTIP_CETA", key, value);

        m.write(bw, "N-TRIPLES");

        m.write(System.out, "N-TRIPLES");

        m.close();
 
        
//        
//        /**
//         * 
//         * Variante (A)
//         *  
//         */ 
//        Driver driver = GraphDatabase.driver("bolt://host", AuthTokens.basic("neo4j", "8cwrr"));
//        Session session = driver.session();
//
//        
//        /**
//         * 
//         * Varainte (B)
//         * 
//         */
//        GraphDatabaseSettings.BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );
//        GraphDatabaseService graphDb = new GraphDatabaseFactory()
//        .newEmbeddedDatabaseBuilder( new File("__NEU4J") )
//        .setConfig( bolt.type, "BOLT" )
//        .setConfig( bolt.enabled, "true" )
//        .setConfig( bolt.address, "ihost:7687" )
//        .newGraphDatabase();
//         
//
//        session.run("CREATE (z:Person {name:'Arthur', title:'King'})");
//        session.run("CREATE (x:Person {name:'Brunhild', title:'Dragon'})");
//
//        session.run("CREATE (x)-[knows:KNOWS]-> (z)" );
//
//        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
//         
//
//        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = 'Arthur' RETURN a.name AS name, a.title AS title");
//        while (result.hasNext()) {
//            Record record = result.next();
//            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
//        }
//
//        session.close();
//        driver.close();

    }
    
    
 

}

/**
 * All tags and properties for the semantic export are created here.
 *
 * @author kamir
 */
class DatasetContextualizer {

    static void addTagToDataset(Model m, String dataset, String key, String value) {
        Resource s = m.createResource(dataset);
        Property p = m.createProperty(key);
        m.add(s, p, value);
    }

    static void addTagToDatasetToColumn(Model m, String dataset, String column, String key, String value) {

        Resource s1 = m.createResource(column);
        Property p1 = m.createProperty(key);
        m.add(s1, p1, value);

        Resource s2 = m.createResource(dataset);
        Property p2 = m.createProperty("hasColumn");
        m.add(s2, p2, column);

        Property p3 = m.createProperty("isColumnIn");
        m.add(s1, p3, dataset);

    }

    static void createDataset(Model m, String dataset) {
        Resource s1 = m.createResource(dataset);
        Property p1 = m.createProperty("isOftype");
        m.add(s1, p1, "raw:dataset");
    }

    static void createDatasource(Model m, String datasource) {
        Resource s1 = m.createResource(datasource);
        Property p1 = m.createProperty("isOftype");
        m.add(s1, p1, "unbounded:datasource");
    }

    static void linkDatasetToDatasource(Model m, String dataset, String datasource) {
        Resource s1 = m.createResource(datasource);
        Property p1 = m.createProperty("produces");
        m.add(s1, p1, dataset);

        Resource s2 = m.createResource(dataset);
        Property p2 = m.createProperty("isProducedby");
        m.add(s2, p2, datasource);
    }

}
