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
 * ������EditActivity 
 * ��Ҫ���ܣ��Բ����б��ļ�����ɾ�� ��ק������������б� 
 * ���ߣ�zhangsj 
 * �������ڣ�2014��5��9��
 */
public class EditActivity extends Activity {

	Button btnOK;
	DragListAdapter2 adapter = null;
	ArrayList<String> data = new ArrayList<String>();

	ArrayList<VideoFile> videoFileList = new ArrayList<VideoFile>();
	public  static String videopath=HomePageActivity.CONFIG_PATH+"videofilelist.xml";
	private DragListView2 dragListView;
	/**
	 * ��ǰѡ���������
	 */
//	private int currSelectedPosition = -1;
//	private String currSelectedFileName = "";
	VideoListManager videoListManager = new VideoListManager();
	private Button btn_DeleAll;

	/**
	 * ���ӵ� xml �ļ�
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
	 * �ر� �� Activity������ ������
	 */
	private void ShowMainActivity() {

		finish();

		Intent intent = new Intent();

		intent.setClass(EditActivity.this, PlayerActivity.class);

		startActivity(intent);
	}

	private void deleteAllVideoFile() throws Exception {

		// ɾ�� xml�ļ�
		XmlTool.RemoveAll(XmlTool.getXmlFilePath());

		// ��� �ڴ� ͼƬ����Ϊ �������ӵ��ļ�����������uuid������ �ڴ�ͼƬ�� id���Ѿ���Ч�ˣ�
		PlayerActivity.hashMapImage_Added.clear();

		// ��ɰ�ť
		ShowMainActivity();
	}

	/**
	 * �ж� �༭ �б� �Ƿ��Ѿ� �����
	 * 
	 * @return
	 */
	private boolean isSorted() {
		//int SortedCount = PlayerActivity.VideoFileList_Public.size();
		int SortedCount = PlayerActivity.GetVideoFileList_Public().size();

		return SortedCount > 0;
	}

	/**
	 * �������� ��Ŀ�б� ˳��
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

			// ɾ�� ��� xml�ļ� ���� item ��
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
						// ѭ�� ���ӵ� xml ��
						addFileToXml(videoFile, filePath);
					}
				}

			}
		}
	}

	/**
	 * �������� ��Ŀ�б� ˳��
	 * 
	 * @throws Exception
	 */
	private void ReSortViedoList2() throws Exception {

		if (isSorted()) {

			Log.i("˳�����仯", "��ʼ��������");

			// int count = HomePageActivity.VideoFileList_Public.size();

			// ʵ���� xml �ļ�·��
			String xmlFilePath = XmlTool.getXmlFilePath();

			// �� �µ� list�б����󣬴��뵽 xmlTool ������ �� �½� xml�ĵ����󷽷��С�
			// ���ڴ������´����µ�xml�ĵ�����
			// ������ͨ��xml�ļ�·��������ԭ��xml�ĵ����ݡ�
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
		System.out.println("���ý�Ŀ�༭Activity------onCreate()����");
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
		// ���ݽ��
		data = new ArrayList<String>();

		// for (int i = 0; i < 24; i++) {
		// data.add("Aѡ��" + i);
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
	 * �����¼�
	 */
	private void AddListener() {

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					// ��ȡ��ǰ ���µ� �б����򣬼� 1-10��ÿ�� tvItemIndex ��ֵ

					ReSortViedoList2();
//					ReSortViedoList();

					// ShowMainActivity();

					// finish();

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					Log.e("����1", e.getMessage());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("����2", e.getMessage());

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
		System.out.println("���ý�Ŀ�༭Activity------onStaret()����");
		super.onStart();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("���ý�Ŀ�༭Activity------onPause()����");
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("���ý�Ŀ�༭Activity------onstop()����");
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("���ý�Ŀ�༭Activity------onDestroy()����");
		super.onDestroy();
	}
	
	
}