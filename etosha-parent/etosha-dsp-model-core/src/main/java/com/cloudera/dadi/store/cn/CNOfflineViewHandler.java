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
class CNOfflineViewHandler {
    
    DaDiEntity ent = null;
    
    // uses MetadataSink ...
    // works like CNClientTester ...

    CNOfflineViewHandler(Object o) {
        ent = (DaDiEntity)o;
    }

    void persist() {
        
        Properties props = ent.getProperties();
        Vector<String> tags = ent.getTags();
        String id = ent.getSubject();

        ObjectMetadata omd = new ObjectMetadata();
        omd.setIdentity( id );
        omd.addProperties(props);
        omd.addTags(tags);
        
        // here we read Metadata from a local path ....        
        //     ObjectMetadata omd = MetadataSink.getMDOForHDFSFolderByName( id );
        //        
        //     omd.initDataFromJSON();
        //     omd.addTags( tags );
        //     omd.addProperties(props);
        
        MetadataSink.putMDOLocal( omd, OfflineClouderaNavigatorDaDiStore.pw );
        
    }

    String getInfo() {
        return ent.describe();
    }
    
}
