package com.szaoto.ak10;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.dataaccess.returnClass;
import com.szaoto.ak10.leddisplay.CabinetAddActivity;
import com.szaoto.ak10.sqlitedata.SqliteDB;
import com.szaoto.ak10.wirednetwork.WirednetConfig;
import com.szaoto.ak10.wirednetwork.WirednetConfigDb;

enum EPort{
	ePort_LEDConstructor,
	ePort_JKylin;
}
//全局程序单例

public class Ak10Application extends Application {	
	SocketCommService sockCommService_LedConstructor = null;
	SocketCommService sockCommService_Jkylin = null;
	public static String g_strConfPathString;
	//public static String g_Version="1.0.0.5_EDID_test_012216";
	public static String g_Version="1.0.0.5_test_012711";
	private static int gLedid = 1;
	static public ArrayList<String> gArrCabSerieStrings = new ArrayList<String>(); 
	static public HashMap<String, ArrayList<String>> gMapModels;
	//static private EPort m_ePort = EPort.ePort_LEDConstructor;
	
	//Application中需要开两个线程，一个是socket等待县城 一个是spi总线上数据等待线程
	
	private static int nVersionCode;//给设备程序识别版本(升级)用的，必须是一个interger值，整数，代表app更新过多少次
	private static String strVersionName;//给用户看的app版本
	public Ak10Application() {

	}
	
	//获取版本
	private void GetVersion(){
		try {
			String pName = getPackageName();
			PackageManager pm = getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(pName, PackageManager.GET_CONFIGURATIONS);
			nVersionCode = pinfo.versionCode;
			strVersionName = pinfo.versionName;			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static String GetVersionName(){
		return strVersionName;
	}
	public static void SetLedId(int nLedId){
		gLedid = nLedId;
	}
	public static int GetLedId(){
		return gLedid;
	}
	public static int GetPort(EPort ePort) {
		int nPort = 8889;
		if (ePort == EPort.ePort_LEDConstructor) {
			nPort = 8889;
		} else if (ePort == EPort.ePort_JKylin) {
			nPort = 8890;
		} else {
			nPort = 8889;
		}
		return nPort;
	}
	
	//程序启动的时候
	@Override
	public void onCreate() {
		//将配置文件copy进去
		OnInitAssetFiles();		
		SqliteDB.OpenDB(); 
		SpiControl.OpenSpiDevice();
		InitNetWork();
		LoadCbtLib();
		GetVersion();
		//打开socket通信服务程序,一直不停
		//打开一个Socket等待线程
		sockCommService_LedConstructor = new SocketCommService(GetPort(EPort.ePort_LEDConstructor));
		sockCommService_LedConstructor.StartSocketListeningThread();

		sockCommService_Jkylin = new SocketCommService(GetPort(EPort.ePort_JKylin));
		sockCommService_Jkylin.StartSocketListeningThread();
		sockCommService_Jkylin.StartTimingPowerThread();//定时开关屏线程

		
        //sockCommService.StartSpiListeningThread();
		//影响软件运行速度
		super.onCreate();
	}

	private void LoadCbtLib()
	{
		CabinetAddActivity.loadModelData();
	}
	
	private void InitNetWork()
	{
		WirednetConfig tWirenetConfig = WirednetConfigDb.GetWirednetConfig();
		try 
		{
			WirednetConfigDb.SaveWirednetConfig(tWirenetConfig);
			WirednetConfigDb.OpenWirednetHotplugDetect();		
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void OnInitAssetFiles()
	{
		g_strConfPathString = this.getFilesDir() + "//config//";
		File file = new File(g_strConfPathString);
		if (file.exists())
		{
			CopyAssets();
		} else {
			file.mkdir();
			CopyAssets();
		}
	}
	
	private void CopyAssets() {
		AssetManager assetManager = this.getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
			Log.v("文件长度是：", "+++" + files.length);
		} catch (IOException e) {
		}
		if (files == null) {
			return;
		}
		// 不知道什么原因，assets中只放入了两个文件，但是长度是5，所以剪掉三个，只要前面的文件
		// int length = files.length - 3;
		int length = files.length;
		// 按照文件个数来挨个copy
		for (int i = 0; i < length; i++) {
			InputStream in = null;
			OutputStream out = null;
			File f = new File(g_strConfPathString + files[i]);

			try {
				if (!(new File(g_strConfPathString + files[i])).exists()) {
					in = assetManager.open(files[i]);
					Log.v(getString(R.string.log_homepage_mark),
							getString(R.string.log_homepage_mark) + "1");
					Log.v("1", files[i]);
					out = new FileOutputStream(f);
					Log.v(getString(R.string.log_homepage_mark),
							getString(R.string.log_homepage_mark) + "2");
					copyFile(in, out);
					Log.v(getString(R.string.log_homepage_mark),
							getString(R.string.log_homepage_mark) + "3");
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				}
			} catch (Exception e) {
				Log.v(getString(R.string.log_homepage_unusual),
						getString(R.string.log_homepage_unusual));
			}
		}
	}
	
	// 拷貝文件用到的方法,在CopyAssets()方法中使用到了,只要传入参数：输入流和输出流就ok。
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			Log.v(getString(R.string.log_homepage_readcontext), "" + read);
			out.write(buffer, 0, read);
		}
	}
	
	//程序关闭的时候
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (sockCommService_LedConstructor!=null) {
			sockCommService_LedConstructor.StopSocketListeningThread();
		}

		if (sockCommService_Jkylin!=null) {
			sockCommService_Jkylin.StopSocketListeningThread();
			sockCommService_Jkylin.StopTimingPowerThread();
		}

		SqliteDB.CloseDB();
		super.onTerminate();
	}
}
