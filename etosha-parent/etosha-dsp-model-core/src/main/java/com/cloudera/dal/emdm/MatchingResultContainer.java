/*
 * Copyright 2016 kamir.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.dal.emdm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * In this container we collect MatchingResults for presentation and export in a
 * Web-UI or even in the core engine.
 *
 * E.g., all item with a result score bigger than a threshold could be collected
 * and exported as CSV already, beside writing into HBase.
 *
 * @author kamir
 */
public class MatchingResultContainer {

    Vector<MatchingResult> vmr = null;

    public MatchingResultContainer() {
        vmr = new Vector<MatchingResult>();
    }

    public void addResultSet( Vector<Match> v, String claim, String lang) {
        MatchingResult mr = new MatchingResult();
        mr.claim = claim;
        mr.lang = lang;
        mr.matches = v;
        vmr.add(mr);
    }

    public void addResults(Vector<MatchingResult> vmr) {
        for( MatchingResult mr : vmr ) {
            addResult( mr );
        }
    }
    
    public void addResult(MatchingResult r) {
        if ( r != null ) vmr.add(r);
    }
    
    public int getSize() {
        if ( vmr == null ) return 0;
        else return vmr.size();
    }
    
    public void storeMatchResults( EMDMStore ms ) throws IOException {
    
        if ( vmr != null ) {
          for (MatchingResult mr : vmr) {
              
              ms.putMatchResults(mr);
              
          }
        }
        else {
            System.out.println( ">>> NO MATCH-STORE available ... ");
        }
        
    }

    /**
     * TODO: Use a velocity templat to render a cool output ... for now we do a
     * boring HTML-table. ;-(
     */
    public String toHTMLString() {

        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1\">");
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th>Claim</th>");
        sb.append("<th>Part</th>");
        sb.append("<th>Score</th>");
        sb.append("<th>User Rating</th>");
        sb.append("<th>Lang</th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");

        for (MatchingResult mr : vmr) {
            for (Match m : mr.matches) {
                sb.append("<tr>");
                sb.append("<td><i>" + mr.claim + "</i></td>");
                sb.append("<td>" + m.part + "</td>");
                sb.append("<td><b>" + m.score + "</b></td>");
                sb.append("<td>" + m.userRating + "</td>");
                sb.append("<td>" + m.lang + "</td>");
                sb.append("</tr>\n");
            }
        }

        sb.append("</tbody>");
        sb.append("</table>\n");

        return sb.toString();
    }

    public String toCSV() {

        StringBuffer sb = new StringBuffer();
        sb.append("claim,part,score,user_rating,lang\n");

        for (MatchingResult mr : vmr) {
            for (Match m : mr.matches) {
                sb.append(mr.claim + ",");
                sb.append(m.part + ",");
                sb.append(m.score + ",");
                sb.append(m.userRating + ",");
                sb.append(m.lang);
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    
    public void writeToStreamAsXLSSheet(OutputStream os) throws IOException {
        
        HSSFWorkbook wb = this.getWorkBook();

        // write it as an excel attachment
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        
        wb.write(outByteStream);
        
        byte[] outArray = outByteStream.toByteArray();

        os.write(outArray);

    }
    
    
    public HSSFWorkbook getWorkBook() {
    
        int x = 0;
        int y = 0;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow row = sheet.createRow(y);

        HSSFCell cell = row.createCell(x);
        cell.setCellValue("Claim");
        x++;
        cell = row.createCell(x);
        cell.setCellValue("Part");
        x++;
        cell = row.createCell(x);
        cell.setCellValue("Score");
        x++;
        cell = row.createCell(x);
        cell.setCellValue("User Rating");
        x++;
        cell = row.createCell(x);
        cell.setCellValue("Language");
        x++;

        int z = 0;
        for (MatchingResult mr : vmr) {
            
            z++;
    
            for (Match m : mr.matches) {
        
                y++;
                
                row = sheet.createRow(y);
                x=0;
            
                cell = row.createCell(x);
                cell.setCellValue(z);
                x++;
                
                cell = row.createCell(x);
                cell.setCellValue(mr.claim);
                x++;
                cell = row.createCell(x);
                cell.setCellValue(m.part);
                x++;
                cell = row.createCell(x);
                cell.setCellValue(m.score);
                x++;
                cell = row.createCell(x);
                cell.setCellValue(m.userRating );
                x++;
                cell = row.createCell(x);
                cell.setCellValue(m.lang);
                x++;
            
            } 
            
        }
        return wb;

    }

}
