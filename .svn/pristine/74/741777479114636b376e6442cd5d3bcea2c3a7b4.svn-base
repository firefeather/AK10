/*
   * 文件名 MonitorActivity.java
   * 包含类名列表com.szaoto.ak10.monitor
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:55:08
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.monitor;


import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.MonitorData;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.datacomm.MonitorComm;

/*
 * 类名MonitorActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class MonitorActivity extends Activity {

//	private NavigationBar navigationBar;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private SharedPreferences preferences; 
	private SharedPreferences.Editor editor;
	private static Object lock = new Object();

	AbsoluteLayout layouttemp;
	AbsoluteLayout layouthum;
	TabHost mTabHost ;
	TextView envitemp;
	TextView envihumi;
	TextView connectstateshow;
	Packager packager;
	SpiControl spiControl;
	Thread monitorThread;
	ThresholdAdjust mThresholdAdjust;
	Thread mMonitorStateThread;
	private static Object lockObjectauto = new Object();
	private static Object lockObjectstate = new Object();
	String lockObjectau = "";
	String lockObjectst = "" ;
	ImageView imageHumityview;
	ImageView imagetempview;
	boolean bExit = false;
	Timer timer = new Timer();
	Timer timeraoto = new Timer();
	Timer timerstate = new Timer();
//	Timer timermonitorautocheckTimer = new Timer();
	int autotime;
	boolean autotimemark;
	boolean autotimecheck;
	boolean iscurrentactivityclose;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
			 case 1:    
				 settempthreshold((double)preferences.getFloat("temalarm", 0) , 2);
				 settempthreshold((double)preferences.getFloat("humialarm", 0) , 3);  
				 break;
			 case 2:
				 MonitorData monitordata = new MonitorData();
					byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
					if (0 < MonitorComm.SendMonitorDataWriteCommond(1))//addDisplayID
					{
						SystemClock.sleep(1000);
						ucSendData = MonitorComm.ReadBackData(1);//addDisplayID
						if(null != ucSendData)
						{
							monitordata = packager.UnPackCustomMonitorData(null, ucSendData, null);
							if (mThresholdAdjust.IsEnvirTempAlarm(monitordata.getfTemperature()))
								{
								
								}
						}
					}
					ImageView viewB = (ImageView)findViewById(R.id.ImageNeedleB);
					ImageView viewT = (ImageView)findViewById(R.id.ImageNeedleT);
					ImageView viewH = (ImageView)findViewById(R.id.ImageNeedleH);
					SetNeedlePosition(viewB , monitordata.getnBrightness(), 1);
					SetNeedlePosition(viewT , monitordata.getfTemperature() , 2);
					SetNeedlePosition(viewH , monitordata.getfHumidity() , 3);
					DecimalFormat df = new DecimalFormat("########.0");
					ReadEnvironmentparm(monitordata.getnBrightness(), 
							Float.valueOf(df.format(monitordata.getfTemperature())) ,
							Float.valueOf(df.format(monitordata.getfHumidity()))); 
					break;
			 case 3:
				 {
					 ImageView disconnectionImageView = (ImageView)findViewById(R.id.disconnectstate);
					 ImageView connectionImageView = (ImageView)findViewById(R.id.connectstate);
					 ImageView backupImageView = (ImageView)findViewById(R.id.backupstate);
					 if(msg.arg1 == -1)
					 {
						 disconnectionImageView.setVisibility(View.VISIBLE);
						 connectionImageView.setVisibility(View.GONE);
						 backupImageView.setVisibility(View.GONE);
						 if(msg.arg2 == 1)
							 connectstateshow.setText(getString(R.string.monitor_connect_state_disconnection));
					 }
					 else if(msg.arg1 ==0)
					 {
						 disconnectionImageView.setVisibility(View.GONE);
						 connectionImageView.setVisibility(View.GONE);
						 backupImageView.setVisibility(View.VISIBLE);
						 if(msg.arg2 == 1)
							 connectstateshow.setText(getString(R.string.monitor_connect_state_backup));
					 }
					 else {
						 disconnectionImageView.setVisibility(View.GONE);
						 connectionImageView.setVisibility(View.VISIBLE);
						 backupImageView.setVisibility(View.GONE);
						 if(msg.arg2 == 1)
							 connectstateshow.setText(getString(R.string.monitor_connect_state_connection));
					}
				 }
			}
			super.handleMessage(msg);   
		};
	};
	TimerTask timerTask  = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message=new Message();
			message.what=1;
			handler.sendMessage(message);
		}
	};
	TimerTask timerTaskauto = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lockObjectauto) {//lockObjectauto =wait代表lockobjectauto.wait() 状态为等待  =run表示lockobjectauto.wait()为开启，=stop表示线程结束
				lockObjectau = "run";
				lockObjectauto.notify();
				}	
		}
	};
	TimerTask timerTaskstate = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (lockObjectstate) {//lockObjectstate =wait代表lockobjectstate.wait() 状态为等待  =run表示lockobjectstate.wait()为开启，=stop表示线程结束
				lockObjectst = "run";
				lockObjectstate.notify();
			}
		}
	};
	private ImageView btnMonitorHome;
	private Button btnMonitorSetings;
	private Button btnMonitorBack;
	public Handler getHandler(){
		return this.handler;
		}
	boolean loadconnection()////加载连线图，得到参数
	{
		return true;
	}
	void drawcabinets(int num , int cabinettype)//	箱体数量确定位置，箱体类型确定选用箱体图
	{
	//	getxy(num);
		int x =5;
		int y = 5;
		for(int i =0 ;i< x;i++)
		{
			
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SerialPortControlBroadCast.SetCurrentContext(this);
		setContentView(R.layout.monitor);
		preferences = getSharedPreferences("11",MODE_WORLD_READABLE); 
        //获得修改器 
        editor = preferences.edit();
        mThresholdAdjust = new ThresholdAdjust(this);
        connectstateshow = (TextView)findViewById(R.id.Connectstateshow);
        imagetempview = (ImageView)findViewById(R.id.imagetemp);
        imageHumityview= (ImageView)findViewById(R.id.imageHumit);
//		navigationBar = new NavigationBar("monitor_MonitorActivity",this);
        btnMonitorHome= (ImageView)findViewById(R.id.MonitorHome);
        btnMonitorSetings= (Button)findViewById(R.id.btn_Monitor_setings);
        btnMonitorBack= (Button)findViewById(R.id.btn_MonitorBack);
        btnMonitorHome.setOnClickListener(ClickHandler);
        btnMonitorSetings.setOnClickListener(ClickHandler);
        btnMonitorBack.setOnClickListener(ClickHandler);
        
        
		layouttemp = new AbsoluteLayout(this);
        layouthum = new AbsoluteLayout(this);
        envitemp = new TextView(this);
        envihumi = new TextView(this);
        envitemp.setWidth(30);
        envitemp.setHeight(20);
        envihumi.setWidth(30);
        envihumi.setHeight(20);
      
		SetmaxminValue(100f, 0f ,1);
		SetmaxminValue(100f ,0f ,3);
		SetmaxminValue(120f, -40f ,2);
		layouttemp = (AbsoluteLayout)findViewById(R.id.TextminT);
		layouthum = (AbsoluteLayout)findViewById(R.id.ablayout2);
		
		ImageView viewB = (ImageView)findViewById(R.id.ImageNeedleB);
		ImageView viewT = (ImageView)findViewById(R.id.ImageNeedleT);
		ImageView viewH = (ImageView)findViewById(R.id.ImageNeedleH);
		SetNeedlePosition(viewB , 0 , 1);
		SetNeedlePosition(viewT , 0 , 2);
		SetNeedlePosition(viewH , 0 , 3);
		Loadthresholdparm();
		iscurrentactivityclose = false;
		//autotimemark = false;
		
		 boolean bl = mThresholdAdjust.IsEnvirTempAlarm(40);
		
	        Button btn3 = (Button)findViewById(R.id.buttonread);
	        btn3.setOnClickListener(new View.OnClickListener(){
				@SuppressWarnings("static-access")
				@Override
				public void onClick(View v) {
					MonitorData monitordata = new MonitorData();
					byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
					if (0 < MonitorComm.SendMonitorDataWriteCommond(1))//adddisplayid
					{
						SystemClock.sleep(300);
						ucSendData = MonitorComm.ReadBackData(1);//adddisplayid
						if(null != ucSendData)
						{
							monitordata = packager.UnPackCustomMonitorData(null, ucSendData, null);
							if (mThresholdAdjust.IsEnvirTempAlarm(monitordata.getfTemperature()))
								{
								
								}
						}
					}
					ImageView viewB = (ImageView)findViewById(R.id.ImageNeedleB);
					ImageView viewT = (ImageView)findViewById(R.id.ImageNeedleT);
					ImageView viewH = (ImageView)findViewById(R.id.ImageNeedleH);
					SetNeedlePosition(viewB , monitordata.getnBrightness(), 1);
					SetNeedlePosition(viewT , monitordata.getfTemperature() , 2);
					SetNeedlePosition(viewH , monitordata.getfHumidity() , 3);
					DecimalFormat df = new DecimalFormat("########.0");
					ReadEnvironmentparm(monitordata.getnBrightness(), 
							Float.valueOf(df.format(monitordata.getfTemperature())) ,
							Float.valueOf(df.format(monitordata.getfHumidity())));
					//df.format(monitordata.getfTemperature())
					//	startActivityForResult (new Intent(MainActivity.this, MonitorConifg.class),  1);
				}
	        });
	        timer.schedule(timerTask, 1000 );
	       // new Timer()
	        if(autotimecheck)
	        {
	        	monitorThread = new Thread(new MonitorThread(this, lockObjectauto));
				monitorThread.start();
				timeraoto.schedule(timerTaskauto, 1000, autotime);
			}
	       mMonitorStateThread = new Thread(new MonitorStateThread(this ,lockObjectstate));
	       mMonitorStateThread.start();
	       timerstate. schedule(timerTaskstate, 1000,5000);
	  }
	
	View.OnClickListener ClickHandler = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		  switch (v.getId()) {
		case R.id.MonitorHome:
			startActivity(new Intent(MonitorActivity.this,HomePageActivity.class));
			break;
		case R.id.btn_Monitor_setings:
			startActivity(new Intent(MonitorActivity.this,MonitorConfigActivity.class));
			break;
		case R.id.btn_MonitorBack:
			finish();
			break;

		default:
			break;
		}
			
		}
	};
	@Override
	protected void onStop() {
//		synchronized (lockObjectauto){
//			lockObjectau = "stop";
//			lockObjectauto.notify();
//		}
//		synchronized (lockObjectstate){
//			lockObjectst = "stop";
//			lockObjectstate.notify();
//		}
//		timeraoto.cancel();
//		timerstate.cancel();
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//iscurrentactivityclose = true;
	//	iscurrentactivityclose = true;
		synchronized (lockObjectauto){
			lockObjectau = "stop";
			lockObjectauto.notify();
		}
		synchronized (lockObjectstate){
			lockObjectst = "stop";
			lockObjectstate.notify();
		}
		timeraoto.cancel();
		timerstate.cancel();
		super.onDestroy();
	}
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		  if(resultCode == 0)
		  {}
			  else{
			  	layouttemp.removeView(envitemp);
		    	layouthum.removeView(envihumi);
		    	Double ff;
		    	String result = data.getExtras().getString("Autotime");//得到新Activity 关闭后返回的数据 
		    	
		    	//checkBox.setChecked(preferences.getBoolean("Autotimecheck", false));
		    	data.getExtras().getBoolean("Autotimecheck");//////////读出是否轮循
		    	result = data.getExtras().getString("temalarm");
		        settempthreshold(Double.valueOf(result) , 2);
		        result = data.getExtras().getString("humialarm");
		        settempthreshold(Double.valueOf(result) , 3);
		        result = data.getExtras().getString("badlightnum");
		     //   result =  checkBox
		        result = data.getExtras().getString("cabtempalarm");
		        result = data.getExtras().getString("cabhumialarm");
		        result = data.getExtras().getString("cabbadlightnum");
		        }
	        } 
	  public void Loadthresholdparm()
	  {
			preferences.getFloat("temalarm", 0);
			preferences.getFloat("humialarm", 0) ;
			setAutotime(preferences.getInt("Autotime", 5000));
			setAutotimecheck(preferences.getBoolean("Autotimecheck", false));
		  return ;
	  }
	  public void ReadEnvironmentparm(Short brightness ,Float temp ,Float humi)
	    {
	    	TextView view1 = (TextView)findViewById(R.id.TextValueB);
	    	view1.setText(brightness.toString());
	    	TextView view2 = (TextView)findViewById(R.id.TextValueT);
	    	view2.setText(temp.toString());
	    	TextView view3 = (TextView)findViewById(R.id.TextValueH);
	    	view3.setText(humi.toString());
	    	return ;
	    }
	 private void SetmaxminValue(Float max, Float min ,int  type){
	    	////type =1表示亮度 =2表示 温度 =3表示湿度\设定环境监控值的范围 
	    	//if ( max <= min)
	    	Float mid = (max+min)/2;
	    	switch( type)
	    	{
	    	case 1:
	    		TextView view = new TextView(this);
	    		view = (TextView)findViewById(R.id.TextMinB);
	    		view.setText(min.toString());
	    		view.setTextColor(Color.rgb(20, 0, 0));
	    		view = (TextView)findViewById(R.id.TextMaxB);
	    		view.setText(max.toString());
	    		view.setTextColor(Color.rgb(20, 0, 0));
	    		break;
	    	case 2:
	    		TextView view1;
	    		view1 = (TextView)findViewById(R.id.textminT);
	    		view1.setText(min.toString());
	    		view1.setTextColor(Color.rgb(20, 0, 0));
	    		view1 = (TextView)findViewById(R.id.TextMaxT);
	    		view1.setText(max.toString());
	    		view1.setTextColor(Color.rgb(20, 0, 0));
	    		break;
	    	case 3:
	    		TextView view2;
	    		view2 = (TextView)findViewById(R.id.TextMinh);
	    		view2.setText(min.toString());
	    		view2.setTextColor(Color.rgb(20, 0, 0));
	    		view2 = (TextView)findViewById(R.id.TextMaxh);
	    		view2.setText(max.toString());
	    		view2.setTextColor(Color.rgb(20, 0, 0));
	    		break;
	    	default:
	    		break;
	    	}
	    	return;
	    }
	// onTouchEvent

	 private void settempthreshold(Double value , int type) {//type =1 liangdu type =2 wendu type=3 shidu
	    	int positionx = 0;
	    	int positiony = 0;
	    	//AbsoluteLayout layout =0; = new AbsoluteLayout(this);
	    	switch (type) {
			case 1:
				ImageView view = (ImageView)findViewById(R.id.imageBrightness);
				int centerx = (view.getLeft()+view.getRight())/2;
				int centery = (view.getTop()+view.getBottom())/2;
				int r = Math.abs((view.getTop()+view.getBottom())/2);
				
				break;
			case 2:
				
				int centerx1 = (imagetempview.getLeft()+imagetempview.getRight())/2;
		    	 int centery1 = (imagetempview.getTop()+imagetempview.getBottom())/2;
		    	 int r1 = Math.abs((imagetempview.getTop()+imagetempview.getBottom())/2);
		    	 Double tangle = 400f + 1.25f *value;
		    	 tangle = tangle * Math.PI /180f;
		    	 Double s2 = Math.sin(tangle);
		    	 Double s1 = r1* Math.sin(tangle)*0.6f;
		    	 
		    	 
		    	positiony = centery1 - (int)(r1 * Math.sin(tangle)*0.6f)-10;
		    	positionx = centerx1 - (int)(r1 * Math.cos(tangle)*0.6f)-15;
		    	
		    	envitemp.setText(value.toString());
		    	envitemp.setTextColor(Color.rgb(185, 15, 31));
		    	AbsoluteLayout.LayoutParams lp =  
		    			   new AbsoluteLayout.LayoutParams(  
		    			    ViewGroup.LayoutParams.WRAP_CONTENT,  
		    			    ViewGroup.LayoutParams.WRAP_CONTENT,  
		    			    positionx,positiony);  
		    	layouttemp.addView(envitemp, lp);
				break;
			case 3:
				int centerx2 = (imageHumityview.getLeft()+imageHumityview.getRight())/2;
				int centery2 = (imageHumityview.getTop()+imageHumityview.getBottom())/2;
				int r2 = Math.abs((imageHumityview.getTop()+imageHumityview.getBottom())/2);
				Double tangle1 = 350f + 2f *value;
				tangle1 = tangle1 * Math.PI /180f;
				
				positiony = centery2 - (int)(r2 * Math.sin(tangle1)*0.6f)-10;
				positionx = centerx2 - (int)(r2 * Math.cos(tangle1)*0.6f)-15;
		    	
		    	envihumi.setText(value.toString());
		    	envihumi.setTextColor(Color.rgb(185, 15, 31));
		    	AbsoluteLayout.LayoutParams lp1 =  
		    			   new AbsoluteLayout.LayoutParams(  
		    			    ViewGroup.LayoutParams.WRAP_CONTENT,  
		    			    ViewGroup.LayoutParams.WRAP_CONTENT,  
		    			    positionx,positiony);  
		    	layouthum.addView(envihumi, lp1);
				break;
			default:
				break;
			}
	    	return;
		}
	  private void SetNeedlePosition(ImageView needle , float value , int type)//type =1表示亮度 =2表示 温度 =3表示湿度
	    {
	    	float end = 0f;
	    	float start  = 0f;
	    	float dur = 0f;
	    	float PositionDegree = 0f;
	    	switch(type)
	    	{
	    	case 1://角度 从 260~460度 
	    		end = 100f;
	    		start  = 0f;
	    		dur =200/100;
	    		PositionDegree = 260 + dur * value;
	    		break;
	    	case 2:
	    		end  = 120f;
	    		start  = -40f;
	    		dur =200/160;
	    		PositionDegree = 260 + (value +40f) * dur;
	    		break;
	    	case 3:
	    		end = 100f;
	    		start = 0f;
	    		dur = 200/100;
	    		PositionDegree = 260 + dur * value;
	    		break;
	    	default:
	    		break;
	    	}
	    	RotateAnimation animation = new RotateAnimation(PositionDegree, 
	    			PositionDegree + dur, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.9f);
			animation.setDuration(1); //设置动画时间1秒
			animation.setFillAfter(true);  //设置动画停留在结束为止
			needle.startAnimation(animation);  //为指针设置动画
	    }
	public int getAutotime() {
		return autotime;
	}
	public void setAutotime(int autotime) {
		this.autotime = autotime;
	}
	public boolean getAutotimecheck() {
		return autotimecheck;
	}
	public void setAutotimecheck(boolean autotimecheck) {
		this.autotimecheck = autotimecheck;
	}
	public boolean isIscurrentactivityclose() {
		return iscurrentactivityclose;
	}
	public void setIscurrentactivityclose(boolean iscurrentactivityclose) {
		this.iscurrentactivityclose = iscurrentactivityclose;
	}
	 
}
