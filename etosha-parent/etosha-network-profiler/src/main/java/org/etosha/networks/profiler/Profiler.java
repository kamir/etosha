/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etosha.networks.profiler;

import java.io.File;

/**
 *
 * @author kamir
 */
public interface Profiler {

    public int getNumberEdges();

    public int getNumberVertices();

    public double getDiameter();

    public int getMaxCLusterNrNodes();

    public int getMaxCLusterNrEdges();

    public double getGlobalClusterCoefficient();

    public void storeImage(File folderOut, int timeInSeconds);
    
}
