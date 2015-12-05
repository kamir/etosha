package org.etosha.vocab;

import org.apache.jena.rdf.model.*;

/**
 *
 * @author kamir
 *
 * EtoshaNetworkVocabulary
 *
 * vocabulary class for namespace http://www.semanpix.de/2015/etosha-net/1.0#
 * 
 * Statistical properties of networks are described by this ontology.
 *
 */
public class EtoshaNetworkVocabulary {

    private static Model m = ModelFactory.createDefaultModel();

    protected static final String uri = "http://www.semanpix.de/2015/etosha-net/1.0#";

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

    /** <p>Provides the filterthreshold for link creation of this graph (linkstrength is greater than TS).</p> */
    public static final Property TS = m.createProperty( uri + "TS" );
    
    /** <p>Provides the number of edges of this graph.</p> */
    public static final Property zEdges = m.createProperty( uri + "ZEDGES" );

    /** <p>Provides the number of nodes of this graph.</p> */
    public static final Property zNodes = m.createProperty( uri + "ZNODES" );
    
    /** <p>Provides the number of clusters this graph.</p> */
    public static final Property zClusters = m.createProperty( uri + "ZCLUSTERS" );
    
    /** <p>Provides the global diameter of this graph.</p> */
    public static final Property diameter = m.createProperty( uri + "DIAMETER" );

    /** <p>Provides the global clustering coefficient of this graph.</p> */
    public static final Property globalClusterCoefficient = m.createProperty( uri + "CLUSTERING_GLOBAL" );
    
    
    
    
 
}

/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
