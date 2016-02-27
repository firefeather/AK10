package com.szaoto.ak10.leddisplay;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.datacomm.InterfaceComm;
import com.szaoto.ak10.ownerdraw.CabinetViewObj;
import com.szaoto.ak10.ownerdraw.ChannelViewObj;
import com.szaoto.ak10.ownerdraw.InterfaceViewObj;
import com.szaoto.ak10.ownerdraw.LedConfigCustomView;
import com.szaoto.ak10.ownerdraw.ObjLog;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.sqlitedata.CbtData;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.util.UtilFun;

/**
 * 
 * ���� LedDisplaySetupCardActivity ���� LED��������ģ�飬���Զ԰忨�������ã��Ŵ󣬵������� ������ zhangsj
 * �������� 2014��6��30��
 * 
 * 
 */
public class LedConstructActivity extends FragmentActivity implements
		View.OnDragListener, View.OnClickListener {
	private CardFragment mCardFragment;
	public ToolFragment mToolFragment;
	// private NavgationFragment mNavgationFragment;
	// private LedDisplayFragment mLedDisplayFragment;
	private FragmentManager fragmentManager;

	// hh �Զ���View����
	LedConfigCustomView m_LedConfigCustomView;

	RelativeLayout syscardshow;
	private TextView sys_tv;

	private RelativeLayout mainlayout;
	private LinearLayout cardShowLayout;
	private LinearLayout acqcardshow;
	private TextView[] textViewsArr_Snd = new TextView[10];
	TextView tTextView = null;
	private TextView[] textViewsArr_Acq = new TextView[2];
	TextView tTextView_Acq = null;

	FragmentTransaction fragmentTransaction;
	private LinearLayout leddisplay_top;
	private ZoomControls mZoomControls;
	private RelativeLayout zoomButtonShow;
	private Button zoomToSmall;
	private RelativeLayout zoomSmallShow;
	boolean mLableVisible;
	boolean bShowResolution;

	LinearLayout card_config;
	LinearLayout layout_leddisplay;
	CheckBox SelectChannel, SelectInterface, SelectCabinet, SelectLable;
	public RelativeLayout scrollayout;
	private FrameLayout Lablelayout;
	private TextView textleddisplay_top;

	ProgressDialog progChDataInitDiag;
	public int gLedId;

	View gViewCardInterfaceView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.leddisplay_configration);
		SerialPortControlBroadCast.SetCurrentContext(this);
		gLedId = Ak10Application.GetLedId();

		mainlayout = (RelativeLayout) findViewById(R.id.Rcontainer);
		Lablelayout = (FrameLayout) findViewById(R.id.popLable);
		textleddisplay_top = (TextView) findViewById(R.id.text_leddisplay);

		// ��ȡLED��ID��

		textleddisplay_top.setText("LED" + gLedId);
		card_config = (LinearLayout) findViewById(R.id.card_config);
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();

		// ���ذ忨ģ��
		mCardFragment = new CardFragment();
		fragmentTransaction.add(R.id.card_fragment, mCardFragment);
		mCardFragment.setActivity(this);

		m_LedConfigCustomView = (LedConfigCustomView) findViewById(R.id.ledconfigcustomview);
		m_LedConfigCustomView.m_LedConfigActivity = this;
		m_LedConfigCustomView.setOnDragListener(this);

		// ���ع�����ģ��

		mToolFragment = new ToolFragment();
		fragmentTransaction.add(R.id.tool_fragment, mToolFragment);
		mToolFragment.setActivity(this);
		fragmentTransaction.commit();
		leddisplay_top = (LinearLayout) findViewById(R.id.leddisplay_top);

	}

	private void InitCbtFromDb() {

		ArrayList<CbtData> arrayListCbtDatas = CabinetDB.GetAllRecord(gLedId);
		int narrayListCbtDatasSize = arrayListCbtDatas.size(); 
		for (int i = 0; i < narrayListCbtDatasSize; i++) {

			CbtData tCbtData = arrayListCbtDatas.get(i);

			int nID = tCbtData.Id;
			int noffsetx = tCbtData.offsetX;
			int noffsety = tCbtData.offsetY;
			int nWidth = tCbtData.width;
			int nHeight = tCbtData.height;
			int nShowId = tCbtData.addrshowid;

			CabinetViewObj tCbtViewObj = new CabinetViewObj();

			tCbtViewObj.setmBasicViewID(nID);
			tCbtViewObj.m_leftOrg = noffsetx;
			tCbtViewObj.m_topOrg = noffsety;
			tCbtViewObj.m_width = nWidth;
			tCbtViewObj.m_height = nHeight;
			tCbtViewObj.setStrTypeString(tCbtData.strModelType);
            tCbtViewObj.setM_ShowAddressId(nShowId); 
			tCbtViewObj.setM_AddressId(tCbtData.address);

			m_LedConfigCustomView.AddBasicView(tCbtViewObj);
		}
	}

	private void InitChnFromDb() {

		ArrayList<ChnData> arrayListChanDatas = ChannelDB.GetAllRecord(gLedId);
		int narrayListChanDatasSize = arrayListChanDatas.size(); 
		for (int i = 0; i < narrayListChanDatasSize; i++) {
			ChnData tChnData = arrayListChanDatas.get(i);
			int nID = tChnData.Id;
			int noffsetx = tChnData.offsetX;
			int noffsety = tChnData.offsetY;
			int nWidth = tChnData.width;
			int nHeight = tChnData.height;		
			byte[] byteMacAddress = tChnData.macaddress;
			int nVideoSourceId = tChnData.videosourceid;

			ChannelViewObj tChnViewObj = new ChannelViewObj();
			tChnViewObj.setmBasicViewID(nID);
			tChnViewObj.m_leftOrg = noffsetx;
			tChnViewObj.m_topOrg = noffsety;
			tChnViewObj.m_width = nWidth;
			tChnViewObj.m_height = nHeight;
			tChnViewObj.setmMacAddress(byteMacAddress);
			tChnViewObj.setChPortId(nVideoSourceId);

			m_LedConfigCustomView.AddBasicView(tChnViewObj);
			m_LedConfigCustomView.InitSetVideoSource(tChnViewObj);
		}

	}

	private void InitIntfaceFromDb() {

		ArrayList<IntfData> arrayListIntfDatas = InterfaceDB.GetAllRecord(gLedId);
		int narrayListIntfDatasSize = arrayListIntfDatas.size();
		for (int i = 0; i < narrayListIntfDatasSize; i++) {
			IntfData tintfData = arrayListIntfDatas.get(i);

			int nID = tintfData.Id;
			int noffsetx = tintfData.offsetX;
			int noffsety = tintfData.offsetY;
			int nWidth = tintfData.width;
			int nHeight = tintfData.height;
			int nParentId = tintfData.channelid;
			byte[] macaddress = tintfData.macaddress;

			InterfaceViewObj tIntfViewObj = new InterfaceViewObj();
			tIntfViewObj.setmBasicViewID(nID);
			tIntfViewObj.m_leftOrg = noffsetx;
			tIntfViewObj.m_topOrg = noffsety;
			tIntfViewObj.m_width = nWidth;
			tIntfViewObj.m_height = nHeight;
			
			//��Բɼ�����λ��
			ChnData tChnData = ChannelDB.GetRecordById(nParentId, gLedId);
			tIntfViewObj.xMoveRela = noffsetx-tChnData.offsetX;
			tIntfViewObj.yMoveRela = noffsety-tChnData.offsetY;
			
			tIntfViewObj.setmMacAddress(macaddress);
			tIntfViewObj.setmBackGroundColor(UtilFun.GetColorById(nID));

			m_LedConfigCustomView.AddBasicView(tIntfViewObj);

			// ����parent video surce
			int nArrayChanelViewsListSize = m_LedConfigCustomView.m_ArrayChanelViewsList.size();
			for (int j = 0; j < nArrayChanelViewsListSize; j++) {
				if (m_LedConfigCustomView.m_ArrayChanelViewsList.get(j).getmBasicViewID() == nParentId) {
					tIntfViewObj.setmParentChan(m_LedConfigCustomView.m_ArrayChanelViewsList.get(j));
					m_LedConfigCustomView.m_ArrayChanelViewsList.get(j).getM_ArrayChildViewObj().add(tIntfViewObj);
					break;
				}
			}

			m_LedConfigCustomView.InitSetSendcardParams(tIntfViewObj);
		}
	}

	// �����ص�ʱ��
	@Override
	protected void onResume() {

		// group.removeAllViews();
		m_LedConfigCustomView.RemoveAllViews();

		// ����
		try {
			InitCbtFromDb();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// �ɼ���
			InitChnFromDb();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			// ���Ϳ�
			InitIntfaceFromDb();

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		HomePageActivity.InitialLEDNew();
		
		super.onResume();
	}

	public void LableShow() {

		Lablelayout.setVisibility(View.VISIBLE);
		SelectChannel = (CheckBox) findViewById(R.id.config_check_channel);
		SelectInterface = (CheckBox) findViewById(R.id.config_check_interface);
		SelectCabinet = (CheckBox) findViewById(R.id.config_check_cabinet);
		SelectLable = (CheckBox) findViewById(R.id.config_check_All);

		SelectChannel.setOnClickListener(this);
		SelectInterface.setOnClickListener(this);

		SelectCabinet.setOnClickListener(this);
		SelectLable.setOnClickListener(this);
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {

		return super.onCreateView(name, context, attrs);
	}

	/*******************************************
	 * �����忨��ʾ���������ÿһ����ͬ�Ŀ������Ʋ�ͬ�Ķ˿� Ҫ���ܹ��������
	 *******************************************/
	public void deleteViews() {
		if (syscardshow != null) {
			mainlayout.removeView(syscardshow);
		}
		if (cardShowLayout != null) {
			mainlayout.removeView(cardShowLayout);
		}
		if (acqcardshow != null) {
			mainlayout.removeView(acqcardshow);
		}
		if (gViewCardInterfaceView != null) {
			mainlayout.removeView(gViewCardInterfaceView);
		}

		CustomToast.showToast(this, "", Toast.LENGTH_SHORT);
	}

	public void AddInterfaceViewNormal(final byte[] MACAddress)// ��ͨ�ķ��Ϳ�
	{
		final int tSlotNum = MACAddress[5];
		// �����Ӳ���
		cardShowLayout = new LinearLayout(this);
		cardShowLayout.setBackgroundResource(R.drawable.card);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // ��Ļ���ȣ����أ�

		RelativeLayout.LayoutParams RL_PP = new RelativeLayout.LayoutParams(
				200, 600);
		RL_PP.setMargins(width - 210, 130, 1, 0);

		cardShowLayout.setOrientation(LinearLayout.VERTICAL);
		cardShowLayout.setLayoutParams(RL_PP);
		cardShowLayout.setClickable(true);

		String strInfoString = String.valueOf(tSlotNum);
		TextView textInfoTextView = new TextView(this);
		LinearLayout.LayoutParams tParaInfo = new LinearLayout.LayoutParams(
				100, 70);
		tParaInfo.setMargins(40, 180, 36, 4);
		textInfoTextView.setText(strInfoString);
		textInfoTextView.setGravity(Gravity.CENTER);
		textInfoTextView.setTextColor(Color.BLACK);
		textInfoTextView.setTextSize(20);
		cardShowLayout.addView(textInfoTextView);

		for (int i = 3; i >= 0; i--) {

			textViewsArr_Snd[i] = new TextView(this);
			textViewsArr_Snd[i].setLongClickable(true);
			textViewsArr_Snd[i].setId(i + 1);
			String strText = "SND_" + String.valueOf(i + 1);
			textViewsArr_Snd[i].setText(strText);
			textViewsArr_Snd[i].setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams Para1 = new LinearLayout.LayoutParams(
					100, 80);
			if (i == 3) {
				Para1.setMargins(60, 65, 36, 4);
			} else {
				Para1.setMargins(60, 10, 36, 4);
			}

			textViewsArr_Snd[i].setLayoutParams(Para1);
			textViewsArr_Snd[i].setClickable(true);
			textViewsArr_Snd[i].setBackgroundResource(R.drawable.send_port);
			cardShowLayout.addView(textViewsArr_Snd[i]);
			textViewsArr_Snd[i].setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int id = v.getId();
					String strInfo = strGetInfo(MACAddress, id, 1);
					ClipData.Item item = new ClipData.Item(strInfo);
					String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
					ClipData dragData = new ClipData(strInfo, mimeTypes, item);

					View.DragShadowBuilder myShadow = new DragShadowBuilder(v);
					// Starts the drag
					v.startDrag(dragData, myShadow, null, 0);
					return false;
				}
			});

		}

		mainlayout.addView(cardShowLayout);
		setContentView(mainlayout);

		CustomToast.showToast(this, "", Toast.LENGTH_SHORT);

		cardShowLayout.setOnClickListener(new OnClickListener() {
			private boolean isSNDCardShow = true;

			@Override
			public void onClick(View v) {
				if (isSNDCardShow) {
					cardShowLayout.setVisibility(View.INVISIBLE);
					isSNDCardShow = false;
				} else {
					isSNDCardShow = true;
				}
			}
		});
		return;
	}

	public void AddInterfaceViewFiber(final byte[] MACAddress)// ��ͨ�ķ��Ϳ�
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		gViewCardInterfaceView = inflater.inflate(
				R.layout.layout_fiberinterface, null);

		RelativeLayout.LayoutParams RL_PP = new RelativeLayout.LayoutParams(
				270, 560);
		RL_PP.setMargins(1000, 130, 0, 0);

		gViewCardInterfaceView.setLayoutParams(RL_PP);
		gViewCardInterfaceView.setClickable(true);

		textViewsArr_Snd[0] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND01);
		textViewsArr_Snd[1] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND02);
		textViewsArr_Snd[2] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND03);
		textViewsArr_Snd[3] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND04);
		textViewsArr_Snd[4] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND05);
		textViewsArr_Snd[5] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND06);
		textViewsArr_Snd[6] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND07);
		textViewsArr_Snd[7] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND08);
		textViewsArr_Snd[8] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND09);
		textViewsArr_Snd[9] = (TextView) gViewCardInterfaceView
				.findViewById(R.id.SND10);

		for (int i = 0; i < 10; i++) {

			textViewsArr_Snd[i].setClickable(true);

			final int nId = i + 1;
			textViewsArr_Snd[i].setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					String strInfo = strGetInfo(MACAddress, nId, 1);
					ClipData.Item item = new ClipData.Item(strInfo);
					String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
					ClipData dragData = new ClipData(strInfo, mimeTypes, item);
					View.DragShadowBuilder myShadow = new DragShadowBuilder(v);
					// Starts the drag
					v.startDrag(dragData, // the data to be dragged
							myShadow, // the drag shadow builder
							null, // no need to use local data
							0 // flags (not currently used, set to 0)
					);
					return false;
				}
			});

		}

		mainlayout.addView(gViewCardInterfaceView);

		gViewCardInterfaceView.setOnClickListener(new OnClickListener() {
			private boolean isSNDCardShow = true;

			@Override
			public void onClick(View v) {
				if (isSNDCardShow) {
					gViewCardInterfaceView.setVisibility(View.INVISIBLE);
					isSNDCardShow = false;
				} else {
					isSNDCardShow = true;
				}
			}
		});
	}

	public void AddInterfaceView(final byte[] MACAddress) {
		// ��ȡ���Ϳ����ͺ�
		int CardType = InterfaceComm.GetSndCardHardwareType(MACAddress);

		if (CardType == 2)// Fiber
		{
			AddInterfaceViewFiber(MACAddress);
		} else if (CardType == 1) // RJ45*4
		{
			AddInterfaceViewNormal(MACAddress);
		} else {
			Toast.makeText(this, "CardType = " + CardType + "Not Support!",
					Toast.LENGTH_LONG).show();
		}

		CustomToast.showToast(this, "", Toast.LENGTH_SHORT);

		m_LedConfigCustomView.invalidate();
		return;
	}

	// 0 chan 1:intef
	private String strGetInfo(byte[] MacAddress, int nInterfaceID, int tTypeID) {
		String strMacAddress = UtilFun.bytes2HexString(MacAddress, 6, "-");
		String strRet = String.valueOf(tTypeID) + ":" + strMacAddress + ":"
				+ String.valueOf(nInterfaceID);
		return strRet;
	}

	public void AddSysViews(final byte[] MacAddress) {
		// �¼�ϵͳ������
		final int tSlotNum = MacAddress[5];
		syscardshow = new RelativeLayout(this);
		syscardshow.setBackgroundResource(R.drawable.card);
		RelativeLayout.LayoutParams sysRL_PP = new RelativeLayout.LayoutParams(
				200, 600);
		sysRL_PP.setMargins(4, 130, 1, 110);
		sysRL_PP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		syscardshow.setLayoutParams(sysRL_PP);
		syscardshow.setClickable(true);
		syscardshow.setLongClickable(true);

		// ������Ϣ
		String strInfoString = String.valueOf(tSlotNum);
		TextView textInfoTextView = new TextView(this);
		LinearLayout.LayoutParams tParaInfo = new LinearLayout.LayoutParams(
				100, 70);
		tParaInfo.setMargins(50, 150, 36, 4);
		textInfoTextView.setText(strInfoString);
		textInfoTextView.setGravity(Gravity.CENTER);
		textInfoTextView.setTextColor(Color.BLACK);
		textInfoTextView.setTextSize(20);
		syscardshow.addView(textInfoTextView);
		// ����ϵͳ���˿�
		// �˿�1
		sys_tv = new TextView(this);
		sys_tv.setId(1);
		sys_tv.setText("SYS_1");
		sys_tv.setGravity(Gravity.TOP);
		sys_tv.setTextColor(Color.WHITE);
		sys_tv.setBackgroundResource(R.drawable.sys_port);

		RelativeLayout.LayoutParams sysPara1 = new RelativeLayout.LayoutParams(90, 160);
		sysPara1.setMargins(60, 100, 50, 4);
		sysPara1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		sys_tv.setLayoutParams(sysPara1);
		syscardshow.addView(sys_tv);

		sys_tv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String strInfo = strGetInfo(MacAddress, 1, 2);
				ClipData.Item item = new ClipData.Item(strInfo);
				String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
				ClipData dragData = new ClipData(strInfo, mimeTypes, item);

				View.DragShadowBuilder myShadow = new DragShadowBuilder(sys_tv);

				v.startDrag(dragData, myShadow, null, 0);
				return false;
			}
		});

		mainlayout.addView(syscardshow);
		setContentView(mainlayout);
		syscardshow.setOnClickListener(new OnClickListener() {
			private boolean isSysCardShow = true;

			@Override
			public void onClick(View v) {

				if (isSysCardShow) {
					syscardshow.setVisibility(View.INVISIBLE);

					isSysCardShow = false;
				} else {
					isSysCardShow = true;
				}
			}
		});

		m_LedConfigCustomView.invalidate();
		CustomToast.showToast(this, "", Toast.LENGTH_SHORT);
	}

	public void AddInterface(InterfaceViewObj interfaceViewObj) {

		int tID = interfaceViewObj.getmBasicViewID();
		int tParentChanID = interfaceViewObj.getmParentChan().getmBasicViewID();
		// data
		IntfData interfData = new IntfData();
		interfData.Id = tID;
		interfData.offsetX = UtilFun.f2i(interfaceViewObj.m_leftOrg);
		interfData.offsetY = UtilFun.f2i(interfaceViewObj.m_topOrg);
		interfData.width = UtilFun.f2i(interfaceViewObj.m_width);
		interfData.height = UtilFun.f2i(interfaceViewObj.m_height);
		interfData.macaddress = interfaceViewObj.getmMacAddress();
		interfData.channelid = tParentChanID;
		interfData.name = "sendcard_" + tID / 1000 + "_" + tID % 1000;
		interfData.ledid = gLedId;
		InterfaceDB.AddData(interfData);
	}

	static public int AddInterfDbData(int id, int tParentChid, int tledid,
			byte[] macaddress, int offx, int offy, int width, int height) {

		// �ȼ��tParentChid��Chan�治���ڣ������ڵĻ������ӵ����ݿ�

		if (!ChannelDB.CheckChanExist(tParentChid, tledid)) {
			return -1;
		}

		IntfData interfData = new IntfData();
		interfData.Id = id;
		interfData.offsetX = offx;
		interfData.offsetY = offy;
		interfData.width = width;
		interfData.height = height;
		interfData.macaddress = macaddress;
		interfData.channelid = tParentChid;
		interfData.name = "sendcard_" + id / 1000 + "_" + id % 1000;
		interfData.ledid = tledid;

		InterfaceDB.AddData(interfData);

		return 0;
	}

	public void AddAcqViews(final byte[] MacAddress) {

		final int tSlotNum = MacAddress[5];

		int CardType = ChanComm.GetAcqCardHardwareType(MacAddress);

		// �¼Ӳɼ�������
		acqcardshow = new LinearLayout(this);
		acqcardshow.setBackgroundResource(R.drawable.card);
		RelativeLayout.LayoutParams acqRL_PP = new RelativeLayout.LayoutParams(
				200, 600);
		acqRL_PP.setMargins(4, 130, 1, 110);
		acqRL_PP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		acqcardshow.setLayoutParams(acqRL_PP);
		acqcardshow.setOrientation(LinearLayout.VERTICAL);
		acqcardshow.setClickable(true);
		// ���Ӳɼ����˿�
		// ������Ϣ

		String strInfoString = String.valueOf(tSlotNum);
		TextView textInfoTextView = new TextView(this);
		LinearLayout.LayoutParams tParaInfo = new LinearLayout.LayoutParams(
				100, 70);
		tParaInfo.setMargins(50, 150, 36, 4);
		textInfoTextView.setText(strInfoString);
		textInfoTextView.setGravity(Gravity.CENTER);
		textInfoTextView.setTextColor(Color.BLACK);
		textInfoTextView.setTextSize(20);
		acqcardshow.addView(textInfoTextView);

		// �˿�1
		// AddPort(1);
		// AddPort(2);
		for (int i = 0; i < 2; i++) {

			textViewsArr_Acq[i] = new TextView(this);
			textViewsArr_Acq[i].setLongClickable(true);

			String strText = "ACQ_" + String.valueOf(i + 1);
			textViewsArr_Acq[i].setText(strText);
			textViewsArr_Acq[i].setGravity(Gravity.TOP);
			LinearLayout.LayoutParams Para1 = new LinearLayout.LayoutParams(90,
					160);

			if (i == 0) {
				Para1.setMargins(60, 70, 50, 4);
				textViewsArr_Acq[i].setId(1);

				if (CardType == 0x01) // DVI+HDMI
				{
					textViewsArr_Acq[i]
							.setBackgroundResource(R.drawable.sys_port);
				}

				else if (CardType == 0x02) // HDMI+HDMI
				{
					textViewsArr_Acq[i]
							.setBackgroundResource(R.drawable.acq_port);
				}

			} else {
				Para1.setMargins(60, 20, 50, 4);
				textViewsArr_Acq[i].setId(2);
				textViewsArr_Acq[i].setBackgroundResource(R.drawable.acq_port);
			}
			textViewsArr_Acq[i].setLayoutParams(Para1);

			textViewsArr_Acq[i].setClickable(true);
			textViewsArr_Acq[i].setLongClickable(true);

			acqcardshow.addView(textViewsArr_Acq[i]);

			tTextView_Acq = textViewsArr_Acq[i];

			final int portID = i + 1;

			textViewsArr_Acq[i].setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					String strInfo = strGetInfo(MacAddress, portID, 0);
					ClipData.Item item = new ClipData.Item(strInfo);
					String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
					ClipData dragData = new ClipData(strInfo, mimeTypes, item);

					View.DragShadowBuilder myShadow = new DragShadowBuilder(
							tTextView_Acq);
					// Starts the drag
					v.startDrag(dragData, // the data to be dragged
							myShadow, // the drag shadow builder
							null, // no need to use local data
							0 // flags (not currently used, set to 0)
					);
					return false;
				}
			});

		}

		mainlayout.addView(acqcardshow);
		setContentView(mainlayout);
		acqcardshow.setOnClickListener(new OnClickListener() {
			private boolean IsAcqCardShow = true;

			@Override
			public void onClick(View v) {
				if (IsAcqCardShow) {
					acqcardshow.setVisibility(View.INVISIBLE);
					IsAcqCardShow = false;
				} else {
					IsAcqCardShow = true;
				}
			}
		});

		m_LedConfigCustomView.invalidate();

		CustomToast.showToast(this, "", Toast.LENGTH_SHORT);
	}

	// �Ϸ��¼������µ�ʱ�����ж�����,���յ���group

	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		case DragEvent.ACTION_DROP: // ��ק�ַſ�˲�䣬ִ�л��ƶ���
			// ��ȡ�ſ�ʱ��ӿڶ����pos
			int nXDrop = (int) event.getX();
			int nYDrop = (int) event.getY();
			// �����ж�̧��˲��������������ĸ�������
			ClipData.Item tItemData = event.getClipData().getItemAt(0);
			String strInfo = (String) tItemData.getText();
			// ��ֳ��ĸ�slot���ĸ��ӿ�
			String[] strPackInfoStrings = strInfo.split(":");
			String strType = strPackInfoStrings[0];

			/** MAC,����������ʵ�忨ʱ��ȡmac��ַ ****/
			String strMACAddress = strPackInfoStrings[1];
			byte[] byteMacAddress = UtilFun.hexStringSplit2Bytes(strMACAddress,
					"-");
			String strSlotNumber = String.valueOf(byteMacAddress[5]);
			String strInterfaceNumber = strPackInfoStrings[2];

			// �����Ƿ���������Ϣ�����ھ;ܾ����ӣ������ھ�����
			if (Integer.parseInt(strType) == 0) {
				int nArrayChanelViewsListSize = m_LedConfigCustomView.m_ArrayChanelViewsList.size();
				for (int i = 0; i < nArrayChanelViewsListSize; i++) {

					ChannelViewObj tChannelViewObj = m_LedConfigCustomView.m_ArrayChanelViewsList.get(i);
					int tID = tChannelViewObj.getmBasicViewID();
					int tInterfaceNumber = tID % 1000;
					if (tID / 1000 == Integer.parseInt(strSlotNumber)
							&& (tInterfaceNumber == Integer
									.parseInt(strInterfaceNumber))) {
						Toast.makeText(this, R.string.text_channelconfig,
								Toast.LENGTH_SHORT).show();
						return true;
					}
				}

				// id = slot*1000+id
				int tID = Integer.parseInt(strInterfaceNumber)
						+ Integer.parseInt(strSlotNumber) * 1000;
				// ���ĸ�id���浽channel��
				// Ҫ���ݿ����ͺ����������channel�Ľӿ��ͺ�
				AddChannel(tID, byteMacAddress, nXDrop, nYDrop);
			} else if (Integer.parseInt(strType) == 1) {
				// InterfacePort interfacePort =
				// DataAccessSendCardsData.GetSendCardData(strMACAddress,
				// Integer.valueOf(strIDInfo));
				int nArrayInterfaceViewListSize = m_LedConfigCustomView.m_ArrayInterfaceViewList.size();
				for (int i = 0; i < nArrayInterfaceViewListSize; i++) {

					InterfaceViewObj tInterfaceView = m_LedConfigCustomView.m_ArrayInterfaceViewList.get(i);

					int tID = tInterfaceView.getmBasicViewID();
					int tInterfaceNumber = tID % 1000;

					if (tID / 1000 == Integer.parseInt(strSlotNumber)
							&& (tInterfaceNumber == Integer
									.parseInt(strInterfaceNumber))) {
						Toast.makeText(this, R.string.text_interfaceconfig,
								Toast.LENGTH_SHORT).show();
						return true;
					}
				}

				// interface
				// ����û����ͬ��id�����棬�еĻ�������
				ArrayList<ChannelViewObj> tArrayList = m_LedConfigCustomView.m_ArrayChanelViewsList;

				int nX = UtilFun.f2i(event.getX());
				int nY = UtilFun.f2i(event.getY());

				// û��chan����
				if (tArrayList.size() == 0) {
					Toast.makeText(this, R.string.text_add_acqcard,
							Toast.LENGTH_SHORT).show();
					return false;
				}

				int i = 0;
				int ntArrayListSize = tArrayList.size();
				for (i = 0; i < ntArrayListSize; i++) {

					RectF tRectF = new RectF(
							tArrayList.get(i).m_leftCustomView,
							tArrayList.get(i).m_topCustomView,
							tArrayList.get(i).m_leftCustomView
									+ tArrayList.get(i).m_WidthZoomed,
							tArrayList.get(i).m_topCustomView
									+ tArrayList.get(i).m_HeightZoomed);

					if (m_LedConfigCustomView.PtInRect(new Point(nX, nY),
							tRectF)) {
						break;
					}

				}

				if (i == tArrayList.size()) {
					return true;
				}

				// �ҵ�������ľ��Σ�ֱ����������ε����Ͻ�����interface
				ChannelViewObj tCHANOBJ = tArrayList.get(i);

				// ����Interface
				InterfaceViewObj tNewInterfaceViewObj = new InterfaceViewObj();
				tNewInterfaceViewObj.m_leftCustomView = nXDrop;
				tNewInterfaceViewObj.m_topCustomView = nYDrop;
				// ��OrgX,Y

				tNewInterfaceViewObj.m_leftZoomed = nXDrop
						+ m_LedConfigCustomView.m_ViewPortPosF.x;
				tNewInterfaceViewObj.m_topZoomed = nYDrop
						+ m_LedConfigCustomView.m_ViewPortPosF.y;

				tNewInterfaceViewObj.m_leftOrg = UtilFun
						.f2i(tNewInterfaceViewObj.m_leftZoomed
								/ m_LedConfigCustomView.m_Factor) ;
				tNewInterfaceViewObj.m_topOrg = UtilFun
						.f2i(tNewInterfaceViewObj.m_topZoomed
								/ m_LedConfigCustomView.m_Factor) ;

				tNewInterfaceViewObj.m_width = 600;
				tNewInterfaceViewObj.m_height = 600;
				tNewInterfaceViewObj.setmParentChan(tCHANOBJ);
				tNewInterfaceViewObj
						.setmSlotID(Integer.parseInt(strSlotNumber));
				tNewInterfaceViewObj.setmMacAddress(byteMacAddress);
				int tID = Integer.parseInt(strInterfaceNumber) + 1000
						* Integer.parseInt(strSlotNumber);
				tNewInterfaceViewObj.setmBasicViewID(tID);
				tNewInterfaceViewObj.setmBackGroundColor(UtilFun
						.GetColorById(tID));



				m_LedConfigCustomView.AddBasicView(tNewInterfaceViewObj);

				AddInterface(tNewInterfaceViewObj);

				// /////////////////////////////////////

				// ����interface��ʼ״̬
				// 1���涨 ��ƵԴ
				int nVideoSourcePortId = tNewInterfaceViewObj.getmParentChan()
						.getChPortId();
				InterfaceComm.SetSendCardChPortAndEnable(true,
						nVideoSourcePortId,
						tNewInterfaceViewObj.getmMacAddress(),
						tNewInterfaceViewObj.getmBasicViewID() % 1000);
				// 2.���¼�������

				short x = (short) UtilFun.f2i(tNewInterfaceViewObj.m_leftOrg
						- tNewInterfaceViewObj.getmParentChan().m_leftOrg);
				short y = (short) UtilFun.f2i(tNewInterfaceViewObj.m_topOrg
						- tNewInterfaceViewObj.getmParentChan().m_topOrg);
				short w = (short) UtilFun.f2i(tNewInterfaceViewObj.m_width);
				short h = (short) UtilFun.f2i(tNewInterfaceViewObj.m_height);
				int ncfg3d = tNewInterfaceViewObj.m_nCfg3d;
				tNewInterfaceViewObj.xMoveRela = x;
				tNewInterfaceViewObj.yMoveRela = y;

				InterfaceComm.SetSendCardPortParam(x, y, w, h,ncfg3d,
						tNewInterfaceViewObj.getmMacAddress(),
						tNewInterfaceViewObj.getmBasicViewID() % 1000);

				// /////////////////////////////////////////
				// ����Interface�Ĳ���
				tCHANOBJ.getM_ArrayChildViewObj().add(tNewInterfaceViewObj);
			} else if (Integer.parseInt(strType) == 2) {
				int tempID = Integer.parseInt(strInterfaceNumber)
						+ Integer.parseInt(strSlotNumber) * 1000;
				int nArrayChanelViewsListSize = m_LedConfigCustomView.m_ArrayChanelViewsList.size();
				for (int i = 0; i < nArrayChanelViewsListSize; i++) {
					ChannelViewObj tSystemCardView = (ChannelViewObj) m_LedConfigCustomView.m_ArrayChanelViewsList.get(i);
					int tID = tSystemCardView.getmBasicViewID();
					if (tID == tempID) {
						Toast.makeText(this, R.string.text_interfaceconfig,
								Toast.LENGTH_LONG).show();
						return true;
					}
				}
				// id = slot*1000+id
				int tID = Integer.parseInt(strInterfaceNumber)
						+ Integer.parseInt(strSlotNumber) * 1000;

				// ���ĸ�id���浽channel��

				AddSystemCard(tID, byteMacAddress, nXDrop, nYDrop);
			}
			break;
		default:
			break;
		}

		return true;
	}

	private void AddSystemCard(int tChID, byte[] macAddress, int xDrop,
			int yDrop) {

		int nLeft = xDrop;
		int nTop = yDrop;

		if (nLeft < 0)
			nLeft = 0;
		if (nTop < 0)
			nTop = 0;

		ChannelViewObj tChViewObj = new ChannelViewObj();
		tChViewObj.setmBasicViewID(tChID);
		tChViewObj.setmBackGroundColor(Color.argb(140, 25, 58, 137)); // ���ð�͸��
		tChViewObj.setmMacAddress(macAddress); // Mac��ַ
		tChViewObj.m_width = 800;
		tChViewObj.m_height = 1280;

		// ������Ƶͨ��

		int chPortNum = tChID % 1000 + tChID / 1000;
		tChViewObj.setChPortId(chPortNum);

		int LEDID = Ak10Application.GetLedId();

		// ϵͳ���Ľӿ��ͺ�
		// //////////////////////////
		int nCardInterfaceType = 1;
		// //////////////////////////

		// �����ݿ�������Channel����Ϣ
		ChnData tChnData = new ChnData();
		tChnData.Id = tChID;
		tChnData.macaddress = macAddress;
		tChnData.offsetX = (int) (xDrop/m_LedConfigCustomView.m_Factor);
		tChnData.offsetY = (int) (yDrop/m_LedConfigCustomView.m_Factor);
		tChnData.strChName = "sys_" + tChID / 1000 + "_" + tChID % 1000;
		tChnData.frame_freq = 30;
		tChnData.height = 768;
		tChnData.width = 1024;
		tChnData.Ledid = gLedId;
		tChnData.videosourceid = chPortNum;
		ChannelDB.AddData(tChnData);

		// ����λ���������ݣ�������Ƶͨ�� ???������ʲô���ͣ�֮ǰû��˵
		ChanComm.SetAcqCardPortNumAndEnable(true, chPortNum,
				nCardInterfaceType, LEDID);

		// ////////////////////////////////////
		// Channel��λ��
		tChViewObj.m_leftCustomView = xDrop;
		tChViewObj.m_topCustomView = yDrop;

		tChViewObj.m_leftZoomed = xDrop
				+ m_LedConfigCustomView.m_ViewPortPosF.x;
		tChViewObj.m_topZoomed = yDrop + m_LedConfigCustomView.m_ViewPortPosF.y;

		tChViewObj.m_leftOrg = UtilFun.f2i(tChViewObj.m_leftZoomed
				/ m_LedConfigCustomView.m_Factor);
		tChViewObj.m_topOrg = UtilFun.f2i(tChViewObj.m_topZoomed
				/ m_LedConfigCustomView.m_Factor);

		// �ϳ���֮�󱣴�
		m_LedConfigCustomView.AddBasicView(tChViewObj);

		ObjLog tlog = new ObjLog();
		tlog.setM_BasicViewObjFrom(null);
		tlog.setM_BasicViewObjTo(tChViewObj);
		tlog.setM_ActionMode(m_LedConfigCustomView.LOG_CREATE);
		m_LedConfigCustomView.m_BackForwardStack.UpdateCurOpStation(tlog);

	}

	// j: 0/1
	/**
	 * 
	 * @param tChID
	 * @param macAddress
	 * @param xDrop
	 * @param yDrop
	 * @param InterfaceType
	 */
	public void AddChannel(int tChID, byte[] macAddress, int xDrop, int yDrop) {
		// ��group������chan
		int nLeft = xDrop;
		int nTop = yDrop;

		if (nLeft < 0)
			nLeft = 0;
		if (nTop < 0)
			nTop = 0;

		ChannelViewObj tChViewObj = new ChannelViewObj();
		tChViewObj.setmBasicViewID(tChID);
		tChViewObj.setmBackGroundColor(Color.argb(140, 25, 58, 137)); // ���ð�͸��
		tChViewObj.setmMacAddress(macAddress); // Mac��ַ
		tChViewObj.m_width = 1920;
		tChViewObj.m_height = 1080;
		
//		xDrop = (int) (xDrop/m_LedConfigCustomView.m_Factor);
//		yDrop = (int) (yDrop/m_LedConfigCustomView.m_Factor);

		// ������Ƶͨ��
		AddChanDbData(macAddress, tChID, xDrop, yDrop, gLedId, 0);

		int chPortNum = tChID % 1000 + tChID / 1000;
		tChViewObj.setChPortId(chPortNum);

		// ////////////////////////////////////
		// Channel��λ��
		tChViewObj.m_leftCustomView = xDrop;
		tChViewObj.m_topCustomView = yDrop;

		tChViewObj.m_leftZoomed = xDrop
				+ m_LedConfigCustomView.m_ViewPortPosF.x;
		tChViewObj.m_topZoomed = yDrop + m_LedConfigCustomView.m_ViewPortPosF.y;

		tChViewObj.m_leftOrg = UtilFun.f2i(tChViewObj.m_leftZoomed
				/ m_LedConfigCustomView.m_Factor);
		tChViewObj.m_topOrg = UtilFun.f2i(tChViewObj.m_topZoomed
				/ m_LedConfigCustomView.m_Factor);

		// �ϳ���֮�󱣴�
		m_LedConfigCustomView.AddBasicView(tChViewObj);

		ObjLog tlog = new ObjLog();
		tlog.setM_BasicViewObjFrom(null);
		tlog.setM_BasicViewObjTo(tChViewObj);
		tlog.setM_ActionMode(m_LedConfigCustomView.LOG_CREATE);
		m_LedConfigCustomView.m_BackForwardStack.UpdateCurOpStation(tlog);

		// ��ק������ʱ�������Ƶͨ���Ų�����
		// ������Ƶͨ��
	}

	public static void AddChanDbData(byte[] macAddress, int tChID, int xDrop,
			int yDrop, int tLedid, int tSysCard) {

		// �˿��ͺ� HDMI
		int chPortNum = tChID % 1000 + tChID / 1000;

		// �����ݿ�������Channel����Ϣ
		ChnData tChnData = new ChnData();
		tChnData.Id = tChID;
		tChnData.macaddress = macAddress;
		tChnData.offsetX = xDrop;
		tChnData.offsetY = yDrop;
		if (tSysCard == 1) {
			tChnData.strChName = "sys_" + tChID / 1000 + "_" + tChID % 1000;
		} else {
			tChnData.strChName = "acq_" + tChID / 1000 + "_" + tChID % 1000;
		}

		tChnData.frame_freq = 30;
		tChnData.height = 768;
		tChnData.width = 1024;
		tChnData.videosourceid = chPortNum;
		tChnData.Ledid = tLedid;
		ChannelDB.AddData(tChnData);

		// ����λ���������ݣ�������Ƶͨ�� ???������ʲô���ͣ�֮ǰû��˵
		ChanComm.SetAcqCardPortNumAndEnable(true, chPortNum, tChID % 1000,
				tLedid);
		// //////////////////////////////////////////

	}

	public void HideView() {
		card_config.setVisibility(View.GONE);
		leddisplay_top.setVisibility(View.GONE);

	}

	// ��ʾȫ��ʱ��Ҫ��ʾ�ķŴ���С��ť���˳�ȫ����ͼ��
	public void ZoomButton() {
		zoomButtonShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa = new RelativeLayout.LayoutParams(
				300, 80);
		zoomPa.setMargins(4, 4, 50, 100);
		zoomPa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomButtonShow.setLayoutParams(zoomPa);
		zoomSmallShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa1 = new RelativeLayout.LayoutParams(
				50, 50);
		zoomPa1.setMargins(4, 10, 30, 100);
		zoomPa1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomSmallShow.setLayoutParams(zoomPa1);
		mZoomControls = new ZoomControls(this);
		mZoomControls.setClickable(true);
		zoomButtonShow.addView(mZoomControls);

		zoomToSmall = new Button(this);
		zoomToSmall.setBackgroundResource(R.drawable.exit_fullscreen_nor);
		zoomToSmall.setClickable(true);
		zoomSmallShow.addView(zoomToSmall);

		mainlayout.addView(zoomSmallShow);
		mainlayout.addView(zoomButtonShow);
		zoomToSmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				leddisplay_top.setVisibility(View.VISIBLE);
				card_config.setVisibility(View.VISIBLE);
				deleteZoomButton();

			}
		});
		mZoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ����Ŵ���Ļ
				m_LedConfigCustomView.ZoomIn();
				// Toast.makeText(getApplication(), "����˷Ŵ�ť",
				// Toast.LENGTH_SHORT).show();
			}
		});
		mZoomControls.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����С��Ļ
				// Toast.makeText(getApplication(), "�������С��ť",
				// Toast.LENGTH_SHORT).show();
				m_LedConfigCustomView.ZoomOut();
			}
		});

		setContentView(mainlayout);

	}

	public void deleteZoomButton() {

		if (zoomButtonShow != null && zoomSmallShow != null) {
			mainlayout.removeView(zoomButtonShow);
			mainlayout.removeView(zoomSmallShow);
		}

	}

	// ��ʾȫ��ʱ��Ҫ��ʾ���˳�ȫ����ͼ��
	public void ZoomSmallButton() {
		zoomSmallShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa1 = new RelativeLayout.LayoutParams(
				50, 50);
		zoomPa1.setMargins(4, 10, 30, 100);
		zoomPa1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomSmallShow.setLayoutParams(zoomPa1);

		zoomToSmall = new Button(this);
		// zoomToSmall.setBackgroundColor(R.drawable.show);
		zoomToSmall.setBackgroundResource(R.drawable.exit_fullscreen_nor);
		zoomToSmall.setClickable(true);
		zoomSmallShow.addView(zoomToSmall);

		mainlayout.addView(zoomSmallShow);
		zoomToSmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				leddisplay_top.setVisibility(View.VISIBLE);
				card_config.setVisibility(View.VISIBLE);
				if (zoomSmallShow != null) {
					mainlayout.removeView(zoomSmallShow);
				}

			}
		});
	}

	// ���ر�ǩ��
	public void HideLable() {
		Lablelayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ��ʾChannel
		case R.id.config_check_channel:
			// Toast.makeText(getApplication(), "��ʾChannel��ǩ",
			// Toast.LENGTH_SHORT).show();
			m_LedConfigCustomView.ShowChan(SelectChannel.isChecked());

			break;
		// ��ʾInterface��ǩ
		case R.id.config_check_interface:
			m_LedConfigCustomView.ShowIntf(SelectInterface.isChecked());

			break;
		// ��ʾcabinet��ǩ
		case R.id.config_check_cabinet:
			m_LedConfigCustomView.ShowCbt(SelectCabinet.isChecked());

			break;
		// ��ʾ���б�ǩ
		case R.id.config_check_All:
			m_LedConfigCustomView.ShowLable(SelectLable.isChecked());
			break;

		default:
			break;
		}

	}

}