package com.feng.web.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.feng.web.model.WxConfig;
import com.feng.web.model.WxMessage;
import com.feng.web.model.WxMessage.Image;
import com.feng.web.model.WxMessage.MessageType;
import com.feng.web.model.WxMessage.Voice;

public class WxMsgUtil {
	
	public static WxMessage buildRepeatMsg(WxMessage msg){
    	WxMessage result = new WxMessage();
    	result.setFromUserName(msg.getToUserName());
    	result.setToUserName(msg.getFromUserName());
    	result.setCreateTime(getUtcTime());
    	
    	result = buildTestMsg(result, msg);
    	
    	return result;
    }
	
	private static WxMessage buildTestMsg(WxMessage result, WxMessage msg) {
		String content = msg.getContent();
		String msgType = msg.getMsgType();
		if(msgType.equalsIgnoreCase(MessageType.TEXT.name()) && content!=null && !content.trim().equals("")){
			if(content.equals("文本")){
				result.setContent("Hi!");
				result.setMsgType(MessageType.TEXT.name());
			}else if(content.equals("音乐")){
				Voice voice = new Voice();
				voice.setMediaId(WxConfig.VoiceMediaId);
				result.setVoice(voice);
				result.setMsgType(MessageType.VOICE.name());
			}else if(content.equals("图片")){
				Image image = new Image();
				image.setMediaId(WxConfig.ImageMediaId);
				result.setImage(image);
				result.setMsgType(MessageType.IMAGE.name());
			}else{
				result.setContent("请输入文本、音乐、图片");
				result.setMsgType(MessageType.TEXT.name());
			}
		}else if(msgType.equalsIgnoreCase(MessageType.EVENT.name())){
			String s = "event:"+msg.getEvent();
			if(msg.getEventKey()!=null)
				s += "  eventKey:"+msg.getEventKey();
			if(msg.getTicket()!=null)
				s += "  ticket:"+msg.getTicket();
			result.setContent(s);
			result.setMsgType(MessageType.TEXT.name());
		}else{
			result.setContent("请输入文本、音乐、图片");
			result.setMsgType(MessageType.TEXT.name());
		}
		
		return result;
	}
	

	private static String getUtcTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }

	/**
     * 解析微信发来的请求（XML）
     * @param request
     * @return map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static Map<String,String> parseXml(HttpServletRequest request) {
    	try {
	        // 将解析结果存储在HashMap中
	        Map<String,String> map = new HashMap<String,String>();
	        // 从request中取得输入流
	        InputStream inputStream = request.getInputStream();
	        System.out.println("获取输入流");
	        // 读取输入流
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(inputStream);
	        // 得到xml根元素
	        Element root = document.getRootElement();
	        // 得到根元素的所有子节点
	        List<Element> elementList = root.elements();
	
	        // 遍历所有子节点
	        for (Element e : elementList) {
	            System.out.println(e.getName() + "|" + e.getText());
	            map.put(e.getName(), e.getText());
	        }
	
	        // 释放资源
	        inputStream.close();
	        inputStream = null;
	        return map;
    	} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
    	return null;
    }
    
    public static String bean2Xml(WxMessage msg){
    	StringBuffer buffer  = new StringBuffer();
		buffer.append("<xml>");
		
		Map<String, Object> p = BeanUtil.bean2Map(msg);
		Set<String> s = p.keySet();
		Iterator<String> i = s.iterator();
		
		while (i.hasNext()){
			String key = i.next();
			if(key.equals("class")){}
			else {
				Object value = p.get(key);
				String name = key.substring(0, 1).toUpperCase() + key.substring(1);
				buffer.append("<");
				buffer.append(name);
				buffer.append(">");
				if(value == null || value.toString().equals("")){
					buffer.append("");
				} else {
					buffer.append(value);
				}
				buffer.append("</");
				buffer.append(name);
				buffer.append(">");
			}
		}
		
		buffer.append("</xml>");
		return buffer.toString();
    }
}
