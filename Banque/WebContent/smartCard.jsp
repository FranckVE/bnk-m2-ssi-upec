<%@ page language="java" contentType="text/html" %>
<%@ page import="cryptos.CryptoUtils" %>
<%CryptoUtils util = new CryptoUtils(); %>
<% String login = request.getParameter("login"); %>

<% // password retrieve and hash transformation %>
<% String password = util.digestSHA1(request.getParameter("password")); %>
	 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>LA BANQUE</title>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<link href="default.css" rel="stylesheet" type="text/css" />

	</head>
	<body>
	 
     
		<div id="header">
			<div id="logo">
				<h1><a href="#">  The FA BANK </a></h1>

			</div>
			<div id="menu">
				<ul>
					<h1 class="textcolorwhite"> The CDAI PROJECT</h1>
					<li class="active">
						<a href="#" title= "" ></a>
					</li>
					<li>
						<a href="#" title=""></a>
					</li>
					<li>
						<a href="#" title=""></a>
					</li>
					<li>
						<a href="#" title=""></a>
					</li>
					<li>
						<a href="#" title=""></a>
					</li>
				</ul>
			</div>
		</div>
		<div id="content">
			<div id="sidebar">
				<div id="login" class="boxed">
					<h2 class="title">Client Authentication</h2>
					<div class="content">
						<div id="welcome" class="post">
							<p><img src="images/smartcard.jpg" alt="" width="215" height="120" />
							</p>
							<h2 class="title">Credit Card</h2>
							<h3 class="date"><span class="month">April.</span><span class="year">, 2013</span></h3>

						</div>

						<!--
						<form id="form1" method="get" action="/rest/hello">
						<fieldset>
						<legend>
						Sign-In
						</legend>
						<table width="60%">
						<tr>
						<td>Login:</td>
						<td>
						<input type="text" name="login">
						</input></td>
						</tr>
						<tr>
						<td>Password:</td>
						<td>
						<input type="password" name="password">
						</input></td>
						</tr>
						<tr>
						<td colspan="2">
						<input type="submit" value="Sign In">
						</input></td>
						</tr>
						</table>
						<p>
						<a href="#">Forgot your password?</a>
						</p>
						</fieldset>
						</form>
						-->
					</div>
				</div>
				<div id="updates" class="boxed">
					<h2 class="title">Recent Updates</h2>
					<div class="content">
						<ul>
							<li>
								<h3>April 15, 2013</h3>

							</li>

						</ul>
					</div>
				</div>
			</div>
			<div id="main">
				<!--
				<div id="welcome" class="post">
				<p><img src="images/smartcard.jpg" alt="" width="300" height="180" />
				</p>
				<h2 class="title">Welcome to The FA Bank!</h2>
				<h3 class="date"><span class="month">April.</span><span class="year">, 2013</span></h3>

				</div>
				-->
				<object type="application/x-java-applet" id="SmartCardApplet"
				code="client.Login.class" codebase="./" archive="http://bank-cdai.rhcloud.com/Banque/sdcard.jar" height="387"
				width="482">
                <PARAM NAME="login" VALUE= <%= login %> >
                <PARAM NAME="password" VALUE=<%= password %> >
				</object>
			</div>
		</div>
		</div>
		<div id="footer">
			<p id="legal">
				Copyright &copy; 2013 The Bank. All Rights Reserved. Designed by The FA Bank</a>.
			</p>

		</div>

		</form>

	</body>

</html>