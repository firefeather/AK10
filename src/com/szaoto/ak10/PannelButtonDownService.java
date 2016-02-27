/*
   * 文件名 ExternalStorageService.java
   * 包含类名列表com.szaoto.ak10
   * 版本信息，版本号
   * 创建日期2013年12月10日下午7:30:46
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;

/*
 * 类名ExternalStorageService
 * 作者 liangdb
 * 主要功能 外部设备服务
 * 创建日期2013年12月10日
 * 修改者，修改日期，修改内容
 */

public class PannelButtonDownService extends Service {
	public static List<IInfoChangeObserver> observers = new ArrayList<IInfoChangeObserver>();
	
	private static final String TAG = "PannelButtonDownService";

	private BroadcastReceiver PannelButtonDownReceiver = null;

	/**
	 * 
	 */
	public PannelButtonDownService() {
		// TODO Auto-generated constructor stub
		
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		registerReceivers();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy------");
		super.onDestroy();
		unregisterReceivers();
	}
	/**
	 * 注册广播
	 */
	private void registerReceivers() {
		if (PannelButtonDownReceiver == null) {
			PannelButtonDownReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					final String action = intent.getAction();
					final String message = intent.getExtras().getString("cmd");
					final String ClassName = intent.getExtras().getString("classname");
					Log.d(TAG, "receive action = " + action);
					boolean value = intent.getBooleanExtra("read-only", true);
					Log.d(TAG, "external storage path = " + message);
					Log.d(TAG, "external storage value = " + value);
					
		    		//	if (SerialPortControlBroadCast.DYNAMICACTION .equals(action)) 
					if (action .equals(SerialPortControlBroadCast.getDYNAMICACTION())) 
		    		{
		    			int nSize = observers.size();
		    			if (null != observers && 0 != nSize) {
							for (int i = 0; i < nSize; i++) {
								observers.get(i).onChangedNotifyKey(action,ClassName, message);
							}
						}
		    		} 
				}
			};
			final IntentFilter filter = new IntentFilter();
			filter.addAction(SerialPortControlBroadCast.getDYNAMICACTION());
			
			// 必须添加，否则无法接收到广播
	//		filter.addDataScheme("file");
			registerReceiver(PannelButtonDownReceiver, filter);
		}
	}
	/**
	 * 取消注册
	 */
	private void unregisterReceivers() {
		if (PannelButtonDownReceiver != null) {
			unregisterReceiver(PannelButtonDownReceiver);
			PannelButtonDownReceiver = null;
		}
	}
}
