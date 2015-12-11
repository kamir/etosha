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
package org.etosha.contextualizer.tools;

import wikiapiclient.WikiORIGINAL;

/**
 *
 * @author kamir
 */
public class WikiSynchTool {
    
    /**
     * SPO is mapped to
     * 
     * S  => pagname
     * PO => [[P::O]]
     * 
     * We create a new page with that name if it not exists.
     * Now, we append the property [[P::O]] to the pagetext.
     * 
     */
    public void rdfToWiki( String rdfContent, WikiORIGINAL wiki ) {
    
    }
    
    /**
     * Here we simply load the RDF content from the Wiki.
     * 
     * @param wikipagename
     * @param wiki
     * @return 
     */
    public String rdfFromWiki( String wikipagename, WikiORIGINAL wiki ) {
        return "NO DATA AVAILABLE. Not implemented: rdfFromWiki ...";
    }
    
}
