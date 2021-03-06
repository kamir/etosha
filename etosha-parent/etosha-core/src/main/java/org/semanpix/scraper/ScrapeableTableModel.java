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
package org.semanpix.scraper; 


import java.net.URL;

/**
 *
 * @author kamir
 */
public class ScrapeableTableModel {
    
     
    public URL url = null;
    public String selector = null;
    public long time_scraped = -1;
    
    public String[] headers = null;
    public String[] types = null;
    
    public String database = null;
    public String tabName = null;
    public String partitionCOLUMN = null;
    public String partitionVALUE = null;
    
}
