package com.computergodzilla.cosinesimilarity;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * Create an 3-Gram Index
 * 
 * @author MK
 */
public class Test3 {
    
    static File netFolder = null;
  
    static BufferedWriter brNODES = null;
    static BufferedWriter brLINKS = null;

    public static void main(String[] args) throws LockObtainFailedException, IOException
    {
        getCosineSimilarity();
    }
    
    public static void getCosineSimilarity() throws LockObtainFailedException, IOException
    {
        
       String experimentToRun = "exp4";
       Indexer.useNgramsAsTerms = false;
       Indexer.N = 1;
              
       netFolder = new File( "/Users/kamir/Desktop/TEMP/" + experimentToRun + "_" + System.currentTimeMillis() + "_corpus_net" );
       netFolder.mkdirs();

       brNODES = new BufferedWriter( new FileWriter( netFolder.getAbsolutePath() + "/nodes.csv"));
       brLINKS = new BufferedWriter( new FileWriter( netFolder.getAbsolutePath() + "/links.csv"));
       
       File folderTV = new File( netFolder.getAbsolutePath() + "/tv" );
       folderTV.mkdirs();
       
       brLINKS.write( "Source\tTarget\tCosSIM\tJSD\tLabel\n" );
       brNODES.write( "Id\tWeight\n" );
       
 //      Indexer index = new Indexer( "./docs/"+experimentToRun+"/all", "./index/"+experimentToRun+"/indexFromHDFS2", "text" );
       Indexer index = new Indexer( "./docs/"+experimentToRun+"/all", "./index/"+experimentToRun+"/n_"+Indexer.N+"_GramIndex", "text", "answer" );
       index.index();
       
       VectorGenerator vectorGenerator = new VectorGenerator();
       vectorGenerator.GetAllTerms();       
       
       DocVector[] docVector = vectorGenerator.GetDocumentVectors(Configuration.FIELD_CONTENT_1); // getting document vectors
       for(int i = 0; i < docVector.length; i++) {
           
           storeNode( docVector, i );
           dumpTermVector( docVector[i] , folderTV );
       
           for(int j = 0; j < docVector.length; j++) {
               
                  if ( i == j ) {
                  
                  }
                  else {
                      
                    double cosineSimilarity = CosineSimilarity.CosineSimilarity(docVector[j], docVector[i]);
               
                    double jsd = -2.0;
                    JensenShannonSimilarity simil;
                      try {
                          simil = new JensenShannonSimilarity(docVector[j], docVector[i]);
                          jsd = simil.jsd;
                      } 
                      catch (Exception ex) {
                          Logger.getLogger(Test3.class.getName()).log(Level.SEVERE, null, ex);
                          jsd = -2.0;
                      }
                    
                    
                    System.out.println( i + " " + j + " " + getLinkLabels(docVector,i,j) + "CosSim=" + cosineSimilarity + " JensenShannonSim=" + jsd);
                    storeLink( docVector, i, j, cosineSimilarity, jsd);
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
        brNODES.write( docVector[i].id + "\t" + docVector[i].length + "\n" );
    }

    private static void storeLink(DocVector[] docVector, int i, int j, double cosineSimilarity, double jsd) throws IOException {
        brLINKS.write( docVector[i].id + "\t"+docVector[j].id + "\t" + cosineSimilarity + "\t" + jsd + "\t" + i+":"+j + "\n" );
    }

    static int dumpedVectors = 0;
    private static void dumpTermVector(DocVector v, File folderTV) throws IOException {

        dumpedVectors++;
        
        FileWriter fw = new FileWriter( folderTV + "/" + v.id + "_termvector_sorted.csv" );
        
        Map<String, Integer> terms = v.terms;
        Map<String, Double> termfreq = new HashMap<String,Double>();
 
        Set<String> keys = terms.keySet();
        for( String key : keys ) {
            double count = v.vector2.getEntry( terms.get(key).intValue() );
            if ( count > 0 ) {
                // System.out.println( v.id + " : *** " + key + " => " + terms.get(key).intValue() + " ===> " + count );
                termfreq.put(key, count);
            }
        }    
        
        termfreq = MapUtil.sortByValue( termfreq );
        
        StringBuffer sb = new StringBuffer();
        
        DecimalFormat df = new DecimalFormat("#,###,##0.000000");
        
        Set<String> keys2 = termfreq.keySet();

        Map<String,Double> sortedByTfIDF = new HashMap<String,Double>();
        
        double N = (double)AllTerms.totalNoOfDocumentInIndex;
        for( String key2 : keys2 ) {
                      
            double tf = (double)termfreq.get(key2);
            
            double ni = (double)AllTerms.getAllTermCount(key2);
            
            double idf = Math.log( 1.0 + (double)N / (double)ni );
            
            double tfidf = tf * idf;
         
            String line = v.id + "\t" + N + "\t" + ni + "\t" + tf + "\t" + df.format( (1.0 + N/ni) ) + "\t" + df.format(idf) + "\t" + key2 + "\t" + df.format( tfidf );
            // System.out.println(  line    );
            // sb.insert( 0, line + "\n" );
            
            sortedByTfIDF.put( line, tfidf);
            
        }
        
        sortedByTfIDF = MapUtil.sortByValue( sortedByTfIDF );
        
        for( String line : sortedByTfIDF.keySet() ) {
            
            sb.insert( 0, line + "\n" );
            
        }
        
        String header = "docId\tN\tni\ttf\t(1+N/ni)\tidf\tword\ttfidf\n";
        fw.write( header );
        fw.write( sb.toString()  );
        fw.close();
        
        System.out.println( "(" + dumpedVectors + ") -------" );
        System.out.println(header);
        System.out.println(sb.toString());
    }
}
 