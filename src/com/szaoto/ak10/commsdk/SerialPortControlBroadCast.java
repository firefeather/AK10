/*
   * �ļ��� SerialPortControlBroadCast.java
   * ���������б�com.szaoto.ak10.commsdk
   * �汾��Ϣ���汾��
   * ��������2014��1��15������5:30:46
   * ��Ȩ���� liangdb-szaoto
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
 * ����SerialPortControl
 * ���� liangdb
 * ��Ҫ���� ���ڿ�����
 * ��������2014��1��15��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class SerialPortControlBroadCast {
	private static Context mCurrentContext = null;
	public static int m_fd = -1;
	private final static int CMD_LENGHT = 1; // ����ȣ��̶�1��byte��
	private final static int MESSAGE_RECEIVED_CMD = 1; 
	public static boolean systemconfigtype = false;
	private static String DYNAMICACTION = "PannelKeyDown";
	
	public static long  preTimeStamp =-1;
	//�޼���ť1��
	//���ܰ���13�������ܰ�������5��ģ�尴����6����WriteSerialPort�ٰ�����1��ȷ�ϰ�����1��ȡ��������
		//���ٰ������������ȡ�ɫ�¡����Ͷȡ��Աȶȡ��л������ԡ�
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

	public final static byte CMD_CLOCKWISE 		= 0x60;//˳ʱ��
	public final static byte CMD_ANTICLOCKWISE 	= 0x61;//��ʱ��
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
	
	/*ARM -> FPGA�������
	0x10���رհ��������Զ��ơ���Ĭ�ϴ򿪣�
	0x11���򿪰��������Զ��ơ�
	0x20~2c��������Ӧ�����ƣ�˳��ͬ����0x5x��
	0x2f���������а����ơ�
	0x30~3c���رճ�����Ӧ�����ƣ�˳��ͬ����0x5x��
	0x3f���ر����г��������ơ�*/

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
		String sCurrentContext = SerialPortControlBroadCast.mCurrentContext.getClass().getName().toString();//��ǰ��������
		String sLedSelActivityContext = LedSelActivity.class.getName().toString();
		if (systemconfigtype) {
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
		}else {
			//�Ե�ǰ���ǿ��ƻ��߲���ҳ���һЩҳ����������ƻ��߲���ҳ��Ĵ���
			String sCabinetAddActivity = CabinetAddActivity.class.getName();
			String sCabinetLibraryActivity = CabinetLibraryActivity.class.getName();
			String sLedSelActivity = LedSelActivity.class.getName();
			
			//��ǰ���ǿ���ҳ��
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
			
			//��ǰ���ǲ���ҳ��
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
			
			//��ǰ�ǿ���ҳ��
			if (sCurrentContext.equals(ControlActivity.class.getName())){
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
			}
			//��ǰ�ǲ���ҳ��
			if (sCurrentContext.equals(TestActivity.class.getName())){
				Intent intent = new Intent();
		        intent.setAction(getDYNAMICACTION());
		        intent.putExtra("cmd", String.valueOf(cmd[0]));
		        intent.putExtra("classname", sCurrentContext);
		        mContext.sendBroadcast(intent);
			}
			//��ǰ�ǲ�������ҳ��
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
		String sCurrentContext = SerialPortControlBroadCast.mCurrentContext.getClass().getName().toString();//��ǰ��������
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
		
		//�Ե�ǰ���ǿ��ƻ��߲���ҳ���һЩҳ����������ƻ��߲���ҳ��Ĵ���
		String sCabinetAddActivity = CabinetAddActivity.class.getName();
		String sCabinetLibraryActivity = CabinetLibraryActivity.class.getName();
		String sLedSelActivity = LedSelActivity.class.getName();
		//��ǰ���ǿ���ҳ��
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
		//��ǰ���ǲ���ҳ��
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
		
		//�Բ�ͬ�����Ĵ���
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
			case CMD_CLOCKWISE:	//˳ʱ��
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
			case CMD_ANTICLOCKWISE: //��ʱ��
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
	
	//��Ϣ����
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
						//��� fd �����ݿɶ������� 1, �� ��û�����ݿɶ������� 0������ʱ ����-1�� 
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
									//����������������������������������Ӧ
									if (preTimeStamp==-1) {
 										preTimeStamp = System.currentTimeMillis();
									}else {
										long tCurTimeStamp = System.currentTimeMillis();//��ǰʱ��								
										long dTime = tCurTimeStamp-preTimeStamp;//����һ�ΰ����ļ��ʱ��															
										if (dTime<=500) {
											preTimeStamp = tCurTimeStamp;//�����������������������Ӧ
											continue;
										}
										preTimeStamp = tCurTimeStamp;
									}									
								}
								
								//������Ч��������Ϣ					
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
