<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String name = request.getParameter("login");
	String pass = request.getParameter("password");
	session.setAttribute("login",name);
	session.setAttribute("password",pass);
	

%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%= session.getAttribute("login") %>
<% String name1 = session.getAttribute("login").toString();
	System.out.println(name1);
%>
</body>
</html>