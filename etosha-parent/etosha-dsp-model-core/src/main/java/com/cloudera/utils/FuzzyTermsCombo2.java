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
package com.cloudera.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class FuzzyTermsCombo2 implements java.io.Serializable {
    
    public long docCount = 0;
    
    double fuzzynessRatio = 0.2;
  
    /***
     * Q: What is the meaning of the integer? Is it a count?
     * 
     */
    
    // Map of term, slop tuples, represents a given combination 
    // of words in a description
    public Hashtable<String, Integer> termsMap = new Hashtable<String, Integer>();

    int length2LevThreshold(String s ){
        double ratio = fuzzynessRatio; //allow to change 0.3 of all letters
        int len = s.length();
        if (len < 2) {
              // this makes all the difference by the way:
              // it may mean
              // "bumper front" -> "bumper f" vs. "bumper back" -> "bumper b"
            return 1;
        }    
        else
            return (int)(len * ratio);
    }
    
    public String toString(){
        Iterator it = termsMap.keySet().iterator();
        String s  = "";
        while ( it.hasNext() ) {
            s = s + it.next().toString();
        }
        return s;
    }
    
    public void put( Vector<String> set ) {
        Iterator it = set.iterator();
        while ( it.hasNext() ) {
           String s = (String)it.next();
          
           /**
            *  Q: was passiert hier ????
            * 
            */
           //termsMap += (s -> length2LevThreshold(s))
           termsMap.put(s, length2LevThreshold(s));
        }
    }
    
}

 
  

  


 


 