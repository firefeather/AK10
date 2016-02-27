package com.szaoto.ak10.configuration;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.EDIDCfg;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;

/*
 * 类名 AcqCardSetupActivity
 * 主要功能   采集卡分辨率，偏移量设置
 * 创建者 zhangsj
 * 创建日期2014年8月21日
 * 修改者，修改日期，修改内容
 */
@SuppressLint({ "CutPasteId", "DefaultLocale" })
public class AcqCardSetupActivity  extends Activity implements OnClickListener{

	  TextView tv_Resolution;           //分辨率
	  Spinner sp_reslution;
	  
      TextView tv_frame_frequency;  //帧频   
      Spinner sp_frame;//帧频
      
      TextView tvOffSetX;                 //偏移量x
      TextView tvOffSetY;                 //偏移量y
	  EditText Ed_AcqOffsetx;
	  EditText Ed_AcqOffsety;
		
      TextView activityNameTextView;
 
	  TextView tvOffSetX_value;     //偏移量x值
	  TextView tvOffSetY_value;     //偏移量y值
	  
	  Button btn_Ensure;
	  Button btn_Set;
	  Button btn_Cancel;
	  Button btn_Read;
	  boolean spin_Framsign;
	  
	  byte[] RcvData = new byte[6];
	  byte[] RcvReslutionx = new byte[4];
	  byte[] RcvReslutiony = new byte[4];
	  byte[] RcvFrame = new byte[4];
		 
	  //TextView readReslutionTextView;
	  TextView readFrameTextView;
	  TextView txtVersion;
	  EditText et_ReadAcqcard_width;	//采集卡分辨率宽度
	  EditText et_ReadAcqcard_height;	//采集卡分辨率宽度
	  static int m_nReadWidth;
	  static int m_nReadHeight;
	  
	  //id
      int m_id;
      int g_ledid;
		
      ArrayList<String>  arrResolotionArrayList;
      ArrayList<String>  arrFrameArrayList;
      List<EDIDCfg> m_nListEDIDCfg;
      
      public static int GetReadAcqcardwidth(){
    	  return m_nReadWidth;
      }
      public static int GetReadAcqcardheight(){
    	  return m_nReadHeight;
      }
      
		@Override
     protected void onCreate(Bundle savedInstanceState) {
     	// TODO Auto-generated method stub
     	super.onCreate(savedInstanceState);
     	setContentView(R.layout.leddisplay_capture_cardset);   	
     	SerialPortControlBroadCast.SetCurrentContext(this);
     	spin_Framsign = false;
     	initView();
     	Bundle bundle = this.getIntent().getExtras(); 
		m_id = bundle.getInt("id");
		g_ledid = Ak10Application.GetLedId();
		
		LoadDataFromDb(m_id);
		
		ChnData tChnData = ChannelDB.GetRecordById(m_id, g_ledid);
		
		String strVersion = ChanComm.GetAcqCardSoftwareVersion(tChnData.macaddress);
		
		if (strVersion!=null) {
			txtVersion.setText(strVersion);
		}

		
		activityNameTextView.setText(getString(R.string.tv_capture_card)+ m_id/1000 + "_"+m_id%1000);
     }	

    private void LoadDataFromDb(int id)
	{
    	ChnData tChanData = ChannelDB.GetRecordById(id, g_ledid);
    	
        //offsetx offsety
    	Ed_AcqOffsetx.setText(String.valueOf(tChanData.offsetX));
    	Ed_AcqOffsety.setText(String.valueOf(tChanData.offsetY));   	
    	//分辨率
    	String strResString = tChanData.width+"X"+tChanData.height;
    	String strFrame =String.valueOf(tChanData.frame_freq);
    	m_nReadWidth = tChanData.width;
    	m_nReadHeight = tChanData.height;
    	
    	//分辨率
    	int nSize = arrResolotionArrayList.size();
    	for (int i = 0; i < nSize; i++) {
			if (strResString.equals(arrResolotionArrayList.get(i))) {
				sp_reslution.setSelection(i);
			}
		}
    	
    	//帧频
//   	nSize = arrFrameArrayList.size();
//    	for (int i = 0; i < nSize; i++) {
//			if (strFrame.equals(arrFrameArrayList.get(i))) {
//				sp_frame.setSelection(i);
//			}
//		}

    	int nShowIndex = 0;
    	String strRes = arrResolotionArrayList.get(sp_reslution.getSelectedItemPosition());
		arrFrameArrayList.clear();
		for (EDIDCfg cf : m_nListEDIDCfg) {
			if (0 == strRes.compareTo(cf.m_sResolution)) {	
				arrFrameArrayList.add(String.valueOf(cf.m_iFrame));
				if (Integer.valueOf(strFrame) == cf.m_iFrame) {
					nShowIndex = arrFrameArrayList.size() - 1;
				}
			}
		}
		sp_frame.setSelection(nShowIndex);
	}
		

