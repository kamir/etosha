package org.etosha.core.context;

public class SemanticJobContext {
	
	public String jobPn = null;
	
	public String getJobCategories(String project, String lUser,
			String lHostName, String drivername, String hadoopCluster, String localEnv) {
		String cat1 = "[[Category:" + project + "]]";	
		String cat2 = "[[Category:" + lUser + "]]";	
		String cat3 = "[[Category:" + lHostName + "]]";	 // from where was the job submitted
		String cat4 = "[[Category:" + drivername + "]]";
		String cat5 = "[[Category:" + hadoopCluster + "]]";
		String cat6 = "[[Category:" + localEnv + "]]";
		String cat = cat1.concat(cat2.concat(cat3.concat(cat4.concat(cat5.concat(cat6)))));
		return cat;
	}

}
