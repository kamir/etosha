package com.computergodzilla.cosinesimilarity;

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
 * analyse the FAQ Index
 *
 * @author MK
 */
public class Test20 {

    static File netFolder = null;

    static BufferedWriter brNODES = null;
    static BufferedWriter brLINKS = null;

    public static void main(String[] args) throws LockObtainFailedException, IOException {
        getCosineSimilarity();
    }

    public static void getCosineSimilarity() throws LockObtainFailedException, IOException {

        String experimentToRun = "exp21";
        Indexer.useNgramsAsTerms = false;
        Indexer.N = 1;

        netFolder = new File("./networks/JSD_" + experimentToRun + "_" + System.currentTimeMillis() + "_corpus_net");
        netFolder.mkdirs();

        brNODES = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/nodes.csv"));
        brLINKS = new BufferedWriter(new FileWriter(netFolder.getAbsolutePath() + "/links.csv"));

        File folderTV = new File(netFolder.getAbsolutePath() + "/tv");
        folderTV.mkdirs();

        // brLINKS.write( "Source\tTarget\tCosSIM\tJSD\tLabel\n" );
        brLINKS.write("Source\tTarget\tWeight\tJSD\tCos2\tJSD2\tLabel\n");
        brNODES.write("Id\tWeight\n");

        //      Indexer index = new Indexer( "./docs/"+experimentToRun+"/all", "./index/"+experimentToRun+"/indexFromHDFS2", "text" );
        Indexer index = new Indexer("./docs/" + experimentToRun + "/all", "./index/" + experimentToRun + "/all", "text", "answer");
        index.index();

        VectorGenerator vectorGenerator = new VectorGenerator();
        vectorGenerator.GetAllTerms();

        DocVector[] docVector = vectorGenerator.GetDocumentVectors(Configuration.FIELD_CONTENT_1); // getting document vectors

        System.out.println(">>> # of docs: " + docVector.length);

        CorpusBasedUtilities cbu = new CorpusBasedUtilities();
        EvalFeatures ev = new EvalFeatures();

        for (int i = 0; i < docVector.length; i++) {

            storeNode(docVector, i);

            dumpTermVector(docVector[i], folderTV);

            vocabDist vdA = cbu.computeVocabulary(docVector[i].id);

            for (int j = 0; j < docVector.length; j++) {

                vocabDist vdB = cbu.computeVocabulary(docVector[j].id);

                if (i == j) {

                } else {

                    double cosineSimilarity = CosineSimilarity.CosineSimilarity(docVector[j], docVector[i]);

                    double jsd = -2.0;
                    double jsd2 = -2.0;
                    double coss2 = -2.0;

                    JensenShannonSimilarity simil;

                    try {

                        simil = new JensenShannonSimilarity(docVector[j], docVector[i]);
                        jsd = simil.jsd;

                        coss2 = cbu.computeTfCosineGivenVocabDists(vdA, vdB);
                        jsd2 = ev.getJSDivergence(vdA, vdB);

                    } catch (Exception ex) {
                        Logger.getLogger(Test20.class.getName()).log(Level.SEVERE, null, ex);
                        jsd = -2.0;
                    }

                    System.out.println(i + " " + j + " " + getLinkLabels(docVector, i, j) + "CosSim=" + cosineSimilarity + " JensenShannonSim=" + jsd + " CosSim2=" + coss2 + " JensenShannonSim2=" + jsd2);

//                    if ( cosineSimilarity > 0.3 )
                    storeLink(docVector, i, j, cosineSimilarity, jsd, coss2, jsd2);
                }

            }
        }

        brLINKS.close();
        brNODES.close();

    }

    private static String getLinkLabels(DocVector[] docVector, int i, int j) {
        return "(" + docVector[i].id + " # " + docVector[j].id + ") ";
    }

    private static void storeNode(DocVector[] docVector, int i) throws IOException {
        brNODES.write(docVector[i].id + "\t" + docVector[i].length + "\n");
    }

    private static void storeLink(DocVector[] docVector, int i, int j, double cosineSimilarity, double jsd, double c2, double j2) throws IOException {
        brLINKS.write(docVector[i].id + "\t" + docVector[j].id + "\t" + cosineSimilarity + "\t" + jsd + "\t" + c2 + "\t" + j2 + "\t" + i + ":" + j + "\n");
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
            Logger.getLogger(Test20.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
}
