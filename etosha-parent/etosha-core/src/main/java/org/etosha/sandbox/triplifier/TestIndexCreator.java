package org.etosha.sandbox.triplifier;

//      Learn from:  
// 
//      http://www.l3s.de/~siberski/bibtex2rdf/
// 
//      to make a tool, which allows triplification of
//      data in a SOLR index defined by a given SCHEMA.
// 
//      We implement a "SOLR SCHEMA to TRIPLE" converter here.

import com.hp.hpl.jena.rdf.model.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList; 


/**
 *
 * @author kamir
 */
public class TestIndexCreator {
    
/**
 * This terminal application creates an Apache Lucene index in a folder and adds files into this index
 * based on the input of the user.
 */ 
  private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

  private IndexWriter writer;
  private ArrayList<File> queue = new ArrayList<File>();
  private ArrayList<File> pdfQueue = new ArrayList<File>();


  public static void main(String[] args) throws IOException {
    
    String indexLocation = "./tmp/index";
    
    System.out.println("The path where the index will be created is: " + indexLocation + ")");

    BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in));

    TestIndexCreator indexer = null;
    try { 
      indexer = new TestIndexCreator(indexLocation);
    } catch (Exception ex) {
      System.out.println("Cannot create index..." + ex.getMessage());
      System.exit(-1);
    }

    //===================================================
    //read input from user until he enters q for quit
    //===================================================
//    while (!indexLocation.equalsIgnoreCase("q")) {
//      try {
//        System.out.println("Enter the full path to add into the index (q=quit): (e.g. /home/ron/mydir or c:\\Users\\ron\\mydir)");
//        System.out.println("[Acceptable file types: .xml, .html, .html, .txt, .pdf]");
//        s = br.readLine();
//        if (s.equalsIgnoreCase("q")) {
//          break;
//        }
//
//        //try to add file into the index
////        indexer.indexFileOrDirectory(s);
//        
//      } catch (Exception e) {
//        System.out.println("Error indexing " + s + " : " + e.getMessage());
//      }
//    }

    //===================================================
    //after adding, we always have to call the
    //closeIndex, otherwise the index is not created    
    //===================================================
    indexer.closeIndex();

    //=========================================================
    // Now search
    //=========================================================
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(25, true);

    String s = "";
    while (!s.equalsIgnoreCase("q")) {
      try {
        System.out.println("Enter the search query (q=quit):");
        s = br.readLine();
        if (s.equalsIgnoreCase("q")) {
          break;
        }
        Query q = new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(s);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
        
        }

        Model m = ModelFactory.createDefaultModel();
        
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
        
          Triplifier.document2triples( m, d );        
        
        }

        m.write( System.out, "N-TRIPLES" );
        
        
      } 
      catch (Exception e) {
        System.out.println("Error searching " + s + " : " + e.getMessage());
      }
    }

  }

  /**
   * Constructor
   * @param indexDir the name of the folder in which the index should be created
   * @throws java.io.IOException when exception creating index.
   */
  TestIndexCreator(String indexDir) throws IOException {
    // the boolean true parameter means to create a new index everytime, 
    // potentially overwriting any existing files there.
    FSDirectory dir = FSDirectory.open(new File(indexDir));


    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

    writer = new IndexWriter(dir, config);
  }

  /**
   * Indexes a file or directory
   * @param fileName the name of a text file or a folder we wish to add to the index
   * @throws java.io.IOException when exception
   */
  public void indexFileOrDirectory(String fileName) throws IOException {
    //===================================================
    //gets the list of files in a folder (if user has submitted
    //the name of a folder) or gets a single file name (is user
    //has submitted only the file name) 
    //===================================================
    addFiles(new File(fileName));
    
    int originalNumDocs = writer.numDocs();
    for (File f : queue) {
      FileReader fr = null;
      try {
        Document doc = new Document();

        //===================================================
        // add contents of file
        //===================================================
        fr = new FileReader(f);
        doc.add(new TextField("contents", fr));
        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

        writer.addDocument(doc);
        System.out.println("Added: " + f);
      } catch (Exception e) {
        System.out.println("Could not add: " + f);
      } finally {
        fr.close();
      }
    }
    
    int newNumDocs = writer.numDocs();
    System.out.println("");
    System.out.println("************************");
    System.out.println((newNumDocs - originalNumDocs) + " documents added.");
    System.out.println("************************");
    
    queue.clear();
    
    originalNumDocs = writer.numDocs();
    for (File f : pdfQueue) {
      FileReader fr = null;
      try {
          
        Document doc = new Document();

        //===================================================
        // add contents of file
        //===================================================
        fr = new FileReader(f);
        doc.add(new TextField("contents", fr));
        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

        writer.addDocument(doc);
        System.out.println("Added: " + f);
      } catch (Exception e) {
        System.out.println("Could not add: " + f);
      } finally {
        fr.close();
      }
    }
    
    newNumDocs = writer.numDocs();
    System.out.println("");
    System.out.println("************************");
    System.out.println((newNumDocs - originalNumDocs) + " PDF documents added.");
    System.out.println("************************");
    
    pdfQueue.clear();

  }

  private void addFiles(File file) {

    if (!file.exists()) {
      System.out.println(file + " does not exist.");
    }
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        addFiles(f);
      }
    } else {
      String filename = file.getName().toLowerCase();
      //===================================================
      // Only index text files
      //===================================================
      if (filename.endsWith(".htm") || filename.endsWith(".html") || 
              filename.endsWith(".xml") || filename.endsWith(".txt")) {
        queue.add(file);
      } else {
        System.out.println("Skipped " + filename);
      }
      //===================================================
      // Only index text files
      //===================================================
      if (filename.endsWith(".pdf") ) {
        pdfQueue.add(file);
      } else {
        System.out.println("Skipped " + filename);
      }
    }
  }

  /**
   * Close the index.
   * @throws java.io.IOException when exception closing
   */
  public void closeIndex() throws IOException {
    writer.close();
  }
}