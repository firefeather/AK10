package com.szaoto.ak10.ownerdraw;

import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.util.UtilFun;




public class CabinetViewObj extends BasicViewObj{

    private int m_ParentInterfId;
	private int m_AddressId;
	private int m_ShowAddressId;
	private String strTypeString;
	private RECT m_OrgRect = new RECT();
	
	private float xMoveRela;
	private float yMoveRela;
    
	public CabinetViewObj() {
		super();
		
		setmBackGroundColor(0);
	}

	/**
	 * @return the m_ParentInterfId
	 */
	public int getM_ParentInterfId() {
		return m_ParentInterfId;
	}

	/**
	 * @param m_ParentInterfId the m_ParentInterfId to set
	 */
	public void setM_ParentInterfId(int m_ParentInterfId) {
		this.m_ParentInterfId = m_ParentInterfId;
	}

	/**
	 * @return the m_AddressId
	 */
	public int getM_AddressId() {
		return m_AddressId;
	}

	/**
	 * @param m_AddressId the m_AddressId to set
	 */
	public void setM_AddressId(int m_AddressId) {
		this.m_AddressId = m_AddressId;
	}

	/**
	 * @return the m_OrgRect
	 */
	public RECT getM_OrgRect() {
		
		m_OrgRect.left =UtilFun.f2i(m_leftOrg);
		m_OrgRect.top =UtilFun.f2i(m_topOrg);
		m_OrgRect.right =UtilFun.f2i(m_leftOrg+m_width);
		m_OrgRect.bottom =UtilFun.f2i(m_topOrg+m_height);
		
		return m_OrgRect;
	}

	/**
	 * @param m_OrgRect the m_OrgRect to set
	 */
	public void setM_OrgRect(RECT m_OrgRect) {
		this.m_OrgRect = m_OrgRect;
	}

	/**
	 * @return the strTypeString
	 */
	public String getStrTypeString() {
		return strTypeString;
	}

	/**
	 * @param strTypeString the strTypeString to set
	 */
	public void setStrTypeString(String strTypeString) {
		this.strTypeString = strTypeString;
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

	public int getM_ShowAddressId() {
		return m_ShowAddressId;
	}

	public void setM_ShowAddressId(int m_ShowAddressId) {
		this.m_ShowAddressId = m_ShowAddressId;
	}

	
	
}
