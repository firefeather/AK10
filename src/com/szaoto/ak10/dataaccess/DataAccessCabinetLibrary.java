/*
 * 文件名 DataAccessColourTem.java
 * 包含类名列表com.szaoto.ak10.dataaccess
 * 版本信息，版本号
 * 创建日期2014年1月17日上午10:50:54
 * 版权声明 liangdb-szaoto
 */
package com.szaoto.ak10.dataaccess;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.common.CabinetData.CHIP_TYPE;
import com.szaoto.ak10.common.CabinetData.CStructSingleScanCard;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.common.CabinetData.INLINEMODE;
import com.szaoto.ak10.common.CabinetData.LinkTable;
import com.szaoto.ak10.common.CabinetData.MonitorItem;
import com.szaoto.ak10.common.CabinetData.ScanCardAttachment;
import com.szaoto.ak10.common.Display.Display;
import com.szaoto.ak10.entity.CabinetSeries;
import com.szaoto.ak10.util.TraverseDictionary;
import com.szaoto.ak10.util.UtilFun;

/*
 * 类名DataAccessCabinetLibrary
 * 作者 zhangjj
 * 主要功能 
 * 创建日期2014年9月25日
 * 修改者，修改日期，修改内容
 */
public class DataAccessCabinetLibrary extends DataAccessBase {
	static String fileUsbpath;// 核心板旧版调试路径
	static String filepath = Ak10Application.g_strConfPathString;
	private static String sFileCabinetString = "Cabinet.cbt";
	private static String sFileCabinetsString = "CabinetSeries.cbs";
	//private static CHIP_TYPE chiptype;
	private static String sUSBFileCabinet = ".cbt";
	//private static String sUSBFileCabinetSeries = ".cbs";
	private static String sUSBFileCabinetName = ".cbt";
	private static String sUSBFileCabinetSeriesName = ".cbs";

	/**
	 * @param context
	 */
	public DataAccessCabinetLibrary(Context context) {
		super(context);

	}

	public static List<String> getCabinetSeriseID(int Filetype)
	// Filetype =0 表示本地，Filetype=1表示U盘 //得到箱体系列名称
	{// ///////zhangjj
		List<String> listnameList = new ArrayList<String>();
		// String filepath = null;
		if (Filetype == 0) {

			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,
						sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("ID")) {
							listnameList.add(childNode.getTextContent().toString());
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (Filetype == 1) {
			fileUsbpath = TraverseDictionary.GetUDiskDir();
			if (fileUsbpath == null) {
				return listnameList;
			}
			fileUsbpath = GetUsbCabintSeriesFileName();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetSeriesName));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("ID")) {
							listnameList.add(childNode.getTextContent()
									.toString());
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return listnameList;

	}

	// 获取U盘箱体系列名称
	public static String GetUsbCabintSeriesFileName() {

		TraverseDictionary TD = new TraverseDictionary();
		TD.GetFiles(fileUsbpath, ".cbt", true);
		List<String> listNamecbtSeries = TD.getLstFile();
		for (int i = 0; i < listNamecbtSeries.size(); i++) {
			sUSBFileCabinetSeriesName = listNamecbtSeries.get(i);
			// 此处箱体系列名称只取了一个，保证U盘只有一个 文件，若要同步多个,getCabinetSerisName要放在这里
		}
		return fileUsbpath;
	}

