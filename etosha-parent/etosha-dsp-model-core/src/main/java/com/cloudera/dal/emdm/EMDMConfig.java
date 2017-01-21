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
package com.cloudera.dal.emdm;

import com.cloudera.dal.HBaseEMDMStoreAdapter;
import com.cloudera.dal.admin.EMDMBaseTabAdmin;

/**
 *
 * @author kamir
 */
public class EMDMConfig {
    
    /**
     * Initialize the EMDMConfig object with exakt two parameters:
     *  
     *   ZK
     *   TN
     * 
     * @param ZK
     * @param TN 
     */
    public static void init( String ZK, String TN ) {
        String[] ARGS = new String[2];
        ARGS[0] = ZK;
        ARGS[1] = TN;
        init( ARGS );
    }
    
    /**
     * With "NULL" as args array we use simply default
     * settings.
     */ 
    public static void init( String[] args ) {
        
        /**
         * We can switch the key-generation procedure between a focused 
         * (per language) or a spread mode. Spread mode has the advantage 
         * of using many RegionServers randomly if available.
         */
        EMDMEntityKeyFactory.DEFAULT_KEYMODE_SPREAD = EMDMEntityKeyFactory.KEYMODE_FOCUSED;
        
        /**
         * Zookeeper configuration for SOLR and HBase
         */
        String ZK =  args[0]; // "dl-mn04-d,dl-mn02-d,dl-mn03-d";
        
//        if ( args != null ) {
//            System.out.println("[EMDMConfig] >>> ZK: " + args[0] );
//            HBaseEMDMStoreAdapter.defaultZookeeperIP = args[0];
//            ZK = args[0];
//        }
//        else {
//            ZK = "quickstart.cloudera";
//        }
        
        HBaseEMDMStoreAdapter.defaultZookeeperIP = ZK;
        
        /**
         * We overwrite the default table name ...
         */
        EMDMBaseTabAdmin.docTabName = args[1];
        
        System.out.println("[MatchConfig] >>> Zookeeper-Hosts finally used in HBaseEMDMStoreAdapter : " + HBaseEMDMStoreAdapter.defaultZookeeperIP );
        System.out.println("[MatchConfig] >>> Table used in HBaseEMDMStoreAdapter                   : " + EMDMBaseTabAdmin.docTabName );

    }
    
}
