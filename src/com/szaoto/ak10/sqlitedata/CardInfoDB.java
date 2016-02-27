package com.szaoto.ak10.sqlitedata;

import java.util.ArrayList;

import android.database.Cursor;

import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.util.ByteConvert;
import com.szaoto.ak10.util.UtilFun;

public class CardInfoDB {


	public static String strTblName_cardinfo = "tb_cardinfo";
	//构造函数
    public CardInfoDB()
    {
    	super();
    
    }
	//增加数据
    public static void AddData(CardInformation cardinfo)
    {
 
        int Id = cardinfo.getnSlotID();
        int Type = cardinfo.getnType();
        byte[] MacAddress = cardinfo.getUcMACAddress();
        
        String strMac = MacAddress[0]+"-"+MacAddress[1]+"-"+MacAddress[2]+"-"+
        		MacAddress[3]+"-"+MacAddress[4]+"-"+MacAddress[5];
        String strVersion = cardinfo.getsVersion();
        String strHardwareId = cardinfo.getsHardwareID();
        String strDate = cardinfo.getdDate();
       
    	String strSQLString = "insert into "+strTblName_cardinfo + "('id','type','macaddress','version','hid','date')"
        +" VALUES ("+Id+","+Type+ ",'"+ strMac +"','"+ strVersion +"','"+strHardwareId +"','"+ strDate +"')";
    	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
  			
    }
    
    
    
    //删除所有数据
    public static void DeleteAllData()
    {  	
    	String strSQLString = "DELETE  FROM "+strTblName_cardinfo;  	
    	SqliteDB.m_DbDatabase.execSQL(strSQLString);
    }
    
    //
    
    
    
    //通过ID获取一条记录
    public static CardInformation GetRecordById(int id)
    {
    	CardInformation cardInformation = new CardInformation();
    	
 
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo+" WHERE id =" + id;
     			  
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                if(c.moveToNext())
	                {              
	                	cardInformation.setnSlotID(c.getShort(c.getColumnIndex("id")));
	                	cardInformation.setnType(c.getShort(c.getColumnIndex("type")));
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] byteMac =  new byte[6];
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
	                	
	                	cardInformation.setUcMACAddress(byteMac);
	                	
	                	cardInformation.setsVersion(c.getString((c.getColumnIndex("version"))));
	                	cardInformation.setsHardwareID(c.getString((c.getColumnIndex("hid"))));
	                	cardInformation.setsDate(c.getString((c.getColumnIndex("date"))));
	           
	      
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return cardInformation;
    	
    }
    
    //获取采集卡列表
    public static ArrayList<CardInformation> GetAcqRecords()
    {
    	
    	ArrayList<CardInformation> tRetArrayList  = new ArrayList<CardInformation>();
    	
    	
 
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo+" WHERE type = 2" ;
     			  
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {
	                	CardInformation cardInformation = new CardInformation();
	                	
	                	cardInformation.setnSlotID(c.getShort(c.getColumnIndex("id")));
	                	cardInformation.setnType(c.getShort(c.getColumnIndex("type")));
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] byteMac =  new byte[6];
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
	                	
	                	cardInformation.setUcMACAddress(byteMac);
	                	
	                	cardInformation.setsVersion(c.getString((c.getColumnIndex("version"))));
	                	cardInformation.setsHardwareID(c.getString((c.getColumnIndex("hid"))));
	                	cardInformation.setsDate(c.getString((c.getColumnIndex("date"))));
	           
	                	tRetArrayList.add(cardInformation);
	      
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return tRetArrayList;
    	
    }
    
