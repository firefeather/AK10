package com.szaoto.ak10.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.R.id;
import com.szaoto.ak10.adapter.DragListAdapter2;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom1.DragListView2;
import com.szaoto.ak10.custom1.VideoListManager;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.util.XmlTool;

/**
 * 类名：EditActivity 
 * 主要功能：对播放列表文件进行删除 拖拽操作保存更新列表 
 * 作者：zhangsj 
 * 创建日期：2014年5月9日
 */
public class EditActivity extends Activity {

	Button btnOK;
	DragListAdapter2 adapter = null;
	ArrayList<String> data = new ArrayList<String>();

	ArrayList<VideoFile> videoFileList = new ArrayList<VideoFile>();
	public  static String videopath=HomePageActivity.CONFIG_PATH+"videofilelist.xml";
	private DragListView2 dragListView;
	/**
	 * 当前选中项的索引
	 */
//	private int currSelectedPosition = -1;
//	private String currSelectedFileName = "";
	VideoListManager videoListManager = new VideoListManager();
	private Button btn_DeleAll;

	/**
	 * 添加到 xml 文件
	 * 
	 * @param fileName
	 * @param filePath
	 * @param videofilePath
	 * @param imagePath
	 * @throws Exception
	 */
	protected void addToXml(String fileName, String filePath,
			String videofilePath, String imagePath, String duration,
			String specialEffect) throws Exception {

		XmlTool.addXml(filePath, fileName, videofilePath, imagePath, duration,
				specialEffect);

	}

