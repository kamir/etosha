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
package com.cloudera.dal.graph;

import com.cloudera.dal.admin.EMDMBaseTabAdmin;
import com.cloudera.dal.emdm.EMDMConfig;
import com.cloudera.dal.emdm.EMDMStore;
import java.io.IOException;

/**
 * 
 * @author kamir
 */
public class TopicNetworkCreator {
    
    public static void main(String[] args) throws IOException {
        
        /**
         * 
         * DEFINE A ClusterContext here ...
         * 
         */
//        EMDMBaseTabAdmin.default_server = "QSVM";
        
        
        // Print some output: goes to your special stream
        
        EMDMConfig.init( args );
        
        /**
         * Create a table 'partsMatch' with two CF in HBase.
         * 
         * CF1 : holds the raw data in JSON
         * CF2 : holds the exploded data structure for direct access to fields
         * 
         * hbase shell
         * 
         */
        
        //
        //    This is deleting the data !!!
        //
        
        // EMDMBaseTabAdmin.main(null);
        
        // SoleraPMCPing.main(null);
   
        // Init EMDMStore
        EMDMStore.adapter = null;
        
        EMDMStore ms = new EMDMStore();
        ms.init();
        
        TopicNetwork.exportTopicNetwork(ms.getTopicGraphLinks() , EMDMStore.getTableName() );

        System.out.println("Nr of rows: " + EMDMStore.getNrOfRows() );

    }
    
}
