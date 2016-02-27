package com.szaoto.ak10.ownerdraw;

import android.graphics.RectF;

import com.szaoto.ak10.util.StackManager;

public class InterfaceViewObj extends BasicViewObj {

	public StackManager m_positionStackManager = new StackManager();
	private ChannelViewObj mParentChan;
	private int mParentID;
	private int mSlotID;
	private byte[] mMacAddress;

	public float xMoveRela;
	public float yMoveRela;

	final   int reflectSpace = 30 ;
	
	
	//定义相应的8个区域
	private RectF HotSpotRectlt = new RectF();
	private RectF HotSpotRectlm = new RectF();
	private RectF HotSpotRectlb = new RectF();
	private RectF HotSpotRecttm = new RectF();
	private RectF HotSpotRectbm = new RectF();
	private RectF HotSpotRectrt = new RectF();
	private RectF HotSpotRectrm = new RectF();
	private RectF HotSpotRectrb = new RectF();
	
	
	
	public InterfaceViewObj() {
		super();	
	}


	/**
	 * @return the mParentChan
	 */
	public ChannelViewObj getmParentChan() {
		return mParentChan;
	}


	/**
	 * @param mParentChan the mParentChan to set
	 */
	public void setmParentChan(ChannelViewObj mParentChan) {
		this.mParentChan = mParentChan;
	}


	/**
	 * @return the mParentID
	 */
	public int getmParentID() {
		return mParentID;
	}


	/**
	 * @param mParentID the mParentID to set
	 */
	public void setmParentID(int mParentID) {
		this.mParentID = mParentID;
	}


	/**
	 * @return the mSlotID
	 */
	public int getmSlotID() {
		return mSlotID;
	}


	/**
	 * @param mSlotID the mSlotID to set
	 */
	public void setmSlotID(int mSlotID) {
		this.mSlotID = mSlotID;
	}


	/**
	 * @return the mMacAddress
	 */
	public byte[] getmMacAddress() {
		return mMacAddress;
	}


	/**
	 * @param mMacAddress the mMacAddress to set
	 */
	public void setmMacAddress(byte[] mMacAddress) {
		this.mMacAddress = mMacAddress;
	}




	/**
	 * @return the hotSpotRectlt
	 */
	public RectF getHotSpotRectlt() {
		
		HotSpotRectlt.left = m_leftCustomView-50;
		HotSpotRectlt.right = m_leftCustomView+50;
		HotSpotRectlt.top = m_topCustomView-50;
		HotSpotRectlt.bottom = m_topCustomView+50;
		
		return HotSpotRectlt;
	}


	/**
	 * @param hotSpotRectlt the hotSpotRectlt to set
	 */
	public void setHotSpotRectlt(RectF hotSpotRectlt) {
		HotSpotRectlt = hotSpotRectlt;
	}


	/**
	 * @return the hotSpotRectlm
	 */
	public RectF getHotSpotRectlm() {
		
		HotSpotRectlm.left = m_leftCustomView-reflectSpace;
		HotSpotRectlm.right = m_leftCustomView+reflectSpace;
		HotSpotRectlm.top = m_topCustomView+m_HeightZoomed/2-reflectSpace;
		HotSpotRectlm.bottom = m_topCustomView+m_HeightZoomed/2+reflectSpace;
		
		return HotSpotRectlm;
	}


	/**
	 * @param hotSpotRectlm the hotSpotRectlm to set
	 */
	public void setHotSpotRectlm(RectF hotSpotRectlm) {
	
		HotSpotRectlm = hotSpotRectlm;
	}


	/**
	 * @return the hotSpotRectlb
	 */
	public RectF getHotSpotRectlb() {
		
		HotSpotRectlb.left = m_leftCustomView-reflectSpace;
		HotSpotRectlb.right = m_leftCustomView+reflectSpace;
		HotSpotRectlb.top = m_topCustomView-reflectSpace;
		HotSpotRectlb.bottom = m_topCustomView+reflectSpace;
		
		return HotSpotRectlb;
	}


	/**
	 * @param hotSpotRectlb the hotSpotRectlb to set
	 */
	public void setHotSpotRectlb(RectF hotSpotRectlb) {
		HotSpotRectlb = hotSpotRectlb;
	}


