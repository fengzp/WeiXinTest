<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SPRINGMVC</title>
</head>
<body>
	<sf:form action="upload" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
		<input type="file" name="file">
		<input type="submit" value="提交">
	</sf:form>
</body>
</html>