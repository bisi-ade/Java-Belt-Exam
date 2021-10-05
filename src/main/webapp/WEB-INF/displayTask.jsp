<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Task Manager Display Page</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="/js/app.js"></script>
</head>
<body>
		<a href="/dashboard">Dashboard</a>
	<h1>Task: ${ task.name }</h1>
	<p>Creator: ${ task.creator.firstName} ${ task.creator.lastName}</p>
	<p>Assignee: ${ task.assignee.firstName} ${ task.assignee.lastName}</p>
	<p>Priority:
		<c:choose>
			<c:when test="${ task.priority == 1 }">High</c:when>
			<c:when test="${ task.priority == 2 }">Medium</c:when>
			<c:when test="${ task.priority == 3 }">Low</c:when>
		</c:choose>
	</p>
	<c:if test="${ task.creator.id == user_id }">
		<a href="/tasks/edit/${ task.id }">Edit</a>
		
		<form action="/tasks/destroy/${ task.id }"method="post">
    <input type="hidden" name="_method" value="delete">
    <input type="submit" value="Delete">
</form>
		
	</c:if>
	<c:if test="${ task.assignee.id == user_id }">
		<a href="/tasks/destroy/${ task.id }"></a>
		
		<form action="/tasks/destroy/${ task.id }"method="post">
    <input type="hidden" name="_method" value="delete">
    <input type="submit" value="Complete">
		</form>
	</c:if>

</body>
</html>