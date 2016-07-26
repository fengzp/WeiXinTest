<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SPRINGMVC</title>
</head>
<body>
	<c:forEach items="${users }" var="kv">
		${kv.value.username } -- ${kv.value.password } -- 
		<a href="${kv.value.username }/update">修改</a>
		<a href="${kv.value.username }/delete">删除</a> <br/>
	</c:forEach>
	<a href="add">添加</a>
	<a href="upload">上传</a>
</body>
</html>