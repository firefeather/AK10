/*
   * �ļ��� CardInformation.java
   * ���������б�com.szaoto.ak10.configuration
   * �汾��Ϣ���汾��
   * ��������2014��1��22������11:25:44
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.configuration;


/*
 * ����CardInformation
 * ���� liangdb
 * ��Ҫ���� ����ͨ����Ϣ
 * ��������2014��1��22��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class CardInformation {

	/**
	 * @return the ucMACAddress
	 */
	public byte[] getUcMACAddress() {
		return ucMACAddress;
	}

	/**
	 * @param ucMACAddress the ucMACAddress to set
	 */
	public void setUcMACAddress(byte[] ucMACAddress) {
		this.ucMACAddress = ucMACAddress;
	}

	/**
	 * @return the nType
	 */
	public short getnType() {
		return nType;
	}

	/**
	 * @param nType the nType to set
	 */
	public void setnType(short nType) {
		this.nType = nType;
	}

	/**
	 * @return the nSlotID
	 */
	public short getnSlotID() {
		return nSlotID;
	}

	/**
	 * @param nSlotID the nSlotID to set
	 */
	public void setnSlotID(short nSlotID) {
		this.nSlotID = nSlotID;
	}

	/**
	 * @return the sVersion
	 */
	public String getsVersion() {
		return sVersion;
	}

	/**
	 * @param sVersion the sVersion to set
	 */
	public void setsVersion(String sVersion) {
		this.sVersion = sVersion;
	}

	/**
	 * @return the sHardwareID
	 */
	public String getsHardwareID() {
		return sHardwareID;
	}

	/**
	 * @param sHardwareID the sHardwareID to set
	 */
	public void setsHardwareID(String sHardwareID) {
		this.sHardwareID = sHardwareID;
	}

	/**
	 * @return the sDate
	 */
	public String getdDate() {
		return sDate;
	}

	/**
	 * @param sDate the sDate to set
	 */
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	//Mac��ַ���Ĵ���0x000 �C 0x005��
	private byte[] ucMACAddress = new byte[6];
	//�����ͣ�
	//0x01	���ؿ�
	//0x02	�ɼ���
	//0x03	���Ϳ�
	private short nType;	
	//��۵�ַ
	private short nSlotID;
	//�����汾	�����汾
	private String sVersion;
	//Ӳ��ID���Ĵ���0x008 �C 0x00F��
	private String sHardwareID;
	//���������գ�
	//BCD���룬��2014��1��14�գ�0x14, 0x01, 0x14
	private String sDate;
	/**
	 * 
	 */
	public CardInformation() {
		// TODO Auto-generated constructor stub
	}

}
