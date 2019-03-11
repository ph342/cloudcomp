<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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
		<!--     <c:forEach items="${requestScope['indexServlet.allItems']}" var="item"/> -->

	<table>
		<c:forEach items="${indexServlet.allItems}" var="item">
			<tr>
				<td>${item.getItem_nr()}</td>
				<td><c:out value="${item.getName()}" /></td>
				<td><c:out value="${item.getDescription()}" /></td>
				<td><fmt:formatNumber value="${item.getPrice()}"
						type="currency" currencyCode="${item.getCurr()}" /></td>
			</tr>
		</c:forEach>
	</table>

	<p>${requestScope['indexServlet.allItems']}</p>
	<!-- we need to use requestScope[] if the name contains any "." -->
</body>
</html>
