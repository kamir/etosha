package com.etosha;

import com.computergodzilla.cosinesimilarity.AllTerms;
import com.computergodzilla.cosinesimilarity.Configuration;
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
public class QualifyKnowledgeBase {

    static File netFolder = null;

    static BufferedWriter brNODES = null;
    static BufferedWriter brLINKS = null;

    public static void main(String[] args) throws LockObtainFailedException, IOException {
        calcTwoLayerNetworksFromKB();
    }

    public static void calcTwoLayerNetworksFromKB() throws LockObtainFailedException, IOException {

        // analysis-context 
        String experimentToRun = "kb-index-07-2015";

        // is Index available?
        boolean indexExist = true;

        // analysis-context-store 
        String wikiname = "www.semanpix.de/oldtimer/wiki";

        // we do work with single terms only.
        Indexer.useNgramsAsTerms = false;
        Indexer.N = 1;

//        // we do work with n-grams.
//        Indexer.useNgramsAsTerms = true;
//        Indexer.N = 3;
        netFolder = new File("./networks/" + experimentToRun + "/JSD_" + experimentToRun + "_" + System.currentTimeMillis() + "_corpus_net");
        netFolder.mkdirs();

        brNODES = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/nodes.csv"));
        brLINKS = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/links.csv"));

        File folderTV = new File(netFolder.getAbsolutePath() + "/tv");
        folderTV.mkdirs();

        brLINKS.write("Source\tTarget\tWeight\tJSD\tCos2\tJSD2\tLinkLabel\tLinkCategory\n");

        brNODES.write("Id\tWeight\n");

        String LATEST = "snapshot.20150714185632159";
        
        String indexBasePath = "/GITHUB.cloudera.internal/TTFAQ/data/shard1/" + LATEST;
        // String indexBasePath = "/GITHUB.cloudera.internal/MorphMiner2/bundle/index";
        Indexer index = new Indexer("./docs/" + experimentToRun + "/all", indexBasePath, "question", "answer");
        if (!indexExist) {
            //      Indexer index = new Indexer( "./docs/"+experimentToRun+"/all", "./index/"+experimentToRun+"/indexFromHDFS2", "text" );
            index.index();
        }

        VectorGenerator vectorGenerator = new VectorGenerator();
        vectorGenerator.GetAllTerms();

        DocVector[] docVector1 = vectorGenerator.GetDocumentVectors(Configuration.FIELD_CONTENT_1); // getting document vectors
        DocVector[] docVector2 = vectorGenerator.GetDocumentVectors(Configuration.FIELD_CONTENT_2); // getting document vectors

        

        System.out.println(">>> # of docs 1: " + docVector1.length);
        System.out.println(">>> # of docs 2: " + docVector2.length);

        CorpusBasedUtilities cbu = new CorpusBasedUtilities();
        EvalFeatures ev = new EvalFeatures();

        processLayer(docVector1, "Q" );
        processLayer(docVector2, "A" );
        vectorGenerator.listStaticLinksDocumentVectors();
        
        vectorGenerator.closeReader();

        brLINKS.close();
        brNODES.close();

    }

    private static String getLinkLabels(DocVector[] docVector, int i, int j) {
        return "(" + docVector[i].id + " # " + docVector[j].id + ") ";
    }

    private static void storeNode(DocVector[] docVector, int i) throws IOException {
        brNODES.write(docVector[i].id + "\t" + docVector[i].length + "\n");
    }

    public static void storeLink(DocVector[] docVector, int i, int j, double cosineSimilarity, double jsd, double c2, double j2, String layer) throws IOException {
        brLINKS.write(docVector[i].id + "\t" + docVector[j].id + "\t" + cosineSimilarity + "\t" + jsd + "\t" + c2 + "\t" + j2 + "\t" + i + ":" + j  + "\t" + layer + "\n");
    }
    
    public static void storeLink2(DocVector[] docVector, String i, String j, double cosineSimilarity, double jsd, double c2, double j2, String layer) throws IOException {
        String line = i + "\t" + j + "\t" + cosineSimilarity + "\t" + jsd + "\t" + c2 + "\t" + j2 + "\t" + i + ":" + j  + "\t" + layer + "\n";
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
            Logger.getLogger(QualifyKnowledgeBase.class.getName()).log(Level.SEVERE, null, ex);
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
                    } catch (Exception ex) {
                        Logger.getLogger(QualifyKnowledgeBase.class.getName()).log(Level.SEVERE, null, ex);
                        jsd = -2.0;
                    }

                    System.out.println(i + " " + j + " " + getLinkLabels(docVector1, i, j) + "CosSim=" + cosineSimilarity + " JensenShannonSim=" + jsd + " CosSim2=" + coss2 + " JensenShannonSim2=" + jsd2);

//                    if ( cosineSimilarity > 0.3 )
                    storeLink2(docVector1, layer+":"+i, layer+":"+j, cosineSimilarity, jsd, coss2, jsd2, layer);
                }

            }
        }
    }
}