	/**
	 * @return the hotSpotRecttm
	 */
	public RectF getHotSpotRecttm() {
		
		HotSpotRecttm.left = m_leftCustomView+m_WidthZoomed/2-reflectSpace;
		HotSpotRecttm.right = m_leftCustomView+m_WidthZoomed/2+reflectSpace;
		HotSpotRecttm.top = m_topCustomView-reflectSpace;
		HotSpotRecttm.bottom = m_topCustomView+reflectSpace;
		
		return HotSpotRecttm;
	}


	/**
	 * @param hotSpotRecttm the hotSpotRecttm to set
	 */
	public void setHotSpotRecttm(RectF hotSpotRecttm) {
		HotSpotRecttm = hotSpotRecttm;
	}


	/**
	 * @return the hotSpotRectbm
	 */
	public RectF getHotSpotRectbm() {
		
		HotSpotRectbm.left = m_leftCustomView+m_WidthZoomed/2-reflectSpace;
		HotSpotRectbm.right = m_leftCustomView+m_WidthZoomed/2+reflectSpace;
		HotSpotRectbm.top = m_topCustomView+m_HeightZoomed-reflectSpace;
		HotSpotRectbm.bottom = m_topCustomView+m_HeightZoomed+reflectSpace;

		return HotSpotRectbm;
	}


	/**
	 * @param hotSpotRectbm the hotSpotRectbm to set
	 */
	public void setHotSpotRectbm(RectF hotSpotRectbm) {
		HotSpotRectbm = hotSpotRectbm;
	}


	/**
	 * @return the hotSpotRectrt
	 */
	public RectF getHotSpotRectrt() {
		
		HotSpotRectrt.left = m_leftCustomView+m_WidthZoomed-reflectSpace;
		HotSpotRectrt.right = m_leftCustomView+m_WidthZoomed+reflectSpace;
		HotSpotRectrt.top = m_topCustomView-reflectSpace;
		HotSpotRectrt.bottom = m_topCustomView+reflectSpace;
		
		return HotSpotRectrt;
	}


	/**
	 * @param hotSpotRectrt the hotSpotRectrt to set
	 */
	public void setHotSpotRectrt(RectF hotSpotRectrt) {
		HotSpotRectrt = hotSpotRectrt;
	}


	/**
	 * @return the hotSpotRectrm
	 */
	public RectF getHotSpotRectrm() {
		
		HotSpotRectrm.left = m_leftCustomView+m_WidthZoomed-reflectSpace;
		HotSpotRectrm.right = m_leftCustomView+m_WidthZoomed+reflectSpace;
		HotSpotRectrm.top = m_topCustomView+m_HeightZoomed/2-reflectSpace;
		HotSpotRectrm.bottom = m_topCustomView+m_HeightZoomed/2+reflectSpace;
		
		return HotSpotRectrm;
	}


	/**
	 * @param hotSpotRectrm the hotSpotRectrm to set
	 */
	public void setHotSpotRectrm(RectF hotSpotRectrm) {
		HotSpotRectrm = hotSpotRectrm;
	}


	/**
	 * @return the hotSpotRectrb
	 */
	public RectF getHotSpotRectrb() {
		
		HotSpotRectrb.left = m_leftCustomView+m_WidthZoomed-reflectSpace;
		HotSpotRectrb.right = m_leftCustomView+m_WidthZoomed+reflectSpace;
		HotSpotRectrb.top = m_topCustomView+m_HeightZoomed-reflectSpace;
		HotSpotRectrb.bottom = m_topCustomView+m_HeightZoomed+reflectSpace;
		
		return HotSpotRectrb;
	}


	/**
	 * @param hotSpotRectrb the hotSpotRectrb to set
	 */
	public void setHotSpotRectrb(RectF hotSpotRectrb) {
		HotSpotRectrb = hotSpotRectrb;
	}


	public float getxMoveRela() {
		return xMoveRela;
	}


	public void setxMoveRela(float xMoveRela) {
		this.xMoveRela = xMoveRela;
	}


	public float getyMoveRela() {
		return yMoveRela;
	}


	public void setyMoveRela(float yMoveRela) {
		this.yMoveRela = yMoveRela;
	}

}
