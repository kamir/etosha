<div id="tab-2">
   <%   String chartID = request.getParameter("chartID");
            System.out.println( "* files.jsp -> " + chartID ); %>
         <iframe id="iFRAME" width="1350" height="400" frameborder="0" scrolling="no" 
                marginheight="0" marginwidth="0" 
                <%// src="http://127.0.0.1:8080/FileBrowser/Browser.jsp" %>
                <%//src="http://127.0.0.1:9080/share/page/site/rh2/documentlibrary"  %>
                
                src="<%=request.getContextPath() %>/ace/build/le.jsp?chartID=<%=chartID %>" style="width:1100px; height:720px;"  >
        </iframe>  
</div>