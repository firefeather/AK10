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

