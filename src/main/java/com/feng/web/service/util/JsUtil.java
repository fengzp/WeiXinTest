package com.feng.web.service.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.feng.web.model.WxConfig;

public class JsUtil {

	public static String createJsApiSignature(String noncestr, String timestamp, String url){
		if(WxConfig.jsApiTicket == null)
			return "";
		
		String jsapi_ticket = WxConfig.jsApiTicket;
		
		System.out.println(noncestr);
		System.out.println(timestamp);
		System.out.println(url);
		System.out.println(jsapi_ticket);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(noncestr, "noncestr");
		map.put(jsapi_ticket, "jsapi_ticket");
		map.put(timestamp, "timestamp");
		map.put(url, "url");
		
		String[] strArray = { noncestr, jsapi_ticket, timestamp, url };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(map.get(str)).append("=").append(str).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		System.out.println("js_sign: "+sb.toString());
		
		return StringUtil.sha1(sb.toString());
	}
	
	public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //ע���������������ȫ��Сд���ұ�������
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