	private void addFileToXml(VideoFile videoFile, String filePath) {
		try {

			addToXml(videoFile.getFileName(), filePath,
					videoFile.getFilePath(), videoFile.getFilePath(),
					videoFile.getDuration(), videoFile.getSpecialEffect());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 关闭 本 Activity，返回 主界面
	 */
	private void ShowMainActivity() {

		finish();

		Intent intent = new Intent();

		intent.setClass(EditActivity.this, PlayerActivity.class);

		startActivity(intent);
	}

	private void deleteAllVideoFile() throws Exception {

		// 删除 xml文件
		XmlTool.RemoveAll(XmlTool.getXmlFilePath());

		// 清空 内存 图片，因为 后面添加的文件，会新生成uuid，导致 内存图片的 id，已经无效了，
		PlayerActivity.hashMapImage_Added.clear();

		// 完成按钮
		ShowMainActivity();
	}

	/**
	 * 判断 编辑 列表 是否已经 排序过
	 * 
	 * @return
	 */
	private boolean isSorted() {
		//int SortedCount = PlayerActivity.VideoFileList_Public.size();
		int SortedCount = PlayerActivity.GetVideoFileList_Public().size();

		return SortedCount > 0;
	}

	/**
	 * 重新排列 节目列表 顺序
	 * 
	 * @throws Exception
	 */
	private void ReSortViedoList() throws Exception {
		if (isSorted()) {

			View view;

			TextView textView;

			// File xmlFile = new
			// File(Environment.getExternalStorageDirectory(),
			// "/AK10Player/data/videofilelist.xml");

			File xmlFile = new File(videopath);

			String filePath = xmlFile.getPath();

			// 删除 清空 xml文件 所有 item ，
			XmlTool.RemoveAll(filePath);

			VideoFile videoFile;

			int allCount = videoFileList.size();

			String itemId = "";
			String OldItemId = "";

			for (int i = 0; i < allCount; i++) {

				videoFile = new VideoFile();

				view = dragListView.getChildAt(i);

				textView = (TextView) view.findViewById(id.tvId);

				itemId = textView.getText().toString().trim();

				for (VideoFile videoFile2 : videoFileList) {

					OldItemId = videoFile2.getId().trim();

					if (OldItemId.equals(itemId)) {

						videoFile = videoFile2;
						// 循环 添加到 xml 中
						addFileToXml(videoFile, filePath);
					}
				}

			}
		}
	}

	/**
	 * 重新排列 节目列表 顺序
	 * 
	 * @throws Exception
	 */
	private void ReSortViedoList2() throws Exception {

		if (isSorted()) {

			Log.i("顺序发生变化", "开始重新排序");

			// int count = HomePageActivity.VideoFileList_Public.size();

			// 实例化 xml 文件路径
			String xmlFilePath = XmlTool.getXmlFilePath();

			// 将 新的 list列表对象，传入到 xmlTool 工具类 的 新建 xml文档对象方法中。
			// 在内存中重新创建新的xml文档对象，
			// 创建后，通过xml文件路径，覆盖原有xml文档内容。
			// XmlTool.createXmlFileByVideoFileList(xmlFilePath,
			// videoFileList_New);
			// XmlTool.RemoveAll(xmlFilePath);

			//boolean createOk = XmlTool.createXmlFileByVideoFileList(
			//		xmlFilePath, PlayerActivity.VideoFileList_Public);

			boolean createOk = XmlTool.createXmlFileByVideoFileList(
					xmlFilePath, PlayerActivity.GetVideoFileList_Public());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("调用节目编辑Activity------onCreate()方法");
		setContentView(R.layout.activity_edit);
		SerialPortControlBroadCast.SetCurrentContext(this);
		setUpView();

		try {
			initData();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter = new DragListAdapter2(this, data, videoFileList);
		dragListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		AddListener();

		//PlayerActivity.VideoFileList_Public.clear();
		PlayerActivity.GetVideoFileList_Public().clear();
	}

	private void MsgShow(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public void initData() throws Exception {
		// 数据结果
		data = new ArrayList<String>();

		// for (int i = 0; i < 24; i++) {
		// data.add("A选项" + i);
		// }

		// R.xml.videofilelist;

		// XmlPullParser xmlPullParser = this.getResources().getXml(
		// R.xml.videofilelist);

		XmlPullParser xmlPullParser = getXmlPullParser();

		data = videoListManager.getObjectList(xmlPullParser);

		XmlPullParser xmlPullParser2 = getXmlPullParser();
		videoFileList = videoListManager.getObjectList2(xmlPullParser2);
	}

	private XmlPullParser getXmlPullParser() throws XmlPullParserException,
			FileNotFoundException {
		// File xmlFile = new File(Environment.getExternalStorageDirectory(),
		// "/AK10Player/data/videofilelist.xml");

		File xmlFile = new File(videopath);

		if (!xmlFile.exists()) {

			XmlTool.CopyXmlFile(this);

			// xmlFile = new File(Environment.getExternalStorageDirectory(),
			// "/AK10Player/data/videofilelist.xml");

			xmlFile = new File(videopath);

		}

		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance()
				.newPullParser();

		xmlPullParser.setInput(new FileInputStream(xmlFile), "utf-8");
		return xmlPullParser;
	}

	/**
	 * 监听事件
	 */
	private void AddListener() {

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					// 获取当前 最新的 列表排序，即 1-10，每个 tvItemIndex 的值

					ReSortViedoList2();
//					ReSortViedoList();

					// ShowMainActivity();

					// finish();

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					Log.e("错误1", e.getMessage());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("错误2", e.getMessage());

				}

				ShowMainActivity();
			}

		});

		btn_DeleAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					deleteAllVideoFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void setUpView() {
		btnOK = (Button) findViewById(R.id.btnOK);

		btn_DeleAll = (Button) findViewById(R.id.btn_DeleAll);

		dragListView = (DragListView2) findViewById(R.id.DragListView2_edit);

		// lv.setOnCreateContextMenuListener(lvOnCreateContextMenu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// ShowMainActivity();
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println("调用节目编辑Activity------onStaret()方法");
		super.onStart();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("调用节目编辑Activity------onPause()方法");
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("调用节目编辑Activity------onstop()方法");
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("调用节目编辑Activity------onDestroy()方法");
		super.onDestroy();
	}
	
	
}
