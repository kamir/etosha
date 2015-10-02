/**
 * Our simplest representation of a Time Series is
 * a binary data array and a properties object.
 * 
 **/
package data.ts;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author kamir
 */
public class SimpleDocWrapper implements Serializable {
    
    public byte[] data;
    
    public Properties props = new Properties();
    
}
