package com.szaoto.ak10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;

import android.text.format.Time;
import android.util.Log;
import android.util.Xml;

import com.szaoto.ak10.common.Display.ColourRGB;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.configuration.CardInformationList;
import com.szaoto.ak10.configuration.EdidSet;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.datacomm.InterfaceComm;
import com.szaoto.ak10.datacomm.LEDParamComm;
import com.szaoto.ak10.leddisplay.LedConstructActivity;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.ColorTemperData;
import com.szaoto.ak10.sqlitedata.ColorTemperDb;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.util.ByteConvert;
import com.szaoto.ak10.util.LogUtil;
import com.szaoto.ak10.util.UtilFun;

public class SocketCommService   {
	private static final int MAX_NET_BUFFER = 65536;
	private static final int MAX_FIFO_BUFFER = 28 * 36;
	private static final int BYTE28 = 28;
	private static final int ONE_PACK = 64;
	
    public static List<Socket> m_ListSocket = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService mExecutorService = null; //thread pool
    
    Thread threadSocketListen = null;  
	Thread thread_TimingPower = null; //定时开关屏线程
    boolean FlagListenSocketStopRequested = false;
	boolean FlagTimingPowerStopRequested = false;
	static public CtrlStruct m_sCtrlStruct;

    public static List<INetDataChangeObserver> netobservers = new ArrayList<INetDataChangeObserver>();
    private int m_nPort = Ak10Application.GetPort(EPort.ePort_LEDConstructor);
    
