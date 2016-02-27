/*
 * �ļ��� HomePageActivity.java
 * ���������б�com.szaoto.ak10
 * �汾��Ϣ���汾��
 * ��������2013��11��8������11:33:53
 * ��Ȩ���� liangdb-szaoto
 */
package com.szaoto.ak10;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.configuration.CardInformationList;
import com.szaoto.ak10.control.ControlActivity;
import com.szaoto.ak10.custom.Win8ImageView;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.datacomm.InterfaceComm;
import com.szaoto.ak10.leddisplay.LedSelActivity;
import com.szaoto.ak10.monitor.MonitorActivity;
import com.szaoto.ak10.player.PlayerActivity;
import com.szaoto.ak10.sqlitedata.CardInfoDB;
import com.szaoto.ak10.sqlitedata.ChanGroupData;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.sqlitedata.ChanGroup_CurrentDb;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.sqlitedata.Sys_Para;
import com.szaoto.ak10.sqlitedata.Sys_ParaDB;
import com.szaoto.ak10.systemconfig.SysConfigActivity;
import com.szaoto.ak10.test.TestActivity;


/*
 * ����SystemCardActivity
 * ����   liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 * �޸��� zhangsj
 * �޸����� �������б��е�������ק����
 * �޸����� 2014��5��14��
 */
public class HomePageActivity extends Activity{

	private static HomePageActivity mHomePageActivity = null;

	Win8ImageView btn_SetupWin8ImageView;
	Win8ImageView btn_LedDisplayWin8ImageView;
	Win8ImageView btn_SystemWin8ImageView;
	Win8ImageView btn_PlayerWin8ImageView;
	Win8ImageView btn_TestWin8ImageView;
	Win8ImageView btn_ControlwiWin8ImageView;
	Win8ImageView btn_MonitoWin8ImageView;


//	public static Intent intentAcquisition2SendCardActivity;
	public static Intent intentLedDisplayActivity;
	public static Intent intentSystemCardActivity;
	public static Intent intentSystemConfigActivity;
	public static Intent intentPlayerActivity;
	public static Intent intentTestActivity;
	public static Intent intentControlActivity;
	public static Intent intentMonitorActivity;
	public static Intent acq2;
	public static String CONFIG_PATH;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		SetLanguage();//����
		setContentView(R.layout.homepage);
		SerialPortControlBroadCast.SetCurrentContext(this);
		mHomePageActivity = this;
		// /////////////////////////////////////////////////////////////////////
		// ��������
		CONFIG_PATH = Ak10Application.g_strConfPathString;

