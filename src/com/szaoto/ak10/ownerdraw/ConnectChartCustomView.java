package com.szaoto.ak10.ownerdraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.datacomm.ConnChartComm;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.util.UtilFun;

public class ConnectChartCustomView extends View {

	Paint m_Paint;
	PointF m_PrevFPts = new PointF();
	PointF m_DownFPts = new PointF();
	PointF m_TouchDownPts = new PointF();
	boolean bClick = false;

	// 定义CustomView中的对象列表,相对坐标为本View
	ArrayList<BasicViewObj> m_ViewObj = null;
	OnBasicViewChangeListener m_BasicViewChangeListener;

	private boolean bShowConnectionLine = true;

	RectF m_MoveRectF = new RectF();

	public PointF m_pointInterF = new PointF();

	// 相对于LedDisplay的位置
	/**/
	int gTouchMode = 0;
	private PointF mid = new PointF();
	private float oldDistance;
	private static final int NONE = 0;
	private static final int MOVE = 1;
	private static final int ZOOM = 2;
	private static final int SCROLL = 3;
	public float gFactor = 1.0f;
	public float gTempFactor = 1.0f;

	public boolean bNeedInitAddressLineChart;

	// 经过连线图排序后的箱体
	public ArrayList<CabinetViewObj> gAddressedCbtList = new ArrayList<CabinetViewObj>();
	private ArrayList<CabinetViewObj> listinitialcabinetsDefines = new ArrayList<CabinetViewObj>();

	// 编址方式
	private int mStartAddressNum = 1;
	private int mType = 0;

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	private int mMode = 1;

	// ////////////////////////////////////////

	public BackForwardStack m_BackForwardStack = new BackForwardStack();
	// LedDisplay相对于View的坐标
	private PointF m_ViewPortPosF = new PointF();
	// 相对LED
	public ArrayList<CabinetViewObj> m_ArrayCabinetViewList = new ArrayList<CabinetViewObj>();

	// 选择的View对象
	// private BasicViewObj m_SelBasicView=null;

	// 比例因子
	public float m_Factor = 1.0f;
	final private int SPACE = 20;

	public ArrayList<CabinetViewObj> m_SelectedCbtArrayList = new ArrayList<CabinetViewObj>();

	// 当前选择的BaiscView
	// public ArrayList<CabinetViewObj> m_ArraySelBasicView = new
	// ArrayList<CabinetViewObj>();

	public ConnectChartCustomView(Context context) {
		super(context);
		m_Paint = new Paint();
		InitLedScreenViewHolder();
		this.setClickable(true);
	}

