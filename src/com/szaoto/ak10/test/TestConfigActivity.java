/*
s   * 文件名 TestConfigActivity.java
   * 包含类名列表com.szaoto.ak10.test
   * 版本信息，版本号
   * 创建日期2013年11月11日下午2:20:56
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.test;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
//import com.szaoto.ak10.configuration.SystemActivity.SpinnerXMLSelectedListener;
import com.szaoto.ak10.dataaccess.returnClass;

/*
 * 类名TestConfigActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月11日
 * 修改者，修改日期，修改内容
 */
public class TestConfigActivity extends TabActivity {
	//public class TestConfigActivity extends Activity {

	private static TestConfigActivity mTestConfigActivity;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	TabHost mTabHost ;
	private SharedPreferences preferences; 
	private SharedPreferences.Editor editor;
//	private NavigationBar navigationBar;
	EditText textltx;
	EditText textlty;
	EditText textwidth;
	EditText textheigh;
	EditText  testgray;
	Button testmodeset;
	EditText intervel;
	EditText testrobbinwandh;
	EditText testrobbinstartgray;
	EditText testgridintervel;
	Button autoset;
	Intent intent; 
	CheckBox checkfullscreen;  //全屏设置
	CheckBox checklinetype_hor;///线型4
	CheckBox checklinetype_ver;///线型4
	CheckBox checklinetype_splash;///线型4
	CheckBox checklinetype_backslack;//线型4
	RadioGroup radioGroupcolchange;// 变换选项 （颜色变换）
	public static String colchangetype;//   选中的变换项
	Spinner spinner;
	EditText testspotspacing;
	
	Button btnCancel;
	
	int spinnershoutypeindex;
	int modestate;

	public static TestConfigActivity getInstance () {
		   if (mTestConfigActivity != null) {
		       return mTestConfigActivity;
		   }
		   return null;
		}	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_config);
		SerialPortControlBroadCast.SetCurrentContext(this);
		mTestConfigActivity = this;
		SerialPortControlBroadCast.SetCurrentContext(this);
		onCreateTab();
		preferences = getSharedPreferences("11",MODE_WORLD_READABLE); 
        //获得修改器 
        editor = preferences.edit(); 
      /*  navigationBar = new NavigationBar("test_TestConfigActivity",this);
        navigationBar.setbSettingVisible(false);*/

        textltx     = (EditText)findViewById(R.id.EditTextltx);
		textlty 	= (EditText)findViewById(R.id.EditTextlty);
		textwidth   = (EditText)findViewById(R.id.EditTextwidth);
		textheigh   = (EditText)findViewById(R.id.EditTextheight);
		testmodeset = (Button)findViewById(R.id.btnmodeset);
		intervel    = (EditText)findViewById(R.id.Editintervel);
		autoset     = (Button)findViewById(R.id.btnautotest);
		testgray = (EditText)findViewById(R.id.testgraylevel);
		checkfullscreen = (CheckBox)findViewById(R.id.checktestconfigfullscreen);
		testrobbinwandh       = (EditText)findViewById(R.id.edittext_wandh);
		testrobbinstartgray   = (EditText)findViewById(R.id.edittest_startgray);
		testgridintervel      =  (EditText)findViewById(R.id.testgridintervel);
		checklinetype_hor 	  = (CheckBox)findViewById(R.id.testconfig_linetype1);
		checklinetype_ver	  = (CheckBox)findViewById(R.id.testconfig_linetype2);
		checklinetype_splash  = (CheckBox)findViewById(R.id.testconfig_linetype3);
		checklinetype_backslack = (CheckBox)findViewById(R.id.testconfig_linetype4);
		testspotspacing = (EditText)findViewById(R.id.spotspacing);
		spinner = (Spinner) findViewById(R.id.spinnerspotshowtype);
		
		btnCancel = (Button) findViewById(R.id.testcancel);
		btnCancel.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				 Intent intent = new Intent(TestConfigActivity.this,TestActivity.class);
                 setResult(-1, intent);
				 finish();	
			}
		});

		String showtype[] = {getString(R.string.test_spots_showtype1),getString(R.string.test_spots_showtype2)};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_spinner,showtype/*R.layout.simple_spinner_item*/); 
        adapter.setDropDownViewResource(R.layout.style_listoption);

		spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		radioGroupcolchange = (RadioGroup)findViewById(R.id.radioGroupcolchange);
		radioGroupcolchange.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.testconfigradio0)
				{
					colchangetype = "singlecolor";					
				}else if(checkedId == R.id.testconfigradio1)
				{
					colchangetype = "doublecolor";					
				}else if(checkedId == R.id.testconfigradio2)
				{
					colchangetype = "triplecolor";					
				}
			}
		});
		testmodeset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	setContentView(R.layout.test_config);
				if(modestate == 0){
					modestate = 1;
					testmodeset.setText(getString(R.string.test_mode_dynamic));
				}else{
					modestate = 0;
					testmodeset.setText(getString(R.string.test_mode_static));
				}		
			}
		});
		autoset.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	setContentView(R.layout.test_config);
