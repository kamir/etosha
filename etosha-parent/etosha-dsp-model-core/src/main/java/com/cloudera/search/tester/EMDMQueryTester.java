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
package com.cloudera.search.tester;

import com.cloudera.search.queries.FieldStatsQuery;
import com.cloudera.search.queries.IdQuery;
import com.cloudera.search.queries.SimpleFieldQuery;
import com.cloudera.search.QueryConstants;
import com.cloudera.search.queries.SecondOrderQuery; 
import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kamir
 */
public class EMDMQueryTester {
    
    
    private static final String CONNECTION_URL_PROPERTY = "connection.url";
    private static final String JDBC_DRIVER_NAME_PROPERTY = "jdbc.driver.class.name";
    private static final String JDBC_QUERY = "jdbc.query";
    private static final String KEYTAB_FILE = "keytab.file";
    
    private static String connectionUrl;
    private static String jdbcDriverName;
    private static String query;
    private static String keytabFile;
    
    public static void main( String[] a ) throws IOException, MalformedURLException, SolrServerException, Exception {

        
        String jaasFile = "./jaas.conf";
        File f1 = new File(jaasFile);
        
        System.out.println(">>> JAAS-Config : " + fileExists(jaasFile));
        System.out.println(">>> file        : " + f1.getAbsolutePath() );

        String krb5conf = "./krb5.conf";
        File f2 = new File(krb5conf);
        System.out.println(">>> KRB5-Config : " + fileExists(krb5conf));
        System.out.println(">>> file        : " + f2.getAbsolutePath() );

        File fJaasFile = new File(jaasFile);

        System.setProperty("java.security.auth.login.config", fJaasFile.getAbsolutePath());
        System.setProperty("java.security.krb5.conf", krb5conf);
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        
        
                // Obtain a LoginContext, needed for authentication. Tell 
        // it to use the LoginModule implementation specified by 
        // the entry named "JaasSample" in the JAAS login 
        // configuration file and to also use the specified 
        // CallbackHandler.
        LoginContext lc = null;
        try {
            lc = new LoginContext("Search-client",
                    new TextCallbackHandler());
        } catch (LoginException le) {
            System.err.println("Cannot create LoginContext. "
                    + le.getMessage());
            System.exit(-1);
        } catch (SecurityException se) {
            System.err.println("Cannot create LoginContext. "
                    + se.getMessage());
            System.exit(-1);
        }

        try {

            // attempt authentication
            lc.login();

        } catch (LoginException le) {

            System.err.println("Authentication failed: ");
            System.err.println("  " + le.getMessage());
            System.exit(-1);

        }

        System.out.println("Authentication succeeded!");
        
        
        String[] args = stripOutQuotes( a );
 
        if ( a.length < 1 ) {
            a = new String[1];
            a[0] = "*";
            args = stripOutQuotes( a );
        }
        
        
            
        SimpleFieldQuery psq1 = SimpleFieldQuery.init();
        psq1.buildQuery( "OND_OFFER");
        psq1.executeQueryZeroRows();
        // System.out.println( psq1.getResultAsXML() );
        System.out.println( psq1.getNrOfResult() );
        psq1.reset();
        
        SimpleFieldQuery psq2 = SimpleFieldQuery.init();
        psq2.buildQuery( "ENTERTAINMENT_STATISTICS");
        psq2.executeQueryZeroRows();
        // System.out.println( psq2.getResultAsXML() );
        System.out.println( psq2.getNrOfResult() );
        psq2.reset();
        
        SimpleFieldQuery psq3 = SimpleFieldQuery.init();
        psq3.buildQuery( "LOUNGE_OFFER_STATISTICS");
        psq3.executeQueryZeroRows();
        // System.out.println( psq3.getResultAsXML() );
        System.out.println( psq3.getNrOfResult() );
        psq3.reset();
        
        SimpleFieldQuery psq4 = SimpleFieldQuery.init();
        psq4.buildQuery( "ASR_STATISTICS");
        psq4.executeQueryZeroRows();
        // System.out.println( psq4.getResultAsXML() );
        System.out.println( psq4.getNrOfResult() );
        psq4.reset();

//////        IdQuery idq = IdQuery.init();
//////        idq.buildQuery( args[0] );
//////        idq.executeQuery();
//////        System.out.println( idq.getResultAsXML() );
//////        idq.reset();
        
//        SecondOrderQuery soq = SecondOrderQuery.init();
//        soq.buildQuery(  args[0] );
//        soq.executeQuery();
//        System.out.println( soq.getResultAsJSON() );
//        soq.reset();
        
//        MLTQueryTester.testMoreLikeThisQuery( args[0] );

//        ComboTester.testQueryCombo(args[0]);
        
//        FieldStatsQuery fsq = FieldStatsQuery.init();
//        fsq.buildQuery( QueryConstants.defaultFields_SMILE_APP.getAsStringArray() );
//        fsq.executeQuery();
//        System.out.println( fsq.getResultAsXML() );

    }

    /**
     * We expect " always pairwaise to be able to strip them.
     * 
     * @param args
     * @return 
     */
    private static String[] stripOutQuotes(String[] args) {
        
        String[] a = new String[args.length];
        
        int i = 0; 
        
        for( String s : args ) {
            if ( s.startsWith("'") && s.endsWith("'") || s.startsWith("\"") && s.endsWith("\"") ) {
                int l = s.length();
                s = s.substring(1,l-1);
            } 
            
            a[i] = s;
            System.out.println("STRIP: ("+i+") " + s);
            i++;
        };
        return a;
    }
    
       private static void loadConfiguration() throws IOException {
        InputStream input = null;
        try {

            String filename = "config.props";
            input = new FileInputStream( new File( filename ) );

            Properties prop = new Properties();
            prop.load(input);

            connectionUrl = prop.getProperty(CONNECTION_URL_PROPERTY);
            jdbcDriverName = prop.getProperty(JDBC_DRIVER_NAME_PROPERTY);

            query = prop.getProperty(JDBC_QUERY);
            keytabFile = prop.getProperty(KEYTAB_FILE);

        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private static boolean fileExists(String filename) {
        File f = new File(filename);
        return (f.exists() && !f.isDirectory());
    }
}
