<!DOCTYPE html>
<html lang="en">
	<head>
		<title>json tree example</title>
		<link href="./../../css/jsontree.css" rel="stylesheet">
		<script src="./../../js/jsontree.min.js"></script>
  	</head>
	<body>

            <h1>Matching Results</h1>
            <hr>
            <h2>Language: <%="PL" %></h2>
            <b>Needle Dataset :</b><%="$NEEDLES" %><br/>
            <b>Haystack Dataset :</b><%="$HAYSTACK" %><br/>
            <hr>
            <h3>Single match tree view</h3>
            <hr>
            <b>claim:</b> <%="CLAIM TEXT TO LOOKUP DATA" %> <br />
            <b>search phrase:</b> <input size="75" value="<%="CLAIM TEXT TO LOOKUP DATA" %>"><input type="submit" value="lookup" /></input>
            <hr>
		<div id="example"></div>
	</body>
	<script>
    var result = JSONTree.create({
      foo: {
        bar: 'foobar',
        baz: 'foobaz',
        qux: [
          {
            foobar: 'bar',
            foobaz: 'baz'
          }
        ]
      },
      bar: [
        {foo: 'barfoo'},
        {qux: 'barqux'}
      ],
      qux: [
        'foo',
        'bar',
        'foobar'
      ],
      baz: true,
      foobar: [0, 1, 2, 3],
      barfoo: null
    });
	document.getElementById("example").innerHTML = result;
	</script>
        <hr>
        <input type="submit" value="put item to session" /> <input type="submit" value="download session results" />
        <hr>
        [ <a href="<%= session.getAttribute("HOMELINK") %>">HOME</a> ]
</html>
