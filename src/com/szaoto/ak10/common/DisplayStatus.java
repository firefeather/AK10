/*
   * 文件名 DisplayStatus.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2014年1月24日下午3:22:52
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common;

/*
 * 类名DisplayStatus
 * 作者 liangdb
 * 主要功能
 * 创建日期2014年1月24日
 * 修改者，修改日期，修改内容
 */
public class DisplayStatus {

	/**
	 * @return the bTimingBrightAdjust
	 */
	public boolean isbTimingBrightAdjust() {
		return bTimingBrightAdjust;
	}

	/**
	 * @param bTimingBrightAdjust the bTimingBrightAdjust to set
	 */
	public void setbTimingBrightAdjust(boolean bTimingBrightAdjust) {
		this.bTimingBrightAdjust = bTimingBrightAdjust;
	}

	/**
	 * @return the bAutoBrightAdjust
	 */
	public boolean isbAutoBrightAdjust() {
		return bAutoBrightAdjust;
	}

	/**
	 * @param bAutoBrightAdjust the bAutoBrightAdjust to set
	 */
	public void setbAutoBrightAdjust(boolean bAutoBrightAdjust) {
		this.bAutoBrightAdjust = bAutoBrightAdjust;
	}

	/**
	 * @return the nBright
	 */
	public int getnBright() {
		return nBright;
	}

	/**
	 * @param nBright the nBright to set
	 */
	public void setnBright(int nBright) {
		this.nBright = nBright;
	}

	/**
	 * @return the nColourTem
	 */
	public int getnColourTem() {
		return nColourTem;
	}

	/**
	 * @param nColourTem the nColourTem to set
	 */
	public void setnColourTem(int nColourTem) {
		this.nColourTem = nColourTem;
	}

	/**
	 * @return the nSaturation
	 */
	public int getnSaturation() {
		return nSaturation;
	}

	/**
	 * @param nSaturation the nSaturation to set
	 */
	public void setnSaturation(int nSaturation) {
		this.nSaturation = nSaturation;
	}

	/**
	 * @return the nContrast
	 */
	public int getnContrast() {
		return nContrast;
	}

	/**
	 * @param nContrast the nContrast to set
	 */
	public void setnContrast(int nContrast) {
		this.nContrast = nContrast;
	}

	/**
	 * @return the nChannel
	 */
	public int getnChannel() {
		return nChannel;
	}

	/**
	 * @param nChannel the nChannel to set
	 */
	public void setnChannel(int nChannel) {
		this.nChannel = nChannel;
	}

	private boolean bTimingBrightAdjust;
	private boolean bAutoBrightAdjust;

	private int nBright;
	private int nColourTem;
	private int nSaturation;
	private int nContrast;
	private int nChannel;
	
	/**
	 * 
	 */
	public DisplayStatus() {
		// TODO Auto-generated constructor stub
	}

}
