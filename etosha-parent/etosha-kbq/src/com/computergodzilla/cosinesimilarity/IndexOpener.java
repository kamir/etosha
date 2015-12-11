package com.computergodzilla.cosinesimilarity;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 * Class to Get the Lucene Index Reader
 * @author Mubin Shrestha
 */
public class IndexOpener {
    
    public static IndexReader GetIndexReader(  File f) throws IOException {
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(f));
        return indexReader;
    }

    /**
     * Returns the total number of documents in the index
     * @return
     * @throws IOException 
     */
    public static Integer TotalDocumentInIndex(File f) throws IOException
    {
        Integer maxDoc = GetIndexReader(f).maxDoc();
        GetIndexReader(f).close();
        return maxDoc;
    }
}