package com.szaoto.ak10.sqlitedata;

import com.szaoto.ak10.sqlitedata.SqliteDB;
import android.database.Cursor;


public class Sys_ParaDB {
	public static Sys_Para GetSys_Para() {		
		Sys_Para sysPara = new Sys_Para();
		String strSQLString = "SELECT * FROM tb_syspara";
		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {					
					sysPara.cfg3d = c.getInt(c.getColumnIndex("cfg3d"));
					sysPara.SynchronDelay = c.getInt(c.getColumnIndex("SynchronDelay"));
					sysPara.DisableScanCycle = c.getInt(c.getColumnIndex("DisableScanCycle"));
				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysPara;
	}

	public static void UpdateCfg3D(Sys_Para sysPara) {
		String strIpSQL = "UPDATE tb_syspara SET cfg3d = '"	+ sysPara.cfg3d + "'";	
		SqliteDB.m_DbDatabase.execSQL(strIpSQL);
	}
	
	public static void UpdateOtherPara(Sys_Para sysPara) {
		String strIpSQL = "UPDATE tb_syspara SET SynchronDelay = '"	+ sysPara.SynchronDelay 
				+ "', DisableScanCycle = '" + sysPara.DisableScanCycle + "'";	
		SqliteDB.m_DbDatabase.execSQL(strIpSQL);
	}
 }
	
  