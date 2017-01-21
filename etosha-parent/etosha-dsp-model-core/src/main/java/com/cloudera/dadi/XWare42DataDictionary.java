package com.cloudera.dadi;

import com.cloudera.dadi.cn.ClouderaNavigatorDaDiStore;
import com.cloudera.dadi.cn.OfflineClouderaNavigatorDaDiStore;
import com.cloudera.dadi.neo4j.Neo4JDaDiStore;
import com.cloudera.neo4j.Neo4JCFG;
import com.cloudera.neo4j.SampleDSPApp;
import static com.cloudera.neo4j.SampleDSPApp.showDemo;
import com.cloudera.neo4j.domain.DataSet;
import com.cloudera.neo4j.domain.EnrichmentPipeline;
import com.cloudera.neo4j.domain.Flow;
import com.cloudera.neo4j.domain.PredictionModel;
import com.cloudera.neo4j.domain.MappingModel;
import com.cloudera.neo4j.domain.ProcessingPipeline;
import com.cloudera.neo4j.domain.SystemUser;
import com.cloudera.neo4j.domain.TrainingPipeline;
import com.cloudera.neo4j.domain.types.DataFlowType;
import com.cloudera.neo4j.domain.types.UserType;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author kamir
 */

public class XWare42DataDictionary {
 
    
    static boolean isSingleMode = true;
    static DaDiStore singleStore = null;

    public static void setSingleStorePersistenceModule(DaDiStore dadi) {
        singleStore = dadi;
        isSingleMode = true;
        System.out.println( ">>> Use the " + dadi.toString() + " store now ... (singlemode=" + isSingleMode + ")" );
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
        
        describe();
        
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

    static void describe() {

        // Define a flow and persist it ...
        DataSet ds = new DataSet();
        ds.title = "RAW_TX_Records";

        MappingModel catMODEL = new MappingModel();
        catMODEL.title = "CategoryMODEL";

        MappingModel nameMODEL = new MappingModel();
        nameMODEL.title = "NormalizedNameMODEL";
        
        DataSet dsSAMPLE = new DataSet();
        dsSAMPLE.title = "sample_" + ds.title;

        SystemUser op1 = new SystemUser();
        op1.name = "Mirko";
        op1.userType = UserType.ANALYST;

        SystemUser op2 = new SystemUser();
        op2.name = "David";
        op2.userType = UserType.BUSINESSUNIT;

        SystemUser op3 = new SystemUser();
        op3.name = "Mirko";
        op3.userType = UserType.OPERATOR;

        EnrichmentPipeline e3 = new EnrichmentPipeline();
        e3.flowType = DataFlowType.BATCH_JOB;
        e3.title = "add normailzed names";
        e3.combinesModel(nameMODEL, ds);

        EnrichmentPipeline e4 = new EnrichmentPipeline();
        e4.flowType = DataFlowType.BATCH_JOB;
        e4.title = "add normailzed names";
        e4.combinesModel(catMODEL, ds);
        
        Flow flow2 = new Flow();
        flow2.flowType = DataFlowType.FILTER;
        flow2.title = "sampling";

        Flow flow3 = new Flow();
        flow3.flowType = DataFlowType.GROWTH;
        flow3.title = "cont ingest";
        flow3.producesData(ds);

        dsSAMPLE.isDerivedFromDatasetViaFlow(ds, flow2);

//        ProcessingPipeline pipe = new ProcessingPipeline(dsMODEL);
//        pipe.title = "NRT classify";
//        pipe.flowType = DataFlowType.STREAM_JOB;
//        pipe.consumesData(ds);
//        pipe.enrichesDataset(ds);

        // Bring things together 
        op1.ownsFlow(e3);
        op1.ownsFlow(e4);
//        op2.ownsFlow(pipe);
        op3.ownsFlow(flow3);
 
        DaDiStore store2 = new OfflineClouderaNavigatorDaDiStore();
        
        DaDiStore store3 = new ClouderaNavigatorDaDiStore();

        XWare42DataDictionary.setSingleStorePersistenceModule(store2);
        
        try {
            XWare42DataDictionary.init();

            Vector<Object> objectsToStoreInDaDi = new Vector<>();

            objectsToStoreInDaDi.add(ds);
            objectsToStoreInDaDi.add(dsSAMPLE);

    //        objectsToStoreInDaDi.add(op1);
    //        objectsToStoreInDaDi.add(op2);
    //        objectsToStoreInDaDi.add(op3);

            objectsToStoreInDaDi.add(flow2); 
    //        objectsToStoreInDaDi.add(pipe);
            objectsToStoreInDaDi.add(flow3);

            XWare42DataDictionary.persist(objectsToStoreInDaDi);

            XWare42DataDictionary.close();
        
        } 
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        // DONE
        System.exit(0);
    }
    
 
    
    
}
 
