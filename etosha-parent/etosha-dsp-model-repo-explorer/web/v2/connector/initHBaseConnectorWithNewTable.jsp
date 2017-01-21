<%@page import="com.cloudera.dal.partsmatch.MatchConfig"%>
<%@page import="com.cloudera.dal.partsmatch.Match"%>
<%@page import="com.cloudera.dal.partsmatch.MatchStore"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Properties"%> 

<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Initialize the HBase-Connector</title>
    </head>
    <body>
        <h3>Initialize the HBase-Connector</h3>
        
        <%

        String note = "Created a new HBase-Connector with a new table.<br>";
        
        String ts = "" + System.currentTimeMillis(); 
        String tn = "newTable_" + ts;

        /**
         * This args can be taken from "WebApp-Context" ...
         */
        String[] args = { "dl-mn04-d,dl-mn02-d,dl-mn03-d", tn };
        
        MatchStore.reset();
        
        MatchConfig.init( args );

        MatchStore ms = MatchStore.forceInit();
        
        request.getServletContext().setAttribute("store", ms);
        request.getServletContext().setAttribute("tablename", tn);
          
        String hueHBaseLink = "http://dl-mn04-d.axadmin.net:8888/hbase/#HBase/" + tn;
        
        request.getServletContext().setAttribute("tableURL", hueHBaseLink);
        
        %>
        <b>Connector type:</b> <%= ms.getClass() %>
        <hr>
        <%=note %>
        <b>tablename: </b> <a href="<%=hueHBaseLink %>"><%=tn %></a>
        <hr>
        
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
        
    </body>
</html>
