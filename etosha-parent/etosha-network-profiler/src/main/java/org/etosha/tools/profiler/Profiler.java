package org.etosha.tools.profiler;

import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import org.gephi.graph.api.UndirectedGraph;

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

    public UndirectedGraph getGraph();
    
}
