package com.szaoto.ak10.sqlitedata;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Point;

public class CabinetDB {

	static String strTblName_cbt = "tb_cbt";

	// 构造函数
	public CabinetDB() {
		super();

	}

	// 增加数据
	public static void AddData(CbtData tCbtData) {

		int Id = tCbtData.Id;
		String strType = tCbtData.strModelType;
		int Address = tCbtData.address;
		int InterfaceId = tCbtData.interfaceID;
		int offsetx = tCbtData.offsetX;
		int offsety = tCbtData.offsetY;
		int width = tCbtData.width;
		int height = tCbtData.height;
		int ledid = tCbtData.LEDid;
		int addrshowid = tCbtData.addrshowid;

		String strSQLString = "insert into "
				+ strTblName_cbt
				+ "('id','model','address','interfaceid','offsetx','offsety','width','height','ledid','addrshowid')"
				+ " VALUES (" + Id + ",'" + strType + "'," + Address + ","
				+ InterfaceId + "," + offsetx + "," + offsety + "," + width
				+ "," + height + "," + ledid + "," + addrshowid + ")";

		SqliteDB.m_DbDatabase.execSQL(strSQLString);

	}

	// 删除数据
	public static void DeleteDataById(int id, int LEDID) {
		String strSQLString = "DELETE FROM " + strTblName_cbt + " WHERE id = "
				+ id + " AND ledid = " + LEDID;
		SqliteDB.m_DbDatabase.execSQL(strSQLString);
	}

	// 删除所有数据
	public static void DeleteAllCabinetData(int LEDID) {
		String strSQLString = "DELETE  FROM " + strTblName_cbt
				+ " WHERE ledid = " + LEDID;
		SqliteDB.m_DbDatabase.execSQL(strSQLString);

	}

	// 更新数据
	public static void UpdateDataById(int id, String strKey, String strValue,
			int LEDID) {

		String strSQLString = "UPDATE " + strTblName_cbt + "SET " + strKey
				+ " = " + strValue + " WHERE id = " + id + " AND ledid = "
				+ LEDID;

		SqliteDB.m_DbDatabase.execSQL(strSQLString);

	}

	public static void UpdateInterfaceIdById(int id, int interfaceid, int LEDID) {

		String strSQLString = "UPDATE " + strTblName_cbt
				+ " SET interfaceid  = " + interfaceid + " WHERE id = " + id
				+ " AND ledid = " + LEDID;

		SqliteDB.m_DbDatabase.execSQL(strSQLString);

	}

	public static void UpdateCbtAddressById(int id, int address, int LEDID) {

		String strSQLString = "UPDATE " + strTblName_cbt + " SET address  = "
				+ address + " WHERE id = " + id + " AND ledid = " + LEDID;

		SqliteDB.m_DbDatabase.execSQL(strSQLString);

	}
	
	public static void UpdateCbtShowIdById(int id, int showid, int LEDID) {
		
		String strSQLString = "UPDATE " + strTblName_cbt + " SET addrshowid  = "
				+ showid + " WHERE id = " + id + " AND ledid = " + LEDID;
		
		SqliteDB.m_DbDatabase.execSQL(strSQLString);
		
	}

	public static void UpdateDataById(int id, String strKey, int nValue,
			int LEDID) {
		String strSQLString = "UPDATE " + strTblName_cbt + " SET " + strKey
				+ " = " + nValue + " WHERE id = " + id + " AND ledid = "
				+ LEDID;

		SqliteDB.m_DbDatabase.execSQL(strSQLString);
	}

	public static void UpdateAddressById(int id, int address, int LEDID) {
		String strSQLString = "UPDATE " + strTblName_cbt + " SET address ="
				+ address + " WHERE id = " + id + " AND ledid = " + LEDID;
		SqliteDB.m_DbDatabase.execSQL(strSQLString);
	}

	// 同时更新几项数据
	public static void UpdateDataByFileds(int id, String[] strKey,
			String[] strValue, int LEDID) {
		for (int i = 0; i < strKey.length; i++) {
			String strSQLString = "UPDATE " + strTblName_cbt + " SET "
					+ strKey[i] + " = " + strValue[i] + " WHERE Id = " + id
					+ " AND ledid = " + LEDID;
			SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}

	}

