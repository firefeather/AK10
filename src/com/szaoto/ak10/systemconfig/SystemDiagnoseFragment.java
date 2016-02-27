/*
   * �ļ��� ControlActivity.java
   * ���������б�com.szaoto.ak10.control
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:51
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.PannelButtonDownService;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.SystemConfig;
import com.szaoto.ak10.commsdk.PannelLedControlBroadCast;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.dataaccess.returnClass;


/*
 * ����ControlActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class SystemDiagnoseFragment extends Fragment implements IInfoChangeObserver {
	static float PositionDegree = 0f;
	public static boolean[] isLight = new boolean[13];
	Timer timer;
	Button btnButton;
	Thread RefrashThread;
	static ImageView imageView;
	static TextView textmodule1;
	static TextView textmodule2;
	static TextView textmodule3;
	static TextView textmodule4;
	static TextView textmodule5;
	static TextView textbright;
	static TextView textcolortemp;
	static TextView textcont;   //�Աȶ�
	static TextView textsatn;   //���Ͷ�
	static TextView texttest;
	static TextView textswitch;
	static TextView textcancel;   //ȡ��
	static TextView textok;   //ȷ��

	static byte[] kkkk = {
		41, 54, 03 ,00 ,01, 04, 00 ,02 ,04, 06 ,(byte)0x08 ,0x0A ,00 ,66 ,01, 00, 00, 50, 00,
		00 ,00 ,00 ,01, 00 ,00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00,
		00 ,00 ,01 ,90 ,01 ,90, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 01,
		90, 01, 90, 00, 00, 00, 00, 00, 00, 00 ,00 ,00 ,00 ,00 ,00 ,01 ,90 ,01 ,90 ,
		00,00, 00, 00, 00, 00, 04, 01, 00, 00, 00, 00, 02, 80, 02, 80, 00, 00, 00, 
		00, 00, 00, (byte) 0xAB, (byte) 0xB2, 06, 65
		};
		
		/*11,22,33,44,55,66,00,00,00,00,00,00,34,56,(byte) 0xFF,(byte) 0xFF,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		55,55,12,34,(byte)0xFF,40,00,00,00,00,00,0x3F,0x3F,(byte)0xF0,0x0F,00,30,00,00,10,01,02,00,00,00,10,00,73,
		
		(byte) 0xAB,(byte) 0xCD,(byte) 0xEF,(byte) 0xdd*/
	//	};
	
	
	
	public static boolean isadded = false ;
	private static View mView ;
	public Context mcontext;
	public static Object object = new Object();
//	TimerTask timtaskTask;
	//private SurfaceView m_huatu;
	float value = 0;
	static Byte  ButtonType = 0  ;
	SystemConfig systemConfig = new SystemConfig();
	byte[] systemConfigPannelState = new byte[13];
