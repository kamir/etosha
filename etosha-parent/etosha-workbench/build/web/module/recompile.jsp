<%-- 
    Document   : recompile
    Created on : 04.01.2012, 21:07:28
    Author     : kamir
--%>

<%@page import="data.CollabProject"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
        <title>(Re)Compile</title>
    </head>
    <body>
        <h3>Projekt neu kompilieren</h3>
        
        <%  
            CollabProject projekt = (CollabProject)request.getSession().getAttribute("chartProjekt");
            CollabProject.recompile( projekt );
        %>
   
    </br>
    <hr />
    
    <hr/>
<a href="./../index.jsp">Weiter</a> ...
    </body>
</html>
