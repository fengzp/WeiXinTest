package com.feng.web.service.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feng.web.model.WxMessage;
import com.feng.web.service.util.BeanUtil;
import com.feng.web.service.util.StringUtil;
import com.feng.web.service.util.WxMsgUtil;

/**
 * 
 * @author fengzp
 *
 */
@WebServlet(urlPatterns = "/WxServlet")
public class WxServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6008816817887502362L;

	/**
	 * Token���ɿ����߿���������д����������ǩ������Token��ͽӿ�URL�а�����Token���бȶԣ��Ӷ���֤��ȫ�ԣ�
	 */
	private final String TOKEN = "fengzp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("��ʼУ��ǩ��");

		/**
		 * ����΢�ŷ�������������ʱ���ݹ�����4������
		 */
		String signature = request.getParameter("signature");// ΢�ż���ǩ��signature����˿�������д��token�����������е�timestamp������nonce������
		String timestamp = request.getParameter("timestamp");// ʱ���
		String nonce = request.getParameter("nonce");// �����
		String echostr = request.getParameter("echostr");// ����ַ���

		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		
//		response.getWriter().print(echostr);
		
//		 ����
		String mySignature = StringUtil.sha1(sort(TOKEN, timestamp, nonce));

		// У��ǩ��
		if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
			System.out.println("ǩ��У��ͨ����");
			response.getWriter().write(echostr);
//			response.getWriter().print(echostr);
		} else {
			System.out.println("ǩ��У��ʧ��.");
		}
	}

	public String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}

		return sb.toString();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String result = "";
		try {
			Map<String, String> map = WxMsgUtil.parseXml(request);
			WxMessage msg = (WxMessage) BeanUtil.map2Bean(WxMessage.class, map);
			System.out.println("�û����͵���Ϣ:"+msg.toString());
			WxMessage resultMsg = WxMsgUtil.buildRepeatMsg(msg);
			result = BeanUtil.bean2Xml(resultMsg, null);
			System.out.println("�ظ�����Ϣ:"+result);
			if(result.equals("")){
				result = "�����쳣";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("�����쳣:"+e.getMessage());
		}
		response.getWriter().write(result);
	}
}
