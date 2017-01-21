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
import static com.cloudera.search.queries.IdQuery.logger;
import com.google.gson.Gson;
import com.cloudera.search.util.FieldExpander;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrDocument;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 * TODO: Implement more init( ... ) methods, which can handle multiple arguments
 * to modify the query.
 *
 * @author kamir
 */
public class FieldStatsQuery extends AbstractQuery {

    final static Logger logger = Logger.getLogger(FieldStatsQuery.class.getName());

    /**
     * This is a cluster wide constant name for now, but it can be changed,
     * e.g., based on a ResourceBundle or web-app / servlet-parameters.
     */
    public static final String collectionName = QueryConstants.defaultCollection_UC2;
  
    /**
     * No parameter: we build a default Vehicle Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static FieldStatsQuery init()
            throws MalformedURLException, SolrServerException {

        if (solr == null) {
            // ZK settings are in QueryConstants ...
            solr = new CloudSolrServer(ZK_LIST);
            solr.setDefaultCollection(collectionName);
        }

        FieldStatsQuery svq = new FieldStatsQuery();
        svq.defaultField = "message";
        return svq;
    }
 
    /**
     * By defaut we search in all fields ...
     *
     * We ignore all filters ...
     *
     * @param matchPattern
     */
    public void buildQuery(String[] fieldList) {

        query = new SolrQuery();
 
        StringBuffer sb = new StringBuffer();
        for( String f : fieldList ) {
            sb.append( "&stats.field=" + f );
        }
        
        logger.info( "{FIELDs in FieldStatsQuery} " + sb.toString() );
        
        query.setQuery("message:*&stats=true" + sb.toString() + "&rows=0&indent=true");
        
        System.out.println( "*:*&stats=true" + sb.toString() + "&rows=0&indent=true" );

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
 
    @Override
    public void debugResults() {

        SolrDocumentList results = response.getResults();
        logger.info( results.getNumFound() + " results in " + this.toString() );
 
    }
}
