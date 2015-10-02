<%-- 
    Document   : startReview
    Created on : 02.07.2015, 14:10:38
    Author     : kamir
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Init a review</title>
    </head>
    <body>
        <h1>Reviewer</h1>
        
        <a href="https://docs.google.com/spreadsheets/d/1thIL4hd4-TISvIXkkSzOeuCja6NMyVGsGe9OM3Aw2Qs/edit#gid=0">Contributorlist</a>
        
        
        <form method="POST" action="/DocWorld/requestReview">
         
        <textarea name="person" rows="2" cols="40">
mirko.kaempf@cloudera.com
mirko.kaempf@gmail.com
mirko@gmail.com
        </textarea>

        <textarea name="person" rows="2" cols="10">
1
1
1
        </textarea>

            <input type="submit" value="Request now ..." name="send" /> 
            
        </form>
    </body>
</html>
