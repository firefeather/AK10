/*
 * 文件名 HomePageActivity.java
 * 包含类名列表com.szaoto.ak10
 * 版本信息，版本号
 * 创建日期2013年11月8日上午11:33:53
 * 版权声明 liangdb-szaoto
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
 * 类名SystemCardActivity
 * 作者   liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 * 修改者 zhangsj
 * 修改内容 将播放列表中的排序及拖拽添加
 * 修改日期 2014年5月14日
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
		SetLanguage();//语言
		setContentView(R.layout.homepage);
		SerialPortControlBroadCast.SetCurrentContext(this);
		mHomePageActivity = this;
		// /////////////////////////////////////////////////////////////////////
		// 导入配置
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

		// 打开串口设备
		SerialPortControlBroadCast.OpenSerialPort(this);		 
		
		InitialLEDNew();
	}
		
	
	/**
	 * 默认第一块屏幕的数据发送
	 */
	
	public static void ClearLedData(int LEDID)
	{
		ChannelDB.DeleteAllChanData(LEDID);
		InterfaceDB.DeleteAllData(LEDID);
		ChanGroupDb.DeleteAllRecords(LEDID);
	}
	
	
	public static void InitialLEDNew()
	{
		//先检查视频端口配置，现在时候存在，不存在就直接删除
		CardInformationList.GetCardInformationList();
		
		final CardInformation[] tCardInformations = CardInformationList.GetCardInformations();	
		
		final ArrayList<CardInformation> tCardInfosHardware = new ArrayList<CardInformation>();
		
		for (int i = 0; i < tCardInformations.length; i++) {
			
			if (tCardInformations[i].getnSlotID()!=-1) {
				tCardInfosHardware.add(tCardInformations[i]);
			}
		}
		
		//初始化加载LED1的配置
		final ArrayList<CardInformation> tCardInfosDb = CardInfoDB.GetAllCardInfoRecord();
	
		if (tCardInfosDb.size()<tCardInfosHardware.size()) {
			//有新插入的卡
			
			new AlertDialog.Builder(HomePageActivity.getInstance())
			/* 弹出窗口的最上头文字 */
			.setTitle(R.string.text_hardware_change)
			/* 设置弹出窗口的图式 */
			.setIcon(android.R.drawable.ic_dialog_info)
			/* 设置弹出窗口的信息 */
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
							/* 弹出窗口的最上头文字 */
							.setTitle(R.string.text_hardware_change)
							/* 设置弹出窗口的图式 */
							.setIcon(android.R.drawable.ic_dialog_info)
							/* 设置弹出窗口的信息 */
							.setMessage(R.string.text_hardware_insert_slot_channege)
							.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialoginterface, int i) {
									
									//删除所有相关配置的表中的数据
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
					
					//和上次的配置完全一致，卡没有改变
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
					/* 弹出窗口的最上头文字 */
					.setTitle(R.string.text_hardware_change)
					/* 设置弹出窗口的图式 */
					.setIcon(android.R.drawable.ic_dialog_info)
					/* 设置弹出窗口的信息 */
					.setMessage(R.string.text_hardware_insert_slot_channege)
					.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							
							//删除所有相关配置的表中的数据
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
			
			//和上次的配置完全一致，卡没有改变
			if (i!=tCardInfosDb.size()) {			
				return;
			}
			
		}
		

		for (int j = 1; j <= 4; j++) {
			//打开所有LED1~4的视频端口			
			OpenChPortsFromDbCfg(j);
			//配置所有LED1~4的发送卡端口
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
			
			//从第二个开始检查
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
			
			//从第二个开始检查
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
						
			        	 //有发送卡
			        	 //不能透传
			        	 continue;
					}
				}
			
			    //没有发送卡
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
	
	//设置视频源
	public static void SetChport( int Ledid) {
		//从数据库中获取
		String strGpNameString = ChanGroup_CurrentDb.GetCurrentName(Ledid);
		if (0 == strGpNameString.length()) {
			//插入一条空记录
			ChanGroup_CurrentDb.AddData("", Ledid);
			return;
		}
		ChanGroupData tGpData = ChanGroupDb.GetRecordByGpName(strGpNameString, Ledid);
		String strCfg = tGpData.strCfg;
		if (strCfg == null) {
			return;
		}

		String[] strCfgItemStrings = strCfg.split(",");

		// 关断所有视频源
		ArrayList<ChnData> tChPortInfoArray = ChannelDB.GetAllRecord(Ledid);
		for (int i = 0; i < tChPortInfoArray.size(); i++) {

			ChanComm.SetAcqCardPortNumAndEnable(false,
					tChPortInfoArray.get(i).videosourceid,
					tChPortInfoArray.get(i).Id % 1000, Ledid);
		}

		// 关断所有的发送卡接口

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
			// 打开视频端口
			ChanComm.SetAcqCardPortNumAndEnable(true,
					tChanData.videosourceid, tChanData.Id % 1000,
					Ledid);

			// 配置以上视频剪切区域
			InterfaceDB.GetChportIdById(Intfid, Ledid);
			IntfData tInterfData = InterfaceDB.GetRecordById(Intfid, Ledid);
			InterfaceComm.SetSendCardChPortAndEnable(true,
					tChanData.videosourceid, tInterfData.macaddress,
					Intfid % 1000);

			//获取3D配置信息
			Sys_Para sys_Para = Sys_ParaDB.GetSys_Para();

			// 要计算相对的，先得到原来的采集卡的区域
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
			 //设置视频源接口
			 
			 //端口使能
			 // InterfaceComm.SetSendCardPortEnable(true, tintfData.macaddress); 
			 // InterfaceComm.SetSendCardVideoCaptureEnable(true, tintfData.macaddress, tintfData.Id%1000);
			 
			 InterfaceComm.SetSendCardChPortAndEnable(true,ChPortNum,tintfData.macaddress,tintfData.Id%1000);
		 
			 //设置剪裁区域
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



	// 用root权限运行命令
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
	// 将assets文件夹下面的文件全部copy到DATA_PATH路径下面，可以使一些用到的工具文件
	


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
		// 关闭SPI设备

		SpiControl.CloseSpiDevice();
		// 关闭串口设备
		//	SerialPortControlBroadCast.ColseSerialPort();
		stopService(new Intent(HomePageActivity.this,
			PannelButtonDownService.class));
		SerialPortControlBroadCast.ColseSerialPort();
		super.onDestroy();
	}

	// 添加播放列表模块拖拽排序到此处
	/**********************************************************/
	/**
	 * 公用的 排列顺序后的列表集合
	 */
//	public static ArrayList<VideoFile> VideoFileList_Public = new ArrayList<VideoFile>();
//
//	/**
//	 * 公用的 已选中的 列表集合 --（添加视频文件）
//	 */
//	public static ArrayList<VideoFile> VideoFileList_Added = new ArrayList<VideoFile>();
//
//	/**
//	 * 公用的 已加载到 内存的 图片（方便读取，不用再次通过文件路径读取图片）
//	 */
//	public static HashMap<String, Bitmap> hashMapImage_Added = new HashMap<String, Bitmap>();
//
//	/**
//	 * 公用 默认 加载 的图片
//	 */
//	public static Bitmap bitmap;
//
//	/**
//	 * 公用 默认图片 的 宽度，高度
//	 */
//	public static int imageDefaultWidth, imageDefaultHeight;
//	/**
//	 * 公用 默认图片 的 宽度，高度
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
		
		preferences = getSharedPreferences("11", MODE_PRIVATE); // zhangsj修改
		
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
