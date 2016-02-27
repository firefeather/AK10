/*
   * 文件名 AcquisitionCard.java
   * 包含类名列表com.szaoto.ak10.configuration
   * 版本信息，版本号
   * 创建日期2013年12月5日下午3:03:57
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.dataaccess;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.szaoto.ak10.HomePageActivity;


public class ReadXmlToClass {
	//private final static String sFile1 = "CopyofCopyoftest.xml";
	private String sFile1 = null;
	public ReadXmlToClass(String filename) {
		sFile1 = filename;
	}

	public boolean Assignment(Object object) {
		
		InputStream is = null;
		try {
			is = new FileInputStream(HomePageActivity.CONFIG_PATH + sFile1);
		} catch (FileNotFoundException e) {
	
			e.printStackTrace();
		}
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			//事件类型
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					eventType = parser.next();
				}else if(eventType == XmlPullParser.START_TAG)
				{
					if (parser.getAttributeCount() == 1) {
						String string = null;
						String string1 = null;
						string = parser.getAttributeValue(0).toString();
						string1 = object.getClass().getName().toString();
						if (string1.equals(string))
						{
							eventType = parser.next();
							Assignmention(object , parser,parser.getName());	
						}
						eventType = parser.next();
					}
				}
				else if(eventType == XmlPullParser.END_TAG)
				{
					eventType = parser.next();
				}else if(eventType == XmlPullParser.TEXT)
				{
					eventType = parser.next();
				}
		//		eventType = parser.next();
			}	
		} catch (Exception e) {
			Log.e("xml pull error", e.getMessage());
		}
		return true;
	
	}
	private  returnClass findsubobject(Object object,String classname) {
		Field[] f = object.getClass().getDeclaredFields();
		returnClass retclass = new returnClass ();

		for(int i = 0; i < f.length; i++){
			String fieldType = f[i].getName();
			if (fieldType.equals(classname)) {
				try {
					try {
						Class<?> fieldType1 = f[i].getType();
						if(!fieldType1.getName().equals("java.util.List") ){	
							Class cls = Class.forName(fieldType1.getName());
							Constructor con = cls.getConstructor(); 
							Object object2 =  con.newInstance();
							retclass.obj = object2;
							retclass.clazz = null;
						}else{
							Class<?> fieldType2 = f[i].getType();
							Type type = f[i].getGenericType();
							Class<?> subClazz = null;
								if (type instanceof ParameterizedType) {
									//获得泛型参数的实际类型
									subClazz = (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
								}
								retclass.clazz = subClazz;
								List temp = new ArrayList<Object>();
								retclass.obj = temp;
							}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}	
		return retclass;
	}
	private Object findsubclass(Object object,String classname) {
		Field[] f = object.getClass().getDeclaredFields();
		Class<?> fieldType1 = null;
		for(int i = 0; i < f.length; i++){
			String fieldType = f[i].getName();
			if (fieldType.equals(classname)) {
				try {
					fieldType1	= f[i].getType();
					//	obj1 = fieldType1.newInstance();
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}	
		return fieldType1;
	}
	
	private void Assignmention(Object object , XmlPullParser parser,String currentsign) {
		int eventType;
		try {
			eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if (name.equals("default") ) {
						currentsign = "default";
					//	object
						int count = parser.getAttributeCount();
						for(int j=0; j<count; j++)
						{
							setXmlValue(object, parser.getAttributeName(j), parser.getAttributeValue(j));	
						}
					}else {
						returnClass rcls = null;
						rcls =	findsubobject(object,name);
						if ( rcls.obj != null) {
							//Class<? > class1 = rcls.obj.getClass();
							//String string = class1.getName();
							if(IsClassAvailable(rcls.obj.getClass()))
							{
								int count = parser.getAttributeCount();
								for(int j=0; j<count; j++)
								{
									setXmlValue(rcls.obj, parser.getAttributeName(j), parser.getAttributeValue(j));	
								}
								Field[] f = object.getClass().getDeclaredFields();
								for(int i = 0;i< f.length;i++)
								{
									if(f[i].getType().getName().equals(rcls.obj.getClass().getName()))
									{
										f[i].setAccessible(true);
										f[i].set(object, rcls.obj);
									}
								}
							}
							else 
							{
								String string2 = rcls.obj.getClass().toString();
								if (string2.equals("java.util.List") ||string2.equals("class java.util.ArrayList")) {
							
									try {
									List<Object> listtemp = new ArrayList() ;
									listtemp = (List<Object>) rcls.obj;
									parser.next();
								
									eventType = parser.getEventType();
									
									//先要检查有没有子项，没有子项的话，return null，否则这里就是死循环
									if(eventType==XmlPullParser.END_TAG)
									{
										object = null;
										return;
									}
									///////////////////////////////////////																		
									while (parser.getEventType() !=XmlPullParser.START_TAG ) {
										parser.next();	
									}
									String name2 = parser.getName();
									
									while(parser.getName().indexOf("listmember")==0)
									{
										Constructor con;
										Object object2 = null ;
										try {
											Class cls;
											cls = Class.forName(rcls.clazz.getName());
											con = cls.getConstructor(); 
										//	con = rcls.clazz.getConstructor();
											object2 =  con.newInstance();
										} catch (NoSuchMethodException e) {
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											e.printStackTrace();
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
										int eventt = parser.getEventType();
										
										Assignmention( object2 , parser,parser.getName());
										listtemp.add(object2);
										while (parser.getEventType() !=2 ) {
											int kk = parser.getEventType();
											String strsString =parser.getName();
											if(strsString != null
												&&strsString.indexOf("listmember")!= 0
												&& kk != 4){
												Field[] f = object.getClass().getDeclaredFields();
												for(int i = 0;i< f.length;i++)
												{
													if(f[i].getType().getName().equals("java.util.List"))
													{
														f[i].setAccessible(true);
														f[i].set(object, listtemp);
													}
												}
												break;
												}
											parser.next();	
										}
									}
					
									} catch (InstantiationException e) {
										e.printStackTrace();
									}
								}
								else{
									Assignmention( rcls.obj ,  parser,name);
									Field[] f = object.getClass().getDeclaredFields();
									for(int i = 0;i< f.length;i++)
									{
										if(f[i].getType().getName().equals(rcls.obj.getClass().getName()))
										{
											f[i].setAccessible(true);
											f[i].set(object, rcls.obj);
										}
									}
								}
							}	
						}else {
							int count = parser.getAttributeCount();
							for(int j=0; j<count; j++)
							{
								setXmlValue(object, parser.getAttributeName(j), parser.getAttributeValue(j));	
							}
						}
					}
					
					break;
				case XmlPullParser.END_TAG:
					String name1 = parser.getName();
					object.getClass();
					if(name1.equals(currentsign))
					{
					//	eventType = parser.next();
						return;
					}					
					break;
				case XmlPullParser.TEXT:
					break;
				}
				eventType = parser.next();
			}		
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private boolean IsClassAvailable(Class<?> clazz)
	{
		boolean state = true;
		Field[] f = null;
		f = clazz.getDeclaredFields();
		for(int i = 0; i<f.length; i++){
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
	private void setXmlValue(Object t, String name, String value){
		try {
			Field[] f = t.getClass().getDeclaredFields();
			for(int i = 0; i < f.length; i++){
				if(f[i].getName().equalsIgnoreCase(name)){
					f[i].setAccessible(true);
					//获得属性类型
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
}
