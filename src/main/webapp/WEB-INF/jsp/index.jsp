<!doctype html>
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

<script type="text/javascript">
	
</script>
</head>

<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse"	data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span	class="icon-bar"></span>
				</button>
				<a class="brand" href="#">Smartsheet Hello World</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="#">Home</a></li>
						<li><a href="about">About</a></li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>

	<div class="container">
		<br/>
		<img src="<c:url value="resources/logo.png" />" />
		<h1>Get started with your Smartsheet Hello World</h1>
		<p>
			This is a template for a Smartsheet Web Application. To access your
			sheets, please allow access first. 
			<a href="login"class="btn btn-primary btn-large"><i class="icon-white icon-hand-right"></i>Login</a>
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
