package data.io;

import hadoop.cache.doc.HBaseDocWorld;

/**
 * Here we scan for all items with and reset their valid-state to 0
 * 
 * @author kamir
 */
public class ResetItemsTool {

    ResetItemsTool rrt = null;
        
    /**
     * This tool has an HBaseDocWorld handler to talk to the DB.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ResetItemsTool rrt = new ResetItemsTool();
        rrt.resetAllItems();
    
    }

    HBaseDocWorld dw = null;
    
    public ResetItemsTool() {
        dw = new HBaseDocWorld();
        dw.init();
    }
    
    private void resetAllItems() {
        
        // SCAN HBASE FOR to reset all valid-states to 0 
        String m = dw.resetItemsToState("0");
        System.out.println("Reset m=" + m + " items." );
        
    }


}
