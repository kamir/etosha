<%-- 
    Document   : storeLatexFile
    Created on : 04.01.2012, 13:55:52
    Author     : kamir

http://mentaljetsam.wordpress.com/2008/06/02/using-javascript-to-post-data-between-pages/


--%>

<%@page import="java.util.Hashtable"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="tools.SVNTool"%>
<%@page import="data.CollabProject"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Store Latex-File</title>
    </head>
    <body>
         
        <% 
            String chartID = request.getParameter("chartID");
            Hashtable<String, CollabProject> allCharts = (Hashtable<String, CollabProject>)application.getAttribute("allCharts");
            String gnuplotData = "???"; 
            String revisionInfo = "kein Chart definiert ...";
            String name = "emptyFile";
            if( allCharts != null ) {
                CollabProject projekt = allCharts.get(chartID);
                
                String data = request.getParameter("data" ); 
            
                System.out.println( data );
                       
                data = URLDecoder.decode(data);
                projekt.storeLatexFile( data );
            
                revisionInfo = SVNTool.getCurrentRevision(projekt);
                       
                CollabProject.reCompile( projekt );
            
                name = projekt.getSingleDocumentFileName( request.getContextPath() + "/DATA/" );  
                  
                name = name.replace( File.separatorChar, '/');
                System.out.println(">>> Vorschau: [" + name + ".pdf]" );
                            }
        %>
        <%= revisionInfo %>
        <hr>
        <a href="<%= request.getContextPath()%>/ace/build/le.jsp?chartID=<%=chartID %>" onclick="reloadIFrame()" >LaTex-Editor</a>  
        
        <script>
            
            function reloadIFrame() {
                
                var html = "<iframe src=\"<%= name %>.pdf?chartID=<%=chartID %>\" style=\"width:1100px; height:720px;\" frameborder=\"1\"></iframe>";
                //alert( html );
                document.getElementById("tab-1").innerHTML = html;                
            }
            
        </script>
        
    </body>
</html>
