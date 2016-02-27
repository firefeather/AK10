package com.szaoto.ak10.sqlitedata;

import android.database.Cursor;

public class CtrlDb {
	
	static String strTblName_cbt = "tb_ledctrlcfg";
	
	public CtrlDb() {
		// TODO Auto-generated constructor stub
	}

    
    //通过ID获取一条记录
    public static CtrlData GetRecordByLedidAndProfileId(int Ledid,int Profileid)
    {
    	CtrlData Data = new CtrlData();
 	
     	String strSQLString = "SELECT * FROM "+strTblName_cbt+" WHERE ledid = " + Ledid + " AND profileid = "+Profileid;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                if(c.moveToNext())
	                {              
	                	Data.nBright =  c.getInt(c.getColumnIndex("bright"));               	
	                	Data.nR =  c.getInt(c.getColumnIndex("r"));
	                	Data.nG =  c.getInt(c.getColumnIndex("g"));
	                	Data.nB =  c.getInt(c.getColumnIndex("b"));              	
	                	Data.nContrast       =  c.getInt(c.getColumnIndex("contrast"));
	                	Data.nSaturation     =  c.getInt(c.getColumnIndex("saturation"));   	
	                	Data.nColorTemp      =  c.getInt(c.getColumnIndex("colortemp"));           	
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
	
    	  return Data;
    	
    }
    
    public static void UpdateTempateParams(int Ledid,int Profileid,CtrlData tCtrlData)
    {
    	String strSqlString = "UPDATE "+ strTblName_cbt + " SET bright = "+ tCtrlData.nBright + ", colortemp = "+
        tCtrlData.nColorTemp + ", r = "+tCtrlData.nR +",g = "+tCtrlData.nG +", b = "+tCtrlData.nB+", contrast = "+tCtrlData.nContrast+", saturation = "
        +tCtrlData.nSaturation+ " WHERE ledid = "+Ledid + " AND profileid = "+Profileid;
    	
    	SqliteDB.m_DbDatabase.execSQL(strSqlString);
    }
    
   
}
