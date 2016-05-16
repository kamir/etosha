/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */
package org.etosha.tools.profiler.minimumspanningtree;

import org.etosha.networks.fluctuationnet.DFALink;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import org.etosha.networks.Link;
import org.gephi.graph.api.UndirectedGraph;
import scala.Serializable;

/**
 * Demonstrates a single graph with 3 layouts in 3 views. The first view is an
 * undirected graph using KKLayout The second view show a TreeLayout view of a
 * MinimumSpanningTree of the first graph. The third view shows the complete
 * graph of the first view, using the layout positions of the
 * MinimumSpanningTree tree view.
 *
 * @author Tom Nelson
 *
 */
@SuppressWarnings("serial")
public class MSTFrame extends JApplet implements Serializable {

    /**
     * the graph
     */
    public Graph<String, Number> graph;
    Forest<String, Number> tree;

    /**
     * the visual components and renderers for the graph
     */
    VisualizationViewer<String, Number> vv0;
    VisualizationViewer<String, Number> vv1;
    VisualizationViewer<String, Number> vv2;
//    VisualizationViewer<String,Number> vv3;

    /**
     * the normal transformer
     */
    MutableTransformer layoutTransformer;

    Dimension preferredSize = new Dimension(300, 300);
    Dimension preferredLayoutSize = new Dimension(400, 400);
    Dimension preferredSizeRect = new Dimension(500, 250);

    /**
     * create an instance of a simple graph in two views with controls to demo
     * the zoom features.
     *
     */
    public MSTFrame() {

    }

    public void initDemoData() {
        // create a simple graph for the demo
        // both models will share one graph
        if (graph == null) {
            graph = TestGraphs.getDemoGraph();
        }

    }

    int edgeCount = 0;
    double TS = 0.5;

    public void addLink(Link l, int LAYER) {

        String s = l.getSource();
        String t = l.getTarget();

        try {

            if (l.getWeight(LAYER) < TS) {
                return;
            }

            // graph.addEdge( l.w[SCALE], s, t);
            if (!graph.containsVertex(s)) {
                graph.addVertex(s);
            }
            if (!graph.containsVertex(t)) {
                graph.addVertex(t);
            }

            graph.addEdge(edgeCount, s, t);

            // System.out.println(edgeCount + " (" + l.getWeight(LAYER) +") - " + s + " - " + t );
            edgeCount++;

        } catch (Exception ex) {
            System.out.println("###ERROR### " + l.w[LAYER] + " - " + l.s + " - " + l.t);
            ex.printStackTrace();

        }
    }

    public void process() {

        MinimumSpanningForest2<String, Number> prim
                = new MinimumSpanningForest2<String, Number>(graph,
                        new DelegateForest<String, Number>(), DelegateTree.<String, Number>getFactory(),
                        new ConstantTransformer(1.0));

        tree = prim.getForest();

        // create two layouts for the one graph, one layout for each model
        Layout<String, Number> layout0 = new KKLayout<String, Number>(graph);
        layout0.setSize(preferredLayoutSize);
        Layout<String, Number> layout1 = new TreeLayout<String, Number>(tree);
        Layout<String, Number> layout2 = new StaticLayout<String, Number>(graph, layout1);

        // create the two models, each with a different layout
        VisualizationModel<String, Number> vm0
                = new DefaultVisualizationModel<String, Number>(layout0, preferredSize);
        VisualizationModel<String, Number> vm1
                = new DefaultVisualizationModel<String, Number>(layout1, preferredSizeRect);
        VisualizationModel<String, Number> vm2
                = new DefaultVisualizationModel<String, Number>(layout2, preferredSizeRect);

        // create the two views, one for each model
        // they share the same renderer
        vv0 = new VisualizationViewer<String, Number>(vm0, preferredSize);
        vv1 = new VisualizationViewer<String, Number>(vm1, preferredSizeRect);
        vv2 = new VisualizationViewer<String, Number>(vm2, preferredSizeRect);

//        vv1.setRenderContext(vv2.getRenderContext());
        vv1.getRenderContext().setMultiLayerTransformer(vv0.getRenderContext().getMultiLayerTransformer());
        vv2.getRenderContext().setMultiLayerTransformer(vv0.getRenderContext().getMultiLayerTransformer());

        vv1.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());

