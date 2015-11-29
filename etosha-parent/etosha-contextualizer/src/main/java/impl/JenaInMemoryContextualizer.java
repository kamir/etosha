package impl;

import contextualizer.IContextualizer;

/**
 * Just for demos and simple tests we experiment with an in Memory
 * contextualizer, which works stateless. All data is collected during runtime.
 * Therefore we use it in the Spark-Shell;
 *
 * @author training
 *
 */
public class JenaInMemoryContextualizer implements IContextualizer {

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

    @Override
    public String getName() {
        return this.getClass().getName();
    }

}
