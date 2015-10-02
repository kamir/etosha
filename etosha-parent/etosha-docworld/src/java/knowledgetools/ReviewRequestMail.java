package knowledgetools;

import com.cloudera.mailer.mails.ActionMail;
import java.util.Properties;

 
public class ReviewRequestMail extends ActionMail {

    
    public ReviewRequestMail(Properties prop, String receiver, String urlBase ) {

        super();
        
        String requestLink = urlBase + prop.getProperty("id") + "&reviewer="+receiver;
        
        String q = prop.getProperty("question");
        String a = prop.getProperty("answer");
        String c = prop.getProperty("context");
        
        messageText = "Hey FAQ-team member, <br><br>here is the RRR-Tool (RandomRevieRequestor). I recently joined the Cloudera training-team to support the knowledge flow.\nMy task is to request " +
                      "quality-reviews from the experts, this means <b>from you!</b> Therefore I pick unvalidated items from our "+
                      "knowledge base. <br><br>Please review and/or rephrase this raw-item from our knowledge base.\n\n" 
                    + "<br><br>" + requestLink + "<br><br>" + getVPNWarningString() + "<br><br>"
                    + "<i>Here is a preview of the item, may be you come back later if you have a free minute for me</i> ;-)"
                    + "<br><hr><br>"
                    + "<b>Q:</b>" + q + "<br><br>"
                    + "<b>A:</b>" + a + "<br><br>"
                    + "<b>Context:</b>" + c + "<br><br>"
                    + "<br><hr>Many thanks for your contribution!<br><br>"
                    + "<b>PS:</b> If you have no time, please set the number of items I can send you per week to 0 or even "
                    + "to a higher value, in case you want more review requests.<br>"
                    + "<a href='https://docs.google.com/spreadsheets/d/1thIL4hd4-TISvIXkkSzOeuCja6NMyVGsGe9OM3Aw2Qs/edit#gid=0'>Contributor List</a>";
                  
        subject = "TT:FAQ # Review Request";
        
        to = receiver;
        
    }

    private String getVPNWarningString() {
        return "<img src='http://training03.mtv.cloudera.com/vpn.png' width='32' hight='32' alt='(requires VPN connection)'/>";
    }
    
 
}
