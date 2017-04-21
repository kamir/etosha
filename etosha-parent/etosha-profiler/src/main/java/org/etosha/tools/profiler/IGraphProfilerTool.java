package org.etosha.tools.profiler;

// JUNG 2
import edu.uci.ics.jung.graph.Graph;

// Gephi
import org.gephi.graph.api.UndirectedGraph;

// JDK
import java.io.File;

/**
 * What can a Network IGraphProfilerTool give us?
 * 
 * Usually, we get a descriptive statistics about the graph.
 * 
 * @author kamir
 */
public interface IGraphProfilerTool {

    public int getNumberEdges();

    public int getNumberVertices();

    // needs to be calculated
    public double getDiameter();

    public int getNrOfNodesInLargestCluster();

    public int getNrOfEdgesInLargestCluster();

    // needs to be calculated
    public double getGlobalClusterCoefficient();

    // write a default representation to a file
    public void storeImage(File folderOut, int timeInSeconds);

    
    
    
    
    
 
    
}
