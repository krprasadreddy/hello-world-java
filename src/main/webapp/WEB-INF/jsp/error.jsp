<!doctype html>
<%@page import="java.io.PrintWriter"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="utf-8">
<title>SmartSheet Hello World</title>

<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link
	href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css"
	rel="stylesheet">
<link
	href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css"
	rel="stylesheet">
</head>

<body>

	<div class="container">
		<div class="row">
			<h1>Error Page</h1>
			<p>Sorry! Something went wrong in the application. Reason :
				${exception.message}</p>
			<pre style="display: none">
				<%
				Exception exception = (Exception) request.getAttribute("exception");
				if (exception != null) {
					exception.printStackTrace();
					exception.printStackTrace(new PrintWriter(out));
				}
				%>
			</pre>
		</div>
	</div>

</body>
</html>
