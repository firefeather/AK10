/*
   * �ļ��� ColourRGB.java
   * ���������б�com.szaoto.ak10.common
   * �汾��Ϣ���汾��
   * ��������2013��12��28������9:45:13
   * ��Ȩ���� liangdb-szaoto
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

