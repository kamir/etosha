<%@page import="com.cloudera.search.queries.VehicleTreeMapQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>



        <%
            VehicleTreeMapQuery svc = VehicleTreeMapQuery.init();
  
            svc.buildQuery(request.getParameter("p"));
              
            svc.executeQuery();      
        %> 
          
<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['treemap']});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
     
            <%= VehicleTreeMapQuery.getResultAsJSONArraySAMPLE() %> 
             
        ]);

        tree = new google.visualization.TreeMap(document.getElementById('chart_div'));

        tree.draw(data, {
          minColor: '#f00',
          midColor: '#ddd',
          maxColor: '#0d0',
          headerHeight: 15,
          fontColor: 'black',
          showScale: true
        });

      }
    </script>
  </head>
  <body>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TreeMap Vehicle Query DEMO</title>
    </head>
    <body>
        <h1>TreeMap Vehicle Query DEMO</h1>
        
         Query on field : [ <%= svc.getDefaultField() %>]</br>
         Query pattern  : <%= request.getParameter("p") %></br>

         <hr>
         
         <h3>Results:</h3>
         <textarea name="xmlResult" rows="15" cols="200"><%= svc.getResultAsXML() %></textarea>
         <hr>
         <h3>Tree Map (using Google Charts API)</h3>         
         <div id="chart_div" style="width: 900px; height: 500px;"></div>
  </body>
</html>