//	LayoutInflater layoutInflatergloble = new LayoutInflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.systemdiagnose);
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		//��ȫ�� ��
		PannelButtonDownService.observers.remove(this);
		PannelLedControlBroadCast.MakeLightsAlwaysOFF();
		for (int i = 0; i < isLight.length; i++) {
			isLight[i] = false;
		}
		//ѡ��
		super.onStop();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	public static void SetView(View view){
		mView = view;
	}
	public static View GetView(){
		return mView;
	}
	
	public static void setAbnormalmodule(byte type){
		switch (type) {
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO1: 
			textmodule1.setBackgroundResource(R.anim.stateabnormal);
			break; 
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO2:
			textmodule2.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO3:
			textmodule3.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO4:
			textmodule4.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO5:
			textmodule5.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_BRIGHTNESS: 
			  textbright.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_COLORTEMP: 
			  textcolortemp.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_SATURATION:
			textcont.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_CONTRAST: 
			textsatn.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_SWITCH:
			textswitch.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_TEST:
			texttest.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_OK: 
			textok.setBackgroundResource(R.anim.stateabnormal);
			break;
		case SerialPortControlBroadCast.CMD_CANCEL: 
			textcancel.setBackgroundResource(R.anim.stateabnormal);
			break;
		default:
			break;
		}
	//	object.notify();
	}
	public static void setnormalmodule(byte type){
		switch (type) {
	
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO1: 
			textmodule1.setBackgroundResource(R.anim.statenormal);
			break; 
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO2:
			textmodule2.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO3:
			textmodule3.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO4:
			textmodule4.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO5:
			textmodule5.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_BRIGHTNESS: 
			  textbright.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_COLORTEMP: 
			  textcolortemp.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_SATURATION:
			textcont.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_CONTRAST: 
			textsatn.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_SWITCH:
			textswitch.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_TEST:
			texttest.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_OK: 
			textok.setBackgroundResource(R.anim.statenormal);
			break;
		case SerialPortControlBroadCast.CMD_CANCEL: 
			textcancel.setBackgroundResource(R.anim.statenormal);
			break;
		default:
			break;
		}
}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.systemdiagnose, null);
		SystemDiagnoseFragment.SetView(view);
		timer = new Timer();
		SerialPortControlBroadCast.SetCurrentContext(getActivity().getApplicationContext());
		
		mcontext = getActivity().getApplicationContext();
		for (int i = 0; i < isLight.length; i++) {
			if (SerialPortControlBroadCast.isLight[i] == true ) {
				setAbnormalmodule((byte)(0x50 + i));
			}
		}
	//	if (!isadded) {//ȷ��ֻ����һ��
			PannelButtonDownService.observers.add(this);
	//		isadded = true;
	//	}
	//	PannelButtonDownService.observers.add(this);
	//	timer.schedule(timtaskTask1, 100,100);
		imageView = (ImageView)view.findViewById(R.id.imageViewyuanquan);
		textmodule1 = (TextView)view.findViewById(R.id.module1);
		textmodule2 = (TextView)view.findViewById(R.id.module2);
		textmodule3 = (TextView)view.findViewById(R.id.module3);
		textmodule4 = (TextView)view.findViewById(R.id.module4);
		textmodule5 = (TextView)view.findViewById(R.id.module5);
		textbright = (TextView)view.findViewById(R.id.brighttest);
		textcolortemp = (TextView)view.findViewById(R.id.colortemprature);
		textcont =(TextView)view.findViewById(R.id.constractiontest);   //�Աȶ�
		textsatn =(TextView)view.findViewById(R.id.Saturationtest);   //���Ͷ�
		texttest =(TextView)view.findViewById(R.id.testdiagnose);
		textswitch=(TextView)view.findViewById(R.id.testswitch);
		textcancel=(TextView)view.findViewById(R.id.testcancel);   //ȡ��
		textok=(TextView)view.findViewById(R.id.testok); 
		
		 
		textmodule1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		
				Toast.makeText(getActivity(), "�ݲ�֧�ִ˹���", Toast.LENGTH_LONG).show();
				
				return;
				
				/* huhao ע�ͣ��ݲ�֧��
				List<byte[]> temp = DealReadBackData.DealRcvData(kkkk, 1484, kkkk.length);
				if (temp==null) {
					return;
				}
				for(byte[] index : temp)
				{
					int address = Packager.UnPack28Data(index, (short)0x49);
				}
				
				*/
			}
		});
	//	RotatePictrue(imageView , 20 );
		//Rect rect = new Rect();
	
		btnButton = (Button)view.findViewById(R.id.dignosesave);
		btnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	value = value + 1f;
			//	RotatePictrue(imageView, value);
			//	setmodule1state(true);
			}
		});
		// TODO Auto-generated method stub
		return view;
		//super.onCreateView(inflater, container, savedInstanceState);
	}
	TimerTask timtaskTask1 = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			value = (float)(value + 1)/360f;
			RotatePictrue(value);
		}
	};
	 public static void RotatePictrue( float value )//type =1��ʾ���� =2��ʾ �¶� =3��ʾʪ��
	    {
	    	float dur = 2f;
	    	PositionDegree = dur * value;
	    	RotateAnimation animation = new RotateAnimation(PositionDegree, 
	    			PositionDegree + 1, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.52f);
			animation.setDuration(1); //���ö���ʱ��1��
			animation.setFillAfter(true);  //���ö���ͣ���ڽ���Ϊֹ
			imageView.startAnimation(animation);  //Ϊָ�����ö���
	    }
	public static Byte getButtonType() {
		return ButtonType;
	}
	public static void setButtonType(byte buttonType) {
		ButtonType = buttonType;
	}
	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		if (!xParam1.equals("android.app.Application")) {
			return 0;
		}
		byte cmd = Byte.parseByte(xParam2);
		switch (cmd) {
		case SerialPortControlBroadCast.CMD_CLOCKWISE:
			value = value + 8;
			if(value >=360){
				value =0;
			}
			SystemDiagnoseFragment.RotatePictrue(value);
			break;
		case SerialPortControlBroadCast.CMD_ANTICLOCKWISE:
			value = value - 8;
			if (value <= 0) {
				value =360;
			}
			SystemDiagnoseFragment.RotatePictrue(value);
			break;
		default:
			{
				if(!isLight[cmd-0x50])
				{
					setAbnormalmodule(cmd);
					isLight[cmd-0x50] = true;
					PannelLedControlBroadCast.MakeSingleLightsAlwaysON((byte)(cmd-0x50+0x20));
				}else {
					setnormalmodule(cmd);
					isLight[cmd-0x50] = false;
					PannelLedControlBroadCast.MakeSingleLightsAlwaysOFF((byte)(cmd-0x50+0x30));
				}
				SystemDiagnoseFragment.mView.postInvalidate();	
				break;
			}
		}
		return 1;
	}
}
// TODO Auto-generated method stub

