<%-- 
    Document   : downloadAsCSV
    Created on : 31.03.2016, 07:52:39
    Author     : kamir
--%>

<%@page import="com.cloudera.dal.partsmatch.MatchingResultContainer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export: CSV-Download</title>
    </head>
    <body>
        <h1>Export the MatchingResultContainer as CSV</h1>
            <% 
                MatchingResultContainer mrc = (MatchingResultContainer)session.getAttribute( "MRC" );
             %>
             <hr>
             Number of Matching Results : <%= mrc.getSize() %>
             <hr>
        <textarea name="csv" rows="45" cols="250"><%= mrc.toCSV() %>
        </textarea>
        <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]


    </body>
</html>
