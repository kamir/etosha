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

import java.util.Properties;

/**
 *
 * @author kamir
 */
public class Match extends RankedMatch{
   
   public static String defaultLang = "ANY";
    
   public String lang = null; // Category for separating data by lang, 
   // must be overwritten in real app.
    
   public String claim = null;
   
   public String part = null;
   
   public double score = 0.0;
   
   public double userRating = 0.0;

   /**
    * i is the index of a match in the match set defined by a MatchResult
    * @param i
    * @return 
    */
    Properties getPropertiesRepresentation(int i) {

        if ( lang == null ) lang = defaultLang;
        
        Properties props = new Properties();
        
        props.put("lang", lang);
        props.put("claim", claim);
        props.put(i+"_p", part);
        props.put(i+"_s", ""+score);
        props.put(i+"_r", ""+userRating);
        
        // props.list( System.out );
        
        return props;
        
    }
    
}
