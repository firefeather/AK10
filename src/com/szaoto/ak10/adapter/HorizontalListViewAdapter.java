package com.szaoto.ak10.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.szaoto.ak10.R;
import com.szaoto.ak10.control.ControlActivity;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.datacomm.InterfaceComm;
import com.szaoto.ak10.sqlitedata.ChanGroupData;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.sqlitedata.ChanGroup_CurrentDb;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.sqlitedata.Sys_Para;
import com.szaoto.ak10.sqlitedata.Sys_ParaDB;

/**
 * 
 * ����HorizontalListViewAdapter ���� zhangsj ��Ҫ���� ˮƽ��̬װ��listview������ ��������2015��3��10��
 * �޸��ߣ��޸����ڣ��޸�����
 * 
 */
public class HorizontalListViewAdapter extends BaseAdapter {
	// private int[] mIconIDs;
	// private String[] mTitles;
	private Context mContext;
	public LayoutInflater mInflater;
	private ArrayList<String> listItems;

	private LayoutInflater listContainer;

	public int m_LEDID;
	public HorizontalListViewAdapter(ControlActivity controlActivity,
			ArrayList<String> tGroupData, int LEDID) {
		this.mContext = controlActivity;
		this.listItems = tGroupData;
		listContainer = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_LEDID = LEDID;
	}

	@Override
	public int getCount() {
		// return mIconIDs.length;
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// convertView = mInflater.inflate(R.layout.horizontal_list_item,
			// null);
			convertView = listContainer.inflate(R.layout.horizontal_list_item,
					null);
			// holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.btnGroupName = (Button) convertView
					.findViewById(R.id.text_list_item);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.btnGroupName.setText((String) listItems.get(position));

		holder.btnGroupName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String strGpNameString = (String) holder.btnGroupName.getText();
				SwitchChannel(strGpNameString);
			}
		});

		return convertView;
	}


	public void SwitchChannel(String strGpNameString){
		ChanGroup_CurrentDb.UpdateCurrentGpName(strGpNameString,  m_LEDID);
		ChanGroupData tGpData = ChanGroupDb.GetRecordByGpName(strGpNameString, m_LEDID);
		String strCfg = tGpData.strCfg;

		String[] strCfgItemStrings = strCfg.split(",");

		// �ض�������ƵԴ
		ArrayList<ChnData> tChPortInfoArray = ChannelDB.GetAllRecord(m_LEDID);
		for (int i = 0; i < tChPortInfoArray.size(); i++) {
			ChanComm.SetAcqCardPortNumAndEnable(false,
					tChPortInfoArray.get(i).videosourceid,
					tChPortInfoArray.get(i).Id % 1000, m_LEDID);
		}

		// �ض����еķ��Ϳ��ӿ�
		ArrayList<IntfData> tIntfDataArrayList = InterfaceDB.GetAllRecord(m_LEDID);
		for (int i = 0; i < tIntfDataArrayList.size(); i++) {
			IntfData tInterfData = tIntfDataArrayList.get(i);
			int tVideoId = InterfaceDB.GetChportIdById(tInterfData.Id,m_LEDID);
			InterfaceComm.SetSendCardChPortAndEnable(false, tVideoId,
					tInterfData.macaddress, tInterfData.Id % 1000);
		}

		for (int i = 0; i < strCfgItemStrings.length; i++) {

			String[] strCfgPairStrings = strCfgItemStrings[i].split("-");

			String strChanId = strCfgPairStrings[1];
			String strIntfId = strCfgPairStrings[0];

			int Chid = Integer.valueOf(strChanId);

			int Intfid = Integer.valueOf(strIntfId);

			ChnData tChanData = ChannelDB.GetRecordById(Chid, m_LEDID);
			// ����Ƶ�˿�
			ChanComm.SetAcqCardPortNumAndEnable(true,
					tChanData.videosourceid, tChanData.Id % 1000,
					m_LEDID);

			// ����������Ƶ��������
			InterfaceDB.GetChportIdById(Intfid, m_LEDID);
			IntfData tInterfData = InterfaceDB.GetRecordById(Intfid, m_LEDID);
			InterfaceComm.SetSendCardChPortAndEnable(true,
					tChanData.videosourceid, tInterfData.macaddress,
					Intfid % 1000);

			// �������ٷ�һ��
			// Ҫ������Եģ��ȵõ�ԭ���Ĳɼ���������
			int tChId = tInterfData.channelid;
			ChnData tChnData = ChannelDB.GetRecordById(tChId, m_LEDID);
			//��ȡ3D������Ϣ
			Sys_Para sys_Para = Sys_ParaDB.GetSys_Para();
			
			InterfaceComm.SetSendCardPortParam(
					(short) (tInterfData.offsetX - tChnData.offsetX),
					(short) (tInterfData.offsetY - tChnData.offsetY),
					(short) tInterfData.width,
					(short) tInterfData.height, 
					sys_Para.cfg3d,
					tInterfData.macaddress,
					Intfid % 1000);
		}
	}
	private static class ViewHolder {
		private Button btnGroupName;
	}
}