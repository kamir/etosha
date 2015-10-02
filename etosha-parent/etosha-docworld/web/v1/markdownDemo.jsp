
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

<%@page import="org.markdown4j.Markdown4jProcessor"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FAQ Item-Viewer Markdown DEMO Page</title>
    </head>
    <body>
        <h1>ID: <%= request.getParameter("id") %></h1>
        <hr>
        
        <%
        
        String test1 = "This is ~~deleted~~ text";
        
        String test2 = "``` ruby\n"+
                       "if (a > 3) {\n"+
                       "moveShip(5 * gravity, DOWN);\n"+
                       "}\n"+
                       "```";
        
        String test3 = "<@Am Anger 27, Stollberg> => <@Müchelner Str. 23, Frankleben>";
        
        String test4 = "%%% yuml style=nofunky scale=120 format=svg\n"+
                        "[Customer]<>-orders*>[Order]\n"+ 
                        "[Order]++-0..*>[LineItem]\n"+
                        "[Order]-[note:Aggregate root.]\n"+
                        "%%%\n";
        
        String test5 = "%%% sequence style=modern-blue\n"+
"title Authentication Sequence\n"+
"Alice->Bob: Authentication Request\n"+
"note right of Bob: Bob thinks about it\n"+
"Bob->Alice: Authentication Response\n"+
"%%%\n";
        
        String html1 = new Markdown4jProcessor().process( test1 );
        String html2 = new Markdown4jProcessor().process( test2 );
        String html3 = new Markdown4jProcessor().process( test3 );
        String html4 = new Markdown4jProcessor().process( test4 );
        String html5 = new Markdown4jProcessor().process( test5 ); 
        
        %>
        <hr>
        
        <%= html1 %><br><hr>
        <%= html2 %><br><hr>
        <%= html3 %><br><hr>
        <%= html4 %><br><hr>
        <%= html5 %>
        
        <hr>
        <p><b>Note:</b> We use <i>MARKDOWN</i> syntax to render the content of textfields.<br><br>
                    See: <a href="https://code.google.com/p/markdown4j/">https://code.google.com/p/markdown4j/</a> for more information.
        
        
        
    </body>
</html>
