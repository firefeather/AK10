package com.szaoto.ak10.sqlitedata;

import android.database.Cursor;

public class ColorTemperDb {

public static String strTblName_ColorTemper = "tb_colortemper";
	
	public ColorTemperDb() {
		// TODO Auto-generated constructor stub
	}
	
	
	//通过所有记录
    public static ColorTemperData GetTmperRecord(int ColorTemper, int LEDID)
    {
    	ColorTemperData retData= new ColorTemperData();	
    
     	String strSQLString = "SELECT * FROM "+strTblName_ColorTemper + " WHERE ledid = "+LEDID + " AND m_nColorTemperature = "
    			+ColorTemper;	 
     	try{
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {     
	                
	                	retData.nEnable =  c.getInt(c.getColumnIndex("m_bEnable"));
	                	retData.nRed = c.getInt(c.getColumnIndex("nRed"));
	                	retData.nGreen = c.getInt(c.getColumnIndex("nGreen"));
	                	retData.nBlue = c.getInt(c.getColumnIndex("nBlue"));
	                	
	                	retData.nICRed = c.getInt(c.getColumnIndex("nICRed"));
	                	retData.nICGreen = c.getInt(c.getColumnIndex("nICGreen"));
	                	retData.nICBlue = c.getInt(c.getColumnIndex("nICBlue"));
	                	
	                	retData.nRedLow = c.getInt(c.getColumnIndex("nRedLow"));
	                	retData.nGreenLow = c.getInt(c.getColumnIndex("nGreenLow"));
	                	retData.nBlueLow = c.getInt(c.getColumnIndex("nBlueLow"));
	                	
	                	retData.nICRedLow = c.getInt(c.getColumnIndex("nICRedLow"));
	                	retData.nICGreenLow = c.getInt(c.getColumnIndex("nICGreenLow"));
	                	retData.nICBlueLow = c.getInt(c.getColumnIndex("nICBlueLow"));
	                	
	                	retData.nICRed1 = c.getInt(c.getColumnIndex("nICRed1"));
	                	retData.nICGreen1 = c.getInt(c.getColumnIndex("nICGreen1"));
	                	retData.nICBlue1 = c.getInt(c.getColumnIndex("nICBlue1"));
	                	
	                	retData.nICRed2 = c.getInt(c.getColumnIndex("nICRed2"));
	                	retData.nICGreen2 = c.getInt(c.getColumnIndex("nICGreen2"));
	                	retData.nICBlue2 = c.getInt(c.getColumnIndex("nICBlue2"));

	                	retData.nICRed6 = c.getInt(c.getColumnIndex("nICRed6"));
	                	retData.nICGreen6 = c.getInt(c.getColumnIndex("nICGreen6"));
	                	retData.nICBlue6 = c.getInt(c.getColumnIndex("nICBlue6"));
	                	
	                	retData.nICRed7 = c.getInt(c.getColumnIndex("nICRed7"));
	                	retData.nICGreen7 = c.getInt(c.getColumnIndex("nICGreen7"));
	                	retData.nICBlue7 = c.getInt(c.getColumnIndex("nICBlue7"));
	                	
	                	retData.nICRed8 = c.getInt(c.getColumnIndex("nICRed8"));
	                	retData.nICGreen8 = c.getInt(c.getColumnIndex("nICGreen8"));
	                	retData.nICBlue8 = c.getInt(c.getColumnIndex("nICBlue8"));
	                	
	                	retData.nICRed9 = c.getInt(c.getColumnIndex("nICRed9"));
	                	retData.nICGreen9 = c.getInt(c.getColumnIndex("nICGreen9"));
	                	retData.nICBlue9 = c.getInt(c.getColumnIndex("nICBlue9"));
	                	
	                	retData.m_bGainEnable_0 = c.getInt(c.getColumnIndex("m_bGainEnable_0"));
	                	retData.m_bGainEnable_1 = c.getInt(c.getColumnIndex("m_bGainEnable_1"));
	                	retData.m_bGainEnable_2 = c.getInt(c.getColumnIndex("m_bGainEnable_2"));
	                	retData.m_bGainEnable_3 = c.getInt(c.getColumnIndex("m_bGainEnable_3"));
	                	retData.m_bGainEnable_4 = c.getInt(c.getColumnIndex("m_bGainEnable_4"));
	                	retData.m_bGainEnable_5 = c.getInt(c.getColumnIndex("m_bGainEnable_5"));
	                	retData.m_bGainEnable_6 = c.getInt(c.getColumnIndex("m_bGainEnable_6"));
	                	retData.m_bGainEnable_7 = c.getInt(c.getColumnIndex("m_bGainEnable_7"));
	                	
	                	retData.m_bResEnable_0 = c.getInt(c.getColumnIndex("m_bResEnable_0"));
	                	retData.m_bResEnable_1 = c.getInt(c.getColumnIndex("m_bResEnable_1"));
	                	retData.m_bResEnable_2 = c.getInt(c.getColumnIndex("m_bResEnable_2"));
	                	retData.m_bResEnable_3 = c.getInt(c.getColumnIndex("m_bResEnable_3"));
	                	retData.m_bResEnable_4 = c.getInt(c.getColumnIndex("m_bResEnable_4"));
	                	retData.m_bResEnable_5 = c.getInt(c.getColumnIndex("m_bResEnable_5"));
	                	retData.m_bResEnable_6 = c.getInt(c.getColumnIndex("m_bResEnable_6"));
	                	retData.m_bResEnable_7 = c.getInt(c.getColumnIndex("m_bResEnable_7"));
	                	
	                	
              
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }	      
    	 
    	  return retData;
    }
    
    public static void UpdateColorTemperRValue(int colortemp,int Rvalue,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_ColorTemper+" SET nRed = "+Rvalue+" WHERE ledid = " + LEDID + " AND m_nColorTemperature = "
    			+colortemp;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateColorTemperGValue(int colortemp,int Gvalue,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_ColorTemper+" SET nGreen = "+Gvalue+" WHERE ledid = " + LEDID + " AND m_nColorTemperature = "
    			+colortemp;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    
    public static void UpdateColorTemperBValue(int colortemp,int Bvalue,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_ColorTemper+" SET nBlue = "+Bvalue+" WHERE ledid = " + LEDID + " AND m_nColorTemperature = "
    			+colortemp;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    }
    

    //更新色温
    public static void UpdateColorTemperValue(ColorTemperData colorTemperData,int colortemp,int LEDID){
    	
    	String strSQLString = "UPDATE "+strTblName_ColorTemper+" SET m_bEnable = "+colorTemperData.nEnable+
    			", nRed = "+colorTemperData.nRed+
    			", nGreen = "+colorTemperData.nGreen+
    			", nBlue = "+colorTemperData.nBlue+
    			", nICRed = "+colorTemperData.nICRed+
    			", nICGreen = "+colorTemperData.nICGreen+
    			", nICBlue = "+colorTemperData.nICBlue+
    			", nRedLow = "+colorTemperData.nRedLow+
    			", nGreenLow = "+colorTemperData.nGreenLow+
    			", nBlueLow = "+colorTemperData.nBlueLow+
    			", nICRedLow = "+colorTemperData.nICRedLow+
    			", nICGreenLow = "+colorTemperData.nICGreenLow+
    			", nICBlueLow = "+colorTemperData.nICBlueLow+
    			", nICRed1 = "+colorTemperData.nICRed1+
    			", nICGreen1 = "+colorTemperData.nICGreen1+
    			", nICBlue1 = "+colorTemperData.nICBlue1+
    			", nICRed2 = "+colorTemperData.nICRed2+
    			", nICGreen2 = "+colorTemperData.nICGreen2+
    			", nICBlue2 = "+colorTemperData.nICBlue2+
    			", nICRed6 = "+colorTemperData.nICRed6+
    			", nICGreen6 = "+colorTemperData.nICGreen6+
    			", nICBlue6 = "+colorTemperData.nICBlue6+
    			", nICRed7 = "+colorTemperData.nICRed7+
    			", nICGreen7 = "+colorTemperData.nICGreen7+
    			", nICBlue7 = "+colorTemperData.nICBlue7+
    			", nICRed8 = "+colorTemperData.nICRed8+
    			", nICGreen8 = "+colorTemperData.nICGreen8+
    			", nICBlue8 = "+colorTemperData.nICBlue8+
    			", nICRed9 = "+colorTemperData.nICRed9+
    			", nICGreen9 = "+colorTemperData.nICGreen9+
    			", nICBlue9 = "+colorTemperData.nICBlue9+
    			", m_bGainEnable_0 = "+colorTemperData.m_bGainEnable_0+
    			", m_bGainEnable_1 = "+colorTemperData.m_bGainEnable_1+
    			", m_bGainEnable_2 = "+colorTemperData.m_bGainEnable_2+
    			", m_bGainEnable_3 = "+colorTemperData.m_bGainEnable_3+
    			", m_bGainEnable_4 = "+colorTemperData.m_bGainEnable_4+
    			", m_bGainEnable_5 = "+colorTemperData.m_bGainEnable_5+
    			", m_bGainEnable_6 = "+colorTemperData.m_bGainEnable_6+
    			", m_bGainEnable_7 = "+colorTemperData.m_bGainEnable_7+
    			", m_bResEnable_0 = "+colorTemperData.m_bResEnable_0+
    			", m_bResEnable_1 = "+colorTemperData.m_bResEnable_1+
    			", m_bResEnable_2 = "+colorTemperData.m_bResEnable_2+
    			", m_bResEnable_3 = "+colorTemperData.m_bResEnable_3+
    			", m_bResEnable_4 = "+colorTemperData.m_bResEnable_4+
    			", m_bResEnable_5 = "+colorTemperData.m_bResEnable_5+
    			", m_bResEnable_6 = "+colorTemperData.m_bResEnable_6+
    			", m_bResEnable_7 = "+colorTemperData.m_bResEnable_7+
    			" WHERE ledid = " + LEDID + " AND m_nColorTemperature = "
    			+colortemp;	
        SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    	
    	
    }
    
    
    
}
