<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet" href="../css/bootstrap.min.css">
        <style>
            body {
                padding-top: 5px;
                padding-bottom: 5px;
            }
        </style>
        <link rel="stylesheet" href="../css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="../css/main.css">

        <script src="../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
    </head>

<%-- 
    Document   : showItem
    Created on : Feb 18, 2015, 3:58:45 AM
    Author     : kamir
--%>

<%@page import="java.util.Properties"%>
<%@page import="data.io.adapter.ObjectEncoder"%>
<%@page import="hadoop.cache.doc.HBaseDocWorld"%>
<%@page import="org.markdown4j.Markdown4jProcessor"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FAQ-Manager</title>
    </head>
    <body>
        
        <h3>FAQ-Team</h3>
        [<a href="https://docs.google.com/spreadsheets/d/1thIL4hd4-TISvIXkkSzOeuCja6NMyVGsGe9OM3Aw2Qs/edit#gid=0">edit team list </a>]
        
        <hr/>
        
        <table border="1">
           <thead>
           </thead>
           <tbody>
               <tr>
                   <td>   
     <h3>HBase-Connector</h3>
       [<a href="initHBaseConnector.jsp">init HBase-Connector</a>]<br/> 
       [<a href="resetHBaseConnector.jsp">reset HBase-Connector</a>]
     
            <h3>Import</h3>
       [<a href="./../static/importItemsFromGD.html">incremental Import from Web-Form</a>]<br/> 
       [<a href="importSkypeChatSnippets.jsp">import Skype-Chat snippets</a>]<br/> 
       [<a href="importTaggedMails.jsp">import Emails with tags</a>]<br/> 
     </td>

       <td>
           
                  <h3>Validation</h3>
       [<a href="runValidationTool.jsp">automatic validation of items</a>]<br/> 
       [<a href="startReview.jsp">run review request</a>]<br/> 

       <h3>Analysis</h3>
       [<a href="runKBAnalyser.jsp">analyse the KB data and Meta-Data</a>]<br/> 

       
       
       </td>
       <td>
           
             <h3>Package</h3>
       [<a href="createNewOfflinePackage.jsp">create a new offline-index </a>]<br/> 
     
       </td>
               </tr>
           </tbody>
       </table>



       <hr>
        
        
    </body>
</html>
