package com.feng.web.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WxMenuUtil {
	
	public static String access_token= "r93uvBIxz9TQUxfPQxyzS8EpoS70cii2OR9twMvT6TEPHLL03TDrp0EOT_1zvkFQ5noZvevlKlCs5AH26B7MXudhp3tcCAGCDYzqTWqprZf-I7QSsZbxVCFWBQ0pQZHiCMAgAEAFPD";

	public static String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
	
	public static String deleteMenu = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+access_token;
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = URLEncoder.encode("http://fengzp.ngrok.natapp.cn/WeXinTest/mvc/user/upload", "utf-8");
		System.out.println(s);
		WxMenuUtil.createWxMenu();
//		WxMenuUtil.deleteWxMenu();
	}
	
	public static void deleteWxMenu(){
		NetUtil.HttpsRequest(deleteMenu, "GET");
	}

	public static void createWxMenu() {
		try {
			File file = new File("C:/Users/feng/Desktop/1.txt");
			if (file.isFile() && file.exists()) {
				FileInputStream in = new FileInputStream(file); 
	            //微信只接受UTF-8的格式
	            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				StringBuffer sb = new StringBuffer();
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					sb.append(lineTxt); 
				}
				in.close();
				br.close();
				
				NetUtil.httpPost(action, sb.toString());
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
	}
	
}
