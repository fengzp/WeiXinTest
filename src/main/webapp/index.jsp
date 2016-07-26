<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.feng.web.model.*" %>
<%@ page import="com.feng.web.service.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-2.2.3.js"></script>
<script type="text/javascript">
var code;
$(document).ready(function(){
	code = getUrlParam("code");
	$("#code").text("code : "+code);
	$("#state").text("state : "+getUrlParam("state"));
});

function getUrlParam(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) 
	return unescape(r[2]); 
	return null; 
} 

/* function getUserMsg2(){
	$.get("userServlet?code="+code, null);
} */
function getUserMsg(){
	$.get("mvc/user/getWxUserMsg?code="+code, null);
}

function sign(){
  $.get("jsSignServlet?url="+window.location.href, function(data,status){
    alert("Data: " + data + "\nStatus: " + status);
  });
}
</script>
<title>Insert title here</title>
</head>
<body>
	access_token为：<%=AccessTokenInfo.accessToken.getAccess_token()%>
	<div id="code"></div>
	<div id="state"></div>
	<button onclick="getUserMsg()">获取用户信息</button>
	<button onclick="sign()">生成签名</button>
</body>
</html>