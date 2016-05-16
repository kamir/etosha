package org.etosha.tools.profiler.common;

import com.carlschroedl.gephi.spanningtree.SpanningTree;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.io.BufferedWriter;
import org.etosha.networks.fluctuationnet.DFALink;
import org.etosha.networks.Link;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.etosha.tools.profiler.Profiler;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.HierarchicalGraph;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.ClusteringCoefficient;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * An SNA profile calculates diameter, global clustercoeficient and other graph
 * properties, typically used in network analysis.
 *
 *
 * @author kamir
 */
public class SNAProfiler implements Profiler {

    public static Profiler profileGephiFileLocaly(double TS, File fIn, String fnOut, String label) {

        SNAProfiler p = new SNAProfiler(TS, fnOut);
        p.label = label;

        p.initFromFile(fIn);

        return p;

    }

    boolean isDirected = true;
    int LAYER = 0;  // Allows selection of a particular Layer, e.g. the fit 
    // range for one specific alpha in a DFA Function.

    ProjectController pc = null;
    GraphController graphController = null;
    Workspace workspace = null;
    GraphModel graphModel = null;

    AttributeController ac = null;
    AttributeModel aModel = null;

    Container container = null;

    DirectedGraph directedGraph = null;

    ExportController ec = null;
    ImportController importController = null;

    String label = null;
    String fnOut = null;
    String TS = null;

    public SNAProfiler(double TS, String fnOut) {

        //Init a project - and therefore a workspace
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();

        this.label = "SNA.profile.TS_" + TS;
        this.fnOut = fnOut;
        this.TS = "" + TS;

        workspace = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        aModel = workspace.getLookup().lookup(AttributeModel.class);

        ec = Lookup.getDefault().lookup(ExportController.class);

        directedGraph = graphModel.getDirectedGraph();

        System.out.println("P  : " + pc.getCurrentProject());
        System.out.println("WS : " + workspace);

    }

    /**
     * RUNS THE PROFILER LOCALLY ON A WORKSTATION ...
     *
     * @return AN INSTANCE OF PROFILER ...
     */
    public static Profiler profileLocaly(double TS, String fnOut, String label, List<Link> linksSMALL) {

        SNAProfiler p = new SNAProfiler(TS, fnOut);

        p.label = label;

        for (Link link : linksSMALL) {

            double fw = link.getWeight(p.LAYER);
            if (!(fw < TS)) {

                String s = link.getSource();
                String t = link.getTarget();

                Node n0 = p.directedGraph.getNode(s);

                if (n0 == null) {
                    n0 = p.graphModel.factory().newNode(s);
                    n0.getNodeData().setLabel(s);
                    p.directedGraph.addNode(n0);

                }

                //Create three nodes
                Node n1 = p.directedGraph.getNode(t);
                if (n1 == null) {
                    n1 = p.graphModel.factory().newNode(t);
                    n1.getNodeData().setLabel(t);
                    p.directedGraph.addNode(n1);

                }

                //Create the edge
                Edge e1 = p.graphModel.factory().newEdge(n0, n1, (float) fw, p.isDirected);

                p.directedGraph.addEdge(e1);

            }

        };

        return p;

    }

    public int getNumberEdges() {
        return directedGraph.getEdgeCount();
    }

    public int getNumberVertices() {
        return directedGraph.getNodeCount();
    }

    @Override
    public double getDiameter() {
        GraphDistance dist = new GraphDistance();
        dist.setDirected(isDirected);
        dist.execute(graphModel, aModel);
        // System.out.println( dist.getReport() );
        return dist.getDiameter();
    }

    @Override
    public int getMaxCLusterNrNodes() {
        System.out.println("Cluster are not yet calculated in this profiler. (" + this.toString() + ")");
        return -1;
    }

    @Override
    public int getMaxCLusterNrEdges() {
        System.out.println("Cluster are not yet calculated in this profiler. (" + this.toString() + ")");
        return -1;
    }

