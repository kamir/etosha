<%-- 
    Document   : init
    Created on : 04.01.2012, 12:34:43
    Author     : kamir
--%>

<%@page import="java.util.Hashtable"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.catalina.Session"%>
<%@page import="data.CollabProject"%>
<%@page contentType="text/html" pageEncoding="windows-1250"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
        <title>Initialisieren ...</title>
    </head>
    <body>

        <%
            String name = request.getParameter("name");
            String basePath = request.getParameter("basePath");
            
            // wird sp�ter in der Session und auch in den IFRAMES als
            // ID zur Referenzierung im App-Context benutzt.
            String chartID = request.getSession().getId();

            StringBuffer sb = new StringBuffer();
            
            if ((name != null) && (basePath != null)) {
                CollabProject projekt = new CollabProject();
                projekt.setBasePath(basePath);
                projekt.setName(name);

                projekt.createFolder();

                request.getSession().setAttribute("myProjekt", projekt);
            } else {



                System.out.println(">>> INIT without PARAMETERS ... ");
            }
        %>

        <h3>Initialisierung </h3>
        <%



            CollabProject projekt = (CollabProject) request.getSession().getAttribute("myProjekt");
            if (projekt == null) {
                sb.append("Es wurde kein <b>Science-Report-Projekt</b> ausgew�hlt.<br><br>");
                sb.append("<b>Aufruf des Moduls:</b> /init.jsp?name=P0/default&basePath=C:/TEST/<br>");
                sb.append("<hr><a href='init.jsp?name=P0/default&basePath=C:/TEST/'>Test-Projekt</a>");
                
            } 
            else {
                System.out.println(">>>>>>> ;-) id=" + request.getSession().getId() + ", " + projekt.basePath + projekt.name);

                Hashtable<String, CollabProject> allCharts = (Hashtable<String, CollabProject>) application.getAttribute("allCharts");
                if (allCharts == null) {
                    allCharts = new Hashtable<String, CollabProject>();
                    application.setAttribute("allCharts", allCharts);
                }
                // BEIM ANLEGEN WIR DIE SESSION ID beutzt, dann immer die ChartID ...
                

                allCharts.put(chartID, projekt);


                
                sb.append("<ul>");

                sb.append("<li>" + projekt.name);
                sb.append("</li>");
                sb.append("<li>" + projekt.basePath);
                sb.append("</li>");

                sb.append("</ul>");

                sb.append("<hr><a href='index.jsp?section=RRTB&chartID="+ chartID +"'>Weiter</a> ");
                
                for (Cookie c : request.getCookies()) {
                    System.out.println(c.getDomain() + " " + c.getName() + " " + c.getValue());

                }
            }
        %>
        </br>
        <hr />
        <%=sb.toString()%>

          
    </body>
</html>
