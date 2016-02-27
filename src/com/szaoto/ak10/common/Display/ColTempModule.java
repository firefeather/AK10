/*
   * 文件名 ColourRGB.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2013年12月28日下午9:45:13
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common.Display;

import java.util.ArrayList;
import java.util.List;

public class ColTempModule {
	public ColTempModule()
	{
		DisplayID = 1;
		ListColTemp = new ArrayList<ColourRGB>();
			for( int i=0;i<=7;i++)
			{		
				ColourRGB coltemp = new ColourRGB();
			   ListColTemp.add(coltemp);
			}
	}
		List<ColourRGB> ListColTemp;
		int DisplayID;
		public List<ColourRGB> getListColTemp() {
			return ListColTemp;
		}
		public void setListColTemp(List<ColourRGB> listColTemp) {
			ListColTemp = listColTemp;
		}
		public int getDisplayID() {
			return DisplayID;
		}
		public void setDisplayID(int displayID) {
			DisplayID = displayID;
		}


}

