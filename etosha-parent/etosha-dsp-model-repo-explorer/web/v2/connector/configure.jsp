<%@page import="com.cloudera.dal.HBaseMatchStoreAdapter"%>
<%@page import="com.cloudera.search.queries.QueryConstants"%>
<%@page import="com.cloudera.dal.admin.SoleraPMTabAdmin"%>
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
        <title>Configure the HBase-Connector</title>
    </head>
    <body>
        <h3>Configure the HBase-Connector</h3>
        <hr>
        // Parametrization of the WEB-UI ...<br>
        String tn = request.getParameter("tn");<br>
        String cn = request.getParameter("cn");<br>
        String ln = request.getParameter("lang");<br>
        <hr>
        
        <form name="connector cfg form" action="initHBaseConnector.jsp">
        
        <table border="1">
            <thead>
            </thead>
            <tbody>
                <tr>
                    <td style="width:250px">Collection name (Haystack) :</td>
                    <td style="width:250px"><input type="text" name="cn" value="common_raw_solic_partsmatch_V2_" style="width:245px;"/></td>
                    <td style="width:250px">Add the language tag (lower case) or use a completely different name.</td>
                </tr>
                <tr>
                    <td>Table name : (Matching Results) : </td>
                    <td><input type="text" name="tn" value="tabBTest" style="width:245px;"/></td><td>Enter the table name. Maybe you have to add only the language code in capital letters.</td>
                </tr>
                <tr>
                    <td>Language (lang) : </td>
                    <td><input type="text" name="lang" value="any" style="width:245px;"/></td><td>Provide the language code.</td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Submit" /></td><td></td>
                </tr>
            </tbody>
        </table>

        </form>

        <hr>
        
               [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
               [ <a href="index.jsp">Index page</a> ]
        
    </body>
</html>
