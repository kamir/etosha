package com.cloudera.dadi.neo4j;

import com.cloudera.dadi.DaDiStore;
//import org.neo4j.ogm.session.Session;
//import org.neo4j.ogm.session.SessionFactory;

/**
 *
 * @author kamir
 */
public class Neo4JDaDiStore implements DaDiStore {

//    SessionFactory sessionFactory = new SessionFactory("com.cloudera.neo4j.domain");
//    Session session = sessionFactory.openSession(); 

    @Override
    public boolean init() throws Exception {
//        session = sessionFactory.openSession();
        return true;
    }

    @Override
    public boolean close() {
//        sessionFactory.close();
        return true;
    }

    @Override
    public void storeEntityWithContext(Object o) {
//        session.save( o );
        System.out.print( "> sotre: " + o.getClass() );
    }

    
    
}
