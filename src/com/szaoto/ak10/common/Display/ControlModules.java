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


public class ControlModules {
	public ControlModules()
	{
		listControlModules = new ArrayList<ControlModule>();
	}
	List<ControlModule> listControlModules;
	public List<ControlModule> getListControlModules() {
		return listControlModules;
	}
	public void setListControlModules(List<ControlModule> listControlModules) {
		this.listControlModules = listControlModules;
	}
}

