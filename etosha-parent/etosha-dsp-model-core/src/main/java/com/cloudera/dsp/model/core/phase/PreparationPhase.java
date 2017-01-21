
package com.cloudera.dsp.model.core.phase;

/**
 *
 * @author kamir
 */
public class PreparationPhase extends LifeCyclePhase
{
    
    public LifeCyclePhase nextPhase() {
        System.out.println( this.getClass() + " >>> go on to training ...");
        return new TrainingPhase( this );
    }
    
    
}