    @Override
    public double getGlobalClusterCoefficient() {

        HierarchicalGraph graph = null;

        if (isDirected) {
            graph = graphModel.getHierarchicalDirectedGraph();
        } else {
            graph = graphModel.getHierarchicalUndirectedGraph();
        }

        ClusteringCoefficient clusteringCoefficientStat = new ClusteringCoefficient();
        clusteringCoefficientStat.setDirected(isDirected);
        clusteringCoefficientStat.triangles(graph);

        double avg = clusteringCoefficientStat.getAverageClusteringCoefficient();
        return avg;
    }

    @Override
    public void storeImage(File folderOut, int timeInSeconds) {

        System.out.println(">>> SNAProfiler ### Write profile to folder:" + folderOut.getAbsolutePath());

        BufferedWriter bw = null;
        try {
            folderOut.mkdirs();

            bw = new BufferedWriter(new FileWriter(folderOut.getAbsolutePath() + "/" + label + "_MST.csv"));

            //Rank color by Degree
            RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
            Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
            AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
            /**
             * The following example lets colors change from blue over white
             * over green to red for the intervals from 0.0..0.33..0.66..1.0
             */
            float[] positions = {0f, 0.33f, 0.66f, 1f};
            colorTransformer.setColorPositions(positions);
            Color[] colors = new Color[]{new Color(0x0000FF), new Color(0xFFFFFF), new Color(0x00FF00), new Color(0xFF0000)};
            colorTransformer.setColors(colors);
            rankingController.transform(degreeRanking, colorTransformer);

            /**
             *
             * Partitioning
             *
             */
            dumpModelSchema(aModel);

            //Get Centrality
            GraphDistance distance = new GraphDistance();
            distance.setDirected(true);
            distance.execute(graphModel, aModel);
            System.out.println("### SNAProfiler ### Run Distances analysis. " + graphModel + " " + aModel);

            dumpModelSchema(aModel);

            Degree degree = new Degree();
            degree.execute(graphModel, aModel);
            System.out.println("### SNAProfiler ### Run Degree analysis. " + graphModel + " " + aModel);

            dumpModelSchema(aModel);

            //Run modularity algorithm - community detection
            Modularity modularity = new Modularity();
            modularity.execute(graphModel, aModel);
            System.out.println("### SNAProfiler ### Run Modularity analysis. " + graphModel + " " + aModel);

            dumpModelSchema(aModel);

            //Partition with 'modularity_class', just created by Modularity algorithm
            AttributeColumn modColumn = aModel.getNodeTable().getColumn(Modularity.MODULARITY_CLASS);
            PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
            Partition p2 = partitionController.buildPartition(modColumn, graphModel.getDirectedGraph());
            System.out.println("### SNAProfiler ### Run PARTITIONER. " + graphModel + " " + aModel);

            System.out.println(">>> " + p2.getPartsCount() + " partitions found");
            NodeColorTransformer nodeColorTransformer2 = new NodeColorTransformer();
            nodeColorTransformer2.randomizeColors(p2);
            partitionController.transform(p2, nodeColorTransformer2);

            /**
             * RANKING
             *
             */
            //Rank size by centrality
            Ranking degreeRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
            AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
            sizeTransformer.setMinSize(1f);
            sizeTransformer.setMaxSize(5f);
            rankingController.transform(degreeRanking2, sizeTransformer);

            //Rank label size - set a multiplier size
            AttributeColumn centralityColumn = aModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
            Ranking centralityRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
            AbstractSizeTransformer labelSizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.LABEL_SIZE);
            labelSizeTransformer.setMinSize(22f);
            labelSizeTransformer.setMaxSize(48f);
            rankingController.transform(centralityRanking2, labelSizeTransformer);
            //Layout for 1 minute

            int t = 5;

            if (timeInSeconds > 0) {
                t = timeInSeconds;
            }

            System.out.println(">>> AutoLayout uses: " + t + " seconds for rendering.");

            AutoLayout autoLayout = new AutoLayout(t, TimeUnit.SECONDS);
            autoLayout.setGraphModel(graphModel);
            YifanHuLayout firstLayout = new YifanHuLayout(null, new StepDisplacement(1f));
            ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
            LabelAdjust thirdLayout = new LabelAdjust(null);
            AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);//True after 10% of layout time
            AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", new Double(500.), 0f);//500 for the complete period
            autoLayout.addLayout(firstLayout, 0.1f);
            autoLayout.addLayout(secondLayout, 0.25f, new AutoLayout.DynamicProperty[]{adjustBySizeProperty, repulsionProperty});
            autoLayout.addLayout(thirdLayout, 0.65f);
            autoLayout.execute();
            PreviewModel previewModel = Lookup.getDefault().lookup(PreviewController.class).getModel();
            previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
            previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);

            // SpanningTree analysis
            /**
             *
             * Problem: Our current edges are usefull if the highest values are
             * used, but SpanningTree works on smallest values.
             *
             */
            SpanningTree st = new SpanningTree();
            st.execute(graphModel, aModel);

            //Iterate over edges
            for (Edge e : directedGraph.getEdges().toArray()) {

                int z = (int) e.getAttributes().getValue("Spanning Tree");

                if (z == 1) {
                    System.out.println(e.getSource().getNodeData().getId() + "\t" + e.getTarget().getNodeData().getId() + "\t" + e.getWeight());
                    bw.write(e.getSource().getNodeData().getId() + "\t" + e.getTarget().getNodeData().getId() + "\t" + e.getWeight());
                    bw.write("\n");
                }

            }
            bw.close();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        dumpModelSchema(aModel);

        //Export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(folderOut.getAbsolutePath() + "/" + label + ".pdf"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ExportController ec2 = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec2.exportFile(new File(folderOut.getAbsolutePath() + "/" + label + ".gexf"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        //Export
