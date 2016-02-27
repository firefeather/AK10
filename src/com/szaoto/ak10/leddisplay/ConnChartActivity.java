/*
   * 文件名 ConnectoinActivity.java
   * 包含类名列表com.szaoto.ak10.leddisplay
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:52:52
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.leddisplay;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.datacomm.ConnChartComm;
import com.szaoto.ak10.ownerdraw.CabinetViewObj;
import com.szaoto.ak10.ownerdraw.ConnectChartCustomView;
import com.szaoto.ak10.ownerdraw.InterfaceViewObj;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.sqlitedata.CbtData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
/*
 * 类名ConnectoinActivity
 * 作ConnectoinActivity* 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 * 修改者：zhangsj
 * 修改日期：2014年7月22日
 * 修改内容：添加连线图
 */
public class ConnChartActivity extends FragmentActivity implements OnClickListener {


	 TextView txt_Port;
	 TextView txt_Save;
	 TextView txt_HS;
	 TextView txt_VS;
	 TextView txt_HZ;
	 TextView txt_VZ;


	 Button btn_connectiont_Scal;
	 Button btn_ShowLine;
	 

	 Context context;
	
	 public int gLEDid;

	

	 RelativeLayout zoomButtonShow;
	 ZoomControls mZoomControls;
	
	 LinearLayout linear_top;
	 LinearLayout linear_menu;
	 LinearLayout linear_tool;
	


	 private RelativeLayout zoomSmallShow;
	 private Button zoomToSmall;
	 Button btn_Show_Chart;
	 Button btn_Hide_Chart;
	
	 TextView txt_back;
	 TextView txt_FullScreen;
	 TextView txt_ZoomIn;
	 TextView txt_ZoomNormal;
	 TextView txt_ZoomOut;
	 TextView txt_ShowAll;
	
	//对应的interface	
	 private PopupWindow popupwindow;
	 InterfaceViewObj gInterfaceView;
	 TextView txt_ConnectMore;
	 TextView txt_SendConnectChart;
	 TextView txt_SaveConnectChart;
	 Button btn_SaveCabinetParameter;
	 Button btn_SendCabinetParameter;
	


	 private TextView ledConnectText;
	 private RelativeLayout layout1_root;
	
	
	ConnectChartCustomView m_ConnChartCustomView =null;
	
