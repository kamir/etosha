package com.cloudera.descriptors.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class JSONEntityDescriptor { 
    
    Vector<String> tags = new Vector<String>();
    Properties props = new Properties();
    
    String name = "NAME.";
    String description = "DESCR.";
    
    public void initDEMODESCRIPTOR() {
         
        putTag( "unbounded" );
        putTag( "large" );
        putTag( "critical" );
        putTag( "has_LIBSVM_structure" );
        
        putProperty( "volume", "100 GB" );
        putProperty( "has_profile", "false" );
    
    }
    
    public String toJSON() throws Exception, FileNotFoundException {
        
        try(Writer writer = new OutputStreamWriter(new FileOutputStream("./data/json/output.json") , "UTF-8")){

            Gson gson = new GsonBuilder().create();
        
            writer.append("curl http://172.24.128.52:7187/api/v8/entities/ec4d0af578abbe11b9eb2f471c89fbdc -u admin:LH_hdp01 -X PUT -H \"Content-Type: application/json\" -d '{");

            writer.append("\"name\":");
            
            gson.toJson(name, writer);
            
            writer.append(", \"description\":");
            
            gson.toJson(description, writer);
            
            writer.append(", \"tags\":");
            
            gson.toJson(tags, writer);
            
            writer.append(", \"properties\":");

            gson.toJson(props, writer);

            writer.append("}}'");
           
        }
        
        
        return ".";
    }

    private void putTag(String tag) {
        tags.add( tag );
    }

    private void putProperty(String k, String v) {
        props.put(k, v);
    }
    
    
    
}
