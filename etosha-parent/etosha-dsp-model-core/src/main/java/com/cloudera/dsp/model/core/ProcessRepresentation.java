
package com.cloudera.dsp.model.core;

import com.cloudera.dsp.model.core.phase.LifeCyclePhase;
import com.cloudera.dsp.model.core.phase.PreparationPhase;

/**
 *
 * @author kamir
 */
public class ProcessRepresentation {
    
    public LifeCyclePhase phase = null;

    void init() {   
        phase = new PreparationPhase();
    }
    
    void evolve(){
        phase = phase.nextPhase();
    }
    
}
