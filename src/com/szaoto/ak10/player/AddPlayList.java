package com.szaoto.ak10.player;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.R;
import com.szaoto.ak10.adapter.FileAdapter;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom1.MediaFileFilter;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.util.TraverseDictionary;
import com.szaoto.ak10.util.UsbStatesReceiver;
import com.szaoto.ak10.util.XmlTool;

/*
 * ����:������ƵͼƬ�ļ���
 * ���ܣ�������ӣ�����sdcardĿ¼��ѡ���ļ������ӵ������б�
 * ����:zhangsj
 * �������ڣ�2014-05-08
 */
public class AddPlayList extends Activity implements IInfoChangeObserver{
	public MyHandler mhandler;
	public UsbStatesReceiver usbstates;
	TextView Usb;
	public  String Usb_PATH ; 
	private Button btn_save_list;
	private ListView lv_add_list;
	private Button btn_back_list;
	// ��¼��ǰ�ĸ��ļ���
	File currentParent;
	// ��¼��ǰ·���µ������ļ��е��ļ�����
	File[] currentFiles;
	private Button btn_SelectAll;

	/**
	 * ���� ѡ�в��� ������ ȫѡ �� ȡ��ȫѡ
	 * 
	 * true(ȫѡ),false(��ѡ)
	 * 
	 * @param checked
	 */
	private void SelectAllItem(boolean checked) {

		// ��� �ڴ� HomePageActivity.VideoFileList_Added
		PlayerActivity.VideoFileList_Added.clear();
		if (checked == true) {

			// �ж� ��� ��ȫѡ���� ����
			VideoFile videoFile;

			// �� ��ǰ �ļ� �����ļ����ˣ��ж� �ļ� �Ƿ� �� ��Ƶ�ļ� �� ͼƬ�ļ�
			for (File file : currentFiles) {

				// ��� ����Ƶ�ļ� �� ͼƬ�ļ� ���� ���� �� �ڴ� HomePageActivity.VideoFileList_Added
				// ��
				if (MediaFileFilter.isVideoFile(file)
						|| MediaFileFilter.isImageFile(file)) {

					videoFile = new VideoFile();

					videoFile.setId("");
					// ��ȡʱ��
					videoFile.setDuration("10");
					videoFile.setFileName(file.getName());
					videoFile.setFilePath(file.getPath());
					videoFile.setImagePath(file.getPath());
					videoFile.setSpecialEffect("1");

					PlayerActivity.VideoFileList_Added.add(videoFile);
				}
			}

			// ����� ȡ��ȫѡ���� ����� HomePageActivity.VideoFileList_Added

		}
		// ֪ͨ adapter��ˢ�� ����

		adapter.notifyDataSetChanged();
	}

