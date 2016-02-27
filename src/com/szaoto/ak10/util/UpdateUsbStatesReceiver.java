package com.szaoto.ak10.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import com.szaoto.ak10.systemconfig.SystemUpgrateFragment;
/**
 * ����UsbStatesReceiver
 * ���� zhangsj
 * ��Ҫ���� �����ļ�����ģ��U��״̬���
 * ��������2015��5��13��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class UpdateUsbStatesReceiver extends BroadcastReceiver  {
	SystemUpgrateFragment execactivity;

	public static final int USB_STATE_MSG = 0x00020;  
    public static final int USB_STATE_ON = 0x00021;  
    public static final int USB_STATE_OFF = 0x00022;  
    public IntentFilter filter = new IntentFilter();
    
	public UpdateUsbStatesReceiver(SystemUpgrateFragment context){
		execactivity = (SystemUpgrateFragment)context;
		filter.addAction(Intent.ACTION_MEDIA_CHECKING);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addDataScheme("file"); 	
	}

	public Intent registerReceiver() {
		return  execactivity.getActivity().registerReceiver(this, filter);
	}

	public void unregisterReceiver() {
		(execactivity).getActivity().unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(execactivity.mhandler == null){
			return;
		}
		
		Message msg = new Message();
	    msg.what = USB_STATE_MSG;  
	      
	    if( intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED ) ||  
	    		intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)){  
	        msg.arg1 = USB_STATE_ON;  
	    }else{  
	        msg.arg1 = USB_STATE_OFF;  
	    }  
	    execactivity.mhandler.sendMessage(msg);  
	};
	
}