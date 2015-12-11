package com.etosha.mailcollectiontool;

import com.etosha.mailcollectiontool.IndexerMailCollection;
import com.etosha.mailcollectiontool.ConfigurationMailCollection;
import com.computergodzilla.cosinesimilarity.AllTerms;
import com.computergodzilla.cosinesimilarity.CosineSimilarity;
import com.computergodzilla.cosinesimilarity.DocVector;
import com.computergodzilla.cosinesimilarity.Indexer;
import com.computergodzilla.cosinesimilarity.JensenShannonSimilarity;
import com.computergodzilla.cosinesimilarity.MapUtil;
import com.computergodzilla.cosinesimilarity.VectorGenerator;
import com.computergodzilla.cosinesimilarity.toolsJSD.vocabDist;
import com.computergodzilla.cosinesimilarity.toolsJSD.CorpusBasedUtilities;
import com.computergodzilla.cosinesimilarity.toolsJSD.EvalFeatures;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * A tool to analyse the QA-Index.
 *
 * We collect all QA-items. Reindexing both and loading all A-items separately
 * from all Q-items allows creation of two layers. Both are connected via a
 * static links if "anser belongs to question".
 *
 * @author MK
 */
public class QualifyMailCollection {
    
    // analysis-context 
    public static String experimentToRun = "mail-collection-01";
    public static String docFolder = "/ETOSHA.WS/tmp/" + experimentToRun;

    static File netFolder = null;

    static BufferedWriter brNODES = null;
    static BufferedWriter brLINKS = null;

    public static void main(String[] args) throws LockObtainFailedException, IOException {
        
        
        calcTwoLayerNetworksFromKB();
        
        
        /**
         * Here we can start to expose the NETWORK.
         * 
         */
        
        // DEFINE A Networklayer in the WIKI-METASTORE
        System.out.println( "* DEFINE A Networklayer in the WIKI-METASTORE" );
        
        
        // COPY DATA TO HDFS
        System.out.println( "* COPY DATA TO HDFS" );
        
        // DEFINE A Hive-Table and TRANSFORM into PARQUET FORMAT
        System.out.println( "* DEFINE A Hive-Table and TRANSFORM into PARQUET FORMAT" );
        
        
    }
    
    public static void calcTwoLayerNetworksFromKB() throws LockObtainFailedException, IOException {

        // is Index available?
        boolean indexExist = true;

        // analysis-context-store 
        String wikiname = "www.semanpix.de/oldtimer/wiki";

        // we do work with single terms only.
        IndexerMailCollection.useNgramsAsTerms = false;
        IndexerMailCollection.N = 1;

//        // we do work with n-grams.
//        IndexerMailCollection.useNgramsAsTerms = true;
//        IndexerMailCollection.N = 3;
        netFolder = new File("/ETOSHA.WS/networks/" + experimentToRun + "/JSD_" + experimentToRun + "_" + System.currentTimeMillis() + "_corpus_net");
        netFolder.mkdirs();

        brNODES = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/nodes.csv"));
        brLINKS = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/links.csv"));

        File folderTV = new File(netFolder.getAbsolutePath() + "/tv");
        folderTV.mkdirs();

        brLINKS.write("Source\tTarget\tWeight\tJSD\tCos2\tJSD2\tLinkLabel\tLinkCategory\n");

        brNODES.write("Id\tWeight\n");

        String LATEST = experimentToRun + "_snapshot_" + System.currentTimeMillis();
        
        String indexBasePath = "/ETOSHA.WS/INDEX/" + LATEST;
        File f = new File( indexBasePath );
        indexExist = f.exists();
        
/**
 * 
 * Describe the Index-Creation better before we go on ...
 * 
 * 
 */        
        // String indexBasePath = "/GITHUB.cloudera.internal/MorphMiner2/bundle/index";
        // source-directory = "./docs/" + experimentToRun + "/all"
        
        String sd = docFolder;
        
        if (!indexExist) {
            //      Indexer index = new Indexer( "./docs/"+experimentToRun+"/all", "./index/"+experimentToRun+"/indexFromHDFS2", "text" );
            IndexerMailCollection index = new IndexerMailCollection( sd, indexBasePath, ConfigurationMailCollection.FIELD_CONTENT_1, ConfigurationMailCollection.FIELD_CONTENT_2);
            System.out.println(">>> Index will be buit now ..." );
            index.index();
        }
        else {
            System.out.println("### Index is already available. (" + indexBasePath + ")" );
        }

        VectorGeneratorMailCollection vectorGenerator = new VectorGeneratorMailCollection();
        VectorGeneratorMailCollection.GetAllTerms();

/**
 * We can use subject-line + Metadata as one layer
 * 
 * Layer 2 is defined by the text of the mail.
 * 
 * 
 * Configuration.FIELD_CONTENT_1   => 
 * Configuration.FIELD_CONTENT_2   => 
 */        
        DocVector[] docVector1 = vectorGenerator.GetDocumentVectors(ConfigurationMailCollection.FIELD_CONTENT_1); // getting document vectors
//        DocVector[] docVector2 = vectorGenerator.GetDocumentVectors(ConfigurationMailCollection.FIELD_CONTENT_2); // getting document vectors

        System.out.println(">>> # of layer-1-items: " + docVector1.length);
//        System.out.println(">>> # of layer-2-items: " + docVector2.length);

        CorpusBasedUtilities cbu = new CorpusBasedUtilities();
  
        EvalFeatures ev = new EvalFeatures();

        processLayer(docVector1, "Layer1" );
        
//        processLayer(docVector2, "Layer2" );
        
//        vectorGenerator.listStaticLinksDocumentVectors();
        
        vectorGenerator.closeReader();

        brLINKS.close();
        brNODES.close();

    }

