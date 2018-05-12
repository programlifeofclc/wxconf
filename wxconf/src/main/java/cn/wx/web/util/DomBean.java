package cn.wx.web.util;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomBean {
	
	private static Logger logger = Logger.getLogger(DomBean.class);
	
	private static DocumentBuilderFactory dbf;
	
	private static String dateFormat = "yyyyMMdd";

	static {
		dbf = DocumentBuilderFactory.newInstance();
	}

	public static <T> T xml2Bean(String data, Class<T> clazz) {
		try {
			StringReader sr = new StringReader(data);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(sr));
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			return nodes2Field(nodes, clazz);
		} catch (Exception e) {
			if(e instanceof NoSuchFieldException) {
				logger.info("一般错误：" + e.getMessage());
			}else{
				e.printStackTrace();
			}
		}finally {
			
		}
		return null;
	}

	
	
	private static <T> T nodes2Field(NodeList nodes, Class<T> clazz) {
		try {
			T bean = clazz.newInstance();
			for (int i = 0; i < nodes.getLength(); i++) {
				try {
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = node.getNodeName();
						Field field = clazz.getField(nodeName);
						NodeList nodelist = node.getChildNodes();
						Class<?> fieldclass = field.getType();
						// 数组和非数组分开处理
						if (fieldclass.isArray()) {
							setFieldValue(field, nodes2ArrayField(nodelist , fieldclass.getComponentType()), bean);
						} else {
							if (nodelist.getLength() == 1) {
								setFieldValue(field, node.getTextContent(), bean);
							} else if (nodelist.getLength() > 1) {
								setFieldValue(field, nodes2Field(nodelist, field.getType()), bean);
							}
						}
					}
				} catch (Exception e) {
					if(e instanceof NoSuchFieldException) {
						logger.info("一般错误：" + e.getMessage());
					}else{
						e.printStackTrace();
					}
				}
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T[] nodes2ArrayField(NodeList nodes, Class<T> clazz) {
		Object array = Array.newInstance(clazz, nodes.getLength());
		try {
			for (int y = 0; y < nodes.getLength(); y++) {
				Node sonNode = nodes.item(y);
				if (clazz == String.class) {
					Array.set(array, y, sonNode.getTextContent());
				} else if (clazz == Long.class || clazz == long.class) {
					Array.set(array, y, Long.parseLong(sonNode.getTextContent()));
				} else if (clazz == Integer.class || clazz == int.class) {
					Array.set(array, y, Integer.parseInt(sonNode.getTextContent()));
				} else if (clazz == Double.class || clazz == double.class) {
					Array.set(array, y, Double.parseDouble(sonNode.getTextContent()));
				} else if (clazz == Boolean.class || clazz == boolean.class) {
					Array.set(array, y, Boolean.parseBoolean(sonNode.getTextContent()));
				} else if (clazz == Date.class) {
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					Array.set(array, y, sdf.parse(sonNode.getTextContent()));
				} else {
					Array.set(array, y, nodes2Field(sonNode.getChildNodes(), clazz));
				}
			}
		} catch (Exception e) {
			System.err.println("非必须异常2:" + e.getMessage());
		}
		return (T[]) array;
	}

	private static void setFieldValue(Field field, Object value, Object bean) {
		if (value == null)
			return;
		try {
			Class<?> fieldClazz = field.getType();
			boolean b = field.isAccessible();
			field.setAccessible(true);
			if (fieldClazz == String.class) {
				field.set(bean, value.toString());
			} else if (fieldClazz == Long.class || fieldClazz == long.class) {
				field.set(bean, Long.parseLong(value.toString()));
			} else if (fieldClazz == Integer.class || fieldClazz == int.class) {
				field.set(bean, Integer.parseInt(value.toString()));
			} else if (fieldClazz == Double.class || fieldClazz == double.class) {
				field.set(bean, Double.parseDouble(value.toString()));
			} else if (fieldClazz == Boolean.class || fieldClazz == boolean.class) {
				field.set(bean, Boolean.parseBoolean(value.toString()));
			} else if (fieldClazz == Date.class) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				field.set(bean, sdf.parse(value.toString()));
			} else {
				field.set(bean, value);
			}
			field.setAccessible(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
