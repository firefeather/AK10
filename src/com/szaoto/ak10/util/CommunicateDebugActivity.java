/*
   * 文件名 CommunicateDebug.java
   * 包含类名列表com.szaoto.ak10.util
   * 版本信息，版本号
   * 创建日期2013年12月30日上午11:00:45
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.sqlitedata.CardInfoDB;

/*
 * 类名CommunicateDebug
 * 作者 liangdb
 * 主要功能 通讯测试
 * 创建日期2013年12月30日
 * 修改者，修改日期，修改内容
 */
public class CommunicateDebugActivity extends Activity {

	
	private Button btn_Write;
	private Button btn_Read;
	
	private Spinner  spinner_SendDest;
	private EditText txt_SendAddr;
	private EditText txt_SendData;
	private EditText txt_RevContent;
	private EditText txt_ReadLength;

	private Button btn_ComBack;
	private ImageView btn_ComHome; 
	

	ArrayList<String> m_ArrMac = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.communicate_debug);
		SerialPortControlBroadCast.SetCurrentContext(this);
	
        btn_ComBack = (Button)findViewById(R.id.btn_CommnicateBack);
        btn_ComHome = (ImageView)findViewById(R.id.CommnicateHome);
        
		btn_Write = (Button)findViewById(R.id.btn_Write);
		btn_Read = (Button)findViewById(R.id.btn_Read);
		
		btn_ComHome.setOnClickListener(clickHandler);
		btn_ComBack.setOnClickListener(clickHandler);
		btn_Write.setOnClickListener(clickHandler);
		btn_Read.setOnClickListener(clickHandler);
		
		spinner_SendDest = (Spinner) findViewById(R.id.spin_SendDest);
		
		txt_SendAddr = (EditText) findViewById(R.id.txt_SendAdd);
		txt_SendData = (EditText) findViewById(R.id.txt_SendData);
		txt_RevContent = (EditText) findViewById(R.id.txt_RevContent);
		txt_ReadLength = (EditText) findViewById(R.id.txt_SendLength);//长度共用
		

		txt_SendAddr.setInputType(InputType.TYPE_CLASS_TEXT);
		txt_SendData.setInputType(InputType.TYPE_CLASS_TEXT);
		txt_RevContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		txt_RevContent.setSingleLine(false);
		txt_RevContent.setHorizontallyScrolling(false);  
		txt_ReadLength.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		txt_SendAddr.setText("02 00");
		txt_SendData.setText("55 55 12 34 FF 40 00 00 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 B7");
		txt_ReadLength.setText("41");
		
		
		//加载MAC地址和FFFFFFFFFFF
		
		m_ArrMac =  CardInfoDB.GetAllCardInfoRecordMacAddress();
		m_ArrMac.add("FF-FF-FF-FF-FF-FF");
		
		spinner_SendDest.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,
				m_ArrMac));
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}

	View.OnClickListener clickHandler = new View.OnClickListener(){
		@SuppressLint("ShowToast")
		public void onClick(View v){
			switch (v.getId()) {
				case R.id.btn_Write:
				{
					String sSendDestString = spinner_SendDest.getSelectedItem().toString();
					String sSendAddr = txt_SendAddr.getText().toString();
					String sSendData = txt_SendData.getText().toString();

					byte[] ucDestAddress = new byte[6];
					byte[] ucAddress = new byte[2];
						
					ucDestAddress = UtilFun.hexStringSplit2Bytes(sSendDestString, "-");
					ucAddress = UtilFun.hexStringSplit2Bytes(sSendAddr, " ");
					
					int nSequenceNumber = 0x0;
					int nLength = 0;
					byte[] ucData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
					//if ("" != sSendData) {
					if (false == sSendData.equals("")) {
						ucData = UtilFun.hexStringSplit2Bytes(sSendData, " ");
					}
					byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
					int nSendLength = ((42 < nLength) ? nLength : 42) + 22;
					ucSendData = Packager.EthernetPackDataWrite(ucDestAddress, ucAddress, nSequenceNumber, nLength, ucData);		
					

					try 
					{
						 SpiControl.WriteSpi(ucSendData, nSendLength);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
		
				
					Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
					
				}
					break;
				case R.id.btn_Read:
				{
					String sRevDestString = spinner_SendDest.getSelectedItem().toString();
					
					String sRevAddr = txt_SendAddr.getText().toString();
					
					byte[] ucDestAddress = new byte[6];
					byte[] ucAddress = new byte[2];
					
				
					ucDestAddress = UtilFun.hexStringSplit2Bytes(sRevDestString, "-");
					ucAddress = UtilFun.hexStringSplit2Bytes(sRevAddr, " ");
					
					int nRevLength = 0;
					String sReadLength = txt_ReadLength.getText().toString();
					//if ("" != sReadLength) {
					if (false == sReadLength.equals("")) {
						nRevLength = Integer.valueOf(sReadLength);
					}
					
					byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
					int nSendLength = ((42 < nRevLength) ? nRevLength : 42) + 22;
					
					ucSendData = Packager.EthernetPackDataRead(ucDestAddress, ucAddress, nRevLength);

				
					byte[] ucRevData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
				
					try 
					{
						SpiControl.WriteSpi(ucSendData,nSendLength);
						Thread.sleep(500);
						ucRevData = SpiControl.ReadSpi(nRevLength);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
			
					
					//接收显示
					
					Toast.makeText(getApplicationContext(), "读取成功", Toast.LENGTH_SHORT).show();
					
					txt_RevContent.setText("");
					
					if (null != ucRevData) {
						if (FrameDataField.ETH_DATA_MAX_SIZE + 18 < ucRevData.length) {
							txt_RevContent.setText(txt_RevContent.getText().toString() + "\n Get too loog data:" + String.valueOf(ucRevData.length));
						}
						else {
							String sRevString = UtilFun.bytes2HexString(ucRevData,ucRevData.length," ");
							txt_RevContent.setText(txt_RevContent.getText().toString() + "\n" + sRevString);	
						}
					}
					else {
						txt_RevContent.setText(txt_RevContent.getText().toString() + "\n" + String.valueOf(SpiControl.GetLastError()));
					} 
					

				}
					break;
				case R.id.btn_CommnicateBack:
					finish();
					break;
				case R.id.CommnicateHome:
				   startActivity(new Intent(CommunicateDebugActivity.this,HomePageActivity.class));
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 
	 */
	public CommunicateDebugActivity() {
	
	}
	
	// view是需要控制的view对象

	/**
     * 收起软键盘
     */
    public static void collapseSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInputMethod(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        }
    }


}
