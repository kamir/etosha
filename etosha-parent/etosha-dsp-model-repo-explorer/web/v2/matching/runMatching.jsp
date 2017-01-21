<%-- 
    Document   : runMatching
    Created on : 02.03.2016, 08:39:37
    Author     : kamir
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Run Matching</title>
    </head>
    <body>

        <h1>Masterdata Matching Engine</h1>
        <hr>
        
        Depending on Request parameter "fb" it will be executed in silent mode
        or interactively.   ===> { fb=[ <%= request.getParameter("fb") %>] }
        
        <hr>
        <h3>Silent Mode</h3>
        The MatchingEngine processes a data set in the background in silent mode.<br/> 
        <b>Call Parameters (WIP):</b><br/>
        
        <table border="1">
            <thead>
                <tr>
                    <th width="25%">Parameter Name</th>
                    <th width="35%">Description</th>
                    <th width="15%">Required</th>
                    <th width="15%">Default value</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>lang: <input type="text" name="lang" value="PL" /> </td>
                    <td>Language of claims to match the against the Masterdata.</td>
                    <td>yes</td>
                    <td>null</td>
                </tr>
                <tr>
                    <td>limit <input type="text" name="limit" value="-1" /></td>
                    <td>Max number of claim records for automatic processing. Usefull if tests are done.
                    If set to -1 there is no limitation.</td>
                    <td>no</td>
                    <td>-1</td>
                </tr>
                <tr>
                    <td>ts <input type="text" name="ts" value="-1" /></td>
                    <td>Threshold for score filter. Lower scores are ignored to reduce workload.</td>
                    <td>no</td>
                    <td>-1</td>
                </tr>
       
                <!--tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr-->
            </tbody>
        </table>

        <h3>Interactive Mode</h3>
        The MatchingEngine pre-processes a data set in the background and offers extracted results
        for iterative interactive post processing, managed by an SME.<br/>
        <b>Additional Call Parameters (WIP):</b><br/>
                <table border="1">
            <thead>
                <tr>
                    <th width="25%">Parameter Name</th>
                    <th width="35%">Description</th>
                    <th width="15%">Required</th>
                    <th width="15%">Default value</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>batch_size <input type="text" name="batchsize" value="10" /></td>
                    <td>Number of selected claims per run. By default, the system loads 10 claims from 
                        raw database for the selected language and presents this data in a list for user selection.<br>
                    The user triggers an explicit matching run for each claim. Results are shown per claim and user can
                    change paramters to tune the matching procedure. This allows preparation of large automatic runs.</td>
                    <td>yes</td>
                    <td>10</td>
                </tr>
       
                <!--tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr-->
            </tbody>
        </table>

        <hr>
       [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]

    </body>
</html>
