package com.szaoto.ak10.ownerdraw;

import java.util.ArrayList;

import com.szaoto.ak10.util.StackManager;

public class ChannelViewObj extends BasicViewObj{

	private int mSlotID;
	private byte[] mMacAddress;
	
	private int ChPortId;
	private ArrayList<InterfaceViewObj> m_ArrayChildViewObj = new ArrayList<InterfaceViewObj>();
    public StackManager m_positionStackManager = new StackManager();
    
    
	private int m_nVideoChPort;//对应的视频通道号
	
	public ChannelViewObj(){
		super();
	}


	public int getmSlotID() {
		return mSlotID;
	}


	public void setmSlotID(int mSlotID) {
		this.mSlotID = mSlotID;
	}


	public byte[] getmMacAddress() {
		return mMacAddress;
	}


	public void setmMacAddress(byte[] mMacAddress) {
		this.mMacAddress = mMacAddress;
	}


	public ArrayList<InterfaceViewObj> getM_ArrayChildViewObj() {
		return m_ArrayChildViewObj;
	}


	public void setM_ArrayChildViewObj(ArrayList<InterfaceViewObj> m_ArrayChildViewObj) {
		this.m_ArrayChildViewObj = m_ArrayChildViewObj;
	}


	public void setsResolution(int nWidth, int nHeight) {
		m_width=nWidth;
		m_height=nHeight;	
	}


    public void UpdateOrgCoordinate(float fleft,float fTop){
    	
    	m_leftOrg = fleft;
    	m_topOrg = fTop;
    	
    }


	public int getM_nVideoChPort() {
		return m_nVideoChPort;
	}


	public void setM_nVideoChPort(int m_nVideoChPort) {
		this.m_nVideoChPort = m_nVideoChPort;
	}


	public int getChPortId() {
		return ChPortId;
	}


	public void setChPortId(int chPortId) {
		ChPortId = chPortId;
	}


	
	

}