//	DataAccessDisplay.LoadDisplay();//YES
/*		Leddisplay leddisplay = new Leddisplay();
	leddisplay.setID(3);
//	DataAccessDisplay.AddDisplay(leddisplay);//Yes
//	DataAccessDisplay.ModifyDisplayCount(3);//Yes
//	DataAccessDisplay.ModifyDisplayResolution(3, "5400X5467");//yes
	LastState laststate = new LastState();
	laststate.setLastColTempModule("5700K");
	laststate.setLastmchannelGroupID(81);
	laststate.setmLastBrightness(81);
	laststate.setmLastContrast(81);
	laststate.setmLastSaturation(81);
	DataAccessDisplay.ModifyDisplayState(3, laststate);
//	DataAccessDisplay.RemoveDisplay(3);//yes
 * */	
//	DataAccessInterface.LoadInterfaceLib();
//	DataAccessColourTem.LoadColTempModules();//yes
/*	Interface interface1 = new Interface();
	interface1.setID(5);
	interface1.setChannel_ID(5);
	DataAccessInterface.Modifyinterface(interface1);
	DataAccessInterface.LoadInterfaceLib();*/
//	DataAccessInterface.RemoveInterface(interface1);
	
//	DataAccessGroup.LoadGroups();
//	DataAccessGroup.RemoveChannelGroup(2);//yes
	/*ChannelGroup cgChannelGroup = new ChannelGroup();
	cgChannelGroup.setmGroupID(2);
	List<Interface> listinInterfaces = cgChannelGroup.getmListInterface();
	//Interface infInterface = 
	DataAccessGroup.AddChannelGroup(cgChannelGroup);*/
	/*Interface minterface = new Interface();
	minterface.setAbsoluteSndRect("1,2,3,4");
	minterface.setRelativeSndRect("2,3,4,5");
	minterface.setID(1);
	minterface.setChannel_ID(5);
	DataAccessGroup.ModifyChannelGroup(2,1,minterface);//YES*/
//		Cabinet cabinet = new Cabinet();
//		DataAccessCabinetLibrary.getCabinetByname("M3_64X128_1", cabinet, 0);
//		DataAccessCabinetLibrary.getCabinetNames(0);
//		DataAccessCabinetLibrary.getCabinetSeriseNames(0);
//		CabinetInformation cabinet = new CabinetInformation();
//		DataAccessCabinetLibrary.AddCabinet(cabinet);
//		DataAccessCabinetLibrary.LoadCabinet();??
//		ForAllScanCardControl.SetOperationProcessing((short)1,(short)2);
//		CStructSingleScanCard tCard = new CStructSingleScanCard();
//		ForAllScanCardControl.SetVideoProcessing((short)1,tCard);
//		RECT rtLoad = new RECT();
//		ForAllScanCardControl.SetScanCardLoadedregion((short)1, rtLoad, (short)1, (short)1);
/*		MonitorData monitor = new MonitorData();
	byte[] lq = {1,2,3,4,5,6};
	byte[] lq1 = {11,12};
	MonitorData tData =	Packager.UnPackCustomMonitorData(lq,lq1,monitor);*/
//	TestClass tClass = new TestClass();
//	Packager.TestClass1(tClass);
	/*byte[] ucSendData = new byte[19992];
	for (int i = 0; i < ucSendData.length; i++) {
		ucSendData[i] = (byte) i;
	}
	byte[] lq = {1,2,3,4,5,6};
	byte[] lq1 = {11,12};
	ForAllScanCardControl.SendtoScanCard(lq,lq1,ucSendData);*/
