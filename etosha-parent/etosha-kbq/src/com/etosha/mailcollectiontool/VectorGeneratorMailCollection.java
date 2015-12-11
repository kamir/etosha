package com.etosha.mailcollectiontool;

import com.computergodzilla.cosinesimilarity.*;
import com.etosha.QualifyKnowledgeBase;
import com.etosha.QualifyKnowledgeBase;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 * Class to generate Document Vectors from Lucene Index
 * @author Mubin Shrestha
 */
public class VectorGeneratorMailCollection {
    

    DocVector[] docVector1;
    DocVector[] docVector2;
 
    private static Map allterms1;
    private static Map allterms2;

    Integer totalNoOfDocumentInIndex;
    IndexReader indexReader;
    
    static File indexFile = null;
    
    public VectorGeneratorMailCollection() throws IOException
    {
        allterms1 = new HashMap<>();
        allterms2 = new HashMap<>();
        
        File f = new File(ConfigurationMailCollection.INDEX_DIRECTORY);
        indexFile = f;
        
        indexReader = IndexOpener.GetIndexReader( f );
        
        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex( f );

        docVector1 = new DocVector[totalNoOfDocumentInIndex];
        docVector2 = new DocVector[totalNoOfDocumentInIndex];
        
        System.out.println( "totalNoOfDocumentInIndex=" + totalNoOfDocumentInIndex );
        
    }
    
    public static void GetAllTerms() throws IOException
    {
        AllTerms allTerms1 = new AllTerms( indexFile );
        allTerms1.initAllTerms( ConfigurationMailCollection.FIELD_CONTENT_1 );
        allterms1 = allTerms1.getAllTerms();
        
        AllTerms allTerms2 = new AllTerms( indexFile );
        allTerms2.initAllTerms(  ConfigurationMailCollection.FIELD_CONTENT_2 );
        allterms2 = allTerms2.getAllTerms();
    }
    
    /**
     * @TODO:
     * Depending on fields or availability of tags we can define a different link strength here.
     * 
     * @throws IOException 
     */
    public void listStaticLinksDocumentVectors() throws IOException {
        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
            String id = indexReader.document(docId).getField("id").stringValue();
            
            QualifyMailCollection.storeLink2(docVector1, "L1:"+id, "L2:"+id, 1.0, 1.0, 1.0, 1.0, "L1->L2");
        }        
        
    }

    
    public DocVector[] GetDocumentVectors( String FIELD ) throws IOException {
        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
            
            String id = indexReader.document(docId).getField("id").stringValue();
//            String context = indexReader.document(docId).getField("context").stringValue();

            Terms vector1 = indexReader.getTermVector( docId, FIELD );
            if ( vector1 != null ) {
                TermsEnum termsEnum1 = null;
                termsEnum1 = vector1.iterator(termsEnum1);
                BytesRef text1 = null;            
                docVector1[docId] = new DocVector(allterms1);  

                while ((text1 = termsEnum1.next()) != null) {
                    String term = text1.utf8ToString();
                    int freq = (int) termsEnum1.totalTermFreq();
                    docVector1[docId].setEntry(term, freq);
                }

                docVector1[docId].normalize();
                docVector1[docId].id = id;
                //docVector1[docId].context = context;
                docVector1[docId].docId = docId;
                docVector1[docId].length = vector1.size();
            }
            
        }        
//        indexReader.close();
        return docVector1;
    }

    public void closeReader() {
        try {
            indexReader.close();
        } catch (IOException ex) {
            Logger.getLogger(VectorGeneratorMailCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}