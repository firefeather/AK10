package com.szaoto.ak10.datacomm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.R.integer;
import android.util.Log;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.common.GammaData;
import com.szaoto.ak10.common.Display.ColourRGB;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.dataaccess.returnClass;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.util.LogUtil;
import com.szaoto.ak10.util.UtilFun;


/**
 * 
 * @author huh
 *
 */
public class LEDParamComm {

	public LEDParamComm() {
		
	}

	private static byte[] getBytes (char[] chars) {   
		Charset cs = Charset.forName ("UTF-8");   
		CharBuffer cb = CharBuffer.allocate (chars.length);   
		cb.put (chars);                 
		cb.flip ();   
		ByteBuffer bb = cs.encode (cb);     
		return bb.array();         
	}

	//发送开关屏控制

	public static int SetPower(int nPowerValue, byte[] macaddress, int Rj45Num)
	{
		//00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27
		//55 55 12 34 F9 D2 00 00 00 01 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2B		关屏
		//55 55 12 34 F9 D2 00 00 00 01 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2A		开屏
		byte[] ucAddress = new byte[2];
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		
		//地址
		int nResult = -1;
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);		
		ucSendData = Packager.PackRelay(macaddress, ucAddress, (short)(-2), (short)0, nPowerValue > 0 ? true : false);
		try {
			LogUtil.WriteLog(ucSendData, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, 64);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	    return nResult;
	}
	
	
	public static int SetContrast(int nContrast,int Chid)
	{
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[4];
		byte[] macaddress = new byte[6];
		
		int nResult = -1;
		
		//地址
		int portNum = Chid%1000;
	
		ucAddress[0] = (byte) 0x01;
		ucAddress[1] =  (byte) (0x03+(portNum-1)*0x20);
		
		int nLength = 2;
		byte[] ucData = new byte[2];
		
		short tContrast = 0;
		if (nContrast < 50)
		{
			tContrast = (short) (256 - (50 - nContrast) * 5);
		}
		else
		{
			tContrast = (short) (256 + (nContrast - 50 ) * 2);
		}
		
		ucData[1] = (byte) ((tContrast&0xff00)>>8);
		ucData[0] = (byte) ((tContrast&0x00ff));
		
		
		//mac
		macaddress=ChannelDB.GetMacByChId(Chid,Ak10Application.GetLedId());
		
		int nSequenceNumber = 0x00;
		
		int nSendLength = ((42 < nLength) ? nLength : 42) + 22;
		ucSendData = Packager.EthernetPackDataWrite(macaddress, ucAddress, nSequenceNumber, 2, ucData);

		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	    return nResult;
	}
	
	public static int SetSaturation(int nSaturation,int Chid)
	{
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[4];
		byte[] macaddress = new byte[6];
	
		//地址
		int portNum = Chid%1000;
		
		ucAddress[0] = (byte) 0x01;
		ucAddress[1] =  (byte) (0x01+(portNum-1)*0x20);

		//mac
		macaddress=ChannelDB.GetMacByChId(Chid,Ak10Application.GetLedId());	
		int nLength = 2;
		byte[] ucData = new byte[2];
		
		short nSaturation_ = 0;
		
		if (nSaturation < 50)
		{
			nSaturation_ = (short) (256 - (50 - nSaturation) * 5);
		}
		else
		{
			nSaturation_ = (short) (256 + (nSaturation - 50 ) * 20);
		}
		
		ucData[1] = (byte) ((nSaturation_&0xff00)>>8);
		ucData[0] = (byte) ((nSaturation_&0x00ff));
		
		int nSequenceNumber = 0x00;		
		int nSendLength = ((42 < nLength) ? nLength : 42) + 22;
		
		ucSendData = Packager.EthernetPackDataWrite(macaddress, ucAddress, nSequenceNumber, nLength, ucData);	
		
		int nResult = -1;

		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	
		return nResult;	
	}
	
	public static int SetColorTemp(byte[] macaddress,int Rj45Num,ColourRGB sColorRGB,short nHighLowGap,short nGrayEnhanceMode)
	{
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];
	
