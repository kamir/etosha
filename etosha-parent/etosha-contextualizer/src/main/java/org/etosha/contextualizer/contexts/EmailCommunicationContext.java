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
package org.etosha.contextualizer.contexts;

import com.google.api.client.util.Base64;
import java.net.URLEncoder;

import com.google.api.services.gmail.model.Message;
import org.etosha.contextualizer.IContextualizer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.etosha.vocab.EtoshaEmailVocabulary;
import org.openrdf.query.Dataset;

/**
 *
 * A context defines properties and background information. The context is
 * represented by a Semantic-Graph.
 *
 * @author kamir
 */
public class EmailCommunicationContext {

    /**
     * 
     * The model is updated by a Contextualizer object, not directly!
     * 
     * @param m
     * @param labels
     * @param cont
     * @return
     * @throws MessagingException
     * @throws IOException 
     */
    public static Model getGoogleEmailAsModel(com.google.api.services.gmail.model.Message m,MimeMessage email, List<String> labels, String thread, IContextualizer cont) throws MessagingException, IOException {

        
        Model model = cont.getModel();


        try {
            
            Properties props = new Properties();

            // TODO 
            // MAKE SURE, that NULL values do not break the import !!!
            
            String sub = email.getSubject();
            if ( sub == null ) sub = "NO SUBJECT";
            props.put("subject", sub );
            
            props.put("from", email.getFrom() != null ? email.getFrom()[0].toString() : "None");
            
            String time = email.getSentDate().getTime() + "";
            props.put("time", time);
            
            props.put("snippet", m.getSnippet());
            props.put("threadId", m.getThreadId());
            props.put("id", m.getId());
            // props.put("body", getText(email.));

            
            /**
             * IDENTIFIER OF THE MAIL --- NOT as in SOLR. SOLR uses the google-mail-id
             */
            String mailIDLiteral = "#####" + props.get("from") + "___" + props.get("time") + "___" + props.get("subject");
            String urlPART = URLEncoder.encode(mailIDLiteral);

            
            
            
            
            
            
            
            Resource mail = model.createResource(EtoshaEmailVocabulary.getURI().concat(urlPART));

            
            
            
            
            
            
            
            //model.add(mail, EtoshaEmailVocabulary.sender, (String) props.get("from"));
            cont.putSPO(urlPART, EtoshaEmailVocabulary.sender, (String) props.get("from"));

            for( String label : labels ) {
                //model.add(mail, EtoshaEmailVocabulary.hasLabel, label);
                cont.putSPO(urlPART, EtoshaEmailVocabulary.hasLabel, label);
            }
            
             
                //model.add(mail, EtoshaEmailVocabulary.hasLabel, label);
                cont.putSPO(urlPART, EtoshaEmailVocabulary.hasMailID, (String)props.get("id"));
            
            
            
            if ( thread != null ) {
                //model.add(mail, EtoshaEmailVocabulary.belongsToThread, (String)thread);
                cont.putSPO(urlPART, EtoshaEmailVocabulary.belongsToThread, (String)thread );
            }

            cont.putSPO(urlPART, EtoshaEmailVocabulary.dateReceived, (String)props.get("time") );
                            
            
            //model.add(mail, EtoshaEmailVocabulary.subject, (String) props.get("subject"));
            cont.putSPO(urlPART, EtoshaEmailVocabulary.subject, (String) props.get("subject"));
            
            Address[] adList = email.getAllRecipients();
            if( adList != null )  {
                for (Address a : adList) {
                    //model.add(mail, EtoshaEmailVocabulary.receiver, a.toString());
                    cont.putSPO(urlPART, EtoshaEmailVocabulary.receiver, a.toString() );

                }
            }
            else {
                cont.putSPO(urlPART, EtoshaEmailVocabulary.receiver, "NONE" );
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

 
    private boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

}