		intentLedDisplayActivity = new Intent(HomePageActivity.this,LedSelActivity.class);
		intentLedDisplayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			
		intentSystemConfigActivity = new Intent(HomePageActivity.this,SysConfigActivity.class);
		intentSystemConfigActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intentPlayerActivity = new Intent(HomePageActivity.this,PlayerActivity.class);
		intentPlayerActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intentTestActivity = new Intent(HomePageActivity.this,TestActivity.class);
		intentTestActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intentControlActivity = new Intent(HomePageActivity.this,ControlActivity.class);
		intentControlActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intentMonitorActivity = new Intent(HomePageActivity.this,MonitorActivity.class);
		intentMonitorActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		btn_LedDisplayWin8ImageView = (Win8ImageView) findViewById(R.id.homeleddisplay);
		btn_LedDisplayWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
					@Override
					public void onClick() {
						startActivity(intentLedDisplayActivity);
					}
				});
		
		btn_SystemWin8ImageView = (Win8ImageView) findViewById(R.id.homesystem);
		btn_SystemWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
					@Override
					public void onClick() {
						startActivity(intentSystemConfigActivity);
					}
				});
		
		btn_PlayerWin8ImageView = (Win8ImageView) findViewById(R.id.homeplayer);
		btn_PlayerWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
					@Override
					public void onClick() {
						startActivity(intentPlayerActivity);
					}
				});
		
		btn_TestWin8ImageView = (Win8ImageView) findViewById(R.id.hometest);
		btn_TestWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
			@Override
			public void onClick() {
				startActivity(intentTestActivity);
			}
		});
		
		btn_ControlwiWin8ImageView = (Win8ImageView) findViewById(R.id.homecontrol);
		btn_ControlwiWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
					@Override
					public void onClick() {
						startActivity(intentControlActivity);
					}
				});
		
		btn_MonitoWin8ImageView = (Win8ImageView) findViewById(R.id.homemonitor);
		btn_MonitoWin8ImageView.setOnClickIntent(new Win8ImageView.OnViewClick() {
					@Override
					public void onClick() {
						Toast.makeText(getApplicationContext(), R.string.text_not_support, Toast.LENGTH_SHORT).show();
						//startActivity(intentMonitorActivity);						
					}
				});				

		startService(new Intent(HomePageActivity.this,ExternalStorageService.class));
		startService(new Intent(HomePageActivity.this,PannelButtonDownService.class));

		// �򿪴����豸
		SerialPortControlBroadCast.OpenSerialPort(this);		 
		
		InitialLEDNew();
	}
		
	
	/**
	 * Ĭ�ϵ�һ����Ļ�����ݷ���
	 */
	
	public static void ClearLedData(int LEDID)
	{
		ChannelDB.DeleteAllChanData(LEDID);
		InterfaceDB.DeleteAllData(LEDID);
		ChanGroupDb.DeleteAllRecords(LEDID);
	}
	
	
	public static void InitialLEDNew()
	{
		//�ȼ����Ƶ�˿����ã�����ʱ����ڣ������ھ�ֱ��ɾ��
		CardInformationList.GetCardInformationList();
		
		final CardInformation[] tCardInformations = CardInformationList.GetCardInformations();	
		
		final ArrayList<CardInformation> tCardInfosHardware = new ArrayList<CardInformation>();
		
		for (int i = 0; i < tCardInformations.length; i++) {
			
			if (tCardInformations[i].getnSlotID()!=-1) {
				tCardInfosHardware.add(tCardInformations[i]);
			}
		}
		
		//��ʼ������LED1������
		final ArrayList<CardInformation> tCardInfosDb = CardInfoDB.GetAllCardInfoRecord();
	
		if (tCardInfosDb.size()<tCardInfosHardware.size()) {
			//���²���Ŀ�
			
			new AlertDialog.Builder(HomePageActivity.getInstance())
			/* �������ڵ�����ͷ���� */
			.setTitle(R.string.text_hardware_change)
			/* ���õ������ڵ�ͼʽ */
			.setIcon(android.R.drawable.ic_dialog_info)
			/* ���õ������ڵ���Ϣ */
			.setMessage(R.string.text_hardware_insert)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {
					
					int j = 0;
					for (; j < tCardInfosDb.size(); j++) {
						
						byte[] MacAddressOld =  tCardInfosDb.get(j).getUcMACAddress();
						byte[] MacAddressNew =  tCardInfosHardware.get(j).getUcMACAddress(); 
						
						int k = 0;
						for (; k < 6; k++) {
							if (MacAddressNew[k]!=MacAddressOld[k]) {
								break;
							}
						}
						
						if (k!=6) {
							new AlertDialog.Builder(HomePageActivity.getInstance())
							/* �������ڵ�����ͷ���� */
							.setTitle(R.string.text_hardware_change)
							/* ���õ������ڵ�ͼʽ */
							.setIcon(android.R.drawable.ic_dialog_info)
							/* ���õ������ڵ���Ϣ */
							.setMessage(R.string.text_hardware_insert_slot_channege)
							.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialoginterface, int i) {
									
									//ɾ������������õı��е�����
									for (int j = 1; j <=4; j++) {
										ClearLedData(j);
									}

									CardInfoDB.DeleteAllData();	
									
									for (int j = 0; j <tCardInformations.length; j++) {	
										
										if (tCardInformations[j].getnSlotID()==-1) {
											continue;
										}
										
										CardInfoDB.AddData(tCardInformations[j]);
									}
						
								}
							})
							.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { 
								public void onClick(DialogInterface dialoginterface, int i) {
									
									
									return ;
									
								}
							}).show();
							
							break;
						}
						
					}
					
					//���ϴε�������ȫһ�£���û�иı�
					if (j!=tCardInfosDb.size()) {			
						return;
					}
					
				}
			})
			.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialoginterface, int i) {
					
				}
			}).show();
			
		}
		
	
		if (tCardInfosDb.size()>=tCardInfosHardware.size()) {
			
			int i = 0;
			for (; i < tCardInfosHardware.size(); i++) {
				
				byte[] MacAddressOld =  tCardInfosDb.get(i).getUcMACAddress();
				byte[] MacAddressNew =  tCardInfosHardware.get(i).getUcMACAddress(); 
				
				int j = 0;
				for (; j < 6; j++) {
					if (MacAddressNew[j]!=MacAddressOld[j]) {
						break;
					}
				}
				
				if (j!=6) {
					new AlertDialog.Builder(HomePageActivity.getInstance())
					/* �������ڵ�����ͷ���� */
					.setTitle(R.string.text_hardware_change)
					/* ���õ������ڵ�ͼʽ */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* ���õ������ڵ���Ϣ */
					.setMessage(R.string.text_hardware_insert_slot_channege)
					.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							
							//ɾ������������õı��е�����
							for (int j = 1; j <=4; j++) {
								ClearLedData(j);
							}
							CardInfoDB.DeleteAllData();	
							
							for (int j = 0; j <tCardInformations.length; j++) {	
								
								if (tCardInformations[j].getnSlotID()==-1) {
									continue;
								}
								
								CardInfoDB.AddData(tCardInformations[j]);
							}
				
						}
					})
					.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialoginterface, int i) {
							
							
							return ;
							
						}
					}).show();
					
					break;
				}
				
			}
			
			//���ϴε�������ȫһ�£���û�иı�
			if (i!=tCardInfosDb.size()) {			
				return;
			}
			
		}
		

		for (int j = 1; j <= 4; j++) {
			//������LED1~4����Ƶ�˿�			
			OpenChPortsFromDbCfg(j);
			//��������LED1~4�ķ��Ϳ��˿�
			ConfigAddSendCardParams(j);
			SetChport(j);
		}

		
		ArrayList<CardInformation> tArrayListAcq =FindAcqCards();
		for (int k = 0; k < tArrayListAcq.size(); k++) {
			
			 ChanComm.SetAcqCardTranparentTransferEnable(false,tArrayListAcq.get(k).getUcMACAddress());
		}
		
	
		ArrayList<CardInformation> tArrayListAcqTransp = FindAcqCardsNeedTransparentTransfer();
		
		for (int k = 0; k < tArrayListAcqTransp.size(); k++) {
	
			 ChanComm.SetAcqCardTranparentTransferEnable(true,tArrayListAcqTransp.get(k).getUcMACAddress());
		}
		
	}

	/**
	 * 
	 * @return
	 */
	
	public static ArrayList<CardInformation> FindAcqCards()
	{

        ArrayList<CardInformation> arrAcqArrayList = new ArrayList<CardInformation>();
	
		CardInformation[] tCardInformations = CardInformationList.GetCardInformations();
		
		for (int i = 0; i < tCardInformations.length; i++) {
			
			//�ӵڶ�����ʼ���
		   int nCurType  =	tCardInformations[i].getnType();
  
		   if (nCurType==2) {
			   arrAcqArrayList.add(tCardInformations[i]);
		   }
		  
		}
		

		return arrAcqArrayList;
		
	}
	
	public static ArrayList<CardInformation> FindAcqCardsNeedTransparentTransfer()
	{

        ArrayList<CardInformation> arrAcqArrayList = new ArrayList<CardInformation>();
        ArrayList<CardInformation> arrInterfArrayList = new ArrayList<CardInformation>(); 
        ArrayList<CardInformation> arrRetAcqArrayList = new ArrayList<CardInformation>();
		
		CardInformation[] tCardInformations = CardInformationList.GetCardInformations();
		
		for (int i = 0; i < tCardInformations.length; i++) {
			
			//�ӵڶ�����ʼ���
		   int nCurType  =	tCardInformations[i].getnType();
  
		   if (nCurType==2) {
			   arrAcqArrayList.add(tCardInformations[i]);
		   }
		   if (nCurType==3) {
			   arrInterfArrayList.add(tCardInformations[i]);
		   }
		}
		
		if (arrAcqArrayList.size()<=1) {		
			return arrAcqArrayList;
		}
		
		else {
		
			for (int i = 1; i < arrAcqArrayList.size(); i++) {
				
				CardInformation tAcqCardInfomationCard = arrAcqArrayList.get(i);
				CardInformation tPrevAcqCardInfomationCard = arrAcqArrayList.get(i-1);
				
			    int SlotId =	tAcqCardInfomationCard.getnSlotID();
			    int PreSlotId = tPrevAcqCardInfomationCard.getnSlotID();
			    
			    
			    int j = 0;
			    for (; j < arrInterfArrayList.size(); j++) {
					
			         int tTmpSlotId = arrInterfArrayList.get(j).getnSlotID();
			         
			         if (tTmpSlotId>PreSlotId&&tTmpSlotId<SlotId) {
						
			        	 //�з��Ϳ�
			        	 //����͸��
			        	 continue;
					}
				}
			
			    //û�з��Ϳ�
			    if (j == arrInterfArrayList.size()) {
			    	arrRetAcqArrayList.add(arrAcqArrayList.get(i));
				}
			    
			}
			
		}

		if (arrAcqArrayList.size()!=0) {
			
			arrRetAcqArrayList.add(arrAcqArrayList.get(0));
		}
		
		return arrRetAcqArrayList;
		
	}
	
	public static void OpenChPortsFromDbCfg(int LEDID)
	{	
		 ArrayList<ChnData> tArrayList =  ChannelDB.GetAllRecord(LEDID);	
		 for (int i = 0; i < tArrayList.size(); i++) {
			 ChnData tChnDataInfo = tArrayList.get(i);
			 ChanComm.SetAcqCardPortNumAndEnable(true, tChnDataInfo.videosourceid, tChnDataInfo.Id%1000,LEDID);			 
		}
	}
	
	//������ƵԴ
	public static void SetChport( int Ledid) {
		//�����ݿ��л�ȡ
		String strGpNameString = ChanGroup_CurrentDb.GetCurrentName(Ledid);
		if (0 == strGpNameString.length()) {
			//����һ���ռ�¼
			ChanGroup_CurrentDb.AddData("", Ledid);
			return;
		}
		ChanGroupData tGpData = ChanGroupDb.GetRecordByGpName(strGpNameString, Ledid);
		String strCfg = tGpData.strCfg;
		if (strCfg == null) {
			return;
		}

		String[] strCfgItemStrings = strCfg.split(",");

		// �ض�������ƵԴ
		ArrayList<ChnData> tChPortInfoArray = ChannelDB.GetAllRecord(Ledid);
		for (int i = 0; i < tChPortInfoArray.size(); i++) {

			ChanComm.SetAcqCardPortNumAndEnable(false,
					tChPortInfoArray.get(i).videosourceid,
					tChPortInfoArray.get(i).Id % 1000, Ledid);
		}

		// �ض����еķ��Ϳ��ӿ�

		ArrayList<IntfData> tIntfDataArrayList = InterfaceDB
				.GetAllRecord(Ledid);
		for (int i = 0; i < tIntfDataArrayList.size(); i++) {

			IntfData tInterfData = tIntfDataArrayList.get(i);

			int tVideoId = InterfaceDB.GetChportIdById(tInterfData.Id,
					Ledid);

			InterfaceComm.SetSendCardChPortAndEnable(false, tVideoId,
					tInterfData.macaddress, tInterfData.Id % 1000);

		}

		for (int i = 0; i < strCfgItemStrings.length; i++) {

			String[] strCfgPairStrings = strCfgItemStrings[i]
					.split("-");

			String strChanId = strCfgPairStrings[1];
			String strIntfId = strCfgPairStrings[0];

			int Chid = Integer.valueOf(strChanId);

			int Intfid = Integer.valueOf(strIntfId);

			ChnData tChanData = ChannelDB.GetRecordById(Chid, Ledid);
			// ����Ƶ�˿�
			ChanComm.SetAcqCardPortNumAndEnable(true,
					tChanData.videosourceid, tChanData.Id % 1000,
					Ledid);

			// ����������Ƶ��������
			InterfaceDB.GetChportIdById(Intfid, Ledid);
			IntfData tInterfData = InterfaceDB.GetRecordById(Intfid, Ledid);
			InterfaceComm.SetSendCardChPortAndEnable(true,
					tChanData.videosourceid, tInterfData.macaddress,
					Intfid % 1000);

			//��ȡ3D������Ϣ
			Sys_Para sys_Para = Sys_ParaDB.GetSys_Para();

			// Ҫ������Եģ��ȵõ�ԭ���Ĳɼ���������
			int tChId = tInterfData.channelid;
			ChnData tChnData = ChannelDB.GetRecordById(tChId, Ledid);
			InterfaceComm.SetSendCardPortParam(
					(short) (tInterfData.offsetX - tChnData.offsetX),
					(short) (tInterfData.offsetY - tChnData.offsetY),
					(short) tInterfData.width,
					(short) tInterfData.height, 
					sys_Para.cfg3d,
					tInterfData.macaddress,
					Intfid % 1000);

		}		
	}
	
	public static void ConfigAddSendCardParams(int LEDID)
	{
		 ArrayList<IntfData> tArrayList =  InterfaceDB.GetAllRecord(LEDID);	 
		 for (int i = 0; i < tArrayList.size(); i++) {	
			 IntfData tintfData = tArrayList.get(i);	
			 int ChPortNum = InterfaceDB.GetChportIdById(tintfData.Id, LEDID);	
			 //������ƵԴ�ӿ�
			 
			 //�˿�ʹ��
			 // InterfaceComm.SetSendCardPortEnable(true, tintfData.macaddress); 
			 // InterfaceComm.SetSendCardVideoCaptureEnable(true, tintfData.macaddress, tintfData.Id%1000);
			 
			 InterfaceComm.SetSendCardChPortAndEnable(true,ChPortNum,tintfData.macaddress,tintfData.Id%1000);
		 
			 //���ü�������
			 ChnData tChnData = ChannelDB.GetRecordById(tintfData.channelid,LEDID);
			 short xRelative = (short) (tintfData.offsetX - tChnData.offsetX);
			 short yRelative = (short) (tintfData.offsetY - tChnData.offsetY);
			 short width = (short) tintfData.width;
			 short height = (short) tintfData.height;			
			 Sys_Para sys_Para = Sys_ParaDB.GetSys_Para();
			 int ncfg3d = sys_Para.cfg3d;
			 
			 InterfaceComm.SetSendCardPortParam(xRelative, yRelative, width, height, ncfg3d, tintfData.macaddress,tintfData.Id%1000);			 
		}	
	}
	
	
	public static HomePageActivity getInstance() {
		if (mHomePageActivity != null) {
			return mHomePageActivity;
		}
		return null;
	}

	public void startControlActivity() {
		intentControlActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intentControlActivity);
	}

	public void startTestActivity() {
		intentTestActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intentTestActivity);
	}

	public void startPlayerActivity() {
		intentPlayerActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intentPlayerActivity);
	}



	// ��rootȨ����������
	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "n");
			os.writeBytes("exitn");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}
		return true;
	}
	// ��assets�ļ���������ļ�ȫ��copy��DATA_PATH·�����棬����ʹһЩ�õ��Ĺ����ļ�
	


	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		stopService(new Intent(HomePageActivity.this,
				ExternalStorageService.class));
		// �ر�SPI�豸

		SpiControl.CloseSpiDevice();
		// �رմ����豸
		//	SerialPortControlBroadCast.ColseSerialPort();
		stopService(new Intent(HomePageActivity.this,
			PannelButtonDownService.class));
		SerialPortControlBroadCast.ColseSerialPort();
		super.onDestroy();
	}

	// ���Ӳ����б�ģ����ק���򵽴˴�
	/**********************************************************/
	/**
	 * ���õ� ����˳�����б�����
	 */