//        ExportController ec3 = Lookup.getDefault().lookup(ExportController.class);
//        PNGExporter exporter = (PNGExporter) ec3.getExporter("png");
//        exporter.setHeight(900);
//        exporter.setHeight(1400);
//        exporter.setMargin(50);
//        
//        try {
//            ec3.exportFile(new File(folderOut.getAbsolutePath() + "/" + label + ".png"),exporter);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }

    private void initFromFile(File fIn) {

        /**
         * Load the Graph here ...
         */
        System.out.println("### SNAProfiler ### " + fIn.getAbsolutePath() + " will be processed now locally. TS=" + TS + " but filtering is ignored.");
        System.out.println("### SNAProfiler ### " + fnOut + " is the target folder. label=" + label);

        pc = Lookup.getDefault().lookup(ProjectController.class);
         
        workspace = pc.getCurrentWorkspace();
        importController = Lookup.getDefault().lookup(ImportController.class);
        try {
            container = importController.importFile(fIn);
//          container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);
//          container.setAllowAutoNode(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        importController.process(container, new DefaultProcessor(), workspace);

            //Get a graph model - it exists because we have a workspace
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        aModel = workspace.getLookup().lookup(AttributeModel.class);

        ec = Lookup.getDefault().lookup(ExportController.class);

        directedGraph = graphModel.getDirectedGraph();

    }

    private void dumpModelSchema(AttributeModel aModel) {

        int i = 0;
        System.out.println("##NODE-List##");
        for (AttributeColumn col : aModel.getNodeTable().getColumns()) {
            System.out.println(" col_" + i + " : " + col);
            i++;
        }

        System.out.println("##EDGE-List##");
        for (AttributeColumn col : aModel.getEdgeTable().getColumns()) {
            System.out.println(" col_" + i + " : " + col);
            i++;
        }
    }

    public UndirectedGraph getGraph() {
        
        return graphModel.getUndirectedGraph();
    
    }

}
