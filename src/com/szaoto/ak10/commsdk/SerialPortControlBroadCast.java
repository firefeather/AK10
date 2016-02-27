/*
   * 文件名 SerialPortControlBroadCast.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2014年1月15日下午5:30:46
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.friendlyarm.AndroidSDK.HardwareControler;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.control.ControlActivity;
import com.szaoto.ak10.leddisplay.CabinetAddActivity;
import com.szaoto.ak10.leddisplay.CabinetLibraryActivity;
import com.szaoto.ak10.leddisplay.LedSelActivity;
import com.szaoto.ak10.systemconfig.SystemDiagnoseFragment;
import com.szaoto.ak10.test.TestActivity;

/*
 * 类名SerialPortControl
 * 作者 liangdb
 * 主要功能 串口控制类
 * 创建日期2014年1月15日
 * 修改者，修改日期，修改内容
 */
public class SerialPortControlBroadCast {
	private static Context mCurrentContext = null;
	public static int m_fd = -1;
	private final static int CMD_LENGHT = 1; // 命令长度（固定1个byte）
	private final static int MESSAGE_RECEIVED_CMD = 1; 
	public static boolean systemconfigtype = false;
	private static String DYNAMICACTION = "PannelKeyDown";
	
	public static long  preTimeStamp =-1;
	//无极旋钮1个
	//功能按键13个：功能按键包括5个模板按键、6个快WriteSerialPort速按键、1个确认按键、1个取消按键。
		//快速按键：包括亮度、色温、饱和度、对比度、切换、测试。
	public final static byte CMD_TEMPLATE_NO1 	= 0x50;
	public final static byte CMD_TEMPLATE_NO2 	= 0x51;
	public final static byte CMD_TEMPLATE_NO3 	= 0x52;
	public final static byte CMD_TEMPLATE_NO4 	= 0x53;                                                                
	public final static byte CMD_TEMPLATE_NO5 	= 0x54;
	public final static byte CMD_BRIGHTNESS 	= 0x55;
	public final static byte CMD_COLORTEMP 		= 0x56;
	public final static byte CMD_SATURATION 	= 0x58;
	public final static byte CMD_CONTRAST 		= 0x57;
	public final static byte CMD_SWITCH 		= 0x5A;
	public final static byte CMD_TEST 			= 0x59;
	public final static byte CMD_OK 			= 0x5C;
	public final static byte CMD_CANCEL 		= 0x5B;

	public final static byte CMD_CLOCKWISE 		= 0x60;//顺时针
	public final static byte CMD_ANTICLOCKWISE 	= 0x61;//逆时针
	public final static byte CMD_CLOCK_VALUE	= 0x70;
	
	private static byte CMD_CURRENT = 0x00;
	private static int value = 0;
	public static boolean[] isLight = new boolean[13];
	static Context mContext;
	
	public static void SetCurrentContext(Context currentContext){
		mCurrentContext = currentContext;
	}
//	public static boolean[] isControlmoduleLight = new boolean[5];
//	public static boolean[] isControl = new boolean[8];
	
	/*ARM -> FPGA串口命令：
	0x10：关闭按键按下自动灯。（默认打开）
	0x11：打开按键按下自动灯。
	0x20~2c：常亮对应按键灯，顺序同命令0x5x。
	0x2f：常亮所有按键灯。
	0x30~3c：关闭常亮对应按键灯，顺序同命令0x5x。
	0x3f：关闭所有常亮按键灯。*/

