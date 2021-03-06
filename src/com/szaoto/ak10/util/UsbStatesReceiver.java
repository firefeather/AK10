package com.szaoto.ak10.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;

import com.szaoto.ak10.player.AddPlayList;
/**
 * 类名UsbStatesReceiver
 * 作者 zhangsj
 * 主要功能 播放文件添加模块U盘状态检测
 * 创建日期2015年5月13日
 * 修改者，修改日期，修改内容
 */
public class UsbStatesReceiver extends BroadcastReceiver  {
	AddPlayList execactivity;

	public static final int USB_STATE_MSG = 0x00020;  
    public static final int USB_STATE_ON = 0x00021;  
    public static final int USB_STATE_OFF = 0x00022;  
    public IntentFilter filter = new IntentFilter();
    
	public UsbStatesReceiver(Context context){
		execactivity = (AddPlayList)context;
		filter.addAction(Intent.ACTION_MEDIA_CHECKING);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addDataScheme("file"); 	
	}

	public Intent registerReceiver() {
		return execactivity.registerReceiver(this, filter);
	}

	public void unregisterReceiver() {
		execactivity.unregisterReceiver(this);
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
