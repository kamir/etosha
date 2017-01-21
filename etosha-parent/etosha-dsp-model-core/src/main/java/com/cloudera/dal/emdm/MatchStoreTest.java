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

import com.cloudera.dal.HBaseEMDMStoreAdapter;
import com.cloudera.dal.admin.EMDMBasePing;
import com.cloudera.dal.admin.EMDMBaseTabAdmin;
import com.cloudera.dal.graph.TopicNetwork;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * The test tool access the DAL to store and reload MatchingResults to
 * HBase.
 *
 * @author kamir
 */
public class MatchStoreTest {
    
    
    private static final Logger LOG = Logger.getLogger(MatchStoreTest.class.getName());
    
    static EMDMStore ms = null;
    
    public static void main(String[] args) throws IOException {
        
        /**
         * 
         * DEFINE A ClusterContext here ...
         * 
         */
//        EMDMBaseTabAdmin.default_server = "QSVM";
        
        MatchStoreTest mst = new MatchStoreTest();
        mst.run( args, null );
    }
    
    public void run(String[] args, ByteArrayOutputStream out) throws IOException {

        PrintStream ps = null;
        
        // IMPORTANT: Save the old System.out!
        PrintStream previousOut = System.out;

        if ( out == null ) {

        } 
        else {    
            
            ps = new PrintStream(out);
    
            // Tell Java to use your special stream
            System.setOut(ps);
        }


        // Print some output: goes to your special stream
        
        EMDMConfig.init( args );
        
        /**
         * Create a table 'partsMatch' with two CF in HBase.
         * 
         * CF1 : holds the raw data in JSON
         * CF2 : holds the exploded data structure for direct access to fields
         * 
         * hbase shell
         * 
         */
        EMDMBaseTabAdmin.main(null);
        
        // EMDMBasePing.main(null);
   
        // Init EMDMStore
        EMDMStore.adapter = null;
        ms = new EMDMStore();
        ms.init();
        
        /**
         * Create MatchResults and simulate votes for high quality matches ...
         */
        runTestSuite_A( ms );
        
        /**
         * Test access patterns for Matches and MatchResults
         * per language and unspecific.
         */
        // runTestSuite_B( ms );
        
        
        /**
         * 
         * This tests the MatchingEngine ResultHandler component ...
         * 
         */
        runTestSuite_C();
        
        /**
         * 
         * This tests the MatchingResultContainer, especially 
         * export functionality 
         */
        runTestSuite_D();

        /**
         * 
         * This tests the export of results stored in HBase
         */
        runTestSuite_E();


        /**
         * 
         * This tests the FULL RESULT WITH LIMIT request ...
         */
        runTestSuite_F();

        /**
         * 
         * This tests the HISTORGRAM FUNCTION per table ... 
         */
        runTestSuite_G();
        
        /**
         * 
         * This tests the Topic-Graph Export as TSV file for Gephi.
         */
        runTestSuite_H();        
        
          
        // Put things back
        System.out.flush();

        System.setOut(previousOut);
    }

    /**
     * Create MatchResults and simulate votes for high quality matches ...
     */
    private static void runTestSuite_A(EMDMStore ms) throws IOException {
        
        System.out.println( "\n TEST A" );
        System.out.println( " ======\n" );
        
        // write matches into the EMDMStore
        MatchingResult v1 = MatchingResult.getTestMatchSamples1();
        ms.putMatchResults(v1);
        
        MatchingResult v2 = MatchingResult.getTestMatchSamples2();
        ms.putMatchResults(v2);
        
        Vector<Match> v2a = MatchingResult.getAsIndexed( v2.matches );
        
        // evaluate the results by several users ...
        System.out.println( "\n>>> Start VOTING by SME." );
        Vector<Match> rankedByUserA = MatchingResult.rankMatches( v2a );
        Vector<Match> rankedByUserB = MatchingResult.rankMatches( rankedByUserA );
        Vector<Match> rankedByUserC = MatchingResult.rankMatches( rankedByUserB );
        Vector<Match> rankedByUserD = MatchingResult.rankMatches( rankedByUserC );

        // ranked results are written back to table
        System.out.println( "\n>>> Put VOTED results into the table." );
        ms.putRankedMatchResults( rankedByUserD );
        
    }

    /**
     * Test access patterns for Matches and MatchResults
     * per language and unspecific.
     */
    private static void runTestSuite_B(EMDMStore ms) throws IOException {
        
        System.out.println( "\n TEST B" );
        System.out.println( " ======\n" );
        

        
        // lets get back a test claim 
        //
        // Get Match Result for a claim in a particular language from DB – g1
        //
        Vector<Match> v1 = MatchingResult.getTestMatchSamples1().matches;
        String CLAIM = v1.elementAt(0).claim;
        String LANG = v1.elementAt(0).lang;
        int z = 10;
        
        Vector<Match> g1 = ms.getMatchesForAClaimInLanguage(CLAIM, LANG).matches;
        
        // SCAN MatchResults from DB – s1
        Vector<MatchingResult> s1 = ms.getMatchResults( z );

        
        // SCAN MatchResults unranked from DB – s2
        Vector<MatchingResult> s2 = ms.getMatchResultsUnranked( z );
        
        
        // SCAN MatchResults for a language from DB – s3
        Vector<MatchingResult> s3 = ms.getMatchResultsForLanguage(LANG, z);


        // SCAN MatchResults for a language unranked from – s4
        Vector<MatchingResult> s4 = ms.getMatchResultsForLanguageUnranked(LANG, z);
        
    }

