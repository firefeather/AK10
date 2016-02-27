/*
   * 文件名 DataAccessColourTem.java
   * 包含类名列表com.szaoto.ak10.dataaccess
   * 版本信息，版本号
   * 创建日期2014年1月17日上午10:50:54
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.dataaccess;

import android.content.Context;
import com.szaoto.ak10.common.MonitorConfigState.CabinetSettingState;
import com.szaoto.ak10.common.MonitorConfigState.EnvironmentSettingState;
import com.szaoto.ak10.common.MonitorConfigState.MonitorSetting;

/*
 * 类名DisplayStatus
 * 作者 liangdb
 * 主要功能 
 * 创建日期2014年1月17日
 * 修改者，修改日期，修改内容
 */
public class DataAccessMonitorConfigState extends DataAccessBase{

	public DataAccessMonitorConfigState(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/*** @param context*/
	//private final static String sFileFlagString = "acquisitioncard.xml";
	
	public boolean SaveCabinetSettingState(CabinetSettingState cabinetSettingState)
	{
		return true;
	}
	
	public boolean SaveEnvironmentSettingState(EnvironmentSettingState environmentSettingState)
	{
		return true;
	}
	
	public boolean SaveMonitorConfigState(MonitorSetting monitorSetting)
	{
		return true;
	}
	
	public MonitorSetting LoadMonitorConfigState(){
		MonitorSetting monitorSetting = null;
		
		return monitorSetting;
	}
}
