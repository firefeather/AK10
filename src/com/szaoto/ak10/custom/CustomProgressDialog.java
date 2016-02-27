package com.szaoto.ak10.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.szaoto.ak10.R;

/******
 *  类名CustomProgressBar
 * 作者 Zhangsj
 * 主要功能 显示数据加载进度
 * 创建日期2015年1月19日
 * 修改者，修改日期，修改内容
 *
 * 
 */

public class CustomProgressDialog extends AlertDialog {
	private int mProgressStyle;
	private boolean mHasStarted;
	private CustomProgressDialog mProgress;
	private String mTitle;
	private String mMessageString;
	private Handler mViewUpdateHandler = new Handler();
	private TextView mTextTitleView; //进度信息标题
	private TextView mMessageTextView; //进度显示信息
	public static final int STYLE_SPINNER = 0;
	public static final int STYLE_HORIZONTAL = 1;
	public CustomProgressDialog(Context context) {
		super(context);
	}
  
	public void SetTextMsg(String strMsg)
	{
		mMessageTextView.setText(strMsg);
	}
	
	public CustomProgressDialog(Context context, String mTitle, String message,boolean cancel) {
		super(context);
		this.mTitle = mTitle;
		this.mMessageString=message;
		setCanceledOnTouchOutside(cancel);
	}

	public CustomProgressDialog getmProgress() {
		return mProgress;
	}
   @Override
   public void setCanceledOnTouchOutside(boolean cancel) {
	super.setCanceledOnTouchOutside(cancel);
	}
	public void setmProgress(CustomProgressDialog mProgress) {
		this.mProgress = mProgress;
	}
   
	public void setProgressStyle(int styleSpinner) {
		mProgressStyle = styleSpinner;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.progressbardialog);
		
      mTextTitleView =(TextView) findViewById(R.id.text_title);
      mMessageTextView =(TextView) findViewById(R.id.textViewPathTitile);
      mTextTitleView.setText(mTitle);
      mMessageTextView.setText(mMessageString);
	}
 


	public void setProgress(int value) {
		if (mHasStarted) {
			mProgress.setProgress(value);
			onProgressChanged();
		} else {
		}

	}
	
//	ProgressDialog
	private void onProgressChanged() {
		if (mProgressStyle == STYLE_HORIZONTAL) {
			if (mViewUpdateHandler!=null && !mViewUpdateHandler.hasMessages(0)) {
				mViewUpdateHandler.sendEmptyMessage(0);
			}
		}
	}
}



