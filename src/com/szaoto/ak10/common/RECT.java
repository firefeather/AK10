/*
   * 文件名 RECT.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2014年4月1日下午7:37:29
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common;

import com.szaoto.ak10.util.UtilFun;

import android.graphics.Point;

/*
 * 类名RECT
 * 作者 liangdb
 * 主要功能
 * 创建日期2014年4月1日
 * 修改者，修改日期，修改内容
 */
public class RECT {
	
	public RECT(int tX, int tY, int tX1, int tY1)
	{
		  left = tX;
		  top =tY;
		  right =tX1;
		  bottom =tY1;
	}
	public RECT() {
		// TODO Auto-generated constructor stub
	}
	public RECT(float tX, float tY, float tX1, float tY1) {
		
		  left =UtilFun.f2i(tX);
		  top =UtilFun.f2i(tY);
		  right =UtilFun.f2i(tX1);
		  bottom =UtilFun.f2i(tY1);
	}
	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}
	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}
	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}
	/**
	 * @return the right
	 */
	public int getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(int right) {
		this.right = right;
	}
	/**
	 * @return the bottom
	 */
	public int getBottom() {
		return bottom;
	}
	/**
	 * @param bottom the bottom to set
	 */
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	
	
	//判断点是否在这个矩形内
	public boolean PtInRect(Point ptTemp){
		
		int tX=ptTemp.x;
		int tY=ptTemp.y;
		
		if ((tX>=left)&&(tX<=right)&&(tY>=top)&&(tY<=bottom)) {
			return true;
		}
		else{
			return false;
		}
		
	}
	
	//判断矩形是否在矩形内
	public boolean RectInRect(RECT rcChild){
		
		if ((rcChild.getRight()<=right)&&rcChild.getLeft()>=left&&(rcChild.getTop()>=top)
				&&(rcChild.bottom<=bottom)){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	 public int left;
	 public int top;
	 public int right;
	 public int bottom;
	 
	public void set(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		setLeft(l);
		setTop(j);
		setRight(k);
		setBottom(l);
	}
}