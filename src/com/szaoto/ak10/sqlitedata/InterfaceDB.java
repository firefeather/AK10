package com.szaoto.ak10.sqlitedata;

import java.util.ArrayList;

import android.database.Cursor;

public class InterfaceDB {


	public static String strTblName_interface = "tb_interface";
	//���캯��
    public InterfaceDB()
    {
    	super();
    }
    
    //���Interface�Ƿ����
    public static boolean CheckInterfaceExist(int Interfid,int LEDID){
    	
    	String strSQLString = "SELECT * FROM "+strTblName_interface+
    			" WHERE id = " + Interfid+" AND ledid = "+LEDID;    	
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
    
	//��������
    public static void AddData(IntfData tIntfData)
    {

        int Id = tIntfData.Id;
        int offsetx = tIntfData.offsetX;
        int offsety = tIntfData.offsetY;
    	int width = tIntfData.width;
    	int height = tIntfData.height;
	    byte[] MacAddress =  tIntfData.macaddress;
	    int channelid = tIntfData.channelid;
	    String strNameString = tIntfData.name;
	    int ledid = tIntfData.ledid;
    	
    	String strMac = MacAddress[0]+"-" + MacAddress[1]+"-" + MacAddress[2]+"-"
    			 + MacAddress[3]+"-" + MacAddress[4]+"-" + MacAddress[5];  
	    
    	String strSQLString = "insert into "+strTblName_interface + "('id','offsetx','offsety','width','height','mac','channelid','name','ledid')"
        +"VALUES ("+Id+","+offsetx+ ","+ offsety +","+ width +","+height +",'"+strMac+"',"+ channelid +",'" + strNameString +"',"+
    			ledid + ")";
    	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);		
    }
    //ɾ������
    public static void DeleteDataById(int id,int LEDID)
    {
    	String strSQLString = "DELETE FROM "+strTblName_interface+" WHERE Id = " + id +" AND ledid = "+LEDID;   	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
    } 

    //ɾ����������
    public static void DeleteAllData(int LEDID)
    {
    	String strSQLString = "DELETE FROM "+strTblName_interface+" WHERE ledid = "+LEDID;   	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);	
    } 
     
    //��������
    public static void UpdateDataById(int id,String strKey,String strValue,int LEDID)
    { 	
    	String strSQLString = "UPDATE "+strTblName_interface+"SET "+strKey+" = "+strValue+" WHERE id = " + id+" AND ledid = "+LEDID; 	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);  
    }
    
    
    public static void UpdateInterfacePosParam(int id,int x,int y,int w,int h, int LEDID)
    {
    	UpdatDataByFileds(id,new String[]{"offsetx","offsety","width","height"},new int[]{x,y,w,h},LEDID);
    }
    
    public static void UpdateChannelId(int id,int ChannelId,int LEDID)
    {
   		String strSQLString = "UPDATE "+strTblName_interface+" SET channelid  = "+ChannelId+
   				" WHERE id = " + id+" AND ledid = "+LEDID;
 		SqliteDB.m_DbDatabase.execSQL(strSQLString);
    }
    
    public static void UpdatDataByFileds(int id,String[] strKey,String[] strValue,int LEDID)
    {
     	for (int i = 0; i < strKey.length; i++) {
     		String strSQLString = "UPDATE "+strTblName_interface+" SET "+strKey[i]+" = "+
     	strValue[i]+" WHERE id = " + id+" AND ledid = "+LEDID;
     		SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}	
    }
    
    //ͬʱ���¼�������
    public static void UpdatDataByFileds(int id,String[] strKey,int[] nValue,int LEDID)
    {
     	for (int i = 0; i < strKey.length; i++) {
     		String strSQLString = "UPDATE "+strTblName_interface+" SET "+strKey[i]+" = "+nValue[i]+" WHERE id = " + id
     				+" AND ledid = "+LEDID;
     		SqliteDB.m_DbDatabase.execSQL(strSQLString);
		}
    }
    
    //ͨ��ID��ȡһ����¼
    public static IntfData GetRecordById(int id,int LEDID)
    {
    	IntfData rIntfData = new IntfData();
     	String strSQLString = "SELECT * FROM "+strTblName_interface+" WHERE id =" + id+" AND ledid = "+LEDID ;   
     	try{	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {              
	                	rIntfData.Id =  c.getInt(c.getColumnIndex("id"));
	                	rIntfData.offsetX =  c.getInt(c.getColumnIndex("offsetx"));
	                	rIntfData.offsetY =  c.getInt(c.getColumnIndex("offsety"));
	                	rIntfData.width =  c.getInt(c.getColumnIndex("width"));
	                	rIntfData.height =  c.getInt(c.getColumnIndex("height"));  	                	
                        String strMac = c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		rIntfData.macaddress[i] = Byte.valueOf(array[i]);
						}   
	                	
	                	rIntfData.channelid =  c.getInt(c.getColumnIndex("channelid"));  
	                	rIntfData.name =  c.getString(c.getColumnIndex("name")); 
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		         	
    	  return rIntfData;   	
    }

    //ͨ�����м�¼
    public static ArrayList<IntfData> GetAllRecord(int LEDID)
    {
    	ArrayList<IntfData> RetArrList= new ArrayList<IntfData>();	
     
     	String strSQLString = "SELECT * FROM "+strTblName_interface + " WHERE ledid = "+LEDID;;  
     	try{
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {     
	                	IntfData rIntfData = new IntfData();
	                	
	                	rIntfData.Id =  c.getInt(c.getColumnIndex("id"));
	                	rIntfData.offsetX =  c.getInt(c.getColumnIndex("offsetx"));
	                	rIntfData.offsetY =  c.getInt(c.getColumnIndex("offsety"));
	                	rIntfData.width =  c.getInt(c.getColumnIndex("width"));
	                	rIntfData.height =  c.getInt(c.getColumnIndex("height"));
                        String strMac =     c.getString(c.getColumnIndex("mac"));
	                	
	                	String[] array = strMac.split("-");
	                	for (int i = 0; i < array.length; i++) {
	                		rIntfData.macaddress[i] = Byte.valueOf(array[i]);
						}   
	                	
	                	rIntfData.channelid =  c.getInt(c.getColumnIndex("channelid"));  
	                	rIntfData.name =  c.getString(c.getColumnIndex("name"));   
	                	
	                	RetArrList.add(rIntfData);
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }	        	
    	  
    	  return RetArrList;
    	
    }
	
	public static int GetChportIdById(int id,int LEDID)
	{		
	    int ChanPortId = -1;	
     	String strSQLString = "SELECT * FROM "+strTblName_interface+" WHERE id =" + id+" AND ledid = "+LEDID;   
     	try{	        
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {                        	
	                	int Chanid =  c.getInt(c.getColumnIndex("channelid"));                 	
	                	ChanPortId = Chanid%1000+Chanid/1000;                
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      	
    	  return ChanPortId;
	}
	
}