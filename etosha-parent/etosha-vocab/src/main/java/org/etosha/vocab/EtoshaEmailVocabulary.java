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
public class EtoshaEmailVocabulary {

        public static Model m = ModelFactory.createDefaultModel();

    protected static final String uri = "http://www.semanpix.de/2015/etosha-mail/1.0#";

    /**
     * returns the URI for this schema
     *
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    /**
     * <p>
     * The namespace of the vocabalary as a resource</p>
     */
    public static final Resource NAMESPACE = m.createResource(uri);

    public static final Property sender = m.createProperty(uri + "sender");
    
    public static final Property receiver = m.createProperty(uri + "receiver");

    public static final Property subject = m.createProperty(uri + "subject");

    public static final Property bodyRaw = m.createProperty(uri + "bodyRaw");

    public static final Property bodyText = m.createProperty(uri + "bodyText");

    public static final Property dateSent = m.createProperty(uri + "dateSent");

    public static final Property dateReceived = m.createProperty(uri + "dateReceived");

    public static final Property hasParts = m.createProperty(uri + "hasParts");

    public static final Property nrOfParts = m.createProperty(uri + "nrOfParts");
    
    public static final Property hasLabel = m.createProperty(uri + "hasLabel");

    public static final Property hasMailID = m.createProperty(uri + "hasMailID");

    public static final Property belongsToThread = m.createProperty(uri + "belongsToThread");

}
