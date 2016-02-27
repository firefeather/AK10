package com.szaoto.ak10.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.util.EncodingUtils;
import com.szaoto.ak10.HomePageActivity;

public class LogUtil {
	private final static String sFileFlagString = "SendData.log";
	private static boolean bLogFlag = false;
	private byte[] buffer;
	static String logstringString = new String();

	public byte[] readFile(String fileName) throws IOException {    
			int length = 0;
			InputStream	in = new FileInputStream(HomePageActivity.CONFIG_PATH + sFileFlagString);
			length = in.available();           
			buffer = new byte[length]; 
			in.read(buffer);
			in.close();
		if(length >=100)
			return buffer;
		else 
			return null;	
	}   
		
	public static  void WriteFile(String msg) throws IOException {    

		String message = msg+"\r\n";
		String StrLog = message;	
		RandomAccessFile randomFile = null;  
		try {	
			
				  // ��һ����������ļ���������д��ʽ     
	            randomFile = new RandomAccessFile(HomePageActivity.CONFIG_PATH+sFileFlagString, "rw");     
	            // �ļ����ȣ��ֽ���     
	            long fileLength = randomFile.length();     
	            // ��д�ļ�ָ���Ƶ��ļ�β��     
	            randomFile.seek(fileLength);     				     
			    byte[] bytes = StrLog.getBytes();
	            String strOut = EncodingUtils.getString(bytes, "UTF-8"); 
	            randomFile.writeBytes(strOut); 
	            randomFile.close();  
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return;
	} 
	public static void WriteLog(String message) throws IOException  {    
		if (bLogFlag) {
			WriteFile(message);
		}
	return;
	} 
	
public static  void WriteLog(byte[] msg,boolean bRcv) throws IOException {    
		if (bLogFlag) {
			String message = new String(msg);//
			message = UtilFun.bytes2HexString(msg, msg.length, " ");
			String strTimeString = String.valueOf(System.currentTimeMillis());

			if (bRcv) {
				WriteFile(strTimeString + ":Rcv:" + message);
			} else {
				WriteFile(strTimeString + ":Snd:" + message);
			}
		}
		return;
	} 
public static  void ClearBuf() throws IOException {    
	logstringString = "";	
	return;
} 

public static void LogOut(byte[] datas,boolean bRcv){
	
	 String message = new String(datas);//
	 message = UtilFun.bytes2HexString(datas, datas.length, " ");	
	 String strTimeString =String.valueOf(System.currentTimeMillis());
	 
	 if (bRcv) {
		 System.out.println(strTimeString+":Rcv:"+message);
	 }else {
		 System.out.println(strTimeString+":Snd:"+message);
	 }
}


}