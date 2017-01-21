<%-- 
    Document   : initDemoData
    Created on : 31.03.2016, 07:36:13
    Author     : kamir
--%>

<%@page import="com.cloudera.dal.partsmatch.MatchStore"%>
<%@page import="com.cloudera.dal.partsmatch.MatchingResult"%>
<%@page import="com.cloudera.dal.partsmatch.MatchingResultContainer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Demo-Page</title>
    </head>
    <body>
        <h1>Init Demo Data from HBase</h1>
        <hr>
        
        <%
         
            MatchingResultContainer mrc = new MatchingResultContainer();
            
            mrc.addResult( MatchingResult.getTestMatchSamples1());
            mrc.addResult( MatchingResult.getTestMatchSamples2());
            mrc.addResult( MatchingResult.getTestMatchSamples3());
            
            session.setAttribute( "MRC", mrc );

            MatchStore ms = (MatchStore)request.getServletContext().getAttribute("store");
            
            String note =  "no problems";
        
            try {
                mrc.storeMatchResults( ms );
            } 
            catch(Exception ex){
                ex.printStackTrace();
            }
         %>   
         
        <hr> 
        <%=mrc.toHTMLString() %>
        <hr>
        <%=note %>
        <hr>
        <a href="./../export/downloadAsCSV.jsp">LOAD CSV</a> |  <a href="./../export/downloadAsXLS.jsp">LOAD XLS</a>
        <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
    </body>
</html>
