/*
   * �ļ��� CabinetLibraryActivity.java
   * ���������б�com.szaoto.ak10.leddisplay
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:22
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.leddisplay;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.ExternalStorageService;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.entity.CabinetSeries;
import com.szaoto.ak10.entity.CabinetXml;
import com.szaoto.ak10.treeview.TreeElement;
import com.szaoto.ak10.treeview.TreeElementParser;
import com.szaoto.ak10.treeview.TreeView;
import com.szaoto.ak10.treeview.TreeView.LastLevelItemClickListener;
import com.szaoto.ak10.treeview.TreeViewAdapter;
import com.szaoto.ak10.util.LibcbtUsbStatesReceiver;
/**
 * ����CabinetLibraryActivity
 * ���� liangdb
 * ��Ҫ����:��������,ͬ��U���е������ļ�������
 * ��������2013��11��8��
 * �޸����ڣ��޸�����
 * �޸���:zhangsj
 */
public class CabinetLibraryActivity extends Activity implements
	OnClickListener, IInfoChangeObserver {
	public MyHandler mhandler;
	public LibcbtUsbStatesReceiver usbstates;
	TextView CabinetLibUsb;
	Context context;
	TextView btn_cabinetback;
	TextView btn_synchro;
	List<CabinetSeries> CabinetSeriesList;
	List<CabinetXml> CabinetList;
	String strDeleteCabinet;
	TreeView treeView;
	String cabinetserisename;
	CabinetSeries cabinetSeries;
    int Filetype=0;
	CabinetInformation cabinet;
	boolean tempData;
	FileInputStream inCabinetSeries;
	FileInputStream inCabinets;
	String cabinetname;
	CabinetSeries sCabinetseries;
	TreeViewAdapter mTreeadapter;
	CustomProgressDialog MergeCabinetDiag;
	String sUSBFileCabinet=".cbt";
	String cBtFileName;
	String CONFIG_PATH;

//	private static String Usb_PATH ="/mnt/usbdisk/";  //���İ��°���Ը�·��
//	private static String Usb_PATH ="/mnt/usb/";  //���İ�ɰ汾����·��
	public  String Usb_PATH ;  //���İ�ɰ汾����·��
	private ImageView CbtLibUsbmount;
	private TextView btn_CablinetLib;
	private TextView btn_Cablinet;
	static String sFileCabinet = "Cabinet.cbt";
	static String sFileCabinets= "CabinetSeries.cbs";
	static boolean cabinets;
	public void MsgShow(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leddisplay_cabinetlibrary);
		SerialPortControlBroadCast.SetCurrentContext(this);
		ExternalStorageService.observers.add(this);
		mhandler = new MyHandler();
		usbstates = new LibcbtUsbStatesReceiver(this);
		CabinetLibUsb = (TextView)findViewById(R.id.cbtlibusbstates);
		CbtLibUsbmount = (ImageView)findViewById(R.id.cbtlibusbmounted);
		btn_cabinetback = (TextView) findViewById(R.id.btn_cabinetback);
		btn_synchro = (TextView) findViewById(R.id.btn_synchro);
		btn_CablinetLib = (TextView) findViewById(R.id.btn_cabinetserieslib);
		btn_Cablinet = (TextView) findViewById(R.id.btn_cabinetlib);
		btn_cabinetback.setOnClickListener(this);
		btn_CablinetLib.setOnClickListener(this);
		btn_Cablinet.setOnClickListener(this);
		btn_synchro.setOnClickListener(this);
	
	}

	@Override
	protected void onResume() {
		try {
			TreeViewLoadData(); // ��ʼ������ͼ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	// ��������ͼ����
	private void TreeViewLoadData() throws Exception {
		 ReadXmlFileData();
		 UpdateTreeViewData();
		}

	public void UpdateTreeViewData() {
		treeView = (TreeView) findViewById(R.id.tree_view);
		// ��������Դ,�ֱ�������ϵ������Ӧ�������ļ�
		List<String> treeElementsString = IntegerateList(CabinetList,CabinetSeriesList);
		
		// �����������ļ�����
		List<TreeElement> treeElements = TreeElementParser.getTreeElements(treeElementsString);
		LastLevelItemClickListener itemClickCallBack = new LastLevelItemClickListener() {

			@Override
			public void onLastLevelItemClick(int position,  //�������������Ŀ����ʾ��ϸ��Ϣ
					TreeViewAdapter adapter) {
				// TODO Auto-generated method stub
				TreeElement element = (TreeElement) adapter.getItem(position);
				Intent intent = new Intent(CabinetLibraryActivity.this,
						CabinetInfomationActivity.class);
				intent.putExtra("CabinetName", element.getTitle());	startActivity(intent);
				//Toast.makeText(getApplicationContext(), element.getTitle(), 300)
				//		.show();
			}
			@Override
			public void onLastLevelItemLongClick(int position,
					TreeViewAdapter adapter) {
						 			  	
				  TreeElement element = (TreeElement) adapter.getItem(position);
				   strDeleteCabinet=element.getTitle();
				   dialogDeleteShow(strDeleteCabinet);  //����ɾ���ڵ�����
//			       ContextMenuShow(strDeleteCabinet);
		       		
			}
		};
		treeView.initData(this, treeElements);// ��ʼ������
		treeView.setLastLevelItemClickCallBack(itemClickCallBack); // ���ýڵ����¼�����
	}

  @Override
   public void onCreateContextMenu(ContextMenu menu, View v,
		ContextMenuInfo menuInfo) {
	// TODO Auto-generated method stub
	  menu.setHeaderTitle("�޸������ɾ��");
	  menu.add(0, 1, Menu.NONE, "�޸�����");
	  menu.add(0, 2, Menu.NONE, "ɾ��");
//	super.onCreateContextMenu(menu, v, menuInfo);
	 
   }
  @Override
public boolean onContextItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	  AdapterContextMenuInfo menuInfo= (AdapterContextMenuInfo) item.getMenuInfo();
	  switch (item.getItemId()) {
	case 1:
		
		break;
	case 2:
//	  dialogDeleteShow(item.getItemId());	
		
		break;

	default:
		break;
	}
	return super.onContextItemSelected(item);
	
}

	public void ReadXmlFileData() throws FileNotFoundException, Exception {
		InputStream inSeries;   //����ϵ��
		InputStream inCabinet; //�����ļ�
		CONFIG_PATH = this.getFilesDir() + "//config//";
		// File xmlSeries= new File("/mnt/usb/","CabinetSeries.cbs");
		// inSeries = new FileInputStream(xmlSeries);
		// File xmlCabinet= new File("/mnt/usb/","Cabinet.cbt");
		File xmlSeries = new File(CONFIG_PATH, "CabinetSeries.cbs");
		inSeries = new FileInputStream(xmlSeries); //����ϵ���ļ�
		File xmlCabinet = new File(CONFIG_PATH, "Cabinet.cbt");
		inCabinet = new FileInputStream(xmlCabinet); //�����ļ�
		if (!xmlSeries.exists()||!xmlCabinet.exists()) {
			Toast.makeText(getApplication(), "û������ϵ�к������ļ�����ͬ��", 200).show();
		}
		CabinetList = pullXmlToCabinetList(inCabinet);  //�������������ļ�
		CabinetSeriesList = pullXmlToCabinetSeriesList(inSeries); //��������ϵ��
	}
	//����ɾ���ڵ�Ի���
	protected void dialogDeleteShow( final String strEleTitle) {
		// ���������Ĳ����ļ�
	
//		LayoutInflater inflater = (LayoutInflater) CabinetLibraryActivity.this
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// �����ĶԻ���
		new AlertDialog.Builder(CabinetLibraryActivity.this)
				/* �������ڵ�����ͷ���� */
				.setTitle(R.string.text_delcabinet)
				/* ���õ������ڵ�ͼʽ */
				.setIcon(android.R.drawable.ic_dialog_info)
				/* ���õ������ڵ���Ϣ */
				.setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
                       //����Ҫɾ�������ݷ���
							CabinetDeleteTask cabinetDeleteTask = new CabinetDeleteTask();
							cabinetDeleteTask.execute();	
					}
				})
				.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
					public void onClick(DialogInterface dialoginterface, int i) {
						Toast.makeText(CabinetLibraryActivity.this, R.string.text_cabinetdel_canceltitle,Toast.LENGTH_SHORT).show();
					}
				}).show();
	}
	// �����б����ݣ��õ�����ͼ��Ҫ���ַ��б�����
	private List<String> IntegerateList(List<CabinetXml> cabinetList,List<CabinetSeries> cabinetSeriesList) {
		List<String> list = new ArrayList<String>();
		// PID-1-PName-false-true-false-null
		// SID-2-SName-false-false-true-PID
		String PStr = " ";   //���ڵ�
		String SStr = " ";   
		int PID, SID;
		for (int i = 0; i < cabinetSeriesList.size(); i++) {
			PID = cabinetSeriesList.get(i).getID();     // ��ȡ����ϵ��ID
			PStr = PID + "=1=" + cabinetSeriesList.get(i).getName()
					+ "=false=true=false=null";
			list.add(PStr);
			for (int j = 0; j < cabinetList.size(); j++) {  //ѭ�������ļ�
				SID = 0;
				SID = cabinetList.get(j).getnSeriesID(); // ��ȡ����ϵ��ID
				if (PID == SID) { // ������ڵ�ID��ϵ��ID���
					                     //�ϲ�2���ļ�Ϊ���νṹ
					SStr = cabinetList.get(j).getnID() + "=2="
							+ cabinetList.get(j).getsName()
							+ "=false=false=true=" + PID;
					list.add(SStr);
				}
			}
		}
		return list;
	}

	// ��������ϵ��xml�ļ�
	private List<CabinetSeries> pullXmlToCabinetSeriesList(InputStream in)
			throws Exception {
		List<CabinetSeries> list = null; // ����Ҫ��ʾ������ϵ�м���
		CabinetSeries cabinetSeries = null; // ��ʼ������ϵ��
		// ��������ʵ��
		XmlPullParser parser = Xml.newPullParser();
		// ������������ָ������
		parser.setInput(in, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				System.out.println("��ʼ��ͷ�ļ�����");
				list = new ArrayList<CabinetSeries>();
				break;
			// �ж��Ƿ�Ϊ��ǩ��ʼԪ��
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("CabinetSeries")) {
					cabinetSeries = new CabinetSeries();
				} else if (parser.getName().equals("ID")) {
					eventType = parser.next(); // �õ����������ֵ
					// cabinetSeries.setcID(Integer.parseInt(parser.getText()));
					cabinetSeries.setID(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("ParentID")) {
					eventType = parser.next(); // �õ����������ֵ
					// cabinet.setSeriesID(Integer.parseInt(parser.getText()));
					cabinetSeries.setParentID(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("name")) {
					eventType = parser.next(); // �õ����������ֵ
					// cabinet.setcName(parser.getText());
					cabinetSeries.setName(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("CabinetSeries")) { // �жϽ�����ǩԪ���Ƿ���CabinetSeries
					list.add(cabinetSeries); // ��cabinetSeries���ӵ�CabinetSeries����
					cabinetSeries = null;
				}
				break;
			}
			// ������һ��Ԫ�ز�������Ӧ�¼�
			eventType = parser.next();
		}
		return list;
	}
	// ���������ļ�
	private List<CabinetXml> pullXmlToCabinetList(InputStream in) throws Exception {
		List<CabinetXml> list = null; // ����Ҫ��ʾ�����弯��
		CabinetXml cabinet = null; // ��ʼ������
		// ��������ʵ��
		XmlPullParser parser = Xml.newPullParser();
		// ������������ָ������
		parser.setInput(in, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			// �ж��Ƿ�Ϊ�ĵ���ʼ
			case XmlPullParser.START_DOCUMENT:
				System.out.println("��ʼ��ͷ�ļ�����");
				list = new ArrayList<CabinetXml>();
				break;
			// �ж��Ƿ�Ϊ��ǩ��ʼԪ��
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("Cabinet")) {
					cabinet = new CabinetXml();
				} else if (parser.getName().equals("ID")) {
					eventType = parser.next(); // �õ����������ֵ
					cabinet.setnID(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("SeriesID")) {
					eventType = parser.next(); // �õ����������ֵ
					cabinet.setnSeriesID(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("Name")) {
					eventType = parser.next(); // �õ����������ֵ
					cabinet.setsName(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("Cabinet")) { // �жϽ�����ǩԪ���Ƿ���Cabinet
					list.add(cabinet); // ��cabinet���ӵ�Cabinet����
					cabinet = null;
				}
				break;
			}
			// ������һ��Ԫ�ز�������Ӧ�¼�
			eventType = parser.next();
		}
		return list;
	}
	// ��ʼ���ؼ�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_synchro:
			startActivity(new Intent(CabinetLibraryActivity.this, CabinetSelectActivity.class));
			finish();
			break;
		// ����
		case R.id.btn_cabinetback:
			startActivity(new Intent(CabinetLibraryActivity.this,LedSelActivity.class));
			finish();
			break;
		case R.id.btn_cabinetserieslib:
			startActivity(new Intent(CabinetLibraryActivity.this,CabinetLibraryManage.class));
			finish();
			break;
		case R.id.btn_cabinetlib:
			startActivity(new Intent(CabinetLibraryActivity.this,CabinetManageActivity.class));
			finish();
			break;
		default:
			break;
		}
	}
	
	  //�ϲ������첽����ˢ�½���
