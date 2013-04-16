<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
	//Afin de sauvegarder le login et mot de passe
	String name0 = request.getParameter("login");
	String pass0 = request.getParameter("password");
	session.setAttribute("login",name0);
	session.setAttribute("password",pass0);
	
	

%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%= session.getAttribute("login") %>
<% String name = session.getAttribute("login").toString();
   System.out.println(name);
   String pass = session.getAttribute("password").toString();
   System.out.println(pass);
   
   //login&MD5(pass)
   
   String passmd = CryptoUtils.digest(pass);
   String message_envoi = new String(name + passmd);
   
   //envoi d'une requete

   
   
   
   
   
%>
</body>
</html>