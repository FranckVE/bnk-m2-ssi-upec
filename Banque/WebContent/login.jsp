<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="sdcard.Challenge" %> 
<% Challenge challenge = new Challenge() ;%>   
<% String token = request.getParameter("token"); %>  
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Redirect the token</title>
</head>
<body>

<% //ici on doit d�chiffre le token et v�rifier ses �l�ments %>

<%  

out.println(token);
challenge.verify(token); %>

</body>
</html>