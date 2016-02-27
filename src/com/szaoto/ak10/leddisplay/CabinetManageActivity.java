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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.entity.CabinetXml;
/**
 * 类名CabinetManageActivity
 * 作者 zhangsj
 * 主要功能:箱体管理，对箱体单独进行名称修改，删除操作
 * 创建日期2015年7月15日
 * 修改日期，修改内容
 * 修改者:
 */
public class CabinetManageActivity extends Activity {
	CabinetManageActivity cbtActivity;
	private ListView listviev_Cabinet;
	private TextView btn_Complete;
	List<CabinetXml> CabinetList;
	private CabinetListAdapter mCabinetLibData;
	public String CONFIG_PATH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cabinets_manage);
		SerialPortControlBroadCast.SetCurrentContext(this);
	    initView();
	    try {
	    	loadCabinetData();
		} catch (Exception e) {
			// TODO: handle exceptionO
		}
	
	}

	private void loadCabinetData() {
	   //解析箱体文件填充
		 try {
			ReadXmlFileData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 UpdateTreeViewData();
	}

	private void initView() {
		listviev_Cabinet = (ListView) findViewById(R.id.listview_cabinetlib);
		btn_Complete = (TextView) findViewById(R.id.btncabinetOK);
		btn_Complete.setOnClickListener(ClickHandler);
		
				
	}
	View.OnClickListener ClickHandler=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btncabinetOK:
				startActivity(new Intent(CabinetManageActivity.this, CabinetLibraryActivity.class));
				finish();
				break;

			default:
				break;
			}
			
		}
	};
	public void ReadXmlFileData() throws FileNotFoundException, Exception {
		InputStream inSeries;   //箱体系列
		InputStream inCabinet; //箱体文件
		CONFIG_PATH = this.getFilesDir() + "//config//";
	
		File xmlCabinet = new File(CONFIG_PATH, "Cabinet.cbt");
		inCabinet = new FileInputStream(xmlCabinet); //箱体文件
		if (!xmlCabinet.exists()) {
			Toast.makeText(getApplication(), R.string.btn_cbsdata_sync, 200).show();
		}
		CabinetList = pullXmlToCabinetList(inCabinet);  //解析箱体箱体文件
		
	}
	
	// 解析箱体文件
		private List<CabinetXml> pullXmlToCabinetList(InputStream in) throws Exception {
			List<CabinetXml> list = null; // 定义要显示的箱体集合
			CabinetXml cabinet = null; // 初始化箱体
			// 创建解析实例
			XmlPullParser parser = Xml.newPullParser();
			// 设置输入流并指明编码
			parser.setInput(in, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				// 判断是否为文档开始
				case XmlPullParser.START_DOCUMENT:
					System.out.println("开始从头文件解析");
					list = new ArrayList<CabinetXml>();
					break;
				// 判断是否为标签开始元素
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("Cabinet")) {
						cabinet = new CabinetXml();
					} else if (parser.getName().equals("ID")) {
						eventType = parser.next(); // 得到箱体的属性值
						cabinet.setnID(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("SeriesID")) {
						eventType = parser.next(); // 得到箱体的属性值
						cabinet.setnSeriesID(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("Name")) {
						eventType = parser.next(); // 得到箱体的属性值
						cabinet.setsName(parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("Cabinet")) { // 判断结束标签元素是否是Cabinet
						list.add(cabinet); // 将cabinet添加到Cabinet集合
						cabinet = null;
					}
					break;
				}
				// 进入下一个元素并触发相应事件
				eventType = parser.next();
			}
			return list;
		}
		public void UpdateTreeViewData() {
//			treeView = (TreeView) findViewById(R.id.tree_view);
			// 填充的数据源,分别是箱体系列所对应的箱体文件
			ArrayList<String> treeElementsString =(ArrayList<String>) DataAccessCabinetLibrary.getCabinetNames(0);
			// 解析读出的文件内容
			mCabinetLibData = new CabinetListAdapter(this, treeElementsString);
			listviev_Cabinet.setAdapter(mCabinetLibData);
			mCabinetLibData.notifyDataSetChanged();
			
		}
		
		//箱体库管理adapter
public class CabinetListAdapter extends BaseAdapter {
					private Context context;
					private ArrayList<String> listItems;
					private LayoutInflater listContainer;
					private ListCbtView listcbtItemView;

					public CabinetListAdapter(CabinetManageActivity groupactivity,ArrayList<String> tGroupsData) {
						super();
						this.context = groupactivity;
						listContainer = LayoutInflater.from(context);
						this.listItems = tGroupsData;
					}

					public CabinetListAdapter(Context application,	ArrayList<String> tGroupsData) {
						this.context = application;
						listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
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
						// 自定义视图
						listcbtItemView = null;
						if (convertView == null) {
							listcbtItemView = new ListCbtView();
							// 获取list_item布局文件的视图
							convertView = listContainer.inflate(R.layout.cabinetlib_list, null);
							// 群组名称控件
							listcbtItemView.tCbName = (TextView) convertView.findViewById(R.id.cbt_list_item_text1);
							listcbtItemView.btnDeleteCbt = (Button) convertView.findViewById(R.id.btnDelCbtFile);	
							// 设置控件集到convertView
							convertView.setTag(listcbtItemView);
						} else {
							listcbtItemView = (ListCbtView) convertView.getTag();
						}
						// 设置文字
						listcbtItemView.tCbName.setText((String) listItems.get(position));
						
						listcbtItemView.btnDeleteCbt.setOnClickListener(new OnClickListener() {
								
									@Override
									public void onClick(View v) {
										
										new AlertDialog.Builder(CabinetManageActivity.this)
										/* 弹出窗口的最上头文字 */
										.setTitle(R.string.text_deletecabinet)
										/* 设置弹出窗口的图式 */
										.setIcon(android.R.drawable.ic_dialog_info)
										/* 设置弹出窗口的信息 */
										.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
											List<String> nSeriesId;

											public void onClick(DialogInterface dialoginterface, int i) {
												
												String strCabtNameString = listItems.get(position).toString();
//												String strCabtNameString = (String) listcbtItemView.tCbName.getText();//获取位置不正确
								     			DataAccessCabinetLibrary.RemoveCabinet(strCabtNameString);
												/*
												 * 出现问题，系列和箱体分别是2个不同的xml文件，
												 * 删除系列时，并没有删除系列下的箱体
												 */
												UpdateTreeViewData();
											}
										})
										.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* 设置跳出窗口的返回事件 */
											public void onClick(DialogInterface dialoginterface, int i) {
												
											}
										}).show();
									}
								});
						
						    convertView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String CbtName=(String)listItems.get(position);
									
								ModifyDialogShow(CbtName);
							}

						    });
						return convertView;
					}
					
					public class ListCbtView {
						public TextView tCbName;
						public Button   btnDeleteCbt;
					}
					
				}
protected void ModifyDialogShow(final String cBtname) 
{

	LayoutInflater inflater = (LayoutInflater) CabinetManageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.input_edit, null);
	final EditText  EditCabinetName= (EditText) layout.findViewById(R.id.input_edit_string);
	EditCabinetName.setText(cBtname);

	// 弹出的对话框
	new AlertDialog.Builder(CabinetManageActivity.this)
			/* 弹出窗口的最上头文字 */
			.setTitle(R.string.text_group_item_modify)
			/* 设置弹出窗口的图式 */
			.setIcon(android.R.drawable.ic_dialog_info)
			/* 设置弹出窗口的信息 */
			.setMessage(R.string.text_group_item_modify_content)
			.setView(layout)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
		        @Override
				public void onClick(DialogInterface dialog, int which) {					
		        	EditCabinetName.setFocusable(true);
		          	String seriesName=EditCabinetName.getText().toString();
		        	DataAccessCabinetLibrary.ModifyCabinetName(cBtname,seriesName);
		        	UpdateTreeViewData();
				}
			})
			.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* 设置跳出窗口的返回事件 */
				public void onClick(DialogInterface dialoginterface, int i) {

				}
			}).show();
	
}

}
