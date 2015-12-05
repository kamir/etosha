/*
 * Copyright 2015 The Apache Software Foundation.
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
package org.etosha.tools;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SemanpixServerManager {
    
    public static void main(String[] ARGS) 
            throws FileNotFoundException, 
                   KeyStoreException, 
                   NoSuchAlgorithmException, 
                   CertificateException, 
                   IOException {

        String alias = "www.semanpix.de";
        String HOST = "semanpix.de";
        String PORT = "443";
        
        // GET CERTIFICATE from server using openssl
        String command = "echo -n | openssl s_client -connect " + HOST + ":" + PORT + 
                         " | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > "+alias+".cert"; 
        
        File f = new File( alias+".cert" );
        
        
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/sh", "-c", command
        );

        Process process = pb.start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.println(">>> CERTIFICATE was requested from " + HOST + ":" + PORT);

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        
        System.out.println(">>> FILE written to: " + f.getAbsolutePath() + "(available:" + f.canRead() + ")");


        
        
        String cmd2 = "sudo keytool -import -trustcacerts -keystore /Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home/jre/lib/security/cacerts -storepass changeit -noprompt -alias " + alias + " -file " + f.getAbsolutePath();

        ProcessBuilder pb2 = new ProcessBuilder(
                "/bin/sh", "-c", cmd2
        );
        Process process2 = pb2.start();

        InputStream is2 = process.getInputStream();
        InputStreamReader isr2 = new InputStreamReader(is2);
        BufferedReader br2 = new BufferedReader(isr2);
        String line2;

        System.out.println(">>> CERTIFICATE import ..." );

        while ((line2 = br2.readLine()) != null) {
            System.out.println(line);
        }
        
        
        
        
        //  SSLUtil.ensureSslCertIsInKeystore( alias, new FileInputStream( f.getAbsolutePath() ));

    }
    
}
