<%-- 
    Document   : init
    Created on : 04.01.2012, 12:34:43
    Author     : kamir
--%>

<%@page import="java.io.File"%>
<%@page import="org.apache.catalina.Session"%>
<%@page import="data.CollabProject"%>
<%@page contentType="text/html" pageEncoding="windows-1250"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
        <title>Create Templates ...</title>
    </head>
<body>
    <h3>Templates anlegen ... </h3>
    <% 
    
    CollabProject projekt = CollabProject.getDefaultProjekt();

    
       
    request.getSession().setAttribute("chartProjekt", projekt );

       
    StringBuffer sb = new StringBuffer();
    sb.append("<ul>");
    
    sb.append("<li>" + projekt.name );
    sb.append("</li>");  
    sb.append("<li>" + projekt.basePath );
    sb.append("</li>");  
    
    sb.append("</ul>");
    
    projekt.createTemplates();
%>
    </br>
    <hr />
    <%=sb.toString() %>
    <hr/>
<a href="index.jsp">Weiter</a> ...
</body>
</html>
