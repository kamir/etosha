<%@page import="java.util.Hashtable"%>
<%@page import="data.GoogleDocument"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
<div id="tab-6" style="width: 96%; height: 720px;">
    
        <%
        
               String chartId = request.getParameter("chartID");
        Hashtable<String, CollabProject> allCharts = (Hashtable<String, CollabProject>)application.getAttribute("allCharts");
        String gnuplotData = "???";
        CollabProject projekt = null;
        if( allCharts != null ) {
            projekt = allCharts.get(chartId);
        }
    if ( projekt == null ) {
        projekt = (CollabProject)request.getSession().getAttribute("chartProjekt");
    }
        // projekt ist schon bekannt
    GoogleDocument gd = projekt.getGoogleDoc( "WP" );      
    String para = gd.docId;  
    
    System.out.println(">>> Google-Docs: [https://docs.google.com/document/pub?id=" + para + "&embedded=true]" );
    %>
    
    
<a href="#" onclick="doEdit()" >Bearbeiten</a> | <a href="#" onclick="doStopEdit()" >Anzeige</a><br>  <br>    
<iframe id="googleview" frameborder="0" style="width: 100%; height: 690px;"
        src="https://docs.google.com/document/pub?id=<%=para %>&embedded=true">
</iframe>


<script>
    
    function doEdit() {
        var adr = "https://docs.google.com/document/d/<%=para %>/edit?hl=de&pli=1";
        // alert( adr );
        document.getElementById('googleview').src = adr;
    };
    
    function doStopEdit() {
        var adr = "https://docs.google.com/document/pub?<%=para %>&embedded=true"
        //alert( adr );
        document.getElementById('googleview').src = adr;
    };
    
</script>
    

</div>