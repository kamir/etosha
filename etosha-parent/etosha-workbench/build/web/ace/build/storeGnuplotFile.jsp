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
        <title>Store Gnuplot-File</title>
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
            
                System.out.println( "[DATA]\n"+data );

                data = URLDecoder.decode(data);
                projekt.storeGnuplotFile( data );

                revisionInfo = SVNTool.getCurrentRevision(projekt);

                CollabProject.reCreateGnuplotChart( projekt );

                name = projekt.getSingleDocumentFileName( request.getContextPath() + "/DATA/" );     
                name = name.replace( File.separatorChar, '/');
                System.out.println(">>> Vorschau: [" + name + ".pdf]" );
            }
        %>
        <%= revisionInfo %>
        <hr>
        <a href="<%=request.getContextPath()%>/ace/build/le_gnuplot.jsp?chartID=<%= chartID%>" onclick="reloadIFrame()" >Gnuplot-Editor</a>  
        
        <script>
            
            function reloadIFrame() {
                
                var html = "<iframe src=\"<%= name %>.pdf\" style=\"width:1100px; height:720px;\" frameborder=\"1\"></iframe>";
                var html2 = "<img src=\"<%=request.getContextPath() %>/DATA/P0/default/chart.png\" />";
                //alert( html );
                document.getElementById("tab-1").innerHTML = html;  
                document.getElementById("gnuplot_preview").innerHTML = html2;  
                
            }
            
        </script>
        
    </body>
</html>