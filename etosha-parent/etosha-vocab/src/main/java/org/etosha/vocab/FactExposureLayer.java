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
package org.etosha.vocab;

import java.util.Hashtable;
import org.apache.jena.rdf.model.Property;

/**
 * 
 * 
 * @author kamir
 */
interface FactExposureLayer {
   
    /**
     * For each fact layer we control access permissions via Sentry.
     * 
     * Here we define a group of Properties, which are organized in 
     * a fact layer and to which the access can be allowed or not, because
     * a person has no permission to a given layer.
     * 
     * This means, the facts must all be "labeled" by the layer they belong to
     * for filtering them later according to the loged in user role.
     * 
     * Here we use the group to provdie a graph with facts about a resource, 
     * which includes more or less details, according to the exposure layer. 
     * This means, already during the export of metadata we can limit, which 
     * prperties are exposed.
     * 
     * For internal use, inside the same organization, we expose all we know.
     * For public use, we expose just a restricted set of facts, and for official 
     * collaborations we work with flexible definitions, to adapt to real world
     * requirements.   
     * 
     * @return 
     */
    public Hashtable<Property,String> getPropertyToAttributeMapping();
   
}
