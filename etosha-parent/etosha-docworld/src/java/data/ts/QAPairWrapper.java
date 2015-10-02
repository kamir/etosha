/**
 * Our simplest representation of Question Answer Pair is
 * a Java-Properties object.
 * 
 **/
package data.ts;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author kamir
 */
public class QAPairWrapper implements Serializable {
    
    public Properties props = new Properties();
    
}
