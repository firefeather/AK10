package com.szaoto.ak10.ownerdraw;

import java.util.ArrayList;

import android.graphics.PointF;

public class ObjLog {

	final int LOG_MOVE =0;
	final int LOG_SCROLL =1;
	final int LOG_SIZE=2;
	final int LOG_CREATE=3;
	final int LOG_CREATE_GROUP=4;
	final int LOG_DELETE=5;
	final int LOG_DELETE_GROUP=6;
	
   private	BasicViewObj m_BasicViewObjFrom = new BasicViewObj();
   private	BasicViewObj m_BasicViewObjTo = new BasicViewObj();
	
   private	float m_fFactorFrom;
   private	float m_fFactorTo;

   private  PointF m_ptVpFrom= new PointF();
   private  PointF m_ptVpTo= new PointF();
   
   private ArrayList<BasicViewObj> basicViewObjArrFrom = new ArrayList<BasicViewObj>();
   private ArrayList<BasicViewObj> basicViewObjArrTo  = new ArrayList<BasicViewObj>();
   
   private  int     m_ActionMode;
   
   public ObjLog(){
	   
   }
   
	//对象改变
   public ObjLog(BasicViewObj basicViewObjFrom,BasicViewObj basicViewObjTo,int nActionMode)
	{
		
	}
   
   public ObjLog(ArrayList<BasicViewObj> basicViewObjArrFrom,ArrayList<BasicViewObj> basicViewObjArrTo,int nActionMode)
	{
		
	}

	//缩放大小改变
   public ObjLog(float fFrom,float fTo,int nActionMode)
	{
		
	}
	
	//viewPort改变
   public ObjLog(PointF ptFrom,PointF ptTo,int nActionMode)
	{
		
	}

	/**
	 * @return the m_BasicViewObjFrom
	 */
	public BasicViewObj getM_BasicViewObjFrom() {
		return m_BasicViewObjFrom;
	}

	/**
	 * @param m_BasicViewObjFrom the m_BasicViewObjFrom to set
	 */
	public void setM_BasicViewObjFrom(BasicViewObj m_BasicViewObjFrom) {
		this.m_BasicViewObjFrom = m_BasicViewObjFrom;
	}

	/**
	 * @return the m_BasicViewObjTo
	 */
	public BasicViewObj getM_BasicViewObjTo() {
		return m_BasicViewObjTo;
	}

	/**
	 * @param m_BasicViewObjTo the m_BasicViewObjTo to set
	 */
	public void setM_BasicViewObjTo(BasicViewObj m_BasicViewObjTo) {
		this.m_BasicViewObjTo = m_BasicViewObjTo;
	}

	/**
	 * @return the m_fFactorFrom
	 */
	public float getM_fFactorFrom() {
		return m_fFactorFrom;
	}

	/**
	 * @param m_fFactorFrom the m_fFactorFrom to set
	 */
	public void setM_fFactorFrom(float m_fFactorFrom) {
		this.m_fFactorFrom = m_fFactorFrom;
	}

	/**
	 * @return the m_fFactorTo
	 */
	public float getM_fFactorTo() {
		return m_fFactorTo;
	}

	/**
	 * @param m_fFactorTo the m_fFactorTo to set
	 */
	public void setM_fFactorTo(float m_fFactorTo) {
		this.m_fFactorTo = m_fFactorTo;
	}

	/**
	 * @return the m_ptVpFrom
	 */
	public PointF getM_ptVpFrom() {
		return m_ptVpFrom;
	}

	/**
	 * @param m_ptVpFrom the m_ptVpFrom to set
	 */
	public void setM_ptVpFrom(PointF m_ptVpFrom) {
		this.m_ptVpFrom = m_ptVpFrom;
	}

	/**
	 * @return the m_ptVpTo
	 */
	public PointF getM_ptVpTo() {
		return m_ptVpTo;
	}

	/**
	 * @param m_ptVpTo the m_ptVpTo to set
	 */
	public void setM_ptVpTo(PointF m_ptVpTo) {
		this.m_ptVpTo = m_ptVpTo;
	}

	/**
	 * @return the m_ActionMode
	 */
	public int getM_ActionMode() {
		return m_ActionMode;
	}

	/**
	 * @param m_ActionMode the m_ActionMode to set
	 */
	public void setM_ActionMode(int m_ActionMode) {
		this.m_ActionMode = m_ActionMode;
	}

	/**
	 * @return the basicViewObjArrFrom
	 */
	public ArrayList<BasicViewObj> getBasicViewObjArrFrom() {
		return basicViewObjArrFrom;
	}

	/**
	 * @param basicViewObjArrFrom the basicViewObjArrFrom to set
	 */
	public void setBasicViewObjArrFrom(ArrayList<BasicViewObj> basicViewObjArrFrom) {
		this.basicViewObjArrFrom = basicViewObjArrFrom;
	}

	/**
	 * @return the basicViewObjArrTo
	 */
	public ArrayList<BasicViewObj> getBasicViewObjArrTo() {
		return basicViewObjArrTo;
	}

	/**
	 * @param basicViewObjArrTo the basicViewObjArrTo to set
	 */
	public void setBasicViewObjArrTo(ArrayList<BasicViewObj> basicViewObjArrTo) {
		this.basicViewObjArrTo = basicViewObjArrTo;
	}



	
	
}
