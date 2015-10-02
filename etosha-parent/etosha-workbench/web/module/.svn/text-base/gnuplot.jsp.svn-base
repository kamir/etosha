<%   
    String chartID = request.getParameter("chartID");
    System.out.println("* files.jsp -> " + chartID);
%>

<div id="tab-5" style="width: 70%; height: 720px;">
    
    <iframe style="width: 1100; height: 720px;" id="iFRAME" frameborder="0" scrolling="auto"
            <%// src="http://127.0.0.1:8080/FileBrowser/Browser.jsp" %>
            <%//src="http://127.0.0.1:9080/share/page/site/rh2/documentlibrary"  %>

            src="<%=request.getContextPath() %>/ace/build/le_gnuplot.jsp?chartID=<%=chartID%>"   >
    </iframe> 
    <div id="gnuplot_preview" style="position: relative; left: -5px; top: -300px;">
        <img width="290" border="0" src="<%=request.getContextPath() %>/DATA/P0/default/chart.png" />
    </div>
</div>