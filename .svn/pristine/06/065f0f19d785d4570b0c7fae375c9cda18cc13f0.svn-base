/*
   * 文件名 DataAccessGammaTable.java
   * 包含类名列表com.szaoto.ak10.dataaccess
   * 版本信息，版本号
   * 创建日期2014年4月4日下午4:48:52
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
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.common.GammaData;
import com.szaoto.ak10.util.UtilFun;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

/*
 * 类名DataAccessGammaTable
 * 作者 liangdb
 * 主要功能 GAMMA读取、存储函数
 * 创建日期2014年4月4日
 * 修改者，修改日期，修改内容
 */
public class DataAccessGammaTable extends DataAccessBase {

	/**
	 * @param context
	 */
	public DataAccessGammaTable(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
private final static String sFileFlagString = "GammaTable.xml";
	
	/*保存LED显示屏GAMMA值*/
	public static boolean SaveDisplayGammaTable(int nDisplayID, GammaData gammadata) throws IllegalArgumentException, IllegalStateException, IOException {		
		FileOutputStream os = new FileOutputStream(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));
		OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");  
		BufferedWriter bufWriter = new BufferedWriter(writer);  

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(bufWriter);
		serializer.startDocument("UTF-8", true);
		
		serializer.startTag(null, "display");
		serializer.attribute(null, "id", "1");
		serializer.attribute(null, "nVideowid", String.valueOf(gammadata.getnVideowid()));
		serializer.attribute(null, "bSendThreeColor", gammadata.isbSendThreeColor() ? "1" : "0");
		serializer.attribute(null, "nGrayLevel", String.valueOf(gammadata.getnGrayLevel()));
		serializer.attribute(null, "r", String.valueOf(gammadata.getfGamma()[0]));
		serializer.attribute(null, "g", String.valueOf(gammadata.getfGamma()[1]));
		serializer.attribute(null, "b", String.valueOf(gammadata.getfGamma()[2]));
		serializer.attribute(null, "rgb", String.valueOf(gammadata.getfGammaRGB()));
		
		String sDataString = "";
		
		switch (gammadata.getnVideowid()) {
			case 10:
			case 12:
				serializer.startTag(null, "rgb");
					serializer.startTag(null, "value");
					
					for (int i = 0; i < 4096; i++) {
						if (i != 4095) {
							sDataString += String.valueOf(gammadata.getsGammaTableRGB()[i]) + ",";
						}
						else {
							sDataString += String.valueOf(gammadata.getsGammaTableRGB()[i]);
						}
					}
					
					serializer.text(sDataString);
					serializer.endTag(null, "value");
				serializer.endTag(null, "rgb");
				break;
			case 8:
			default:
				String sTag = "";
				for (int i = 0; i < 3 ; i ++)
				{
					switch (i)
					{
					case 0:
						sTag = "red";
						break;
					case 1:
						sTag = "green";
						break;
					case 2:
						sTag = "blue";
						break;
					}
					serializer.startTag(null, sTag);
						serializer.startTag(null, "value");
						for (int j = 0; j < 256 ; j ++)
						{
							if (j != 255) {
								sDataString += String.valueOf(gammadata.getsGammaTable()[i][j]) + ",";
							}
							else {
								sDataString += String.valueOf(gammadata.getsGammaTable()[i][j]);
							}
						}
						serializer.text(sDataString);
						serializer.endTag(null, "value");
					serializer.endTag(null, sTag);
				}
				break;
			}			
			

		serializer.endTag(null, "display");
		
		serializer.endDocument();
		writer.flush();
		writer.close();
		
		return true;
	}

	/*读取LED显示屏GAMMA值*/
	public static  GammaData LoadDisplayGammaTable(int nDisplayID) {
		
		//先不分屏测
		nDisplayID = 1;
		
		File file = new File(HomePageActivity.CONFIG_PATH + sFileFlagString);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GammaData gammaData = new GammaData();
		String sCurNodeString = "";
		String sGamma = "";
		short sGammaTable[][] = new short[3][256];
		short sGammaTableRGB[] = new short[4096];
		boolean isGammaFinded = false;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			//事件类型
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						//获得当前节点元素的名称
						String name = parser.getName();
						if (name.equals("display")) {
							if (nDisplayID == Integer.parseInt(parser.getAttributeValue(0)))
							{
								isGammaFinded = true;
								gammaData.setnVideowid(Short.parseShort(parser.getAttributeValue(1)));
								gammaData.setbSendThreeColor(Boolean.parseBoolean(parser.getAttributeValue(2)));
								gammaData.setnGrayLevel(Short.parseShort(parser.getAttributeValue(3)));
								
	    						float[] fGamma = new float[3];
								fGamma[0] = Float.parseFloat(parser.getAttributeValue(4));
								fGamma[1] = Float.parseFloat(parser.getAttributeValue(5));
								fGamma[2] = Float.parseFloat(parser.getAttributeValue(6));
								
								gammaData.setfGamma(fGamma);
								gammaData.setfGammaRGB(Float.parseFloat(parser.getAttributeValue(7)));
							}else {
								isGammaFinded = false;
							}
							
							
						}
						else if (name.equals("red") && isGammaFinded) {
							sCurNodeString = "red";
						}
						else if (name.equals("green") && isGammaFinded) {
							sCurNodeString = "green";
						}
						else if (name.equals("blue") && isGammaFinded) {
							sCurNodeString = "blue";
						}
						else if (name.equals("rgb") && isGammaFinded) {
							sCurNodeString = "rgb";
						}
						else if (name.equals("value") && isGammaFinded) {
							if (sCurNodeString.equals("red")) {
								sGamma = parser.nextText();
								sGammaTable[0] = UtilFun.ParseGamma(sGamma, 256);
							}
							else if (sCurNodeString.equals("green")) {
								sGamma = parser.nextText();
								sGammaTable[1] = UtilFun.ParseGamma(sGamma, 256);
							}
							else if (sCurNodeString.equals("blue")) {
								sGamma = parser.nextText();
								sGammaTable[2] = UtilFun.ParseGamma(sGamma, 256);
							}
							else if (sCurNodeString.equals("rgb")) {
								sGamma = parser.nextText();
								sGammaTableRGB = UtilFun.ParseGamma(sGamma, 4096);
							}
						}
						break;
					case XmlPullParser.END_TAG:
						String nameend = parser.getName();
						break;
				}
 				eventType = parser.next();
			}
			gammaData.setsGammaTable(sGammaTable);
			gammaData.setsGammaTableRGB(sGammaTableRGB);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("xml pull error", e.toString());
		}

		return gammaData;
	}

}
