package org.etosha.tools.experimental.planargraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class to perform various operations that involve traversing a graph.
 * This exists to prevent having to pass multiple state variables to
 * recursive calls when performing the traversals.  Most operations
 * have a corresponding static method in the Graph class for easier
 * calling.
 *
 * @author Max Bogue
 */
public class GraphTraverser<T> {

    /** The graph that this object works on. */
    private Graph<T> graph;

    /** A set to track nodes already searched in the current traversal. */
    private Set<T> searched = new HashSet<T>();

    /** A map used for colorings of the graph. */
    private Map<T, Integer> coloring = null;

    /** A variable to hold the graph resulting from an operation. */
    private Graph<T> result = null;

    /** A target vertex. */
    private T goal = null;

    /** The next vertex in a traversal (used for walkCycle). */
    private T next = null;

    /** The previous vertex in a traversal (used for walkCycle). */
    private T prev = null;

    /**
     * Constructor for a GraphTraverser object.
     * 
     * @param graph
     */
    public GraphTraverser( Graph<T> graph ) {
        this.graph = graph;
    }

    /**
     * Tests whether this graph is bipartite.
     *
     * @return      True if it is bipartite.
     */
    public boolean isBipartite() {
        if ( graph.numVertices() == 0 ) return true;
        coloring = new HashMap<T, Integer>();
        return isBipartite( graph.getVertices().iterator().next(), true );
    }

    /**
     * Private worker function for isBipartite.
     *
     * @param v     The current node in the traversal.
     * @param color The color to try for this node.
     * @return      True if no conflicts were found.
     */
    private boolean isBipartite( T v, boolean color ) {
        if ( coloring.containsKey( v ) ) {
            if ( !coloring.get( v ).equals( color ? 1 : 0 ) ) {
                return false;
            } else {
                return true;
            }
        } else {
            coloring.put( v, color ? 1 : 0 );
            boolean bipartite = true;
            for ( T n : graph.getNeighbors( v ) ) {
                bipartite = bipartite && isBipartite( n, !color );
            }
            return bipartite;
        }
    }

    /**
     * Walks around a cycle, starting from an arbitrary vertex
     * and going in an arbitrary direction.
     *
     * @return      The next vertex in the walk.
     */
    public T walkCycle() {
        if ( next == null ) {
            prev = graph.getVertices().iterator().next();
            next = graph.getNeighbors( prev ).iterator().next();
        } else {
            for ( T n : graph.getNeighbors( next ) ) {
                if ( !n.equals( prev ) ) {
                    prev = next;
                    next = n;
                    break;
                }
            }
        }
        return prev;
    }

    /**
     * Finds a path between two vertices in the graph.
     *
     * @param start     The starting vertex.
     * @param end       The ending vertex.
     * @param banned    A collection of sets this path can't pass through.
     * @return          The path between the two vertices.
     */
    public Graph<T> findPath( T start, T end, Collection<T> banned ) {
        searched.clear();
        searched.addAll( banned );
        result = new Graph<T>();
        goal = end;
        boolean pathFound = findPath( start );
        return pathFound ? result : null;
    }

    /**
     * Private worker function for findPath.
     *
     * @param v     The current node in the path.
     * @return      True if the path was found.
     */
    private boolean findPath( T v ) {
        searched.add( v );
        for ( T n : graph.getNeighbors( v ) ) {
            if ( n.equals( goal ) ) {
                result.addEdge( v, n );
                return true;
            } else if ( !searched.contains( n ) ) {
                result.addEdge( v, n );
                boolean pathFound = findPath( n );
                if ( pathFound ) return true;
                result.removeEdge( v, n );
            }
        }
        return false;
    }

    /**
     * Finds an arbitrary cycle in a biconnected graph.
     *
     * @return      A cycle.
     */
    public Graph<T> findCycle() {
        searched.clear();
        result = new Graph<T>();
        goal = graph.getVertices().iterator().next();
        return findCycle( goal );
    }

    /**
     * Private worker function for findCycle.
     *
     * @param v     The current node in the cycle.
     * @return      A cycle.
     */
    private Graph<T> findCycle( T v ) {
        searched.add( v );
        for ( T n : graph.getNeighbors( v ) ) {
            if ( n.equals( goal ) && result.numVertices() > 2 ) {
                result.addEdge( v, n );
                return result;
            } else if ( !searched.contains( n ) ) {
                result.addEdge( v, n );
                Graph<T> completedCycle = findCycle( n );
                if ( completedCycle != null ) return completedCycle;
                result.removeEdge( v, n );
            }
        }
        return null;
    }

    /**
     * Splits the graph into pieces using the given cycle.
     *
     * @param cycle     The cycle to split the graph with.
     * @return          A set containing all the pieces of the graph.
     */
    public Set<Graph<T>> splitIntoPieces ( Graph<T> cycle ) {
        searched.clear();
        Set<Graph<T>> pieces = new HashSet<Graph<T>>();
        for ( T v : cycle.getVertices() ) {
            searched.add( v );
            for ( T n : graph.getNeighbors( v ) ) {
                if (   !searched.contains( n ) && !cycle.hasEdge( n, v ) ) {
                    result = new Graph<T>();
                    result.addEdge( v, n );
                    makePiece( cycle, n );
                    pieces.add( result );
                }
            }
        }
        return pieces;
    }

    /**
     * Private helper function for splitIntoPieces.  Creates a piece
     * (connected without going through the cycle) of the graph
     * from a cycle and a starting node.
     *
     * @param cycle     The separating cycle.
     * @param v         The current vertex.
     *
     * @return          This piece of the graph.
     */
    private void makePiece( Graph<T> cycle, T v ) {
        if ( cycle.hasVertex( v ) ) return;
        searched.add( v );
        for ( T n : graph.getNeighbors( v ) ) {
            if ( !result.hasEdge( n, v ) ) {
                result.addEdge( v, n );
                makePiece( cycle, n );
            }
        }
    }

} // GraphTraverser
