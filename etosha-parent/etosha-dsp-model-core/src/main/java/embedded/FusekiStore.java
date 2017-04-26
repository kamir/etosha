package embedded;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.jena.atlas.web.ContentType;
import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.fuseki.embedded.FusekiEmbeddedServer;
import org.apache.jena.fuseki.server.DataService;
import org.apache.jena.fuseki.server.OperationName;
import org.apache.jena.graph.Graph;
import org.apache.jena.query.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.sparql.sse.SSE;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import java.io.*;
import java.util.function.Consumer;



/**
 * Created by kamir on 26.04.17.
 */
public class FusekiStore {

    /*package*/
    void query(String URL, String query, Consumer<QueryExecution> body) {
        try (QueryExecution qExec = QueryExecutionFactory.sparqlService(URL, query) ) {
            body.accept(qExec);
        }
    }

    /** Create an HttpEntity for the graph */
    protected HttpEntity graphToHttpEntity(final Graph graph) {
        final RDFFormat syntax = RDFFormat.TURTLE_BLOCKS ;
        ContentProducer producer = new ContentProducer() {
            @Override
            public void writeTo(OutputStream out) {
                RDFDataMgr.write(out, graph, syntax) ;
            }
        } ;
        EntityTemplate entity = new EntityTemplate(producer) ;
        ContentType ct = syntax.getLang().getContentType() ;
        entity.setContentType(ct.getContentType()) ;
        return entity ;
    }

    /*package*/ DatasetGraph dataset() {
        return DatasetGraphFactory.createTxnMem() ;
    }


    public static void main( String[] args ) throws Exception {

        FusekiStore store1 = new FusekiStore();
        FusekiStore store2 = new FusekiStore();

        store1.init( "./s1", "data", 3031, "/GITHUB/FusekiCloud/fuseki-parcel/___WORKINGDATA/fuseki/scripts/main/data/model.ttl" );

        store2.init( "./s2", "/GITHUB/FusekiCloud/fuseki-parcel/___WORKINGDATA/fuseki/scripts/main/data/model.ttl" );
        store2.addContent( store2.getContent("/GITHUB/FusekiCloud/fuseki-parcel/___WORKINGDATA/fuseki/scripts/main/data/partitions/model.ttl") );

    }


    public  void init( String fn, String initialFile) throws Exception {


        init( fn,  this.NAME, this.port, initialFile );

    }

    public  void init( String fn, String NAME, int port, String initialFile) throws Exception {

        this.port = port;

        this.NAME = NAME;


        createIfFolderNotExist( fn );

        DatasetGraph dsg = dataset() ;

//        Txn.executeWrite(dsg,  ()->{
//            Quad q = SSE.parseQuad("(_ :s :p _:b)") ;
//            dsg.add(q);
//        }) ;

        // A service with just being able to do quads operations
        // That is, GET, POST, PUT on  "/data" in N-quads and TriG.
        DataService dataService = new DataService(dsg) ;

        dataService.addEndpoint(OperationName.Quads_RW, "");
        dataService.addEndpoint(OperationName.Query, "");
        dataService.addEndpoint(OperationName.Update, "");
        dataService.addEndpoint(OperationName.Upload, "");

        FusekiEmbeddedServer server = FusekiEmbeddedServer.create()
                .setPort(port)
                .add( "/" + NAME , dataService).enableStats(true)
                .build() ;
        server.start() ;
        try {
            // Put data in.
            String data = "(graph (:s :p 1) (:s :p 2) (:s :p 3))" ;
            Graph g = SSE.parseGraph(data) ;
            HttpEntity e = graphToHttpEntity(g) ;
            HttpOp.execHttpPut("http://localhost:"+port+"/" + NAME, e) ;


            // Get data out.
            try ( TypedInputStream in = HttpOp.execHttpGet("http://localhost:"+port+"/" + NAME) ) {
                Graph g2 = GraphFactory.createDefaultGraph() ;
                RDFDataMgr.read(g2, in, RDFLanguages.contentTypeToLang(in.getContentType())) ;

                System.err.println( "Isomorphic=" + g2.isIsomorphicWith(g2) ) ;
            }



            // Query.
            query("http://localhost:"+port+"/" + NAME, "SELECT * { ?s ?p ?o}", qExec->{
                ResultSet rs = qExec.execSelect() ;

                while(rs.hasNext() ){
                    QuerySolution qs = rs.next();
                        System.err.println( qs.toString() );
                }


                int x = ResultSetFormatter.consume(rs) ;
                System.err.println(("\n\n\n\n\n3=" + x )) ;
            }) ;


            addContent( getContent( initialFile ) , port, NAME );

        }
        finally {
           // server.stop() ;
        }




        // Get data out.
        try ( TypedInputStream in = HttpOp.execHttpGet("http://localhost:"+port+"/" + NAME) ) {
            Graph g2 = GraphFactory.createDefaultGraph() ;
            RDFDataMgr.read(g2, in, RDFLanguages.contentTypeToLang(in.getContentType())) ;

//           FileOutputStream foutstr = new FileOutputStream("RDF_dump.csv");
//           RDFDataMgr.write( foutstr , g2, Lang.CSV );

            FileOutputStream foutstr2 = new FileOutputStream(fn + File.separator + "RDF_dump_JSONLD.json");
            RDFDataMgr.write( foutstr2 , g2, Lang.JSONLD );

            FileOutputStream foutstr3 = new FileOutputStream(fn + File.separator + "RDF_dump.ttl");
            RDFDataMgr.write( foutstr3 , g2, Lang.TTL );

            FileOutputStream foutstr4 = new FileOutputStream(fn + File.separator + "RDF_dump_.nguads");
            RDFDataMgr.write( foutstr4 , g2, Lang.NQUADS );

            FileOutputStream foutstr5 = new FileOutputStream(fn + File.separator + "RDF_dump_.ntriples");
            RDFDataMgr.write( foutstr5 , g2, Lang.NTRIPLES );

            System.err.println( "Isomorphic=" + g2.isIsomorphicWith(g2) ) ;

            // Convert into CSV using arq ....
            // select * { ?s ?p ?o }
            // ./arq --data /GITHUB/FusekiCloud/fuseki-parcel/___WORKINGDATA/fuseki/scripts/main/data/model.ttl --results csv --query q.rq

        }


    }

    private void createIfFolderNotExist(String fn) {

        File f = new File( fn );
        if ( !f.exists() ) {

            f.mkdirs();

        }
    }

    public String NAME = "emdm";
    public int port = 3030;

    public void addContent(String content) {
        addContent(content, port, NAME);
    }

    public void addContent(String content, int port, String NAME) {

        // Update
        UpdateRequest req = UpdateFactory.create("INSERT DATA {" + content  + "}" ) ;
        UpdateExecutionFactory.createRemote(req, "http://localhost:"+port+"/" + NAME).execute();

        // Query again.
        query("http://localhost:"+port+"/" + NAME, "SELECT * { ?s ?p ?o}", qExec-> {
            ResultSet rs = qExec.execSelect() ;
            int x = ResultSetFormatter.consume(rs) ;
            System.err.println( "Nr of tripels => " +  x) ;
        }) ;

    }

    public String getContent(String filename) throws Exception {

        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader( fr );

        StringBuffer sb = new StringBuffer();
        while( br.ready() ) {
            sb.append( br.readLine() );
        }
        br.close();

        return sb.toString();
    }

}
