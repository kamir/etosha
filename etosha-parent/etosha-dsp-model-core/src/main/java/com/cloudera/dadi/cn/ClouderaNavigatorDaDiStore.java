package com.cloudera.dadi.cn;

import com.cloudera.dadi.DaDiStore;

/**
 *
 * @author kamir
 */
public class ClouderaNavigatorDaDiStore implements DaDiStore {

    @Override
    public boolean init() throws Exception { 
        // just ping CN ...
        System.out.println( "!!!! NEED A PING TO CN !!!" );
        return true;
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public void storeEntityWithContext(Object o) {
        
        CNViewHandler view = new CNViewHandler( o );
    
        try {
            view.persist();
            System.out.println( "> store: " + view.getInfo() );
        }
        catch( Exception ex ) {
            System.out.println( "> tried to store: " + view.getClass() + " !!! " + ex.getMessage() );
        }
    
    }

    
    
}
