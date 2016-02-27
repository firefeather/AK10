/*
 * �ļ��� PlayerActivity.java
 * ���������б�com.szaoto.ak10.player
 * �汾��Ϣ���汾��
 * ��������2013��11��8������11:54:07
 * ��Ȩ���� liangdb-szaoto
 */
package com.szaoto.ak10.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.TrackInfo;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.adapter.DragListAdapter;
import com.szaoto.ak10.adapter.DragListAdapter2;
import com.szaoto.ak10.common.SystemConfig;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom1.DragListView2;
import com.szaoto.ak10.custom1.VideoListManager;
import com.szaoto.ak10.dataaccess.DataAccessSystemConfig;
import com.szaoto.ak10.dataaccess.returnClass;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.util.XmlTool;

/*
 * ����PlayerActivity
 * ���� liangdb
 * ��Ҫ���� ������������
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 * �޸��ߣ�zhangsj
 * �޸����ڣ�2014��5��9��
 * �޸����ݣ��ϲ������б����Ž���
 */
public class PlayerActivity extends Activity{

	Button btnOK;
	ArrayList<VideoFile> data = new ArrayList<VideoFile>();
	ArrayList<String> data1 = new ArrayList<String>();
	ArrayList<VideoFile> videoFileList = new ArrayList<VideoFile>();
	public String CONFIG_PATH;

//	private final int MESSAGE_PROCESS = 2;

	private static final String TAG = "PlayerActivity";
	private ImageButton playStopButton;
	private ImageButton btn_PlayerList;
	private TextView tvElapsed;
	private TextView tvTotal;
	private SurfaceView surfaceView;
	private MediaPlayer mediaPlayer=null;
	private SeekBar skb_video = null;
	private String filename; // ��ǰ�����ļ�������
	private int position; // ��¼����λ��
	// private int mCurrentPosition;
	private int mPlayWidth; // ���Ŵ��ڿ��
	private int mPlayHeight; // ���Ŵ��ڸ߶�

	private boolean isChanging = false;// �����������ֹ��ʱ����SeekBar�϶�ʱ���ȳ�ͻ
	private LinearLayout llPlayercontrolLayout; // ����������
	private LinearLayout llPlayerlistLayout; // �����б�
	private RelativeLayout llPlayerTitle;// �����б���
	// private DragListView2 mShowAll; //�༭�������ק
	private DragListView2 mDragEditlist; // �༭�������ק
	// private EditListAdapter mSelectAdapter; //�༭����������
	private DragListAdapter2 mSelectAdapter;

	// ��Ӻͱ༭�����б�ť
	private Button btn_AddProgram;
	private Button btn_EditProgram;

	Timer mTimer = null;
	TimerTask mTimerTask = null;

	// �û�ƫ������
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private boolean m_Run;

	int lastPoint = 0;
	// boolean isBack=false;
	// MyAdapater adapater;
	ListView playlistView;
	DragListAdapter playAdapter;
	LinearLayout abserfaceLay;
	DisplayMetrics dm;
	SystemConfig systemConfig = null;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	// ��һ��������playerActivity�����Ӳ�ͬ����ģ��
	private TextView playHome;
	private TextView playSeting;
	private TextView playBack;
	private SurfaceHolder mSurHolder;
	private ImageButton playStart;
	private ImageButton playPause;


	
	/**********************************************************/
	/**
	 * ���õ� ����˳�����б���
	 */
	private static ArrayList<VideoFile> VideoFileList_Public = new ArrayList<VideoFile>();

	/**
	 * ���õ� ��ѡ�е� �б��� --�������Ƶ�ļ���
	 */
	public static ArrayList<VideoFile> VideoFileList_Added = new ArrayList<VideoFile>();

	/**
	 * ���õ� �Ѽ��ص� �ڴ�� ͼƬ�������ȡ�������ٴ�ͨ���ļ�·����ȡͼƬ��
	 */
	public static HashMap<String, Bitmap> hashMapImage_Added = new HashMap<String, Bitmap>();

	/**
	 * ���� Ĭ�� ���� ��ͼƬ
	 */
	public static Bitmap bitmap;

