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

import com.cloudera.dal.admin.EMDMBasePing;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class acts as a "decoupling-component". The MatchingEngine needs an
 * instance of this class on each node, which produces final results.
 *
 * HDFS and HBase are possible storage layers. Indexing of MatchingResults is
 * done via Crunch-Indexer tool (for HDFS data) or the Lilly-Indexer-Service in
 * case of HBase.
 *
 * @author kamir
 */
public class ResultHandler implements Serializable {

    /**
     * Using the HBase-DAL the tool writes directly into HBase.
     */
    public static boolean store_in_HBase = false;

    /**
     * HDFS storage is used as default storage.
     *
     * The Results-RDD is saved by using a JSON serialization of JavaBeans.
     *
     * RDD[ ... ] => RDD[MatchingResult] => RDD[String] -> saveAsText() ...
     *
     */
    public static boolean store_in_HDFS = true;

    public static String foldername_in_HDFS = "/ME/default/output";

    /**
     * Customize the data handling ...
     */
    public ResultHandler(String[] args) {

        if (store_in_HBase) {

            EMDMConfig.init( args );
            
            // Init EMDMStore
            EMDMStore ms = new EMDMStore();
            ms.init();
            
            // this version drops an existing table 
            ms._forceInit();

            try {
            
                EMDMBasePing.main(null);
            
            } 
            catch (IOException ex) {
                Logger.getLogger(ResultHandler.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                System.out.println(">>> HBase table is not prepared for MatchingResults.");
                System.exit(-1);
            }
        }

        if (store_in_HDFS) {
            
            System.out.println(">>> HDFS-Output will be written to: " + foldername_in_HDFS );

            // TODO: finish implementation of HDFS output per match
            
            // test access ...
            
            // clean if folder exists already ...
            
        }


    }
    
    
}