	public SocketCommService(int nPort) {
		m_nPort = nPort;
		try {	
			server = new ServerSocket(nPort);
			mExecutorService = Executors.newCachedThreadPool();  //create a thread pool
			//System.out.println("服务器已启动...");     
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
    public  Socket GetSocketByIp(byte[] IpAddress )
    {
    	int nSize = m_ListSocket.size();
    	for (int i = 0; i < nSize; i++) {
    		Socket tSocket = m_ListSocket.get(i);
    		InetAddress tAddress = tSocket.getInetAddress();
    		byte[] tAddressIp = tAddress.getAddress();
    		if (tAddressIp[0]==IpAddress[0]
    			&& tAddressIp[1]==IpAddress[1]
    			&& tAddressIp[2]==IpAddress[2]
    			&& tAddressIp[3]==IpAddress[3]) {
				return tSocket;
    		}	 
    	}  
	 
    	return null;
    }
	
	//开启监听相乘
	public void StartSocketListeningThread()
	{
		threadSocketListen = new Thread( new Runnable() {
			@Override
			public void run() {
				 Socket client = null;
				 while(!FlagListenSocketStopRequested) {
					 try {
						 //Log.d("SOCKETDEBUG", "等待监听...");
		        		 client = server.accept();
		        		 //System.out.println("已经连接..."); 
		                 //把客户端放入客户端集合中
		                 m_ListSocket.add(client);
		                 mExecutorService.execute(new SocketHandlerService(client)); 
			               
					} catch (Exception e) {
						 Thread.currentThread().interrupt(); // re-assert interrupt
					}      
		         }	

				 if (client!=null) { 
					 try {
						 client.close();
					 } catch (IOException e) {
						 e.printStackTrace();
					 }
					 m_ListSocket.remove(client); 
				 }
			}
		});
		
		threadSocketListen.start();
	}
	
	//test
	//开启监听相乘
	public void StartTimingPowerThread()
	{
		thread_TimingPower = new Thread( new Runnable() {
			@Override
			public void run() {
				 while(!FlagTimingPowerStopRequested) {
					 try {
						 Calendar nCurrentDate = Calendar.getInstance();
						 int nCurHour = nCurrentDate.get(Calendar.HOUR_OF_DAY);
						 int nCurMin = nCurrentDate.get(Calendar.MINUTE);
						 int nCurSec = nCurrentDate.get(Calendar.SECOND);
						 long currenttime = nCurHour * 3600 + nCurMin * 60 + nCurSec;		 
						 if (m_sCtrlStruct != null) {
							 long nBeginTimeDiff = m_sCtrlStruct.GetBeginTime() - currenttime;
							 long nEndTimeDiff = m_sCtrlStruct.GetEndTime() - currenttime;
							 if ( 0 == nBeginTimeDiff ) {
		      					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
								int nSize = tArrayList.size();
		      					for (int i = 0; i < nSize; i++) {
		      						IntfData tInterfData = tArrayList.get(i);
		      						LEDParamComm.SetPower(1,tInterfData.macaddress, tInterfData.Id%1000);
		      					}			 
								Thread.sleep(1200);
							 }
							 if (0 == nEndTimeDiff) {
		      					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
								int nSize = tArrayList.size();
		      					for (int i = 0; i < nSize; i++) {
		      						IntfData tInterfData = tArrayList.get(i);
		      						LEDParamComm.SetPower(0,tInterfData.macaddress, tInterfData.Id%1000);
		      					}										 
								Thread.sleep(1200);
							 }
						}    
					} catch (Exception e) {
						 Thread.currentThread().interrupt(); // re-assert interrupt
					}      
				}
			}
		});
		
		thread_TimingPower.start();
	}


	public void StopSocketListeningThread()
	{	
	    FlagListenSocketStopRequested = true;
	    
        if ( threadSocketListen != null ) {
        	threadSocketListen.interrupt();
        }
	}
	public void StopTimingPowerThread()
	{	
	    FlagListenSocketStopRequested = true;
	    
        if ( thread_TimingPower != null ) {
        	thread_TimingPower.interrupt();
        }
	}


    /**
     * 循环遍历客户端集合，给每个客户端都发送信息。
     */      
     public static  void SendByte2AllConnectedPeer(byte[] byteCmd) {
         int num =m_ListSocket.size();
         for (int index = 0; index < num; index ++)
         {
             Socket mSocket = m_ListSocket.get(index);
             PrintWriter pout = null;
             try
             {
                 pout = new PrintWriter(new BufferedWriter( new OutputStreamWriter(mSocket.getOutputStream())),true);
                 //pout.println(byteCmd);
                 pout.println(Arrays.toString(byteCmd));
             }catch (IOException e)
             {
                 e.printStackTrace();
             }
         }
    }

     
     /**
      * 循环遍历客户端集合，给每个客户端都发送信息。
      */      
     public void SendMsg2AllConnectedPeer(String strCmd) {
      	
          System.out.println(strCmd);
          int num =m_ListSocket.size();
          for (int index = 0; index < num; index ++)
          {
              Socket mSocket = m_ListSocket.get(index);
              PrintWriter pout = null;
              try
              {
                  pout = new PrintWriter(new BufferedWriter( new OutputStreamWriter(mSocket.getOutputStream())),true);
                  pout.println(strCmd);
              }catch (IOException e)
              {
                  e.printStackTrace();
              }
          }
     }
      

      
     public void SendMsg2ConnectedPeer(String strCmd,Socket socket) {  	 
         PrintWriter pout = null;
         try {
             pout = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
             pout.println(strCmd);
         }catch (IOException e) {
             e.printStackTrace();
         }
     }

	 class SocketHandlerService implements Runnable {
         private Socket socket;
        // private BufferedReader in = null;
         private InputStream inputStream;        
         PrintStream pStream = null;        
         public SocketHandlerService(Socket socket) {	 
             this.socket = socket;
             try 
             {
                 //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	 inputStream = socket.getInputStream();
            	 pStream = new PrintStream(socket.getOutputStream());
             }
             catch(IOException e){
            	  e.printStackTrace();
             }
             
             //SendMsg2ConnectedPeer("OK",socket);
                    
         }
         
         public void SendBytes2ConnectedPeer(byte[] byteCmd,Socket socket)  {
             for (int i = 0; i < byteCmd.length; i++) {	
				 pStream.write(byteCmd[i]);
			 }
         }
         
         @Override
         public void run() {
        	 try {
        		 if ( 8889 == m_nPort) {
        			 Run_LedConstructor_E();
        		 } else if (8890 == m_nPort){
        			 Run_Jkylin();
        		 } else {
        			 ;
        		 }
        	 } catch (Exception e) {
				// TODO: handle exception
        	 }
         }         
         
         private String RespondPack(CtrlStruct ctrlStruct, int nRet) {
        	String str = "";
          	try {        		
              	ECtrlType eType = ctrlStruct.GetCtrlType();
              	switch (eType) {
          		case ePower:
          			{
          			   str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
          			   str += "\n";	
          			   str += "<aoto>";	
          			   str += "\n";	
          			   str += "  	<do id=\"0c\" result =\"0\"  desc =\"ok\" />";
          			   str += "\n";	
          			   str += "</aoto>";
          			   str += "\n";	
          			}
          			break;
          		case eColorTmp:
      				{
           			   str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
           			   str += "\n";	
           			   str += "<aoto>";	
           			   str += "\n";	
           			   str += "  	<do id=\"0d\" result =\"0\"  desc =\"ok\" />";
           			   str += "\n";	
           			   str += "</aoto>";
           			   str += "\n";	
      	 			}
          			break;		
          		case eGamma:
      				{
           			   str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
           			   str += "\n";	
           			   str += "<aoto>";	
           			   str += "\n";	
           			   str += "  	<do id=\"0h\" result =\"0\"  desc =\"ok\" />";
           			   str += "\n";	
           			   str += "</aoto>";
           			   str += "\n";	
      	 			}    			
          			break;
          		case eLight:
      				{
           			   str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
           			   str += "\n";	
           			   str += "<aoto>";	
           			   str += "\n";	
           			   str += "  	<do id=\"0a\" result =\"0\"  desc =\"ok\" />";
           			   str += "\n";	
           			   str += "</aoto>";
           			   str += "\n";	
      	 			}      			
          			break;
          		case eTimeSwitch:
      				{
           			   str += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
           			   str += "\n";	
           			   str += "<aoto>";	
           			   str += "\n";	
           			   str += "  	<do id=\"0f\" result =\"0\"  desc =\"ok\" />";
           			   str += "\n";	
           			   str += "</aoto>";
           			   str += "\n";	
      	 			}      			
          			break;
          		default:
          			break;
          		}
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	
          	return str;
          }
         
         private int DealJkylinCmd(CtrlStruct ctrlStruct) {
          	try {        		
              	ECtrlType eType = ctrlStruct.GetCtrlType();
              	switch (eType) {
          		case eColorTmp:
      				{
      					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
      					ColourRGB sColorRGB = new ColourRGB();
      					sColorRGB.setId(1);
      					sColorRGB.setM_bEnable(true);	
      					ColorTemperData tTemperData = ColorTemperDb.GetTmperRecord(ctrlStruct.GetColorTmp(), 1);
      					
      					sColorRGB.setnRed(tTemperData.nRed);
      					sColorRGB.setnGreen(tTemperData.nGreen);
      					sColorRGB.setnBlue(tTemperData.nBlue);
      					
      					sColorRGB.setnICRed(tTemperData.nICRed);
      					sColorRGB.setnICGreen(tTemperData.nICGreen);
      					sColorRGB.setnICBlue(tTemperData.nICBlue);
      					
      					sColorRGB.setnRedLow(tTemperData.nRedLow);
      					sColorRGB.setnGreenLow(tTemperData.nGreenLow);
      					sColorRGB.setnBlueLow(tTemperData.nRedLow);
      					
      					sColorRGB.setnICRedLow(tTemperData.nICRedLow);
      					sColorRGB.setnICGreenLow(tTemperData.nICGreenLow);
      					sColorRGB.setnICBlueLow(tTemperData.nICBlueLow);
						int nSize = tArrayList.size();
      					for (int i = 0; i < nSize; i++) {
      						IntfData tInterfData =tArrayList.get(i);
      						LEDParamComm.SetColorTemp(tInterfData.macaddress, tInterfData.Id%1000, sColorRGB, (short)0, (short)0);
      						if (1 == ctrlStruct.GetPowerValue()) {
      							LEDParamComm.SavePara(tInterfData.macaddress, tInterfData.Id%1000, 0x01);
							}
      					}
      	 			}
          			break;		
          		case eGamma:
      				{
      					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
						int nSize = tArrayList.size();
      					for (int i = 0; i < nSize; i++) {
      						IntfData tInterfData =tArrayList.get(i);
      						LEDParamComm.SetGamma(ctrlStruct.GetGammaR(), ctrlStruct.GetGammaG(), ctrlStruct.GetGammaB(), 
      								tInterfData.macaddress, tInterfData.Id%1000);
      						if (1 == ctrlStruct.GetPreviewOrSave()) {//需要保存
      							LEDParamComm.SavePara(tInterfData.macaddress, tInterfData.Id%1000, 0x02);
							}
      					}     
      	 			}
          			break;
          		case eLight:
      				{
      					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
						int nSize = tArrayList.size();
      					for (int i = 0; i < nSize; i++) {
      						IntfData tInterfData =tArrayList.get(i);
      						LEDParamComm.SetBright(ctrlStruct.GetLight(),tInterfData.macaddress, tInterfData.Id%1000);
      						if (1 == ctrlStruct.GetPreviewOrSave()) {//需要保存
      							LEDParamComm.SavePara(tInterfData.macaddress, tInterfData.Id%1000, 0x01);
							}
      					}
      	 			}      			
          			break;
          		case ePower:
	      			{
	      				//55 55 12 34 F9 D2 00 00 00 01 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2B		关屏
	      				//55 55 12 34 F9 D2 00 00 00 01 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2A		开屏
	  					ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(1);
						int nSize = tArrayList.size();
	  					for (int i = 0; i < nSize; i++) {
	  						IntfData tInterfData = tArrayList.get(i);
	  						LEDParamComm.SetPower(ctrlStruct.GetPowerValue(),tInterfData.macaddress, tInterfData.Id%1000);
	  					}		
	      			}
	      			break;          			
          		case eTimeSwitch:
      				{
      					//public void StartTimingPowerThread()中处理
      	 			}      			
          			break;
          		default:
          			break;
          		}
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	
          	return 1;
          }
         
         private void RespondToJkylin(String string){   
 			try {
 				Thread.sleep(200);
	    		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));            
	    		writer.write(string);
	    		writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
         }
         public void Run_Jkylin()
         {
             try {        	 	 
                 while(socket!= null && !socket.isClosed()) {                 	 
                	 BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                	 char[] charLenBuf = new char[8];               	
                	 bf.read(charLenBuf);//16进制数据
                	 String strLenString = String.valueOf(charLenBuf);
                	 int nLen = Integer.parseInt(strLenString, 16);
                	 char[] charBuf = new char[nLen];
                	 bf.read(charBuf);
                	 String s = String.valueOf(charBuf);
                	 InputStream inStream = new ByteArrayInputStream(s.getBytes());
                	 m_sCtrlStruct = GetCtrlFromXML(inStream);  	 
                	 int nRet = DealJkylinCmd(m_sCtrlStruct);
                	 String packString = RespondPack(m_sCtrlStruct, nRet);
                	 RespondToJkylin(packString);

					//如果客户端断开了连接
					if (socket!=null&&socket.isClosed()) {
						//关闭了socket
						m_ListSocket.remove(socket);
						socket=null;	 
					}
                }
             } catch (Exception e) {
                 e.printStackTrace();
             }	   
         }

        @SuppressLint("UseValueOf")
     	private CtrlStruct GetCtrlFromXML(InputStream inStream) { 
         	CtrlStruct sCtrlInfo = null; 
     	    XmlPullParser parser = Xml.newPullParser();
     	    try { 
     	    	String sId = null;
     	    	String str = null;
     	        parser.setInput(inStream, "UTF-8");// 设置数据源编码  
     	        int eventType = parser.getEventType();// 获取事件类型  
     	        
     	        while (eventType != XmlPullParser.END_DOCUMENT) {  
     	            switch (eventType) {  
     	            case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理  
     	            	sCtrlInfo = new CtrlStruct();  
     	                break;  
     	            case XmlPullParser.START_TAG://开始读取某个标签  
     	                //通过getName判断读到哪个标签，然后通过nextText()获取文本节点值，或通过getAttributeValue(i)获取属性节点值  
     	                String name = parser.getName();
     	                if (name.equalsIgnoreCase("do")) {
     	                	sId = parser.getAttributeValue(null, "id");
     	                	if (sId.equals("0c") || sId.equals("0C")) {
     							//开关屏
     	                		sCtrlInfo.SetCtrlType(ECtrlType.ePower);
     						} else if (sId.equals("0d") || sId.equals("0D")) {
     							//色温
     							sCtrlInfo.SetCtrlType(ECtrlType.eColorTmp);
     						} else if (sId.equals("0h") || sId.equals("0H")) {
     							//伽马
     							sCtrlInfo.SetCtrlType(ECtrlType.eGamma);
     						} else if (sId.equals("0a") || sId.equals("0A")) {
     							//亮度
     							sCtrlInfo.SetCtrlType(ECtrlType.eLight);
     						} else if (sId.equals("0f") || sId.equals("0F")) {
     							//定时开关屏
     							sCtrlInfo.SetCtrlType(ECtrlType.eTimeSwitch);
     						}
     	                	
     	                	str = null;
     	                	str = parser.getAttributeValue(null, "save");
     	                	if (str != null) {
     							int nValue = new Integer(str);
     							sCtrlInfo.SetPreviewOrSave(nValue);
     						}
     	                	
     	                	str = null;
     	                	str = parser.getAttributeValue(null, "enable");
     	                	if (str != null) {
     							int nValue = new Integer(str);
     							sCtrlInfo.SetTimeSwitchEnable(0 == nValue ? false : true);
     						}
     	                } else if (sId != null) {
     	                    if (name.equalsIgnoreCase("power")) {
     	                    	int nValue = new Integer(parser.nextText());
     	                    	sCtrlInfo.SetPowerValue(nValue);
     	                    } else if (name.equalsIgnoreCase("clrt")) {  
     	                    	int nValue = new Integer(parser.nextText());
     	                    	sCtrlInfo.SetColorTmp(nValue);
     	                    } else if (name.equalsIgnoreCase("r")) {  
     	                    	float fValue = new Float(parser.nextText());
     	                    	sCtrlInfo.SetGammaR(fValue);
     	                    } else if (name.equalsIgnoreCase("g")) {  
     	                    	float fValue = new Float(parser.nextText());
     	                    	sCtrlInfo.SetGammaG(fValue);
     	                    } else if (name.equalsIgnoreCase("b")) {  
     	                    	float fValue = new Float(parser.nextText());
     	                    	sCtrlInfo.SetGammaB(fValue);
     	                    } else if (name.equalsIgnoreCase("light")) {  
     	                    	int nValue = new Integer(parser.nextText());
     	                    	sCtrlInfo.SetLight(nValue);
     	                    } else if (name.equalsIgnoreCase("time")) {
     		                	str = null;
     		                	str = parser.getAttributeValue(null, "start");
     		                	if (str != null) {
     								int nHour = new Integer(str.substring(0, 2));
     								int nMinute = new Integer(str.substring(3, 5));
     								int nSecond = new Integer(str.substring(6, 8));
     								sCtrlInfo.SetBeginTime(nHour * 3600 + nMinute * 60 + nSecond);
     							}

     		                	str = null;
     		                	str = parser.getAttributeValue(null, "end");
     		                	if (str != null) {
     		                		int nHour = new Integer(str.substring(0, 2));
     								int nMinute = new Integer(str.substring(3, 5));
     								int nSecond = new Integer(str.substring(6, 8));
     								sCtrlInfo.SetEndTime(nHour * 3600 + nMinute * 60 + nSecond);
     							}
     	                    }
     	                }  
     	                break;  
     	            case XmlPullParser.END_TAG:// 结束元素事件  
     	                break;  
     	            }  
     	            eventType = parser.next();  
     	        }  
     	        inStream.close();
     	        
     	        
     	    } catch (Exception e) {  
     	        e.printStackTrace();  
     	    }
     	    
     	    return sCtrlInfo;
     	}  	

        

        public void Run_LedConstructor_E() throws IOException {
            while(socket != null && !socket.isClosed()) {
           	 
           	 byte[] buf = new byte[MAX_NET_BUFFER];
           	 int nRev = 0;
           	 
           	 if(0 < (nRev = inputStream.read(buf)))
           	 {
               	 byte[] bufTemp =  new byte[MAX_FIFO_BUFFER];	//存储28字节 最多每次发送  (MAX_FIFO_BUFFER - 18) /  28 个包
               	 int lengthTemp = 0;
               	 
               	 byte[] lastmacaddress = new byte[6];
               	 int nLastInterfId = -1;
               	 int m = 0;
               	 
           		 for(int i = 0; i < nRev ; i ++)
           		 {
               		 if('A' == buf[i] && 'T' == buf[i + 1])
               		 {
               			 //逐个包拷贝
               			 byte[] onepackdata = new byte[ONE_PACK];
               			 onepackdata = UtilFun.CopyOfRange(buf,i,ONE_PACK + i); 
               			 //判断校验是否正确
               			 byte[] crc32 = UtilFun.CRC32(onepackdata,ONE_PACK - 4);
                       	 if(crc32[0] == onepackdata[ONE_PACK - 4] &&
                       		crc32[1] == onepackdata[ONE_PACK - 3] &&
                       		crc32[2] == onepackdata[ONE_PACK - 2] &&
                       		crc32[3] == onepackdata[ONE_PACK - 1])
                       	 {
                       		 if(0 == m)
                       		 {
                       			 lastmacaddress = UtilFun.CopyOfRange(onepackdata,0,6);
                       			 nLastInterfId = ByteConvert.byteToUbyte(onepackdata[15]);
                       			 
                       			 m ++;
                       		 }
                       		 
                       		 byte[] macaddress = UtilFun.CopyOfRange(onepackdata,0,6);
                       		 byte type = macaddress[2];
                       		 switch(type)
                       		 {
									case 0x0a:
										if ('A' == macaddress[3] &&
											'P' == macaddress[4] &&
											'P' == macaddress[5] ) 
										 {
											 byte[] sourceaddress =UtilFun.CopyOfRange(onepackdata,6,12);
											 byte[] datas = new byte[ONE_PACK - 18]; 
											 datas = UtilFun.CopyOfRange(onepackdata,14,ONE_PACK - 18);
											 DelWithAK10(sourceaddress,datas);
										 }
									break;
									
									case 0x01:
									{
										byte[] sourceaddress =UtilFun.CopyOfRange(onepackdata,6,12);
										byte[] datas = new byte[ONE_PACK - 18]; 
										datas = UtilFun.CopyOfRange(onepackdata,14,ONE_PACK - 18);
										DelwithSysCardCmd(macaddress,sourceaddress,datas);
									}
									break;
									
									case 0x02:
									{
										byte[] sourceaddress =UtilFun.CopyOfRange(onepackdata,6,12);
										byte[] datas = new byte[ONE_PACK - 18]; 
										datas = UtilFun.CopyOfRange(onepackdata,14,ONE_PACK - 18);
										DelwithAcqCardCmd(macaddress,sourceaddress,datas);
									}
									break;
									
									case 0x03:
									{
										//仅对 透明转发 + 无应答 包作捆包一次性发送处理
										if(-1 == onepackdata[16] && -1 == onepackdata[17] && 0 == onepackdata[29])
										{
											//这里后续修改为完全透明转发

											byte nSlotNum = macaddress[5];
											if(-1 == nSlotNum)
											{
												ArrayList<IntfData> tArrIntfDatas = InterfaceDB.GetAllRecord(1);	  
							    				for (int i1 = 0; i1 < tArrIntfDatas.size(); i1++) 
							    				{
							    					IntfData tInterfData = tArrIntfDatas.get(i1);
								    				byte[] tMacAddress = tInterfData.macaddress;
								    				int nRj45Num = tInterfData.Id % 1000;
								    				
								    				//地址
								    				byte[] ucAddress = new byte[2];
								    				ucAddress[0] = 0x11;
								    				ucAddress[1] = (byte) (0x00 + (nRj45Num - 1) * 0x10);
								    				
								    				byte[] DataToTransfer = UtilFun.CopyOfRange(onepackdata,20,48);
								    				
								    				byte[] EthPackData = Packager.PackMutiple28byteData(tMacAddress,ucAddress,DataToTransfer,BYTE28);
								    				
								    				//System.arraycopy(EthPackData, 0, bufTemp, lengthTemp, ONE_PACK);
													//lengthTemp += ONE_PACK;
								    				try 
						    						{
						    							SpiControl.WriteSpi(EthPackData, 64);
						    						} 
						    						catch (Exception e) 
						    						{
						    							e.printStackTrace();
						    						}
								    				
							    				}
											}
											else
											{
												byte[] byte28 = UtilFun.CopyOfRange(onepackdata,20,48);
												System.arraycopy(byte28, 0, bufTemp, lengthTemp, BYTE28);
												lengthTemp += BYTE28;
												
												int nInterfId = ByteConvert.byteToUbyte(onepackdata[15]);
												
												if((MAX_FIFO_BUFFER <= lengthTemp && 0 == lengthTemp % BYTE28) ||
													((lastmacaddress[0] != macaddress[0] ||
													lastmacaddress[1] != macaddress[1] ||
													lastmacaddress[2] != macaddress[2] ||
													lastmacaddress[3] != macaddress[3] ||
													lastmacaddress[4] != macaddress[4] ||
													lastmacaddress[5] != macaddress[5]) &&
													nLastInterfId != nInterfId))
												{
													//一次发送
													
													//地址
								    				byte[] ucAddress = new byte[2];
								    				ucAddress[0] = 0x11;
								    				ucAddress[1] = (byte) (0x00 + (nInterfId - 1) * 0x10);
								    				
								    				byte[] EthPackData = Packager.PackMutiple28byteData(macaddress,ucAddress,bufTemp,lengthTemp);
								    				try 
						    						{
						    							SpiControl.WriteSpi(EthPackData,lengthTemp + 4 + 18);
						    						} 
						    						catch (Exception e) 
						    						{
						    							e.printStackTrace();
						    						}
								    				
								    				lastmacaddress = UtilFun.CopyOfRange(onepackdata,0,6);
								    				nLastInterfId = nInterfId;
													lengthTemp = 0;
												}

											}
										}
										else
										{
											byte[] sourceaddress = UtilFun.CopyOfRange(onepackdata,6,12);
											byte[] datas = new byte[34]; 
											datas = UtilFun.CopyOfRange(onepackdata,14,ONE_PACK - 16);
											int nRet =  DelwithSndCardCmd(macaddress,sourceaddress,datas);
											if (-1 == nRet) {
												Log.i("ERROR", "RCVERROR");
											}
										}
									
									}
									break;
									
                       			default:
                       			break;
                       		 }
                        
                       		 i += ONE_PACK - 1;
                       	 }
                       	 else
                       	 {
                       		 continue;
                       	 }
               		 }
               		 else
               		 {
               			 continue;
               		 }
           		}
           		 
           		//一次发送
           		if(0 < lengthTemp && 0 == lengthTemp % BYTE28)
           		{
           			try 
						{
           				//地址
		    				byte[] ucAddress = new byte[2];
		    				ucAddress[0] = 0x11;
		    				ucAddress[1] = (byte) (0x00 + (nLastInterfId - 1) * 0x10);
		    				
		    				byte[] EthPackData = Packager.PackMutiple28byteData(lastmacaddress,ucAddress,bufTemp,lengthTemp);
		    				try 
    						{
    							SpiControl.WriteSpi(EthPackData, (lengthTemp + 4 + 18) < 64 ? 64 : lengthTemp + 4 + 18);
    						} 
    						catch (Exception e) 
    						{
    							e.printStackTrace();
    						}
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
           		}
           	 }
            }
            
            //如果客户端断开了连接
            if (socket != null && socket.isClosed()) {		
           	 //关闭了socket
           	 m_ListSocket.remove(socket);
           	 socket = null;	 
            }
            
        
       }
              
        
        
        
        
       private void Run_LedConstructor()
       {
           try {        	 	 
               while(socket!= null && !socket.isClosed()) { 	
              	 byte[] buf = new byte[64];     	 
              	 inputStream.read(buf, 0, 2);
              	 if (buf[0]!='A'||buf[1]!='T') {
						continue;
              	 }
	 
              	 try {
              		 inputStream.read(buf, 2, 12);
				 } catch (Exception e) {
					e.printStackTrace();
					continue;
				 }
	 
              	// buf = UtilFun.getBytes(chbuf);
              	 //获取mac地址和源地址
              	 byte[] macaddress = UtilFun.CopyOfRange(buf,0,6);
              	 byte[] sourceaddress =UtilFun.CopyOfRange(buf,6,12);
              	 byte[] bytelength =  UtilFun.CopyOfRange(buf,12,14);

              	 //pc源地址          
              	 int length = ByteConvert.byteToUbyte(bytelength[0])*0x100+
              			 ByteConvert.byteToUbyte(bytelength[1]);
              	 
              	 inputStream.read(buf,14,length-14);
            	 
              	 byte[] datas = new byte[length-16]; 
              	 datas = UtilFun.CopyOfRange(buf,14,length-4);        
              	 //LogUtil.WriteLog(buf,false);
              	 byte[] tmpByte = new byte[60];
            	 
              	 for (int i = 0; i < tmpByte.length; i++) {
					tmpByte[i] = buf[i];
				 }
          	 
              	 byte[] crc32 = UtilFun.CRC32(tmpByte, ONE_PACK - 4);
	 
              	 if(crc32[0]!=buf[length-4]||
              		crc32[1]!=buf[length-3]||
              		crc32[2]!=buf[length-2]||
              		crc32[3]!=buf[length-1])
              	 {
              		 continue;
              	 }

              	 //调试打印
              	
                   DealWithCmd(macaddress,sourceaddress,datas);                
               }
               
               //如果客户端断开了连接
               if (socket!=null&&socket.isClosed()) {		
              	 //关闭了socket
              	 m_ListSocket.remove(socket);
              	 socket=null;	 
               }
               
               
           } catch (Exception e) {
               e.printStackTrace();
           }	   
       }


       //处理命令
       public void DealWithCmd(byte[] macaddress,byte[] source,byte[] datas)
       {
    	   //获取目标
    	   if (macaddress[0]=='A'&&macaddress[1]=='T'&&macaddress[2]==0x0a&&
    			   macaddress[3]=='A'&&macaddress[4]=='P'&&macaddress[5]=='P' ) {
    		   DelWithAK10(source,datas);
		   }
    	   else {
    		   DelWithCard(macaddress,source,datas);
		   }
    	   
       }
       
       public void DelWithAK10(byte[] source,byte[] datas)
       {
    	   int nSubFuncId =ByteConvert.byteToUbyte(datas[0])*0x100+ByteConvert.byteToUbyte(datas[1]);  	  
    	   if (nSubFuncId == 0x0001) {
    	   }
    	   if (nSubFuncId == 0x00ff) {  
    		   int mLedid =ByteConvert.byteToUbyte(datas[2]);
    		   HomePageActivity.ClearLedData(mLedid);
    		   NotifyAll(3);
    	   } 
       }
       
       public void DelWithCard(byte[] macaddress,byte[] source,byte[] datas)
       {
    	   //卡类型
    	   byte type = macaddress[2];
    	   
    	   if (type==0x01) {
    		   DelwithSysCardCmd(macaddress,source,datas);
		   }
    	   else if(type==0x02) {
    		   DelwithAcqCardCmd(macaddress,source,datas);
	 	   }
    	   else if(type==0x03) {
    		   int nRet =  DelwithSndCardCmd(macaddress,source,datas);
    		   if (nRet==-1) {
    			   Log.i("ERROR", "RCVERROR");
    		   }
    	   }
       }
       
       public void DelwithSysCardCmd(byte[] macaddress,byte[] source,byte[] datas)
       {
    	   int nLedid =ByteConvert.byteToUbyte(datas[0]);
    	   int nInterfId = ByteConvert.byteToUbyte(datas[1]);
    	   int nSubFuncId =ByteConvert.byteToUbyte(datas[2])*0x100+ByteConvert.byteToUbyte(datas[3]);
    	   
    	   //设置视频剪切区域
    	   if (nSubFuncId == 0x0001) {
			
       		   short x = (short) (ByteConvert.byteToUbyte(datas[4])*0x100+ByteConvert.byteToUbyte(datas[5]));
    		   short y = (short) (ByteConvert.byteToUbyte(datas[6])*0x100+ByteConvert.byteToUbyte(datas[7]));
    		   short w = (short) (ByteConvert.byteToUbyte(datas[8])*0x100+ByteConvert.byteToUbyte(datas[9]));
    		   short h = (short) (ByteConvert.byteToUbyte(datas[10])*0x100+ByteConvert.byteToUbyte(datas[11]));
    		   
    		   int nFrame = ByteConvert.byteToUbyte(datas[12]);
    		   
    		   //设置区域
    		   int id = ByteConvert.byteToUbyte(macaddress[5])*1000+nInterfId;
    		   
    		   //如果系统卡在数据库中不存在，就添加
    		   if(!ChannelDB.CheckChanExist(id, nLedid)){
    			   LedConstructActivity.AddChanDbData(macaddress, id, x, y, nLedid,1);
    		   }
    		   
    		   ChannelDB.UpdateChannelPosParam(id, x, y, w, h, nLedid);
    		   ChannelDB.UpdateFrame(id, nFrame, nLedid);
    		   
    		   NotifyAll(0);

		   }
    	   
    	 //查询版本
    	   if (nSubFuncId == 0x0003) {   
    		  
    		   byte[] byteRetVersion = new byte[2];
    		   ChanComm.GetAcqCardSoftwareVersion(macaddress,byteRetVersion);
    		  
    		   //数据发回
    		   
    		   byte[] datas2send = new byte[64];
    		   
    		   byte[] datasTmp = new byte[60];
    		   
    		   datasTmp[0]=source[0];
    		   datasTmp[1]=source[1];
    		   datasTmp[2]=source[2];
    		   datasTmp[3]=source[3];
    		   datasTmp[4]=source[4];
    		   datasTmp[5]=source[5];
    		   
    		   datasTmp[6]=macaddress[0];
    		   datasTmp[7]=macaddress[1];
    		   datasTmp[8]=macaddress[2];
    		   datasTmp[9]=macaddress[3];
    		   datasTmp[10]=macaddress[4];
    		   datasTmp[11]=macaddress[5];

    		   
    		   datasTmp[12] = 0x00;
    		   datasTmp[13] = 0x40;
    		   
    		   datasTmp[14] = (byte) 0xff;
    		   datasTmp[15] = (byte) 0xff;
    		   datasTmp[16] = (byte) 0x00;
    		   datasTmp[17] = (byte) 0x03;
    		   datasTmp[18] = byteRetVersion[0];
    		   datasTmp[19] = byteRetVersion[1];
    		   
    		   byte[] crc32 = UtilFun.CRC32(datasTmp,ONE_PACK - 4);
    		   
    		   
    		   for (int i = 0; i < 60; i++) {
				
    			   datas2send[i] = datasTmp[i];
			   }
    		   
    		   datas2send[60] = crc32[0];
    		   datas2send[61] = crc32[1];
    		   datas2send[62] = crc32[2];
    		   datas2send[63] = crc32[3];
    		   
    		   
    		   byte[] byteIp = new byte[4];
    		   byteIp[0] = source[2];
    		   byteIp[1] = source[3];
    		   byteIp[2] = source[4];
    		   byteIp[3] = source[5];
    		   
    		   Socket socketTmp =  GetSocketByIp(byteIp); 
    		   
    		   SendBytes2ConnectedPeer(datas2send, socketTmp);
   
		   }
    	 	   
       }
       
       public void DelwithAcqCardCmd(byte[] macaddress,byte[] source,byte[] datas)
       {
    	   int nLedid =ByteConvert.byteToUbyte(datas[0]);
    	   int nInterfId = ByteConvert.byteToUbyte(datas[1]);
    	   int nSubFuncId =ByteConvert.byteToUbyte(datas[2])*0x100+ByteConvert.byteToUbyte(datas[3]);
    	   
    	   //检查数据库中这个id的配置是否存在，如果不存在就添加到数据库
    	   
    	   int nChid = macaddress[5]*1000+nInterfId;
    	   
    	   if (nInterfId!=0xff) {
        	   if (!ChannelDB.CheckChanExist(nChid,nLedid))
        	   {
        		  //增加channel
        		  LedConstructActivity.AddChanDbData(macaddress,nChid,0,0,nLedid,0); 		   
        	   }
		   }
 	   
    	   //设置视频剪切区域
    	   if (nSubFuncId == 0x0001) {
			
    		   short x = (short) (ByteConvert.byteToUbyte(datas[4])*0x100+ByteConvert.byteToUbyte(datas[5]));
    		   short y = (short) (ByteConvert.byteToUbyte(datas[6])*0x100+ByteConvert.byteToUbyte(datas[7]));
    		   short w = (short) (ByteConvert.byteToUbyte(datas[8])*0x100+ByteConvert.byteToUbyte(datas[9]));
    		   short h = (short) (ByteConvert.byteToUbyte(datas[10])*0x100+ByteConvert.byteToUbyte(datas[11]));
    		   
    		   int nFrame = ByteConvert.byteToUbyte(datas[12]);
    		   
    		   //设置区域
    		   int id = macaddress[5]*1000+nInterfId;
    		   ChannelDB.UpdateChannelPosParam(id, x, y, w, h, nLedid);
    		   ChannelDB.UpdateFrame(id, nFrame, nLedid);
    		   
    		   //设置分辨率 edid
    			String tempreslution1 = w+"X"+h;
    			EdidSet Editset = new EdidSet(tempreslution1,nFrame);	
    		    Editset.SetResolutionAndFrame(nInterfId,macaddress,1);
 
    		    NotifyAll(1);
		   }
    	   
    	   //查询版本
    	   if (nSubFuncId == 0x0003) {   
    		  
    		   byte[] byteRetVersion = new byte[2];
    		   ChanComm.GetAcqCardSoftwareVersion(macaddress,byteRetVersion);
    		  
    		   //数据发回
    		   
    		   byte[] datas2send = new byte[64];
    		   
    		   byte[] datasTmp = new byte[60];
    		   
    		   datasTmp[0]=source[0];
    		   datasTmp[1]=source[1];
    		   datasTmp[2]=source[2];
    		   datasTmp[3]=source[3];
    		   datasTmp[4]=source[4];
    		   datasTmp[5]=source[5];
    		   
    		   datasTmp[6]=macaddress[0];
    		   datasTmp[7]=macaddress[1];
    		   datasTmp[8]=macaddress[2];
    		   datasTmp[9]=macaddress[3];
    		   datasTmp[10]=macaddress[4];
    		   datasTmp[11]=macaddress[5];

    		   
    		   datasTmp[12] = 0x00;
    		   datasTmp[13] = 0x40;
    		   
    		   datasTmp[14] = (byte) 0xff;
    		   datasTmp[15] = (byte) 0xff;
    		   datasTmp[16] = (byte) 0x00;
    		   datasTmp[17] = (byte) 0x03;
    		   datasTmp[18] = byteRetVersion[0];
    		   datasTmp[19] = byteRetVersion[1];
    		   
    		   byte[] crc32 = UtilFun.CRC32(datasTmp,ONE_PACK - 4);
    		   
    		   
    		   for (int i = 0; i < 60; i++) {
				
    			   datas2send[i] = datasTmp[i];
			   }
    		   
    		   datas2send[60] = crc32[0];
    		   datas2send[61] = crc32[1];
    		   datas2send[62] = crc32[2];
    		   datas2send[63] = crc32[3];
    		   
    		   
    		   byte[] byteIp = new byte[4];
    		   byteIp[0] = source[2];
    		   byteIp[1] = source[3];
    		   byteIp[2] = source[4];
    		   byteIp[3] = source[5];
    		   
    		   Socket socketTmp =  GetSocketByIp(byteIp); 
    		   
    		   SendBytes2ConnectedPeer(datas2send, socketTmp);
   
		   }
   
       }
       
       protected void NotifyAll(int nType)
       {
       		int nSize = netobservers.size();
			if (null != netobservers && 0 != nSize) {
				for (int i = 0; i < nSize; i++) {
					netobservers.get(i).onNetDataChangeNotify(nType);
				}
			}

       }
       
       public int DelwithSndCardCmd(byte[] macaddress,byte[] source,byte[] datas)
       {
    	   int nLedid =ByteConvert.byteToUbyte(datas[0]);
    	   //哪个插槽
    	   int nInterfId = ByteConvert.byteToUbyte(datas[1]);
    	   int nSlotNum  = ByteConvert.byteToUbyte(macaddress[5]);
    	   int nSubFuncId =ByteConvert.byteToUbyte(datas[2])*0x100+ByteConvert.byteToUbyte(datas[3]);
   
    	   //透明转发
    	   if (nSubFuncId == 0xffff) {
			   
    		   int dataLen = ByteConvert.byteToUbyte(datas[4])*0x100+ByteConvert.byteToUbyte(datas[5]);		   
    		   byte[] DataToTransfer = UtilFun.CopyOfRange(datas, 6, 6+dataLen);	
    		   
    		   //分析是否需要回传
    		   boolean bNeedBack = false;
    		   boolean bLongWait = false;
    		   if((DataToTransfer[9]&0x0f)!=0){
    			   bNeedBack = true;
    		   }
    		   
    		   if (DataToTransfer[5]==(byte)0x91) {
    			   bLongWait = true;
			   }
    		   
    		   //直接向卡转发数据，广播
    		   if (nSlotNum==0xff) {	
    			  ArrayList<IntfData> tArrIntfDatas = InterfaceDB.GetAllRecord(1);
				  int nSize = tArrIntfDatas.size();
    			  for (int i = 0; i < nSize; i++) {
    				  IntfData tInterfData = tArrIntfDatas.get(i);
    				  
    				  byte[] tMacAddress = tInterfData.macaddress;
    				  int    nRj45Num =    tInterfData.Id%1000;
    				  InterfaceComm.SendtoScanCard(tMacAddress,nRj45Num, DataToTransfer);
    				  
    				   if (bNeedBack) {				
    	    			   try {
    	    				   
    						  Thread.sleep(100);    						  
    						  //读数据 
    						  byte[] bytesRcv = InterfaceComm.ReadBackFiFoRcvData(tMacAddress,nRj45Num,bLongWait); 
    						  if (bytesRcv==null||bytesRcv.length<28) {
    							 return -1;
    						  }
    						  
    						    try {
    						    	LogUtil.WriteLog(bytesRcv,true);
    							} catch (IOException e) {
    								e.printStackTrace();
    							}
    						  //  String str = String.format("%d", bytesRcv.length);
    						  //Toast.makeText(HomePageActivity.getInstance(), str, Toast.LENGTH_SHORT).show(); 
    						  int nFrameCnt = bytesRcv.length/28;
    						  for (int j = 0; j < nFrameCnt; j++) {
    							
    				    		   byte[] datas2send = new byte[64];	   
    				    		   byte[] datasTmp = new byte[60];
    				    		   
    							   datasTmp[0]=source[0];
    				    		   datasTmp[1]=source[1];
    				    		   datasTmp[2]=source[2];
    				    		   datasTmp[3]=source[3];
    				    		   datasTmp[4]=source[4];
    				    		   datasTmp[5]=source[5];
    				    		   
    				    		   datasTmp[6]=macaddress[0];
    				    		   datasTmp[7]=macaddress[1];
    				    		   datasTmp[8]=macaddress[2];
    				    		   datasTmp[9]=macaddress[3];
    				    		   datasTmp[10]=macaddress[4];
    				    		   datasTmp[11]=macaddress[5];

    				    		   
    				    		   datasTmp[12] = 0x00;
    				    		   datasTmp[13] = 0x40;
    				    		   
    				    		   datasTmp[14] = (byte) 0xff;
    				    		   datasTmp[15] = (byte) nInterfId;
    				    		   datasTmp[16] = (byte) 0xff;
    				    		   datasTmp[17] = (byte) 0xff;
    				    		   
    				    		   datasTmp[18] = (byte) 0x00;
    				    		   datasTmp[19] = (byte) 0x1c;
    				    		   
    				    		   for (int k = 0; k < 28; k++) {	 
    				    			   datasTmp[20+k] = bytesRcv[k+28*j];
    							   }
    				    		   
    				    		   byte[] crc32 = UtilFun.CRC32(datasTmp,ONE_PACK - 4);
    				    		   
    				    		   for (int k = 0; k < 60; k++) {
    				   				
    				    			   datas2send[k] = datasTmp[k];
    							   }
    				    		   
    				    		   datas2send[60] = crc32[0];
    				    		   datas2send[61] = crc32[1];
    				    		   datas2send[62] = crc32[2];
    				    		   datas2send[63] = crc32[3];
    				    		   
    				    		   
    				    		   byte[] byteIp = new byte[4];
    				    		   byteIp[0] = source[2];
    				    		   byteIp[1] = source[3];
    				    		   byteIp[2] = source[4];
    				    		   byteIp[3] = source[5];
    				    		   
    				    		   Socket socketTmp =  GetSocketByIp(byteIp); 
    				    		   
    				    		   if (!socketTmp.isClosed()) {
    				    			   SendBytes2ConnectedPeer(datas2send, socketTmp);
								   }	     							  
    						  	}    						  
    					    } catch (InterruptedException e) {
    						   e.printStackTrace();
    					    }   	    			   
    				   }    			   				  
    			  	}   
			   	}
    		   	else {
    			   //点播
    			   InterfaceComm.SendtoScanCard(macaddress, nInterfId, DataToTransfer);
    			   
    			   if (bNeedBack) {
        			   try {
        				   
    					  Thread.sleep(200);
    					  //读数据 
    					  byte[] bytesRcv = InterfaceComm.ReadBackFiFoRcvData(macaddress,nInterfId,bLongWait);
    					  
    					  if (bytesRcv==null||bytesRcv.length<28) {
    						 return -1;
    					  }
    					  
    					  int nFrameCnt = bytesRcv.length/28;
    					  
    					  for (int i = 0; i < nFrameCnt; i++) {
    						
    			    		   byte[] datas2send = new byte[64];	   
    			    		   byte[] datasTmp = new byte[60];
    			    		   
    						   datasTmp[0]=source[0];
    			    		   datasTmp[1]=source[1];
    			    		   datasTmp[2]=source[2];
    			    		   datasTmp[3]=source[3];
    			    		   datasTmp[4]=source[4];
    			    		   datasTmp[5]=source[5];
    			    		   
    			    		   datasTmp[6]=macaddress[0];
    			    		   datasTmp[7]=macaddress[1];
    			    		   datasTmp[8]=macaddress[2];
    			    		   datasTmp[9]=macaddress[3];
    			    		   datasTmp[10]=macaddress[4];
    			    		   datasTmp[11]=macaddress[5];

    			    		   
    			    		   datasTmp[12] = 0x00;
    			    		   datasTmp[13] = 0x40;
    			    		   
    			    		   datasTmp[14] = (byte) 0xff;
    			    		   datasTmp[15] = (byte) nInterfId;
    			    		   datasTmp[16] = (byte) 0xff;
    			    		   datasTmp[17] = (byte) 0xff;
    			    		   
    			    		   datasTmp[18] = (byte) 0x00;
    			    		   datasTmp[19] = (byte) 0x1c;
    			    		   
    			    		   for (int j = 0; j < 28; j++) {	 
    			    			   datasTmp[20+j] = bytesRcv[j+28*i];
    						   }
    			    		   
    			    		   byte[] crc32 = UtilFun.CRC32(datasTmp,ONE_PACK - 4);
    			    		   
    			    		   for (int k = 0; k < 60; k++) {
    			   				
    			    			   datas2send[k] = datasTmp[k];
    						   }
    			    		   
    			    		   datas2send[60] = crc32[0];
    			    		   datas2send[61] = crc32[1];
    			    		   datas2send[62] = crc32[2];
    			    		   datas2send[63] = crc32[3];
    			    		   
    			    		   
    			    		   byte[] byteIp = new byte[4];
    			    		   byteIp[0] = source[2];
    			    		   byteIp[1] = source[3];
    			    		   byteIp[2] = source[4];
    			    		   byteIp[3] = source[5];
    			    		   
    			    		   Socket socketTmp =  GetSocketByIp(byteIp); 
    			    		   
    			    		   SendBytes2ConnectedPeer(datas2send, socketTmp);
    						  
    					  }
    					  
    				    } catch (InterruptedException e) {
    					   e.printStackTrace();
    				    } 
    			   }
			   }   
		   }
    	   
    	   //设置视频口
    	   if (nSubFuncId == 0x0002) {
			
    		   //获取视频通道号
    		   int tChanId = ByteConvert.byteToUbyte(datas[4])*1000+ByteConvert.byteToUbyte(datas[5]);  
    		   
    		   if (tChanId==0) {
				  return -1;
			   }
    		   
    		   if (!ChannelDB.CheckChanExist(tChanId, nLedid)) {
    			   
    			   
    			    //获取采集卡的MAC地址
    			    CardInformationList.GetCardInformationList();
    				
    				final CardInformation[] tCardInformations = CardInformationList.GetCardInformations();	
    				
    				int CardIndex = ByteConvert.byteToUbyte(datas[4])-1;
    				
    				byte[] MacAddress = tCardInformations[CardIndex].getUcMACAddress();
    			   
    			    if (MacAddress[2]==1) {
    			    	LedConstructActivity.AddChanDbData(MacAddress, tChanId, 0, 0, nLedid, 1);
					}else {
						LedConstructActivity.AddChanDbData(MacAddress, tChanId, 0, 0, nLedid, 0);
					} 
			   }
    		   
    		   int chPort = tChanId/1000+tChanId%1000;
    		   InterfaceComm.SetSendCardChPortAndEnable(true, chPort, macaddress, nInterfId);
    		   
    		   int tIntfId = ByteConvert.byteToUbyte(macaddress[5])*1000+nInterfId;
    		   if (!InterfaceDB.CheckInterfaceExist(tIntfId, nLedid)) {
   				
    			   //获取channel的x，y
    			   ChnData tChnData =  ChannelDB.GetRecordById(tChanId, nLedid);
    			   
    			   LedConstructActivity.AddInterfDbData(tIntfId, tChanId, nLedid, macaddress, 
    					   tChnData.offsetX, tChnData.offsetY, 400, 400);
    			   
			   }
    		   else {
    			   InterfaceDB.UpdateChannelId(tIntfId, tChanId, nLedid);
			   }
    		   NotifyAll(2);
		   }
    	   
    	   //设置视频剪切区域
    	   if (nSubFuncId == 0x0001) {
			
    		   short x = (short) (ByteConvert.byteToUbyte(datas[4])*0x100+ByteConvert.byteToUbyte(datas[5]));
    		   short y = (short) (ByteConvert.byteToUbyte(datas[6])*0x100+ByteConvert.byteToUbyte(datas[7]));
    		   short w = (short) (ByteConvert.byteToUbyte(datas[8])*0x100+ByteConvert.byteToUbyte(datas[9]));
    		   short h = (short) (ByteConvert.byteToUbyte(datas[10])*0x100+ByteConvert.byteToUbyte(datas[11]));
    		   short cfg3d = (short) (ByteConvert.byteToUbyte(datas[19]));
    		   
    		   InterfaceComm.SetSendCardPortParam(x, y, w, h, cfg3d, macaddress, nInterfId);
    		   int tIntfId = ByteConvert.byteToUbyte(macaddress[5])*1000+nInterfId;
    		   
    		   
    		   IntfData tintfData = InterfaceDB.GetRecordById(tIntfId, nLedid);
    		   ChnData tChnData =  ChannelDB.GetRecordById(tintfData.channelid, nLedid);
    		   
    		   InterfaceDB.UpdateInterfacePosParam(tIntfId, x+tChnData.offsetX, y+tChnData.offsetY, w, h, nLedid);
    		   
    		   NotifyAll(2);
		   }
    	   
    	   //查询版本
    	   if (nSubFuncId == 0x0003) {   
    		  
    		   byte[] byteRetVersion = new byte[2];
    		   InterfaceComm.GetSndCardByteSoftwareVersion(macaddress,byteRetVersion);
    		  
    		   //数据发回
    		   
    		   byte[] datas2send = new byte[64];
    		   
    		   byte[] datasTmp = new byte[60];
    		   
    		   datasTmp[0]=source[0];
    		   datasTmp[1]=source[1];
    		   datasTmp[2]=source[2];
    		   datasTmp[3]=source[3];
    		   datasTmp[4]=source[4];
    		   datasTmp[5]=source[5];
    		   
    		   datasTmp[6]=macaddress[0];
    		   datasTmp[7]=macaddress[1];
    		   datasTmp[8]=macaddress[2];
    		   datasTmp[9]=macaddress[3];
    		   datasTmp[10]=macaddress[4];
    		   datasTmp[11]=macaddress[5];

    		   
    		   datasTmp[12] = 0x00;
    		   datasTmp[13] = 0x40;
    		   
    		   datasTmp[14] = (byte) 0xff;
    		   datasTmp[15] = (byte) 0xff;
    		   datasTmp[16] = (byte) 0x00;
    		   datasTmp[17] = (byte) 0x03;
    		   datasTmp[18] = byteRetVersion[0];
    		   datasTmp[19] = byteRetVersion[1];
    		   
    		   byte[] crc32 = UtilFun.CRC32(datasTmp,ONE_PACK - 4);
    		   
    		   
    		   for (int i = 0; i < 60; i++) {
				
    			   datas2send[i] = datasTmp[i];
			   }
    		   
    		   datas2send[60] = crc32[0];
    		   datas2send[61] = crc32[1];
    		   datas2send[62] = crc32[2];
    		   datas2send[63] = crc32[3];
    		   
    		   
    		   byte[] byteIp = new byte[4];
    		   byteIp[0] = source[2];
    		   byteIp[1] = source[3];
    		   byteIp[2] = source[4];
    		   byteIp[3] = source[5];
    		   
    		   Socket socketTmp =  GetSocketByIp(byteIp); 
    		   
			 //   String str = String.format("%d", datas2send.length);
			 // Toast.makeText(HomePageActivity.getInstance(), str, Toast.LENGTH_SHORT).show(); 

    		   SendBytes2ConnectedPeer(datas2send, socketTmp);
   
		   }
    	   
    	   return 0;
       }
     }
}