	public static List<String> getCabinetSeriseNames(int Filetype)
	// Filetype =0 表示本地，Filetype=1表示U盘 //得到箱体系列名称
	{
		List<String> listnameList = new ArrayList<String>();

		if (Filetype == 0) {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("name")) {
							listnameList.add(childNode.getTextContent().toString());
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} else if (Filetype == 1) {
			fileUsbpath = TraverseDictionary.GetUDiskDir();
			if (fileUsbpath == null) {
				return null;
			}
			fileUsbpath = GetUsbCabintSeriesFileName();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,sUSBFileCabinetSeriesName));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("name")) {
							listnameList.add(childNode.getTextContent().toString());
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return listnameList;

	}

	public static List<String> getCabinetNamesbyseriseID(int SeriseID,int Filetype)
	// Filetype =0 表示本地，Filetype=1表示U盘 //得到箱体系列名称
	{// zhangjj
		List<String> listnameList = new ArrayList<String>();
		// String filepath = null;
		if (Filetype == 0) {
			// filepath = HomePageActivity.CONFIG_PATH ;
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,sFileCabinetString));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("SeriesID")) {
							// SeriseID =
							int seriseid = Integer.valueOf(childNode.getTextContent());
							if (seriseid == SeriseID) {
								for (int j1 = 0; j1 < list1.getLength(); j1++) {
									childNode = element.getChildNodes().item(j1);
									if (childNode.getNodeName().equals("Name")) {
										listnameList.add(childNode.getTextContent().toString());
									}
								}
							}
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (Filetype == 1) {
			fileUsbpath = TraverseDictionary.GetUDiskDir();
			if (fileUsbpath == null) {
				return null;
			}
			fileUsbpath = GetUsbCabinetFile();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetName));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("SeriesID")) {
							int seriseid = Integer.valueOf(childNode.getTextContent());
							if (seriseid == SeriseID) {
								for (int j1 = 0; j1 < list1.getLength(); j1++) {
									childNode = element.getChildNodes().item(j1);
									if (childNode.getNodeName().equals("Name")) {
										listnameList.add(childNode.getTextContent().toString());
									}
								}
							}
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return listnameList;
	}

	public static List<String> getCabinetNames(int Filetype)
	// Filetype =0 表示本地，Filetype=1表示U盘 //得到箱体系列名称
	{
		List<String> listnameList = new ArrayList<String>();
		// String filepath = null;
		if (Filetype == 0) {
			// filepath = HomePageActivity.CONFIG_PATH ;
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,sFileCabinetString));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("Name")) {
							listnameList.add(childNode.getTextContent().toString());
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} else if (Filetype == 1) {
			fileUsbpath = TraverseDictionary.GetUDiskDir();
			if (fileUsbpath == null) {
				return null;
			}
			fileUsbpath = GetUsbCabinetFile();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetName));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("Name")) {
							listnameList.add(childNode.getTextContent().toString());
						}
					}
				}
				// saveXML(doc,filepath +sFileCabinetsString);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listnameList;

	}

	// 获取U盘下的.cbt结尾的箱体文件
	public static String GetUsbCabinetFile() {
		/*
		 * String filepath; filepath = "/mnt/usb/" ;//新版路径,老版/mnt/usb/
		 */
		TraverseDictionary TD = new TraverseDictionary();
		fileUsbpath = TraverseDictionary.GetUDiskDir();
		if (fileUsbpath == null) {
			return null;
		}
		TD.GetFiles(fileUsbpath, sUSBFileCabinet, true);
		List<String> listNamecbt = TD.getLstFile();
		for (int i = 0; i < listNamecbt.size(); i++) {
			sUSBFileCabinetName = listNamecbt.get(i);
		}
		return fileUsbpath;
	}

	public static  HashMap<String, ArrayList<String>> loadModelMapsData()
	{
		
		HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(filepath,
					sFileCabinetString));
			NodeList list = doc.getElementsByTagName("Cabinet");
			for (int i = 0; i < list.getLength(); i++) {
		
				Element element = (Element) list.item(i);
				NodeList list1 = element.getChildNodes();
				Node childNode = null;
				String strSeriesId = null;
				String strSeriesName =null;
				for (int j = 0; j < list1.getLength(); j++) 
				{
					childNode = element.getChildNodes().item(j);
					
				
					if (childNode.getNodeName().equals("SeriesID")) {
						strSeriesId = childNode.getTextContent().toString();
					
						strSeriesName=DataAccessCabinetLibrary.getCbsNameBySeriesId(strSeriesId);

						if (strSeriesName==null) {
							strSeriesName = "Series-"+strSeriesId;
						}
						
						if (hashMap.get(strSeriesName) == null) {
						
							ArrayList<String> arrModel = new ArrayList<String>();			
							hashMap.put(strSeriesName, arrModel);	
						}
						
					}
					
					if (childNode.getNodeName().equals("Name")) {
						if (strSeriesName!=null) {
							ArrayList<String> arrModel = hashMap.get(strSeriesName);
							arrModel.add(childNode.getTextContent().toString());
						}	
						
						break;
					}			
					
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		

	    getCbtSerieStrings(hashMap);     
		     
		return hashMap;
	}
	
   public static ArrayList<String> getCbtSerieStrings(HashMap<String, ArrayList<String>> hashMap){   
		Ak10Application.gArrCabSerieStrings.clear();
	    Iterator<String> iter = hashMap.keySet().iterator();
		     while (iter.hasNext()) {
			   String strKey = (String) iter.next();	   
			   //从cbs中找箱体系列，没有的添加进去   
			 Ak10Application.gArrCabSerieStrings.add(strKey);
		}
	   
	   return Ak10Application.gArrCabSerieStrings;
   }
	
	
	
	public static boolean getCabinetSeriseByname(String cabinetserisename,
			CabinetSeries cabinetSeries, int Filetype)
	// Filetype =0 表示本地，Filetype=1表示USB
	{
		try {
			// String filepath = null;
			if (Filetype == 0) {
				// filepath = HomePageActivity.CONFIG_PATH ;
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().trim().equals("name")) {
							cabinetSeries.setName(childNode.getTextContent()
									.toString());
							if (cabinetserisename.equals(childNode
									.getTextContent().toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);
							/**
							 * 此处需要解析的箱体文件是：Cabinet.cbt和CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cabinetSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("子节点ID解析");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cabinetSeries.setParentID(Integer
										.parseInt(childNode.getTextContent()
												.toString()));
								System.out.println("父节点开始解析");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cabinetSeries.setName(childNode
										.getTextContent().toString());
								System.out.println("箱体系列名称解析");
							}
						}
						break;
					}
				}
			} else if (Filetype == 1) {
				fileUsbpath = TraverseDictionary.GetUDiskDir();
				if (fileUsbpath == null) {
					return (Boolean) null;
				}
				fileUsbpath = GetUsbCabintSeriesFileName();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetSeriesName));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().trim().equals("name")) {
							cabinetSeries.setName(childNode.getTextContent()
									.toString());
							if (cabinetserisename.equals(childNode
									.getTextContent().toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);
							/**
							 * 此处需要解析的箱体文件是：Cabinet.cbt和CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cabinetSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("子节点ID解析");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cabinetSeries.setParentID(Integer
										.parseInt(childNode.getTextContent()
												.toString()));
								System.out.println("父节点开始解析");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cabinetSeries.setName(childNode
										.getTextContent().toString());
								System.out.println("箱体系列名称解析");
							}
						}
						break;
					}
				}
			}

			// saveXML(doc,filepath +sFileCabinetsString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static boolean getCabinetByname(String cabinetname,
			CabinetInformation cabinet, int Filetype) {//
		try {
			// String filepath = null;
			if (Filetype == 0) {
				// filepath = HomePageActivity.CONFIG_PATH ;
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,
						sFileCabinetString));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					// Element element = (Element)list.item(i);
					Node element = list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						String strtempString = childNode.getNodeName();
						if (childNode.getNodeName().trim().equals("Name")) {
							// cabinetSeries.setName(childNode.getTextContent().toString());
							if (cabinetname.equals(childNode.getTextContent()
									.toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);

							if (childNode.getNodeName().trim().equals("ID")) {
								cabinet.setnID(Integer.parseInt(childNode
										.getTextContent().toString()));
							}
							if (childNode.getNodeName().trim()
									.equals("SeriesID")) {
								cabinet.setnSeriesID(Integer.parseInt(childNode
										.getTextContent().toString()));
							}
							if (childNode.getNodeName().trim().equals("Name")) {
								cabinet.setsName(childNode.getTextContent()
										.toString());
							}
							if (childNode.getNodeName().trim().equals("Photo")) {
								cabinet.setsPhoto(childNode.getTextContent()
										.toString());
							}
							if (childNode.getNodeName().trim()
									.equals("SCACount")) {
								cabinet.setnScanCardCount(Short
										.parseShort(childNode.getTextContent()
												.toString()));
							}
							if (childNode.getNodeName().trim()
									.equals("inlinemode")) {
								String string = childNode.getTextContent()
										.toString();
								INLINEMODE[] inlinemode = INLINEMODE.values();
								/*
								 * 1. enum<->int enum -> int: int i =
								 * enumType.value.ordinal(); int -> enum:
								 * enumType b= enumType.values()[i]; 2.
								 * enum<->String enum -> String: enumType.name()
								 * String -> enum: enumType.valueOf(name);
								 */
								int num = Integer.parseInt(childNode
										.getTextContent().toString());
								cabinet.setInlineMode(INLINEMODE.values()[num]);
							}
							if (childNode.getNodeName().trim()
									.equals("ScanCardParaSynchro")) {
								if (Integer.parseInt(childNode.getTextContent()
										.toString()) == 1) {
									cabinet.setbScanCardParaSynchro(true);
								} else {
									cabinet.setbScanCardParaSynchro(false);
								}
							}
							if (childNode.getNodeName().trim()
									.equals("MonitorParaSynchro")) {
								if (Integer.parseInt(childNode.getTextContent()
										.toString()) == 1) {
									cabinet.setbMonitorParaSynchro(true);
								} else {
									cabinet.setbMonitorParaSynchro(false);
								}
							}
							if (childNode.getNodeName().trim()
									.equals("ScanCardAttachments")) {
								List<ScanCardAttachment> listScancardAttachment = new ArrayList<ScanCardAttachment>();
								getScanCardAttachments(childNode,
										listScancardAttachment);
								cabinet.setListScancardAttachment(listScancardAttachment);
							}
							if (childNode.getNodeName().trim()
									.equals("LoadedRegion")) {
								// hh//////////////////////
								try {
									RECT rtRect = new RECT(i1, i1, i1, i1);
									getLoadedRegion((Element) childNode, rtRect);
									cabinet.setRtRect(rtRect);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}

						}
						break;
					}
				}

			} else if (Filetype == 1) {
				fileUsbpath = TraverseDictionary.GetUDiskDir();
				if (fileUsbpath == null) {
					return true;
				}
				fileUsbpath = GetUsbCabinetFile(); // 得到U盘.cbt后缀名的箱体文件名称
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetName));
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					// Element element = (Element)list.item(i);
					Node element = list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						String strtempString = childNode.getNodeName();
						if (childNode.getNodeName().trim().equals("Name")) {
							// cabinetSeries.setName(childNode.getTextContent().toString());
							if (cabinetname.equals(childNode.getTextContent()
									.toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);

							if (childNode.getNodeName().trim().equals("ID")) {
								cabinet.setnID(Integer.parseInt(childNode
										.getTextContent().toString()));
							}
							if (childNode.getNodeName().trim()
									.equals("SeriesID")) {
								cabinet.setnSeriesID(Integer.parseInt(childNode
										.getTextContent().toString()));
							}
							if (childNode.getNodeName().trim().equals("Name")) {
								cabinet.setsName(childNode.getTextContent()
										.toString());
							}
							if (childNode.getNodeName().trim().equals("Photo")) {
								cabinet.setsPhoto(childNode.getTextContent()
										.toString());
							}
							if (childNode.getNodeName().trim()
									.equals("SCACount")) {
								cabinet.setnScanCardCount(Short
										.parseShort(childNode.getTextContent()
												.toString()));
							}
							if (childNode.getNodeName().trim()
									.equals("inlinemode")) {
								String string = childNode.getTextContent()
										.toString();
								INLINEMODE[] inlinemode = INLINEMODE.values();
								/*
								 * 1. enum<->int enum -> int: int i =
								 * enumType.value.ordinal(); int -> enum:
								 * enumType b= enumType.values()[i]; 2.
								 * enum<->String enum -> String: enumType.name()
								 * String -> enum: enumType.valueOf(name);
								 */
								int num = Integer.parseInt(childNode
										.getTextContent().toString());
								cabinet.setInlineMode(INLINEMODE.values()[num]);
							}
							if (childNode.getNodeName().trim()
									.equals("ScanCardParaSynchro")) {
								if (Integer.parseInt(childNode.getTextContent()
										.toString()) == 1) {
									cabinet.setbScanCardParaSynchro(true);
								} else {
									cabinet.setbScanCardParaSynchro(false);
								}
							}
							if (childNode.getNodeName().trim()
									.equals("MonitorParaSynchro")) {
								if (Integer.parseInt(childNode.getTextContent()
										.toString()) == 1) {
									cabinet.setbMonitorParaSynchro(true);
								} else {
									cabinet.setbMonitorParaSynchro(false);
								}
							}
							// 获取扫描卡 附件 信息 在此处 获取 不到
							if (childNode.getNodeName().trim()
									.equals("ScanCardAttachments")) {
								List<ScanCardAttachment> listScancardAttachment = new ArrayList<ScanCardAttachment>();
								getScanCardAttachments(childNode,
										listScancardAttachment);
								cabinet.setListScancardAttachment(listScancardAttachment);
							}
							if (childNode.getNodeName().trim()
									.equals("LoadedRegion")) {
								// hh//////////////////////
								RECT rtRect = new RECT(i1, i1, i1, i1);
								getLoadedRegion((Element) childNode, rtRect);
								cabinet.setRtRect(rtRect);
							}

						}
						break;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static CabinetInformation getCabinetFromUDisk(String strFilePath,
			String CbtName) throws Exception {
		CabinetInformation cabinetinfo = new CabinetInformation();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.parse(new File(strFilePath));
		NodeList list = doc.getElementsByTagName("Cabinet");
		for (int i = 0; i < list.getLength(); i++) {
			int sign = 0;
			// Element element = (Element)list.item(i);
			Node element = list.item(i);
			// element.getn
			NodeList list1 = element.getChildNodes();
			Node childNode = null;
			for (int j = 0; j < list1.getLength(); j++) {
				childNode = element.getChildNodes().item(j);
				if (childNode.getNodeName().trim().equals("Name")) {
					// cabinetSeries.setName(childNode.getTextContent().toString());
					if (CbtName.equals(childNode.getTextContent().toString())) {
						sign = 1;
						break;
					}
				}
			}
			if (sign == 1) {
				for (int i1 = 0; i1 < element.getChildNodes().getLength(); i1++) {
					childNode = element.getChildNodes().item(i1);

					if (childNode.getNodeName().trim().equals("ID")) {
						cabinetinfo.setnID(Integer.parseInt(childNode
								.getTextContent().toString()));
					}
					if (childNode.getNodeName().trim().equals("SeriesID")) {
						cabinetinfo.setnSeriesID(Integer.parseInt(childNode
								.getTextContent().toString()));
					}
					if (childNode.getNodeName().trim().equals("Name")) {
						cabinetinfo.setsName(childNode.getTextContent()
								.toString());
					}
					if (childNode.getNodeName().trim().equals("Photo")) {
						cabinetinfo.setsPhoto(childNode.getTextContent()
								.toString());
					}
					if (childNode.getNodeName().trim().equals("SCACount")) {
						cabinetinfo.setnScanCardCount(Short
								.parseShort(childNode.getTextContent()
										.toString()));
					}
					if (childNode.getNodeName().trim().equals("inlinemode")) {
						/*
						 * 1. enum<->int enum -> int: int i =
						 * enumType.value.ordinal(); int -> enum: enumType b=
						 * enumType.values()[i]; 2. enum<->String enum ->
						 * String: enumType.name() String -> enum:
						 * enumType.valueOf(name);
						 */
						int num = Integer.parseInt(childNode.getTextContent()
								.toString());
						cabinetinfo.setInlineMode(INLINEMODE.values()[num]);
					}
					if (childNode.getNodeName().trim()
							.equals("ScanCardParaSynchro")) {
						if (Integer.parseInt(childNode.getTextContent()
								.toString()) == 1) {
							cabinetinfo.setbScanCardParaSynchro(true);
						} else {
							cabinetinfo.setbScanCardParaSynchro(false);
						}
					}
					if (childNode.getNodeName().trim()
							.equals("MonitorParaSynchro")) {
						if (Integer.parseInt(childNode.getTextContent()
								.toString()) == 1) {
							cabinetinfo.setbMonitorParaSynchro(true);
						} else {
							cabinetinfo.setbMonitorParaSynchro(false);
						}
					}
					// 获取扫描卡 附件 信息 在此处 获取 不到
					if (childNode.getNodeName().trim()
							.equals("ScanCardAttachments")) {
						List<ScanCardAttachment> listScancardAttachment = new ArrayList<ScanCardAttachment>();
						getScanCardAttachments(childNode,
								listScancardAttachment);
						cabinetinfo
								.setListScancardAttachment(listScancardAttachment);
					}
					if (childNode.getNodeName().trim().equals("LoadedRegion")) {
						// hh//////////////////////
						RECT rtRect = new RECT(i1, i1, i1, i1);
						getLoadedRegion((Element) childNode, rtRect);
						cabinetinfo.setRtRect(rtRect);
					}

				}
				break;
			}
		}
		return cabinetinfo;

	}

	public static boolean getScanCardAttachments(Node currentnode,
			List<ScanCardAttachment> listscattachment) {
		NodeList nodeList = currentnode.getChildNodes();
		Node childNode = null;
		for (int i = 0; i < currentnode.getChildNodes().getLength(); i++) {
			ScanCardAttachment scanCardAttachment = new ScanCardAttachment();
			childNode = currentnode.getChildNodes().item(i);

			/*
			 * String string = new String(); string =
			 * childNode.getNodeName().trim();
			 */
			if (childNode.getNodeName().trim().equals("ScanCardAttachment")) {
				Element element = (Element) childNode;
				scanCardAttachment.setnID(Integer.parseInt(element
						.getAttribute("id")));
				scanCardAttachment.setnAddress(Integer.parseInt(element
						.getAttribute("address")));

				Element childeleElement1 = null;
				for (int i1 = 0; i1 < childNode.getChildNodes().getLength(); i1++) {
					Node nodetemp = null;
					nodetemp = childNode.getChildNodes().item(i1);
					// childeleElement1
					String sstringString = nodetemp.getNodeName().trim();

					if (nodetemp.getNodeName().trim().equals("#text")) {
						continue;
					} else {
						childeleElement1 = (Element) nodetemp;
					}
					/*******************************************************************************/
					if (childeleElement1.getNodeName().trim()
							.equals("ScanCard")) {
						CStructSingleScanCard scancard = new CStructSingleScanCard();
						getCStructSingleScanCard(childeleElement1, scancard);
						scanCardAttachment.setScancard(scancard);
					}
					if (childeleElement1.getNodeName().trim()
							.equals("ScanCardLinkup")) {
						LinkTable linkTable = new LinkTable();
						getScanCardLinkup(childeleElement1, linkTable);
						scanCardAttachment.setSlinktable(linkTable);
					}
					if (childeleElement1.getNodeName().trim()
							.equals("HUBLinkup")) {
						LinkTable linkTable = new LinkTable();
						getHUBLinkup(childeleElement1, linkTable);
						scanCardAttachment.setHublinktable(linkTable);
					}
					if (childeleElement1.getNodeName().trim()
							.equals("SectionLinkup")) {
						LinkTable linkTable = new LinkTable();
						getSectionLinkup(childeleElement1, linkTable);
						scanCardAttachment.setsSectionlinktable(linkTable);
					}

					if (childeleElement1.getNodeName().trim()
							.equals("MonitorCard")) {
						// scanCardAttachment.setbTHSBoard(Boolean.parseBoolean(childeleElement1.getAttribute("THBoard")));//温、湿度传感器板
						scanCardAttachment.setbTHSBoard(Short
								.parseShort(childeleElement1
										.getAttribute("THBoard")) == 0 ? false
								: true);// 温、湿度传感器板
						scanCardAttachment
								.setbMFCard(Short.parseShort(childeleElement1
										.getAttribute("MultiFuncBoard")) == 0 ? false
										: true);// 多功能卡
						scanCardAttachment
								.setbPDCard(Short.parseShort(childeleElement1
										.getAttribute("PowerBoard")) == 0 ? false
										: true);// 功率检测卡
						scanCardAttachment
								.setbPBPDCard(Short.parseShort(childeleElement1
										.getAttribute("DotDectorBoard")) == 0 ? false
										: true);
					}
					if (childeleElement1.getNodeName().trim()
							.equals("MonitorItem")) {
						MonitorItem monitorItem = new MonitorItem();
						getMonitorItem(childeleElement1, monitorItem);
						scanCardAttachment.setMonitoritem(monitorItem);
					}
					// if
					// (childeleElement1.getNodeName().trim().equals("Relays"))
					// {
					// getMonitorItem(childeleElement1,
					// scanCardAttachment.getMonitoritem());
					// }
					if (childeleElement1.getNodeName().trim()
							.equals("LoadedRegion")) {
						RECT rect = new RECT();
						getLoadedRegion(childeleElement1, rect);
						scanCardAttachment.setRtRect(rect);
					}
				}
				/***************************************************************************************/
				listscattachment.add(scanCardAttachment);
			}
		}
		return true;
	}

	public static boolean getLoadedRegion(Element element, RECT rtRect) {
		rtRect.setTop(Integer.parseInt(element.getAttribute("top")));
		rtRect.setLeft(Integer.parseInt(element.getAttribute("left")));
		rtRect.setRight(Integer.parseInt(element.getAttribute("right")));
		String strBottom = element.getAttribute("bottom");
		rtRect.setBottom(Integer.parseInt(strBottom));
		return true;
	}

	public static boolean getMonitorItem(Element element,
			MonitorItem monitoritem) {
		if (element.getAttribute("Temperature") != null) {
			monitoritem.setbTemperatureFlag(Short.parseShort(element
					.getAttribute("Temperature")) == 0 ? false : true);
		}
		monitoritem.setbHumidityFlag(Short.parseShort(element
				.getAttribute("Humidity")) == 0 ? false : true);
		monitoritem
				.setbSmogFlag(Short.parseShort(element.getAttribute("Smoke")) == 0 ? false
						: true);
		List<Boolean> bFanStateFlag = new ArrayList<Boolean>();
		bFanStateFlag
				.add(Short.parseShort(element.getAttribute("LeftFan")) == 0 ? false
						: true);
		bFanStateFlag
				.add(Short.parseShort(element.getAttribute("RightFan")) == 0 ? false
						: true);
		monitoritem.setbFanStateFlag(bFanStateFlag);

		monitoritem.setbLEDPointDetect(Short.parseShort(element
				.getAttribute("DotDetect")) == 0 ? false : true);
		monitoritem.setbCapacityFactorFlag(Short.parseShort(element
				.getAttribute("Capacity")) == 0 ? false : true);
		// element.getAttribute("Capacity");
		List<Boolean> bPowerVolFlag = new ArrayList<Boolean>();
		bPowerVolFlag
				.add(Short.parseShort(element.getAttribute("PowerVol1")) == 0 ? false
						: true);
		bPowerVolFlag
				.add(Short.parseShort(element.getAttribute("PowerVol2")) == 0 ? false
						: true);
		bPowerVolFlag
				.add(Short.parseShort(element.getAttribute("PowerVol3")) == 0 ? false
						: true);
		bPowerVolFlag
				.add(Short.parseShort(element.getAttribute("PowerVol4")) == 0 ? false
						: true);
		bPowerVolFlag
				.add(Short.parseShort(element.getAttribute("PowerVol5")) == 0 ? false
						: true);

		monitoritem.setbPowerVolFlag(bPowerVolFlag);
		return true;
	}

	// 获取扫描卡信息
	public static boolean getCStructSingleScanCard(Element element,
			CStructSingleScanCard scancard) {

		String cTmp = element.getAttribute("DeductBit");
		if ("" == cTmp) {
			scancard.setnDeductBit(0);
		} else {
			scancard.setnDeductBit(
					Integer.parseInt(cTmp));
		}
		
		scancard.setnScanCardWidth(Short.parseShort(element
				.getAttribute("ONE_SCAN_CARD_WIDTH")));
		// ONE_SCAN_CARD_HEIGHT 2 96 单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
		// 即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
		scancard.setnScanCardHeight(Short.parseShort(element
				.getAttribute("ONE_SCAN_CARD_HEIGHT")));
		// mod_width 1 32 模组宽度，取值范围1-64
		scancard.setnModuleWidth(Short.parseShort(element
				.getAttribute("mod_width")));
		// mod_height 1 32 模组高度，取值范围1-64
		scancard.setnModuleHeight(Short.parseShort(element
				.getAttribute("mod_height")));
		// 1 3 模组区数 模组区数 = 模组高度/每区行数
		scancard.setnModuleSectionNumber(Short.parseShort(element
				.getAttribute("mod_section_number")));
		// 1 4 模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
		scancard.setnModuleHorNum(Short.parseShort(element
				.getAttribute("mod_hor_number")));
		// 1 3 模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
		scancard.setnModuleVerNum(Short.parseShort(element
				.getAttribute("mod_ver_number")));
		// SCAN_CARD_SECTION_NUM 1 6 扫描卡的区数，区数最大为16
		scancard.setnScanCardSectionNumber(Short.parseShort(element
				.getAttribute("SCAN_CARD_SECTION_NUM")));
		// SCAN_CARD_SECTION_ROW_NUM 1 16 扫描卡每区行数，行数最大为16行
		scancard.setnScanCardSectionRowNumber(Short.parseShort(element
				.getAttribute("SCAN_CARD_SECTION_ROW_NUM")));
		// SCAN_COLOR_DEPTH 1 14 扫描卡扫描的颜色深度，取12~16的整数。
		scancard.setnScanColorDepth(Short.parseShort(element
				.getAttribute("SCAN_COLOR_DEPTH")));
		// GRAY_LEVEL 扫描卡灰度级别
		scancard.setnGrayLedvel(Short.parseShort(element
				.getAttribute("GRAY_LEVEL")));
		// origin_color_bit 原始颜色深度，比如8BIT,10bit，12bit，16bit
		scancard.setnOrginColorBit(Short.parseShort(element
				.getAttribute("origin_color_bit")));
		// SCAN_MODE 1 4 扫描的模式，取 1-2-4-8-16
		scancard.setnScanMode(Short.parseShort(element
				.getAttribute("SCAN_MODE")));
		// DOT_CORRECTION_EN 1 1 单点校正使能，取0，1
		scancard.setbEmendBrightness(Short.parseShort(element
				.getAttribute("DOT_CORRECTION_EN")) == 0 ? false : true);
		// SCAN_GCLK_FREQUENCY 1 12.5 扫描时钟频率，最大30Mhz
		scancard.setfScanClkFrequency(Float.parseFloat(element
				.getAttribute("SCAN_GCLK_FREQUENCY")));
		// ZONE_DCLK_NUM 2 每区移位时钟数，256*16
		scancard.setnZoneClkNum(Integer.parseInt(element
				.getAttribute("ZONE_DCLK_NUM")));
		// 1 0-100 占空比 扫描时钟的占空比
		scancard.setnDutyCycle(Short.parseShort(element
				.getAttribute("duty_cycle_low_value")));
		// PWM_SCAN_GCLK_FREQUENCY 1 12.5 PWM时钟时钟频率，最大30Mhz
		scancard.setfPWMScanClkFrequency(Float.parseFloat(element
				.getAttribute("PWM_SCAN_GCLK_FREQUENCY")));
		// 1 0-100 PWM时钟占空比可调等级
		scancard.setnPWMDutyCycle(Short.parseShort(element
				.getAttribute("pwm_duty_cycle_low_value")));
		// CLR_CLK_NUM 1 4 1~255 消隐时钟数(行消隐时间)
		scancard.setnOeClkNumber(Long.parseLong(element
				.getAttribute("CLR_CLK_NUM")));
		// 刷新频率 根据颜色深度和扫描方式不同而不同
		scancard.setnRefreshRate(Integer.parseInt(element
				.getAttribute("refresh_rate")));
		// REF_FREQ_MIN 刷新率最小值
		scancard.setnRefreshRateMin(Integer.parseInt(element
				.getAttribute("refresh_rate_min")));
		// REF_FREQ_MAX 刷新率最大值
		scancard.setnRefreshRateMax(Integer.parseInt(element
				.getAttribute("refresh_rate_max")));
		// CONFIG_IC_TIME 配寄存器与逐点检测的时间(芯片间隔时间)， 1ms--30s
		scancard.setnConfigICTime(Short.parseShort(element
				.getAttribute("config_ic_time")));
		// dat_freq_num 换帧频率计数器
		scancard.setnDatFreqNum(Integer.parseInt(element
				.getAttribute("dat_freq_num")));
		// OE_DELAY_VALUE 0x01 消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
		scancard.setnOeDelayValue(Short.parseShort(element
				.getAttribute("OE_DELAY_VALUE")));
		// SYN_REFRESH_EN 同步刷新使能。默认为0x1，使能
		scancard.setbSyncRefresh(Short.parseShort(element
				.getAttribute("SYN_REFRESH_EN")) == 0 ? false : true);
		// VIRTUAL_DISP_EN 虚拟显示使能，默认为0x0，不使能
		scancard.setbVirtvalDisp(Short.parseShort(element
				.getAttribute("VIRTUAL_DISP_EN")) == 0 ? false : true);
		// FREQ_DIVISION_COEF 0x7
		// 150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
		scancard.setnFreqDivisionCoeff(Short.parseShort(element
				.getAttribute("FREQ_DIVISION_COEF")));
		// PWM_FREQ_DIVISION_COEF 0x7
		// 125Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
		scancard.setnPWMFreqDivisionCoeff(Short.parseShort(element
				.getAttribute("PWM_FREQ_DIVISION_COEF")));
		// DATA_OUTPUT_REVERSE 输出口逆序包括数据线和扫描线 0x00 数据线逆序：默认为0x0 0x0 不使能 0x1 使能
		scancard.setbDataOutUpReverse(Short.parseShort(element
				.getAttribute("DATA_OUTPUT_REVERSE")) == 0 ? false : true);
		// SCAN_OUTPUT_REVERSE 0x00 扫描线逆序：默认为0x0 0x0 不使能 0x1 使能
		scancard.setbScanOutUpReverse(Short.parseShort(element
				.getAttribute("SCAN_OUTPUT_REVERSE")) == 0 ? false : true);
		// DCB_LINE_CLK_EN 0x00 使能行信号DCB为时钟使带载高度加倍; 0x0 不使能 0x1 2倍 0x2 3倍 0x3 4倍
		scancard.setnDCBlineClkEn(Short.parseShort(element
				.getAttribute("DCB_LINE_CLK_EN")));
		// NO_SIGNAL_DISP 无信号显示：默认为0 0x0：黑屏，0x1：随机画面。0x2：图片
		scancard.setnNoSingleDisp(Short.parseShort(element
				.getAttribute("NO_SIGNAL_DISP")));

		// DATA_INPUT_DIR 0x32 数据方向：（从显示屏正面看）
		// 默认为从右到左0x1 0x0 从左往右 0x1 从右往左 0x2 从上往下 0x3 从下往上
		scancard.setnDataInputDir(Short.parseShort(element
				.getAttribute("DATA_INPUT_DIR")));

		// ROW_DECODE_MODE 行译码方式：默认0x2
		// 0x0 静态无译码 0x6 164译码
		// 0x1 无译码芯片，直接驱动行管 0x7 192译码
		// 0x2 138译码 0x8 193译码
		// 0x3 139译码 0x9 595译码
		// 0x4 145译码或138双O 0xA 4096译码
		// 0x5 154译码 0xB
		scancard.setnRowDecodeMOde(Short.parseShort(element
				.getAttribute("ROW_DECODE_MODE")));
		// DATA_LINE_TYPE 8 0x14 7--0 数据类型大类：默认为0x00，
		scancard.setnDataLineTypeRange(Short.parseShort(element
				.getAttribute("DATA_LINE_TYPE_RANGE")));
		// DATA_LINE_TYPE 8 0x14 7--0 数据类型：默认为0x00，
		// 0x00-0x1F 红绿蓝分开,
		// 0x20-0x18 红绿蓝合一三色1点串行
		// 0x30-0x38 红绿蓝合一三色8点串行
		// 0x40-0x48 红绿蓝合一三色16点串行
		// 0x50-0x6F 红绿蓝合一四色串行
		scancard.setnDataLineType(Short.parseShort(element
				.getAttribute("DATA_LINE_TYPE")));

		// DATA_LINE_CTRL 8 0x00 数据线控制,
		// 控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
		scancard.setnDataLineCtrl(Short.parseShort(element
				.getAttribute("DATA_LINE_CTRL")));

		// FIELD_NUM 总场数，最大为136场，做减1处理。
		scancard.setnFieldNum(Short.parseShort(element
				.getAttribute("FIELD_NUM")));

		// HALF_FIELD_NUM 半场数，最大为9场.默认为0x6，减1处理
		scancard.setnHalfFieldNumber(Short.parseShort(element
				.getAttribute("HALF_FIELD_NUM")));

		// FULL_FIELD_NUM 全场数，最大为128场，减1处理
		scancard.setnFullFieldNumber(Short.parseShort(element
				.getAttribute("FULL_FIELD_NUM")));

		// 起始场
		scancard.setnStartField(Short.parseShort(element
				.getAttribute("start_field")));

		// 终止场
		scancard.setnEndField(Short.parseShort(element
				.getAttribute("end_field")));

		// DATA_POLARITY 4 数据极性：默认为0x0 0x0 高电平点亮 0x1 低电平点亮 0x2-0xF 其他14种情况，预留
		scancard.setnDataPolarity(Short.parseShort(element
				.getAttribute("DATA_POLARITY")));

		// OE_POLARITY 0x1F OE极性：默认为0x0 0x0 低有效 0x1 高有效
		scancard.setnOePolarity(Short.parseShort(element
				.getAttribute("OE_POLARITY")));

		// EMPTY_INSERT_EN 空点插入使能，即每多少点插入多少空点. 默认0x0，不使能
		scancard.setbEmptyInsertEnable(Short.parseShort(element
				.getAttribute("EMPTY_INSERT_EN")) == 0 ? false : true);

		// INSERT_MODE 插入空点方式，前插入还是后插入。 1：前插入空点。0：后插入空点
		scancard.setnInsertMode(Short.parseShort(element
				.getAttribute("INSERT_MODE")));

		// EMPTY_DOT_NUM 插入的空点数，每次最大只能插入64空点，做减1处理。
		scancard.setnEmptyDotNum(Short.parseShort(element
				.getAttribute("EMPTY_DOT_NUM")));

		// REAL_DOT_NUM 15--0 每多少点插入空点，做减1处理。
		scancard.setnRealDotNum(Short.parseShort(element
				.getAttribute("REAL_DOT_NUM")));

		// 双列输出
		scancard.setbDualOutput(Short.parseShort(element
				.getAttribute("dual_out_put")) == 0 ? false : true);

		// 虚拟像素排布方式
		scancard.setnVirTualArray(Short.parseShort(element
				.getAttribute("virTual_array")));

		// 灯板芯片
		scancard.setnChipType(Integer.parseInt(element
				.getAttribute("chip_type")));

		// ref_doule_value 刷新率倍增的倍数，
		scancard.setnRefreshDoubleValue(Short.parseShort(element
				.getAttribute("ref_doule_value")));

		// zhe_rdwr_mode 折处理模块读写折DPRAM的方式。默认为0
		// 0：按列8读写，1：按箱体行读写
		scancard.setnZheRdwrMode(Short.parseShort(element
				.getAttribute("zhe_rdwr_mode")));

		// 显示屏类型 0-全彩实像素，1-全彩虚拟
		scancard.setnScreenType(Short.parseShort(element
				.getAttribute("screen_type")));

		// 单点校正类型, 0-调亮，1-调色
		scancard.setnDotCorrectTye(Short.parseShort(element
				.getAttribute("dot_correct_tye")));

		// 虚拟显示变化
		scancard.setbVirtualChangeFlag(false);
		// sScanCard.bVirtualChangeFlag = false;
		// 原先虚拟显示
		scancard.setbVirtualPrime(scancard.isbVirtvalDisp());
		// sScanCard.bVirtualPrime = sScanCard.bVirtvalDisp;

		// 测试效果/测试结束
		scancard.setbTest(Short.parseShort(element.getAttribute("test_start")) == 0 ? false
				: true);

		// //2013-3-5
		// if ( _GENERAL == scancard.getnChipType())
		// {
		// pAtt = pAtt->Next();
		// cTmp = pAtt->Value();
		// sScanCard.nSecondHighLevel = Integer.parseInt(cTmp);
		// }

		// 亮度有效率
		scancard.setfBrightnessEfficent(Float.parseFloat(element
				.getAttribute("brightness_efficent")));

		// 最小OE宽度
		scancard.setnMinOEWidth(Short.parseShort(element
				.getAttribute("min_oe_width")));

		// 颜色深度变化标识
		scancard.setbScanColorDepthChangeFlag(false);
		// 原先颜色深度
		scancard.setnScanColorDepthPrime(scancard.getnScanColorDepthPrime());

		// 使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
		scancard.setbDotOpenDetection(Short.parseShort(element
				.getAttribute("dot_open_detection")) == 0 ? false : true);

		// PWM输出模式 MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打散模式，0-普通模式
		scancard.setnPWMOutputMode(Short.parseShort(element
				.getAttribute("pwm_output_mode")));

		// 刷新率倍增，PWM静态扫描下有效
		scancard.setbMultiRefreshUnderStaticScan(Short.parseShort(element
				.getAttribute("multi_refresh_under_static_scan")) == 0 ? false
				: true);

		// ONE_SCAN_CARD_WIDTH_REAL
		// 即扫描卡实际宽度。主要用于逐点检测和校正中有关模组的水平垂直个数
		// ONE_SCAN_CARD_HEIGHT_REAL
		// 即扫描卡实际高度。主要用于逐点检测和校正中有关模组的水平垂直个数
		cTmp = element.getAttribute("ONE_SCAN_CARD_WIDTH_REAL");
		if ("" == cTmp) {
			scancard.setnScanCardWidthReal(scancard.getnScanCardWidth());
			scancard.setnScanCardHeightReal(scancard.getnScanCardHeight());
		} else {
			scancard.setnScanCardWidthReal(Short.parseShort(cTmp));
			scancard.setnScanCardHeightReal(Short.parseShort(element
					.getAttribute("ONE_SCAN_CARD_HEIGHT_REAL")));
		}

		// 扩区使能 与本地文件参数不同 zhangsj 2015-01-16
		/*
		 * 在拷贝箱体过程中 ，发现，有的箱体不存在下面参数
		 */

		cTmp = element.getAttribute("extend_enable");
		// if(null != cTmp)
		if ("" != cTmp) {
			// Short.parseShort(element.getAttribute("multi_refresh_under_static_scan"))
			// == 0? false:true
			scancard.setbExtendedEnable(Short.parseShort(cTmp) == 1 ? true
					: false);
			cTmp = element.getAttribute("extend_enableEx");
			scancard.setbExtendedEnableEx(Short.parseShort(cTmp) == 1 ? true
					: false);// Boolean.parseBoolean(cTmp)
			// 区宽
			cTmp = element.getAttribute("section_width");
			scancard.setnSectionWidth(Short.parseShort(cTmp));

			// 横向区个数
			cTmp = element.getAttribute("section_hor_number");
			scancard.setnSectionHorNum(Short.parseShort(cTmp));

			// 卡区宽
			cTmp = element.getAttribute("Card_zone_width");
			// cTmp = element.getAttribute("Card_zone_width");
			// 20150513调试，同步不了缺少该参数箱体，需要加判断zhangsj
			/***********************************************/
			if ("" != cTmp) {

				short short1 = (0 == Short.parseShort(cTmp)) ? scancard
						.getnSectionWidth() : Short.parseShort(cTmp);
				scancard.setnCard_zone_width(short1);
				// 卡区个数
				cTmp = element.getAttribute("Card_zone_Num");
				short1 = (0 == Short.parseShort(cTmp)) ? 1 : Short
						.parseShort(cTmp);
				scancard.setnCard_zone_Num(short1);
			} else {
				// 卡区个数
				scancard.setnCard_zone_width((short) 0); // 150513
				scancard.setnCard_zone_Num((short) 1); // 150513
			}
			/***********************************************/
			/*
			 * Short short1 = (0 == Short.parseShort(cTmp)) ?
			 * scancard.getnSectionWidth() : Short.parseShort(cTmp);
			 * scancard.setnCard_zone_width(short1); //卡区个数
			 * 
			 * cTmp = element.getAttribute("Card_zone_Num");
			 * 
			 * short1 = (0 == Short.parseShort(cTmp)) ? 1 :
			 * Short.parseShort(cTmp); scancard.setnCard_zone_Num(short1);
			 */
			// 灰度增强位数
			cTmp = element.getAttribute("gray_enhance_bit");
			scancard.setnGrayEnhance(Short.parseShort(cTmp));

			// 灰度增强方式
			cTmp = element.getAttribute("gray_enhance_mode");
			scancard.setnGrayEnhanceMode(Short.parseShort(cTmp));

			// 箱体指示灯
			cTmp = element.getAttribute("open_cabinet_lamp");
			scancard.setbOpenCabinetLamp(Short.parseShort(cTmp) == 1 ? true
					: false);// Boolean.parseBoolean(cTmp)
		} else {
			scancard.setbExtendedEnable(false);
			// 区宽
			scancard.setnSectionWidth(scancard.getnModuleWidth());
			// 横向区个数
			scancard.setnSectionHorNum((short) 1);

			// 灰度增强位数
			scancard.setnGrayEnhance((short) 1);
			// 灰度增强方式
			scancard.setnGrayEnhanceMode((short) 3);
			// 箱体指示灯
			scancard.setbOpenCabinetLamp(true);
		}

		// 外部扩区使能
		cTmp = element.getAttribute("extend_enableEx");
		if ("" != cTmp) {
			scancard.setbExtendedEnableEx(Short.parseShort(cTmp) == 1 ? true
					: false);
			// 卡区宽
			cTmp = element.getAttribute("Card_zone_width");
			// 20150513调试，同步不了缺少该参数箱体，需要加判断zhangsj
			/***********************************************/
			if ("" != cTmp) {

				short short1 = (0 == Short.parseShort(cTmp)) ? scancard
						.getnSectionWidth() : Short.parseShort(cTmp);
				scancard.setnCard_zone_width(short1);
				// 卡区个数
				cTmp = element.getAttribute("Card_zone_Num");
				short1 = (0 == Short.parseShort(cTmp)) ? 1 : Short
						.parseShort(cTmp);
				scancard.setnCard_zone_Num(short1);
			} else {
				// 卡区个数
				scancard.setnCard_zone_width((short) 0); // 150513
				scancard.setnCard_zone_Num((short) 1); // 150513
			}
			/***********************************************/
		} else {
			scancard.setbExtendedEnableEx(false);

			// 卡区个数
			scancard.setnCard_zone_Num((short) 1);

			// 卡区宽
			scancard.setnCard_zone_width((short) (scancard.getnScanCardWidth() / (scancard
					.getnCard_zone_Num() * scancard.getnSectionHorNum())));
			;
		}

		// 扩区使能
		// Gamma位数，8，10,12位，默认8位
		cTmp = element.getAttribute("gamma_bit");
		if ("" == cTmp) {
			scancard.setnCustomGamam(8);
		} else {
			scancard.setnCustomGamam(Integer.parseInt(cTmp));
		}

		// 芯片预充电功能, 0 - 打开 1， 关闭,默认打开
		cTmp = element.getAttribute("chip_precharge");
		if ("" == cTmp) {
			scancard.setbChipPrecharge(false);
			scancard.setnGClkDelay((short) 10);
			scancard.setnGClkDelay_B((short) 10);
			scancard.setnGClkDelay_G((short) 10);
		} else {
			scancard.setbChipPrecharge(Short.parseShort(cTmp) == 1 ? true
					: false);

			// RGB三色分别控制GCLK的延迟时间 使能标识
			cTmp = element.getAttribute("gclk_delay_ctrl_by_rgb");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByRGBEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}

			// R色控制GCLK的延迟时间 使能标识
			cTmp = element.getAttribute("gclk_delay_ctrl_by_r");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByREnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}
			// G色控制GCLK的延迟时间 使能标识
			cTmp = element.getAttribute("gclk_delay_ctrl_by_g");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByGEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
				// = Integer.parseInt(cTmp) ? true : false;
			}
			// B色控制GCLK的延迟时间 使能标识
			cTmp = element.getAttribute("gclk_delay_ctrl_by_b");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByBEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}

			// GCLK的延迟时钟数（三色分开时为R)
			cTmp = element.getAttribute("gclk_delay");
			if (!cTmp.equals("")) {
				scancard.setnGClkDelay(Short.parseShort(cTmp));
			} else {
				scancard.setnGClkDelay((short) 10);
			}

			cTmp = element.getAttribute("gclk_delay_G");
			if (!cTmp.equals("")) {
				scancard.setnGClkDelay_G(Short.parseShort(cTmp));
			} else {
				scancard.setnGClkDelay_G((short) 10);
			}

			cTmp = element.getAttribute("gclk_delay_B");
			if (!cTmp.equals("")) {
				scancard.setnGClkDelay_B(Short.parseShort(cTmp));
			} else {
				scancard.setnGClkDelay_B((short) 10);
			}
		}

		
		//寄存器信息
		if (CHIP_TYPE._TLC5958.ordinal() == scancard.getnChipType() || 
			CHIP_TYPE._MBI5152.ordinal() == scancard.getnChipType() ||
			CHIP_TYPE._MBI5153.ordinal() == scancard.getnChipType() )
		{
			//TLC5958
			//reg1
			//全局亮度调节
			cTmp = element.getAttribute("bright");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnBright(4);
			} else {
				scancard.getnDrive_ic_reg().setnBright(Integer.parseInt(cTmp));
			}

			//红色低灰增强
			cTmp = element.getAttribute("lgse_r");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_R(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_R(Integer.parseInt(cTmp));
			}

			//绿色低灰增强
			cTmp = element.getAttribute("lgse_g");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_G(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_G(Integer.parseInt(cTmp));
			}

			//蓝色低灰增强
			cTmp = element.getAttribute("lgse_b");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_B(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_B(Integer.parseInt(cTmp));
			}

			//输出通道延迟使能
			cTmp = element.getAttribute("gdly_enable");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGdly_Enable(1);
			}else{
				scancard.getnDrive_ic_reg().setnGdly_Enable(Integer.parseInt(cTmp));
			}

			//输入数据延迟
			cTmp = element.getAttribute("td_delay");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnTD_Delay(1);
			}else{
				scancard.getnDrive_ic_reg().setnTD_Delay(Integer.parseInt(cTmp));
			}

			//开路检测电压设定
			cTmp = element.getAttribute("lodvth");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLodvth(1);
			}else{
				scancard.getnDrive_ic_reg().setnLodvth(Integer.parseInt(cTmp));
			}

			//reg2
			//全局低灰增强
			cTmp = element.getAttribute("global_lgse");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGlobal_Lgse(0);
			} else {
				scancard.getnDrive_ic_reg().setnGlobal_Lgse(Integer.parseInt(cTmp));
			}

			//打散模式
			cTmp = element.getAttribute("PVM_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPVM_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnPVM_Mode(Integer.parseInt(cTmp));
			}

			//红色EMI削减
			cTmp = element.getAttribute("EMI_r");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_R(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_R(Integer.parseInt(cTmp));
			}

			//绿色EMI削减
			cTmp = element.getAttribute("EMI_g");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_G(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_G(Integer.parseInt(cTmp));
			}

			//蓝色EMI削减
			cTmp = element.getAttribute("EMI_b");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_B(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_B(Integer.parseInt(cTmp));
			}

			//预充电模式
			cTmp = element.getAttribute("pre_charge");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPre_Charge(0);
			} else {
				scancard.getnDrive_ic_reg().setnPre_Charge(Integer.parseInt(cTmp));
			}

			//MBI5152/MBI5153
			//reg1
			//预充电模式
			cTmp = element.getAttribute("pre_charge1");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPre_Charge1(0);
			} else {
				scancard.getnDrive_ic_reg().setnPre_Charge1(Integer.parseInt(cTmp));
			}

			//PWM计数模式
			cTmp = element.getAttribute("pwm_count_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPwm_Count_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnPwm_Count_Mode(Integer.parseInt(cTmp));
			}

			//灰阶模式
			cTmp = element.getAttribute("gray_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGray_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnGray_Mode(Integer.parseInt(cTmp));
			}

			//GCLK倍频
			cTmp = element.getAttribute("enable_gclk");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEnable_GCLK(0);
			} else {
				scancard.getnDrive_ic_reg().setnEnable_GCLK(Integer.parseInt(cTmp));
			}

			//双倍刷新率
			cTmp = element.getAttribute("double_refreserate");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnDouble_RefreseRate(0);
			} else {
				scancard.getnDrive_ic_reg().setnDouble_RefreseRate(Integer.parseInt(cTmp));
			}

			//开路检测电压
			cTmp = element.getAttribute("voltage");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnVoltage(0);
			} else {
				scancard.getnDrive_ic_reg().setnVoltage(Integer.parseInt(cTmp));
			}

			//红绿蓝IC识别
			cTmp = element.getAttribute("icrecognition");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnIC_Recognition(0);
			} else {
				scancard.getnDrive_ic_reg().setnIC_Recognition(Integer.parseInt(cTmp));
			}

			//首行偏暗调节（红色）
			cTmp = element.getAttribute("adjust_red");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Red(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Red(Integer.parseInt(cTmp));
			}

			//首行偏暗调节（绿色）
			cTmp = element.getAttribute("adjust_green");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Green(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Green(Integer.parseInt(cTmp));
			}

			//首行偏暗调节（蓝色）
			cTmp = element.getAttribute("adjust_blue");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Blue(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Blue(Integer.parseInt(cTmp));
			}

			//倒数模式高电平不延伸
			cTmp = element.getAttribute("imhl_donnotstretch");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnImhl_DoNotStretch(0);
			} else {
				scancard.getnDrive_ic_reg().setnImhl_DoNotStretch(Integer.parseInt(cTmp));
			}

		}
		else if ( CHIP_TYPE._MBI5153_E.ordinal() == scancard.getnChipType() || 
				CHIP_TYPE._MBI5155.ordinal() == scancard.getnChipType())
		{
			//R_REG1：MBI5153红色驱动芯片第1组寄存器，默认值为0x9F2B
			cTmp = element.getAttribute("Reg1RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RHigh(Integer.parseInt(cTmp));//寄存器1红高字节
			}
			cTmp = element.getAttribute("Reg1RLow");
			if (cTmp.equals("")) {
				if (CHIP_TYPE._MBI5155.ordinal() == scancard.getnChipType())
				{
					//灰阶cfg1[7] 16，14，默认14bit
					scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(0xAB); //寄存器1红低字节
				} 
				else//_MBI5153_E == m_nChipType
				{
					//灰阶cfg1[7] 14，13，默认14bit
					scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(0x2B); //寄存器1红低字节
				}
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(Integer.parseInt(cTmp));//寄存器1红低字节
			}

			//R_REG1：MBI5153绿色驱动芯片第1组寄存器，默认值0xDF2B
			cTmp = element.getAttribute("Reg1GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GHigh(Integer.parseInt(cTmp));//寄存器1绿高字节
			}
			cTmp = element.getAttribute("Reg1GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GLow(0x2B);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GLow(Integer.parseInt(cTmp));//寄存器1绿低字节
			}

			//R_REG1:MBI5153蓝色驱动芯片第1组寄存器，默认值0xDF2B
			cTmp = element.getAttribute("Reg1BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BHigh(Integer.parseInt(cTmp));//寄存器1蓝高字节
			}
			cTmp = element.getAttribute("Reg1BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BLow(0x2B);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BLow(Integer.parseInt(cTmp));//寄存器1蓝低字节
			}
			

			//R_REG2：MBI5153红色驱动芯片第2组寄存器，默认值为0x4600
			cTmp = element.getAttribute("Reg2RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RHigh(0x0F);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RHigh(Integer.parseInt(cTmp));//寄存器2红高字节
			}
			cTmp = element.getAttribute("Reg2RLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RLow(Integer.parseInt(cTmp));//寄存器2红低字节
			}

			//R_REG2：MBI5153绿色驱动芯片第2组寄存器，默认值0x4500
			cTmp = element.getAttribute("Reg2GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GHigh(0x06);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GHigh(Integer.parseInt(cTmp));//寄存器2绿高字节
			}
			cTmp = element.getAttribute("Reg2GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GLow(Integer.parseInt(cTmp));//寄存器2绿低字节
			}

			//R_REG2:MBI5153蓝色驱动芯片第2组寄存器，默认值0x6500
			cTmp = element.getAttribute("Reg2BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BHigh(0x26);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BHigh(Integer.parseInt(cTmp));//寄存器2蓝高字节
			}
			cTmp = element.getAttribute("Reg2BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BLow(Integer.parseInt(cTmp));//寄存器2蓝低字节
			}

			//R_REG3：MBI5153红色驱动芯片第3组寄存器，默认值为0xC003
			cTmp = element.getAttribute("Reg3RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RHigh(0xC0);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RHigh(Integer.parseInt(cTmp));//寄存器3红高字节
			}
			cTmp = element.getAttribute("Reg3RLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RLow(Integer.parseInt(cTmp));//寄存器3红低字节
			}

			//R_REG3：MBI5153绿色驱动芯片第3组寄存器，默认值0x5003
			cTmp = element.getAttribute("Reg3GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GHigh(0x50);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GHigh(Integer.parseInt(cTmp));//寄存器3绿高字节
			}
			cTmp = element.getAttribute("Reg3GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GLow(Integer.parseInt(cTmp));//寄存器2绿低字节
			}

			//R_REG3:MBI5153蓝色驱动芯片第3组寄存器，默认值0x4003
			cTmp = element.getAttribute("Reg3BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BHigh(0x40);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BHigh(Integer.parseInt(cTmp));//寄存器3蓝高字节
			}
			cTmp = element.getAttribute("Reg3BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BLow(Integer.parseInt(cTmp));//寄存器3蓝低字节
			}

			//最新_MBI5155渐变过渡优化，在寄存器5153保留的基础上增加的字段
			if (CHIP_TYPE._MBI5155.ordinal() == scancard.getnChipType())
			{
				cTmp = element.getAttribute("nDeltaT");//MBI5155 第513/257个GCLK的低电平宽度 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDeltaT((cTmp.equals("")) ? 0x00 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDeltaF");//MBI5155 第513/257个GCLK的高电平宽度 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDeltaF((cTmp.equals("")) ? 0x00 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDHT");//MBI5155 第1个GCLK的高电平宽度 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDHT((cTmp.equals("")) ? 0x1E : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDG_H");//MBI5155 第514/258个GCLK的高电平宽度
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDG_H((cTmp.equals("")) ? 0x26 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDG_L");//MBI5155 第514/258个GCLK的低电平宽度 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDG_L((cTmp.equals("")) ? 0x19 : Integer.parseInt(cTmp));
			}
		}
		else if (CHIP_TYPE._MBI5043.ordinal() == scancard.getnChipType())
		{
			//MBI5043 GCLK双沿采样	0：关闭， 1：开启
			cTmp = element.getAttribute("bGCLKDoublesampling");
			if (!cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().setbGCLKDoublesampling(Short.parseShort(cTmp) == 1 ? true : false);
			}
			if (scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().isbGCLKDoublesampling())//刷新率增倍处理
			{
				scancard.setnRefreshDoubleValue((short) (scancard.getnRefreshDoubleValue() * 2));
			}

			//MBI5043 PWM模式选择	0: 16bit，1: 10bit
			cTmp = element.getAttribute("nPWMMode");
			if (!cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().setnPWMMode((short) Integer.parseInt(cTmp));
			}
		}
		
		
		//网口优先设置
		cTmp = element.getAttribute("netportpriority");
		if (cTmp.equals("")) {
			scancard.setnNetPortPriority((short)0);
		}else{
			scancard.setnNetPortPriority(Short.parseShort(cTmp));
		}

		//锁定优先网口
		cTmp = element.getAttribute("locknetport");
		if (cTmp.equals("")) {
			scancard.setbLockNetPort(false);
		}else{
			scancard.setbLockNetPort(1 == Short.parseShort(cTmp) ? true : false);
		}
		
		return true;
	}

	public static boolean getScanCardLinkup(Element element,
			LinkTable slinktable) {
		String strtemp = null;
		strtemp = element.getAttribute("Len");
		slinktable.setnLen(Long.parseLong(strtemp));
		strtemp = element.getAttribute("data");

		byte[] temp = UtilFun.hexStringSplit2Bytes(strtemp, " ");
		byte[] temp1 = new byte[temp.length]; // 原来写入卡死情况，修改此处即可
		// byte[] temp1 = new byte[65535];
		for (int i = 0; i < temp.length; i++) {
			temp1[i] = temp[i];
		}
		slinktable.setUcLinkTable(temp1);

		return true;
	}

	public static boolean getSectionLinkup(Element element,
			LinkTable hublinktable) {
		String strtemp = null;
		strtemp = element.getAttribute("Len");
		hublinktable.setnLen(Long.parseLong(strtemp));
		strtemp = element.getAttribute("data");

		byte[] temp = UtilFun.hexStringSplit2Bytes(strtemp, " ");
		byte[] temp1 = new byte[temp.length]; // 原来写入卡死情况，修改此处即可
		// byte[] temp1 = new byte[65535];
		for (int i = 0; i < temp.length; i++) {
			temp1[i] = temp[i];
		}
		hublinktable.setUcLinkTable(temp1);
		return true;
	}

	public static boolean getHUBLinkup(Element element, LinkTable hublinktable) {
		String strtemp = null;
		strtemp = element.getAttribute("Len");
		hublinktable.setnLen(Long.parseLong(strtemp));
		strtemp = element.getAttribute("data");
		// LinkTable linkTable = new LinkTable();
		// hublinktable.setUcLinkTable(UtilFun.hexStringSplit2Bytes(strtemp,
		// " "));

		byte[] temp = UtilFun.hexStringSplit2Bytes(strtemp, " ");
		byte[] temp1 = new byte[temp.length];
		for (int i = 0; i < temp.length; i++) {
			temp1[i] = temp[i];
		}
		hublinktable.setUcLinkTable(temp1);
		return true;
	}

	// 删除箱体节点
	public static boolean RemoveCabinet(String cabinetname) {
		List<String> listnameList = new ArrayList<String>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(filepath,
					sFileCabinetString));
			NodeList list1 = doc.getElementsByTagName("Cabinets");
			Element elementroot = null;
			if (list1.getLength() == 1) {
				elementroot = (Element) list1.item(0);
			}
			NodeList list = doc.getElementsByTagName("Cabinet");
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				NodeList list2 = element.getChildNodes();
				Node childNode = null;
				String strtemp = null;
				for (int j = 0; j < list2.getLength(); j++) {
					childNode = element.getChildNodes().item(j);
					if (childNode.getNodeName().equals("Name")) {
						Element element3 = (Element) childNode;
						strtemp = element3.getTextContent();
						break;
					}
				}
				if (strtemp.equals(cabinetname)) {
					elementroot.removeChild(element);
					break;
				}
			}
			saveXML(doc, filepath + sFileCabinetString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
    //通过系列id删除对应箱体
	public static boolean RemoveCabinetBySerid(String cbtSeriesID) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(filepath,
					sFileCabinetString));
			NodeList list1 = doc.getElementsByTagName("Cabinets");
			Element elementroot = null;
			if (list1.getLength() == 1) {
				elementroot = (Element) list1.item(0);
			}
			NodeList list = doc.getElementsByTagName("Cabinet");
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				NodeList list2 = element.getChildNodes();
				Node childNode = null;
				String strtemp = null;
				for (int j = 0; j < list2.getLength(); j++) {
					childNode = element.getChildNodes().item(j);
					if (childNode.getNodeName().equals("SeriesID")) {
						Element element3 = (Element) childNode;
						strtemp = element3.getTextContent();
						break;
					}
				}
				if (strtemp.equals(cbtSeriesID)) {
					elementroot.removeChild(element);
					break;
				}
			}
			saveXML(doc, filepath + sFileCabinetString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static Node GetCabinetNode(String cabinet) {
		Node node = null;
		return node;
	}

	public static boolean AddCabinet(CabinetInformation cabinet) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(
					HomePageActivity.CONFIG_PATH, sFileCabinetString));
			NodeList list = doc.getElementsByTagName("Cabinets");
			Element eleroot = null;
			if (list.getLength() == 1) {
				eleroot = (Element) list.item(0);// //
			}
			Element newelement = doc.createElement("Cabinet");// //
			Element elementID = doc.createElement("ID");
			elementID.setTextContent(String.valueOf(cabinet.getnID()));
			Element elementSeriesID = doc.createElement("SeriesID");
			elementID.setTextContent(String.valueOf(cabinet.getnSeriesID()));
			Element elementName = doc.createElement("Name");
			elementID.setTextContent(String.valueOf(cabinet.getsName()));
			Element elementPhoto = doc.createElement("Photo");
			elementID.setTextContent(String.valueOf(cabinet.getsPhoto()));
			Element elementSCACount = doc.createElement("SCACount");
			elementID
					.setTextContent(String.valueOf(cabinet.getnScanCardCount()));
			Element elementinlinemode = doc.createElement("inlinemode");
			elementID.setTextContent(String.valueOf(cabinet.getInlineMode()));
			Element elementScanCardParaSynchro = doc
					.createElement("ScanCardParaSynchro");
			elementID
					.setTextContent(cabinet.isbScanCardParaSynchro() == true ? String
							.valueOf(1) : String.valueOf(0));
			// String strtemp =
			// String.valueOf(cabinet.isbScanCardParaSynchro());
			Element elementMonitorParaSynchro = doc.createElement("MonitorParaSynchro");
			elementID.setTextContent(cabinet.isbMonitorParaSynchro() == true ? String.valueOf(1) : String.valueOf(0));
			Element elementScanCardAttachments = doc.createElement("ScanCardAttachments");
			// 添加扫描卡附件,此处读取文件信息，有空信息，写入箱体文件中，该部分写入不了

			SetScanCardAttachments(cabinet, doc, elementScanCardAttachments);

			Element elementLoadedRegion = doc.createElement("LoadedRegion");
			getLoadedRegion(elementLoadedRegion, cabinet.getRtRect());

			newelement.appendChild(elementID);
			newelement.appendChild(elementSeriesID);
			newelement.appendChild(elementName);
			newelement.appendChild(elementPhoto);
			newelement.appendChild(elementSCACount);
			newelement.appendChild(elementinlinemode);
			newelement.appendChild(elementScanCardParaSynchro);
			newelement.appendChild(elementMonitorParaSynchro);
			newelement.appendChild(elementScanCardAttachments);
			newelement.appendChild(elementLoadedRegion);
			eleroot.appendChild(newelement);

			// doc.creat
			saveXML(doc, HomePageActivity.CONFIG_PATH + sFileCabinetString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// CabinetLibraryActivity.CONFIG_PATH;
		return false;
	}

	public static boolean SetScancardPram(Element element,
			CStructSingleScanCard sScanCard) {
		
		//减去的色深位数
		element.setAttribute("DeductBit",
				String.valueOf(sScanCard.getnDeductBit()));

		// ONE_SCAN_CARD_WIDTH 2 128 单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
		// 即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
		element.setAttribute("ONE_SCAN_CARD_WIDTH",
				String.valueOf(sScanCard.getnScanCardWidth()));
		// ONE_SCAN_CARD_HEIGHT 2 96 单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
		// 即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
		element.setAttribute("ONE_SCAN_CARD_HEIGHT",
				String.valueOf(sScanCard.getnScanCardHeight()));
		// mod_width 1 32 模组宽度，取值范围1-64
		element.setAttribute("mod_width",
				String.valueOf(sScanCard.getnModuleWidth()));
		// mod_height 1 32 模组高度，取值范围1-64
		element.setAttribute("mod_height",
				String.valueOf(sScanCard.getnModuleHeight()));
		// 1 3 模组区数 模组区数 = 模组高度/每区行数
		element.setAttribute("mod_section_number",
				String.valueOf(sScanCard.getnModuleSectionNumber()));
		// 1 4 模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
		element.setAttribute("mod_hor_number",
				String.valueOf(sScanCard.getnModuleHorNum()));
		// 1 3 模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
		element.setAttribute("mod_ver_number",
				String.valueOf(sScanCard.getnModuleVerNum()));
		// SCAN_CARD_SECTION_NUM 1 6 扫描卡的区数，区数最大为16
		element.setAttribute("SCAN_CARD_SECTION_NUM",
				String.valueOf(sScanCard.getnScanCardSectionNumber()));
		// SCAN_CARD_SECTION_ROW_NUM 1 16 扫描卡每区行数，行数最大为16行
		element.setAttribute("SCAN_CARD_SECTION_ROW_NUM",
				String.valueOf(sScanCard.getnScanCardSectionRowNumber()));
		// SCAN_COLOR_DEPTH 1 14 扫描卡扫描的颜色深度，取12~16的整数。
		element.setAttribute("SCAN_COLOR_DEPTH",
				String.valueOf(sScanCard.getnScanColorDepth()));
		// GRAY_LEVEL 扫描卡灰度级别
		element.setAttribute("GRAY_LEVEL",
				String.valueOf(sScanCard.getnGrayLedvel()));
		// origin_color_bit 原始颜色深度，比如8BIT,10bit，12bit，16bit
		element.setAttribute("origin_color_bit",
				String.valueOf(sScanCard.getnOrginColorBit()));
		// SCAN_MODE 1 4 扫描的模式，取 1-2-4-8-16
		element.setAttribute("SCAN_MODE",
				String.valueOf(sScanCard.getnScanMode()));
		// DOT_CORRECTION_EN 1 1 单点校正使能，取0，1
		// ls.isbPBPDCard() == true? String.valueOf(1):String.valueOf(0)
		element.setAttribute("DOT_CORRECTION_EN",
				sScanCard.isbEmendBrightness() == true ? String.valueOf(1)
						: String.valueOf(0));
		// SCAN_GCLK_FREQUENCY 1 12.5 扫描时钟频率，最大30Mhz
		element.setAttribute("SCAN_GCLK_FREQUENCY",
				String.valueOf(sScanCard.getfScanClkFrequency()));
		// ZONE_DCLK_NUM 2 每区移位时钟数，256*16
		element.setAttribute("ZONE_DCLK_NUM",
				String.valueOf(sScanCard.getnZoneClkNum()));
		// 1 0-100 占空比 扫描时钟的占空比
		element.setAttribute("duty_cycle_low_value",
				String.valueOf(sScanCard.getnDutyCycle()));
		// PWM_SCAN_GCLK_FREQUENCY 1 12.5 PWM时钟时钟频率，最大30Mhz
		element.setAttribute("PWM_SCAN_GCLK_FREQUENCY",
				String.valueOf(sScanCard.getfPWMScanClkFrequency()));
		// 1 0-100 PWM时钟占空比可调等级
		element.setAttribute("pwm_duty_cycle_low_value",
				String.valueOf(sScanCard.getnPWMDutyCycle()));
		// CLR_CLK_NUM 1 4 1~255 消隐时钟数(行消隐时间)
		element.setAttribute("CLR_CLK_NUM",
				String.valueOf(sScanCard.getnOeClkNumber()));
		// 刷新频率 根据颜色深度和扫描方式不同而不同
		element.setAttribute("refresh_rate",
				String.valueOf(sScanCard.getnRefreshRate()));
		// REF_FREQ_MIN 刷新率最小值
		element.setAttribute("refresh_rate_min",
				String.valueOf(sScanCard.getnRefreshRateMin()));
		// REF_FREQ_MAX 刷新率最大值
		element.setAttribute("refresh_rate_max",
				String.valueOf(sScanCard.getnRefreshRateMax()));
		// CONFIG_IC_TIME 配寄存器与逐点检测的时间(芯片间隔时间)， 1ms--30s
		element.setAttribute("config_ic_time",
				String.valueOf(sScanCard.getnConfigICTime()));
		// dat_freq_num 换帧频率计数器
		element.setAttribute("dat_freq_num",
				String.valueOf(sScanCard.getnDatFreqNum()));
		// OE_DELAY_VALUE 0x01 消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
		element.setAttribute("OE_DELAY_VALUE",
				String.valueOf(sScanCard.getnOeDelayValue()));
		// SYN_REFRESH_EN 同步刷新使能。默认为0x1，使能
		element.setAttribute(
				"SYN_REFRESH_EN",
				sScanCard.isbSyncRefresh() == true ? String.valueOf(1) : String
						.valueOf(0));
		// VIRTUAL_DISP_EN 虚拟显示使能，默认为0x0，不使能
		element.setAttribute(
				"VIRTUAL_DISP_EN",
				sScanCard.isbVirtvalDisp() == true ? String.valueOf(1) : String
						.valueOf(0));
		// FREQ_DIVISION_COEF 0x7
		// 150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
		element.setAttribute("FREQ_DIVISION_COEF",
				String.valueOf(sScanCard.getnFreqDivisionCoeff()));
		// PWM_FREQ_DIVISION_COEF 0x7
		// 150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
		element.setAttribute("PWM_FREQ_DIVISION_COEF",
				String.valueOf(sScanCard.getnPWMFreqDivisionCoeff()));
		// DATA_OUTPUT_REVERSE 输出口逆序包括数据线和扫描线 0x00 数据线逆序：默认为0x0 0x0 不使能 0x1 使能
		element.setAttribute("DATA_OUTPUT_REVERSE",
				sScanCard.isbDataOutUpReverse() == true ? String.valueOf(1)
						: String.valueOf(0));
		// SCAN_OUTPUT_REVERSE 0x00 扫描线逆序：默认为0x0 0x0 不使能 0x1 使能
		element.setAttribute("SCAN_OUTPUT_REVERSE",
				sScanCard.isbScanOutUpReverse() == true ? String.valueOf(1)
						: String.valueOf(0));
		// DCB_LINE_CLK_EN 0x00 使能行信号DCB为时钟使带载高度加倍; 0x0 不使能 0x1 2倍 0x2 3倍 0x3 4倍
		element.setAttribute("DCB_LINE_CLK_EN",
				String.valueOf(sScanCard.getnDCBlineClkEn()));
		// NO_SIGNAL_DISP 无信号显示：默认为0 0x0：黑屏，0x1：随机画面。0x2：图片
		element.setAttribute("NO_SIGNAL_DISP",
				String.valueOf(sScanCard.getnNoSingleDisp()));
		// DATA_INPUT_DIR 0x32 数据方向：（从显示屏正面看）
		// 默认为从右到左0x1 0x0 从左往右 0x1 从右往左 0x2 从上往下 0x3 从下往上
		element.setAttribute("DATA_INPUT_DIR",
				String.valueOf(sScanCard.getnDataInputDir()));
		// ROW_DECODE_MODE 行译码方式：默认0x2
		// 0x0 静态无译码 0x6 164译码
		// 0x1 无译码芯片，直接驱动行管 0x7 192译码
		// 0x2 138译码 0x8 193译码
		// 0x3 139译码 0x9 595译码
		// 0x4 145译码或138双O 0xA 4096译码
		// 0x5 154译码 0xB
		element.setAttribute("ROW_DECODE_MODE",
				String.valueOf(sScanCard.getnRowDecodeMOde()));
		// DATA_LINE_TYPE 8 0x14 7--0 数据类型大类：默认为0x00，
		element.setAttribute("DATA_LINE_TYPE_RANGE",
				String.valueOf(sScanCard.getnDataLineTypeRange()));
		// DATA_LINE_TYPE 8 0x14 7--0 数据类型：默认为0x00，
		// 0x00-0x1F 红绿蓝分开,
		// 0x20-0x18 红绿蓝合一三色1点串行
		// 0x30-0x38 红绿蓝合一三色8点串行
		// 0x40-0x48 红绿蓝合一三色16点串行
		// 0x50-0x6F 红绿蓝合一四色串行
		element.setAttribute("DATA_LINE_TYPE",
				String.valueOf(sScanCard.getnDataLineType()));
		// DATA_LINE_CTRL 8 0x00 数据线控制,
		// 控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
		element.setAttribute("DATA_LINE_CTRL",
				String.valueOf(sScanCard.getnDataLineCtrl()));
		// FIELD_NUM 总场数，最大为136场，做减1处理。
		element.setAttribute("FIELD_NUM",
				String.valueOf(sScanCard.getnFieldNum()));
		// HALF_FIELD_NUM 半场数，最大为9场.默认为0x6，减1处理
		element.setAttribute("HALF_FIELD_NUM",
				String.valueOf(sScanCard.getnHalfFieldNumber()));
		// FULL_FIELD_NUM 全场数，最大为128场，减1处理
		element.setAttribute("FULL_FIELD_NUM",
				String.valueOf(sScanCard.getnFullFieldNumber()));
		// 起始场
		element.setAttribute("start_field",
				String.valueOf(sScanCard.getnStartField()));
		// 终止场
		element.setAttribute("end_field",
				String.valueOf(sScanCard.getnEndField()));
		// DATA_POLARITY 4 数据极性：默认为0x0 0x0 高电平点亮 0x1 低电平点亮 0x2-0xF 其他14种情况，预留
		element.setAttribute("DATA_POLARITY",
				String.valueOf(sScanCard.getnDataPolarity()));
		// OE_POLARITY 0x1F OE极性：默认为0x0 0x0 低有效 0x1 高有效
		element.setAttribute("OE_POLARITY",
				String.valueOf(sScanCard.getnOePolarity()));
		// EMPTY_INSERT_EN 空点插入使能，即每多少点插入多少空点. 默认0x0，不使能
		element.setAttribute("EMPTY_INSERT_EN",
				sScanCard.isbEmptyInsertEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// INSERT_MODE 插入空点方式，前插入还是后插入。 1：前插入空点。0：后插入空点
		element.setAttribute("INSERT_MODE",
				String.valueOf(sScanCard.getnInsertMode()));
		// EMPTY_DOT_NUM 插入的空点数，每次最大只能插入64空点，做减1处理。
		element.setAttribute("EMPTY_DOT_NUM",
				String.valueOf(sScanCard.getnEmptyDotNum()));
		// REAL_DOT_NUM 15--0 每多少点插入空点，做减1处理。
		element.setAttribute("REAL_DOT_NUM",
				String.valueOf(sScanCard.getnRealDotNum()));
		// 双列输出
		element.setAttribute(
				"dual_out_put",
				sScanCard.isbDualOutput() == true ? String.valueOf(1) : String
						.valueOf(0));
		// 虚拟像素排布方式
		element.setAttribute("virTual_array",
				String.valueOf(sScanCard.getnVirTualArray()));
		// 灯板芯片
		element.setAttribute("chip_type",
				String.valueOf(sScanCard.getnChipType()));
		// ref_doule_value 刷新率倍增的倍数，
		element.setAttribute("ref_doule_value",
				String.valueOf(sScanCard.getnRefreshDoubleValue()));
		// zhe_rdwr_mode 折处理模块读写折DPRAM的方式。默认为0
		// 0：按列8读写，1：按箱体行读写
		element.setAttribute("zhe_rdwr_mode",
				String.valueOf(sScanCard.getnZheRdwrMode()));
		// 显示屏类型 0-全彩实像素，1-全彩虚拟
		element.setAttribute("screen_type",
				String.valueOf(sScanCard.getnScreenType()));
		// 单点校正类型, 0-调亮，1-调色
		element.setAttribute("dot_correct_tye",
				String.valueOf(sScanCard.getnDotCorrectTye()));

		// 测试效果/测试结束
		element.setAttribute(
				"test_start",
				sScanCard.isbTest() == true ? String.valueOf(1) : String
						.valueOf(0));

		// if (_GENERAL == scancard.getnChipType())
		// {
		// //2013-3-5次高场
		// strcpy(chKey, "Second_Filed");
		// sprintf(chKeyVal,"%d",sScanCard.nSecondHighLevel);
		// ElementSC->SetAttribute(chKey,chKeyVal);
		// }
		// 亮度有效率
		element.setAttribute("brightness_efficent",
				String.valueOf(sScanCard.getfBrightnessEfficent()));
		// 最小OE宽度
		element.setAttribute("min_oe_width",
				String.valueOf(sScanCard.getnMinOEWidth()));
		// 使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
		element.setAttribute("dot_open_detection",
				sScanCard.isbDotOpenDetection() == true ? String.valueOf(1)
						: String.valueOf(0));
		// PWM输出模式 MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打散模式，0-普通模式
		element.setAttribute("pwm_output_mode",
				String.valueOf(sScanCard.getnPWMOutputMode()));
		// 刷新率倍增，PWM静态扫描下有效
		element.setAttribute("multi_refresh_under_static_scan", sScanCard
				.isbMultiRefreshUnderStaticScan() == true ? String.valueOf(1)
				: String.valueOf(0));

		// ONE_SCAN_CARD_WIDTH_REAL
		// 即扫描卡实际宽度。主要用于逐点检测和校正中有关模组的水平垂直个数
		element.setAttribute("ONE_SCAN_CARD_WIDTH_REAL",
				String.valueOf(sScanCard.getnScanCardWidthReal()));
		// ONE_SCAN_CARD_HEIGHT_REAL
		// 即扫描卡实际高度。主要用于逐点检测和校正中有关模组的水平垂直个数
		element.setAttribute("ONE_SCAN_CARD_HEIGHT_REAL",
				String.valueOf(sScanCard.getnScanCardHeightReal()));

		// 扩区使能 此处以下的注释部分写箱体信息时与cbt文件中参数不同，写入异常，暂时注释zhangsj 2015-01-16
		element.setAttribute("extend_enable",
				sScanCard.isbExtendedEnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		element.setAttribute("extend_enableEx",
				sScanCard.isbExtendedEnableEx() == true ? String.valueOf(1)
						: String.valueOf(0));
		// 区宽
		element.setAttribute("section_width",
				String.valueOf(sScanCard.getnSectionWidth()));
		// 横向区个数
		element.setAttribute("section_hor_number",
				String.valueOf(sScanCard.getnSectionHorNum()));

		// 卡区宽
		element.setAttribute("Card_zone_width",
				String.valueOf(sScanCard.getnCard_zone_width()));
		// 卡区个数
		element.setAttribute("Card_zone_Num",
				String.valueOf(sScanCard.getnCard_zone_Num()));

		// 灰度增强位数
		element.setAttribute("gray_enhance_bit",
				String.valueOf(sScanCard.getnGrayEnhance()));
		// 灰度增强方式
		element.setAttribute("gray_enhance_mode",
				String.valueOf(sScanCard.getnGrayEnhanceMode()));
		// 箱体指示灯
		element.setAttribute("open_cabinet_lamp",
				sScanCard.isbOpenCabinetLamp() == true ? String.valueOf(1)
						: String.valueOf(0));
		// gamma位数
		element.setAttribute("gamma_bit",
				String.valueOf(sScanCard.getnCustomGamam()));
		// 芯片预充电功能, 0 - 打开 1， 关闭,默认打开
		element.setAttribute("chip_precharge",
				sScanCard.isbChipPrecharge() == true ? String.valueOf(1)
						: String.valueOf(0));
		// RGB三色分别控制GCLK的延迟时间 使能标识
		element.setAttribute("gclk_delay_ctrl_by_rgb",
				sScanCard.isbGClkCtrlByRGBEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// R色控制GCLK的延迟时间
		element.setAttribute("gclk_delay_ctrl_by_r",
				sScanCard.isbGClkCtrlByREnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		// GCLK的延迟时钟数（三色分开时为R)
		element.setAttribute("gclk_delay",
				String.valueOf(sScanCard.getnGClkDelay()));
		// G色控制GCLK的延迟时间
		element.setAttribute("gclk_delay_ctrl_by_g",
				sScanCard.isbGClkCtrlByGEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// G的GCLK的延迟时钟数
		element.setAttribute("gclk_delay_G",
				String.valueOf(sScanCard.getnGClkDelay_G()));
		// B色控制GCLK的延迟时间
		element.setAttribute("gclk_delay_ctrl_by_b",
				sScanCard.isbGClkCtrlByBEnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		// B的GCLK的延迟时钟数
		element.setAttribute("gclk_delay_B",
				String.valueOf(sScanCard.getnGClkDelay_B()));

		// 寄存器信息
		if (CHIP_TYPE._TLC5958.ordinal() == sScanCard.getnChipType() ||
			CHIP_TYPE._MBI5152.ordinal() == sScanCard.getnChipType() ||
			CHIP_TYPE._MBI5153.ordinal() == sScanCard.getnChipType()) {
			
			// reg1
			// 全局亮度调节
			element.setAttribute("bright",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnBright()));
			// 红色低灰增强
			element.setAttribute("lgse_r",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_R()));
			// 绿色低灰增强
			element.setAttribute("lgse_g",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_G()));
			// 蓝色低灰增强
			element.setAttribute("lgse_b",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_B()));
			// 输出通道延迟使能
			element.setAttribute("gdly_enable", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGdly_Enable()));
			// 输入数据延迟
			element.setAttribute("td_delay",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnTD_Delay()));
			// 开路检测电压设定
			element.setAttribute("lodvth",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLodvth()));
			// reg2
			// 全局低灰增强
			element.setAttribute("global_lgse", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGlobal_Lgse()));
			// 打散模式
			element.setAttribute("PVM_mode",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnPVM_Mode()));
			// 红色EMI削减
			element.setAttribute("EMI_r",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_R()));
			// 绿色EMI削减
			element.setAttribute("EMI_g",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_G()));
			// 蓝色EMI削减
			element.setAttribute("EMI_b",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_B()));
			// 预充电模式
			element.setAttribute("pre_charge", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPre_Charge()));
		
			//MBI5152/5153
			//reg1
			//预充电模式
			element.setAttribute("pre_charge1", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPre_Charge1()));

			//PWM计数模式
			element.setAttribute("pwm_count_mode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPwm_Count_Mode()));

			//灰阶模式
			element.setAttribute("gray_mode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGdly_Enable()));

			//GCLK倍频
			element.setAttribute("enable_gclk", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnEnable_GCLK()));

			//reg2
			//双倍刷新率
			element.setAttribute("double_refreserate", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnDouble_RefreseRate()));

			//开路检测电压
			element.setAttribute("voltage", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnVoltage()));

			//红绿蓝IC识别
			element.setAttribute("icrecognition", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnIC_Recognition()));
			
			//首行偏暗调节（红色）
			element.setAttribute("adjust_red", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Red()));

			//首行偏暗调节（绿色）
			element.setAttribute("adjust_green", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Green()));

			//首行偏暗调节（蓝色）
			element.setAttribute("adjust_blue", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Blue()));

			//倒数模式高电平不延伸
			element.setAttribute("imhl_donnotstretch", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnImhl_DoNotStretch()));
		}
		else if (CHIP_TYPE._MBI5153_E.ordinal() == sScanCard.getnChipType() || 
				CHIP_TYPE._MBI5155.ordinal() == sScanCard.getnChipType())
		{
			
			//寄存器1红高字节
			element.setAttribute("Reg1RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1RHigh()));

			//寄存器1红低字节
			element.setAttribute("Reg1RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1RLow()));

			//寄存器1绿高字节
			element.setAttribute("Reg1GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1GHigh()));
			
			//寄存器1绿低字节
			element.setAttribute("Reg1GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1GLow()));
			
			//寄存器1蓝高字节
			element.setAttribute("Reg1BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1BHigh()));
			
			//寄存器1蓝低字节
			element.setAttribute("Reg1BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1BLow()));

			//寄存器2红高字节
			element.setAttribute("Reg2RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2RHigh()));

			//寄存器2红低字节
			element.setAttribute("Reg2RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2RLow()));

			//寄存器2绿高字节
			element.setAttribute("Reg2GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2GHigh()));

			//寄存器2绿低字节
			element.setAttribute("Reg2GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2GLow()));

			//寄存器2蓝高字节
			element.setAttribute("Reg2BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2BHigh()));

			//寄存器2蓝低字节
			element.setAttribute("Reg2BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2BLow()));

			//寄存器3红高字节
			element.setAttribute("Reg3RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3RHigh()));

			//寄存器3红低字节
			element.setAttribute("Reg3RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3RLow()));

			//寄存器3绿高字节
			element.setAttribute("Reg3GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3GHigh()));

			//寄存器3绿低字节
			element.setAttribute("Reg3GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3GLow()));

			//寄存器3蓝高字节
			element.setAttribute("Reg3BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3BHigh()));

			//寄存器3蓝低字节
			element.setAttribute("Reg3BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3BLow()));

			
			//最新_MBI5155渐变过渡优化，在寄存器5153保留的基础上增加的字段
			if (CHIP_TYPE._MBI5155.ordinal() == sScanCard.getnChipType())
			{
				//MBI5155 第513/257个GCLK的低电平宽度 
				element.setAttribute("nDeltaT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDeltaT()));

				//MBI5155 第513/257个GCLK的高电平宽度 
				element.setAttribute("nDeltaT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDeltaF()));

				//MBI5155 第1个GCLK的高电平宽度 
				element.setAttribute("nDHT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDHT()));

				//MBI5155 第514/258个GCLK的高电平宽度
				element.setAttribute("nDG_H", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDG_H()));

				//MBI5155 第514/258个GCLK的低电平宽度 
				element.setAttribute("nDG_L", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDG_L()));
			}
		}
		else if (CHIP_TYPE._MBI5043.ordinal() == sScanCard.getnChipType())
		{
			//MBI5043 GCLK双沿采样	0：关闭， 1：开启
			element.setAttribute("bGCLKDoublesampling", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().isbGCLKDoublesampling() ? 1: 0));
			
			//MBI5043 PWM模式选择	0: 16bit，1: 10bit
			element.setAttribute("nPWMMode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().getnPWMMode()));
		}
		

		//网口优先设置
		element.setAttribute("netportpriority",
				String.valueOf(sScanCard.getnNetPortPriority()));

		//锁定优先网口
		element.setAttribute("locknetport",
				String.valueOf(sScanCard.isbLockNetPort() ? 1 : 0));
		
		return true;
		// *********************************************************************************************/
	}

	public static boolean SetScanCardLinkup(Element element,
			LinkTable ScanCardlinktable) {
		element.setAttribute("Len", String.valueOf(ScanCardlinktable.getnLen()));
		// //区宽
		int len = (int) ScanCardlinktable.getnLen();
		// byte[] temp = new byte[(int) ScanCardlinktable.getnLen()];
		// String temp1 = UtilFun.bytes2HexString( temp,temp.length ," ");
		// 此处修改临时数据temp1全为0，因为下面数据会陷入死循环，不停止
		// element.setAttribute("data",UtilFun.bytes2HexString(ScanCardlinktable.getUcLinkTable()));
		element.setAttribute("data", UtilFun.bytes2HexString(
				ScanCardlinktable.getUcLinkTable(), len, " "));
		return true;
	}

	public static boolean SetHUBLinkup(Element element, LinkTable Hublinktable) {
		element.setAttribute("Len", String.valueOf(Hublinktable.getnLen()));
		// 区宽
		int len = (int) Hublinktable.getnLen();
		// byte[] temp = new byte[(int) Hublinktable.getnLen()];
		// String temp1 = UtilFun.bytes2HexString( temp,temp.length ," ");
		element.setAttribute("data", UtilFun.bytes2HexString(
				Hublinktable.getUcLinkTable(), len, " "));
		// 下面数据会陷入死循环
		// element.setAttribute("data",UtilFun.bytes2HexString(Hublinktable.getUcLinkTable()));
		return true;
	}

	public static boolean SetSectionLinkup(Element element,
			LinkTable SectionLinkuptable) {
		element.setAttribute("Len",
				String.valueOf(SectionLinkuptable.getnLen()));
		// 区宽
		int len = (int) SectionLinkuptable.getnLen();
		// byte[] temp = new byte[(int) SectionLinkuptable.getnLen()];
		// String temp1 = UtilFun.bytes2HexString( temp,temp.length ," ");
		// element.setAttribute("data",temp1);
		element.setAttribute("data", UtilFun.bytes2HexString(
				SectionLinkuptable.getUcLinkTable(), len, " "));
		return true;
	}

	public static boolean SetMonitorItem(Element element,
			MonitorItem monitorItem) {

		// String string = null;
		// string = element.getAttribute("Temperature");
		// if (!string.equals("")) {
		// element.setAttribute("Temperature",monitorItem.isbTemperatureFlag()==true?String.valueOf(1):String.valueOf(0));
		// }
		element.setAttribute("Temperature",
				monitorItem.isbTemperatureFlag() == true ? String.valueOf(1)
						: String.valueOf(0));
		element.setAttribute("Humidity",
				monitorItem.isbHumidityFlag() == true ? String.valueOf(1)
						: String.valueOf(0));
		element.setAttribute(
				"Smoke",
				monitorItem.isbSmogFlag() == true ? String.valueOf(1) : String
						.valueOf(0));
		element.setAttribute(
				"LeftFan",
				monitorItem.getbFanStateFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute(
				"RightFan",
				monitorItem.getbFanStateFlag().get(1) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute("DotDetect",
				monitorItem.isbLEDPointDetect() == true ? String.valueOf(1)
						: String.valueOf(0));
		element.setAttribute("Capacity",
				monitorItem.isbCapacityFactorFlag() == true ? String.valueOf(1)
						: String.valueOf(0));

		element.setAttribute(
				"PowerVol1",
				monitorItem.getbPowerVolFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute(
				"PowerVol2",
				monitorItem.getbPowerVolFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute(
				"PowerVol3",
				monitorItem.getbPowerVolFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute(
				"PowerVol4",
				monitorItem.getbPowerVolFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		element.setAttribute(
				"PowerVol5",
				monitorItem.getbPowerVolFlag().get(0) == true ? String
						.valueOf(1) : String.valueOf(0));
		// 写箱体信息时有不同参数，写入不了2015-01-16
		/*
		 * 添加判断，存在参数和不存在参数的情况下如何执行, 添加判断后 ，
		 * 无论参数存在不存在都能正常合并数据2015/03/06zhangsj修改
		 */
		/*******************************************************************************/
		String fanRotation = null;
		fanRotation = element.getAttribute("FanRotation");
		if (!fanRotation.equals("")) {
			element.setAttribute(
					"FanRotation",
					monitorItem.isbFanRotationFlag() == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.isbFanRotationFlag();
		}
		String NetWebError = null;
		NetWebError = element.getAttribute("NetWebError");
		if (!NetWebError.equals("")) {
			element.setAttribute(
					"NetWebError",
					monitorItem.isbNetWebErrorFlag() == true ? String
							.valueOf(1) : String.valueOf(0));

		} else {
			monitorItem.isbNetWebErrorFlag();
		}
		String FanRotationd1 = null;
		FanRotationd1 = element.getAttribute("FanRotationd1");
		if (!FanRotationd1.equals("")) {
			element.setAttribute(
					"FanRotationd1",
					monitorItem.getbEightFanRotationFlag().get(0) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.getbEightFanRotationFlag();
		}
		String FanRotationd2 = null;
		FanRotationd2 = element.getAttribute("FanRotationd2");
		if (!FanRotationd2.equals("")) {
			element.setAttribute(
					"FanRotationd2",
					monitorItem.getbEightFanRotationFlag().get(1) == true ? String
							.valueOf(1) : String.valueOf(0));
		}
		String FanRotationd3 = null;
		FanRotationd3 = element.getAttribute("FanRotationd3");
		if (!fanRotation.equals("")) {
			element.setAttribute(
					"FanRotationd3",
					monitorItem.getbEightFanRotationFlag().get(2) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.isbFanRotationFlag();
		}
		String FanRotationd4 = null;
		FanRotationd4 = element.getAttribute("FanRotationd4");
		if (!FanRotationd4.equals("")) {
			element.setAttribute(
					"FanRotationd4",
					monitorItem.getbEightFanRotationFlag().get(3) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.isbFanRotationFlag();
		}
		String FanRotationd5 = null;
		FanRotationd5 = element.getAttribute("FanRotationd5");
		if (!FanRotationd5.equals("")) {
			element.setAttribute(
					"FanRotationd5",
					monitorItem.getbEightFanRotationFlag().get(4) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.isbFanRotationFlag();
		}
		String FanRotationd6 = null;
		FanRotationd6 = element.getAttribute("FanRotationd6");
		if (!FanRotationd6.equals("")) {
			element.setAttribute(
					"FanRotationd6",
					monitorItem.getbEightFanRotationFlag().get(5) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.getbEightFanRotationFlag();
		}
		String FanRotationd7 = null;
		FanRotationd7 = element.getAttribute("FanRotationd7");
		if (!FanRotationd7.equals("")) {
			element.setAttribute(
					"FanRotationd7",
					monitorItem.getbEightFanRotationFlag().get(6) == true ? String
							.valueOf(1) : String.valueOf(0));
		} else {
			monitorItem.getbEightFanRotationFlag();
		}
		String FanRotationd8 = null;
		FanRotationd8 = element.getAttribute("FanRotationd8");
		if (!FanRotationd8.equals("")) {
			element.setAttribute(
					"FanRotationd8",
					monitorItem.getbEightFanRotationFlag().get(7) == true ? String
							.valueOf(1) : String.valueOf(0));

		} else {
			monitorItem.getbEightFanRotationFlag();
		}
		/*******************************************************************************/
		return true;
	}

	public static boolean SetLoadedRegion(Element element, RECT rect) {
		element.setAttribute("top", String.valueOf(rect.getTop()));
		element.setAttribute("left", String.valueOf(rect.getLeft()));
		element.setAttribute("right", String.valueOf(rect.getRight()));
		element.setAttribute("bottom", String.valueOf(rect.getBottom()));
		return true;
	}

	/********************************************************************/

	// 通过系列ID删除箱体0528zhangsj
	public static boolean RemoveCabinetBySeriesId(int nSeriesId) {
		// List<String> listcbtSidList = new ArrayList<String>();
		try {
			// 得到箱体系列
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(filepath,
					sFileCabinetString));

			NodeList list1 = doc.getElementsByTagName("Cabinets");
			Element elementroot = null;
			if (list1.getLength() == 1) {
				elementroot = (Element) list1.item(0);
			}
			NodeList list = doc.getElementsByTagName("Cabinet");
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				NodeList list2 = element.getChildNodes();
				Node childNode = null;
				String strtemp = null;
				String cbseriesid = null;
				for (int j = 0; j < list2.getLength(); j++) {
					childNode = element.getChildNodes().item(j);
					if (childNode.getNodeName().equals("SeriesID")) {
						Element element3 = (Element) childNode;
						strtemp = element3.getTextContent();

						if (Integer.valueOf(strtemp) == nSeriesId) {
							// 根据箱体系列ID来删除对应的箱体节点
							// RemoveCabinet(strtemp);
							elementroot.removeChild(element);
						} else {
							continue;
						}
					}
				}
			}
			saveXML(doc, filepath + sFileCabinetString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	// 删除箱体系列名称0528zhangsj
	public static boolean RemoveCabinetSerise(String cSeriesname) {
		try {
			// 得到箱体系列
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(filepath,
					sFileCabinetsString));
			NodeList list1 = doc.getElementsByTagName("CabinetSerieses");
			Element elementroot = null;
			if (list1.getLength() == 1) {
				elementroot = (Element) list1.item(0);
			}
			NodeList list = doc.getElementsByTagName("CabinetSeries");
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				NodeList list2 = element.getChildNodes();
				Node childNode = null;
				String strtemp = null;
				for (int j = 0; j < list2.getLength(); j++) {
					// int cbseriesid=Integer.valueOf(cbtseriesID.get(j));
					childNode = element.getChildNodes().item(j);
					if (childNode.getNodeName().equals("name")) {
						Element element3 = (Element) childNode;
						strtemp = element3.getTextContent();
						if (strtemp.equals(cSeriesname)) {
							elementroot.removeChild(element);
						}
						break;
					}
				}

			}
			saveXML(doc, filepath + sFileCabinetsString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/********************************************************************/
	public static Display LoadCabinet() {
		ReadXmlToClass readXml = new ReadXmlToClass(sFileCabinetString);
		Display display = new Display();
		readXml.Assignment(display);
		return display;
	}

	public static Display LoadCabinetSerise() {
		ReadXmlToClass readXml = new ReadXmlToClass(sFileCabinetsString);
		Display display = new Display();
		readXml.Assignment(display);
		return display;
	}

	// 添加箱体到xml文件
	public static boolean AddXMLCabinet(CabinetInformation sCabinet) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH, sFileCabinetString));
			NodeList list = doc.getElementsByTagName("Cabinets");
			Element ele = null;
			if (list.getLength() == 1) {
				ele = (Element) list.item(0);
			}
			Element newelement = doc.createElement("Cabinet");
			Element newelement1 = doc.createElement("ID");
			newelement1.setTextContent(Integer.toString(sCabinet.getnID()));// //设置节点内容？？？？？？
			Element newelement2 = doc.createElement("SeriesID");
			newelement2.setTextContent(Integer.toString(sCabinet.getnSeriesID()));
			Element newelement3 = doc.createElement("Name");
			newelement3.setTextContent(sCabinet.getsName());
			Element newelement4 = doc.createElement("Photo");
			newelement4.setTextContent(sCabinet.getsPhoto());
			Element newelement5 = doc.createElement("SCACount");
			newelement5.setTextContent(Integer.toString(sCabinet.getnScanCardCount()));
			Element newelement6 = doc.createElement("inlinemode");
			newelement6.setTextContent(Integer.toString(sCabinet.getInlineMode().ordinal()));
			Element newelement7 = doc.createElement("ScanCardParaSynchro");
			newelement7.setTextContent(Integer.toString(sCabinet.isbScanCardParaSynchro() ? 1 : 0));
			Element newelement8 = doc.createElement("MonitorParaSynchro");
			newelement8.setTextContent(Integer.toString(sCabinet.isbMonitorParaSynchro() ? 1 : 0));

			Element elementscancardElements = doc.createElement("ScanCardAttachments");

			// SetScanCardAttachments(
			// elementscancardElements,sCabinet.getListScancardAttachment());
			// 设置扫描卡附件节点内容zhangsj
			SetScanCardAttachments(sCabinet, doc, elementscancardElements);
			// doc.creat
			Element elementLoadedRegion = doc.createElement("LoadedRegion");
			SetLoadedRegion(elementLoadedRegion, sCabinet.getRtRect());
			// /添加节点
			newelement.appendChild(newelement1);
			newelement.appendChild(newelement2);
			newelement.appendChild(newelement3);
			newelement.appendChild(newelement4);
			newelement.appendChild(newelement5);
			newelement.appendChild(newelement6);
			newelement.appendChild(newelement7);
			newelement.appendChild(newelement8);
			newelement.appendChild(elementscancardElements);
			newelement.appendChild(elementLoadedRegion);
			ele.appendChild(newelement);
			saveXML(doc, HomePageActivity.CONFIG_PATH + sFileCabinetString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void SetScanCardAttachments(CabinetInformation sCabinet,
			org.w3c.dom.Document doc, Element elementscancardElements) {
		for (ScanCardAttachment ls : sCabinet.getListScancardAttachment()) {
			Element elementScanCardAttachment = doc
					.createElement("ScanCardAttachment");
			elementScanCardAttachment.setAttribute("id",
					String.valueOf(ls.getnID()));
			elementScanCardAttachment.setAttribute("address",
					String.valueOf(ls.getnAddress()));

			Element elementScanCard = doc.createElement("ScanCard");
			SetScancardPram(elementScanCard, ls.getScancard()); //

			Element elementScanCardLinkup = doc.createElement("ScanCardLinkup");
			SetScanCardLinkup(elementScanCardLinkup, ls.getSlinktable());

			Element elementHUBLinkup = doc.createElement("HUBLinkup");
			SetHUBLinkup(elementHUBLinkup, ls.getHublinktable());
			Element elementSectionLinkup = null;
			if (ls.getsSectionlinktable() != null) {
				elementSectionLinkup = doc.createElement("SectionLinkup");
				SetSectionLinkup(elementSectionLinkup,
						ls.getsSectionlinktable());
			}
			Element elementMonitorCard = doc.createElement("MonitorCard");
			elementMonitorCard.setAttribute(
					"THBoard",
					ls.isbTHSBoard() == true ? String.valueOf(1) : String
							.valueOf(0));
			elementMonitorCard.setAttribute(
					"MultiFuncBoard",
					ls.isbMFCard() == true ? String.valueOf(1) : String
							.valueOf(0));
			elementMonitorCard.setAttribute(
					"PowerBoard",
					ls.isbPDCard() == true ? String.valueOf(1) : String
							.valueOf(0));
			elementMonitorCard.setAttribute(
					"DotDectorBoard",
					ls.isbPBPDCard() == true ? String.valueOf(1) : String
							.valueOf(0));

			Element elementMonitorItem = doc.createElement("MonitorItem");
			SetMonitorItem(elementMonitorItem, ls.getMonitoritem());

			Element elementLoadedRegion = doc.createElement("LoadedRegion");
			SetLoadedRegion(elementLoadedRegion, ls.getRtRect());
			// elementscancardElements.appendChild(elementScanCardAttachment);
			elementScanCardAttachment.appendChild(elementScanCard);
			elementScanCardAttachment.appendChild(elementScanCardLinkup);
			elementScanCardAttachment.appendChild(elementHUBLinkup);
			if (elementSectionLinkup != null) {
				elementScanCardAttachment.appendChild(elementSectionLinkup);
			}
			elementScanCardAttachment.appendChild(elementMonitorCard);
			elementScanCardAttachment.appendChild(elementMonitorItem);
			elementScanCardAttachment.appendChild(elementLoadedRegion);
			elementscancardElements.appendChild(elementScanCardAttachment);
		}
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

	// 添加箱体系列文件到xml文件中
	public static boolean WriteXMLCabinetSeries(CabinetSeries sCabinetseries) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(
					HomePageActivity.CONFIG_PATH, sFileCabinetsString));
			NodeList list = doc.getElementsByTagName("CabinetSerieses");
			Element ele = null;
			if (list.getLength() == 1) {
				ele = (Element) list.item(0);
			}
			Element newelement = doc.createElement("CabinetSeries");
			Element newelement1 = doc.createElement("ID");
			newelement1
					.setTextContent(Integer.toString(sCabinetseries.getID()));// //设置节点内容？？？？？？
			Element newelement2 = doc.createElement("ParentID");
			newelement2.setTextContent(Integer.toString(sCabinetseries
					.getParentID()));
			Element newelement3 = doc.createElement("name");
			newelement3.setTextContent(sCabinetseries.getName());
			// /添加节点
			newelement.appendChild(newelement1);
			newelement.appendChild(newelement2);
			newelement.appendChild(newelement3);
			ele.appendChild(newelement);
			saveXML(doc, HomePageActivity.CONFIG_PATH + sFileCabinetsString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	// 添加箱体系列文件到xml文件中 2015-07-13zsj
		public static boolean ModifyCabinetSeriesName(String SeriesName,String cBseriesName) {
				
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH, sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					
					Node node = list.item(i); //当前遍历的节点 CabinetSeries
					NodeList childNodes=node.getChildNodes();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node chNode=childNodes.item(j);
						if (chNode instanceof Element) {
							
							String filedName=chNode.getNodeName();
							String value=chNode.getTextContent();
							if (value.equals(SeriesName)) {
								chNode.setTextContent(cBseriesName);
							}
						}
					}
					
					saveXML(doc, HomePageActivity.CONFIG_PATH + sFileCabinetsString);
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			return false;
		}

   public static String getCbsNameBySeriesId(String sId)
   {
		try {

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,
						sFileCabinetsString));
				NodeList listSeries = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < listSeries.getLength(); i++) {
	
					Element element = (Element) listSeries.item(i);

					NodeList listChild = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < listChild.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().trim().equals("ID")) {
							
							String tId =  childNode.getTextContent().toString();
							
							if (!tId.equals(sId)) {	
								break;
							}

						}
						
						if (childNode.getNodeName().trim().equals("name")) {
							
							String sName =  childNode.getTextContent().toString();
		
							return sName;

						}
					}
					
				}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	
		return null;
	
	   
   }		
		
	public static boolean getCabinetSeriseByID(int CabinetSeriesID,
			CabinetSeries cSeries, int Filetype) {
		try {
			// String filepath = null;
			if (Filetype == 0) {
				// filepath = HomePageActivity.CONFIG_PATH ;
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(filepath,
						sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().trim().equals("ID")) {
							cSeries.setID(Integer.parseInt(childNode.getTextContent().toString()));
							if (cSeries.toString().equals(childNode.getTextContent().toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);
							/**
							 * 此处需要解析的箱体文件是：Cabinet.cbt和CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("子节点ID解析");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cSeries.setParentID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("父节点开始解析");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cSeries.setName(childNode.getTextContent()
										.toString());
								System.out.println("箱体系列名称解析");
							}
						}
						break;
					}
				}
			} else if (Filetype == 1) {
				fileUsbpath = TraverseDictionary.GetUDiskDir();
				if (fileUsbpath == null) {
					return true;
				}
				fileUsbpath = GetUsbCabintSeriesFileName();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(fileUsbpath,
						sUSBFileCabinetSeriesName));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					int sign = 0;
					Element element = (Element) list.item(i);
					// element.getn
					NodeList list1 = element.getChildNodes();
					Node childNode = null;
					for (int j = 0; j < list1.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().trim().equals("ID")) {
							cSeries.setID(Integer.parseInt(childNode.getTextContent().toString()));
							if (cSeries.toString().equals(childNode.getTextContent().toString())) {
								sign = 1;
								break;
							}
						}
					}
					if (sign == 1) {
						for (int i1 = 0; i1 < element.getChildNodes()
								.getLength(); i1++) {
							childNode = element.getChildNodes().item(i1);
							/**
							 * 此处需要解析的箱体文件是：Cabinet.cbt和CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("子节点ID解析");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cSeries.setParentID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("父节点开始解析");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cSeries.setName(childNode.getTextContent()
										.toString());
								System.out.println("箱体系列名称解析");
							}
						}
						break;
					}
				}
			}
			// saveXML(doc,filepath +sFileCabinetsString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	//修改箱体名称，保存 zsj 2015-07-14 add 
	public static void ModifyCabinetName(String cabinetName, String text) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH, sFileCabinetString));
				NodeList list1 = doc.getElementsByTagName("Cabinets");
				NodeList list = doc.getElementsByTagName("Cabinet");
				for (int i = 0; i < list.getLength(); i++) {
					Element element = (Element) list.item(i);
					NodeList list2 = element.getChildNodes();
					Node childNode = null;
					String strtemp = null;
					for (int j = 0; j < list2.getLength(); j++) {
						childNode = element.getChildNodes().item(j);
						if (childNode.getNodeName().equals("Name")) {
							
					
							Element element3 = (Element) childNode;
							
							String strNode = element3.getTextContent();
							if (strNode.equals(cabinetName)){
								element3.setTextContent(text);		
								saveXML(doc, HomePageActivity.CONFIG_PATH + sFileCabinetString);
								return;
							}
							
								
							
						}
					}
		
				}
					
	  } catch (Exception ex) {
		ex.printStackTrace();
	}
				
	}
}
