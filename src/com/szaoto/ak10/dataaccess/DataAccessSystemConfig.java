/*
   * 文件名 DataAccessColourTem.java
   * 包含类名列表com.szaoto.ak10.dataaccess
   * 版本信息，版本号
   * 创建日期2014年1月17日上午10:50:54
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.dataaccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import org.xmlpull.v1.XmlSerializer;

import com.szaoto.ak10.HomePageActivity;

import android.content.Context;
import android.util.Xml;

import com.szaoto.ak10.common.SystemConfig;

/*
 * 类名DisplayStatus
 * 作者 liangdb
 * 主要功能 
 * 创建日期2014年1月17日
 * 修改者，修改日期，修改内容
 */
public class DataAccessSystemConfig extends DataAccessBase{

	/**
	 * @param context
	 */
	public DataAccessSystemConfig(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private final static String sFileFlagString = "systemconfig.xml";

	public static boolean SaveSystemScreenTime(long time)
	{
		return true;
	}
	public static boolean SaveSystemPassword(String password)
	{
		return true;
	}
	public static boolean SystemLisencetype(int lisencetype)
	{
		return true;
	}
	public static boolean SystemLisencestate(int lisencestate)
	{
		return true;
	}
	
	public static boolean SaveSystemConfig(SystemConfig systemConfig) throws IllegalArgumentException, IllegalStateException, IOException {
		
		FileOutputStream os = new FileOutputStream(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));
	//	OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");  
		OutputStreamWriter writer = new OutputStreamWriter(os,"ASCII");  
		BufferedWriter bufWriter = new BufferedWriter(writer);  

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(bufWriter);
	//	serializer.startDocument("UTF-8", true);
		serializer.startDocument("ASCII", true);
		serializer.startTag(null, "XMLFile");
		
		//for (SystemCard systemCard : systemCards) {
			
			serializer.startTag(null, "SystemConfig");
			
		//	serializer.attribute(null, "sMACAddress", UtilFun.bytes2HexString(systemConfig.getUcMACAddress(), 6, "-"));
		//	serializer.attribute(null, "nType", "1");
		//	serializer.attribute(null, "nSlotID", String.valueOf(systemConfig.getnSlotID()));
			
			serializer.startTag(null, "UISettingTime");
			serializer.text(String.valueOf(systemConfig.getUISettingTime()));
			serializer.endTag(null, "UISettingTime");
			
			serializer.startTag(null, "SystemPassword");
			serializer.text(String.valueOf(systemConfig.getSystemPassword()));
			serializer.endTag(null, "SystemPassword");
			
			serializer.startTag(null, "SystemLisencetype");
			serializer.text(String.valueOf(systemConfig.getSystemLisencetype()));
			serializer.endTag(null, "SystemLisencetype");
			
			serializer.startTag(null, "DiagnoseConfig");
			serializer.text(String.valueOf(systemConfig.getDiagnoseConfig()));
			serializer.endTag(null, "DiagnoseConfig");
			
			
			
			serializer.endTag(null, "SystemConfig");
			
	//	}
		
		serializer.endTag(null, "XMLFile");
		
		serializer.endDocument();
		writer.flush();
		writer.close();
		
		return true;
	}

	public static SystemConfig LoadSystemConfig(){
		
		InputStream is = null;
		try {
			is = new FileInputStream(HomePageActivity.CONFIG_PATH + sFileFlagString);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		XmlParse xmlParse = new XmlParse();

		@SuppressWarnings("unchecked")
	//	List<SystemCard> systemCards = (List<SystemCard>) xmlParse.getXmlList(is, SystemConfig.class,"card");
		SystemConfig syscofigConfig = (SystemConfig)xmlParse.getXmlObject(is, SystemConfig.class);
		//	(SystemConfig)xmlParse.getXmlObject(is,SystemConfig.class);
		return syscofigConfig;
		
	}

}
