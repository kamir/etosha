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

import java.io.BufferedWriter;
import java.util.Vector;

/**
 * The MatchingEngine produces a set of MatchinResults.
 *
 *
 * @author kamir
 */
public class MatchingResult {

    // Per claim we get a list of matches ...
    public String claim;

    public String lang = Match.defaultLang;

    // a set of matches (limitted by query execusion to 20)...
    public Vector<Match> matches;

    public MatchingResult() {
        matches = new Vector<Match>();
    }

    /**
     *
     * Generate z random Matching results for testing the Null model
     * with random nr of random scores.
     * 
     * 
     */
    public static Vector<MatchingResult> getRandomMatchResults(int z) {
        Vector<MatchingResult> c = new Vector<MatchingResult>();
        int i = 0;
        while (i < z) {

            MatchingResult r1 = new MatchingResult();

            r1.claim = "c" + (i + 1);

            /**
             * Match:
             *
             * m3.part => name of the part m3.claim => to which claim is this
             * part matching m3.score => What is the matching engine thingking?
             * (boosted result per candidate)
             */
            Vector<Match> v = new Vector<Match>();
            int zM = (int) (Math.random() * 20);
            int j = 0;
            while (j < zM) {
                v.add(getMatchSample("m"+(j+1), r1.claim));
                j++;
            }
            r1.matches = v;
            
            c.add(r1);
            i++;
        }
        
        return c;

    }

    /**
     * Produce some testdata.
     *
     * @return
     */
    public static MatchingResult getTestMatchSamples1() {

        MatchingResult r1 = new MatchingResult();

        r1.claim = "Spiegel vorn linke Seite";

        /**
         * Match:
         *
         * m3.part => name of the part m3.claim => to which claim is this part
         * matching m3.score => What is the matching engine thingking? (boosted
         * result per candidate)
         */
        Vector<Match> v = new Vector<Match>();
        v.add(getMatchSample("Rückspiegel vorn links", r1.claim));
        v.add(getMatchSample("Rückspiegel v l", r1.claim));
        v.add(getMatchSample("Spiegel v. l. (aussen)", r1.claim));
        v.add(getMatchSample("SPIEGEL v l (aussen)", r1.claim));
        v.add(getMatchSample("SPIEGEL VORN la", r1.claim));

        r1.matches = v;

        return r1;

    }

    /**
     * Produce some testdata.
     *
     * @return
     */
    public static MatchingResult getTestMatchSamples2() {

        MatchingResult r2 = new MatchingResult();

        r2.claim = "Bremsscheibe linke Seite hinten";

        Vector<Match> v = new Vector<Match>();
        v.add(getMatchSample("Bremsscheibe hinten links", r2.claim));
        v.add(getMatchSample("Bremsscheibe h l", r2.claim));
        v.add(getMatchSample("BREMSSCHEIBE HINTEN, \"LINKS\"", r2.claim));

        r2.matches = v;

        return r2;

    }

    /**
     * Produce some testdata.
     *
     * @return
     */
    public static MatchingResult getTestMatchSamples3() {

        MatchingResult r3 = new MatchingResult();

        r3.claim = "Fahrertür linke Seite vorn";

        Vector<Match> v = new Vector<Match>();
        v.add(getMatchSample("Tür vorn links", r3.claim));
        v.add(getMatchSample("Tür v. l.", r3.claim));
        v.add(getMatchSample("DOOR f.l.", r3.claim));

        r3.matches = v;

        return r3;

    }

    // parameter for testdata generation
    static double SCALE = 15.00; // max score - score is uniform random in range
    // 0 ... scale

    static private Match getMatchSample(String sugest, String claim) {
        Match m3 = new Match();
        m3.part = sugest;
        m3.claim = claim;
        m3.score = Math.random() * SCALE;
        return m3;
    }

    /**
     * MatchingResults can be changed by a person (SME).
     *
     * Ranking means, adding a value to the existing rank.
     *
     * @param v2
     * @param v3
     * @return
     */
    static Vector<Match> rankMatches(Vector<Match> v2) {

        Vector<Match> v3 = new Vector<Match>();
        int i = 0;
        for (Match m : v2) {
            m.userRating = m.userRating + getBinaryVoteForMatch();
            v3.add(m);
            i++;
        }

        return v3;
    }

    /**
     * Votes are gathered in the user interface.
     *
     * The MATCHES ARRAY CORRSPONDS TO THE VOTES ARRAY in element order.
     *
     * @param v2
     * @param votes
     * @return
     */
    static Vector<Match> rankMatches(Match[] v2, Integer[] votes) {

        Vector<Match> v3 = new Vector<Match>();
        int i = 0;
        for (Match m : v2) {
            m.userRating = m.userRating + votes[i];
            v3.add(m);
            i++;
        }

        return v3;
    }

    /**
     * Create a random value within a range, for testing only.
     *
     * @return
     */
    private static int getBinaryVoteForMatch() {
        int v = (int) (Math.random() * 1.5);
        return v;
    }

    /**
     * Each result is enriched by an index, to define the column-name for HBase
     * storage.
     *
     * @param v2
     * @return
     */
    static Vector<Match> getAsIndexed(Vector<Match> v2) {

        Vector<Match> v3 = new Vector<Match>();

        int i = 0;
        for (Match m : v2) {
            m.indexInMatchResult = i;
            v3.add(m);
            i++;
        }

        return v3;

    }

    /**
     * Convert the object into JSON using the MatchResultEncoder helper.
     */
    public String toJSON() {
        return MatchResultEncoder.getJSONObjectForClaimPartsMatches(this);
    }

//    /**
//     * Writes the claim and the suggested matches to the file stream in a Gephi
//     * conform format.
//     * 
//     * claim: SOURCE
//     * match: TARGET
//     * score: WEIGHT
//     * vote : VOTE
//     * 
//     * @param bw 
//     */
//    public void getSubNetwork(BufferedWriter bw) {
//       
//        // TBD
//        
//    }
}
