<%@page import="com.cloudera.search.SecureClient"%>
<%@page import="com.cloudera.search.QueryConstants"%>
<%@page import="com.cloudera.dal.emdm.*"%>
 

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

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <style>
            body {
                padding-top: 5px;
                padding-bottom: 5px;
            }
        </style>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/main.css">

        <script src="js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="js/vendor/bootstrap-modal/bootstrap-modal.pack.js"></script>
        <script src="js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
<body>
    
    
        <%
         
            SecureClient.login();
            
            String homelink = request.getParameter("HL");
            
            // define the backlink on JSP pages to the place called home.
            // if ( homelink == null ) homelink = "v2";
            if ( homelink == null ) homelink = ".";
            
            String root = request.getContextPath();
            
            session.setAttribute("HOMELINK", root + "/" + homelink);
        
            // bring in some example data ...
//            MatchingResultContainer mrc = new MatchingResultContainer();
            
//            mrc.addResult( MatchingResult.getTestMatchSamples1());
//            mrc.addResult( MatchingResult.getTestMatchSamples2());
//            mrc.addResult( MatchingResult.getTestMatchSamples3());
            
//            session.setAttribute( "MRC", mrc );
            
         %>   
    
    
    
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation" >
      <div class="container">
        <div class="navbar-header ">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
            <a class="navbar-brand" href="index.jsp"><span class="glyphicon glyphicon-home" style="color:gray"></span> <font color="#ffb70c"><b>Lufthansa</b></font> <font color="white">EMDM</font> </a>
        </div>
        <div>
        <ul class="nav navbar-nav">
                  <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
               <span class="glyphicon glyphicon-question-sign" style="color:#ffb70c"></span> Inventory     
               <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
               <li><a href="v2/feedback/showItem.jsp"><span class="glyphicon glyphicon-play" style="color:#ffb70c"></span> Show Items</a></li>
               <li><a href="v2/feedback/editItem.jsp"><span class="glyphicon glyphicon-edit" style="color:#ffb70c"></span> Edit Item</a></li>
            </ul>
         </li>   
         <li><a href="#"><span class="glyphicon glyphicon-road" style="color:#ffb70c"></span> <b>Guide</b></a></li>
         <li><a href="#"><span class="glyphicon glyphicon-cog" style="color:#ffb70c"></span> Export</a></li>
         <li><a href="#"><span class="glyphicon glyphicon-dashboard" style="color:#ffb70c"></span> Analysis</a></li>
         <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
               <span class="glyphicon glyphicon-wrench" style="color:#ffb70c"></span> Administration    
               <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
               <!--li><a href="v1/kb/status.jsp"><span class="glyphicon glyphicon-signal" style="color:#e96f19"></span> Show Knowledgebase status</a></li>
               <li><a href="v1/startReview.jsp"><span class="glyphicon glyphicon-search" style="color:#e96f19"></span> Run review request generator</a></li>
               <li><a href="v1/indextool/packageIndex.jsp"><span class="glyphicon glyphicon-book" style="color:#e96f19"></span> Package index</a></li>
               <li class="divider"></li-->
               <li><a href="v2/manage.jsp"><span class="glyphicon glyphicon-search" style="color:#ffb70c"></span> Manage the tool-suite </a></li>
               <li><a href="v2/connector/configure.jsp"><span class="glyphicon glyphicon-open" style="color:#ffb70c"></span> Initialize HBase connector ...</a></li>
               <li><a href="v2/connector/resetHBaseConnector.jsp"><span class="glyphicon glyphicon-refresh" style="color:#ffb70c"></span> Reset HBase connector ...</a></li>
               <li class="divider"></li>
               <li><a href="http://127.0.0.1:8161/admin/queues.jsp">Active-MQ</a></li> 
               <li class="divider"></li>
               <li><a href="http://172.24.128.56:7180/cmf/home">Cloudera Manager</a></li> 
               <li><a href="http://127.0.0.1:7187">Cloudera Navigator</a></li> 
               <li class="divider"></li>
               <li><a href="http://172.24.128.52:8888/accounts/login">HUE</a></li> 
               
               <li class="divider"></li>
               <li><a href="http://172.24.128.52:8888/search/?collection=50338">SOLR-Index (dev_smileapp_collection)</a></li>
               <li><a href="http://172.24.128.52:8888/search/?collection=50354">SOLR-Index (prod_smileapp_collection)</a></li>
               <li><a href="http://172.24.128.52:8888/search/?collection=50343">SOLR-Index (md_col_02a)</a></li>
            </ul>
         </li>
      </ul>  
          </div>

      </div>
    </div>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
          <h1><font style="color:#ffb70c"><b>Lufthansa</b></font></h1>
          <h5><font style="color:#000066">Enterprise Metadata Management</font></h5>  
          <h3></h3>
          
     <hr style="border:solid #ffffff 1px;
