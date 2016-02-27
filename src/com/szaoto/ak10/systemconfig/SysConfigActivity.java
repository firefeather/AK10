package com.szaoto.ak10.systemconfig;


import com.szaoto.ak10.R;
import com.szaoto.ak10.common.SystemConfig;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.dataaccess.DataAccessSystemConfig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SysConfigActivity extends FragmentActivity{
	//����Fragment
	NetWorkSettingFragment ethFragment = null;
	LanguageSetFragment langsetFragment = null;
//	SystemDiagnoseFragment sysdiagFragment = null;
//	SystemPwdFragment syspwdFragment =null;
//	SystemResetFragment sysrstFragment =null;
//	SystemSecurityFragment systemSecurityFragment =null;
	SystemUpgrateFragment  systemUpgrateFragment =null;
	SysCfg3dFragment syscfg3DFragment = null;
	UIControlFragment uictrlFragment = null;
	
	TextView txtTitle;
	
	public static  SystemConfig systemconfig = null;
	
	private RadioGroup mFunSelGroup;
	
	TextView btnBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sysconfig);
		SerialPortControlBroadCast.SetCurrentContext(this);
		systemconfig = DataAccessSystemConfig.LoadSystemConfig();
		txtTitle = (TextView) findViewById(R.id.systitle);
		txtTitle.setText(R.string.sysupdate);
		
		initView();
	}
	
	public void initView()
	{
		
		btnBack = (TextView) findViewById(R.id.text_systemtback);
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			     finish();	
			}
		});
		
		mFunSelGroup = (RadioGroup) findViewById(R.id.radio_funcsel);
		
		mFunSelGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				
				switch (checkedId) {
				
		           case R.id.uicontrol:
		       		if (uictrlFragment == null) {
		       			uictrlFragment = new UIControlFragment();
						// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
						addFragment(uictrlFragment);
						showFragment(uictrlFragment);
					} else {
						if (uictrlFragment.isHidden()) {
							showFragment(uictrlFragment);
						}
					}
		       		txtTitle.setText(R.string.uicontrol);
		       		
		        	break;
//		           case R.id.syspwd:
//		        	   
//			       		if (syspwdFragment == null) {
//			       			syspwdFragment = new SystemPwdFragment();
//							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
//							addFragment(syspwdFragment);
//							showFragment(syspwdFragment);
//						} else {
//							if (syspwdFragment.isHidden()) {
//								showFragment(syspwdFragment);
//							}
//						}  
//			       		txtTitle.setText(R.string.syspwd);
//		        	   break;
//		           case R.id.syssecurity:
//		        	   
//			       		if (systemSecurityFragment == null) {
//			       			systemSecurityFragment = new SystemSecurityFragment();
//							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
//							addFragment(systemSecurityFragment);
//							showFragment(systemSecurityFragment);
//						} else {
//							if (systemSecurityFragment.isHidden()) {
//								showFragment(systemSecurityFragment);
//							}
//						}  
//			       		txtTitle.setText(R.string.syssecurity);
//			       		
//		        	   break;
//		           case R.id.sysdiag:
//		        	   
//			       		if (sysdiagFragment == null) {
//			       			sysdiagFragment = new SystemDiagnoseFragment();
//							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
//							addFragment(sysdiagFragment);
//							showFragment(sysdiagFragment);
//						} else {
//							if (sysdiagFragment.isHidden()) {
//								showFragment(sysdiagFragment);
//							}
//						}  
//			       		
//			       		txtTitle.setText(R.string.sysdiag);
//		        	   
//		        	   break;
//		           case R.id.network:
//		        	   
//			       		if (ethFragment == null) {
//			       			ethFragment = new EthrenetSetFragment();
//							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
//							addFragment(ethFragment);
//							showFragment(ethFragment);
//						} else {
//							if (ethFragment.isHidden()) {
//								showFragment(ethFragment);
//							}
//						} 
//		        	   
//			       		txtTitle.setText(R.string.networksetting);
//			       		
//		        	   break;
//		           case R.id.sysreset:
//		        	   
//			       		if (sysrstFragment == null) {
//			       			sysrstFragment = new SystemResetFragment();
//							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
//							addFragment(sysrstFragment);
//							showFragment(sysrstFragment);
//						} else {
//							if (sysrstFragment.isHidden()) {
//								showFragment(sysrstFragment);
//							}
//						}
//			       	  txtTitle.setText(R.string.sysreset);
//		        	   break;
		           case R.id.sysupdate:
		        	   
			       		if (systemUpgrateFragment == null) {
			       			systemUpgrateFragment = new SystemUpgrateFragment();
							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
							addFragment(systemUpgrateFragment);
							showFragment(systemUpgrateFragment);
						} else {
							if (systemUpgrateFragment.isHidden()) {
								showFragment(systemUpgrateFragment);
							}
						}
		        	   
			       	 txtTitle.setText(R.string.sysupdate);
		        	   
		        	   break;
		           case R.id.langsetting:
		        	   
			       		if (langsetFragment == null) {
			       			langsetFragment = new LanguageSetFragment();
							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
							addFragment(langsetFragment);
							showFragment(langsetFragment);
						} else {
							if (langsetFragment.isHidden()) {
								showFragment(langsetFragment);
							}
						}
			       	 txtTitle.setText(R.string.langsetting);
		        	   break;
		        	   
		           case R.id.netsetting:
		        	   
		        	   if (ethFragment == null) {
		        		   ethFragment = new NetWorkSettingFragment();
		        		   // �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
		        		   addFragment(ethFragment);
		        		   showFragment(ethFragment);
		        	   } else {
		        		   if (ethFragment.isHidden()) {
		        			   showFragment(ethFragment);
		        		   }
		        	   }
		        	   txtTitle.setText(R.string.networksetting);
		        	   break;

					case R.id.othersetting:
			       		if (syscfg3DFragment == null) {
			       			syscfg3DFragment = new SysCfg3dFragment();
							// �жϵ�ǰ�����Ƿ����أ�������ؾͽ���������ʾ��false��ʾ��ʾ��true��ʾ��ǰ��������
							addFragment(syscfg3DFragment);
							showFragment(syscfg3DFragment);
						} else {
							if (syscfg3DFragment.isHidden()) {
								showFragment(syscfg3DFragment);
							}
						}   
			       	 	txtTitle.setText(R.string.Tridsetting);
			       	 	break;		   
				}				
			}
		});	
		
		
		if (systemUpgrateFragment == null) {
			systemUpgrateFragment = new SystemUpgrateFragment();
			addFragment(systemUpgrateFragment);
			showFragment(systemUpgrateFragment);
		} else {
			showFragment(systemUpgrateFragment);
		}

	}
	
	/** ����Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_layout, fragment);
		ft.commit();
	}

	/** ɾ��Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	/** ��ʾFragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		// ����Fragment���л�����
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// �ж�ҳ���Ƿ��Ѿ�����������Ѿ���������ô�����ص�
		if (ethFragment != null) {
			ft.hide(ethFragment);
		}
		if (langsetFragment != null) {
			ft.hide(langsetFragment);
		}
//		if (sysdiagFragment != null) {
//			ft.hide(sysdiagFragment);
//		}
//		if (syspwdFragment != null) {
//			ft.hide(syspwdFragment);
//		}
//		if (sysrstFragment != null) {
//			ft.hide(sysrstFragment);
//		}
//
//		if (systemSecurityFragment != null) {
//			ft.hide(systemSecurityFragment);
//		}
		if (systemUpgrateFragment != null) {
			ft.hide(systemUpgrateFragment);
		}
		if (syscfg3DFragment != null) {
			ft.hide(syscfg3DFragment);
		}
		if (uictrlFragment != null) {
			ft.hide(uictrlFragment);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	
}