package com.szaoto.ak10.sqlitedata;

import android.database.Cursor;

public class CtrlLastStationDb {

	public static String strTblName_lastctrlinfo = "tb_lastsavedledctrlinfo";
	
	public CtrlLastStationDb() {
		// TODO Auto-generated constructor stub
	}
	
	
	//通过所有记录
    public static CtrlLastStationData GetLEDRecord(int LEDID)
    {
    	CtrlLastStationData retData= new CtrlLastStationData();	
    
     	String strSQLString = "SELECT * FROM "+strTblName_lastctrlinfo + " WHERE ledid = "+LEDID;  
     	try{
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {     
	                
	                	retData.bright =  c.getInt(c.getColumnIndex("bright"));
	                	retData.colortemper = c.getInt(c.getColumnIndex("colortemper"));
	                	retData.r =  c.getInt(c.getColumnIndex("r"));
	                	retData.g =  c.getInt(c.getColumnIndex("g"));
	                	retData.b =  c.getInt(c.getColumnIndex("b"));
	                	retData.contrast =  c.getInt(c.getColumnIndex("contrast"));  
	                	retData.saturation =  c.getInt(c.getColumnIndex("saturation"));  
	                	retData.strGpName =  c.getString(c.getColumnIndex("gpname"));  
	                	
	               
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }	      
    	 
    	  return retData;
    }
    

    public static void UpdateLastBright(int bright,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET bright = "+bright+" WHERE ledid = " + LEDID;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateLastColorTemper(int colortemper,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET colortemper = "+colortemper+" WHERE ledid = " + LEDID;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateLastContrast(int contrast,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET contrast = "+contrast+" WHERE ledid = " + LEDID;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateLastSaturation(int saturation,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET saturation = "+saturation+" WHERE ledid = " + LEDID;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateLastR(int R,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET r = "+R+" WHERE ledid = " + LEDID;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    public static void UpdateLastG(int G,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET g = "+G+" WHERE ledid = " + LEDID;	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    public static void UpdateLastB(int B,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_lastctrlinfo+" SET r = "+B+" WHERE ledid = " + LEDID;	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
}
