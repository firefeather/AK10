package com.szaoto.ak10.leddisplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.R;

/**
 * ������ToolFragment ���ܣ����������� ���ߣ� zhangsj �������ڣ�2014/07/02 �޸���,�޸����ڣ��޸�����
 * 
 */
public class ToolFragment extends Fragment implements OnClickListener {
	/*
	 * public ToolFragment(List<Interface> listinterface) { // TODO
	 * Auto-generated constructor stub // lastListInterfaces = listinterface; }
	 */

	private LedConstructActivity mLedConstructActivity;
	/* ���������� */

	// TextView txt_undo; // ����
	// TextView txt_redo; // �ָ�
	TextView txt_lock; // ����
	TextView txt_fullscreen; // ȫ��
	TextView txt_zoom_in; // �Ŵ�
	TextView txt_zoom_out; // ��С

	TextView txt_ScaleVideo;
	
	CheckBox chk_channel;
	CheckBox chk_interface;
	CheckBox chk_lable;
	CheckBox chk_cabinet;
	TextView txt_showAll;
	TextView txt_zoom_normal;
	TextView txt_tool_lable;
	TextView txt_ToConnect;
	PopupWindow popupWindowLable;
	TextView txt_Delete;
	TextView txt_Setting;
	Button btn_ToLeft;
	Button btn_ToRight;
	private LinearLayout layout_View;
	private boolean isEdit = true;
	private boolean isShowLable = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tool_fragment, null);
		try {
			initView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	private void initView(View view) {

		// ��������ť����

		// txt_undo = (TextView) view.findViewById(R.id.btn_tool_undo);
		// txt_redo = (TextView) view.findViewById(R.id.btn_tool_redo);
		txt_lock = (TextView) view.findViewById(R.id.texttool_lock);
		txt_ToConnect = (TextView) view.findViewById(R.id.texttoolconnect);
		layout_View = (LinearLayout) view.findViewById(R.id.layout_tool_del); // ��ת,ɾ���������鰴ť
		layout_View.setVisibility(View.INVISIBLE);
		// btn_ToLeft = (Button) view.findViewById(R.id.btn_tool_moveleft);
		// btn_ToRight = (Button) view.findViewById(R.id.btn_tool_moveright);

		txt_fullscreen = (TextView) view.findViewById(R.id.texttool_full);
		txt_showAll = (TextView) view.findViewById(R.id.showall);
		txt_zoom_in = (TextView) view.findViewById(R.id.textZoomin);
		txt_zoom_out = (TextView) view.findViewById(R.id.textZoomout);
		txt_zoom_normal = (TextView) view.findViewById(R.id.textZoomnormal);
		txt_tool_lable = (TextView) view.findViewById(R.id.texttool_show);
		txt_Delete = (TextView) view.findViewById(R.id.texttooldelete);
		txt_Setting = (TextView) view.findViewById(R.id.texttoolsetting);
		txt_ScaleVideo =  (TextView) view.findViewById(R.id.texttoolvideoscale);
		//txt_ScaleVideo.setVisibility(8);//������ʱ����

		txt_ToConnect.setOnClickListener(this);

		// btn_ToLeft.setOnClickListener(this);
		// btn_ToRight.setOnClickListener(this);

		// txt_undo.setOnClickListener(this);
		// txt_redo.setOnClickListener(this);
		txt_lock.setOnClickListener(this);

		txt_zoom_in.setOnClickListener(this);
		txt_zoom_out.setOnClickListener(this);
		txt_zoom_normal.setOnClickListener(this);
		txt_fullscreen.setOnClickListener(this);
		txt_showAll.setOnClickListener(this);
		txt_tool_lable.setOnClickListener(this);
		txt_Delete.setOnClickListener(this);
		txt_Setting.setOnClickListener(this);
		txt_ScaleVideo.setOnClickListener(this);

		// txt_redo.setEnabled(false);
		// txt_undo.setEnabled(false);

		/*
		 * chk_channel.setChecked(LedDisplayDataLayer.getInstance().
		 * getgLedDisplayConfigActAdapter().bChannel);
		 * chk_lable.setChecked(LedDisplayDataLayer
		 * .getInstance().getgLedDisplayConfigActAdapter().blabel);
		 * chk_interface.setChecked(LedDisplayDataLayer.getInstance().
		 * getgLedDisplayConfigActAdapter().bInterFace);
		 * chk_cabinet.setChecked(LedDisplayDataLayer
		 * .getInstance().getgLedDisplayConfigActAdapter().bCabinet);
		 */
	}

	public void ShowGoTo(boolean bShow, int nObjStyle) {

		if (bShow) {
			layout_View.setVisibility(View.VISIBLE);

			if (nObjStyle == 1) {// interface
				//

			} else if (nObjStyle == 2) {
				// ��ת���
			}

		} else {
			layout_View.setVisibility(View.INVISIBLE);
		}

	}

	// ��LedDisplayConfigActivity�������
	public void setActivity(LedConstructActivity mainActivity) {
		// TODO Auto-generated method stub
		mLedConstructActivity = mainActivity;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.texttoolconnect: //
			// ��ת������ͼ
			mLedConstructActivity.m_LedConfigCustomView.GotoLineActivity();
			break;
		case R.id.texttool_show: // �Ƿ���ʾ��ǩ
			if (isShowLable) {
				mLedConstructActivity.LableShow();
				isShowLable = false;
			} else {
				mLedConstructActivity.HideLable();
				isShowLable = true;
			}
			break;
		case R.id.texttoolsetting:
			mLedConstructActivity.m_LedConfigCustomView.GotoSettingsAct(mLedConstructActivity);
			break;		
		case R.id.texttoolvideoscale:			
			mLedConstructActivity.m_LedConfigCustomView.GotoScaleSetActivity(mLedConstructActivity);
			break;
		case R.id.texttooldelete: // ɾ�� view
			if (mLedConstructActivity.m_LedConfigCustomView.GetCurSelBasicView() == null) {
				return;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(mLedConstructActivity);
			builder.setTitle(R.string.text_del_sure);
			builder.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int whichButton) {
							mLedConstructActivity.m_LedConfigCustomView.DeleteCurSelView();
						}
					});
			builder.setNegativeButton(R.string.Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			builder.create().show();
			break;
		//
		// case R.id.btn_tool_undo: // ������һ������
		// //��ȡ��һ���Ķ���״̬
		// txt_redo.setEnabled(true);
		// mLedConstructActivity.m_LedConfigCustomView.UndoOperation();
		//
		// int tID =
		// mLedConstructActivity.m_LedConfigCustomView.m_BackForwardStack.nCurOpId;
		// if (tID<0) {
		// txt_undo.setEnabled(false);
		// }
		//
		// break;
		// case R.id.btn_tool_redo: // �ָ���һ������
		// //��ȡ��һ���Ķ���״̬
		// txt_undo.setEnabled(true);
		// mLedConstructActivity.m_LedConfigCustomView.RedoOperation();
		//
		// int tRedoID =
		// mLedConstructActivity.m_LedConfigCustomView.m_BackForwardStack.nCurOpId;
		// if
		// (tRedoID>=mLedConstructActivity.m_LedConfigCustomView.m_BackForwardStack.m_arrBackAndForwardStack.size()-1)
		// {
		// txt_redo.setEnabled(false);
		// }
		//
		// break;

		case R.id.textZoomin: // �Ŵ�
			((LedConstructActivity) getActivity()).m_LedConfigCustomView.ZoomIn();
			break;
		case R.id.textZoomout: // ��С
			((LedConstructActivity) getActivity()).m_LedConfigCustomView.ZoomOut();
			break;
		case R.id.textZoomnormal: //
			((LedConstructActivity) getActivity()).m_LedConfigCustomView.ZoomNormal();
			break;
		case R.id.texttool_full:// ȫ��
			((LedConstructActivity) getActivity()).HideView();
			((LedConstructActivity) getActivity()).ZoomButton();
			break;
		case R.id.showall:// ��ʾȫ��
			mLedConstructActivity.m_LedConfigCustomView.FitToZoom();
			break;
		case R.id.texttool_lock: // ����
			Lock();
			break;
		default:
			break;
		}

	}

	private void Lock() {

		if (isEdit) { // ���ɱ༭״̬
			Drawable tDrawable = getResources().getDrawable(R.drawable.unlockpress);
			txt_lock.setCompoundDrawablesWithIntrinsicBounds(null, tDrawable, null, null);

			isEdit = false;
			((LedConstructActivity) getActivity()).m_LedConfigCustomView.bLockScreen = true;
			// Toast.makeText(mLedConstructActivity, R.string.text_lock_ui,
			// Toast.LENGTH_SHORT).show();
		} else {// �ɱ༭״̬
			Drawable tDrawable = getResources().getDrawable(R.drawable.unlock);
			txt_lock.setCompoundDrawablesWithIntrinsicBounds(null, tDrawable, null, null);
			((LedConstructActivity) getActivity()).m_LedConfigCustomView.bLockScreen = false;
			// Toast.makeText(mLedConstructActivity, R.string.text_unlock_ui,
			// Toast.LENGTH_SHORT).show();
			isEdit = true;
		}
	}
}