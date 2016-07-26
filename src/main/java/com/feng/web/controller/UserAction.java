package com.feng.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.feng.web.model.AccessTokenInfo;
import com.feng.web.model.CreateQrCodeRequest;
import com.feng.web.model.CreateQrCodeRequest.Action_Info;
import com.feng.web.model.CreateQrCodeRequest.Scene;
import com.feng.web.model.CreateQrCodeResponse;
import com.feng.web.model.WebAccessToken;
import com.feng.web.model.WxConfig;
import com.feng.web.model.WxUser;
import com.feng.web.service.util.GsonUtils;
import com.feng.web.service.util.NetUtil;

@Controller
@RequestMapping(value="/user")
public class UserAction {

	@RequestMapping(value="getWxUserMsg", method=RequestMethod.GET)
	public String getWxUserMsg(Model model, String code){
		System.out.println("useraction: "+code);
		String url = String.format(WxConfig.GET_WEBTOKEN, "wxa1d7918774ce0f09", "8cfb267d462eae71fb365eb9e35dc201", code);
		
		String resp = NetUtil.httpGet(url);
		
		WebAccessToken webAccessToken = GsonUtils.json2Bean(resp, WebAccessToken.class);
		if(webAccessToken != null){
			url = String.format(WxConfig.GET_USERMSG, webAccessToken.getAccess_token(), webAccessToken.getOpenid());
			resp = NetUtil.httpGet(url);
			model.addAttribute("user", GsonUtils.json2Bean(resp, WxUser.class));
		}
		
		return "user/wsusermsg";
	}
	
	@RequestMapping(value="createQrCode", method=RequestMethod.GET)
	public String createQrCode(Model model){
		String url = String.format(WxConfig.CREATE_TICKET, AccessTokenInfo.accessToken.getAccess_token());
		CreateQrCodeRequest req = new CreateQrCodeRequest();
		req.setExpire_seconds(1000 * 60 * 60 *24);
		req.setAction_name("QR_SCENE");
		req.setAction_info(new Action_Info(new Scene(1, "123")));
		String result = NetUtil.httpPost(url, GsonUtils.bean2json(req));
		CreateQrCodeResponse resp = GsonUtils.json2Bean(result, CreateQrCodeResponse.class); 
		model.addAttribute("ticket", resp);
		
		try {
			url = String.format(WxConfig.GET_QRCODE, URLEncoder.encode(resp.getTicket(), "UTF-8"));
			System.out.println(url);
			model.addAttribute("url", url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "user/qrcode";
	}
	
	@RequestMapping(value="/share", method=RequestMethod.GET)
	public String share(){
		return "user/share";
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String upload(){
		return "user/upload";
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException{
		String realPath = request.getSession().getServletContext().getRealPath("/resource");
		System.out.println(realPath);
		File uploadFile = new File(realPath + "/" + file.getOriginalFilename());
		FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);
		return "user/uploadsuccess";
	}
}
