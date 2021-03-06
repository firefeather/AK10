/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;


import java.io.IOException;
import com.szaoto.ak10.R;
import com.szaoto.ak10.dataaccess.DataAccessSystemConfig;
import com.szaoto.ak10.util.AESCipher;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * 类名ControlActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class SystemPwdFragment extends Fragment {
	SystemConfigActivity mApp;
//	 SystemConfig sytconfig = new SystemConfig();
	 EditText oripasswordEditText;
	 EditText newpasswordEditText;
	 EditText newpasswordconfirmEditText;
	 TextView oripasswordTextView;
	 TextView newpasswordTextView;
	 TextView newpasswordconfirmTextView;
	 Button btn_saveButton;
	 Button btn_saveButton1;
//	 CipherMessage cmCipherMessage;
//	 String algorithm = "DES"; // 定义加密算法,可用 DES,DESede,Blowfish 
	 String dest = null; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.systempassword);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.systempassword, null);
		
		
	//	SystemConfigActivity.systemconfig = DataAccessSystemConfig.LoadSystemConfig();
		oripasswordEditText = (EditText)view.findViewById(R.id.origalpasswordeditText);
		oripasswordTextView = (TextView)view.findViewById(R.id.orgpasswordmetiontextView);
		newpasswordconfirmTextView = (TextView)view.findViewById(R.id.newpasswordconfirmmetiontextView);
	    newpasswordEditText = (EditText)view.findViewById(R.id.newpasswordEditText01);
	    newpasswordconfirmEditText = (EditText)view.findViewById(R.id.newpasswordconfirmEditText02);
		btn_saveButton = (Button)view.findViewById(R.id.btnsavepassword);
		btn_saveButton1 = (Button)view.findViewById(R.id.acqupgrade);
		btn_saveButton1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					try {
						//dest = AESCipher.encrypt("zhangjj", newpasswordEditText.getText().toString());
						dest = AESCipher.encrypt("zhangjj", "19870801");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					SystemConfigActivity.systemconfig .setSystemPassword(dest);
					DataAccessSystemConfig.SaveSystemConfig(SystemConfigActivity.systemconfig );
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String strencrypt = null;
			
				try {
					strencrypt = AESCipher.encrypt("zhangjj", oripasswordEditText.getText().toString());
					int k =323;
					k= 12;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if(strencrypt.equals(SystemConfigActivity.systemconfig.getSystemPassword())){
					oripasswordTextView.setText(R.string.text_passwd_right);
				} else {
					oripasswordTextView.setText(R.string.text_passwd_right);
					newpasswordEditText.setText("");
					newpasswordconfirmEditText.setText("");
					newpasswordconfirmTextView.setText("");
					return;
				}
				
			if(!newpasswordEditText.getText().toString().equals(newpasswordconfirmEditText.getText().toString()))
				{
					newpasswordconfirmTextView.setText(R.string.text_passwd_diffentintwo);
					//oripasswordTextView.setText("密码错误");
					newpasswordEditText.setText("");
					newpasswordconfirmEditText.setText("");
					newpasswordconfirmTextView.setText("");
				}
				else {
					String strdecryptString = null;
					try {
						strdecryptString = AESCipher.encrypt("zhangjj", newpasswordEditText.getText().toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SystemConfigActivity.systemconfig .setSystemPassword(strdecryptString);
					try {
						DataAccessSystemConfig.SaveSystemConfig(SystemConfigActivity.systemconfig );
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newpasswordconfirmTextView.setText(R.string.text_passwd_modify_success);
				}	
			}
		});
		oripasswordEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			//	oripasswordTextView.setText("");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		newpasswordEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			//	newpasswordconfirmTextView.setText("");
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		newpasswordconfirmEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			//	newpasswordconfirmTextView.setText("");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		// TODO Auto-generated method stub
	//	return super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}
	public void setActivity(SystemConfigActivity mainActivity) {
		// TODO Auto-generated method stub
		mApp = mainActivity;
	}
	

	

}
