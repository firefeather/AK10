/*
 * �ļ��� DataAccessColourTem.java
 * ���������б�com.szaoto.ak10.dataaccess
 * �汾��Ϣ���汾��
 * ��������2014��1��17������10:50:54
 * ��Ȩ���� liangdb-szaoto
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
 * ����DataAccessCabinetLibrary
 * ���� zhangjj
 * ��Ҫ���� 
 * ��������2014��9��25��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class DataAccessCabinetLibrary extends DataAccessBase {
	static String fileUsbpath;// ���İ�ɰ����·��
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
	// Filetype =0 ��ʾ���أ�Filetype=1��ʾU�� //�õ�����ϵ������
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

	// ��ȡU������ϵ������
	public static String GetUsbCabintSeriesFileName() {

		TraverseDictionary TD = new TraverseDictionary();
		TD.GetFiles(fileUsbpath, ".cbt", true);
		List<String> listNamecbtSeries = TD.getLstFile();
		for (int i = 0; i < listNamecbtSeries.size(); i++) {
			sUSBFileCabinetSeriesName = listNamecbtSeries.get(i);
			// �˴�����ϵ������ֻȡ��һ������֤U��ֻ��һ�� �ļ�����Ҫͬ�����,getCabinetSerisNameҪ��������
		}
		return fileUsbpath;
	}

	public static List<String> getCabinetSeriseNames(int Filetype)
	// Filetype =0 ��ʾ���أ�Filetype=1��ʾU�� //�õ�����ϵ������
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
	// Filetype =0 ��ʾ���أ�Filetype=1��ʾU�� //�õ�����ϵ������
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
	// Filetype =0 ��ʾ���أ�Filetype=1��ʾU�� //�õ�����ϵ������
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

	// ��ȡU���µ�.cbt��β�������ļ�
	public static String GetUsbCabinetFile() {
		/*
		 * String filepath; filepath = "/mnt/usb/" ;//�°�·��,�ϰ�/mnt/usb/
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
			   //��cbs��������ϵ�У�û�е����ӽ�ȥ   
			 Ak10Application.gArrCabSerieStrings.add(strKey);
		}
	   
	   return Ak10Application.gArrCabSerieStrings;
   }
	
	
	
	public static boolean getCabinetSeriseByname(String cabinetserisename,
			CabinetSeries cabinetSeries, int Filetype)
	// Filetype =0 ��ʾ���أ�Filetype=1��ʾUSB
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
							 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cabinetSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("�ӽڵ�ID����");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cabinetSeries.setParentID(Integer
										.parseInt(childNode.getTextContent()
												.toString()));
								System.out.println("���ڵ㿪ʼ����");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cabinetSeries.setName(childNode
										.getTextContent().toString());
								System.out.println("����ϵ�����ƽ���");
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
							 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cabinetSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("�ӽڵ�ID����");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cabinetSeries.setParentID(Integer
										.parseInt(childNode.getTextContent()
												.toString()));
								System.out.println("���ڵ㿪ʼ����");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cabinetSeries.setName(childNode
										.getTextContent().toString());
								System.out.println("����ϵ�����ƽ���");
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
				fileUsbpath = GetUsbCabinetFile(); // �õ�U��.cbt��׺���������ļ�����
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
							// ��ȡɨ�迨 ���� ��Ϣ �ڴ˴� ��ȡ ����
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
					// ��ȡɨ�迨 ���� ��Ϣ �ڴ˴� ��ȡ ����
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
						// scanCardAttachment.setbTHSBoard(Boolean.parseBoolean(childeleElement1.getAttribute("THBoard")));//�¡�ʪ�ȴ�������
						scanCardAttachment.setbTHSBoard(Short
								.parseShort(childeleElement1
										.getAttribute("THBoard")) == 0 ? false
								: true);// �¡�ʪ�ȴ�������
						scanCardAttachment
								.setbMFCard(Short.parseShort(childeleElement1
										.getAttribute("MultiFuncBoard")) == 0 ? false
										: true);// �๦�ܿ�
						scanCardAttachment
								.setbPDCard(Short.parseShort(childeleElement1
										.getAttribute("PowerBoard")) == 0 ? false
										: true);// ���ʼ�⿨
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

	// ��ȡɨ�迨��Ϣ
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
		// ONE_SCAN_CARD_HEIGHT 2 96 ����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
		// ��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
		scancard.setnScanCardHeight(Short.parseShort(element
				.getAttribute("ONE_SCAN_CARD_HEIGHT")));
		// mod_width 1 32 ģ����ȣ�ȡֵ��Χ1-64
		scancard.setnModuleWidth(Short.parseShort(element
				.getAttribute("mod_width")));
		// mod_height 1 32 ģ��߶ȣ�ȡֵ��Χ1-64
		scancard.setnModuleHeight(Short.parseShort(element
				.getAttribute("mod_height")));
		// 1 3 ģ������ ģ������ = ģ��߶�/ÿ������
		scancard.setnModuleSectionNumber(Short.parseShort(element
				.getAttribute("mod_section_number")));
		// 1 4 ģ����������ģ�������� = ɨ�迨����/ģ�����
		scancard.setnModuleHorNum(Short.parseShort(element
				.getAttribute("mod_hor_number")));
		// 1 3 ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
		scancard.setnModuleVerNum(Short.parseShort(element
				.getAttribute("mod_ver_number")));
		// SCAN_CARD_SECTION_NUM 1 6 ɨ�迨���������������Ϊ16
		scancard.setnScanCardSectionNumber(Short.parseShort(element
				.getAttribute("SCAN_CARD_SECTION_NUM")));
		// SCAN_CARD_SECTION_ROW_NUM 1 16 ɨ�迨ÿ���������������Ϊ16��
		scancard.setnScanCardSectionRowNumber(Short.parseShort(element
				.getAttribute("SCAN_CARD_SECTION_ROW_NUM")));
		// SCAN_COLOR_DEPTH 1 14 ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
		scancard.setnScanColorDepth(Short.parseShort(element
				.getAttribute("SCAN_COLOR_DEPTH")));
		// GRAY_LEVEL ɨ�迨�Ҷȼ���
		scancard.setnGrayLedvel(Short.parseShort(element
				.getAttribute("GRAY_LEVEL")));
		// origin_color_bit ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
		scancard.setnOrginColorBit(Short.parseShort(element
				.getAttribute("origin_color_bit")));
		// SCAN_MODE 1 4 ɨ���ģʽ��ȡ 1-2-4-8-16
		scancard.setnScanMode(Short.parseShort(element
				.getAttribute("SCAN_MODE")));
		// DOT_CORRECTION_EN 1 1 ����У��ʹ�ܣ�ȡ0��1
		scancard.setbEmendBrightness(Short.parseShort(element
				.getAttribute("DOT_CORRECTION_EN")) == 0 ? false : true);
		// SCAN_GCLK_FREQUENCY 1 12.5 ɨ��ʱ��Ƶ�ʣ����30Mhz
		scancard.setfScanClkFrequency(Float.parseFloat(element
				.getAttribute("SCAN_GCLK_FREQUENCY")));
		// ZONE_DCLK_NUM 2 ÿ����λʱ������256*16
		scancard.setnZoneClkNum(Integer.parseInt(element
				.getAttribute("ZONE_DCLK_NUM")));
		// 1 0-100 ռ�ձ� ɨ��ʱ�ӵ�ռ�ձ�
		scancard.setnDutyCycle(Short.parseShort(element
				.getAttribute("duty_cycle_low_value")));
		// PWM_SCAN_GCLK_FREQUENCY 1 12.5 PWMʱ��ʱ��Ƶ�ʣ����30Mhz
		scancard.setfPWMScanClkFrequency(Float.parseFloat(element
				.getAttribute("PWM_SCAN_GCLK_FREQUENCY")));
		// 1 0-100 PWMʱ��ռ�ձȿɵ��ȼ�
		scancard.setnPWMDutyCycle(Short.parseShort(element
				.getAttribute("pwm_duty_cycle_low_value")));
		// CLR_CLK_NUM 1 4 1~255 ����ʱ����(������ʱ��)
		scancard.setnOeClkNumber(Long.parseLong(element
				.getAttribute("CLR_CLK_NUM")));
		// ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
		scancard.setnRefreshRate(Integer.parseInt(element
				.getAttribute("refresh_rate")));
		// REF_FREQ_MIN ˢ������Сֵ
		scancard.setnRefreshRateMin(Integer.parseInt(element
				.getAttribute("refresh_rate_min")));
		// REF_FREQ_MAX ˢ�������ֵ
		scancard.setnRefreshRateMax(Integer.parseInt(element
				.getAttribute("refresh_rate_max")));
		// CONFIG_IC_TIME ��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1ms--30s
		scancard.setnConfigICTime(Short.parseShort(element
				.getAttribute("config_ic_time")));
		// dat_freq_num ��֡Ƶ�ʼ�����
		scancard.setnDatFreqNum(Integer.parseInt(element
				.getAttribute("dat_freq_num")));
		// OE_DELAY_VALUE 0x01 �����ӳ�ʱ����������������Ĭ��Ϊ0x01������1������
		scancard.setnOeDelayValue(Short.parseShort(element
				.getAttribute("OE_DELAY_VALUE")));
		// SYN_REFRESH_EN ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
		scancard.setbSyncRefresh(Short.parseShort(element
				.getAttribute("SYN_REFRESH_EN")) == 0 ? false : true);
		// VIRTUAL_DISP_EN ������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
		scancard.setbVirtvalDisp(Short.parseShort(element
				.getAttribute("VIRTUAL_DISP_EN")) == 0 ? false : true);
		// FREQ_DIVISION_COEF 0x7
		// 150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
		scancard.setnFreqDivisionCoeff(Short.parseShort(element
				.getAttribute("FREQ_DIVISION_COEF")));
		// PWM_FREQ_DIVISION_COEF 0x7
		// 125Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
		scancard.setnPWMFreqDivisionCoeff(Short.parseShort(element
				.getAttribute("PWM_FREQ_DIVISION_COEF")));
		// DATA_OUTPUT_REVERSE �����������������ߺ�ɨ���� 0x00 ����������Ĭ��Ϊ0x0 0x0 ��ʹ�� 0x1 ʹ��
		scancard.setbDataOutUpReverse(Short.parseShort(element
				.getAttribute("DATA_OUTPUT_REVERSE")) == 0 ? false : true);
		// SCAN_OUTPUT_REVERSE 0x00 ɨ��������Ĭ��Ϊ0x0 0x0 ��ʹ�� 0x1 ʹ��
		scancard.setbScanOutUpReverse(Short.parseShort(element
				.getAttribute("SCAN_OUTPUT_REVERSE")) == 0 ? false : true);
		// DCB_LINE_CLK_EN 0x00 ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�; 0x0 ��ʹ�� 0x1 2�� 0x2 3�� 0x3 4��
		scancard.setnDCBlineClkEn(Short.parseShort(element
				.getAttribute("DCB_LINE_CLK_EN")));
		// NO_SIGNAL_DISP ���ź���ʾ��Ĭ��Ϊ0 0x0��������0x1��������档0x2��ͼƬ
		scancard.setnNoSingleDisp(Short.parseShort(element
				.getAttribute("NO_SIGNAL_DISP")));

		// DATA_INPUT_DIR 0x32 ���ݷ��򣺣�����ʾ�����濴��
		// Ĭ��Ϊ���ҵ���0x1 0x0 �������� 0x1 �������� 0x2 �������� 0x3 ��������
		scancard.setnDataInputDir(Short.parseShort(element
				.getAttribute("DATA_INPUT_DIR")));

		// ROW_DECODE_MODE �����뷽ʽ��Ĭ��0x2
		// 0x0 ��̬������ 0x6 164����
		// 0x1 ������оƬ��ֱ�������й� 0x7 192����
		// 0x2 138���� 0x8 193����
		// 0x3 139���� 0x9 595����
		// 0x4 145�����138˫O 0xA 4096����
		// 0x5 154���� 0xB
		scancard.setnRowDecodeMOde(Short.parseShort(element
				.getAttribute("ROW_DECODE_MODE")));
		// DATA_LINE_TYPE 8 0x14 7--0 �������ʹ��ࣺĬ��Ϊ0x00��
		scancard.setnDataLineTypeRange(Short.parseShort(element
				.getAttribute("DATA_LINE_TYPE_RANGE")));
		// DATA_LINE_TYPE 8 0x14 7--0 �������ͣ�Ĭ��Ϊ0x00��
		// 0x00-0x1F �������ֿ�,
		// 0x20-0x18 ��������һ��ɫ1�㴮��
		// 0x30-0x38 ��������һ��ɫ8�㴮��
		// 0x40-0x48 ��������һ��ɫ16�㴮��
		// 0x50-0x6F ��������һ��ɫ����
		scancard.setnDataLineType(Short.parseShort(element
				.getAttribute("DATA_LINE_TYPE")));

		// DATA_LINE_CTRL 8 0x00 �����߿���,
		// ����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
		scancard.setnDataLineCtrl(Short.parseShort(element
				.getAttribute("DATA_LINE_CTRL")));

		// FIELD_NUM �ܳ��������Ϊ136��������1������
		scancard.setnFieldNum(Short.parseShort(element
				.getAttribute("FIELD_NUM")));

		// HALF_FIELD_NUM �볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
		scancard.setnHalfFieldNumber(Short.parseShort(element
				.getAttribute("HALF_FIELD_NUM")));

		// FULL_FIELD_NUM ȫ���������Ϊ128������1����
		scancard.setnFullFieldNumber(Short.parseShort(element
				.getAttribute("FULL_FIELD_NUM")));

		// ��ʼ��
		scancard.setnStartField(Short.parseShort(element
				.getAttribute("start_field")));

		// ��ֹ��
		scancard.setnEndField(Short.parseShort(element
				.getAttribute("end_field")));

		// DATA_POLARITY 4 ���ݼ��ԣ�Ĭ��Ϊ0x0 0x0 �ߵ�ƽ���� 0x1 �͵�ƽ���� 0x2-0xF ����14�������Ԥ��
		scancard.setnDataPolarity(Short.parseShort(element
				.getAttribute("DATA_POLARITY")));

		// OE_POLARITY 0x1F OE���ԣ�Ĭ��Ϊ0x0 0x0 ����Ч 0x1 ����Ч
		scancard.setnOePolarity(Short.parseShort(element
				.getAttribute("OE_POLARITY")));

		// EMPTY_INSERT_EN �յ����ʹ�ܣ���ÿ���ٵ������ٿյ�. Ĭ��0x0����ʹ��
		scancard.setbEmptyInsertEnable(Short.parseShort(element
				.getAttribute("EMPTY_INSERT_EN")) == 0 ? false : true);

		// INSERT_MODE ����յ㷽ʽ��ǰ���뻹�Ǻ���롣 1��ǰ����յ㡣0�������յ�
		scancard.setnInsertMode(Short.parseShort(element
				.getAttribute("INSERT_MODE")));

		// EMPTY_DOT_NUM ����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1������
		scancard.setnEmptyDotNum(Short.parseShort(element
				.getAttribute("EMPTY_DOT_NUM")));

		// REAL_DOT_NUM 15--0 ÿ���ٵ����յ㣬����1������
		scancard.setnRealDotNum(Short.parseShort(element
				.getAttribute("REAL_DOT_NUM")));

		// ˫�����
		scancard.setbDualOutput(Short.parseShort(element
				.getAttribute("dual_out_put")) == 0 ? false : true);

		// ���������Ų���ʽ
		scancard.setnVirTualArray(Short.parseShort(element
				.getAttribute("virTual_array")));

		// �ư�оƬ
		scancard.setnChipType(Integer.parseInt(element
				.getAttribute("chip_type")));

		// ref_doule_value ˢ���ʱ����ı�����
		scancard.setnRefreshDoubleValue(Short.parseShort(element
				.getAttribute("ref_doule_value")));

		// zhe_rdwr_mode �۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
		// 0������8��д��1���������ж�д
		scancard.setnZheRdwrMode(Short.parseShort(element
				.getAttribute("zhe_rdwr_mode")));

		// ��ʾ������ 0-ȫ��ʵ���أ�1-ȫ������
		scancard.setnScreenType(Short.parseShort(element
				.getAttribute("screen_type")));

		// ����У������, 0-������1-��ɫ
		scancard.setnDotCorrectTye(Short.parseShort(element
				.getAttribute("dot_correct_tye")));

		// ������ʾ�仯
		scancard.setbVirtualChangeFlag(false);
		// sScanCard.bVirtualChangeFlag = false;
		// ԭ��������ʾ
		scancard.setbVirtualPrime(scancard.isbVirtvalDisp());
		// sScanCard.bVirtualPrime = sScanCard.bVirtvalDisp;

		// ����Ч��/���Խ���
		scancard.setbTest(Short.parseShort(element.getAttribute("test_start")) == 0 ? false
				: true);

		// //2013-3-5
		// if ( _GENERAL == scancard.getnChipType())
		// {
		// pAtt = pAtt->Next();
		// cTmp = pAtt->Value();
		// sScanCard.nSecondHighLevel = Integer.parseInt(cTmp);
		// }

		// ������Ч��
		scancard.setfBrightnessEfficent(Float.parseFloat(element
				.getAttribute("brightness_efficent")));

		// ��СOE����
		scancard.setnMinOEWidth(Short.parseShort(element
				.getAttribute("min_oe_width")));

		// ��ɫ��ȱ仯��ʶ
		scancard.setbScanColorDepthChangeFlag(false);
		// ԭ����ɫ���
		scancard.setnScanColorDepthPrime(scancard.getnScanColorDepthPrime());

		// ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
		scancard.setbDotOpenDetection(Short.parseShort(element
				.getAttribute("dot_open_detection")) == 0 ? false : true);

		// PWM���ģʽ MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-��ɢģʽ��0-��ͨģʽ
		scancard.setnPWMOutputMode(Short.parseShort(element
				.getAttribute("pwm_output_mode")));

		// ˢ���ʱ�����PWM��̬ɨ������Ч
		scancard.setbMultiRefreshUnderStaticScan(Short.parseShort(element
				.getAttribute("multi_refresh_under_static_scan")) == 0 ? false
				: true);

		// ONE_SCAN_CARD_WIDTH_REAL
		// ��ɨ�迨ʵ�ʿ��ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		// ONE_SCAN_CARD_HEIGHT_REAL
		// ��ɨ�迨ʵ�ʸ߶ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		cTmp = element.getAttribute("ONE_SCAN_CARD_WIDTH_REAL");
		if ("" == cTmp) {
			scancard.setnScanCardWidthReal(scancard.getnScanCardWidth());
			scancard.setnScanCardHeightReal(scancard.getnScanCardHeight());
		} else {
			scancard.setnScanCardWidthReal(Short.parseShort(cTmp));
			scancard.setnScanCardHeightReal(Short.parseShort(element
					.getAttribute("ONE_SCAN_CARD_HEIGHT_REAL")));
		}

		// ����ʹ�� �뱾���ļ�������ͬ zhangsj 2015-01-16
		/*
		 * �ڿ������������ �����֣��е����岻�����������
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
			// ����
			cTmp = element.getAttribute("section_width");
			scancard.setnSectionWidth(Short.parseShort(cTmp));

			// ����������
			cTmp = element.getAttribute("section_hor_number");
			scancard.setnSectionHorNum(Short.parseShort(cTmp));

			// ������
			cTmp = element.getAttribute("Card_zone_width");
			// cTmp = element.getAttribute("Card_zone_width");
			// 20150513���ԣ�ͬ������ȱ�ٸò������壬��Ҫ���ж�zhangsj
			/***********************************************/
			if ("" != cTmp) {

				short short1 = (0 == Short.parseShort(cTmp)) ? scancard
						.getnSectionWidth() : Short.parseShort(cTmp);
				scancard.setnCard_zone_width(short1);
				// ��������
				cTmp = element.getAttribute("Card_zone_Num");
				short1 = (0 == Short.parseShort(cTmp)) ? 1 : Short
						.parseShort(cTmp);
				scancard.setnCard_zone_Num(short1);
			} else {
				// ��������
				scancard.setnCard_zone_width((short) 0); // 150513
				scancard.setnCard_zone_Num((short) 1); // 150513
			}
			/***********************************************/
			/*
			 * Short short1 = (0 == Short.parseShort(cTmp)) ?
			 * scancard.getnSectionWidth() : Short.parseShort(cTmp);
			 * scancard.setnCard_zone_width(short1); //��������
			 * 
			 * cTmp = element.getAttribute("Card_zone_Num");
			 * 
			 * short1 = (0 == Short.parseShort(cTmp)) ? 1 :
			 * Short.parseShort(cTmp); scancard.setnCard_zone_Num(short1);
			 */
			// �Ҷ���ǿλ��
			cTmp = element.getAttribute("gray_enhance_bit");
			scancard.setnGrayEnhance(Short.parseShort(cTmp));

			// �Ҷ���ǿ��ʽ
			cTmp = element.getAttribute("gray_enhance_mode");
			scancard.setnGrayEnhanceMode(Short.parseShort(cTmp));

			// ����ָʾ��
			cTmp = element.getAttribute("open_cabinet_lamp");
			scancard.setbOpenCabinetLamp(Short.parseShort(cTmp) == 1 ? true
					: false);// Boolean.parseBoolean(cTmp)
		} else {
			scancard.setbExtendedEnable(false);
			// ����
			scancard.setnSectionWidth(scancard.getnModuleWidth());
			// ����������
			scancard.setnSectionHorNum((short) 1);

			// �Ҷ���ǿλ��
			scancard.setnGrayEnhance((short) 1);
			// �Ҷ���ǿ��ʽ
			scancard.setnGrayEnhanceMode((short) 3);
			// ����ָʾ��
			scancard.setbOpenCabinetLamp(true);
		}

		// �ⲿ����ʹ��
		cTmp = element.getAttribute("extend_enableEx");
		if ("" != cTmp) {
			scancard.setbExtendedEnableEx(Short.parseShort(cTmp) == 1 ? true
					: false);
			// ������
			cTmp = element.getAttribute("Card_zone_width");
			// 20150513���ԣ�ͬ������ȱ�ٸò������壬��Ҫ���ж�zhangsj
			/***********************************************/
			if ("" != cTmp) {

				short short1 = (0 == Short.parseShort(cTmp)) ? scancard
						.getnSectionWidth() : Short.parseShort(cTmp);
				scancard.setnCard_zone_width(short1);
				// ��������
				cTmp = element.getAttribute("Card_zone_Num");
				short1 = (0 == Short.parseShort(cTmp)) ? 1 : Short
						.parseShort(cTmp);
				scancard.setnCard_zone_Num(short1);
			} else {
				// ��������
				scancard.setnCard_zone_width((short) 0); // 150513
				scancard.setnCard_zone_Num((short) 1); // 150513
			}
			/***********************************************/
		} else {
			scancard.setbExtendedEnableEx(false);

			// ��������
			scancard.setnCard_zone_Num((short) 1);

			// ������
			scancard.setnCard_zone_width((short) (scancard.getnScanCardWidth() / (scancard
					.getnCard_zone_Num() * scancard.getnSectionHorNum())));
			;
		}

		// ����ʹ��
		// Gammaλ����8��10,12λ��Ĭ��8λ
		cTmp = element.getAttribute("gamma_bit");
		if ("" == cTmp) {
			scancard.setnCustomGamam(8);
		} else {
			scancard.setnCustomGamam(Integer.parseInt(cTmp));
		}

		// оƬԤ��繦��, 0 - �� 1�� �ر�,Ĭ�ϴ�
		cTmp = element.getAttribute("chip_precharge");
		if ("" == cTmp) {
			scancard.setbChipPrecharge(false);
			scancard.setnGClkDelay((short) 10);
			scancard.setnGClkDelay_B((short) 10);
			scancard.setnGClkDelay_G((short) 10);
		} else {
			scancard.setbChipPrecharge(Short.parseShort(cTmp) == 1 ? true
					: false);

			// RGB��ɫ�ֱ����GCLK���ӳ�ʱ�� ʹ�ܱ�ʶ
			cTmp = element.getAttribute("gclk_delay_ctrl_by_rgb");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByRGBEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}

			// Rɫ����GCLK���ӳ�ʱ�� ʹ�ܱ�ʶ
			cTmp = element.getAttribute("gclk_delay_ctrl_by_r");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByREnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}
			// Gɫ����GCLK���ӳ�ʱ�� ʹ�ܱ�ʶ
			cTmp = element.getAttribute("gclk_delay_ctrl_by_g");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByGEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
				// = Integer.parseInt(cTmp) ? true : false;
			}
			// Bɫ����GCLK���ӳ�ʱ�� ʹ�ܱ�ʶ
			cTmp = element.getAttribute("gclk_delay_ctrl_by_b");
			if (!cTmp.equals("")) {
				scancard.setbGClkCtrlByBEnable(Short.parseShort(cTmp) == 1 ? true
						: false);
			}

			// GCLK���ӳ�ʱ��������ɫ�ֿ�ʱΪR)
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

		
		//�Ĵ�����Ϣ
		if (CHIP_TYPE._TLC5958.ordinal() == scancard.getnChipType() || 
			CHIP_TYPE._MBI5152.ordinal() == scancard.getnChipType() ||
			CHIP_TYPE._MBI5153.ordinal() == scancard.getnChipType() )
		{
			//TLC5958
			//reg1
			//ȫ�����ȵ���
			cTmp = element.getAttribute("bright");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnBright(4);
			} else {
				scancard.getnDrive_ic_reg().setnBright(Integer.parseInt(cTmp));
			}

			//��ɫ�ͻ���ǿ
			cTmp = element.getAttribute("lgse_r");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_R(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_R(Integer.parseInt(cTmp));
			}

			//��ɫ�ͻ���ǿ
			cTmp = element.getAttribute("lgse_g");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_G(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_G(Integer.parseInt(cTmp));
			}

			//��ɫ�ͻ���ǿ
			cTmp = element.getAttribute("lgse_b");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLgse_B(0);
			}else{
				scancard.getnDrive_ic_reg().setnLgse_B(Integer.parseInt(cTmp));
			}

			//���ͨ���ӳ�ʹ��
			cTmp = element.getAttribute("gdly_enable");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGdly_Enable(1);
			}else{
				scancard.getnDrive_ic_reg().setnGdly_Enable(Integer.parseInt(cTmp));
			}

			//���������ӳ�
			cTmp = element.getAttribute("td_delay");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnTD_Delay(1);
			}else{
				scancard.getnDrive_ic_reg().setnTD_Delay(Integer.parseInt(cTmp));
			}

			//��·����ѹ�趨
			cTmp = element.getAttribute("lodvth");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnLodvth(1);
			}else{
				scancard.getnDrive_ic_reg().setnLodvth(Integer.parseInt(cTmp));
			}

			//reg2
			//ȫ�ֵͻ���ǿ
			cTmp = element.getAttribute("global_lgse");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGlobal_Lgse(0);
			} else {
				scancard.getnDrive_ic_reg().setnGlobal_Lgse(Integer.parseInt(cTmp));
			}

			//��ɢģʽ
			cTmp = element.getAttribute("PVM_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPVM_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnPVM_Mode(Integer.parseInt(cTmp));
			}

			//��ɫEMI����
			cTmp = element.getAttribute("EMI_r");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_R(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_R(Integer.parseInt(cTmp));
			}

			//��ɫEMI����
			cTmp = element.getAttribute("EMI_g");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_G(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_G(Integer.parseInt(cTmp));
			}

			//��ɫEMI����
			cTmp = element.getAttribute("EMI_b");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEMI_B(0);
			} else {
				scancard.getnDrive_ic_reg().setnEMI_B(Integer.parseInt(cTmp));
			}

			//Ԥ���ģʽ
			cTmp = element.getAttribute("pre_charge");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPre_Charge(0);
			} else {
				scancard.getnDrive_ic_reg().setnPre_Charge(Integer.parseInt(cTmp));
			}

			//MBI5152/MBI5153
			//reg1
			//Ԥ���ģʽ
			cTmp = element.getAttribute("pre_charge1");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPre_Charge1(0);
			} else {
				scancard.getnDrive_ic_reg().setnPre_Charge1(Integer.parseInt(cTmp));
			}

			//PWM����ģʽ
			cTmp = element.getAttribute("pwm_count_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnPwm_Count_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnPwm_Count_Mode(Integer.parseInt(cTmp));
			}

			//�ҽ�ģʽ
			cTmp = element.getAttribute("gray_mode");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnGray_Mode(0);
			} else {
				scancard.getnDrive_ic_reg().setnGray_Mode(Integer.parseInt(cTmp));
			}

			//GCLK��Ƶ
			cTmp = element.getAttribute("enable_gclk");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnEnable_GCLK(0);
			} else {
				scancard.getnDrive_ic_reg().setnEnable_GCLK(Integer.parseInt(cTmp));
			}

			//˫��ˢ����
			cTmp = element.getAttribute("double_refreserate");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnDouble_RefreseRate(0);
			} else {
				scancard.getnDrive_ic_reg().setnDouble_RefreseRate(Integer.parseInt(cTmp));
			}

			//��·����ѹ
			cTmp = element.getAttribute("voltage");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnVoltage(0);
			} else {
				scancard.getnDrive_ic_reg().setnVoltage(Integer.parseInt(cTmp));
			}

			//������ICʶ��
			cTmp = element.getAttribute("icrecognition");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnIC_Recognition(0);
			} else {
				scancard.getnDrive_ic_reg().setnIC_Recognition(Integer.parseInt(cTmp));
			}

			//����ƫ�����ڣ���ɫ��
			cTmp = element.getAttribute("adjust_red");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Red(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Red(Integer.parseInt(cTmp));
			}

			//����ƫ�����ڣ���ɫ��
			cTmp = element.getAttribute("adjust_green");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Green(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Green(Integer.parseInt(cTmp));
			}

			//����ƫ�����ڣ���ɫ��
			cTmp = element.getAttribute("adjust_blue");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().setnAdjust_Blue(0);
			} else {
				scancard.getnDrive_ic_reg().setnAdjust_Blue(Integer.parseInt(cTmp));
			}

			//����ģʽ�ߵ�ƽ������
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
			//R_REG1��MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵΪ0x9F2B
			cTmp = element.getAttribute("Reg1RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RHigh(Integer.parseInt(cTmp));//�Ĵ���1����ֽ�
			}
			cTmp = element.getAttribute("Reg1RLow");
			if (cTmp.equals("")) {
				if (CHIP_TYPE._MBI5155.ordinal() == scancard.getnChipType())
				{
					//�ҽ�cfg1[7] 16��14��Ĭ��14bit
					scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(0xAB); //�Ĵ���1����ֽ�
				} 
				else//_MBI5153_E == m_nChipType
				{
					//�ҽ�cfg1[7] 14��13��Ĭ��14bit
					scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(0x2B); //�Ĵ���1����ֽ�
				}
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1RLow(Integer.parseInt(cTmp));//�Ĵ���1����ֽ�
			}

			//R_REG1��MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵ0xDF2B
			cTmp = element.getAttribute("Reg1GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GHigh(Integer.parseInt(cTmp));//�Ĵ���1�̸��ֽ�
			}
			cTmp = element.getAttribute("Reg1GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GLow(0x2B);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1GLow(Integer.parseInt(cTmp));//�Ĵ���1�̵��ֽ�
			}

			//R_REG1:MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵ0xDF2B
			cTmp = element.getAttribute("Reg1BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BHigh(0xFF);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BHigh(Integer.parseInt(cTmp));//�Ĵ���1�����ֽ�
			}
			cTmp = element.getAttribute("Reg1BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BLow(0x2B);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg1BLow(Integer.parseInt(cTmp));//�Ĵ���1�����ֽ�
			}
			

			//R_REG2��MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵΪ0x4600
			cTmp = element.getAttribute("Reg2RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RHigh(0x0F);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RHigh(Integer.parseInt(cTmp));//�Ĵ���2����ֽ�
			}
			cTmp = element.getAttribute("Reg2RLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2RLow(Integer.parseInt(cTmp));//�Ĵ���2����ֽ�
			}

			//R_REG2��MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵ0x4500
			cTmp = element.getAttribute("Reg2GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GHigh(0x06);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GHigh(Integer.parseInt(cTmp));//�Ĵ���2�̸��ֽ�
			}
			cTmp = element.getAttribute("Reg2GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2GLow(Integer.parseInt(cTmp));//�Ĵ���2�̵��ֽ�
			}

			//R_REG2:MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵ0x6500
			cTmp = element.getAttribute("Reg2BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BHigh(0x26);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BHigh(Integer.parseInt(cTmp));//�Ĵ���2�����ֽ�
			}
			cTmp = element.getAttribute("Reg2BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BLow(0x00);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg2BLow(Integer.parseInt(cTmp));//�Ĵ���2�����ֽ�
			}

			//R_REG3��MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵΪ0xC003
			cTmp = element.getAttribute("Reg3RHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RHigh(0xC0);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RHigh(Integer.parseInt(cTmp));//�Ĵ���3����ֽ�
			}
			cTmp = element.getAttribute("Reg3RLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3RLow(Integer.parseInt(cTmp));//�Ĵ���3����ֽ�
			}

			//R_REG3��MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵ0x5003
			cTmp = element.getAttribute("Reg3GHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GHigh(0x50);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GHigh(Integer.parseInt(cTmp));//�Ĵ���3�̸��ֽ�
			}
			cTmp = element.getAttribute("Reg3GLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3GLow(Integer.parseInt(cTmp));//�Ĵ���2�̵��ֽ�
			}

			//R_REG3:MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵ0x4003
			cTmp = element.getAttribute("Reg3BHigh");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BHigh(0x40);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BHigh(Integer.parseInt(cTmp));//�Ĵ���3�����ֽ�
			}
			cTmp = element.getAttribute("Reg3BLow");
			if (cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BLow(0x03);
			}else{
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().setnReg3BLow(Integer.parseInt(cTmp));//�Ĵ���3�����ֽ�
			}

			//����_MBI5155��������Ż����ڼĴ���5153�����Ļ��������ӵ��ֶ�
			if (CHIP_TYPE._MBI5155.ordinal() == scancard.getnChipType())
			{
				cTmp = element.getAttribute("nDeltaT");//MBI5155 ��513/257��GCLK�ĵ͵�ƽ���� 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDeltaT((cTmp.equals("")) ? 0x00 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDeltaF");//MBI5155 ��513/257��GCLK�ĸߵ�ƽ���� 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDeltaF((cTmp.equals("")) ? 0x00 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDHT");//MBI5155 ��1��GCLK�ĸߵ�ƽ���� 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDHT((cTmp.equals("")) ? 0x1E : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDG_H");//MBI5155 ��514/258��GCLK�ĸߵ�ƽ����
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDG_H((cTmp.equals("")) ? 0x26 : Integer.parseInt(cTmp));

				cTmp = element.getAttribute("nDG_L");//MBI5155 ��514/258��GCLK�ĵ͵�ƽ���� 
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().setnDG_L((cTmp.equals("")) ? 0x19 : Integer.parseInt(cTmp));
			}
		}
		else if (CHIP_TYPE._MBI5043.ordinal() == scancard.getnChipType())
		{
			//MBI5043 GCLK˫�ز���	0���رգ� 1������
			cTmp = element.getAttribute("bGCLKDoublesampling");
			if (!cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().setbGCLKDoublesampling(Short.parseShort(cTmp) == 1 ? true : false);
			}
			if (scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().isbGCLKDoublesampling())//ˢ������������
			{
				scancard.setnRefreshDoubleValue((short) (scancard.getnRefreshDoubleValue() * 2));
			}

			//MBI5043 PWMģʽѡ��	0: 16bit��1: 10bit
			cTmp = element.getAttribute("nPWMMode");
			if (!cTmp.equals("")) {
				scancard.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().setnPWMMode((short) Integer.parseInt(cTmp));
			}
		}
		
		
		//������������
		cTmp = element.getAttribute("netportpriority");
		if (cTmp.equals("")) {
			scancard.setnNetPortPriority((short)0);
		}else{
			scancard.setnNetPortPriority(Short.parseShort(cTmp));
		}

		//������������
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
		byte[] temp1 = new byte[temp.length]; // ԭ��д�뿨��������޸Ĵ˴�����
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
		byte[] temp1 = new byte[temp.length]; // ԭ��д�뿨��������޸Ĵ˴�����
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

	// ɾ������ڵ�
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
    //ͨ��ϵ��idɾ����Ӧ����
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
			// ����ɨ�迨����,�˴���ȡ�ļ���Ϣ���п���Ϣ��д�������ļ��У��ò���д�벻��

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
		
		//��ȥ��ɫ��λ��
		element.setAttribute("DeductBit",
				String.valueOf(sScanCard.getnDeductBit()));

		// ONE_SCAN_CARD_WIDTH 2 128 ����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
		// ��ɨ�迨���ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
		element.setAttribute("ONE_SCAN_CARD_WIDTH",
				String.valueOf(sScanCard.getnScanCardWidth()));
		// ONE_SCAN_CARD_HEIGHT 2 96 ����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
		// ��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
		element.setAttribute("ONE_SCAN_CARD_HEIGHT",
				String.valueOf(sScanCard.getnScanCardHeight()));
		// mod_width 1 32 ģ����ȣ�ȡֵ��Χ1-64
		element.setAttribute("mod_width",
				String.valueOf(sScanCard.getnModuleWidth()));
		// mod_height 1 32 ģ��߶ȣ�ȡֵ��Χ1-64
		element.setAttribute("mod_height",
				String.valueOf(sScanCard.getnModuleHeight()));
		// 1 3 ģ������ ģ������ = ģ��߶�/ÿ������
		element.setAttribute("mod_section_number",
				String.valueOf(sScanCard.getnModuleSectionNumber()));
		// 1 4 ģ����������ģ�������� = ɨ�迨����/ģ�����
		element.setAttribute("mod_hor_number",
				String.valueOf(sScanCard.getnModuleHorNum()));
		// 1 3 ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
		element.setAttribute("mod_ver_number",
				String.valueOf(sScanCard.getnModuleVerNum()));
		// SCAN_CARD_SECTION_NUM 1 6 ɨ�迨���������������Ϊ16
		element.setAttribute("SCAN_CARD_SECTION_NUM",
				String.valueOf(sScanCard.getnScanCardSectionNumber()));
		// SCAN_CARD_SECTION_ROW_NUM 1 16 ɨ�迨ÿ���������������Ϊ16��
		element.setAttribute("SCAN_CARD_SECTION_ROW_NUM",
				String.valueOf(sScanCard.getnScanCardSectionRowNumber()));
		// SCAN_COLOR_DEPTH 1 14 ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
		element.setAttribute("SCAN_COLOR_DEPTH",
				String.valueOf(sScanCard.getnScanColorDepth()));
		// GRAY_LEVEL ɨ�迨�Ҷȼ���
		element.setAttribute("GRAY_LEVEL",
				String.valueOf(sScanCard.getnGrayLedvel()));
		// origin_color_bit ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
		element.setAttribute("origin_color_bit",
				String.valueOf(sScanCard.getnOrginColorBit()));
		// SCAN_MODE 1 4 ɨ���ģʽ��ȡ 1-2-4-8-16
		element.setAttribute("SCAN_MODE",
				String.valueOf(sScanCard.getnScanMode()));
		// DOT_CORRECTION_EN 1 1 ����У��ʹ�ܣ�ȡ0��1
		// ls.isbPBPDCard() == true? String.valueOf(1):String.valueOf(0)
		element.setAttribute("DOT_CORRECTION_EN",
				sScanCard.isbEmendBrightness() == true ? String.valueOf(1)
						: String.valueOf(0));
		// SCAN_GCLK_FREQUENCY 1 12.5 ɨ��ʱ��Ƶ�ʣ����30Mhz
		element.setAttribute("SCAN_GCLK_FREQUENCY",
				String.valueOf(sScanCard.getfScanClkFrequency()));
		// ZONE_DCLK_NUM 2 ÿ����λʱ������256*16
		element.setAttribute("ZONE_DCLK_NUM",
				String.valueOf(sScanCard.getnZoneClkNum()));
		// 1 0-100 ռ�ձ� ɨ��ʱ�ӵ�ռ�ձ�
		element.setAttribute("duty_cycle_low_value",
				String.valueOf(sScanCard.getnDutyCycle()));
		// PWM_SCAN_GCLK_FREQUENCY 1 12.5 PWMʱ��ʱ��Ƶ�ʣ����30Mhz
		element.setAttribute("PWM_SCAN_GCLK_FREQUENCY",
				String.valueOf(sScanCard.getfPWMScanClkFrequency()));
		// 1 0-100 PWMʱ��ռ�ձȿɵ��ȼ�
		element.setAttribute("pwm_duty_cycle_low_value",
				String.valueOf(sScanCard.getnPWMDutyCycle()));
		// CLR_CLK_NUM 1 4 1~255 ����ʱ����(������ʱ��)
		element.setAttribute("CLR_CLK_NUM",
				String.valueOf(sScanCard.getnOeClkNumber()));
		// ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
		element.setAttribute("refresh_rate",
				String.valueOf(sScanCard.getnRefreshRate()));
		// REF_FREQ_MIN ˢ������Сֵ
		element.setAttribute("refresh_rate_min",
				String.valueOf(sScanCard.getnRefreshRateMin()));
		// REF_FREQ_MAX ˢ�������ֵ
		element.setAttribute("refresh_rate_max",
				String.valueOf(sScanCard.getnRefreshRateMax()));
		// CONFIG_IC_TIME ��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1ms--30s
		element.setAttribute("config_ic_time",
				String.valueOf(sScanCard.getnConfigICTime()));
		// dat_freq_num ��֡Ƶ�ʼ�����
		element.setAttribute("dat_freq_num",
				String.valueOf(sScanCard.getnDatFreqNum()));
		// OE_DELAY_VALUE 0x01 �����ӳ�ʱ����������������Ĭ��Ϊ0x01������1������
		element.setAttribute("OE_DELAY_VALUE",
				String.valueOf(sScanCard.getnOeDelayValue()));
		// SYN_REFRESH_EN ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
		element.setAttribute(
				"SYN_REFRESH_EN",
				sScanCard.isbSyncRefresh() == true ? String.valueOf(1) : String
						.valueOf(0));
		// VIRTUAL_DISP_EN ������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
		element.setAttribute(
				"VIRTUAL_DISP_EN",
				sScanCard.isbVirtvalDisp() == true ? String.valueOf(1) : String
						.valueOf(0));
		// FREQ_DIVISION_COEF 0x7
		// 150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
		element.setAttribute("FREQ_DIVISION_COEF",
				String.valueOf(sScanCard.getnFreqDivisionCoeff()));
		// PWM_FREQ_DIVISION_COEF 0x7
		// 150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
		element.setAttribute("PWM_FREQ_DIVISION_COEF",
				String.valueOf(sScanCard.getnPWMFreqDivisionCoeff()));
		// DATA_OUTPUT_REVERSE �����������������ߺ�ɨ���� 0x00 ����������Ĭ��Ϊ0x0 0x0 ��ʹ�� 0x1 ʹ��
		element.setAttribute("DATA_OUTPUT_REVERSE",
				sScanCard.isbDataOutUpReverse() == true ? String.valueOf(1)
						: String.valueOf(0));
		// SCAN_OUTPUT_REVERSE 0x00 ɨ��������Ĭ��Ϊ0x0 0x0 ��ʹ�� 0x1 ʹ��
		element.setAttribute("SCAN_OUTPUT_REVERSE",
				sScanCard.isbScanOutUpReverse() == true ? String.valueOf(1)
						: String.valueOf(0));
		// DCB_LINE_CLK_EN 0x00 ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�; 0x0 ��ʹ�� 0x1 2�� 0x2 3�� 0x3 4��
		element.setAttribute("DCB_LINE_CLK_EN",
				String.valueOf(sScanCard.getnDCBlineClkEn()));
		// NO_SIGNAL_DISP ���ź���ʾ��Ĭ��Ϊ0 0x0��������0x1��������档0x2��ͼƬ
		element.setAttribute("NO_SIGNAL_DISP",
				String.valueOf(sScanCard.getnNoSingleDisp()));
		// DATA_INPUT_DIR 0x32 ���ݷ��򣺣�����ʾ�����濴��
		// Ĭ��Ϊ���ҵ���0x1 0x0 �������� 0x1 �������� 0x2 �������� 0x3 ��������
		element.setAttribute("DATA_INPUT_DIR",
				String.valueOf(sScanCard.getnDataInputDir()));
		// ROW_DECODE_MODE �����뷽ʽ��Ĭ��0x2
		// 0x0 ��̬������ 0x6 164����
		// 0x1 ������оƬ��ֱ�������й� 0x7 192����
		// 0x2 138���� 0x8 193����
		// 0x3 139���� 0x9 595����
		// 0x4 145�����138˫O 0xA 4096����
		// 0x5 154���� 0xB
		element.setAttribute("ROW_DECODE_MODE",
				String.valueOf(sScanCard.getnRowDecodeMOde()));
		// DATA_LINE_TYPE 8 0x14 7--0 �������ʹ��ࣺĬ��Ϊ0x00��
		element.setAttribute("DATA_LINE_TYPE_RANGE",
				String.valueOf(sScanCard.getnDataLineTypeRange()));
		// DATA_LINE_TYPE 8 0x14 7--0 �������ͣ�Ĭ��Ϊ0x00��
		// 0x00-0x1F �������ֿ�,
		// 0x20-0x18 ��������һ��ɫ1�㴮��
		// 0x30-0x38 ��������һ��ɫ8�㴮��
		// 0x40-0x48 ��������һ��ɫ16�㴮��
		// 0x50-0x6F ��������һ��ɫ����
		element.setAttribute("DATA_LINE_TYPE",
				String.valueOf(sScanCard.getnDataLineType()));
		// DATA_LINE_CTRL 8 0x00 �����߿���,
		// ����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
		element.setAttribute("DATA_LINE_CTRL",
				String.valueOf(sScanCard.getnDataLineCtrl()));
		// FIELD_NUM �ܳ��������Ϊ136��������1������
		element.setAttribute("FIELD_NUM",
				String.valueOf(sScanCard.getnFieldNum()));
		// HALF_FIELD_NUM �볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
		element.setAttribute("HALF_FIELD_NUM",
				String.valueOf(sScanCard.getnHalfFieldNumber()));
		// FULL_FIELD_NUM ȫ���������Ϊ128������1����
		element.setAttribute("FULL_FIELD_NUM",
				String.valueOf(sScanCard.getnFullFieldNumber()));
		// ��ʼ��
		element.setAttribute("start_field",
				String.valueOf(sScanCard.getnStartField()));
		// ��ֹ��
		element.setAttribute("end_field",
				String.valueOf(sScanCard.getnEndField()));
		// DATA_POLARITY 4 ���ݼ��ԣ�Ĭ��Ϊ0x0 0x0 �ߵ�ƽ���� 0x1 �͵�ƽ���� 0x2-0xF ����14�������Ԥ��
		element.setAttribute("DATA_POLARITY",
				String.valueOf(sScanCard.getnDataPolarity()));
		// OE_POLARITY 0x1F OE���ԣ�Ĭ��Ϊ0x0 0x0 ����Ч 0x1 ����Ч
		element.setAttribute("OE_POLARITY",
				String.valueOf(sScanCard.getnOePolarity()));
		// EMPTY_INSERT_EN �յ����ʹ�ܣ���ÿ���ٵ������ٿյ�. Ĭ��0x0����ʹ��
		element.setAttribute("EMPTY_INSERT_EN",
				sScanCard.isbEmptyInsertEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// INSERT_MODE ����յ㷽ʽ��ǰ���뻹�Ǻ���롣 1��ǰ����յ㡣0�������յ�
		element.setAttribute("INSERT_MODE",
				String.valueOf(sScanCard.getnInsertMode()));
		// EMPTY_DOT_NUM ����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1������
		element.setAttribute("EMPTY_DOT_NUM",
				String.valueOf(sScanCard.getnEmptyDotNum()));
		// REAL_DOT_NUM 15--0 ÿ���ٵ����յ㣬����1������
		element.setAttribute("REAL_DOT_NUM",
				String.valueOf(sScanCard.getnRealDotNum()));
		// ˫�����
		element.setAttribute(
				"dual_out_put",
				sScanCard.isbDualOutput() == true ? String.valueOf(1) : String
						.valueOf(0));
		// ���������Ų���ʽ
		element.setAttribute("virTual_array",
				String.valueOf(sScanCard.getnVirTualArray()));
		// �ư�оƬ
		element.setAttribute("chip_type",
				String.valueOf(sScanCard.getnChipType()));
		// ref_doule_value ˢ���ʱ����ı�����
		element.setAttribute("ref_doule_value",
				String.valueOf(sScanCard.getnRefreshDoubleValue()));
		// zhe_rdwr_mode �۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
		// 0������8��д��1���������ж�д
		element.setAttribute("zhe_rdwr_mode",
				String.valueOf(sScanCard.getnZheRdwrMode()));
		// ��ʾ������ 0-ȫ��ʵ���أ�1-ȫ������
		element.setAttribute("screen_type",
				String.valueOf(sScanCard.getnScreenType()));
		// ����У������, 0-������1-��ɫ
		element.setAttribute("dot_correct_tye",
				String.valueOf(sScanCard.getnDotCorrectTye()));

		// ����Ч��/���Խ���
		element.setAttribute(
				"test_start",
				sScanCard.isbTest() == true ? String.valueOf(1) : String
						.valueOf(0));

		// if (_GENERAL == scancard.getnChipType())
		// {
		// //2013-3-5�θ߳�
		// strcpy(chKey, "Second_Filed");
		// sprintf(chKeyVal,"%d",sScanCard.nSecondHighLevel);
		// ElementSC->SetAttribute(chKey,chKeyVal);
		// }
		// ������Ч��
		element.setAttribute("brightness_efficent",
				String.valueOf(sScanCard.getfBrightnessEfficent()));
		// ��СOE����
		element.setAttribute("min_oe_width",
				String.valueOf(sScanCard.getnMinOEWidth()));
		// ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
		element.setAttribute("dot_open_detection",
				sScanCard.isbDotOpenDetection() == true ? String.valueOf(1)
						: String.valueOf(0));
		// PWM���ģʽ MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-��ɢģʽ��0-��ͨģʽ
		element.setAttribute("pwm_output_mode",
				String.valueOf(sScanCard.getnPWMOutputMode()));
		// ˢ���ʱ�����PWM��̬ɨ������Ч
		element.setAttribute("multi_refresh_under_static_scan", sScanCard
				.isbMultiRefreshUnderStaticScan() == true ? String.valueOf(1)
				: String.valueOf(0));

		// ONE_SCAN_CARD_WIDTH_REAL
		// ��ɨ�迨ʵ�ʿ��ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		element.setAttribute("ONE_SCAN_CARD_WIDTH_REAL",
				String.valueOf(sScanCard.getnScanCardWidthReal()));
		// ONE_SCAN_CARD_HEIGHT_REAL
		// ��ɨ�迨ʵ�ʸ߶ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		element.setAttribute("ONE_SCAN_CARD_HEIGHT_REAL",
				String.valueOf(sScanCard.getnScanCardHeightReal()));

		// ����ʹ�� �˴����µ�ע�Ͳ���д������Ϣʱ��cbt�ļ��в�����ͬ��д���쳣����ʱע��zhangsj 2015-01-16
		element.setAttribute("extend_enable",
				sScanCard.isbExtendedEnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		element.setAttribute("extend_enableEx",
				sScanCard.isbExtendedEnableEx() == true ? String.valueOf(1)
						: String.valueOf(0));
		// ����
		element.setAttribute("section_width",
				String.valueOf(sScanCard.getnSectionWidth()));
		// ����������
		element.setAttribute("section_hor_number",
				String.valueOf(sScanCard.getnSectionHorNum()));

		// ������
		element.setAttribute("Card_zone_width",
				String.valueOf(sScanCard.getnCard_zone_width()));
		// ��������
		element.setAttribute("Card_zone_Num",
				String.valueOf(sScanCard.getnCard_zone_Num()));

		// �Ҷ���ǿλ��
		element.setAttribute("gray_enhance_bit",
				String.valueOf(sScanCard.getnGrayEnhance()));
		// �Ҷ���ǿ��ʽ
		element.setAttribute("gray_enhance_mode",
				String.valueOf(sScanCard.getnGrayEnhanceMode()));
		// ����ָʾ��
		element.setAttribute("open_cabinet_lamp",
				sScanCard.isbOpenCabinetLamp() == true ? String.valueOf(1)
						: String.valueOf(0));
		// gammaλ��
		element.setAttribute("gamma_bit",
				String.valueOf(sScanCard.getnCustomGamam()));
		// оƬԤ��繦��, 0 - �� 1�� �ر�,Ĭ�ϴ�
		element.setAttribute("chip_precharge",
				sScanCard.isbChipPrecharge() == true ? String.valueOf(1)
						: String.valueOf(0));
		// RGB��ɫ�ֱ����GCLK���ӳ�ʱ�� ʹ�ܱ�ʶ
		element.setAttribute("gclk_delay_ctrl_by_rgb",
				sScanCard.isbGClkCtrlByRGBEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// Rɫ����GCLK���ӳ�ʱ��
		element.setAttribute("gclk_delay_ctrl_by_r",
				sScanCard.isbGClkCtrlByREnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		// GCLK���ӳ�ʱ��������ɫ�ֿ�ʱΪR)
		element.setAttribute("gclk_delay",
				String.valueOf(sScanCard.getnGClkDelay()));
		// Gɫ����GCLK���ӳ�ʱ��
		element.setAttribute("gclk_delay_ctrl_by_g",
				sScanCard.isbGClkCtrlByGEnable() == true ? String.valueOf(1)
						: String.valueOf(0));
		// G��GCLK���ӳ�ʱ����
		element.setAttribute("gclk_delay_G",
				String.valueOf(sScanCard.getnGClkDelay_G()));
		// Bɫ����GCLK���ӳ�ʱ��
		element.setAttribute("gclk_delay_ctrl_by_b",
				sScanCard.isbGClkCtrlByBEnable() == true ? String.valueOf(1)
						: String.valueOf(0));

		// B��GCLK���ӳ�ʱ����
		element.setAttribute("gclk_delay_B",
				String.valueOf(sScanCard.getnGClkDelay_B()));

		// �Ĵ�����Ϣ
		if (CHIP_TYPE._TLC5958.ordinal() == sScanCard.getnChipType() ||
			CHIP_TYPE._MBI5152.ordinal() == sScanCard.getnChipType() ||
			CHIP_TYPE._MBI5153.ordinal() == sScanCard.getnChipType()) {
			
			// reg1
			// ȫ�����ȵ���
			element.setAttribute("bright",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnBright()));
			// ��ɫ�ͻ���ǿ
			element.setAttribute("lgse_r",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_R()));
			// ��ɫ�ͻ���ǿ
			element.setAttribute("lgse_g",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_G()));
			// ��ɫ�ͻ���ǿ
			element.setAttribute("lgse_b",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLgse_B()));
			// ���ͨ���ӳ�ʹ��
			element.setAttribute("gdly_enable", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGdly_Enable()));
			// ���������ӳ�
			element.setAttribute("td_delay",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnTD_Delay()));
			// ��·����ѹ�趨
			element.setAttribute("lodvth",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnLodvth()));
			// reg2
			// ȫ�ֵͻ���ǿ
			element.setAttribute("global_lgse", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGlobal_Lgse()));
			// ��ɢģʽ
			element.setAttribute("PVM_mode",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnPVM_Mode()));
			// ��ɫEMI����
			element.setAttribute("EMI_r",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_R()));
			// ��ɫEMI����
			element.setAttribute("EMI_g",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_G()));
			// ��ɫEMI����
			element.setAttribute("EMI_b",
					String.valueOf(sScanCard.getnDrive_ic_reg().getnEMI_B()));
			// Ԥ���ģʽ
			element.setAttribute("pre_charge", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPre_Charge()));
		
			//MBI5152/5153
			//reg1
			//Ԥ���ģʽ
			element.setAttribute("pre_charge1", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPre_Charge1()));

			//PWM����ģʽ
			element.setAttribute("pwm_count_mode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnPwm_Count_Mode()));

			//�ҽ�ģʽ
			element.setAttribute("gray_mode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnGdly_Enable()));

			//GCLK��Ƶ
			element.setAttribute("enable_gclk", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnEnable_GCLK()));

			//reg2
			//˫��ˢ����
			element.setAttribute("double_refreserate", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnDouble_RefreseRate()));

			//��·����ѹ
			element.setAttribute("voltage", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnVoltage()));

			//������ICʶ��
			element.setAttribute("icrecognition", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnIC_Recognition()));
			
			//����ƫ�����ڣ���ɫ��
			element.setAttribute("adjust_red", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Red()));

			//����ƫ�����ڣ���ɫ��
			element.setAttribute("adjust_green", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Green()));

			//����ƫ�����ڣ���ɫ��
			element.setAttribute("adjust_blue", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnAdjust_Blue()));

			//����ģʽ�ߵ�ƽ������
			element.setAttribute("imhl_donnotstretch", String.valueOf(sScanCard
					.getnDrive_ic_reg().getnImhl_DoNotStretch()));
		}
		else if (CHIP_TYPE._MBI5153_E.ordinal() == sScanCard.getnChipType() || 
				CHIP_TYPE._MBI5155.ordinal() == sScanCard.getnChipType())
		{
			
			//�Ĵ���1����ֽ�
			element.setAttribute("Reg1RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1RHigh()));

			//�Ĵ���1����ֽ�
			element.setAttribute("Reg1RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1RLow()));

			//�Ĵ���1�̸��ֽ�
			element.setAttribute("Reg1GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1GHigh()));
			
			//�Ĵ���1�̵��ֽ�
			element.setAttribute("Reg1GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1GLow()));
			
			//�Ĵ���1�����ֽ�
			element.setAttribute("Reg1BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1BHigh()));
			
			//�Ĵ���1�����ֽ�
			element.setAttribute("Reg1BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg1BLow()));

			//�Ĵ���2����ֽ�
			element.setAttribute("Reg2RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2RHigh()));

			//�Ĵ���2����ֽ�
			element.setAttribute("Reg2RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2RLow()));

			//�Ĵ���2�̸��ֽ�
			element.setAttribute("Reg2GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2GHigh()));

			//�Ĵ���2�̵��ֽ�
			element.setAttribute("Reg2GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2GLow()));

			//�Ĵ���2�����ֽ�
			element.setAttribute("Reg2BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2BHigh()));

			//�Ĵ���2�����ֽ�
			element.setAttribute("Reg2BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg2BLow()));

			//�Ĵ���3����ֽ�
			element.setAttribute("Reg3RHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3RHigh()));

			//�Ĵ���3����ֽ�
			element.setAttribute("Reg3RLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3RLow()));

			//�Ĵ���3�̸��ֽ�
			element.setAttribute("Reg3GHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3GHigh()));

			//�Ĵ���3�̵��ֽ�
			element.setAttribute("Reg3GLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3GLow()));

			//�Ĵ���3�����ֽ�
			element.setAttribute("Reg3BHigh", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3BHigh()));

			//�Ĵ���3�����ֽ�
			element.setAttribute("Reg3BLow", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5153_E().getnReg3BLow()));

			
			//����_MBI5155��������Ż����ڼĴ���5153�����Ļ��������ӵ��ֶ�
			if (CHIP_TYPE._MBI5155.ordinal() == sScanCard.getnChipType())
			{
				//MBI5155 ��513/257��GCLK�ĵ͵�ƽ���� 
				element.setAttribute("nDeltaT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDeltaT()));

				//MBI5155 ��513/257��GCLK�ĸߵ�ƽ���� 
				element.setAttribute("nDeltaT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDeltaF()));

				//MBI5155 ��1��GCLK�ĸߵ�ƽ���� 
				element.setAttribute("nDHT", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDHT()));

				//MBI5155 ��514/258��GCLK�ĸߵ�ƽ����
				element.setAttribute("nDG_H", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDG_H()));

				//MBI5155 ��514/258��GCLK�ĵ͵�ƽ���� 
				element.setAttribute("nDG_L", String.valueOf(sScanCard
						.getnDrive_ic_reg().getsDrive_ic_reg_MBI5155().getnDG_L()));
			}
		}
		else if (CHIP_TYPE._MBI5043.ordinal() == sScanCard.getnChipType())
		{
			//MBI5043 GCLK˫�ز���	0���رգ� 1������
			element.setAttribute("bGCLKDoublesampling", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().isbGCLKDoublesampling() ? 1: 0));
			
			//MBI5043 PWMģʽѡ��	0: 16bit��1: 10bit
			element.setAttribute("nPWMMode", String.valueOf(sScanCard
					.getnDrive_ic_reg().getsDrive_ic_reg_MBI5043().getnPWMMode()));
		}
		

		//������������
		element.setAttribute("netportpriority",
				String.valueOf(sScanCard.getnNetPortPriority()));

		//������������
		element.setAttribute("locknetport",
				String.valueOf(sScanCard.isbLockNetPort() ? 1 : 0));
		
		return true;
		// *********************************************************************************************/
	}

	public static boolean SetScanCardLinkup(Element element,
			LinkTable ScanCardlinktable) {
		element.setAttribute("Len", String.valueOf(ScanCardlinktable.getnLen()));
		// //����
		int len = (int) ScanCardlinktable.getnLen();
		// byte[] temp = new byte[(int) ScanCardlinktable.getnLen()];
		// String temp1 = UtilFun.bytes2HexString( temp,temp.length ," ");
		// �˴��޸���ʱ����temp1ȫΪ0����Ϊ�������ݻ�������ѭ������ֹͣ
		// element.setAttribute("data",UtilFun.bytes2HexString(ScanCardlinktable.getUcLinkTable()));
		element.setAttribute("data", UtilFun.bytes2HexString(
				ScanCardlinktable.getUcLinkTable(), len, " "));
		return true;
	}

	public static boolean SetHUBLinkup(Element element, LinkTable Hublinktable) {
		element.setAttribute("Len", String.valueOf(Hublinktable.getnLen()));
		// ����
		int len = (int) Hublinktable.getnLen();
		// byte[] temp = new byte[(int) Hublinktable.getnLen()];
		// String temp1 = UtilFun.bytes2HexString( temp,temp.length ," ");
		element.setAttribute("data", UtilFun.bytes2HexString(
				Hublinktable.getUcLinkTable(), len, " "));
		// �������ݻ�������ѭ��
		// element.setAttribute("data",UtilFun.bytes2HexString(Hublinktable.getUcLinkTable()));
		return true;
	}

	public static boolean SetSectionLinkup(Element element,
			LinkTable SectionLinkuptable) {
		element.setAttribute("Len",
				String.valueOf(SectionLinkuptable.getnLen()));
		// ����
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
		// д������Ϣʱ�в�ͬ������д�벻��2015-01-16
		/*
		 * �����жϣ����ڲ����Ͳ����ڲ�������������ִ��, �����жϺ� ��
		 * ���۲������ڲ����ڶ��������ϲ�����2015/03/06zhangsj�޸�
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

	// ͨ��ϵ��IDɾ������0528zhangsj
	public static boolean RemoveCabinetBySeriesId(int nSeriesId) {
		// List<String> listcbtSidList = new ArrayList<String>();
		try {
			// �õ�����ϵ��
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
							// ��������ϵ��ID��ɾ����Ӧ������ڵ�
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

	// ɾ������ϵ������0528zhangsj
	public static boolean RemoveCabinetSerise(String cSeriesname) {
		try {
			// �õ�����ϵ��
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

	// �������嵽xml�ļ�
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
			newelement1.setTextContent(Integer.toString(sCabinet.getnID()));// //���ýڵ����ݣ�����������
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
			// ����ɨ�迨�����ڵ�����zhangsj
			SetScanCardAttachments(sCabinet, doc, elementscancardElements);
			// doc.creat
			Element elementLoadedRegion = doc.createElement("LoadedRegion");
			SetLoadedRegion(elementLoadedRegion, sCabinet.getRtRect());
			// /���ӽڵ�
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

	// ��������ϵ���ļ���xml�ļ���
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
					.setTextContent(Integer.toString(sCabinetseries.getID()));// //���ýڵ����ݣ�����������
			Element newelement2 = doc.createElement("ParentID");
			newelement2.setTextContent(Integer.toString(sCabinetseries
					.getParentID()));
			Element newelement3 = doc.createElement("name");
			newelement3.setTextContent(sCabinetseries.getName());
			// /���ӽڵ�
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
	// ��������ϵ���ļ���xml�ļ��� 2015-07-13zsj
		public static boolean ModifyCabinetSeriesName(String SeriesName,String cBseriesName) {
				
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new File(HomePageActivity.CONFIG_PATH, sFileCabinetsString));
				NodeList list = doc.getElementsByTagName("CabinetSeries");
				for (int i = 0; i < list.getLength(); i++) {
					
					Node node = list.item(i); //��ǰ�����Ľڵ� CabinetSeries
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
							 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("�ӽڵ�ID����");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cSeries.setParentID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("���ڵ㿪ʼ����");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cSeries.setName(childNode.getTextContent()
										.toString());
								System.out.println("����ϵ�����ƽ���");
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
							 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs return
							 * null;
							 */
							if (childNode.getNodeName().trim().equals("ID")) {
								cSeries.setID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("�ӽڵ�ID����");
							}
							if (childNode.getNodeName().trim()
									.equals("parentID")) {
								cSeries.setParentID(Integer.parseInt(childNode
										.getTextContent().toString()));
								System.out.println("���ڵ㿪ʼ����");
							}
							if (childNode.getNodeName().trim().equals("name")) {
								cSeries.setName(childNode.getTextContent()
										.toString());
								System.out.println("����ϵ�����ƽ���");
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

	//�޸��������ƣ����� zsj 2015-07-14 add 
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