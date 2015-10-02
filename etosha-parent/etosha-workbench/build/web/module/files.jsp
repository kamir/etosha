<%@page import="java.util.Hashtable"%>
<%@page import="java.lang.String"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
<div id="tab-0" style="width: 70%; height: 720px;">
    
    <div id="tools">
        <a href="#" onclick="initProjekt()">Initialisieren</a> | 
        <a href="#" onclick="createTemplates()">Templates erstellen</a> | 
        <a href="#" onclick="recompileLatexFile()">(Re)Compile</a>
    </div>
    
    <div id="upload_form" >    
        
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
                projekt = CollabProject.getDefaultProjekt();
        }
  %>           
    
    


<form action="upload.jsp" enctype="multipart/form-data" method="post" onsubmit="startProgress()" style="position: relative; top: 15; margin: 5">
    
    <p>
    <table border="0" width="100%" cellpadding="3" cellspacing="0">
        <tr>
            <td style="text-align: right;text-shadow: white 1px 1px 1px;" width="22%"><b>Projekt-Name :</b> </td>
            <td><input class="eingabe" size="65" type="text" id="name" value="<%= projekt.name %>" name="name"/></td>
        </tr>    
        <tr>
            <td td style="text-align: right;text-shadow: white 1px 1px 1px;" width="22%" title="Worum geht es in diesem Projekt?">Workpackage :</td>
            <td><input class="eingabe" title="Worum geht es in diesem Projekt? Hinterlegen Sie hier eine Google-Docs Id, damit Sie im Team an der Definition des Working-Package arbeiten können." size="65"  type="text" id="name" value="<%=projekt.googleDocuments.get("WP").docId%>" name="name"/></td>
    </table><br>
      <h3 style="color: #5bc0de;text-shadow: white 1px 1px 1px;">&nbsp;Neue Dateien hinzufügen ...</h3>
  
        <table>    
             
        
        <tr style=""><td style="text-align: right;text-shadow: white 1px 1px 1px;">EPS-Datei :</td><td> <input class="default" title="Eine EPS-Datei kann z.B. aus Origin exportiert werden ..." size="65"  type="file" id="file1" name="file1"/></td></tr>
        
        <tr style=""><td style="text-align: right;text-shadow: white 1px 1px 1px;">LaTex-Datei :</td><td><input class="default" title="Mit der Latex-Datei für die Abbildung lassen sich einzelne Abbildungen besser organisieren und im Hauptdokument einbinden." size="65" type="file" id="file2" name="file2"/><br/>
        <tr style=""><td style="text-align: right;text-shadow: white 1px 1px 1px;">Messwerte :</td><td><input class="default" title="Archivieren Sie hier die originalen Messdaten als ZIP-Archiv, oder hinterlegen Sie eine Datei mit einer Beschreibung der Messdaten." size="65"  type="file" id="file3" name="file3"/><br/>
        <tr style=""><td style="text-align: right;text-shadow: white 1px 1px 1px;">Origin-File :</td><td><input class="default" title="Mit Origin lassen sich hochwertige Abbildungen und aufwendige Analysen durchführen. Legen Sie das zugehörige Origin-Projekt hier ab." size="65" value=".mm" type="file" id="file4" name="file4"/><br/>
        <tr style=""><td style="text-align: right;text-shadow: white 1px 1px 1px;">Mind-Map :</td><td><input class="default" title="Mit Origin lassen sich hochwertige Abbildungen und aufwendige Analysen durchführen. Legen Sie das zugehörige Origin-Projekt hier ab." size="65"  value=".opj" type="file" id="file5" name="file4"/><br/>
       
        <tr><td></td><td style="text-align: right;"><input  type="submit" title="Vorhandene Dateien werden, bevor diese überschrieben werden, im Versions-Kontrollsystem archiviert." value="hochladen starten" id="uploadbutton"/>  </td></tr>
    </table>
        <br/>

        <div id="progressBar" style="display: none;">

            <div id="theMeter">
                <div id="progressBarText"></div>

                <div id="progressBarBox">
                    <div id="progressBarBoxContent"></div>
                </div>
            </div>
        </div>
    </p>
</form>
</div>
<div id="listing">

   
        
    
    <%
       if ( projekt != null ) {
     
            File f = projekt.getBaseFolder();
            File[] liste = f.listFiles();
            for( File f1 : liste ) { 
               line = f1.getName() + "; " + new Date(f1.lastModified() );  
    %>        
        <%= line + "<br>" %>
    <%      }
        }
    %>


</div>
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
