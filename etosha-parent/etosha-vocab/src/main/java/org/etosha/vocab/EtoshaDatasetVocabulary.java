package org.etosha.vocab;
 
import java.util.Hashtable;
import org.apache.jena.rdf.model.*;

/**
 *
 * @author kamir
 *
 * EtoshaDatasetVocabulary
 *
 * vocabulary class for namespace http://www.semanpix.de/2015/etosha-ds/1.0#
 *
 */
public class EtoshaDatasetVocabulary {
    
    static FactExposureLayer layer1_PUBLIC = null; // fixed
    static FactExposureLayer layer2_PROTECTED = null; // variable
    static FactExposureLayer layer3_PRIVATE = null; // fixed
   
    public static void initLayers() {
        layer1_PUBLIC = new ExposureLayer1();
//        layer2_PROTECTED = new ExposureLayer2();
//        layer3_PRIVATE = new ExposureLayer3();
    }
    
    private static Model m = ModelFactory.createDefaultModel();

    protected static final String uri = "http://www.semanpix.de/2015/etosha-ds/1.0#";

    /**
     * returns the URI for this schema
     *
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }
    
    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m.createResource( uri );

    
    
    /** <p>Referes to a Schema-URI or is not available if no Schema exist.</p> */
    public static final Property hasSchema = m.createProperty( uri + "hasSchema" );

    /** <p>Referes to an owner if such exist.</p> */
    public static final Property isOwnedBy = m.createProperty( uri + "isOwnedBy" );

    /** <p>Referes to a Cluster-descriptor.</p> */
    public static final Property isLocatedInCluster = m.createProperty( uri + "isLocatedInCluster" );

    /** <p>Can be just a bunch of files, an Hive table, a HBase table, an Avro or Parquet file, an Accumulo table or even a Solr Index.</p> */
    public static final Property isOfType = m.createProperty( uri + "isOfHDFSDatasetType" );    
    /**
     * HOW DO I MODEL an enum in the schema????
     */
    
    
 
}

/**
 * 
 * Which of the Hive-Table-Properties will be exposed on which layer?
 * 
 * Here we define the "property name in Hive METADATA" => RDF-Poperty mapping.
 * 
 * @author kamir
 */

class ExposureLayer1 implements FactExposureLayer {

    @Override
    public Hashtable<Property, String> getPropertyToAttributeMapping() {
        
        Hashtable<Property, String> list = new Hashtable<Property, String>();
        
        
        
        // additional props, not available in HIVE
        list.put( EtoshaDatasetVocabulary.isOwnedBy , "getOwner" );
        
        return list;
    
    }
    
    
    
    
}