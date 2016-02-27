/*
 * 文件名 PlayerActivity.java
 * 包含类名列表com.szaoto.ak10.player
 * 版本信息，版本号
 * 创建日期2013年11月8日上午11:54:07
 * 版权声明 liangdb-szaoto
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
 * 类名PlayerActivity
 * 作者 liangdb
 * 主要功能 播放器主窗口
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 * 修改者：zhangsj
 * 修改日期：2014年5月9日
 * 修改内容：合并播放列表到播放界面
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
	private String filename; // 当前播放文件的名称
	private int position; // 记录播放位置
	// private int mCurrentPosition;
	private int mPlayWidth; // 播放窗口宽度
	private int mPlayHeight; // 播放窗口高度

	private boolean isChanging = false;// 互斥变量，防止定时器与SeekBar拖动时进度冲突
	private LinearLayout llPlayercontrolLayout; // 播放器控制
	private LinearLayout llPlayerlistLayout; // 播放列表
	private RelativeLayout llPlayerTitle;// 播放列表导航
	// private DragListView2 mShowAll; //编辑界面可拖拽
	private DragListView2 mDragEditlist; // 编辑界面可拖拽
	// private EditListAdapter mSelectAdapter; //编辑界面适配器
	private DragListAdapter2 mSelectAdapter;

	// 添加和编辑播放列表按钮
	private Button btn_AddProgram;
	private Button btn_EditProgram;

	Timer mTimer = null;
	TimerTask mTimerTask = null;

	// 用户偏好设置
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
	// 在一个主界面playerActivity上连接不同的子模块
	private TextView playHome;
	private TextView playSeting;
	private TextView playBack;
	private SurfaceHolder mSurHolder;
	private ImageButton playStart;
	private ImageButton playPause;


	
	/**********************************************************/
	/**
	 * 公用的 排列顺序后的列表集合
	 */
	private static ArrayList<VideoFile> VideoFileList_Public = new ArrayList<VideoFile>();

	/**
	 * 公用的 已选中的 列表集合 --（添加视频文件）
	 */
	public static ArrayList<VideoFile> VideoFileList_Added = new ArrayList<VideoFile>();

	/**
	 * 公用的 已加载到 内存的 图片（方便读取，不用再次通过文件路径读取图片）
	 */
	public static HashMap<String, Bitmap> hashMapImage_Added = new HashMap<String, Bitmap>();

	/**
	 * 公用 默认 加载 的图片
	 */
	public static Bitmap bitmap;

	/**
	 * 公用 默认图片 的 宽度，高度
	 */
	public static int imageDefaultWidth, imageDefaultHeight;
	/**
	 * 公用 默认图片 的 宽度，高度
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
		System.out.println("开始启动Activity+++++oncreate()方法");
		setContentView(R.layout.player);
//<<<<<<< .mine
//	    preferences = getSharedPreferences("11", MODE_PRIVATE);
//=======
		SerialPortControlBroadCast.SetCurrentContext(this);
		mTimer = new Timer();
//		navigationBar = new NavigationBar("player_PlayerActivity", this);
		preferences = getSharedPreferences("11", MODE_PRIVATE);

		// 获得修改器
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
		// 设置显示区域大小
		abserfaceLay = (LinearLayout) findViewById(R.id.abserfaceLay);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				dm.widthPixels, dm.heightPixels, 0);
		abserfaceLay.addView(surfaceView, lp);
		 // 设置音频流类型
		
		/* 下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前 */
