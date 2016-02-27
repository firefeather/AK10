/*
   * 文件名 SystemCardActivity.java
   * 包含类名列表com.szaoto.ak10.configuration
   * 版本信息，版本号
   * 创建日期2014年1月8日下午2:22:08
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.configuration;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;

/*
 * 类名SystemCardSetupActivity
 * 创建者 zhangsj
 * 主要功能    系统卡配置界面
 * 创建日期2014年7月3日
 * 修改者，修改日期，修改内容
 */
public class SystemCardSetupActivity extends Activity implements OnClickListener{


	 TextView tv_Set_Width;
	 TextView tv_Set_Height;
	 TextView tv_OffSet_X;
	 TextView tv_OffSet_Y;
	 EditText et_OffSet_X_value;
	 EditText et_OffSet_Y_value;
	 EditText sys_et_width;
	 EditText sys_et_height;
	 Button btn_ensure;
	 Button btn_cancel;
	 TextView activityNameTextView;
	 TextView txtVersionTextView;
	 
     int    m_id; 
	 int    gLedid;
	 int ncfg3d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leddisplay_system_cardset);
		SerialPortControlBroadCast.SetCurrentContext(this);
		Bundle bundle = getIntent().getExtras();
		m_id =  (Integer) bundle.get("id");
		
		gLedid = Ak10Application.GetLedId();
		
		initView();	
		
		LoadDataFromDb(m_id);
		
		ChnData tChnData = ChannelDB.GetRecordById(m_id, gLedid);
		String strVersion = ChanComm.GetAcqCardSoftwareVersion(tChnData.macaddress);
		if (strVersion!=null) {
			txtVersionTextView.setText(strVersion);
		}
		
		activityNameTextView.setText(getString(R.string.tv_syscard)+ m_id%1000 + "_"+m_id/1000);
	}
	private void initView() {

		tv_Set_Width = (TextView) findViewById(R.id.tv_width);
		tv_Set_Height = (TextView) findViewById(R.id.tv_Sysheight);
		tv_OffSet_X = (TextView) findViewById(R.id.tv_Sysoffset_x);
		tv_OffSet_Y = (TextView) findViewById(R.id.tv_offset_y);
		et_OffSet_X_value =(EditText) findViewById(R.id.edit_Offsetx);
		et_OffSet_Y_value =(EditText) findViewById(R.id.edit_Offsety);
		sys_et_width = (EditText) findViewById(R.id.Editwidth);
		sys_et_height = (EditText) findViewById(R.id.Editheight);
		btn_ensure = (Button) findViewById(R.id.btn_sysensure);
		btn_cancel = (Button) findViewById(R.id.btn_sysCancel);
		btn_ensure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		txtVersionTextView = (TextView) findViewById(R.id.txtSysVersion);
		activityNameTextView=(TextView) findViewById(R.id.tv_syscard_set);  
		
	}

    private void LoadDataFromDb(int id)
    {
    	ChnData tChanData = ChannelDB.GetRecordById(id, gLedid);
    	
    	sys_et_width.setText(String.valueOf(tChanData.width));
    	sys_et_height.setText(String.valueOf(tChanData.height));
    	et_OffSet_X_value.setText(String.valueOf(tChanData.offsetX));
    	et_OffSet_Y_value.setText(String.valueOf(tChanData.offsetY));
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_sysensure:
      
			String strW = sys_et_width.getText().toString();
			String strH = sys_et_height.getText().toString();
			String strX = et_OffSet_X_value.getText().toString();
			String strY = et_OffSet_Y_value.getText().toString();
			
			//if(strX.equals("")||strY.equals(""))
			if (0 == strX.length() || 0 == strY.length()) 
			{
				 Toast.makeText(this,getString(R.string.offsetnull), Toast.LENGTH_SHORT).show();
				 return;
			}
			if (0 == strW.length() || 0 == strH.length())
			//if(strW.equals("")||strH.equals(""))
			{
				 Toast.makeText(this,getString(R.string.heightnull), Toast.LENGTH_SHORT).show();
				 return;
			}
			
			ChannelDB.UpdateChannelPosParam(m_id, Integer.valueOf(strX),
					Integer.valueOf(strY),  Integer.valueOf(strW), Integer.valueOf(strH), gLedid);
			
	        finish();
			break;
		case R.id.btn_sysCancel:
			finish();
			break;

		default:
			break;
		}
	}

}