	/**
	 * ���� Ĭ��ͼƬ �� ��ȣ��߶�
	 */
	public static int imageDefaultWidth, imageDefaultHeight;
	/**
	 * ���� Ĭ��ͼƬ �� ��ȣ��߶�
	 */
	
	public static void SetVideoFileList_Public(ArrayList<VideoFile> videolist){
		VideoFileList_Public = videolist;
	}
	public static ArrayList<VideoFile> GetVideoFileList_Public(){
		return VideoFileList_Public;
	}
	public static void SetVideoFileList_Added(ArrayList<VideoFile> videolist){
		VideoFileList_Added = videolist;
	}
	public static ArrayList<VideoFile> GetVideoFileList_Added(){
		return VideoFileList_Added;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("��ʼ����Activity+++++oncreate()����");
		setContentView(R.layout.player);
//<<<<<<< .mine
//	    preferences = getSharedPreferences("11", MODE_PRIVATE);
//=======
		SerialPortControlBroadCast.SetCurrentContext(this);
		mTimer = new Timer();
//		navigationBar = new NavigationBar("player_PlayerActivity", this);
		preferences = getSharedPreferences("11", MODE_PRIVATE);

		// ����޸���
		editor = preferences.edit();
		initView();
		playVideo();
		
	}

	private void playVideo() {
		try {
			dataInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTimer = new Timer();
		surfaceView = new SurfaceView(this);
		mSurHolder = surfaceView.getHolder();

		systemConfig = DataAccessSystemConfig.LoadSystemConfig();
		mediaPlayer = new MediaPlayer();
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// ������ʾ�����С
		abserfaceLay = (LinearLayout) findViewById(R.id.abserfaceLay);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				dm.widthPixels, dm.heightPixels, 0);
		abserfaceLay.addView(surfaceView, lp);
		 // ������Ƶ������
		
		/* ��������Surface��ά���Լ��Ļ����������ǵȴ���Ļ����Ⱦ���潫�������͵��û���ǰ */
//		this.surfaceView.getHolder().setType(
//				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//		this.surfaceView.getHolder().setFixedSize(mPlayWidth, mPlayHeight);// ���÷ֱ���
//		// this.surfaceView.getHolder().setFixedSize(dm.widthPixels,
//		// dm.heightPixels);
//		this.surfaceView.getHolder().setKeepScreenOn(true);
//		this.surfaceView.getHolder().addCallback(new SurfaceListener());
	
		
		
		
	}

