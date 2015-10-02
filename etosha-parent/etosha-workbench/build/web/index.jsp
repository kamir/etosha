<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Scientific Social Office - Report Editor (Version 0.5.1 BETA)</title>
        <script src='resources/js/upload.js'> </script>
        <script src='dwr/interface/UploadMonitor.js'> </script>
        <script src='dwr/engine.js'> </script>
        <script src='dwr/util.js'> </script>
        <script type='text/javascript' src='js/font/KidTYPEPaint_400.font.js'></script>
        <style type="text/css">
            body { font: 11px Lucida Grande, Verdana, Arial, Helvetica, sans serif; }
            #progressBar { padding-top: 5px; }
            #progressBarBox { width: 350px; height: 20px; border: 1px inset; background: #eee;}
            #progressBarBoxContent { width: 0; height: 20px; border-right: 1px solid #444; background: #9ACB34; }
        </style>

        <link rel="stylesheet" href="css/main.css" type="text/css" media="screen" />
        <!--link rel="stylesheet" href="css/style.css" type="text/css" media="screen" /-->
        <%@include file="js/selectPage.js" %>
        <%@include file="js/timerscript.js" %>

        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>

        <script>
            $(document).ready(function() {
                $("#container").tabs();
            });
        </script>

    </head>
    <body id="main_body">

        <%
                 String chartID = request.getParameter("chartID");
                 System.out.println( "* files.jsp -> " + chartID );
        %>

        <div id='logo'>
            <img src='img/customLogo.gif'>
        </div>
        
        <div id='modules'>
            <!--img src='<%= request.getContextPath()%>/img/guider-icons-by-e-young/png/32x32/notebook.png'/><br><br>
            <a href="#" onclick="loadRRTB()">Research-Report<br> Toolbox</a> <br><br><br>
            
            <img src='<%= request.getContextPath()%>/img/guider-icons-by-e-young/png/32x32/magic.png'/><br><br>
            <a href="#" onclick="loadKBTB()">Knowledge-Base<br> Toolbox</a> <br><br><br>
            
            
            <img src='<%= request.getContextPath()%>/img/guider-icons-by-e-young/png/32x32/disc.png'/><br><br>
            <a href="#" onclick="loadADMINTB()">System-Admin<br> Toolbox</a> <br><br><br-->
            <div class="main_button">
            <img width="64" src='<%= request.getContextPath()%>/img/a/notes-icon.png'/><br><br>
            <a href="#" onclick="loadRRTB()">Research-Report</a> <br><br><br>
            </div>            
            
            <div class="main_button">
            <img width="64" src='<%= request.getContextPath()%>/img/a/browser-icon.png'/><br><br>
            <a href="#" onclick="loadKBTB()">Knowledge-Base</a> <br><br><br>
            </div>
            
            <div class="main_button">
            <img width="64" src='<%= request.getContextPath()%>/img/a/graph-icon.png'/><br><br>
            <a href="#" onclick="loadHadoopTools()">Hadoop-Cluster</a> <br><br><br>
            </div>
            
            <div class="main_button">
            <img width="64" src='<%= request.getContextPath()%>/img/a/tools-icon.png'/><br><br>
            <a href="#" onclick="loadADMINTB()">System-Admin</a> <br><br><br>
            </div>
            
        </div>

        <div id="projekt">
            <jsp:include page="module/projektDetails.jsp" />   
        </div>
        
        <div id="content">
            <%
                String mode=request.getParameter("section");
                
                 
                System.out.println("** SWITCH to mode: " + mode );
                if( mode != null ) {
                    if ( mode.equals("ADMINTB") ) {
                    System.out.println("ADMIN");
                
            %>
            <jsp:include page="mainContainerADMIN.jsp" />   
            <%
                    }
                    else if( mode.equals("RRTB") ) {
                    System.out.println("RRTB");
                
            %>
            <jsp:include page="mainContainerRRTB.jsp" />   
            <%
                    }
                    else if( mode.equals("KBTB") ) {
                    System.out.println("RRTB");
                
            %>
            <jsp:include page="mainContainerKBTB.jsp" />   
            <%
                    }
                    else if( mode.equals("HADOOP") ) {
                    System.out.println("HADOOP");
                
            %>
            <jsp:include page="mainContainerHADOOP.jsp" />   
            <%
                    }
                

                }
                else { 
                    System.out.println("RESEARCH");
             %>
             <jsp:include page="mainContainerINTRO.jsp" />   
             <%          
                }
                  
            %>
            
            <%@include file="footer.jsp" %>
        </div>
        
        <script>
            function loadRRTB() { 
                window.location.href="index.jsp?section=RRTB&chartID=<%=chartID %>";
            };
            
            function loadKBTB() { 
                 window.location.href="index.jsp?section=KBTB&chartID=<%=chartID %>";
            };
            
            function loadADMINTB() { 
                window.location.href="index.jsp?section=ADMINTB&chartID=<%=chartID %>";
            };
            
                    
            function loadHadoopTools() { 
                window.location.href="index.jsp?section=HADOOP&chartID=<%=chartID %>";
            };
            
        </script>

        

    </body>
</html>