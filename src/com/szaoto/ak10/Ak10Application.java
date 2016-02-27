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
//ȫ�ֳ�����

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
	
	//Application����Ҫ�������̣߳�һ����socket�ȴ��س� һ����spi���������ݵȴ��߳�
	
	private static int nVersionCode;//���豸����ʶ��汾(����)�õģ�������һ��intergerֵ������������app���¹����ٴ�
	private static String strVersionName;//���û�����app�汾
	public Ak10Application() {

	}
	
	//��ȡ�汾
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
	
	//����������ʱ��
	@Override
	public void onCreate() {
		//�������ļ�copy��ȥ
		OnInitAssetFiles();		
		SqliteDB.OpenDB(); 
		SpiControl.OpenSpiDevice();
		InitNetWork();
		LoadCbtLib();
		GetVersion();
		//��socketͨ�ŷ������,һֱ��ͣ
		//��һ��Socket�ȴ��߳�
		sockCommService_LedConstructor = new SocketCommService(GetPort(EPort.ePort_LEDConstructor));
		sockCommService_LedConstructor.StartSocketListeningThread();

		sockCommService_Jkylin = new SocketCommService(GetPort(EPort.ePort_JKylin));
		sockCommService_Jkylin.StartSocketListeningThread();
		sockCommService_Jkylin.StartTimingPowerThread();//��ʱ�������߳�

		
        //sockCommService.StartSpiListeningThread();
		//Ӱ����������ٶ�
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
			Log.v("�ļ������ǣ�", "+++" + files.length);
		} catch (IOException e) {
		}
		if (files == null) {
			return;
		}
		// ��֪��ʲôԭ��assets��ֻ�����������ļ������ǳ�����5�����Լ���������ֻҪǰ����ļ�
		// int length = files.length - 3;
		int length = files.length;
		// �����ļ�����������copy
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
	
	// ��ؐ�ļ��õ��ķ���,��CopyAssets()������ʹ�õ���,ֻҪ������������������������ok��
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			Log.v(getString(R.string.log_homepage_readcontext), "" + read);
			out.write(buffer, 0, read);
		}
	}
	
	//����رյ�ʱ��
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
