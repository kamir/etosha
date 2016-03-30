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




        <!--link rel="stylesheet" href="../css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="../css/main.css">
        <script src="../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="../js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script-->

        <link rel="canonical" href="http://www.bootstraptoggle.com">

        <link href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.3/styles/github.min.css" rel="stylesheet" >
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
        <link href="../js/vendor/bootstrap-toggle-master/css/bootstrap-toggle.css" rel="stylesheet">
        <link href="../js/vendor/bootstrap-toggle-master/doc/stylesheet.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
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

    <%

        String note = "Created a new connector ...";
        ServletContext context = config.getServletContext();

        Object odw = context.getAttribute("dw");

        HBaseDocWorld dw = null;

        if (odw == null) {

            dw = new HBaseDocWorld();
            dw.init();

            context.setAttribute("dw", dw);
            odw = dw;
        } else {

            note = "Connector is already active.";
            dw = (HBaseDocWorld) odw;
        }

        String itemId = request.getParameter("id");

        if (itemId != null) {
            itemId = java.net.URLDecoder.decode(itemId);
        }

        if (itemId == null) {
            itemId = "1/6/2015 13:24:53_mirko.kaempf@cloudera.com_14";
        }

    %>
    <!DOCTYPE html>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>SOLER :: Part Matching Item Viewer</title>
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
                        <a class="navbar-brand" href="./../index.html"><span class="glyphicon glyphicon-home" style="color:gray"></span> <font color="#00bfff"><b>Q</b>n<b>A</b></font><font color="white">manager</font> </a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <font size="-2" color="white"><b>QA-Item</b> ID: <b><%=itemId%></b><br> Item available in HBase: <b><%= dw.hasKey(itemId)%></b></font>
                    </div><!--/.navbar-collapse -->
                </div>
            </div>


            <!-- Main jumbotron for a primary marketing message or call to action -->
            <div class="jumbotron">
                <div class="container">

                    <h3>FAQ-Item Viewer</h3>
                    <div align="center"> 
                        [<a href="editItem.jsp?id=<%= itemId%>"><b>EDIT</b></a> this item] [<a href="http://training09.mtv.cloudera.com:8888/search/?collection=20000004"><b>SEARCH</b></a> other items]

                        <hr>
                        <%

                            String record = dw.getDocStructured(itemId);

                            // here we receive some Base64 encoded Strings in a PropertyFile
                            Properties props = (Properties) ObjectEncoder.fromString(record);

                            String question = props.getProperty("question");

                            String answer = props.getProperty("answer");

                            String illu = props.getProperty("illu");

                            String isValid = props.getProperty("isValid");
                            String isAnswered = props.getProperty("isAnswered");
                            String isPublic = props.getProperty("isPublic");
                            String isPublished = props.getProperty("isPublished");
                            
                            String lIsValid = props.getProperty("isValid");
                            String lIsAnswered = props.getProperty("isAnswered");
                            String lIsPublic = props.getProperty("isPublic");
                            String lIsPublished = props.getProperty("isPublished");



                            String validChecked = "";
                            String answeredChecked = "";
                            String publicChecked = "";
                            String publishedChecked = "";


                            System.out.println("### " + itemId);
                            System.out.println("> isValid     : " + isValid);
                            System.out.println("> isAnswered  : " + isAnswered);
                            System.out.println("> isPublic    : " + isPublic);

                            if (isValid == null) {
                                isValid = "0";
                            }

                            if (isValid.equals("1")) {
                                validChecked = "checked";
                                lIsValid = "validated";
                            } else {
                                lIsValid = "not validated";
                            }

                            if (isAnswered == null) {
                                isAnswered = "0";
                            }

                            if (isAnswered.equals("1")) {
                                answeredChecked = "checked";
                                lIsAnswered = "answered";
                            } else {
                                lIsAnswered = "not answered";
                            }

                            if (isPublic == null) {
                                isPublic = "0";
                            }

                            if (isPublic.equals("1")) {
                                publicChecked = "checked";
                                lIsPublic = "public";
                            } else {
                                lIsPublic = "not public";
                            }
                            
                            if (isPublished.equals("1")) {
                                publishedChecked = "checked";
                                lIsPublished = "published";
                            } else {
                                lIsPublished = "not published";
                            }

                            System.out.println("> C Valid     : " + validChecked);
                            System.out.println("> C Answered  : " + answeredChecked);
                            System.out.println("> C Public    : " + publicChecked);
                            System.out.println("> C Published : " + publishedChecked);

                            String html1 = new Markdown4jProcessor().process(question);
                            String html2 = new Markdown4jProcessor().process(answer);
                            //String html3 = new Markdown4jProcessor().process( illu );

                        %>

                        <table border="1" style="width:100%">
                            <thead>
                                <tr><%                    if ( isValid.equals("1") ) {
                                        isValid = "<img src='http://semanpix.de/opendata/wiki/images/0/03/QAItem_isValid_TRUE.png' with='24' height='24'></img>";
                                    } else {
                                        isValid = "<img src='http://semanpix.de/opendata/wiki/images/2/29/QAItem_isValid_FALSE.png' with='24' height='24'>";
                                    }

                                    if (isAnswered.equals("1") ) {
                                        isAnswered = "<img src='http://semanpix.de/opendata/wiki/images/2/22/QAItem_isAnswered_TRUE.png' with='24' height='24'/>";
                                    } else {
                                        isAnswered = "<img src='http://semanpix.de/opendata/wiki/images/8/8e/QAItem_isAnswered_FALSE.png' with='24' height='24'/>";
                                    }

                                    if (isPublic.equals("1") ) {
                                        isPublic = "<img src='http://semanpix.de/opendata/wiki/images/5/5a/QAItem_isPublic_TRUE.png' with='24' height='24'></img>";
                                    } else {
                                        isPublic = "<img src='http://semanpix.de/opendata/wiki/images/a/aa/QAItem_isPublic_FALSE.png' with='24' height='24'></img>";
                                    }
                                    
                                    if (isPublished.equals("1") ) {
                                        isPublished = "<img src='http://semanpix.de/opendata/wiki/images/1/11/QAItem_isPublished_TRUE.png' with='24' height='24'></img>";
                                    } else {
                                        isPublished = "<img src='http://semanpix.de/opendata/wiki/images/c/ca/QAItem_isPublished_FALSE.png' with='24' height='24'></img>";
                                    }


                                    %><br>

                            </div>
                            </tr>
                            <tr>
                                <th style="width:50%">Question: </th>
                                <th style="width:50%">Answer: </th>
                            </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><%= html1%></td>
                                    <td><%= html2%></td>
                                </tr>
                                <!--tr>
                                    <td>Tags:</td>
                                    <td>Background search:</td>
                                </tr>
                                <tr>
                                    <td> </td>
                                    <td>
                                        <hr>
                                        1) <b>GrepCode</b> <br>
                                        <a href="http://grepcode.com/search/?query=Writable" target="_blank"> 
                                            http://grepcode.com/search/?query=Writable </a>  ...
                                            <hr>
                                        2) <b>Doc Pool</b> <br>
                                        <a href="http://TC/searchDP/?query=Writable" target="_blank"> 
                                            http://TC/searchDP/?query=Writable </a>  ... 
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                    </td>
                                    <td></td>
                                </tr-->

                            </tbody>
                        </table>
                        <br>

                        <%= isValid%> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= isAnswered%>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= isPublic%> &nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= isPublished%>   
                        <br>   <%= lIsValid%> | <%= lIsAnswered%> | <%= lIsPublic%> | <%= lIsPublished%>  <br>


                        <hr>
                        | <a href="http://www.w3schools.com/bootstrap/bootstrap_grid_system.asp"><b>Bootstrap </b>help</a> | 
                        More about <a href="https://code.google.com/p/markdown4j/"> <b>Markdown</b> </a> | 


                    </div>
                </div>
            </div>

            <div class="container">
                <!-- Example row of columns -->
                <div class="row">
                    <div class="col-md-4">
                        <h2>Questions and Answers</h2>
                        <p>If you found interesting answers to good questions, worth spreading, add it to the first data layer via
                            this simple form.</p>
                        <p>You can review and edit the table using <a href="https://docs.google.com/a/cloudera.com/spreadsheet/ccc?key=0AnwImjy2siFOdFRSQmwxS2ctZEhZaGZQR21ZdmVpM2c#gid=0">Google-Docs</a>.</p>
                        <p><a class="btn btn-success" href="./../static/googleform.html" role="button">Start asking &raquo;</a></p>
                    </div>
                    <div class="col-md-4">
                        <h2>Review</h2>
                        <p>Come in, and review existing topics! The more we think about the content and the more iterations we conduct, the better
                            our content will be.</p><p>Well understood questions, with valid answers, and cleaned from all
                            sensitive information are usefull in our training. Such <b>QA</b> items is what we collect.</p>
                        <p><a class="btn btn-default" href="./../v1/startReview.jsp" role="button">Improve the content &raquo;</a></p>
                    </div>
                    <div class="col-md-4">
                        <h2>Analysis</h2>
                        <p>What are the hot topics? What relations can be found between all the questions, and
                            how are the topics evolving over time?</p> Automatic analysis can be the best starting point for our 
                        next generation of training materials. Why not using this approach also as a general QA solution in workshops and consulting projects.</p>

                        <p><a class="btn btn-default" href="./../v1/startAnalysis.jsp" data-toggle="modal"
                              data-target="#modal_STATS" role="button">Find more facts &raquo;</a></p>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.3/highlight.min.js"></script>
    <script src="../js/vendor/bootstrap-toggle-master/doc/script.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script src="../js/vendor/bootstrap-toggle-master/js/bootstrap-toggle.js"></script>
</body>
</html>
