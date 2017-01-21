<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@page import="com.cloudera.search.queries.VehicleSimpleQuery"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vehicle Query DEMO</title>
    </head>
    <body>
        <h1>Vehicle Query DEMO</h1>
        
        <%
            VehicleSimpleQuery svc = VehicleSimpleQuery.init();
         %>
 
         Query on field : [<%= svc.getDefaultField() %>]</br>
         Query pattern  : <%= request.getParameter("p") %></br>

        <%
            svc.buildQuery(request.getParameter("p"));
              
            svc.executeQuery();      
         %>

         <hr>
         
         <h3>Results:</h3>
         <textarea name="xmlResult" rows="15" cols="200"><%= svc.getResultAsXML() %></textarea>
         <hr>
         
    </body>
</html>
