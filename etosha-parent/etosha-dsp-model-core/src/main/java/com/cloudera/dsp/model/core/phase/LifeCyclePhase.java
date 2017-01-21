
package com.cloudera.dsp.model.core.phase;

/**
 *
 * @author kamir
 */
public class LifeCyclePhase {

    public LifeCyclePhase nextPhase() {
        System.out.println( this.getClass() + " >>> go on ...");
        return this;
    }
    
}