        vv0.addChangeListener(vv1);
        vv1.addChangeListener(vv2);

        vv0.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv2.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        Color back = Color.decode("0xffffbb");
        vv0.setBackground(back);
        vv1.setBackground(back);
        vv2.setBackground(back);

        vv0.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv0.setForeground(Color.darkGray);
        vv1.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv1.setForeground(Color.darkGray);
        vv2.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv2.setForeground(Color.darkGray);

        // share one PickedState between the two views
        PickedState<String> ps = new MultiPickedState<String>();
        vv0.setPickedVertexState(ps);
        vv1.setPickedVertexState(ps);
        vv2.setPickedVertexState(ps);

        PickedState<Number> pes = new MultiPickedState<Number>();
        vv0.setPickedEdgeState(pes);
        vv1.setPickedEdgeState(pes);
        vv2.setPickedEdgeState(pes);

        // set an edge paint function that will show picking for edges
        vv0.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(vv0.getPickedEdgeState(), Color.black, Color.red));
        vv0.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(vv0.getPickedVertexState(),
                Color.red, Color.yellow));
        vv1.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Number>(vv1.getPickedEdgeState(), Color.black, Color.red));
        vv1.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(vv1.getPickedVertexState(),
                Color.red, Color.yellow));

        // add default listeners for ToolTips
        vv0.setVertexToolTipTransformer(new ToStringLabeller());
        vv1.setVertexToolTipTransformer(new ToStringLabeller());
        vv2.setVertexToolTipTransformer(new ToStringLabeller());

        vv0.setLayout(new BorderLayout());
        vv1.setLayout(new BorderLayout());
        vv2.setLayout(new BorderLayout());

        Font font = vv0.getFont().deriveFont(Font.BOLD, 16);
        JLabel vv0Label = new JLabel("<html>Original Graph<p>using KKLayout");
        vv0Label.setFont(font);
        JLabel vv1Label = new JLabel("Minimum Spanning Trees");
        vv1Label.setFont(font);
        JLabel vv2Label = new JLabel("Original Graph using TreeLayout");
        vv2Label.setFont(font);
        JPanel flow0 = new JPanel();
        flow0.setOpaque(false);
        JPanel flow1 = new JPanel();
        flow1.setOpaque(false);
        JPanel flow2 = new JPanel();
        flow2.setOpaque(false);
        flow0.add(vv0Label);
        flow1.add(vv1Label);
        flow2.add(vv2Label);
        vv0.add(flow0, BorderLayout.NORTH);
        vv1.add(flow1, BorderLayout.NORTH);
        vv2.add(flow2, BorderLayout.NORTH);

//        vv2.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<Number,Paint>() {
//
//			public Paint transform(Number e) {
//				if(tree.getEdges().contains(e) == false) return Color.lightGray;
//				return Color.black;
//			}});
        Container content = getContentPane();
        JPanel grid = new JPanel(new GridLayout(0, 1));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new GraphZoomScrollPane(vv0), BorderLayout.WEST);
        grid.add(new GraphZoomScrollPane(vv1));
        grid.add(new GraphZoomScrollPane(vv2));
//        panel.add(new GraphZoomScrollPane(vv3), BorderLayout.EAST);
        panel.add(grid);

        content.add(panel);

        // create a GraphMouse for each view
        DefaultModalGraphMouse gm0 = new DefaultModalGraphMouse();
        DefaultModalGraphMouse gm1 = new DefaultModalGraphMouse();
        DefaultModalGraphMouse gm2 = new DefaultModalGraphMouse();
        DefaultModalGraphMouse gm3 = new DefaultModalGraphMouse();

        vv0.setGraphMouse(gm0);
        vv1.setGraphMouse(gm1);
        vv2.setGraphMouse(gm2);
