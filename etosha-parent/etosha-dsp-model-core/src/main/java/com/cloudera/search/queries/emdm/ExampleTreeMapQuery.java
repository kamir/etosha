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
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

/**
 *
 * TODO : Implement query parametrizaion and result transformations.
 * 
 * @author kamir
 */
public class ExampleTreeMapQuery extends SimpleFieldQuery {
    
    /**
     * No parameter: we build a default Vehicle Query ...
     *
     * @return
     * @throws MalformedURLException
     * @throws SolrServerException
     */
    public static ExampleTreeMapQuery init()
            throws MalformedURLException, SolrServerException {

        if (solr == null) {
            // ZK settings are in QueryConstants ...
            solr = new CloudSolrServer(ZK_LIST);
            solr.setDefaultCollection( QueryConstants.defaultCollection_Layer2);
        }

        ExampleTreeMapQuery svq = new ExampleTreeMapQuery();
        return svq;
    }

 
    public static String getResultAsJSONArraySAMPLE() {
        
        String r = "['Location', 'Parent', 'Market trade volume (size)', 'Market increase/decrease (color)'],"+
          "['Global',    null,                 0,                               0],"+
          "['America',   'Global',             0,                               0],"+
          "['Europe',    'Global',             0,                               0],"+
          "['Asia',      'Global',             0,                               0],"+
          "['Australia', 'Global',             0,                               0],"+
          "['Africa',    'Global',             0,                               0],"+
          "['Brazil',    'America',            11,                              10],"+
          "['USA',       'America',            52,                              31],"+
          "['Mexico',    'America',            24,                              12],"+
          "['Canada',    'America',            16,                              -23],"+
          "['France',    'Europe',             42,                              -11],"+
          "['Germany',   'Europe',             31,                              -2],"+
          "['Sweden',    'Europe',             22,                              -13],"+
          "['Italy',     'Europe',             17,                              4],"+
          "['UK',        'Europe',             21,                              -5],"+
          "['China',     'Asia',               36,                              4],"+
          "['Japan',     'Asia',               20,                              -12],"+
          "['India',     'Asia',               40,                              63],"+
          "['Laos',      'Asia',               4,                               34],"+
          "['Mongolia',  'Asia',               1,                               -5],"+
          "['Israel',    'Asia',               12,                              24],"+
          "['Iran',      'Asia',               18,                              13],"+
          "['Pakistan',  'Asia',               11,                              -52],"+
          "['Egypt',     'Africa',             21,                              0],"+
          "['S. Africa', 'Africa',             30,                              43],"+
          "['Sudan',     'Africa',             12,                              2],"+
          "['Congo',     'Africa',             10,                              12],"+
          "['Zaire',     'Africa',             8,                               10]";
          
       return r;
    
    }
    
    
}