	/**
	 * ȫѡ ��ť ��� �¼�
	 */
	private OnClickListener btnSelectAllClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (btn_SelectAll.getText().toString().trim().equals("ȫѡ")||btn_SelectAll.getText().toString().trim().equals("SelectAll")) {

				SelectAllItem(true);

				btn_SelectAll.setText(R.string.text_cancelselectall);
			
		     	}
			   else{
				SelectAllItem(false);
				btn_SelectAll.setText(R.string.text_selectall);
			}
		//	selectAll Cancel SelectAll
		}

	};
	private FileAdapter adapter;
	private List<File> fileList;
	private ImageView Usbmount;
	/**
	 * ������һ��
	 */
	public void backDir() {
		try {

//			PlayerActivity.VideoFileList_Added.clear();
		
			if (!currentParent.getCanonicalPath().equals("/")) {

				// ��ȡ��һ��Ŀ¼
				currentParent = currentParent.getParentFile();
				// �г���ǰĿ¼�µ������ļ�
				currentFiles = currentParent.listFiles();
				if (currentFiles != null) {
					// �ٴθ���ListView
					inflateListView2(currentFiles);
				}
			} else {

				finish();
				Intent intent = new Intent();
				intent.setClass(AddPlayList.this, PlayerActivity.class);
//				startActivity(intent);
				startActivityForResult(intent, RESULT_OK);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void MsgShow(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_play_list);
		SerialPortControlBroadCast.SetCurrentContext(this);
		setupView();
		mhandler = new MyHandler();
		usbstates = new UsbStatesReceiver(this);
		Usb = (TextView)findViewById(R.id.usbstates);
		Usbmount = (ImageView)findViewById(R.id.usbmounted);
		GetDirectory();
		AddListener();
		// ÿ�δ��ļ�������������� HomePageActivity.VideoFileList_Added��
		PlayerActivity.VideoFileList_Added.clear();

	}

	public void GetDirectory() {
		if (Usb_PATH == null) {
			Usb_PATH="/";
		}else {
		Usb_PATH = TraverseDictionary.GetUDiskDir();
		}
		File root = new File(Usb_PATH);
		if (root.exists()) {
			currentParent = root;
			currentFiles = root.listFiles();

			// ��currentFiles���listview
			// inflateListView(currentFiles);
			if (currentFiles != null) {
				inflateListView2(currentFiles);
			}	
		}
	}

	private void inflateListView2(File[] files) {

		fileList = Arrays.asList(files);
		adapter = null;
		adapter = new FileAdapter(fileList, AddPlayList.this);

		lv_add_list.setAdapter(adapter);
		

		// ===========

		// ���� ȫѡ ��ť
		btn_SelectAll.setVisibility(btn_SelectAll.INVISIBLE);
		// �� ��ǰ �ļ� �����ļ����ˣ��ж� �ļ� �Ƿ� �� ��Ƶ�ļ� �� ͼƬ�ļ�
		for (File file : files) {
			// ��� ����Ƶ�ļ� �� ͼƬ�ļ� ���� ���� �� �ڴ� HomePageActivity.VideoFileList_Added
			if (MediaFileFilter.isVideoFile(file)
					|| MediaFileFilter.isImageFile(file)) {
				// ��� ����һ���ļ� �� ��Ƶ�ļ� �� ͼƬ�ļ� ������ʾ ȫѡ ��ť
				btn_SelectAll.setVisibility(btn_SelectAll.VISIBLE);
			}
		}
		adapter.notifyDataSetChanged();
	}
/*
	private void inflateListView(File[] files) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (File file : files) {

			Map<String, Object> listItem = new HashMap<String, Object>();

			if (file.isDirectory()) {
				// ��·���ļ���
				listItem.put("icon", R.drawable.folder);
				listItem.put("fileName", file.getName());

			} else {

				// .AVI, .wma, .rmvb,.rm, .flash,.mp4,.mid, 3GP
				// ���ļ�
				if (MediaFileFilter.isVideoFile(file)) {
					// ����Ƶ�ļ�
					listItem.put("icon", R.drawable.file);
					listItem.put("isSelected", false);

				} else if (MediaFileFilter.isImageFile(file)) {
					// ��ͼƬ�ļ�
					listItem.put("icon", R.drawable.photo);
					listItem.put("isSelected", false);

				} else {
					// ���� �ļ�
					listItem.put("icon", R.drawable.cd_rw_drive);
				}

				listItem.put("fileName", file.getName());

			}

			listItems.add(listItem);
		}

		SimpleAdapter adapter = new SimpleAdapter(AddPlayList.this, listItems,
				R.layout.add_play_list_item, new String[] { "icon", "fileName",
						"isSelected" }, new int[] { R.id.icon, R.id.name,
						id.checkBox_SelectedFile2 });

		lv_add_list.setAdapter(adapter);

		try {
			// tvpath.setText("��ǰ·��Ϊ:" + currentParent.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	private void AddListener() {

		btn_save_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					if (PlayerActivity.VideoFileList_Added.size() > 0) {
						XmlTool.addXmlByVideoFileList(XmlTool.getXmlFilePath(),
								PlayerActivity.VideoFileList_Added);
						MsgShow(getString(R.string.text_add_success));
					
						Intent intent = new Intent();
						intent.setClass(AddPlayList.this, PlayerActivity.class);
//						startActivity(intent);
						startActivityForResult(intent, RESULT_OK);
						finish();

					} else {
						MsgShow(getString(R.string.text_selectaddfile));
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		lv_add_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				int i = position;

				// ����û��������ļ���ֱ�ӷ��أ������κδ���
				if (currentFiles[position].isFile()) {
					// Ҳ���Զ�����չ������ļ���
					File file = currentFiles[position];
                 //�������Ƶ��ͼƬ�ļ�������ʾchecckbox
					if (MediaFileFilter.isVideoFile(file)|| MediaFileFilter.isImageFile(file)){

						CheckBox checkBox = (CheckBox) view
								.findViewById(com.szaoto.ak10.R.id.checkBox_SelectedFile2);
                       //�������ѡ��״̬������Ϊfalse,������Ϊtrue
						if (checkBox.isChecked()) {
							checkBox.setChecked(false);
						} else {
							checkBox.setChecked(true);

						}
					} else {
						MsgShow(getString(R.string.text_play_select));
					}

					return;
				}
				// ��ȡ�û�������ļ��� �µ������ļ�
				File[] tem = currentFiles[position].listFiles();
				if (tem == null || tem.length == 0) {

					Toast.makeText(AddPlayList.this, R.string.text_path_nofiles,
							Toast.LENGTH_LONG).show();
				} else {
					// ��ȡ�û��������б����Ӧ���ļ��У���Ϊ��ǰ�ĸ��ļ���
					currentParent = currentFiles[position];
					// ���浱ǰ�ĸ��ļ����ڵ�ȫ���ļ����ļ���
					currentFiles = tem;
					// �ٴθ���ListView
					inflateListView2(currentFiles);
				}
			}

		});

		btn_back_list.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				backDir();
			}
		});

		btn_SelectAll.setOnClickListener(btnSelectAllClick);
	}
@Override
protected void onDestroy() {
	
	super.onDestroy();
	System.out.println("=============addPlaylist onDestory()");
}
	private void setupView() {
		btn_save_list = (Button) findViewById(R.id.btn_save_list);
		btn_SelectAll = (Button) findViewById(R.id.btn_SelectAll);

		lv_add_list = (ListView) findViewById(R.id.lv_add_list);
		// tvpath = (TextView) findViewById(R.id.tv_filepath_list);
		btn_back_list = (Button) findViewById(R.id.btn_back_list);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == event.KEYCODE_BACK) {
			backDir();
		}

		return true;
	}
	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		 Usb_PATH=xParam2;
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	protected void onStart() {
		super.onStart();
		
		usbstates.registerReceiver();
		System.out.println("=============addPlaylist onStart()");
		System.out.println("=============addPlaylist ע��U��()");
	}
	@Override
	protected void onStop() {
		super.onStop();
		usbstates.unregisterReceiver();
		System.out.println("=============addPlaylist onStop()");
		System.out.println("=============addPlaylist ȡ��U��ע��");
	}
	public class MyHandler extends Handler{
		public MyHandler(){};
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("=============mainactivity handler");
//			Usb.setText("usb states");
			if(msg.arg1 == 0x00021)	{
				Usb.setText(R.string.text_usbmount);
				Usbmount.setVisibility(View.VISIBLE);
			   Usbmount.setBackgroundResource(R.drawable.usb_mounted);}
			else if(msg.arg1 == 0x00022){ 
				Usb.setText(R.string.text_usbunmount);
				Usbmount.setVisibility(View.VISIBLE);
				Usbmount.setBackgroundResource(R.drawable.usb_unmounted);}
				}
		}
	}