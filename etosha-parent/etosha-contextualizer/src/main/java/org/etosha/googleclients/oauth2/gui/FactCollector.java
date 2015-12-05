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
package org.etosha.googleclients.oauth2.gui;

import org.apache.solr.common.SolrInputDocument;

/**
 *
 * Wraps around the real fact store.
 * 
 * We use SOLR and FUSEKI in parallel.
 * 
 * @author kamir
 */
class FactCollector {

    void init() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Factory to create a new SOLR Document
     * 
     * @param id
     * @param gMailFAQIndexerv1
     * @return 
     */
    SolrInputDocument getSolrInputDocument(String id, String gMailFAQIndexerv1) {
        return null;
    }
     

    /**
     * 
     * 
     * @param faqMails02Collection
     * @param doc 
     */
    void updateCollection(String faqMails02Collection, SolrInputDocument doc) {
        
    }
}
