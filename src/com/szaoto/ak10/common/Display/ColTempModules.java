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

public class ColTempModules {
	public ColTempModules()
	{
		ListColTempModule = new ArrayList<ColTempModule>();
	}
	List<ColTempModule> ListColTempModule;

	public List<ColTempModule> getListColTempModule() {
		return ListColTempModule;
	}
	public void setListColTempModule(List<ColTempModule> listColTempModule) {
		ListColTempModule = listColTempModule;
	}
}
