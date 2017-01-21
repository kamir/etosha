<%@page import="com.cloudera.dal.partsmatch.MatchingResultContainer"%>
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
        <title>Show Session and Connector Status</title>

    </head>
    <body>
        <%
            
        MatchStore ms = (MatchStore)request.getServletContext().getAttribute("store");
        String type = "n.a.";

        if (ms != null) {
            type = "[" + ms.getClass().getName() + "]";
        } 
        
        String tn = MatchStore.getTableName();
        int z = MatchStore.getNrOfRows();
        
        %>

        <h3>HBase-Connector:</h3>
        
        <b>Connector type :</b> <i><%=type %></i><br>
        <b>Table Name :</b> <i><%=tn %></i><br>
        <b>Nr of MatchResults :</b> <i><%=z %></i><br>
        
        
        <hr>
        
        <%
        
        MatchingResultContainer mrc = (MatchingResultContainer)session.getAttribute("MRC");
        
        %>
        <h3>Matching Result-Container:</h3>
        <b>Nr of items:</b>[<%= mrc.getSize() %>]
        <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
             
    </body>
</html>
