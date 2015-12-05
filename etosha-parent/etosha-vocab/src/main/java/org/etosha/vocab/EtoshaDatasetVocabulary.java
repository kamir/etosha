package org.etosha.vocab;
 
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
    public static final Property isOfHDFSDatasetType = m.createProperty( uri + "isOfHDFSDatasetType" );    
    /**
     * HOW DO I MODEL an enum in the schema????
     */
 
}