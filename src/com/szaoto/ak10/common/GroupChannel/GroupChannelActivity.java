package com.szaoto.ak10.common.GroupChannel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.leddisplay.LedConstructActivity;
import com.szaoto.ak10.sqlitedata.ChanGroupData;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;

/**
 * 类名GroupChannelActivity 作者 zhangsj 主要功能 通道群组配置 创建日期2014年10月11日 修改者，修改日期，修改内容
 * 
 */
public class GroupChannelActivity extends Activity {

	private TextView txt_Back;
	private TextView txt_GpManage;
	public TextView txtGroupNameTextView;

	ArrayList<String> tArrGroupData = new ArrayList<String>();// 前面项的列表

	InterfaceListAdapter gAdapter = null;
	ArrayList<String> m_strInterfaceNameArrayList = new ArrayList<String>();
	ArrayList<String> m_strChannelNameArrayList = new ArrayList<String>();
	String m_strGpName;

	// 存Interface与Channel
	ArrayList<Integer> tGroupid = new ArrayList<Integer>();// 存群组ID的列表

	private ListView listgroup;
	private ListView group_Interface;

	public static String CONFIG_PATH;
	// List<Interface> mInterface;
	// private MyAdapter LeftGroupAdapter;
	private TextView ledGroupText;
	public InterfaceListAdapter tAdapter;
	public int m_ledId;
	private TextView textChannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_channel);
		SerialPortControlBroadCast.SetCurrentContext(this);
		listgroup = (ListView) findViewById(R.id.list_group_left);
		group_Interface = (ListView) findViewById(R.id.list_group_channel);

		// 初始化adapter
		gAdapter = new InterfaceListAdapter(this);
		group_Interface.setAdapter(gAdapter);

		initView();

	}

	public void LoadDataFromDb() {
		m_ledId = Ak10Application.GetLedId();

		m_strInterfaceNameArrayList.clear();
		m_strChannelNameArrayList.clear();

		ArrayList<ChanGroupData> arrGpDataArrayList = ChanGroupDb.GetAllGpRecords(m_ledId);

		final ArrayList<String> strArrGpNameArrayList = new ArrayList<String>();

		for (int i = 0; i < arrGpDataArrayList.size(); i++) {

			String strNameString = arrGpDataArrayList.get(i).gpName;
			strArrGpNameArrayList.add(strNameString);
		}

		listgroup.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, strArrGpNameArrayList));

		if (strArrGpNameArrayList.size() != 0) {
			OnChangeSelectGroup(strArrGpNameArrayList.get(0));
		} else {
			txtGroupNameTextView.setText(getString(R.string.text_group_tips));

			m_strInterfaceNameArrayList.clear();
			m_strChannelNameArrayList.clear();

			gAdapter.notifyDataSetChanged();

		}

		listgroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String strGpName = strArrGpNameArrayList.get(position);
				OnChangeSelectGroup(strGpName);
			}
		});

	}

	public void OnChangeSelectGroup(String strGpName) {

		ChanGroupData tGpData = ChanGroupDb.GetRecordByGpName(strGpName,
				m_ledId);

		String strCfg = tGpData.strCfg;

		String[] strCfgItem = strCfg.split(",");

		m_strInterfaceNameArrayList.clear();
		m_strChannelNameArrayList.clear();

		for (int i = 0; i < strCfgItem.length; i++) {

			String[] strCfgInterAndChanStrings = strCfgItem[i].split("-");

			String strIntfID = strCfgInterAndChanStrings[0];
			String strChanID = strCfgInterAndChanStrings[1];
			int tinterfId;

			try {
				tinterfId = Integer.valueOf(strIntfID);
			} catch (Exception e) {
				ChanGroupDb.DeleteRecordByGpName(strGpName, m_ledId);
				LoadDataFromDb();
				return;
			}

			IntfData tIntfData = InterfaceDB.GetRecordById(tinterfId, m_ledId);

			// 如果不存在这个interface，说明已经被删除了，那么这个群组必须被删除
			if (tIntfData == null) {
				ChanGroupDb.DeleteRecordByGpName(strGpName, m_ledId);
				LoadDataFromDb();
				return;

			}

			String strInterf = tIntfData.name;

			m_strInterfaceNameArrayList.add(strInterf);

			ChnData tChnData = ChannelDB.GetRecordById(
					Integer.valueOf(strChanID), m_ledId);
			String strChan = tChnData.strChName;
			m_strChannelNameArrayList.add(strChan);

		}

		if (m_strInterfaceNameArrayList.size() == 0
				|| m_strChannelNameArrayList.size() == 0) {
			ChanGroupDb.DeleteRecordByGpName(strGpName, m_ledId);
			LoadDataFromDb();
			return;
		}

		m_strGpName = tGpData.gpName;
		// gAdapter = new InterfaceListAdapter(this,arrIntfArrayList,
		// arrChanArrayList,tGpData.gpName);
		// group_Interface.setAdapter(tAdapter);

		txtGroupNameTextView.setText(strGpName);
		gAdapter.notifyDataSetChanged();

	}

	public final class ViewHolder {
		public TextView mInterfaceView;
		public Spinner mSpin;
	}

	@Override
	protected void onResume() {

		LoadDataFromDb();

		super.onResume();
	}

	class InterfaceListAdapter extends BaseAdapter {

		// 得到一个LayoutInfalter对象用来导入布局
		private LayoutInflater mInflater;

		public InterfaceListAdapter(GroupChannelActivity mActivity) {
			super();

			this.mInflater = LayoutInflater.from(mActivity);

		}

		@Override
		public int getCount() {
			return m_strInterfaceNameArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return m_strInterfaceNameArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			String tIntfName = m_strInterfaceNameArrayList.get(position);
			String tChanName = m_strChannelNameArrayList.get(position);

			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(
						R.layout.group_minterface1_item, null);

				holder.mInterfaceView = (TextView) convertView
						.findViewById(R.id.interface_id);
				holder.mSpin = (Spinner) convertView
						.findViewById(R.id.interface1_ch);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mInterfaceView.setText(tIntfName);

			final ArrayList<String> strChanNameArrayList = ChanGroupDb
					.GetChannelNameArray(m_ledId);

			holder.mSpin.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_spinner_dropdown_item,
					strChanNameArrayList));

			for (int i = 0; i < strChanNameArrayList.size(); i++) {
				if (strChanNameArrayList.get(i).equals(tChanName)) {
					holder.mSpin.setSelection(i);
				}
			}

			holder.mSpin
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int positionSpin, long id) {
							String chanNameString = strChanNameArrayList
									.get(positionSpin);
							m_strChannelNameArrayList.set(position,
									chanNameString);

							String strCfg = null;

							for (int i = 0; i < m_strInterfaceNameArrayList.size(); i++) {

								String strIntfnameString = m_strInterfaceNameArrayList.get(i);

								if (strIntfnameString == null) {
									ChanGroupDb.DeleteRecordByGpName(m_strGpName, m_ledId);
									LoadDataFromDb();
									return;
								}

								int tIntfId = ChanGroupDb.GetInterfaceIdByName(strIntfnameString, m_ledId);
								String strChannameString = m_strChannelNameArrayList.get(i);
								int tChanId = ChanGroupDb.GetChannelIdByName(strChannameString, m_ledId);
								if (strCfg == null) {
									strCfg = tIntfId + "-" + tChanId;
								} else {
									strCfg += tIntfId + "-" + tChanId;
								}

								if (i != m_strInterfaceNameArrayList.size() - 1) {
									strCfg += ",";
								}
							}

							ChanGroupDb.UpdateCfgString(strCfg, m_strGpName,m_ledId);

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});

			return convertView;
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		txt_Back = (TextView) findViewById(R.id.text_cabinettback);
		txt_GpManage = (TextView) findViewById(R.id.txt_groupmanage);

		txtGroupNameTextView = (TextView) findViewById(R.id.tvGroupName);

		textChannel = (TextView) findViewById(R.id.text_channel);
		ledGroupText = (TextView) findViewById(R.id.text_led_groupchannel);
		// 获取LEDID并设置
		m_ledId = Ak10Application.GetLedId();
		ledGroupText.setText(">LED" + m_ledId + ">");

		txt_Back.setOnClickListener(ClickHandler);
		txt_GpManage.setOnClickListener(ClickHandler);
	}

	View.OnClickListener ClickHandler = new OnClickListener() {
		private int index = 0;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_cabinettback:
				finish();
				startActivity(new Intent(GroupChannelActivity.this,
						LedConstructActivity.class));
				break;
			case R.id.txt_groupmanage: // 群组管理
				// 添加群组时，直接添加到xml文件里面去了,需要调用DataAceesGroups里面的add方法
				startActivity(new Intent(GroupChannelActivity.this,
						GroupManageActivity.class));
				// dialogshow();
				break;

			case R.id.text_channel:
				index = tArrGroupData.size() - 1;
				Toast.makeText(
						getApplication(),
						R.string.text_cancel_group_click
								+ tArrGroupData.get(index), Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}

		}
	};

}