//		this.surfaceView.getHolder().setType(
//				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//		this.surfaceView.getHolder().setFixedSize(mPlayWidth, mPlayHeight);// 设置分辨率
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

		// 播放控制
		llPlayerTitle = (RelativeLayout) findViewById(R.id.Layout_PlayerTitle);
		llPlayerTitle.bringToFront();
		// 播放控制
		llPlayercontrolLayout = (LinearLayout) findViewById(R.id.Layout_PlayerControl);
		llPlayercontrolLayout.bringToFront();
		// llPlayercontrolLayout.setAlpha(90);

		// 播放列表
		llPlayerlistLayout = (LinearLayout) findViewById(R.id.Layout_PlayerList);

		llPlayerlistLayout.bringToFront();

		llPlayerlistLayout.setVisibility(View.GONE);
		mDragEditlist = (DragListView2) findViewById(R.id.del_win_show_all);
		// llPlayerlistLayout.setAlpha(90);

		m_Run = true;

				
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == 0) {
		 Log.i("onActivity", "返回Activitiy数据");
		}
		else {
			
//			onResume();
			// dm.widthPixels, dm.heightPixels
//			abserfaceLay.removeView(surfaceView); // 播放子界面显示区
			if (data.getExtras().getBoolean("fullscreen") == true) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						dm.widthPixels, dm.heightPixels, 0);
				abserfaceLay.addView(surfaceView, lp);
			} else {
				mPlayWidth = data.getExtras().getInt("textwidth");
				mPlayHeight = data.getExtras().getInt("textheigh");
				// 设置显示区域大小

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						mPlayWidth, mPlayHeight, data.getExtras().getInt(
								"textltx"));
				abserfaceLay.addView(surfaceView, lp);
			}
			
			 Log.i("onActivity", "返回数据");
		}
		System.out.println("调用播放器Activity------onActivityResult()方法");
	}

	// 长按时，需要显示的信息
	private void ShowEditPlayListActivity(int Position) {
		// finish();
		VideoFile videoFile = data.get(Position); // 每个位置，对应的data。

		Intent intent = new Intent();

		intent.putExtra("videoFile", videoFile); // 传递实体对象

		intent.setClass(PlayerActivity.this, EditPlaylist.class);
		startActivity(intent);
	}

	// 自定义比较器
	class MyComparator implements Comparator<File> {
		@Override
		public int compare(File lhs, File rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}

	}

	// TODO --Class-- MyAdapater 自定义适配器
	// 按钮点击事件
	View.OnClickListener clickHandler = new View.OnClickListener() {
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				// 添加节目按钮
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

					Log.i("点击-编辑按钮", "进入编辑界面");
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

	// 数据初始化
	public void dataInit() throws Exception {
		CONFIG_PATH = this.getFilesDir() + "//config//";
		File xmlFile = new File(CONFIG_PATH, "videofilelist.xml");
		System.out.println("xml文件存放的路径是：" + CONFIG_PATH);
		System.out.println("需要解析的xml文件是：" + xmlFile);

		if (!xmlFile.exists()) {
			// 如果不存在xml文件，或者目录，文件被删除，那就复制一份文件到目录
			XmlTool.CopyXmlFile(this);
			xmlFile = new File(Environment.getDataDirectory(),
					"/data/com.szaoto.ak10/files/config/videofilelist.xml");
			System.out.println("如果没有xml文件，那么重新复制文件：" + xmlFile);
		}
		/*
		 * 解析数据步骤 1、创建解析工厂 2、创建解析器 3、设置数据源 4、开始解析
		 */
		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance()
				.newPullParser();
		xmlPullParser.setInput(new FileInputStream(xmlFile), "utf-8");
		data = VideoListManager.getObjectList2(xmlPullParser);
//		filename = data.get(position).getFilePath();
		System.out.println("开始解析数据：" + data);
		playlistView = (ListView) findViewById(R.id.list);
		playAdapter = new DragListAdapter(this, data);
		playlistView.setAdapter(playAdapter);
		playAdapter.notifyDataSetChanged();
	}

	// 播放器模块的按钮点击监听器
	private class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.btn_Play:// 来自播放按钮
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
//				case R.id.btn_pause:// 来自播放按钮
//					mediaPlayer.pause();
//					break;
				case R.id.btn_Stop:// 来自播放按钮
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

//	// 播放视频区域监听事件
//	private class SurfaceListener implements SurfaceHolder.Callback {
//
//		@Override
//		// 画面修改
//		public void surfaceChanged(SurfaceHolder holder, int format, int width,
//				int height) {
//			Log.i("TAG", "surfaceChanged");
//		}
//
//		@Override
//		// 画面创建
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
//			// 方法在onResume()后被调用
////			 try {
////				
////			 if ( mediaPlayer !=null) {
//////						 mediaPlayer = new MediaPlayer();
////			 // 设置音频流类型
////			 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////			 // 设置视频显示位置
////			 mediaPlayer.setDisplay(mSurHolder);
////			 // 设置播放的资源路径
////			 mediaPlayer.setDataSource(filename);
////			 // 准备播放
////			 // mMediaPlayer.prepare();
////			 mediaPlayer.prepareAsync();
////			
////			 // 准备播放的监听器
////			 mediaPlayer
////			 .setOnPreparedListener(new OnPreparedListener() {
////			
////			 @Override
////			 public void onPrepared(MediaPlayer mp) {
////			 // 开始播放
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
////			 // 设置播放按钮不能点击
////			 playStopButton.setEnabled(false);
////			 // 播放完成的状态
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
//		// 画面销毁
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			// Log.i("TAG", "surfaceDestroyed");
//			Log.e("mediaPlayer", "surface destroyed");
//			// 在SurfaceView被销毁之前保存当前的视频播放位置
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
							System.out.println("改变进度条时间显示方法+++++"+nCurrentPosition+"ms");
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
	 * 播放视频
	 */
	public void play() throws IOException {
		try {

			mediaPlayer.reset();
			mediaPlayer.getCurrentPosition();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// 设置视频显示位置

			mediaPlayer.setDisplay(mSurHolder);
			// 设置播放的资源路径
			mediaPlayer.setDataSource(filename);
			System.out.println("设置播放视频源:"+filename);
			// 准备播放
			// mMediaPlayer.prepare();
			mediaPlayer.prepareAsync();
			System.out.println("准备异步播放模式:"+filename);
			// 准备播放的监听器
			mediaPlayer.setOnPreparedListener(preparedListener);

			// 设置播放按钮不能点击
			playStart.setEnabled(false);
			// 播放完成的状态
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					playStart.setEnabled(true);
					mediaPlayer.release();
//					skb_video.setProgress(0);
//					skb_video.invalidate();
					System.out.println("播放完成:"+filename);
				}
			});

			// 播放的过程中出现问题 设置播放按钮能点击
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					playStopButton.setEnabled(false);
					System.out.println("播放错误--------"+what);
					return false;
				}
			});
			mediaPlayer.setOnInfoListener(new OnInfoListener() {
				
				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					System.out.println("播放信息--------"+what);
					System.out.println("播放信息--------"+extra);
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
			System.out.println("开始播放视频:"+filename);
			playStart.setEnabled(true);
			
			onResumePlay();

		}
	};

	// 获取视频节目时长
	private String getTimeStringByMilliseconds(int nDuration) {
		String sTimeString = "";
		int nSecondTime = nDuration / 1000;
		if (1 <= nSecondTime / (60 * 60)) {
			// 小时
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
			// 分钟
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
			// 秒
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
	 * SeekBar进度改变事件
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

			mTimer.cancel(); // 程序退出时cancel timer
			mTimer.purge();
			// mTimer=null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
		m_Run = false;
		System.out.println("调用停止方法+++++onstop()");
		super.onStop();
	}

	// 暂停
	@Override
	protected void onPause() {
		// 当其他Activity被打开，停止播放
		if (mediaPlayer.isPlaying()) {
			position = mediaPlayer.getCurrentPosition();// 得到播放位置
			mediaPlayer.stop();
			System.out.println("调用暂停方法+++++onpause()");
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
			System.out.println("销毁当前Activity------ondestroy()!");
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
			// mTimer.cancel(); // 程序退出时cancel timer
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
		System.out.println("开始启动+++++onstart()");
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
		System.out.println("重新启动+++++onRestart()");
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		System.out.println("调用onResume方法+++++++++");
		try {
//			dataInit();
			abserfaceLay.removeView(surfaceView); // 播放子界面显示区

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
			this.surfaceView.getHolder().setFixedSize(mPlayWidth, mPlayHeight);// 设置分辨率
//			// this.surfaceView.getHolder().setFixedSize(dm.widthPixels,
//			// dm.heightPixels);
			this.surfaceView.getHolder().setKeepScreenOn(true);
//			// this.surfaceView.getHolder().addCallback(new SurfaceListener());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 点击编辑时，需要对播放列表进行操作
		// mShowAll = (DragListView2) findViewById(R.id.del_win_show_all);

		System.out.println("编辑播放列表拖拽：" + mDragEditlist);
		// 编辑适配器
		mSelectAdapter = new DragListAdapter2(this, data1, videoFileList);
		System.out.println("编辑播放列表适配器：" + mSelectAdapter);
		System.out.println(data1);
		mDragEditlist.setAdapter(mSelectAdapter);
		mSelectAdapter.notifyDataSetChanged();

		// 更新数据
		// 播放列表上面的条目点击事件
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
							// 设置视频显示位置
							
		                   
							mediaPlayer.setDisplay(mSurHolder);

							try {
								mediaPlayer.setDataSource(filename);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mediaPlayer.prepareAsync();

							System.out.println("准备异步播放模式:" + filename);
							// 准备播放的监听器
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
		// 播放列表上的长按事件
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