//	TestClass tClass = new TestClass();
//	Packager.TestClass1(tClass);
//		CStructSingleScanCard smAttachment = new CStructSingleScanCard();
//		smAttachment.setnScanCardWidth((short)81);
//	short address,CStructSingleScanCard ScanCard,short nEmptyByte,short nPackID
//	ForAllScanCardControl.SetScanCardParam((short)1, smAttachment, (short)1,(short) 1);
	
//		LinkTable lt = new LinkTable(20);
//	lt.getUcLinkTable();
//	ForAllScanCardControl.SetHUBParam((short)1,smAttachment);
//		ForAllScanCardControl.SetHUBLookup((short)1 , lt);
//	ForAllScanCardControl.SetNoSingleDisp((short)1,(short)2);
//	ForAllScanCardControl.SetOperationProcessing((short)1,(short)2);
//	ForAllScanCardControl.SetVideoProcessing((short)1,smAttachment);//��Ҫ��ֵ׼ȷ
//	ForAllScanCardControl.SetCorrectProcessingLookup((short)1,smAttachment);//��Ҫ��ֵ׼ȷ

//	lt1.getUcLinkTable();
//		ForAllScanCardControl.SetScanCardSectionLinkTable((short)1,lt1);
//	GammaData sGammaData = new GammaData() ;
//	ForAllScanCardControl.SetGammaTable((short)1,sGammaData,0);
//	ForAllScanCardControl.SetOpenCabinetLamps((short)1,true);
//	ForAllScanCardControl.SetCalibrationEnable((short)1,true,(short)1);
//	ForAllScanCardControl.	SetScanCardIntelligentPara(1);
	
/*	int nResult = 0;
	for(int i = 0;i<6;i++)
	{//zhangjjnow
		nResult = ForAllScanCardControl.SetScanCardParam((short) 0xFF, cabinet.getListScancardAttachment().get(0).getScancard(), 
				(short)64,(short) i);
		if(nResult <0 ){
			nResult = -1;
		}
	}*/
	// public int SendScancardPara(short address,ScanCardAttachment  sScanCardAttachment,
	//			short nEmptyByte,GammaData sGammaData,
	//			boolean bSendScanCardLoadRegion)
//	LinkTable lt1 = new LinkTable();
//	lt1.getUcLinkTable();
//	ForAllScanCardControl.SetScanCardSectionLinkTable((short)1,lt1);

/*	CabinetInformation cabinet = new CabinetInformation();
	DataAccessCabinetLibrary.getCabinetByname("M3.5_main",cabinet,0);
	GammaData sGammaData = new GammaData();
	sGammaData = DataAccessGammaTable.LoadDisplayGammaTable(1);
	ForAllScanCardControl.SendScancardPara((short)0xFF, cabinet.getListScancardAttachment().get(0),
										(short)64,sGammaData,true);
//	Toast.makeText(this, "wancheng",100 ).show();
	Toast.makeText(getActivity()," wancheng", 100).show();*/
//	DataAccessConnection.LoadConnetion();
	/*Cabinet cabinet = new Cabinet();
	cabinet.setID(3);
	cabinet.setCbtRect("100,100,200,200");
	cabinet.setType("1");*/
//	DataAccessConnection.AddcabinetInDisplay(1, cabinet);
//	DataAccessConnection.RemoveCabinetInDisplay(1, 3);
//	 DataAccessInterface.LoadInterfaceLib();
/*	AcquisitionCardsData llll = new AcquisitionCardsData();
	try {
		DataAccessAcquisitionCardDatas.SaveAcquisitionCardsData(llll);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
//	ForAllScanCardControl.SendConnection(1);
	
//	ForAllScanCardControl.SendConnection(1);
//	byte[] cmd = new byte[100];
	

//	cmd[0] =0x10;//���رհ��������Զ��ơ���Ĭ�ϴ򿪣�
//	cmd[0] =0x11;//���򿪰��������Զ��ơ�
//	cmd[1] =0x20;//��~2c������Ӧ�����ƣ�˳��ͬ����0x5x��
//	cmd[2] =0x21;//��~2c������Ӧ�����ƣ�˳��ͬ����0x5x��
//	cmd[3] =0x22;//��~2c������Ӧ�����ƣ�˳��ͬ����0x5x��
	
//	cmd[0] =0x2f;//���������а����ơ�
//	cmd[1] =0x30;//~3c���رճ�����Ӧ�����ƣ�˳��ͬ����0x5x��
//	cmd[4] =0x2f;//���������а����ơ�
//		cmd[5] =0x3f;//���ر����г��������ơ�


//	SerialPortControlBroadCast.WriteSerialPort(cmd);