	/**
	 * 
	 */
	public SerialPortControlBroadCast() {
//		SerialPortControlBroadCast.CMD_CURRENT = 0x00;
//		SerialPortControlBroadCast.value = 0;
	}
	public static int OpenSerialPort(Context context)
	{
		mContext = context;
		if(-1 == m_fd){ 
			m_fd = HardwareControler.openSerialPort("/dev/s3c2410_serial1", 115200, 8, 1);
			listenCmd(); 
		}
		return m_fd;
	}
	public static void ColseSerialPort()
	{
		if (-1 == m_fd) {
			m_fd = -1;
			return; 
		} 

		HardwareControler.close(m_fd);
	}
	public static int WriteSerialPort(byte[] data)
	{
		if (-1 == m_fd) { 
			return -1; 
		}
		return HardwareControler.write(m_fd, data);
	}
/*	
	private static void doCmd(byte[] cmd) throws InterruptedException { 		
		String sCurrentContext = SerialPortControlBroadCast.mCurrentContext.getClass().getName().toString();//当前窗体名称
		String sLedSelActivityContext = LedSelActivity.class.getName().toString();
		if (systemconfigtype) {
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
		}else {
			//对当前不是控制或者测试页面的一些页面调不出控制或者测试页面的处理
			String sCabinetAddActivity = CabinetAddActivity.class.getName();
			String sCabinetLibraryActivity = CabinetLibraryActivity.class.getName();
			String sLedSelActivity = LedSelActivity.class.getName();
			
			//当前不是控制页面
			if (!sCurrentContext.equals( ControlActivity.class.getName() )){
				if (ControlKeyAdjugement(cmd[0]) ) {
					if (SerialPortControlBroadCast.mCurrentContext instanceof Activity)
					{
					    Activity activity = (Activity)mCurrentContext;
//						if (sCurrentContext == sCabinetAddActivity) {
//							activity.finish();
//							LedSelActivity.getInstance().finish();
//						} else if (sCurrentContext == sCabinetLibraryActivity){
//							activity.finish();
//							LedSelActivity.getInstance().finish();
//						}
//						else if (sCurrentContext == sLedSelActivity) {
//							LedSelActivity.getInstance().finish();
//						}
//						else{
//							;
//						}
											
						
						if (sCurrentContext.equals(sCabinetAddActivity)) {
							activity.finish();
							LedSelActivity.getInstance().finish();
						} else if (sCurrentContext.equals(sCabinetLibraryActivity)){
							activity.finish();
							LedSelActivity.getInstance().finish();
						}
						else if (sCurrentContext.equals(sLedSelActivity)) {
							LedSelActivity.getInstance().finish();
						}
						else{
							;
						}
						
						
						
					}										
					
					HomePageActivity.getInstance().startControlActivity();

					if (null == ControlActivity.getInstance()) {
					}
					else {
						ControlActivity.getInstance().SetTemplate(1);
					}
				}
			}
			
			//当前不是测试页面
			if (!sCurrentContext.equals(TestActivity.class.getName())) {
				if (TestKeyAdjugement(cmd[0]) ) {
					
					if (SerialPortControlBroadCast.mCurrentContext instanceof Activity)
					{
					    Activity activity = (Activity)mCurrentContext;
						if (sCurrentContext.equals(sCabinetAddActivity)) {
							activity.finish();
							LedSelActivity.getInstance().finish();
						} else if (sCurrentContext.equals(sCabinetLibraryActivity)){
							activity.finish();
							LedSelActivity.getInstance().finish();
						}
						else if (sCurrentContext.equals(sLedSelActivity)) {
							LedSelActivity.getInstance().finish();
						}
						else{
							;
						}
					}
					
					HomePageActivity.getInstance().startTestActivity();
					PannelLedControlBroadCast.MakePannelChoicesOFF();
					PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TESTLIGHT); 
				}
			}
			
			//当前是控制页面
			if (sCurrentContext.equals(ControlActivity.class.getName())){
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
			}
			//当前是测试页面
			if (sCurrentContext.equals(TestActivity.class.getName())){
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
			}
			//当前是测试配置页面
			if (sCurrentContext.equals(TestConfigActivity.class.getName())){
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
			}
		}  
		
		if (cmd[0] != CMD_CLOCKWISE && cmd[0] != CMD_ANTICLOCKWISE &&
			cmd[0] != CMD_OK && cmd[0] != CMD_CANCEL) {
			CMD_CURRENT = cmd[0];
		}
		
	//	SystemDiagnoseActivity.mView.postInvalidate();	
	} 
*/
	