	private void initView() {
		

		playHome = (TextView) findViewById(R.id.text_playermain);
		playSeting = (TextView) this.findViewById(R.id.text_playersetting);
		playBack = (TextView) this.findViewById(R.id.text_playerback);

		playHome.setOnClickListener(clickHandler);
		playSeting.setOnClickListener(clickHandler);
		playBack.setOnClickListener(clickHandler);

		playStopButton = (ImageButton) this.findViewById(R.id.btn_Stop);
		playStart = (ImageButton) this.findViewById(R.id.btn_Play);
		playPause = (ImageButton) this.findViewById(R.id.btn_pause);
		btn_PlayerList = (ImageButton) this.findViewById(R.id.btn_PlayerList);

		btn_AddProgram = (Button) findViewById(R.id.btn_AddProgram);
		btn_EditProgram = (Button) findViewById(R.id.btn_EditProgram);

		btn_AddProgram.setOnClickListener(clickHandler);
		btn_EditProgram.setOnClickListener(clickHandler);

		ButtonClickListener listener = new ButtonClickListener();
		playStopButton.setOnClickListener(listener);
		playPause.setOnClickListener(listener);
		playStart.setOnClickListener(listener);
		btn_PlayerList.setOnClickListener(listener);

		tvElapsed = (TextView) findViewById(R.id.tv_Elapsed);
		tvTotal = (TextView) findViewById(R.id.tv_Total);

		tvElapsed.setText("00:00");
		tvTotal.setText("00:00");

		skb_video = (SeekBar) this.findViewById(R.id.seekBar_Progress);
		skb_video.setOnSeekBarChangeListener(new SeekBarChangeEvent());

		// ���ſ���
		llPlayerTitle = (RelativeLayout) findViewById(R.id.Layout_PlayerTitle);
		llPlayerTitle.bringToFront();
		// ���ſ���
		llPlayercontrolLayout = (LinearLayout) findViewById(R.id.Layout_PlayerControl);
		llPlayercontrolLayout.bringToFront();
		// llPlayercontrolLayout.setAlpha(90);

		// �����б�
		llPlayerlistLayout = (LinearLayout) findViewById(R.id.Layout_PlayerList);

		llPlayerlistLayout.bringToFront();

		llPlayerlistLayout.setVisibility(View.GONE);
		mDragEditlist = (DragListView2) findViewById(R.id.del_win_show_all);
		// llPlayerlistLayout.setAlpha(90);

		m_Run = true;

				
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == 0) {
		 Log.i("onActivity", "����Activitiy����");
		}
		else {
			
//			onResume();
			// dm.widthPixels, dm.heightPixels
//			abserfaceLay.removeView(surfaceView); // �����ӽ�����ʾ��
			if (data.getExtras().getBoolean("fullscreen") == true) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						dm.widthPixels, dm.heightPixels, 0);
				abserfaceLay.addView(surfaceView, lp);
			} else {
				mPlayWidth = data.getExtras().getInt("textwidth");
				mPlayHeight = data.getExtras().getInt("textheigh");
				// ������ʾ�����С

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						mPlayWidth, mPlayHeight, data.getExtras().getInt(
								"textltx"));
				abserfaceLay.addView(surfaceView, lp);
			}
			
			 Log.i("onActivity", "��������");
		}
		System.out.println("���ò�����Activity------onActivityResult()����");
	}

	// ����ʱ����Ҫ��ʾ����Ϣ
	private void ShowEditPlayListActivity(int Position) {
		// finish();
		VideoFile videoFile = data.get(Position); // ÿ��λ�ã���Ӧ��data��

		Intent intent = new Intent();

		intent.putExtra("videoFile", videoFile); // ����ʵ�����

		intent.setClass(PlayerActivity.this, EditPlaylist.class);
		startActivity(intent);
	}

	// �Զ���Ƚ���
	class MyComparator implements Comparator<File> {
		@Override
		public int compare(File lhs, File rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}

	}

	// TODO --Class-- MyAdapater �Զ���������
	// ��ť����¼�
	View.OnClickListener clickHandler = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				// ��ӽ�Ŀ��ť
				case R.id.text_playermain:
					finish();
					Intent intentHome = new Intent(PlayerActivity.this,
							HomePageActivity.class);
					startActivity(intentHome);
					// CustomToast.showToast(PlayerActivity.this, "", 200);
					break;
				case R.id.text_playersetting:
					Intent intentSeting = new Intent(PlayerActivity.this,
							PlayerConfigActivity.class);
					startActivity(intentSeting);

					// CustomToast.showToast(PlayerActivity.this, "", 200);
					break;
				case R.id.text_playerback:
					  finish();
					
					 Intent intentBack = new Intent(PlayerActivity.this,
					 HomePageActivity.class); 
					 startActivity(intentBack);
					 

					// CustomToast.showToast(PlayerActivity.this, "", 200);
					break;
				case R.id.btn_AddProgram:
//					finish();
					Intent intent = new Intent(PlayerActivity.this,
							AddPlayList.class);
					startActivity(intent);

					// CustomToast.showToast(PlayerActivity.this, "", 200);
					break;
				case R.id.btn_EditProgram:

					Log.i("���-�༭��ť", "����༭����");
