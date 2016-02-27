package com.szaoto.ak10.dataaccess;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

/**
 * ��pull����xml�����˷�װ�����ø�ÿ��xml���ٴ���һ����Ӧ�Ľ�����
 * 
 * @author liangdb
 * 
 *
 */
public class XmlParse {
	
	/**
	 * ����XML
	 * @param is        xml�ֽ���
	 * @param clazz     �ֽ���      �磺Object.class
	 * @param startName       ��ʼλ��
	 * @return          ����List�б�
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getXmlList(InputStream is, Class<?> clazz, String startName) {
		List list = null;
		XmlPullParser parser = Xml.newPullParser();
		Object object = null;
		try {
			parser.setInput(is, "UTF-8");
			//�¼�����
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<Object>();
						break;
					case XmlPullParser.START_TAG:
						//��õ�ǰ�ڵ�Ԫ�ص�����
						String name = parser.getName();
						if (startName.equals(name)) {
							object = clazz.newInstance();
							//�жϱ�ǩ���Ƿ������ԣ�����У���ȫ����������
							int count = parser.getAttributeCount();
							for(int i=0; i<count; i++)
								setXmlValue(object, parser.getAttributeName(i), parser.getAttributeValue(i));
						} else if (object != null) {
							setXmlValue(object, name, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						if (startName.equals(parser.getName())) {
							list.add(object);
							object = null;
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("xml pull error", e.toString());
		}
		return list;
	}
	
	
	
	/**
	 * ����XML
	 * @param is        xml�ֽ���
	 * @param clazz     �ֽ���      �磺Object.class
	 * @return          ����Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getXmlObject(InputStream is, Class<?> clazz) {
		XmlPullParser parser = Xml.newPullParser();
		Object object = null;
		List list = null;
		Object subObject = null;
	//	parser.
		String subName = null;
		try {
			parser.setInput(is, "UTF-8");
		//	parser.setProperty(name, value)
			//�¼�����
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					object = clazz.newInstance();
					break;
				case XmlPullParser.START_TAG:
					//��õ�ǰ�ڵ�Ԫ�ص�����
					String name = parser.getName();
					Field[] f = null;
					if(subObject == null){
						f = object.getClass().getDeclaredFields();
						//�жϱ�ǩ���Ƿ������ԣ�����У���ȫ����������
						int count = parser.getAttributeCount();
						for(int j = 0; j < count; j ++)
							setXmlValue(object, parser.getAttributeName(j), parser.getAttributeValue(j));
					}else{
						f = subObject.getClass().getDeclaredFields();
					}
					
					for(int i = 0; i < f.length; i++){
						if(f[i].getName().equalsIgnoreCase(name)){
							//�ж��ǲ���List����
							if(f[i].getType().getName().equals("java.util.List")){
								Type type = f[i].getGenericType();
								if (type instanceof ParameterizedType) {
									//��÷��Ͳ�����ʵ������
									Class<?> subClazz = (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
									subObject = subClazz.newInstance();
									subName = f[i].getName();
									
									//�жϱ�ǩ���Ƿ������ԣ�����У���ȫ����������
									int count = parser.getAttributeCount();
									for(int j=0; j<count; j++)
										setXmlValue(subObject, parser.getAttributeName(j), parser.getAttributeValue(j));
									
									if(list == null){
										list = new ArrayList<Object>();
										f[i].setAccessible(true);
										f[i].set(object, list);
									}
								}
							}else{   //��ͨ����
								if(subObject != null){
									setXmlValue(subObject, name, parser.nextText());
								}else{
									setXmlValue(object, name, parser.nextText());
								}
							}
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if (subObject != null && subName.equalsIgnoreCase(parser.getName())) {
						list.add(subObject);
						subObject = null;
						subName = null;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("xml pull error", e.getMessage());
		}
		return object;
	}
	
	
	/**
	 * ��xml��ǩ��ֵ��ת���ɶ��������Ե�ֵ
	 * @param  t    ����
	 * @param name   xml��ǩ��
	 * @param value  xml��ǩ����Ӧ��ֵ
	 */
	private void setXmlValue(Object t, String name, String value){
		try {
			Field[] f = t.getClass().getDeclaredFields();
			for(int i = 0; i < f.length; i++){
				if(f[i].getName().equalsIgnoreCase(name)){
					f[i].setAccessible(true);
					//�����������
					Class<?> fieldType = f[i].getType();
					if(fieldType == String.class) {
						f[i].set(t, value);
					}else if(fieldType == Integer.TYPE) {
						f[i].set(t, Integer.parseInt(value));
					}else if(fieldType == Float.TYPE) {
						f[i].set(t, Float.parseFloat(value));
					}else if(fieldType == Double.TYPE) {
						f[i].set(t, Double.parseDouble(value));
					}else if(fieldType == Long.TYPE) {
						f[i].set(t, Long.parseLong(value));
					}else if(fieldType == Short.TYPE) {
						f[i].set(t, Short.parseShort(value));
					}else if(fieldType == Boolean.TYPE) {
						f[i].set(t, Boolean.parseBoolean(value));
					}else{
						f[i].set(t, value);
					}
				}
			}
		} catch (Exception e) {
			Log.e("xml error", e.toString());
		}
	}
	/*
	private boolean IsClassAvailable(Class<?> clazz)
	{
		boolean state = true;
		Field[] f = null;
		f = clazz.getDeclaredFields();
		String string = null;
		string = new String();
		for(int i = 0; i<f.length; i++){
			 string = f[i].getName();
				Class<?> fieldType = f[i].getType();
				if(fieldType == String.class) {
				}else if(fieldType == Integer.TYPE) {
				}else if(fieldType == Float.TYPE) {
				}else if(fieldType == Double.TYPE) {
				}else if(fieldType == Long.TYPE) {
				}else if(fieldType == Short.TYPE) {
				}else if(fieldType == Boolean.TYPE) {
				}else{
					state = false;
				}
		}
		return state;	
	}
	*/
}
