package com.szaoto.ak10.leddisplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szaoto.ak10.ExternalStorageService;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.GroupChannel.GroupChannelActivity;
import com.szaoto.ak10.configuration.CardInformation;
import com.szaoto.ak10.configuration.CardInformationList;
import com.szaoto.ak10.custom.CustomProgressDialog;
import com.szaoto.ak10.sqlitedata.CardInfoDB;

/**
 * ������CardFragment ���ܣ�Ӳ���忨��������,���Ӳ���忨״̬������ʾ �����ߣ�zhangsj �������ڣ�2014��7��10��
 */
public class CardFragment extends Fragment implements OnClickListener,
		IInfoChangeObserver {

	CustomProgressDialog WaitDiag;

	private LedConstructActivity mLedDispalyConfigActivity;
	PopupWindow popupWindow;

	ImageView btn_Slot1; // ϵͳ��
	ImageView btn_Slot2; // �ɼ���
	ImageView btn_Slot3; // ���Ϳ�1
	ImageView btn_Slot4; // ���Ϳ�2
	ImageView btn_Slot5; // ���Ϳ�3
	ImageView btn_Slot6; // ���Ϳ�4
	ImageView btn_Slot7; // ���Ϳ�5
	ImageView btn_Slot8; // ���Ϳ�6
	// 8����۵Ŀ�����
	// 1��ϵͳ����2���ɼ�����3�����Ϳ�
	private CardInformation[] cardInformations = new CardInformation[8];
	private TextView btn_Home;
	private TextView btn_Back;
	private TextView btn_LearnMore;
	private PopupWindow popupwindow;
	private Button btnGroupChannel;
	private Button btnSendConnParam;
	private Button btnSendParam;

	ProgressDialog progSaveCurDiag;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.card_fragment, null);
		ExternalStorageService.observers.add(this);
		try {
			LoadData(); // ���ذ忨����
			SaveCardInfoToDB();
			initView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	// ���忨��Ϣ���ص����ݿ���

	public void SaveCardInfoToDB() {
		// ��Ϊ����忨��Ϣ��ʵʱ��ȡ�ģ�����ÿ�����ݿ��е����ݱ��������
		CardInfoDB.DeleteAllData();
		for (int i = 0; i < cardInformations.length; i++) {

			if (cardInformations[i].getnSlotID() == -1) {
				continue;
			}

			CardInfoDB.AddData(cardInformations[i]);
		}
	}

	private void LoadData() {

		CardInformationList.GetCardInformationList();

		cardInformations = CardInformationList.GetCardInformations();
	}

	public void setActivity(LedConstructActivity mainActivity) {
		mLedDispalyConfigActivity = mainActivity;
	}

	private void initView(View view) {

		btn_Home = (TextView) view.findViewById(R.id.text_toolmain);
		btn_Back = (TextView) view.findViewById(R.id.text_toolback);
		btn_LearnMore = (TextView) view.findViewById(R.id.text_toolmore);
		btn_Home.setOnClickListener(this);
		btn_Back.setOnClickListener(this);
		btn_LearnMore.setOnClickListener(this);

		btn_Slot1 = (ImageView) view.findViewById(R.id.btn_Slot1);
		btn_Slot2 = (ImageView) view.findViewById(R.id.btn_Slot2);
		btn_Slot3 = (ImageView) view.findViewById(R.id.btn_Slot3);
		btn_Slot4 = (ImageView) view.findViewById(R.id.btn_Slot4);
		btn_Slot5 = (ImageView) view.findViewById(R.id.btn_Slot5);
		btn_Slot6 = (ImageView) view.findViewById(R.id.btn_Slot6);
		btn_Slot7 = (ImageView) view.findViewById(R.id.btn_Slot7);
		btn_Slot8 = (ImageView) view.findViewById(R.id.btn_Slot8);

		btn_Slot1.setOnClickListener(this);
		btn_Slot2.setOnClickListener(this);
		btn_Slot3.setOnClickListener(this);
		btn_Slot4.setOnClickListener(this);
		btn_Slot5.setOnClickListener(this);
		btn_Slot6.setOnClickListener(this);
		btn_Slot7.setOnClickListener(this);
		btn_Slot8.setOnClickListener(this);

		btn_Slot1.setVisibility(View.INVISIBLE);
		btn_Slot2.setVisibility(View.INVISIBLE);
		btn_Slot3.setVisibility(View.INVISIBLE);
		btn_Slot4.setVisibility(View.INVISIBLE);
		btn_Slot5.setVisibility(View.INVISIBLE);
		btn_Slot6.setVisibility(View.INVISIBLE);
		btn_Slot7.setVisibility(View.INVISIBLE);
		btn_Slot8.setVisibility(View.INVISIBLE);

		for (int i = 0; i < cardInformations.length; i++) {
			if (-1 != cardInformations[i].getnSlotID()) {

				short tCardType = cardInformations[i].getnType();
				switch (i) {
				case 0:
					SetDrawabelByType(btn_Slot1, tCardType);
					break;
				case 1:
					SetDrawabelByType(btn_Slot2, tCardType);
					break;
				case 2:
					SetDrawabelByType(btn_Slot3, tCardType);
					break;
				case 3:
					SetDrawabelByType(btn_Slot4, tCardType);
					break;
				case 4:
					SetDrawabelByType(btn_Slot5, tCardType);
					break;
				case 5:
					SetDrawabelByType(btn_Slot6, tCardType);
					break;
				case 6:
					SetDrawabelByType(btn_Slot7, tCardType);
					break;
				case 7:
					SetDrawabelByType(btn_Slot8, tCardType);
					break;
				default:
					break;
				}
			}
		}
	}

	private void SetDrawabelByType(ImageView btn_Slot, short nType) {

		switch (nType) {
		case 1:
			btn_Slot.setImageResource(R.drawable.selector_sys);
			break;
		case 2:
			btn_Slot.setImageResource(R.drawable.selector_acq);
			break;
		case 3:
			btn_Slot.setImageResource(R.drawable.selector_snd);
			break;
		default:
			break;
		}

		btn_Slot.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		ExternalStorageService.observers.remove(this);
		super.onDestroy();
	}

	private void StartCardDialog(int Index) {

		byte[] MACaddress = cardInformations[Index - 1].getUcMACAddress();

		switch (cardInformations[Index - 1].getnType()) {
		case 1:
			mLedDispalyConfigActivity.deleteViews();
			mLedDispalyConfigActivity.AddSysViews(MACaddress);
			break;
		case 2:
			mLedDispalyConfigActivity.deleteViews();
			mLedDispalyConfigActivity.AddAcqViews(MACaddress);
			break;
		case 3:
			mLedDispalyConfigActivity.deleteViews();
			try {
				mLedDispalyConfigActivity.AddInterfaceView(MACaddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_Slot1:
			StartCardDialog(1);
			break;
		case R.id.btn_Slot2:
			StartCardDialog(2);
			break;
		case R.id.btn_Slot3:
			StartCardDialog(3);
			break;
		case R.id.btn_Slot4:
			StartCardDialog(4);
			break;
		case R.id.btn_Slot5:
			StartCardDialog(5);
			break;
		case R.id.btn_Slot6:
			StartCardDialog(6);
			break;
		case R.id.btn_Slot7:
			StartCardDialog(7);
			break;
		case R.id.btn_Slot8:
			StartCardDialog(8);
			break;
		case R.id.text_toolmain:
			mLedDispalyConfigActivity.finish();
			mLedDispalyConfigActivity.startActivity(new Intent(
					mLedDispalyConfigActivity, HomePageActivity.class));
			break;
		case R.id.text_toolback:

			mLedDispalyConfigActivity.finish();
			mLedDispalyConfigActivity.startActivity(new Intent(
					mLedDispalyConfigActivity, CabinetAddActivity.class));

			break;
		case R.id.btn_group_channel:
			mLedDispalyConfigActivity.finish();
			mLedDispalyConfigActivity.startActivity(new Intent(
					mLedDispalyConfigActivity, GroupChannelActivity.class));
			break;

		case R.id.btn_SendAllConnParmas:

			// ��������ͼ
			// mLedDispalyConfigActivity.m_LedConfigCustomView.SendConnChart();

			new SendConnParamTask().execute();
			
			popupwindow.dismiss();

			break;
		case R.id.btn_SendAllParmas:
			
			// ��������ͼ
			// mLedDispalyConfigActivity.m_LedConfigCustomView.SendConnChart();
			
			new SendAllParamTask().execute();
			
			popupwindow.dismiss();
			
			break;

		case R.id.text_toolmore:
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				initPopupWindow();
				popupwindow.showAsDropDown(v, 40, 15);
			}
			break;
		default:
			break;
		}
	}

	// �첽����
	// ��ȡ�ҵĳ�����Ϣ
	class SendConnParamTask extends AsyncTask<Void, Void, Void> {
		public SendConnParamTask() {
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			WaitDiag  = new CustomProgressDialog(getActivity(),
					getString(R.string.text_dataload),
					getString(R.string.text_dataloading), false);
			WaitDiag.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			mLedDispalyConfigActivity.m_LedConfigCustomView.SendConnChart();
			return null;
		}

		@Override
		protected void onPostExecute(Void ErrorCode) {
			
			WaitDiag.dismiss();
			super.onPostExecute(ErrorCode);
		}

	}

	// 
    class SendAllParamTask extends AsyncTask<Void, Void, Void> {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				WaitDiag  = new CustomProgressDialog(getActivity(),
						getString(R.string.text_dataload),
						getString(R.string.text_dataloading), false);
				WaitDiag.show();
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				mLedDispalyConfigActivity.m_LedConfigCustomView.SendAllConnParam();
				return null;
			}

			@Override
			protected void onPostExecute(Void ErrorCode) {
				
				WaitDiag.dismiss();
				super.onPostExecute(ErrorCode);
			}

	}
	
	private void initPopupWindow() {
		// // ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		View customView = mLedDispalyConfigActivity.getLayoutInflater()
				.inflate(R.layout.drop_list_menu_config, null, false);
		// ����PopupWindowʵ��,200,150�ֱ��ǿ��Ⱥ͸߶�
		popupwindow = new PopupWindow(customView, 200, 400);
		// ���ö���Ч�� [R.style.AnimationFade ���Լ����ȶ���õ�]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// �Զ���view���Ӵ����¼�
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});

		/** ���������ʵ���Զ�����ͼ�Ĺ��� */

		btnGroupChannel = (Button) customView
				.findViewById(R.id.btn_group_channel);

		btnGroupChannel.setOnClickListener(this);

		btnSendConnParam = (Button) customView
				.findViewById(R.id.btn_SendAllConnParmas);

		btnSendConnParam.setOnClickListener(this);
		
		btnSendParam = (Button) customView
				.findViewById(R.id.btn_SendAllParmas);
		
		btnSendParam.setOnClickListener(this);

	}

	// ��д�ӿ�IInfoChangeObserver�еķ���
	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		Log.d("info", "external storage path = " + xParam1);
		Log.d("info", "external storage value = " + xParam2);
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}

}