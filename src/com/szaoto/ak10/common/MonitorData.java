package com.szaoto.ak10.common;


public class MonitorData {
	public static final int FAN_NUM = 2; 	//风扇个数
	public static final int RELAY_NUMBER_MAX = 4; //继电器最大个数4
	public static final int POWER_VOL_NUM = 5; //箱体（扫描卡）5路电压值
	public static final int DEAD_LEAD_NUM_MAX = 65535;//坏灯数组
	public static final int SCAN_LINE_STATE_NUM = 16; //扫描线出错状态，相应bit位为1标示对应扫描线出错
	public static final int FAN_ROTATION_NUM_MAX = 8;
	public MonitorData()
	{
		nFanState = new short[FAN_NUM];
		nPowerState = new short[2];
		bWorkState = new boolean[RELAY_NUMBER_MAX];
		fPowerVol = new float[POWER_VOL_NUM];
		sErrorPoint = new ErrorPoint[ DEAD_LEAD_NUM_MAX];
		nDClkState = new short[SCAN_LINE_STATE_NUM];
		nScanLineState = new short[SCAN_LINE_STATE_NUM];
		nFanRotation = new short[FAN_ROTATION_NUM_MAX];
		bSmog = false;
	}
	long tttttttt;
	short nBrightness;			//亮度值
	short nBrightness2;			//亮度值
	float fTemperature;			//温度值
	float fHumidity;			//湿度值
	boolean bSmog;					//是否烟雾报警
	short nFanState[];			//风扇FAN_L状态	1	0x00	未检测		0x01	正常开启	0x03	异常关闭	0x04	正常关闭	0x05	异常开启
	short nPowerState[];       //电源状态
	boolean bWorkState[];			//4路继电器工作状态（0 - 继电器断开 1 - 继电器闭合）
	Version version;			//监控卡版本
	//////////////////////////////////////////////////////////////////////////
	float fPowerVol[];			//箱体（扫描卡）5路电压值
	int nCapacityFactor;		//箱体（扫描卡）功率
	short nErrorPointNum;		//坏灯个数
	ErrorPoint sErrorPoint[];//坏灯数组
	short nDClkState[];		//DCLK出错状态，相应bit位为1标示对应DCLK出错
	short nScanLineState[];	//扫描线出错状态，相应bit位为1标示对应扫描线出错
	//////////////////////////////////////////////////////////////////////////
	boolean bConnectStatus;		//LED显示屏连接状态（查询LED显示屏主ATLVC版本）
	boolean bBackUpStatus;			//LED显示屏备份状态（查询LED显示屏备ATLVC版本）
	short bDotDetectErrorFlag;//逐点检测错误标识，-1 --成功 0 -- 初始化  1 -- 失败  2 -- 异常，超出32坏点 2012-10-26 sunj
	short nFanRotation[];
	int nNetWebErrorPackageFlag ;
	short nNetWebErrorNum;
	LastMonitorAlarm mLastAlarm;
	public short getnBrightness() {
		return nBrightness;
	}
	public void setnBrightness(short nBrightness) {
		this.nBrightness = nBrightness;
	}
	public short getnBrightness2() {
		return nBrightness2;
	}
	public void setnBrightness2(short nBrightness2) {
		this.nBrightness2 = nBrightness2;
	}
	public float getfTemperature() {
		return fTemperature;
	}
	public void setfTemperature(float fTemperature) {
		this.fTemperature = fTemperature;
	}
	public float getfHumidity() {
		return fHumidity;
	}
	public void setfHumidity(float fHumidity) {
		this.fHumidity = fHumidity;
	}
	public Boolean getbSmog() {
		return bSmog;
	}
	public void setbSmog(Boolean bSmog) {
		this.bSmog = bSmog;
	}
	public short[] getnFanState() {
		return nFanState;
	}
	public void setnFanState(short[] nFanState) {
		this.nFanState = nFanState;
	}
	public short[] getnPowerState() {
		return nPowerState;
	}
	public void setnPowerState(short[] nPowerState) {
		this.nPowerState = nPowerState;
	}
	public boolean[] getbWorkState() {
		return bWorkState;
	}
	public void setbWorkState(boolean[] bWorkState) {
		this.bWorkState = bWorkState;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public float[] getfPowerVol() {
		return fPowerVol;
	}
	public void setfPowerVol(float[] fPowerVol) {
		this.fPowerVol = fPowerVol;
	}
	public int getnCapacityFactor() {
		return nCapacityFactor;
	}
	public void setnCapacityFactor(int nCapacityFactor) {
		this.nCapacityFactor = nCapacityFactor;
	}
	public short getnErrorPointNum() {
		return nErrorPointNum;
	}
	public void setnErrorPointNum(short nErrorPointNum) {
		this.nErrorPointNum = nErrorPointNum;
	}
	public ErrorPoint[] getsErrorPoint() {
		return sErrorPoint;
	}
	public void setsErrorPoint(ErrorPoint[] sErrorPoint) {
		this.sErrorPoint = sErrorPoint;
	}
	public short[] getnDClkState() {
		return nDClkState;
	}
	public void setnDClkState(short[] nDClkState) {
		this.nDClkState = nDClkState;
	}
	public short[] getnScanLineState() {
		return nScanLineState;
	}
	public void setnScanLineState(short[] nScanLineState) {
		this.nScanLineState = nScanLineState;
	}
	public Boolean getbConnectStatus() {
		return bConnectStatus;
	}
	public void setbConnectStatus(Boolean bConnectStatus) {
		this.bConnectStatus = bConnectStatus;
	}
	public Boolean getbBackUpStatus() {
		return bBackUpStatus;
	}
	public void setbBackUpStatus(Boolean bBackUpStatus) {
		this.bBackUpStatus = bBackUpStatus;
	}
	public short getbDotDetectErrorFlag() {
		return bDotDetectErrorFlag;
	}
	public void setbDotDetectErrorFlag(short bDotDetectErrorFlag) {
		this.bDotDetectErrorFlag = bDotDetectErrorFlag;
	}
	public short[] getnFanRotation() {
		return nFanRotation;
	}
	public void setnFanRotation(short[] nFanRotation) {
		this.nFanRotation = nFanRotation;
	}
	public int getnNetWebErrorPackageFlag() {
		return nNetWebErrorPackageFlag;
	}
	public void setnNetWebErrorPackageFlag(int nNetWebErrorPackageFlag) {
		this.nNetWebErrorPackageFlag = nNetWebErrorPackageFlag;
	}
	public short getnNetWebErrorNum() {
		return nNetWebErrorNum;
	}
	public void setnNetWebErrorNum(short nNetWebErrorNum) {
		this.nNetWebErrorNum = nNetWebErrorNum;
	}
	public LastMonitorAlarm getmLastAlarm() {
		return mLastAlarm;
	}
	public void setmLastAlarm(LastMonitorAlarm mLastAlarm) {
		this.mLastAlarm = mLastAlarm;
	}

}