	public ConnectChartCustomView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		m_Paint = new Paint();
		InitLedScreenViewHolder();
		this.setClickable(true);
	}

	public void UpdateSelRect() {
		float xMax = 0;
		float yMax = 0;
		float xMin = 0;
		float yMin = 0;

		if (m_SelectedCbtArrayList.size() == 1) {
			CabinetViewObj tCbtObj = m_SelectedCbtArrayList.get(0);
			xMax = tCbtObj.m_leftCustomView + tCbtObj.m_WidthZoomed;
			xMin = tCbtObj.m_leftCustomView;
			yMax = tCbtObj.m_topCustomView + tCbtObj.m_HeightZoomed;
			yMin = tCbtObj.m_topCustomView;

		} else {

			for (int i = 0; i < m_SelectedCbtArrayList.size(); i++) {
				CabinetViewObj tCbtObj = m_SelectedCbtArrayList.get(i);

				if (i == 0) {
					xMax = tCbtObj.m_leftCustomView + tCbtObj.m_WidthZoomed;
					xMin = tCbtObj.m_leftCustomView;
					yMax = tCbtObj.m_topCustomView + tCbtObj.m_HeightZoomed;
					yMin = tCbtObj.m_topCustomView;
				} else {
					if (xMax < tCbtObj.m_leftCustomView + tCbtObj.m_WidthZoomed) {
						xMax = tCbtObj.m_leftCustomView + tCbtObj.m_WidthZoomed;
					}
					if (xMin > tCbtObj.m_leftCustomView) {
						xMin = tCbtObj.m_leftCustomView;
					}

					if (yMax < tCbtObj.m_topCustomView + tCbtObj.m_HeightZoomed) {
						yMax = tCbtObj.m_topCustomView + tCbtObj.m_HeightZoomed;
					}
					if (yMin > tCbtObj.m_topCustomView) {
						yMin = tCbtObj.m_topCustomView;
					}
				}

			}
		}

		m_MoveRectF.left = xMin;
		m_MoveRectF.top = yMin;
		m_MoveRectF.right = xMax;
		m_MoveRectF.bottom = yMax;

		for (int i = 0; i < m_SelectedCbtArrayList.size(); i++) {
			CabinetViewObj tCbtObj = m_SelectedCbtArrayList.get(i);
			tCbtObj.setxMoveRela(tCbtObj.m_leftCustomView - m_MoveRectF.left);
			tCbtObj.setyMoveRela(tCbtObj.m_topCustomView - m_MoveRectF.top);
		}

	}

	public ConnectChartCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_Paint = new Paint();
		InitLedScreenViewHolder();
		this.setClickable(true);
	}

	public void SelectAllCabinetViewObj() {

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			m_ArrayCabinetViewList.get(i).setSel(true);
			CabinetViewObj tCabinetViewObj = m_ArrayCabinetViewList.get(i);
			m_SelectedCbtArrayList.add(tCabinetViewObj);
		}

		invalidate();
	}

	public void UnSelectAllCabinetViewObj() {

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			m_ArrayCabinetViewList.get(i).setSel(false);
			m_SelectedCbtArrayList.remove(m_ArrayCabinetViewList.get(i));
		}

		invalidate();

	}

	public void ZoomIn() {
		m_Factor += 0.1;
		ZoomViews(m_Factor);
	}

	public void ZoomOut() {
		m_Factor -= 0.1;
		ZoomViews(m_Factor);
	}

	public void FitToZoom() {
		//

		float zoomx;
		float zoomy;

		float leftMin = m_ArrayCabinetViewList.get(0).m_leftOrg;
		float rightMax = m_ArrayCabinetViewList.get(0).m_leftOrg
				+ m_ArrayCabinetViewList.get(0).m_width;
		float topMin = m_ArrayCabinetViewList.get(0).m_topOrg;
		float bottomMax = m_ArrayCabinetViewList.get(0).m_topOrg
				+ m_ArrayCabinetViewList.get(0).m_height;

		ArrayList<PointF> arrPoint = new ArrayList<PointF>();
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			CabinetViewObj tCabinetViewObj = (CabinetViewObj) m_ArrayCabinetViewList
					.get(i);

			PointF point1 = new PointF(tCabinetViewObj.m_leftOrg,
					tCabinetViewObj.m_topOrg);
			PointF point2 = new PointF(tCabinetViewObj.m_leftOrg
					+ tCabinetViewObj.m_width, tCabinetViewObj.m_topOrg
					+ tCabinetViewObj.m_height);
			arrPoint.add(point1);
			arrPoint.add(point2);

			if (leftMin >= point1.x) {
				leftMin = point1.x;
			}
			if (topMin >= point1.y) {
				topMin = point1.y;
			}
			if (rightMax <= point2.x) {
				rightMax = point2.x;
			}
			if (bottomMax <= point2.y) {
				bottomMax = point2.y;
			}
		}

		System.out.println("Y方向的最小值是:" + topMin);
		System.out.println("Y方向的最大值是:" + bottomMax);
		System.out.println("x方向的最小值是:" + leftMin);
		System.out.println("x方向的最大值是:" + rightMax);
		zoomx = (float) (1.00f * 1280 / (rightMax - leftMin));
		zoomy = (float) (1.00f * 560 / (bottomMax - topMin));

		System.out.println("x方向的比例是:" + zoomx);
		System.out.println("y方向的比例是:" + zoomy);

		float gZoomFactor;

		if (zoomx > zoomy) {
			gZoomFactor = zoomy;
		} else {
			gZoomFactor = zoomx;
		}
		// m_Factor=(float) (m_Factor*gZoomFactor);
		/******* 聚焦对象 *****/
		m_ViewPortPosF.x = leftMin;
		m_ViewPortPosF.y = topMin;
		float tViewPortX = m_Factor * m_ViewPortPosF.x;
		float tViewPortY = m_Factor * m_ViewPortPosF.y;
		setM_ViewPortPosF(new PointF(tViewPortX, tViewPortY));
		/*****************/
		ZoomViews(gZoomFactor);

	}

	public void ZoomNormal() {
		m_Factor = 1;
		ZoomViews(m_Factor);
	}

	// 初始化数据
	private void InitLedScreenViewHolder() {

		m_ViewPortPosF = new PointF();

		// 初始是重合的
		m_ViewPortPosF.x = 0;
		m_ViewPortPosF.y = 0;

		setM_ViewPortPosF(m_ViewPortPosF);

	}

	// 新添加BasicView,这个坐标是相对于LedDisplay的
	public void AddBasicView(BasicViewObj tBasicView) {

		// 初始状态
		AddView(tBasicView);
		// 添加BasicView对象改变的方法
		// m_BasicViewChangeListener.OnBasicViewChange(tBasicView);
		invalidate();
	}

	public void SetOnBasicViewChange(
			OnBasicViewChangeListener tBasicViewChangeListenr) {
		m_BasicViewChangeListener = tBasicViewChangeListenr;
	}

	// 删去BasicView
	public void DeleteBasicView(BasicViewObj tBasicView) {
		if (tBasicView instanceof CabinetViewObj) {
			m_ArrayCabinetViewList.remove(tBasicView);
		}

		UpdateSelRect();
		invalidate();
	}

	// 查找选择的对象
	private BasicViewObj FindBasicViewByDownPoint(PointF tDownPointF) {
		// 这个tDownPoint是针对View的坐标

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			RectF tRectF = new RectF();

			tRectF.left = m_ArrayCabinetViewList.get(i).m_leftCustomView;
			tRectF.top = m_ArrayCabinetViewList.get(i).m_topCustomView;
			tRectF.right = m_ArrayCabinetViewList.get(i).m_leftCustomView
					+ m_ArrayCabinetViewList.get(i).m_WidthZoomed;
			tRectF.bottom = m_ArrayCabinetViewList.get(i).m_topCustomView
					+ m_ArrayCabinetViewList.get(i).m_HeightZoomed;
			if (PtInRect(tDownPointF, tRectF)) {
				return m_ArrayCabinetViewList.get(i);
			}
		}

		return null;

	}

	// 移动视口，对象本身不移动
	public void ScrollView(float deltaX, float deltaY) {
		float tViewPortX = m_ViewPortPosF.x - deltaX;
		float tViewPortY = m_ViewPortPosF.y - deltaY;

		if (tViewPortX < 0) {
			tViewPortX = 0;
		}

		if (tViewPortY < 0) {
			tViewPortY = 0;
		}

		if (tViewPortX > 6400) {
			tViewPortX = 6400;
		}
		if (tViewPortY > 3420) {
			tViewPortY = 3420;
		}

		// String strInfoString =
		// "ViewPort:X="+tViewPortX+"Y="+tViewPortY+"DX="+deltaX+",DY="+deltaY;

		// Log.i("SCRoll", strInfoString);

		setM_ViewPortPosF(new PointF(tViewPortX, tViewPortY));

		invalidate();

	}

	// 对象大小改变
	public void ChangeBasicViewLengthAndWidth(int BasicView, int deltaX,
			int deltaY) {

	}

	public void DrawCabinet(Canvas canvas, Paint paint,
			CabinetViewObj tCabinetView) {
		paint.setStyle(Style.FILL);

		int tColor = tCabinetView.getmBackGroundColor();
		if (tColor != 0) {
			paint.setColor(tColor);
		} else {
			paint.setColor(getResources().getColor(R.color.cbt_green));
		}

		// 画外包矩形
		RectF tmpRectF = new RectF();
		tmpRectF.left = tCabinetView.m_leftCustomView + 1;
		tmpRectF.top = tCabinetView.m_topCustomView + 1;
		tmpRectF.right = tCabinetView.m_leftCustomView
				+ tCabinetView.m_WidthZoomed - 1;
		tmpRectF.bottom = tCabinetView.m_topCustomView
				+ tCabinetView.m_HeightZoomed - 1;
		canvas.drawRect(tmpRectF, paint);

		if (tCabinetView.isSel()) {
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.select);
			canvas.drawBitmap(bmp, tmpRectF.right - 25, tmpRectF.top + 5, null);
		}

		paint.setColor(Color.BLACK);
		// paint.setStrokeWidth(5);
		paint.setTextSize(25*m_Factor);
		String strID = "ID:" + String.valueOf(tCabinetView.getmBasicViewID());
		// String strAddress = String.valueOf(tCabinetView.getM_AddressId());
		canvas.drawText(strID, tmpRectF.left + 3, tmpRectF.top + 18, paint);
		
		String strShowId = String.valueOf(tCabinetView.getM_ShowAddressId());
		// String strAddress = String.valueOf(tCabinetView.getM_AddressId());
		paint.setTextSize(35*m_Factor);
		if (!strShowId.equals("-1")&&!strShowId.equals("0")) {
			canvas.drawText(strShowId, (tmpRectF.left+tmpRectF.right)/2,
					(tmpRectF.top + tmpRectF.bottom)/2, paint);
		}
	
	}

	public void DrawCabinetArray(Canvas canvas, Paint paint) {
		ArrayList<CabinetViewObj> tArr_BasicViews = m_ArrayCabinetViewList;

		for (int i = 0; i < tArr_BasicViews.size(); i++) {

			DrawCabinet(canvas, paint, tArr_BasicViews.get(i));
		}

	}

	// 画选择框线和点
	public void DrawRectAndPoint(RectF rect, Canvas canvas, Paint paint) {

		PointF[] ptArrPointsF = new PointF[8];

		for (int i = 0; i < ptArrPointsF.length; i++) {
			ptArrPointsF[i] = new PointF();
		}

		// 左上
		ptArrPointsF[0].x = rect.left;
		ptArrPointsF[0].y = rect.top;
		// 左中
		ptArrPointsF[1].x = rect.left;
		ptArrPointsF[1].y = ((rect.bottom - rect.top) / 2) + rect.top;
		// 左下
		ptArrPointsF[2].x = rect.left;
		ptArrPointsF[2].y = rect.bottom;
		// 上中
		ptArrPointsF[3].x = ((rect.right - rect.left) / 2) + rect.left;
		ptArrPointsF[3].y = rect.top;
		// 下中
		ptArrPointsF[4].x = ((rect.right - rect.left) / 2) + rect.left;
		ptArrPointsF[4].y = rect.bottom;
		// 右上
		ptArrPointsF[5].x = rect.right;
		ptArrPointsF[5].y = rect.top;
		// 右中
		ptArrPointsF[6].x = rect.right;
		ptArrPointsF[6].y = (rect.top - rect.bottom) / 2 + rect.bottom;
		// 右下
		ptArrPointsF[7].x = rect.right;
		ptArrPointsF[7].y = rect.bottom;

		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth(3);

		float[] fBorders = { ptArrPointsF[0].x, ptArrPointsF[0].y,
				ptArrPointsF[2].x, ptArrPointsF[2].y, ptArrPointsF[2].x,
				ptArrPointsF[2].y, ptArrPointsF[7].x, ptArrPointsF[7].y,
				ptArrPointsF[7].x, ptArrPointsF[7].y, ptArrPointsF[5].x,
				ptArrPointsF[5].y, ptArrPointsF[5].x, ptArrPointsF[5].y,
				ptArrPointsF[0].x, ptArrPointsF[0].y, };

		for (int i = 0; i < 8; i++) {

			canvas.drawCircle(ptArrPointsF[i].x, ptArrPointsF[i].y, 10, paint);

		}

		canvas.drawLines(fBorders, paint);

	}

	public void DrawInterface(Canvas canvas, Paint paint,
			InterfaceViewObj tInterfaceView) {
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.argb(127, 255, 0, 255));
		paint.setStrokeWidth(3);
		BasicViewObj tBasicView = tInterfaceView;

		// 画外包矩形
		RectF tmpRectF = new RectF();
		tmpRectF.left = tBasicView.m_leftCustomView + 1;
		tmpRectF.top = tBasicView.m_topCustomView + 1;
		tmpRectF.right = tBasicView.m_leftCustomView + tBasicView.m_WidthZoomed
				- 1;
		tmpRectF.bottom = tBasicView.m_topCustomView
				+ tBasicView.m_HeightZoomed - 1;
		canvas.drawRect(tmpRectF, paint);

		// 画外框选择效果
		if (tInterfaceView.isSel()) {
			DrawRectAndPoint(tmpRectF, canvas, paint);
		}

	}

	// 画所有的对象
	public void DrawBaiscViewObject(Canvas canvas, Paint paint) {

		DrawCabinetArray(canvas, paint);
		if (bShowConnectionLine) {
			DrawLineChart(canvas, paint);
		}

	}

	public void DrawLineChart(Canvas canvas, Paint paint) {

		if (gAddressedCbtList == null) {
			return;
		}

		if (gAddressedCbtList.size() < 2) {
			return;
		}

		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);

		for (int i = 0; i < gAddressedCbtList.size() - 1; i++) {

			float tCenterX = gAddressedCbtList.get(i).m_leftCustomView
					+ gAddressedCbtList.get(i).m_WidthZoomed / 2;
			float tCenterY = gAddressedCbtList.get(i).m_topCustomView
					+ gAddressedCbtList.get(i).m_HeightZoomed / 2;

			float tCenterX1 = gAddressedCbtList.get(i + 1).m_leftCustomView
					+ gAddressedCbtList.get(i).m_WidthZoomed / 2;
			float tCenterY1 = gAddressedCbtList.get(i + 1).m_topCustomView
					+ gAddressedCbtList.get(i).m_HeightZoomed / 2;

			canvas.drawLine(tCenterX, tCenterY, tCenterX1, tCenterY1, paint);

		}

		if (gAddressedCbtList.size() != 0) {

			// mPaint.setColor(Color.RED);
			float tCenterX = gAddressedCbtList.get(0).m_leftCustomView
					+ gAddressedCbtList.get(0).m_WidthZoomed / 2;
			float tCenterY = gAddressedCbtList.get(0).m_topCustomView
					+ gAddressedCbtList.get(0).m_HeightZoomed / 2;

			float tCenterX1 = gAddressedCbtList
					.get(gAddressedCbtList.size() - 1).m_leftCustomView
					+ gAddressedCbtList.get(gAddressedCbtList.size() - 1).m_WidthZoomed
					/ 2;
			float tCenterY1 = gAddressedCbtList
					.get(gAddressedCbtList.size() - 1).m_topCustomView
					+ gAddressedCbtList.get(gAddressedCbtList.size() - 1).m_HeightZoomed
					/ 2;

			canvas.drawCircle(tCenterX, tCenterY, 15, paint);
			canvas.drawCircle(tCenterX1, tCenterY1, 15, paint);

			paint.setColor(Color.RED);
			paint.setTextSize(18);
			canvas.drawText("S", tCenterX - 4, tCenterY + 5, paint);
			canvas.drawText("E", tCenterX1 - 4, tCenterY1 + 5, paint);

		}
	}

	public boolean PtInRect(PointF point, RectF rect) {
		if (point.x >= rect.left && point.x <= rect.right
				&& point.y >= rect.top && point.y <= rect.bottom) {

			return true;
		} else {
			return false;
		}
	}

	public void ZoomViews(float fFactor) {
		if (fFactor < 0.2) {
			fFactor = 0.2f;
		}
		if (fFactor > 2) {
			fFactor = 2f;
		}

		SetZoomFactor(fFactor);
		invalidate();
	}

	// 撤销恢复
	public void RedoOperation() {

		m_BackForwardStack.getRedoOpStation();
		// m_LedScreenViewHolder.setM_ArrayObjListZoomed(tArrayList);
		invalidate();

	}

	public void UndoOperation() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getActionMasked();
		if (action == MotionEvent.ACTION_POINTER_DOWN) {
			oldDistance = (float) Math.sqrt((event.getX(0) - event.getX(1))
					* (event.getX(0) - event.getX(1))
					+ (event.getY(0) - event.getY(1))
					* (event.getY(0) - event.getY(1)));

			if (oldDistance > 10f) {
				mid.set((event.getX(0) + event.getX(1)) / 2,
						(event.getY(0) + event.getY(1)) / 2);
				gTempFactor = m_Factor;
				gTouchMode = ZOOM;
			}

		} else if (action == MotionEvent.ACTION_POINTER_UP) {
			// getParent().requestDisallowInterceptTouchEvent(false);

			float newDistance;
			newDistance = (float) Math.sqrt((event.getX(0) - event.getX(1))
					* (event.getX(0) - event.getX(1))
					+ (event.getY(0) - event.getY(1))
					* (event.getY(0) - event.getY(1)));
			if (newDistance > 10f) {

				m_Factor = newDistance / oldDistance;
				m_Factor = m_Factor * gTempFactor;
				ZoomViews(m_Factor);

				// oldDistance = newDistance;
			}

			gTouchMode = NONE;

		} else if (action == MotionEvent.ACTION_MOVE) {

			if (gTouchMode == ZOOM) {
				float newDistance1;
				newDistance1 = (float) Math
						.sqrt((event.getX(0) - event.getX(1))
								* (event.getX(0) - event.getX(1))
								+ (event.getY(0) - event.getY(1))
								* (event.getY(0) - event.getY(1)));

				if (newDistance1 > 10f) {
					m_Factor = newDistance1 / oldDistance;
					m_Factor = m_Factor * gTempFactor;
					ZoomViews(m_Factor);

				}

				// return true;
			}

			else if (gTouchMode == SCROLL) {

				// 滚动
				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();

				float fDeltaX = tPointF.x - m_PrevFPts.x;
				float fDeltaY = tPointF.y - m_PrevFPts.y;

				if ((Math.abs(fDeltaX) >= 1024f && Math.abs(fDeltaY) <= -1024f)) {
					fDeltaX = 0;
					fDeltaY = 0;
				}

				if (!(Math.abs(fDeltaX) <= 5f && Math.abs(fDeltaY) <= 5f)) {
					ScrollView(fDeltaX, fDeltaY);
				}

				m_PrevFPts = tPointF;
			} else if (gTouchMode == MOVE) {

				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();

				float fDeltaX = tPointF.x - m_PrevFPts.x;
				float fDeltaY = tPointF.y - m_PrevFPts.y;

				MoveBasicView(fDeltaX, fDeltaY);

				m_PrevFPts = tPointF;
			}

		} else if (action == MotionEvent.ACTION_DOWN) {

			gTouchMode = MOVE;

			PointF tPointF = new PointF();
			tPointF.x = event.getX();
			tPointF.y = event.getY();

			m_DownFPts.x = event.getX();
			m_DownFPts.y = event.getY();

			// 看落下的点是否在选择的BaiscView的Rect内
			// m_LedScreenViewHolder.getM_ArraySelObjView();

			gTouchMode = SCROLL;
			int i = 0;
			for (; i < m_SelectedCbtArrayList.size(); i++) {

				BasicViewObj tBasicVieObj = m_SelectedCbtArrayList.get(i);

				RectF tRectF = new RectF();
				tRectF.left = tBasicVieObj.m_leftCustomView;
				tRectF.top = tBasicVieObj.m_topCustomView;
				tRectF.right = tBasicVieObj.m_leftCustomView
						+ tBasicVieObj.m_WidthZoomed;
				tRectF.bottom = tBasicVieObj.m_topCustomView
						+ tBasicVieObj.m_HeightZoomed;

				if (PtInRect(tPointF, tRectF)) {
					gTouchMode = MOVE;
					break;
				} else {
					continue;
				}

			}

			m_PrevFPts = tPointF;

		} else if (action == MotionEvent.ACTION_UP) {

			// 如果抬起的点和落下的点事同一点，选择
			gTouchMode = NONE;

			PointF tPointF = new PointF();
			tPointF.x = event.getX();
			tPointF.y = event.getY();

			boolean bClick = false;

			if (Math.abs(tPointF.x - m_DownFPts.x) <= 3
					&& Math.abs(tPointF.y - m_DownFPts.y) <= 3) {
				bClick = true;
			}

			if (bClick) {

				CabinetViewObj tBasicViewObj = (CabinetViewObj) FindBasicViewByDownPoint(tPointF);
				if (mMode == 0) {

					int a;
					for (a = 0; a < listinitialcabinetsDefines.size(); a++) {
						if (listinitialcabinetsDefines.get(a).getmBasicViewID() == tBasicViewObj
								.getmBasicViewID()) {
							break;
						}
					}

					if (a == listinitialcabinetsDefines.size()) {
						return true;
					}
					// 这里判定是点击
					// 0 水平s 1水平z 2垂直s 3垂直z
					else {
						gAddressedCbtList = new ArrayList<CabinetViewObj>();
						InteligentAddress(tBasicViewObj, gAddressedCbtList,
								mType);

						HashMap<String, CabinetInformation> CbtInfoHashMap = ConnChartComm
								.GetCbtInfoMap(Ak10Application.GetLedId());

						for (int j = 0; j < gAddressedCbtList.size(); j++) {

							int nTempNum = 0;

							if (j >= 1) {
								int nAddressId = gAddressedCbtList.get(j - 1)
										.getM_AddressId();
								CabinetInformation cbtInformationPrev = CbtInfoHashMap
										.get(gAddressedCbtList.get(j - 1)
												.getStrTypeString());

								if (cbtInformationPrev != null) {
									nTempNum = nAddressId
											+ cbtInformationPrev
													.getListScancardAttachment()
													.size();
								}

							}

							else {
								nTempNum = mStartAddressNum;
							}

							// 将地址值赋值给箱体
							gAddressedCbtList.get(j).setM_AddressId(nTempNum);
							gAddressedCbtList.get(j).setM_ShowAddressId(mStartAddressNum+j);
						}

						AssignCbtAddressDataToDb(gAddressedCbtList);
						AssignCbtShowAddressIdToDb(gAddressedCbtList);

						for (int j = 0; j < listinitialcabinetsDefines.size(); j++) {
							listinitialcabinetsDefines.get(j)
									.setmBackGroundColor(0);
						}
						
						
						
						CustomToast.showToast(getContext(), "", 5);
						mMode = 1;

					}
				} else if (mMode == 1) {

					// 获取选择的对象

					if (tBasicViewObj != null) {

						if (!tBasicViewObj.isSel()) {
							tBasicViewObj.setSel(true);
							m_SelectedCbtArrayList.add(tBasicViewObj);
						} else {
							tBasicViewObj.setSel(false);
							m_SelectedCbtArrayList.remove(tBasicViewObj);
						}
					} else {

						UnSelectAllCabinetViewObj();
					}
				}
				invalidate();
				UpdateSelRect();
			}

			SnapCabinet(1);

			// 将坐标更新进去
			for (int j = 0; j < m_SelectedCbtArrayList.size(); j++) {

				int nID = m_SelectedCbtArrayList.get(j).getmBasicViewID();
				int nx = UtilFun.f2i(m_SelectedCbtArrayList.get(j).m_leftOrg
						+ m_pointInterF.x);
				int ny = UtilFun.f2i(m_SelectedCbtArrayList.get(j).m_topOrg
						+ m_pointInterF.y);

				CabinetDB.UpdateCoordinate(nID, new Point(nx, ny),Ak10Application.GetLedId());
			}

			invalidate();
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		DrawBaiscViewObject(canvas, m_Paint);
		super.onDraw(canvas);
	}

	public ArrayList<CabinetViewObj> getM_SelectedCbtArrayList() {
		return m_SelectedCbtArrayList;
	}

	public void setM_SelectedCbtArrayList(
			ArrayList<CabinetViewObj> m_SelectedCbtArrayList) {
		this.m_SelectedCbtArrayList = m_SelectedCbtArrayList;
	}

	public void setM_ViewPortPosF(PointF t_ViewPortPosF) {

		this.m_ViewPortPosF = t_ViewPortPosF;

		// 更新viewport后，要跟新zoomed和view
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			float tLeft = m_ArrayCabinetViewList.get(i).m_leftZoomed
					- m_ViewPortPosF.x;
			float tTop = m_ArrayCabinetViewList.get(i).m_topZoomed
					- m_ViewPortPosF.y;

			m_ArrayCabinetViewList.get(i).m_leftCustomView = tLeft;
			m_ArrayCabinetViewList.get(i).m_topCustomView = tTop;

		}

		UpdateSelRect();

	}

	// 箱体吸附
	public void SnapCabinet(int tStyle) {

		for (int a = 0; a < m_SelectedCbtArrayList.size(); a++) {

			BasicViewObj tBasicViewObj = m_SelectedCbtArrayList.get(a);

			// 移动对象4个角的坐标，//换算过去的1:1的坐标
			float left1 = tBasicViewObj.m_leftCustomView;
			float top1 = tBasicViewObj.m_topCustomView;
			float right1 = tBasicViewObj.m_leftCustomView
					+ tBasicViewObj.m_WidthZoomed;
			float bottom1 = tBasicViewObj.m_topCustomView
					+ tBasicViewObj.m_HeightZoomed;

			// tBasicView.adsorptCbtViewPosPoint.x=-1;
			// tBasicView.adsorptCbtViewPosPoint.y=-1;

			PointF[] ptsTemPoints = new PointF[4];
			ptsTemPoints[0] = new PointF(left1, top1);
			ptsTemPoints[1] = new PointF(left1, bottom1);
			ptsTemPoints[2] = new PointF(right1, top1);
			ptsTemPoints[3] = new PointF(right1, bottom1);

			float TempX = 0;
			float TempY = 0;
			int nStation = -1;

			for (int j = 0; j < m_ArrayCabinetViewList.size(); j++) { // 遍历箱体
				CabinetViewObj cabinetView = m_ArrayCabinetViewList.get(j);

				if (cabinetView != null) {

					if (cabinetView.getmBasicViewID() == tBasicViewObj
							.getmBasicViewID()) {
						continue;
					}

					// 箱体4个角的坐标
					float left = cabinetView.m_leftCustomView;
					float top = cabinetView.m_topCustomView;
					float right = cabinetView.m_leftCustomView
							+ cabinetView.m_WidthZoomed;
					float bottom = cabinetView.m_topCustomView
							+ cabinetView.m_HeightZoomed;

					int i = 0;
					for (i = 0; i < 4; i++) {

						switch (i) {
						case 0:
							TempX = left;
							TempY = top;
							break;
						case 1:
							TempX = right;
							TempY = top;
							break;
						case 2:
							TempX = left;
							TempY = bottom;
							break;
						case 3:
							TempX = right;
							TempY = bottom;
							break;
						default:
							break;
						}
						int m = 0;
						for (m = 0; m < 4; m++) {
							if ((Math.abs(ptsTemPoints[m].x - TempX) <= SPACE
									* m_Factor && Math.abs(ptsTemPoints[m].y
									- TempY) <= SPACE * m_Factor)) {
								nStation = m + 1;
								break;
							}
						}

						if (nStation != -1) {
							break;
						}
						// 查看时哪个角吸附

					}
					// 变换大小
					if (tStyle == 0) {
						continue;
					} else {
						// 移动

						if (nStation == -1) {
							continue;
						}

						float dx = TempX - ptsTemPoints[nStation - 1].x;
						float dy = TempY - ptsTemPoints[nStation - 1].y;

						RectF tRect = new RectF();

						if (nStation == 1) {
							tRect.left = TempX;
							tRect.top = TempY;
							tRect.right = right1 + dx;
							tRect.bottom = bottom1 + dy;

						} else if (nStation == 2) {
							tRect.left = TempX;
							tRect.top = top1 + dy;
							tRect.right = right1 + dx;
							tRect.bottom = TempY;

						} else if (nStation == 3) {

							tRect.left = left1 + dx;
							tRect.top = TempY;
							tRect.right = TempX;
							tRect.bottom = bottom1 + dy;

						} else if (nStation == 4) {

							tRect.left = left1 + dx;
							tRect.top = top1 + dy;
							tRect.right = TempX;
							tRect.bottom = TempY;
						}

						tBasicViewObj.m_leftOrg = (tRect.left + m_ViewPortPosF.x)
								/ m_Factor;
						tBasicViewObj.m_topOrg = (tRect.top + m_ViewPortPosF.y)
								/ m_Factor;

						tBasicViewObj.m_leftZoomed = tRect.left
								+ m_ViewPortPosF.x;
						tBasicViewObj.m_topZoomed = tRect.top
								+ m_ViewPortPosF.y;

						tBasicViewObj.m_leftCustomView = tRect.left;
						tBasicViewObj.m_topCustomView = tRect.top;

						break;
					}
				}
			}

		}

	}

	public void AddView(BasicViewObj tBasicView) {

		// zoom
		tBasicView.m_leftZoomed = tBasicView.m_leftOrg * m_Factor;
		tBasicView.m_topZoomed = tBasicView.m_topOrg * m_Factor;
		tBasicView.m_WidthZoomed = tBasicView.m_width * m_Factor;
		tBasicView.m_HeightZoomed = tBasicView.m_height * m_Factor;
		// view in custom
		tBasicView.m_leftCustomView = tBasicView.m_leftZoomed
				- m_ViewPortPosF.x;
		tBasicView.m_topCustomView = tBasicView.m_topZoomed - m_ViewPortPosF.y;

		if (tBasicView instanceof CabinetViewObj) {
			m_ArrayCabinetViewList.add((CabinetViewObj) tBasicView);
		}

	}

	public BasicViewObj findBasicViewByID(int tBasicViewID) {
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			if (m_ArrayCabinetViewList.get(i).getmBasicViewID() == tBasicViewID) {
				return m_ArrayCabinetViewList.get(i);
			}

		}

		return null;
	}

	public void MoveBasicView(float tDeltaX, float tDeltaY) {
		// 更新org坐标
		// 当整体中有任何一个触及到边界的时候停止移动

		float tWidth = m_MoveRectF.right - m_MoveRectF.left;
		float tHeight = m_MoveRectF.bottom - m_MoveRectF.top;

		m_MoveRectF.left += tDeltaX;
		m_MoveRectF.top += tDeltaY;

		if (m_MoveRectF.left <= 0) {
			m_MoveRectF.left = 0;
		}
		if (m_MoveRectF.top <= 0) {
			m_MoveRectF.top = 0;
		}

		m_MoveRectF.right = m_MoveRectF.left + tWidth;
		m_MoveRectF.bottom = m_MoveRectF.top + tHeight;

		for (int i = 0; i < m_SelectedCbtArrayList.size(); i++) {

			CabinetViewObj tCabinetViewObj = m_SelectedCbtArrayList.get(i);

			tCabinetViewObj.m_leftCustomView = m_MoveRectF.left
					+ tCabinetViewObj.getxMoveRela();
			tCabinetViewObj.m_topCustomView = m_MoveRectF.top
					+ tCabinetViewObj.getyMoveRela();

			tCabinetViewObj.m_leftZoomed = tCabinetViewObj.m_leftCustomView
					- m_ViewPortPosF.x;
			tCabinetViewObj.m_topZoomed = tCabinetViewObj.m_topCustomView
					- m_ViewPortPosF.y;

			tCabinetViewObj.m_leftOrg = tCabinetViewObj.m_leftZoomed / m_Factor;
			tCabinetViewObj.m_topOrg = tCabinetViewObj.m_topZoomed / m_Factor;
		}

		invalidate();
	}

	public void SetZoomFactor(float fFactor) {

		m_Factor = fFactor;
		// m_ArrayObjListZoomed.clear();
		// m_ViewPortPosF.x=m_ViewPortPosF.x*fFactor;
		// m_ViewPortPosF.y=m_ViewPortPosF.y*fFactor;

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			BasicViewObj tBasicView = m_ArrayCabinetViewList.get(i);

			tBasicView.m_leftZoomed = tBasicView.m_leftOrg * fFactor;
			tBasicView.m_topZoomed = tBasicView.m_topOrg * fFactor;
			tBasicView.m_WidthZoomed = tBasicView.m_width * fFactor;
			tBasicView.m_HeightZoomed = tBasicView.m_height * fFactor;

			tBasicView.m_leftCustomView = tBasicView.m_leftOrg * fFactor
					- m_ViewPortPosF.x;
			tBasicView.m_topCustomView = tBasicView.m_topOrg * fFactor
					- m_ViewPortPosF.y;

		}

	}

	public void InitAddressLine() {

		confirminitialcabinets(0);

		gAddressedCbtList.clear();

		InteligentAddress(listinitialcabinetsDefines.get(0), gAddressedCbtList,
				mType);

		HashMap<String, CabinetInformation> CbtInfoHashMap = ConnChartComm
				.GetCbtInfoMap(Ak10Application.GetLedId());

		for (int j = 0; j < gAddressedCbtList.size(); j++) {

			int nTempNum;

			if (j >= 1) {
				int nAddressId = gAddressedCbtList.get(j - 1).getM_AddressId();
				CabinetInformation cbtInformationPrev = CbtInfoHashMap
						.get(gAddressedCbtList.get(j - 1).getStrTypeString());
				nTempNum = nAddressId
						+ cbtInformationPrev.getListScancardAttachment().size();
			}

			else {
				nTempNum = mStartAddressNum;
			}

			// 将地址值赋值给箱体
			gAddressedCbtList.get(j).setM_AddressId(nTempNum);
		}
		// 在这里把箱体的地址和ID,区域写到xml文件中
		// WriteCbtAddressDataToXML(gAddressedCbtList);
		AssignCbtAddressDataToDb(gAddressedCbtList);

		for (int j = 0; j < listinitialcabinetsDefines.size(); j++) {
			// #7FFF00
			listinitialcabinetsDefines.get(j).setmBackGroundColor(0);
		}

		invalidate();
	}

	public void InitAddressLineByInitAddress() {
		gAddressedCbtList.clear();
		// 初始加载的时候就按照addressid加载进来
		gAddressedCbtList = m_ArrayCabinetViewList;
		invalidate();
	}

	// 获取可以作为起始点的箱体位置，并设置为红色
	// 张建军 16:37:35
	public void confirminitialcabinets(int type)// type =0 为横向，type = 1为纵向
	{

		listinitialcabinetsDefines.clear();
		if (type == 0 || type == 2)// 水平
		{
			List<CabinetViewObj> topsDefines = new ArrayList<CabinetViewObj>();
			List<CabinetViewObj> bottomdDefines = new ArrayList<CabinetViewObj>();
			CabinetViewObj OneInTop = null;
			CabinetViewObj OneInBottom = null;
			int Number = m_ArrayCabinetViewList.size();
			for (int i = 0; i < Number; i++) {// 得到最上和最下的两个箱体
				CabinetViewObj child = (CabinetViewObj) m_ArrayCabinetViewList
						.get(i);
				if (OneInTop == null || Number == 1) {
					OneInTop = child;
					OneInBottom = child;
				} else {

					RECT tRectOneInTop = OneInTop.getM_OrgRect();
					RECT tRectChild = child.getM_OrgRect();
					RECT tRectOneInBottom = OneInBottom.getM_OrgRect();

					if (tRectOneInTop.getTop() >= tRectChild.getTop()) {
						OneInTop = child;
					} else if (tRectOneInBottom.getTop() <= tRectChild.getTop()) {
						OneInBottom = child;
					}
				}
			}
			// 根据得到的上下两个箱体，分别找出与这两个箱体Y坐标一致的箱体组合List 分别
			// 放入topsDefines和bottomdDefines
			for (int i = 0; i < Number; i++) {// 得到最上和最下的两个箱体
				CabinetViewObj child = (CabinetViewObj) m_ArrayCabinetViewList
						.get(i);

				RECT tRectOneInTop = OneInTop.getM_OrgRect();
				RECT tRectChild = child.getM_OrgRect();
				RECT tRectOneInBottom = OneInBottom.getM_OrgRect();

				if (tRectOneInTop.getTop() == tRectChild.getTop()) {
					topsDefines.add(child);
				}
				if (tRectOneInBottom.getTop() == tRectChild.getTop()) {
					bottomdDefines.add(child);
				}
			}
			// 根据得到的两个箱体的List分别找到横坐标最大和最小的两个箱体，选中并添加响应
			CabinetViewObj BasicViewxMin = null;
			CabinetViewObj BasicViewxMax = null;
			for (CabinetViewObj tDefine : topsDefines) {
				if (BasicViewxMin == null) {
					BasicViewxMin = tDefine;
					BasicViewxMax = tDefine;
				} else {

					RECT tRectBasicViewxMin = BasicViewxMin.getM_OrgRect();
					RECT tRectBasicViewxMax = BasicViewxMax.getM_OrgRect();

					RECT tRectDefine = tDefine.getM_OrgRect();

					if (tRectBasicViewxMin.getLeft() >= tRectDefine.getLeft()) {
						BasicViewxMin = tDefine;
					} else if (tRectBasicViewxMax.getLeft() <= tRectDefine
							.getLeft()) {
						BasicViewxMax = tDefine;
					}
				}
			}
			// BasicViewxMin
			BasicViewxMin.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewxMin);

			BasicViewxMax.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewxMax);

			BasicViewxMin = null;
			BasicViewxMax = null;
			for (CabinetViewObj tDefine : bottomdDefines) {
				if (BasicViewxMin == null) {
					BasicViewxMin = tDefine;
					BasicViewxMax = tDefine;
				} else {

					RECT tRectBasicViewxMin = BasicViewxMin.getM_OrgRect();
					RECT tRectBasicViewxMax = BasicViewxMax.getM_OrgRect();

					RECT tRectDefine = tDefine.getM_OrgRect();

					if (tRectBasicViewxMin.getLeft() >= tRectDefine.getLeft()) {
						BasicViewxMin = tDefine;
					} else if (tRectBasicViewxMax.getLeft() <= tRectDefine
							.getLeft()) {
						BasicViewxMax = tDefine;
					}
				}
			}
			BasicViewxMin.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewxMin);
			BasicViewxMax.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewxMax);

		} else if (type == 1 || type == 3) {
			List<CabinetViewObj> leftDefines = new ArrayList<CabinetViewObj>();
			List<CabinetViewObj> rightDefines = new ArrayList<CabinetViewObj>();
			CabinetViewObj OneInleft = null;
			CabinetViewObj OneInright = null;
			int Number = m_ArrayCabinetViewList.size();
			for (int i = 0; i < Number; i++) {// 得到最上和最下的两个箱体
				CabinetViewObj child = (CabinetViewObj) m_ArrayCabinetViewList
						.get(i);
				if (OneInleft == null || Number == 1) {
					OneInleft = child;
					OneInright = child;
				} else {

					RECT tRectOneInleft = OneInleft.getM_OrgRect();
					RECT tRectOneInright = OneInright.getM_OrgRect();
					RECT tRectchild = child.getM_OrgRect();

					if (tRectOneInleft.getLeft() >= tRectchild.getLeft()) {
						OneInleft = child;
					} else if (tRectOneInright.getLeft() <= tRectchild
							.getLeft()) {
						OneInright = child;
					}
				}
			}
			// 根据得到的上下两个箱体，分别找出与这两个箱体x坐标一致的箱体组合List 分别
			// 放入topsDefines和bottomdDefines
			for (int i = 0; i < Number; i++) {// 得到最上和最下的两个箱体
				CabinetViewObj child = (CabinetViewObj) m_ArrayCabinetViewList
						.get(i);

				RECT tRectOneInleft = OneInleft.getM_OrgRect();
				RECT tRectOneInright = OneInright.getM_OrgRect();
				RECT tRectchild = child.getM_OrgRect();

				if (tRectOneInleft.getLeft() == tRectchild.getLeft()) {
					leftDefines.add(child);
				}
				if (tRectOneInright.getLeft() == tRectchild.getLeft()) {
					rightDefines.add(child);
				}
			}
			// 根据得到的两个箱体的List分别找到y标最大和最小的两个箱体，选中并添加响应
			CabinetViewObj BasicViewyMin = null;
			CabinetViewObj BasicViewyMax = null;
			for (CabinetViewObj tDefine : leftDefines) {
				if (BasicViewyMin == null) {
					BasicViewyMin = tDefine;
					BasicViewyMax = tDefine;
				} else {

					RECT tRectBasicViewyMin = BasicViewyMin.getM_OrgRect();
					RECT tRectBasicViewyMax = BasicViewyMax.getM_OrgRect();
					RECT tRectDefine = tDefine.getM_OrgRect();

					if (tRectBasicViewyMin.getTop() >= tRectDefine.getTop()) {
						BasicViewyMin = tDefine;
					} else if (tRectBasicViewyMax.getTop() <= tRectDefine
							.getTop()) {
						BasicViewyMax = tDefine;
					}
				}
			}
			// BasicViewxMin
			BasicViewyMin.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewyMin);
			BasicViewyMax.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewyMax);

			BasicViewyMin = null;
			BasicViewyMax = null;
			for (CabinetViewObj tDefine : rightDefines) {
				if (BasicViewyMin == null) {
					BasicViewyMin = tDefine;
					BasicViewyMax = tDefine;
				} else {

					RECT tRectBasicViewyMin = BasicViewyMin.getM_OrgRect();
					RECT tRectBasicViewyMax = BasicViewyMax.getM_OrgRect();
					RECT tRectDefine = tDefine.getM_OrgRect();

					if (tRectBasicViewyMin.getTop() >= tRectDefine.getTop()) {
						BasicViewyMin = tDefine;
					} else if (tRectBasicViewyMax.getTop() <= tRectDefine
							.getTop()) {
						BasicViewyMax = tDefine;
					}
				}
			}
			BasicViewyMin.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewyMin);

			BasicViewyMax.setmBackGroundColor(Color.RED);
			listinitialcabinetsDefines.add(BasicViewyMax);

		}
	}

	public void InteligentAddress(CabinetViewObj cabinetViewObj,
			ArrayList<CabinetViewObj> gAddressedCbtList2, int type) {
		List<CabinetViewObj> tempDefines = new ArrayList<CabinetViewObj>();
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			CabinetViewObj child = (CabinetViewObj) m_ArrayCabinetViewList
					.get(i);
			if (child.getmBasicViewID() != -1) {
				tempDefines.add(child);
			}

		}

		List<CabinetViewObj> temp1 = new ArrayList<CabinetViewObj>(tempDefines);
		for (CabinetViewObj index : temp1) {
			if (cabinetViewObj == index) {
				tempDefines.remove(index);
				gAddressedCbtList2.add(index);
				// break;
			}
		}
		List<CabinetViewObj> tempsameline = new ArrayList<CabinetViewObj>();
		List<CabinetViewObj> tempsameline1 = new ArrayList<CabinetViewObj>();

		while (!tempDefines.isEmpty())// 水平Ｓ型
		{

			tempsameline.clear();
			tempsameline1.clear();
			CabinetViewObj lastone = null;
			List<CabinetViewObj> temp2 = new ArrayList<CabinetViewObj>(
					tempDefines);
			// temp2 = tempDefines;
			if (type == 0 || type == 2)// shuiping S和Z型
			{
				for (CabinetViewObj index1 : temp2) {
					if (cabinetViewObj.m_topOrg == index1.m_topOrg) {
						CabinetViewObj temp = null;
						temp = index1;
						tempDefines.remove(index1);
						tempsameline.add(temp);
					}
				}
			} else {
				for (CabinetViewObj index1 : temp2) {
					if (cabinetViewObj.m_leftOrg == index1.m_leftOrg) {
						CabinetViewObj temp = null;
						temp = index1;
						tempDefines.remove(index1);
						tempsameline.add(temp);
					}
				}
			}
			// tempsameline.s
			if (!tempsameline.isEmpty()) {
				CabinetViewObj[] arrayDefines = new CabinetViewObj[tempsameline
						.size()];
				int original = 0;
				for (CabinetViewObj idx : tempsameline) {
					arrayDefines[original++] = idx;
				}
				if (type == 0 || type == 2)// shuiping S和Z型
				{
					for (int i = 0; i < tempsameline.size() - 1; i++)// 统一y坐标的相对X的排序。排序规则：相对于mCurrentView
																		// 的x坐标绝对值从小到大排列
					{
						for (int j = 0; j < tempsameline.size() - 1 - i; j++) {
							if (Math.abs((arrayDefines[j].m_leftOrg - cabinetViewObj.m_leftOrg)) >= Math
									.abs((arrayDefines[j + 1].m_leftOrg - cabinetViewObj.m_leftOrg))) {
								CabinetViewObj tempBasicView = null;
								tempBasicView = arrayDefines[j];
								arrayDefines[j] = arrayDefines[j + 1];
								arrayDefines[j + 1] = tempBasicView;
							}
						}
					}
					for (int j = 0; j < arrayDefines.length; j++) {
						gAddressedCbtList2.add(arrayDefines[j]);
						if (j == arrayDefines.length - 1) {
							lastone = arrayDefines[j];
						}
					}
				} else {
					for (int i = 0; i < tempsameline.size() - 1; i++)// 统一y坐标的相对X的排序。排序规则：相对于mCurrentView
																		// 的x坐标绝对值从小到大排列
					{
						for (int j = 0; j < tempsameline.size() - 1 - i; j++) {
							if (Math.abs((arrayDefines[j].m_topOrg - cabinetViewObj.m_topOrg)) >= Math
									.abs((arrayDefines[j + 1].m_topOrg - cabinetViewObj.m_topOrg))) {
								CabinetViewObj tempBasicView = null;
								tempBasicView = arrayDefines[j];
								arrayDefines[j] = arrayDefines[j + 1];
								arrayDefines[j + 1] = tempBasicView;
							}
						}
					}
					for (int j = 0; j < arrayDefines.length; j++) {
						gAddressedCbtList2.add(arrayDefines[j]);
						if (j == arrayDefines.length - 1) {
							lastone = arrayDefines[j];
						}
					}
				}
			} else {
				lastone = cabinetViewObj;
			}
			if (!tempDefines.isEmpty()) {
				if (type == 0 || type == 2)// shuiping S和Z型
				{
					float mindistance = -1;
					CabinetViewObj temp = null;

					for (CabinetViewObj index2 : tempDefines) { // 这里通过y坐标判断下一步走线方法
						if (mindistance == -1 && temp == null) {
							mindistance = Math.abs(lastone.m_topOrg
									- index2.m_topOrg);
							temp = index2;
						} else {
							if (mindistance > Math.abs(lastone.m_topOrg
									- index2.m_topOrg)) {
								mindistance = Math.abs(lastone.m_topOrg
										- index2.m_topOrg);
								temp = index2;
							}
						}
					}
					for (CabinetViewObj index3 : tempDefines) {
						if (temp.m_topOrg == index3.m_topOrg) {
							tempsameline1.add(index3);
						}
					}
					CabinetViewObj[] arrayDefines = new CabinetViewObj[tempsameline1
							.size()];
					int original = 0;
					for (CabinetViewObj idx : tempsameline1) {
						arrayDefines[original++] = idx;
					}

					for (int i = 0; i < tempsameline1.size() - 1; i++)// 统一y坐标的相对X的排序。排序规则：相对于mCurrentView
																		// 的x坐标绝对值从小到大排列
					{
						for (int j = 0; j < tempsameline1.size() - 1 - i; j++) {
							if (Math.abs(arrayDefines[j].m_leftOrg) >= Math
									.abs(arrayDefines[j + 1].m_leftOrg)) {
								CabinetViewObj tempBasicView = null;
								tempBasicView = arrayDefines[j];
								arrayDefines[j] = arrayDefines[j + 1];
								arrayDefines[j + 1] = tempBasicView;
							}
						}
					}
					// /////////可以判断是S型还是Z型
					// if(arrayDefines[0].get == )//S型
					if (type == 0) {// S
						cabinetViewObj = (Math.abs(arrayDefines[0].m_leftOrg
								- lastone.m_leftOrg) <= Math
								.abs(arrayDefines[arrayDefines.length - 1].m_leftOrg
										- lastone.m_leftOrg)) ? arrayDefines[0]
								: arrayDefines[arrayDefines.length - 1];
					} else if (type == 2) {// N
						cabinetViewObj = (Math.abs(arrayDefines[0].m_leftOrg
								- lastone.m_leftOrg) >= Math
								.abs(arrayDefines[arrayDefines.length - 1].m_leftOrg
										- lastone.m_leftOrg)) ? arrayDefines[0]
								: arrayDefines[arrayDefines.length - 1];
					}
					tempDefines.remove(cabinetViewObj);
					gAddressedCbtList2.add(cabinetViewObj);
				}

				else {// 垂直情况
					float mindistance = -1;
					CabinetViewObj temp = null;
					for (CabinetViewObj index2 : tempDefines) { // 这里通过y坐标判断下一步走线方法
						if (mindistance == -1 && temp == null) {
							mindistance = Math.abs(lastone.m_leftOrg
									- index2.m_leftOrg);
							temp = index2;
						} else {
							if (mindistance > Math.abs(lastone.m_leftOrg
									- index2.m_leftOrg)) {
								mindistance = Math.abs(lastone.m_leftOrg
										- index2.m_leftOrg);
								temp = index2;
							}
						}
					}
					for (CabinetViewObj index3 : tempDefines) {
						if (temp.m_leftOrg == index3.m_leftOrg) {
							tempsameline1.add(index3);
						}
					}
					CabinetViewObj[] arrayDefines = new CabinetViewObj[tempsameline1
							.size()];
					int original = 0;
					for (CabinetViewObj idx : tempsameline1) {
						arrayDefines[original++] = idx;
					}

					for (int i = 0; i < tempsameline1.size() - 1; i++)// 统一y坐标的相对X的排序。排序规则：相对于mCurrentView
																		// 的x坐标绝对值从小到大排列
					{
						for (int j = 0; j < tempsameline1.size() - 1 - i; j++) {
							if (Math.abs(arrayDefines[j].m_topOrg) >= Math
									.abs(arrayDefines[j + 1].m_topOrg)) {
								CabinetViewObj tempBasicView = null;
								tempBasicView = arrayDefines[j];
								arrayDefines[j] = arrayDefines[j + 1];
								arrayDefines[j + 1] = tempBasicView;
							}
						}
					}
					// /////////可以判断是S型还是Z型
					// if(arrayDefines[0].get == )//S型
					if (type == 1) {// S
						cabinetViewObj = (Math.abs(arrayDefines[0].m_topOrg
								- lastone.m_topOrg) <= Math
								.abs(arrayDefines[arrayDefines.length - 1].m_topOrg
										- lastone.m_topOrg)) ? arrayDefines[0]
								: arrayDefines[arrayDefines.length - 1];
					} else if (type == 3) {// N
						cabinetViewObj = (Math.abs(arrayDefines[0].m_topOrg
								- lastone.m_topOrg) >= Math
								.abs(arrayDefines[arrayDefines.length - 1].m_topOrg
										- lastone.m_topOrg)) ? arrayDefines[0]
								: arrayDefines[arrayDefines.length - 1];
					}

					tempDefines.remove(cabinetViewObj);
					gAddressedCbtList2.add(cabinetViewObj);
				}
			}
		}

		return;

	}

	public void AssignCbtAddressDataToDb(ArrayList<CabinetViewObj> tArrayList) {
		for (int i = 0; i < tArrayList.size(); i++) {

			CabinetViewObj CbtviewObj = tArrayList.get(i);

			int id = CbtviewObj.getmBasicViewID();

			CabinetDB.UpdateAddressById(id, CbtviewObj.getM_AddressId(),
					Ak10Application.GetLedId());

		}
	}
	public void AssignCbtShowAddressIdToDb(ArrayList<CabinetViewObj> tArrayList) {
		for (int i = 0; i < tArrayList.size(); i++) {
			
			CabinetViewObj CbtviewObj = tArrayList.get(i);
			
			int id = CbtviewObj.getmBasicViewID();
			
			CabinetDB.UpdateCbtShowIdById(id, CbtviewObj.getM_ShowAddressId(),
					Ak10Application.GetLedId());
			
		}
	}

	/**
	 * @return the bShowConnectionLine
	 */
	public boolean isbShowConnectionLine() {
		return bShowConnectionLine;
	}

	/**
	 * @param bShowConnectionLine
	 *            the bShowConnectionLine to set
	 */
	public void setbShowConnectionLine(boolean bShowConnectionLine) {
		this.bShowConnectionLine = bShowConnectionLine;
		invalidate();
	}

	/**
	 * @return the mMode
	 */
	public int getmMode() {
		return mMode;
	}

	/**
	 * @param mMode
	 *            the mMode to set
	 */
	public void setmMode(int mMode) {
		this.mMode = mMode;
	}

	public int getmStartAddressNum() {
		return mStartAddressNum;
	}

	public void setmStartAddressNum(int mStartAddressNum) {
		this.mStartAddressNum = mStartAddressNum;
	}

	public void InitAddressFormXML(ArrayList<CabinetViewObj> CabinetvLists) {
		gAddressedCbtList.clear();
		for (int i = 0; i < CabinetvLists.size() - 1; i++) {
			for (int j = 0; j < CabinetvLists.size() - 1 - i; j++) {
				CabinetViewObj tView = CabinetvLists.get(j);
				CabinetViewObj tView1 = CabinetvLists.get(j + 1);

				int nAddress = tView.getM_AddressId();
				int nAddress1 = tView1.getM_AddressId();

				if (nAddress > nAddress1) {
					CabinetViewObj temp = new CabinetViewObj();
					temp = tView;
					// tView1 = tView;
					// tView = temp;
					CabinetvLists.set(j, tView1);
					CabinetvLists.set(j + 1, temp);
				}
			}
		}
		// gAddressedCbtList.

		gAddressedCbtList = CabinetvLists;

		invalidate();

	}

}
