/*
   * �ļ��� MonitorConfigActivity.java
   * ���������б�com.szaoto.ak10.monitor
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:55:27
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.monitor;


import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;

/*
 * ����MonitorConfigActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
@SuppressWarnings("deprecation")
public class MonitorConfigActivity extends TabActivity {
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	EditText EvirEditAutotime;
	EditText EvirEdittemp;
	EditText EvirEditHuiSet;
	EditText EvirEditbadlightnum;
	EditText editcabT;
	EditText editcabH;
	EditText editcabBadnum;
	Intent intent;
	CheckBox checkBox;
	TabHost mTabHost ;
	private SharedPreferences preferences; 
	private SharedPreferences.Editor editor;
	private Button btnMonitorConf;
	private ImageView btnMonitorConfHome;
//	private NavigationBar navigationBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_config);
		SerialPortControlBroadCast.SetCurrentContext(this);
		preferences = getSharedPreferences("11",MODE_PRIVATE); 
        //����޸��� 
        editor = preferences.edit();
        onCreateTab();
		 EvirEditAutotime = new EditText(this);
		
		 EvirEdittemp = new EditText(this);
		 EvirEditHuiSet = new EditText(this);
		 EvirEditbadlightnum = new EditText(this);
		 editcabT = new EditText(this);
		 editcabH = new EditText(this);
		 editcabBadnum = new EditText(this);
		 checkBox = (CheckBox)findViewById(R.id.chk_editAutotime);
		 EvirEdittemp = (EditText)findViewById(R.id.edittemp);
		 EvirEditAutotime = (EditText)findViewById(R.id.editautotime);
		 EvirEditHuiSet = (EditText)findViewById(R.id.editHuiSet);
		 EvirEditbadlightnum = (EditText)findViewById(R.id.editbadlightnum);
		 editcabT = (EditText)findViewById(R.id.editcabT);
		 editcabH = (EditText)findViewById(R.id.editcabH);
		 editcabBadnum = (EditText)findViewById(R.id.editcabBadnum);
//		 navigationBar   = new NavigationBar("monitor_MonitorConfigActivity",this);
//		 navigationBar.setbSettingVisible(false);
		 btnMonitorConf = (Button)findViewById(R.id.btn_MonitorConfigBack);
		 btnMonitorConfHome = (ImageView)findViewById(R.id.MonitorConfigHome);
		 btnMonitorConf.setOnClickListener(ClickHandler);
		 btnMonitorConfHome.setOnClickListener(ClickHandler);
		 
		 
		 
		 
		 LoadMonitorConfigparm();
		
	
		 EvirEditHuiSet.addTextChangedListener(new TextWatcher() {
			   @Override
			   public void onTextChanged(CharSequence s, int start, int before, int count) {
			   }
			    // TODO Auto-generated method stub
			   @Override
			   public void beforeTextChanged(CharSequence s, int start, int count,
			     int after){}
			    // TODO Auto-generated method stub
			   @Override
			   public void afterTextChanged(Editable s) { }
			    // TODO Auto-generated method stu  
		});
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {//��Ӧ���� 
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-genrated method stub
				if(tabId.equals("EnvirSetting")){

				}
				else if(tabId.equals("CabinetSetting")){

				}
			}
		});
		Button btn = (Button)findViewById(R.id.btnmonitorsave);
        btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				editor.putInt("Autotime", Integer.valueOf(EvirEditAutotime.getText().toString()));
				editor.putFloat("temalarm", Float.valueOf(EvirEdittemp.getText().toString()));
				editor.putFloat("humialarm", Float.valueOf(EvirEditHuiSet.getText().toString()));
				editor.putInt("badlightnum", Integer.valueOf(EvirEditbadlightnum.getText().toString()));
				editor.putBoolean("Autotimecheck", checkBox.isChecked());
				// intent.putExtra("Autotimecheck", checkBox.isChecked());
				editor.putFloat("cabtempalarm", Float.valueOf(editcabT.getText().toString()));
				editor.putFloat("cabhumialarm", Float.valueOf(editcabH.getText().toString()));
				editor.putInt("cabbadlightnum", Integer.valueOf(editcabBadnum.getText().toString()));
				editor.commit(); 
				 
			   intent = new Intent();//������ʹ��Intent����
	    	   intent.putExtra("Autotime", EvirEditAutotime.getText().toString());
	    	   intent.putExtra("Autotimecheck", checkBox.isChecked());
	    	   
	    	   intent.putExtra("temalarm", EvirEdittemp.getText().toString());
	           intent.putExtra("humialarm",EvirEditHuiSet.getText().toString());
	           intent.putExtra("badlightnum", EvirEditbadlightnum.getText().toString());
	           intent.putExtra("cabtempalarm", editcabT.getText().toString());
	           intent.putExtra("cabhumialarm", editcabH.getText().toString());
	           intent.putExtra("cabbadlightnum", editcabBadnum.getText().toString());
	           MonitorConfigActivity.this.setResult(RESULT_OK, intent);//���÷������� 
	           MonitorConfigActivity.this.finish();//�ر�Activity 
			}
        });	
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	View.OnClickListener ClickHandler = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

                      switch (v.getId()) {
					case R.id.btn_MonitorConfigBack:
						finish();
						break;
					case R.id.MonitorConfigHome:
						startActivity(new Intent(MonitorConfigActivity.this,HomePageActivity.class));
						break;
			
					}
			
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		intent = null;
	}
	private void onCreateTab() {   
		mTabHost = getTabHost();
		//mTabHost = (TabHost) findViewById(R.id.tabhost);
        LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
        inflater_tab1.inflate(R.layout.monitor_cabinet_setting, mTabHost.getTabContentView());
        inflater_tab1.inflate(R.layout.monitor_enviroment_setting,mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec("CabinetSetting").setIndicator(getString(R.string.monitor_cabinet_setting)).setContent(R.id.Layout_monitor_cabinet_setting));
        mTabHost.addTab(mTabHost.newTabSpec("EnvirSetting").setIndicator(getString(R.string.monitor_environment_setting)).setContent(R.id.Layout_monitor_environment_setting));
        
        TabWidget tabWidget = (TabWidget)findViewById(android.R.id.tabs);
		int count = tabWidget.getChildCount();
		  for (int i = 0; i < count; i++) {
		   View view = tabWidget.getChildTabViewAt(i);   
		   final TextView tv = (TextView) view.findViewById(android.R.id.title);
		   tv.setTextSize(20);
		   tv.setTextColor(this.getResources().getColorStateList(
		     android.R.color.white));
		  }	
	}
	void LoadMonitorConfigparm()//�������ò��� ��Ĭ��ֵΪ0
	{
		 EvirEditAutotime.setText(String.valueOf(preferences.getInt("Autotime", 0)));
		 EvirEdittemp.setText(String.valueOf(preferences.getFloat("temalarm", 0))) ;
		 EvirEditHuiSet.setText(String.valueOf(preferences.getFloat("humialarm", 0))) ;
		 EvirEditbadlightnum.setText(String.valueOf(preferences.getInt("badlightnum", 0)));
		 checkBox.setChecked(preferences.getBoolean("Autotimecheck", false));
		 
		 editcabT.setText(String.valueOf(preferences.getFloat("cabtempalarm", 0))) ;
		 editcabH.setText(String.valueOf(preferences.getFloat("cabhumialarm", 0))) ;
		 editcabBadnum.setText(String.valueOf(preferences.getInt("cabbadlightnum", 0)));
	}
}