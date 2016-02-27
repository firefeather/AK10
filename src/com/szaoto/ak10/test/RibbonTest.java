/*
   * �ļ��� RibbonTest.java
   * ���������б�com.szaoto.ak10.test
   * �汾��Ϣ���汾��
   * ��������2013��11��11������11:21:48
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.test;

import android.graphics.Color;

/*
 * ����RibbonTest
 * ���� liangdb
 * ��Ҫ���� ɫ������
 * ��������2013��11��11��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class RibbonTest {
	
	public int m_nColorType;	//��ɫ����		0:��ɫ���� 1:��ɫ���� 2:��ɫ���� 3:��ɫ����
	public int m_nHorV;			//�����������	1:���� 2������
	
	public int m_nWidthHeight;	//ɫ�����߶�
	public int m_nStartGray;	//��ʼ�Ҷ�
	public int m_nSingleGray;	//���Ҷ����� 
	
	public int m_nColor;		//��ǰ��ɫ
	
	public int m_nSPos;			//��Կ�ʼλ��
	//
	private int m_nHWSize;		//��ɫɫ����߱�ʶ
	private int m_curGray;		//��ǰ�Ҷ�
	/**
	 * 
	 */
	public RibbonTest() {
		
		m_nHorV = 1;
		m_nWidthHeight = 200;
		m_nStartGray = 0;
		m_nSingleGray = 1;
		m_nSPos = 0;
		
		m_nHWSize = 0;
		m_curGray = 0;
	}
	public void ResetCurColor()
	{
		m_curGray = m_nStartGray;
		m_nHWSize = 0;
	}
	/*
	 * ���� 
	 * @param 
	 * @return 
	 * @throws IOException 
	 * @throws NullPointerException 
	 */

	public void GetCurColor() {
		if(0 == m_nHWSize || m_nHWSize >= m_nWidthHeight)
		{
			m_nHWSize = 0;
			m_curGray = m_nStartGray;
		}
		else
		{
			if (0 == m_nHWSize % m_nSingleGray)
			{
				m_curGray ++;
			}
		}

		switch (m_nColorType)
		{
		case 0 :
			m_nColor = Color.rgb(m_curGray, 0, 0);
			break;
		case 1 :
			m_nColor = Color.rgb(0, m_curGray, 0);
			break;
		case 2 :
			m_nColor = Color.rgb(0, 0, m_curGray);
			break;
		case 3 :
			m_nColor = Color.rgb(m_curGray, m_curGray, m_curGray);
			break;
		default:
			break;
		}
		m_nHWSize ++;
	}

}
