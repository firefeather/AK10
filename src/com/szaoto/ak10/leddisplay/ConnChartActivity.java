/*
   * �ļ��� ConnectoinActivity.java
   * ���������б�com.szaoto.ak10.leddisplay
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:52:52
   * ��Ȩ���� liangdb-szaoto
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
 * ����ConnectoinActivity
 * ��ConnectoinActivity* ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 * �޸��ߣ�zhangsj
 * �޸����ڣ�2014��7��22��
 * �޸����ݣ���������ͼ
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
	
	//��Ӧ��interface	
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
	
	//��������ʾ
	private CustomProgressDialog mSendCabinetDialog;//�����������	
//	private CustomProgressDialog mSaveCabinetDialog;//�����������
//	private CustomProgressDialog mSendConnectDialog;//��������ͼ
//	private CustomProgressDialog mSaveConnectDialog;//��������ͼ
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
	    //��Ҫ������view��
       
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
	 * ���ݼ��ؽ���
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
    	
    	txt_SendConnectChart = (TextView) findViewById(R.id.text_sendconnect);//��������ͼ
    	txt_SaveConnectChart = (TextView) findViewById(R.id.text_save_connection);//��������ͼ
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
		//��ȡLED����ID��
		layout1_root = (RelativeLayout) findViewById(R.id.connection_layout_root );
		ledConnectText.setText(">"+"LED"+Ak10Application.GetLedId()+">");
		//Interface�˿ں�
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
		//ˮƽZ�������߷�ʽ	
	    case R.id.text_Hz:
		{
			setLineStyle(2);
			break;
		}
		//��ֱZ�������߷�ʽ	
	    case R.id.text_vz:
	    {
	    	setLineStyle(3);
			break;
		}
	    //ˮƽS�������߷�ʽ	
	    case R.id.text_HS:
	    {
	    	setLineStyle(0);
			break;
		}
	 	//��ֱS�������߷�ʽ	
	    case R.id.text_VS:
	    { 
	    	setLineStyle(1);
	    	break;
	    }
	    
	    case R.id.btn_send_cabinet: // �����������
	    	
	    	SendCbtParamTask tTaskSendParamTask = new SendCbtParamTask();  	
	    	tTaskSendParamTask.execute();  	
	    	mSendCabinetDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_send_cabinetparameter, Toast.LENGTH_SHORT).show();
			break ;
		case R.id.btn_save_cabinet_parameter: // �����������
			ConnChartComm.SaveCabinetParam(m_IntfId, gLEDid);
			Toast.makeText(getApplication(), R.string.leddisplay_save_cabinetparameter, 10).show();
			/*
	    	SaveCbtParamTask tTaskSaveParamTask = new SaveCbtParamTask();  	
	    	tTaskSaveParamTask.execute();  	
	    	mSaveCabinetDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_save_cabinetparameter, Toast.LENGTH_SHORT).show();
			*/
			break ;

		case R.id.text_sendconnect: //��������ͼ	
			ConnChartComm.SendConnChart(m_IntfId, gLEDid, m_ConnChartCustomView.gAddressedCbtList);
			Toast.makeText(getApplication(), R.string.leddisplay_send_connection, Toast.LENGTH_SHORT).show();
			//SendConnectTask tTaskSendConnectTask = new SendConnectTask();  	
	    	//tTaskSendConnectTask.execute();  	
	    	//mSendConnectDialog.show();
			//Toast.makeText(getApplication(), R.string.leddisplay_send_connection, Toast.LENGTH_SHORT).show();

			break; 
		case R.id.text_save_connection: // ����
			ConnChartComm.SaveConnChart(m_IntfId, gLEDid);
			Toast.makeText(getApplication(), R.string.leddisplay_save_connection, Toast.LENGTH_SHORT).show();
/*
			SaveConnectTask tTaskSaveConnectTask = new SaveConnectTask();  	
	    	tTaskSaveConnectTask.execute();  	
	    	mSaveConnectDialog.show();
			Toast.makeText(getApplication(), R.string.leddisplay_save_connection, Toast.LENGTH_SHORT).show();
*/
			break;
		case R.id.btn_show_line: // ��ʾ����
		
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

	//����View
	public  void HideView() {
		linear_top.setVisibility(View.GONE);
		linear_menu.setVisibility(View.GONE);
		linear_tool.setVisibility(View.GONE);
   }
	//��С��ʱɾ��View
	protected void deleteZoomButton() {
		  if (zoomButtonShow!=null&&zoomSmallShow!=null) {
				layout1_root.removeView(zoomButtonShow);
				layout1_root.removeView(zoomSmallShow);
		     }
		
	}
 
	//����ʱ,��ʾ���Ű�ť
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

				// ����Ŵ���Ļ
				m_ConnChartCustomView.ZoomIn();

			}
		});
		mZoomControls.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �����С��Ļ                                  
				m_ConnChartCustomView.ZoomOut();
			}
		});

		setContentView(layout1_root);
	}

	//��ʼ���������
	
	private void InitCabinetViews(int interfaceid){
		
	     ArrayList<CbtData> tCbtArrayList = CabinetDB.GetCbtRecordByIntfId(interfaceid,gLEDid);	
		
	     IntfData interfData = InterfaceDB.GetRecordById(interfaceid,gLEDid);
	     
	     m_ConnChartCustomView.m_pointInterF.x= interfData.offsetX;
	     m_ConnChartCustomView.m_pointInterF.y= interfData.offsetY;
	     
	     int nCntNoAddress = 0;
		 for (int i = 0; i < tCbtArrayList.size(); i++) {
			    CbtData tCabinetData=tCbtArrayList.get(i);
	   			//����1:1����			
	   			//����ͼ������������� ,���interface������	
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
	   			//��������
	   			m_ConnChartCustomView.AddBasicView(tCbtViewObj);
	 		  }

		       if (nCntNoAddress>=1) {
		    	   m_ConnChartCustomView.bNeedInitAddressLineChart = true;
			   }
		        
		      //��ʼ������ͼ
		       if (m_ConnChartCustomView.bNeedInitAddressLineChart)
		       {
		    	   m_ConnChartCustomView.InitAddressLine();
		       }else {
		    	   m_ConnChartCustomView.InitAddressLineByInitAddress();
			   }
		           
	}
	

    private void initmPopupWindowView() {
		// // ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		View customView = getLayoutInflater().inflate(R.layout.drop_list_menu,
				null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ��Ⱥ͸߶�
		popupwindow = new PopupWindow(customView, 300, 400);
		// ���ö���Ч�� [R.style.AnimationFade ���Լ����ȶ���õ�]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// �Զ���view���Ӵ����¼�
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

		/** ���������ʵ���Զ�����ͼ�Ĺ��� */
		 btn_SendCabinetParameter = (Button) customView.findViewById(R.id.btn_send_cabinet);
		 btn_SaveCabinetParameter = (Button) customView.findViewById(R.id.btn_save_cabinet_parameter);
		 btn_ShowLine = (Button) customView.findViewById(R.id.btn_show_line);

		 btn_SendCabinetParameter.setOnClickListener(this);
		 btn_SaveCabinetParameter.setOnClickListener(this);
		 btn_ShowLine.setOnClickListener(this);

	}

}

	
			
		
		
	


