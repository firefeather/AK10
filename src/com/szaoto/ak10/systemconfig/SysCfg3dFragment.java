/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;
import java.util.ArrayList;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.datacomm.LEDParamComm;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.sqlitedata.Sys_Para;
import com.szaoto.ak10.sqlitedata.Sys_ParaDB;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class SysCfg3dFragment extends Fragment{
	//3D配置
	private CheckBox chk_3D;
	private RadioButton rdo_LeftAndRight;
	private RadioButton rdo_TopAndButton;
	private RadioButton rdo_FrameSeq;  
	private Button btn_OK;
	private int m_nCfg3d;
	private int m_nCfg3d_last;//上一次操作值

	//同步延时参数和关闭扫描周期参数配置
	private EditText edit_SynchronDelay;
	private EditText edit_DisableScanCycle;
	private Button btn_Set;
	private Button btn_Save;
	private int m_nSynchronDelay;//同步延时参数
	private int m_nDisableScanCycle;//关闭扫描周期参数
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.syscfg3d, null);
		initView(view);
		Sys_Para sysPara = Sys_ParaDB.GetSys_Para();
		m_nCfg3d = sysPara.cfg3d;
		m_nCfg3d_last = m_nCfg3d;
		m_nSynchronDelay = sysPara.SynchronDelay;
		m_nDisableScanCycle = sysPara.DisableScanCycle;
		switch (m_nCfg3d) {
		case 1:
			chk_3D.setChecked(true);
			rdo_LeftAndRight.setEnabled(true);
			rdo_TopAndButton.setEnabled(true);
			rdo_FrameSeq.setEnabled(true);
			rdo_LeftAndRight.setChecked(true);
			rdo_TopAndButton.setChecked(false);
			rdo_FrameSeq.setChecked(false);
			break;
		case 2:
			chk_3D.setChecked(true);
			rdo_LeftAndRight.setEnabled(true);
			rdo_TopAndButton.setEnabled(true);
			rdo_FrameSeq.setEnabled(true);
			rdo_LeftAndRight.setChecked(false);
			rdo_TopAndButton.setChecked(true);
			rdo_FrameSeq.setChecked(false);
			break;		
		case 3:
			chk_3D.setChecked(true);
			rdo_LeftAndRight.setEnabled(true);
			rdo_TopAndButton.setEnabled(true);
			rdo_FrameSeq.setEnabled(true);
			rdo_LeftAndRight.setChecked(false);
			rdo_TopAndButton.setChecked(false);
			rdo_FrameSeq.setChecked(true);
			break;
		default:
			chk_3D.setChecked(false);
			rdo_LeftAndRight.setEnabled(false);
			rdo_TopAndButton.setEnabled(false);
			rdo_FrameSeq.setEnabled(false);
			break;
		}
		edit_SynchronDelay.setText(String.valueOf(m_nSynchronDelay));
		edit_DisableScanCycle.setText(String.valueOf(m_nDisableScanCycle));
		
		setOnClickListener();

		return view;
	}
	
	private void initView(View view) {
		//3D影院
		btn_OK = (Button) view.findViewById(R.id.btn3DSet);
		chk_3D = (CheckBox)view.findViewById(R.id.checkBox3D);
		rdo_LeftAndRight = (RadioButton)view.findViewById(R.id.radioButton_LeftAndRight);
		rdo_TopAndButton = (RadioButton)view.findViewById(R.id.radioButton_TopAndButton);
		rdo_FrameSeq = (RadioButton)view.findViewById(R.id.radioButton_FrameSeq);
		edit_SynchronDelay = (EditText)view.findViewById(R.id.editSysCfg_SyncDelay);
		edit_DisableScanCycle = (EditText)view.findViewById(R.id.editSysCfg_DisScanCycle);
		btn_Set = (Button)view.findViewById(R.id.btn3DCfgSet);
		btn_Save = (Button)view.findViewById(R.id.btn3DCfgSave);
	}
	
	private void setOnClickListener()
	{		
		btn_OK.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {		
				//更新数据库
				Sys_Para sysPara = Sys_ParaDB.GetSys_Para();
				sysPara.cfg3d = m_nCfg3d;
				Sys_ParaDB.UpdateCfg3D(sysPara);
				
				//发送数据
				for (int j = 1; j <= 4; j++) {
					//打开所有LED1~4的视频端口			
					HomePageActivity.getInstance().OpenChPortsFromDbCfg(j);
					//配置所有LED1~4的发送卡端口
					HomePageActivity.getInstance().ConfigAddSendCardParams(j);
					HomePageActivity.getInstance().SetChport(j);
				}			
				
				Toast.makeText(getActivity(), getString(R.string.TridsettingSucceed), Toast.LENGTH_SHORT).show();
			}		
		});
		btn_Set.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				//更新数据库
				Sys_Para sysPara = Sys_ParaDB.GetSys_Para();
				sysPara.SynchronDelay = Integer.valueOf(edit_SynchronDelay.getText().toString());
				sysPara.DisableScanCycle = Integer.valueOf(edit_DisableScanCycle.getText().toString());
				Sys_ParaDB.UpdateOtherPara(sysPara);
				
				//发送数据
				for (int j = 1; j <= 4; j++) {
					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(j);
					int nSize = tArrayList.size();
					for (int i = 0; i < nSize; i++) {
						IntfData tInterfData =tArrayList.get(i);
						LEDParamComm.SetSysPara(sysPara.SynchronDelay, sysPara.DisableScanCycle, tInterfData.macaddress, tInterfData.Id%1000);
					}		
				}
				
				Toast.makeText(getActivity(), getString(R.string.SYssettingSucceed), Toast.LENGTH_SHORT).show();	
			}	
		});
		
		btn_Save.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				//保存数据
				for (int j = 1; j <= 4; j++) {
					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(j);
					int nSize = tArrayList.size();
					for (int i = 0; i < nSize; i++) {
						IntfData tInterfData =tArrayList.get(i);
						LEDParamComm.SavePara(tInterfData.macaddress, tInterfData.Id%1000, 0x01);
					}		
				}
			}	
		});
		
		chk_3D.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {			
				if (m_nCfg3d > 0) {
					chk_3D.setChecked(false);
					m_nCfg3d = 0;
					rdo_LeftAndRight.setEnabled(false);
					rdo_TopAndButton.setEnabled(false);
					rdo_FrameSeq.setEnabled(false);
				}
				else {
					chk_3D.setChecked(true);
					rdo_LeftAndRight.setEnabled(true);
					rdo_TopAndButton.setEnabled(true);
					rdo_FrameSeq.setEnabled(true);
					m_nCfg3d = m_nCfg3d_last;
					if (0 == m_nCfg3d) {
						m_nCfg3d = 1;
						m_nCfg3d_last = 1;
						rdo_LeftAndRight.setChecked(true);
						rdo_TopAndButton.setChecked(false);
						rdo_FrameSeq.setChecked(false);
					}
				}
			}	
		});
		
		rdo_LeftAndRight.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				m_nCfg3d = 1;
				m_nCfg3d_last = 1;
				rdo_TopAndButton.setChecked(false);
				rdo_FrameSeq.setChecked(false);
			}	
		});
				
		rdo_TopAndButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				m_nCfg3d = 2;
				m_nCfg3d_last = 2;
				rdo_LeftAndRight.setChecked(false);
				rdo_FrameSeq.setChecked(false);
			}	
		});
		
		rdo_FrameSeq.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				m_nCfg3d = 3;
				m_nCfg3d_last = 3;
				rdo_LeftAndRight.setChecked(false);
				rdo_TopAndButton.setChecked(false);
			}	
		});
		

	}
}
