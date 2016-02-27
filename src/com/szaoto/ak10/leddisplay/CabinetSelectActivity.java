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
 * 类名CabinetSelectActivity
 * 主要功能:箱体库管理,箱体文件同步
 * 创建日期2015年5月28日
 * 修改日期，修改内容
 * 作者 :zhangsj
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
				// 选择箱体与本地箱体进行合并
				if (m_cbtFilePath == null) {
					Toast.makeText(btn_Submit.getContext(), getString(R.string.text_fileselecttips),Toast.LENGTH_LONG).show();
					return;
				}
				// 选择的文件
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

	// 合并箱体异步任务，刷新界面
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
			// 获取到本地文件里面的箱体名称
			List<String> LocalCabinet = DataAccessCabinetLibrary.getCabinetNames(0);
			// 获取到U盘里面的
			for (int i = 0; i < arrSelFilePathArrayList.size(); i++) {String strFilePath = arrSelFilePathArrayList.get(i);
				try {
					isMergingCabinet(LocalCabinet, strFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 对比2个文件，将U盘里面不同 的箱体名称合并到本地文件中
			// 数据处理
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// 界面更新
			MergeCabinetDiag.dismiss();
			
			//库更新
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
	// 比较本地和U盘里面的Cabinet.cbt文件,将不同内容合并
	public void isMergingCabinet(List<String> localCabinet,
			String strcbtFilePath) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce); // 出现异常时，输出异常信息
			return;
		}
		org.w3c.dom.Document doc_loaclCabinet = null; // 本地箱体
		org.w3c.dom.Document doc_loaclSeries = null; // 本地箱体系列
		org.w3c.dom.Document doc_usbdisk = null; // U盘箱体

		// 此处获取U盘中以.cbt结尾的箱体文件
		// 获取两个XML文件的Document
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
		// 获取本地文件Cabinet.cbt的SeriesID和Name
		// int SeriesID=cabinet.getnSeriesID();
		// List<String> listSeries
		// =DataAccessCabinetLibrary.getCabinetNamesbyseriseID(SeriesID, 0);
		// 获取两个文件的根节点
		Element root_localCbt = doc_loaclCabinet.getDocumentElement(); // 本地文件的根节点
		Element root_localCbtSeries = doc_loaclSeries.getDocumentElement(); // 本地箱体系列文件的根节点
		Element root_uDisk = (Element) doc_usbdisk.getDocumentElement(); // U盘文件的根节点

		// 箱体系列文件
		NodeList localSeriesNodes = root_localCbtSeries
				.getElementsByTagName("CabinetSeries");
		int local_cbtSeriesNum = localSeriesNodes.getLength();
		String strlocCabinetSeriesID = null;
		List<CabinetSeries> listSeries = new ArrayList<CabinetSeries>(); // 存放本地箱体系列的ID和name
		for (int i = 0; i < local_cbtSeriesNum; i++) {
			Element localCbtSeries = (Element) localSeriesNodes.item(i);
			Element itemCbtSeriesID = (Element) localCbtSeries
					.getElementsByTagName("ID").item(0);
			strlocCabinetSeriesID = itemCbtSeriesID.getFirstChild()
					.getNodeValue();
			int CbtSeriesID = Integer.parseInt(strlocCabinetSeriesID);
			listSeries.add(new CabinetSeries(CbtSeriesID));
		}
		// 本地箱体节点
		NodeList LocalpersonNodes = root_localCbt
				.getElementsByTagName("Cabinet");
		// 得到本地文件Cabinet.cbt里面的节点ID和SeriesID
		int local_cabinetNum = LocalpersonNodes.getLength();
		// 得到本地文件中的箱体节点数量
		String strLSeriesName = null;
		String strLocalSeriesID = null;
		List<CabinetInformation> listCbt = new ArrayList<CabinetInformation>(); // 存放本地箱体的SeriesID和箱体Name
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
		// 得到U盘中箱体节点
		NodeList usbNodeList = root_uDisk.getElementsByTagName("Cabinet");
		int item_number = usbNodeList.getLength();
		String strUSBSeriesID = null;
		String strUSBcbtName = null;
		// 得到U盘Cabinet.cbt中的节点ID和SeriesID
		List<CabinetInformation> listUsb = new ArrayList<CabinetInformation>(); // 存放本地箱体的SeriesID和箱体Name
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
		// 比较U盘里面 的 Cabinet的SeriesName和本地文件中的SeriesName
		// 如果一样，那就不添加，不一样，那就添加
		// 比较cbt.SeriesID==cbs.ID 相等
		// 再比较Lcbt.name==Ucbt.name,如果相等，那就不用添加，
		// 不相等，就根据Ucbt.name，读出U盘箱体结构，调用AddXml(cabinet)
		// 同步时有重复的就不添加
		// 首先遍历所有节点，查找是否有完全相同的节点，防止同一节点已定义多次
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
			// 如果箱体系列和箱体文件都删除完，为空时，直接添加数据
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
			// 存在数据时，判断添加
			for (int j = 0; j < listCbt.size(); j++) {
				if (IsNodeExist(listCbt, strUname)) // 判定是否有相同箱体存在
				{
					continue;
				} else {
					// 不存在
					int k = 0;
					for (; k < listSeries.size(); k++) {
						CabinetSeries lcbs = listSeries.get(k);
						if (SerID == lcbs.getID()) { // 系列相同
							// 仍然需要一个判断系列ID存在的方法，防止重复
							break;
						}
					}
					if (k < listSeries.size()) {
						try {
							// 读取箱体
							cbtUsb = DataAccessCabinetLibrary
									.getCabinetFromUDisk(strFilePath, strUname);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// DataAccessCabinetLibrary.getCabinetByname(strUname,
						// cbtUsb, 1);
						if (IsNodeExist(listCbt, strUname)) // 判定是否有相同箱体存在
						{
							continue;
							}else {
						DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
						System.out.println("添加的箱体名称" + strUname);
						System.out.println("当前外部存储路径" + Usb_PATH);
						break;
						}
					}
					if (k == listSeries.size()) {
						String stUname = cbtUsb.getsName();
						int SeresID = cbtUsb.getnSeriesID();
						String seriesName = stUname+"_series";
						int localSeriesID=listSeries.get(i).getID();
						//先添加系列节点，再添加箱体节点2015-08-12
						/**************/
						if (localSeriesID==SeresID) {   //如果本地文件中的系列ID与U盘中箱体的系列ID相同，就不添加该系列
							continue;
						}else {
						DataAccessCabinetLibrary
								.WriteXMLCabinetSeries(new CabinetSeries(
										SeresID, 0, seriesName));
						System.out.println("添加的箱体系列名称" + seriesName);
						System.out.println("当前外部存储路径" + Usb_PATH);
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
						if (IsNodeExist(listCbt, strUname)) // 判定是否有相同箱体存在
						{
							continue;
							}else {
						DataAccessCabinetLibrary.AddXMLCabinet(cbtUsb);
						/*
						 * 当前写入箱体文件时，存在，数据写入不成功，
						 * 原因在DataAccessCabinetLibrary里面SetHUBLinkup数据读取写入时
						 * UtilFun
						 * .bytes2HexString(Hublinktable.getUcLinkTable()转换时
						 * 进入死循环，因此ScanCardAttachment下面所有条目的数据data都为0暂时代替，
						 */
						System.out.println("添加的箱体名称" + strUname);
						break;
					}
					}
				}
				}
			}
		}
		/**************************************************/
	}

	// 判定是否有重复的存在
	public boolean IsNodeExist(List<CabinetInformation> listCbt, String strName) {
		for (int i = 0; i < listCbt.size(); i++) {
			if (listCbt.get(i).getsName().equals(strName)) {
				return true;
			}
		}
		return false;
	}
}
