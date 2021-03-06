package com.szaoto.ak10;

enum ECtrlType{
	ePower,	//开关屏
	eColorTmp,	//色温
	eGamma,	//gamma
	eLight,	//亮度
	eTimeSwitch //定时开关
}

public class CtrlStruct {
	private ECtrlType m_eCurrentType;
	private int m_nPowerValue;
	private int m_nColorTmp;
	private float m_fGammaR;
	private float m_fGammaG;
	private float m_fGammaB;
	private int m_nLight;
	private int m_nSave;//save: 0 =预览，1 = 保存； 色温，伽马，亮度共用，通过m_eCurrentType来区分
	private boolean m_bTimeSwitchEnable;//定时开关
	private long m_nBeginTime; //HH:mm:ss 3600*HH + mm*60 + ss
	private long m_nEndTime;//HH:mm:ss 3600*HH + mm*60 + ss
      
	public CtrlStruct() {
		super();
	}
	
	//操作类型
	public void SetCtrlType(ECtrlType eType){
		m_eCurrentType = eType;
	}
	public ECtrlType GetCtrlType(){
		return m_eCurrentType;
	}
	
	//开关屏//0 = 关屏，1 = 开屏
	public void SetPowerValue(int nValue){
		m_nPowerValue = nValue;
	}
	public int GetPowerValue(){
		return m_nPowerValue;
	}
	
	//色温
	public void SetColorTmp(int nValue){
		m_nColorTmp = nValue;
	}
	public int GetColorTmp(){
		return m_nColorTmp;
	}
	
	//伽马
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
	
	//亮度
	public void SetLight(int nValue){
		m_nLight = nValue;
	}
	public int GetLight(){
		return m_nLight;
	}
	
	//save: 0 =预览，1 = 保存； 色温，伽马，亮度共用，通过m_eCurrentType来区分
	public void SetPreviewOrSave(int nValue){
		m_nSave = nValue;
	}
	public int GetPreviewOrSave(){
		return m_nSave;
	}
	
	//定时开关使能
	public void SetTimeSwitchEnable(Boolean bEnable){
		m_bTimeSwitchEnable = bEnable;
	}
	public boolean GetTimeSwitchEnable(){
		return m_bTimeSwitchEnable;
	}
	//定时开关开始结束时间
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
	
  