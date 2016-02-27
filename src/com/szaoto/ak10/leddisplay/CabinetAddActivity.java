/*
 * �ļ��� CabinetAddActivity.java
 * ���������б�com.szaoto.ak10.leddisplay
 * �汾��Ϣ���汾��
 * ��������2013��11��8������11:53:04
 * ��Ȩ���� huhao
 */
package com.szaoto.ak10.leddisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.ownerdraw.BasicViewObj;
import com.szaoto.ak10.ownerdraw.CabinetAddCustomView;
import com.szaoto.ak10.ownerdraw.CabinetViewObj;
import com.szaoto.ak10.ownerdraw.ObjLog;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.sqlitedata.CbtData;
import com.szaoto.ak10.util.UtilFun;

public class CabinetAddActivity extends Activity implements
		View.OnClickListener {
	/**
	 * �ؼ�
	 */
	private int rows;
	private int columns;
	TextView btn_Next;
	TextView btn_Cabinet_Add;
	TextView btn_Cabinet_Back;
	Spinner mSpin_CabinetSeries;
	Spinner mSpin_CabinetModel;
	TextView btn_Delete_cabinet;
	EditText edit_Row;
	EditText edit_Column;
	EditText edit_X;
	EditText edit_Y;
	// ImageButton btn_Save_cabinet;

	CabinetAddCustomView m_CabinetAddCustomView;

	private RelativeLayout layout_AddCabinet_Show;
	private RelativeLayout layout_AddCabinet_Root;
	Button btn_AddConfirm;
	Button btn_AddCancel;
	TextView btnCabinet_ZoomIn;
	TextView btnCabinet_ZoomNormal;
	TextView btnCabinet_ZoomOut;
	TextView txtCabinet_ShowAll;
	TextView txtCabinet_FullScreen;
	CheckBox btn_SelectAll;
	private TextView ledDisplayText;
	private TextView textCabinet_Add;
	/**
	 * ����
	 */

	public int gLEDID;

	private RelativeLayout layout_AddCabinet_Tool;
	private RelativeLayout layout_AddCabinet_Text;
	private RelativeLayout zoomButtonFull;
	private RelativeLayout zoomSmallShow;
	private ZoomControls mZoomControls;
	private Button zoomToSmall;
	private RelativeLayout layout_AddCabinet_Top;
	private boolean bIsSelAll = false;

	CustomProgressDialog MergeCabinetDiag;

	private CustomProgressDialog mReadCabinetDialog;

	final int LOG_MOVE = 0;
	final int LOG_SCROLL = 1;
	final int LOG_SIZE = 2;
	final int LOG_CREATE = 3;
	final int LOG_CREATE_GROUP = 4;
	final int LOG_DELETE = 5;
	final int LOG_DELETE_GROUP = 6;

	private TextView textCabinet_Next;
	private TextView textCabinet_Select;
	private TextView textCabinet_ShowAll;
	private TextView textCabinet_FullScreen;

	/**
	 * ���ݼ��ؽ���
	 */
	class DataLoadTask extends AsyncTask<Integer, Integer, String> {

		public DataLoadTask() {

		}

		@Override
		protected String doInBackground(Integer... params) {
			// loadModelData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			mReadCabinetDialog.dismiss();
			// ����SpinUi
			UpDateSpinUI();
			super.onPostExecute(result);
		}
	}

	public void UpDateSpinUI() {
		List<String> tList = Ak10Application.gArrCabSerieStrings;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, tList);
		adapter.setDropDownViewResource(R.layout.style_listoption);
		mSpin_CabinetSeries.setAdapter(adapter);
		mSpin_CabinetSeries.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String strTemp = Ak10Application.gArrCabSerieStrings
								.get(position);
						// �õ�����
						List<String> tList = Ak10Application.gMapModels
								.get(strTemp);

						ArrayAdapter<String> tAdapterModel = getAdapter(tList);
						tAdapterModel
								.setDropDownViewResource(R.layout.style_listoption);
						mSpin_CabinetModel.setAdapter(tAdapterModel);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		
		// Ĭ��ѡ��һ��,�ͺ�ѡ��
		if (tList.size() > 0) {
			List<String> tListModel = Ak10Application.gMapModels.get(tList.get(0));
			ArrayAdapter<String> adapterModel = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, tListModel);
			adapter.setDropDownViewResource(R.layout.style_listoption);

			mSpin_CabinetModel.setAdapter(adapterModel);	
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.leddisplay_cabinetadd);
		SerialPortControlBroadCast.SetCurrentContext(this);
		initView();
		// ����ϵͳ���� ��������Ҫʱ�����ʾ
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// �첽��������

		mReadCabinetDialog = new CustomProgressDialog(this,
				getString(R.string.text_dataload),
				getString(R.string.text_dataloading), false);
		mReadCabinetDialog.show();

		DataLoadTask dTask = new DataLoadTask();
		dTask.execute();

	}

	/*
	 * �����ݿ��м�������
	 */
	private void InitCbtFromDb() {

		ArrayList<CbtData> arrayListCbtDatas = CabinetDB.GetAllRecord(gLEDID);
		int nSize = arrayListCbtDatas.size();
		for (int i = 0; i < nSize; i++) {

			CbtData tCbtData = arrayListCbtDatas.get(i);

			int nID = tCbtData.Id;
			int noffsetx = tCbtData.offsetX;
			int noffsety = tCbtData.offsetY;
			int nWidth = tCbtData.width;
			int nHeight = tCbtData.height;

			CabinetViewObj tCbtViewObj = new CabinetViewObj();

			tCbtViewObj.setmBasicViewID(nID);
			tCbtViewObj.m_leftOrg = noffsetx;
			tCbtViewObj.m_topOrg = noffsety;
			tCbtViewObj.m_width = nWidth;
			tCbtViewObj.m_height = nHeight;
			tCbtViewObj.setStrTypeString(tCbtData.strModelType);
			m_CabinetAddCustomView.AddBasicView(tCbtViewObj);

		}

	}

	/**
	 * ���������ͺŲ���
	 */
	public static void loadModelData() {
		// ����ϵ������
		HashMap<String, ArrayList<String>> hashMap = DataAccessCabinetLibrary
				.loadModelMapsData();
		Ak10Application.gMapModels = hashMap;

	}

	private ArrayAdapter<String> getAdapter(List<String> tList) {
		ArrayAdapter<String> tAdapterModel = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, tList);
		return tAdapterModel;
	}

	@SuppressLint("CutPasteId")
	private void initView() {
		ledDisplayText = (TextView) findViewById(R.id.text_ledcabinet_setup);
		// ��ȡLED��ID ��
		gLEDID = Ak10Application.GetLedId();
		ledDisplayText.setText("LED" + gLEDID + ">");
		btn_Next = (TextView) findViewById(R.id.text_cabinetnext);
		btn_Cabinet_Add = (TextView) findViewById(R.id.text_cabinetadd);
		btn_Delete_cabinet = (TextView) findViewById(R.id.text_cabinetdel);
		btn_Cabinet_Back = (TextView) findViewById(R.id.btn_cbtback);
		btn_AddConfirm = (Button) findViewById(R.id.cabinet_add_confirm);
		btn_AddCancel = (Button) findViewById(R.id.cabinet_add_cancel);
		btnCabinet_ZoomIn = (TextView) findViewById(R.id.text_zoomin);
		btnCabinet_ZoomNormal = (TextView) findViewById(R.id.text_zoomnormal);
		btnCabinet_ZoomOut = (TextView) findViewById(R.id.text_zoomout);

		textCabinet_ShowAll = (TextView) findViewById(R.id.text_showall);

		textCabinet_Add = (TextView) findViewById(R.id.text_cabinetadd);
		textCabinet_Next = (TextView) findViewById(R.id.text_cabinetnext);
		textCabinet_Select = (TextView) findViewById(R.id.text_selectall);

		textCabinet_FullScreen = (TextView) findViewById(R.id.text_fullscreen);

		btn_SelectAll = (CheckBox) findViewById(R.id.cabinet_tool_selects);
		txtCabinet_ShowAll = (TextView) findViewById(R.id.text_showall);
		txtCabinet_FullScreen = (TextView) findViewById(R.id.text_fullscreen);
		mSpin_CabinetSeries = (Spinner) findViewById(R.id.spin_CabinetSeries);
		mSpin_CabinetModel = (Spinner) findViewById(R.id.spin_CabinetModel);

		layout_AddCabinet_Text = (RelativeLayout) findViewById(R.id.layout_text);
		layout_AddCabinet_Show = (RelativeLayout) findViewById(R.id.Rlayout_Row);
		layout_AddCabinet_Top = (RelativeLayout) findViewById(R.id.cabinet_top);
		layout_AddCabinet_Root = (RelativeLayout) findViewById(R.id.cabinetAdd_layout);
		layout_AddCabinet_Tool = (RelativeLayout) findViewById(R.id.cabinet_add_toolbar);

		edit_Row = (EditText) findViewById(R.id.edit_Row);
		edit_Column = (EditText) findViewById(R.id.edit_Column);
		edit_X = (EditText) findViewById(R.id.edit_X);
		edit_Y = (EditText) findViewById(R.id.edit_Y);

		textCabinet_Add.setOnClickListener(this);

		textCabinet_Next.setOnClickListener(this);
		textCabinet_Select.setOnClickListener(this);
		textCabinet_ShowAll.setOnClickListener(this);
		textCabinet_FullScreen.setOnClickListener(this);

		btn_Cabinet_Add.setOnClickListener(this);
		btn_Next.setOnClickListener(this);
		btn_Delete_cabinet.setOnClickListener(this);
		btn_Cabinet_Back.setOnClickListener(this);
		btn_AddConfirm.setOnClickListener(this);
		btn_AddCancel.setOnClickListener(this);
		btnCabinet_ZoomIn.setOnClickListener(this);
		btnCabinet_ZoomNormal.setOnClickListener(this);
		btnCabinet_ZoomOut.setOnClickListener(this);

		btn_SelectAll.setOnClickListener(this);
		txtCabinet_ShowAll.setOnClickListener(this);
		txtCabinet_FullScreen.setOnClickListener(this);

	}

	public void DeleteCbtFromUI() {
		ArrayList<CabinetViewObj> tCbtList = m_CabinetAddCustomView.m_SelectedCbtArrayList;

		// //////////////////////////////////
		int nSize = tCbtList.size();
		for (int i = 0; i < nSize; i++) {
			CabinetDB.DeleteDataById(tCbtList.get(i).getmBasicViewID(), gLEDID);
		}
		// //////////////////////////////////
		//
		ObjLog tObjLog = new ObjLog();

		tObjLog.setM_ActionMode(LOG_DELETE_GROUP);
		ArrayList<BasicViewObj> tArrayList = new ArrayList<BasicViewObj>();
		nSize = tCbtList.size();
		for (int i = 0; i < nSize; i++) {
			tArrayList.add(tCbtList.get(i));
		}
		tObjLog.setBasicViewObjArrFrom(tArrayList);
		tObjLog.setBasicViewObjArrTo(null);

		m_CabinetAddCustomView.m_BackForwardStack.UpdateCurOpStation(tObjLog);

		nSize = tCbtList.size();
		for (int i = 0; i < nSize; i++) {
			CabinetViewObj tViewObj = tCbtList.get(0);
			m_CabinetAddCustomView.DeleteBasicView(tViewObj);
			m_CabinetAddCustomView.m_SelectedCbtArrayList.remove(tViewObj);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.text_cabinetadd:
			layout_AddCabinet_Show.setVisibility(View.VISIBLE);
			break;
		// layout_AddCabinet_Show.setVisibility(View.VISIBLE);
		// break;
		case R.id.text_cabinetdel:

			int nCbtCnt = m_CabinetAddCustomView.m_SelectedCbtArrayList.size();
			if (nCbtCnt == 0) {
				Toast.makeText(getApplication(),
						R.string.text_cabinetdeletetip, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// �˴������жϣ������û��ѡ�� ����ʾҪѡ�в���ɾ��
			AlertDialog dlg = new AlertDialog.Builder(CabinetAddActivity.this)
					.setTitle(R.string.text_doingtip)
					.setMessage(R.string.text_deletecabinet)
					.setPositiveButton(R.string.control_confirm,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									DeleteCbtFromUI();
								}
							})
					.setNegativeButton(R.string.Cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
			dlg.show();
			break;
		case R.id.cabinet_add_confirm:
			int nID = GetCurMaxID() + 1;
			AddCabinetsToView(nID);
			layout_AddCabinet_Show.setVisibility(View.GONE);

			CustomToast.showToast(this, "", Toast.LENGTH_SHORT);

			break;
		case R.id.cabinet_add_cancel:
			layout_AddCabinet_Show.setVisibility(View.GONE);
			break;
		case R.id.btn_cbtback:
			this.finish();
			break;

		case R.id.cabinet_tool_selects:
		case R.id.text_selectall:
			bIsSelAll = !bIsSelAll;
			if (bIsSelAll) {
				m_CabinetAddCustomView.SelectAllCabinetViewObj();
			} else {
				m_CabinetAddCustomView.UnSelectAllCabinetViewObj();
			}
			
			m_CabinetAddCustomView.UpdateSelRect();
			
			break;

		case R.id.text_cabinetnext:

			int nCbtCntSel = m_CabinetAddCustomView.m_ArrayCabinetViewList
					.size();

			if (nCbtCntSel == 0) {
				Toast.makeText(getApplication(), R.string.text_cabinetaddtip,
						Toast.LENGTH_LONG).show();
			} else {
				// ������һ������
				Intent putIntent = new Intent(CabinetAddActivity.this,
						LedConstructActivity.class);
				startActivity(putIntent);
				finish();
			}

			break;

		case R.id.text_zoomin:
			m_CabinetAddCustomView.ZoomIn();
			break;

		case R.id.text_zoomnormal:
			m_CabinetAddCustomView.ZoomNormal();
			break;

		case R.id.text_zoomout:
			m_CabinetAddCustomView.ZoomOut();
			break;

		case R.id.text_showall:
			m_CabinetAddCustomView.FitToZoom();
			break;

		case R.id.text_fullscreen:
			HideLayoutView();
			ZoomFullButton();
			break;

		default:
			break;
		}
	}

	// ��ʾȫ��ʱ��Ҫ��ʾ�ķŴ���С��ť���˳�ȫ����ͼ��
	public void ZoomFullButton() {
		zoomButtonFull = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa = new RelativeLayout.LayoutParams(
				300, 80);
		zoomPa.setMargins(4, 4, 50, 100);
		zoomPa.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomButtonFull.setLayoutParams(zoomPa);
		zoomSmallShow = new RelativeLayout(this);
		RelativeLayout.LayoutParams zoomPa1 = new RelativeLayout.LayoutParams(
				50, 50);
		zoomPa1.setMargins(4, 10, 30, 100);
		zoomPa1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		zoomSmallShow.setLayoutParams(zoomPa1);
		mZoomControls = new ZoomControls(this);
		mZoomControls.setClickable(true);
		zoomButtonFull.addView(mZoomControls);

		zoomToSmall = new Button(this);
		zoomToSmall.setBackgroundResource(R.drawable.exit_fullscreen_nor);
		zoomToSmall.setClickable(true);
		zoomSmallShow.addView(zoomToSmall);

		layout_AddCabinet_Root.addView(zoomSmallShow);
		layout_AddCabinet_Root.addView(zoomButtonFull);
		zoomToSmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				layout_AddCabinet_Text.setVisibility(View.VISIBLE);
				layout_AddCabinet_Top.setVisibility(View.VISIBLE);
				layout_AddCabinet_Tool.setVisibility(View.VISIBLE);
				deleteZoomButton();
				System.out.println("��Ļ����¼�--------------------->");

			}
		});
		mZoomControls.setOnZoomInClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����Ŵ���Ļ
				m_CabinetAddCustomView.ZoomIn();
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
				m_CabinetAddCustomView.ZoomOut();
			}
		});

		setContentView(layout_AddCabinet_Root);

	}

	public void deleteZoomButton() {

		if (zoomButtonFull != null && zoomSmallShow != null) {
			layout_AddCabinet_Root.removeView(zoomButtonFull);
			layout_AddCabinet_Root.removeView(zoomSmallShow);
		}
	}

	public void HideLayoutView() {
		layout_AddCabinet_Text.setVisibility(View.GONE);
		layout_AddCabinet_Top.setVisibility(View.GONE);
		layout_AddCabinet_Show.setVisibility(View.GONE);
		layout_AddCabinet_Tool.setVisibility(View.GONE);
	}

	private int GetCurMaxID() {

		int nRet = 0;

		int nCbtCnt = m_CabinetAddCustomView.m_ArrayCabinetViewList.size();

		if (nCbtCnt == 0) {
			return 0;
		}

		int nSize = m_CabinetAddCustomView.m_ArrayCabinetViewList.size();
		for (int i = 0; i < nSize; i++) {

			CabinetViewObj viewObj = (CabinetViewObj) m_CabinetAddCustomView.m_ArrayCabinetViewList
					.get(i);

			int nID = viewObj.getmBasicViewID();

			if (nID > nRet) {
				nRet = nID;
			}
		}

		return nRet;

	}

	private int AddCabinetsToView(int tID) {

		if (edit_Row.getText().toString().isEmpty()
				|| edit_Column.getText().toString().isEmpty()) {
			Toast.makeText(this, R.string.text_cabinetrowcolumntip,
					Toast.LENGTH_SHORT).show();
			return -1;
		}
		rows = (Integer.parseInt(edit_Row.getText().toString()));
		columns = (Integer.parseInt(edit_Column.getText().toString()));
		// ��ȡcabinetlistѡ��ĵ�ǰ����
		int nGroupIndex = mSpin_CabinetSeries.getSelectedItemPosition();
		int nModelIndex = mSpin_CabinetModel.getSelectedItemPosition();

		if (nGroupIndex == -1 || nModelIndex == -1) {
			new AlertDialog.Builder(this).setTitle("��������")
					.setMessage("����ϵ�л����ͺ�Ϊ��").setPositiveButton("ȷ��", null)
					.show();

			return -1;
		}

		String strGroupNameString = Ak10Application.gArrCabSerieStrings
				.get(nGroupIndex);
		String strModelNameString = Ak10Application.gMapModels.get(
				strGroupNameString).get(nModelIndex);

		CabinetInformation tCabinetInformation = new CabinetInformation();
		DataAccessCabinetLibrary.getCabinetByname(strModelNameString,
				tCabinetInformation, 0);

		// ���Բ��������޷�ͨ��
		int wide = UtilFun.f2i(tCabinetInformation.getRtRect().getRight()
				- tCabinetInformation.getRtRect().getLeft());
		int height = UtilFun.f2i(tCabinetInformation.getRtRect().getBottom()
				- tCabinetInformation.getRtRect().getTop());

		// int wide =100;
		// int height=100;
		// �����ж������������겻��Ϊ��
		if (edit_X.getText().toString().isEmpty()
				|| edit_Y.getText().toString().isEmpty()) {
			Toast.makeText(this, R.string.text_cabinetrowxy, Toast.LENGTH_SHORT)
					.show();
			return -1;
		}
		// ��ȡ��ʼƫ�Ƶ�ֵ
		int nStartX = Integer.parseInt(edit_X.getText().toString());
		int nStartY = Integer.parseInt(edit_Y.getText().toString());

		ArrayList<BasicViewObj> tArrayListCbtTmp = new ArrayList<BasicViewObj>();

		// //////////////hh

		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {

				int x = i * wide + nStartX;
				int y = j * height + nStartY;

				CabinetViewObj tCbtObj = new CabinetViewObj();
				tCbtObj.setmBasicViewID(j * columns + i + tID);

				tCbtObj.m_leftOrg = x;
				tCbtObj.m_topOrg = y;
				tCbtObj.m_width = wide;
				tCbtObj.m_height = height;

				tCbtObj.setStrTypeString(strModelNameString);

				m_CabinetAddCustomView.AddBasicView(tCbtObj);

				// �������ݵ����ݿ�
				CbtData tCbtData = new CbtData();
				tCbtData.address = -1;
				tCbtData.addrshowid=-1;
				tCbtData.Id = j * columns + i + tID;
				tCbtData.interfaceID = -1;
				tCbtData.offsetX = x;
				tCbtData.offsetY = y;
				tCbtData.strModelType = strModelNameString;
				tCbtData.width = wide;
				tCbtData.height = height;
				tCbtData.LEDid = gLEDID;
				CabinetDB.AddData(tCbtData);

				tArrayListCbtTmp.add((BasicViewObj) tCbtObj);

			}
		}

		ObjLog tlog = new ObjLog();
		tlog.setBasicViewObjArrFrom(null);
		tlog.setBasicViewObjArrTo(tArrayListCbtTmp);
		tlog.setM_ActionMode(LOG_CREATE_GROUP);

		m_CabinetAddCustomView.m_BackForwardStack.UpdateCurOpStation(tlog);

		return 0;

	}

	@Override
	protected void onResume() {

		try {
			m_CabinetAddCustomView = (CabinetAddCustomView) findViewById(R.id.cabinetaddcustomview);
			InitCbtFromDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}
}