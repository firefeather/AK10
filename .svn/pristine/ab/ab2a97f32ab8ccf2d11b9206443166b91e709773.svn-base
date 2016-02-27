/*
   * 文件名 CardInformation.java
   * 包含类名列表com.szaoto.ak10.configuration
   * 版本信息，版本号
   * 创建日期2014年1月22日上午11:25:44
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.configuration;


/*
 * 类名CardInformation
 * 作者 liangdb
 * 主要功能 卡的通用信息
 * 创建日期2014年1月22日
 * 修改者，修改日期，修改内容
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

	//Mac地址（寄存器0x000 – 0x005）
	private byte[] ucMACAddress = new byte[6];
	//卡类型：
	//0x01	主控卡
	//0x02	采集卡
	//0x03	发送卡
	private short nType;	
	//插槽地址
	private short nSlotID;
	//卡主版本	卡副版本
	private String sVersion;
	//硬件ID（寄存器0x008 – 0x00F）
	private String sHardwareID;
	//出厂年月日：
	//BCD编码，如2014年1月14日：0x14, 0x01, 0x14
	private String sDate;
	/**
	 * 
	 */
	public CardInformation() {
		// TODO Auto-generated constructor stub
	}

}
