package com.szaoto.ak10.configuration;

import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.datacomm.ChanComm;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetVideoScaleActivity extends Activity {

	
	EditText et_Height;
	EditText et_Width;
	
	TextView tv_CurResolution;
	
	Button btnOK;
	Button btnClose;
	
	CheckBox checkBoxEnable;
	
	int ChanId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_videoscale);
		SerialPortControlBroadCast.SetCurrentContext(this);
		
		et_Height = (EditText) findViewById(R.id.et_scaleHeight);
		et_Width = (EditText) findViewById(R.id.et_scaleWidth);
		checkBoxEnable = (CheckBox)findViewById(R.id.cbEnable);
		tv_CurResolution = (TextView) findViewById(R.id.curRes);
		
		
		Intent tIntent = getIntent();	
	    String strReso  =	tIntent.getStringExtra("resolution");
	    tv_CurResolution.setText(strReso);
	    
	    String strChanId = tIntent.getStringExtra("ChanId");
	    
	    ChanId = Integer.valueOf(strChanId);
	    
	    
	    btnOK = (Button) findViewById(R.id.btnOk);
	    btnClose = (Button) findViewById(R.id.btnClose);
	    
	    
	    btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	    
	    checkBoxEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean bCheck) {
				int videoId = ChanId%1000+ChanId/1000;
				int interfaceNum = ChanId%1000;
			
				if (bCheck) {
					btnOK.setEnabled(true);
					ChanComm.EnableVideoScale(videoId,interfaceNum,true);
				}else {
					ChanComm.EnableVideoScale(videoId,interfaceNum,false);
					btnOK.setEnabled(false);
				}
			}
		});
	    
	    btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String strH = et_Height.getText().toString();
				String strW = et_Width.getText().toString();
				
				if (strH.isEmpty()||strW.isEmpty()) {					
					Toast.makeText(getApplicationContext(), getString(R.string.noempty), Toast.LENGTH_SHORT).show();					
					return;
				}				
				
				//��������
				int ScaleHeight = Integer.valueOf(strH);
				int ScaleWidth = Integer.valueOf(strW);				
				
				int videoId = ChanId%1000+ChanId/1000;
				int interfaceNum = ChanId%1000;
				boolean bRet = ChanComm.SetVideoScale(videoId, interfaceNum, ScaleHeight, ScaleWidth, true);//ֻ�е�ʹ��ʱ��Ż����
				
				if (bRet) {					
					//ChannelDB.UpdateChannelWHParam(ChanId,ScaleWidth,ScaleHeight,Ak10Application.GetLedId());					
					Toast.makeText(getApplicationContext(), getString(R.string.set_ok), Toast.LENGTH_SHORT).show();					
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.set_fail), Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
		super.onCreate(savedInstanceState);
	}
	
	
	
}