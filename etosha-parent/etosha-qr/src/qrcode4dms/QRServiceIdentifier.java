/*
 * Ein WebService wird mittels QR-Code konfiguriert, wobei 
 * auf einer URL ein XML-File oder eine RDF-Ressoruce 
 * abgerufen wird.
 * 
 * a) einfacher Webserver
 * b) Refinder (Semantic-Web)
 * c) CFG-WIKI with templates
 * 
 * Der Service Identifier wird auf eine URL und damit auf eine konkrete 
 * Beschreibung eines Web-Services zeigen.
 * 
 * Damit lässt sich dynamisch eine "Client-Implementierung" laden und mit
 * den passenden Daten konfigurieren, sodass auf den Service zugegriffen 
 * werden kann. 
 * 
 * Die App sendet dazu ein "WebIntent" an den "WebIntentService", der 
 * daraufhin die Bibliotheken für den Client-Adapter bereitstellet. 
 * 
 * Mittels Zertifikat wird die Sicherheit der Implementierung und die
 * Laufzeit / Gültigkeit selbiger geregelt.
 */
package qrcode4dms;

/**
 *
 * @author kamir
 */
public class QRServiceIdentifier {
    
    public static int CFG_RECORD_TYPE_XML = 0;
    public static int CFG_RECORD_TYPE_RDF = 1;
    public static int CFG_RECORD_TYPE_WIKI = 2;

    public static int CFG_RECORD_TYPE = 0;
    
    
    
}
