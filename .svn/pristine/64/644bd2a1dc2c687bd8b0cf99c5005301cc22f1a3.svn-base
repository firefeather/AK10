package com.szaoto.ak10.leddisplay;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.colortemp.ColorTempSetActivity;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;

/**
 * 
 * 类名：LedDisplaySetupActivity
 * 功能：LED显示屏配置界面，选择LED屏，可以对采集卡，
 *          发送卡进行设置，配置操作
 * @author zhangsj
 *
 */
public class LedSelActivity extends Activity implements OnClickListener{
	
	private static LedSelActivity mLedSelActivity = null;
	ImageView ledisplay1;      
	ImageView ledisplay2;    
	ImageView ledisplay3;
	ImageView ledisplay4;
	 
	ImageView[] imageArr;

	TextView txtTitle;	
	TextView btn_cabinetlibrary; //箱体库
    TextView txt_Back;

    TextView btnColorTemper;
    TextView btnGamma;
    
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leddisplay);
		SerialPortControlBroadCast.SetCurrentContext(this);
		mLedSelActivity = this;
		//加载xml中的箱体数目
	    initView();
	}
    //增加
	// 初始化各控件
	private void initView() {
		// TODO Auto-generated method stub
		
		txtTitle = (TextView) findViewById(R.id.leddisplay_config_text);
		
		//txtTitle.setText("LED配置");
		
		ledisplay1 = (ImageView) findViewById(R.id.leddisplay1);
		ledisplay2 = (ImageView) findViewById(R.id.leddisplay2);
		ledisplay3 = (ImageView) findViewById(R.id.leddisplay3);
		ledisplay4 = (ImageView) findViewById(R.id.leddisplay4);

		imageArr=new ImageView[4];
		imageArr[0]=ledisplay1;
		imageArr[1]=ledisplay2;
		imageArr[2]=ledisplay3;
		imageArr[3]=ledisplay4;
		btn_cabinetlibrary = (TextView) findViewById(R.id.btn_cabinetlibrary);
		txt_Back = (TextView) findViewById(R.id.text_leddisplayback);
		
		
		btnColorTemper = (TextView) findViewById(R.id.btn_colortemper);
		btnGamma = (TextView) findViewById(R.id.btn_gamma);
		ledisplay1.setOnClickListener(this);
		ledisplay2.setOnClickListener(this);
		ledisplay3.setOnClickListener(this);
		ledisplay4.setOnClickListener(this);
		btnColorTemper.setOnClickListener(this);
		btnGamma.setOnClickListener(this);
				

		btn_cabinetlibrary.setOnClickListener(this);
		txt_Back.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.btn_colortemper:
			Intent tIntent = new Intent(LedSelActivity.this,ColorTempSetActivity.class);
			tIntent.putExtra("LoadType", "ColorTemper");
			
			try {
				startActivity(tIntent);
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			break;
			
		case R.id.btn_gamma:
			
			Intent tIntent1 = new Intent(LedSelActivity.this,ColorTempSetActivity.class);
			tIntent1.putExtra("LoadType", "Gamma");
			
			try {
				startActivity(tIntent1);
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			break;
		
		case R.id.leddisplay1:
			Ak10Application.SetLedId(1);
			startActivity(new Intent(LedSelActivity.this,	CabinetAddActivity.class));
			break;
		case R.id.leddisplay2:
			Ak10Application.SetLedId(2);
			startActivity(new Intent(LedSelActivity.this,	CabinetAddActivity.class));
			break;
		case R.id.leddisplay3:
			Ak10Application.SetLedId(3);
			startActivity(new Intent(LedSelActivity.this,	CabinetAddActivity.class));
			break;
		case R.id.leddisplay4:
			Ak10Application.SetLedId(4);
			startActivity(new Intent(LedSelActivity.this,	CabinetAddActivity.class));
			break;

		case R.id.btn_cabinetlibrary:   //箱体库管理
			try {
				startActivity(new Intent(LedSelActivity.this,	CabinetLibraryActivity.class));
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;

		case R.id.text_leddisplayback:   //返回
			this.finish();
			break;
  
		default:
			break;
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	public static Activity getInstance() {
		if (mLedSelActivity != null) {
			return mLedSelActivity;
		}
		return null;
	}
}
