
<%@page import="com.cloudera.dal.partsmatch.MatchStore"%>
<%@page import="java.util.Properties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

 <%

        String note = "Created a new HBase-Connector ...";
        ServletContext context = config.getServletContext();

        MatchStore ms = (MatchStore)context.getAttribute("store");

        if (ms == null) {

            ms = new MatchStore();
            ms.init();;

            context.setAttribute("store", ms);

        } 
        else {

            note = "HBase-Connector is already active.";
        
        }

        String lang  = request.getParameter("lang");
        
        if ( lang == null ) 
            lang = "TEST";


        %>



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

        <link rel="stylesheet" href="../../css/bootstrap.min.css">
        <style>
            body {
                padding-top: 5px;
                padding-bottom: 5px;
            }
        </style>
        <link rel="stylesheet" href="../../css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="../../css/main.css">

        <script src="../../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../../js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>

        <link rel="canonical" href="http://www.bootstraptoggle.com">

        <link href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.3/styles/github.min.css" rel="stylesheet" >
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="../../js/vendor/bootstrap-toggle-master/css/bootstrap-toggle.css" rel="stylesheet">
        <link href="../../js/vendor/bootstrap-toggle-master/doc/stylesheet.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>

   

 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>FAQ-Item Editor</title>
        </head>
        <body>

            <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="./../../index.html"><span class="glyphicon glyphicon-home" style="color:gray"></span> <font color="#00bfff"><b>Q</b>n<b>A</b></font><font color="white">manager</font> </a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <font size="-2" color="white"><b>QA-Item</b> ID: <b><%="ABC"%></b><br> Item available in HBase: <b><%= "CDE" %></b></font>
                    </div><!--/.navbar-collapse -->
                </div>
            </div>


            <!-- Main jumbotron for a primary marketing message or call to action -->
            <div class="jumbotron">
                <div class="container">


                    <h3>MatchResult Item Editor</h3>


             
                    <br>

                    <div  align="center"> 

                        [<a href="showItem.jsp?id=<%= "ABC"%>"><b>Show</b></a> stored QA-Item] [<a href="http://training09.mtv.cloudera.com:8888/search/?collection=20000004"><b>Search</b></a> other Items]
                        <form action="/DocWorld/saveItem" method="PUT">
                            <hr>
                            <%= "B1"%> | <%= "B2"%> | <%= "B3"%> | <%= "B4"%>  <br>

                            <input type="checkbox" <%="V1"%> name="cbValid" data-toggle="toggle" data-on="<img src='http://semanpix.de/opendata/wiki/images/0/03/QAItem_isValid_TRUE.png' with='24' height='24'></img>" data-off="<img src='http://semanpix.de/opendata/wiki/images/2/29/QAItem_isValid_FALSE.png' with='24' height='24'>"> &nbsp;&nbsp;&nbsp;
                            <input type="checkbox" <%="V2"%> name="cbAnswered" data-toggle="toggle" data-on="<img src='http://semanpix.de/opendata/wiki/images/2/22/QAItem_isAnswered_TRUE.png' with='24' height='24'></img>" data-off="<img src='http://semanpix.de/opendata/wiki/images/8/8e/QAItem_isAnswered_FALSE.png' with='24' height='24'>"> &nbsp;&nbsp;&nbsp;
                            <input type="checkbox" <%="V3"%> name="cbPublic" data-toggle="toggle" data-on="<img src='http://semanpix.de/opendata/wiki/images/5/5a/QAItem_isPublic_TRUE.png' with='24' height='24'></img>" data-off="<img src='http://semanpix.de/opendata/wiki/images/a/aa/QAItem_isPublic_FALSE.png' with='24' height='24'>"> 
                            <input type="checkbox" <%="v4"%> name="cbPublished" data-toggle="toggle" data-on="<img src='http://semanpix.de/opendata/wiki/images/1/11/QAItem_isPublished_TRUE.png' with='24' height='24'></img>" data-off="<img src='http://semanpix.de/opendata/wiki/images/c/ca/QAItem_isPublished_FALSE.png' with='24' height='24'>"> 

                            </div>





                            <input type="hidden" name="id" value="<%="ABC"%>">
                            <table border="1" style="width:100%">
                                <thead>
                                    <tr>

                                    </tr>

                                    <tr>
                                        <th style="width:50%">Question:</th>
                                        <th style="width:50%">Answer:</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><input type="submit" class="btn btn-info" value="save" /></td>
                                        <td  align="right"><input type="submit" class="btn btn-info" value="save" /></td>
                                    </tr>

                                    <tr>
                                        <td><textarea wrap="on" name="question" rows="30" cols="20" style="width:100%"><%= "A"%></textarea></td>
                                        <td><textarea wrap="on" name="answer" rows="30" cols="20" style="width:100%"><%= "B"%></textarea></td>
                                    </tr>
                                    <!--tr>
                                        <td>Tags:</td>
                                        <td>Background search:</td>
                                    </tr-->
                                    <tr>
                                        <td> </td>
                                        <td>
                                            <!--hr>
                                            1) <b>GrepCode</b> <br>
                                            <a href="http://grepcode.com/search/?query=Writable" target="_blank"> 
                                                http://grepcode.com/search/?query=Writable </a>  ...
                                                <hr>
                                            2) <b>Doc Pool</b> <br>
                                            <a href="http://TC/searchDP/?query=Writable" target="_blank"> 
                                                http://TC/searchDP/?query=Writable </a>  ...  -->
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><input type="submit" class="btn btn-info" value="save" /></td>
                                        <td align="right"><input type="submit" class="btn btn-info" value="save" /></td>
                                    </tr>

                                </tbody>

                                <br>  



                            </table>
                        </form>
                        <hr>



                        <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.3/highlight.min.js"></script>
                        
                        <script src="../../js/vendor/bootstrap-toggle-master/doc/script.js"></script>
                        
                        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
                        
                        <script src="../../js/vendor/bootstrap-toggle-master/js/bootstrap-toggle.js"></script>



                        </body>
                        </html>
