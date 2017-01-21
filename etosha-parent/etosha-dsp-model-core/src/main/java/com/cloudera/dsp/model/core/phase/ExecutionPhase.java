
package com.cloudera.dsp.model.core.phase;

/**
 *
 * @author kamir
 */
public class ExecutionPhase extends LifeCyclePhase
{

    TestingPhase prevPhase = null;
    
    ExecutionPhase(TestingPhase aThis) {
        prevPhase = null;
    }
    
}
