package com.feng.web.service.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feng.web.model.WxMessage;
import com.feng.web.service.util.BeanUtil;
import com.feng.web.service.util.StringUtil;
import com.feng.web.service.util.WxMsgUtil;

/**
 * 
 * @author fengzp
 *
 */
@WebServlet(urlPatterns = "/WxServlet")
public class WxServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6008816817887502362L;

	/**
	 * Token可由开发者可以任意填写，用作生成签名（该Token会和接口URL中包含的Token进行比对，从而验证安全性）
	 */
	private final String TOKEN = "fengzp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("开始校验签名");

		/**
		 * 接收微信服务器发送请求时传递过来的4个参数
		 */
		String signature = request.getParameter("signature");// 微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串

		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		
//		response.getWriter().print(echostr);
		
//		 加密
		String mySignature = StringUtil.sha1(sort(TOKEN, timestamp, nonce));

		// 校验签名
		if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
			System.out.println("签名校验通过。");
			response.getWriter().write(echostr);
//			response.getWriter().print(echostr);
		} else {
			System.out.println("签名校验失败.");
		}
	}

	public String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}

		return sb.toString();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String result = "";
		try {
			Map<String, String> map = WxMsgUtil.parseXml(request);
			WxMessage msg = (WxMessage) BeanUtil.map2Bean(WxMessage.class, map);
			System.out.println("用户发送的信息:"+msg.toString());
			WxMessage resultMsg = WxMsgUtil.buildRepeatMsg(msg);
			result = BeanUtil.bean2Xml(resultMsg, null);
			System.out.println("回复的信息:"+result);
			if(result.equals("")){
				result = "处理异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发生异常:"+e.getMessage());
		}
		response.getWriter().write(result);
	}
}