//					finish();

					Intent intentE = new Intent(PlayerActivity.this,
							EditActivity.class);
					startActivity(intentE);
					

					break;

				default:
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// ���ݳ�ʼ��
	public void dataInit() throws Exception {
		CONFIG_PATH = this.getFilesDir() + "//config//";
		File xmlFile = new File(CONFIG_PATH, "videofilelist.xml");
		System.out.println("xml�ļ���ŵ�·���ǣ�" + CONFIG_PATH);
		System.out.println("��Ҫ������xml�ļ��ǣ�" + xmlFile);

		if (!xmlFile.exists()) {
			// ���������xml�ļ�������Ŀ¼���ļ���ɾ�����Ǿ͸���һ���ļ���Ŀ¼
			XmlTool.CopyXmlFile(this);
			xmlFile = new File(Environment.getDataDirectory(),
					"/data/com.szaoto.ak10/files/config/videofilelist.xml");
			System.out.println("���û��xml�ļ�����ô���¸����ļ���" + xmlFile);
		}
		/*
		 * �������ݲ��� 1�������������� 2������������ 3����������Դ 4����ʼ����
		 */
		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance()
				.newPullParser();
		xmlPullParser.setInput(new FileInputStream(xmlFile), "utf-8");
		data = VideoListManager.getObjectList2(xmlPullParser);
//		filename = data.get(position).getFilePath();
		System.out.println("��ʼ�������ݣ�" + data);
		playlistView = (ListView) findViewById(R.id.list);
		playAdapter = new DragListAdapter(this, data);
		playlistView.setAdapter(playAdapter);
		playAdapter.notifyDataSetChanged();
	}

	// ������ģ��İ�ť���������
	private class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.btn_Play:// ���Բ��Ű�ť
				{
					boolean isPlay = true;
					
										
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					
						playStart.setImageResource(R.drawable.play);
						isPlay=false;
						
					}else {
						mediaPlayer.start();
						playStart.setImageResource(R.drawable.pause);
						isPlay=true;
					}
				}
					break;
//				case R.id.btn_pause:// ���Բ��Ű�ť
//					mediaPlayer.pause();
//					break;
				case R.id.btn_Stop:// ���Բ��Ű�ť
				   //if (mediaPlayer.isPlaying()||mediaPlayer!=null) {
					if (mediaPlayer.isPlaying()) {					   
					   mediaPlayer.stop();
					   skb_video.setProgress(0);
					   surfaceView.setVisibility(View.INVISIBLE);
					  
//					   surfaceView.
				}
			     	break;
				// play();
				// if (mediaPlayer.isPlaying()) {
				// mediaPlayer.pause();
				// playStopButton.setImageResource(R.drawable.play);
				// if (mTimerTask != null) {
				// mTimerTask.cancel();
				// // mTimerTask = null;
				// }
				// if (mTimer != null) {
				// mTimer.cancel();
				// // mTimer = null;
				// }
				// } else {
				// mediaPlayer.prepareAsync();
				// mediaPlayer.start();
				// playStopButton.setImageResource(R.drawable.pause);
				// onResumePlay();
				// }////////////

				case R.id.btn_PlayerList:
					if (View.VISIBLE == llPlayerlistLayout.getVisibility()) {
						llPlayerlistLayout.setVisibility(View.INVISIBLE);
					} else {
						llPlayerlistLayout.setVisibility(View.VISIBLE);
					}
					break;
				default:

					break;
				}
			} catch (Exception e) {

				Log.e(TAG, e.toString());
			}
		}
	}

