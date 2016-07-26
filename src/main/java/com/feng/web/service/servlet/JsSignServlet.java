package com.feng.web.service.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feng.web.model.WxConfig;
import com.feng.web.service.util.JsUtil;

/**
 * 
 * @author fengzp
 * 2016年5月20日上午10:37:49
 */
@WebServlet(urlPatterns = "/jsSignServlet")
public class JsSignServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2107531580427857077L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进入JsSignServlet");
		String url = request.getParameter("url");
		
//		String signature = JsUtil.createJsApiSignature(noncestr, timestamp, url);
//		response.getWriter().write(timestamp+","+noncestr+","+signature);

		Map<String, String> map = JsUtil.sign(WxConfig.jsApiTicket, URLEncoder.encode(url, "utf-8"));
		System.out.println(map.toString());
		
		response.getWriter().write(map.get("timestamp")+","+map.get("nonceStr")+","+map.get("signature"));
	}
}