//	  class CabinetMergeTask extends AsyncTask<Integer, Integer, String>{
//				 public CabinetMergeTask() {
//		     }
//				 @Override
//			protected void onPreExecute() {
//				// TODO Auto-generated method stub
//				super.onPreExecute();
//				MergeCabinetDiag.show();
//			}
//			@Override
//			protected void onProgressUpdate(Integer... values) {
//			// TODO Auto-generated method stub
//			super.onProgressUpdate(values);
//			}	 
//			@Override
//			protected String doInBackground(Integer... params) {
//				//���ھͽ��жԱȣ�Ȼ��ϲ�,д�뵽�����ļ�����ȥ
//				//��ȡ����������ϵ����Ϣ
//				List<String> LocalCabinetSeries=DataAccessCabinetLibrary.getCabinetSeriseNames(0);
//             //��ȡ�������ļ��������������
//				List<String> LocalCabinet=DataAccessCabinetLibrary.getCabinetNames(0);
//				//��ȡ��U�������
//				List<String> UsbCabinet=DataAccessCabinetLibrary.getCabinetNames(1);
//				//�Ա�2���ļ�����U�����治ͬ ���������ƺϲ��������ļ���
//				 //���ݴ���
//					try {
//						isMergingCabinet(LocalCabinet, UsbCabinet);
//						 ReadXmlFileData();
//						//Toast.makeText(getApplication(), "���ݺϲ����", Toast.LENGTH_SHORT).show();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				return null;
//			}
			
