/*
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
package com.cloudera.search;
  
import com.cloudera.search.util.FieldList;
import java.io.Serializable;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

/**
 * QueryConstants contain all configuration parameters for a particular use case. 
 * used by the query tools
 * or in the webapplication layer. 
 * 
 * It should be the single source of truth during runtime.
 * 
 * For faster testing, I implement constants, later on, during parametrization
 * those constants can be replaced by variable, and initialized during statup.
 * 
 * @author kamir
 */
public class QueryConstants implements Serializable {
    
    // TODO : implement an initialization method which creates this
    //        String from environment variables: 
    //
    //             export ZK-PORT=2181
    //             export ZK="lhansahdp1-230-8:2181,lhansahdp1-230-2:2181,lhansahdp1-230-3:2181/solr"
    //    
    public static String ZK_LIST = "lhansahdp1-230-8:2181,lhansahdp1-230-2:2181,lhansahdp1-230-3:2181/solr";
    
    
    /**
     * Has to be changed for other use cases.
     */
    public static String defaultCollection_UC1 = "md_col_02a";
    public static String defaultCollection_UC2 = "dev_smileapp_collection";
    public static String defaultCollection_UC3 = "prod_smileapp_collection";

    
    /**
     * Has to be changed for Collection-Alliases.
     */
    public static String defaultCollection_Layer1 = "prod_smileapp_collection";
    public static String defaultCollection_Layer2 = "md_col_02a";

    
            
    /**
     * This is a cluster wide constant name for a DEMO-collection.
     * It can be changed, e.g., based on a ResourceBundle or 
     * using web-app / servlet-parameters.
     */
    // public static String collectionName = defaultCollection_UC1;
    
    public static CloudSolrServer solr = null;

    public static FieldList defaultFields_SMILE_APP = FieldList.init( FieldList.getDummyFields() );

    public long defaultNrOfRows = 20;

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
    

}
