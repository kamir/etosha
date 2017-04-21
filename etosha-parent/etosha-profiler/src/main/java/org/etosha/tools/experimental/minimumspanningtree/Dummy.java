/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etosha.tools.experimental.minimumspanningtree;


import org.etosha.tools.experimental.minimumspanningtree.MSTTool;
import java.io.IOException;
import org.openide.util.Exceptions;

/**
 *
 * @author kamir
 */
public class Dummy {
     
    static public String ext = ".csv";
    
    static public String[] dfaResults = { 
                        "b10", 
                        "b2",
                        "b9",  
                        "c10",  
                        "c2", 
                        "c9"};  

    static StringBuffer sb = new StringBuffer();
    
    public static void main(String[] args) {
        try {
            
            for( String s : dfaResults ) {
                
                String fn = "/GITHUB/SparkNetworkCreator/data/out/DCCA-net-SORTED-" + s + ext + "/part-00000";
                MSTTool tool = new MSTTool( sb );
                
                tool.process(fn, 0, s);
                tool.process(fn, 1, s);
                tool.process(fn, 2, s);
            
            }
            
            System.out.println( sb.toString() );
            
        } 
        catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
}
