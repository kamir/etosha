/*
 * Copyright 2016 kamir.
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
package com.cloudera.search.queries.emdm;

import com.cloudera.search.QueryConstants;
import com.cloudera.search.queries.SimpleFieldQuery; 
import java.net.MalformedURLException;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

/**
 *
 * TODO : Implement query parametrizaion and result transformations.
 * 
 * @author kamir
 */
public class BadDatasetsQuery extends SimpleFieldQuery {
    
    /**
     * No parameter: we build a default Vehicle Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static BadDatasetsQuery init()
            throws MalformedURLException, SolrServerException {

        if (solr == null) {
            // ZK settings are in QueryConstants ...
            solr = new CloudSolrServer(ZK_LIST);
            solr.setDefaultCollection(QueryConstants.defaultCollection_Layer2);
        }

        BadDatasetsQuery svq = new BadDatasetsQuery();
        svq.logger = Logger.getLogger(BadDatasetsQuery.class.getName());
        svq.setDefaultField( "tags" );
       

        return svq;
    }

        /**
     * By defaut we search in all fields ...
     *
     * We ignore all filters ...
     *
     * @param matchPattern
     * @throws java.lang.Exception
     */
    @Override
    public void buildQuery(String matchPattern) throws Exception {

        query = new SolrQuery();

        String field = getDefaultField();
        
        if ( field == null ) throw new Exception( "NO FIELD" );
        
        query.setQuery(field + ":" + matchPattern);

        query.setFields("id", "tags"); 

    }
 
 
    
}