 //初始化控件
	@SuppressLint("CutPasteId")
	private void initView() {

		tv_Resolution = (TextView) findViewById(R.id.tv_Resolution);
		tv_frame_frequency = (TextView) findViewById(R.id.tv_frame_frequency);
		tvOffSetX = (TextView) findViewById(R.id.tv_p_x);
		tvOffSetY = (TextView) findViewById(R.id.tv_p_y);
		tvOffSetX_value = (TextView) findViewById(R.id.Ed_AcqOffsetx);
		tvOffSetY_value = (TextView) findViewById(R.id.Ed_AcqOffsety);
		Ed_AcqOffsetx = (EditText)findViewById(R.id.Ed_AcqOffsetx);
		Ed_AcqOffsety = (EditText)findViewById(R.id.Ed_AcqOffsety);
		btn_Read = (Button)findViewById(R.id.btnRead);
		btn_Set = (Button)findViewById(R.id.SetEdid);
		activityNameTextView = (TextView)findViewById(R.id.tv_capturecard_set);
		sp_reslution  = (Spinner) findViewById(R.id.sp_Resolution);
		sp_frame = (Spinner) findViewById(R.id.sp_Frame);
		btn_Ensure =(Button) findViewById(R.id.btn_acq_ensure);
		btn_Cancel =(Button) findViewById(R.id.btn_acq_Cancel);
		btn_Ensure.setOnClickListener(this);
		btn_Cancel.setOnClickListener(this);
		btn_Read.setOnClickListener(this);
		btn_Set.setOnClickListener(this);
		
		// 为sp_reslution视图添加setOnItemSelectedListener监听
		sp_reslution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){	
				//DisplayToast("滚动到第"+Long.toString(arg0.getSelectedItemId())+"项");
				
				
				String strRes = arrResolotionArrayList.get(sp_reslution.getSelectedItemPosition());//分辨率 
				String strCurrentFrame = arrFrameArrayList.get(sp_frame.getSelectedItemPosition());//帧频

				int nShowIndex = 0;
				arrFrameArrayList.clear();
				for (EDIDCfg cf : m_nListEDIDCfg) {
					if (0 == strRes.compareTo(cf.m_sResolution)) {	
						arrFrameArrayList.add(String.valueOf(cf.m_iFrame));
						if (Integer.valueOf(strCurrentFrame) == cf.m_iFrame) {
							nShowIndex = arrFrameArrayList.size() - 1;
						}
					}
				}
				sp_frame.setSelection(nShowIndex);
				
			}

			public void onNothingSelected(AdapterView<?> arg0){
				//没有选中
			}
		});
		
		// 为sp_frame视图添加setOnItemSelectedListener监听
		sp_frame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){	
				//DisplayToast("滚动到第"+Long.toString(arg0.getSelectedItemId())+"项");
				
				int nShowIndex = 0;
				String strRes = arrResolotionArrayList.get(sp_reslution.getSelectedItemPosition());//分辨率 
				String strFrame = arrFrameArrayList.get(sp_frame.getSelectedItemPosition());//帧频
				arrResolotionArrayList.clear();
				for (EDIDCfg cf : m_nListEDIDCfg) {
					if (Integer.valueOf(strFrame) == cf.m_iFrame) {
						arrResolotionArrayList.add(cf.m_sResolution);
						if (0 == strRes.compareTo(cf.m_sResolution)) {
							nShowIndex = arrResolotionArrayList.size() - 1;
						}
					}
				}
				sp_reslution.setSelection(nShowIndex);

			}

			public void onNothingSelected(AdapterView<?> arg0){
				//没有选中
			}
		});
		
		
		//readReslutionTextView = (TextView)findViewById(R.id.ResolutionRead);
		readFrameTextView = (TextView)findViewById(R.id.FrameRead);		
		txtVersion = (TextView) findViewById(R.id.txtVersionDetail);
		et_ReadAcqcard_width = (EditText)findViewById(R.id.Sp_acqcard_w);
		et_ReadAcqcard_height = (EditText)findViewById(R.id.Sp_acqcard_h);
		
		//初始化设置分辨率下拉列表
		arrResolotionArrayList = null;
		arrResolotionArrayList = new ArrayList<String>();
		
		/*
		arrResolotionArrayList.add("640X480");
		arrResolotionArrayList.add("800X600");
		arrResolotionArrayList.add("1024X768");
		arrResolotionArrayList.add("1280X800");
		arrResolotionArrayList.add("1280X1024");
		arrResolotionArrayList.add("1440X900");
		arrResolotionArrayList.add("1440X1080");
		arrResolotionArrayList.add("1600X900");
		arrResolotionArrayList.add("1600X1200");
		arrResolotionArrayList.add("1920X1080");
		arrResolotionArrayList.add("1920X1200");
		arrResolotionArrayList.add("2048X1152");
		arrResolotionArrayList.add("2048X2160");
		arrResolotionArrayList.add("3840X2160");
		arrResolotionArrayList.add("2976X3348");
		arrResolotionArrayList.add("2560X1600");
		arrResolotionArrayList.add("4096X2160");
		*/
		
		//EDID标准用的分辨率：