    private static String getLinkLabels(DocVector[] docVector, int i, int j) {
        return "(" + docVector[i].id + " # " + docVector[j].id + ") ";
    }

    private static void storeNode(DocVector[] docVector, int i) throws IOException {
        if ( docVector[i] != null )
            brNODES.write(docVector[i].id + "\t" + docVector[i].length + "\n");
        else 
            brNODES.write(i + "\t" + "no vector available" + "\n");
        
    }   

    public static void storeLink(DocVector[] docVector, int i, int j, double cosineSimilarity, double jsd, double c2, double j2, String layer) throws IOException {
        brLINKS.write(docVector[i].id + "\t" + docVector[j].id + "\t" + cosineSimilarity + "\t" + jsd + "\t" + c2 + "\t" + j2 + "\t" + i + ":" + j  + "\t" + layer + "\n");
    }
    
    public static void storeLink2(DocVector[] docVector, String i, String j, double cosineSimilarity, double jsd, double c2, double j2, String layer) throws IOException {
        String line = i + "\t" + j + "\t" + cosineSimilarity + "\n"; // + "\t" + jsd + "\t" + c2 + "\t" + j2 + "\t" + i + ":" + j  + "\t" + layer + "\n";
        if( !line.contains("\tNaN\t") )
            brLINKS.write( line );
    }

    static int dumpedVectors = 0;

    private static void dumpTermVector(DocVector v, File folderTV) throws IOException {

        dumpedVectors++;

        FileWriter fw = new FileWriter(folderTV + "/" + cleanForFileName(v.id) + "_termvector_sorted.csv");

        Map<String, Integer> terms = v.terms;
        Map<String, Double> termfreq = new HashMap<String, Double>();

        Set<String> keys = terms.keySet();
        for (String key : keys) {
            double count = v.vector2.getEntry(terms.get(key).intValue());
            if (count > 0) {
                // System.out.println( v.id + " : *** " + key + " => " + terms.get(key).intValue() + " ===> " + count );
                termfreq.put(key, count);
            }
        }

        termfreq = MapUtil.sortByValue(termfreq);

        StringBuffer sb = new StringBuffer();

        DecimalFormat df = new DecimalFormat("#,###,##0.000000");

        Set<String> keys2 = termfreq.keySet();

        Map<String, Double> sortedByTfIDF = new HashMap<String, Double>();

        double N = (double) AllTerms.totalNoOfDocumentInIndex;
        for (String key2 : keys2) {

            double tf = (double) termfreq.get(key2);

            double ni = (double) AllTerms.getAllTermCount(key2);

            double idf = Math.log(1.0 + (double) N / (double) ni);

            double tfidf = tf * idf;

            String line = v.id + "\t" + N + "\t" + ni + "\t" + tf + "\t" + df.format((1.0 + N / ni)) + "\t" + df.format(idf) + "\t" + key2 + "\t" + df.format(tfidf);
            // System.out.println(  line    );
            // sb.insert( 0, line + "\n" );

            sortedByTfIDF.put(line, tfidf);

        }

        sortedByTfIDF = MapUtil.sortByValue(sortedByTfIDF);

        for (String line : sortedByTfIDF.keySet()) {

            sb.insert(0, line + "\n");

        }

        String header = "docId\tN\tni\ttf\t(1+N/ni)\tidf\tword\ttfidf\n";
        fw.write(header);
        fw.write(sb.toString());
        fw.close();

        System.out.println("(" + dumpedVectors + ") -------");
        System.out.println(header);
        System.out.println(sb.toString());
    }

    private static String cleanForFileName(String id) {
        try {
            id = DatatypeConverter.printBase64Binary(id.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            id = id.replaceAll("/", "_");
            Logger.getLogger(QualifyMailCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    private static void processLayer(DocVector[] docVector1, String layer) throws IOException {

        for (int i = 0; i < docVector1.length; i++) {

            storeNode(docVector1, i);

            // dumpTermVector(docVector1[i], folderTV);
            
            //vocabDist vdA = cbu.computeVocabulary(docVector1[i].id);
            for (int j = 0; j < docVector1.length; j++) {

                // vocabDist vdB = cbu.computeVocabulary(docVector1[j].id);
                if (i == j) {

                } else {

                    double cosineSimilarity = CosineSimilarity.CosineSimilarity(docVector1[j], docVector1[i]);

                    double jsd = -2.0;
                    double jsd2 = -2.0;
                    double coss2 = -2.0;

                    JensenShannonSimilarity simil;

                    try {

                        simil = new JensenShannonSimilarity(docVector1[j], docVector1[i]);
                        jsd = simil.jsd;

//                        coss2 = cbu.computeTfCosineGivenVocabDists(vdA, vdB);
//                        jsd2 = ev.getJSDivergence(vdA, vdB);
                    } 
                    catch (Exception ex) {
                        Logger.getLogger(QualifyMailCollection.class.getName()).log(Level.SEVERE, null, ex);
                        jsd = -5.0;
                    }

                    try {
                        
                        
                        System.out.println(i + " " + j + " " + getLinkLabels(docVector1, i, j) + "CosSim=" + cosineSimilarity + " JensenShannonSim=" + jsd + " CosSim2=" + coss2 + " JensenShannonSim2=" + jsd2);

                    
                    }
                    catch(Exception ex) {
                        
                    }
                    
//                    if ( cosineSimilarity > 0.3 )
                    storeLink2(docVector1, ""+i, ""+j, cosineSimilarity, jsd, coss2, jsd2, layer);
//                    storeLink2(docVector1, layer+":"+i, layer+":"+j, cosineSimilarity, jsd, coss2, jsd2, layer);
                }

            }
        }
    }
}
