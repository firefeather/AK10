/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.common.SystemConfig;
import com.szaoto.ak10.commsdk.PannelLedControlBroadCast;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.dataaccess.DataAccessSystemConfig;



/*
 * 类名SystemConfigActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class SystemConfigActivity extends FragmentActivity {
	//public static  byte Type  ;
	public static  SystemConfig systemconfig = null;
	private static SystemConfigActivity mSystemConfigActivity = null;
	TextView m_UIcontrolTextView      ;
	TextView m_SystemPasswordTextView ;
	TextView m_EthrneTextView         ;
	TextView m_SystemResetTextView    ;
	TextView m_SystemDiagnoseTextView ;
	TextView m_SystemSecurityTextView ;
	TextView m_SystemUpgrateTextView  ;
	TextView m_SystemLanguageTextView ;
	Button   m_BtnSysBack             ;
	android.support.v4.view.ViewPager viewPager ;
	android.support.v4.app.FragmentManager fragmentManager ;
	UIControlFragment m_UiFragment;
	SystemPwdFragment m_SystemPasswordFragment;
	EthrenetSetFragment m_EthrenetFragment;
	static SystemDiagnoseFragment m_SystemDiagnoseActivity;
	int[] selectList;
	int selectID = 0;
	SystemConfig sytconfig;
	Bundle bd;
	public static SystemConfigActivity getInstance() {
		if (getmSystemConfigActivity() != null) {
			return getmSystemConfigActivity();
		}
		return null;
	}
	@Override
	protected void onRestart() {
		SerialPortControlBroadCast.SetCurrentContext(this);
		super.onRestart();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		SerialPortControlBroadCast.mCurrentContext = null;
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemconfig_layout);
		SerialPortControlBroadCast.SetCurrentContext(this);
		setmSystemConfigActivity(this);
		systemconfig = DataAccessSystemConfig.LoadSystemConfig();
		SerialPortControlBroadCast.SetCurrentContext(this);
		 m_UIcontrolTextView = (TextView)findViewById(R.id.uicontroltextview);
		 m_SystemPasswordTextView = (TextView)findViewById(R.id.systempassword);
		 m_EthrneTextView = (TextView)findViewById(R.id.ethrenetsetting);
		 m_SystemResetTextView = (TextView)findViewById(R.id.systemreset);
		 m_SystemDiagnoseTextView = (TextView)findViewById(R.id.systemdignose);
		 m_SystemSecurityTextView  = (TextView)findViewById(R.id.systemsecurity);
		 m_SystemUpgrateTextView = (TextView)findViewById(R.id.systemupdate);
		 m_SystemLanguageTextView =  (TextView)findViewById(R.id.languagesetting);
		 m_BtnSysBack =  (Button)findViewById(R.id.btn_syt_back);
		 
		 viewPager = (ViewPager) findViewById(R.id.viewPager);

		 initData();
		 sytconfig = DataAccessSystemConfig.LoadSystemConfig();
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}
	public static SystemDiagnoseFragment getSystemDiagnoseActivity() {
		return m_SystemDiagnoseActivity;
	}
	
	private void initData() {
		selectList = new int[] { 0, 1, 1, 1 , 1, 1, 1 ,1};// 0表示选中，1表示未选中(默认第一个选中)
		m_UIcontrolTextView.setOnClickListener(clickHandler);
		m_SystemPasswordTextView.setOnClickListener(clickHandler);
		m_EthrneTextView.setOnClickListener(clickHandler);
		m_SystemResetTextView.setOnClickListener(clickHandler);
		m_SystemDiagnoseTextView.setOnClickListener(clickHandler);
		m_SystemSecurityTextView. setOnClickListener(clickHandler);
		m_SystemUpgrateTextView.setOnClickListener(clickHandler);
		m_SystemLanguageTextView.setOnClickListener(clickHandler);
		 m_BtnSysBack.setOnClickListener(clickHandler);
		viewPager.setAdapter(adapter);
		m_SystemDiagnoseActivity = (SystemDiagnoseFragment) adapter.getItem(3);
		viewPager.setOnPageChangeListener(changeListener);
	}
	private SimpleOnPageChangeListener changeListener=new SimpleOnPageChangeListener(){
		public void onPageSelected(int position) {
			setSelectedTitle(position);
		//	viewPager.setCurrentItem(position);//
		}
	};
	private FragmentPagerAdapter adapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {
		
		public int getCount() {
			return selectList.length;
		}
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment=new UIControlFragment();
				break;
			case 1:
				fragment=new SystemPwdFragment();
				break;
			case 2:
				fragment=new SystemSecurityFragment();
				break;
			case 3:
				fragment = new SystemDiagnoseFragment();
				m_SystemDiagnoseActivity = (SystemDiagnoseFragment) fragment;
				break;
			case 4:
				fragment=new EthrenetSetFragment();
				break;
			case 5:
				fragment=new SystemResetFragment();
				break;
			case 6:
				fragment=new SystemUpgrateFragment();//系统升级
				break;
			case 7:
				fragment=new LanguageSetFragment();//语言设置
				break;
			}
			return fragment;
		}
	};
	View.OnClickListener clickHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_syt_back:
				SerialPortControlBroadCast.systemconfigtype = false;
				for (int i = 0; i < SerialPortControlBroadCast.isLight.length; i++) {
					SerialPortControlBroadCast.isLight[i] = false;
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				finish();
				break;
			case R.id.uicontroltextview:
				if (selectID == 0) {
					return;
				} else {
					setSelectedTitle(0);
					viewPager.setCurrentItem(0);
					SerialPortControlBroadCast.systemconfigtype = false;
				//	
					//viewPager.
				}
				break;
			case R.id.systempassword:
				
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				/*
				if (selectID == 1) {
					return;
				} else {
					setSelectedTitle(1);
					viewPager.setCurrentItem(1);
					SerialPortControlBroadCast.systemconfigtype = false;
			//		PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}*/
				break;
			case R.id.systemsecurity:
				/*
				if (selectID == 2) {
					return;
				} else {
					setSelectedTitle(2);
					viewPager.setCurrentItem(2);
				//	viewPager.getcu
					SerialPortControlBroadCast.systemconfigtype = false;
				//PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}*/
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				break;
			case R.id.systemdignose:
				
				/*
				if (selectID == 3) {
					return;
				} else {
		
					setSelectedTitle(3);
					viewPager.setCurrentItem(3);
				//	PannelLedControlBroadCast.MakeLightsAlwaysOFF();
					PannelLedControlBroadCast.systemconfigtype = true;
				}
				*/
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				break;
			case R.id.ethrenetsetting:
				
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				/*
				if (selectID == 4) {
					return;
				} else {
					setSelectedTitle(4);
					viewPager.setCurrentItem(4);
					SerialPortControlBroadCast.systemconfigtype = false;
				//	PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				*/
				break;
			
			case R.id.systemreset:
				
				Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
				/*
				if (selectID == 5) {
					return;
				} else {
					setSelectedTitle(5);
					viewPager.setCurrentItem(5);
					SerialPortControlBroadCast.systemconfigtype = false;
				//	PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}
				*/
				break;
			case R.id.systemupdate:
				if (selectID == 6) {
					return;
				} else {
					setSelectedTitle(6);
					viewPager.setCurrentItem(6);
					SerialPortControlBroadCast.systemconfigtype = false;
				//	PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}
				break;
			case R.id.languagesetting:
				if (selectID == 7) {
					return;
				} else {
					setSelectedTitle(7);
					viewPager.setCurrentItem(7);
					SerialPortControlBroadCast.systemconfigtype = false;
				//	PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				}
				break;
			default:
				break;
			}
		}
	};
	private void setSelectedTitle(int position) {
		for (int i = 0; i < selectList.length; i++) {
			if (selectList[i] == 0) {
				selectList[i] = 1;
			}
		}
		selectList[position] = 0;
		selectID = position;
	}
	public static SystemConfigActivity getmSystemConfigActivity() {
		return mSystemConfigActivity;
	}
	public static void setmSystemConfigActivity(SystemConfigActivity mSystemConfigActivity) {
		SystemConfigActivity.mSystemConfigActivity = mSystemConfigActivity;
	}
}