//	// ������Ƶ��������¼�
//	private class SurfaceListener implements SurfaceHolder.Callback {
//
//		@Override
//		// �����޸�
//		public void surfaceChanged(SurfaceHolder holder, int format, int width,
//				int height) {
//			Log.i("TAG", "surfaceChanged");
//		}
//
//		@Override
//		// ���洴��
//		public void surfaceCreated(SurfaceHolder holder) {
//			// try {
//			// // mediaPlayer = new MediaPlayer();
//			// mediaPlayer.setDisplay(mSurHolder);
//			// mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			// mediaPlayer.setOnBufferingUpdateListener((OnBufferingUpdateListener)
//			// this);
//			// mediaPlayer.setOnPreparedListener(preparedListener);
//			//
//			//
//			// } catch (Exception e) {
//			// // TODO: handle exception
//			// Log.e("mediaPlayer", "error",e);
//			// }
//			Log.e("mediaPlayer", "surface created");
//			// }
//			// ������onResume()�󱻵���
////			 try {
////				
////			 if ( mediaPlayer !=null) {
//////						 mediaPlayer = new MediaPlayer();
////			 // ������Ƶ������
////			 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////			 // ������Ƶ��ʾλ��
////			 mediaPlayer.setDisplay(mSurHolder);
////			 // ���ò��ŵ���Դ·��
////			 mediaPlayer.setDataSource(filename);
////			 // ׼������
////			 // mMediaPlayer.prepare();
////			 mediaPlayer.prepareAsync();
////			
////			 // ׼�����ŵļ�����
////			 mediaPlayer
////			 .setOnPreparedListener(new OnPreparedListener() {
////			
////			 @Override
////			 public void onPrepared(MediaPlayer mp) {
////			 // ��ʼ����
////			 mediaPlayer.start();
////			 mediaPlayer.seekTo(position);
////			 // onResumePlay();
////			 // int nDuration = mediaPlayer.getDuration();
////			 // skb_video.setMax(nDuration);
////			 // tvElapsed.setText("00:00");
////			 // tvTotal.setText(getTimeStringByMilliseconds(nDuration));
////			 // mediaPlayer.start();
////			
////			 }
////			 });
////			 // ���ò��Ű�ť���ܵ��
////			 playStopButton.setEnabled(false);
////			 // ������ɵ�״̬
////			 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
////			
////			 @Override
////			 public void onCompletion(MediaPlayer mp) {
////			 playStopButton.setEnabled(true);
////			 }
////			 });
////			
////			 }
////			 } catch (IOException e) {
////			 e.printStackTrace();
////			 }
//			
//			
//
//		}
//
//		@Override
//		// ��������
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			// Log.i("TAG", "surfaceDestroyed");
//			Log.e("mediaPlayer", "surface destroyed");
//			// ��SurfaceView������֮ǰ���浱ǰ����Ƶ����λ��
//			// if (mediaPlayer != null) {
//			// position = mediaPlayer.getCurrentPosition();
//			// mediaPlayer.stop();
//			// mediaPlayer.release();
//			// }
//		}
//	}

	public void onResumePlay() {

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							int nCurrentPosition = mediaPlayer
									.getCurrentPosition();
							if (60 * 60 < nCurrentPosition / 1000) {
								return;
							}
							skb_video.setProgress(nCurrentPosition);
							tvElapsed.setText(getTimeStringByMilliseconds(nCurrentPosition));
							System.out.println("�ı������ʱ����ʾ����+++++"+nCurrentPosition+"ms");
						}
					});

				}
			};
			if (mTimer == null) {
				mTimer = new Timer();
			}
			mTimer.schedule(mTimerTask, 0, 300);
		} else {
			
		}

	}

	/**
	 * ������Ƶ
	 */
	public void play() throws IOException {
		try {

			mediaPlayer.reset();
			mediaPlayer.getCurrentPosition();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// ������Ƶ��ʾλ��

			mediaPlayer.setDisplay(mSurHolder);
			// ���ò��ŵ���Դ·��
			mediaPlayer.setDataSource(filename);
			System.out.println("���ò�����ƵԴ:"+filename);
			// ׼������
			// mMediaPlayer.prepare();
			mediaPlayer.prepareAsync();
			System.out.println("׼���첽����ģʽ:"+filename);
			// ׼�����ŵļ�����
			mediaPlayer.setOnPreparedListener(preparedListener);

			// ���ò��Ű�ť���ܵ��
			playStart.setEnabled(false);
			// ������ɵ�״̬
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					playStart.setEnabled(true);
					mediaPlayer.release();
//					skb_video.setProgress(0);
//					skb_video.invalidate();
					System.out.println("�������:"+filename);
				}
			});

			// ���ŵĹ����г������� ���ò��Ű�ť�ܵ��
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					playStopButton.setEnabled(false);
					System.out.println("���Ŵ���--------"+what);
					return false;
				}
			});
			mediaPlayer.setOnInfoListener(new OnInfoListener() {
				
				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					System.out.println("������Ϣ--------"+what);
					System.out.println("������Ϣ--------"+extra);
					return false;
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	}

	OnPreparedListener preparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			int nDuration = mediaPlayer.getDuration();
			skb_video.setMax(nDuration);
			tvElapsed.setText("00:00");
			tvTotal.setText(getTimeStringByMilliseconds(nDuration));
		    mediaPlayer.start();
		    mediaPlayer.setLooping(true);
			System.out.println("��ʼ������Ƶ:"+filename);
			playStart.setEnabled(true);
			
			onResumePlay();

		}
	};

	// ��ȡ��Ƶ��Ŀʱ��
	private String getTimeStringByMilliseconds(int nDuration) {
		String sTimeString = "";
		int nSecondTime = nDuration / 1000;
		if (1 <= nSecondTime / (60 * 60)) {
			// Сʱ
			int nHour = nSecondTime / (60 * 60);
			int nMinute = ((nSecondTime - (60 * nHour)) % 60) / 60;
			int nSecond = (nSecondTime - 60 * 60 * nHour * nMinute) % 60;

			if (10 > nHour) {
				sTimeString += "0" + String.valueOf(nHour);
			} else {
				sTimeString += String.valueOf(nHour);
			}
			sTimeString += ":";
			if (10 > nMinute) {
				sTimeString += "0" + String.valueOf(nMinute);
			} else {
				sTimeString += String.valueOf(nMinute);
			}
			sTimeString += ":";
			if (10 > nSecond) {
				sTimeString += "0" + String.valueOf(nSecond);
			} else {
				sTimeString += String.valueOf(nSecond);
			}
		} else if (1 <= nSecondTime / 60) {
			// ����
			int nMinute = nSecondTime / 60;
			int nSecond = (nSecondTime - 60 * nMinute) % 60;

			// sTimeString += "00:";
			if (10 > nMinute) {
				sTimeString += "0" + String.valueOf(nMinute);
			} else {
				sTimeString += String.valueOf(nMinute);
			}
			sTimeString += ":";
			if (10 > nSecond) {
				sTimeString += "0" + String.valueOf(nSecond);
			} else {
				sTimeString += String.valueOf(nSecond);
			}
		} else {
			// ��
			sTimeString += "00:";

			if (10 > nSecondTime) {
				sTimeString += "0" + String.valueOf(nSecondTime);
			} else {
				sTimeString += String.valueOf(nSecondTime);
			}
		}

		return sTimeString;
	}

	/*
	 * SeekBar���ȸı��¼�
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser == true) {
				mediaPlayer.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isChanging = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mediaPlayer.seekTo(seekBar.getProgress());
			isChanging = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (mTimer != null) {

			mTimer.cancel(); // �����˳�ʱcancel timer
			mTimer.purge();
			// mTimer=null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
		m_Run = false;
		System.out.println("����ֹͣ����+++++onstop()");
		super.onStop();
	}

	// ��ͣ
	@Override
	protected void onPause() {
		// ������Activity���򿪣�ֹͣ����
		if (mediaPlayer.isPlaying()) {
			position = mediaPlayer.getCurrentPosition();// �õ�����λ��
			mediaPlayer.stop();
			System.out.println("������ͣ����+++++onpause()");
		}
		m_Run = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			System.out.println("���ٵ�ǰActivity------ondestroy()!");
		}
		else if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		} else if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
			// mTimer=null;
			// mTimer.cancel(); // �����˳�ʱcancel timer
//		m_Run = false;
		hashMapImage_Added.clear();
		VideoFileList_Added.clear();
		VideoFileList_Public.clear();
		
		super.onDestroy();	
	}

	/**
	 * @param event
	 * @return
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {

		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			if (View.VISIBLE == llPlayercontrolLayout.getVisibility()) {
				llPlayercontrolLayout.setVisibility(View.GONE);
				
				llPlayerlistLayout.setVisibility(View.INVISIBLE);

				llPlayerTitle.setVisibility(View.GONE);
			} else {
				llPlayercontrolLayout.setVisibility(View.VISIBLE);
				// llPlayerlistLayout.setVisibility(View.VISIBLE);
				// navigationBar.setbVisible(true);
				llPlayerTitle.setVisibility(View.VISIBLE);
			}
			// CustomToast.showToast(PlayerActivity.this, "", 200);

			break;

		}
		return surfaceView.onTouchEvent(event);
	}

	/*
	 * private Handler handler = new Handler() {
	 * 
	 * @SuppressLint("HandlerLeak")
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * 
	 * switch (msg.what) { case MESSAGE_PROCESS: if (isChanging == true) {
	 * return; }
	 * 
	 * int nCurrentPosition = mediaPlayer.getCurrentPosition(); if (60 * 60 <
	 * nCurrentPosition / 1000) { return; }
	 * skb_video.setProgress(nCurrentPosition);
	 * tvElapsed.setText(getTimeStringByMilliseconds(nCurrentPosition));
	 * 
	 * break; default: break; } } };
	 */

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTrackballEvent(ev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println("��ʼ����+++++onstart()");
		super.onStart();
		m_Run = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("��������+++++onRestart()");
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		System.out.println("����onResume����+++++++++");
		try {
//			dataInit();
			abserfaceLay.removeView(surfaceView); // �����ӽ�����ʾ��

			if (preferences.getBoolean("fullscreen", false) == true) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						dm.widthPixels, dm.heightPixels, 0);
				abserfaceLay.addView(surfaceView, lp);
			} else {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						preferences.getInt("textwidth", 200),
						preferences.getInt("textheigh", 200),
						preferences.getInt("textltx", 0));
				abserfaceLay.addView(surfaceView, lp);
			}

