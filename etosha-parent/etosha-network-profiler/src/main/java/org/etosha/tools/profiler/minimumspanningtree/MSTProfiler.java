package org.etosha.tools.profiler.minimumspanningtree;

import org.etosha.networks.fluctuationnet.DFALink;
import org.etosha.networks.Link;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.List;
import javax.imageio.ImageIO;
import org.etosha.tools.profiler.Profiler;
import org.gephi.graph.api.UndirectedGraph;
import org.openide.util.Exceptions;

/**
 *
 * @author kamir
 */
public class MSTProfiler implements Profiler {

    int LAYER = 0;  // Allows selection of a particular Layer, e.g. the fit 
    // range for one specific alpha in a DFA Function.

    MSTFrame f = null;
    
    
    String label = null;

    public MSTProfiler(double TS, String fnOut) {

        /**
         * The Minimum-Spanning-Tree Frame is used for inspection ...
         */
        f = MSTFrame.init(fnOut, TS);

    }

    /**
     * RUNS THE PROFILER LOCALLY ON A WORKSTATION ...
     * Uses the MSTFrame which implements the logic of the MST analysis
     * based on the Gephi-toolkit and an additional plugin for Gephi.
     *
     * @return AN INSTANCE OF PROFILER ...
     */
    public static Profiler profileLocaly(double TS, String fnOut, String l, List<Link> linksSMALL) {

        MSTProfiler p = new MSTProfiler(TS, fnOut);

        p.label = l;
        
        for (Link link : linksSMALL) {
            p.f.addLink( link, p.LAYER );
        };

        p.f.process();
        p.f.open();

        return p;

    }

    public int getNumberEdges() {
        return f.graph.getEdgeCount();
    }

    public int getNumberVertices() {
        return f.graph.getVertexCount();
    }

    @Override
    public double getDiameter() {
        System.out.println("Diameter is not yet calculated in this profiler. (" + this.toString() + ")");
        return -1;
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
        System.out.println("ClusterCoefficient is not yet calculated in this profiler. (" + this.toString() + ")");
        return -1;
    }

    @Override
    public void storeImage(File folderOut, int t) {
        Container c = f.getContentPane();
        BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paint(im.getGraphics());
        folderOut.mkdirs();
        try {
           ImageIO.write(im, "PNG", new File( folderOut.getAbsolutePath() + "/" + label + ".png" ));
        } 
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public UndirectedGraph getGraph() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
