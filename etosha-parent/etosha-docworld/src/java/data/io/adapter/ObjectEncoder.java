/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.io.adapter;

import java.util.*;
import java.io.*;

import java.io.IOException;
import org.apache.hadoop.hbase.util.Base64;

/**
 *
 * @author kamir
 */
public class ObjectEncoder {
    
   /** Read the object from Base64 string. */
   public static Object fromString( String s ) throws IOException ,
                                                       ClassNotFoundException {
        byte [] data = Base64.decode( s );
        
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
   }

    /** Write the object to a Base64 string. */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return new String( Base64.encodeBytes( baos.toByteArray() ) );
    }
    
}
