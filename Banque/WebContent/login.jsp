<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="bank.BankChallenge" %> 
<%@ page import="javax.servlet.RequestDispatcher" %>
<% String token = request.getParameter("token"); %>  

<%
	BankChallenge challenge = new BankChallenge() ;
%>   

  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Redirect the token</title>
</head>
<body>

<% //ici on doit déchiffre le token et vérifier ses éléments %>

<%  

out.println(token);
if ( challenge.verify(token)){
	
	// transmettre les infos sur le client
	RequestDispatcher rd = request.getRequestDispatcher("Accueil.jsp");
		              rd.forward(request, response);
}
else {
	RequestDispatcher rd = request.getRequestDispatcher("Erreur.jsp");
    rd.forward(request, response);
}
	
	%>

</body>
</html>