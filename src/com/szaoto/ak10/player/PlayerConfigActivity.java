/*
   * �ļ��� PlayerConfigActivity.java
   * ���������б�com.szaoto.ak10.player
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:54:16
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.player;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;

/*
 * ����PlayerConfigActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class PlayerConfigActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	EditText textltx;
	EditText textlty;
	EditText textwidth;
	EditText textheigh;
	CheckBox Checkfullscreen;
	private SharedPreferences preferences; 
	private SharedPreferences.Editor editor;
	private TextView btnConfHome;
	private TextView btnConfBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("=============playconfigActivity onCreate()");
		setContentView(R.layout.player_config);
		SerialPortControlBroadCast.SetCurrentContext(this);
		textltx   = (EditText)findViewById(R.id.EditTextltx);
		textlty   = (EditText)findViewById(R.id.EditTextlty);
		textwidth = (EditText)findViewById(R.id.EditTextwidth);
		textheigh = (EditText)findViewById(R.id.EditTextheight);
		Checkfullscreen = (CheckBox)findViewById(R.id.checkfullscreen);
		
		btnConfBack = (TextView)findViewById(R.id.text_playerback);
		btnConfHome = (TextView)findViewById(R.id.text_playerconfigmain);
		
		
		btnConfBack.setOnClickListener(ClickHandler);
		btnConfHome.setOnClickListener(ClickHandler);
		
		
		preferences = getSharedPreferences("11",MODE_PRIVATE); //�޸�MODE_WORLD_READABLE
        //����޸��� 
        editor = preferences.edit(); 
        LoadRectparm( );
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Button btn = (Button)findViewById(R.id.btnRectParmsave);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				 editor.putBoolean("fullscreen", Checkfullscreen.isChecked());
				 editor.putInt("textltx", Integer.valueOf(textltx.getText().toString()));
				 editor.putInt("textlty", Integer.valueOf(textlty.getText().toString()));
				 editor.putInt("textwidth", Integer.valueOf(textwidth.getText().toString()));
				 editor.putInt("textheigh", Integer.valueOf(textheigh.getText().toString()));
				 editor.commit(); 
				 intent.putExtra("textltx",   Integer.valueOf(textltx.getText().toString()));
				 intent.putExtra("textlty",	  Integer.valueOf(textlty.getText().toString()));
				 intent.putExtra("textwidth", Integer.valueOf(textwidth.getText().toString()));
				 intent.putExtra("textheigh", Integer.valueOf(textheigh.getText().toString()));
				 intent.putExtra("fullscreen", Checkfullscreen.isChecked());
				 
				 PlayerConfigActivity.this.setResult(RESULT_OK, intent);//���÷������� 
				 PlayerConfigActivity.this.finish();//�ر�Activity 
			     // �ύ���д�������� 	
			}
		});
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("=============playconfigActivity onStop()");
		super.onStop();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println("=============playconfigActivity onStart()");
		super.onStart();
	}
	@Override
	protected void onResume() {
		System.out.println("=============playconfigActivity onResume()");
			super.onResume();
			
	}
	
	void LoadRectparm(){
		Checkfullscreen.setChecked((preferences.getBoolean("fullscreen", false)));
		textltx.setText(String.valueOf(preferences.getInt("textltx", 0)));
		textlty.setText(String.valueOf(preferences.getInt("textlty", 0)));
		textwidth.setText(String.valueOf(preferences.getInt("textwidth", 200)));
		textheigh.setText(String.valueOf(preferences.getInt("textheigh", 200)));
	}
	View.OnClickListener ClickHandler = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_playerback:
				System.out.println("=============playconfigActivity �����˳�");
				finish();
				startActivity(new Intent(PlayerConfigActivity.this,PlayerActivity.class));
				break;
			
			case R.id.text_playerconfigmain:
				startActivity(new Intent(PlayerConfigActivity.this,HomePageActivity.class));
				break;

			default:
				break;
			}
			
		}
		
	};
}