package com.szaoto.ak10.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.szaoto.ak10.R;
import com.szaoto.ak10.R.id;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom1.VideoListManager;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.util.XmlTool;
/**
 * 类名：EditPlaylist
 * 功能：长按编辑播放列表文件
 *       1、可以对播放列表中的视频文件时长，特效进行修改
 *       2、完成操作，保存，更新播放列表，重新写入xml文件中
 * 作者： zhangsj
 * 修改日期：2014年5月9日
 */
public class EditPlaylist extends Activity {

	private Button btnEditSave;
	private EditText etFileName;
	private String currSelectedFileName;
	private String Duration;
	private String SpecialEffect;
	VideoListManager videoListManager = new VideoListManager();
	private EditText etDuration;
	private Spinner specialEf;
	private VideoFile videoFile;

	private void setView() {
		btnEditSave = (Button) findViewById(id.btnEditSave);

		etFileName = (EditText) findViewById(id.etFileName);
		etDuration = (EditText) findViewById(id.etDuration);
		// etSpecialEffect = (EditText) findViewById(id.etSpecialEffect);
		specialEf = (Spinner) findViewById(R.id.specialEf);
		String[] mItems = getResources().getStringArray(R.array.planets_arry);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, mItems);
		specialEf.setAdapter(adapter);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setData();
		super.onResume();
	}

	private void setData() {

		videoFile = (VideoFile) getIntent().getSerializableExtra("videoFile");

		currSelectedFileName = videoFile.getFileName();

		Duration = videoFile.getDuration();

		SpecialEffect = videoFile.getSpecialEffect();

		etDuration.setText(Duration);
		// etSpecialEffect.setText(SpecialEffect);
		//添加视频特效
		int position = 0;
		String[] mItems = getResources().getStringArray(R.array.planets_arry);
		int i = 0;
		for (i = 0; i < mItems.length; i++) {
			if (SpecialEffect.equalsIgnoreCase(mItems[i])) {
				position = i;
				break;
			}
		}
		specialEf.setSelection(position);
		etFileName.setText(currSelectedFileName);

		etFileName.setFocusable(true);

		etFileName.setSelection(etFileName.getText().length());

	}

	private void setListener() {

		btnEditSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String filePath = videoFile.getFilePath();

					// 文件 所在文件夹 路径
					String rawFilePath = filePath.replace(
							videoFile.getFileName(), "").trim();

					videoFile.setFileName(etFileName.getText().toString());

					videoFile.setFilePath(rawFilePath + videoFile.getFileName());

					videoFile.setDuration(etDuration.getText().toString());
					//videoFile.setDuration(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                    String[] mItems = getResources().getStringArray(R.array.planets_arry);
                                      
                    String tempS =  mItems[specialEf.getSelectedItemPosition()];
                    
					videoFile
							.setSpecialEffect(tempS);
					// videoFile.setSpecialEffect(etSpecialEffect.getText()
					// .toString());
					XmlTool.updateXml(XmlTool.getXmlFilePath(), videoFile);

					MsgShow("编辑成功！");

					finish();
					Intent intent = new Intent();
					intent.setClass(EditPlaylist.this, PlayerActivity.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void MsgShow(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	// ===========================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_play_list);
		SerialPortControlBroadCast.SetCurrentContext(this);
		setView();
		setListener();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			Intent intent = new Intent();
			intent.setClass(EditPlaylist.this, PlayerActivity.class);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
