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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.Serializable;

/**
 *
 * @author kamir
 */
public class MatchResultEncoder implements Serializable {

    /**
     * Each claim will be stored in a row, together with all suggestions and
     * votes.
     *
     * @param claimText
     * @param lang
     * @return
     */
    public static String getJSONObjectForClaimPartsMatch(Match m) {

        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("match", Match.class);

        return xstream.toXML(m);

    }

    public static MatchingResult getMatchingResultFromString(String json) {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        
        xstream.alias("matchingresult", MatchingResult.class);
        MatchingResult m = (MatchingResult) xstream.fromXML(json);
        
        return m;
    }
    
    public static Match getMatchFromString(String json) {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        
        xstream.alias("match", Match.class);
        Match m = (Match) xstream.fromXML(json);
        
        return m;
    }
    
    public static String getJSONObjectForClaimPartsMatches(MatchingResult m) {

        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("matchingresult", MatchingResult.class);

        return xstream.toXML(m);

    }
    
    

}