//			@Override
//			protected void onPostExecute(String result) {
//			//�������
//				try {
//					//TreeViewLoadData();
//					UpdateTreeViewData();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				MergeCabinetDiag.dismiss();
//			    super.onPostExecute(result);
//			}
//		}		
	  //ɾ���ڵ��첽����ˢ�½���
	  class CabinetDeleteTask extends AsyncTask<Integer, Integer, String>{
		  public CabinetDeleteTask() {
		  }
		  @Override
		  protected String doInBackground(Integer... params) {
			  //���ݴ���
			  try {
				  DataAccessCabinetLibrary.RemoveCabinet( strDeleteCabinet); //ɾ������
				  ReadXmlFileData();
				  //Toast.makeText(getApplication(), "���ݺϲ����", Toast.LENGTH_SHORT).show();
			  } catch (Exception e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  return null;
		  }
		  @Override
		  protected void onPostExecute(String result) {
			  //�������
			  try {
				  //TreeViewLoadData();
				  UpdateTreeViewData();
			  } catch (Exception e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  super.onPostExecute(result);
		  }
	  }		
//	// �Ƚϱ��غ�U�������Cabinet.cbt�ļ�,����ͬ���ݺϲ�
//	public void isMergingCabinet(List<String> localCabinet,List<String> usbCabinet) throws Exception {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = null;
//		try {
//			db = dbf.newDocumentBuilder();
//		} catch (ParserConfigurationException pce) {
//			System.err.println(pce); // �����쳣ʱ������쳣��Ϣ
//		}
//		org.w3c.dom.Document doc_loaclCabinet = null; // ��������
//		org.w3c.dom.Document doc_loaclSeries = null; // ��������ϵ��
//		org.w3c.dom.Document doc_usbdisk = null; // U������
//		
//		TraverseDictionary TD = new TraverseDictionary();
//		
//		Usb_PATH = TD.GetUDiskDir();
//		if (Usb_PATH == null) {
//			return;
//		}
//		//�˴���ȡU������.cbt��β�������ļ�
//		TD.GetFiles(Usb_PATH, sUSBFileCabinet, true);
//		List<String> listNamecbt = TD.getLstFile();
//		for (String string : listNamecbt) {
//			cBtFileName =string;
//		}
//		// ��ȡ����XML�ļ���Document
//		try {
//			// doc_main = db.parse(mainFileName);
//			doc_loaclCabinet = db.parse(new File(HomePageActivity.CONFIG_PATH,	sFileCabinet));
//			doc_loaclSeries = db.parse(new File(HomePageActivity.CONFIG_PATH,	sFileCabinets));
//			doc_usbdisk = db.parse(new File(Usb_PATH, cBtFileName));
//		} catch (DOMException dom) {
//			System.err.println(dom.getMessage());
//		} catch (Exception ioe) {
//			System.err.println(ioe);
//		}
//		// ��ȡ�����ļ�Cabinet.cbt��SeriesID��Name
//		// int SeriesID=cabinet.getnSeriesID();
//		// List<String> listSeries
//		// =DataAccessCabinetLibrary.getCabinetNamesbyseriseID(SeriesID, 0);
//		// ��ȡ�����ļ��ĸ��ڵ�
//		Element root_localCbt = doc_loaclCabinet.getDocumentElement(); // �����ļ��ĸ��ڵ�
//		Element root_localCbtSeries = doc_loaclSeries.getDocumentElement(); // ��������ϵ���ļ��ĸ��ڵ�
//		Element root_uDisk = (Element) doc_usbdisk.getDocumentElement(); // U���ļ��ĸ��ڵ�
//
//		// ����ϵ���ļ�
//		NodeList localSeriesNodes = root_localCbtSeries.getElementsByTagName("CabinetSeries");
//		int local_cbtSeriesNum = localSeriesNodes.getLength();
//		String strlocCabinetSeriesID = null;
//		List<CabinetSeries> listSeries = new ArrayList<CabinetSeries>(); // ��ű�������ϵ�е�ID��name
//		for (int i = 0; i < local_cbtSeriesNum; i++) {
//			Element localCbtSeries = (Element) localSeriesNodes.item(i);
//			Element itemCbtSeriesID = (Element) localCbtSeries
//					.getElementsByTagName("ID").item(0);
//			strlocCabinetSeriesID = itemCbtSeriesID.getFirstChild().getNodeValue();
//			int CbtSeriesID = Integer.parseInt(strlocCabinetSeriesID);
//			listSeries.add(new CabinetSeries(CbtSeriesID));
//		}
//
//		// ��������ڵ�
//		NodeList LocalpersonNodes = root_localCbt
//				.getElementsByTagName("Cabinet");
//		// �õ������ļ�Cabinet.cbt����Ľڵ�ID��SeriesID
//		int local_cabinetNum = LocalpersonNodes.getLength();
//		// �õ������ļ��е�����ڵ�����
//		String strLSeriesName = null;
//		String strLocalSeriesID = null;
//		List<CabinetInformation> listCbt = new ArrayList<CabinetInformation>(); // ��ű��������SeriesID������Name
//		for (int i = 0; i < local_cabinetNum; i++) {
//			Element localeleCbt = (Element) LocalpersonNodes.item(i);
//			Element itemSeriesID = (Element) localeleCbt.getElementsByTagName(
//					"SeriesID").item(0);
//			strLocalSeriesID = itemSeriesID.getFirstChild().getNodeValue();
//			int SeriesID = Integer.parseInt(strLocalSeriesID);
//			Element itemSeriesName = (Element) localeleCbt
//					.getElementsByTagName("Name").item(0);
//			strLSeriesName = itemSeriesName.getFirstChild().getNodeValue();
//			listCbt.add(new CabinetInformation(SeriesID, strLSeriesName));
//		}
//		// �õ�U��������ڵ�
//		NodeList usbNodeList = root_uDisk.getElementsByTagName("Cabinet");
//		int item_number = usbNodeList.getLength();
//		String strUSBSeriesID = null;
//		String strUSBcbtName = null;
//		// �õ�U��Cabinet.cbt�еĽڵ�ID��SeriesID
//		List<CabinetInformation> listUsb = new ArrayList<CabinetInformation>(); // ��ű��������SeriesID������Name
//		for (int j = 0; j < item_number; j++) {
//			Element eleUSBCbt = (Element) usbNodeList.item(j);
//			Element itemUSeriesID = (Element) eleUSBCbt.getElementsByTagName(
//					"SeriesID").item(0);
//			strUSBSeriesID = itemUSeriesID.getFirstChild().getNodeValue();
//			int USeriesID = Integer.parseInt(strUSBSeriesID);
//			Element itemUSBSeriesName = (Element) eleUSBCbt
//					.getElementsByTagName("Name").item(0);
//			strUSBcbtName = itemUSBSeriesName.getFirstChild().getNodeValue();
//			listUsb.add(new CabinetInformation(USeriesID, strUSBcbtName));
//		}
//		// �Ƚ�U������ �� Cabinet��SeriesName�ͱ����ļ��е�SeriesName
//		// ���һ�����ǾͲ����ӣ���һ�����Ǿ�����
//		// �Ƚ�cbt.SeriesID==cbs.ID ���
//		// �ٱȽ�Lcbt.name==Ucbt.name,�����ȣ��ǾͲ������ӣ�
//		// ����ȣ��͸���Ucbt.name������U������ṹ������AddXml(cabinet)
//		//ͬ��ʱ���ظ��ľͲ�����
//		//���ȱ������нڵ㣬�����Ƿ�����ȫ��ͬ�Ľڵ㣬��ֹͬһ�ڵ��Ѷ�����   
//			    CompareXML(listSeries, listCbt, listUsb);
//	}
//     
	//�ж��Ƿ����ظ��Ĵ���
	public boolean IsNodeExist(	List<CabinetInformation> listCbt,String strName)
	{
		for (int i = 0; i < listCbt.size(); i++) {
			if(listCbt.get(i).getsName().equals(strName))
				{
                  return true;
				}
		}

		return false;
	}	
//	private void   CompareXML(List<CabinetSeries> listSeries,	List<CabinetInformation> listCbt, List<CabinetInformation> listUsb) {
//			/****************************************/
//			for (int i = 0; i < listUsb.size(); i++) {
//				CabinetInformation cbtUsb = listUsb.get(i);
//				int SerID = cbtUsb.getnSeriesID();
//				String strUname = cbtUsb.getsName();
//				for (int j = 0; j < listCbt.size(); j++) {
//					if(IsNodeExist(listCbt,strUname))  //�ж��Ƿ�����ͬ�������
//					{
//						continue;
//					} else {
//						// ������
//						int k = 0;
//						for (; k < listSeries.size(); k++) {
//							CabinetSeries lcbs = listSeries.get(k);
//							if (SerID == lcbs.getID()) { // ϵ����ͬ
//							//��Ȼ��Ҫһ���ж�ϵ��ID���ڵķ�������ֹ�ظ�	
//								break;
//							}
//						}
//						if (k < listSeries.size()) {
//							
//							cabinets = DataAccessCabinetLibrary.getCabinetByname(
//									strUname, cbtUsb, 1);
//							DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
//							System.out.println("���ӵ���������" + strUname);
//							System.out.println("��ǰ�ⲿ�洢·��" + Usb_PATH);
//							break;
//						}
//						if (k == listSeries.size()) {
//							String stUname = cbtUsb.getsName();
//							int SeresID = cbtUsb.getnSeriesID();
//							String seriesName = "SERIES-" + SeresID;
//							DataAccessCabinetLibrary	.WriteXMLCabinetSeries(new CabinetSeries(SeresID, 0, seriesName));
//							System.out.println("���ӵ�����ϵ������" + seriesName);
//							System.out.println("��ǰ�ⲿ�洢·��" + Usb_PATH);
//							cabinets = DataAccessCabinetLibrary.getCabinetByname(	stUname, cbtUsb, 1);
//							DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
//							/*
//							 * ��ǰд�������ļ�ʱ�����ڣ�����д�벻�ɹ���ԭ����DataAccessCabinetLibrary����SetHUBLinkup���ݶ�ȡд��ʱ
//							 * UtilFun.bytes2HexString(Hublinktable.getUcLinkTable()ת��ʱ
//							 * ������ѭ�������ScanCardAttachment����������Ŀ������data��Ϊ0��ʱ���棬
//							 */
//							System.out.println("���ӵ���������" + strUname);
//							break;
//						}
//					}
//				}
//			}
//			/**************************************************/
//	}
	
  /**********��ȡU��·��*************/	
	/*public  String GetUDiskDir()
	{
		  String tPath ="/mnt/usbdisk/";  //���İ��°���Ը�·��
		  String tPathOldString ="/mnt/usb/" ;
		  File file = new File(tPath);
		  if (file.exists()) {
			return tPath;
		  }else {
			  File file1 = new File(tPathOldString);
			  if (file1.exists()) {
				 return tPathOldString;
			  }else {
				 Toast.makeText(getApplication(), getString(R.string.text_checkusb),Toast.LENGTH_SHORT).show();
				 return null;
			 }
		  }	
	}*/
	/*********************************/
	@Override
	// U��״̬�ı����
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
	    String string = xParam1;	
		Usb_PATH=xParam2;
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		usbstates.registerReceiver();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		usbstates.unregisterReceiver();
	}
	public class MyHandler extends Handler{
		public MyHandler(){
			
		};
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("=============mainactivity handler");
//			Usb.setText("usb states");
			if(msg.arg1 == 0x00021)	{
				CabinetLibUsb.setText(R.string.text_usbmount);
				CbtLibUsbmount.setVisibility(View.VISIBLE);
				CbtLibUsbmount.setBackgroundResource(R.drawable.usb_mounted);}
			else if(msg.arg1 == 0x00022){ 
				CabinetLibUsb.setText(R.string.text_usbunmount);
				CbtLibUsbmount.setVisibility(View.VISIBLE);
				CbtLibUsbmount.setBackgroundResource(R.drawable.usb_unmounted);}
				}
		}
}