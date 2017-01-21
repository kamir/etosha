package com.cloudera.navigator.client;

import com.jayway.jsonpath.JsonPath;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class ObjectMetadata {

    String response = null;
    
    public void setIdentity(String id) {
        this.props.put("SUBJECT", id);
    }

    
    Vector<String> tags = new Vector<String>();
    Properties props = new Properties();
 
    public ObjectMetadata() {}
 
    
    public ObjectMetadata( String json_representation ) {
        response = json_representation;
    }
 
    String getIdentity() {
        
        String identity = "UNKNOWN";
        
        if ( response != null ) {
                try {
                    List<String> ids = JsonPath.read(response, "$.[*].identity");
                    if ( ids != null && ids.size() > 0 ) return ids.get(0);
                    else return identity;
                }
                catch( Exception ex) {
                    System.out.println( ">>> JSON ISSUE: " + ex.getMessage() );
                }
        }
        
        if ( props.containsKey("SUBJECT") ) {
                identity = (String) props.get("SUBJECT");
        }        
        
        return identity;
    }
    
    public void deleteTags() {
    
    }
    
    public void addTag( String tag ) {
    
    }
    
    public void deleteAllProperties() {
    
    }

    public void deleteProperty(String k) {
    
    }
    
    public void addProperty( String k, String v ) {
    
    }

    public void addTags(Vector<String> t) {

        Enumeration _tags = t.elements();
        while( _tags.hasMoreElements() ) {
            
            String id = this.getIdentity();
            
            String tag = (String)_tags.nextElement();
           
            System.out.println( "### ADD-TAG ###"   );
            System.out.println( "SUBJECT         : " + id );
            System.out.println( "key             : " + "IS_TAGGED_AS" );
            System.out.println( "value           : " + tag );
            System.out.println();
            
        }
        
        tags.addAll(t);
        
    }

    public void addProperties(Properties p) {
        Enumeration names = p.propertyNames();
        while( names.hasMoreElements() ) {
            
            String id = this.getIdentity();
            
            String key = (String)names.nextElement();
            String value = p.getProperty(key);
           
            System.out.println( "### ADD-KVP ###"   );
            System.out.println( "SUBJECT         : " + id );
            System.out.println( "key             : " + key );
            System.out.println( "value           : " + value );
            System.out.println();
        }

        props.putAll(p);
    }

    public void initDataFromJSON() {
        System.out.println( ">>> READ JSON DATA FROM RESPONSE ... ");
    }

    
    
}
