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
package org.apache.etosha.hdfsindexer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.DocumentSelector;
import org.apache.tika.io.IOUtils;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class HDFSMetadataLoader {

    static String collection = null;
    static String zkHostString = null;
    static SolrServer solr = null;
    static Configuration conf = null;

    /**
     * Parsing context.
     */
    static ParseContext context = null;

    /**
     * Configured parser instance.
     */
    static Parser parser = null;
    
    /**
     * Captures requested embedded images
     */
    static ImageSavingParser imageParser = null;
    
    /**
     * 
     * Defaults point to our training cluster ...
     * 
     * @throws Exception 
     */
    public static void init() throws Exception {
        zkHostString = "training01.sjc.cloudera.com:2181,training03.sjc.cloudera.com:2181,training06.sjc.cloudera.com:2181/solr";
        solr = new CloudSolrServer(zkHostString);
        collection = "hdfs_fdi1";
            
        context = new ParseContext();
        parser = new AutoDetectParser();

        imageParser = new ImageSavingParser(parser);
        context.set(DocumentSelector.class, new ImageDocumentSelector());
        context.set(Parser.class, imageParser);
    }

    public static void importHDFSMetadataForFolder(String folder, String mode) throws LoginException, IOException, SolrServerException {

        try {
            System.out.println("> Operation mode : " + mode );
       
            String fsn = "file:///"; 
            if ( mode.equals("l"))
                fsn = ReadCMD.read("> fs.defaultFS : ", "file:///" );
        
            if ( mode.equals("r"))
                fsn = ReadCMD.read("> fs.defaultFS : ", "hdfs://training06.sjc.cloudera.com:8020" );
            
            System.out.println("> Index folder   : " + folder );
            System.out.println("> fs.defaultFS   : " + fsn );

            // Initialize the File System
            conf = new Configuration();
            conf.set("fs.defaultFS", fsn );

            Path path = new Path( fsn + folder );
            System.out.println(">>> Path : " + path.toString() );

            processPath(path, conf);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        };
    }

    /**
     * We index all files in a path and submit at the end of this task.
     * 
     * @param path
     * @param conf 
     */
    private static void processPath(Path path, Configuration conf) {
        try {
            
            if ( path.getName().startsWith(".") ) return;
            
            FileSystem fs = FileSystem.get(path.toUri(), conf);
            
            // we ignore the subdirectories ...
            FileStatus[] status = fs.listStatus( path );
            for (int i = 0; i < status.length; i++) {
                System.out.println(status[i].getPath());
                importHDFSdataFromFolder(status[i], conf);
            }
            
            UpdateRequest commit = new UpdateRequest();
            commit.setAction(UpdateRequest.ACTION.COMMIT, true, true);
            commit.setParam("collection", collection);
            commit.process(solr);
        } 
        catch (Exception ex) {
            Logger.getLogger(HDFSMetadataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void importHDFSdataFromFolder(FileStatus f,Configuration conf)  {

        try {
            
            /**

            System.out.println("**** Imorting now *** \n" + f.getPath().toString());
            
            System.out.println("getPath " + f.getPath());
            
            System.out.println("getBlockSize " + f.getBlockSize());
            System.out.println("getReplication " + f.getReplication());
            
            System.out.println("getGroup " + f.getGroup());
            System.out.println("getOwner " + f.getOwner());
            
            System.out.println("getLen " + f.getLen());
            System.out.println("getModificationTime " + f.getModificationTime());
            
            System.out.println("getPermission " + f.getPermission().toShort());
            
            **/
            
            /**
             *
             *
             *
             * <field name="id" type="string" required="true" indexed="true"
             * stored="true" multiValued="false" />
             *
             * <field name="owner" type="string" required="true" indexed="true"
             * stored="true" multiValued="false" />
             * <field name="group" type="string" required="true" indexed="true"
             * stored="true" multiValued="false" />
             * <field name="replication" type="int" required="true" indexed="true"
             * stored="true" multiValued="false" />
             * <field name="blockSize" type="int" required="true" indexed="true"
             * stored="true" multiValued="false" />
             *
             * <field name="len" type="int" required="true" indexed="true"
             * stored="true" multiValued="false" />
             * <field name="modificationTime" type="date" required="true"
             * indexed="true" stored="true" multiValued="false" />
             *
             * <field name="isdir" type="boolean" required="true" indexed="true"
             * stored="true" multiValued="false" />
             *
             * <field name="permission" type="string" required="true" indexed="true"
             * stored="true" multiValued="false" />
             *
             * <dynamicField name="*_i" type="int" indexed="true" stored="true"/>
             * <dynamicField name="*_s" type="string" indexed="true" stored="true"/>
             * <dynamicField name="*_d" type="date" indexed="true" stored="true"/>          *
             *
             */
            // UUID uid = UUID.randomUUID();
            // System.out.println("# UUID:    > " + uid.toString());

     
            SolrInputDocument document = new SolrInputDocument();
            document.addField("context", "fdi");
            document.addField("id", f.getPath().toUri());
            
            String cont = "n.a.";
            if ( f.isDirectory() ) {
                
            }
            else {
                cont = handleContent(document, f.getPath() );
            }
                
            document.addField("content", cont);
            
            document.addField("group", f.getGroup());
            document.addField("owner", f.getOwner());
            document.addField("replication", (int) f.getReplication());
            document.addField("blockSize", f.getBlockSize());
            
            document.addField("len", f.getLen());
            document.addField("isdir", f.isDir());
            document.addField("permission", f.getPermission().toString());
            document.addField("indexer", "FILEINDEXER");
            
            Date d = new Date(f.getModificationTime());
            
            DateFormat dfmt1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dfmt2 = new SimpleDateFormat("hh:mm:ss");
            document.addField("modificationTime", dfmt1.format(d) + "T" + dfmt2.format(d) + "Z");
            
            UpdateRequest add = new UpdateRequest();
            add.add(document);
            add.setParam("collection", collection);
            add.process(solr);
            
            if( f.isDir() ) processPath( f.getPath(), conf );
            
        } 
        catch (SolrServerException ex) {
            Logger.getLogger(HDFSMetadataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(HDFSMetadataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static String handleContent(SolrInputDocument document, Path path) {
   
        try {
            Metadata metadata = new Metadata();
            
            FileSystem hdfs = FileSystem.get( conf );

            URI uri = path.toUri();
            FSDataInputStream instrm = hdfs.open(new Path(uri));
            
            TikaInputStream stream = TikaInputStream.get( instrm );
            try {
            
                ParserResults data = handleStream(stream, metadata);
                
                String[] names = data.md.names();
                Arrays.sort(names);
                
                for (String name : names) {
                    String value = data.md.get(name);
                    data.metadataBuffer.append(name);
                    data.metadataBuffer.append(": ");
                    data.metadataBuffer.append(value);
                    data.metadataBuffer.append("\n");
                    document.addField(name+"_s", value);
                }
                
                return data.textBuffer.toString();

            } 
            finally {
                stream.close();
            }
        } 
        catch (Throwable t) {
            t.printStackTrace(); 
        }                
        return "n.a.";
    }

  
    
    
    private static ParserResults handleStream(InputStream input, Metadata md)
            throws Exception {
        
        ParserResults pr = new ParserResults();
        
        pr.htmlBuffer = new StringWriter();
        pr.textBuffer = new StringWriter();
        pr.textMainBuffer = new StringWriter();
        pr.xmlBuffer = new StringWriter();
        pr.metadataBuffer = new StringBuilder();
        pr.md = md;

        ContentHandler handler = new TeeContentHandler(
                getHtmlHandler(pr.htmlBuffer),
                getTextContentHandler(pr.textBuffer),
                getTextMainContentHandler(pr.textMainBuffer),
                getXmlContentHandler(pr.xmlBuffer));

        context.set(DocumentSelector.class, new ImageDocumentSelector());

//        input = new ProgressMonitorInputStream(
//                this, "Parsing stream", input);
//        parser.parse(input, handler, md, context);


        String name = md.get(Metadata.RESOURCE_NAME_KEY);
        if (name != null && name.length() > 0) {
            name = "Apache Tika: " + name;
        } else {
            name = "Apache Tika: unnamed document";
        }

//        setText(metadata, metadataBuffer.toString());
//        setText(xml, xmlBuffer.toString());
//        setText(text, textBuffer.toString());
//        setText(textMain, textMainBuffer.toString());
//        setText(html, htmlBuffer.toString());
        
        return pr;
        
    }

    private static void handleError(String name, Throwable t) {
        StringWriter writer = new StringWriter();
        writer.append("Apache Tika was unable to parse the document\n");
        writer.append("at " + name + ".\n\n");
        writer.append("The full exception stack trace is included below:\n\n");
        t.printStackTrace(new PrintWriter(writer));

//        JEditorPane editor =
//            new JEditorPane("text/plain", writer.toString());
//        editor.setEditable(false);
//        editor.setBackground(Color.WHITE);
//        editor.setCaretPosition(0);
//        editor.setPreferredSize(new Dimension(600, 400));
//
//        JDialog dialog = new JDialog(this, "Apache Tika error");
//        dialog.add(new JScrollPane(editor));
//        dialog.pack();
//        dialog.setVisible(true);
    }
    
    
    /**
     * Creates and returns a content handler that turns XHTML input to
     * simplified HTML output that can be correctly parsed and displayed
     * by {@link JEditorPane}.
     * <p>
     * The returned content handler is set to output <code>html</code>
     * to the given writer. The XHTML namespace is removed from the output
     * to prevent the serializer from using the &lt;tag/&gt; empty element
     * syntax that causes extra "&gt;" characters to be displayed.
     * The &lt;head&gt; tags are dropped to prevent the serializer from
     * generating a &lt;META&gt; content type tag that makes
     * {@link JEditorPane} fail thinking that the document character set
     * is inconsistent.
     * <p>
     * Additionally, it will use ImageSavingParser to re-write embedded:(image) 
     * image links to be file:///(temporary file) so that they can be loaded.
     *
     * @param writer output writer
     * @return HTML content handler
     * @throws TransformerConfigurationException if an error occurs
     */
    private static org.xml.sax.ContentHandler getHtmlHandler(Writer writer)
            throws TransformerConfigurationException {
        SAXTransformerFactory factory = (SAXTransformerFactory)
            SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
        handler.setResult(new StreamResult(writer));
        return new ContentHandlerDecorator(handler) {
            @Override
            public void startElement(
                    String uri, String localName, String name, Attributes atts)
                    throws SAXException {
                if (XHTMLContentHandler.XHTML.equals(uri)) {
                    uri = null;
                }
                if (!"head".equals(localName)) {
                    if("img".equals(localName)) {
                       AttributesImpl newAttrs;
                       if(atts instanceof AttributesImpl) {
                          newAttrs = (AttributesImpl)atts;
                       } else {
                          newAttrs = new AttributesImpl(atts);
                       }
                       
                       for(int i=0; i<newAttrs.getLength(); i++) {
                          if("src".equals(newAttrs.getLocalName(i))) {
                             String src = newAttrs.getValue(i);
                             if(src.startsWith("embedded:")) {
                                String filename = src.substring(src.indexOf(':')+1);
                                try {
                                   File img = imageParser.requestSave(filename);
                                   String newSrc = img.toURI().toString();
                                   newAttrs.setValue(i, newSrc);
                                } catch(IOException e) {
                                   System.err.println("Error creating temp image file " + filename);
                                   // The html viewer will show a broken image too to alert them
                                }
                             }
                          }
                       }
                       super.startElement(uri, localName, name, newAttrs);
                    } else {
                       super.startElement(uri, localName, name, atts);
                    }
                }
            }
            @Override
            public void endElement(String uri, String localName, String name)
                    throws SAXException {
                if (XHTMLContentHandler.XHTML.equals(uri)) {
                    uri = null;
                }
                if (!"head".equals(localName)) {
                    super.endElement(uri, localName, name);
                }
            }
            @Override
            public void startPrefixMapping(String prefix, String uri) {
            }
            @Override
            public void endPrefixMapping(String prefix) {
            }
        };
    }

    private static org.xml.sax.ContentHandler getTextContentHandler(Writer writer) {
        return new BodyContentHandler(writer);
    }
    private static org.xml.sax.ContentHandler getTextMainContentHandler(Writer writer) {
        return new BoilerpipeContentHandler(writer);
    }

    private static org.xml.sax.ContentHandler getXmlContentHandler(Writer writer)
            throws TransformerConfigurationException {
        SAXTransformerFactory factory = (SAXTransformerFactory)
            SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        handler.setResult(new StreamResult(writer));
        return handler;
    }
    
    /**
     * A {@link DocumentSelector} that accepts only images.
     */
    private static class ImageDocumentSelector implements DocumentSelector {
      public boolean select(Metadata metadata) {
         String type = metadata.get(Metadata.CONTENT_TYPE);
         return type != null && type.startsWith("image/");
      }
    }
    
    /**
     * A recursive parser that saves certain images into the temporary
     *  directory, and delegates everything else to another downstream
     *  parser.
     */
    private static class ImageSavingParser extends AbstractParser {
      private Map<String,File> wanted = new HashMap<String,File>();
      private Parser downstreamParser;
      private File tmpDir;
      
      private ImageSavingParser(Parser downstreamParser) {
         this.downstreamParser = downstreamParser;
         
         try {
            File t = File.createTempFile("tika", ".test");
            tmpDir = t.getParentFile();
         } catch(IOException e) {}
      }public File requestSave(String embeddedName) throws IOException {
         String suffix = ".tika";
         
         int splitAt = embeddedName.lastIndexOf('.');
         if (splitAt > 0) {
            embeddedName.substring(splitAt);
         }
         
         File tmp = File.createTempFile("tika-embedded-", suffix);
         wanted.put(embeddedName, tmp);
         return tmp;
      }
      
      public Set<MediaType> getSupportedTypes(ParseContext context) {
         // Never used in an auto setup
         return null;
      }

      public void parse(InputStream stream, org.xml.sax.ContentHandler handler,
            Metadata metadata, ParseContext context) throws IOException,
            SAXException, TikaException {
         String name = metadata.get(Metadata.RESOURCE_NAME_KEY);
         if(name != null && wanted.containsKey(name)) {
            FileOutputStream out = new FileOutputStream(wanted.get(name));
            IOUtils.copy(stream, out);
            out.close();
         } else {
            if(downstreamParser != null) {
               downstreamParser.parse(stream, handler, metadata, context);
            }
         }
      }

    }

}

class ParserResults  {
    
    public Metadata md = null;
    public StringWriter htmlBuffer = new StringWriter();
    public StringWriter textBuffer = new StringWriter();
    public StringWriter textMainBuffer = new StringWriter();
    public StringWriter xmlBuffer = new StringWriter();
    public StringBuilder metadataBuffer = new StringBuilder();

    
}