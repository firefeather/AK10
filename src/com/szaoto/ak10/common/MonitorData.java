package com.szaoto.ak10.common;


public class MonitorData {
	public static final int FAN_NUM = 2; 	//���ȸ���
	public static final int RELAY_NUMBER_MAX = 4; //�̵���������4
	public static final int POWER_VOL_NUM = 5; //���壨ɨ�迨��5·��ѹֵ
	public static final int DEAD_LEAD_NUM_MAX = 65535;//��������
	public static final int SCAN_LINE_STATE_NUM = 16; //ɨ���߳���״̬����ӦbitλΪ1��ʾ��Ӧɨ���߳���
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
	short nBrightness;			//����ֵ
	short nBrightness2;			//����ֵ
	float fTemperature;			//�¶�ֵ
	float fHumidity;			//ʪ��ֵ
	boolean bSmog;					//�Ƿ�������
	short nFanState[];			//����FAN_L״̬	1	0x00	δ���		0x01	��������	0x03	�쳣�ر�	0x04	�����ر�	0x05	�쳣����
	short nPowerState[];       //��Դ״̬
	boolean bWorkState[];			//4·�̵�������״̬��0 - �̵����Ͽ� 1 - �̵����պϣ�
	Version version;			//��ؿ��汾
	//////////////////////////////////////////////////////////////////////////
	float fPowerVol[];			//���壨ɨ�迨��5·��ѹֵ
	int nCapacityFactor;		//���壨ɨ�迨������
	short nErrorPointNum;		//���Ƹ���
	ErrorPoint sErrorPoint[];//��������
	short nDClkState[];		//DCLK����״̬����ӦbitλΪ1��ʾ��ӦDCLK����
	short nScanLineState[];	//ɨ���߳���״̬����ӦbitλΪ1��ʾ��Ӧɨ���߳���
	//////////////////////////////////////////////////////////////////////////
	boolean bConnectStatus;		//LED��ʾ������״̬����ѯLED��ʾ����ATLVC�汾��
	boolean bBackUpStatus;			//LED��ʾ������״̬����ѯLED��ʾ����ATLVC�汾��
	short bDotDetectErrorFlag;//���������ʶ��-1 --�ɹ� 0 -- ��ʼ��  1 -- ʧ��  2 -- �쳣������32���� 2012-10-26 sunj
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
