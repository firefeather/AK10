package com.szaoto.ak10.dataaccess;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.EDIDCfg;

public  class DataAccessAcquisitionCardEdidCfg {
	private final static String sFileFlagString = "Edid.cfg";
	
	//AcquisitionCardActivity context;
	Context context;
	private byte [] buffer;
	List<EDIDCfg> m_edidcfg;
	
	public DataAccessAcquisitionCardEdidCfg( int type )//type == 0 为AcqCardSetupActivity,
																		//==2时为LedDisplayConfigActivity   ==1时 AcquisitionCardActivity
	{
		switch (type) {
		case 0:
			DataAccessAcquisitionCardGetEdidcfgs();
			break;
		case 1:
			DataAccessAcquisitionCardGetEdidcfgs();
			break;
		case 2:
			DataAccessAcquisitionCardGetEdidcfgs();
			break;
		default:
			break;
		}
	}	

	public byte[] readFile(String fileName) throws IOException {    
			int length = 0;
			//InputStream in = context.getResources().getAssets().open(fileName);
			InputStream is = new FileInputStream(fileName);
			length = is.available();           
			buffer = new byte[length]; 
			is.read(buffer);
			is.close(); 
	//		res = EncodingUtils.getString(buffer, "UTF-8");       
		if(length >=100)
			return buffer;
		else 
			return null;	
	}    
	public List<EDIDCfg> DataAccessAcquisitionCardGetEdidcfgs()
	{
		try{
			readFile(HomePageActivity.CONFIG_PATH+sFileFlagString);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		String str = EncodingUtils.getString(buffer, "UTF-8");
		int lengthvaild;
		int index = str.lastIndexOf(';');
		lengthvaild= str.length() - index;
		String temp = str.substring(index);
		index = temp.indexOf('\n');
		temp = temp.substring(index+1);
		lengthvaild= lengthvaild - index;
		int lastposition = 0;
		lastposition = temp.indexOf('\n');
		//List<EDIDCfg> m_edidcfg = null ;
		m_edidcfg= new ArrayList<EDIDCfg>();
		while(lastposition != -1)
		{
			//lastposition = temp.indexOf('\n');//是否抛出异常
			
			String linestring = temp.substring(0,lastposition);
			//linestring = linestring.replaceAll(" ", "");
			int startindex = 0;
			EDIDCfg editcfg = new EDIDCfg()  ; //new EDIDCfg;
			int k=0;
			int count = linestring.length();
			for(int i = 0;i < count; i++)
			{
			//	m_edidcfg
				String str1 = new String("");
				if((linestring.charAt(i) == ' ' && linestring.charAt(i+1)!=' ')||i == linestring.lastIndexOf(' ')||i >count-3)
				{
					if(linestring.charAt(i) == ' ' && linestring.charAt(i+1)!=' ')
					{   k++;
						str1 = linestring.substring(startindex, i); 
						str1=  str1.substring(0,str1.indexOf(' '));
					}
				 // str1.replaceAll(" ", "");
					if(i > count -3)
					{
						k++;
						str1 = linestring.substring(linestring.lastIndexOf(' ')+1);
					}
					/*if( i == linestring.lastIndexOf(' '))
					{
						if(i > count-3)
						{
						k++;
						str1 = linestring.substring(i+1); 
						}
					//	str1=  str1.substring(0,str1.indexOf(' '));
					}*/
				  switch(k-1)
				  {
				  case 0://分辨率
						editcfg.setM_sResolution(str1) ;
						break;
					case 1://帧率
						Integer value =Integer.parseInt(str1,10);
						editcfg.m_iFrame = value.intValue();
						break;
					case 2://水平遮没点数(Horizontal Blanking)
						Integer value1 =Integer.parseInt(str1,10);
						editcfg.m_iHBlanking = value1.intValue();
						break;
					case 3://水平同步偏移H.Sync Offset
						Integer value2 =Integer.parseInt(str1,10);
						editcfg.m_iHSyncOffset = value2.intValue();
						break;
					case 4://水平同步脉冲宽度H.Sync Pulse Width
						Integer value3 =Integer.parseInt(str1,10);
						editcfg.m_iHSyncPulseWidth = value3.intValue();
						break;
					case 5://垂直遮没线数(Vertical Blanking)
						Integer value4 =Integer.parseInt(str1,10);
						editcfg.m_iVBlanking = value4.intValue();
						break;
					case 6://垂直同步偏移V.Sync Offset
						Integer value5 =Integer.parseInt(str1,10);
						editcfg.m_iVSyncOffset = value5.intValue();
						break;
					case 7://垂直同步脉冲宽度V.Sync Pulse Width
						Integer value6 =Integer.parseInt(str1,10);
						editcfg.m_iVSyncPulseWidth = value6.intValue();
						break;
					case 8://是否支持AK100和AK1000采集卡
						Integer value7 =Integer.parseInt(str1,10);
						editcfg.m_iSupportValue = value7.intValue();
					default:
						break;
				  }  
				  startindex = i+1;
				}	
			//处理每一行信息	
			}
			m_edidcfg.add(editcfg);
			lastposition = temp.indexOf('\n');
			temp = temp.substring(lastposition +1);				
		}
		return m_edidcfg;
	}
	public ArrayAdapter<String> GetResolutionAdapter( )
	{
	//	ArrayAdapter<String> adapterResolution = new ArrayAdapter<String>()  ;
		ArrayAdapter<String> adapterResolution = new ArrayAdapter<String>(context, R.layout.style_spinner);
		for(EDIDCfg edidfg :m_edidcfg)
		{
			String temp = edidfg.m_sResolution;
			if (false == temp.equals("")) {
				adapterResolution.add(temp);
			}
		}	
		return adapterResolution;
	}
	public ArrayAdapter<String> GetFrameAdapterfromResolution(String str )
	{
		ArrayAdapter<String> FramefromResolution = new ArrayAdapter<String>(context, R.layout.style_spinner);
		if(str == null)
		{
			FramefromResolution.add("-1111");
			return FramefromResolution;
		}
		//ArrayAdapter<String> FramefromResolution;
		for(EDIDCfg edidfg :m_edidcfg)
		{
			String temp = edidfg.m_sResolution;
			if(str.equals(temp))
			{
				String strTmp = String.valueOf(edidfg.m_iFrame);
				FramefromResolution.add(strTmp);
			}
		}	
		return FramefromResolution;
	}
	
	
}

