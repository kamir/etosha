<%@page import="com.cloudera.dal.partsmatch.MatchingResultContainer"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.poi.hssf.usermodel.*" %>
<%@page import="java.io.*" %>


<%

Date date = new Date();
                
MatchingResultContainer mrc = (MatchingResultContainer)session.getAttribute( "MRC" );

String fn = "solera-pm-resultset-"+date+".xls";

// create a spreadsheet
HSSFWorkbook wb = mrc.getWorkBook();

// write spreadsheet as an MS-Excel file
ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
wb.write(outByteStream);
byte [] outArray = outByteStream.toByteArray();
response.setContentType("application/ms-excel");
response.setContentLength(outArray.length);
response.setHeader("Expires:", "0");  
response.setHeader("Content-Disposition", "attachment; filename=" + fn );
OutputStream outStream = response.getOutputStream();
outStream.write(outArray);
outStream.flush();

%>
 