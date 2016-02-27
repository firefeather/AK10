/*
   * 文件名 RibbonTest.java
   * 包含类名列表com.szaoto.ak10.test
   * 版本信息，版本号
   * 创建日期2013年11月11日上午11:21:48
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.test;

import android.graphics.Color;

/*
 * 类名RibbonTest
 * 作者 liangdb
 * 主要功能 色条测试
 * 创建日期2013年11月11日
 * 修改者，修改日期，修改内容
 */
public class RibbonTest {
	
	public int m_nColorType;	//颜色类型		0:红色渐变 1:绿色渐变 2:蓝色渐变 3:白色渐变
	public int m_nHorV;			//横向或者纵向	1:横向 2：纵向
	
	public int m_nWidthHeight;	//色条宽、高度
	public int m_nStartGray;	//起始灰度
	public int m_nSingleGray;	//单灰度像素 
	
	public int m_nColor;		//当前颜色
	
	public int m_nSPos;			//相对开始位置
	//
	private int m_nHWSize;		//单色色条宽高标识
	private int m_curGray;		//当前灰度
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
	 * 概述 
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
