/*
   * 文件名 GrayTest.java
   * 包含类名列表com.szaoto.ak10.test
   * 版本信息，版本号
   * 创建日期2013年11月11日上午11:21:17
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.test;


import android.graphics.Color;

/*
 * 类名GrayTest
 * 作者 liangdb
 * 主要功能 灰度测试
 * 创建日期2013年11月11日
 * 修改者，修改日期，修改内容
 */
public class GrayTest {
	
	public int m_nColorType;	//颜色类型		0:红色 1:绿色 2:蓝色 3:白色
	public boolean m_bShowText;	//是否显示文字
	
	public int m_nColor;		//颜色
	
	private int m_curGray;		//当前灰度

	/**
	 * 
	 */
	public GrayTest() {
		// TODO Auto-generated constructor stub
		m_bShowText = false;
		m_nColor = Color.RED;
		m_curGray = 0;
	}
	
	public void ResetCurColor()
	{
		m_curGray = 0;
	}
	
	public void GetCurColor() 
	{
		if (255 < m_curGray) {
			m_curGray = 0;
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
		
		m_curGray ++;
	}

}
