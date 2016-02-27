/*
   * 文件名 DataAccessGammaTable.java
   * 包含类名列表com.szaoto.ak10.dataaccess
   * 版本信息，版本号
   * 创建日期2014年4月4日下午4:48:52
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.dataaccess;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.common.GammaData;
import com.szaoto.ak10.util.UtilFun;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class DataAccessAnalyseJkylin extends DataAccessBase {

	byte[] buf = new byte[64];
	
	/**
	* 将一个字符串转化为输入流
	*/
	public static InputStream getStringStream(String sInputString){
		if (sInputString != null && !sInputString.trim().equals("")){
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	} 
	
	/**
	 * @param context
	 */
	public DataAccessAnalyseJkylin(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
}
