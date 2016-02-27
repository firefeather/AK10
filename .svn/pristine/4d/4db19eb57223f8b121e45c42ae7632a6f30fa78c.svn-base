package com.szaoto.ak10.sqlitedata;

import java.util.ArrayList;

import android.database.Cursor;

public class ChannelDB {


	public static String strTblName_channel = "tb_channel";
	//构造函数
    public ChannelDB()
    {
    	super();
    
    }
    //检查Channel是否存在
    public static boolean CheckChanExist(int Chid,int LEDID){
    	
    	String strSQLString = "SELECT * FROM "+strTblName_channel+
    			" WHERE id = " + Chid+" AND ledid = "+LEDID;    
    	
    	try{
	          
            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
            if(c!=null)
            {
                if(c.moveToNext())
                {    
                	c.close();
                	return true;
                }
                else {
                	c.close();
					return false;
				}
               
            }
      }catch(Exception e)
      {
            e.printStackTrace();
      }
 	
      return false;
    }
    
	//增加数据
    public static void AddData(ChnData tChnData)
    {
 
        int Id = tChnData.Id;
        int offsetx = tChnData.offsetX;
        int offsety = tChnData.offsetY;
    	int width = tChnData.width;
    	int height = tChnData.height;
    	byte[] MacAddress =  tChnData.macaddress;
    	int nvid = tChnData.videosourceid;
    	int ledid = tChnData.Ledid;
    	
    	String strMac = MacAddress[0]+"-" + MacAddress[1]+"-" + MacAddress[2]+"-"
    			 + MacAddress[3]+"-" + MacAddress[4]+"-" + MacAddress[5];
    	
    	String strChName =  tChnData.strChName;
    	int frame_freq =  tChnData.frame_freq;
        
    	String strSQLString = "insert into "+strTblName_channel +
    			"('id','offsetx','offsety','width','height','mac','name','framefreq','vsourceid','ledid')"
                +"VALUES ("+Id+","+offsetx+ ","+ offsety +","+ width +","+height +",'"+ strMac +"','"+
    			strChName +"',"+frame_freq +","+nvid+","+ledid+")";
    	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
 			
    }
    //删除数据
    public static void DeleteDataById(int id,int LEDID)
    {
     	
  	
    	String strSQLString = "DELETE FROM "+strTblName_channel+" WHERE id = " + id
    			+" AND ledid = "+LEDID;
  	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
  
    	
    } 
    //删除所有数据
    public static void DeleteAllChanData(int LEDID)
    {
    	String strSQLString = "DELETE FROM "+strTblName_channel +" WHERE ledid = "+LEDID;	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
   	
    }
     
