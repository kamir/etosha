
import java.util.Hashtable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kamir
 */
public class SchemaRepository {

    
    static public SchemaRepository getSchemaRepository() {
        if ( repository == null ) repository = new SchemaRepository();

        // DO SOME LifeCycle things ...
        
        return repository;
    }
    
    private static SchemaRepository repository = null;
    
    private SchemaRepository() {
        init();
    }
    
    String fn = "_schemaRepository.dump";
    
    public void init() {
        // READ LAST STATE FROM DISC
        System.out.println( ">>> SchemaRepository <<< ... load from: " + fn );
        
        
    }
    
    /** 
     * USE AN APP SHUTDOWN HOOK to call this method.
     */
    public void persist() {
        // DUMP ALL IN RAM DATA INTO NEW FILE.     
        
        SchemaAnalyzer.analyzeSchemaRelations( this.simpleSchemaRepo );
        System.out.println( ">>> SchemaRepository <<< ... dump to: " + fn );

        
        
    }
    
    /**
     *   Cache the data in MEMORY ...
     */ 
    Hashtable<String,String> simpleSchemaRepo = new Hashtable<String,String>();
    
    //  Maybe, we persist also in our WIKI to enable human readers to insect ...
    boolean doSynchronizeWithWIKI = false;
    
    /**
     *  We can push all schema data also into the FUSEKI store.
     *  This is required for advanced schema analysis feature.
     */
    boolean doSynchronizeWithFUSEKI = false;
    
    
    public void putSchema(String key, String schema){
        if( doSynchronizeWithWIKI ) SchemaRepository.repository.putSchemaToWiki( key, schema );
        if( doSynchronizeWithFUSEKI ) SchemaRepository.repository.putSchemaToFUSEKI( key, schema );
        
    }

    public String getSchemaAsAVRO(String key){
        String schema = simpleSchemaRepo.get(key);
    
        // IMPLEMENT THE AVRO Schema-Creation.
        
        
        //return null;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSchema(String key){
        return simpleSchemaRepo.get(key);
    }
    
    private void putSchemaToWiki(String key, String schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void putSchemaToFUSEKI(String key, String schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    
}
