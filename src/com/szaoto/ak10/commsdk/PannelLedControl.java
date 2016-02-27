/*
   * 文件名 SerialPortControl.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2014年1月15日下午5:30:46
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;


/*
 * 类名SerialPortControl
 * 作者 liangdb
 * 主要功能 串口控制类
 * 创建日期2014年1月15日
 * 修改者，修改日期，修改内容
 */
public class PannelLedControl {

//	public static int m_fd = -1;
	private final static int CMD_LENGHT = 1; // 命令长度（固定1个byte）
	//private final static int MESSAGE_RECEIVED_CMD = 1; 
	
	//无极旋钮1个
	//功能按键13个：功能按键包括5个模板按键、6个快WriteSerialPort速按键、1个确认按键、1个取消按键。
		//快速按键：包括亮度、色温、饱和度、对比度、切换、测试。
	
	public final static byte CMD_TEMPLATE_NO1LIGHT 	= 0x20;
	public final static byte CMD_TEMPLATE_NO2LIGHT 	= 0x21;
	public final static byte CMD_TEMPLATE_NO3LIGHT 	= 0x22;
	public final static byte CMD_TEMPLATE_NO4LIGHT 	= 0x23;
	public final static byte CMD_TEMPLATE_NO5LIGHT 	= 0x24;
	public final static byte CMD_BRIGHTNESSLIGHT 	= 0x25;
	public final static byte CMD_COLORTEMPLIGHT 	= 0x26;
	public final static byte CMD_SATURATIONLIGHT	= 0x27;
	public final static byte CMD_CONTRASTLIGHT 		= 0x28;
	public final static byte CMD_SWITCHLIGHT 		= 0x2A;
	public final static byte CMD_TESTLIGHT 			= 0x29;
	public final static byte CMD_OKLIGHT 			= 0x2C;
	public final static byte CMD_CANCELLIGHT 		= 0x2B;
	
	public final static byte CMD_TEMPLATE_NO1DARK 	= 0x30;
	public final static byte CMD_TEMPLATE_NO2DARK 	= 0x31;
	public final static byte CMD_TEMPLATE_NO3DARK 	= 0x32;
	public final static byte CMD_TEMPLATE_NO4DARK 	= 0x33;
	public final static byte CMD_TEMPLATE_NO5DARK 	= 0x34;
	public final static byte CMD_BRIGHTNESSDARK 	= 0x35;
	public final static byte CMD_COLORTEMPDARK 	    = 0x36;
	public final static byte CMD_SATURATIONDARK	    = 0x37;
	public final static byte CMD_CONTRASTDARK 		= 0x38;
	public final static byte CMD_SWITCHDARK 		= 0x3A;
	public final static byte CMD_TESTDARK 			= 0x39;
	public final static byte CMD_OKDARK 			= 0x3C;
	public final static byte CMD_CANCELDARK 		= 0x3B;
	
	
	public final static byte CloseBtndownAutoLight 		= 0x10;
	public final static byte OpenBtndownAutoLight 		= 0x11;
	public final static byte LightsAlwaysON 		= 0x2F;
	public final static byte LightsAlwaysOFF 		= 0x3F;
	
	byte[] cmd = new byte[CMD_LENGHT];
	/*ARM -> FPGA串口命令：
	0x10：关闭按键按下自动灯。（默认打开）
	0x11：打开按键按下自动灯。
	0x20~2c：常亮对应按键灯，顺序同命令0x5x。
	0x2f：常亮所有按键灯。
	0x30~3c：关闭常亮对应按键灯，顺序同命令0x5x。
	0x3f：关闭所有常亮按键灯。*/
	public PannelLedControl()
	{
		
	}
	public static int MakeLightsAlwaysON()
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = LightsAlwaysON;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int MakeLightsAlwaysOFF()
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = LightsAlwaysOFF;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int MakeSingleLightsAlwaysON(byte commondOn)
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = commondOn;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int MakeSingleLightsAlwaysOFF(byte commondOff)
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = commondOff;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int CloseBtndownAutoLight()
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = CloseBtndownAutoLight;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	
	public static int OpenBtndownAutoLight()
	{
		byte[] cmd = new byte[CMD_LENGHT];
		cmd[0] = OpenBtndownAutoLight;
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int MakePannelModulesOFF()
	{
		byte[] cmd = new byte[5];
		cmd[0] = CMD_TEMPLATE_NO1DARK;
		cmd[1] = CMD_TEMPLATE_NO2DARK;
		cmd[2] = CMD_TEMPLATE_NO3DARK;
		cmd[3] = CMD_TEMPLATE_NO4DARK;
		cmd[4] = CMD_TEMPLATE_NO5DARK;
		
		return SerialPortControl.WriteSerialPort(cmd);
	}
	public static int MakePannelChoicesOFF()
	{
		byte[] cmd = new byte[8];
		cmd[0] = CMD_BRIGHTNESSDARK;
		cmd[1] = CMD_COLORTEMPDARK;
		cmd[2] = CMD_SATURATIONDARK;
		cmd[3] = CMD_CONTRASTDARK;
		cmd[4] = CMD_SWITCHDARK;
		cmd[5] = CMD_TESTDARK;
		cmd[6] = CMD_OKDARK;
		cmd[7] = CMD_CANCELDARK;
		
		return SerialPortControl.WriteSerialPort(cmd);
	}
	
	/**
	 * 
	 */
	
}
