/*
 * Copyright 2015 The Apache Software Foundation.
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
package org.semanpix;

import java.io.File;
import java.util.Vector;

/**
 *
 * @author kamir
 */
public class SheetFactory {
    
    
    /**
     * From SparkSQL we take the Vector of all rows and export it as 
     * an Excel Sheet.
     * 
     * @return 
     */
    public static int dumpRowsVectorToExcelSheet(Vector rows, String tab, File document) {
        
        int status = 0;

        System.out.println( ">>> Excel-Sheet Factory <<<");
        System.out.println( ">>> File   : " + document.getAbsolutePath() );
        System.out.println( ">   Tab    : " + tab);
        System.out.println( ">>> Volume : " + rows.size() );
        
        return status;
    }
    
        /**
     * From SparkSQL we take the Vector of all rows and export it as 
     * an Excel Sheet.
     * 
     * @return 
     */
    public static int dumpRowsVectorToGoogleDocs(Vector rows, String tab, File document) {
        
        int status = 0;

        System.out.println( ">>> Excel-Sheet Factory <<<");
        System.out.println( ">>> File   : " + document.getAbsolutePath() );
        System.out.println( ">   Tab    : " + tab);
        System.out.println( ">>> Volume : " + rows.size() );
        
        return status;
    }
    
}
