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
import static com.cloudera.search.queries.IdQuery.solr;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse; 
import org.apache.solr.common.params.MoreLikeThisParams;

/**
 * The SecondOrder query allows us to search in a second language with implicit
 * translation. 
 *
 * @author kamir
 */
public class SecondOrderQuery extends AbstractQuery {
 
    private SolrQuery query;
    private Vector<SolrQuery> queries = new Vector<SolrQuery>();
    
    String alternativeField;
    
    /**
     * No parameter: we build a Parts Match Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static SecondOrderQuery init()
            throws MalformedURLException, SolrServerException {

        if (solr == null) {
            // ZK settings are in QueryConstants ...
            solr = new CloudSolrServer(ZK_LIST);
            solr.setDefaultCollection( QueryConstants.defaultCollection_UC1 );
            System.out.println( "[COMPONENT]   SecondOrderQuery" );
            System.out.println( "[ZK]          " + ZK_LIST );
            System.out.println( "[Collection]  " + QueryConstants.defaultCollection_UC1 );
        }

        SecondOrderQuery svq = new SecondOrderQuery();
        svq.defaultField = "message";
        svq.alternativeField = "message";
        
        return svq;
    }

    public SecondOrderQuery(String phraseForText1) throws MalformedURLException, SolrServerException {
        SecondOrderQuery q1 = SecondOrderQuery.init();
        q1.buildQuery(phraseForText1);
    }

    private SecondOrderQuery() {}
    
        /**
     * By defaut we search in all fields ...
     *
     * We ignore all filters ...
     *
     * @param matchPattern
     */
    public void buildQuery(String textToMatch) throws MalformedURLException, SolrServerException {
        
        // Long[] ids = queryForIdsToMatchText( textToMatch );
        String[] translatedPhrases = queryForPhrasesInAlternativeFieldToTextMatchesInDefaultField( textToMatch );
        
        if ( translatedPhrases != null ) {
            for( String i : translatedPhrases ) {
                
                query = buildUpSimplePartsMatchQueryOnAlternateField( i , this.alternativeField );
                queries.add(query);

            }

            System.out.println( queries.size() + " queries are prepared.");
        }
        else {
            System.out.println( "No Matching-Phrases for phrase {" + textToMatch + "} found.");
        }
    }   
    
    /*
     * Build up a MoreLikeThis query to retrieve documents 
     * similar to the one with id originalId
     */
    private SolrQuery buildUpMoreLikeThisQuery(String originalId) {
        
        SolrQuery query = new SolrQuery();
        
        query.setQueryType("/" + MoreLikeThisParams.MLT);
        query.set(MoreLikeThisParams.MATCH_INCLUDE, true);
        query.set(MoreLikeThisParams.MIN_DOC_FREQ, 1);
        query.set(MoreLikeThisParams.MIN_TERM_FREQ, 1);
        query.set(MoreLikeThisParams.MIN_WORD_LEN, 7);
        query.set(MoreLikeThisParams.BOOST, false);
        query.set(MoreLikeThisParams.MAX_QUERY_TERMS, 1000);
        query.set(MoreLikeThisParams.SIMILARITY_FIELDS, defaultField );
        query.setQuery("id:" + originalId);
        query.set("fl", "id,score");
        // query.addFilterQuery("field3:" + field3);
        int maxResults = 10;
        query.setRows(maxResults);
        return query;
    }

    private String[] queryForPhrasesInAlternativeFieldToTextMatchesInDefaultField(String textToMatch) throws MalformedURLException, SolrServerException {
//        SimpleFieldQuery psq = SimpleFieldQuery.init();
//        psq.buildQuery(textToMatch);
//        psq.executeQuery();
//        return psq.getResultForField( this.alternativeField, 5 );
        
        return null;
    }
    
    private Long[] queryForIdsToMatchText(String textToMatch) throws MalformedURLException, SolrServerException {
        IdQuery idq;
        try {
            idq = IdQuery.init();
            idq.buildQuery(textToMatch);
            idq.executeQuery();
            return idq.getResultAsLongArray();
        } catch (Exception ex) {
            Logger.getLogger(SecondOrderQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

        for( SolrQuery q : queries ) {
            try {

                QueryResponse response = solr.query(query);
                
                System.out.println( response.getRequestUrl() );

            } 
            catch (SolrServerException ex) {
                Logger.getLogger(SecondOrderQuery.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }

    @Override
    public void debugResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * USE PATTERN ON DEFAULT COLLECTION AND DEFAULT FIELD.
     * 
     * @param pattern
     * @return 
     * @throws java.net.MalformedURLException 
     */
    public static String query( String pattern ) throws MalformedURLException, SolrServerException {
        SecondOrderQuery q = new SecondOrderQuery( pattern );
        q.executeQuery();
        return q.getResultAsJSON();
    }

    private SolrQuery buildUpSimplePartsMatchQueryOnAlternateField(String phrase, String field) throws MalformedURLException, SolrServerException {
        IdQuery psq;
        try {
            psq = IdQuery.init();
            psq.buildQuery(phrase);
            return psq.query;
        } catch (Exception ex) {
            Logger.getLogger(SecondOrderQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
