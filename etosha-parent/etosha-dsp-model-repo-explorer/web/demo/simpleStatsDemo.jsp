<%@page import="java.util.Vector"%>
<%@page import="com.cloudera.search.util.FieldList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@page import="com.cloudera.search.queries.FieldStatsQuery"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Simple Statistics DEMO</title>
    </head>
    <body>
        <h1>Simple Statistics DEMO</h1>
        
        <%
            FieldStatsQuery svc = FieldStatsQuery.init();
         %>
 
         Query on field : [<%= svc.getDefaultField() %>]</br>
         Query pattern  : <%= request.getParameter("p") %></br>

        <%
            Vector<String> fields = FieldList.getFieldsOfType_N_for_Collection_c(null, null);
            svc.buildQuery( fields.toArray( new String[fields.size()] ) );
              
            svc.executeQuery();      
         %>

         <hr>
         
         <h3>Results:</h3>
         <textarea name="xmlResult" rows="15" cols="200"><%= svc.getResultAsXML() %></textarea>
         <hr>
         
    </body>
</html>
