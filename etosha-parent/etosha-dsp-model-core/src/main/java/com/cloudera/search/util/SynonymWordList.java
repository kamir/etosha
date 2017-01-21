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
package com.cloudera.search.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kamir
 */
public class SynonymWordList {
    
    String collectionName = null;
    String localCFGFolder = null;
    
    String fileName = null;
           
    /**
     * after successfull instantiation this string contains the raw data
     * from synonyme list file, otherwise it is null.
     */
    String rawList = null;
    
    public SynonymWordList( String CN, String folder, String file ) throws FileNotFoundException, IOException {
        collectionName = CN;
        localCFGFolder = folder;
        fileName = file;
        
        StringBuffer sb = new StringBuffer();
        
        System.out.println( ">>> Edit synonym word list for collection: ["+collectionName+"]");
        
        File f = new File( localCFGFolder + "/" + fileName );
        if ( f.exists() ) {
            BufferedReader br = new BufferedReader( new FileReader( f ));
            while( br.ready() ) {
                String line = br.readLine();
                System.out.println( "=> " + line );
                sb.append(line + "\n");
            }
            br.close();
            rawList = sb.toString();
        }
        else {
            System.out.println( ">  Synonym file does not exist." );
        }
    }

    void storeNewSynonyms(String newData) throws IOException {
        
        String ts = "" + System.currentTimeMillis();
        
        File fn = new File( localCFGFolder + "/" + ts + fileName );
        
        BufferedWriter bw = new BufferedWriter( new FileWriter( fn ) );
        bw.write( rawList + "\n" );
        bw.write("# added new synonyms : " + ts + "\n" );
        bw.write(newData);
        bw.flush();
        bw.close();
        
        System.out.println( "\n >>> Create a new synonym word list ... \n");

        System.out.println( rawList + "\n" );
        System.out.println( "# added new synonyms : " + ts + "\n" );
        System.out.println(newData);

    }

    void submitToCluster() {
        System.out.println("\n >>> ONLINE REFRESH IS NOT YET IMPLEMENTED! " );
        System.out.println(" >   You have to update the instancedir and to reload the collection. ");
        System.out.println(" >   s o l r c t l   command will be shown here soon ...");
        System.out.println(" >   Reindexing is required if index-time synonyms are used.");        
    }
           
    
}
