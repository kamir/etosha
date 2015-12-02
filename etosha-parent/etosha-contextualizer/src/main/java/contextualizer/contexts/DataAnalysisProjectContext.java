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
package contextualizer.contexts;

import java.util.Vector;

/**
 *
 * DA => Data Analysis Project
 * 
 * @author kamir
 */
public class DataAnalysisProjectContext {
    
    Vector<String> category = null;
    
    /**
     * For the tests we use the 6 static categories in the ConceptBrowser
     * on the DEMO page (http://www.semanpix.de/opendata/etosha/v2/cb.html).
     */
    public DataAnalysisProjectContext() {
        category.add("Project");     // http://semanpix.de/opendata/wiki/index.php?title=Category:Project   
        category.add("ClusterUser"); // http://semanpix.de/opendata/wiki/index.php?title=Category:ClusterUser
        category.add("PP");          // http://semanpix.de/opendata/wiki/index.php?title=Category:PP
        category.add("CP");          // http://semanpix.de/opendata/wiki/index.php?title=Category:CP
        category.add("DataSet");     // http://semanpix.de/opendata/wiki/index.php?title=Category:DataSet
        category.add("Cluster_Docu");     // http://semanpix.de/opendata/wiki/index.php?title=Category:Cluster_Docu
    }
    
    // the elements have properties, which are related to perspectives:
    
    
    
    /**
     * 
     * Analyst:        - algorithmic, scientific
     * Business User:  - HR, ER, Docu, financial-cost
     * Operator:       - system-resources, storage, compute requirements
     * 
     * 
     * Finally this class loads an ontology and uses no static definitions.
     * 
     */
    
    
}
