package com.feng.web.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;

import com.feng.web.model.WxConfig;
import com.feng.web.model.WxMessage;

public class WxMediaUtil {
	
	private static final String token = "NyTgtiz3-g6fRFqq3MQQ6TJ-C0Fd1-Loh7ZG34e8dKk7ycslWrpcfwh06-nbjoa5ZqxcOaerjG3PD2QlWWSXnziBici1j1rj_gMRCI5BjrbwDFOWxnjtzR0YeeDIzWkfMVPhAHAIRL";
	
	public static void main(String[] args) {
		File file = new File("C:/Users/feng/Desktop/feng/2.JPG");
		String result = WxMediaUtil.upload(WxMessage.MessageType.IMAGE.name(), file);
		
//		File file = new File("C:/Users/feng/Desktop/feng/1.mp3");
//		String result = WxMediaUtil.upload(WxMessage.MessageType.VOICE.name(), file);
		
//		File file = new File("C:/Users/feng/Desktop/feng/apple.mp4");
//		String result = WxMediaUtil.upload(WxMessage.MessageType.VIDEO.name(), file);
		System.out.println(result);
		
		//Q--HOiK8Ye5foHegBcqrpeNtzKgy6m-y-3dDLWTOyOwaqBf_u-9kr4DsJkXOH5TO
		//Tr8zTacwl7kDUuF6c7IrAvmfWDkXcLmBCPJ7AApVKPL7rNeXV0TYUTS5Ne__uISd
		//YsgYy0KT1Mz0X9P1v5jxJttjockBgUNKn2z0_yz4l_KI5FhhTWBHJalTOe-CZ8Ig
//		String id = "niArEt9PIAkLNaUJPUproo89UqAgkNY7xUjzWYBAw64TLv5cZW-hI6FsnD06JDjf";
//		String name = id+".mp4";
//		File file = download("C:/Users/feng/Desktop/down/"+name, String.format(DOWNLOAD_MEDIA, token, id), "GET", null);
//		System.out.println(file.getName());
	}
	
	@SuppressWarnings("deprecation")
	public static String upload(String type, File file) {
		String result = "";

		if (file == null || token == null || type == null) {
			return null;
		}

		if (!file.exists()) {
			System.out.println("上传文件不存在,请检查!");
			return null;
		}

		String url = WxConfig.UPLOAD_MEDIA;
		// JSONObject jsonObject = null;
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Cache-Control", "no-cache");
		FilePart media;
		HttpClient httpClient = new HttpClient();
		// 信任任何类型的证书
		Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		try {
			media = new FilePart("media", file);
			Part[] parts = new Part[] { new StringPart("access_token", token), new StringPart("type", type), media };
			MultipartRequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(entity);
			int status = httpClient.executeMethod(post);
			if (status == HttpStatus.SC_OK) {
				result = post.getResponseBodyAsString();
				// jsonObject = JSONObject.parseObject(text);
			} else {
				System.out.println("upload Media failure status is:" + status);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