//		arrResolotionArrayList.add("640X480");
//		arrResolotionArrayList.add("800X600");
//		arrResolotionArrayList.add("1024X768");
//		arrResolotionArrayList.add("1280X800");
//		arrResolotionArrayList.add("1440X900");
//		arrResolotionArrayList.add("1440X1080");
//		arrResolotionArrayList.add("1600X900");
//		arrResolotionArrayList.add("1600X1200");
//		arrResolotionArrayList.add("1920X1080");
//		arrResolotionArrayList.add("1920X1200");
//		arrResolotionArrayList.add("2048X1152");
		{
			EdidSet Editset = new EdidSet("1920X1080",30);
			Editset.AnalyseEDIDCfgFile(1);
			List<EDIDCfg> nListEdidCfgs = Editset.GetListEdidCfgs();
			m_nListEDIDCfg = nListEdidCfgs;
			for (EDIDCfg cf : nListEdidCfgs) {
				if (cf.m_iSupportValue < 10) {
					continue;
				}
				
				//·Ö±æÂÊ
				int i = 0;
				int iArrCnt = arrResolotionArrayList.size();
				for (i = 0; i < iArrCnt; i++) {
					String strTmp = arrResolotionArrayList.get(i);
					if (0 == strTmp.compareTo(cf.m_sResolution)) {
						break;
					}
				}
				if (i == iArrCnt) {
					arrResolotionArrayList.add(cf.m_sResolution);
				}
			}
		}

		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrResolotionArrayList);
		sp_reslution.setAdapter(adapter);	

		
		//初始化设置帧频下拉列表
		arrFrameArrayList  = new ArrayList<String>();
		
