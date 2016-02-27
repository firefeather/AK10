/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;

import java.io.IOException;
import com.szaoto.ak10.dataaccess.DataAccessSystemConfig;
import com.szaoto.ak10.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/*
 * 类名ControlActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class SystemSecurityFragment extends Fragment {

	RadioButton udiviceButton;
	RadioButton otherRadioButton;
	 RadioGroup radioGroup;
	   CheckBox udeviceBox;
	   CheckBox otherBox;
	     Button btnsave;
//	SystemConfig systemConfig;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.systemlisence);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SystemConfigActivity.systemconfig  = DataAccessSystemConfig.LoadSystemConfig();
		View view =inflater.inflate(R.layout.systemlisence, null);
		udiviceButton = (RadioButton)view.findViewById(R.id.Udevice);
		otherRadioButton = (RadioButton)view.findViewById(R.id.othersource);
		udeviceBox = (CheckBox)view.findViewById(R.id.UdeviceCheckBox);
		otherBox = (CheckBox)view.findViewById(R.id.OtherSourceCheckBox);
		radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup1);
		btnsave = (Button)view.findViewById(R.id.lisencesave);
		
		if(SystemConfigActivity.systemconfig.getSystemLisencetype() == 0)
		{
			udiviceButton.setChecked(true);
			udeviceBox.setVisibility(View.VISIBLE);
			otherBox.setVisibility(View.INVISIBLE);
		//	btn_Slot1.setVisibility(View.INVISIBLE)
			if(SystemConfigActivity.systemconfig.getSystemLisencestate())
			{
				udeviceBox.setChecked(true);
			}
			else{
				udeviceBox.setChecked(false);
			}
		}else{
			otherRadioButton.setChecked(true);
			otherBox.setVisibility(View.VISIBLE);
			udeviceBox.setVisibility(View.INVISIBLE);
			if(SystemConfigActivity.systemconfig .getSystemLisencestate())
			{
				otherBox.setChecked(true);
			}
			else{
				otherBox.setChecked(false);
			}
		}
		udiviceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				udiviceButton.setSelected(true);
				udeviceBox.setVisibility(View.VISIBLE);
				otherBox.setVisibility(View.INVISIBLE);
			//	btn_Slot1.setVisibility(View.INVISIBLE)
				if(SystemConfigActivity.systemconfig .getSystemLisencestate())
				{
					udeviceBox.setSelected(true);
				}
				else{
					udeviceBox.setSelected(false);
				}
			}
		});
		otherRadioButton .setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				udiviceButton.setSelected(false);
				otherBox.setVisibility(View.VISIBLE);
				udeviceBox.setVisibility(View.INVISIBLE);
				if(SystemConfigActivity.systemconfig .getSystemLisencestate())
				{
					otherBox.setSelected(true);
				}
				else{
					otherBox.setSelected(false);
				}
			}
		});
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 0:
					udiviceButton.setSelected(true);
					udeviceBox.setVisibility(View.VISIBLE);
					otherBox.setVisibility(View.INVISIBLE);
				//	btn_Slot1.setVisibility(View.INVISIBLE)
					if(SystemConfigActivity.systemconfig .getSystemLisencestate())
					{
						udeviceBox.setSelected(true);
					}
					else{
						udeviceBox.setSelected(false);
					}
					break;
				case 1:
					udiviceButton.setSelected(false);
					otherBox.setVisibility(View.VISIBLE);
					udeviceBox.setVisibility(View.INVISIBLE);
					if(SystemConfigActivity.systemconfig .getSystemLisencestate())
					{
						otherBox.setSelected(true);
					}
					else{
						otherBox.setSelected(false);
					}
					break;
				default:
					break;
				}
			}
		});
			btnsave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(udiviceButton.isChecked())
					{
					SystemConfigActivity.systemconfig .setSystemLisencetype(0);
						if (udeviceBox.isChecked()) {
							SystemConfigActivity.systemconfig .setSystemLisencestate(true);
						}
					}
				else
				{
					SystemConfigActivity.systemconfig .setSystemLisencetype(1);
					if (otherBox.isChecked()) {
						SystemConfigActivity.systemconfig .setSystemLisencestate(true);
					}
				}
				try {
					DataAccessSystemConfig.SaveSystemConfig(SystemConfigActivity.systemconfig );
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		//return super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}
	//OnCheckedChangeListener()
}
