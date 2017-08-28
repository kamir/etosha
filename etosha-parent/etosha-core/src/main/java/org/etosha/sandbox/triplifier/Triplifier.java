package org.etosha.sandbox.triplifier;


import org.apache.jena.rdf.model.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

/**
 * How is a Triplifier working?
 * 
 * We produce multiple SPOs from some input. 
 * 
 * One simple assumption is, that a SOLR query gives a context or the querie
 * is at least related to a context. 
 * 
 * Therefore we define a context by a context-identifier and a query by a 
 * query identifier. 
 * 
 * The query links the set of entitis which are found by it to this context. 
 * So we use the "Query-In-Context-ID" to define a named graph,
 * which conatins all the elements received by the query.
 * 
 * Each element is now triplified based on a atrribute => PREDICATE table.
 * 
 * EXAMPLE:
 * 
 * "docs":[
        {
         "id":"MA147LL/A",
         "sku":"MA147LL/A",
         "name":"Apple 60 GB iPod with Video Playback Black",
         "manu":"Apple Computer Inc.",
         "includes":"earbud headphones, USB cable",
         "weight":5.5,
         "price":399.0,
         "popularity":10,
         "inStock":true,
         "timestamp":"2007-01-31T05:12:44.562Z",
         "cat":[
          "electronics",
          "music"],
         "features":[
          "iTunes, Podcasts, Audiobooks",
          "Stores up to 15,000 songs, 25,000 photos, or 150 hours of video",
          "2.5-inch, 320x240 color TFT LCD display with LED backlight",
          "Up to 20 hours of battery life",
          "Plays AAC, MP3, WAV, AIFF, Audible, Apple Lossless, H.264 video",
          "Notes, Calendar, Phone book, Hold button, ... "]},
          
    SUBJECT:
    * 
    * We use the id attribute as the subject.
    * It is of type "literal".
    * 
    * Now we model "sku" as property: hasSKU and write:
    * 
    >>> "MA147LL/A" hasSKU "MA147LL/A .
    
    ==> All the single field attributes can be processed like this.

       We maintain an attribute PROPERTY table to represent the relations.
       If a relation does not exist in our ontology we must built it maually.
    
    Multivalue fields are inspected recursively.
    
     
     
    
 *
 * @author kamir
 */
class Triplifier {

    static void document2triples(Model m, Document d) {

        // define ID
        Resource s = m.createResource( "MySOLRDocument" );
        Property pID = m.createProperty( "hasDocID" );
        
        String id = d.get("id");
        if ( id == null ) id = System.currentTimeMillis() + "";
        
        m.add( s, pID, id );

        
        // iterate on all fields
        for( IndexableField f : d.getFields() ) {
 
           String value = f.toString();
           Resource o = m.createResource( value );
  
           String attributeName = f.name();
           
           // exclude ID
           if ( ! attributeName.equals( "id") ) {
               Property p = AttributePredicateMapper.getPropertyForSOLRAttribute( attributeName, m );
               m.add( s, p, o );
           
               // Iterate on multi-value fields
               System.out.println("MISSING FEATURE: Iterate on multi-value fields");
           }
           
        }
            
    }
    
}
