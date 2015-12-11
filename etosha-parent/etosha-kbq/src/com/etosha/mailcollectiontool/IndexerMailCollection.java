// Indexer.java
package com.etosha.mailcollectiontool;

import com.etosha.mailcollectiontool.ConfigurationMailCollection;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * Class to create Lucene Index from files. Remember this class will only index
 * files inside a folder. If there are multiple folder inside the source folder
 * it will not index those files.
 *
 * It will only index text files
 *
 * @author Mubin Shrestha
 */
public class IndexerMailCollection {

    public static boolean useNgramsAsTerms = true;
    public static int N = 2;

    private File sourceDirectory;
    private File indexDirectory;
    private static String fieldName;

    /**
     * 
     * @param sd
     * @param id
     * @param field1
     * @param field2 
     */
    public IndexerMailCollection(String sd, String id, String field1, String field2) {
        
        ConfigurationMailCollection.SOURCE_DIRECTORY_TO_INDEX = sd;
        ConfigurationMailCollection.INDEX_DIRECTORY = id;
        ConfigurationMailCollection.FIELD_CONTENT_1 = field1;
        ConfigurationMailCollection.FIELD_CONTENT_2 = field2;

        this.sourceDirectory = new File(ConfigurationMailCollection.SOURCE_DIRECTORY_TO_INDEX);
        this.indexDirectory = new File(ConfigurationMailCollection.INDEX_DIRECTORY);
        fieldName = ConfigurationMailCollection.FIELD_CONTENT_1;

        this.indexDirectory.mkdirs();
        this.sourceDirectory.mkdirs();
        
        File infolder = new File( sd );
        System.out.println(">>> Documents to work on : " + infolder.getAbsolutePath() );
        System.out.println(">>> Index location       : " + id);
        System.out.println(">>> Index field 1        : " + field1);
        System.out.println(">>> Index field 2        : " + field2);
    }

    public IndexerMailCollection() {
        this.sourceDirectory = new File(ConfigurationMailCollection.SOURCE_DIRECTORY_TO_INDEX);
        this.indexDirectory = new File(ConfigurationMailCollection.INDEX_DIRECTORY);
        fieldName = ConfigurationMailCollection.FIELD_CONTENT_1;

        this.indexDirectory.mkdirs();
        this.sourceDirectory.mkdirs();
    }

    /**
     * Method to create Lucene Index Keep in mind that always index text value
     * to Lucene for calculating Cosine Similarity. You have to generate tokens,
     * terms and their frequencies and store them in the Lucene Index.
     *
     * @throws CorruptIndexException
     * @throws LockObtainFailedException
     * @throws IOException
     */
    public void index() throws CorruptIndexException,
            LockObtainFailedException, IOException {
        
        
        System.out.println( ">>> Process source directory  : " + sourceDirectory.getAbsolutePath() );
        
        System.out.println( ">>> Create index in directory : " + indexDirectory.getAbsolutePath() );
        
        Directory dir = FSDirectory.open(indexDirectory);
        
        

        Analyzer analyzer = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET);  // using stop words
        // Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);  // using stop words
        // System.out.println(">>> Overwrite the Analyser: DO NOT USE STOPWORDS FILTER !!!");

        /**
         *
         * Source:
         * http://toolongdidntread.com/lucene/using-lucene-to-generate-n-grams/
         *
         *
         *
         * 1) Run a document through an Analyzer which filters out the stuff we
         * don’t care about. SimpleAnalyzer, in this case, applies a lower case
         * filter and a letter tokenizer, which makes all text lowercase and
         * divides text at non-letters, respectively.
         *
         * 2) Wrap this analyzer with ShingleAnalyzerWrapper which constructs
         * shingles (token n-grams) from a stream. This is the main thing we
         * want to accomplish.
         *
         * 3) We generate a TokenStream which enumerates (a fancy word for
         * establishes) “fields” from a “document” (what I mentioned earlier).
         *
         * 4) Given a token stream, we want to extract certain things from it,
         * like just the characters and not all the other stuff that comes along
         * with the stream. We’ll use CharTermAttribute which extract just the
         * words from the stream.
         *
         * 5) Finally, we iterate over the stream by incrementing the tokens,
         * extracting each CharTermAttribute from the tokens.
         *
         */
        if (useNgramsAsTerms) {
            analyzer = getNGramAnalyser(analyzer, N);

        }

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

