<!DOCTYPE html>
<html lang="en">
	<head>
		<title>json tree example</title>
		<link href="./../../css/jsontree.css" rel="stylesheet">
		<script src="./../../js/jsontree.min.js"></script>
  	</head>
	<body>
            <h1>Collected Matching Results</h1>
            <hr>
            <h2>nr of collected items : <%="Z" %></h2>
            <b>Needle Dataset :</b><%="$NEEDLES" %><br/>
            <b>Haystack Dataset :</b><%="$HAYSTACK" %><br/>
            <hr>
            <h3>Multi match table view</h3>
            <hr>
            <input type="submit" value="load CSV" /> <input type="submit" value="load XLS" />
            <hr>
            [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
	</body>
</html>