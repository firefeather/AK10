/*
   * 文件名 FrameDataField.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2013年12月28日下午8:34:03
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

/*
 * 类名FrameDataField
 * 作者 liangdb
 * 主要功能 
 * 创建日期2013年12月28日
 * 修改者，修改日期，修改内容
 */
public class FrameDataField {

	public final static int ETH_DATA_MAX_SIZE = 1500;
	
	/**
	 * 
	 */
	public FrameDataField() {
		// TODO Auto-generated constructor stub
		
	}
	
	boolean bMulticast;		//组播	0
	int nMulticastNum;		//组播号	0
	boolean bNoMulticast;	//非组播	0
	boolean bAnswer;		//应答	
	boolean bRead;			//读/写	true:读	false:写
	boolean bFIFO;			//FIFO/REG true:FIFO flase:REG
	byte ucAddress[] = new byte[2];			//地址

	int nSerialNumber;		//序列号
	int nLength;			//长度
	byte ucData[] = new byte[ETH_DATA_MAX_SIZE - 5];			//数据

	public boolean isbMulticast() {
		return bMulticast;
	}
	public void setbMulticast(boolean bMulticast) {
		this.bMulticast = bMulticast;
	}
	public int getnMulticastNum() {
		return nMulticastNum;
	}
	public void setnMulticastNum(int nMulticastNum) {
		this.nMulticastNum = nMulticastNum;
	}
	public boolean isbNoMulticast() {
		return bNoMulticast;
	}
	public void setbNoMulticast(boolean bNoMulticast) {
		this.bNoMulticast = bNoMulticast;
	}
	public boolean isbAnswer() {
		return bAnswer;
	}
	public void setbAnswer(boolean bAnswer) {
		this.bAnswer = bAnswer;
	}
	public boolean isbRead() {
		return bRead;
	}
	public void setbRead(boolean bRead) {
		this.bRead = bRead;
	}
	public boolean isbFIFO() {
		return bFIFO;
	}
	public void setbFIFO(boolean bFIFO) {
		this.bFIFO = bFIFO;
	}
	public byte[] getUcAddress() {
		return ucAddress;
	}
	public void setUcAddress(byte[] ucAddress) {
		this.ucAddress = ucAddress;
	}
	public int getnSerialNumber() {
		return nSerialNumber;
	}
	public void setnSerialNumber(int nSerialNumber) {
		this.nSerialNumber = nSerialNumber;
	}
	public int getnLength() {
		return nLength;
	}
	public void setnLength(int nLength) {
		this.nLength = nLength;
	}
	public byte[] getUcData() {
		return ucData;
	}
	public void setUcData(byte[] ucData) {
		this.ucData = ucData;
	}
	public static int getEthDataMaxSize() {
		return ETH_DATA_MAX_SIZE;
	}
}
