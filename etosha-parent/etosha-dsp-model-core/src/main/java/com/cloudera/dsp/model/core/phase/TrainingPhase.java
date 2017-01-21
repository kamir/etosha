
package com.cloudera.dsp.model.core.phase;

/**
 *
 * @author kamir
 */
public class TrainingPhase extends LifeCyclePhase
{
    PreparationPhase prevPhase = null;
    
    TrainingPhase() {
        super();
    }

    
    TrainingPhase(PreparationPhase aThis) {
        super();
        prevPhase = aThis;
    }
    
    public LifeCyclePhase nextPhase() {
        System.out.println( this.getClass() + " >>> go on to testing ...");
        return new TestingPhase( this );
    }
    
}
