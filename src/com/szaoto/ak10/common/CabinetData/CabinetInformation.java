package com.szaoto.ak10.common.CabinetData;

import java.util.List;

import com.szaoto.ak10.common.RECT;

/**
 * ������ToolFragment
 * ���ܣ�����������
 * ���ߣ�  zhangsj
 * �������ڣ�2014/07/02
 * �޸���,�޸����ڣ��޸�����
 * 
 */
public class CabinetInformation{
	int nID;					        //����ID
	int nSeriesID;				//��������ϵ�е�ID
//	String sSeriesName;         //��������ϵ�е�����
	String sName;				//�����ͺ�
	boolean bRead;					//�Ѷ�ȡ��ʾ

	String sPhoto;				//������Ƭ
	int nAddress;				//����������ͼ�еĵ�ַ����ַ�õ�����������ͬһ��·��Ψһ��

	short nScanCardCount;				//ɨ�迨������1��4��
	boolean bScanCardParaSynchro;		//ɨ�迨�����Ƿ�ͬ���޸�
	boolean bMonitorParaSynchro;		//��ز����Ƿ�ͬ���޸�
	INLINEMODE InlineMode;		//���߷�ʽ
	List<ScanCardAttachment> ListScancardAttachment;
//	map<short,ScanCardAttachment> mScancardAttachment;	//ɨ�迨����ظ�����
	RECT rtRect;				//�������ϽǼ���С
	int nAtlvcID;				//���ӵ�ATLVC
	

	public CabinetInformation() {
		super();
	}
	
	public CabinetInformation(int nSeriesID, String sName) {
		super();
		this.nSeriesID = nSeriesID;
		this.sName = sName;
	}
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
	public int getnSeriesID() {
		return nSeriesID;
	}
	public void setnSeriesID(int nSeriesID) {
		this.nSeriesID = nSeriesID;
	}
/*	public String getsSeriesName() {
		return sSeriesName;
	}
	public void setsSeriesName(String sSeriesName) {
		this.sSeriesName = sSeriesName;
	}
*/	
	
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public boolean isbRead() {
		return bRead;
	}
	public void setbRead(boolean bRead) {
		this.bRead = bRead;
	}
	public String getsPhoto() {
		return sPhoto;
	}
	public void setsPhoto(String sPhoto) {
		this.sPhoto = sPhoto;
	}
	public int getnAddress() {
		return nAddress;
	}
	public void setnAddress(int nAddress) {
		this.nAddress = nAddress;
	}
	public short getnScanCardCount() {
		return nScanCardCount;
	}
	public void setnScanCardCount(short nScanCardCount) {
		this.nScanCardCount = nScanCardCount;
	}
	public boolean isbScanCardParaSynchro() {
		return bScanCardParaSynchro;
	}
	public void setbScanCardParaSynchro(boolean bScanCardParaSynchro) {
		this.bScanCardParaSynchro = bScanCardParaSynchro;
	}
	public boolean isbMonitorParaSynchro() {
		return bMonitorParaSynchro;
	}
	public void setbMonitorParaSynchro(boolean bMonitorParaSynchro) {
		this.bMonitorParaSynchro = bMonitorParaSynchro;
	}
	public INLINEMODE getInlineMode() {
		return InlineMode;
	}
	public void setInlineMode(INLINEMODE inlineMode) {
		InlineMode = inlineMode;
	}
	public List<ScanCardAttachment> getListScancardAttachment() {
		return ListScancardAttachment;
	}
	public void setListScancardAttachment(
			List<ScanCardAttachment> listScancardAttachment) {
		ListScancardAttachment = listScancardAttachment;
	}
	public RECT getRtRect() {
		return rtRect;
	}
	public void setRtRect(RECT rtRect) {
		this.rtRect = rtRect;
	}
	public int getnAtlvcID() {
		return nAtlvcID;
	}
	public void setnAtlvcID(int nAtlvcID) {
		this.nAtlvcID = nAtlvcID;
	}

}
