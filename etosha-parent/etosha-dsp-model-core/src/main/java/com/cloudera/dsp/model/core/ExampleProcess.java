
package com.cloudera.dsp.model.core;

/**
 *
 * @author kamir
 */
public class ExampleProcess {
    
    public static void main(String args[]) {
    
        /**
         * This class is a reference and acts as an automatic Metadata exposure
         * DEMO for the "Enterprise Metadata Toolsuite".
         */
        ProcessRepresentation process = new ProcessRepresentation();
           
        process.init();
        
        System.out.println( ">>> DSP Demo ... ");
        
        System.out.println( "> Prepare data ... ");
        process.evolve();
        
        System.out.println( "> Train the model ... ");
        process.evolve();

        System.out.println( "> Test the model ... ");
        process.evolve();
        
        System.out.println( "> Execute the model ... ");
        process.evolve();
        process.evolve();
        process.evolve();

    }
    
}
