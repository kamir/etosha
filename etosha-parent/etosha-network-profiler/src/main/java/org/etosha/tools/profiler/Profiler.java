package org.etosha.tools.profiler;

import java.io.File;

/**
 *
 * @author kamir
 */
public interface Profiler {

    public int getNumberEdges();

    public int getNumberVertices();

    public double getDiameter();

    public int getMaxCLusterNrNodes();

    public int getMaxCLusterNrEdges();

    public double getGlobalClusterCoefficient();

    public void storeImage(File folderOut, int timeInSeconds);
    
}