    public static ArrayList<CardInformation> GetSndRecords()
    {
    	
    	ArrayList<CardInformation> tRetArrayList  = new ArrayList<CardInformation>();
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo+" WHERE type = 3" ;
     			  
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {
	                	CardInformation cardInformation = new CardInformation();
	                	
	                	cardInformation.setnSlotID(c.getShort(c.getColumnIndex("id")));
	                	cardInformation.setnType(c.getShort(c.getColumnIndex("type")));
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] byteMac =  new byte[6];
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
	                	
	                	cardInformation.setUcMACAddress(byteMac);
	                	
	                	cardInformation.setsVersion(c.getString((c.getColumnIndex("version"))));
	                	cardInformation.setsHardwareID(c.getString((c.getColumnIndex("hid"))));
	                	cardInformation.setsDate(c.getString((c.getColumnIndex("date"))));
	           
	                	tRetArrayList.add(cardInformation);
	      
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return tRetArrayList;
    	
    }
    
    public static ArrayList<CardInformation> GetSysCardRecords()
    {
    	
    	ArrayList<CardInformation> tRetArrayList  = new ArrayList<CardInformation>();
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo+" WHERE type = 1" ;
     			  
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {
	                	CardInformation cardInformation = new CardInformation();
	                	
	                	cardInformation.setnSlotID(c.getShort(c.getColumnIndex("id")));
	                	cardInformation.setnType(c.getShort(c.getColumnIndex("type")));
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] byteMac =  new byte[6];
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
	                	
	                	cardInformation.setUcMACAddress(byteMac);
	                	
	                	cardInformation.setsVersion(c.getString((c.getColumnIndex("version"))));
	                	cardInformation.setsHardwareID(c.getString((c.getColumnIndex("hid"))));
	                	cardInformation.setsDate(c.getString((c.getColumnIndex("date"))));
	           
	                	tRetArrayList.add(cardInformation);
	      
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return tRetArrayList;
    	
    }
    
    
    public static ArrayList<String> GetAllCardInfoRecordMacAddress()
    {
    	ArrayList<String> arrRet = new ArrayList<String>();
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {         	            	
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] tBytes = new byte[6];
	                	
	                	String[] arrTmp =strMac.split("-");
	                	
	                	for (int i = 0; i < 6; i++) {
							
	                		tBytes[i] = Byte.valueOf(arrTmp[i]);             		
						}
	                	
	                    String strTmp =	UtilFun.bytes2HexString(tBytes,6,"-");    	
	                	arrRet.add(strTmp);
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return arrRet;
    	
    }
    
    public static ArrayList<CardInformation> GetAllCardInfoRecord()
    {
    	
    	ArrayList<CardInformation> arrRet = new ArrayList<CardInformation>();
 
     	String strSQLString = "SELECT * FROM "+strTblName_cardinfo;    
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {     
	                	CardInformation cardInformation = new CardInformation();
	                	
	                	cardInformation.setnSlotID(c.getShort(c.getColumnIndex("id")));
	                	cardInformation.setnType(c.getShort(c.getColumnIndex("type")));
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	byte[] byteMac =  new byte[6];
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
	                	
	                	cardInformation.setUcMACAddress(byteMac);
	                	
	                	cardInformation.setsVersion(c.getString((c.getColumnIndex("version"))));
	                	cardInformation.setsHardwareID(c.getString((c.getColumnIndex("hid"))));
	                	cardInformation.setsDate(c.getString((c.getColumnIndex("date"))));
	           
	                	arrRet.add(cardInformation);
	      
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	
    	
    	  return arrRet;
    	
    }
    
    public static byte[] GetMacById(int id)
    {
    	byte[] byteMac =  new byte[6];
  
 	
     	String strSQLString = "SELECT macaddress FROM "+strTblName_cardinfo+" WHERE id =" + id;
     	 
     	try{
	          
	            Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
	            if(c!=null)
	            {
	                while(c.moveToNext())
	                {              
	                	
	                	String strMac = c.getString((c.getColumnIndex("macaddress")));
	                	
	                	
	                	String[] strArrMac =strMac.split("-");
	                	for (int i = 0; i < strArrMac.length; i++) {
							byteMac[i] = Byte.valueOf(strArrMac[i]);
						}
     
	                }
	                c.close();
	            }
	      }catch(Exception e)
	      {
	            e.printStackTrace();
	      }
		      
    	 
    	
    	  return byteMac;
    	
    }

}
