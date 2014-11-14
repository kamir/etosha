package org.etosha.io.docs;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author kamir
 */
public class MSExcelFileLoader {
	
	static int headerOffset = 1;

    public static SimpleTable doLoadTable(
             File worksheetFile, String tableName,
             int xCol, int yCol,
             int firstRow, int lastRow ) 
             throws FileNotFoundException, IOException {

    	int nrOfRows = lastRow - firstRow;
    	
    	SimpleTable stab = new SimpleTable( nrOfRows, tableName );
    	stab.setColumnNr( xCol, yCol );
    	
        InputStream input = new FileInputStream(worksheetFile.getAbsolutePath());
        HSSFWorkbook wb = new HSSFWorkbook(input);
        HSSFSheet sheet = wb.getSheet(tableName);

        // header row with offset
        HSSFRow rowH = sheet.getRow(firstRow-headerOffset-1);

        String headerX = rowH.getCell(xCol-1).getStringCellValue();
        String headerY = rowH.getCell(yCol-1).getStringCellValue();


    	stab.setColumnLabels( headerX, headerY );

        for ( int r = firstRow-1 ; r < lastRow-1; r++ ) {
            HSSFRow row = sheet.getRow(r);
            
            double xValue = row.getCell(xCol-1).getNumericCellValue();

            double yValue = row.getCell(yCol-1).getNumericCellValue();

            stab.addNewDataRow(xValue, yValue);
        }

        return stab;
    };

    // TODO: {TOP} implement the SMW based parametrisation !!! 
    public static void main( String[] args ) {
    	
        File file = new File( "./data/result.xls" );
        
        String tableName = "tab1";
        
        try {
            SimpleTable mr = MSExcelFileLoader.doLoadTable(file, tableName, 1, 2, 2, 5);
            System.out.println(mr);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(MSExcelFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(MSExcelFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    };


}


