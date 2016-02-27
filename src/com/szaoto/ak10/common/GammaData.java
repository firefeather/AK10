/*
   * �ļ��� GammaData.java
   * ���������б�com.szaoto.ak10.common
   * �汾��Ϣ���汾��
   * ��������2014��1��10������7:30:39
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.common;

/*
 * ����GammaData
 * ���� liangdb
 * ��Ҫ���� GAMMA ����
 * ��������2014��1��10��
 * �޸��ߣ��޸����ڣ��޸�����
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

//	private short nVideowid;					//b00-8bit��Ƶ b01-10bit��Ƶ b10-12bit��Ƶ
//	private boolean bSendThreeColor;			//��ɫͬʱ����
//	private short nGrayLevel;					//�Ҷȼ���
//	private float fGamma[] = new float[3];		//GAMMAֵ
//	private short sGammaTable[][] = new short[3][256];				//GAMMA��
//	private float fGammaRGB;					//Gammaֵ������10,12λ
//	private short sGammaTableRGB[] = new short[4096];	//GAMMA��������10,12λ
	private short nVideowid;					//b00-8bit��Ƶ b01-10bit��Ƶ b10-12bit��Ƶ
	private boolean bSendThreeColor;			//��ɫͬʱ����
	private short nGrayLevel;					//�Ҷȼ���
	private float fGamma[] = new float[3];		//GAMMAֵ
	private short sGammaTable[][] = new short[3][256];				//GAMMA��
	private float fGammaRGB;					//Gammaֵ������10,12λ
	private short sGammaTableRGB[] = new short[4096];	//GAMMA��������10,12λ

	
	/**
	 * 
	 */
	public GammaData() {
		// TODO Auto-generated constructor stub
		 nVideowid = 0;					//b00-8bit��Ƶ b01-10bit��Ƶ b10-12bit��Ƶ
		 bSendThreeColor = false;			//��ɫͬʱ����
		 nGrayLevel = 0;					//�Ҷȼ���
		//private float fGamma[] = new float[3];		//GAMMAֵ
	//	private short sGammaTable[][] = new short[3][256];				//GAMMA��
		  fGammaRGB = 0.0f;					//Gammaֵ������10,12λ
	//	private short sGammaTableRGB[] = new short[4096];
	}

}