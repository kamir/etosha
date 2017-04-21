/*
 * Copyright 2017 The Apache Software Foundation.
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
package org.etosha.tools.profiler;

import java.io.File;

/**
 * 
 * This Model Handler cares about personal metadata tracking.
 * 
 * We load an RDF graph from user.home and persist it after a "flush()" command
 * a time-stamped file in the same folder. For convenience we store it twice.
 * The second copy is called "current" and reflects the current state.
 * 
 * There is no synchronization!!!!! Be carefull !!!!
 * 
 * @author kamir
 */
public class EtoshaModelHandler {

    public static void init(String FOLDER_DSM) {
        BASE_FOLDER_DSM = FOLDER_DSM;
        File f = new File(BASE_FOLDER_DSM);
        System.out.println("#BASE_FOLDER_DSM: " + f.getAbsolutePath()  + " => (r:" + f.canRead() + " - w:" + f.canWrite() + ")" );
    }

    /**
     * MLLib Model properties
     */
    public static String BASE_FOLDER_DSM = "../data/in/modelrepo/";

    /**
     * Network profiles
     */
    public static String BASE_FOLDER_GEXF = "./data/in/TSBASE/networks";
    public static String PROFILE_OUTPUT_FOLDER = "./data/out/PROFILES/from_GEXF_with_EtoshaReferenceProfiler";

    
    public EtoshaModelHandler() {    }

    public File[] getStorageFiles() {
        
        String ts = "" + System.currentTimeMillis();
        
        File f1 = new File( System.getProperty("user.home") + "/etosha/emds-current" );
        File f2 = new File( System.getProperty("user.home") + "/etosha/emds-" + ts + "." );
        
        File[] files = new File[2];
        files[0] = f1;
        files[1] = f2;
        
        for( File f : files ) {
            if ( !f.exists() ) {
                File p = f.getParentFile();
                if ( !p.exists() ) p.mkdirs();
            }
            
            System.out.println(">>> model-store : " + f.getAbsolutePath() +  " (w:" + f.canWrite()+")");
            
        }
        
        return files;
    }
    
    
}
