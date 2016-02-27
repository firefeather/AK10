package com.szaoto.ak10.monitor;

import android.content.Context;
import android.content.SharedPreferences;
public class ThresholdAdjust {
	SharedPreferences preferences ;
	SharedPreferences.Editor editor;
	private float SetEnvirTemvalue;
	private float SetEnvirHumvalue;
	private int SetEnvirBadLightvalue;
	private float SetCabinetTemvalue;
	private float SetCabinetHumvalue;
	private int SetCabinetBadlightvalue;
//	private float SetEnvirTemvalue;
	
	ThresholdAdjust(Context context)
	{
		preferences = context.getSharedPreferences("11", Context.MODE_WORLD_READABLE);
		editor = preferences.edit(); 
	//	preferences = context.getSharedPreferences("11",MODE_WORLD_READABLE); 
	}
	public boolean IsEnvirTempAlarm(float ReadEnvirTemvalue) {
		float fl = preferences.getFloat("temalarm", 0);
		if(ReadEnvirTemvalue <= fl)
			return false;
		else 
			return true;
		//preferences.getFloat("humialarm", 0) ;
	}
	public boolean IsEnvirHumAlarm(float ReadEnvirHumvalue) {
		
		if(ReadEnvirHumvalue <= preferences.getFloat("humialarm", 0))
			return true;
		else 
			return false;
		//preferences.getFloat("humialarm", 0) ;
	}
	
}
