package com.cloudera.dadi.store.jena;

import com.cloudera.dadi.store.DaDiStore;

/**
 * Created by kamir on 23.04.17.
 */
public class FusekiDaDiStore implements DaDiStore {

    @Override
    public boolean init() throws Exception {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public void storeEntityWithContext(Object o) {

    }

}
