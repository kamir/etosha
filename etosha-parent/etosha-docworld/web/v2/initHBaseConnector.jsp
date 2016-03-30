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
    Document   : initHBaseConnector
    Created on : Feb 18, 2015, 5:34:49 AM
    Author     : kamir
--%>
 
<%@page import="hadoop.cache.doc.HBaseDocWorld"%>
<%@page import="hadoop.cache.ts.HBaseTimeSeriesCache"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Initialize the HBase-Connector</title>
    </head>
    <body>
        <h3>Initialize the HBase-Connector</h3>
        <%

            String note = "Created a new connector ...";
            ServletContext context = config.getServletContext();

            Object odw = context.getAttribute( "dw" );
            
            HBaseDocWorld dw = null;
            
            if ( odw == null ) {
                
                dw = new HBaseDocWorld();
                dw.init();
                
                context.setAttribute( "dw", dw );
                odw = dw;
            }
            else {
                
                note = "Connector is already active.";
                dw = (HBaseDocWorld) odw;
                
                dw.put("abc", "123");
                note = note + " => " + dw.hasKey("abc");
                
            }
        %>
        <b>Connector type:</b> <%= dw.getClass() %>
        <hr>
        <h4><%=note %></h4>
        <hr>
        
        <a href="showItem.jsp">SHOW</a>
        
        
    </body>
</html>
