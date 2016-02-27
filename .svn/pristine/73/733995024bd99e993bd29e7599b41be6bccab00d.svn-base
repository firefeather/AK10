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

