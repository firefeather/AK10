/*
   * 文件名 GammaData.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2014年1月10日下午7:30:39
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common;

/*
 * 类名GammaData
 * 作者 liangdb
 * 主要功能 GAMMA 数据
 * 创建日期2014年1月10日
 * 修改者，修改日期，修改内容
 */
public class GammaData {
	

	/**
	 * @return the nVideowid
	 */
	public short getnVideowid() {
		return nVideowid;
	}

	/**
	 * @param nVideowid the nVideowid to set
	 */
	public void setnVideowid(short nVideowid) {
		this.nVideowid = nVideowid;
	}

	/**
	 * @return the bSendThreeColor
	 */
	public boolean isbSendThreeColor() {
		return bSendThreeColor;
	}

	/**
	 * @param bSendThreeColor the bSendThreeColor to set
	 */
	public void setbSendThreeColor(boolean bSendThreeColor) {
		this.bSendThreeColor = bSendThreeColor;
	}

	/**
	 * @return the nGrayLevel
	 */
	public short getnGrayLevel() {
		return nGrayLevel;
	}

	/**
	 * @param nGrayLevel the nGrayLevel to set
	 */
	public void setnGrayLevel(short nGrayLevel) {
		this.nGrayLevel = nGrayLevel;
	}

	/**
	 * @return the fGamma
	 */
	public float[] getfGamma() {
		return fGamma;
	}

	/**
	 * @param fGamma the fGamma to set
	 */
	public void setfGamma(float[] fGamma) {
		this.fGamma = fGamma;
	}

	/**
	 * @return the sGammaTable
	 */
	public short[][] getsGammaTable() {
		return sGammaTable;
	}

	/**
	 * @param sGammaTable the sGammaTable to set
	 */
	public void setsGammaTable(short[][] sGammaTable) {
		this.sGammaTable = sGammaTable;
	}

	/**
	 * @return the fGammaRGB
	 */
	public float getfGammaRGB() {
		return fGammaRGB;
	}

	/**
	 * @param fGammaRGB the fGammaRGB to set
	 */
	public void setfGammaRGB(float fGammaRGB) {
		this.fGammaRGB = fGammaRGB;
	}

	/**
	 * @return the sGammaTableRGB
	 */
	public short[] getsGammaTableRGB() {
		return sGammaTableRGB;
	}

	/**
	 * @param sGammaTableRGB the sGammaTableRGB to set
	 */
	public void setsGammaTableRGB(short[] sGammaTableRGB) {
		this.sGammaTableRGB = sGammaTableRGB;
	}

//	private short nVideowid;					//b00-8bit视频 b01-10bit视频 b10-12bit视频
//	private boolean bSendThreeColor;			//三色同时发送
//	private short nGrayLevel;					//灰度级别
//	private float fGamma[] = new float[3];		//GAMMA值
//	private short sGammaTable[][] = new short[3][256];				//GAMMA表
//	private float fGammaRGB;					//Gamma值，用于10,12位
//	private short sGammaTableRGB[] = new short[4096];	//GAMMA表，用于10,12位
	private short nVideowid;					//b00-8bit视频 b01-10bit视频 b10-12bit视频
	private boolean bSendThreeColor;			//三色同时发送
	private short nGrayLevel;					//灰度级别
	private float fGamma[] = new float[3];		//GAMMA值
	private short sGammaTable[][] = new short[3][256];				//GAMMA表
	private float fGammaRGB;					//Gamma值，用于10,12位
	private short sGammaTableRGB[] = new short[4096];	//GAMMA表，用于10,12位

	
	/**
	 * 
	 */
	public GammaData() {
		// TODO Auto-generated constructor stub
		 nVideowid = 0;					//b00-8bit视频 b01-10bit视频 b10-12bit视频
		 bSendThreeColor = false;			//三色同时发送
		 nGrayLevel = 0;					//灰度级别
		//private float fGamma[] = new float[3];		//GAMMA值
	//	private short sGammaTable[][] = new short[3][256];				//GAMMA表
		  fGammaRGB = 0.0f;					//Gamma值，用于10,12位
	//	private short sGammaTableRGB[] = new short[4096];
	}

}
