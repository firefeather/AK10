package com.szaoto.ak10.datacomm;

import java.util.ArrayList;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.configuration.CardInformationList;
import com.szaoto.ak10.util.AESCipher;

public class MonitorComm {


	static public byte[] SendCardReadAddress(int portid){
		byte[] ucAddress = new byte[2];
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x01 + portid *0x10);
		return ucAddress;
	}
	
	static public byte[] ReadBackData(int DisplayID)//¶ÁÊý¾Ý
	{
		byte[] Readback = new byte[FrameDataField.ETH_DATA_MAX_SIZE -5];
		return Readback;
	}

	static public int SendMonitorDataWriteCommond(int DisplayID) {
	
		return 0;
	}

	static public byte[] SendCardWriteAddress(int portid){
		byte[] ucAddress = new byte[2];
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + portid *0x10);
		return ucAddress;
	}



	static public int SendMonitorConnectstateCommond(int DisplayID) {
	
		return 0;
	}
	
	
}