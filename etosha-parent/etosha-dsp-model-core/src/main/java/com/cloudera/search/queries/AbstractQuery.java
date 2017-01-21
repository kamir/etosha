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
package com.cloudera.search.queries;

import com.cloudera.search.QueryConstants;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author kamir
 */
abstract public class AbstractQuery extends QueryConstants {

    public static boolean loggedIn = false;
    
    public Logger logger = null;
    
    public static boolean debug = true;

    public SolrQuery query = null;
    
    public QueryResponse response = null;
    
    /**
     * Execute without a cache handler since the cache has no result for this
     * query.
     *
     * Result will be stored as local object until it was consumed by the user
     * of this query object.
     *
     * Large results my break the application and should be streamed ...
     */
    public void executeQueryZeroRows() {

        try {
            
            int rows = 0;
            
            query.setRows( rows );
            
            System.out.println("[QUERY]       " +  query.toString() );
            
            response = solr.query(query);

            if (debug) {
                debugResults();
            }

        } 
        catch (SolrServerException ex) {
            if ( logger != null )
                logger.log(Level.SEVERE, null, ex);
            else System.err.println( ex.getCause() );
        }
        
    }

    /**
     * Execute without a cache handler since the cache has no result for this
     * query.
     *
     * Result will be stored as local object until it was consumed by the user
     * of this query object.
     *
     * Large results my break the application and should be streamed ...
     */
    public void executeQuery() {

        try {
            
            int rows = Integer.parseInt( "" + defaultNrOfRows );
            
            query.setRows( rows );
            
            System.out.println("[QUERY]       " +  query.toString() );
            
            response = solr.query(query);

            if (debug) {
                debugResults();
            }

        } 
        catch (SolrServerException ex) {
            if ( logger != null )
                logger.log(Level.SEVERE, null, ex);
            else System.err.println( ex.getCause() );
        }
        
    }
    
    public void reset() {
        solr = null;
    }
    
    public String getResultAsJSON() {
        Gson gson = new Gson(); 
        String json = gson.toJson(response.getResults());
        return json;
    }

    abstract public void debugResults();
    
    String defaultField = null;

    public String getDefaultField() {
        return defaultField;
    }
    
    public void setDefaultField( String f) {
        defaultField = f;
    }
}
