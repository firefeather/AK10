/*
   * �ļ��� FrameDataField.java
   * ���������б�com.szaoto.ak10.commsdk
   * �汾��Ϣ���汾��
   * ��������2013��12��28������8:34:03
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

/*
 * ����FrameDataField
 * ���� liangdb
 * ��Ҫ���� 
 * ��������2013��12��28��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class FrameDataField {

	public final static int ETH_DATA_MAX_SIZE = 1500;
	
	/**
	 * 
	 */
	public FrameDataField() {
		// TODO Auto-generated constructor stub
		
	}
	
	boolean bMulticast;		//�鲥	0
	int nMulticastNum;		//�鲥��	0
	boolean bNoMulticast;	//���鲥	0
	boolean bAnswer;		//Ӧ��	
	boolean bRead;			//��/д	true:��	false:д
	boolean bFIFO;			//FIFO/REG true:FIFO flase:REG
	byte ucAddress[] = new byte[2];			//��ַ

	int nSerialNumber;		//���к�
	int nLength;			//����
	byte ucData[] = new byte[ETH_DATA_MAX_SIZE - 5];			//����

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
