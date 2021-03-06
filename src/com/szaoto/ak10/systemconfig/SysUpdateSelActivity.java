package com.szaoto.ak10.systemconfig;

import java.io.FileInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.sqlitedata.CardInfoDB;
import com.szaoto.ak10.util.TraverseDictionary;
import com.szaoto.ak10.util.UtilFun;

public class SysUpdateSelActivity extends Activity {

	TextView txt_TitleFile;
	TextView txt_TitleCard;
	
	ListView listview_File;
	ListView listView_Card;
	Button   btnSubmitButton;
	Button   btnClose;
	
	ArrayList<String> ArrPathInfos = new ArrayList<String>();
	ArrayList<CardInformation> m_CardInfoSelArrayList = new ArrayList<CardInformation>();
	
	String   m_strFilePath;
	ArrayList<CardInformation> m_CardinfomationArrayList = new ArrayList<CardInformation>();
	
	int      nCardStyle;
	private String Usb_PATH;
	private String sBinfile = ".bin";
	

	
	CustomProgressDialog mProgressDialog;
	//ProgressDialog mProgressDialog ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_update_sel);
		SerialPortControlBroadCast.SetCurrentContext(this);
		txt_TitleFile = (TextView) findViewById(R.id.textViewPathTitile);
		txt_TitleCard = (TextView) findViewById(R.id.textViewUpdateItemSel);
		listview_File = (ListView) findViewById(R.id.listViewFilePath);
		listView_Card = (ListView) findViewById(R.id.listViewUpdateItemSel);
		btnSubmitButton = (Button) findViewById(R.id.btnSubmit);
		btnClose        =  (Button) findViewById(R.id.btnClose);
		//系统正在升级中，请稍后....
		mProgressDialog = new CustomProgressDialog(this, getString(R.string.text_system_update),
				getString(R.string.text_system_updating),false);
	
		nCardStyle = getIntent().getIntExtra("cardtype", 1);
		
		txt_TitleFile.setTextSize(25);
		txt_TitleCard.setTextSize(25);
		
		txt_TitleFile.setTextColor(Color.RED);
		txt_TitleCard.setTextColor(Color.RED);
		
