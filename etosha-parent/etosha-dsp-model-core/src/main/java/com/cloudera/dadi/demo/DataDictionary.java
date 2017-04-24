package com.cloudera.dadi.demo;

import com.cloudera.dadi.store.DaDiStore;
import com.cloudera.dadi.store.cn.ClouderaNavigatorDaDiStore;
import com.cloudera.dadi.store.cn.OfflineClouderaNavigatorDaDiStore;
import com.cloudera.neo4j.domain.DataSet;
import com.cloudera.neo4j.domain.Flow;
import com.cloudera.neo4j.domain.PredictionModel;
import com.cloudera.neo4j.domain.ProcessingPipeline;
import com.cloudera.neo4j.domain.SystemUser;
import com.cloudera.neo4j.domain.TrainingPipeline;
import com.cloudera.neo4j.domain.types.DataFlowType;
import com.cloudera.neo4j.domain.types.UserType;
import java.util.Vector;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kamir
 */
//@Configuration
//@Import(Neo4JCFG.class)
//@RestController("/")
public class DataDictionary {

//    static ConfigurableApplicationContext context;
    
    static boolean isSingleMode = true;
    static DaDiStore singleStore = null;

    public static void setSingleStorePersistenceModule(DaDiStore dadi) {
        singleStore = dadi;
        isSingleMode = true;
    }

    public static void init() throws Exception {

        if (isSingleMode) {
            singleStore.init();
        } else {
            System.out.println(">>> MultiStore Data Dictionary not ready yet. <<<");
        }

    }

    public static void close() {

        if (isSingleMode) {
            singleStore.close();
        } else {
            System.out.println(">>> MultiStore Data Dictionary not ready yet. <<<");
        }

    }

    public static void main(String[] args) {
 

//        SpringApplication app = new SpringApplication(DataDictionary.class);
        // ... customize app settings here
   
//        context = app.run(args);
        
        try{
            showTheLittleDemo();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void run( String[] args ) {
        
        System.out.println( ">>> Start DEMO ... ");
        
    }

    public static void persist(Vector<Object> objectsToStoreInDaDi) {

        if (isSingleMode) {
            for (Object o : objectsToStoreInDaDi) {
                singleStore.storeEntityWithContext(o);
            }
        } else {
            System.out.println(">>> MultiStore Data Dictionary not ready yet. <<<");
        }

    }

    static void showTheLittleDemo() throws Exception {

        // Define a flow and persist it ...
        DataSet ds = new DataSet();
        ds.title = "DS_4_DEMO";

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

        dsSAMPLE.isDerivedFromDatasetViaFlow(ds, flow2);

        tp.consumesData(dsSAMPLE);
        tp.producesModel(dsMODEL);

        dsMODEL.isDerivedFromDatasetViaFlow(dsSAMPLE, tp);
        dsMODEL.accuracy = 0.85;
        dsMODEL.goal = "http://wikipedia.org";

        ProcessingPipeline pipe = new ProcessingPipeline(dsMODEL);
        pipe.title = "NRT classify";
        pipe.flowType = DataFlowType.STREAM_JOB;
        pipe.consumesData(ds);
        pipe.enrichesDataset(ds);

        // Bring things together 
        op1.ownsFlow(tp);
        op2.ownsFlow(pipe);
        op3.ownsFlow(flow3);

//        DaDiStore store1 = new Neo4JDaDiStore();
        DaDiStore store2 = new OfflineClouderaNavigatorDaDiStore();
        DaDiStore store3 = new ClouderaNavigatorDaDiStore();

        DataDictionary.setSingleStorePersistenceModule(store2);
        System.out.println( ">>> Use the CN store now ... ");
        
        DataDictionary.init();

        Vector<Object> objectsToStoreInDaDi = new Vector<>();

        objectsToStoreInDaDi.add(ds);
        objectsToStoreInDaDi.add(dsSAMPLE);
        objectsToStoreInDaDi.add(op1);
        objectsToStoreInDaDi.add(op2);
        objectsToStoreInDaDi.add(op3);
        objectsToStoreInDaDi.add(tp);
        objectsToStoreInDaDi.add(flow2);
        objectsToStoreInDaDi.add(dsMODEL);
        objectsToStoreInDaDi.add(pipe);
        objectsToStoreInDaDi.add(flow3);

        DataDictionary.persist(objectsToStoreInDaDi);

        DataDictionary.close();

        // DONE
        System.exit(0);
    }
    
    
//    @RequestMapping("/moin")
//    public String getString(@RequestParam(value = "name",required = false) String name) {
//        String greeting = (name == null ? "Mr. Unknonwn" : name);
//        return "MOIN : " + greeting;
//    }

 
//    @RequestMapping("/stop")
//    public void getString() {
//        context.close();
//        System.exit(0);
//    }
    
    
}
 
