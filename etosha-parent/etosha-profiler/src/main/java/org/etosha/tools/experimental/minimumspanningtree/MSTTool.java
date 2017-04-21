package org.etosha.tools.experimental.minimumspanningtree;

/**
 *
 * This project is used to calculate the MST for networks. It is based on a
 * Gephi-Plugin.
 *
 * Binary content was extracted form the pack200 format (nbm file) and is now
 * "a.jar".
 *
 */ 
import com.carlschroedl.gephi.spanningtree.SpanningTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.*;
import org.gephi.filters.plugin.attribute.AttributeRangeBuilder;
import org.gephi.filters.plugin.dynamic.DynamicRangeBuilder;
import org.gephi.filters.plugin.dynamic.DynamicRangeBuilder.DynamicRangeFilter;
import org.gephi.filters.spi.EdgeFilter;
import org.gephi.filters.spi.FilterBuilder;

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
import org.gephi.io.exporter.preview.PNGExporter;
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
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup; 

public class MSTTool {

    File output = new File("./output");

    StringBuffer sb = null;
    
    MSTTool(StringBuffer _sb) {
        sb = _sb;
    }

    public void process(String fn, int SCALE, String label) throws IOException {

        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();

        Workspace workspace1 = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        AttributeModel aModel = workspace1.getLookup().lookup(AttributeModel.class);

        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);

        File f = new File(fn);

        System.out.println(f.getAbsolutePath() + " " + f.exists());

//        try {
//            cat(f);
//        } catch (IOException ex) {
//            Logger.getLogger(TestPlanarity.class.getName()).log(Level.SEVERE, null, ex);
//        }
        loadNodesAndEdges(f, graphModel, SCALE);

        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

        //Count nodes and edges
        System.out.println("Nodes: " + undirectedGraph.getNodeCount() + " Edges: " + undirectedGraph.getEdgeCount());

//        //Export only visible graph
//        PDFExporter pdfExporter1 = (PDFExporter) ec.getExporter("pdf");     //Get GEXF exporter
//        //exporter.setExportVisible(true);  //Only exports the visible (filtered) graph
//        pdfExporter1.setWorkspace(workspace1);
//        try {
//            ec.exportFile(new File(output + "/" + f.getName() + "_all.pdf"), pdfExporter1);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//
//        }
        

        //Layout for 1 minute
        AutoLayout autoLayout = new AutoLayout(1500, TimeUnit.MILLISECONDS);
        autoLayout.setGraphModel(graphModel);
        
        YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
        ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
        AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);//True after 10% of layout time
        AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);//500 for the complete period
        autoLayout.addLayout(firstLayout, 0.15f);
        autoLayout.addLayout(secondLayout, 0.85f, new AutoLayout.DynamicProperty[]{adjustBySizeProperty, repulsionProperty});
        autoLayout.execute();





        // SpanningTree analysis
        SpanningTree st = new SpanningTree();
        st.execute(graphModel, aModel);
        
        
        
               //Create a dynamic range filter query
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
//        FilterBuilder[] builders = Lookup.getDefault().lookup(DynamicRangeBuilder.class).getBuilders();
//        DynamicRangeFilter dynamicRangeFilter = (DynamicRangeFilter) builders[0].getFilter();     //There is only one TIME_INTERVAL column, so it's always the [0] builder
//        Query dynamicQuery = filterController.createQuery(dynamicRangeFilter);

        //Create a attribute range filter query - on the price column
        AttributeColumn treeCol = aModel.getEdgeTable().getColumn("spanningtree");
        AttributeRangeBuilder.AttributeRangeFilter attributeRangeFilter = new AttributeRangeBuilder.AttributeRangeFilter(treeCol);
        Query treeQuery = filterController.createQuery(attributeRangeFilter);

        //Set dynamic query as child of price query
        filterController.add(treeQuery);

        //Set the filters parameters - Keep nodes between 2007-2008 which have average price >= 7
        attributeRangeFilter.setRange(new Range( 1, 1 ));

        //Execute the filter query
        GraphView view = filterController.filter(treeQuery);

        Graph filteredGraph = graphModel.getGraph(view);
        
        graphModel.setVisibleView(view);
        
        
        //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLUE));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 15f);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);

        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.9f));
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,new Font("Arial",Font.ITALIC,120));

        
        previewController.refreshPreview();
        
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(output + "/" + label + "_SCALE_" + SCALE + "_MST.csv"));

        //Iterate over edges
        for (Edge e : undirectedGraph.getEdges().toArray()) {

            int z = (int) e.getAttributes().getValue("Spanning Tree");

            if (z == 1) {
                // System.out.println(e.getSource().getNodeData().getId() + "\t" + e.getTarget().getNodeData().getId() + "\t" + e.getWeight());
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
            ec.exportFile(new File(output + "/" + label + "_SCALE_" + SCALE + "_MST" + ".gexf"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

   
        String line = label + "\t" + SCALE + "\t" + PARSE_REPORT( st.getReport() ) + "\t";
        sb.append(line + "\n");

        //Export full graph, save it to test.png and show it in an image frame
        //Export only visible graph
        PNGExporter exporter = (PNGExporter) ec.getExporter("png");     //Get GEXF exporter

        exporter.setWorkspace(workspace1);
        try {
            ec.exportFile(new File(output + "/" + label + "_SCALE_" + SCALE + "_MST" + ".png"), exporter);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
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
    private void loadNodesAndEdges(File f, GraphModel graphModel, int SCALE) throws IOException {

        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

        BufferedReader br = new BufferedReader(new FileReader(f));
        while (br.ready()) {
            String line = br.readLine();
            String[] fields = line.split("\t");
            String s = fields[0];
            String t = fields[1];
            String w = fields[2 + SCALE];

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

            float fw = Float.parseFloat(w);

            //Create three edges
            Edge e1 = graphModel.factory().newEdge(n0, n1, fw, false);

            undirectedGraph.addNode(n0);
            undirectedGraph.addNode(n1);
            undirectedGraph.addEdge(e1);

        }

        System.out.println();

    }

    private String PARSE_REPORT(String report) {
        
        String[] p = report.split("<br/>");
        String p2 = p[1].split(": ")[1];
    
        String p1 = p[0].split(": ")[1];
    
        return p1 + "\t" + p2;
    
    }

}