background-color:#000000;height:1px;width:1400px;
text-align:left;">

                  <div class="navbar-collapse collapse">
                      
          <form class="navbar-form navbar-left" role="form">
            <div class="form-group" >
      Context: 
               <select id="lang" class="form-selectpicker">
                     <optgroup label="Layer 0">
    <option>Hosts</option>
    <option>Services</option>
    <option>Dashboards</option>
  </optgroup>
                     <optgroup label="Layer 1">
    <option>Navigator-Search</option>
    <option>Navigator-Policy</option>
    <option>Navigator-Analysis</option>
  </optgroup>

                       <optgroup label="Layer 2">
    <option>Use-Case 1</option>
    <option>Use-Case 2</option>
    
  </optgroup>
  </optgroup>

  
</select>
      <br>
              <input id="claim" type="text" size="65" placeholder="*:*"value="" class="form-control" >
              
                   <a href="" class="btn btn-lg btn-default"
   data-toggle="modal"
   data-target="#modal_GO" onclick="ql();" ><b>search</b> </a>
              <script> function ql() { 
                  
                  var lang = document.getElementById('lang').value;
                  var claim = document.getElementById('claim').value;
                
                  document.getElementById('qqinfo').innerHTML = "<b>Claim</b> : " + claim + " <b>Lang </b> : " + lang ;
                  
                  var url = "v2/matching/requestClaimFromHBase.jsp?claim=" + claim + "&lang="+lang; 
                  
                  document.getElementById('QSR').src = url; 
                  
                  document.getElementById('frameURL').innerHTML = url; 
              
} </script>   
              
            </div>
 <br><br>
              
            <div>
                      <BR/>
              <br/>
              
              <BR/>
              <br/>
              
                  <a href="" class="btn btn-lg btn-default"
   data-toggle="modal"
   data-target="#modal_GO" onclick="uc1();" >Use-Case <b>1</b><br/><br/><font style="color:#000066"><b>SMILE-App</b><br/> [DASHBOARD]  </font></a>
              <script> function uc1() { 
                  
                  document.getElementById('qqinfo').innerHTML = "<b>Use Case 1</b>";
                  
                  var url = "v1/uc1/simpleDashboard.jsp"; 
                  
                  document.getElementById('QSR').src = url; 
                  document.getElementById('frameURL').innerHTML = url; 

} </script>
              
       
              