		int nResult = -1;		
		//地址
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);
	
		int nSendLength = 28 + 22 + 14;
		ucSendData = Packager.PackSetColorTemperature(macaddress, ucAddress, sColorRGB, nHighLowGap, nGrayEnhanceMode);	

		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	
		return nResult;
	}


	//对发送卡设置
	public static int SavePara(byte[] macaddress,int Rj45Num, int nSaveType)
	{
		//		nSaveType值说明：
		//		0x0	不存储
		//		0x1	将扫描卡参数存储到Flash（包括参数、亮度、色温、智能地址，带载区域）
		//		0x2	将伽马值存储到Flash
		//		0x3	将走线查找表存储到Flash
		//		0x4	将读取视频地址存到Flash
		//		0x5	将运算处理包存到Flash
		//		0x6	将读校正地址包存到Flash
		//		0x7	将边界点类型查找包存到FLASH
		//		0x8	将边界校正系数包存到FLASH
		//		0x9	将区查找包存到FLASH
		byte[] EthPackData;
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];
	
		//地址
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);
	
		int nResult = -1;	
		int nSendLength = 28 + 22 + 14;
		ucSendData = Packager.PackSaveScanCardPara(macaddress, ucAddress, (short) (-1), nSaveType, false);
		
		
		nResult = InterfaceComm.SendtoScanCard(macaddress, Rj45Num, ucSendData);
		
		//EthPackData = Packager.PackMutiple28byteData(macaddress,ucAddress,ucSendData,28);
