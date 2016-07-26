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
	openid: ${user.openid} <br/>
	nickname: ${user.nickname} <br/>
	sex: ${user.sex} <br/>
	province: ${user.province} <br/>
	city: ${user.city} <br/>
	country: ${user.country} <br/>
	headimgurl: ${user.headimgurl} <br/>
	<img src="${user.headimgurl}" /> <br/>
	unionid: ${user.unionid} <br/>
</body>
</html>