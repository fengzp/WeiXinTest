package com.feng.web.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feng.web.model.WebAccessToken;
import com.feng.web.model.WxConfig;
import com.feng.web.service.util.GsonUtils;
import com.feng.web.service.util.NetUtil;

/**
 * 用户信息
 * @author fengzp
 * 2016年5月17日下午5:18:45
 */
@WebServlet(urlPatterns="/userServlet",
			initParams = {
			        @WebInitParam(name = "appId", value = "wxa1d7918774ce0f09"),
			        @WebInitParam(name = "appSecret", value = "8cfb267d462eae71fb365eb9e35dc201")
			}
)
public class UserMsgServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1518773652193099465L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		System.out.println("userservlet: "+code);
		String url = String.format(WxConfig.GET_WEBTOKEN, getInitParameter("appId"), getInitParameter("appSecret"), code);
		
		String resp = NetUtil.HttpsRequest(url, "GET");
		
		WebAccessToken webAccessToken = GsonUtils.json2Bean(resp, WebAccessToken.class);
		if(webAccessToken != null){
			url = String.format(WxConfig.GET_USERMSG, webAccessToken.getAccess_token(), webAccessToken.getOpenid());
			resp = NetUtil.httpGet(url);
			response.getWriter().write(resp);
		}
	}
}
