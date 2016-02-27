package com.szaoto.ak10.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import com.szaoto.ak10.entity.CabinetSeries;
import com.szaoto.ak10.entity.CabinetXml;
import com.szaoto.ak10.entity.VideoFile;
/**
 * ������XmlTool ����xml���� 
 * ��Ҫ���ܣ���xml�ļ����ж�ȡ����ɾ�Ĳ���
 * ���ߣ� zhangsj 
 * �޸����ڣ�2014��5��9�� 
 * �޸����ݣ��޸���xml�Ĵ��·����������xml�ļ����λ��ͳһ 
 */
public class XmlTool {
	/*
	// ����xml�ļ�
	public static boolean createXmlFile(final String xmlPath) {
		File xmlFile = new File(xmlPath);
		FileOutputStream fileOPStream = null;
		try {
			fileOPStream = new FileOutputStream(xmlFile);
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
			return false;
		}

		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileOPStream, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "books");

			for (int i = 0; i < 5; i++) {
				serializer.startTag(null, "book");
				serializer.startTag(null, "bookname");
				serializer.text("Android�̳�" + i);
				serializer.endTag(null, "bookname");
				serializer.startTag(null, "bookauthor");
				serializer.text("Frankie" + i);
				serializer.endTag(null, "bookauthor");
				serializer.endTag(null, "book");
			}

			serializer.endTag(null, "books");
			serializer.endDocument();

			serializer.flush();
			fileOPStream.close();

			return true;
		} catch (Exception e) {
			Log.e("XmlParserUtil", "error occurred while creating xml file");
			return false;
		}

	}
*/
	/**
	 * dom����xml�ļ� xmlPath xml��·��
	 */
	/*
	public static void domParseXML(final String xmlPath) {
		File file = new File(xmlPath);
		if (!file.exists() || file.isDirectory()) {
			Log.e("domParseXML", "file not exists");
			return;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		}
		Document doc = null;
		try {
			doc = db.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc != null) {
			Element root = doc.getDocumentElement();
			NodeList books = root.getElementsByTagName("book");
			String res = "�������ͨ��dom����:" + "\n";
			int nLength = books.getLength();
			for (int i = 0; i < nLength; i++) {
				Element book = (Element) books.item(i);
				Element bookname = (Element) book.getElementsByTagName("bookname").item(0);
				Element bookauthor = (Element) book.getElementsByTagName("bookauthor").item(0);
				res += "����: " + bookname.getFirstChild().getNodeValue() + " "
						+ "����: " + bookauthor.getFirstChild().getNodeValue() + "\n";
			}	
		}
	}
*/
	/**
	 * xmlPullParser����xml�ļ� xmlPath xml��·��
	 */
	/*
	public static void xmlPullParseXML(final String xmlPath) {
		String res = "�������ͨ��XmlPullParse����:" + "\n";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			try {
				xmlPullParser.setInput(new StringReader(
						bufferedReaderFile(xmlPath)));
			} catch (Exception e) {
				Log.e("xmlPullParseXML", e.toString());
			}

			int eventType = xmlPullParser.getEventType();
			try {
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("bookname".equals(nodeName)) {
							res += "����: " + xmlPullParser.nextText() + " ";
						} else if ("bookauthor".equals(nodeName)) {
							res += "����: " + xmlPullParser.nextText() + "\n";
						}
						break;

					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * ��sd���ж�ȡxml�ļ�������
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	/*
	private static String bufferedReaderFile(final String path)
			throws IOException {
		File file = new File(path);
		if (!file.exists() || file.isDirectory())
			throw new FileNotFoundException();

		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		StringBuffer sb = new StringBuffer();
		temp = br.readLine();
		while (temp != null) {
			sb.append(temp + " ");
			temp = br.readLine();
		}
		br.close();

		return sb.toString();
	}
*/
	/**
	 * ����XMLת���ɶ���
	 * 
	 * @param is
	 *            ������
	 * @param clazz
	 *            ����Class
	 * @param fields
	 *            �ֶμ���һһ��Ӧ�ڵ㼯��
	 * @param elements
	 *            �ڵ㼯��һһ��Ӧ�ֶμ���
	 * @param itemElement
	 *            ÿһ��Ľڵ��ǩ
	 * @return
	 */
	public static List<Object> parse(InputStream is, Class<?> clazz,
			List<String> fields, List<String> elements, String itemElement) {
		Log.v("rss", "��ʼ����XML.");
		List<Object> list = new ArrayList<Object>();
		try {

			XmlPullParser xmlPullParser = Xml.newPullParser();

			xmlPullParser.setInput(is, "UTF-8");
			int event = xmlPullParser.getEventType();

			Object obj = null;

			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					if (itemElement.equals(xmlPullParser.getName())) {
						obj = clazz.newInstance();
					}
					if (obj != null
							&& elements.contains(xmlPullParser.getName())) {
						setFieldValue(obj, fields.get(elements
								.indexOf(xmlPullParser.getName())),
								xmlPullParser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (itemElement.equals(xmlPullParser.getName())) {
						list.add(obj);
						obj = null;
					}
					break;
				}
				event = xmlPullParser.next();
			}
		} catch (Exception e) {
			Log.e("rss", "����XML�쳣��" + e.getMessage());
			throw new RuntimeException("����XML�쳣��" + e.getMessage());
		}
		return list;
	}

