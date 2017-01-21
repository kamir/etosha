
package com.cloudera.dsp.model.core.phase;

/**
 *
 * @author kamir
 */
public class TestingPhase extends LifeCyclePhase
{

    TrainingPhase prevPhase = null;
    
    TestingPhase(){   
        super();
    }
    
    TestingPhase(TrainingPhase aThis) {
        super();
        prevPhase = aThis;
    }
    
    public LifeCyclePhase nextPhase() {
        System.out.println( this.getClass() + " >>> go on to execution ...");
        return new ExecutionPhase( this );
    }
    
}
