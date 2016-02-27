/*
 * �ļ��� TestActivity.java
 * ���������б�com.szaoto.ak10.test
 * �汾��Ϣ���汾��
 * ��������2013��11��8������11:54:49
 * ��Ȩ���� liangdb-szaoto
 */
package com.szaoto.ak10.test;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.PannelButtonDownService;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.PannelLedControlBroadCast;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.custom.TravelView;
import com.szaoto.ak10.custom.TravelView.OnTravelListener;

/*
 * ����TestActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
@SuppressWarnings("deprecation")
public class TestActivity extends Activity implements IInfoChangeObserver {
	private static TestActivity mTestActivity = null;
	private int nCurrentMode = 0;
	private int nGridColorCnt = 1;
	

	private SurfaceView m_Diplay;
	private SurfaceHolder sfh;
	private int m_DisplayX, m_DisplayY;
	private int m_DisplayWidth, m_DisplayHeight;

	private int m_DrawIndex; //
	private GrayTest m_GrayTest = new GrayTest();
	private RibbonTest m_RibbonTest = new RibbonTest();
	private GridTest m_GridTest = new GridTest();
	private SpotsTest m_SpotsTest = new SpotsTest();

	private Timer m_Timer = null;
	private TimerTask m_TimerTask;
	private SharedPreferences preferences;
	AbsoluteLayout abslayoutsurfaceview;
	DisplayMetrics dm;
	int configparmtextltx;
	int configparmtextlty;
	int configparmtextwidth;
	int configparmtextheigh;
	int configparmtestmodeset;
	int configparmintervel;
	// int configparmautoset;
	int testgraylevel;
	int testrobbinwandh;
	int testrobbinstartgray;

	private TextView testHome;
	private TextView testSeting;
	private TextView testBack;

	final Handler handlerDraw = new Handler();
	Runnable runnable;

	private TravelView m_travelTravelView;
	private static int lcIndex = 0;//����任����ɫ���

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -1) {
			return;
		}

		abslayoutsurfaceview.removeView(m_Diplay);

		if (configparmtextheigh <= 80) {
			configparmtextheigh = 80;
		}
		if (data.getExtras().getBoolean("checkfullscreen") == true) {
			m_DisplayWidth = dm.widthPixels;
			m_DisplayHeight = dm.heightPixels;
			m_DisplayX = 0;
			m_DisplayY = 0;
			AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(
					m_DisplayWidth, m_DisplayHeight - 160, m_DisplayX,
					m_DisplayY);
			abslayoutsurfaceview.addView(m_Diplay, lp);
		} else {
			m_DisplayWidth = preferences
					.getInt("textwidthtest", dm.widthPixels);

			if (m_DisplayWidth > 1280) {
				m_DisplayWidth = 1280;
			}

			m_DisplayHeight = preferences.getInt("textheightest",
					dm.heightPixels);

			if (m_DisplayHeight > 800) {
				m_DisplayHeight = 800;
			}
			AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
					m_DisplayWidth, m_DisplayHeight - 92, m_DisplayX,
					m_DisplayY);
			abslayoutsurfaceview.addView(m_Diplay, params);
		}

		if (configparmtextheigh <= 80) {
			configparmtextheigh = 80;
		}

		configparmtestmodeset = data.getExtras().getInt("testmodeset");
		configparmintervel = data.getExtras().getInt("intervel");

		if (configparmtestmodeset != 0) {
			handlerDraw.postDelayed(runnable, configparmintervel);
		} else {
			handlerDraw.removeCallbacks(runnable);
		}
		//
		// configparmautoset = data.getExtras().getInt("autoset");
		testgraylevel = data.getExtras().getInt("testgraylevel");
		testrobbinwandh = data.getExtras().getInt("testrobbinwandh");
		testrobbinstartgray = data.getExtras().getInt("testrobbinstartgray");
		m_RibbonTest.m_nStartGray = testrobbinstartgray;
		m_RibbonTest.m_nWidthHeight = testrobbinwandh;
		m_GridTest.m_bHorizontalLine = data.getExtras().getBoolean("checklinetype_hor");
		m_GridTest.m_bVerticalLine = data.getExtras().getBoolean("checklinetype_ver");
		m_GridTest.m_bLeftDiagonalLine = data.getExtras().getBoolean("checklinetype_LeftDiagonalLine");
		m_GridTest.m_bRightDiagonalLine = data.getExtras().getBoolean("checklinetype_RightDiagonalLine");
		m_GridTest.m_nSpace = data.getExtras().getInt("testgridspacing");

		if (0 == data.getExtras().getInt("spinnershoutypeindex")) {
			m_SpotsTest.m_nStyle = 2;
		} else if (1 == data.getExtras().getInt("spinnershoutypeindex")) {
			m_SpotsTest.m_nStyle = 1;
		}
		m_SpotsTest.m_nSpace = data.getExtras().getInt("testspotspacing");

	}

	void LoadData() {
		testgraylevel = preferences.getInt("testgraylevel", 200);
		m_RibbonTest.m_nStartGray = preferences.getInt("testrobbinstartgray",200);
		m_RibbonTest.m_nWidthHeight = preferences.getInt("testrobbinwandh", 200);
		m_GridTest.m_bHorizontalLine = preferences.getBoolean("checklinetype_hor", true);
		m_GridTest.m_bVerticalLine = preferences.getBoolean("checklinetype_ver", true);
		m_GridTest.m_bLeftDiagonalLine = preferences.getBoolean("checklinetype_LeftDiagonalLine", true);
		m_GridTest.m_bRightDiagonalLine = preferences.getBoolean("checklinetype_RightDiagonalLine", true);
		m_GridTest.m_nSpace = preferences.getInt("testgridspacing", 50);
		m_SpotsTest.m_nSpace = preferences.getInt("testspotspacing", 50);

		configparmtestmodeset = preferences.getInt("testmodeset", 0);
		configparmintervel = preferences.getInt("intervel", 100);
	}

	void drawfromindex(int index) {
		switch (index) {
		case 0: {
			m_GrayTest.m_nColorType = 0;
			m_GrayTest.m_nColor = Color.rgb(testgraylevel, 0, 0);
			Draw(1);
		}
			break;
		case 1: {
			m_GrayTest.m_nColorType = 1;
			m_GrayTest.m_nColor = Color.rgb(0, testgraylevel, 0);
			Draw(1);
		}
			break;
		case 2: {
			m_GrayTest.m_nColorType = 2;
			m_GrayTest.m_nColor = Color.rgb(0, 0, testgraylevel);
			Draw(1);
		}
			break;
		case 3: {
			m_GrayTest.m_nColorType = 3;
			m_GrayTest.m_nColor = Color.WHITE;
			m_GrayTest.m_nColor = Color.rgb(testgraylevel, testgraylevel,
					testgraylevel);
			Draw(1);
		}
			break;
		case 4: {
			m_GrayTest.m_nColorType = 3;
			m_GrayTest.m_nColor = Color.BLACK;
			Draw(1);
		}
			break;
		case 5: {
			m_RibbonTest.m_nColorType = 0;
			m_RibbonTest.m_nHorV = 2;
			Draw(2);
		}
			break;
		case 6: {
			m_RibbonTest.m_nColorType = 1;
			m_RibbonTest.m_nHorV = 2;
			Draw(2);
		}
			break;
		case 7: {
			m_RibbonTest.m_nColorType = 2;
			m_RibbonTest.m_nHorV = 2;
			Draw(2);
		}
			break;
		case 8: {
			m_RibbonTest.m_nColorType = 3;
			m_RibbonTest.m_nHorV = 2;
			Draw(2);
		}
			break;
		case 9: {
			m_RibbonTest.m_nColorType = 0;
			m_RibbonTest.m_nHorV = 1;
			Draw(2);
		}
			break;
		case 10: {
			m_RibbonTest.m_nColorType = 1;
			m_RibbonTest.m_nHorV = 1;
			Draw(2);
		}
			break;
		case 11: {
			m_RibbonTest.m_nColorType = 2;
			m_RibbonTest.m_nHorV = 1;
			Draw(2);
		}
			break;
		case 12: {
			m_RibbonTest.m_nColorType = 3;
			m_RibbonTest.m_nHorV = 1;
			Draw(2);
		}
			break;
		case 13: {
			Draw(3);
		}
			break;
		case 14: {
			Draw(4);
		}
			break;
		default:
			break;
		}
		m_DrawIndex = index;
	}

	public static TestActivity getInstance() {
		if (mTestActivity != null) {
			return mTestActivity;
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		SerialPortControlBroadCast.SetCurrentContext(this);
		initView();//Home,Setting,Back
		mTestActivity = this;
		SerialPortControlBroadCast.SetCurrentContext(this);
		PannelButtonDownService.observers.add(this);
		preferences = getSharedPreferences("11", MODE_WORLD_READABLE);
		// ����޸���
		String colchangetype = preferences.getString("colchangetype", "singlecolor");
		if(colchangetype.equals("singlecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(1);
		}
		else if(colchangetype.equals("doublecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(2);
		}
		else if(colchangetype.equals("triplecolor"))
		{
			TestActivity.getInstance().SetGridColorCnt(3);
		}
		getWindow().setBackgroundDrawable(null);//�����ڱ�������Ϊ�գ������Ͳ��ử���ڱ����������Ч��
		getWindow().setFormat(PixelFormat.RGBX_8888);
		
		LoadData();//��ȡ������Ϣ
		m_DrawIndex = 1;
		m_Diplay = new SurfaceView(this);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		m_travelTravelView = (TravelView) findViewById(R.id.travelView_Test);
		m_travelTravelView.setCurrentIndex(0);

		abslayoutsurfaceview = (AbsoluteLayout) findViewById(R.id.absLayout_surfaceview);
		m_travelTravelView.initView(R.drawable.bg, R.drawable.cover,
				new int[] { R.drawable.red, R.drawable.green, R.drawable.blue,
						R.drawable.white, R.drawable.black,
						R.drawable.gradual_h_b2r, R.drawable.gradual_h_b2g,
						R.drawable.gradual_h_b2b, R.drawable.gradual_h_b2w,
						R.drawable.gradual_v_b2r, R.drawable.gradual_v_b2g,
						R.drawable.gradual_v_b2b, R.drawable.gradual_v_b2w,
						R.drawable.gridding, R.drawable.spots }, new String[] {
						"", "", "", "", "", "", "", "", "", "", "", "", "", "",
						"" }, new boolean[] { true, true, true, true, true,
						true, true, true, true, true, true, true, true, true,
						true });

		m_travelTravelView.setOnTravelListener(new OnTravelListener() {
			public void onTravel(final int index) {
				runOnUiThread(new Runnable() {
					public void run() {
						drawfromindex(index);
					}
				});

			}
		});
		m_travelTravelView.refreshDrawableState();
		runnable = new Runnable() {
			@Override
			public void run() {
				if (configparmtestmodeset != 0) {
					switch (m_DrawIndex) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4: {
						m_GrayTest.GetCurColor();
						Draw(1);
					}
						break;
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12: {
						m_RibbonTest.m_nSPos += 2;

						if (m_RibbonTest.m_nSPos >= m_RibbonTest.m_nWidthHeight) {
							m_RibbonTest.m_nSPos = 0;
						}

						Draw(2);
					}
						break;
					case 13: {
						if (m_GridTest.m_nSPos > m_GridTest.m_nSpace) {
							m_GridTest.m_nSPos = 0;
						}
						m_GridTest.m_nSPos++;

						Draw(3);
					}

						break;

					default:
						break;
					}
				}
				handlerDraw.postDelayed(this, configparmintervel); // �ӳ�1����ٴ�ִ��
			}
		};

		if (m_DisplayHeight <= 80) {
			m_DisplayHeight = 80;
		}

		if (preferences.getBoolean("checkfullscreen", false) == true) {
			m_DisplayWidth = dm.widthPixels; // - 95;
			m_DisplayHeight = dm.heightPixels;
			m_DisplayX = 0;
			m_DisplayY = 0;
			AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(
					m_DisplayWidth, m_DisplayHeight, m_DisplayX, m_DisplayY);

			abslayoutsurfaceview.addView(m_Diplay, lp);
		} else {
			m_DisplayWidth = preferences
					.getInt("textwidthtest", dm.widthPixels);

			if (m_DisplayWidth > 1280) {
				m_DisplayWidth = 1280;
			}

			m_DisplayHeight = preferences.getInt("textheightest",
					dm.heightPixels);

			if (m_DisplayHeight > 800) {
				m_DisplayHeight = 800;
			}

			m_DisplayX = preferences.getInt("textltxtest", 0);
			m_DisplayY = preferences.getInt("textltytest", 0);
			AbsoluteLayout.LayoutParams params = // zhangjjadd ��Ҫ����
			new AbsoluteLayout.LayoutParams(m_DisplayWidth, m_DisplayHeight,
					m_DisplayX, m_DisplayY);
			abslayoutsurfaceview.addView(m_Diplay, params);
		}

		sfh = m_Diplay.getHolder();
		sfh.addCallback(new MyCallBack());// �Զ�����surfaceCreated�Լ�surfaceChanged

		m_DrawIndex = 0;

		if (configparmtestmodeset != 0) {
			handlerDraw.postDelayed(runnable, 100); // �ӳ�1����ٴ�ִ��
		}

	}

	private void initView() {
		testHome = (TextView) findViewById(R.id.text_testback);
		testSeting = (TextView) this.findViewById(R.id.text_testsetting);
		testBack = (TextView) this.findViewById(R.id.text_testmain);
		testHome.setOnClickListener(clickHandler);
		testSeting.setOnClickListener(clickHandler);
		testBack.setOnClickListener(clickHandler);
	}

	View.OnClickListener clickHandler = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_testmain:
				startActivity(new Intent(TestActivity.this,
						HomePageActivity.class));
				handlerDraw.removeCallbacks(runnable);
				break;
			case R.id.text_testback:
				handlerDraw.removeCallbacks(runnable);
				finish();
				break;
			case R.id.text_testsetting:
				handlerDraw.removeCallbacks(runnable);
				startActivityForResult(new Intent(TestActivity.this,
						TestConfigActivity.class), 0);
				break;

			default:
				break;
			}
		}
	};

	// /////////////////////////////////////////////////////////////////////////////////
	// ���ò���ģʽ����ť��
	// nClock 0����ʼ 1��˳ʱ�� 2����ʱ��
	public int SetTestMode(int nClock) {
		switch (nClock) {
		case 0:
			nCurrentMode = 0;
			break;
		case 1:
			if (14 > nCurrentMode) {
				nCurrentMode++;
			} else {
				return nCurrentMode;
			}
			break;
		case 2:
			if (0 < nCurrentMode) {
				nCurrentMode--;
			} else {
				return nCurrentMode;
			}
			break;
		default:
			break;
		}
		drawfromindex(nCurrentMode);

		return nCurrentMode;
	}
	
	public void SetGridColorCnt(int nCnt) {		
		nGridColorCnt = nCnt;
	}

	// /////////////////////////////////////////////////////////////////////////////////

	class MyCallBack implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			m_GrayTest.m_nColorType = 0;
			m_GrayTest.m_nColor = Color.rgb(testgraylevel, 0, 0);

			// �ϴ�����index
			int nIndex = m_travelTravelView.getCurrentIndex();
			drawfromindex(nIndex);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}
	}

	private void Draw(int nIndex) {
		// ClearDraw();
		switch (nIndex) {
		case 1:
			DrawGray();
			break;
		case 2:
			DrawRibbon();
			break;
		case 3:
			DrawGrid();
			break;
		case 4:
			DrawSpots();
			break;
		default:
			break;
		}
		CustomToast.showToast(TestActivity.this, "", 500);
	}

	private void DrawGray() {
		Canvas canvas = sfh.lockCanvas(new Rect(m_DisplayX, m_DisplayY,
				m_DisplayWidth, m_DisplayHeight));

		if (canvas == null) {
			return;
		}

		Paint p = new Paint();
		//p.setDither(true);
		p.setColor(m_GrayTest.m_nColor);
		p.setStyle(Paint.Style.FILL);
		canvas.drawRect(m_DisplayX, m_DisplayY, m_DisplayWidth,
				m_DisplayHeight - 160, p); // ������
		// ������Ļ��ʾ����
		sfh.unlockCanvasAndPost(canvas);
	}

	private void DrawRibbon() {
		Canvas canvas = sfh.lockCanvas(new Rect(m_DisplayX, m_DisplayY,
				m_DisplayWidth, m_DisplayHeight - 80));

		if (canvas == null) {
			return;
		}

		m_RibbonTest.ResetCurColor();

		int spos = m_RibbonTest.m_nSPos - m_RibbonTest.m_nWidthHeight;
		if (1 == m_RibbonTest.m_nHorV) {
			int nColorBegin = 0;
			int nColorMid1 = 0;
			int nColorMid2 = 0;
			int nColorEnd = 0;
			// ����
			for (int i = m_DisplayY + spos; i < m_DisplayHeight + m_DisplayY; i++) {
				m_RibbonTest.GetCurColor();
				Paint p = new Paint();
				//p.setDither(true);
				p.setColor(m_RibbonTest.m_nColor);
				//int[] fadeColors = {0x00000000, 0xFF000000, 0xFF000000, 0x00000000};
				//p.setShader(new LinearGradient(0, 0, 0, i, fadeColors, null,LinearGradient.TileMode.CLAMP));
				//p.setXfermode(new AvoidXfermode(0xFF307070, 255, AvoidXfermode.Mode.TARGET));
				float[] pts = { m_DisplayX, i, m_DisplayWidth + m_DisplayX, i };
				canvas.drawLines(pts, p);
				
				if (i== m_DisplayX + spos) {
					nColorBegin = m_RibbonTest.m_nColor;					
				}
				else if (i== (m_DisplayHeight + spos) / 4) {
					nColorMid1 = m_RibbonTest.m_nColor;
				}				
				else if (i== 3 * (m_DisplayHeight + spos) / 4) {
					nColorMid2 = m_RibbonTest.m_nColor;
				}
				else if (i== m_DisplayWidth + m_DisplayX - 1) {
					nColorEnd = m_RibbonTest.m_nColor;
				}
			}
			
			/*
			{
				Paint mPaint = null;
				mPaint = new Paint();

				// ���Խ�����Ⱦ
			    Shader mLinearGradient1 = null;
			    // ���Խ�����Ⱦ
			    Shader mLinearGradient2 = null;
			    // ���Խ�����Ⱦ
			    Shader mLinearGradient3 = null;

			    // ����LinearGradient�����ý�����ɫ����
		        // ��һ��,�ڶ���������ʾ������� ������������յ��ڶԽǵ�����λ��
		        // ������,���ĸ�������ʾ�����յ�
		        // �����������ʾ������ɫ
		        // ��������������Ϊ��,��ʾ����,ֵΪ0-1 new float[] {0.25f, 0.5f, 0.75f, 1 }
		        // ������ǿյģ���ɫ���ȷֲ������ݶ��ߡ�
		        // ���߸���ʾƽ�̷�ʽ
		        // CLAMP�ظ����һ����ɫ�����
		        // MIRROR�ظ���ɫ��ͼ��ˮƽ��ֱ�����Ѿ���ʽ�����з�תЧ��
		        // REPEAT�ظ���ɫ��ͼ��ˮƽ��ֱ����
			    
			    //mLinearGradient1 = new LinearGradient(0, 0, 0, 100, new int[] {
		        //        Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		        //        Shader.TileMode.CLAMP);		               		    

			   
		        //mLinearGradient2 = new LinearGradient(0, 0, 0, 100, new int[] {
		        //        Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		        //        Shader.TileMode.MIRROR);
		        //mLinearGradient3 = new LinearGradient(0, 0, 0, 100, new int[] {
		        //        Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		        //        Shader.TileMode.REPEAT);
		       
			    mLinearGradient1 = new LinearGradient(0, 0, 0, 50, 
			    		new int[] {nColorBegin, nColorMid1, nColorMid2, nColorEnd }, null,
		                Shader.TileMode.CLAMP);
		        mLinearGradient2 = new LinearGradient(0, 0, 0, 100, 
		        		new int[] {nColorBegin, nColorMid1, nColorMid2, nColorEnd }, null,
		                Shader.TileMode.MIRROR);
		        mLinearGradient3 = new LinearGradient(0, 0, 0, 100, 
		        		new int[] {nColorBegin, nColorMid1, nColorMid2, nColorEnd }, null,
		                Shader.TileMode.REPEAT);
		        
		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient1);
		        canvas.drawRect(0, 0, 200, 200, mPaint);

		        
		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient2);
		        canvas.drawRect(0, 250, 200, 450, mPaint);

		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient3);
		        canvas.drawRect(0, 500, 200, 700, mPaint);
		        
			}*/
		
		} else if (2 == m_RibbonTest.m_nHorV) {
			int nColorBegin = 0;
			int nColorEnd = 0;
			// ����
			for (int i = m_DisplayX + spos; i < m_DisplayWidth + m_DisplayX; i++) {
				m_RibbonTest.GetCurColor();
				Paint p = new Paint();
				//p.setDither(true);
				p.setColor(m_RibbonTest.m_nColor);
				//int[] fadeColors = {0x00000000, 0xFF000000, 0xFF000000, 0x00000000};
				//p.setShader(new LinearGradient(0, 0, 0, i, fadeColors, null,LinearGradient.TileMode.CLAMP));
				//p.setXfermode(new AvoidXfermode(0xFF307070, 255, AvoidXfermode.Mode.TARGET));
				
				// float[] pts = { i,m_DisplayX,i,m_DisplayWidth + m_DisplayX };
				 
				float[] pts = { i, m_DisplayY, i, m_DisplayHeight + m_DisplayY };
				canvas.drawLines(pts, p);
				
				if (i== m_DisplayX + spos) {
					nColorBegin = m_RibbonTest.m_nColor;					
				} 
				if (i== m_DisplayWidth + m_DisplayX - 1) {
					nColorEnd = m_RibbonTest.m_nColor;
				}
			}
			
			/*
			{
				Paint mPaint = null;
				mPaint = new Paint();

				// ���Խ�����Ⱦ
			    Shader mLinearGradient1 = null;
			    // ���Խ�����Ⱦ
			    Shader mLinearGradient2 = null;
			    // ���Խ�����Ⱦ
			    Shader mLinearGradient3 = null;

			    // ����LinearGradient�����ý�����ɫ����
		        // ��һ��,�ڶ���������ʾ������� ������������յ��ڶԽǵ�����λ��
		        // ������,���ĸ�������ʾ�����յ�
		        // �����������ʾ������ɫ
		        // ��������������Ϊ��,��ʾ����,ֵΪ0-1 new float[] {0.25f, 0.5f, 0.75f, 1 }
		        // ������ǿյģ���ɫ���ȷֲ������ݶ��ߡ�
		        // ���߸���ʾƽ�̷�ʽ
		        // CLAMP�ظ����һ����ɫ�����
		        // MIRROR�ظ���ɫ��ͼ��ˮƽ��ֱ�����Ѿ���ʽ�����з�תЧ��
		        // REPEAT�ظ���ɫ��ͼ��ˮƽ��ֱ����
			    ///*
			    //mLinearGradient1 = new LinearGradient(0, 0, 0, 100, new int[] {
		        //        Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		        //        Shader.TileMode.CLAMP);
		        //        */			    
			    //mLinearGradient1 = new LinearGradient(0, 0, 0, 300, 
			    //		new int[] {nColorBegin, nColorEnd }, null,
		        //        Shader.TileMode.CLAMP);
			    /*
		        mLinearGradient2 = new LinearGradient(0, 0, 0, 100, new int[] {
		                Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		                Shader.TileMode.MIRROR);
		        mLinearGradient3 = new LinearGradient(0, 0, 0, 100, new int[] {
		                Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null,
		                Shader.TileMode.REPEAT);
		       //

		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient1);
		        canvas.drawRect(0, 0, 400, 400, mPaint);

		        
		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient2);
		        canvas.drawRect(0, 250, 200, 450, mPaint);

		        // ���ƽ���ľ���
		        mPaint.setShader(mLinearGradient3);
		        canvas.drawRect(0, 500, 200, 700, mPaint);
				
			}
			*/
		}
		// ������Ļ��ʾ����
		sfh.unlockCanvasAndPost(canvas);
	}

	private void DrawGrid() {
		Canvas canvas = sfh.lockCanvas(new Rect(m_DisplayX, m_DisplayY,
				m_DisplayWidth, m_DisplayHeight - 80));

		if (canvas == null) {
			return;
		}

		Paint p = new Paint();
		//p.setDither(true);
		canvas.drawColor(Color.BLACK);
		
		
		String colchangetype = preferences.getString("colchangetype", "singlecolor");
		if(colchangetype.equals("singlecolor"))
		{
			nGridColorCnt = 1;
		}
		else if(colchangetype.equals("doublecolor"))
		{
			nGridColorCnt = 2;
		}
		else if(colchangetype.equals("triplecolor"))
		{
			nGridColorCnt = 3;
		}				
		
		lcIndex = lcIndex < nGridColorCnt ? lcIndex : 0; 
		p.setColor(m_GridTest.m_nColor[lcIndex]);
		lcIndex++;
		int iPos = m_GridTest.m_nSPos;
		// ����
		if (m_GridTest.m_bHorizontalLine) {
			int x = m_DisplayX;
			int y = m_DisplayY + iPos;
			int nTempy = y;
			while (nTempy > 0) {
				nTempy = nTempy - m_GridTest.m_nSpace - 1;
				float[] pts = { x, nTempy, x + m_DisplayWidth + m_DisplayX,
						nTempy };
				canvas.drawLines(pts, p);
			}
			while (y < m_DisplayHeight + m_DisplayY) {
				float[] pts = { x, y, x + m_DisplayWidth + m_DisplayX, y };
				canvas.drawLines(pts, p);
				y += m_GridTest.m_nSpace + 1;
			}
		}
		// ����
		if (m_GridTest.m_bVerticalLine) {
			int x = m_DisplayX + iPos;
			int y = m_DisplayY;
			int nTempx = x;
			while (nTempx > 0) {
				nTempx = nTempx - m_GridTest.m_nSpace - 1;
				float[] pts = { nTempx, y, nTempx,
						y + m_DisplayHeight + m_DisplayY };
				canvas.drawLines(pts, p);
			}
			while (x < m_DisplayWidth + m_DisplayX) {
				float[] pts = { x, y, x, y + m_DisplayHeight + m_DisplayY };
				canvas.drawLines(pts, p);
				x += m_GridTest.m_nSpace + 1;
			}
		}
		// ��б��
		if (m_GridTest.m_bLeftDiagonalLine) {
			int x = m_DisplayX + iPos;
			int y = m_DisplayY + iPos;
			int nMax = m_DisplayWidth + m_DisplayX + m_DisplayHeight
					+ m_DisplayY;
			while (x < nMax) {
				float[] pts = { x, 0, 0, y };
				canvas.drawLines(pts, p);

				x += m_GridTest.m_nSpace + 1;
				y += m_GridTest.m_nSpace + 1;
			}
		}
		// ��б��
		if (m_GridTest.m_bRightDiagonalLine) {
			int x = m_DisplayX + iPos;
			int y = m_DisplayHeight + m_DisplayY - x;
			int nMax = m_DisplayWidth + m_DisplayX + m_DisplayHeight
					+ m_DisplayY;
			while (y > -nMax) {
				float[] pts = { 0, y, x, m_DisplayHeight + m_DisplayY };
				canvas.drawLines(pts, p);
				x += m_GridTest.m_nSpace + 1;
				y -= m_GridTest.m_nSpace + 1;
			}
		}

		// ������Ļ��ʾ����
		sfh.unlockCanvasAndPost(canvas);
	}

	private void DrawSpots() {
		Canvas canvas = sfh.lockCanvas(new Rect(m_DisplayX, m_DisplayY,
				m_DisplayWidth, m_DisplayHeight - 80));

		if (canvas == null) {
			return;
		}

		boolean bFlag = false;

		if (1 == m_SpotsTest.m_nStyle) {
			bFlag = true; // �׵���
		} else if (2 == m_SpotsTest.m_nStyle) {
			bFlag = false; // �ڵ���
		}
		boolean bCurFlag = bFlag;
		int clr;

		for (int y = 0; y < m_DisplayHeight + m_DisplayY;) {
			for (int x = 0; x < m_DisplayWidth + m_DisplayX;) {
				if (bFlag) {
					clr = Color.BLACK;
				} else {
					clr = Color.WHITE;
				}
				Paint p = new Paint();
				p.setColor(clr);
				p.setStyle(Paint.Style.FILL);
				canvas.drawRect(x, y, x + m_SpotsTest.m_nSpace, y
						+ m_SpotsTest.m_nSpace, p);

				x += m_SpotsTest.m_nSpace;
				bFlag = !bFlag;
			}
			y += m_SpotsTest.m_nSpace;
			bCurFlag = !bCurFlag;
			bFlag = bCurFlag;
		}
		// ������Ļ��ʾ����
		sfh.unlockCanvasAndPost(canvas);
	}

	public Rect getDisplayRect() {
		Rect rect = new Rect();
		rect.left = m_DisplayX;
		rect.top = m_DisplayY;
		rect.right = rect.left + m_DisplayWidth;
		rect.bottom = rect.top + m_DisplayHeight;

		return rect;
	}

	// ��ʼ����
	public void StartAnimation(int nInterval) {
		m_Timer.schedule(m_TimerTask, 0, nInterval);
	}

	// ֹͣ����
	public void StopAnimation() {
		while (!m_TimerTask.cancel()) {
			m_Timer.cancel();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		SerialPortControlBroadCast.SetCurrentContext(this);
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		mTestActivity = this;
		SerialPortControlBroadCast.SetCurrentContext(this);
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// StopAnimation();
		mTestActivity = null;
		PannelLedControlBroadCast.MakeLightsAlwaysOFF();
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// StopAnimation();
		PannelButtonDownService.observers.remove(this);
		mTestActivity = null;
		super.onDestroy();
	}

	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		if (!xParam1.equals(TestActivity.class.getName().toString())) {
			return 0;
		}
		byte cmd = Byte.parseByte(xParam2);
		switch (cmd) {
		case SerialPortControlBroadCast.CMD_TEST:

			System.out.println("test...");
			if (null == TestActivity.getInstance()) {
			} else {
				// TestActivity.getInstance().SetTestMode(0);
			}
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TESTLIGHT);
			break;

		case SerialPortControlBroadCast.CMD_CLOCKWISE:
			switch (SerialPortControlBroadCast.GetCMD_CURRENT()) {

			case SerialPortControlBroadCast.CMD_TEST:

				if (null != TestActivity.getInstance()) {
					SetTestMode(1);
				}
				// PannelLedControlBroadCast.MakePannelChoicesOFF();
				// PannelLedControlBroadCast.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TESTLIGHT);

			default:
				break;
			}
			break;
		case SerialPortControlBroadCast.CMD_ANTICLOCKWISE:
			switch (SerialPortControlBroadCast.GetCMD_CURRENT()) {
			case SerialPortControlBroadCast.CMD_TEST:
				if (null != TestActivity.getInstance()) {
					TestActivity.getInstance().SetTestMode(2);
				}

			default:
				break;
			}

			break;
		case SerialPortControlBroadCast.CMD_CANCEL:

			finish();
			break;

		default:
			if (SerialPortControlBroadCast.CMD_CLOCK_VALUE == (cmd & 0xF0)) {

			}
			break;
		}
		return 1;
	}
}