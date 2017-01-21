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
import com.cloudera.search.SecureClient;
import com.google.gson.Gson;
import com.cloudera.search.util.FieldExpander;
import com.cloudera.search.util.FieldExpander;
import java.io.IOException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrDocument;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 * TODO: Implement more init( ... ) methods, which can handle multiple arguments
 * to modify the query if needed.
 * 
 * 
 * An IdQuery gives back a list of document ids for a sertain search.
 *
 * @author kamir
 */
public class IdQuery extends AbstractQuery {

    final static Logger logger = Logger.getLogger(IdQuery.class.getName());
 
    /**
     * No parameter: we build a default Vehicle Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static IdQuery init()
            throws MalformedURLException, SolrServerException, Exception {

        if (solr == null) {
            // ZK settings are in QueryConstants ...
            solr = new CloudSolrServer(ZK_LIST);
            solr.setDefaultCollection(QueryConstants.defaultCollection_UC1);
            System.out.println( "[COMPONENT]   IdQuery" );
            System.out.println( "[ZK]          " + ZK_LIST );
            System.out.println( "[Collection]  " + QueryConstants.defaultCollection_UC1 );
            System.out.println( "[Loged in]    " + AbstractQuery.loggedIn );
            
        }

        IdQuery svq = new IdQuery();
        
        if( !AbstractQuery.loggedIn ) 
            try {
                SecureClient.login();
        } 
        catch (IOException ex) {
            Logger.getLogger(IdQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return svq;
    }

    private IdQuery() {    
        defaultField = "message";
    }
 

    /**
     * By defaut we search in field_1 only ...
     * We ignore all filters ...
     *  
     * Here we can also wrap around another query, and only retrieve the id
     * field. Must guarantee, that an id field really exist.
     * 
     * @param matchPattern
     */
    public void buildQuery(String matchPattern) {
        query = new SolrQuery();
        query.setQuery( this.defaultField + ":" + matchPattern);
        query.setFields("id," + this.defaultField);
    }
    
    public void debugResults() {

        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            logger.info(result.getFieldValue("id=")
                    + " " + this.getDefaultField() + "='" + result.getFieldValue(this.defaultField) + "'.");
        }

    }

//    /**
//     * Execute with cache handler ...
//     */
//    public void executeQuery( QueryCacheHandler qch ) {
//        
//        byte[] q = serializeQueryState( this.query );
//        
//        byte[] re = QueryCacheHandler.getResultForQuery(q);
//        
//        if ( re == null ) {
//            executeQuery();
//        }
//        else {
//            response = deserializeQueryReult( re );
//        }
//        
//    }
//
//    private byte[] serializeQueryState(SolrQuery query) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private QueryResponse deserializeQueryReult(byte[] re) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
 

    /**
     *
     * Alternatives:
     * -------------
     * 
     * - use Apache Velocity to render the result set here or in the JSP.
     * - render HTML code programatically.
     * 
     */
    public String getResultAsXML() {

        StringBuffer sb = new StringBuffer();
        
        SolrDocumentList results = response.getResults();
        
        sb.append("<results z='" + results.getNumFound() + "'>\n");
        
        for (int i = 0; i < results.size(); i++) {
            String xml = ClientUtils.toXML(ClientUtils.toSolrInputDocument(results.get(i)));
            logger.info( xml );
            sb.append(xml + "\n");
        }
        
        sb.append("</results>\n");

        return sb.toString();
    }
 

    Long[] getResultAsLongArray() {
        
        Vector<Long> ids = new Vector<Long>();
        
        int i = 0;
        long z = response.getResults().getNumFound();
        
        System.out.println("[NR OF IDs loaded  ]    z=" + z );
        System.out.println("[NR OF rows loaded ] rows=" + defaultNrOfRows );

        long zR = response.getResults().getNumFound();
        if ( zR == 0 ) return null;
                
        if ( zR < defaultNrOfRows ) defaultNrOfRows = zR;
        
        while( i < defaultNrOfRows ) {
            ids.add( Long.parseLong( "" + response.getResults().get(i).getFieldValue("id") ));
            i++;
        }
        
        if ( i == 0 ) return null;
        
        Long[] IDS = new Long[i];
        ids.copyInto(IDS);
        return IDS;
    }
    
    /**
     * USE PATTERN ON DEFAULT COLLECTION AND DEFAULT FIELD.
     * 
     * @param pattern
     * @return 
     * @throws java.net.MalformedURLException 
     */
    public static String query( String pattern ) throws MalformedURLException, SolrServerException, Exception {
        IdQuery q = IdQuery.init();
        q.buildQuery( pattern );
        q.executeQuery();
        return q.getResultAsJSON();
    }
    
    public IdQuery(String t) throws MalformedURLException, SolrServerException, Exception {
        IdQuery q = IdQuery.init();
        q.buildQuery(t);
    }

}
