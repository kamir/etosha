package knowledgetools;

import com.cloudera.mailer.mails.ActionMail;

 
public class ReviewRequestReportMail extends ActionMail {

    
    public ReviewRequestReportMail(String report, String receiver) {
        
        super();
        
        messageText = "Today I created some FAQ-Item review requests.\n\n" + report;
        
        subject = "TT.FQA :: Review Request Report";

        to = receiver;  
        
    }
    
 
}
