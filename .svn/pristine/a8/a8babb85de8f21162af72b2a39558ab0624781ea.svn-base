package com.szaoto.ak10.colortemp;

import java.io.InputStream;
import java.util.HashMap;
import com.szaoto.ak10.sqlitedata.ColorTemperData;

public interface ColorTemperParser  {

	/** 
     * 解析输入流 得到Book对象集合 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    public HashMap<Integer,ColorTemperData> parse(InputStream is) throws Exception;  
      
    /** 
     * 序列化ColorTemperData对象集合 得到XML形式的字符串 
     * @param books 
     * @return 
     * @throws Exception 
     */  
    public String serialize(HashMap<Integer,ColorTemperData> ColorTemperMap) throws Exception; 
	
}
