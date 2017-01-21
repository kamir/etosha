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

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author kamir
 */
public class EMDMEntityKeyFactory {

    public static int DEFAULT_KEYMODE_SPREAD = 1;
    
//    public static int KEYMODE_SPREAD = 0;
    public static int KEYMODE_FOCUSED = 1;
    
    /**
     * Each claim will be stored in a row, together with all 
     * suggestions and votes.
     * 
     * @param claimText
     * @param lang         - not used in version 1.2
     * @return 
     */
    public static String getKeyForClaim( String claimText, String lang, int KEYMODE ) {
        
        switch( KEYMODE ) {
            case 1 : { // KEYMODE_FOCUSED
                return claimText.toString(); 
            }
            
//            case 0 : { // KEYMODE_SPREAD
//                return new String( DigestUtils.md5( lang + "___" + claimText.toString() ) ); 
//            }
        }
        
        return lang + "###" + claimText.toString();

    }
    
}
