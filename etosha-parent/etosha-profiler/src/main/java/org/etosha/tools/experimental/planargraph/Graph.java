package org.etosha.tools.experimental.planargraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generic class to represent a graph.  Also contains several static
 * functions for some useful graph operations, many implemented using
 * the GraphTraversal class.
 *
 * @author Max Bogue
 */
public class Graph<T> implements Cloneable {

    /** Maps vertices to a set containing all adjacent vertices. */
    HashMap<T, Set<T>> adjacencyMap = new HashMap<T, Set<T>>();

    /** Default constructor for a graph. */
    public Graph() {}

    /**
     * Constructs a graph identical to the given graph.
     *
     * @param source    The graph to copy.
     */
    public Graph( Graph<T> source ) {
        //this.adjacencyMap = (HashMap<T, Set<T>>) source.adjacencyMap.clone();
        for ( T v : source.getVertices() ) {
            for ( T u : source.getNeighbors( v ) ) {
                this.addEdge( v, u );
            }
        }
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param v     The vertex to add.
     */
    public void addVertex( T v ) {
        if ( !adjacencyMap.containsKey( v ) ) {
            adjacencyMap.put( v, new HashSet<T>() );
        }
    }

    /**
     * Adds an undirected edge between two vertices.
     *
     * @param v1    One endpoint of the edge to add.
     * @param v2    The other endpoint of the edge.
     */
    public void addEdge( T v1, T v2 ) {
        addVertex(v1);
        addVertex(v2);
        adjacencyMap.get(v1).add(v2);
        adjacencyMap.get(v2).add(v1);
    }

    /**
     * Removes an undirected edge from between two vertices.
     *
     * @param v1    One endpoint of the edge to remove.
     * @param v2    The other endpoint of the edge.
     */
    public void removeEdge( T v1, T v2 ) {
        if ( hasEdge(v1, v2) && hasEdge(v2, v1) ) {
            adjacencyMap.get(v1).remove(v2);
            adjacencyMap.get(v2).remove(v1);
            if ( adjacencyMap.get(v1).size() == 0 ) adjacencyMap.remove(v1);
            if ( adjacencyMap.get(v2).size() == 0 ) adjacencyMap.remove(v2);
        }
    }

    /**
     * @return The number of vertices in the graph.
     */
    public int numVertices() {
        return adjacencyMap.size();
    }

    /**
     * @return The number of edges in the graph.
     */
    public int numEdges() {
        int count = 0;
        for ( Set<T> edges : adjacencyMap.values() ) {
            count += edges.size();
        }
        return count / 2;
    }

    /**
     * Gets all the neighbors of a given node.
     *
     * @param v     The vertex whose neighbors to get.
     * @return      A set containing all of the vertex's neighbors.
     */
    public Set<T> getNeighbors( T v ) {
        return adjacencyMap.get( v );
    }

    /**
     * @param v     A vertex.
     * @return      The degree (number of neighbors) of the given vertex, or
     *              -1 if no such vertex exists in this graph.
     */
    public int getDegree( T v ) {
        if ( adjacencyMap.containsKey( v ) ) {
            return adjacencyMap.get( v ).size();
        } else {
            return -1;
        }
    }

    /**
     * @return      A set containing all the vertices of this graph.
     */
    public Set<T> getVertices() {
        return adjacencyMap.keySet();
    }

    /**
     * @param v     A vertex.
     * @return      Whether the given vertex is in this graph.
     */
    public boolean hasVertex( T v ) {
        return adjacencyMap.containsKey( v );
    }

    /**
     * @param v     A vertex.
     * @return      Whether the given vertex is in this graph.
     */
    public boolean hasEdge( T v1, T v2 ) {
        return adjacencyMap.containsKey( v1 ) && adjacencyMap.get( v1 ).contains( v2 );
    }

    /** Prints out this graph to System.out */
    public void print() {
        System.out.println(  );
        for ( T k : adjacencyMap.keySet() ) {
            System.out.print( k + ":" );
            for ( T v : adjacencyMap.get(k) ) {
                System.out.print( " " + v );
            }
            System.out.println();
        }
    }

// Static Methods

    /**
     * Tests whether a connected graph is a cycle.
     *
     * @param graph     The graph to test.
     * @return          Whether it is a cycle or not.
     */
    public static <T> boolean isCycle( Graph<T> graph ) {
        boolean isCycle = graph.numVertices() > 2;
        for ( T v : graph.getVertices() ) {
            isCycle = isCycle && graph.getDegree( v ) == 2;
        }
        return isCycle;
    }

    /**
     * Tests whether a connected graph is a path.
     *
     * @param graph     The graph to test.
     * @return          Whether it is a path or not.
     */
    public static <T> boolean isPath( Graph<T> graph ) {
        T start = null;
        int endPoints = 0;
        for ( T v : graph.getVertices() ) {
            int degree = graph.getDegree( v );
            if ( degree == 1 ) {
                endPoints++;
            } else if ( degree != 2 ) {
                return false;
            }
        }
        if ( endPoints != 2 ) return false;
        return true;        
    }

    /**
     * Tests whether a connected graph is a bipartite.
     *
     * @param graph     The graph to test.
     * @return          Whether it is bipartite or not.
     */
    public static <T> boolean isBipartite( Graph<T> graph ) {
        return (new GraphTraverser<T>( graph )).isBipartite();
    }

    /**
     * Splits the graph into pieces using the cycle.
     *
     * @param graph     The graph.
     * @param cycle     The separating cycle.
     * @return          A set containing the resulting pieces.
     */
    public static <T> Set<Graph<T>> splitIntoPieces( Graph<T> graph, Graph<T> cycle ) {
        return (new GraphTraverser<T>( graph )).splitIntoPieces( cycle );
    }

    /**
     * Adds two graphs together to produce a new graph with every vertex
     * and edge contained in the original two.
     *
     * @param g1    The first graph.
     * @param g2    The second graph.
     * @return      g1 + g2, a new graph with all their vertices and edges.
     */
    public static <T> Graph<T> addGraphs( Graph<T> g1, Graph<T> g2 ) {
        Graph<T> newGraph = new Graph<T>();
        for ( T v : g1.getVertices() ) {
            for ( T u : g1.getNeighbors( v ) ) {
                newGraph.addEdge( v, u );
            }
        }
        for ( T v : g2.getVertices() ) {
            for ( T u : g2.getNeighbors( v ) ) {
                newGraph.addEdge( v, u );
            }
        }
        return newGraph;
    }

    /**
     * Subtract one graph from another to produce a new graph with every edge in the
     * first graph but not the second, and every remaining vertex with degree > 0.
     *
     * @param g1    The first graph.
     * @param g2    The second graph.
     * @return      g1 - g2.
     */
    public static <T> Graph<T> subtractGraphs( Graph<T> g1, Graph<T> g2 ) {
        Graph<T> newGraph = new Graph<T>( g1 );
        for ( T v : g2.getVertices() ) {
            for ( T u : g2.getNeighbors( v ) ) {
                newGraph.removeEdge( v, u );
            }
        }
        return newGraph;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Graph g = new Graph();
        g.adjacencyMap = (HashMap) this.adjacencyMap.clone();
        return g;
    }

    
    
} // Graph
