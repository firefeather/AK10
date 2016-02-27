package com.szaoto.ak10.common.CabinetData;

import java.util.List;

import com.szaoto.ak10.common.RECT;

/**
 * 类名：ToolFragment
 * 功能：工具栏区域
 * 作者：  zhangsj
 * 创建日期：2014/07/02
 * 修改者,修改日期，修改内容
 * 
 */
public class CabinetInformation{
	int nID;					        //箱体ID
	int nSeriesID;				//箱体所属系列的ID
//	String sSeriesName;         //箱体所属系列的名称
	String sName;				//箱体型号
	boolean bRead;					//已读取标示

	String sPhoto;				//箱体照片
	int nAddress;				//箱体在连线图中的地址（编址得到，在连接在同一链路中唯一）

	short nScanCardCount;				//扫描卡个数（1～4）
	boolean bScanCardParaSynchro;		//扫描卡参数是否同步修改
	boolean bMonitorParaSynchro;		//监控参数是否同步修改
	INLINEMODE InlineMode;		//入线方式
	List<ScanCardAttachment> ListScancardAttachment;
//	map<short,ScanCardAttachment> mScancardAttachment;	//扫描卡及相关附件表
	RECT rtRect;				//箱体左上角及大小
	int nAtlvcID;				//连接的ATLVC
	

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
