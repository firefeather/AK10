package com.szaoto.ak10.systemconfig;

import java.io.IOException;

import com.szaoto.ak10.R;
import com.szaoto.ak10.wirednetwork.WirednetConfig;
import com.szaoto.ak10.wirednetwork.WirednetConfigDb;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NetWorkSettingFragment extends Fragment{
	
	private String strIp;
	private String strGateWay;
	private String strMask;
	
	private EditText etIp;
	private EditText etMask;
	private EditText etGateWay;
	private Button btnOK;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_networksetting, null);		
		etIp = (EditText) view.findViewById(R.id.etIP);
		etMask = (EditText) view.findViewById(R.id.etMask);
		etGateWay = (EditText) view.findViewById(R.id.etGateWay);
		btnOK = (Button) view.findViewById(R.id.btnNetSet);
			
		WirednetConfig netConfig = WirednetConfigDb.GetWirednetConfig();
		
		strIp  =	netConfig.ipaddr;
		strGateWay  =	netConfig.gateway;
		strMask  =	netConfig.mask;
		
		if (strIp!=null&&!strIp.isEmpty()) {
			etIp.setText(strIp);
		}
		if (strGateWay!=null&&!strGateWay.isEmpty()) {
			etGateWay.setText(strGateWay);
		}
		if (strMask!=null&&!strMask.isEmpty()) {
			etMask.setText(strMask);
		}
		
		
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				strIp  =	etIp.getText().toString();
				strGateWay  =	etGateWay.getText().toString();
				strMask  =	etMask.getText().toString();							
				if (strIp==null||strIp.isEmpty()) {					
					Toast.makeText(getActivity(), "IP:Empty Param", Toast.LENGTH_SHORT).show();					
					return;				
				}
				if (strGateWay==null||strGateWay.isEmpty()) {					
					Toast.makeText(getActivity(), "GateWay: Empty Param", Toast.LENGTH_SHORT).show();					
					return;					
				}
				if (strMask==null||strMask.isEmpty()) {				
					Toast.makeText(getActivity(), "Mask: Empty Param", Toast.LENGTH_SHORT).show();				
					return;		
				}
				
				WirednetConfig tWirenetConfig = new WirednetConfig();				
				tWirenetConfig.dhcp="no";
				tWirenetConfig.ipaddr=strIp;
				tWirenetConfig.mask=strMask;
				tWirenetConfig.gateway=strGateWay;
				try 
				{
					WirednetConfigDb.SaveWirednetConfig(tWirenetConfig);
					WirednetConfigDb.OpenWirednetHotplugDetect();				
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		return view;
	}	
}
