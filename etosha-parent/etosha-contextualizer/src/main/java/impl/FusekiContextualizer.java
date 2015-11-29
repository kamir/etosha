package impl;

import contextualizer.IContextualizer;

/**
 * For single cluster usage and multi cluster usafe we offer a statefull
 * contextualizer.
 *
 * All data is collected during runtime, can be persisted, exported and even
 * imported from remote locations. We use it in the Spark-Shell and in the
 * Etosha-cli;
 *
 * @author training
 *
 */
public class FusekiContextualizer implements IContextualizer {

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void open() {
		// TODO Auto-generated method stub

    }

    @Override
    public void close() {
		// TODO Auto-generated method stub

    }

    @Override
    public void putNSPO(String n, String s, String p, String o) {
		// TODO Auto-generated method stub

    }

    @Override
    public void putSPO(String s, String p, String o) {
		// TODO Auto-generated method stub

    }

    @Override
    public void init() {
		// TODO Auto-generated method stub

    }

    @Override
    public void initDEMO() {
		// TODO Auto-generated method stub

    }

}
