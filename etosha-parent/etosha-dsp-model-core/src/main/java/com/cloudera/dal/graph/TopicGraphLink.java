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

import java.io.IOException;
import java.util.Hashtable;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


/**
 *
 * @author kamir
 */
public class TopicGraphLink {
    
    public static int linkCreationCounter = 0;

    String source = "";
    String target = "";
    Double weight = 0.0;

    
    /**
     * One record goes in, a set of Links goes out.
     *
     */
    public static Hashtable<Integer,TopicGraphLink> getTopicGraphLinksFromHBaseRow(Result r) throws IOException {

        linkCreationCounter ++;
        
        Hashtable<Integer,TopicGraphLink> links = new Hashtable<Integer,TopicGraphLink>();

        String theSource = null;
        
        CellScanner sc = r.cellScanner();

        // take source as claim
        // take number of links from zMatches 
        // iterate over the number of links
        while (sc.advance()) {

            Cell c = sc.current();

            String colName = new String(CellUtil.cloneQualifier(c));
            
            System.out.println( linkCreationCounter + ">>> found column : " + colName );

            if (colName.equals("claim")) {
                theSource = new String(CellUtil.cloneValue(c));
                System.out.println( linkCreationCounter + ">>> found source name : " + theSource );
            }

            if (colName.endsWith("_s")) {
                
                int linkId = getLinkId( colName );
                TopicGraphLink link = links.get( linkId );
                
                if ( link == null ) {
                    
                    link = new TopicGraphLink();
                    
                    link.source = theSource;
                    links.put(linkId, link);
                    
                }
                
                handleScore(c, link);
            }

            if (colName.endsWith("_p")) {

                int linkId = getLinkId( colName );
                TopicGraphLink link = links.get( linkId );
                
                if ( link == null ) {
                
                    link = new TopicGraphLink();
                    
                    link.source = theSource;
                    links.put(linkId, link);
                }
                
                handleTarget(c, link );
            }

        }
        
        //
        // NOW we must give the final props per line 
        //
        for( int i : links.keySet() ) {
            
            TopicGraphLink l = links.get(i);
            l.source = theSource;
            
            System.out.println(linkCreationCounter + " # " + l.toTSVLine() );

        }
        
        return links;

    }

    public TopicGraphLink() {    }

    public static void handleScore(Cell c, TopicGraphLink l) {

        Double value = Double.parseDouble(Bytes.toString(CellUtil.cloneValue(c)));
        
        l.weight = value;

        System.out.println( l.toTSVLine() );

    }

    private static void handleTarget(Cell c, TopicGraphLink l) {
        
        String theTarget = new String(CellUtil.cloneValue(c));
        
        l.target = theTarget;
        
        System.out.println( l.toTSVLine() );
        
    }

    private static int getLinkId(String colName) {
        
        int id = Integer.parseInt( colName.split("_")[0] );

        System.out.println( ">>> found id: " + id );
        
        return id;
        
    }

    String toTSVLine() {
    
        String s = cleanFromTab( this.source ) + "\t" +  cleanFromTab( this.target ) + "\t" + this.weight; 
        return s;
        
    }

    private String cleanFromTab(String source) {
        return source;
//        return source.replaceAll("\t", "___");
    }

}
