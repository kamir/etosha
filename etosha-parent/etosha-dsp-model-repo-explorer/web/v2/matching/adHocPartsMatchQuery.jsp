<%@page import="com.cloudera.search.queries.QueryConstants"%>
<%@page import="com.cloudera.search.queries.PartsSimpleQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ad-Hoc-Parts Matching Query</title>
    </head>
    <body>
        <h1>Ad-Hoc-Parts Matching Query</h1>
        <%
            
            PartsSimpleQuery psc = PartsSimpleQuery.init();
            
            String phrase = request.getParameter("p");
         
            String info = "";
         
            if ( phrase == null ) {
                info = "Phrase can not be NULL or empty!\n"+
                       "Please provide a valid non empty search " +
                       "phrase and try again.";
            }
            else {
                psc.buildQuery( phrase );
                psc.executeQuery();      
                info = psc.getResultAsJSON();
            }
            
         %>

         Query on collection : [<%= QueryConstants.collectionName %>]</br>
         Query on field : [<%= psc.getDefaultField() %>]</br>
         Query pattern : <%= phrase %></br>

         <hr>
         
         <h3>Results:</h3>
         <textarea name="xmlResult" rows="15" cols="200"><%= info %></textarea>
         <hr>
         
         <%
             if( phrase == null ) {
         %>
         <textarea name="jsonResult"><%= psc.getResultAsJSON() %>
         </textarea>
         <%
             }
         %>
    </body>
</html>
