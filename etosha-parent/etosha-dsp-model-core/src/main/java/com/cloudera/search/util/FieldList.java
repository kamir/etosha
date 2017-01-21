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

import java.util.Vector;

public class FieldList {
    
    Vector<String> data = null;

    /**
     * Factory method for FieldList
     * 
     * @param listData
     * @return 
     */
    public static FieldList init(Vector<String> listData) {
        
        FieldList fl = new FieldList();

        fl.data=listData;
        // fl.data=getDummyFields();
        // fl.data=getFieldsOfType_N_for_Collection_c( "COLLECTION", "TYPE" );
        
        return fl;
    }

    /**
     * Get DUMMY field-list.
     * 
     * This works for a SOLERA index with vehicle data.
     */
    public static Vector<String> getDummyFields() {
        Vector<String> list = new Vector<String>();
        list.add( "log_message" );
        list.add( "message" );
        return list;
    }

//    /**
//     * Get fields of a given type t for a SOLR collection name n
//     * 
//     * See: https://cwiki.apache.org/confluence/display/solr/Schema+API#SchemaAPI-ListFields
//     */
//    public static Vector<String> getFieldsOfType_N_for_Collection_c( String n, String c ) {
//
//        // TODO: IMPLEMENT HTTP Request to SOLR for dynamic field inspection.
//        
//        Vector<String> list = new Vector<String>();
// 
//        return list;
//    }
    
    
    public String[] getAsStringArray() {
        String[] a = new String[data.size()];
        return data.toArray(a);
    }
    
}
