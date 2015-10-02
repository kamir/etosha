/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;
  
import com.cloudera.mailer.mails.ActionMail;
import java.util.Properties;
import knowledgetools.ReviewRequestMail;
import knowledgetools.ReviewRequestReportMail;


/**
 *
 * @author kamir
 */
public class MailFactory {

    public static MailFactory mf = null;
    
    public static MailFactory init(){
        mf = new MailFactory();
        return mf;
    }
    
    static public String operator1 = "mirko@cloudera.com";
    static public String operator2 = "mirko@cloudera.com";
    static public String defaultUser = "mirko@cloudera.com";
    
    /**
     * This is a request to the reviewer.
     */
    public static ActionMail getReviewRequestMail( Properties doc, String reviewerEmail, String baseUrl ) {
        if ( reviewerEmail == null ) reviewerEmail = defaultUser;
        return new ReviewRequestMail( doc, reviewerEmail, baseUrl );
    } 

    /**
     * This is a report to the manager.
     */
    public static ActionMail getReviewRequestMail( String report ) {
        return new ReviewRequestReportMail( report, operator1 );
    } 

    
    
    
    
}
