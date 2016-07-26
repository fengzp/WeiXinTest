<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.feng.web.model.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../../js/jquery-2.2.3.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	/* wx.checkJsApi({
	    jsApiList: ['onMenuShareAppMessage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
	    success: function(res) {
	        // 以键值对的形式返回，可用的api值true，不可用为false
	        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
	        alert(res);
	    }
	}); */
	sign();
});

function sign(){
	var url = encodeURIComponent(location.href.split('#')[0]);
  	$.get("../../jsSignServlet?url="+url, function(data,status){
	    /* alert("Data: " + data + "\nStatus: " + status); */
	    var arr = data.split(",");
	    wx.config({
	        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	        appId: <%=WxConfig.appId%>, // 必填，公众号的唯一标识
	        timestamp: arr[0], // 必填，生成签名的时间戳
	        nonceStr: arr[1], // 必填，生成签名的随机串
	        signature: arr[2],// 必填，签名，见附录1
	        jsApiList: ["onMenuShareAppMessage"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	    });
	  });
}

wx.ready(function(){
	alert("注册事件");
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	$("#bt").click(
	    function (){
			alert("123");
			alert(wx);
			wx.onMenuShareAppMessage({
			    title: 'Hi!', // 分享标题
			    desc: 'how are you?', // 分享描述
			    link: 'https://www.baidu.com', // 分享链接
			    imgUrl: 'http://img4.imgtn.bdimg.com/it/u=4236942158,2307642402&fm=21&gp=0.jpg', // 分享图标
			    type: '', // 分享类型,music、video或link，不填默认为link
			    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
			    success: function () { 
			        // 用户确认分享后执行的回调函数
			        alert("成功");
			    },
			    cancel: function () { 
			        // 用户取消分享后执行的回调函数
			    	alert("失败");
			    }
			});
		}
	    
	    alert("已注册分享事件");
	);
});



wx.error(function(res){

    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	alert(res);
});
</script>
<title>分享</title>
</head>
<body>
	<button id="bt" >分享到朋友圈</button>
</body>
</html>