//				if(autostate == 0){
//					autostate = 1;
//					autoset.setText(getString(R.string.test_mode_stateon));
//				}else{
//					autostate = 0;
//					autoset.setText(getString(R.string.test_mode_stateoff));			
//				}
			}
		});
		LoadTestConfigparm();
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {//响应测试 
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if(tabId.equals("gray")){
				//	setContentView(R.id.Layout_gray);
				}
				else if(tabId.equals("ribbon")){
				//	setContentView(R.id.Layout_ribbon);
				}
				else if(tabId.equals("grid")){
				//	setContentView(R.id.Layout_grid);
				}
				else if(tabId.equals("spots")){
				//	setContentView(R.id.Layout_spots);
				}
			}
		});
		Button btn =(Button)findViewById(R.id.testconfigsave);
		btn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				SaveCurSetting();
			}
		});
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	

	public void SaveCurSetting()
	{
		 intent = new Intent();
		 
		 int tX = Integer.valueOf(textltx.getText().toString());
		 if (tX>=1280) {
			
			 Toast.makeText(this, getString(R.string.text_xbig), Toast.LENGTH_SHORT).show();
			 
			 return;
		}
		 int tY = Integer.valueOf(textlty.getText().toString());
		 if (tY>=800) {
			 
			 Toast.makeText(this, getString(R.string.text_ybig), Toast.LENGTH_SHORT).show();
			 
			 return;
		 }
		 int tW = Integer.valueOf(textwidth.getText().toString());
		 if (tW>=1280) {
			 
			 Toast.makeText(this, getString(R.string.text_wbig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 int tH = Integer.valueOf(textheigh.getText().toString());
		 if (tH>=800) {	 
			 Toast.makeText(this, getString(R.string.text_hbig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 
		 editor.putInt("textltxtest", 	Integer.valueOf(textltx.getText().toString()));
		 editor.putInt("textltytest", 	Integer.valueOf(textlty.getText().toString()));
		 editor.putInt("textwidthtest", Integer.valueOf(textwidth.getText().toString()));
		 editor.putInt("textheightest", Integer.valueOf(textheigh.getText().toString()));
		 if(modestate ==1){
			 editor.putInt("testmodeset", 1);
		 }else {
			 editor.putInt("testmodeset", 0);
		} 
		 editor.putInt("intervel", 	Integer.valueOf(intervel.getText().toString()));
//		 if(autostate ==1){
//			 editor.putInt("autoset", 1);
//		 }else {
//			 editor.putInt("autoset", 0);
//		} 
		 
		 
		 int tGrayLvl = Integer.valueOf(testgray.getText().toString());
		 if (tGrayLvl>255) {
			 
			 Toast.makeText(this, getString(R.string.text_parambig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 
		 editor.putInt("testgraylevel",Integer.valueOf(testgray.getText().toString()));
		 editor.putBoolean("checkfullscreen",checkfullscreen.isChecked());
		
		 
		 int tRobbinwandh = Integer.valueOf(testrobbinwandh.getText().toString());
		 if (tRobbinwandh>1024) {
			 
			 Toast.makeText(this, getString(R.string.text_parambig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 
		 editor.putInt("testrobbinwandh",Integer.valueOf(testrobbinwandh.getText().toString()));
		 
		 
		 int tRobbinstartgray = Integer.valueOf(testrobbinstartgray.getText().toString());
		 if (tRobbinstartgray>=255) {
			 
			 Toast.makeText(this, getString(R.string.text_parambig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 
		 editor.putInt("testrobbinstartgray",Integer.valueOf(testrobbinstartgray.getText().toString()));
		 
		 
		 int ntestgridintervel = Integer.valueOf(testgridintervel.getText().toString());
		 if (ntestgridintervel>=1280) {
			 
			 Toast.makeText(this, getString(R.string.text_parambig), Toast.LENGTH_SHORT).show();
			 return;
		 }
		 
		 editor.putInt("testgridspacing",Integer.valueOf(testgridintervel.getText().toString()));
		
		 editor.putBoolean("checklinetype_hor",checklinetype_hor.isChecked());
		 editor.putBoolean("checklinetype_ver",checklinetype_ver.isChecked());
		 editor.putBoolean("checklinetype_LeftDiagonalLine",checklinetype_splash.isChecked());
		 editor.putBoolean("checklinetype_RightDiagonalLine",checklinetype_backslack.isChecked());
		 editor.putString("colchangetype",colchangetype);
		 editor.putInt("spinnershoutypeindex",spinnershoutypeindex);
		 editor.putInt("testspotspacing",Integer.valueOf(testspotspacing.getText().toString()));
		 editor.commit(); 
		 
		 
		 
		 intent.putExtra("textltxtest",   Integer.valueOf(textltx.getText().toString()));
		 intent.putExtra("textltytest",	  Integer.valueOf(textlty.getText().toString()));
		 intent.putExtra("textwidthtest", Integer.valueOf(textwidth.getText().toString()));
		 intent.putExtra("textheightest", Integer.valueOf(textheigh.getText().toString()));
		 
		 
		 intent.putExtra("checkfullscreen", checkfullscreen.isChecked());
		 if(modestate ==1){
			 intent.putExtra("testmodeset", 1);
		 }else {
			 intent.putExtra("testmodeset", 0);
		 }
//		if(autostate ==1){
//			intent.putExtra("autoset", 1);
//			}else {
//				intent.putExtra("autoset", 0);
//			} 
		intent.putExtra("testgraylevel", Integer.valueOf(testgray.getText().toString()));
		intent.putExtra("checkfullscreen", checkfullscreen.isChecked());
		intent.putExtra("intervel", Integer.valueOf(intervel.getText().toString()));
		intent.putExtra("testrobbinwandh", Integer.valueOf(testrobbinwandh.getText().toString()));
		intent.putExtra("testrobbinstartgray", Integer.valueOf(testrobbinstartgray.getText().toString()));
		intent.putExtra("testgridspacing", Integer.valueOf(testgridintervel.getText().toString()));
		intent.putExtra("checklinetype_hor", checklinetype_hor.isChecked());
		intent.putExtra("checklinetype_ver", checklinetype_ver.isChecked());
		intent.putExtra("checklinetype_LeftDiagonalLine", checklinetype_splash.isChecked());
		intent.putExtra("checklinetype_RightDiagonalLine", checklinetype_backslack.isChecked());
		intent.putExtra("colchangetype",colchangetype);
		intent.putExtra("spinnershoutypeindex",spinnershoutypeindex);
		intent.putExtra("testspotspacing",Integer.valueOf(testspotspacing.getText().toString()));

		/*	
		if(colchangetype.equals("singlecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(1);
		}
		else if(colchangetype.equals("doublecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(2);
		}
		else if(colchangetype.equals("triplecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(3);
		}
		*/
		
		setResult(0, intent);//设置返回数据 
		
		finish();//关闭Activity 
	}
	

	class SpinnerXMLSelectedListener implements OnItemSelectedListener{   
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,   
	             long arg3) {  
	    	spinnershoutypeindex = arg2;
	     }   
	    public void onNothingSelected(AdapterView<?> arg0) {   
	    	
	     }   		
	  } 
	void LoadTestConfigparm(){
		modestate = preferences.getInt("testmodeset", 0);
		if(modestate == 0){
			testmodeset.setText(getString(R.string.test_mode_static));
		}else{
			testmodeset.setText(getString(R.string.test_mode_dynamic));
		}
		
//		autostate = preferences.getInt("autoset", 0);
//		if(autostate == 0){
//			autoset.setText(getString(R.string.test_mode_stateoff));
//		}else{
//			autoset.setText(getString(R.string.test_mode_stateon));
//		}
		
		intervel.setText(String.valueOf(preferences.getInt("intervel", 50)));
		textltx.setText(String.valueOf(preferences.getInt("textltxtest", 0)));
		textlty.setText(String.valueOf(preferences.getInt("textltytest", 0)));
		textwidth.setText(String.valueOf(preferences.getInt("textwidthtest", 200)));
		textheigh.setText(String.valueOf(preferences.getInt("textheightest", 200)));
		checkfullscreen.setChecked(preferences.getBoolean("checkfullscreen", true));
		testgray.setText(String.valueOf(preferences.getInt("testgraylevel", 200)));
		testrobbinwandh.setText(String.valueOf(preferences.getInt("testrobbinwandh", 200)));
		testrobbinstartgray.setText(String.valueOf(preferences.getInt("testrobbinstartgray", 200)));
		testgridintervel.setText(String.valueOf(preferences.getInt("testgridspacing", 50)));
		
		checklinetype_hor.setChecked(preferences.getBoolean("checklinetype_hor", true));
		checklinetype_ver.setChecked(preferences.getBoolean("checklinetype_ver", true));
		checklinetype_splash.setChecked(preferences.getBoolean("checklinetype_LeftDiagonalLine", true));
		checklinetype_backslack.setChecked(preferences.getBoolean("checklinetype_RightDiagonalLine", true));
		colchangetype = preferences.getString("colchangetype", "singlecolor");
		if(colchangetype.equals("singlecolor"))
		{
			RadioButton btn = (RadioButton)findViewById(R.id.testconfigradio0);
			btn.setChecked(true);
			TestActivity.getInstance().SetGridColorCnt(1);
		}
		else if(colchangetype.equals("doublecolor"))
		{
			RadioButton btn = (RadioButton)findViewById(R.id.testconfigradio1);
			btn.setChecked(true);
			TestActivity.getInstance().SetGridColorCnt(2);
		}
		else if(colchangetype.equals("triplecolor"))
		{
			RadioButton btn = (RadioButton)findViewById(R.id.testconfigradio2);
			btn.setChecked(true);
			TestActivity.getInstance().SetGridColorCnt(3);
		}
		spinner.setSelection(preferences.getInt("spinnershoutypeindex", 0));
		testspotspacing.setText(String.valueOf(preferences.getInt("testspotspacing", 50)));
	}
	private void onCreateTab() {   
		mTabHost = getTabHost();
		//mTabHost = (TabHost) findViewById(R.id.tabhost);
        LayoutInflater inflater_tab1 = LayoutInflater.from(this);   
        
        inflater_tab1.inflate(R.layout.test_config_gray,   mTabHost.getTabContentView());
        inflater_tab1.inflate(R.layout.test_config_ribbon, mTabHost.getTabContentView());
        inflater_tab1.inflate(R.layout.test_config_grid,   mTabHost.getTabContentView());
        inflater_tab1.inflate(R.layout.test_config_spots,  mTabHost.getTabContentView());
        
        mTabHost.addTab(mTabHost.newTabSpec("gray").setIndicator(getString(R.string.TestConif_tab1name)).setContent(R.id.Layout_gray));
        mTabHost.addTab(mTabHost.newTabSpec("ribbon").setIndicator(getString(R.string.TestConif_tab2name)).setContent(R.id.Layout_ribbon));
        mTabHost.addTab(mTabHost.newTabSpec("grid").setIndicator(getString(R.string.TestConif_tab3name)).setContent(R.id.Layout_grid));
        mTabHost.addTab(mTabHost.newTabSpec("spots").setIndicator(getString(R.string.TestConif_tab4name)).setContent(R.id.Layout_spots)); 
        TabWidget tabWidget = (TabWidget)findViewById(android.R.id.tabs);
		int count = tabWidget.getChildCount();
		  for (int i = 0; i < count; i++) {
		   View view = tabWidget.getChildTabViewAt(i);   
		   final TextView tv = (TextView) view.findViewById(android.R.id.title);
		   tv.setTextSize(30);
		   tv.setTextColor(this.getResources().getColorStateList(
		     android.R.color.white));
		  }	
	}
	/**
	 * 
	 */
	public TestConfigActivity() {
		// TODO Auto-generated constructor stub
		
	}
}
