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
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.common.GroupChannel.GroupManageActivity;
import com.szaoto.ak10.common.GroupChannel.GroupManageActivity.GroupListAdapter;
import com.szaoto.ak10.common.GroupChannel.GroupManageActivity.GroupListAdapter.ListGroupView;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.entity.CabinetSeries;
import com.szaoto.ak10.entity.CabinetXml;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.treeview.TreeElement;
import com.szaoto.ak10.treeview.TreeElementParser;
import com.szaoto.ak10.treeview.TreeView;
import com.szaoto.ak10.treeview.TreeViewAdapter;
import com.szaoto.ak10.treeview.TreeView.LastLevelItemClickListener;
/**
 * ����CabinetLibraryManage
 * 
 * ��Ҫ����:��������,����ϵ���ļ���ɾ��
 * ��������2015��5��28��
 * �޸����ڣ��޸�����
 * ���� :zhangsj
 */
public class CabinetLibraryManage extends Activity {

	TextView btn_Ensure;
	private ListView listview_CabinetLib;
	public String CONFIG_PATH;
	List<CabinetSeries> CabinetSeriesList;
	List<CabinetXml> CabinetList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cabinet_library_manage);
		SerialPortControlBroadCast.SetCurrentContext(this);
		initView();
		try {
			TreeViewLoadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// ��������ͼ����
		private void TreeViewLoadData() throws Exception {
			 ReadXmlFileData();
			 UpdateTreeViewData();
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
				Toast.makeText(getApplication(), R.string.btn_cbsdata_sync, 200).show();
			}
			CabinetList = pullXmlToCabinetList(inCabinet);  //�������������ļ�
			CabinetSeriesList = pullXmlToCabinetSeriesList(inSeries); //��������ϵ��
		}
		
	private void initView() {
		btn_Ensure=(TextView) findViewById(R.id.btncabinetlibOK);
		listview_CabinetLib=(ListView) findViewById(R.id.listview_cabinetserieslib);
		btn_Ensure.setOnClickListener(ClickHandler);
		
	}
	View.OnClickListener ClickHandler= new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btncabinetlibOK:
			startActivity(new Intent(CabinetLibraryManage.this, CabinetLibraryActivity.class));
			finish();
			break;

		default:
			break;
		}
			
		}
	};
	private CabinetListAdapter mCabinetLibData;
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
	public void UpdateTreeViewData() {
//		treeView = (TreeView) findViewById(R.id.tree_view);
		// ��������Դ,�ֱ�������ϵ������Ӧ�������ļ�
		ArrayList<String> treeElementsString =(ArrayList<String>) DataAccessCabinetLibrary.getCabinetSeriseNames(0);
		// �����������ļ�����
		mCabinetLibData = new CabinetListAdapter(this, treeElementsString);
		listview_CabinetLib.setAdapter(mCabinetLibData);
		mCabinetLibData.notifyDataSetChanged();
		
	}

	//��������adapter
		public class CabinetListAdapter extends BaseAdapter {
			private Context context;
			private ArrayList<String> listItems;
			private LayoutInflater listContainer;
			private ListCbsView listItemView;

			public CabinetListAdapter(CabinetLibraryManage groupactivity,ArrayList<String> tGroupsData) {
				super();
				this.context = groupactivity;
				listContainer = LayoutInflater.from(context);
				this.listItems = tGroupsData;
			}

			public CabinetListAdapter(Context application,	ArrayList<String> tGroupsData) {
				this.context = application;
				listContainer = LayoutInflater.from(context); // ������ͼ����������������
				this.listItems = tGroupsData;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return listItems.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// �Զ�����ͼ
				listItemView = null;
				if (convertView == null) {
					listItemView = new ListCbsView();
					// ��ȡlist_item�����ļ�����ͼ
					convertView = listContainer.inflate(R.layout.cabinetseries_list, null);
					// Ⱥ�����ƿؼ�
					listItemView.tCbseriesName = (TextView) convertView.findViewById(R.id.cbs_list_item_text1);
					listItemView.btnDeleteCbs = (Button) convertView.findViewById(R.id.btnDelCbsFile);	
					// ���ÿؼ�����convertView
					convertView.setTag(listItemView);
				} else {
					listItemView = (ListCbsView) convertView.getTag();
				}
				// ��������
				listItemView.tCbseriesName.setText((String) listItems.get(position));
				
				listItemView.btnDeleteCbs.setOnClickListener(new OnClickListener() {
						
							@Override
							public void onClick(View v) {
								
								new AlertDialog.Builder(CabinetLibraryManage.this)
								/* �������ڵ�����ͷ���� */
								.setTitle(R.string.btn_cbsdata_del)
								/* ���õ������ڵ�ͼʽ */
								.setIcon(android.R.drawable.ic_dialog_info)
								/* ���õ������ڵ���Ϣ */
								.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
									List<String> nSeriesId;

									public void onClick(DialogInterface dialoginterface, int i) {
										
//										String strCabsNameString = (String) listItemView.tCbseriesName.getText();
										String strCabinetseries=listItems.get(position);
//										CabinetXml strCabinetName=CabinetList.get(position);
										// ��xml�ļ���������ϵ������ɾ������ϵ��	
										nSeriesId=DataAccessCabinetLibrary.getCabinetSeriseID(0);
										int serid = Integer.parseInt(nSeriesId.get(position)); 
										DataAccessCabinetLibrary.RemoveCabinetSerise(strCabinetseries);
										DataAccessCabinetLibrary.RemoveCabinetBySeriesId(serid);
										/*
										 * �������⣬ϵ�к�����ֱ���2����ͬ��xml�ļ���
										 * ɾ��ϵ��ʱ����û��ɾ��ϵ���µ�����
										 */
										UpdateTreeViewData();
									}
								})
								.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
									public void onClick(DialogInterface dialoginterface, int i) {
										
									}
								}).show();
							}
						});
				
				    convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String CBseries=(String)listItems.get(position);
							
						ModifyDialogShow(CBseries);
					}
				});
				return convertView;
			}
			public class ListCbsView {
				public TextView tCbseriesName;
				public Button   btnDeleteCbs;
			}
		}
		//�޸�����ϵ������
		protected void ModifyDialogShow(final String strCbtSeriesName) {

			LayoutInflater inflater = (LayoutInflater) CabinetLibraryManage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.input_edit, null);
			final EditText  EditCabinetSeriesName= (EditText) layout.findViewById(R.id.input_edit_string);
			EditCabinetSeriesName.setText(strCbtSeriesName);
		
			// �����ĶԻ���
			new AlertDialog.Builder(CabinetLibraryManage.this)
					/* �������ڵ�����ͷ���� */
					.setTitle(R.string.text_group_item_modify)
					/* ���õ������ڵ�ͼʽ */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* ���õ������ڵ���Ϣ */
					.setMessage(R.string.text_group_item_modify_content)
					.setView(layout)
					.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				        @Override
						public void onClick(DialogInterface dialog, int which) {					
				        	EditCabinetSeriesName.setFocusable(true);
				          	String seriesName=EditCabinetSeriesName.getText().toString();
				        	DataAccessCabinetLibrary.ModifyCabinetSeriesName(strCbtSeriesName,seriesName);
				        	UpdateTreeViewData();
						}
					})
					.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
						public void onClick(DialogInterface dialoginterface, int i) {

						}
					}).show();
			
		}
}