<%@page import="java.util.Hashtable"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
<div id="tab-1" style="width:96%; height:720px;">
    <%
      String chartID = request.getParameter("chartID");
            System.out.println( "* files.jsp -> " + chartID );
            
            Hashtable<String, CollabProject> allCharts = (Hashtable<String, CollabProject>)application.getAttribute("allCharts");
        String line = "";
        
        CollabProject projekt = null;
        if( allCharts != null ) {
            projekt = allCharts.get(chartID);
        }
    
    if ( projekt == null ) {
        projekt = (CollabProject)request.getSession().getAttribute("chartProjekt");
    }
        // projekt ist schon bekannt   
    String name = projekt.getSingleDocumentFileName( request.getContextPath() + "/DATA/" );     
    name = name.replace( File.separatorChar, '/');
    System.out.println(">>> Vorschau: [" + name + ".pdf]" );
    %>
    <iframe src="<%= name %>.pdf?<id=<%= System.currentTimeMillis() %>" style="width:100%; height:720px;" frameborder="0">
    
    </iframe>
    
</div>
