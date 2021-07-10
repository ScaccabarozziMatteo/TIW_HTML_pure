<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Prova TIW</title>
</head>
<body>
<h1><%= "Benvenuti nella prima homepage creata" %></h1>
<h4><%= "Seleziona una delle possibili scelte" %></h4>
<br/>
<a href="login-servlet" >Visualizza prodotti</a><br>
<a href="${pageContext.request.contextPath}/link.html" >Aggiungi prodotto</a>

<form th:insert="formLogin" action="${pageContext.request.contextPath}/login-servlet" method="post">
    Username
<input type="text" maxlength="20" required name="email"><br><br>
    Password
<input type="password" maxlength="20" required name="password"><br><br>
<input type="submit" value="Invia"></form>

</body>
</html>