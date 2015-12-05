
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;

/**
 * @author M. Kaempf
 */
public class SimpleTraceGraphView {
    
    static ScraperTool st = null;

    Graph<Integer, String> g;
    Hashtable<URL, Integer> nodes = new Hashtable<URL, Integer>();

    /**
     * Creates a new instance of SimpleTraceGraphView
     */
    public SimpleTraceGraphView(File f, Vector<URL> liste) {

        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        g = new SparseMultigraph<Integer, String>();

        int i = 0;
        int vCounter = 0;

        Integer lastNode = null;

        for (URL u : liste) {

            Integer v = nodes.get(u);
            if (v == null) {
                nodes.put(u, vCounter);
                vCounter++;
                v = vCounter;
                g.addVertex(v);
            }

            if (i > 0) {
                g.addEdge("edge-" + i, lastNode, v);
            }

            lastNode = v;

            i++;
        }
    }

    public static void main(String[] args) {

        File f = new File("./data/STG.graphml");

        SimpleTraceGraphView sgv = new SimpleTraceGraphView(f, null); //We create our graph in here
        sgv.open();

    }

    public void open() {

        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300, 300)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(350, 350)); //Sets the viewing area size

        JFrame frame = new JFrame("Simple Scraper-Trace Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

}
