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


public class ControlModule {
	public ControlModule()
	{		
		DisplayID = -1;
		ListModules = new ArrayList<ModuleData>();
		for(int i = 0;i<5;i++)
		{
			ModuleData temp = new ModuleData();	
			temp.setId(i);
			ListModules.add(temp);
		}
		
	}
	int DisplayID;
	List<ModuleData> ListModules ;
	public int getDisplayID() {
		return DisplayID;
	}
	public void setDisplayID(int displayID) {
		DisplayID = displayID;
	}
	public List<ModuleData> getListModules() {
		return ListModules;
	}
	public void setListModules(List<ModuleData> listModules) {
		ListModules = listModules;
	}
	
}

