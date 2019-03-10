<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="content-type"
	content="application/xhtml+xml; charset=UTF-8" />
<link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet'
	type='text/css'>

<title>Hello App Engine</title>
</head>
<body>
	<h2>Hello!</h2>

	<p>${requestScope['indexServlet.testData']}</p> <!-- we need to use requestScope[] if the name contains any "." -->
</body>
</html>
