<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
<div id="tab-7" style="width: 97%; height: 720px;">
    
    <!--div id="tools">
        <a href="#" onclick="initProjekt()">Initialisieren</a> | 
        <a href="#" onclick="createTemplates()">Templates erstellen</a> | 
        <a href="#" onclick="recompileLatexFile()">(Re)Compile</a>
    </div-->
    
    <div id="refinder">    
         <iframe id="refinderview" style="width:100%;height: 710px;" frameborder="0" src="http://www.getrefinder.com">
         </iframe>
    </div>
</div>    
<!--script>
    
    function recompileLatexFile() { 
        window.location.href="module/recompile.jsp";
    }
    
    function initProjekt() { 
        window.location.href="init.jsp?name=P0/default&basePath=C:/TEST/";
    }
    
    function createTemplates() { 
        window.location.href="createTemplates.jsp";
    }
    
</script-->
