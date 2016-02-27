/*
   * 文件名 SerialPortControl.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2014年1月15日下午5:30:46
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

import android.os.Handler;
import android.os.Message;
import com.friendlyarm.AndroidSDK.HardwareControler;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.control.ControlActivity;
import com.szaoto.ak10.systemconfig.SystemDiagnoseFragment;
import com.szaoto.ak10.test.TestActivity;

/*
 * 类名SerialPortControl
 * 作者 liangdb
 * 主要功能 串口控制类
 * 创建日期2014年1月15日
 * 修改者，修改日期，修改内容
 */
public class SerialPortControl {

	public static int m_fd = -1;
	private final static int CMD_LENGHT = 1; // 命令长度（固定1个byte）
	private final static int MESSAGE_RECEIVED_CMD = 1; 
	
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
	public final static byte CMD_SATURATION 	= 0x57;
	public final static byte CMD_CONTRAST 		= 0x58;
	public final static byte CMD_SWITCH 		= 0x5A;
	public final static byte CMD_TEST 			= 0x59;
	public final static byte CMD_OK 			= 0x5C;
	public final static byte CMD_CANCEL 		= 0x5B;

	public final static byte CMD_CLOCKWISE 		= 0x60;
	public final static byte CMD_ANTICLOCKWISE 	= 0x61;
	public final static byte CMD_CLOCK_VALUE	= 0x70;
	
	private static byte CMD_CURRENT = 0;
	private static int value = 0;
	public static boolean[] isLight = new boolean[13];
	
	
	
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
	public SerialPortControl() {

	}
	public static int OpenSerialPort()
	{
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
	
	private static void doCmd(byte[] cmd) throws InterruptedException { 
		
		
	//	if(HomePageActivity.systemconfigtype)
		if(SerialPortControlBroadCast.systemconfigtype)
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
						PannelLedControl.MakeSingleLightsAlwaysON((byte)(cmd[0]-0x50+0x20));
					}else {
						SystemDiagnoseFragment.setnormalmodule(cmd[0]);
						isLight[cmd[0]-0x50] = false;
						PannelLedControl.MakeSingleLightsAlwaysOFF((byte)(cmd[0]-0x50+0x30));
					}
					SystemDiagnoseFragment.GetView().postInvalidate();
					break;
				}
			}
			return;
		}
		switch (cmd[0]) { 
			case CMD_TEMPLATE_NO1: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(1);
				}
				PannelLedControl.MakeLightsAlwaysOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TEMPLATE_NO1LIGHT);
				break; 
			case CMD_TEMPLATE_NO2:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(2);
				}
				PannelLedControl.MakeLightsAlwaysOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TEMPLATE_NO2LIGHT);
				break;
			case CMD_TEMPLATE_NO3:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(3);
				}
				PannelLedControl.MakeLightsAlwaysOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TEMPLATE_NO3LIGHT);
				break;
			case CMD_TEMPLATE_NO4:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(4);
				}
				PannelLedControl.MakeLightsAlwaysOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TEMPLATE_NO4LIGHT);
				break;
			case CMD_TEMPLATE_NO5:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetTemplate(5);
				}
				PannelLedControl.MakeLightsAlwaysOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TEMPLATE_NO5LIGHT);
				break;
			case CMD_BRIGHTNESS: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetBright();
		//			ControlActivity.mView.postInvalidate();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_BRIGHTNESSLIGHT);
				break;
			case CMD_COLORTEMP: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetColorTemp();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_COLORTEMPLIGHT);  
				break;
			case CMD_SATURATION:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetSaturation();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_SATURATIONLIGHT);
				break;
			case CMD_CONTRAST: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetFocusForSetContrast();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_CONTRASTLIGHT);
				break;
			case CMD_SWITCH:
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SwitchChannel();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_SWITCHLIGHT);
				break;
			case CMD_TEST:
				HomePageActivity.getInstance().startTestActivity();
				if (null == TestActivity.getInstance()) {
				}
				else {
					//TestActivity.getInstance().SetTestMode(0);
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TESTLIGHT); 
				break;
			case CMD_OK: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetOK();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_OKLIGHT); 
				break;
			case CMD_CANCEL: 
				HomePageActivity.getInstance().startControlActivity();
				if (null == ControlActivity.getInstance()) {
				}
				else {
					ControlActivity.getInstance().SetCancel();
				}
				PannelLedControl.MakePannelChoicesOFF();
				PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_CANCELLIGHT); 			
				break;
			case CMD_CLOCKWISE: 
				switch (CMD_CURRENT) {
				case CMD_BRIGHTNESS:
					if (null != ControlActivity.getInstance()) {
						try {
							ControlActivity.getInstance().SetBrightIncrease();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case CMD_COLORTEMP:
					if (null != ControlActivity.getInstance()) {
						
						//hh
						//ControlActivity.getInstance().SetColorTempIncrease();
					}
				//	PannelLedControl.MakePannelChoicesOFF();
				//	PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_COLORTEMPLIGHT); 
					break;
				case CMD_SATURATION:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetSaturationIncrease();
					}
				//	PannelLedControl.MakePannelChoicesOFF();
				//	PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_SATURATIONLIGHT); 
					break;
				case CMD_CONTRAST:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetContrastIncrease();
					}
				//	PannelLedControl.MakePannelChoicesOFF();
				//	PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_CONTRASTLIGHT); 
					break;
				case CMD_TEST:
					if (null != TestActivity.getInstance()) {
						TestActivity.getInstance().SetTestMode(1);
					}
				//	PannelLedControl.MakePannelChoicesOFF();
				//	PannelLedControl.MakeSingleLightsAlwaysON(PannelLedControl.CMD_TESTLIGHT); 

				default:
					break;
				}			
				break;
			case CMD_ANTICLOCKWISE: 
				switch (CMD_CURRENT) {
				case CMD_BRIGHTNESS:
					if (null != ControlActivity.getInstance()) {
						ControlActivity.getInstance().SetBrightReduce();
					}
					break;
				case CMD_COLORTEMP:
					if (null != ControlActivity.getInstance()) {
						//hh
						//ControlActivity.getInstance().SetColorTempReduce();
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
		
	//	SystemDiagnoseActivity.mView.postInvalidate();	
	} 
	
	private static Handler handler = new Handler(){ 
		@Override 
		public void handleMessage(Message msg) { 
	
			switch (msg.what) { 
				case MESSAGE_RECEIVED_CMD: 
					byte[] cmd = (byte[])msg.obj; 
					try {
						doCmd(cmd);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
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
			while (true) 
			{
				try 
				{ 
					Thread.sleep(10); 

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
							//Thread.sleep(100); 
							byte[] buf = new byte[CMD_LENGHT]; 
							int n = HardwareControler.read(m_fd, buf, buf.length); 
							if(-1 != n)
							{
								Message message = new Message(); 
								message.what = MESSAGE_RECEIVED_CMD; 
								message.obj = buf; 
								handler.sendMessage(message); 
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
}
