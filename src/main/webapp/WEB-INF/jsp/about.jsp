<!doctype html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="utf-8">
<title>Smartsheet Hello World</title>

<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link
	href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css"
	rel="stylesheet">
<link
	href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css"
	rel="stylesheet">

<script type="text/javascript">
	
</script>
</head>

<body>

	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="brand" href="#">Smartsheet HelloWorld</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li><a href="home">Home</a></li>
						<li class="active"><a href="#">About</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>

	<div class="container">

		<img src="<c:url value="resources/logo.png" />" /> <br />
		<p>
			This is part of the <a href="http://www.cloudspokes.com">Cloudspokes
				Challenge</a> : <a href="http://www.cloudspokes.com/challenges/2193">Smartsheet
				Hello World Setup Challenge</a>
		</p>
		<p>
			Author: <a href="http://www.cloudspokes.com/members/romin">Romin
				Irani</a>
		</p>

		<p>
			Enhanced on <a href="http://www.cloudspokes.com">Cloudspokes</a> Challenge : <br/>
				<a href="http://www.cloudspokes.com/challenges/2210">Smartsheet	- JAVA, Heroku 'Hello World' Enhancements</a> <br/>
				by <a href="http://www.cloudspokes.com/members/eucuepo">Eugenio Cuevas</a>
		</p>
		<p>
			Re-worked and cleaned up by <a href="http://www.cloudspokes.com/members/kyanskeem">Kyan Skeem</a>
		</p>

	</div>
	<!-- /container -->

	<script src="http://twitter.github.com/bootstrap/assets/js/jquery.js"></script>
	<script
		src="http://twitter.github.com/bootstrap/assets/js/bootstrap-modal.js"></script>
	<script
		src="http://twitter.github.com/bootstrap/assets/js/bootstrap-tab.js"></script>

</body>
</html>
