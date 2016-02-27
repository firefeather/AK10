/*
   * �ļ��� GridTest.java
   * ���������б�com.szaoto.ak10.test
   * �汾��Ϣ���汾��
   * ��������2013��11��11������11:22:11
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.test;


import android.graphics.Color;

/*
 * ����GridTest
 * ���� liangdb
 * ��Ҫ���� �������
 * ��������2013��11��11��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class GridTest {
	
	
	public int m_nSpace;					//���
	
	public boolean m_bHorizontalLine;		//����
	public boolean m_bVerticalLine;			//����
	public boolean m_bLeftDiagonalLine;		//��б��
	public boolean m_bRightDiagonalLine;	//��б��
	
	public int m_nColor[];					//��ǰ��ɫ,��ɫ�任
	public int m_nSPos;						//��Կ�ʼλ��

	public GridTest() {
		// TODO Auto-generated constructor stub
		m_nSpace = 20;
		
		m_bHorizontalLine = true;
		m_bVerticalLine = true;
		m_bLeftDiagonalLine = false;
		m_bRightDiagonalLine = false;
		
		//m_nColor = Color.WHITE;
		m_nColor = new int[3];
		m_nColor[0] = Color.WHITE;
		m_nColor[1] = Color.RED;
		m_nColor[2] = Color.BLUE;
		m_nSPos = 0;
	}
}