//		arrFrameArrayList.add("30");
//		arrFrameArrayList.add("50");
//		arrFrameArrayList.add("56");
//		arrFrameArrayList.add("60");
//		arrFrameArrayList.add("70");
//		arrFrameArrayList.add("72");
//		arrFrameArrayList.add("75");
//		arrFrameArrayList.add("85");
//		arrFrameArrayList.add("100");

		String strRes = arrResolotionArrayList.get(sp_reslution.getSelectedItemPosition());
		arrFrameArrayList.clear();
		for (EDIDCfg cf : m_nListEDIDCfg) {
			if (0 == strRes.compareTo(cf.m_sResolution)) {	
				arrFrameArrayList.add(String.valueOf(cf.m_iFrame));
			}
		}
		
		ArrayAdapter<String> adapterFrame= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrFrameArrayList);
		sp_frame.setAdapter(adapterFrame);	
	}
	
	public class Readinfo{
		public int nFrame;
		public String strRes;
		
	}

	class  ResAndFrameReadTask extends AsyncTask<Void, Void, Readinfo > {

		ProgressDialog dialog=null;
		
		@Override
		protected void onPreExecute() {
			
			if (dialog==null) {
				dialog=ProgressDialog.show(AcqCardSetupActivity.this, "",getString(R.string.log_config_dataisloading));
				dialog.show();
			}
			
			super.onPreExecute();
		}
		
		@Override
		protected Readinfo doInBackground(Void... arg0) {
			
			Readinfo readinfo = new Readinfo();
			
			ChnData tChanData =  ChannelDB.GetRecordById(m_id, g_ledid);
			
			String strtReslution = null;
			strtReslution=	ChanComm.GetAcqCardResolotion(tChanData.videosourceid,tChanData.Id%1000);
			int nFrame = 0;
			try {
				nFrame = ChanComm.GetAcqCardFrame(tChanData.videosourceid,tChanData.Id%1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			readinfo.strRes=strtReslution;
			readinfo.nFrame=nFrame;
			
			return readinfo; 
		}
		// TODO Auto-generated method stub
		@Override
		protected void onPostExecute(Readinfo readInfo) {		
			if (dialog!=null&&dialog.isShowing()) {
				dialog.dismiss();
				dialog=null;
			}
			
			//readReslutionTextView.setText(readInfo.strRes);
			readFrameTextView.setText(String.valueOf(readInfo.nFrame));

			String[] strResCfg = readInfo.strRes.split("X");
			String strWidthString = strResCfg[0];
			String strHeightString = strResCfg[1];		 
			et_ReadAcqcard_width.setText(strWidthString);
			et_ReadAcqcard_height.setText(strHeightString);
			
			m_nReadWidth = Integer.valueOf(strWidthString);
			m_nReadHeight = Integer.valueOf(strHeightString);
	      
			super.onPostExecute(readInfo);
		}
	}


	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View v) {
     switch (v.getId()) {
		case R.id.btn_acq_ensure:
		
			 String strX = Ed_AcqOffsetx.getText().toString();
			 String strY = Ed_AcqOffsety.getText().toString();
			 
			 if (strX.equals("")||strY.equals("")) {
				 Toast.makeText(this,getString(R.string.offsetnull), Toast.LENGTH_SHORT).show();
				 return;
			 }
			
			 String strRes = arrResolotionArrayList.get(sp_reslution.getSelectedItemPosition());
			 
			 String strReadX = et_ReadAcqcard_width.getText().toString();
			 String strReadY = et_ReadAcqcard_height.getText().toString();
			 
			 String[] strResCfg = strRes.split("X");			 
			 String strWidthString = "";
			 String strHeightString = "";
			 if (0 == strReadX.compareTo("") || 0 == strReadY.compareTo("")) {
				 strWidthString = strResCfg[0];
				 strHeightString = strResCfg[1];
			 } else {
				 if (Integer.valueOf(strReadX) > 0 && Integer.valueOf(strReadY) > 0
					 && Integer.valueOf(strReadX) < Integer.valueOf(strResCfg[0]) 
					 && Integer.valueOf(strReadY) < Integer.valueOf(strResCfg[1]) ) {
					 strWidthString = strReadX;
					 strHeightString = strReadY;
				 } else { 
					 strWidthString = strResCfg[0];
					 strHeightString = strResCfg[1];
				 }
			 }
			 
			 ChannelDB.UpdateChannelPosParam(m_id, Integer.valueOf(strX),
					 Integer.valueOf(strY), Integer.valueOf(strWidthString), Integer.valueOf(strHeightString), g_ledid);
	    
			 //帧频
			 String strFrame = arrFrameArrayList.get(sp_frame.getSelectedItemPosition());
			 ChannelDB.UpdateFrame(m_id, Integer.valueOf(strFrame), g_ledid);
			 
	         finish();
	         
			 break;
			
		case R.id.btn_acq_Cancel:
			finish();
			break;
		case R.id.SetEdid:
		{
			String tempreslution1 = sp_reslution.getSelectedItem().toString();
			int frame1 = Integer.parseInt(sp_frame.getSelectedItem().toString());
			
			ChnData tChanData =  ChannelDB.GetRecordById(m_id, g_ledid);
			
			EdidSet Editset = new EdidSet(tempreslution1,frame1);	
			
			int nResultset1 = Editset.SetResolutionAndFrame(tChanData.Id%1000,tChanData.macaddress,1);
			
			if (-1 < nResultset1) {
				Toast.makeText(getApplicationContext(),R.string.text_set_success, Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(getApplicationContext(),R.string.text_set_failed, Toast.LENGTH_LONG).show();
			}
			
			
			break;
		}
		case R.id.btnRead:
		    new ResAndFrameReadTask().execute();
			break;	
		default:
			break;
		}	
	}
}
