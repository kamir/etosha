package contextualizer;

public interface IContextualizer {

	public abstract void open();

	public abstract void close();

	public abstract void putNSPO(String n, String s, String p, String o);

	public abstract void putSPO(String s, String p, String o);

	public abstract void init();
	
	public abstract void initDEMO();

        public abstract String getName();
	

}