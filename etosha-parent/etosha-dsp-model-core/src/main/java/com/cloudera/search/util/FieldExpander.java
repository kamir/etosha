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

import java.util.logging.Logger;

/**
 *
 * @author kamir
 * 
 * The FieldExpander component creates a query string for a given match pattern
 * for a set of field connected with an OR operator.
 * 
 */
public class FieldExpander {

    final static Logger logger = Logger.getLogger(FieldExpander.class.getName());

    /**
     * For all fields we do a query for the matchPattern.
     * 
     * Default concatenation is: OR operator
     * 
     * @param matchPattern
     * @return 
     */
    public static String getQueryForFields(String matchPattern) {
        return getQueryForFields_OR( matchPattern );
    }
    
    /**
     * For all fields we do an optional query for the matchPattern.
     * 
     * @param matchPattern
     * @return 
     */
    public static String getQueryForFields_OR(String matchPattern) {
        StringBuffer sb = new StringBuffer();
        for( String f : relevantFields ) {
            sb.append(f+":"+matchPattern+" OR ");
        }
        int l = sb.toString().length();
        String q = sb.toString().substring(0, l - 4);
        
        logger.info( "[QUERY-Part] {" + q + "}" );
        
        return q;
    }
    
    /**
     * For all fields we do a query for the matchPattern,
     * with AND operator.
     * 
     * @param matchPattern
     * @return 
     */
    public static String getQueryForFields_AND(String matchPattern) {
        
        StringBuffer sb = new StringBuffer();
        
        for( String f : relevantFields ) {
            sb.append(f+":"+matchPattern+" AND ");
        }
        
        int l = sb.toString().length();
        String q = sb.toString().substring(0, l - 4);
        
        logger.info( "<QUERY:> " + q);
        
        return q;
    }
    
    static public String[] relevantFields = { "manufacturer_desc_ui", "aspiration_desc_ui" };
    
    
}
