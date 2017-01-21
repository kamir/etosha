<%@page import="org.apache.commons.math3.stat.Frequency"%>
<%@page import="org.apache.commons.math3.stat.descriptive.SummaryStatistics"%>
<%@page import="com.cloudera.dal.partsmatch.MatchStore"%>
<%@page import="com.cloudera.search.queries.QueryConstants"%>
<%@page import="com.cloudera.search.queries.PartsSimpleQuery"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@page import="com.cloudera.search.queries.VehicleSimpleQuery"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MatchingResult statitics </title>
    </head>
    <body>
        <h1>MatchingResult statistics</h1>

        <%
        
        MatchStore ms = (MatchStore)request.getServletContext().getAttribute("store");
       
        StringBuffer sb = new StringBuffer();
  
        if ( ms == null ) {

            sb.append("Connector is not configured." );
            
        }
        else {

            com.cloudera.utils.StatisticalSummary summary = ms.getHistogramForAllMatchesInAllRows();
         
            SummaryStatistics s = summary.s;
        
            sb.append( "Variance : " + s.getPopulationVariance() + "\n");
            sb.append( "Mean     : " + s.getMean() + "\n");
            sb.append( "Min      : " + s.getMin() + "\n");
            sb.append( "Max      : " + s.getMax() + "\n");
        
            Frequency f = summary.f;
        
            sb.append( "Frequency      : " + f.toString() + "\n");
        }    
            
         %>

         <textarea name="result" rows="15" cols="200"><%= sb.toString() %></textarea>
         
         <hr>
         
         
    </body>
</html>
