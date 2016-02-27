/*
   * �ļ��� ControlActivity.java
   * ���������б�com.szaoto.ak10.control
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:51
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;



import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.R;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.util.TraverseDictionary;
import com.szaoto.ak10.util.UpdateUsbStatesReceiver;

/*
 * ����ControlActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class SystemUpgrateFragment extends Fragment implements IInfoChangeObserver {
	public  String Usb_PATH; 
	SystemUpgrateFragment context;
	UpdateUsbStatesReceiver updateUsbstate;
	public MyHandler mhandler;
	Context mcontext;
	Button btnAPKupgrateButton;
	Button btnACQupgrateButton;
	Button btnSENDupgrateButton;
	Button btnSYSupgrateButton;
	View viewmain;
	CustomProgressDialog progUpgradeDiag;

	TextView txtVersion;
	
	int  bUSB = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mhandler = new MyHandler();
		updateUsbstate = new UpdateUsbStatesReceiver(this);
		
//		String sUsb=TraverseDictionary.GetUDiskDir();
//		if (sUsb.equals("")) {
//			bUSB = -1;
//		} else {
//			bUSB = 1;
//		}
		
		//���ֻ�ǻ�ȡ��ǰsd��״̬������Ҫ��������������÷���
		String sState = Environment.getExternalStorageState();//��õ�ǰsd��״̬
		if (sState.equals(Environment.MEDIA_MOUNTED)) {
			bUSB = 1;
		}
		else {
			bUSB = -1;
		}
	//	setContentView(R.layout.uicontrol);	
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 viewmain =inflater.inflate(R.layout.systemupgrate, null);
		btnAPKupgrateButton = (Button) viewmain.findViewById(R.id.apkupgratebtn);
		btnACQupgrateButton = (Button) viewmain.findViewById(R.id.ACQupgrade);
		btnSENDupgrateButton = (Button) viewmain.findViewById(R.id.SENDupgrade);
		btnSYSupgrateButton = (Button) viewmain.findViewById(R.id.SYSupgrade);
	
		txtVersion = (TextView) viewmain.findViewById(R.id.txtVersion);

		txtVersion.setText("App Version: V"+Ak10Application.GetVersionName());
		
		
	//	CardInformation 
		progUpgradeDiag = 
				new CustomProgressDialog(getActivity(), getString(R.string.text_system_update),getString(R.string.text_system_updating),false);
		btnAPKupgrateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (bUSB==-1) {
					Toast.makeText(getActivity(), getString(R.string.text_usbunmount), Toast.LENGTH_SHORT).show();
					return;
				}
				ApkUpdate();
			}
		});
		btnACQupgrateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				//AcquisitionUpdate();
				
				if (bUSB==-1) {
					Toast.makeText(getActivity(), getString(R.string.text_usbunmount), Toast.LENGTH_SHORT).show();
					return;
				}
				Intent putIntent = new Intent(getActivity(),SysUpdateSelActivity.class);
				putIntent.putExtra("cardtype", 1);
				startActivity(putIntent);			
			}
		});
		btnSENDupgrateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (bUSB==-1) {
					Toast.makeText(getActivity(), getString(R.string.text_usbunmount), Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent putIntent = new Intent(getActivity(),SysUpdateSelActivity.class);
				putIntent.putExtra("cardtype", 2);
				startActivity(putIntent);
				//SendCardUpdate();
			}
		});
		btnSYSupgrateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (bUSB==-1) {
					Toast.makeText(getActivity(), getString(R.string.text_usbunmount), Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent putIntent = new Intent(getActivity(),SysUpdateSelActivity.class);
				putIntent.putExtra("cardtype", 3);
				startActivity(putIntent);
				//ApkUpdate();
			}
		});
		return viewmain;
	}
	public static String ACQSUFFIX = ".acq";
	public static String SENDCARDSUFFIX = ".send";
	public static String SYSSUFFIX = ".send";


	boolean ApkUpdate()
	{
		Dialog alertDialog = new AlertDialog.Builder(getActivity()). 
                setTitle(R.string.text_program_update). 
                setMessage(R.string.text_acq_update_ensure). 
                setIcon(R.drawable.ic_launcher). 
                setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() { 
					@Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    	PackageManager pManager = getActivity().getPackageManager();
                		PackageInfo pi = null;
                		try {
                			pi = pManager.getPackageInfo("com.szaoto.ak10", 0);
                		} catch (NameNotFoundException e) {
                			e.printStackTrace();
                			return;
                		} 
                		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER); 
                		resolveIntent.setPackage(pi.packageName);  
                		List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);  
                		ResolveInfo ri = apps.iterator().next(); 
                		if (ri != null ) { 
               		        Usb_PATH = TraverseDictionary.GetUDiskDir();
	                		if (Usb_PATH == null) {
	                			return;
	                		}
	                		//�˴���ȡU������.apk��β���ļ�
	                		TraverseDictionary TD=new TraverseDictionary();
	                		TD.GetFilePaths(Usb_PATH, ".apk", true);
	                		String strFilepath = TD.getLstFilePath().get(0);
	                		File f=new File(strFilepath);//���԰汾��ͨ��U���е�apk��������
	                		Intent install = new Intent(Intent.ACTION_VIEW);       
	                		install.setDataAndType(Uri.fromFile(f),"application/vnd.android.package-archive");    
	                		startActivity(install);
                		}
                    } 
                }). 
                setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    	return;
                    } 
                }). 
                create(); 
				alertDialog.show(); 
		return true;	
	}

   @Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	updateUsbstate.registerReceiver();
}
   
   @Override
public void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	updateUsbstate.unregisterReceiver();
}
   public class MyHandler extends Handler{
		public MyHandler(){};
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("=============mainactivity handler");
//			Usb.setText("usb states");
			if(msg.arg1 == UpdateUsbStatesReceiver.USB_STATE_ON)	{//�� U�� 				
				bUSB = 1;
			 }
			else if(msg.arg1 == UpdateUsbStatesReceiver.USB_STATE_OFF){ //�� U�� 			
				bUSB = -1;		
			}		
		}
	}


	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		   Usb_PATH = xParam2;
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}