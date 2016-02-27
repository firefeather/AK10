/*
   * �ļ��� DataAccessColourTem.java
   * ���������б�com.szaoto.ak10.dataaccess
   * �汾��Ϣ���汾��
   * ��������2014��1��17������10:50:54
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.dataaccess;

import android.content.Context;
import com.szaoto.ak10.common.TestConfigState.GrayState;
import com.szaoto.ak10.common.TestConfigState.GridState;
import com.szaoto.ak10.common.TestConfigState.RibbonState;
import com.szaoto.ak10.common.TestConfigState.SpotState;
import com.szaoto.ak10.common.TestConfigState.TestConfig;
import com.szaoto.ak10.common.TestConfigState.WindowSetting;

/*
 * ����DisplayStatus
 * ���� liangdb
 * ��Ҫ���� 
 * ��������2014��1��17��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class DataAccessTestConfigState extends DataAccessBase{

	public DataAccessTestConfigState(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param context
	 */

	public boolean SaveWindowType(boolean isfullscreen )
	{
		return true;
	}
	
	public boolean SaveTestMoudule(int  type )
	{
		return true;
	}
	
	public boolean SaveWindowSetting(WindowSetting windowSetting )
	{
		return true;
	}

	public boolean SaveGrayState(GrayState grayState )
	{
		return true;
	}
	
	public boolean SaveGridState(GridState gridState)
	{
		return true;
	}
	
	public boolean SaveRibbonState(RibbonState ribbonstate)
	{
		return true;
	}
	
	public boolean SaveSpotState(SpotState spotstate)
	{
		return true;
	}
	
	public TestConfig LoadTestConfigState(){
		TestConfig testConfig = null;
		
		return testConfig;
	}
}
