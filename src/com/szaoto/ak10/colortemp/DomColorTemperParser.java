package com.szaoto.ak10.colortemp;

import java.io.InputStream;  
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;  

import android.annotation.SuppressLint;
import com.szaoto.ak10.sqlitedata.ColorTemperData;

public class DomColorTemperParser implements ColorTemperParser {

	@SuppressLint("UseSparseArrays")
	@Override
	public HashMap<Integer, ColorTemperData> parse(InputStream is) throws Exception {
		
			    HashMap<Integer, ColorTemperData> MapTemperColor = 
			    		new HashMap<Integer, ColorTemperData>();  
			    
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
		        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
		        Document doc = builder.parse(is);   //解析输入流 得到Document实例  
		        Element rootElement = doc.getDocumentElement();  
		        NodeList items = rootElement.getElementsByTagName("template");  
	            for (int k = 0; k < items.getLength(); k++) {  	         
	     
                		 Node colortemperSingleNode = items.item(k);
                		 int TempKey = 0;
          			     ColorTemperData tColorTemperData = new ColorTemperData();
                		 if(colortemperSingleNode.getNodeName().equals("template"))
                		 {
                			   NamedNodeMap tMap = colortemperSingleNode.getAttributes();			   
                			   for (int l = 0; l < tMap.getLength(); l++) {
								
                				     Node tNode = tMap.item(l);
                				     String nameString = tNode.getNodeName();
                				     String valueString = tNode.getNodeValue();
                				     
                				     if (nameString.equals("m_nColorTemperature")) {						
                				    	String strKeyTemp = valueString; 
                				    	String strValueString= strKeyTemp.substring(0, strKeyTemp.indexOf('K')); 
                				    	TempKey = Integer.valueOf(strValueString); 
									 }
                				     if (nameString.equals("m_bEnable")) {						
                				    	 tColorTemperData.nEnable = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nRed")) {						
                				    	 tColorTemperData.nRed = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nGreen")) {						
                				    	 tColorTemperData.nGreen = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nBlue")) {						
                				    	 tColorTemperData.nBlue = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed")) {						
                				    	 tColorTemperData.nICRed = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen")) {						
                				    	 tColorTemperData.nICGreen = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue")) {						
                				    	 tColorTemperData.nICBlue = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nRedLow")) {						
                				    	 tColorTemperData.nRedLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nGreenLow")) {						
                				    	 tColorTemperData.nGreenLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nGreenLow")) {						
                				    	 tColorTemperData.nGreenLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nBlueLow")) {						
                				    	 tColorTemperData.nBlueLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRedLow")) {						
                				    	 tColorTemperData.nICRedLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreenLow")) {						
                				    	 tColorTemperData.nICGreenLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlueLow")) {						
                				    	 tColorTemperData.nICBlueLow = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed1")) {						
                				    	 tColorTemperData.nICRed1 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen1")) {						
                				    	 tColorTemperData.nICGreen1 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue1")) {						
                				    	 tColorTemperData.nICBlue1 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed2")) {						
                				    	 tColorTemperData.nICRed2 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen2")) {						
                				    	 tColorTemperData.nICGreen2 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue2")) {						
                				    	 tColorTemperData.nICBlue2 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed6")) {						
                				    	 tColorTemperData.nICRed6 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen6")) {						
                				    	 tColorTemperData.nICGreen6 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue6")) {						
                				    	 tColorTemperData.nICBlue6 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed7")) {						
                				    	 tColorTemperData.nICRed7 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen7")) {						
                				    	 tColorTemperData.nICGreen7 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue7")) {						
                				    	 tColorTemperData.nICBlue7 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed8")) {						
                				    	 tColorTemperData.nICRed8 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen8")) {						
                				    	 tColorTemperData.nICGreen8 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue8")) {						
                				    	 tColorTemperData.nICBlue8 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICRed9")) {						
                				    	 tColorTemperData.nICRed9 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICGreen9")) {						
                				    	 tColorTemperData.nICGreen9 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("nICBlue9")) {						
                				    	 tColorTemperData.nICBlue9 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_0")) {						
                				    	 tColorTemperData.m_bGainEnable_0 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_1")) {						
                				    	 tColorTemperData.m_bGainEnable_1 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_2")) {						
                				    	 tColorTemperData.m_bGainEnable_2 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_3")) {						
                				    	 tColorTemperData.m_bGainEnable_3 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_4")) {						
                				    	 tColorTemperData.m_bGainEnable_4 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_5")) {						
                				    	 tColorTemperData.m_bGainEnable_5 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_6")) {						
                				    	 tColorTemperData.m_bGainEnable_6 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bGainEnable_7")) {						
                				    	 tColorTemperData.m_bGainEnable_7 = Integer.valueOf(valueString); 	 
                				     }

                				     if (nameString.equals("m_bResEnable_0")) {						
                				    	 tColorTemperData.m_bResEnable_0 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_1")) {						
                				    	 tColorTemperData.m_bResEnable_1 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_2")) {						
                				    	 tColorTemperData.m_bResEnable_2 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_3")) {						
                				    	 tColorTemperData.m_bResEnable_3 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_4")) {						
                				    	 tColorTemperData.m_bResEnable_4 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_5")) {						
                				    	 tColorTemperData.m_bResEnable_5 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_6")) {						
                				    	 tColorTemperData.m_bResEnable_6 = Integer.valueOf(valueString); 	 
                				     }
                				     if (nameString.equals("m_bResEnable_7")) {						
                				    	 tColorTemperData.m_bResEnable_7 = Integer.valueOf(valueString); 	 
                				     }
        				    
                				   
							   }
                			 
                		 }
                		 
                		 MapTemperColor.put(TempKey, tColorTemperData);
						}
	                	
	                	
	                	
	                
	             
	          
	        

		return MapTemperColor;
	}

	

	@Override
	public String serialize(HashMap<Integer, ColorTemperData> ColorTemperMap)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