//	public static ArrayList<VideoFile> VideoFileList_Public = new ArrayList<VideoFile>();
//
//	/**
//	 * ���õ� ��ѡ�е� �б����� --��������Ƶ�ļ���
//	 */
//	public static ArrayList<VideoFile> VideoFileList_Added = new ArrayList<VideoFile>();
//
//	/**
//	 * ���õ� �Ѽ��ص� �ڴ�� ͼƬ�������ȡ�������ٴ�ͨ���ļ�·����ȡͼƬ��
//	 */
//	public static HashMap<String, Bitmap> hashMapImage_Added = new HashMap<String, Bitmap>();
//
//	/**
//	 * ���� Ĭ�� ���� ��ͼƬ
//	 */
//	public static Bitmap bitmap;
//
//	/**
//	 * ���� Ĭ��ͼƬ �� ���ȣ��߶�
//	 */
//	public static int imageDefaultWidth, imageDefaultHeight;
//	/**
//	 * ���� Ĭ��ͼƬ �� ���ȣ��߶�
//	 */
//	public static boolean systemconfigtype;
	@Override
	protected void onRestart() {
		SerialPortControlBroadCast.SetCurrentContext(this);
		super.onRestart();
	}
	@Override
	protected void onStop() {
//		SerialPortControlBroadCast.mCurrentContext = null; 
		super.onStop();
	}
	@Override
	protected void onResume() {
//		bitmap = BitmapFactory.decodeResource(getResources(), drawable.video);
//
//		imageDefaultHeight = bitmap.getHeight();
//
//		imageDefaultWidth = bitmap.getWidth();

		super.onResume();
	}
	/**********************************************************/
	
	

	public void SetLanguage() {
		
		SharedPreferences preferences;
		
		preferences = getSharedPreferences("11", MODE_PRIVATE); // zhangsj�޸�
		
		int langtype = -1;
		if (preferences.getString("LANGUAGETYPE", "Default").equals("Default")) {

		} 
		else
		{
			langtype = Integer.valueOf(preferences.getString("LANGUAGETYPE","Default"));
		}
		
		Resources resources = getResources();
		
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
		
		if (langtype == 1) 
		{
			config.locale = Locale.SIMPLIFIED_CHINESE;
		} 
		else if (langtype == 0) 
		{
			config.locale = Locale.ENGLISH;
		}
	
		resources.updateConfiguration(config, dm);
	}
}