		listview_File.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				m_strFilePath = ArrPathInfos.get(position);
			}
		});
		
		btnClose.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		listView_Card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
			
		});
		
		//升级确定
		btnSubmitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//文件路径
				m_CardInfoSelArrayList = new ArrayList<CardInformation>();
				SparseBooleanArray sparseBooleanArray = listView_Card.getCheckedItemPositions();
				int nSize = m_CardinfomationArrayList.size();
				for (int i = 0; i < nSize; i++) {					
					if (sparseBooleanArray.get(i)) {
						m_CardInfoSelArrayList.add(m_CardinfomationArrayList.get(i));
					}					
				}
				
				if (m_strFilePath==null) {			
					Toast.makeText(btnSubmitButton.getContext(),R.string.text_fileselecttips, Toast.LENGTH_LONG).show();
					return;
				}
				
				String strFilepathString = m_strFilePath;				
				//需要升级阿德卡的信息
				ArrayList<byte[]> tArrayList = new ArrayList<byte[]>();
				nSize = m_CardInfoSelArrayList.size();
				for (int i = 0; i < nSize; i++) {
					
					CardInformation tCardInfo = m_CardInfoSelArrayList.get(i);					
					byte[] Macaddress = tCardInfo.getUcMACAddress();					
					tArrayList.add(Macaddress);					
				} 
				
				if (tArrayList.size()==0) {
					
					Toast.makeText(btnSubmitButton.getContext(),"No Card Select", Toast.LENGTH_LONG).show();
					return;
				}
				
				DataUpgradeTask tDaskUpgradeTask = new DataUpgradeTask(strFilepathString,tArrayList);//发送卡为2
				tDaskUpgradeTask.execute();
				//progUpgradeDiag.show();
				mProgressDialog.show();
			}
		});
		
		InitData();
		
	}

	class DataUpgradeTask extends AsyncTask<Integer, Integer, String>{
		  String filename  ;
		  ArrayList<byte[]>  m_ArrMacAddress  = new ArrayList<byte[]>();
		  int packNum ;
		  
		  public DataUpgradeTask(String filename, ArrayList<byte[]>  MacAddress) {
			 this.filename = filename;
			 this.m_ArrMacAddress = MacAddress;
	     }
		  
		@Override
		protected String doInBackground(Integer... params) {
		int nSize = m_ArrMacAddress.size();
		for (int j = 0; j < nSize; j++) {
			
			byte[] tMac = m_ArrMacAddress.get(j);
			
			byte[] ucAddressFIFO = new byte[2];
			ucAddressFIFO[0]=0x10;
			ucAddressFIFO[1]=(byte) 0xf0;
			
	  	    byte[] ucSendData = new byte[1024 + 22];
			FileInputStream fin = null;
			
			try{
	
			    fin = new FileInputStream(filename);
			    int length = fin.available();

			    
			    int nTmp = length%1024;
			    if(nTmp==0)
			    {
				   packNum = length/1024;
			    }else {
			       packNum = length/1024+1;
				}
			    

			    
			    byte[] bytes2transfer = new byte[length];    
			    fin.read(bytes2transfer);
    
		   
		       
		       
		        //开始升级，写0x80到0x0F0
				byte[] ucAddressReg = new byte[2];
				ucAddressReg[0]=0x00;
				ucAddressReg[1]=(byte) 0xf0;
				byte[] DataInit = new byte[1];
				DataInit[0]=(byte) 0x80;
				ucSendData=Packager.EthernetPackDataWrite(tMac, ucAddressReg, 0x00, 1, DataInit);
				SpiControl.WriteSpi(ucSendData, ucSendData.length);
		        
				try 
				{
					
					if (bytes2transfer.length<=1024) {
						ucSendData = Packager.EthernetPackDataWrite(tMac, ucAddressFIFO, 0, bytes2transfer.length, bytes2transfer);
				    	SpiControl.WriteSpi(ucSendData, ucSendData.length);
					}
					else 
					{
						    int i = 0;
						    for (;i < bytes2transfer.length; i+=1024) {				    	
						    	byte[] tmpBytes;
						    	if (bytes2transfer.length-i<1024) {
						    		tmpBytes = UtilFun.CopyOfRange(bytes2transfer, i, bytes2transfer.length);
								}else {
									tmpBytes = UtilFun.CopyOfRange(bytes2transfer, i, i+1024);
								}
						    	ucSendData = Packager.EthernetPackDataWrite(
						    			tMac, ucAddressFIFO, 0, 1024, tmpBytes);
						    	SpiControl.WriteSpi(ucSendData, ucSendData.length);
						    	
						    	//3、	读reg地址0x0F0，直至bit0 = 0
						    	int tProgress = i/1024 + 1;
						    	publishProgress(tProgress);
						    	while(true)
						    	{

						    		try {
						    			ucSendData = Packager.EthernetPackDataRead(tMac,ucAddressReg,1);
								    	SpiControl.WriteSpi(ucSendData, ucSendData.length);
							    		byte[] byteRcv = SpiControl.ReadSpi(64);
							    		if (byteRcv==null) {
											continue;
										}
							    		if (byteRcv.length!=0) {
							    			
								    		int nResult = byteRcv[18]&0x01;
								    		if (nResult == 0) {
								    			// 
												break;
											}
								    		
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
						    	}
//						    	
							}
						    
		
						}	
				        //结束升级，写0x40到0x0F0
						DataInit[0]=(byte) 0x40;
						ucSendData=Packager.EthernetPackDataWrite(tMac, ucAddressReg, 0x00, 1, DataInit);
				        SpiControl.WriteSpi(ucSendData, ucSendData.length);
			    
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
		
		        
			    }
			    catch(Exception e)
			    { 
			           e.printStackTrace(); 
			    }
	   }
		
	     return null;
	}
		
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        int value = values[0];  
        double fMsg = ((double)value)/packNum;
        		
        mProgressDialog.SetTextMsg(getString(R.string.text_system_updating) + (int)(fMsg*100) + "%");
    }  
		
	@Override
	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();	  
		new  AlertDialog.Builder(btnSubmitButton.getContext())    
		                .setTitle(R.string.text_update_success )  
		                .setMessage(R.string.text_updatesuccess_reset)  
		                .setPositiveButton(R.string.OK,  null )  
		                .show();  	
	    super.onPostExecute(result);
	}
	}
	
    public void InitData()
    {
    	InitPathList();
    	InitItemList();
    }
	public void InitPathList()
	{
		TraverseDictionary TD = new TraverseDictionary();
		Usb_PATH=TraverseDictionary.GetUDiskDir();
		TD.GetFilePaths(Usb_PATH, sBinfile , true);
		ArrPathInfos = (ArrayList<String>) TD.getLstFilePath();
		listview_File.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,ArrPathInfos)); 
		listview_File.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	public void InitItemList()
	{
		ArrayList<String> ArrCardInfos = new ArrayList<String>();
		//采集卡
		if (1 == nCardStyle) {	
		   
		   m_CardinfomationArrayList =	CardInfoDB.GetAcqRecords();
		   int nSize = m_CardinfomationArrayList.size();
		   for (int i = 0; i < nSize; i++) {
			   String strInfoString;	
			   CardInformation tCardInformation = m_CardinfomationArrayList.get(i);
			   strInfoString = "AcqCard Positon:"+tCardInformation.getnSlotID();
			   byte[] mac = tCardInformation.getUcMACAddress();
			   String strmac = mac[0]+"-"+mac[1]+"-"+mac[2]+"-"+ mac[3]+"-"+mac[4]+"-"+mac[5];
			   strInfoString+= "                                   Mac:"+strmac;
			   
			   ArrCardInfos.add(strInfoString);
		   }
		   
		   listView_Card.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,ArrCardInfos));
 
		   listView_Card.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		} else if (2 == nCardStyle) {
			   
			   m_CardinfomationArrayList =	CardInfoDB.GetSndRecords();
			   int nSize = m_CardinfomationArrayList.size();
			   for (int i = 0; i < nSize; i++) {
				   String strInfoString;	
				   CardInformation tCardInformation = m_CardinfomationArrayList.get(i);
				   strInfoString = "SendCard Position:"+tCardInformation.getnSlotID();
				   byte[] mac = tCardInformation.getUcMACAddress();
				   String strmac = mac[0]+"-"+mac[1]+"-"+mac[2]+"-"+ mac[3]+"-"+mac[4]+"-"+mac[5];
				   strInfoString+= "                                Mac:"+strmac;
				   
				   ArrCardInfos.add(strInfoString);
			   }
			   
			   listView_Card.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,ArrCardInfos));
			   listView_Card.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		} else if (3 == nCardStyle) {
			   
			   m_CardinfomationArrayList =	CardInfoDB.GetSysCardRecords();
			   int nSize = m_CardinfomationArrayList.size();
			   for (int i = 0; i < nSize; i++) {
				   String strInfoString;	
				   CardInformation tCardInformation = m_CardinfomationArrayList.get(i);
				   strInfoString = "SysCard Position:"+tCardInformation.getnSlotID();
				   byte[] mac = tCardInformation.getUcMACAddress();
				   String strmac = mac[0]+"-"+mac[1]+"-"+mac[2]+"-"+ mac[3]+"-"+mac[4]+"-"+mac[5];
				   strInfoString+= "                                Mac:"+strmac;
				   
				   ArrCardInfos.add(strInfoString);
			   }
			   
			   listView_Card.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,ArrCardInfos));
			   listView_Card.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		
	}

}
