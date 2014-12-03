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
package org.apache.tika.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitorInputStream;
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
import org.xml.sax.ContentHandler;

/**
 *
 * @author kamir
 */
public class SimpleTikaParser {

    private static ParseContext context = null;
    private static Parser parser = null;

    public static void main(String args[]) throws Exception {

        String[] adresses = {
            "http://semanpix.de/oldtimer/wiki/images/5/5e/Peter001.jpg",
            "http://semanpix.de/opendata/wiki/index.php?title=Main_Page"
        };
        
        SimpleTikaParser sp = new SimpleTikaParser();
    
        File f = new File("./Peter001.jpg");

        System.out.println( sp.getMetaData(f) );
        
    };
        



    private static String handleStream(InputStream input, Metadata md)
            throws Exception {

        StringWriter htmlBuffer = new StringWriter();
        StringWriter textBuffer = new StringWriter();
        StringWriter textMainBuffer = new StringWriter();
        StringWriter xmlBuffer = new StringWriter();
        StringBuilder metadataBuffer = new StringBuilder();

        ContentHandler handler = new TeeContentHandler(
                TikaGUI.getTextContentHandler(textBuffer),
                TikaGUI.getTextMainContentHandler(textMainBuffer),
                TikaGUI.getXmlContentHandler(xmlBuffer));

        context.set(DocumentSelector.class, new TikaGUI.ImageDocumentSelector());

        parser.parse(input, handler, md, context);

        String[] names = md.names();
        Arrays.sort(names);
        for (String name : names) {
            metadataBuffer.append(name);
            metadataBuffer.append(": ");
            metadataBuffer.append(md.get(name));
            metadataBuffer.append("\n");
        }

        System.out.println( metadataBuffer.toString() );
        
        return metadataBuffer.toString();
    }

    public String getMetaData(File f) throws IOException, Exception {

                
        try {
            
            context = new ParseContext();
            parser = new AutoDetectParser();
            
            
            URL url = f.toURI().toURL();
            
            System.out.println(url);
            
            InputStream input = url.openStream();
            
            Metadata metadata = new Metadata();
            
            return handleStream(input, metadata);
            
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(SimpleTikaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return " *** no metadata available *** ";
        
    }

}
