<%-- 
    Document   : initDemoData
    Created on : 31.03.2016, 07:36:13
    Author     : kamir
--%>

<%@page import="com.cloudera.dal.partsmatch.Match"%>
<%@page import="java.util.Vector"%>
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
        <h1>Init Demo Data</h1>
        <hr>
        
        <%
            MatchStore ms = (MatchStore)request.getServletContext().getAttribute("store");
  
            String CLAIM = "Bremsscheibe linke Seite hinten";
            String LANG = "PL";
        
            MatchingResultContainer mrc = new MatchingResultContainer();
            
            mrc.addResult( ms.getMatchesForAClaimInLanguage(CLAIM, LANG) );
            
            session.setAttribute( "MRC", mrc );
             
            
            String hueHBaseLink = "http://dl-mn04-d.axadmin.net:8888/hbase/#HBase/" + ms.getTableName();

        %>   
         
        <%=mrc.toHTMLString() %>
        <hr>
        
        <%=ms.getTableName() %> |  <a href="<%= hueHBaseLink %>">TABLE in HUE</a> 
        
        <hr>
        <a href="./../export/downloadAsCSV.jsp">LOAD CSV</a> |  <a href="./../export/downloadAsXLS.jsp">LOAD XLS</a>
        <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
    </body>
</html>
