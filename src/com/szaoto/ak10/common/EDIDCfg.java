package com.szaoto.ak10.common;



public class EDIDCfg {
	public String m_sResolution;	//�ֱ���
	public int m_iFrame;			//֡��	
	public int m_iHBlanking; //ˮƽ��û����(Horizontal Blanking)
	public int m_iHSyncOffset;//ˮƽͬ��ƫ��H.Sync Offset
	public int m_iHSyncPulseWidth;//	ˮƽͬ��������H.Sync Pulse Width
	public int m_iVBlanking; 	//��ֱ��û����(Vertical Blanking)
	public int m_iVSyncOffset; //��ֱͬ��ƫ��V.Sync Offset
	public int m_iVSyncPulseWidth;	//��ֱͬ��������V.Sync Pulse Width
	public int m_iSupportValue; //ʮ����00������֧�֣�01��֧��AK100��10��֧��AK1000��11����֧��
	public String getM_sResolution() {
		return m_sResolution;
	}
	public void setM_sResolution(String m_sResolution) {
		this.m_sResolution = m_sResolution;
	}
	public int getM_iFrame() {
		return m_iFrame;
	}
	public void setM_iFrame(int m_iFrame) {
		this.m_iFrame = m_iFrame;
	}
	public int getM_iHBlanking() {
		return m_iHBlanking;
	}
	public void setM_iHBlanking(int m_iHBlanking) {
		this.m_iHBlanking = m_iHBlanking;
	}
	public int getM_iHSyncOffset() {
		return m_iHSyncOffset;
	}
	public void setM_iHSyncOffset(int m_iHSyncOffset) {
		this.m_iHSyncOffset = m_iHSyncOffset;
	}
	public int getM_iHSyncPulseWidth() {
		return m_iHSyncPulseWidth;
	}
	public void setM_iHSyncPulseWidth(int m_iHSyncPulseWidth) {
		this.m_iHSyncPulseWidth = m_iHSyncPulseWidth;
	}
	public int getM_iVBlanking() {
		return m_iVBlanking;
	}
	public void setM_iVBlanking(int m_iVBlanking) {
		this.m_iVBlanking = m_iVBlanking;
	}
	public int getM_iVSyncOffset() {
		return m_iVSyncOffset;
	}
	public void setM_iVSyncOffset(int m_iVSyncOffset) {
		this.m_iVSyncOffset = m_iVSyncOffset;
	}
	public int getM_iVSyncPulseWidth() {
		return m_iVSyncPulseWidth;
	}
	public void setM_iVSyncPulseWidth(int m_iVSyncPulseWidth) {
		this.m_iVSyncPulseWidth = m_iVSyncPulseWidth;
	}
	public int getM_iSupportValue() {
		return m_iSupportValue;
	}
	public void setM_iSupportValue(int m_iSupportValue) {
		this.m_iSupportValue = m_iSupportValue;
	}

}