	//进度条显示
	private CustomProgressDialog mSendCabinetDialog;//发送箱体参数	
//	private CustomProgressDialog mSaveCabinetDialog;//保存箱体参数
//	private CustomProgressDialog mSendConnectDialog;//发送连线图
//	private CustomProgressDialog mSaveConnectDialog;//保存连线图
	private int m_IntfId;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SerialPortControlBroadCast.SetCurrentContext(this);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.leddisplay_connection);
		initView();
	    Intent tIntent = getIntent();
	   
	    Bundle bl= tIntent.getExtras();
	    m_IntfId =  bl.getInt("intfId");
	   
	    gLEDid = Ak10Application.GetLedId();
	    //需要操作的view类
       
		InitCabinetViews(m_IntfId);	
		
		mSendCabinetDialog = new CustomProgressDialog(this,getString(R.string.leddisplay_send_cabinetparameter),
				getString(R.string.text_send_cbtparameterstate),false);
		/*
		mSaveCabinetDialog = new CustomProgressDialog(this,getString(R.string.leddisplay_save_cabinetparameter),
				getString(R.string.text_savecabinetdata),false);	
		mSendConnectDialog = new CustomProgressDialog(this,getString(R.string.leddisplay_send_connection),
				getString(R.string.text_send_connection),false);	
		mSaveConnectDialog = new CustomProgressDialog(this,getString(R.string.leddisplay_save_connection),
				getString(R.string.text_save_connection),false);	
				*/

		//InitCabinetViews();
	
	}
	
	/**
	 * 数据加载进程
	 */
	class SendCbtParamTask extends AsyncTask<Integer, Integer, String>{
  
		 public SendCbtParamTask() {
			 
	     }
		 
		 @Override
		 protected String doInBackground(Integer... params) {
			SendCbtParamData();	
			return null;
		 }
		
		@Override
		protected void onPostExecute(String result) {		
			mSendCabinetDialog.dismiss();
			super.onPostExecute(result);
		}
	}
	/*
	class SaveCbtParamTask extends AsyncTask<Integer, Integer, String>{
		  
		 public SaveCbtParamTask() {
			 
	     }
		 
		 @Override
		 protected String doInBackground(Integer... params) {
			SaveCbtParamData();	
			return null;
		 }
		
		@Override
		protected void onPostExecute(String result) {		
			mSaveCabinetDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	class SendConnectTask extends AsyncTask<Integer, Integer, String>{  
		 public SendConnectTask() {
			 
	     }
		 
		 @Override
		 protected String doInBackground(Integer... params) {
			SendConnectData();
			return null;
		 }
		
		@Override
		protected void onPostExecute(String result) {		
			mSendConnectDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	class SaveConnectTask extends AsyncTask<Integer, Integer, String>{  
		 public SaveConnectTask() {
			 
	     }
		 
		 @Override
		 protected String doInBackground(Integer... params) {
			SaveConnectData();
			return null;
		 }
		
		@Override
		protected void onPostExecute(String result) {		
			mSaveConnectDialog.dismiss();
			super.onPostExecute(result);
		}
	}
	*/
    private void initView() {
    	
    	linear_top = (LinearLayout) findViewById(R.id.conntop);
    	linear_menu = (LinearLayout) findViewById(R.id.connmenu);
    	linear_tool = (LinearLayout) findViewById(R.id.conntool);
    	
    	txt_SendConnectChart = (TextView) findViewById(R.id.text_sendconnect);//发送连线图
    	txt_SaveConnectChart = (TextView) findViewById(R.id.text_save_connection);//保存连线图
    	txt_back = (TextView) findViewById(R.id.text_connectback);
    	txt_HS = (TextView)  findViewById(R.id.text_HS);
    	txt_VS = (TextView) findViewById(R.id.text_VS);
    	txt_HZ = (TextView) findViewById(R.id.text_Hz);
    	txt_VZ = (TextView) findViewById(R.id.text_vz);

		txt_ShowAll = (TextView) findViewById(R.id.texttool_showall);
		txt_FullScreen = (TextView) findViewById(R.id.texttool_full);
		txt_ZoomIn = (TextView) findViewById(R.id.textZoomin);
		txt_ZoomNormal = (TextView) findViewById(R.id.textZoomnormal);
		txt_ZoomOut = (TextView) findViewById(R.id.textZoomout);
		txt_ConnectMore = (TextView) findViewById(R.id.text_more);
		ledConnectText = (TextView) findViewById(R.id.text_connect_setup);
		//获取LED大屏ID号
		layout1_root = (RelativeLayout) findViewById(R.id.connection_layout_root );
		ledConnectText.setText(">"+"LED"+Ak10Application.GetLedId()+">");
		//Interface端口号
		txt_Port = (TextView) findViewById(R.id.spin_connect_Port);
		
		txt_back.setOnClickListener(this);
		txt_HS.setOnClickListener(this);
		txt_VS.setOnClickListener(this);
		txt_HZ.setOnClickListener(this);
		txt_VZ.setOnClickListener(this);

		txt_ShowAll.setOnClickListener(this);
		txt_FullScreen.setOnClickListener(this);
		txt_ZoomIn.setOnClickListener(this);
		txt_ZoomNormal.setOnClickListener(this);
		
		txt_ZoomOut.setOnClickListener(this);
		txt_ConnectMore.setOnClickListener(this);
		txt_SendConnectChart.setOnClickListener(this);
		txt_SaveConnectChart.setOnClickListener(this);
		
		m_ConnChartCustomView = (ConnectChartCustomView) findViewById(R.id.connectchartcustomview);
	}


    public void SendCbtParamData(){
    	ConnChartComm.SendCabinetParam(m_IntfId, gLEDid, m_ConnChartCustomView.gAddressedCbtList);
    }
    /*
    public void SaveCbtParamData(){
    	ConnChartComm.SaveCabinetParam(m_IntfId, gLEDid);
    }
    public void SendConnectData(){
    	ConnChartComm.SendConnChart(m_IntfId, gLEDid, m_ConnChartCustomView.gAddressedCbtList);
    }
    public void SaveConnectData(){
    	ConnChartComm.SaveConnChart(m_IntfId, gLEDid);
    }*/

	@Override
	public  void  onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.text_connectback:
			 finish();
			break;
		case R.id.text_more:
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				initmPopupWindowView();
				popupwindow.showAsDropDown(v, 10, 15);
			}
			break;


		case R.id.texttool_showall:
			m_ConnChartCustomView.FitToZoom();
			break;
		case R.id.texttool_full:
			HideView();
			showZoom();
		
			break;
		case R.id.textZoomin:
			m_ConnChartCustomView.ZoomIn();
			
			break;
		case R.id.textZoomnormal:
			m_ConnChartCustomView.ZoomNormal();
			
			break;
		case R.id.textZoomout:
			m_ConnChartCustomView.ZoomOut();
			break;

		case R.id.spin_connect_Port:
			break;
		//水平Z字形连线方式	
	    case R.id.text_Hz:
		{
			setLineStyle(2);
			break;
		}
		//垂直Z字形连线方式	
	    case R.id.text_vz:
	    {
	    	setLineStyle(3);
			break;
		}
	    //水平S字形连线方式	
	    case R.id.text_HS:
	    {
	    	setLineStyle(0);
			break;
		}
	 	//垂直S字形连线方式	
	    case R.id.text_VS:
	    { 
	    	setLineStyle(1);
	    	break;
	    }
	    
	    case R.id.btn_send_cabinet: // 发送箱体参数
	    	
	    	SendCbtParamTask tTaskSendParamTask = new SendCbtParamTask();  	
	    	tTaskSendParamTask.execute();  	
	    	mSendCabinetDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_send_cabinetparameter, Toast.LENGTH_SHORT).show();
			break ;
		case R.id.btn_save_cabinet_parameter: // 保存箱体参数
			ConnChartComm.SaveCabinetParam(m_IntfId, gLEDid);
			Toast.makeText(getApplication(), R.string.leddisplay_save_cabinetparameter, 10).show();
			/*
	    	SaveCbtParamTask tTaskSaveParamTask = new SaveCbtParamTask();  	
	    	tTaskSaveParamTask.execute();  	
	    	mSaveCabinetDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_save_cabinetparameter, Toast.LENGTH_SHORT).show();
			*/
			break ;

		case R.id.text_sendconnect: //发送连线图	
			ConnChartComm.SendConnChart(m_IntfId, gLEDid, m_ConnChartCustomView.gAddressedCbtList);
			Toast.makeText(getApplication(), R.string.leddisplay_send_connection, Toast.LENGTH_SHORT).show();
			//SendConnectTask tTaskSendConnectTask = new SendConnectTask();  	
	    	//tTaskSendConnectTask.execute();  	
	    	//mSendConnectDialog.show();
			//Toast.makeText(getApplication(), R.string.leddisplay_send_connection, Toast.LENGTH_SHORT).show();

			break; 
		case R.id.text_save_connection: // 保存
			ConnChartComm.SaveConnChart(m_IntfId, gLEDid);
			Toast.makeText(getApplication(), R.string.leddisplay_save_connection, Toast.LENGTH_SHORT).show();