//		try {
//			LogUtil.WriteLog(ucSendData, false);
//			//LogUtil.WriteLog(EthPackData, false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try 
//		{
//			nResult = SpiControl.WriteSpi(EthPackData, nSendLength);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}

		return nResult;	
	}
	
	//对发送卡设置
	public static int SetBright(int nBright,byte[] macaddress,int Rj45Num)
	{
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];
	
		
		//地址
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);
	
		int nBrightness = (int)(2.56 * nBright + 0.5);
		int nResult = -1;	
		int nSendLength = 28 + 22 + 14;
		ucSendData = Packager.PackSetBright(macaddress, ucAddress, nBrightness);
		try {
			//String strFunNameString = "SetBright():";
			//byte[] ucName = UtilFun.hexStringToBytes(strFunNameString);
			//LogUtil.WriteLog(strFunNameString);
			
			LogUtil.WriteLog(ucSendData, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return nResult;	
	}

	//10,12位时，用GammaR
	public static int SetGamma(float GammaR, float GammaG, float GammaB, byte[] macaddress,int Rj45Num)
	{
		GammaData sGammaData = new GammaData();
		short nVideowid = 8;
		short nGrayLevel = 16;
		sGammaData.setnVideowid(nVideowid);
		sGammaData.setnGrayLevel(nGrayLevel);
		//颜色类型
		switch (nVideowid)
		{
		case 10:
		case 12:
			sGammaData.setfGammaRGB(GammaR);//Gamma值，用于10,12位
			for (int n = 0; n < 4096;n ++)
			{	
				sGammaData.getsGammaTableRGB()[n] = 
					(short) GetYByGamma(sGammaData.getnGrayLevel(),	n, sGammaData.getfGammaRGB(),(short) 12);							
			}
			break;
		case 8:
		default:
			float[]gamma = new float[3];
			gamma[0] = GammaR;
			gamma[1] = GammaG;
			gamma[2] = GammaB;
			sGammaData.setfGamma(gamma);

			SetOneGammaTable(sGammaData, (short)0);
			SetOneGammaTable(sGammaData, (short)1);
			SetOneGammaTable(sGammaData, (short)2);
			break;
		}
		
		//byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];
		byte[] EthPackData = new byte[64];
		
		//地址
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);
	
		int Max28Bytelength = 53 * 28;// 1500个字节中最多可同时发送53(1484字节)个扫描卡28字节命令。1484/28	// = 53
		int nResult = -1;	
		int nSendLength = 28 * 32 + 22 + 14;
		byte[] ucSendData;
		for(int i = 0; i < 3; i++){
			ucSendData = Packager.PackSetGamma(macaddress, ucAddress, sGammaData, i);
			int length = ucSendData.length;
			try {
				String strFunNameString = "SetGamma():";
				LogUtil.WriteLog(strFunNameString);				
				
				//LogUtil.WriteLog(ucSendData, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (int j = 0; j < ucSendData.length/28; j++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, j * 28, temp, 0, 28);
						
				EthPackData = Packager.PackMutiple28byteData(macaddress, ucAddress, temp, temp.length);
				try {
					LogUtil.WriteLog(temp, false);
					//LogUtil.WriteLog(EthPackData, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					nResult = SpiControl.WriteSpi(EthPackData,64);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (0 > nResult) {
					return 1;
				}
			}
			
//			if (length / 28 < 53) {
//				EthPackData = Packager.PackMutiple28byteData(macaddress,
//						ucAddress, ucSendData, length);
//				try {
//					LogUtil.WriteLog(EthPackData, false);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//				try {
//					nResult = SpiControl.WriteSpi(EthPackData,
//							(length + 18) < 64 ? 64 : length + 18);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				if (0 > nResult) {
//					return 1;
//				}
//			}
			
//			try 
//			{
//				nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
//			} 
//			catch (Exception e) 
//			{
//				e.printStackTrace();
//			}		
		}
		
		return nResult;	
	}
	
	private static void SetOneGammaTable(GammaData gm, short nColorID){
		short sGammaTable[][] = new short[3][256]; //GAMMA表
		sGammaTable = gm.getsGammaTable();

		short nGrayLevel = gm.getnGrayLevel();
		float fGamma[] = gm.getfGamma();//GAMMA值
		if (nGrayLevel == 1)
		{
			sGammaTable[nColorID][0] = 0;
			for (int n = 0; n < 256; n++)
			{
				sGammaTable[nColorID][n] = 0xFF;
			}
		}
		else if(nGrayLevel >= 2 && nGrayLevel <= 4)
		{
			for (int n = 0; n < 256;n ++)
			{
				sGammaTable[nColorID][n] = (short) (GetYByGamma(nGrayLevel, n, fGamma[nColorID],(short)8) * 4);
			}
		}
		else 
		{
			for (int n = 0; n < 256;n ++)
			{
				sGammaTable[nColorID][n] = (short) GetYByGamma(nGrayLevel, n, fGamma[nColorID],(short)8);
			}
		}
		
		gm.setsGammaTable(sGammaTable);
	}

	//通过GAMMA及X值得到Y值
	private static double GetYByGamma(short nGrayLevel, double x, double dGamma,short nVideowid)
	{
		int GAMMA_CALIBRATION_COLOR_DEPTH = 16;//校正、伽马颜色灰度级数	
		double f = java.lang.Math.pow(2.0, GAMMA_CALIBRATION_COLOR_DEPTH) - 1;
		double y = 0.0f;
		switch (nVideowid)
		{
		case 10:
		case 12:
			y = java.lang.Math.pow(x / 4095.0, dGamma) * f;
			if(nGrayLevel > 5 &&( x > 15 && x < 2115))
				y += 9;
			break;
		case 8:
		default:
			y = java.lang.Math.pow(x / 255.0, dGamma) * f;
			if(nGrayLevel > 5 &&( x > 0 && x < 51))
				y += 7;
			break;
		}
		return y;
	}
	
	//设置1.	同步延时参数 2.	关闭扫描周期参数
	public static int SetSysPara(int nSynchronDelay, int nDisableScanCycle, byte[] macaddress, int Rj45Num)
	{	
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];
			
		//地址
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (Rj45Num-1) *0x10);
	
		int nResult = -1;	
		int nSendLength = 28 + 22 + 14;
		ucSendData = Packager.PackSet3DPara(macaddress, ucAddress, nSynchronDelay, nDisableScanCycle);
		try {	
			LogUtil.WriteLog(ucSendData, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return nResult;	
	}
}
