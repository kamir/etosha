package com.cloudera.neo4j;
 
import com.cloudera.dadi.DaDiStore;
import com.cloudera.dadi.DataDictionary;
import com.cloudera.dadi.neo4j.Neo4JDaDiStore;
import com.cloudera.neo4j.domain.DataSet;
import com.cloudera.neo4j.domain.Flow;
import com.cloudera.neo4j.domain.PredictionModel;
import com.cloudera.neo4j.domain.ProcessingPipeline;
import com.cloudera.neo4j.domain.SystemUser;
import com.cloudera.neo4j.domain.TrainingPipeline;
import com.cloudera.neo4j.domain.types.DataFlowType;
import com.cloudera.neo4j.domain.types.UserType;

import com.cloudera.neo4j.services.DatasetService;
import java.awt.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;
import java.util.Vector;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.ConfigurableApplicationContext;
//  
//@Configuration
//@Import(Neo4JCFG.class)
//@RestController("/")
public class SampleDSPApp extends WebMvcConfigurerAdapter {

    
    // static ConfigurableApplicationContext context;
    
    /**
     * 
     * 
     * Stop this program:  ps -ef | grep DSPApp | awk '{print $2}'
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        
//        SpringApplication app = new SpringApplication(SampleDSPApp.class);
//        // ... customize app settings here
//   
//        context = app.run(args);
        
        showDemo( args );
        
    }
    
    
    public static void showDemo(String[] args) {
    
        Toolkit.getDefaultToolkit().beep();
        
        System.out.println(">>> Let's do something good with data.");
        
        DataSet ds = new DataSet();

        ds.title = "DS_" + System.currentTimeMillis();
 
        PredictionModel dsMODEL = new PredictionModel();
        dsMODEL.title = "model1";

        DataSet dsSAMPLE = new DataSet();
        dsSAMPLE.title = "sample_" + ds.title;

        SystemUser op1 = new SystemUser();
        op1.name = "Max";
        op1.userType = UserType.ANALYST;

        SystemUser op2 = new SystemUser();
        op2.name = "Billy";
        op2.userType = UserType.BUSINESSUNIT;

        SystemUser op3 = new SystemUser();
        op3.name = "Charly";
        op3.userType = UserType.OPERATOR;
        
        TrainingPipeline tp = new TrainingPipeline();
        tp.flowType = DataFlowType.BATCH_JOB;
        tp.title = "train decsision trees";

        Flow flow2 = new Flow();
        flow2.flowType = DataFlowType.FILTER;
        flow2.title = "sampling";
        
        Flow flow3 = new Flow();
        flow3.flowType = DataFlowType.GROWTH;
        flow3.title = "cont ingest";
        flow3.producesData(ds);

    

        dsSAMPLE.isDerivedFromDatasetViaFlow( ds , flow2 );
        
        tp.consumesData( dsSAMPLE );
        tp.producesModel( dsMODEL );
        
        dsMODEL.isDerivedFromDatasetViaFlow(dsSAMPLE, tp);
        dsMODEL.accuracy = 0.85;
        dsMODEL.goal = "http://wikipedia.org";
       
        ProcessingPipeline pipe = new ProcessingPipeline( dsMODEL );
        pipe.title = "NRT classify";
        pipe.flowType = DataFlowType.STREAM_JOB;
        pipe.consumesData(ds);
        pipe.enrichesDataset( ds );
        
        
        // Bring things together 
        op1.ownsFlow( tp );
        op2.ownsFlow( pipe );
        op3.ownsFlow( flow3 );
        
        DaDiStore store = new Neo4JDaDiStore();
        
        DataDictionary.setSingleStorePersistenceModule( store );
        
        Vector<Object> objectsToStoreInDaDi = new Vector<>();
        
        objectsToStoreInDaDi.add( ds );
        objectsToStoreInDaDi.add( dsSAMPLE );
        objectsToStoreInDaDi.add( op1 );
        objectsToStoreInDaDi.add( op2 );
        objectsToStoreInDaDi.add( op3 );
        objectsToStoreInDaDi.add( tp );
        objectsToStoreInDaDi.add( flow2 );
        objectsToStoreInDaDi.add( dsMODEL );
        objectsToStoreInDaDi.add( pipe );
        objectsToStoreInDaDi.add( flow3 ); 
        
        DataDictionary.persist( objectsToStoreInDaDi );
    } 

    @Autowired
    DatasetService dsService;

    @RequestMapping("/stop")
    public void getString() {
//        context.close();
        System.exit(0);
    }
    
     @RequestMapping("/hey")
    public String getString(@RequestParam(value = "name",required = false) String name) {
        String greeting = (name == null ? "Mr. Unknonwn" : name);
        return "HEY : " + greeting;
    }

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
        return dsService.graph(limit == null ? 100 : limit);
    }

}