//			this.surfaceView.getHolder().setType(
//					SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			this.surfaceView.getHolder().setFixedSize(mPlayWidth, mPlayHeight);// ���÷ֱ���
//			// this.surfaceView.getHolder().setFixedSize(dm.widthPixels,
//			// dm.heightPixels);
			this.surfaceView.getHolder().setKeepScreenOn(true);
//			// this.surfaceView.getHolder().addCallback(new SurfaceListener());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ����༭ʱ����Ҫ�Բ����б���в���
		// mShowAll = (DragListView2) findViewById(R.id.del_win_show_all);

		System.out.println("�༭�����б���ק��" + mDragEditlist);
		// �༭������
		mSelectAdapter = new DragListAdapter2(this, data1, videoFileList);
		System.out.println("�༭�����б���������" + mSelectAdapter);
		System.out.println(data1);
		mDragEditlist.setAdapter(mSelectAdapter);
		mSelectAdapter.notifyDataSetChanged();

		// ��������
		// �����б��������Ŀ����¼�
		playlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					dataInit();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				filename = data.get(position).getFilePath();
				File videoFile = new File(filename);
				if (videoFile.exists()) {

					// playStopButton.setImageResource(R.drawable.pause);
					// playStart.setVisibility(View.INVISIBLE);
					// mediaPlayer.setOnPreparedListener(preparedListener);

					 if (mediaPlayer!=null) {
						 mediaPlayer.stop();
						 mediaPlayer.getCurrentPosition();
//						 mediaPlayer.release();
						 
						 
							// mediaPlayer = new MediaPlayer();
							
							surfaceView.setVisibility(View.VISIBLE);
										
							mediaPlayer.reset();
							
//							final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50);
//							 tg.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
//							 tg.release();

							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							// ������Ƶ��ʾλ��
							
		                   
							mediaPlayer.setDisplay(mSurHolder);

							try {
								mediaPlayer.setDataSource(filename);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mediaPlayer.prepareAsync();

							System.out.println("׼���첽����ģʽ:" + filename);
							// ׼�����ŵļ�����
							mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

								@Override
								public void onPrepared(MediaPlayer mp) {
															  
									int nDuration = mediaPlayer.getDuration();
									skb_video.setMax(nDuration);
									tvElapsed.setText("00:00");
									tvTotal.setText(getTimeStringByMilliseconds(nDuration));
									skb_video.invalidate();
									mediaPlayer.start();
									playStart.setImageResource(R.drawable.pause);
									mediaPlayer.setLooping(true);
									 onResumePlay();
									
								}
							}); 
					 }


					//
				} else {
					Toast.makeText(
							getApplicationContext(),
							getString(R.string.text_hit_title)
									+ data.get(position).getFileName(),
							Toast.LENGTH_SHORT).show();

				}

				//

			}
		});
		// �����б��ϵĳ����¼�
		playlistView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShowEditPlayListActivity(position);
				return false;
			}
		});

		super.onResume();
	}


}
