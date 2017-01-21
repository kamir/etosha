<%-- 
    Document   : runTestsOnline.jsp
    Created on : 05.04.2016, 10:58:19
    Author     : kamir
--%>

<%@page import="java.io.PrintStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.cloudera.dal.partsmatch.MatchStoreTest"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Run Test-Suite online</title>
    </head>
    <body>
        <h1>MatchStoreTest</h1>
        
        <%
        
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            
            String[] args = { "dl-mn04-d,dl-mn02-d,dl-mn03-d", "TestOnline_" + System.currentTimeMillis() };
 
            MatchStoreTest mst = new MatchStoreTest();
            mst.run(args, stream);
            
         %>   
         <textarea name="" rows="15" cols="50"><%= stream.toString() %></textarea>
         <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]

    </body>
</html>
