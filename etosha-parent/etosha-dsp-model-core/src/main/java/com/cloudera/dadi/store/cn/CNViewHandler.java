package com.cloudera.dadi.store.cn;

import com.cloudera.navigator.client.MetadataSink;
import com.cloudera.navigator.client.ObjectMetadata;
import com.cloudera.neo4j.domain.DaDiEntity;
import java.util.Properties;
import java.util.Vector;

/**
 * 
 * @author kamir
 */
class CNViewHandler {
    
    DaDiEntity ent = null;
    
    // uses MetadataSink ...
    // works like CNClientTester ...

    CNViewHandler(Object o) {
        ent = (DaDiEntity)o;
    }

    void persist() {
        
        Properties props = ent.getProperties();
        Vector<String> tags = ent.getTags();
        String id = ent.getSubject();
        
        ObjectMetadata omd = MetadataSink.getMDOForHDFSFolderByName( id );
        
        omd.initDataFromJSON();
        omd.addTags( tags );
        omd.addProperties(props);
        
        MetadataSink.putMDO( omd );
        
    }

    String getInfo() {
        return ent.describe();
    }
    
}
