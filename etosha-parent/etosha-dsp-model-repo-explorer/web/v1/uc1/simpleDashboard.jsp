<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.cloudera.search.QueryConstants"%>
<%@page import="com.cloudera.search.queries.SimpleFieldQuery"%>
<%@page import="com.cloudera.search.queries.IdQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style type="text/css">
    
    .xmlResult {
        
        color: #003bb3;
        font-family: arial;
        font-size: 58pt;
        height: 250px;
        width: 500px;
        
    }
    
    .xmlResult2 {
        
        color: #003bb3;
        font-family: arial;
        font-size: 38pt;
        height: 250px;
        width: 500px;
        
    }    
</style>
    


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UC-01 Process Dashboard</title>
    </head>
    <body>
        <h1>UC-01 Process Dashboard</h1>
        <table border="1" style="width: 1000">
  
          <%
       
            response.setIntHeader("Refresh", 5);
              
            String info = "";
            String infoASR = "ASR";
            String infoLOUNGE = "LOUNGE";
            String infoENT = "ENT";
            String infoOND = "OND";
                    
            String infoCMD_PREP = "CMD_PREP";
            String infoCMD_DECIDE = "CMD_DEC";

        SimpleFieldQuery psq1 = SimpleFieldQuery.init();
        psq1.buildQuery( "OND_OFFER");
        psq1.executeQueryZeroRows();
        // System.out.println( psq1.getResultAsXML() );
        infoOND =  psq1.getNrOfResult();
        System.out.println( infoOND );
        psq1.reset();
        
        SimpleFieldQuery psq2 = SimpleFieldQuery.init();
        psq2.buildQuery( "ENTERTAINMENT_STATISTICS");
        psq2.executeQueryZeroRows();
        // System.out.println( psq2.getResultAsXML() );
        infoENT = psq2.getNrOfResult();
        System.out.println( infoENT );
        psq2.reset();
        
        SimpleFieldQuery psq3 = SimpleFieldQuery.init();
        psq3.buildQuery( "LOUNGE_OFFER_STATISTICS");
        psq3.executeQueryZeroRows();
        // System.out.println( psq3.getResultAsXML() );
        infoLOUNGE = psq3.getNrOfResult();
        System.out.println( infoLOUNGE );
        psq3.reset();
        
        SimpleFieldQuery psq4 = SimpleFieldQuery.init();
        psq4.buildQuery( "ASR_STATISTICS");
        psq4.executeQueryZeroRows();
        // System.out.println( psq4.getResultAsXML() );
        infoASR = psq4.getNrOfResult();
        System.out.println( infoASR );
        psq4.reset();

        SimpleFieldQuery psq5 = SimpleFieldQuery.init();
        psq5.buildQuery( "COMMAND_PREPARE_ENTERTAINMENT_OFFER");
        psq5.executeQueryZeroRows();
        // System.out.println( psq4.getResultAsXML() );
        infoCMD_PREP = psq5.getNrOfResult();
        System.out.println( infoCMD_PREP );
        psq5.reset();
        

        SimpleFieldQuery psq6 = SimpleFieldQuery.init();
        psq6.buildQuery( "COMMAND_DECIDE_ENTERTAINMENT_OFFER");
        psq6.executeQueryZeroRows();
        // System.out.println( psq4.getResultAsXML() );
        infoCMD_DECIDE = psq6.getNrOfResult();
        System.out.println( infoCMD_DECIDE );
        psq6.reset();


        long time = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat( "HH:mm:ss" );
        
        String now = format.format( new Date( time ) );

            
         %>
            
            <tbody >
                <tr>
                    <td>ASR Offers<br/>
                    <div class="xmlResult"><%= infoASR %></div>
                    </td>
                    <td>Lounge Offers<br/>
                    <div class="xmlResult"><%= infoLOUNGE %></div>
                    </td>    
                </tr>
                <tr>
                    <td>Entertainment Offer<br/>
                        <div class="xmlResult2"><%= infoENT %>  <br/>PREP: <%= infoCMD_PREP %><br/>DECIDE: <%= infoCMD_DECIDE %></div>
                    </td>
                    
                    <td>OND Offer</br>
                    <div class="xmlResult"><%= infoOND %></div>
                    </td>
                </tr>
            </tbody>
        </table>
        Last updated: <%= now %>

        
 
        
    </body>
</html>
