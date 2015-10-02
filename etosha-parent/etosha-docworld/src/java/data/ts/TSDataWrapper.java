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
public class TSDataWrapper implements Serializable {
    
    public double[] data;
    public Properties props = new Properties();
    
    public TSDataWrapper( int nrOfvalues ) {
        data = new double[nrOfvalues];
        for( int i = 0; i < nrOfvalues; i++ ) {
            data[i] = 0.0;
        }
    }
    
    public static TSDataWrapper initRandomSeries( int z ) {
        TSDataWrapper mapper = new TSDataWrapper( z );
        for( int i = 0; i < z; i++ ) {
            mapper.data[i] = Math.random();
        }
        return mapper;
    }
}
