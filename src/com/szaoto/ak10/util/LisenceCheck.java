package com.szaoto.ak10.util;

import java.io.FileInputStream;

import com.szaoto.ak10.common.SystemConfig;

public class LisenceCheck {
	
	SystemConfig systemConfig;
	public static boolean CheckVideoState(String fileName ) {
		byte[] buf = new byte[1];
		byte[] crc = new byte[1];
		FileInputStream fin = null;
		try{ 
			 fin = new FileInputStream(fileName);
				byte[] readbuf = new byte[10];
			    int length = fin.available(); 
			    fin.skip(length/2);
			    fin.read(readbuf,0 , 10);
			    int sum = 0;
				for (int i = 0; i < 10; i++)
				{
					sum += readbuf[i];
				}
				crc[0]  = (byte) (256 - (sum % 256));
				fin.skip(length/2 -11);
				fin.read(buf, 0, 1);
				fin.close();
			    }catch(Exception e){ 
			           e.printStackTrace(); 
			}
		int k1 = (int)crc[0];
		int k2 = (int)buf[0];
		if(k1 == k2)
		{
			return true;
		}
		else {
			return false;
		}  

	}
	

}
