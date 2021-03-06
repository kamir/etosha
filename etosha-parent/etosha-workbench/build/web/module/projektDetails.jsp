<%@page import="java.util.Hashtable"%>
<%@page import="java.io.File"%>
<%@page import="data.CollabProject"%>
 
<%
    Hashtable<String, CollabProject> allCharts = (Hashtable<String, CollabProject>)application.getAttribute("allCharts");
    CollabProject projekt = (CollabProject)request.getSession().getAttribute("myProjekt");    
    
    String projectName = "P0"+File.separator+"default";
    if ( projekt == null ) { 
        projekt = new CollabProject();
        projekt.name = projectName;
        request.getSession().setAttribute("chartProjekt", projekt );
        System.out.println("---> Projekt: " + projekt.name +" (neu initialisiert)" ); 
    }
    else {
       System.out.println(">>>> Projekt: " + projekt.name + " (in Session)" ); 
    }
       
    StringBuffer sb = new StringBuffer();
    
    String mname = projekt.name.replace('/', '\\');
    String mbasePath = projekt.basePath.replace('/', '\\');
    String sessionID = request.getSession().getId();
    
    
    String mode=request.getParameter("section");
                
    String feld1 = ""; 
    String feld2 = ""; 
          
            
    System.out.println("** SWITCH to mode: " + mode );
    if( mode != null ) {
        if ( mode.equals("RRTB") ) {
            feld1 ="";
            feld2 ="&lt;Research-Report-Editor/&gt";
        }
        else if ( mode.equals("KBTB") ) {
            feld1 ="";
            feld2 ="&ltKnowledge-Base /&gt";
            
            
        }
        else if ( mode.equals("ADMINTB") ) {
            feld1 ="";
            feld2 ="Administration";
            
            
        }
        else if ( mode.equals("HADOOP") ) {
            feld1 ="";
            feld2 ="&ltHadoop-Workspace /&gt";
            
            
        }
        
                       
        
    }
    
%>    

<div id='header'>
    <table width="500" border='0' >
        <tr class='headerrow'>
            <td style='width=100px;'></td>
            <td><b><%= feld1 %></b>  <i><%=feld2 %></i></td>
        </tr>
    </table>  
    <table width="200" border='0' >
        <tr class='headerrow'>
            <td width='12%' class='header-info' style='text-align: right;'>Basis-Pfad:</td>
            <td><%= mbasePath %></td>
            <td class='header-info' style='text-align: right;'>Projekt-Name:</td>
            <td><%= mname %></td>
            <!--td style='text-shadow: white 1px 1px 2px; font-size: 9px; color: lightgray;'>"+sessionID+"</td-->
        </tr>
    </table>
</div>

 
  
 
