<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
<div id="tab-8" style="width: 96%; height: 720px;">
    
    <!--div id="tools">
        <a href="#" onclick="initProjekt()">Initialisieren</a> | 
        <a href="#" onclick="createTemplates()">Templates erstellen</a> | 
        <a href="#" onclick="recompileLatexFile()">(Re)Compile</a>
    </div-->
    
    <div id="mindmap">    
        <applet code="org.freeplane.main.applet.FreeplaneApplet.class" archive="<%=request.getContextPath() %>/module/freeplane/freeplaneviewer.jar" width="100%" height="100%">
            <param name="type" value="application/x-java-applet;version=1.5"/>
            <param name="scriptable" value="false"/>
            <param name="browsemode_initial_map" value="./DATA/P0/default/map.mm"/>
            <param name="selection_method" value="selection_method_direct"/>
        </applet>
    </div>
<script>
    
    function recompileLatexFile() { 
        window.location.href="module/recompile.jsp";
    }
    
    function initProjekt() { 
        window.location.href="init.jsp?name=P0/default&basePath=C:/TEST/";
    }
    
    function createTemplates() { 
        window.location.href="createTemplates.jsp";
    }
    
</script>
</div>