    private static void runTestSuite_C() {
        
        System.out.println( "\n TEST C" );
        System.out.println( " ======\n" );
        
        ResultHandler rh = new ResultHandler( null );
        
        // lets say, we have an RDD with many "Resutls" ...

        // this has to be done on all RDD elements ...
        MatchingResult mr = MatchingResult.getTestMatchSamples1();
        String json = mr.toJSON();
        
        // a new RDD of JSON-Strings can now be stored into the location 
        // defined by ResultHandler.
        // 
        // this is now a RDD operation again.
        System.out.println(  ResultHandler.foldername_in_HDFS + " is used for results." );
        
    }

    /**
     * Test the export functionality for MatchingResults stored in a 
     * MatchingResultContainer
     * 
     * @throws IOException 
     */
    private static void runTestSuite_D() throws IOException {

        System.out.println( "\n TEST D" );
        System.out.println( " ======\n" );
        
        MatchingResultContainer mrc = new MatchingResultContainer();
        mrc.addResult( MatchingResult.getTestMatchSamples1());
        mrc.addResult( MatchingResult.getTestMatchSamples2());
        mrc.addResult( MatchingResult.getTestMatchSamples3());
            
        File f = new File( "tmp" );
        f.mkdir();
        
        File f1 = new File( "tmp/testSuite_D.html" );
        File f2 = new File( "tmp/testSuite_D.csv" );
        File f3 = new File( "tmp/testSuite_D.xls" );
        
        FileWriter fw1 = new FileWriter( f1 );
        fw1.write( mrc.toHTMLString() );
        fw1.close();
        
        FileWriter fw2 = new FileWriter( f2 );
        fw2.write( mrc.toCSV() );
        fw2.close();
        
        FileOutputStream fos = new FileOutputStream( f3 );
        mrc.writeToStreamAsXLSSheet( fos );
        fos.close();
 
    }
    
    /**
     * Test the export functionality for MatchingResults stored in a 
     * MatchingResultContainer
     * 
     * @throws IOException 
     */
    private static void runTestSuite_E() throws IOException {
        
        System.out.println( "\n TEST E" );
        System.out.println( " ======\n" );

        MatchingResultContainer mrc = new MatchingResultContainer();
        
        String CLAIM = "Bremsscheibe linke Seite hinten";
        String LANG = "PL";
            
        mrc.addResult( ms.getMatchesForAClaimInLanguage(CLAIM, LANG) );
        
        File f = new File( "tmp" );
        f.mkdir();
        
        File f1 = new File( "tmp/testSuite_E.html" );
        File f2 = new File( "tmp/testSuite_E.csv" );    
        File f3 = new File( "tmp/testSuite_E.xls" );
        
        FileWriter fw1 = new FileWriter( f1 );
        fw1.write( mrc.toHTMLString() );
        fw1.close();
        
        FileWriter fw2 = new FileWriter( f2 );
        fw2.write( mrc.toCSV() );
        fw2.close();
        
        FileOutputStream fos = new FileOutputStream( f3 );
        mrc.writeToStreamAsXLSSheet( fos );
        fos.close();
        
    }
    
    private static void runTestSuite_F() throws IOException {
        
        System.out.println( "\n TEST F" );
        System.out.println( " ======\n" );

        Vector<MatchingResult> res = ms._getMatchResultsWithLimit( 100 );
        
        MatchingResultContainer mrc = new MatchingResultContainer();
        mrc.vmr = res;
        
        File f = new File( "tmp" );
        f.mkdir();
        
        File f1 = new File( "tmp/testSuite_F.html" );
        File f2 = new File( "tmp/testSuite_F.csv" );    
        File f3 = new File( "tmp/testSuite_F.xls" );
        
        FileWriter fw1 = new FileWriter( f1 );
        fw1.write( mrc.toHTMLString() );
        fw1.close();
        
        FileWriter fw2 = new FileWriter( f2 );
        fw2.write( mrc.toCSV() );
        fw2.close();
        
        FileOutputStream fos = new FileOutputStream( f3 );
        mrc.writeToStreamAsXLSSheet( fos );
        fos.close();
        
    
    }
    
    private static void runTestSuite_G() throws IOException {
        
        System.out.println( "\n TEST G" );
        System.out.println(   " ======\n" );
    
        com.cloudera.utils.StatisticalSummary summary = ms.getHistogramForAllMatchesInAllRows();
         
        SummaryStatistics s = summary.s;
        
        System.out.println( "Variance : " + s.getPopulationVariance() );
        System.out.println( "Mean     : " + s.getMean() );
        System.out.println( "Min      : " + s.getMin() );
        System.out.println( "Max      : " + s.getMax() );
        
        Frequency f = summary.f;
        
        System.out.println( "Frequency      : " + f.toString() );
         
    }
    
    private static void runTestSuite_H() throws IOException {
        
        System.out.println( "\n TEST H" );
        System.out.println(   " ======\n" );
        
        
        
        for( MatchingResult r : MatchingResult.getRandomMatchResults(100) )
            ms.putMatchResults( r );
    
        TopicNetwork.exportTopicNetwork( ms.getTopicGraphLinks() , "TestH");
        
        com.cloudera.utils.StatisticalSummary summary = ms.getHistogramForAllMatchesInAllRows();
         
        SummaryStatistics s = summary.s;
        
        System.out.println( "Variance : " + s.getPopulationVariance() );
        System.out.println( "Mean     : " + s.getMean() );
        System.out.println( "Min      : " + s.getMin() );
        System.out.println( "Max      : " + s.getMax() );
        
        Frequency f = summary.f;
        
        System.out.println( "Frequency      : " + f.toString() );
        
        
         
    }
}
