
package knowledgetools;

import java.util.Properties;

/**
 *
 * @author kamir
 */
class ReviewRequest {

    @Override
    public String toString() {
        return "ReviewRequest{" + mail + " => " + pr.getProperty("id") + '}';
    }
    
    String mail = "";
    Properties pr = null;

    ReviewRequest(String m, Properties p) {
        mail = m;
        pr = p;
    }
    
    
}
