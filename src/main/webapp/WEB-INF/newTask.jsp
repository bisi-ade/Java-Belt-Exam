<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>New Task Page</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="/js/app.js"></script>
</head>
<body>
		<a href="/dashboard">Dashboard</a>
	<h1>New Task:</h1>
	<p>${ error }</p>
	<p><form:errors path="task.*"/></p>
	<form:form action="/tasks/new" method="post" modelAttribute="task">
	    <p>
	        <form:label path="name">Task:</form:label>
	        <form:input path="name"/>
	    </p>
	    <p>
	        <form:label path="assignee">Assignee (optional):</form:label>
	        <form:select path="assignee">
	        	<form:option value="">Unassigned</form:option>]
	        <c:forEach items="${ users }" var="user">
	        	<form:option value="${ user }">${ user.firstName } ${ user.lastName }</form:option>
        	</c:forEach>
	        </form:select>
	    </p>
	    <p>
	        <form:label path="priority">Priority:</form:label>
	        <form:select path="priority">
	        	<form:option value="" selected="true" disabled="true">Choose one:</form:option>
	        	<form:option value="1">High</form:option>
	        	<form:option value="2">Medium</form:option>
	        	<form:option value="3">Low</form:option>
	        </form:select>
	    </p>
	    <input type="submit" value="Submit"/>
	</form:form> 

</body>
</html>