/*
   * 文件名 GammaData.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2014年1月10日下午7:30:39
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common;

import com.szaoto.ak10.util.UtilFun;


/*
 * 类名GammaData
 * 作者 zhangjj
 * 主要功能 GAMMA 数据
 * 创建日期2014年1月10日
 * 修改者，修改日期，修改内容
 */
public class SystemConfig {

	private  int UISettingTime;
	private  String SystemPassword;	
	private boolean SystemLisencestate;//1是打开0是关闭
	private int SystemLisencetype;//=1是外部视频源，=0是U盘视频
	private String DiagnoseConfig ;//保存按键的状态
	public byte[] getDiagnoseConfigByte()
	{
		if (DiagnoseConfig != null) {
			return UtilFun.hexStringSplit2Bytes(DiagnoseConfig, "-");
		}else {
			return  null;
		}
	//	return null;
	}
	// <SystemLisencetype>1</SystemLisencetype>
	// <SystemLisencestate>
	public  SystemConfig() {
		// TODO Auto-generated constructor stub
	}

	public String getSystemPassword() {
		return SystemPassword;
	}
	public void setSystemPassword(String systemPassword) {
		SystemPassword = systemPassword;
	}
	public int getUISettingTime() {
		return UISettingTime;
	}
	public void setUISettingTime(int uISettingTime) {
		UISettingTime = uISettingTime;
	}
	
	public boolean getSystemLisencestate() {
		return SystemLisencestate;
	}
	public void setSystemLisencestate(boolean systemLisencestate) {
		SystemLisencestate = systemLisencestate;
	}
	public int getSystemLisencetype() {
		return SystemLisencetype;
	}
	public void setSystemLisencetype(int systemLisencetype) {
		SystemLisencetype = systemLisencetype;
	}

	public String getDiagnoseConfig() {
		return DiagnoseConfig;
	}

	public void setDiagnoseConfig(String diagnoseConfig) {
		DiagnoseConfig = diagnoseConfig;
	}

}
