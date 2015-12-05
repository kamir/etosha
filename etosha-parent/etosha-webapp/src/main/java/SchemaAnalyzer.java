import java.util.Hashtable;

/**
 *
 * @author kamir
 */
class SchemaAnalyzer {

    /**
     * For all schemas we can calculate some metrics do show their dependency
     * graph.
     * 
     * @param simpleSchemaRepo 
     */
    static void analyzeSchemaRelations(Hashtable<String, String> simpleSchemaRepo) {

    }
    
    /**
     * NORMALIZE by ordering ...
     *
     * If schema A is the same as B they must be the same point in the 
     * term-vector-space.
     * 
     * [A] TERM IS: type___name
     * (1) Order is not relevant in this case
     * 
     * [B] TERM IS: NR___type
     * (1) no matter how the column is named, the order and the type matters.
     */
    
    /**
     * Use only pairs with same number of columns.
     * 
     * All pairs with the same number of columns form schema-clusters.
     * 
     * Shorter schemas can be included in such clusters.
     * Such clusters can overlap.
     */
    
    
}
