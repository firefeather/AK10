package com.szaoto.ak10.common.CabinetData;

import java.util.ArrayList;
import java.util.List;

import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.common.Display.ColourRGB;

public class ScanCardAttachment {
	public ScanCardAttachment()
	{
		for (int i = 0; i <2; i++) {
			RelayPar rp = new RelayPar();
			Listrelaypar.add(rp);
		}
	}
	int nID;					//扫描卡ID（1～4，在同一箱体内唯一）
	int nAddress;				//扫描卡地址（编址得到，在连接在同一链路中唯一）

	CStructSingleScanCard scancard;			//扫描卡参数
 
	LinkTable slinktable;		//扫描卡走线查找
	LinkTable hublinktable;	//HUB走线查找
	LinkTable sSectionlinktable;	//扫描卡区走线查找

	//是否存在
	boolean bTHSBoard;				//温、湿度传感器板	
	boolean bMFCard;				//多功能卡
	boolean bPDCard;				//功率检测卡
	boolean bPBPDCard;				//逐点检测卡
	//是否监控
	MonitorItem monitoritem;	//监控项使能
	//继电器
	List<RelayPar> Listrelaypar = new ArrayList<RelayPar>();
//	RelayPar[]  relaypar = new RelayPar[2];		//继电器
	ColourRGB colourrgb;		//扫描卡色温
	RECT rtRect;				//扫描卡左上角及大小
	int nAtlvcID;				//连接的ATLVC
	short nPort;				//连接的端口（1～4：AK6：1U、1D、2U、2D；AK10：A、B、C、D）
	boolean bBackUp;				//是否备份返回
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
	public int getnAddress() {
		return nAddress;
	}
	public void setnAddress(int nAddress) {
		this.nAddress = nAddress;
	}
	public CStructSingleScanCard getScancard() {
		return scancard;
	}
	public void setScancard(CStructSingleScanCard scancard) {
		this.scancard = scancard;
	}
	public LinkTable getSlinktable() {
		return slinktable;
	}
	public void setSlinktable(LinkTable slinktable) {
		this.slinktable = slinktable;
	}
	public LinkTable getHublinktable() {
		return hublinktable;
	}
	public void setHublinktable(LinkTable hublinktable) {
		this.hublinktable = hublinktable;
	}
	public LinkTable getsSectionlinktable() {
		return sSectionlinktable;
	}
	public void setsSectionlinktable(LinkTable sSectionlinktable) {
		this.sSectionlinktable = sSectionlinktable;
	}
	public boolean isbTHSBoard() {
		return bTHSBoard;
	}
	public void setbTHSBoard(boolean bTHSBoard) {
		this.bTHSBoard = bTHSBoard;
	}
	public boolean isbMFCard() {
		return bMFCard;
	}
	public void setbMFCard(boolean bMFCard) {
		this.bMFCard = bMFCard;
	}
	public boolean isbPDCard() {
		return bPDCard;
	}
	public void setbPDCard(boolean bPDCard) {
		this.bPDCard = bPDCard;
	}
	public boolean isbPBPDCard() {
		return bPBPDCard;
	}
	public void setbPBPDCard(boolean bPBPDCard) {
		this.bPBPDCard = bPBPDCard;
	}
	public MonitorItem getMonitoritem() {
		return monitoritem;
	}
	public void setMonitoritem(MonitorItem monitoritem) {
		this.monitoritem = monitoritem;
	}
	public List<RelayPar> getListrelaypar() {
		return Listrelaypar;
	}
	public void setListrelaypar(List<RelayPar> listrelaypar) {
		Listrelaypar = listrelaypar;
	}
	public ColourRGB getColourrgb() {
		return colourrgb;
	}
	public void setColourrgb(ColourRGB colourrgb) {
		this.colourrgb = colourrgb;
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
	public short getnPort() {
		return nPort;
	}
	public void setnPort(short nPort) {
		this.nPort = nPort;
	}
	public boolean isbBackUp() {
		return bBackUp;
	}
	public void setbBackUp(boolean bBackUp) {
		this.bBackUp = bBackUp;
	}
}
