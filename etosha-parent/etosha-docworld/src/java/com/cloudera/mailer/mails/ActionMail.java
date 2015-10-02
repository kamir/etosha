package com.cloudera.mailer.mails;

import java.util.Date;
import java.util.Properties;

/**
 *
 * @author kamir
 */
abstract public class ActionMail {
    
    public ActionMail() {
        d = new Date( System.currentTimeMillis() );
    }
     
    public Date d = null;
    
    static public String from = "mirko.kaempf@googlemail.com";
    
    public String to = null;
    public String subject = "Testmail :: FAQ ACTION REQUIRED";
    
    public String messageText = "Guess what you could do now?";
    
    public String[] attachements = null;
    public String[] files = null;

    Properties props = null;

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
    
}
