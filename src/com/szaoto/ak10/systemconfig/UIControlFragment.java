/*
   * �ļ��� ControlActivity.java
   * ���������б�com.szaoto.ak10.control
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:51
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;



import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import com.szaoto.ak10.R;

/*
 * ����ControlActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class UIControlFragment extends Fragment  {


	SystemConfigActivity mApp;
	private SeekBar mbrightadjustSeekBar;
	Button btnsave;
	Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity = getActivity();
		View view = inflater.inflate(R.layout.uicontrol, null);
		
		mbrightadjustSeekBar = (SeekBar) view.findViewById(R.id.seekBarbright);
		btnsave = (Button) view.findViewById(R.id.uisave);


		
		mbrightadjustSeekBar.setMax(255);
		int normal = Settings.System.getInt(activity.getContentResolver(),  
	                Settings.System.SCREEN_BRIGHTNESS, 255); 
		
		mbrightadjustSeekBar.setProgress(normal);  
		
		mbrightadjustSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					    int tmpInt = seekBar.getProgress();  
					    
		                // ������С��80ʱ�����ó�80����ֹ̫�ڿ������ĺ����  
		                if (tmpInt < 80) {  
		                    tmpInt = 80;  
		                }  
		  
		                // ���ݵ�ǰ���ȸı�����  
		                Settings.System.putInt(getActivity().getContentResolver(),  
		                        Settings.System.SCREEN_BRIGHTNESS, tmpInt);  
		                tmpInt = Settings.System.getInt(getActivity().getContentResolver(),  
		                        Settings.System.SCREEN_BRIGHTNESS, -1);  
		                WindowManager.LayoutParams wl = getActivity().getWindow().getAttributes();  
		  
		                float tmpFloat = (float) tmpInt / 255;  
		                if (tmpFloat > 0 && tmpFloat <= 1) {  
		                    wl.screenBrightness = tmpFloat;  
		                }  
		                getActivity().getWindow().setAttributes(wl);  
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
				

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						
					}
				});

//		btnsave.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				int brightness = mbrightadjustSeekBar.getProgress();
//
//				SharedPreferences shp = getActivity().getSharedPreferences("bright_setting", 0);
//				SharedPreferences.Editor editor = shp.edit();
//				
//				editor.putInt("bright",brightness);
//				editor.commit();
//				
//				Toast.makeText(getActivity(),getString(R.string.OK), Toast.LENGTH_LONG).show();
//
//			}
//		});
		return view;
	}

	public void setActivity(SystemConfigActivity mainActivity) {
		// TODO Auto-generated method stub
		mApp = mainActivity;
	}



	public static void setBrightness(Activity activity, int brightness) {

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = brightness;
		activity.getWindow().setAttributes(lp);
	}

	public static void saveBrightness(ContentResolver resolver, int brightness) {
		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				brightness);
		resolver.notifyChange(uri, null);
	}

	public static int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

}
