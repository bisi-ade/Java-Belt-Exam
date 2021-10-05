<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Dashboard</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="/js/app.js"></script>
</head>
<body>
		<h1>Welcome, <c:out value="${user.email}" /></h1>
	<a href="/logout">Logout</a>
	<br /><br />
	<h2>All Tasks:</h2>
	<a href="/sorthigh">Priority High - Low</a>
	<span> -- </span>
	<a href="/sortlow">Priority Low - High</a>
	<br /><br />
	<table>
		<thead>
			<tr>
				<th>Name:</th>
				<th>Creator:</th>
				<th>Assignee:</th>
				<th>Priority:</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${ tasks }" var="task">
			<tr>
				<td>
					<a href="/tasks/show/${ task.id }">${ task.name}</a>
				</td>
				<td>${ task.creator.firstName} ${ task.creator.lastName}</td>
				<td>${ task.assignee.firstName} ${ task.assignee.lastName}</td>
				<td>
				<c:choose>
					<c:when test="${ task.priority == 1 }">High</c:when>
					<c:when test="${ task.priority == 2 }">Medium</c:when>
					<c:when test="${ task.priority == 3 }">Low</c:when>
				</c:choose>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<br />
	<a href="/tasks/new">New Task</a>

</body>
</html>