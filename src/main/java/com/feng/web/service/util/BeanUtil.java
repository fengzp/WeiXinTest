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
 * 需要jdom.jar
 * @author fengzp
 * 2016年5月17日上午10:37:26
 */
public class BeanUtil{
	
	/**
	 * 把Bean转成Map
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
	        BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性  
	        Object obj = clazz.newInstance(); // 创建 JavaBean 对象  
	  
	        // 给 JavaBean 对象的属性赋值  
	        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
	        for (int i = 0; i< propertyDescriptors.length; i++) {  
	            PropertyDescriptor descriptor = propertyDescriptors[i];  
	            String propertyName = descriptor.getName();  
	            if(toUpper)
	            	propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	            if (map.containsKey(propertyName)) {  
	                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。  
	                String value = map.get(propertyName);  
	                
					descriptor.getWriteMethod().invoke(obj, value);
	            }  
	        }  
	        return obj;  
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("map2bean转换异常");
		}  
		return null;
    }
	
	public static String bean2Xml(Object obj) throws InstantiationException  { 
		return bean2Xml(obj, null);
	}
	
	/**
	 * 1.<strong>bean类的属性必须有getter、setter方法</strong> 
	 * 2.<strong>生成的xml不会设置到xml的属性</strong> 
	 */
	@SuppressWarnings("unchecked")
	public static String bean2Xml(Object obj,String root) throws InstantiationException  {  
        //加这一句判断是为了方便递归调用，如果root为空，那么视为第一次调用，也就是xml的根  
        if(root==null || root.trim().equals("")){  
            root = "xml";  
        }  
        if(root.equalsIgnoreCase("item")){  
            ////、！！为什么只有这个以的item的i是小写的，其他的标签都是第一个字符大写的。why  
            root = root.toLowerCase();  
        }  
              
        try {  
        //声明一个列表 存放需要转换成xml类的所有设计的父类  
        //这个地方没有涉及到属性为类的地方，应为下面的处理方法是，如果属性是类的话，就递归调用这个方法，所以没有什么需要担心的  
        List<Class<?>> cl = new ArrayList<Class<?>>();  
          
        //获取对象的类  
        Class<?> clazz = obj.getClass();  
          
        //判断，如果父类不是Object的话把所有的类放到上面声明的list里面   
        String simpleName = clazz.getSimpleName();  
          
        while (!"Object".equals(simpleName)) {  
            cl.add(clazz);  
            clazz = clazz.getSuperclass();  
            simpleName = clazz.getSimpleName();  
        }  
        //上面是从子类开始存放的，所以这里要反转  
        Collections.reverse(cl);  
          
        //开始创建xml  
        StringBuffer xml = new StringBuffer("<"+root+">\n");  
          
        for (Class<?> cz : cl) {  
            //System.out.println("list  " + cz.getName());  
            //获取类里面声明的所有属性，之所以上面要获取父级的类，时应为这里只能获取当前类的声明方法，无法获取到集成来的方法  
            Field[] fs = cz.getDeclaredFields();  
            for (Field f : fs) {  
                //获取属性的类型  
                Class<?> ct =f.getType();  
                //获取类对应的名称  
                String ctName = ct.getSimpleName();  
                String fName = f.getName();  
                String firstUpperName = fName.substring(0, 1).toUpperCase()+fName.substring(1);  
                  
                //System.out.println();  
                //当然，如果类是基础类型的直接设值就可以了  
                if("int".equals(ctName) || "long".equals(ctName)|| "double".equals(ctName)|| "float".equals(ctName) || "char".equals(ctName)){  
                    xml.append(getFieldXml(f,cz,obj)).append("\n");  
                }else if("String".equals(ctName)||"char".equals(ctName)){  
                    xml.append(getFieldXml(f,cz,obj)).append("\n");  
                }else {  
                    //如果属性不是基础类型的，则需要递归调用，也就是把上面的方法冲走一遍  
                    //通过执行get方法，获取对象的返回值。  
                    String methodName = "get" +firstUpperName;  
                    Class<?>[] cnull = null;  
                    Method m=cz.getMethod(methodName, cnull);  
                    Object[] osnull =null;  
                    Object reto=m.invoke(obj, osnull);  
                      
                    if(null!=reto){  
                        if(reto instanceof Collection<?>){  
                            //如果结果是集合类型，那么获取子元素继续  
                            xml.append("<"+firstUpperName+">\n");  
                            Collection<Object> ol = (Collection<Object>)reto;  
                            for(Object o:ol){  
                                xml.append(bean2Xml(o,o.getClass().getSimpleName()));  
                            }  
                              
                            xml.append("</"+firstUpperName+">\n");  
                        }else if(reto instanceof Array){  
                            //如果结果是数组类型，那么获取数组里每个元素继续  
                            xml.append("<"+firstUpperName+">\n");  
                            Object[] ol = (Object[])reto;  
                            for(Object o:ol){  
                                xml.append(bean2Xml(o,o.getClass().getSimpleName()));  
                            }  
                              
                            xml.append("</"+firstUpperName+">\n");  
                        } else{  
                            //如果返回值不为null 执行递归调用  
                            xml.append(bean2Xml(reto,ctName));  
                        }  
                    }else{  
                        //如果为null，直接在这个属性里面设置为空就可以了  
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
     * 通过Filed，Class，和Objec对象获取需要生成的对应Filed的字符串 
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
//                    System.err.println(fName +" 对应的值为0");  
                }  
                str +=reto.toString();  
            }else if("double".equals(ctName)|| "float".equals(ctName)){  
                if("0.0".equals(reto.toString())||"0".equals(reto.toString())){  
//                    System.err.println(fName +" 对应的值为0");  
                }  
                str +=reto.toString();  
            }else{  
                str +="<![CDATA["+reto.toString()+"]]>";  
            }  
        }else{  
//            System.err.println(fName +" 对应的值为空");  
        }  
        str +="</"+firstUpperName+">";  
        return str;  
    }  
      
    /** 
     * 通过xml字符串转换成给定类的对象 
     * <br> 
     * 1.转换过程中不会操作任何xml的属性操作，值是一个标签对应一个属性来操作的 
     * <br> 
     * 2.参数Class<?> c对应的类，需要设置的属性都必须有getter，setter方法 
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
//                  System.err.println(ncn + " 实例化失败，请检查是否存在这个类 ");  
//              }  
                if(null==clazz1){  
                    //没有找到对应的类，这里只是忽略  
                    System.out.println(methodName + " 方法未找到，或者返回类型不正确 ");  
                    return;  
                }  
                Object o1 = clazz1.newInstance();  
                  
                try {  
                    //反射获取set方法,帮o1的值设置进o里面  
                    Method m =clazz.getMethod(methodName, clazz1);  
                    m.invoke(o, o1);  
                } catch (Exception e2) {  
                    //应该是没有找到对应的方法，忽略  
                    System.err.println(methodName +" 方法调用失败，请检查方法是否存在");  
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
                        //目前只支持带一个参数的情况  
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
