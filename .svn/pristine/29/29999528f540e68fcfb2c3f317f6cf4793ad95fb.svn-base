package com.szaoto.ak10.sqlitedata;

import java.io.File;

import com.szaoto.ak10.HomePageActivity;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


//SQLiteDatabase 全局数据库变量 管理全局数据库

public class SqliteDB {
	@SuppressLint("SdCardPath")
//	private static String DbPath="/data/data/com.szaoto.ak10/databases/ak10.db";	
	private static String DbPath="/data/data/com.szaoto.ak10/files/config/ak10.db";	
    public static SQLiteDatabase m_DbDatabase = null;
	
	public SqliteDB() {		
		super();		
		OpenDB();
	}
	
	public static void CloseDB()
	{
		if (m_DbDatabase.isOpen()) {
			m_DbDatabase.close();
		}
	}
	
	public static boolean IsDBOpen()
	{
		if (m_DbDatabase.isOpen()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void OpenDB()
	{
		File file = new File(DbPath);
		m_DbDatabase=SQLiteDatabase.openOrCreateDatabase(file,null);
		
		if (m_DbDatabase==null) {	
			Log.e("DB ERROR","Open DB Error");
			return;
		}
	}
	
	//数据库接口
	
	
}