	public static void UpdateDataByFileds(int id, String[] strKey,
			int[] nValue, int LEDID) {

		for (int i = 0; i < strKey.length; i++) {
			String strSQLString = "UPDATE " + strTblName_cbt + " SET "
					+ strKey[i] + " = " + nValue[i] + " WHERE id = " + id
					+ " AND ledid = " + LEDID;
			SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}

	}

	// 更新箱体左上角坐标
	public static void UpdateCoordinate(int id, Point point, int LEDID) {
		UpdateDataByFileds(id, new String[] { "offsetx", "offsety" },
				new int[] { point.x, point.y }, LEDID);
	}

	public static ArrayList<CbtData> GetCbtRecordByIntfId(int intfId, int LEDID) {
		ArrayList<CbtData> RetArrList = new ArrayList<CbtData>();

		String strSQLString = "SELECT * FROM " + strTblName_cbt
				+ " WHERE interfaceid = " + intfId + " AND ledid = " + LEDID
				+ " ORDER BY address ASC";

		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {
					CbtData rCBTData = new CbtData();
					rCBTData.Id = c.getInt(c.getColumnIndex("id"));
					rCBTData.strModelType = c.getString(c
							.getColumnIndex("model"));
					rCBTData.address = c.getInt(c.getColumnIndex("address"));
					rCBTData.interfaceID = c.getInt(c
							.getColumnIndex("interfaceid"));
					rCBTData.offsetX = c.getInt(c.getColumnIndex("offsetx"));
					rCBTData.offsetY = c.getInt(c.getColumnIndex("offsety"));
					rCBTData.width = c.getInt(c.getColumnIndex("width"));
					rCBTData.height = c.getInt(c.getColumnIndex("height"));
					rCBTData.addrshowid = c.getInt(c.getColumnIndex("addrshowid"));

					RetArrList.add(rCBTData);
				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return RetArrList;
	}

	// 通过所有记录
	public static String GetTypeStringById(int id, int LEDID) {
		String strRet = null;

		String strSQLString = "SELECT * FROM " + strTblName_cbt
				+ " WHERE id = " + id + " AND ledid = " + LEDID;
		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {

					strRet = c.getString(c.getColumnIndex("model"));

				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;
	}

	// 通过所有型号记录
	public static ArrayList<String> GetTypeStringArray(int LEDID) {
		ArrayList<String> arrRet = new ArrayList<String>();

		String strSQLString = "SELECT DISTINCT model FROM " + strTblName_cbt
				+ " WHERE ledid = " + LEDID;
		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {
					String strTmp = c.getString(c.getColumnIndex("model"));

					arrRet.add(strTmp);

				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrRet;
	}

	// 通过所有记录
	public static ArrayList<CbtData> GetAllRecord(int LEDID) {
		ArrayList<CbtData> RetArrList = new ArrayList<CbtData>();

		String strSQLString = "SELECT * FROM " + strTblName_cbt
				+ " WHERE ledid = " + LEDID;
		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {
					CbtData retCBTData = new CbtData();
					retCBTData.Id = c.getInt(c.getColumnIndex("id"));
					retCBTData.strModelType = c.getString(c
							.getColumnIndex("model"));
					retCBTData.address = c.getInt(c.getColumnIndex("address"));
					retCBTData.interfaceID = c.getInt(c
							.getColumnIndex("interfaceid"));
					retCBTData.offsetX = c.getInt(c.getColumnIndex("offsetx"));
					retCBTData.offsetY = c.getInt(c.getColumnIndex("offsety"));
					retCBTData.width = c.getInt(c.getColumnIndex("width"));
					retCBTData.height = c.getInt(c.getColumnIndex("height"));
					retCBTData.addrshowid = c.getInt(c.getColumnIndex("addrshowid"));

					RetArrList.add(retCBTData);
				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return RetArrList;
	}

}
