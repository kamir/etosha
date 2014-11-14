/*
 * Copyright 2014 The Apache Software Foundation.
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
package org.etosha.core.sc.connector.external;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchProviderException;
import java.util.Properties;
import java.util.Vector;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.security.auth.login.LoginException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.cmd.EtoshaContextLogger;

/**
 *
 * @author training
 */
public class GMailLoader {
    
    public static EtoshaContextLogger clt = null;
    
    public static Configuration cfg = null;
    
  

    public static void run(String[] args) throws UnknownHostException, LoginException, IOException {
        
        /** 
         * We import all mails in subfolders to categories which have the same name
         * like the subfolder.
         * 
         * We create catgeories for each subfolder in cat=label 
         * if they do not exist.
         */
        String label = "etosha";
        
        if (args != null) {
            label = args[1];
        }
        
        if ( label == null ) {
            label = "etosha"; 
        }    
        
        GMailLoader.importEmailsForLabels( label );
        
    }

    public static String[] importEmailsForLabels(String label) throws LoginException, IOException {

        Vector<String> list = new Vector<String>();
        
        String[] senderList = null;
        
        Properties props = System.getProperties();
        
        props.setProperty("mail.imaps.host" , "imap.gmail.com");
        props.setProperty("mail.store.protocol" , "imaps");
        props.setProperty("mail.imaps.port" , "993");
        
        try {
            
            Session session = Session.getDefaultInstance(props, null);

            String name = javax.swing.JOptionPane.showInputDialog("Username:");
            String pwd = javax.swing.JOptionPane.showInputDialog("Password:");
            
            String account = name;  
            Store store2 = session.getStore("imaps");
            store2.connect("imap.gmail.com", account, pwd);

            Folder[] f2 = store2.getDefaultFolder().list();
            
            for(Folder fd:f2)
                System.out.println("("+account+") >> "+fd.getName());
     
            System.out.print(">>> work on: "+ label );
            
            Folder f = store2.getFolder(label);
            f.open(Folder.READ_ONLY);
            Folder[] f3 = f.list();
            System.out.println("  z="+ f3.length );
            
            for(Folder fd:f3) {
                createCategoriesForFolders( fd , label );
            }
        } 
        catch (MessagingException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        return senderList;
    }
 

    private static void createCategoriesForFolders(Folder f, String label) throws MessagingException, LoginException, IOException {
          
          System.out.println( "**** " + f.getFullName() );
        
//          clt.scb.createCategoryIfNotExists( f.getName() );
          
          f.open(Folder.READ_ONLY);
          
          Message[] m2 = f.getMessages();
          
          for(Message m : m2) { 
              
              System.out.println( "[[Category:" + f.getFullName() + 
                      "]]  > " + m.getSubject() );
              
//              clt.scb.appendMailIfNotExists( m, f.getFullName() );
              
          }
          
    }
}
