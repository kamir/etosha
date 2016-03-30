package org.etosha.tools.experimental.planargraph;

import static org.etosha.tools.experimental.planargraph.TestPlanarityForEdgeList.readGraphFromFile;
import static org.etosha.tools.experimental.planargraph.TestPlanarityForEdgeList.testPlanarity;
import com.carlschroedl.gephi.spanningtree.SpanningTree;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.toolkit.demos.*;
import org.gephi.io.generator.plugin.DynamicGraph;
import org.gephi.io.generator.plugin.RandomGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ContainerFactory;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.AppendProcessor;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import static org.etosha.tools.experimental.planargraph.TestPlanarityForEdgeList.testPlanarity;

public class GeneratePlanarGraph {

    /**
     * Step one:
     *
     * We load a graph completely. We calculate the spanning tree and store the
     * graph again. We build the MPFG from the links.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        GeneratePlanarGraph generatePlanarGraph = new GeneratePlanarGraph();
        generatePlanarGraph.script();

    }

    File output = new File("./output");

    public void script() throws IOException {

        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();

        Workspace workspace1 = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        AttributeModel aModel = workspace1.getLookup().lookup(AttributeModel.class);

        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);

        // HERE WE NEED TO LOAD THE SORTED LINK-LIST
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("./input"));

        int i = chooser.showDialog(null, "Select grpah");

        File f = chooser.getSelectedFile();

        try {
            cat(f);
        } catch (IOException ex) {
            Logger.getLogger(TestPlanarity.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadNodesAndEdges(f, graphModel);

        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

        //Count nodes and edges
        System.out.println("Nodes: " + undirectedGraph.getNodeCount() + " Edges: " + undirectedGraph.getEdgeCount());

        //Export only visible graph
        PDFExporter pdfExporter1 = (PDFExporter) ec.getExporter("pdf");     //Get GEXF exporter
        //exporter.setExportVisible(true);  //Only exports the visible (filtered) graph
        pdfExporter1.setWorkspace(workspace1);
        try {
            ec.exportFile(new File(output + "/" + f.getName() + "_all.pdf"), pdfExporter1);
        } catch (IOException ex) {
            ex.printStackTrace();

        }

        // SpanningTree analysis
        SpanningTree st = new SpanningTree();
        st.execute(graphModel, aModel);

        BufferedWriter bw = new BufferedWriter(new FileWriter(output + "/" + f.getName() + "_MST.csv"));

        //Iterate over edges
        for (Edge e : undirectedGraph.getEdges().toArray()) {

            int z = (int) e.getAttributes().getValue("Spanning Tree");

            if (z == 1) {
                System.out.println(e.getSource().getNodeData().getId() + "\t" + e.getTarget().getNodeData().getId() + "\t" + e.getWeight());
                bw.write(e.getSource().getNodeData().getId() + "\t" + e.getTarget().getNodeData().getId() + "\t" + e.getWeight());
                bw.write("\n");
            }

        }
        bw.close();

//        /**
//         * Currently there is no filtering of the MST, just a labeling in the
//         * gexf-file
//         */
        try {
            ec.exportFile(new File(output + "/" + f.getName() + ".gexf"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        //Export to Writer
//        Exporter exporterGraphML = ec.getExporter("graphml");     //Get GraphML exporter
//        exporterGraphML.setWorkspace(workspace);
//        StringWriter stringWriter = new StringWriter();
//        ec.exportWriter(stringWriter, (CharacterExporter) exporterGraphML);
//        System.out.println(stringWriter.toString());   //Uncomment this line
        System.out.println(st.getReport());

////////        // a new empty graph
////////        test.Graph<Integer> graph = new test.Graph<Integer>();
////////
////////        for (Edge e : undirectedGraph.getEdges().toArray()) {
////////
////////            // int z = (int) e.getAttributes().getValue("Spanning Tree");
////////            //if ( z == 1 ) {
////////            graph = addIfResultIsPlanar(graph,
////////                    e.getSource().getNodeData().getId(),
////////                    e.getTarget().getNodeData().getId());
////////
////////        }
////////
////////        // show PFMG
////////        Set<Integer> v = graph.getVertices();
////////        for (Integer s : v) {
////////            Set<Integer> n = graph.getNeighbors(s);
////////            for (Integer t : n) {
////////                System.out.println(s + "\t" + t);
////////            }
////////
////////        }

    }

    /**
     * Simply "cat" the content of the input file ...
     *
     *
     * @param f
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void cat(File f) throws FileNotFoundException, IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));
        while (br.ready()) {
            System.out.println(br.readLine());
        }
        System.out.println();

    }

    /**
     * A Gephi-GraphModel is used to represent a Graph. Than we can calculate
     *
     * The MinimumSpanningTree is calculated by a Gephi-Plugin.
     *
     *
     * @param f
     * @param graphModel
     * @throws IOException
     */
    private void loadNodesAndEdges(File f, GraphModel graphModel) throws IOException {

        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

        BufferedReader br = new BufferedReader(new FileReader(f));
        while (br.ready()) {
            String line = br.readLine();
            String[] fields = line.split(" ");
            String s = fields[0];
            String t = fields[1];

            //Create three nodes
            Node n0 = graphModel.getUndirectedGraph().getNode(s);
            if (n0 == null) {
                n0 = graphModel.factory().newNode(s);
                n0.getNodeData().setLabel("Node " + s);
            }

            //Create three nodes
            Node n1 = graphModel.getUndirectedGraph().getNode(t);
            if (n1 == null) {
                n1 = graphModel.factory().newNode(t);
                n1.getNodeData().setLabel("Node " + t);
            }

            //Create three edges
            Edge e1 = graphModel.factory().newEdge(n0, n1, 1f, false);

            undirectedGraph.addNode(n0);
            undirectedGraph.addNode(n1);
            undirectedGraph.addEdge(e1);

        }

        System.out.println();

    }

    private org.etosha.tools.experimental.planargraph.Graph<Integer> addIfResultIsPlanar(org.etosha.tools.experimental.planargraph.Graph<Integer> graph, String _s, String _t) {
        System.out.print(_s + " => " + _t + " (");
        try {

            org.etosha.tools.experimental.planargraph.Graph<Integer> g = (org.etosha.tools.experimental.planargraph.Graph<Integer>) graph.clone();

            int s = Integer.parseInt(_s);
            int t = Integer.parseInt(_t);

            boolean ip = true;

            g.addEdge(s, t);

            int z = g.numEdges();
            int y = 3 * g.numVertices() - 6;

            if (z > 10) {
                ip = isPlanar(g);
            }

            System.out.println(ip + ")");
            if (ip) {
                return g;
            } else {
                return graph;
            }
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private boolean isPlanar(org.etosha.tools.experimental.planargraph.Graph<Integer> g) {

        org.etosha.tools.experimental.planargraph.Graph<Integer> cycle = (new GraphTraverser<Integer>(g)).findCycle();
        if (cycle == null) {
            return true;
        }

        System.out.print(testPlanarity(g, cycle) ? " planar " : " nonplanar ");

        return testPlanarity(g, cycle) ? true : false;
    }
}
