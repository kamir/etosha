
package com.cloudera.mailer;

import com.cloudera.mailer.mails.ActionMail;
import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import knowledgetools.ReviewRequestMail;

/**
 *
 * @author kamir
 */
public class MailerTool {
    
    public static void main(String[] ARGS){
        sendMail( new ReviewRequestMail(new Properties(), "mirko.kaempf@cloudera.com", "b") );
    }
    
    public static String sendMail(ActionMail m) {
        String result;
 
        // Sender's email ID needs to be mentioned
        final String from = m.from;
        
        System.out.println( ">> FROM: " + from);

        //Get the session object  
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", from);
                
        

        // Get the default Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication( from, "LeipzigBS5#");//change accordingly  
                    }
                });

        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
        
            System.out.println( message );

            message.setHeader("Content-Type", "text/html");
            
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress( m.to ));
            // Set Subject: header field
            message.setSubject(m.subject);
            // Now set the actual message
            
            if ( m.files != null ) {

                Multipart multipart = new MimeMultipart();
                
                MimeBodyPart tbp = new MimeBodyPart();
                tbp.setText( m.messageText,"charset=ISO-8859-1", "html" );
                tbp.setHeader("Content-Type", "text/html");

                multipart.addBodyPart( tbp );
                
                for( int i = 0; i < m.files.length; i++ ) {

                    MimeBodyPart messageBodyPart = new MimeBodyPart();

                    messageBodyPart = new MimeBodyPart();

                    if ( m.files[i].startsWith( "files/") ) {
                        m.files[i]= m.files[i].substring(6);
                        System.out.println(">>> REMOVED prefix ... " + m.files[i] );
                    }    
                    else {
                        System.out.println(">>> Nothing to REMOVE !!! " + m.files[i] );
                    }                    
                    String file = "files/" + m.files[i];
                    String fileName = m.attachements[i];

                    DataSource source = new FileDataSource(file);
   
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(fileName);
                    multipart.addBodyPart(messageBodyPart);

                    
                }
                
                message.setContent(multipart, "text/html");

                
            }
            else {
                message.setContent( m.messageText, "text/html" );
            
            }

            System.out.println( ">>> Send now ...");

            // Send message
            Transport.send(message);
            result = ">>> Sent message "+ m.getClass() + " successfully ...";
            
            
        } catch (MessagingException mex) {
            mex.printStackTrace();
            
            result = "!!! ERROR !!! Was unable to send message  "+ m.getClass() + " ...";
        }
        
        System.out.println( result );
        
        return result;
    }
    
}
