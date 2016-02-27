package com.szaoto.ak10.sqlitedata;

import java.util.ArrayList;

import com.szaoto.ak10.dataaccess.returnClass;

import android.database.Cursor;

public class ChanGroup_CurrentDb{
	public ChanGroup_CurrentDb() {
		
	}
	public static String strTblName_ChGroupcurrent = "tb_chgroup_current";
	
	public static String GetCurrentName(int LEDID)
	{
		String sCurrentName = "";
     	String strSQLString = "SELECT * FROM tb_chgroup_current WHERE ledid = "+LEDID;
     	try{
	          
            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
            if(c!=null)
            {
                while(c.moveToNext())
                {              
                	sCurrentName = c.getString(c.getColumnIndex("current_groupname"));    
                }
                c.close();
            }
     	}
        catch(Exception e)
        {
              e.printStackTrace();
        }
     	return sCurrentName;    		
	}
	
	
	public static void UpdateCurrentGpName(String strNewGpName,int LEDID)
	{
		String strSQLString = "UPDATE tb_chgroup_current SET current_groupname = '"+strNewGpName+
				"' WHERE ledid = "+LEDID;	
		SqliteDB.m_DbDatabase.execSQL(strSQLString);	
	}
	
	//增加数据
    public static void AddData(String strNewGpName,int LEDID)
    {     
    	String strSQLString = "insert into "+strTblName_ChGroupcurrent + "('ledid','current_groupname')"
        +" VALUES ("+LEDID+ ",'"+ strNewGpName +"')";
    	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);		
    }
    
    public static void DeleteCurrentGpName(int LEDID)
    {  	
    	String strSQLString = "DELETE  FROM tb_chgroup_current WHERE ledid = '" + LEDID;
    	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
    }
}
