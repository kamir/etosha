<%-- 
    Document   : showSearchResultsAsPDF
    Created on : 26.07.2015, 13:35:55
    Author     : kamir
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Results for offine Export</title>
    </head>
    <body>
        <h1>We load the search results and export a list of QA items.</h1>
        Stored items contain markup. We render HTML first and than we render the HTML into a PDF file.
        <hr>
        
        <%
           
            StringBuffer sb = new StringBuffer();
            
                    ServletContext context = config.getServletContext();
                    Enumeration en = context.getAttributeNames();
                    
                    while( en.hasMoreElements() ) {
                        String s = (String) en.nextElement();
                        sb.append(s + "<br/>");
                    }

            
            
         %>   
    </body>
</html>
