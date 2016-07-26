package com.feng.web.model;


public class WxConfig {

	public static String ImageMediaId = "1JcIX4cVYVjASjx-dv8yaJ8MIElTYSev8CoQ1rTLhwkTsxnM2n1v0zxBF1bEGir0";
	public static String VoiceMediaId = "Tr8zTacwl7kDUuF6c7IrAvmfWDkXcLmBCPJ7AApVKPL7rNeXV0TYUTS5Ne__uISd";
	public static String VideoMediaId = "YsgYy0KT1Mz0X9P1v5jxJttjockBgUNKn2z0_yz4l_KI5FhhTWBHJalTOe-CZ8Ig";
	
	public static final String appId = "wxa1d7918774ce0f09";
	public static final String appSecret = "8cfb267d462eae71fb365eb9e35dc201";
	
	public static String jsApiTicket;
	
	//http请求方式: POST/FORM,需使用https
	public static final String UPLOAD_MEDIA = "https://api.weixin.qq.com/cgi-bin/media/upload";
	//http请求方式: GET,https调用
	public static final String DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
	//获取网页授权token
	public static final String GET_WEBTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	//获取用户信息
	public static final String GET_USERMSG = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	/**http请求方式: POST
	   POST数据格式：json
	   POST数据例子：{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
	 */
	public static final String CREATE_TICKET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
	/**
	 * HTTP GET请求
	 * 提醒：TICKET记得进行UrlEncode
	 */
	public static final String GET_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	
}
