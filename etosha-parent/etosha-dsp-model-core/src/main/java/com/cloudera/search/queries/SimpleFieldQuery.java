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

import com.cloudera.search.queries.AbstractQuery;
import com.cloudera.search.QueryConstants;
import com.google.gson.Gson;
import com.cloudera.search.util.FieldExpander;
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
 * to modify the query.
 *
 * @author kamir
 */
public class SimpleFieldQuery extends AbstractQuery {

    /**
     * No parameter: we build a default Parts Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static SimpleFieldQuery init()
            throws MalformedURLException, SolrServerException {

        // ZK settings are in QueryConstants ...
        solr = new CloudSolrServer(ZK_LIST);
        solr.setDefaultCollection( QueryConstants.defaultCollection_UC3 );
        
        System.out.println("[COMPONENT]   SimpleFieldQuery");
        System.out.println("[ZK]          " + ZK_LIST);
        System.out.println("[Collection]  " + QueryConstants.defaultCollection_UC3);

        SimpleFieldQuery svq = new SimpleFieldQuery();
        svq.logger = Logger.getLogger(SimpleFieldQuery.class.getName());
        svq.setDefaultField( "message" );

        return svq;
    }

    /**
     * By defaut we search in all fields ...
     *
     * We ignore all filters ...
     *
     * @param matchPattern
     */
    public void buildQuery(String matchPattern) throws Exception {

        query = new SolrQuery();

        query.setQuery(getDefaultField() + ":" + matchPattern);

        query.setFields("id", "message"); 

    }
    
  
    public String getNrOfResult() {

        StringBuffer sb = new StringBuffer();

        SolrDocumentList results = response.getResults();

        String z = ""+ results.getNumFound();

        return z;
    }

    /**
     *
     * Alternatives: -------------
     *
     * - use Apache Velocity to render the result set here or in the JSP. -
     * render HTML code programatically.
     *
     */
    public String getResultAsXML() {

        StringBuffer sb = new StringBuffer();

        SolrDocumentList results = response.getResults();
        
        if (results == null ) return "NULL";

        sb.append("<results z='" + results.getNumFound() + "'>\n");

        for (int i = 0; i < results.size(); i++) {
            String xml = ClientUtils.toXML(ClientUtils.toSolrInputDocument(results.get(i)));
            logger.info(xml);
            sb.append(xml + "\n");
        }

        sb.append("</results>\n");

        return sb.toString();
    }

    public void debugResults() {
//        SolrDocumentList results = response.getResults();
//        for (SolrDocument result : results) {
//            logger.info(result.getFieldValue("manufacturer_desc_ui") + " has engcode='" + result.getFieldValue("engcode") + "'.");
//        }
    }

    String[] getResultForField(String alternativeField, int max) {

        Vector<String> alternativeMatches = new Vector<String>();

        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            String s = (String) result.getFieldValue(alternativeField);
            alternativeMatches.add(s);
        }

        String[] r = new String[max];
        for (int i = 0; i < max; i++) {
            r[i] = alternativeMatches.elementAt(i);
        }

        return r;
    }
}
