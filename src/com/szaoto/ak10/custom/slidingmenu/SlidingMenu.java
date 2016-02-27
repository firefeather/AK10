package com.szaoto.ak10.custom.slidingmenu;

/*
 * ����SlidingMenu
 * ���� liangdb
 * ��Ҫ���� ������ �˵�
 * ��������2013��11��11��
 * �޸��ߣ��޸����ڣ��޸�����
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.szaoto.ak10.custom.slidingmenu.SlidingView.OnScrollCloseListener;
import com.szaoto.ak10.custom.slidingmenu.SlidingView.OnScrollOpenListener;

public class SlidingMenu extends RelativeLayout {

	private SlidingView mSlidingView;
	private View mMenuView;
	private View mDetailView;
	// �м�ҳ�汣�����
	private int alignScreenWidth;

	public SlidingMenu(Context context) {
		super(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addViews(View left, View center, View right) {
		setLeftView(left);
		setRightView(right);
		setCenterView(center);
	}

	public void setAlignScreenWidth(int alignScreenWidth) {
		this.alignScreenWidth = alignScreenWidth;
	}

	public void setLeftView(View view) {
		LayoutParams behindParams = new LayoutParams(alignScreenWidth,
				LayoutParams.FILL_PARENT);
		addView(view, behindParams);
		mMenuView = view;
	}

	public void setRightView(View view) {
		LayoutParams behindParams = new LayoutParams(alignScreenWidth,
				LayoutParams.FILL_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(view, behindParams);
		mDetailView = view;
	}

	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mSlidingView = new SlidingView(getContext());
		addView(mSlidingView, aboveParams);
		mSlidingView.setView(view);
		mSlidingView.invalidate();
		mSlidingView.setMenuView(mMenuView);
		mSlidingView.setDetailView(mDetailView);
	}

	public void showLeftView() {
		mSlidingView.showLeftView();
	}

	public void showRightView() {
		mSlidingView.showRightView();
	}

	public void showCenterView() {
		mSlidingView.showCenterView();
	}

	public void setOnScrollOpenListener(OnScrollOpenListener onScrollEndListener) {
		mSlidingView.setOnScrollOpenListener(onScrollEndListener);
	}

	public void setOnScrollCloseListener(
			OnScrollCloseListener mOnScrollCloseListener) {
		mSlidingView.setOnScrollCloseListener(mOnScrollCloseListener);
	}
}
