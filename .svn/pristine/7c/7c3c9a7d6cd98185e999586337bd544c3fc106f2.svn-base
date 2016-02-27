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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.common.Display.ColTempModule;
import com.szaoto.ak10.common.Display.ColTempModules;
import com.szaoto.ak10.common.Display.ColourRGB;

/*
 * 类名DataAccessColourTem
 * 作者 liangdb
 * 主要功能 LED显示屏色温值存取
 * 创建日期2014年1月17日
 * 修改者，修改日期，修改内容
 */
public class DataAccessColourTem extends DataAccessBase {

	/**
	 * @param context
	 */
	public DataAccessColourTem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private final static String sFileFlagString = "ColourTem.xml";
	/*保存LED显示屏色温值*/
	@SuppressWarnings("null")
	public static ColourRGB GetDisplayColourTem(int DisplayID,int TemColNum)
	{
		ColourRGB colourRGB = null;
		try {
			   String filePath = null;
			   filePath = HomePageActivity.CONFIG_PATH + sFileFlagString;
			   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			   DocumentBuilder db = dbf.newDocumentBuilder();
			   org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));	  
			   NodeList list = doc.getElementsByTagName("listmember1");
			   for(int i =0 ;i<list.getLength();i++)
				  {
					 Element ele = (Element) list.item(i);
					 String tempstr = ele.getAttribute("ID");
					 if(tempstr.equals(Integer.toString(DisplayID)))
					 {
						 NodeList list1= ele.getElementsByTagName("ListColTemp");
						 for(int i1 =0 ;i1<list1.getLength();i1++)
						 {
							 Element ele1 = (Element) list1.item(i1);
							 NodeList list11= ele.getElementsByTagName("listmember2");
							 for(int i11 =0 ;i11<list11.getLength();i11++)
							 {
								 Element ele11 = (Element) list11.item(i11);
								 if(ele11.getAttribute("id").equals(Integer.toString(TemColNum)))
								 {
									 colourRGB.setId(Integer.valueOf(ele11.getAttribute("id")));
									 colourRGB.setM_nColorTemperature(ele11.getAttribute( "m_nColorTemperature" ));
									 colourRGB.setM_bEnable(Boolean.valueOf(ele11.getAttribute("m_bEnable")));
									 colourRGB.setnRed(Integer.valueOf(ele11.getAttribute("nRed")));
									 colourRGB.setnGreen(Integer.valueOf(ele11.getAttribute("nGreen")));
									 colourRGB.setnBlue(Integer.valueOf(ele11.getAttribute("nBlue")));
									 colourRGB.setnICRed(Integer.valueOf(ele11.getAttribute("nICRed")));
									 colourRGB.setnICGreen(Integer.valueOf(ele11.getAttribute("nICGreen")));
									 colourRGB.setnICBlue(Integer.valueOf(ele11.getAttribute("nICBlue")));
									 colourRGB.setnRedLow(Integer.valueOf(ele11.getAttribute("nRedLow")));
									 colourRGB.setnGreenLow(Integer.valueOf(ele11.getAttribute("nGreenLow")));
									 colourRGB.setnBlueLow(Integer.valueOf(ele11.getAttribute("nBlueLow")));
									 colourRGB.setnICRedLow(Integer.valueOf(ele11.getAttribute("nICRedLow")));
									 colourRGB.setnICGreenLow(Integer.valueOf(ele11.getAttribute("nICGreenLow")));
									 colourRGB.setnICBlueLow(Integer.valueOf(ele11.getAttribute("nICBlueLow")));
								 }
								 break;
							 }
						 }
						 break;
					 }
				  }
			   saveXML(doc,filePath);
		    } catch (Exception ex) {
			   ex.printStackTrace();
		    }
		return colourRGB;
	}
	
	public static boolean ModifyDisplayColourTem(int DisplayID, ColourRGB colourRGB)
	{
		try {
			   String filePath = null;
			   filePath = HomePageActivity.CONFIG_PATH + sFileFlagString;
			   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			   DocumentBuilder db = dbf.newDocumentBuilder();
			   org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));	  
			   NodeList list = doc.getElementsByTagName("listmember1");
			   for(int i =0 ;i<list.getLength();i++)
				  {
					 Element ele = (Element) list.item(i);
					 String tempstr = ele.getAttribute("DisplayID");
					 if(tempstr.equals(Integer.toString(DisplayID)))
					 {
						 NodeList list1= ele.getElementsByTagName("ListColTemp");
						 for(int i1 =0 ;i1<list1.getLength();i1++)
						 {
							 Element ele1 = (Element) list1.item(i1);
							 NodeList list11= ele1.getElementsByTagName("listmember2");
							 int k = list11.getLength();
							 for(int i11 =0 ;i11<list11.getLength();i11++)
							 {
								 Element ele11 = (Element) list11.item(i11);
								 if(ele11.getAttribute("id").equals(Integer.toString(colourRGB.getId())))
								 {
									 ele11.setAttribute( "id", Integer.toString(colourRGB.getId()));
									 ele11.setAttribute( "m_nColorTemperature", colourRGB.getM_nColorTemperature());
									 ele11.setAttribute( "m_bEnable", Boolean.toString(colourRGB.isM_bEnable()));
									 ele11.setAttribute( "nRed", Integer.toString(colourRGB.getnRed()));
									 ele11.setAttribute( "nGreen", Integer.toString(colourRGB.getnGreen()));
									 ele11.setAttribute( "nBlue", Integer.toString(colourRGB.getnBlue()));
									 ele11.setAttribute( "nICRed", Integer.toString(colourRGB.getnICRed()));
									 ele11.setAttribute( "nICGreen", Integer.toString(colourRGB.getnICGreen()));
									 ele11.setAttribute( "nICBlue", Integer.toString(colourRGB.getnICBlue()));
									 ele11.setAttribute( "nRedLow", Integer.toString(colourRGB.getnRedLow()));
									 ele11.setAttribute( "nGreenLow", Integer.toString(colourRGB.getnGreenLow()));
									 ele11.setAttribute( "nBlueLow", Integer.toString(colourRGB.getnBlueLow()));
									 ele11.setAttribute( "nICRedLow", Integer.toString(colourRGB.getnICRedLow()));
									 ele11.setAttribute( "nICGreenLow", Integer.toString(colourRGB.getnICGreenLow()));
									 ele11.setAttribute( "nICBlueLow", Integer.toString(colourRGB.getnICRedLow()));	
									 break;
								 }
								 
							 }
						 }
						 break;
					 }
				  }
			   saveXML(doc,filePath);
		    } catch (Exception ex) {
			   ex.printStackTrace();
		    }
		return false;	
	}
	public static boolean SaveDisplayColourTem(int nDisplayID, ColourRGB[] colourtemflag) throws IllegalArgumentException, IllegalStateException, IOException {		
		FileOutputStream os = new FileOutputStream(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));
		OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");  
		BufferedWriter bufWriter = new BufferedWriter(writer);  

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(bufWriter);
		serializer.startDocument("UTF-8", true);
		
		serializer.startTag(null, "display");
		serializer.attribute(null, "id", "1");

		for (int i = 0; i < colourtemflag.length; i++) {
			serializer.startTag(null, "template");		
			serializer.attribute(null, "id", String.valueOf(colourtemflag[i].getId()));
			serializer.attribute(null, "m_nColorTemperature", colourtemflag[i].getM_nColorTemperature());
			serializer.attribute(null, "m_bEnable", (colourtemflag[i].isM_bEnable() ? "1" : "0"));
			serializer.attribute(null, "nRed", String.valueOf(colourtemflag[i].getnRed()));
			serializer.attribute(null, "nGreen", String.valueOf(colourtemflag[i].getnGreen()));
			serializer.attribute(null, "nBlue", String.valueOf(colourtemflag[i].getnBlue()));
			serializer.attribute(null, "nICRed", String.valueOf(colourtemflag[i].getnICRed()));
			serializer.attribute(null, "nICGreen", String.valueOf(colourtemflag[i].getnICGreen()));
			serializer.attribute(null, "nICBlue", String.valueOf(colourtemflag[i].getnICBlue()));
			serializer.attribute(null, "nRedLow", String.valueOf(colourtemflag[i].getnRedLow()));
			serializer.attribute(null, "nGreenLow", String.valueOf(colourtemflag[i].getnGreenLow()));
			serializer.attribute(null, "nBlueLow", String.valueOf(colourtemflag[i].getnBlueLow()));
			serializer.attribute(null, "nICRedLow", String.valueOf(colourtemflag[i].getnICRedLow()));
			serializer.attribute(null, "nICGreenLow", String.valueOf(colourtemflag[i].getnICGreenLow()));
			serializer.attribute(null, "nICBlueLow", String.valueOf(colourtemflag[i].getnICBlueLow()));

			serializer.endTag(null, "template");
		}
		serializer.endTag(null, "display");
		
		serializer.endDocument();
		writer.flush();
		writer.close();
		
		return true;
	}
	public static boolean SaveDisplayColourTemFromClass(ColTempModules colTempModules) throws IllegalArgumentException, IllegalStateException, IOException {		
		String classnameString = colTempModules.getClass().getName();
		FileOutputStream os = new FileOutputStream(new File(HomePageActivity.CONFIG_PATH,sFileFlagString));
		OutputStreamWriter writer = new OutputStreamWriter(os,"UTF-8");  
		BufferedWriter bufWriter = new BufferedWriter(writer);  

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(bufWriter);
		serializer.startDocument("UTF-8", true);
		
		serializer.startTag(null, "XMLFile");
		serializer.attribute(null, "class", classnameString);
		
		serializer.startTag(null, "default");
		List<ColTempModule> listColTempModule = colTempModules.getListColTempModule();
		serializer.startTag(null, "ListColTempModule");
		for(ColTempModule ls: listColTempModule)
		{
			serializer.startTag(null, "listmember1");
			serializer.attribute(null, "DisplayID", Integer.toString(ls.getDisplayID()));
			serializer.startTag(null, "ListColTemp");
			List<ColourRGB> listColRGB = ls.getListColTemp();
			for(ColourRGB lc: listColRGB)
			{
				serializer.startTag(null, "listmember2");

				serializer.attribute(null, "id", Integer.toString(lc.getId()));
				serializer.attribute(null, "m_nColorTemperature", lc.getM_nColorTemperature());
				serializer.attribute(null, "m_bEnable", Boolean.toString(lc.isM_bEnable()));
				serializer.attribute(null, "nRed", Integer.toString(lc.getnRed()));
				serializer.attribute(null, "nGreen", Integer.toString(lc.getnGreen()));
				serializer.attribute(null, "nBlue", Integer.toString(lc.getnBlue()));
				serializer.attribute(null, "nICRed", Integer.toString(lc.getnICRed()));
				serializer.attribute(null, "nICGreen", Integer.toString(lc.getnICGreen()));
				serializer.attribute(null, "nICBlue", Integer.toString(lc.getnICBlue()));
				serializer.attribute(null, "nRedLow", Integer.toString(lc.getnRedLow()));
				serializer.attribute(null, "nGreenLow", Integer.toString(lc.getnGreenLow()));
				serializer.attribute(null, "nBlueLow", Integer.toString(lc.getnBlueLow()));
				serializer.attribute(null, "nICRedLow", Integer.toString(lc.getnICRedLow()));
				serializer.attribute(null, "nICGreenLow", Integer.toString(lc.getnICGreenLow()));
				serializer.attribute(null, "nICBlueLow", Integer.toString(lc.getnICRedLow()));
				
				serializer.endTag(null, "listmember2");
			}
			serializer.endTag(null, "ListColTemp");
			serializer.endTag(null, "listmember1");
		}
		serializer.endTag(null, "ListColTempModule");
		serializer.endTag(null, "default");
		serializer.endTag(null, "XMLFile");
		serializer.endDocument();
		writer.flush();
		writer.close();
		
		return true;
	}

	/*读取LED显示屏色温值*/
	public static boolean LoadDisplayColourTem(int nDisplayID, ColourRGB[] colourtemflag) {
		
		File file = new File(HomePageActivity.CONFIG_PATH + sFileFlagString);
		// if (!file.exists()){
	//	E:\work\AL131005\04Development\Software\ak10
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				// is = context.getResources().getAssets().open(sFileFlagString);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			XmlParse xmlParse = new XmlParse();
			
			@SuppressWarnings("unchecked")
			List<ColourRGB> colourRGBs = (List<ColourRGB>) xmlParse.getXmlList(is, ColourRGB.class, "template");
			int nCnt = 0;
			for (ColourRGB colourRGB : colourRGBs){
				colourtemflag[nCnt] = colourRGB;
				nCnt ++;
			}
		return true;
		
	}
	public static ColTempModules LoadColTempModules(){
		ReadXmlToClass readXml = new ReadXmlToClass(sFileFlagString);
		ColTempModules coltempmodules = new ColTempModules();
		readXml.Assignment(coltempmodules);
		return coltempmodules;
	}
	private static void saveXML(org.w3c.dom.Document document, String filePath) {

		try {

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		}

	}
}