/*
			SaveConnectTask tTaskSaveConnectTask = new SaveConnectTask();  	
	    	tTaskSaveConnectTask.execute();  	
	    	mSaveConnectDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_save_connection, Toast.LENGTH_SHORT).show();
*/
			break;
		case R.id.btn_show_line: // 显示连线
		
			m_ConnChartCustomView.setbShowConnectionLine(!m_ConnChartCustomView.isbShowConnectionLine());

		    if (m_ConnChartCustomView.isbShowConnectionLine()) {
		    	btn_ShowLine.setText(R.string.leddisplay_hide_connection);

			}else {
				btn_ShowLine.setText(R.string.leddisplay_show_connection);

			}

			break;
	   }
	   
	}
	
	private void setLineStyle(int nStyle)
	{
		 LayoutInflater factory = LayoutInflater.from(ConnChartActivity.this);//
	      final View textEntryView = factory.inflate(R.layout.dialog, null);
	       AlertDialog dlg = new AlertDialog.Builder(ConnChartActivity.this)
	      .setTitle(R.string.text_input_startnum)
	      .setView(textEntryView)
	      .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton) {
	            EditText strStartNum = (EditText) textEntryView.findViewById(R.id.username_edit);
	            String string = strStartNum.getText().toString();
	            m_ConnChartCustomView.setmStartAddressNum(Integer.parseInt(string));         

	          }
	      })
	      .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton) {
	          }
	      })
	      .create();
	      dlg.show();
	
  	      m_ConnChartCustomView.setmType(nStyle);
  	      m_ConnChartCustomView.setmMode(0);
  	      m_ConnChartCustomView.confirminitialcabinets(nStyle);
	}

	//隐藏View
	public  void HideView() {
		linear_top.setVisibility(View.GONE);
		linear_menu.setVisibility(View.GONE);
		linear_tool.setVisibility(View.GONE);
   }
	//最小化时删除View
	protected void deleteZoomButton() {
		  if (zoomButtonShow!=null&&zoomSmallShow!=null) {
				layout1_root.removeView(zoomButtonShow);
				layout1_root.removeView(zoomSmallShow);
		     }
		
	}
 
	//缩放时,显示缩放按钮
	public void showZoom() {
		zoomButtonShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa = new RelativeLayout.LayoutParams(
				200, 80);
		zoomPa.setMargins(4, 4, 120, 100);
		zoomPa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomButtonShow.setLayoutParams(zoomPa);

		zoomSmallShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa1 = new RelativeLayout.LayoutParams(
				50, 50);
		zoomPa1.setMargins(4, 4, 20, 100);
		zoomPa1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomSmallShow.setLayoutParams(zoomPa1);

		zoomToSmall = new Button(this);
		zoomToSmall.setBackgroundResource(R.drawable.exit_fullscreen_nor);
		zoomToSmall.setClickable(true);
		zoomSmallShow.addView(zoomToSmall);

		mZoomControls = new ZoomControls(this);
		mZoomControls.setClickable(true);
		zoomButtonShow.addView(mZoomControls);
		layout1_root.addView(zoomSmallShow);
		layout1_root.addView(zoomButtonShow);

		zoomToSmall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				
				linear_top.setVisibility(View.VISIBLE);
				linear_menu.setVisibility(View.VISIBLE);
				linear_tool.setVisibility(View.VISIBLE);
				
				deleteZoomButton();
			}
		});
		mZoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 点击放大屏幕
				m_ConnChartCustomView.ZoomIn();

			}
		});
		mZoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击缩小屏幕                                  
				m_ConnChartCustomView.ZoomOut();
			}
		});

		setContentView(layout1_root);
	}

	//初始化箱体参数
	
	private void InitCabinetViews(int interfaceid){
		
	     ArrayList<CbtData> tCbtArrayList = CabinetDB.GetCbtRecordByIntfId(interfaceid,gLEDid);	
		
	     IntfData interfData = InterfaceDB.GetRecordById(interfaceid,gLEDid);
	     
	     m_ConnChartCustomView.m_pointInterF.x= interfData.offsetX;
	     m_ConnChartCustomView.m_pointInterF.y= interfData.offsetY;
	     
	     int nCntNoAddress = 0;
		 for (int i = 0; i < tCbtArrayList.size(); i++) {
			    CbtData tCabinetData=tCbtArrayList.get(i);
	   			//绝对1:1坐标			
	   			//连线图界面用相对坐标 ,相对interface的坐标	
	   			RECT tRcRect = new RECT();
	   			tRcRect.left=tCabinetData.offsetX-interfData.offsetX;
	   			tRcRect.top=tCabinetData.offsetY-interfData.offsetY;
	   			tRcRect.right=tCabinetData.offsetX-interfData.offsetX+tCabinetData.width;
	   			tRcRect.bottom=tCabinetData.offsetY-interfData.offsetY+tCabinetData.height;  
	   			
	   			CabinetViewObj tCbtViewObj = new CabinetViewObj();
	   			
	   			tCbtViewObj.m_leftOrg = tRcRect.left;
	   			tCbtViewObj.m_topOrg  = tRcRect.top;
	   			tCbtViewObj.m_width  = tRcRect.right-tRcRect.left;
	   			tCbtViewObj.m_height  = tRcRect.bottom-tRcRect.top;
	   			tCbtViewObj.setM_AddressId(tCabinetData.address);
	   			
	   			tCbtViewObj.setmBasicViewID(tCabinetData.Id);
	   			tCbtViewObj.setM_ParentInterfId(interfaceid);
	   			tCbtViewObj.setStrTypeString(tCabinetData.strModelType);
	   			tCbtViewObj.setM_ShowAddressId(tCabinetData.addrshowid);
	   			
	   			if (tCbtViewObj.getM_AddressId()==-1) {
					nCntNoAddress++;
				}
	   			//添加箱体
	   			m_ConnChartCustomView.AddBasicView(tCbtViewObj);
	 		  }

		       if (nCntNoAddress>=1) {
		    	   m_ConnChartCustomView.bNeedInitAddressLineChart = true;
			   }
		        
		      //初始化连线图
		       if (m_ConnChartCustomView.bNeedInitAddressLineChart)
		       {
		    	   m_ConnChartCustomView.InitAddressLine();
		       }else {
		    	   m_ConnChartCustomView.InitAddressLineByInitAddress();
			   }
		           
	}
	

    private void initmPopupWindowView() {
		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.drop_list_menu,
				null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, 300, 400);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}

		});

		/** 在这里可以实现自定义视图的功能 */
		 btn_SendCabinetParameter = (Button) customView.findViewById(R.id.btn_send_cabinet);
		 btn_SaveCabinetParameter = (Button) customView.findViewById(R.id.btn_save_cabinet_parameter);
		 btn_ShowLine = (Button) customView.findViewById(R.id.btn_show_line);

		 btn_SendCabinetParameter.setOnClickListener(this);
		 btn_SaveCabinetParameter.setOnClickListener(this);
		 btn_ShowLine.setOnClickListener(this);

	}

}

	
			
		
		
	



