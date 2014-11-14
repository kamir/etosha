/*
 * Copyright 2014 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.etosha.cmd.admintools;

import java.io.IOException;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Metadata;
import org.apache.hadoop.io.Text;
import org.etosha.model.FileMD;

/**
 *
 * @author training
 */
public class SEQFileInspector {
    
    
    public static FileMD inspectFile( Configuration cfg, Path p) throws IOException {
        FileMD fmd = new FileMD();
        
        
        FileSystem fs = FileSystem.get(cfg);
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, p, cfg);
        System.out.println( "Key       : " + reader.getKeyClassName() );
        System.out.println( "Value     : " + reader.getValueClassName() );
        System.out.println( "Codec     : " + reader.getCompressionCodec() );
        System.out.println( "Comp.type : " + getType( reader.isBlockCompressed() ) );

        Metadata md = reader.getMetadata();
        TreeMap<Text,Text> map = md.getMetadata();
        
        for( Text t : map.keySet() ) {
            System.out.println( t.toString() + " : " + map.get(t) );
        }
        
        return fmd;
    }
    
    /**
     * What type of compresseion 
     * @param blockCompressed
     * @return 
     */

    private static String getType( boolean blockCompressed ) {
        if ( blockCompressed ) return "BLOCK";
        else {
            return "RECORD or NONE";
        }
    }
    
}
