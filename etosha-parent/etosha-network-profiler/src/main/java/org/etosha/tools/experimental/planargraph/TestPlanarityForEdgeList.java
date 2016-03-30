package org.etosha.tools.experimental.planargraph;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * Class for testing the planarity of a biconnected graph.
 *
 * @author Max Bogue
 */
public class TestPlanarityForEdgeList {

    /**
     * Set containing the symbol strings of length four that
     * indicate a not interlaced graph.
     */
    private static final Set<String> notInterlacedSet = makeNIS();

    /** Private method to create the above set. */
    private static Set<String> makeNIS() {
        Set<String> set = new HashSet<String>();
        String[] strings = { "xbyb", "bybx", "ybxb", "bxby" };
        for ( String s : strings ) set.add(s);
        return set;
    }

    /**
     * Main method.
     *
     * @param args[0]   The file name to read the graph from.
     */
    public static void main ( String[] args ) {

        // SELECT AN EDGE LIST
        // JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new File( "./input" ) );
        // Dialog zum Speichern von Dateien anzeigen
        int i = chooser.showDialog(null, "Select edgelist");

        File f = chooser.getSelectedFile();   
        
        try {
            cat( f );
        } 
        catch (IOException ex) {
            Logger.getLogger(TestPlanarity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        Graph<Integer> graph = readGraphFromFile( f.getAbsolutePath() );
        
        Graph<Integer> cycle = (new GraphTraverser<Integer>( graph )).findCycle();
        System.out.println( testPlanarity( graph, cycle ) ? "planar" : "nonplanar" );
        
    }

    
    /**
     * Constructs a graph from the edges listed in a given file.
     *
     * @param fileName  The file path containing the graph data.
     * @return          The newly constructed graph.
     */
    public static Graph<Integer> readGraphFromFile( String fileName ) {
        Graph<Integer> graph = new Graph<Integer>();
        Scanner in = null;
        try {
            in = new Scanner( new BufferedInputStream( new FileInputStream( fileName )));
            while ( in.hasNextInt() ) {
                int a = in.nextInt();
                int b = in.nextInt();
                graph.addEdge( a, b );
            }
        } catch ( Exception e ) {
            System.err.println( "Error reading from input file \'" + fileName + "\'." );
            System.err.println( "Usage: java TestPlanarity <input_file>" );
            System.exit(1);
        }
        return graph;
    }
    
    /**
     * Tests the planarity of a BICONNECTED graph.  Has to suppress warnings
     * because Java is silly and can't handle arrays of generics properly.
     *
     * @param graph     The graph to test for planarity.
     * @param cycle     A cycle within the above graph, preferably separating.
     * @return          Whether the graph is planar or not.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean testPlanarity( Graph<T> graph, Graph<T> cycle ) {
        if ( graph.numEdges() > 3 * graph.numVertices() - 6 ) {
            return false;
        }
        try {
        Set<Graph<T>> pieces = Graph.splitIntoPieces( graph, cycle );
        for ( Graph<T> piece : pieces ) {
            if ( !Graph.isPath( piece ) ) {     // Don't bother if the piece is a path.

                // Need a starting vertex that is an attachment point between the piece and the cycle.
                T start = null;
                for ( T v : cycle.getVertices() ) {
                    if ( piece.hasVertex( v ) ) {
                        start = v;
                        break;
                    }
                }

                // Construct the part of the new cycle that is coming from the old cycle.
                Graph<T> cycleSegment = new Graph<T>( cycle );
                T prev = start;

                // Choose an arbitrary direction to traverse the cycle in.
                T curr = cycle.getNeighbors( prev ).iterator().next();

                // Remove all the edges between the starting attachment point and the
                // next found attachment point from the cycleSegment graph.
                cycleSegment.removeEdge( prev, curr );
                while ( !piece.hasVertex( curr ) ) {
                    for ( T v : cycle.getNeighbors( curr ) ) {
                        if ( !v.equals( prev ) ) {
                            prev = curr;
                            curr = v;
                            break;
                        }
                    }
                    cycleSegment.removeEdge( prev, curr );
                }
                T end = curr;       // end is the next attachment point found.

                // Find a path through the piece connecting the attachment points, but
                // make sure that it doesn't go through a different attachment point.
                GraphTraverser<T> traverser = new GraphTraverser<T>( piece );
                Graph<T> piecePath = traverser.findPath( start, end, cycle.getVertices() );

                // Construct the new graph and the new cycle accordingly.
                Graph<T> pp = Graph.addGraphs( cycle, piece );
                Graph<T> cp = Graph.addGraphs( cycleSegment, piecePath );

                // Recurse using them as parameters.
                boolean planar = testPlanarity( pp, cp );
                if ( !planar ) return false;
            }
        }

        // If all the piece/cycle combinations are planar, then test the interlacement.
        Graph<Integer> interlacement = new Graph<Integer>();
        Object[] pieceArray = pieces.toArray();

        // For each pair of pieces, see if they're interlaced.
        for ( int i = 0; i < pieceArray.length; i++ ) {
            Graph<T> x = (Graph<T>) pieceArray[i];
            for ( int j = i + 1; j < pieceArray.length; j++ ) {
                Graph<T> y = (Graph<T>) pieceArray[j];

                char lastChar = ' ';    // Store the last character added to make things easier.
                String symList = "";    // The list of symbols representing the interlacement of the pieces.
                int bCount = 0;         // The number of 'b' symbols.  Again, to make things easy.

                // Walk around the cycle and construct the symbol list.
                GraphTraverser<T> traverser = new GraphTraverser<T>( cycle );
                for ( int k = 0; k < cycle.numVertices(); k++ ) {
                    T v = traverser.walkCycle();
                    // If a node is in both pieces, then add a 'b'.
                    if ( x.hasVertex( v ) && y.hasVertex( v ) ) {
                        bCount++;
                        symList += 'b';
                        lastChar = 'b';
                    // Else add if it's only in one piece and it's not the last symbol added.
                    } else if ( x.hasVertex( v ) && lastChar != 'x' ) {
                        symList += 'x';
                        lastChar = 'x';
                    } else if ( y.hasVertex( v ) && lastChar != 'y' ) {
                        symList += 'y';
                        lastChar = 'y';
                    }
                }
                // Check for wrap-around adjacency of x's or y's.
                if ( (lastChar == 'x' || lastChar == 'y') && symList.charAt(0) == lastChar ) {
                    symList = symList.substring(1);
                }
                boolean interlaced = false;
                if ( symList.length() > 4 || bCount > 2 ) {
                    interlaced = true;
                } else if ( symList.length() == 4 && !notInterlacedSet.contains( symList ) ) {
                    interlaced = true;
                }
                if ( interlaced ) {
                    interlacement.addEdge( i, j );
                }
            }
        }
        return Graph.isBipartite( interlacement );
        
        }
        catch( Exception ex) {
            return false;
        }
    }

    /**
     * Tiny helper function that correctly performs modulo.
     *
     * @param x     The starting number.
     * @param y     The mod.
     * @return      x mod y
     */
    private static int mod( int x, int y ) {
        int mod = x % y;
        return mod < 0 ? mod + y : mod;
    }

    private static void cat(File f) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader( new FileReader( f ) );
        while( br.ready() ) {
            System.out.println( br.readLine() );
        }
        System.out.println(  );
    }

} // TestPlanarity
