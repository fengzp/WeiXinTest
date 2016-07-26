package com.feng.web.service.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feng.web.model.AccessToken;
import com.feng.web.model.AccessTokenInfo;
import com.feng.web.model.WxConfig;
import com.feng.web.service.util.GsonUtils;
import com.feng.web.service.util.NetUtil;

/**
 * 获取accessToken的Servlet
 * @author fengzp
 *
 */
@WebServlet(
         name = "AccessTokenServlet",
         urlPatterns = {"/AccessTokenServlet"},
         loadOnStartup = 1,
         initParams = {
                 @WebInitParam(name = "appId", value = WxConfig.appId),
                 @WebInitParam(name = "appSecret", value = WxConfig.appSecret)
         })
public class AccessTokenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7318150388093742524L;

	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("AccessTokenServlet启动");
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try{
						AccessTokenInfo.accessToken = getAccessToken();
						if(AccessTokenInfo.accessToken != null){
							System.out.println(AccessTokenInfo.accessToken.toString());
							Thread.sleep(1000 * 7000);
						}else{
							Thread.sleep(1000 * 3);
						}
					}catch(Exception e){
						System.out.println("发生异常:"+e.getMessage());
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try{
						String ticket = getJsapiTicket();
						if(ticket != null){
							WxConfig.jsApiTicket = ticket;
							System.out.println("JsApiTicket: "+ticket);
							Thread.sleep(1000 * 7000);
						}else{
							Thread.sleep(1000 * 3);
						}
					}catch(Exception e){
						System.out.println("发生异常:"+e.getMessage());
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	protected AccessToken getAccessToken() {
		//http请求方式: GET
		String url = String.format("https://api.weixin.qq.com/cgi-bin/token?"
				+ "grant_type=client_credential&appid=%s&secret=%s", WxConfig.appId, WxConfig.appSecret);
		//{"access_token":"ACCESS_TOKEN","expires_in":7200}
		String result = NetUtil.HttpsRequest(url, "");
		
		return GsonUtils.json2Bean(result, AccessToken.class);
	}
	
	protected String getJsapiTicket() {
		//http请求方式: GET
		if(AccessTokenInfo.accessToken != null){
			String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
					+ "access_token=%s&type=jsapi", AccessTokenInfo.accessToken.getAccess_token());
			String result = NetUtil.httpGet(url);
	        JSONObject json = JSON.parseObject(result);
	        return json.getString("ticket");
		}else{
			return null;
		}
	}
}
