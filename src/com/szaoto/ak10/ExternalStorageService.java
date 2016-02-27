/*
   * �ļ��� ExternalStorageService.java
   * ���������б�com.szaoto.ak10
   * �汾��Ϣ���汾��
   * ��������2013��12��10������7:30:46
   * ��Ȩ���� liangdb-szaoto
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

/*
 * ����ExternalStorageService
 * ���� liangdb
 * ��Ҫ���� �ⲿ�豸����
 * ��������2013��12��10��
 * �޸��ߣ��޸����ڣ��޸�����
 */

public class ExternalStorageService extends Service {
	public static List<IInfoChangeObserver> observers = new ArrayList<IInfoChangeObserver>();
	private static final String TAG = "ExternalStorageService";
	private BroadcastReceiver externalStorageReceiver = null;

	/**
	 * 
	 */
	public ExternalStorageService() {
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
		super.onDestroy();
		unregisterReceivers();
	}
	/**
	 * ע��㲥
	 */
	private void registerReceivers() {
		if (externalStorageReceiver == null) {
			externalStorageReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					final String action = intent.getAction();
					final String path = intent.getData().getPath();
					Log.d(TAG, "receive action = " + action);
					boolean value = intent.getBooleanExtra("read-only", true);
					Log.d(TAG, "external storage path = " + path);
					Log.d(TAG, "external storage value = " + value);
					//	sendBroadcast(intent);
		    		if (Intent.ACTION_MEDIA_EJECT.equals(action)) 
		    		{
		    			// ���˸о� ACTION_MEDIA_EJECT ��
		    			//  ACTION_MEDIA_UNMOUNTED ��
		    			// sd ��������
		    			if (null != observers && 0 != observers.size()) {
							for (int i = 0; i < observers.size(); i++) {
								observers.get(i).onChangedNotify(0, action, path);
							}
						}
		    		} else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) 
		    		{
		    			// sd ���Ѿ����Ƴ�����
		    		} else if (Intent.ACTION_MEDIA_SHARED.equals(action)) 
		    		{
		    			// ѡ��ͨ�� usb ����
		    		} else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) 
		    			
		    			
		    		{
		    			// sd ������
		    			if (null != observers && 0 != observers.size()) {
							for (int i = 0; i < observers.size(); i++) {
								observers.get(i).onChangedNotify(0, action, path);
							}
						}
		    			
		    		}
		    		/*
		    		if (null != observers && 0 != observers.size()) {
						for (int i = 0; i < observers.size(); i++) {
							observers.get(i).onChangedNotify(0, action, path);
						}
					}
					*/
				}
			};

			//ACTION_MEDIA_REMOVED ��ʾ sdcard �Ѿ��ӿ����Ƴ���
			//ACTION_MEDIA_UNMOUNTED ֻ����˵�� sd ��û�� mount ���ļ�ϵͳ���棬������˵�����Ѿ��ӿ����Ƴ���
			
			//ACTION_MEDIA_BAD_REMOVAL ֻ����ֱ�Ӱγ� sd ��ʱ��ϵͳ�Żᷢ�������� action �㲥��
			//ACTION_MEDIA_REMOVED ���ܺ��ַ�ʽ�ӿ��۰γ� sd ��ʱ��ϵͳ�ͻᷢ�������� action �㲥��

           //	ACTION_MEDIA_SHARED ѡ��ͨ�� usb ���� 

			//���յ� ACTION_MEDIA_EJECT �㲥֮��sd �����ǿ��Զ�д�ģ�ֱ�����յ� ACTION_MEDIA_REMOVED��ACTION_MEDIA_UNMOUNTED�ȹ㲥֮��sd ���Ų����Զ�д��


			final IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
			filter.addAction(Intent.ACTION_MEDIA_BUTTON);
			filter.addAction(Intent.ACTION_MEDIA_CHECKING);
			filter.addAction(Intent.ACTION_MEDIA_EJECT);
			filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			filter.addAction(Intent.ACTION_MEDIA_NOFS);
			filter.addAction(Intent.ACTION_MEDIA_REMOVED);
			filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
			filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
			filter.addAction(Intent.ACTION_MEDIA_SHARED);
			filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
			filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			// �������ӣ������޷����յ��㲥
			filter.addDataScheme("file");
			registerReceiver(externalStorageReceiver, filter);
		}
	}
	/**
	 * ȡ��ע��
	 */
	private void unregisterReceivers() {
		if (externalStorageReceiver != null) {
			unregisterReceiver(externalStorageReceiver);
			externalStorageReceiver = null;
		}
	}
}