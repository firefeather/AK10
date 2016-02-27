package com.szaoto.ak10;

enum ECtrlType{
	ePower,	//������
	eColorTmp,	//ɫ��
	eGamma,	//gamma
	eLight,	//����
	eTimeSwitch //��ʱ����
}

public class CtrlStruct {
	private ECtrlType m_eCurrentType;
	private int m_nPowerValue;
	private int m_nColorTmp;
	private float m_fGammaR;
	private float m_fGammaG;
	private float m_fGammaB;
	private int m_nLight;
	private int m_nSave;//save: 0 =Ԥ����1 = ���棻 ɫ�£�٤�������ȹ��ã�ͨ��m_eCurrentType������
	private boolean m_bTimeSwitchEnable;//��ʱ����
	private long m_nBeginTime; //HH:mm:ss 3600*HH + mm*60 + ss
	private long m_nEndTime;//HH:mm:ss 3600*HH + mm*60 + ss
      
	public CtrlStruct() {
		super();
	}
	
	//��������
	public void SetCtrlType(ECtrlType eType){
		m_eCurrentType = eType;
	}
	public ECtrlType GetCtrlType(){
		return m_eCurrentType;
	}
	
	//������//0 = ������1 = ����
	public void SetPowerValue(int nValue){
		m_nPowerValue = nValue;
	}
	public int GetPowerValue(){
		return m_nPowerValue;
	}
	
	//ɫ��
	public void SetColorTmp(int nValue){
		m_nColorTmp = nValue;
	}
	public int GetColorTmp(){
		return m_nColorTmp;
	}
	
	//٤��
	public void SetGammaR(float fValue){
		m_fGammaR = fValue;
	}	
	public void SetGammaG(float fValue){
		m_fGammaG = fValue;
	}	
	public void SetGammaB(float fValue){
		m_fGammaB = fValue;
	}
	public float GetGammaR(){
		return m_fGammaR;
	}
	public float GetGammaG(){
		return m_fGammaG;
	}
	public float GetGammaB(){
		return m_fGammaB;
	}
	
	//����
	public void SetLight(int nValue){
		m_nLight = nValue;
	}
	public int GetLight(){
		return m_nLight;
	}
	
	//save: 0 =Ԥ����1 = ���棻 ɫ�£�٤�������ȹ��ã�ͨ��m_eCurrentType������
	public void SetPreviewOrSave(int nValue){
		m_nSave = nValue;
	}
	public int GetPreviewOrSave(){
		return m_nSave;
	}
	
	//��ʱ����ʹ��
	public void SetTimeSwitchEnable(Boolean bEnable){
		m_bTimeSwitchEnable = bEnable;
	}
	public boolean GetTimeSwitchEnable(){
		return m_bTimeSwitchEnable;
	}
	//��ʱ���ؿ�ʼ����ʱ��
	public void SetBeginTime(long date){
		m_nBeginTime = date;
	}
	public long GetBeginTime(){
		return m_nBeginTime;
	}
	public void SetEndTime(long date){
		m_nEndTime = date;
	}
	public long GetEndTime(){
		return m_nEndTime;
	}
}
	
  