	/**
	 * ����XMLת���ɶ���
	 * 
	 * @param clazz
	 *            ����Class
	 * @param fields
	 *            �ֶμ���һһ��Ӧ�ڵ㼯��
	 * @param elements
	 *            �ڵ㼯��һһ��Ӧ�ֶμ���
	 * @param itemElement
	 *            ÿһ��Ľڵ��ǩ
	 * @return
	 */
	public static List<Object> parseByXmlId(Class<?> clazz,
			List<String> fields, List<String> elements, String itemElement,
			XmlPullParser xmlPullParser) {
		Log.v("rss", "��ʼ����XML.");
		List<Object> list = new ArrayList<Object>();
		try {
			// XmlPullParser xmlPullParser = Xml.newPullParser();
			// xmlPullParser.setInput(is, "UTF-8");
			int event = xmlPullParser.getEventType();
			Object obj = null;
			String itemName = "";
			String primaryKey = "";
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					itemName = xmlPullParser.getName();
					if (itemElement.equals(itemName)) {
						primaryKey = xmlPullParser.getAttributeValue(null, "id");
						obj = clazz.newInstance();
					}
					if (obj != null && elements.contains(itemName)) {

						// �����ֶ�ֵ��ע�⣺����ת����Ŀǰֻ֧��String���ͣ����������ͣ�Ҫ��������ת��

						String propertyName = fields.get(elements.indexOf(itemName));

						if (propertyName.equals("id")) {
							setFieldValue(obj, propertyName, primaryKey);
						} else {
							setFieldValue(obj, propertyName,xmlPullParser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if (itemElement.equals(xmlPullParser.getName())) {
						list.add(obj);
						obj = null;
					}
					break;
				}
				event = xmlPullParser.next();
			}
		} catch (Exception e) {
			Log.e("rss", "����XML�쳣��" + e.getMessage());
			throw new RuntimeException("����XML�쳣��" + e.getMessage());
		}
		return list;
	}

	/**
	 * �����ֶ�ֵ
	 * 
	 * @param propertyName
	 *            �ֶ���
	 * @param obj
	 *            ʵ������
	 * @param value
	 *            �µ��ֶ�ֵ
	 * @return
	 */
	public static void setFieldValue(Object obj, String propertyName,
			Object value) {
		try {

			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			field.set(obj, value);

		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * ���ӵ� xml�ļ�
	 * 
	 * @param filePath
	 * @param fileName
	 * @param videofilePath
	 * @param imagePath
	 * @throws Exception
	 */
	public static void addXml(String filePath, String fileName,
			String videofilePath, String imagePath, String duration,
			String specialEffect) throws Exception {

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		Element elementP = document.createElement("VideoFile");

		String PrimaryKey = UUID.randomUUID().toString();

		elementP.setAttribute("id", PrimaryKey);

		Element element_id = document.createElement("id");

		// element_id.setTextContent(PrimaryKey);

		element_id.setTextContent(String.valueOf(+document
				.getElementsByTagName("VideoFile").getLength() + 1));

		Element element_fileName = document.createElement("fileName");

		element_fileName.setTextContent(fileName);

		Element element_filePath = document.createElement("filePath");

		element_filePath.setTextContent(videofilePath);

		Element element_imagePath = document.createElement("imagePath");

		element_imagePath.setTextContent(imagePath);

		Element element_duration = document.createElement("duration");

		element_duration.setTextContent(duration);
		// element_duration.setTextContent(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);

		Element element_specialEffect = document.createElement("specialEffect");

		element_specialEffect.setTextContent(specialEffect);

		elementP.appendChild(element_id);
		elementP.appendChild(element_fileName);
		elementP.appendChild(element_filePath);
		elementP.appendChild(element_imagePath);
		elementP.appendChild(element_duration);
		elementP.appendChild(element_specialEffect);

		// ��ȡ���ڵ�
		Element eltRoot = document.getDocumentElement();
		// ��Ҷ�ڵ���뵽���ڵ���
		eltRoot.appendChild(elementP);
		// �����޸ĺ��Դ�ļ�
		saveXML(document, filePath);

	}

	/**
	 * ���ӵ� xml�ļ�
	 * 
	 * @param filePath
	 * @param fileName
	 * @param videofilePath
	 * @param imagePath
	 * @throws Exception
	 */
	public static void addXmlByVideoFileList(String filePath,
			List<VideoFile> videoFileList) throws Exception {

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		Element elementP;
		Element element_id;
		Element element_fileName;
		Element element_filePath;
		Element element_imagePath;
		Element element_duration;
		Element element_specialEffect;

		String PrimaryKey; // ��������

		int nodeCount = document.getElementsByTagName("VideoFile").getLength();

		// ��ȡ���ڵ�
		Element eltRoot = document.getDocumentElement();

		for (VideoFile videoFile : videoFileList) {

			elementP = document.createElement("VideoFile");

			PrimaryKey = UUID.randomUUID().toString();

			elementP.setAttribute("id", PrimaryKey);

			element_id = document.createElement("id");

			// element_id.setTextContent(PrimaryKey);

			nodeCount++;

			element_id.setTextContent(String.valueOf(nodeCount));

			element_fileName = document.createElement("fileName");

			element_fileName.setTextContent(videoFile.getFileName());

			element_filePath = document.createElement("filePath");

			element_filePath.setTextContent(videoFile.getFilePath());

			element_imagePath = document.createElement("imagePath");

			element_imagePath.setTextContent(videoFile.getImagePath());

			element_duration = document.createElement("duration");

			element_duration.setTextContent(videoFile.getDuration());
			// element_duration.setTextContent(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);

			element_specialEffect = document.createElement("specialEffect");

			element_specialEffect.setTextContent(videoFile.getSpecialEffect());

			elementP.appendChild(element_id);
			elementP.appendChild(element_fileName);
			elementP.appendChild(element_filePath);
			elementP.appendChild(element_imagePath);
			elementP.appendChild(element_duration);
			elementP.appendChild(element_specialEffect);

			// ��Ҷ�ڵ���뵽���ڵ���
			eltRoot.appendChild(elementP);
			// �����޸ĺ��Դ�ļ�
		}

		saveXML(document, filePath);

	}

	/**
	 * �޸� xml �ļ�
	 * 
	 * @param filePath
	 * @param pointion
	 * @throws Exception
	 */
	public static void updateXml(String filePath, VideoFile videoFile)
			throws Exception {

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		// Element elementTest = document.getElementById("hengli14");
		// String textString = elementTest.getTextContent();

		Element element = document.getElementById(videoFile.getId());

		if (element.hasChildNodes()) {

			NodeList nodeList = element.getChildNodes();

			//Node node2 = null;

			int nNodeCount = nodeList.getLength();

			String nodename = "";

			for (int i = 0; i < nNodeCount; i++) {

				nodename = nodeList.item(i).getNodeName();

				// if (nodename.equals("id")) {
				// nodeList.item(i).setTextContent(videoFile.getId());
				// }

				if (nodename.equals("fileName")) {
					nodeList.item(i).setTextContent(videoFile.getFileName());
				}

				if (nodename.equals("filePath")) {
					nodeList.item(i).setTextContent(videoFile.getFilePath());
				}

				if (nodename.equals("imagePath")) {
					nodeList.item(i).setTextContent(videoFile.getImagePath());
				}
				if (nodename.equals("duration")) {
					nodeList.item(i).setTextContent(videoFile.getDuration());
				}
				if (nodename.equals("specialEffect")) {
					nodeList.item(i).setTextContent(
							videoFile.getSpecialEffect());
				}
			}

			// �����޸ĺ��Դ�ļ�
			saveXML(document, filePath);
		}

	}

	/**
	 * ɾ�� xml�ļ��� Ԫ��
	 * 
	 * @param filePath
	 * @param pointion
	 * @throws Exception
	 */
	public static void deleteXml(String filePath, String videoFileId)
			throws Exception {

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		// NodeList nodeList = document.getElementsByTagName("VideoFile");

		Node node = document.getElementById(videoFileId);

		document.getFirstChild().removeChild(node);

		// node.getParentNode().removeChild(node);

		saveXML(document, filePath);
	}

	/**
	 * ���浽 xml �ļ�
	 * 
	 * @param document
	 * @param filePath
	 */
	private static void saveXML(Document document, String filePath) {

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

	/**
	 * �� �ʲ��ļ����е� xml �ļ� ���Ƶ� sd����
	 * 
	 * @param context
	 */
	public static void CopyXmlFile(Context context) {
		try {
			File xmlFile = new File(Environment.getDataDirectory(),
					"/data/com.szaoto.ak10/files/config/videofilelist.xml");
			System.out.println("xml�ļ���ȡ��" + xmlFile);

			if (!xmlFile.exists()) {

				InputStream xmlInputStream = context.getResources().getAssets()
						.open("videofilelist.xml");

				xmlFile.getParentFile().mkdirs();

				xmlFile.createNewFile();

				FileOutputStream out = new FileOutputStream(xmlFile);

				byte[] buffer = new byte[1024];

				int byteCount = -1;

				while ((byteCount = xmlInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, byteCount);
				}

				xmlInputStream.close();

				out.close();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * ���� ��ǰѡ�е� ����ֵ ��ȡ xml�ļ� �� ��Ӧ�� VideoFile ����
	 * 
	 * @param currSelectedPosition
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static VideoFile getVideoInfoByIndex(int currSelectedPosition,
			String filePath) throws Exception {

		VideoFile videoFile = new VideoFile();

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		NodeList nodeList = document.getElementsByTagName("VideoFile");

		Node node = nodeList.item(currSelectedPosition);

		Node childNode = null;
		int nNodeCount = node.getChildNodes().getLength();
		for (int i = 0; i < nNodeCount; i++) {
			childNode = node.getChildNodes().item(i);

			// <id>1</id>
			// <fileName>������.mp4</fileName>
			// <filePath>sdcard\files\videos\dhxy1.mp4</filePath>
			// <imagePath>sdcard\files\images\dhxy1.jpg</imagePath>

			if (childNode.getNodeName().trim().equals("id")) {
				videoFile.setId(childNode.getTextContent().toString());
			}
			if (childNode.getNodeName().trim().equals("fileName")) {
				videoFile.setFileName(childNode.getTextContent().toString());
			}
			if (childNode.getNodeName().trim().equals("filePath")) {
				videoFile.setFilePath(childNode.getTextContent().toString());
			}
			if (childNode.getNodeName().trim().equals("imagePath")) {
				videoFile.setImagePath(childNode.getTextContent().toString());
			}

			if (childNode.getNodeName().trim().equals("duration")) {
				videoFile.setDuration(childNode.getTextContent().toString());
			}
			if (childNode.getNodeName().trim().equals("specialEffect")) {
				videoFile.setSpecialEffect(childNode.getTextContent()
						.toString());
			}

		}

		return videoFile;
	}

	/**
	 * ��ȡ���һ�� �ļ���Ϣ�� id��Ҳ���������ӵ�һ�� �ļ���Ϣ ��id
	 * 
	 */
	public static int getLastVideoId(String filePath) throws Exception {
		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		NodeList nodeList = document.getElementsByTagName("VideoFile");

		String id = nodeList.item(nodeList.getLength() - 1).getFirstChild()
				.getNodeValue();

		return Integer.parseInt(id);
	}

	/**
	 * ��� xml�ļ� ����� ���� item
	 * 
	 * @throws Exception
	 */
	public static void RemoveAll(String filePath) throws Exception {
		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		// NodeList nodeList = document.getElementsByTagName("VideoFile");
		//
		// Node node;
		//
		// for (int i = 0; i < nodeList.getLength(); i++) {
		//
		// node = nodeList.item(i);
		//
		// node.getParentNode().removeChild(node);
		// }
		Element rootElement = document.getDocumentElement();

		rootElement.setTextContent("");

		// document.removeChild(document.getFirstChild());
		//
		// Node newChild = document.createElement("VideoFileList");
		//
		// document.appendChild(newChild);

		saveXML(document, filePath);

	}

	/**
	 * ���� �µ� �б��������� ���� xml�ļ�
	 * 
	 * @param xmlFilePath
	 * @param videoFileList_New
	 * @return
	 */
	public static boolean createXmlFileByVideoFileList(String xmlFilePath,
			List<VideoFile> videoFileList_New) {

		File xmlFile = new File(xmlFilePath);

		FileOutputStream fileOPStream = null;

		try {

			fileOPStream = new FileOutputStream(xmlFile);

		} catch (FileNotFoundException e) {

			Log.e("FileNotFoundException", "can't create FileOutputStream");

			return false;
		}

		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileOPStream, "UTF-8");

			serializer.startDocument(null, true);

			// ���ڵ�
			serializer.startTag(null, "VideoFileList");

			int i = 0;
			// �ӽڵ�
			for (VideoFile videoFile : videoFileList_New) {

				serializer.startTag(null, "VideoFile").attribute(null, "id", videoFile.getId());

				i++;

				serializer.startTag(null, "id");
				serializer.text(String.valueOf(i));
				serializer.endTag(null, "id");

				serializer.startTag(null, "fileName");
				serializer.text(videoFile.getFileName());
				serializer.endTag(null, "fileName");

				serializer.startTag(null, "filePath");
				serializer.text(videoFile.getFilePath());
				serializer.endTag(null, "filePath");

				serializer.startTag(null, "imagePath");
				serializer.text(videoFile.getImagePath());
				serializer.endTag(null, "imagePath");

				serializer.startTag(null, "duration");
				serializer.text(videoFile.getDuration());
				serializer.endTag(null, "duration");

				serializer.startTag(null, "specialEffect");
				serializer.text(videoFile.getSpecialEffect());
				serializer.endTag(null, "specialEffect");

				serializer.endTag(null, "VideoFile");
			}

			serializer.endTag(null, "VideoFileList");
			serializer.endDocument();

			serializer.flush();
			fileOPStream.close();

			return true;
		} catch (Exception e) {

			Log.e("XmlParserUtil", "error occurred while creating xml file");
			return false;
		}
	}
	/**
	 * ��ȡ xml�����ļ� ��·��
	 * 
	 * @return
	 */
	public static String getXmlFilePath() {
		File xmlFile = new File(Environment.getDataDirectory(),
				"/data/com.szaoto.ak10/files/config/videofilelist.xml");
		return xmlFile.getPath();
	}

	/********************** ���岿�� ***************************/
	/**
	 * ��ȡUSB�ϵ�xml�ļ�·��
	 * 
	 * @return
	 */
	public static String getUsbXmlFilePath() {
//		File xmlFile = new File(Environment.getExternalStorageDirectory(),
				File xmlFile = new File("/mnt/usbdisk/",
				"CabinetSeries.cbs");
		System.out.println("U��xml�ļ�·����" + xmlFile);
		return xmlFile.getPath();
	}
	/*
	 * ����������ȡ������Ϣ
	 */
	public static CabinetXml getCabinetInfoByIndex(int currSelectedPosition,
			String filePath) throws SAXException, IOException,
			ParserConfigurationException {
		CabinetXml cabinet = new CabinetXml();

		DocumentBuilderFactory documentBuilderFactory

		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		NodeList nodeList = document.getElementsByTagName("Cabinet");

		Node node = nodeList.item(currSelectedPosition);

		Node childNode = null;
		int nNodeCount = node.getChildNodes().getLength();
		for (int i = 0; i < nNodeCount; i++) {
			childNode = node.getChildNodes().item(i);
			/**
			 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs Cabinet.cbt�ṹ��
			 * <ID>85</ID> <SeriesID>80</SeriesID> <Name>FS10J</Name>
			 * 
			 */

			if (childNode.getNodeName().trim().equals("ID")) {
				cabinet.setnID(Integer.parseInt(childNode.getTextContent()
						.toString()));
			}
			if (childNode.getNodeName().trim().equals("nSeriesID")) {
				cabinet.setnSeriesID(Integer.parseInt(childNode
						.getTextContent().toString()));
			}
			if (childNode.getNodeName().trim().equals("nSeriesName")) {
				cabinet.setsSeriesName(childNode.getTextContent().toString());
			}
			if (childNode.getNodeName().trim().equals("sName")) {
				cabinet.setsName(childNode.getTextContent().toString());
			}

			if (childNode.getNodeName().trim().equals("nAddress")) {
				cabinet.setnAddress(Integer.parseInt(childNode.getTextContent()
						.toString()));
			}
			
		}

		return cabinet;
	}
	/*
	 * ��U���ϵ������ļ����Ƶ�data/com.szaoto.ak10/files/configĿ¼
	 */
	public static void CopyCabinetXmlFile(Context context) {
		try {
			File xmlFile = new File(Environment.getDataDirectory(),
					"/data/com.szaoto.ak10/files/config/Cabinet.cbt");
		//	Log.d(TAG, "�Ѿ������������ļ�������ͬ��");
		//	if (!xmlFile.exists()) {
		//		System.out.println("�����������ļ�������һ��cabinet.cbt�ļ�");
				File cabinetXml = new File("/mnt/usbdisk/","Cabinet.cbt");
				InputStream  xmlInputStream = new FileInputStream(cabinetXml) ;
				xmlFile.getParentFile().mkdirs();
				xmlFile.createNewFile();
				FileOutputStream out = new FileOutputStream(xmlFile);
				byte[] buffer = new byte[1024];
				int byteCount = -1;
				while ((byteCount = xmlInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, byteCount);
				}
				xmlInputStream.close();
				out.close();
			//}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/*
	 * ͨ�������������ϵ��
	 */
	public static CabinetSeries getCabinetSeriesInfoByIndex(
			int currSelectedPosition, String filePath) throws Exception {
		CabinetSeries cabinetSeries = new CabinetSeries();
		DocumentBuilderFactory documentBuilderFactory
		= DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();

		Document document = documentBuilder.parse(new File(filePath));

		NodeList nodeList = document.getElementsByTagName("CabinetSeries");

		Node node = nodeList.item(currSelectedPosition);

		Node childNode = null;
       //ѭ�������ڵ�
       int nNodeCount = node.getChildNodes().getLength();
		for (int i = 0; i < nNodeCount; i++) {
			childNode = node.getChildNodes().item(i);
			/**
			 * �˴���Ҫ�����������ļ��ǣ�Cabinet.cbt��CabinetSeries.cbs return null;
			 */
			if (childNode.getNodeName().trim().equals("ID")) {
				cabinetSeries.setID(Integer.parseInt(childNode.getTextContent()
						.toString()));
				System.out.println("�ӽڵ�ID����");
			}
			if (childNode.getNodeName().trim().equals("parentID")) {
				cabinetSeries.setParentID(Integer.parseInt(childNode
						.getTextContent().toString()));
				System.out.println("���ڵ㿪ʼ����");
			}
			if (childNode.getNodeName().trim().equals("name")) {
				cabinetSeries.setName(childNode.getTextContent().toString());
				System.out.println("����ϵ�����ƽ���");
			}
		}
		return cabinetSeries;
	}
	// ������Ҫ����������ϵ���ļ���dataĿ¼
	public static void CopyCabinetSeriesXmlFile(Context context) {
		try {
			File xmlFile = new File(Environment.getDataDirectory(),
					"/data/com.szaoto.ak10/files/config/CabinetSeries.cbs");
	//		System.out.println("�Ѿ���������ϵ�����ļ�������ͬ��" + xmlFile);
	//		if (!xmlFile.exists()) {
				 File CabinetXml = new File(getUsbXmlFilePath());
				InputStream xmlInputStream = new FileInputStream(CabinetXml);
   //			InputStream xmlInputStream = context.getResources().getAssets()
//						.open("CabinetSeries.cbs");
				System.out.println("û������ϵ���ļ������¸���һ��"+CabinetXml);
				xmlFile.getParentFile().mkdirs();
				xmlFile.createNewFile();
				FileOutputStream out = new FileOutputStream(xmlFile);
				byte[] buffer = new byte[1024];
				int byteCount = -1;
				while ((byteCount = xmlInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, byteCount);
				}
				xmlInputStream.close();
				out.close();
	//		}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


}