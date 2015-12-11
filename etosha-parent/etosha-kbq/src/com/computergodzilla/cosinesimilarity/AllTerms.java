/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computergodzilla.cosinesimilarity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 * Class that will get all the terms in the index.
 * @author Mubin Shrestha
 */
public class AllTerms {

  
    private Map<String,Integer> allTerms;
    
    public static Hashtable<String,Integer> _allTermsCount = null; 
    
    // DOC COUNT
    static Hashtable<String,Integer> _allTermsInDocCount = new Hashtable<String,Integer>();
            
    public static Integer totalNoOfDocumentInIndex;
    IndexReader indexReader;
    
    public AllTerms(File f) throws IOException
    {    
        allTerms = new HashMap<>();
        indexReader = IndexOpener.GetIndexReader(f);
        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex(f);
        _allTermsCount = new Hashtable<String,Integer>(); 
    }
        
    public void initAllTerms( String FIELD ) throws IOException
    {
        int pos = 0;
        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
            System.out.println(">>> lookup for VECTOR: " + FIELD );
            Terms vector = indexReader.getTermVector(docId, FIELD);
            
            if ( vector != null ) {
                TermsEnum termsEnum = null;
                termsEnum = vector.iterator(termsEnum);
                BytesRef text = null;
                while ((text = termsEnum.next()) != null) {
                    String term = text.utf8ToString();
                    allTerms.put(term, pos++);

                    // DOC COUNT
                    if ( !_allTermsCount.containsKey(term) ) {
                        _allTermsCount.put(term, 1 );
                        _allTermsInDocCount.put(term, 1 );
                    }
                    else {
                        int z = _allTermsCount.get(term);
                        z = z + 1;
                        _allTermsCount.put(term, z);
                    }

                }
            }
            else{
                System.out.println( ">>> NO TERMVECTOR in docID: " + docId );
            }
     
        }       
        
        //Update postition
        pos = 0;
        for(Entry<String,Integer> s : allTerms.entrySet())
        {        
            //System.out.println("# " + s.getKey() + " => " + _allTermsCount.get( s.getKey() ) );
            s.setValue(pos++);
        }
    }
    
    public Map<String, Integer> getAllTerms() {
        return allTerms;
    }
    
    public static int getAllTermCount( String k ) {
        return _allTermsCount.get( k );
    }

    public static int getTermInDocsCount( String k ) {
        return _allTermsInDocCount.get( k );
    }
    
}
