package com.computergodzilla.cosinesimilarity;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * Main Class
 * @author Mubin Shrestha
 */
public class Test1 {
    
    static File netFolder = null;
  
    static BufferedWriter brNODES = null;
    static BufferedWriter brLINKS = null;

    public static void main(String[] args) throws LockObtainFailedException, IOException
    {
        
        Indexer.useNgramsAsTerms = false;
        
        getCosineSimilarity();
    }
    
    public static void getCosineSimilarity() throws LockObtainFailedException, IOException
    {
        
       netFolder = new File( "/Users/kamir/Desktop/" + System.currentTimeMillis() + "_corpus_net" );
       netFolder.mkdirs();

       brNODES = new BufferedWriter( new FileWriter( netFolder.getAbsolutePath() + "/nodes.csv"));
       brLINKS = new BufferedWriter( new FileWriter( netFolder.getAbsolutePath() + "/links.csv"));
       
       brLINKS.write( "Source\tTarget\tLabel\tWeight\n" );
       brNODES.write( "Id\tWeight\n" );

       Indexer index = new Indexer();
       index.index();
       VectorGenerator vectorGenerator = new VectorGenerator();
       vectorGenerator.GetAllTerms();       
       
       DocVector[] docVector = vectorGenerator.GetDocumentVectors(Configuration.FIELD_CONTENT_1); // getting document vectors
       for(int i = 0; i < docVector.length; i++) {
           
           storeNode( docVector, i );
       
           for(int j = 0; j < docVector.length; j++) {
                    double cosineSimilarity = CosineSimilarity.CosineSimilarity(docVector[j], docVector[i]);
                    
                    System.out.println( i + " " + j + " " + getLinkLabels(docVector,i,j) + "CosSim=" + cosineSimilarity);
                    storeLink( docVector, i, j, cosineSimilarity);

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

    private static void storeLink(DocVector[] docVector, int i, int j, double cosineSimilarity) throws IOException {
        brLINKS.write( docVector[i].id + "\t"+docVector[j].id + "\t" + i+":"+j + "\t" + cosineSimilarity + "\n" );
    }
}