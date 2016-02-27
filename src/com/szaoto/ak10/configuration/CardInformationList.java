/*
   * 文件名 CardInformationList.java
   * 包含类名列表com.szaoto.ak10.configuration
   * 版本信息，版本号
   * 创建日期2014年1月22日上午11:33:28
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.configuration;

import java.util.ArrayList;

import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.util.UtilFun;

/*
 * 类名CardInformationList
 * 作者 liangdb
 * 主要功能 插槽上所有卡的通用信息
 * 创建日期2014年1月22日
 * 修改者，修改日期，修改内容
 */
public class CardInformationList {

	private static ArrayList<CardInformation> lCardInformations = new ArrayList<CardInformation>();
	/**
	 * 
	 */
	public CardInformationList() {
		// TODO Auto-generated constructor stub
	}
	
	public static CardInformation[] GetCardInformations() {
		CardInformation[] cardInformations = new CardInformation[8];
		for (int i = 0; i < cardInformations.length; i++) {
			cardInformations[i] = new CardInformation();
			cardInformations[i].setnSlotID((short) -1);
		}
		
		for (CardInformation cardInformation : lCardInformations) {
			cardInformations[cardInformation.getnSlotID() - 1] = cardInformation;
		}
		
		return cardInformations;
	}
	
	public static int[] GetSlotType() {
		int[] nSlotType = new int[8];
		for (int i = 0; i < nSlotType.length; i++) {
			nSlotType[i] = -1;
		}
		
		for (CardInformation cardInformation : lCardInformations) {
			nSlotType[cardInformation.getnSlotID() - 1] = cardInformation.getnType();
		}
		
		return nSlotType;
	}

	public static int GetCardInformationList()
	{
		byte[] ucMACAddress = new byte[6];
		ucMACAddress[0] = (byte) 0xFF;
		ucMACAddress[1] = (byte) 0xFF;
		ucMACAddress[2] = (byte) 0xFF;
		ucMACAddress[3] = (byte) 0xFF;
		ucMACAddress[4] = (byte) 0xFF;
		ucMACAddress[5] = (byte) 0xFF;
		
		byte[] ucAddress = new byte[2];
		ucAddress[0] = 0x00;
		ucAddress[1] = 0x00;
		
		int nRevLength = 16;
		
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
		int nSendLength = ((42 < nRevLength) ? nRevLength : 42) + 22;
		
		ucSendData = Packager.EthernetPackDataRead(ucMACAddress, ucAddress, nRevLength);

		int nResult = -1;
	
		try 
		{
			nResult = SpiControl.WriteSpi(ucSendData,nSendLength);
			
			//wait
			try 
			{
				Thread.sleep(500);
			} 
			catch (InterruptedException e) 
			{

				e.printStackTrace();
			}
			//读取返回
			
			byte[] ucRevData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
			//ucRevData = SpiControl.ReadSpi(nRevLength);
			ucRevData = SpiControl.ReadSpi(1500);
			//接收显示
			if (null != ucRevData) {
				
				if (FrameDataField.ETH_DATA_MAX_SIZE + 18 < ucRevData.length) {
					return -1;
				}
				else {
					if (64 <= ucRevData.length) {
						lCardInformations.clear();
						if (0 != ucRevData.length % 64) {
							return -1;
						}
						int nPackNum = ucRevData.length / 64;
						for (int i = 0; i < nPackNum; i++) {
							CardInformation cardInformation = new CardInformation();
							byte[] ucSourceMACAddress = new byte[6];
							ucSourceMACAddress[0] =  ucRevData[i * 64 + 18];
							ucSourceMACAddress[1] =  ucRevData[i * 64 + 19];
							ucSourceMACAddress[2] =  ucRevData[i * 64 + 20];
							ucSourceMACAddress[3] =  ucRevData[i * 64 + 21];
							ucSourceMACAddress[4] =  ucRevData[i * 64 + 22];
							ucSourceMACAddress[5] =  ucRevData[i * 64 + 23];
							
							cardInformation.setUcMACAddress(ucSourceMACAddress);
							cardInformation.setnType(ucRevData[i * 64 + 20]);
							cardInformation.setnSlotID(ucRevData[i * 64 + 23]);
							byte[] ucVersion = new byte[2];
							ucVersion[0] = ucRevData[i * 64 + 21];
							ucVersion[1] = ucRevData[i * 64 + 22];
							cardInformation.setsVersion(UtilFun.bytes2HexStringEx(ucVersion, 2, "."));
							
							byte[] ucHardwareID = new byte[8];
							ucHardwareID[0] = ucRevData[i * 64 + 26];
							ucHardwareID[1] = ucRevData[i * 64 + 27];
							ucHardwareID[2] = ucRevData[i * 64 + 28];
							ucHardwareID[3] = ucRevData[i * 64 + 29];
							ucHardwareID[4] = ucRevData[i * 64 + 30];
							ucHardwareID[5] = ucRevData[i * 64 + 31];
							ucHardwareID[6] = ucRevData[i * 64 + 32];
							ucHardwareID[7] = ucRevData[i * 64 + 33];
							
							cardInformation.setsHardwareID(UtilFun.bytes2HexString(ucHardwareID, 8));

							byte[] ucDate = new byte[3];
							ucDate[0] = ucRevData[i * 64 + 29];
							ucDate[1] = ucRevData[i * 64 + 30];
							ucDate[2] = ucRevData[i * 64 + 31];
							cardInformation.setsDate(UtilFun.bytes2HexString(ucDate, 3, "-"));
							
							lCardInformations.add(cardInformation);
						}
					}
				}
			}
			else {
				return -1;
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		
		if (0 > nResult) {
			return nResult;
		}
	
		return 0;
	}

}
