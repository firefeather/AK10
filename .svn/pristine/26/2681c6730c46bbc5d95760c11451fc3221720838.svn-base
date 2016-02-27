package com.szaoto.ak10.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TraverseDictionary {
	private String listFileName;
	private List<String> lstFile = new ArrayList<String>();  //结果 List 
	
	private List<String> lstFilePath = new ArrayList<String>();  //结果 List 
	  
	public void GetFiles(String Path, String Extension, boolean IsIterative)  //搜索目录，扩展名，是否进入子文件夹 
	{ 
	    File[] files = new File(Path).listFiles(); 
	    if (null == files) {
			return;
		}
	    try {  //捕获异常
	    for (int i = 0; i < files.length; i++) 
	    { 
	        File f = files[i]; 
	        if (f.isFile()) 
	        { 
	            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension))  //判断扩展名 
	                lstFile.add(f.getName()); //直接得到名称,15-01-20修改zsj
	             if (!IsIterative) 
	                break; 
	        } 
	        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //忽略点文件（隐藏文件/文件夹） 
	            GetFiles(f.getPath(), Extension, IsIterative); 
	       }
	    } catch (Exception e) {
		}
	}

	public void GetFilePaths(String Path, String Extension, boolean IsIterative)  //搜索目录，扩展名，是否进入子文件夹 
	{ 		
	    File[] files = new File(Path).listFiles(); 
	    if (null == files) {
			return;
		}
	    try {  //捕获异常
		    for (int i = 0; i < files.length; i++) 
		    { 
		        File f = files[i]; 
		        if (f.isFile()) 
		        { 
		            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension))  //判断扩展名 
		            	lstFilePath.add(f.getPath()); //直接得到名称,15-01-20修改zsj
		             if (!IsIterative) 
		                break; 
		        } 
		        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //忽略点文件（隐藏文件/文件夹） 
		        	GetFilePaths(f.getPath(), Extension, IsIterative); 
		      }  
		  } catch (Exception e) {
		}    
	}
	
   public static  String GetUDiskDir()
	{
	   String tPath ="/mnt/usbdisk/";  //核心板新版调试该路径
	   //String tPath ="/mnt/media_rw/usbdisk/";  //核心板新版调试该路径	
	   //String tPathOldString ="/mnt/usb/";
	   String tPath1 ="/storage/usbdisk/";  //核心板新版调试该路径
	   //String tPathOldString ="/mnt/media_rw/usbdisk/";	
	   String tPath2 ="/mnt/usb/";
	   File file = new File(tPath);
	   if (file.exists()) {
		   return tPath;
	   }else {
		   File file1 = new File(tPath1);
		   if (file1.exists()) {			   
			   return tPath1;
		   }else {
			   File file2 = new File(tPath2);
			   if (file2.exists()) {
				   return tPath2;
			   } else {
				   return null;
			   }				
		   }
	   }	
	}
	   
	   
	public String getListFileName() {
		return listFileName;
	}



	public void setListFileName(String listFileName) {
		this.listFileName = listFileName;
	}



	public List<String> getLstFile() {
		return lstFile;
	}

	
	
	public void setLstFile(List<String> lstFile) {
		this.lstFile = lstFile;
	}

	public List<String> getLstFilePath() {
		return lstFilePath;
	}

	public void setLstFilePath(List<String> lstFilePath) {
		this.lstFilePath = lstFilePath;
	}
	

}