//        vv3.setGraphMouse(gm3);

        // create zoom buttons for scaling the transformer that is
        // shared between the two models.
        final ScalingControl scaler = new CrossoverScalingControl();

        vv0.scaleToLayout(scaler);
        vv1.scaleToLayout(scaler);
        vv2.scaleToLayout(scaler);
//        vv3.scaleToLayout(scaler);

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv1, 1.1f, vv1.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv1, 1 / 1.1f, vv1.getCenter());
            }
        });

        JPanel zoomPanel = new JPanel(new GridLayout(1, 2));
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel modePanel = new JPanel();
        modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
        gm1.getModeComboBox().addItemListener(gm2.getModeListener());
        gm1.getModeComboBox().addItemListener(gm0.getModeListener());
        gm1.getModeComboBox().addItemListener(gm3.getModeListener());
        modePanel.add(gm1.getModeComboBox());

        JPanel controls = new JPanel();
        zoomPanel.add(plus);
        zoomPanel.add(minus);
        controls.add(zoomPanel);
        controls.add(modePanel);
        content.add(controls, BorderLayout.SOUTH);

        System.out.println(">>> Edges:    " + graph.getEdgeCount());
        System.out.println(">>> Vertices: " + graph.getVertexCount());
    }

    /**
     * a driver for this demo
     */
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MSTFrame frame = new MSTFrame();
        frame.initDemoData();
        frame.process();

        f.getContentPane().add(frame);
        f.pack();
        f.setVisible(true);
    }

    JFrame f = null;
    String label = null;

    public static MSTFrame init(String l, double ts) {

        MSTFrame mstf = new MSTFrame();

        mstf.label = l + " (ts=" + ts + ")";

        mstf.f = new JFrame(mstf.label);
        mstf.f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        mstf.TS = ts;

        System.out.println(">>> TS: " + ts);

        mstf.graph = new UndirectedSparseGraph<String, Number>();

        return mstf;
    }

    public void open() {

        f.getContentPane().add(this);
        f.pack();
        f.setVisible(true);

    }

    public static String[][] pairs = { { "a", "b", "3" }, {
			"a", "c", "4" }, {
			"a", "d", "5" }, {
			"d", "c", "6" }, {
			"d", "e", "7" }, {
			"e", "f", "8" }, {
			"f", "g", "9" }, {
			"h", "i", "1" }
	};
    
    /**
     * The Gephi-Graph comes in and is transformed inot a JUNG-Graph" because we
     * have to create an Minimum Spanning tree.
     */
    public void setGraph(UndirectedGraph graph) {
        
        boolean directed = false;
   
        UndirectedSparseMultigraph g = new UndirectedSparseMultigraph<String, Number>();

		for (int i = 0; i < pairs.length; i++) {
			String[] pair = pairs[i];
			createEdge(g, pair[0], pair[1], Integer.parseInt(pair[2]));
		}

		// let's throw in a clique, too
		for (int i = 1; i <= 10; i++) {
			for (int j = i + 1; j <= 10; j++) {
				String i1 = "c" + i;
				String i2 = "c" + j;
                g.addEdge(Math.pow(i+2,j), i1, i2);
			}
		}

		// and, last, a partial clique
		for (int i = 11; i <= 20; i++) {
			for (int j = i + 1; j <= 20; j++) {
				if (Math.random() > 0.6)
					continue;
				String i1 = "p" + i;
				String i2 = "p" + j;
                g.addEdge(Math.pow(i+2,j), i1, i2);
			}
		} 

    }
    
    	private static void createEdge(
			Graph<String, Number> g,
			String v1Label,
			String v2Label,
			int weight) {
			g.addEdge(new Double(Math.random()), v1Label, v2Label);
	}

}
