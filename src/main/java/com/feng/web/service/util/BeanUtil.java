package com.feng.web.service.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * ��Ҫjdom.jar
 * @author fengzp
 * 2016��5��17������10:37:26
 */
public class BeanUtil{
	
	/**
	 * ��Beanת��Map
	 */
	public static Map<String, Object> bean2Map(Object bean)  {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			Map<String, Object> fields = new HashMap<String, Object>();
			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
				fields.put(pd.getName(), pd.getReadMethod().invoke(bean));
			}
			return fields;
		} catch (IntrospectionException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static Object map2Bean(Class<?> clazz, Map<String, String> map) {  
		return map2Bean(clazz, map, true);
    }
	
	public static Object map2Bean(Class<?> clazz, Map<String, String> map, boolean toUpper) {  
		try {
	        BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // ��ȡ������  
	        Object obj = clazz.newInstance(); // ���� JavaBean ����  
	  
	        // �� JavaBean ��������Ը�ֵ  
	        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
	        for (int i = 0; i< propertyDescriptors.length; i++) {  
	            PropertyDescriptor descriptor = propertyDescriptors[i];  
	            String propertyName = descriptor.getName();  
	            if(toUpper)
	            	propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	            if (map.containsKey(propertyName)) {  
	                // ����һ����� try ������������һ�����Ը�ֵʧ�ܵ�ʱ��Ͳ���Ӱ���������Ը�ֵ��  
	                String value = map.get(propertyName);  
	                
					descriptor.getWriteMethod().invoke(obj, value);
	            }  
	        }  
	        return obj;  
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("map2beanת���쳣");
		}  
		return null;
    }
	
	public static String bean2Xml(Object obj) throws InstantiationException  { 
		return bean2Xml(obj, null);
	}
	
	/**
	 * 1.<strong>bean������Ա�����getter��setter����</strong> 
	 * 2.<strong>���ɵ�xml�������õ�xml������</strong> 
	 */
	@SuppressWarnings("unchecked")
	public static String bean2Xml(Object obj,String root) throws InstantiationException  {  
        //����һ���ж���Ϊ�˷���ݹ���ã����rootΪ�գ���ô��Ϊ��һ�ε��ã�Ҳ����xml�ĸ�  
        if(root==null || root.trim().equals("")){  
            root = "xml";  
        }  
        if(root.equalsIgnoreCase("item")){  
            ////������Ϊʲôֻ������Ե�item��i��Сд�ģ������ı�ǩ���ǵ�һ���ַ���д�ġ�why  
            root = root.toLowerCase();  
        }  
              
        try {  
        //����һ���б� �����Ҫת����xml���������Ƶĸ���  
        //����ط�û���漰������Ϊ��ĵط���ӦΪ����Ĵ������ǣ������������Ļ����͵ݹ�����������������û��ʲô��Ҫ���ĵ�  
        List<Class<?>> cl = new ArrayList<Class<?>>();  
          
        //��ȡ�������  
        Class<?> clazz = obj.getClass();  
          
        //�жϣ�������಻��Object�Ļ������е���ŵ�����������list����   
        String simpleName = clazz.getSimpleName();  
          
        while (!"Object".equals(simpleName)) {  
            cl.add(clazz);  
            clazz = clazz.getSuperclass();  
            simpleName = clazz.getSimpleName();  
        }  
        //�����Ǵ����࿪ʼ��ŵģ���������Ҫ��ת  
        Collections.reverse(cl);  
          
        //��ʼ����xml  
        StringBuffer xml = new StringBuffer("<"+root+">\n");  
          
        for (Class<?> cz : cl) {  
            //System.out.println("list  " + cz.getName());  
            //��ȡ�������������������ԣ�֮��������Ҫ��ȡ�������࣬ʱӦΪ����ֻ�ܻ�ȡ��ǰ��������������޷���ȡ���������ķ���  
            Field[] fs = cz.getDeclaredFields();  
            for (Field f : fs) {  
                //��ȡ���Ե�����  
                Class<?> ct =f.getType();  
                //��ȡ���Ӧ������  
                String ctName = ct.getSimpleName();  
                String fName = f.getName();  
                String firstUpperName = fName.substring(0, 1).toUpperCase()+fName.substring(1);  
                  
                //System.out.println();  
                //��Ȼ��������ǻ������͵�ֱ����ֵ�Ϳ�����  
                if("int".equals(ctName) || "long".equals(ctName)|| "double".equals(ctName)|| "float".equals(ctName) || "char".equals(ctName)){  
                    xml.append(getFieldXml(f,cz,obj)).append("\n");  
                }else if("String".equals(ctName)||"char".equals(ctName)){  
                    xml.append(getFieldXml(f,cz,obj)).append("\n");  
                }else {  
                    //������Բ��ǻ������͵ģ�����Ҫ�ݹ���ã�Ҳ���ǰ�����ķ�������һ��  
                    //ͨ��ִ��get��������ȡ����ķ���ֵ��  
                    String methodName = "get" +firstUpperName;  
                    Class<?>[] cnull = null;  
                    Method m=cz.getMethod(methodName, cnull);  
                    Object[] osnull =null;  
                    Object reto=m.invoke(obj, osnull);  
                      
                    if(null!=reto){  
                        if(reto instanceof Collection<?>){  
                            //�������Ǽ������ͣ���ô��ȡ��Ԫ�ؼ���  
                            xml.append("<"+firstUpperName+">\n");  
                            Collection<Object> ol = (Collection<Object>)reto;  
                            for(Object o:ol){  
                                xml.append(bean2Xml(o,o.getClass().getSimpleName()));  
                            }  
                              
                            xml.append("</"+firstUpperName+">\n");  
                        }else if(reto instanceof Array){  
                            //���������������ͣ���ô��ȡ������ÿ��Ԫ�ؼ���  
                            xml.append("<"+firstUpperName+">\n");  
                            Object[] ol = (Object[])reto;  
                            for(Object o:ol){  
                                xml.append(bean2Xml(o,o.getClass().getSimpleName()));  
                            }  
                              
                            xml.append("</"+firstUpperName+">\n");  
                        } else{  
                            //�������ֵ��Ϊnull ִ�еݹ����  
                            xml.append(bean2Xml(reto,ctName));  
                        }  
                    }else{  
                        //���Ϊnull��ֱ�������������������Ϊ�վͿ�����  
                        xml.append("<"+firstUpperName+">\n</"+firstUpperName+">\n");  
                    }  
                }  
            }  
              
        }  
        xml.append("</"+root+">\n");  
          
        return xml.toString();  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        }   
        return null;  
    }  
  
    /** 
     * ͨ��Filed��Class����Objec�����ȡ��Ҫ���ɵĶ�ӦFiled���ַ��� 
     */  
    private static String getFieldXml(Field f, Class<?> cz, Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{  
        String fName = f.getName();  
          
        String firstUpperName = fName.substring(0, 1).toUpperCase()+fName.substring(1);  
        String str = "<"+firstUpperName+">";  
          
        String methodName = "get" +firstUpperName;  
          
//        System.out.println(fName+"  "+methodName);   
        Class<?>[] cnull = null;  
        Method m=cz.getMethod(methodName, cnull);  
        Object[] osnull =null;  
        Object reto=m.invoke(obj, osnull);  
        if(null!=reto){  
              
            Class<?> ct =f.getType();  
            String ctName = ct.getSimpleName();  
            if("int".equals(ctName) || "long".equals(ctName)){  
                if("0".equals(reto.toString())){  
//                    System.err.println(fName +" ��Ӧ��ֵΪ0");  
                }  
                str +=reto.toString();  
            }else if("double".equals(ctName)|| "float".equals(ctName)){  
                if("0.0".equals(reto.toString())||"0".equals(reto.toString())){  
//                    System.err.println(fName +" ��Ӧ��ֵΪ0");  
                }  
                str +=reto.toString();  
            }else{  
                str +="<![CDATA["+reto.toString()+"]]>";  
            }  
        }else{  
//            System.err.println(fName +" ��Ӧ��ֵΪ��");  
        }  
        str +="</"+firstUpperName+">";  
        return str;  
    }  
      
    /** 
     * ͨ��xml�ַ���ת���ɸ�����Ķ��� 
     * <br> 
     * 1.ת�������в�������κ�xml�����Բ�����ֵ��һ����ǩ��Ӧһ�������������� 
     * <br> 
     * 2.����Class<?> c��Ӧ���࣬��Ҫ���õ����Զ�������getter��setter���� 
     */  
    public static <T> T xml2Bean(String xml, Class<T> c) {  
        T t = null;  
        try {  
            t= c.newInstance();  
            SAXBuilder sb = new SAXBuilder();  
            StringReader read = new StringReader(xml);  
            InputSource source = new InputSource(read);  
            Document doc = sb.build(source);  
            Element root =doc.getRootElement();  
            convery(root,c,t);  
              
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return t;  
    }  
  
    @SuppressWarnings("unchecked")
	private static void convery(Element e,Class<?> clazz,Object o) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{  
        String en =e.getName();  
        if("xml".equals(en)){  
            List<Element> el = e.getChildren();  
            for(Element e1:el){  
                convery(e1,clazz,o);  
            }  
        }else{  
            List<Element> el = e.getChildren();  
            if(el.size()>0){  
                String childClassName =e.getName();  
                  
                String methodName = "get" +childClassName.substring(0, 1).toUpperCase()+childClassName.substring(1);  
                  
                Class<?> clazz1 = null;  
                Method[] ms = clazz.getMethods();  
                for(Method m:ms){  
//                  System.out.println(m.getName());  
                    if(methodName.equals(m.getName())){  
                        clazz1 = m.getReturnType();  
                    }  
                }  
//              try {  
//                  clazz1 = Class.forName(ncn);  
//              } catch (Exception e2) {  
//                  System.err.println(ncn + " ʵ����ʧ�ܣ������Ƿ��������� ");  
//              }  
                if(null==clazz1){  
                    //û���ҵ���Ӧ���࣬����ֻ�Ǻ���  
                    System.out.println(methodName + " ����δ�ҵ������߷������Ͳ���ȷ ");  
                    return;  
                }  
                Object o1 = clazz1.newInstance();  
                  
                try {  
                    //�����ȡset����,��o1��ֵ���ý�o����  
                    Method m =clazz.getMethod(methodName, clazz1);  
                    m.invoke(o, o1);  
                } catch (Exception e2) {  
                    //Ӧ����û���ҵ���Ӧ�ķ���������  
                    System.err.println(methodName +" ��������ʧ�ܣ����鷽���Ƿ����");  
                    return ;  
                }  
                  
                for(Element e1:el){  
                    convery(e1,clazz1,o1);  
                }  
            }else{  
                String eName = e.getName();  
                String methodName = "set" +eName.substring(0, 1).toUpperCase()+eName.substring(1);  
                Method[] ms=clazz.getMethods();  
                for(Method m:ms){  
                    if(m.getName().equals(methodName)){  
                        Class<?>[] c= m.getParameterTypes();  
                        //Ŀǰֻ֧�ִ�һ�����������  
                        String text = e.getTextTrim();  
                        Object v = null;  
                        String cn = c[0].getSimpleName();  
                          
                        if("int".equals(cn)){  
                            v = Integer.parseInt(text);  
                        }else if("long".equals(cn)){  
                            v= Long.parseLong(text);  
                        }else if("double".equals(cn)){  
                            v = Double.parseDouble(text);  
                        }else if("float".equals(cn)){  
                            v = Float.parseFloat(text);  
                        }else{  
                            v = text;  
                        }  
                        m.invoke(o,v);  
                    }  
                }  
            }  
        }  
    }  
}
