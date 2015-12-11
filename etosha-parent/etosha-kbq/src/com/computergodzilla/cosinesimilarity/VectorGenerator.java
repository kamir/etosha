package com.computergodzilla.cosinesimilarity;

import com.etosha.QualifyKnowledgeBase;
import com.etosha.mailcollectiontool.ConfigurationMailCollection;
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
public class VectorGenerator {
    

    DocVector[] docVector1;
    DocVector[] docVector2;
 
    private Map allterms1;
    private Map allterms2;

    Integer totalNoOfDocumentInIndex;
    IndexReader indexReader;
    
    File indexFile = null;
    
    public VectorGenerator() throws IOException
    {
        
        allterms1 = new HashMap<>();
        allterms2 = new HashMap<>();
        
        File f = new File(Configuration.INDEX_DIRECTORY);
        indexFile = f;
        
        indexReader = IndexOpener.GetIndexReader(f);
        
        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex(f);

        docVector1 = new DocVector[totalNoOfDocumentInIndex];
        docVector2 = new DocVector[totalNoOfDocumentInIndex];
        
        System.out.println( "totalNoOfDocumentInIndex=" + totalNoOfDocumentInIndex );
        
    }
    
    public void GetAllTerms() throws IOException
    {
        AllTerms allTerms1 = new AllTerms( indexFile );
        allTerms1.initAllTerms( Configuration.FIELD_CONTENT_1 );
        allterms1 = allTerms1.getAllTerms();
        
        AllTerms allTerms2 = new AllTerms( indexFile );
        allTerms2.initAllTerms(  Configuration.FIELD_CONTENT_2 );
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
            QualifyKnowledgeBase.storeLink2(docVector1, "Q:"+id, "A:"+id, 1.0, 1.0, 1.0, 1.0, "QA");
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
            Logger.getLogger(VectorGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
