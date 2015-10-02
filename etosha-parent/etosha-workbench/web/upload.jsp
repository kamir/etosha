<%@page import="tools.SVNTool"%>
<%@page import="data.CollabProject"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@ page import="be.telio.mediastore.ui.upload.MonitoredDiskFileItemFactory" %>
<%@ page import="be.telio.mediastore.ui.upload.UploadListener" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.FileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.FileUploadException" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
/* Licence:
*   Use this however/wherever you like, just don't blame me if it breaks anything.
*
* Credit:
*   If you're nice, you'll leave this bit:
*
*   Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
*   email : plosson@users.sourceforge.net
*/
--%>
<%
    UploadListener listener = new UploadListener(request, 30);
    
    CollabProject projekt = (CollabProject)request.getSession().getAttribute("chartProjekt");
 
    String projectName = "P0/default";
    if ( projekt == null ) { 
        projekt = new CollabProject();
        projekt.name = projectName;
        request.getSession().setAttribute("chartProjekt", projekt );
        System.out.println("---> Projekt: " + projekt.name +" (neu initialisiert)" ); 
    }
    else {
       System.out.println(">>>> Projekt: " + projekt.name +" (für UPLOAD)"); 
    }
    
    // Create a factory for disk-based file items
    FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);

    // Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);

    StringBuffer sb = new StringBuffer();
    sb.append("<ul>");
    try
    {
        
        
        String currentFileName = "";
        // process uploads ..
        List<FileItem> liste = upload.parseRequest(request);
        
        // Process the uploaded items
        Iterator iter = liste.iterator();
        while (iter.hasNext()) {
            
            sb.append("<li>");
            
            FileItem item = (FileItem) iter.next();

            File folder = new File( projekt.basePath + projectName );
            if ( !folder.exists() ) { 
                folder.mkdirs();
                
                SVNTool.createFolder( folder, projekt );
            };
                
            if (item.isFormField()) {
                // FORM FIELD
                String name = item.getFieldName();
                String value = item.getString();
                
                if ( name.equals("name") ) {
                    projectName = value;
                    sb.append( "<b>Name:</b> " + value + "</li>");
                    projekt.name = value;
                    request.getSession().setAttribute("chartProjekt", projekt);
                }
            } 
            else {
                // FILE
                // Process a file upload
                String name = item.getFieldName();
                String value = item.getName();
                
                sb.append( "<b>"+ name + "</b>:" + value + "</li>");
 
                File uploadedFile = new File( projekt.basePath + projectName + "/" + value );
                if ( item.getSize() > 0 ) {
                    item.write(uploadedFile);
                    SVNTool.addFile( uploadedFile, projekt );
                }
                
            }
           
        }
        sb.append("</li>");
        
    }
    catch (FileUploadException e)
    {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    sb.append("</ul>");
%>
<html>
<head><title>Done</title></head>
<body>
    <h3>Upload beendet ...</h3>
    </br>
    <hr />
    <%=sb.toString() %>
    <hr/>
<a href="index.jsp">Weiter</a> ...
</body>
</html>