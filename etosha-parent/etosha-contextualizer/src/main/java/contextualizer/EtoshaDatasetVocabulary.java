
package contextualizer;
 
import org.apache.jena.rdf.model.*;

/**
 *
 * @author kamir
 *
 * EtoshaNetworkVocabulary
 *
 * vocabulary class for namespace http://www.semanpix.de/2015/etosha-net/1.0#
 *
 */
public class EtoshaDatasetVocabulary {

    private static Model m = ModelFactory.createDefaultModel();

    protected static final String uri = "http://www.semanpix.de/2015/etosha-dataset/1.0#";

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

    public static final Property isOwnedBy = m.createProperty( uri + "isOwnedBy" );
    
    
 
}