    //更新数据
    public static void UpdateDataById(int id,String strKey,String strValue,int LEDID)
    {
    
    	String strSQLString = "UPDATE "+strTblName_channel+" SET "+strKey+" = "+strValue+" WHERE id = " + id
             + " AND ledid = "+LEDID;
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);

    }
    
    //同时更新几项数据
    public static void UpdatDataByFileds(int id,String[] strKey,String[] strValue,int LEDID)
    {
     	for (int i = 0; i < strKey.length; i++) {
     		String strSQLString = "UPDATE "+strTblName_channel+" SET "+strKey[i]+" = "+strValue[i]+" WHERE id = " + id
     		+" AND ledid = "+LEDID;
     		SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}
    }
    
    public static void UpdatDataByFileds(int id,String[] strKey,int[] strValue,int LEDID)
    {
     	for (int i = 0; i < strKey.length; i++) {
     		String strSQLString = "UPDATE "+strTblName_channel+" SET "+strKey[i]+" = "+strValue[i]+" WHERE id = " + id
     		+" AND ledid = "+LEDID;
     		SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}
    }
    
    public static void UpdateChannelPosParam(int id,int x,int y,int w,int h,int LEDID)
    {
    	UpdatDataByFileds(id,new String[]{"offsetx","offsety","width","height"},new int[]{x,y,w,h},LEDID);
    }
    
    public static void UpdateChannelWHParam(int id,int w,int h,int LEDID)
    {
    	UpdatDataByFileds(id,new String[]{"width","height"},new int[]{w,h},LEDID);
    }
    
    public static void UpdateFrame(int id,int frame,int LEDID)
    {
    	UpdatDataByFileds(id,new String[]{"framefreq"},new int[]{frame},LEDID);
    }
    
    
    public static ChnData GetRecordByVideosourceId(int videosourceid,int LEDID)
    {
    	ChnData rChnData = new ChnData();

     	
     	String strSQLString = "SELECT * FROM "+strTblName_channel+" WHERE videosourceid = " + videosourceid+" AND ledid = "+LEDID;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {              
	                	rChnData.Id =  c.getInt(c.getColumnIndex("id"));
	                	rChnData.offsetX =  c.getInt(c.getColumnIndex("offsetx"));
	                	rChnData.offsetY =  c.getInt(c.getColumnIndex("offsety"));
	                	rChnData.width =  c.getInt(c.getColumnIndex("width"));
	                	rChnData.height =  c.getInt(c.getColumnIndex("height")); 
	                	String strMac =     c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		rChnData.macaddress[i] = Byte.valueOf(array[i]);
						}
	                	
	                	rChnData.strChName =  c.getString(c.getColumnIndex("name"));  
	                	rChnData.frame_freq =  c.getInt(c.getColumnIndex("framefreq")); 
	                	rChnData.videosourceid =  c.getInt(c.getColumnIndex("vsourceid")); 
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return rChnData;
    }
    
    
    //通过id获取macaddress
    
    public static byte[] GetMacByChId(int chid,int LEDID)
    {
    	byte[] byteRet = new byte[6];

     	
     	String strSQLString = "SELECT * FROM "+strTblName_channel+" WHERE id = " + chid+" AND ledid = "+LEDID;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {              

	                	String strMac =     c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		byteRet[i] = Byte.valueOf(array[i]);
						}
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return byteRet;
    	
    }
    
    //通过ID获取一条记录
    public static ChnData GetRecordById(int id,int LEDID)
    {
    	ChnData rChnData = new ChnData();

 	
     	String strSQLString = "SELECT * FROM "+strTblName_channel+" WHERE id = " + id+" AND ledid = "+LEDID;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {              
	                	rChnData.Id =  c.getInt(c.getColumnIndex("id"));
	                	rChnData.offsetX =  c.getInt(c.getColumnIndex("offsetx"));
	                	rChnData.offsetY =  c.getInt(c.getColumnIndex("offsety"));
	                	rChnData.width =  c.getInt(c.getColumnIndex("width"));
	                	rChnData.height =  c.getInt(c.getColumnIndex("height")); 
	                	String strMac =     c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		rChnData.macaddress[i] = Byte.valueOf(array[i]);
						}
	                	
	                	rChnData.strChName =  c.getString(c.getColumnIndex("name"));  
	                	rChnData.frame_freq =  c.getInt(c.getColumnIndex("framefreq")); 
	                	rChnData.videosourceid =  c.getInt(c.getColumnIndex("vsourceid")); 
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return rChnData;
    	
    }
    
    //通过所有记录
    public static ArrayList<ChnData> GetAllRecord(int LEDID)
    {
    	ArrayList<ChnData> RetArrList= new ArrayList<ChnData>();	
     
     	String strSQLString = "SELECT * FROM "+strTblName_channel +" WHERE ledid = "+LEDID + "  ORDER BY id ASC  ";  
     	try{
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {     
	                	ChnData rChnData = new ChnData();
	                	
	                	rChnData.Id =  c.getInt(c.getColumnIndex("id"));
	                	rChnData.offsetX =  c.getInt(c.getColumnIndex("offsetx"));
	                	rChnData.offsetY =  c.getInt(c.getColumnIndex("offsety"));
	                	rChnData.width =  c.getInt(c.getColumnIndex("width"));
	                	rChnData.height =  c.getInt(c.getColumnIndex("height")); 
	                	
	                	String strMac =     c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		rChnData.macaddress[i] = Byte.valueOf(array[i]);
						} 
	                	
	                	rChnData.strChName =  c.getString(c.getColumnIndex("name"));  
	                	rChnData.frame_freq =  c.getInt(c.getColumnIndex("framefreq")); 
	                	rChnData.videosourceid =  c.getInt(c.getColumnIndex("vsourceid")); 
	                	
	                	RetArrList.add(rChnData);
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }	      
    	
    	  
    	  return RetArrList;
    	
    }
    
}
	

