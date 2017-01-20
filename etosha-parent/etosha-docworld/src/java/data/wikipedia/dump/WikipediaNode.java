/**
 *
 * Our simplest representation of a Wikipedia Page is
 * a binary data array together with a set of properties.
 * 
 **/
package data.wikipedia.dump;
 
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * @author kamir
 */
public class WikipediaNode implements Serializable, Writable, WritableComparable {
    
    public byte[] data;
    public Properties props = new Properties();
    
    public WikipediaNode( byte[] _data, Properties _props ) {
        data = _data;
        props = _props;
    }

    public void write(DataOutput d) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void readFields(DataInput di) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
