/*
 * Copyright 2015 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.etosha.sandbox.triplifier;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;

 
/**
 *
 * @author kamir
 */
public class AttributePredicateMapper {
    
    // LOAD THE SOLR schema and read the URI ANNOTATION FROM FIELD COMMENTS !!!
    
    
    static public Property getPropertyForSOLRAttribute( String attributeName, Model m ) {
        Property p = m.createProperty( "http://semanpix.de/etosha-property/" + attributeName );
        return p;
    }
    
     
    
}