<a href="" class="btn btn-lg btn-default"
   data-toggle="modal"
   data-target="#modal_GO" onclick="uc2();" >Use-Case <b>2</b><br/><br/><font style="color:#000066"><b>Model QA</b><br/> [DASHBOARD]   </font></a>
              <script> function uc2() { 
                  
                  
                  document.getElementById('qqinfo').innerHTML = "<b>Use Case 2</b>";
                  
                  var url = "v1/uc2/simpleDashboard.jsp"; 
                  
                  document.getElementById('QSR').src = url; 
                  document.getElementById('frameURL').innerHTML = url; 

} </script>
              
            </div>
              
              
          </form>
        </div><!--/.navbar-collapse -->
        <hr>
 
        
            
         <hr>
      </div>
    </div>
    <font style="color:#000066"> 
    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4">
          <h2>Data Dictionary</h2>
          <p>Automatic <b>extraction of metadata</b> from datasets and process provides valuable information for
              process monitoring. Domain experts add feedback and additional information for better accuracy.</p>
          <p>An <b>SME</b> can review and edit the metadata.</p>
          <p><a class="btn btn-default" href="https://im-wiki.lufthansa.com/display/ENA/Use-Cases" role="button">Improve metadata quality &raquo;</a></p>
        </div>
        <div class="col-md-4">
          <h2>Quality Inspection</h2>
          <p>Automatic <b>data extraction procedures</b>, such as dataset profiling, and descriptor inspection
             can be triggered manually or as Oozie worklow.</p>
          <p><a class="btn btn-default" href="v2/matching/runMatching.jsp" role="button">Run inspection &raquo;</a></p>
       </div>
        <div class="col-md-4">
          <h2>Metadata Analysis</h2>
          <p>Explore the data and <b>identify typical patterns</b> or risks. Such information allows us to modify
              or optimize operations.</p> 
          <p><a class="btn btn-default" href="v2/dashboards/index.jsp" data-toggle="modal"
   data-target="#modal_STATS" role="button">Explore metadata sets &raquo;</a></p>
        </div>
      </div>
  </div>
    </font>
</div>
      

<div class="modal fade" style="width:1200px;" id="modal_STATS" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
    <div class="modal-dialog" style="width:1100px;" >
        <div class="modal-content">
            <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
            <h4 class="modal-title" id="myModalLabel"><b>Corpus Analysis</b> (TODAY)</h4>
            </div>
            <div class="modal-body">
                
                <!--iframe width="900" height="350" src="//docs.google.com/spreadsheets/d/1t5lXIzs5wHRovi9TrWS3Q0PUZ0NuLnetAz59i202ZNo/gviz/chartiframe?oid=1070188261" 
                        seamless frameborder=0 scrolling=no></iframe-->
                <iframe width="900" height="350" src="static/analysis_stat.html" 
                        seamless frameborder=0 scrolling=no></iframe>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              
        </div>
    </div>
  </div>
</div>
      
       
<div class="modal fade" style="width:1600px;" id="modal_GO" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
    <div class="modal-dialog" style="width:1400px;" >
        <div class="modal-content">
            <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
            <h4 class="modal-title" id="myModalLabel">Lufthansa - Datahub :: Enterprise Metadata Integration</h4> 
            
            <div id="qqinfo"></div>
   
            <div id="frameURL"></div>
            
            </div>
            
            <div class="modal-body">
                
    
                <iframe id="QSR" scrolling="yes" width="1250" height="650" src="#" 
                        seamless frameborder=0 scrolling=no></iframe>
                
            </div>
            <div class="modal-footer">
                <div >
                    <div id="qqLoadPDF"></div><hr><button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
        </div>
    </div>
  </div>
</div>

      
      
      
      
      <hr>
      <div style=""><center>
      <a href="http://172.24.128.56:7187/"> [Cloudera Navigator] </a> 
      
      <a href="http://172.24.128.56:7180/"> [Cloudera Manager] </a> </center>
      </div> 

      
      <hr>
       

      <footer>
          <div>
              <p><center>www.cloudera.com</center></p>
          </div>
      </footer>
    </div> <!-- /container -->        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.11.0.js"><\/script>')</script>

        <script src="js/vendor/bootstrap.min.js"></script>

        <script src="js/main.js"></script>

        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
        <script>
            (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
            function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
            e=o.createElement(i);r=o.getElementsByTagName(i)[0];
            e.src='//www.google-analytics.com/analytics.js';
            r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
            ga('create','UA-XXXXX-X');ga('send','pageview');
        </script>
        
        

        
 
        
                
 
        
    
    
    
    


</body>
</html>
