package com.cloudera.dadi;

/**
 * DaDiStore is marker Interface for Data Dictionary Store Implementations
 * 
 * @author kamir
 */
public interface DaDiStore {
    
    public boolean init() throws Exception;
    
    public boolean close();
    
    public void storeEntityWithContext( Object o );
    
}