	private static void doCmd_V1(byte[] cmd, int arg) throws InterruptedException { 
		String sCurrentContext = SerialPortControlBroadCast.mCurrentContext.getClass().getName().toString();//当前窗体名称
		//String sLedSelActivityContext = LedSelActivity.class.getName().toString();
		if(systemconfigtype)
		{
			switch (cmd[0]) {
			case CMD_CLOCKWISE:
				value = value + 8;
				if(value >=360){
					value =0;
				}
				SystemDiagnoseFragment.RotatePictrue(value);
				break;
			case CMD_ANTICLOCKWISE:
				value = value - 8;
				if (value <= 0) {
					value =360;
				}
				SystemDiagnoseFragment.RotatePictrue(value);
				break;
			default:
				{
					if(!isLight[cmd[0]-0x50])
					{
						SystemDiagnoseFragment.setAbnormalmodule(cmd[0]);
						isLight[cmd[0]-0x50] = true;
						PannelLedControlBroadCast.MakeSingleLightsAlwaysON((byte)(cmd[0]-0x50+0x20));
					}else {
						SystemDiagnoseFragment.setnormalmodule(cmd[0]);
						isLight[cmd[0]-0x50] = false;
						PannelLedControlBroadCast.MakeSingleLightsAlwaysOFF((byte)(cmd[0]-0x50+0x30));
					}
					SystemDiagnoseFragment.GetView().postInvalidate();	
					break;
				}
			}
			return;
		}
		
		//对当前不是控制或者测试页面的一些页面调不出控制或者测试页面的处理
		String sCabinetAddActivity = CabinetAddActivity.class.getName();
		String sCabinetLibraryActivity = CabinetLibraryActivity.class.getName();
		String sLedSelActivity = LedSelActivity.class.getName();
		//当前不是控制页面
		if (!sCurrentContext.equals( ControlActivity.class.getName() )){
			if (ControlKeyAdjugement(cmd[0]) ) {
				if (SerialPortControlBroadCast.mCurrentContext instanceof Activity)
				{
				    Activity activity = (Activity)mCurrentContext;
					if (sCurrentContext.equals(sCabinetAddActivity)) {
						activity.finish();
						LedSelActivity.getInstance().finish();
					} else if (sCurrentContext.equals(sCabinetLibraryActivity)){
						activity.finish();
						LedSelActivity.getInstance().finish();
					}
					else if (sCurrentContext.equals(sLedSelActivity)) {
						LedSelActivity.getInstance().finish();
					}
					else{
						//activity.finish();
					}
				}
			}
		}
		//当前不是测试页面
		if (!sCurrentContext.equals(TestActivity.class.getName())) {
			if (TestKeyAdjugement(cmd[0]) ) {				
				if (SerialPortControlBroadCast.mCurrentContext instanceof Activity)
				{
				    Activity activity = (Activity)mCurrentContext;
					if (sCurrentContext.equals(sCabinetAddActivity)) {
						activity.finish();
						LedSelActivity.getInstance().finish();
					} else if (sCurrentContext.equals(sCabinetLibraryActivity)){
						activity.finish();
						LedSelActivity.getInstance().finish();
					}
					else if (sCurrentContext.equals(sLedSelActivity)) {
						LedSelActivity.getInstance().finish();
					}
					else{
						//activity.finish();
					}
				}				
			}
		}
		
		//对不同按键的处理
		switch (cmd[0]) { 
			case CMD_TEMPLATE_NO1: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(1);
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO1LIGHT);
				break; 
			case CMD_TEMPLATE_NO2:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(2);
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO2LIGHT);
				break;
			case CMD_TEMPLATE_NO3:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(3);
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO3LIGHT);
				break;
			case CMD_TEMPLATE_NO4:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(4);
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO4LIGHT);
				break;
			case CMD_TEMPLATE_NO5:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(5);
				}
				PannelLedControlBroadCast.MakeLightsAlwaysOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO5LIGHT);
				break;
			case CMD_BRIGHTNESS: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetBright();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_BRIGHTNESSLIGHT);
				break;
			case CMD_COLORTEMP: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetColorTemp();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_COLORTEMPLIGHT);  
				break;
			case CMD_SATURATION:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetSaturation();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_SATURATIONLIGHT);
				break;
			case CMD_CONTRAST: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetContrast();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_CONTRASTLIGHT);
				break;
			case CMD_SWITCH:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SwitchChannel();			
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_SWITCHLIGHT);
				break;
			case CMD_TEST:
				HomePageActivity.getInstance().startTestActivity();
				if (null == TestActivity.getInstance()) {
				}
				else {
					//TestActivity.getInstance().SetTestMode(0);
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TESTLIGHT);
				break;
			case CMD_OK: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					//ControlActivity.getInstance().SetOK();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_OKLIGHT); 
				break;
			case CMD_CANCEL: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetCancel();
				}
				PannelLedControlBroadCast.MakePannelChoicesOFF();
				PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_CANCELLIGHT); 			
				break;
			case CMD_CLOCKWISE:	//顺时针
				switch (CMD_CURRENT) {
				case CMD_BRIGHTNESS:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetBrightIncrease();
					}
					break;
				case CMD_COLORTEMP:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetColorTempIncrease();
					}
					break;
				case CMD_SATURATION:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetSaturationIncrease();
					}
					break;
				case CMD_CONTRAST:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetContrastIncrease();
					}
					break;
				case CMD_TEST:
					if (null != TestActivity.getInstance()) {
						TestActivity.getInstance().SetTestMode(1);
					}
				default:
					break;
				}			
				break;
			case CMD_ANTICLOCKWISE: //逆时针
				switch (CMD_CURRENT) {
				case CMD_BRIGHTNESS:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetBrightReduce();
					}
					break;
				case CMD_COLORTEMP:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetColorTempReduce();
					}
					break;
				case CMD_SATURATION:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetSaturationReduce();
					}
					break;
				case CMD_CONTRAST:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetContrastReduce();
					}
					break;
				case CMD_TEST:
					if (null != TestActivity.getInstance()) {
						TestActivity.getInstance().SetTestMode(2);
					}					
				default:
					break;
				}
				
				break;
			default: 
				if (CMD_CLOCK_VALUE == (cmd[0] & 0xF0)) {
					
				}
				break; 
				
		} 
		if (cmd[0] != CMD_CLOCKWISE && cmd[0] != CMD_ANTICLOCKWISE &&
			cmd[0] != CMD_OK && cmd[0] != CMD_CANCEL) {
			CMD_CURRENT = cmd[0];
		}
		//SystemDiagnoseActivity.mView.postInvalidate();	
		
		String str;
		str = "doCmd ID:" + arg + "    Time:" + System.currentTimeMillis();
		Log.i("Hello world", str);
	} 
	
	//消息处理
	private static Handler handler = new Handler(){ 
		@Override 
		public void handleMessage(Message msg) { 
			switch (msg.what) { 
				case MESSAGE_RECEIVED_CMD: 
					byte[] cmd = (byte[])msg.obj; 
					try {							
						
						String str;
						str = "Rcv  ID:" + msg.arg1 + "    Time:" + System.currentTimeMillis();
						Log.i("Hello world", str);
						
						
						//doCmd(cmd);
						doCmd_V1(cmd, msg.arg1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
					break; 
				default: 
					break; 
			}
		} 
	};
 
	public static void listenCmd() { 
		new Thread() { 
		@Override 
		public void run() { 
			int i = 0;
			while (true) 
			{
				try { 
					Thread.sleep(1); 
					if (-1 == m_fd) 
					{
						continue; 
					}
					else
					{						
						//如果 fd 有数据可读，返回 1, 如 果没有数据可读，返回 0，出错时 返回-1。 
						int result = HardwareControler.select(m_fd, 2, 0); 
						if (1 == result) 
						{

							byte[] buf = new byte[CMD_LENGHT]; 
							int n = HardwareControler.read(m_fd, buf, buf.length); 
							if(-1 != n)
							{
								if (!FromBroadKey(buf[0])) {
									continue;
								}
								
								if (buf[0] != CMD_CLOCKWISE && buf[0] != CMD_ANTICLOCKWISE) {
									//控制连续按键，半秒内连续按键，不响应
									if (preTimeStamp==-1) {
 										preTimeStamp = System.currentTimeMillis();
									}else {
										long tCurTimeStamp = System.currentTimeMillis();//当前时间								
										long dTime = tCurTimeStamp-preTimeStamp;//和上一次按键的间隔时间															
										if (dTime<=500) {
											preTimeStamp = tCurTimeStamp;//如果半秒内连续按键，不响应
											continue;
										}
										preTimeStamp = tCurTimeStamp;
									}									
								}
								
								//按键有效，发送消息					
								Message message = new Message(); 
								message.what = MESSAGE_RECEIVED_CMD; 
								message.obj = buf; 
								message.arg1 = i;								
								String str;
								str = "Send ID:" + i + "    Time:" + System.currentTimeMillis();
								Log.i("Hello world", str);
								i++;
								
								//byte[] cmd = (byte[])message.obj; 
								//doCmd_V1(cmd);
								//Thread.sleep(100);
								//handler.sendMessage(message);
								handler.sendMessageAtFrontOfQueue(message);
							}
						}
					} 
				} 
				catch (InterruptedException e) 
				{ 
					e.printStackTrace(); 
				} 
				
				} 
			} 
	
		}.start(); 
	}
		
	
	private static boolean ControlKeyAdjugement(byte cmd) {
		return (cmd != CMD_SWITCH && cmd !=CMD_TEST &&
				cmd != CMD_OK && cmd != CMD_CANCEL&&
				cmd != CMD_CLOCKWISE && cmd != CMD_ANTICLOCKWISE&&
				cmd != CMD_CLOCK_VALUE);
	}
	private static boolean TestKeyAdjugement(byte cmd) {
		return (cmd == CMD_TEST);
	}

	private static boolean FromBroadKey(byte cmd) {
		if ( cmd == CMD_TEMPLATE_NO1 || cmd == CMD_TEMPLATE_NO2 ||cmd == CMD_TEMPLATE_NO3 
		|| cmd == CMD_TEMPLATE_NO4 || cmd == CMD_TEMPLATE_NO5 
		|| cmd == CMD_BRIGHTNESS || cmd == CMD_COLORTEMP
		|| cmd == CMD_SATURATION || cmd == CMD_CONTRAST
		|| cmd == CMD_TEST || cmd == CMD_SWITCH
		|| cmd == CMD_CANCEL ||cmd == CMD_OK
		|| cmd == CMD_CLOCKWISE ||cmd == CMD_ANTICLOCKWISE ||cmd == CMD_CLOCK_VALUE ) {
			return true;					
		} else {
			return false;
		}
	}	
		
	public static String getDYNAMICACTION() {
		return DYNAMICACTION;
	}
	public static void setDYNAMICACTION(String dYNAMICACTION) {
		DYNAMICACTION = dYNAMICACTION;
	} 

	public static byte GetCMD_CURRENT(){
		return CMD_CURRENT;
	}
	public static void SetCMD_CURRENT(byte nCMD_CURRENT){
		CMD_CURRENT = nCMD_CURRENT;
	}
	public static int GetValue(){
		return value;
	}
	public static void SetValue(int nValue){
		value = nValue;
	}
}
