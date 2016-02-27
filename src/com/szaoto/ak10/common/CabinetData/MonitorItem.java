/*
   * 文件名 LinkTable.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2014年4月1日下午7:39:05
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common.CabinetData;

import java.util.ArrayList;
import java.util.List;

/*
 * 类名LinkTable
 * 作者 liangdb
 * 主要功能
 * 创建日期2014年4月1日
 * 修改者，修改日期，修改内容
 */
public class MonitorItem {
	private final static int POWER_VOL_NUM = 5;
	private final static int FAN_NUM = 2;
	private final static int FAN_ROTATION_NUM_MAX = 8;
	boolean bHumidityFlag;			//湿度
	boolean bTemperatureFlag;		//温度
	boolean bSmogFlag;				//烟雾
	boolean bBrightFlag;			//亮度
	boolean bLEDPointDetect;		//逐点监测
	List<Boolean> bPowerVolFlag = new ArrayList<Boolean>();
	List<Boolean> bFanStateFlag = new ArrayList<Boolean>();
	//boolean[] bPowerVolFlag = new boolean[POWER_VOL_NUM];		//电源电压
	//boolean[] bFanStateFlag = new boolean[FAN_NUM];		//风扇 0-左 1-右
	
	boolean bCapacityFactorFlag;	//功率
	boolean bVersionFlag;			//版本

	boolean bFanRotationFlag;         //2013-03-25 风扇转速
	boolean bNetWebErrorFlag ;        //2013-04-26
//	boolean[] bEightFanRotationFlag = new boolean[FAN_ROTATION_NUM_MAX];
	List<Boolean> bEightFanRotationFlag = new ArrayList<Boolean>();
	public boolean isbHumidityFlag() {
		return bHumidityFlag;
	}
	public void setbHumidityFlag(boolean bHumidityFlag) {
		this.bHumidityFlag = bHumidityFlag;
	}
	public boolean isbTemperatureFlag() {
		return bTemperatureFlag;
	}
	public void setbTemperatureFlag(boolean bTemperatureFlag) {
		this.bTemperatureFlag = bTemperatureFlag;
	}
	public boolean isbSmogFlag() {
		return bSmogFlag;
	}
	public void setbSmogFlag(boolean bSmogFlag) {
		this.bSmogFlag = bSmogFlag;
	}
	public boolean isbBrightFlag() {
		return bBrightFlag;
	}
	public void setbBrightFlag(boolean bBrightFlag) {
		this.bBrightFlag = bBrightFlag;
	}
	public boolean isbLEDPointDetect() {
		return bLEDPointDetect;
	}
	public void setbLEDPointDetect(boolean bLEDPointDetect) {
		this.bLEDPointDetect = bLEDPointDetect;
	}
	public List<Boolean> getbPowerVolFlag() {
		return bPowerVolFlag;
	}
	public void setbPowerVolFlag(List<Boolean> bPowerVolFlag) {
		this.bPowerVolFlag = bPowerVolFlag;
	}
	public List<Boolean> getbFanStateFlag() {
		return bFanStateFlag;
	}
	public void setbFanStateFlag(List<Boolean> bFanStateFlag) {
		this.bFanStateFlag = bFanStateFlag;
	}
	public boolean isbCapacityFactorFlag() {
		return bCapacityFactorFlag;
	}
	public void setbCapacityFactorFlag(boolean bCapacityFactorFlag) {
		this.bCapacityFactorFlag = bCapacityFactorFlag;
	}
	public boolean isbVersionFlag() {
		return bVersionFlag;
	}
	public void setbVersionFlag(boolean bVersionFlag) {
		this.bVersionFlag = bVersionFlag;
	}
	public boolean isbFanRotationFlag() {
		return bFanRotationFlag;
	}
	public void setbFanRotationFlag(boolean bFanRotationFlag) {
		this.bFanRotationFlag = bFanRotationFlag;
	}
	public boolean isbNetWebErrorFlag() {
		return bNetWebErrorFlag;
	}
	public void setbNetWebErrorFlag(boolean bNetWebErrorFlag) {
		this.bNetWebErrorFlag = bNetWebErrorFlag;
	}
	public List<Boolean> getbEightFanRotationFlag() {
		return bEightFanRotationFlag;
	}
	public void setbEightFanRotationFlag(List<Boolean> bEightFanRotationFlag) {
		this.bEightFanRotationFlag = bEightFanRotationFlag;
	}
	public static int getPowerVolNum() {
		return POWER_VOL_NUM;
	}
	public static int getFanNum() {
		return FAN_NUM;
	}
	public static int getFanRotationNumMax() {
		return FAN_ROTATION_NUM_MAX;
	}
}
