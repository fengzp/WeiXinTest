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
			if(content.equals("�ı�")){
				result.setContent("Hi!");
				result.setMsgType(MessageType.TEXT.name());
			}else if(content.equals("����")){
				Voice voice = new Voice();
				voice.setMediaId(WxConfig.VoiceMediaId);
				result.setVoice(voice);
				result.setMsgType(MessageType.VOICE.name());
			}else if(content.equals("ͼƬ")){
				Image image = new Image();
				image.setMediaId(WxConfig.ImageMediaId);
				result.setImage(image);
				result.setMsgType(MessageType.IMAGE.name());
			}else{
				result.setContent("�������ı������֡�ͼƬ");
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
			result.setContent("�������ı������֡�ͼƬ");
			result.setMsgType(MessageType.TEXT.name());
		}
		
		return result;
	}
	

	private static String getUtcTime() {
        Date dt = new Date();// �������Ҫ��ʽ,��ֱ����dt,dt���ǵ�ǰϵͳʱ��
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// ������ʾ��ʽ
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }

	/**
     * ����΢�ŷ���������XML��
     * @param request
     * @return map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static Map<String,String> parseXml(HttpServletRequest request) {
    	try {
	        // ����������洢��HashMap��
	        Map<String,String> map = new HashMap<String,String>();
	        // ��request��ȡ��������
	        InputStream inputStream = request.getInputStream();
	        System.out.println("��ȡ������");
	        // ��ȡ������
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(inputStream);
	        // �õ�xml��Ԫ��
	        Element root = document.getRootElement();
	        // �õ���Ԫ�ص������ӽڵ�
	        List<Element> elementList = root.elements();
	
	        // ���������ӽڵ�
	        for (Element e : elementList) {
	            System.out.println(e.getName() + "|" + e.getText());
	            map.put(e.getName(), e.getText());
	        }
	
	        // �ͷ���Դ
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
