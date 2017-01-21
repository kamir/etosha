<%@page import="com.cloudera.search.QueryConstants"%>
<%@page import="com.cloudera.search.queries.IdQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UC-02 Process Dashboard</title>
    </head>
    <body>
        <h1>UC-02 Process Dashboard</h1>
        <%
            
            response.setIntHeader("Refresh", 5);

                        
            IdQuery q1 = IdQuery.init();
            
            // the query phrase is defined by the "p-parameter of the JSP".
            String phrase = request.getParameter("p");
         
            String info = "";

            phrase = "*";

            if ( phrase == null ) {
                info = "Phrase can not be NULL or empty!\n"+
                       "Please provide a valid non empty search " +
                       "phrase and try again.";
                
                phrase = "*";
            }
            else {
                q1.buildQuery( phrase );
                q1.executeQuery();      
                info = q1.getResultAsJSON();
            }
            
         %>

         Query on collection : [<%= QueryConstants.defaultCollection_Layer2 %>]</br>
         Query on field : [<%= q1.getDefaultField() %>]</br>
         Query pattern : <%= phrase %></br>

         <hr>
         
         <h3>Results:</h3>
         <textarea name="xmlResult" rows="15" cols="200"><%= info %></textarea>
         <hr>
         
         <%
             if( phrase == null ) {
         %>
         <textarea name="jsonResult"><%= q1.getResultAsJSON() %>
         </textarea>
         <%
             }
         %>
    </body>
</html>
