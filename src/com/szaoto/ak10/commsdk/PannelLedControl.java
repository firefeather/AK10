/*
   * �ļ��� SerialPortControl.java
   * ���������б�com.szaoto.ak10.commsdk
   * �汾��Ϣ���汾��
   * ��������2014��1��15������5:30:46
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;


/*
 * ����SerialPortControl
 * ���� liangdb
 * ��Ҫ���� ���ڿ�����
 * ��������2014��1��15��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class PannelLedControl {

//	public static int m_fd = -1;
	private final static int CMD_LENGHT = 1; // ����ȣ��̶�1��byte��
	//private final static int MESSAGE_RECEIVED_CMD = 1; 
	
	//�޼���ť1��
	//���ܰ���13�������ܰ�������5��ģ�尴����6����WriteSerialPort�ٰ�����1��ȷ�ϰ�����1��ȡ��������
		//���ٰ������������ȡ�ɫ�¡����Ͷȡ��Աȶȡ��л������ԡ�
	
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
	/*ARM -> FPGA�������
	0x10���رհ��������Զ��ơ���Ĭ�ϴ򿪣�
	0x11���򿪰��������Զ��ơ�
	0x20~2c��������Ӧ�����ƣ�˳��ͬ����0x5x��
	0x2f���������а����ơ�
	0x30~3c���رճ�����Ӧ�����ƣ�˳��ͬ����0x5x��
	0x3f���ر����г��������ơ�*/
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
