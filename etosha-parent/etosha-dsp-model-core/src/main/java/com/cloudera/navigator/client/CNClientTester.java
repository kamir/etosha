/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudera.navigator.client;

/**
 *
 * @author kamir
 */
public class CNClientTester {
    
    public static void main( String args[] ) {
        
        // works in Spark-Shell
        String id = "fx_m1";
        ObjectMetadata omd = MetadataSink.getMDOForHDFSFolderByName( id );
        System.out.println( "id: " + id + " => identity:" + omd.getIdentity() ); 
        
    }
    
}
