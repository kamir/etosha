/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.neo4j.domain;

import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public interface DaDiEntity {
    
    Vector<String> getTags();
    
    Properties getProperties();

    public String getSubject();

    public String describe();
    
}
