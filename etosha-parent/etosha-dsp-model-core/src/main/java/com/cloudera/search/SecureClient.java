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
package com.cloudera.search;

import com.cloudera.search.queries.AbstractQuery;
import com.cloudera.search.tester.*;
import com.cloudera.search.queries.FieldStatsQuery;
import com.cloudera.search.queries.IdQuery;
import com.cloudera.search.queries.SimpleFieldQuery;
import com.cloudera.search.queries.SecondOrderQuery; 
import com.cloudera.search.queries.emdm.BadDatasetsQuery;
import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kamir
 */
public class SecureClient {
    
    private static final String CONNECTION_URL_PROPERTY = "connection.url";
    private static final String JDBC_DRIVER_NAME_PROPERTY = "jdbc.driver.class.name";
    private static final String JDBC_QUERY = "jdbc.query";
    private static final String KEYTAB_FILE = "keytab.file";
    
    private static String connectionUrl;
    private static String jdbcDriverName;
    private static String query;
    private static String keytabFile;

    static String jaasFile = "/etc/lh-emdm/jaas.conf";
    static String krb5conf = "/etc/lh-emdm/krb5.conf";
    
    public static void main(String[] ARGS) {
        try {
            login();
        } catch (IOException ex) {
            Logger.getLogger(SecureClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SolrServerException ex) {
            Logger.getLogger(SecureClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SecureClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void login() throws IOException, MalformedURLException, SolrServerException, Exception {

        File f1 = new File(jaasFile);
        
        System.out.println(">>> JAAS-Config : " + fileExists(jaasFile));
        System.out.println(">>> file        : " + f1.getAbsolutePath() );

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
            
            AbstractQuery.loggedIn = true;

        } 
        catch (LoginException le) {

            System.err.println("Authentication failed: ");
            System.err.println("  " + le.getMessage());

            AbstractQuery.loggedIn = true;

        }

        System.out.println("Authentication succeeded!");
     
        IdQuery idq = IdQuery.init();
        idq.buildQuery( "*" );
        idq.executeQuery();
        System.out.println( idq.getResultAsXML() );
        idq.reset();
        
        BadDatasetsQuery badq = BadDatasetsQuery.init();
        badq.buildQuery( "bad_model" );
        badq.executeQuery();
        System.out.println( badq.getNrOfResult());
        badq.reset();

        BadDatasetsQuery badq2 = BadDatasetsQuery.init();
        badq2.buildQuery( "valid_model" );
        badq2.executeQuery();
        System.out.println( badq2.getNrOfResult());
        badq2.reset();

        System.out.println("Authentication status: " + AbstractQuery.loggedIn );
     
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
