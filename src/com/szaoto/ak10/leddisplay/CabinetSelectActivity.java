package com.szaoto.ak10.leddisplay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.entity.CabinetSeries;
import com.szaoto.ak10.util.TraverseDictionary;
/**
 * ����CabinetSelectActivity
 * ��Ҫ����:��������,�����ļ�ͬ��
 * ��������2015��5��28��
 * �޸����ڣ��޸�����
 * ���� :zhangsj
 */
public class CabinetSelectActivity extends Activity {

	private TextView btn_Back;
	private Button btn_Submit;
	private Button btn_Close;
	private String Usb_PATH;
	public static String CONFIG_PATH;
	private String sCabinetfile = ".cbt";
	private ListView listview_Cabinet;
	ArrayList<String> CabinetPathInfos = new ArrayList<String>();

	ArrayList<String> mSelCabinetPathInfos = new ArrayList<String>();
	String m_cbtFilePath;
	CustomProgressDialog MergeCabinetDiag;
	private static String sFileCabinet = "Cabinet.cbt";
	private static String sFileCabinets = "CabinetSeries.cbs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cabinet_select);
		SerialPortControlBroadCast.SetCurrentContext(this);
		initView();
		initData();
	}

	private void initView() {
		

		btn_Back = (TextView) findViewById(R.id.btn_selcabinetback);
		btn_Submit = (Button) findViewById(R.id.btnCabinetSubmit);
		btn_Close = (Button) findViewById(R.id.btnCabinetClose);
		listview_Cabinet = (ListView) findViewById(R.id.listViewcabinetFilePath);

		btn_Back.setOnClickListener(ClickHandler);
		btn_Submit.setOnClickListener(ClickHandler);
		btn_Close.setOnClickListener(ClickHandler);
		listview_Cabinet.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				m_cbtFilePath = CabinetPathInfos.get(position);
			}

		});
	}

	View.OnClickListener ClickHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_selcabinetback:
				startActivity(new Intent(CabinetSelectActivity.this,CabinetLibraryActivity.class));
				finish();
				break;
			case R.id.btnCabinetSubmit:
				// ѡ�������뱾��������кϲ�
				if (m_cbtFilePath == null) {
					Toast.makeText(btn_Submit.getContext(), getString(R.string.text_fileselecttips),Toast.LENGTH_LONG).show();
					return;
				}
				// ѡ����ļ�
				SparseBooleanArray sparseBooleanArray = listview_Cabinet.getCheckedItemPositions();
				for (int i = 0; i < CabinetPathInfos.size(); i++) {
					if (sparseBooleanArray.get(i)) {
						mSelCabinetPathInfos.add(CabinetPathInfos.get(i));
					}
				}
				MergeCabinetDiag = new CustomProgressDialog(
						CabinetSelectActivity.this,
						getString(R.string.text_mergcabinet),
						getString(R.string.text_load), false);
				CabinetMergeTask cabinetMergeTask = new CabinetMergeTask(mSelCabinetPathInfos);
				cabinetMergeTask.execute();
				break;
			case R.id.btnCabinetClose:
				startActivity(new Intent(CabinetSelectActivity.this,CabinetLibraryActivity.class));
				finish();
				break;
			default:
				break;
			}
		}
	};

	// �ϲ������첽����ˢ�½���
	class CabinetMergeTask extends AsyncTask<Integer, Integer, String> {

		ArrayList<String> arrSelFilePathArrayList = new ArrayList<String>();

		public CabinetMergeTask(ArrayList<String> tArrayList) {
			arrSelFilePathArrayList = tArrayList;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			MergeCabinetDiag.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(Integer... params) {
			// ��ȡ�������ļ��������������
			List<String> LocalCabinet = DataAccessCabinetLibrary.getCabinetNames(0);
			// ��ȡ��U�������
			for (int i = 0; i < arrSelFilePathArrayList.size(); i++) {String strFilePath = arrSelFilePathArrayList.get(i);
				try {
					isMergingCabinet(LocalCabinet, strFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// �Ա�2���ļ�����U�����治ͬ ���������ƺϲ��������ļ���
			// ���ݴ���
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// �������
			MergeCabinetDiag.dismiss();
			
			//�����
			CabinetAddActivity.loadModelData();
			
			Toast.makeText(CabinetSelectActivity.this, getString(R.string.datloadtip), Toast.LENGTH_LONG).show();
			
			super.onPostExecute(result);
		}
	}

	private void initData() {
		TraverseDictionary TD = new TraverseDictionary();
		Usb_PATH = TraverseDictionary.GetUDiskDir();
		if (null != Usb_PATH) {
			TD.GetFilePaths(Usb_PATH, sCabinetfile, true);
			CabinetPathInfos = (ArrayList<String>) TD.getLstFilePath();
			listview_Cabinet.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice,
					CabinetPathInfos));
			listview_Cabinet.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
	}

	// ///////////////////////
	// �Ƚϱ��غ�U�������Cabinet.cbt�ļ�,����ͬ���ݺϲ�
	public void isMergingCabinet(List<String> localCabinet,
			String strcbtFilePath) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce); // �����쳣ʱ������쳣��Ϣ
			return;
		}
		org.w3c.dom.Document doc_loaclCabinet = null; // ��������
		org.w3c.dom.Document doc_loaclSeries = null; // ��������ϵ��
		org.w3c.dom.Document doc_usbdisk = null; // U������

		// �˴���ȡU������.cbt��β�������ļ�
		// ��ȡ����XML�ļ���Document
		try {
			// doc_main = db.parse(mainFileName);
			doc_loaclCabinet = db.parse(new File(HomePageActivity.CONFIG_PATH,sFileCabinet));
			doc_loaclSeries = db.parse(new File(HomePageActivity.CONFIG_PATH,sFileCabinets));
			doc_usbdisk = db.parse(new File(strcbtFilePath));
		} catch (DOMException dom) {
			System.err.println(dom.getMessage());
			return;
		} catch (Exception ioe) {
			System.err.println(ioe);
			return;
		}
		// ��ȡ�����ļ�Cabinet.cbt��SeriesID��Name
		// int SeriesID=cabinet.getnSeriesID();
		// List<String> listSeries
		// =DataAccessCabinetLibrary.getCabinetNamesbyseriseID(SeriesID, 0);
		// ��ȡ�����ļ��ĸ��ڵ�
		Element root_localCbt = doc_loaclCabinet.getDocumentElement(); // �����ļ��ĸ��ڵ�
		Element root_localCbtSeries = doc_loaclSeries.getDocumentElement(); // ��������ϵ���ļ��ĸ��ڵ�
		Element root_uDisk = (Element) doc_usbdisk.getDocumentElement(); // U���ļ��ĸ��ڵ�

		// ����ϵ���ļ�
		NodeList localSeriesNodes = root_localCbtSeries
				.getElementsByTagName("CabinetSeries");
		int local_cbtSeriesNum = localSeriesNodes.getLength();
		String strlocCabinetSeriesID = null;
		List<CabinetSeries> listSeries = new ArrayList<CabinetSeries>(); // ��ű�������ϵ�е�ID��name
		for (int i = 0; i < local_cbtSeriesNum; i++) {
			Element localCbtSeries = (Element) localSeriesNodes.item(i);
			Element itemCbtSeriesID = (Element) localCbtSeries
					.getElementsByTagName("ID").item(0);
			strlocCabinetSeriesID = itemCbtSeriesID.getFirstChild()
					.getNodeValue();
			int CbtSeriesID = Integer.parseInt(strlocCabinetSeriesID);
			listSeries.add(new CabinetSeries(CbtSeriesID));
		}
		// ��������ڵ�
		NodeList LocalpersonNodes = root_localCbt
				.getElementsByTagName("Cabinet");
		// �õ������ļ�Cabinet.cbt����Ľڵ�ID��SeriesID
		int local_cabinetNum = LocalpersonNodes.getLength();
		// �õ������ļ��е�����ڵ�����
		String strLSeriesName = null;
		String strLocalSeriesID = null;
		List<CabinetInformation> listCbt = new ArrayList<CabinetInformation>(); // ��ű��������SeriesID������Name
		for (int i = 0; i < local_cabinetNum; i++) {
			Element localeleCbt = (Element) LocalpersonNodes.item(i);
			Element itemSeriesID = (Element) localeleCbt.getElementsByTagName("SeriesID").item(0);
			strLocalSeriesID = itemSeriesID.getFirstChild().getNodeValue();
			int SeriesID = Integer.parseInt(strLocalSeriesID);
			Element itemSeriesName = (Element) localeleCbt
					.getElementsByTagName("Name").item(0);
			strLSeriesName = itemSeriesName.getFirstChild().getNodeValue();
			listCbt.add(new CabinetInformation(SeriesID, strLSeriesName));
		}
		// �õ�U��������ڵ�
		NodeList usbNodeList = root_uDisk.getElementsByTagName("Cabinet");
		int item_number = usbNodeList.getLength();
		String strUSBSeriesID = null;
		String strUSBcbtName = null;
		// �õ�U��Cabinet.cbt�еĽڵ�ID��SeriesID
		List<CabinetInformation> listUsb = new ArrayList<CabinetInformation>(); // ��ű��������SeriesID������Name
		for (int j = 0; j < item_number; j++) {
			Element eleUSBCbt = (Element) usbNodeList.item(j);
			Element itemUSeriesID = (Element) eleUSBCbt.getElementsByTagName(
					"SeriesID").item(0);
			strUSBSeriesID = itemUSeriesID.getFirstChild().getNodeValue();
			int USeriesID = Integer.parseInt(strUSBSeriesID);
			Element itemUSBSeriesName = (Element) eleUSBCbt
					.getElementsByTagName("Name").item(0);
			strUSBcbtName = itemUSBSeriesName.getFirstChild().getNodeValue();
			listUsb.add(new CabinetInformation(USeriesID, strUSBcbtName));
		}
		// �Ƚ�U������ �� Cabinet��SeriesName�ͱ����ļ��е�SeriesName
		// ���һ�����ǾͲ���ӣ���һ�����Ǿ����
		// �Ƚ�cbt.SeriesID==cbs.ID ���
		// �ٱȽ�Lcbt.name==Ucbt.name,�����ȣ��ǾͲ�����ӣ�
		// ����ȣ��͸���Ucbt.name������U������ṹ������AddXml(cabinet)
		// ͬ��ʱ���ظ��ľͲ����
		// ���ȱ������нڵ㣬�����Ƿ�����ȫ��ͬ�Ľڵ㣬��ֹͬһ�ڵ��Ѷ�����
		CompareXML(listSeries, listCbt, listUsb, strcbtFilePath);
	}

	private void CompareXML(List<CabinetSeries> listSeries,
			List<CabinetInformation> listCbt, List<CabinetInformation> listUsb,
			String strFilePath) {
		/****************************************/
		for (int i = 0; i < listUsb.size(); i++) {
			CabinetInformation cbtUsb = listUsb.get(i);
			int SerID = cbtUsb.getnSeriesID();
			String strUname = cbtUsb.getsName();
			// �������ϵ�к������ļ���ɾ���꣬Ϊ��ʱ��ֱ���������
			if (listSeries.size() == 0 || listCbt.size() == 0) {
				try {
					cbtUsb = DataAccessCabinetLibrary.getCabinetFromUDisk(
							strFilePath, strUname);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String seriesName = strUname+"_series";
				DataAccessCabinetLibrary
						.WriteXMLCabinetSeries(new CabinetSeries(
								SerID, 0, seriesName));
				/**************/
				DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
			
			}
			// ��������ʱ���ж����
			for (int j = 0; j < listCbt.size(); j++) {
				if (IsNodeExist(listCbt, strUname)) // �ж��Ƿ�����ͬ�������
				{
					continue;
				} else {
					// ������
					int k = 0;
					for (; k < listSeries.size(); k++) {
						CabinetSeries lcbs = listSeries.get(k);
						if (SerID == lcbs.getID()) { // ϵ����ͬ
							// ��Ȼ��Ҫһ���ж�ϵ��ID���ڵķ�������ֹ�ظ�
							break;
						}
					}
					if (k < listSeries.size()) {
						try {
							// ��ȡ����
							cbtUsb = DataAccessCabinetLibrary
									.getCabinetFromUDisk(strFilePath, strUname);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// DataAccessCabinetLibrary.getCabinetByname(strUname,
						// cbtUsb, 1);
						if (IsNodeExist(listCbt, strUname)) // �ж��Ƿ�����ͬ�������
						{
							continue;
							}else {
						DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
						System.out.println("��ӵ���������" + strUname);
						System.out.println("��ǰ�ⲿ�洢·��" + Usb_PATH);
						break;
						}
					}
					if (k == listSeries.size()) {
						String stUname = cbtUsb.getsName();
						int SeresID = cbtUsb.getnSeriesID();
						String seriesName = stUname+"_series";
						int localSeriesID=listSeries.get(i).getID();
						//�����ϵ�нڵ㣬���������ڵ�2015-08-12
						/**************/
						if (localSeriesID==SeresID) {   //��������ļ��е�ϵ��ID��U���������ϵ��ID��ͬ���Ͳ���Ӹ�ϵ��
							continue;
						}else {
						DataAccessCabinetLibrary
								.WriteXMLCabinetSeries(new CabinetSeries(
										SeresID, 0, seriesName));
						System.out.println("��ӵ�����ϵ������" + seriesName);
						System.out.println("��ǰ�ⲿ�洢·��" + Usb_PATH);
						// cabinets =
						// DataAccessCabinetLibrary.getCabinetByname(stUname,
						// cbtUsb, 1);
						try {
							cbtUsb = DataAccessCabinetLibrary
									.getCabinetFromUDisk(strFilePath, strUname);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (IsNodeExist(listCbt, strUname)) // �ж��Ƿ�����ͬ�������
						{
							continue;
							}else {
						DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
						/*
						 * ��ǰд�������ļ�ʱ�����ڣ�����д�벻�ɹ���
						 * ԭ����DataAccessCabinetLibrary����SetHUBLinkup���ݶ�ȡд��ʱ
						 * UtilFun
						 * .bytes2HexString(Hublinktable.getUcLinkTable()ת��ʱ
						 * ������ѭ�������ScanCardAttachment����������Ŀ������data��Ϊ0��ʱ���棬
						 */
						System.out.println("��ӵ���������" + strUname);
						break;
					}
					}
				}
				}
			}
		}
		/**************************************************/
	}

	// �ж��Ƿ����ظ��Ĵ���
	public boolean IsNodeExist(List<CabinetInformation> listCbt, String strName) {
		for (int i = 0; i < listCbt.size(); i++) {
			if (listCbt.get(i).getsName().equals(strName)) {
				return true;
			}
		}
		return false;
	}
}