        if (indexDirectory.exists()) {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        } 
        else {
            // Add new documents to an existing index:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        }

        IndexWriter writer = new IndexWriter(dir, iwc);

        for (File f : sourceDirectory.listFiles()) {

            System.out.println("> DOC  :  " + f.getAbsolutePath() );
 
            if (f.isDirectory()) {

                System.out.println(">>> Indexer processes FILE : " + f.getAbsolutePath());

                for (File fileTXT : f.listFiles()) {

                    String at = getAllText(fileTXT);

                    System.out.println("> file  :  " + fileTXT.getAbsolutePath());
                    Document doc = new Document();
                    
                    FieldType fieldType = new FieldType();
                    fieldType.setIndexed(true);
                    fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
                    fieldType.setStored(true);
                    fieldType.setStoreTermVectors(true);
                    fieldType.setTokenized(true);
                    Field contentField = new Field(fieldName, at, fieldType);
                    doc.add(contentField);

                    FieldType fieldType2 = new FieldType();
                    fieldType2.setIndexed(true);
                    fieldType2.setStored(true);
                    fieldType2.setStoreTermVectors(false);
                    fieldType2.setTokenized(false);
                    Field idField = new Field("id", fileTXT.getAbsolutePath(), fieldType2);
                    doc.add(idField);

                    FieldType fieldType3 = new FieldType();
                    fieldType3.setIndexed(false);
                    fieldType3.setStored(true);
                    fieldType3.setStoreTermVectors(false);
                    fieldType3.setTokenized(false);
                    Field rawField = new Field("raw", at, fieldType3);
                    doc.add(rawField);

                    writer.addDocument(doc);
                    
                    

                }

            }
                else {

                if (!f.getName().startsWith(".DS_Store")) {

                    String at = getAllText(f);
                    
                    Document doc = new Document();
                    FieldType fieldType = new FieldType();
                    fieldType.setIndexed(true);
                    fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
                    fieldType.setStored(true);
                    fieldType.setStoreTermVectors(true);
                    fieldType.setTokenized(true);
                    Field contentField = new Field(fieldName, at, fieldType);
                    doc.add(contentField);

                    FieldType fieldType2 = new FieldType();
                    fieldType2.setIndexed(true);
                    fieldType2.setStored(true);
                    fieldType2.setStoreTermVectors(false);
                    fieldType2.setTokenized(false);
                    Field idField = new Field("id", f.getAbsolutePath(), fieldType2);
                    doc.add(idField);
            
                            FieldType fieldType3 = new FieldType();
                    fieldType3.setIndexed(false);
                    fieldType3.setStored(true);
                    fieldType3.setStoreTermVectors(false);
                    fieldType3.setTokenized(false);
                    Field rawField = new Field("raw", at, fieldType3);
                    doc.add(rawField);

                    writer.addDocument(doc);
                }

            }
        }
        writer.close();
    }

    /**
     * Method to get all the texts of the text file. Lucene cannot create the
     * term vetors and tokens for reader class. You have to index its text
     * values to the index. It would be more better if this was in another
     * class. I am lazy you know all.
     *
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    int i = 0;

    public String getAllText(File f) throws FileNotFoundException, IOException {
        String textFileContent = "";
        i++;
        System.out.println("(" + i + ")" + f.getAbsolutePath());

        for (String line : Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset())) {
            textFileContent += line;
        }
        return textFileContent;
    }

//    private Analyzer getNGramAnalyser(Analyzer analyzer, int i) {
//        ShingleAnalyzerWrapper shingleAnalyzer = new ShingleAnalyzerWrapper(analyzer, i, i);
//        return shingleAnalyzer;
//    }
    /**
     * public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int
     * minShingleSize, int maxShingleSize, String tokenSeparator, boolean
     * outputUnigrams, boolean outputUnigramsIfNoShingles) Creates a new
     * ShingleAnalyzerWrapper Parameters: defaultAnalyzer - Analyzer whose
     * TokenStream is to be filtered minShingleSize - Min shingle (token ngram)
     * size maxShingleSize - Max shingle size tokenSeparator - Used to separate
     * input stream tokens in output shingles outputUnigrams - Whether or not
     * the filter shall pass the original tokens to the output stream
     * outputUnigramsIfNoShingles - Overrides the behavior of
     * outputUnigrams==false for those times when no shingles are available
     * (because there are fewer than minShingleSize tokens in the input stream)?
     * Note that if outputUnigrams==true, then unigrams are always output,
     * regardless of whether any shingles are available. Overwirte simple
     * Analyser - no stopword removal
     *
     */
    private Analyzer getNGramAnalyser(Analyzer analyzer, int i) {

        ShingleAnalyzerWrapper shingleAnalyzer = new ShingleAnalyzerWrapper(analyzer, i, i, ShingleFilter.DEFAULT_TOKEN_SEPARATOR, false, false, "#");

        return shingleAnalyzer;
        
    }
}
