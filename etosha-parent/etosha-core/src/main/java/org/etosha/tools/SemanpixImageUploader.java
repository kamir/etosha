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
package org.etosha.tools;

import org.semanpix.parser.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitorInputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.tika.extractor.DocumentSelector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.etosha.cmd.EtoshaContextLogger;
import org.etosha.core.sc.connector.internal.ImageToWikiUploader;
import static org.etosha.tools.gui.WikiTemplateRenderer.clt;
import org.xml.sax.ContentHandler;

/**
 *
 * @author kamir
 */
public class SemanpixImageUploader {

    static String defaultFolder = "/Users/kamir/projects/Semanpix/GITHUB/netbeans-projects/Semanpix3/DATA/PixBase/UPLOADS/";

    static SemanpixImageParser sp = new SemanpixImageParser();

    static EtoshaContextLogger clt = null;
    
    public static void main(String args[]) throws Exception {

        SemanpixImageUploader ul = new SemanpixImageUploader();
        ul.init();
        
        ImageToWikiUploader.clt = clt;
        
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(defaultFolder);
        
        System.out.println("WATCHING: " + defaultFolder);

        dir.register(watcher, ENTRY_CREATE);

        while (true) {

            WatchKey key;
            try {
                // wait for a key to be available
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                // get event type
                WatchEvent.Kind<?> kind = event.kind();

                // get file name
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;

                Path fileName = ev.context();
                File file = new File(dir.toString() + "/" + ev.context());

                System.out.println(kind.name() + ": " + fileName);

                if (kind == OVERFLOW) {
                    continue;
                } else if (kind == ENTRY_CREATE) {
                    // process create event

                    System.out.println(">>> CREATE : " + fileName.toString() + " recognized for smw-upload ...");
                    URL url = file.toURI().toURL();

                    String md = sp.getMetaData(url);
                    Properties props = sp.getMetaDataAsProperties(url);

                    String result = ImageToWikiUploader._importImageToNewPage(file, props);

                    System.out.println(result);

                } else if (kind == ENTRY_DELETE) {
                    // process delete event

                    System.out.println(">>> D " + fileName.toString() + " recognized for smw-upload ...");

                } else if (kind == ENTRY_MODIFY) {
                    // process modify event

                    System.out.println(">>> M " + fileName.toString() + " recognized for smw-upload ...");

                }
            }

            // IMPORTANT: The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
    
        
    public static void init() throws Exception {
        
        clt = new EtoshaContextLogger();

        clt.setConf(new Configuration());

        File cfgFile = EtoshaContextLogger.getCFGFile();

        if (cfgFile.exists()) {
            /**
             * according to:
             *
             * http://stackoverflow.com/questions/11478036/hadoop-configuration-
             * property-returns-null
             *
             * we add the resource as a URI
             */
            clt.getConf().addResource(cfgFile.getAbsoluteFile().toURI().toURL());
        }

        clt.initConnector();
         
    }

}
