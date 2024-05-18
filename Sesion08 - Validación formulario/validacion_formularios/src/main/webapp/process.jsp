<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resultado del Formulario</title>
</head>
<body>
<h1>Resultados del formulario</h1>
<p><strong>Nombre:</strong> <%= request.getParameter("nombre") %></p>
<p><strong>Email:</strong> <%= request.getParameter("email") %></p>
<a href="formulario_javascript.html">Volver al formulario</a>
</body>
</html>
