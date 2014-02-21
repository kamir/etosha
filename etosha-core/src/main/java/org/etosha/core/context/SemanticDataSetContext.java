package org.etosha.core.context;


public class SemanticDataSetContext {
	
	public boolean isExplicit = true; // default 
	
	public String getDataSetCategories(SemanticJobContext jCont, String project, String lUser,String lHostName) {
		
		String cat1 = "[[Category:" + project + "]]";	
		String cat2 = "[[Category:" + lUser + "]]";	
		
		String cat3 = "[[Category:ExplicitDataSet]]";
		if( !isExplicit ) cat3 = "[[Category:ImplicitDataSet]]";
		
		String prop1 = "[[wasCreatedByJob::"+jCont.jobPn+"]]";
		
		String cat = cat1.concat(cat2.concat(cat3.concat(prop1)));
		
		return cat;
	}

}
