package com.szaoto.ak10.ownerdraw;

//���ƶ���
public class BasicViewObj implements Cloneable{

	//ʵ������
	public float m_leftOrg;
	public float m_topOrg;
	
	//���ź�����
	public float m_leftZoomed;
	public float m_topZoomed;
	public float m_WidthZoomed;
	public float m_HeightZoomed;
	
	//���CustomView����	
	public float m_leftCustomView;
	public float m_topCustomView;
	
	public float m_width;
	public float m_height;
	public int m_nCfg3d;
	
	private int mBasicViewID;

	private int   m_SelPrior;
	
	private boolean m_bSel=false;
	
	private boolean m_bVisible=true;
	
	//private RectF  m_RectF=new  RectF();

	private String m_strLable;//��ʾ��obj�ϵı�ǩ	
	private int mBackGroundColor;//����ɫ
	private int mFrontColor;//ǰ��ɫ
	private boolean m_bLableVisible = true;//�Ƿ���ʾ��ǩ
	private String m_strResolution;//�ֱ���
	
	public float getM_width() {
		return m_width;
	}

	public void setM_width(float m_width) {
		this.m_width = m_width;
	}

	public float getM_height() {
		return m_height;
	}

	public void setM_height(float m_height) {
		this.m_height = m_height;
	}



	public BasicViewObj()
	{
		super();
	}
	
	
	public int getmBasicViewID() {
		return mBasicViewID;
	}

	public void setmBasicViewID(int mID) {
		this.mBasicViewID = mID;
	}

	public String getM_strLable() {
		return m_strLable;
	}

	public void setM_strLable(String m_strLable) {
		this.m_strLable = m_strLable;
	}

	public int getmBackGroundColor() {
		return mBackGroundColor;
	}

	public void setmBackGroundColor(int mBackGroundColor) {
		this.mBackGroundColor = mBackGroundColor;
	}

	public int getmFrontColor() {
		return mFrontColor;
	}

	public void setmFrontColor(int mFrontColor) {
		this.mFrontColor = mFrontColor;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// ʵ��clone����
		return super.clone();
	}


	public String getM_strResolution() {
		return m_strResolution;
	}

	public void setM_strResolution(String m_strResolution) {
		this.m_strResolution = m_strResolution;
	}

	public int getM_SelPrior() {
		return m_SelPrior;
	}

	public void setM_SelPrior(int m_SelPrior) {
		this.m_SelPrior = m_SelPrior;
	}

	public boolean isSel() {
		return m_bSel;
	}

	public void setSel(boolean m_bSel) {
		this.m_bSel = m_bSel;
	}

	/**
	 * @return the m_bVisible
	 */
	public boolean isM_bVisible() {
		return m_bVisible;
	}

	/**
	 * @param m_bVisible the m_bVisible to set
	 */
	public void setM_bVisible(boolean m_bVisible) {
		this.m_bVisible = m_bVisible;
	}

	/**
	 * @return the m_bLableVisible
	 */
	public boolean isM_bLableVisible() {
		return m_bLableVisible;
	}

	/**
	 * @param m_bLableVisible the m_bLableVisible to set
	 */
	public void setM_bLableVisible(boolean m_bLableVisible) {
		this.m_bLableVisible = m_bLableVisible;
	}

	
	
}