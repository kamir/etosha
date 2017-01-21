<%@page import="com.cloudera.dal.HBaseMatchStoreAdapter"%>
<%@page import="com.cloudera.search.queries.QueryConstants"%>
<%@page import="com.cloudera.dal.admin.SoleraPMTabAdmin"%>
<%@page import="com.cloudera.dal.partsmatch.Match"%>
<%@page import="com.cloudera.dal.partsmatch.MatchStore"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Properties"%> 

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

        String note = "Created a new HBase-Connector ";
 
        MatchStore ms = (MatchStore)request.getServletContext().getAttribute("store");
        
        // do we have a connector available?
        //MatchStore ms = (MatchStore)session.getAttribute("store");
        
        // now we force to reinit with a new table ...
        HBaseMatchStoreAdapter.hba = null;
        ms.adapter = null;
        ms = null;

        // Parametrization of the WEB-UI ...
        String tn = request.getParameter("tn");
        String cn = request.getParameter("cn");
        String ln = request.getParameter("lang");
        
        
        if ( tn == null ) {
            tn = "tableName";
        } 
        if ( cn == null ) {
            cn = "collection";
        } 
        if ( ln == null ) {
            ln = "language";
        } 
            
        note = note + "<br>with new table reference: tn=" + tn;
        note = note + "<br>with cllection reference: cn=" + cn;
        note = note + "<br>for language code       : langCoden=" + ln;

        /**
         * GLOBAL changes for the whole APP-Server:
         * 
         *   the only table name for the only possible HBaseConnector 
         *   in this app.
         */
        SoleraPMTabAdmin.docTabName = tn;
        
        /**
         * GLOBAL changes for the whole APP-Server:
         * 
         *   the only collection name for the only possible QueryConstants 
         *   in this app.
         */
        QueryConstants.defaultMatchingCollection = cn;
        QueryConstants.collectionName = cn;
        QueryConstants.lang = ln;
        
        Match.defaultLang = ln;
        
        if (ms == null) {

            ms = new MatchStore();
            ms.init();

            request.getServletContext().setAttribute("store", ms);
            
                    
            request.getServletContext().setAttribute("store", ms);
            request.getServletContext().setAttribute("tablename", tn);

            String hueHBaseLink = "http://dl-mn04-d.axadmin.net:8888/hbase/#HBase/" + tn;

            request.getServletContext().setAttribute("tableURL", hueHBaseLink);


        } 
        else {

            note = "HBase-Connector is already active.";
        
        }

        String lang  = request.getParameter("lang");
        
        if ( lang == null ) 
            lang = "TEST";
        %>
        <b>Connector type:</b> <%= ms.getClass() %>
        <hr>
        <h4><%=note %></h4>
        
        <hr>
        
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
        
    </body>
</html>
