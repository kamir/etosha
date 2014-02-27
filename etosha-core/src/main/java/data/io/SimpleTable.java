package data.io;

public class SimpleTable {
	
	
	public SimpleTable( int nrOfRows, String name ) {
		mr = new Double[2][nrOfRows];
	};

    public Double[][] mr = null;
    public String name = null;
	public String[] colLabel = new String[2];
	public Integer[] colNr = new Integer[2];
	
	public void setColumnLabels(String xCol, String yCol) {
		colLabel[0] = "x:"+xCol;
		colLabel[1] = "y:"+yCol;
	}

	public void setColumnNr(int xCol, int yCol) {
		colNr[0] = xCol;
		colNr[1] = yCol;
	}

	int lastRow = 0;
	public void addNewDataRow(double xValue, double yValue) {
		mr[0][lastRow] = xValue;
		mr[1][lastRow] = yValue;
		
		lastRow = lastRow + 1;
	}
	
	public String toString() {
		String back = colLabel[0] + "\t" + colLabel[1] + "\n";
		for(int row = 0; row < lastRow; row++ ){
			back = back + mr[0][row] + "\t" + mr[1][row] + "\n";
		}
		
		return back;
	}


	
}