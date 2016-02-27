package com.szaoto.ak10.ownerdraw;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.INetDataChangeObserver;
import com.szaoto.ak10.R;
import com.szaoto.ak10.SocketCommService;
import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.configuration.AcqCardSetupActivity;
import com.szaoto.ak10.configuration.SendCardSetupActivity;
import com.szaoto.ak10.configuration.SetVideoScaleActivity;
import com.szaoto.ak10.configuration.SystemCardSetupActivity;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.datacomm.ChanComm;
import com.szaoto.ak10.datacomm.ConnChartComm;
import com.szaoto.ak10.datacomm.InterfaceComm;
import com.szaoto.ak10.leddisplay.ConnChartActivity;
import com.szaoto.ak10.leddisplay.LedConstructActivity;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.sqlitedata.CbtData;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.util.UtilFun;

public class LedConfigCustomView extends View implements INetDataChangeObserver {

	Paint m_Paint;
	PointF m_PrevFPts = new PointF();
	PointF m_DownFPts = new PointF();
	PointF m_TouchDownPts = new PointF();
	boolean bClick = false;

	// 定义CustomView中的对象列表,相对坐标为本View
	ArrayList<BasicViewObj> m_ViewObj = null;
	OnBasicViewChangeListener m_BasicViewChangeListener;

	// 相对于LedDisplay的位置
	/**/
	int gTouchMode = 0;
	private PointF mid = new PointF();
	private float oldDistance;

	private static final int NONE = 0;
	private static final int MOVE = 1;
	private static final int ZOOM = 2;
	private static final int SCROLL = 3;
	private static final int SIZEBM = 4;
	private static final int SIZERM = 5;
	private static final int SIZERB = 6;

	public float gFactor = 1.0f;
	public float gTempFactor = 1.0f;

	public LedConstructActivity m_LedConfigActivity = null;

	private boolean bShowINTF = true;
	private boolean bShowCHAN = true;
	private boolean bShowLABEL = true;
	private boolean bShowCBT = true;

	public final int LOG_MOVE = 0;
	public final int LOG_SCROLL = 1;
	public final int LOG_SIZE = 2;
	public final int LOG_CREATE = 3;
	public final int LOG_CREATE_GROUP = 4;
	public final int LOG_DELETE = 5;
	public final int LOG_DELETE_GROUP = 6;

	BasicViewObj m_BasicViewDownSel = new BasicViewObj();

	public final static int MSGINTF = 0;
	public final static int MSGCHAN = 1;
	public final static int MSGCLEAR = 2;

	// 定义画图边界大小
	public final int xMax = 12800;
	public final int yMax = 8000;

	// ////////////////////////////////////////

	public BackForwardStack m_BackForwardStack = new BackForwardStack();
	// LedDisplay相对于View的坐标
	public PointF m_ViewPortPosF = new PointF();
	// 相对LED
	public ArrayList<CabinetViewObj> m_ArrayCabinetViewList = new ArrayList<CabinetViewObj>();
	public ArrayList<InterfaceViewObj> m_ArrayInterfaceViewList = new ArrayList<InterfaceViewObj>();
	public ArrayList<ChannelViewObj> m_ArrayChanelViewsList = new ArrayList<ChannelViewObj>();

	// 选择的View对象
	private BasicViewObj m_CurSelBasicView = null;

	// 比例因子
	public float m_Factor = 1.0f;
	final private int SPACE = 20;

	// public ArrayList<CabinetViewObj> m_SelectedCbtArrayList =new
	// ArrayList<CabinetViewObj>();

	// 当前选择的BaiscView
	// public ArrayList<CabinetViewObj> m_ArraySelBasicView = new
	// ArrayList<CabinetViewObj>();

	// ///////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////

	ProgressDialog progSaveCurDiag;
	// //////////////////////////////////////////////////////////
	private PointF m_ViewPortPosFOrg = new PointF(0, 0);
	private PointF m_DownSelFPt = new PointF();
	public boolean bLockScreen = false;

	public LedConfigCustomView(Context context) {
		super(context);
		m_Paint = new Paint();
		InitLedScreenViewHolder();

		SocketCommService.netobservers.add(this);

		this.setClickable(true);
	}

	public LedConfigCustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_Paint = new Paint();
		InitLedScreenViewHolder();
		SocketCommService.netobservers.add(this);
		this.setClickable(true);
	}

	public LedConfigCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_Paint = new Paint();
		InitLedScreenViewHolder();
		SocketCommService.netobservers.add(this);
		this.setClickable(true);
	}

	public void RemoveAllViews() {
		m_ArrayCabinetViewList.clear();
		m_ArrayChanelViewsList.clear();
		m_ArrayInterfaceViewList.clear();
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
		float zoomx1;
		float zoomy1;
		float max;
		float min;
		float gZoomFactor;
		ArrayList<PointF> arrPoint = new ArrayList<PointF>();
		ArrayList<PointF> ChannelarrPoint = new ArrayList<PointF>();
		float leftMin = m_ArrayCabinetViewList.get(0).m_leftOrg;
		float rightMax = m_ArrayCabinetViewList.get(0).m_leftOrg
				+ m_ArrayCabinetViewList.get(0).m_width;
		float topMin = m_ArrayCabinetViewList.get(0).m_topOrg;
		float bottomMax = m_ArrayCabinetViewList.get(0).m_topOrg
				+ m_ArrayCabinetViewList.get(0).m_height;
		// 添加对Channel位置及比例的判断
		float ChannelleftMin = m_ArrayChanelViewsList.get(0).m_leftOrg;
		float ChannelrightMax = m_ArrayChanelViewsList.get(0).m_leftOrg
				+ m_ArrayChanelViewsList.get(0).m_width;
		float ChanneltopMin = m_ArrayChanelViewsList.get(0).m_topOrg;
		float ChannelbottomMax = m_ArrayChanelViewsList.get(0).m_topOrg
				+ m_ArrayChanelViewsList.get(0).m_height;
		ChannelViewObj tChannelViewObj = null;
		for (int i = 0; i < m_ArrayChanelViewsList.size(); i++) {
			tChannelViewObj = (ChannelViewObj) m_ArrayChanelViewsList.get(i);
			PointF point1 = new PointF(tChannelViewObj.m_leftOrg, tChannelViewObj.m_topOrg);
			PointF point2 = new PointF(tChannelViewObj.m_leftOrg
					+ tChannelViewObj.m_width, tChannelViewObj.m_topOrg
					+ tChannelViewObj.m_height);
			ChannelarrPoint.add(point1);
			ChannelarrPoint.add(point2);

			if (ChannelleftMin >= point1.x) {
				ChannelleftMin = point1.x;
			}
			if (ChanneltopMin >= point1.y) {
				ChanneltopMin = point1.y;
			}
			if (ChannelrightMax <= point2.x) {
				ChannelrightMax = point2.x;
			}
			if (ChannelbottomMax <= point2.y) {
				ChannelbottomMax = point2.y;
			}
			m_ViewPortPosFOrg.x = ChannelleftMin;
			m_ViewPortPosFOrg.y = ChanneltopMin;
		}
		System.out.println("Y方向的最小值是:" + ChanneltopMin);
		System.out.println("Y方向的最大值是:" + ChannelbottomMax);
		System.out.println("x方向的最小值是:" + ChannelleftMin);
		System.out.println("x方向的最大值是:" + ChannelrightMax);
		zoomx1 = (float) (1.00f * 1280 / (ChannelrightMax - ChannelleftMin));
		zoomy1 = (float) (1.00f * 560 / (ChannelbottomMax - ChanneltopMin));

		System.out.println("x方向的比例是:" + zoomx1);
		System.out.println("y方向的比例是:" + zoomy1);

		// if (zoomx1 > zoomy1) {
		// gZoomFactor = zoomx1;
		// } else {
		// gZoomFactor = zoomy1;
		// }
		// 箱体
		CabinetViewObj tCabinetViewObj = null;
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
			tCabinetViewObj = (CabinetViewObj) m_ArrayCabinetViewList.get(i);

			PointF point1 = new PointF(tCabinetViewObj.m_leftOrg, tCabinetViewObj.m_topOrg);
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
			m_ViewPortPosFOrg.x = leftMin;
			m_ViewPortPosFOrg.y = topMin;
		}

		System.out.println("Y方向的最小值是:" + topMin);
		System.out.println("Y方向的最大值是:" + bottomMax);
		System.out.println("x方向的最小值是:" + leftMin);
		System.out.println("x方向的最大值是:" + rightMax);
		zoomx = (float) (1.00f * 1280 / (rightMax - leftMin));
		zoomy = (float) (1.00f * 560 / (bottomMax - topMin));

		System.out.println("x方向的比例是:" + zoomx);
		System.out.println("y方向的比例是:" + zoomy);
		max = zoomx;
		min = zoomx;
		if (zoomy > max)
			max = zoomy;
		if (zoomx1 > max)
			max = zoomx1;
		if (zoomy1 > max)
			max = zoomy1;
		if (zoomy < min)
			min = zoomy;
		if (zoomx1 < min)
			min = zoomx1;
		if (zoomy1 < min)
			min = zoomy1;
		if (max > min) {
			gZoomFactor = min;
		} else {
			gZoomFactor = max;
		}

		float tViewPortX = m_Factor * m_ViewPortPosFOrg.x;
		float tViewPortY = m_Factor * m_ViewPortPosFOrg.y;

		setM_ViewPortPosF(new PointF(tViewPortX, tViewPortY));
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
		if (tBasicView instanceof InterfaceViewObj) {
			m_ArrayInterfaceViewList.remove(tBasicView);
		}
		if (tBasicView instanceof ChannelViewObj) {
			m_ArrayChanelViewsList.remove(tBasicView);
		}

		invalidate();
	}

	// 查找选择的对象
	private BasicViewObj FindBasicViewByDownPoint(PointF tDownPointF) {
		// 这个tDownPoint是针对View的坐标
		for (int i = m_ArrayInterfaceViewList.size() - 1; i >= 0; i--) {

			RectF tRectF = new RectF();

			tRectF.left = m_ArrayInterfaceViewList.get(i).m_leftCustomView;
			tRectF.top = m_ArrayInterfaceViewList.get(i).m_topCustomView;
			tRectF.right = m_ArrayInterfaceViewList.get(i).m_leftCustomView
					+ m_ArrayInterfaceViewList.get(i).m_WidthZoomed;
			tRectF.bottom = m_ArrayInterfaceViewList.get(i).m_topCustomView
					+ m_ArrayInterfaceViewList.get(i).m_HeightZoomed;
			if (PtInRect(tDownPointF, tRectF)) {
				return m_ArrayInterfaceViewList.get(i);
			}
		}

		for (int i = m_ArrayChanelViewsList.size() - 1; i >= 0; i--) {

			RectF tRectF = new RectF();

			tRectF.left = m_ArrayChanelViewsList.get(i).m_leftCustomView;
			tRectF.top = m_ArrayChanelViewsList.get(i).m_topCustomView;
			tRectF.right = m_ArrayChanelViewsList.get(i).m_leftCustomView
					+ m_ArrayChanelViewsList.get(i).m_WidthZoomed;
			tRectF.bottom = m_ArrayChanelViewsList.get(i).m_topCustomView
					+ m_ArrayChanelViewsList.get(i).m_HeightZoomed;
			if (PtInRect(tDownPointF, tRectF)) {
				return m_ArrayChanelViewsList.get(i);
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

		// (xMax-m_ViewPortPosFOrg.x)*m_Factor,
		// (yMax-m_ViewPortPosFOrg.y)*m_Factor

		if ((xMax - m_ViewPortPosFOrg.x) * m_Factor < 1275) {
			tViewPortX = xMax*m_Factor - 1275;
		}
		if ((yMax - m_ViewPortPosFOrg.y) * m_Factor <550) {
			tViewPortY = yMax*m_Factor - 550;
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
		paint.setColor(getResources().getColor(R.color.cbt_green));

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
			canvas.drawBitmap(bmp, tmpRectF.right - 25*m_Factor, tmpRectF.top + 5*m_Factor, null);
		}

		paint.setColor(Color.YELLOW);
		// paint.setStrokeWidth(5);
		paint.setTextSize(18*m_Factor);
		String strID ="ID:" + String.valueOf(tCabinetView.getmBasicViewID());
		canvas.drawText(strID, tmpRectF.left + 10*m_Factor, tmpRectF.top + 15*m_Factor, paint);

		paint.setTextSize(18 * m_Factor);
		String strCbtName = tCabinetView.getStrTypeString();
		canvas.drawText(strCbtName, tmpRectF.left + 10*m_Factor, tmpRectF.top
			+35*m_Factor, paint);

		// 画地址
		if (tCabinetView.getM_AddressId() != -1) {
			paint.setColor(Color.RED);
			paint.setStrokeWidth(2);
			//String strAddressID = String.valueOf(tCabinetView.getM_AddressId());
			// canvas.drawText(strAddressID, (tmpRectF.left+tmpRectF.right)/2,
			// (tmpRectF.top+tmpRectF.bottom)/2, paint);
			String strShowId = String.valueOf(tCabinetView.getM_ShowAddressId());
			// String strAddress = String.valueOf(tCabinetView.getM_AddressId());
			paint.setTextSize(18*m_Factor*2);
			if (!strShowId.equals("-1")&&!strShowId.equals("0")) {
				canvas.drawText(strShowId, (tmpRectF.left+tmpRectF.right)/2-25*m_Factor,
						(tmpRectF.top + tmpRectF.bottom)/2, paint);
			}
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

		/*
		 * for(int i=0;i<8;i++){
		 * 
		 * canvas.drawCircle(ptArrPointsF[i].x, ptArrPointsF[i].y, 10, paint);
		 * 
		 * }
		 */

		canvas.drawLines(fBorders, paint);

	}

	public void DrawRectAndPointIntf(RectF rect, Canvas canvas, Paint paint) {
		paint.setStyle(Style.FILL);

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

		canvas.drawCircle(ptArrPointsF[4].x, ptArrPointsF[4].y, 10, paint);
		canvas.drawCircle(ptArrPointsF[6].x, ptArrPointsF[6].y, 10, paint);
		canvas.drawCircle(ptArrPointsF[7].x, ptArrPointsF[7].y, 10, paint);

		canvas.drawLines(fBorders, paint);

	}

	public void DrawInterface(Canvas canvas, Paint paint,
			InterfaceViewObj tInterfaceView) {

		// 画基本信息
		String strID = "ID:"
				+ String.valueOf(UtilFun.f2i(tInterfaceView.getmBasicViewID()));
		String strCoordString = "LEFT:"
				+ String.valueOf(UtilFun.f2i(tInterfaceView.m_leftOrg))
				+ " TOP:"
				+ String.valueOf(UtilFun.f2i(tInterfaceView.m_topOrg));
		String strWidAndHeightString = "LOAD:"
				+ String.valueOf(UtilFun.f2i(tInterfaceView.m_width)) + "*"
				+ String.valueOf(UtilFun.f2i(tInterfaceView.m_height));

		paint.setStyle(Style.FILL);
		// paint.setColor(Color.argb(127,255,0,255));
		int tColor = tInterfaceView.getmBackGroundColor();
		paint.setColor(tColor);
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

		if (bShowLABEL) {
			paint.setColor(Color.WHITE);

			float fontSize = 15 * m_Factor;

			if (fontSize >= 30)
				fontSize = 30;
			if (fontSize <= 12)
				fontSize = 12;

			paint.setTextSize(fontSize);

			canvas.drawText(strID, tmpRectF.left + 5, tmpRectF.top + 18, paint);
			canvas.drawText(strCoordString, tmpRectF.left + 5,
					tmpRectF.top + 36, paint);
			canvas.drawText(strWidAndHeightString, tmpRectF.left + 5,
					tmpRectF.top + 54, paint);
		}

		// 外侧边界框线
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawRect(tmpRectF, paint);

		// 画外框选择效果
		if (tInterfaceView.isSel()) {
			DrawRectAndPointIntf(tmpRectF, canvas, paint);
		}

	}

	public void DrawInterfaceArray(Canvas canvas, Paint paint) {

		ArrayList<InterfaceViewObj> tArr_BasicViews = m_ArrayInterfaceViewList;

		for (int i = 0; i < tArr_BasicViews.size(); i++) {

			DrawInterface(canvas, paint, tArr_BasicViews.get(i));
		}

	}

	public void DrawChanelArray(Canvas canvas, Paint paint) {
		paint.setStyle(Style.FILL);
		paint.setColor(Color.argb(140, 25, 58, 137));
		paint.setStrokeWidth(3);

		ArrayList<ChannelViewObj> tArr_BasicViews = m_ArrayChanelViewsList;
		for (int i = 0; i < tArr_BasicViews.size(); i++) {

			ChannelViewObj tChView = tArr_BasicViews.get(i);
			// 画外包矩形
			DrawChanel(canvas, paint, tChView);
		}
	}

	public void DrawChanel(Canvas canvas, Paint paint, ChannelViewObj tChannelViewObj) {
		// 画外包矩形
		RectF tmpRectF = new RectF();
		tmpRectF.left = tChannelViewObj.m_leftCustomView + 1;
		tmpRectF.top = tChannelViewObj.m_topCustomView + 1;
		tmpRectF.right = tChannelViewObj.m_leftCustomView
				+ tChannelViewObj.m_WidthZoomed - 1;
		tmpRectF.bottom = tChannelViewObj.m_topCustomView
				+ tChannelViewObj.m_HeightZoomed - 1;

		// 画信息
		String strID = "ID:"
				+ String.valueOf(UtilFun.f2i(tChannelViewObj.getmBasicViewID()));
		String strCoordString = "LEFT:"
				+ String.valueOf(UtilFun.f2i(tChannelViewObj.m_leftOrg))
				+ " TOP:"
				+ String.valueOf(UtilFun.f2i(tChannelViewObj.m_topOrg));
		String strWidAndHeightString = "LOAD:"
				+ String.valueOf(UtilFun.f2i(tChannelViewObj.m_width)) + "*"
				+ String.valueOf(UtilFun.f2i(tChannelViewObj.m_height));

		if (bShowLABEL) {
			paint.reset();
			paint.setColor(Color.WHITE);

			float fontSize = 15 * m_Factor;

			if (fontSize >= 30)
				fontSize = 30;
			if (fontSize <= 12)
				fontSize = 12;

			paint.setTextSize(fontSize);

			canvas.drawText(strID, (tmpRectF.left + tmpRectF.right) / 2 - 5,
					(tmpRectF.top + tmpRectF.bottom) / 2 - 30, paint);
			canvas.drawText(strCoordString,
					(tmpRectF.left + tmpRectF.right) / 2 - 5,
					(tmpRectF.top + tmpRectF.bottom) / 2, paint);
			canvas.drawText(strWidAndHeightString,
					(tmpRectF.left + tmpRectF.right) / 2 - 5,
					(tmpRectF.top + tmpRectF.bottom) / 2 + 30, paint);
		}

		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.argb(140, 25, 58, 137));
		paint.setStrokeWidth(3);
		canvas.drawRect(tmpRectF, paint);

		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1.5f);
		canvas.drawRect(tmpRectF, paint);

		// 画外框选择效果
		if (tChannelViewObj.isSel()) {
			DrawRectAndPoint(tmpRectF, canvas, paint);
		}
	}

	// 画所有的对象
	public void DrawBaiscViewObject(Canvas canvas, Paint paint) {
		if (bShowCBT) {
			DrawCabinetArray(canvas, paint);
		}
		if (bShowCHAN) {
			DrawChanelArray(canvas, paint);
		}
		if (bShowINTF) {
			DrawInterfaceArray(canvas, paint);
		}

		// 画外边框

		// 外侧边界框线

		if (m_Factor < 0.2) {
			m_Factor = 0.2f;
		}
		if (m_Factor > 2) {
			m_Factor = 2f;
		}

		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		canvas.drawLine((xMax - m_ViewPortPosFOrg.x) * m_Factor, 0,
				(xMax - m_ViewPortPosFOrg.x) * m_Factor,
				(yMax - m_ViewPortPosFOrg.y) * m_Factor, paint);
		canvas.drawLine(0, (yMax - m_ViewPortPosFOrg.y) * m_Factor,
				(xMax - m_ViewPortPosFOrg.x) * m_Factor,
				(yMax - m_ViewPortPosFOrg.y) * m_Factor, paint);

	}

	public boolean PtInRect(Point point, RectF rect) {
		if (point.x >= rect.left && point.x <= rect.right
				&& point.y >= rect.top && point.y <= rect.bottom) {

			return true;
		} else {
			return false;
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

		ObjLog tObjLog = m_BackForwardStack.getRedoOpStation();
		if (tObjLog == null) {
			Toast.makeText(getContext(), "已经前进完毕", Toast.LENGTH_LONG).show();
			return;
		}

		int tMode = tObjLog.getM_ActionMode();

		if (tMode == LOG_CREATE) {
			// 群体添加，要群体添加
			AddBasicView(tObjLog.getM_BasicViewObjTo());
			// 从文件中添加
		}

		if (tMode == LOG_DELETE) {
			// 从UI中添加
			DeleteBasicView(tObjLog.getM_BasicViewObjFrom());
			// 从文件中添加
		}

		if (tMode == LOG_SCROLL) {

			PointF tPointFFrom = tObjLog.getM_ptVpFrom();
			PointF tPointFTo = tObjLog.getM_ptVpTo();

			float deltaX = tPointFFrom.x - tPointFTo.x;
			float deltaY = tPointFFrom.y - tPointFTo.y;
			ScrollView(deltaX, deltaY);
			m_ViewPortPosF = tPointFTo;
			m_ViewPortPosFOrg.x = tPointFTo.x / m_Factor;
			m_ViewPortPosFOrg.y = tPointFTo.y / m_Factor;
		}
		if (tMode == LOG_MOVE) {

			ArrayList<BasicViewObj> tToListObjs = tObjLog
					.getBasicViewObjArrTo();
			for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
				CabinetViewObj tCbtObj = m_ArrayCabinetViewList.get(i);
				for (int j = 0; j < tToListObjs.size(); j++) {
					if (tCbtObj.getmBasicViewID() == tToListObjs.get(j)
							.getmBasicViewID()) {
						tCbtObj.m_leftOrg = tToListObjs.get(j).m_leftOrg;
						tCbtObj.m_topOrg = tToListObjs.get(j).m_topOrg;

						tCbtObj.m_leftZoomed = tToListObjs.get(j).m_leftZoomed;
						tCbtObj.m_topZoomed = tToListObjs.get(j).m_topZoomed;

						tCbtObj.m_leftCustomView = tToListObjs.get(j).m_leftCustomView;
						tCbtObj.m_topCustomView = tToListObjs.get(j).m_topCustomView;

						break;
					}
				}

			}

		}

		invalidate();

	}

	public void UndoOperation() {

		ObjLog tObjLog = m_BackForwardStack.getUndoOpStation();

		if (tObjLog == null) {
			Toast.makeText(getContext(), "已经返回完毕", Toast.LENGTH_LONG).show();
			return;
		}

		int tMode = tObjLog.getM_ActionMode();

		if (tMode == LOG_CREATE) {
			// 从UI中删除
			DeleteBasicView(tObjLog.getM_BasicViewObjTo());
			// 从文件中删除
		}

		if (tMode == LOG_DELETE) {
			AddBasicView(tObjLog.getM_BasicViewObjFrom());
			// 从文件中添加
		}

		if (tMode == LOG_SCROLL) {

			PointF tPointFFrom = tObjLog.getM_ptVpFrom();
			PointF tPointFTo = tObjLog.getM_ptVpTo();

			float deltaX = tPointFFrom.x - tPointFTo.x;
			float deltaY = tPointFFrom.y - tPointFTo.y;
			ScrollView(-deltaX, -deltaY);
			m_ViewPortPosF = tPointFFrom;
			m_ViewPortPosFOrg.x = tPointFFrom.x / m_Factor;
			m_ViewPortPosFOrg.y = tPointFFrom.y / m_Factor;
		}

		if (tMode == LOG_MOVE) {

			ArrayList<BasicViewObj> tFromListObjs = tObjLog
					.getBasicViewObjArrFrom();
			for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {
				CabinetViewObj tCbtObj = m_ArrayCabinetViewList.get(i);
				for (int j = 0; j < tFromListObjs.size(); j++) {
					if (tCbtObj.getmBasicViewID() == tFromListObjs.get(j)
							.getmBasicViewID()) {
						tCbtObj.m_leftOrg = tFromListObjs.get(j).m_leftOrg;
						tCbtObj.m_topOrg = tFromListObjs.get(j).m_topOrg;

						tCbtObj.m_leftZoomed = tFromListObjs.get(j).m_leftZoomed;
						tCbtObj.m_topZoomed = tFromListObjs.get(j).m_topZoomed;

						tCbtObj.m_leftCustomView = tFromListObjs.get(j).m_leftCustomView;
						tCbtObj.m_topCustomView = tFromListObjs.get(j).m_topCustomView;

						break;
					}
				}

			}

		}

		invalidate();

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

				gFactor = newDistance / oldDistance;
				gFactor = gFactor * gTempFactor;
				ZoomViews(gFactor);
				m_Factor = gFactor;
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
					gFactor = newDistance1 / oldDistance;
					gFactor = gFactor * gTempFactor;
					ZoomViews(gFactor);
				}
			} else if (gTouchMode == SCROLL) {
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

			else if (gTouchMode == SIZEBM) {
				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();

				float fDeltaX = tPointF.x - m_PrevFPts.x;
				float fDeltaY = tPointF.y - m_PrevFPts.y;

				if (!(m_CurSelBasicView.m_HeightZoomed <= 100 && fDeltaY < 0)) {
					SizeBasicViewMB(fDeltaX, fDeltaY);
				}

				m_PrevFPts = tPointF;
			}

			else if (gTouchMode == SIZERM) {
				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();
				float fDeltaX = tPointF.x - m_PrevFPts.x;
				float fDeltaY = tPointF.y - m_PrevFPts.y;
				// 当移动到小雨100的时候不让往小移动
				if (!(m_CurSelBasicView.m_WidthZoomed <= 100 && fDeltaX < 0)) {
					SizeBasicViewRM(fDeltaX, fDeltaY);
				}

				m_PrevFPts = tPointF;

			} else if (gTouchMode == SIZERB) {
				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();
				float fDeltaX = tPointF.x - m_PrevFPts.x;
				float fDeltaY = tPointF.y - m_PrevFPts.y;

				if (!(m_CurSelBasicView.m_HeightZoomed <= 100 && fDeltaY < 0)) {
					SizeBasicViewMB(fDeltaX, fDeltaY);
				}

				if (!(m_CurSelBasicView.m_WidthZoomed <= 100 && fDeltaX < 0)) {
					SizeBasicViewRM(fDeltaX, fDeltaY);
				}

				m_PrevFPts = tPointF;
			}

		} else if (action == MotionEvent.ACTION_DOWN) {

			PointF tPointF = new PointF();
			tPointF.x = event.getX();
			tPointF.y = event.getY();

			if (bLockScreen) {
				gTouchMode = SCROLL;
				m_PrevFPts = tPointF;
				return super.onTouchEvent(event);
			}

			m_DownFPts.x = event.getX();
			m_DownFPts.y = event.getY();

			if (m_CurSelBasicView != null) {
				m_DownSelFPt.x = m_CurSelBasicView.m_leftCustomView;
				m_DownSelFPt.y = m_CurSelBasicView.m_topCustomView;
			}

			// 看落下的点是否在选择的BaiscView的Rect内
			// m_LedScreenViewHolder.getM_ArraySelObjView();
			gTouchMode = SCROLL;
			if (m_CurSelBasicView != null) {

				RectF tRectF = new RectF();
				tRectF.left = m_CurSelBasicView.m_leftCustomView;
				tRectF.top = m_CurSelBasicView.m_topCustomView;
				tRectF.right = m_CurSelBasicView.m_leftCustomView + m_CurSelBasicView.m_WidthZoomed;
				tRectF.bottom = m_CurSelBasicView.m_topCustomView + m_CurSelBasicView.m_HeightZoomed;

				// 选择是否是大小改变

				if (m_CurSelBasicView instanceof ChannelViewObj) {
					if (PtInRect(tPointF, tRectF)) {
						gTouchMode = MOVE;
						m_PrevFPts = tPointF;
						return super.onTouchEvent(event);
					}
				}

				if (m_CurSelBasicView instanceof InterfaceViewObj) {

					InterfaceViewObj tInterfaceViewObj = (InterfaceViewObj) m_CurSelBasicView;
					if (tInterfaceViewObj.m_WidthZoomed >= 80
							|| tInterfaceViewObj.m_HeightZoomed >= 80) {
						if (PtInRect(tPointF,tInterfaceViewObj.getHotSpotRectbm())) {
							gTouchMode = SIZEBM;
							m_PrevFPts = tPointF;
							return super.onTouchEvent(event);
						} else if (PtInRect(tPointF,tInterfaceViewObj.getHotSpotRectrm())) {
							gTouchMode = SIZERM;

							m_PrevFPts = tPointF;
							return super.onTouchEvent(event);
						}

						else if (PtInRect(tPointF,tInterfaceViewObj.getHotSpotRectrb())) {
							gTouchMode = SIZERB;
							m_PrevFPts = tPointF;

							return super.onTouchEvent(event);
						}

						else if (PtInRect(tPointF, tRectF)) {
							try {
								m_BasicViewDownSel = (BasicViewObj) m_CurSelBasicView.clone();
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}

							gTouchMode = MOVE;
							m_PrevFPts = tPointF;
							return super.onTouchEvent(event);
						}
					}
					else {
						if (PtInRect(tPointF, tRectF)) {
							gTouchMode = MOVE;
							m_PrevFPts = tPointF;
							return super.onTouchEvent(event);
						}
					}
				}
			}
			m_PrevFPts = tPointF;

		} else if (action == MotionEvent.ACTION_UP) {

			// 当MOVE停止的时候,如果interface改变了Channel
			if (gTouchMode == MOVE) {

				// 这里来做防抖，抬起和落下不超过6个像素，移动回去
				PointF tPointF = new PointF();
				tPointF.x = event.getX();
				tPointF.y = event.getY();

				Log.v("CORD", "brefore:" + m_CurSelBasicView.m_leftCustomView
						+ "," + m_CurSelBasicView.m_topCustomView);

				double dMovDis = getDistance(new PointF(
						m_CurSelBasicView.m_leftCustomView,
						m_CurSelBasicView.m_topCustomView), m_DownSelFPt);
				if (dMovDis <= 5) {
					m_CurSelBasicView.m_leftCustomView = m_DownSelFPt.x;
					m_CurSelBasicView.m_topCustomView = m_DownSelFPt.y;
					m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView	+ m_ViewPortPosF.x;
					m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView + m_ViewPortPosF.y;
					m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed / m_Factor;
					m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed / m_Factor;
				}

				Log.v("CORD", "after:" + m_CurSelBasicView.m_leftCustomView
						+ "," + m_CurSelBasicView.m_leftCustomView);

				if (m_CurSelBasicView instanceof ChannelViewObj) {
					float tdx = 0, tdy = 0;
					if (m_CurSelBasicView.m_leftOrg < 0) {
						tdx = m_CurSelBasicView.m_leftCustomView;
						m_CurSelBasicView.m_leftOrg = 0;
					}
					if (m_CurSelBasicView.m_topOrg < 0) {
						tdy = m_CurSelBasicView.m_topCustomView;
						m_CurSelBasicView.m_topOrg = 0;
					}

					MoveBasicView(tdx, tdy);
				}

				if (m_CurSelBasicView instanceof InterfaceViewObj) {

					int i = 0;
					for (; i < m_ArrayChanelViewsList.size(); i++) {
						RectF tRectF = new RectF();

						tRectF.left = m_ArrayChanelViewsList.get(i).m_leftCustomView;
						tRectF.top = m_ArrayChanelViewsList.get(i).m_topCustomView;
						tRectF.right = m_ArrayChanelViewsList.get(i).m_leftCustomView
								+ m_ArrayChanelViewsList.get(i).m_WidthZoomed;
						tRectF.bottom = m_ArrayChanelViewsList.get(i).m_topCustomView
								+ m_ArrayChanelViewsList.get(i).m_HeightZoomed;

						if (PtInRect(tPointF, tRectF)) {
							break;
						}
					}

					if (i != m_ArrayChanelViewsList.size()) {
						ChannelViewObj tChannelViewObj = m_ArrayChanelViewsList.get(i);

						int tID = tChannelViewObj.getmBasicViewID();
						int nCurParentID = ((InterfaceViewObj) m_CurSelBasicView)
								.getmParentChan().getmBasicViewID();
						if (tID != nCurParentID) {
							// 更换Channel
							((InterfaceViewObj) m_CurSelBasicView)
									.getmParentChan()
									.getM_ArrayChildViewObj()
									.remove((InterfaceViewObj) m_CurSelBasicView);

							((InterfaceViewObj) m_CurSelBasicView).setmParentChan(tChannelViewObj);
							tChannelViewObj.getM_ArrayChildViewObj().add(
									(InterfaceViewObj) m_CurSelBasicView);

							m_CurSelBasicView.m_leftOrg = tChannelViewObj.m_leftOrg;
							m_CurSelBasicView.m_topOrg = tChannelViewObj.m_topOrg;

							m_CurSelBasicView.m_leftZoomed = tChannelViewObj.m_leftZoomed;
							m_CurSelBasicView.m_topZoomed = tChannelViewObj.m_topZoomed;

							m_CurSelBasicView.m_leftCustomView = tChannelViewObj.m_leftCustomView;
							m_CurSelBasicView.m_topCustomView = tChannelViewObj.m_topCustomView;
							m_CurSelBasicView.m_nCfg3d = tChannelViewObj.m_nCfg3d;

							((InterfaceViewObj) m_CurSelBasicView).xMoveRela = 0;
							((InterfaceViewObj) m_CurSelBasicView).yMoveRela = 0;

							// 在数据库中更换chport
							InterfaceDB.UpdateChannelId(
									m_CurSelBasicView.getmBasicViewID(),
									tChannelViewObj.getmBasicViewID(),
									Ak10Application.GetLedId());

							// 更新视频源
							InterfaceComm.SetSendCardChPortAndEnable(true,
									tChannelViewObj.getChPortId(),
									((InterfaceViewObj) m_CurSelBasicView).getmMacAddress(),
									m_CurSelBasicView.getmBasicViewID() % 1000);

						}

						// 落在里面但是超过边界

						if (tChannelViewObj.m_leftOrg > m_CurSelBasicView.m_leftOrg) {
							m_CurSelBasicView.m_leftCustomView = tChannelViewObj.m_leftCustomView;
							m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView	+ m_ViewPortPosF.x;
							m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed / m_Factor;
						}
						if (tChannelViewObj.m_topOrg > m_CurSelBasicView.m_topOrg) {
							m_CurSelBasicView.m_topCustomView = tChannelViewObj.m_topCustomView;
							m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView + m_ViewPortPosF.y;
							m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed / m_Factor;
						}
						if (tChannelViewObj.m_leftOrg + tChannelViewObj.m_width < m_CurSelBasicView.m_leftOrg
								+ m_CurSelBasicView.m_width) {
							m_CurSelBasicView.m_leftCustomView = tChannelViewObj.m_leftCustomView
									+ tChannelViewObj.m_WidthZoomed
									- m_CurSelBasicView.m_WidthZoomed;
							m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView	+ m_ViewPortPosF.x;
							m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed/ m_Factor;
						}
						if (tChannelViewObj.m_topOrg + tChannelViewObj.m_height < m_CurSelBasicView.m_topOrg
								+ m_CurSelBasicView.m_height) {
							m_CurSelBasicView.m_topCustomView = tChannelViewObj.m_topCustomView
									+ tChannelViewObj.m_HeightZoomed
									- m_CurSelBasicView.m_HeightZoomed;
							m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView + m_ViewPortPosF.y;
							m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed / m_Factor;
						}

					} else {
						// 为空，回撤
						// 回撤到边界的位置
						ChannelViewObj tChannelViewObj = ((InterfaceViewObj) m_CurSelBasicView).getmParentChan();

						// 左侧
						if (tPointF.x <= tChannelViewObj.m_leftCustomView) {
							m_CurSelBasicView.m_leftCustomView = tChannelViewObj.m_leftCustomView;
							m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView	+ m_ViewPortPosF.x;
							m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed / m_Factor;
						}
						// 顶部
						if (tPointF.y <= tChannelViewObj.m_topCustomView) {
							m_CurSelBasicView.m_topCustomView = tChannelViewObj.m_topCustomView;
							m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView + m_ViewPortPosF.y;
							m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed / m_Factor;
						}
						// 右侧
						if (tPointF.x >= tChannelViewObj.m_leftCustomView
								+ tChannelViewObj.m_WidthZoomed) {
							m_CurSelBasicView.m_leftCustomView = tChannelViewObj.m_leftCustomView
									+ tChannelViewObj.m_WidthZoomed
									- m_CurSelBasicView.m_WidthZoomed;
							m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView	+ m_ViewPortPosF.x;
							m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed / m_Factor;

						}
						// 下侧
						if (tPointF.y >= tChannelViewObj.m_topCustomView
								+ tChannelViewObj.m_HeightZoomed) {
							m_CurSelBasicView.m_topCustomView = tChannelViewObj.m_topCustomView
									+ tChannelViewObj.m_HeightZoomed
									- m_CurSelBasicView.m_HeightZoomed;
							m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView + m_ViewPortPosF.y;
							m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed / m_Factor;
						}
					}
				}
			}
			if (m_CurSelBasicView != null) {
				Log.v("CORD", "after1:" + m_CurSelBasicView.m_leftCustomView
						+ "," + m_CurSelBasicView.m_leftCustomView);
			}
			// 大小改变时候的吸附
			if (gTouchMode == SIZERB) {
				float dHt = m_CurSelBasicView.m_topCustomView + m_CurSelBasicView.m_HeightZoomed;
				float dDeltaMinHt = dHt	- (m_ArrayCabinetViewList.get(0).m_topCustomView + m_ArrayCabinetViewList.get(0).m_HeightZoomed);

				for (int i = 1; i < m_ArrayCabinetViewList.size(); i++) {
					float dHt1 = m_ArrayCabinetViewList.get(i).m_topCustomView
							+ m_ArrayCabinetViewList.get(i).m_HeightZoomed;
					float dDeltaX = dHt - dHt1;
					if (Math.abs(dDeltaMinHt) > Math.abs(dDeltaX)) {
						dDeltaMinHt = dDeltaX;
					}

				}

				float tmpHeight = m_CurSelBasicView.m_HeightZoomed - dDeltaMinHt;
				if (tmpHeight / m_Factor >= 100) {
					m_CurSelBasicView.m_HeightZoomed -= dDeltaMinHt;
					m_CurSelBasicView.m_height = m_CurSelBasicView.m_HeightZoomed / m_Factor;
				}

				float dRt = m_CurSelBasicView.m_leftCustomView + m_CurSelBasicView.m_WidthZoomed;
				float dDeltaMinWd = dRt - (m_ArrayCabinetViewList.get(0).m_leftCustomView 
						+ m_ArrayCabinetViewList.get(0).m_WidthZoomed);
				for (int i = 1; i < m_ArrayCabinetViewList.size(); i++) {
					float dRt1 = m_ArrayCabinetViewList.get(i).m_leftCustomView
							+ m_ArrayCabinetViewList.get(i).m_WidthZoomed;
					float dDeltaX = dRt - dRt1;
					if (Math.abs(dDeltaMinWd) > Math.abs(dDeltaX)) {
						dDeltaMinWd = dDeltaX;
					}
				}

				float tmpWidth = m_CurSelBasicView.m_WidthZoomed - dDeltaMinWd;
				if (tmpWidth / m_Factor >= 100) {
					m_CurSelBasicView.m_WidthZoomed -= dDeltaMinWd;
					m_CurSelBasicView.m_width = m_CurSelBasicView.m_WidthZoomed	/ m_Factor;
				}
				invalidate();

				// 默认抬起了就修改了数据
				if (m_CurSelBasicView instanceof InterfaceViewObj) {
					IntfModifyCutParam((InterfaceViewObj) m_CurSelBasicView);
					IntfModifyDbParam((InterfaceViewObj) m_CurSelBasicView);
				}

			}

			if (gTouchMode == SIZEBM) {

				float dHt = m_CurSelBasicView.m_topCustomView
						+ m_CurSelBasicView.m_HeightZoomed;

				float dDeltaMin = dHt
						- (m_ArrayCabinetViewList.get(0).m_topCustomView + m_ArrayCabinetViewList
								.get(0).m_HeightZoomed);

				for (int i = 1; i < m_ArrayCabinetViewList.size(); i++) {

					float dHt1 = m_ArrayCabinetViewList.get(i).m_topCustomView
							+ m_ArrayCabinetViewList.get(i).m_HeightZoomed;
					float dDeltaX = dHt - dHt1;

					if (Math.abs(dDeltaMin) > Math.abs(dDeltaX)) {
						dDeltaMin = dDeltaX;
					}

				}

				float tmpHeight = m_CurSelBasicView.m_HeightZoomed - dDeltaMin;

				if (tmpHeight / m_Factor >= 100) {
					m_CurSelBasicView.m_HeightZoomed -= dDeltaMin;
					m_CurSelBasicView.m_height = m_CurSelBasicView.m_HeightZoomed / m_Factor;
				}
				invalidate();

				// 默认抬起了就修改了数据
				if (m_CurSelBasicView instanceof InterfaceViewObj) {
					IntfModifyCutParam((InterfaceViewObj) m_CurSelBasicView);
					IntfModifyDbParam((InterfaceViewObj) m_CurSelBasicView);
				}

			}
			if (gTouchMode == SIZERM) {
				// 查找所有箱体右边点坐标与此时这个view距离最近的

				float dRt = m_CurSelBasicView.m_leftCustomView
						+ m_CurSelBasicView.m_WidthZoomed;

				float dDeltaMin = dRt
						- (m_ArrayCabinetViewList.get(0).m_leftCustomView + m_ArrayCabinetViewList
								.get(0).m_WidthZoomed);

				for (int i = 1; i < m_ArrayCabinetViewList.size(); i++) {

					float dRt1 = m_ArrayCabinetViewList.get(i).m_leftCustomView
							+ m_ArrayCabinetViewList.get(i).m_WidthZoomed;
					float dDeltaX = dRt - dRt1;

					if (Math.abs(dDeltaMin) > Math.abs(dDeltaX)) {
						dDeltaMin = dDeltaX;
					}

				}

				float tmpWidth = m_CurSelBasicView.m_WidthZoomed - dDeltaMin;

				if (tmpWidth / m_Factor > 100) {
					m_CurSelBasicView.m_WidthZoomed -= dDeltaMin;
					m_CurSelBasicView.m_width = m_CurSelBasicView.m_WidthZoomed	/ m_Factor;
				}

				invalidate();

				// 默认抬起了就修改了数据
				if (m_CurSelBasicView instanceof InterfaceViewObj) {

					IntfModifyCutParam((InterfaceViewObj) m_CurSelBasicView);
					IntfModifyDbParam((InterfaceViewObj) m_CurSelBasicView);
				}

			}
			// 如果抬起的点和落下的点事同一点，选择

			PointF tPointF = new PointF();
			tPointF.x = event.getX();
			tPointF.y = event.getY();

			boolean bClick = false;

			if (m_CurSelBasicView != null) {
				Log.v("CORD", "after3:" + m_CurSelBasicView.m_leftCustomView
						+ "," + m_CurSelBasicView.m_leftCustomView);
			}

			if (Math.abs(tPointF.x - m_DownFPts.x) <= 6
					&& Math.abs(tPointF.y - m_DownFPts.y) <= 6) {
				bClick = true;
			}

			if (bClick) {
				// 获取选择的对象
				m_CurSelBasicView = (BasicViewObj) FindBasicViewByDownPoint(tPointF);

				UnSelectAllViewObj();
				if (m_CurSelBasicView != null) {
					m_CurSelBasicView.setSel(!m_CurSelBasicView.isSel());
					if (!m_CurSelBasicView.isSel()) {
						m_CurSelBasicView = null;
					}
					m_LedConfigActivity.mToolFragment.ShowGoTo(true, 2);

				} else {

				}

				if (m_CurSelBasicView != null) {
					Log.v("CORD", "after4:"
							+ m_CurSelBasicView.m_leftCustomView + ","
							+ m_CurSelBasicView.m_leftCustomView);
				}
				invalidate();
			}

			if (m_CurSelBasicView != null) {

				if (m_CurSelBasicView instanceof ChannelViewObj) {
					SnapChannel();
				}
				SnapCabinet(1);

				if (m_CurSelBasicView != null) {
					Log.v("CORD", "after5:"
							+ m_CurSelBasicView.m_leftCustomView + ","
							+ m_CurSelBasicView.m_leftCustomView);
				}
				invalidate();

				// 默认抬起了就修改了数据
				if (m_CurSelBasicView instanceof InterfaceViewObj) {
					IntfModifyCutParam((InterfaceViewObj) m_CurSelBasicView);
					IntfModifyDbParam((InterfaceViewObj) m_CurSelBasicView);
				}

				if (m_CurSelBasicView instanceof ChannelViewObj) {
					ChanModifyDbParam((ChannelViewObj) m_CurSelBasicView);
				}
			}

			gTouchMode = NONE;
		}

		return super.onTouchEvent(event);
	}

	public void UnSelectAllViewObj() {

		for (int i = 0; i < m_ArrayChanelViewsList.size(); i++) {

			if (m_CurSelBasicView != null) {

				if (m_CurSelBasicView.getmBasicViewID() != m_ArrayChanelViewsList
						.get(i).getmBasicViewID()) {
					m_ArrayChanelViewsList.get(i).setSel(false);
				}
			} else {
				m_ArrayChanelViewsList.get(i).setSel(false);
			}

		}

		for (int i = 0; i < m_ArrayInterfaceViewList.size(); i++) {
			if (m_CurSelBasicView != null) {
				if (m_CurSelBasicView.getmBasicViewID() != m_ArrayInterfaceViewList
						.get(i).getmBasicViewID()) {
					m_ArrayInterfaceViewList.get(i).setSel(false);
				}
			} else {
				m_ArrayInterfaceViewList.get(i).setSel(false);
			}
		}

		invalidate();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		DrawBaiscViewObject(canvas, m_Paint);
		super.onDraw(canvas);
	}

	public void setM_ViewPortPosF(PointF t_ViewPortPosF) {
		this.m_ViewPortPosF = t_ViewPortPosF;

		m_ViewPortPosFOrg.x = t_ViewPortPosF.x / m_Factor;
		m_ViewPortPosFOrg.y = t_ViewPortPosF.y / m_Factor;

		// 更新viewport后，要跟新zoomed和view
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			float tLeft = m_ArrayCabinetViewList.get(i).m_leftZoomed
					- m_ViewPortPosF.x;
			float tTop = m_ArrayCabinetViewList.get(i).m_topZoomed
					- m_ViewPortPosF.y;

			m_ArrayCabinetViewList.get(i).m_leftCustomView = tLeft;
			m_ArrayCabinetViewList.get(i).m_topCustomView = tTop;

		}

		for (int i = 0; i < m_ArrayInterfaceViewList.size(); i++) {

			float tLeft = m_ArrayInterfaceViewList.get(i).m_leftZoomed
					- m_ViewPortPosF.x;
			float tTop = m_ArrayInterfaceViewList.get(i).m_topZoomed
					- m_ViewPortPosF.y;

			m_ArrayInterfaceViewList.get(i).m_leftCustomView = tLeft;
			m_ArrayInterfaceViewList.get(i).m_topCustomView = tTop;

		}

		for (int i = 0; i < m_ArrayChanelViewsList.size(); i++) {

			float tLeft = m_ArrayChanelViewsList.get(i).m_leftZoomed
					- m_ViewPortPosF.x;
			float tTop = m_ArrayChanelViewsList.get(i).m_topZoomed
					- m_ViewPortPosF.y;

			m_ArrayChanelViewsList.get(i).m_leftCustomView = tLeft;
			m_ArrayChanelViewsList.get(i).m_topCustomView = tTop;

		}

	}

	// 只吸附左上角，左上角靠近其他的
	public void SnapChannel() {

		if (m_CurSelBasicView == null) {
			return;
		}

		float leftCur = m_CurSelBasicView.m_leftCustomView;
		float topCur = m_CurSelBasicView.m_topCustomView;

		for (int j = 0; j < m_ArrayChanelViewsList.size(); j++) { // 遍历箱体
			ChannelViewObj chanViewObj = m_ArrayChanelViewsList.get(j);

			if (chanViewObj != null) {

				if (chanViewObj.getmBasicViewID() == m_CurSelBasicView
						.getmBasicViewID()) {
					continue;
				}

				float left = chanViewObj.m_leftCustomView;
				float top = chanViewObj.m_topCustomView;
				float right = chanViewObj.m_leftCustomView
						+ chanViewObj.m_WidthZoomed;
				float bottom = chanViewObj.m_topCustomView
						+ chanViewObj.m_HeightZoomed;

				if (Math.abs(leftCur - left) <= SPACE * m_Factor
						&& Math.abs(topCur - bottom) <= SPACE * m_Factor) {
					// 左下

					m_CurSelBasicView.m_leftCustomView = left;
					m_CurSelBasicView.m_topCustomView = bottom;

				}

				if (Math.abs(leftCur - right) <= SPACE * m_Factor
						&& Math.abs(topCur - top) <= SPACE * m_Factor) {
					// 右上
					m_CurSelBasicView.m_leftCustomView = right;
					m_CurSelBasicView.m_topCustomView = top;
				}

				if (Math.abs(leftCur - right) <= 150 * m_Factor
						&& Math.abs(topCur - bottom) <= 150 * m_Factor) {
					// 右下
					m_CurSelBasicView.m_leftCustomView = right;
					m_CurSelBasicView.m_topCustomView = bottom;
				}

				// 坐标调整
				m_CurSelBasicView.m_leftZoomed = m_CurSelBasicView.m_leftCustomView
						+ m_ViewPortPosF.x;
				m_CurSelBasicView.m_topZoomed = m_CurSelBasicView.m_topCustomView
						+ m_ViewPortPosF.y;

				m_CurSelBasicView.m_leftOrg = m_CurSelBasicView.m_leftZoomed
						/ m_Factor;
				m_CurSelBasicView.m_topOrg = m_CurSelBasicView.m_topZoomed
						/ m_Factor;

				break;
			}
		}

		invalidate();
	}

	// 箱体吸附
	public void SnapCabinet(int tStyle) {

		// for (int a = 0; a < m_SelectedCbtArrayList.size(); a++) {

		if (m_CurSelBasicView == null) {
			return;
		}

		BasicViewObj tBasicViewObj = m_CurSelBasicView;

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

				float tDeltaX = tRect.left - tBasicViewObj.m_leftCustomView;
				float tDeltaY = tRect.top - tBasicViewObj.m_topCustomView;

				tBasicViewObj.m_leftOrg = (tRect.left + m_ViewPortPosF.x)
						/ m_Factor;
				tBasicViewObj.m_topOrg = (tRect.top + m_ViewPortPosF.y)
						/ m_Factor;

				tBasicViewObj.m_leftZoomed = tRect.left + m_ViewPortPosF.x;
				tBasicViewObj.m_topZoomed = tRect.top + m_ViewPortPosF.y;

				tBasicViewObj.m_leftCustomView = tRect.left;
				tBasicViewObj.m_topCustomView = tRect.top;

				// 如果是chann吸附还要考虑子view的吸附
				if (tBasicViewObj instanceof ChannelViewObj) {

					for (int k = 0; k < ((ChannelViewObj) m_CurSelBasicView)
							.getM_ArrayChildViewObj().size(); k++) {
						InterfaceViewObj tInterfaceViewObj = ((ChannelViewObj) m_CurSelBasicView)
								.getM_ArrayChildViewObj().get(k);

						tInterfaceViewObj.m_leftCustomView += tDeltaX;
						tInterfaceViewObj.m_topCustomView += tDeltaY;

						tInterfaceViewObj.m_leftZoomed = tInterfaceViewObj.m_leftCustomView
								+ m_ViewPortPosF.x;
						tInterfaceViewObj.m_topZoomed = tInterfaceViewObj.m_topCustomView
								+ m_ViewPortPosF.y;

						tInterfaceViewObj.m_leftOrg = tInterfaceViewObj.m_leftZoomed
								/ m_Factor;
						tInterfaceViewObj.m_topOrg = tInterfaceViewObj.m_topZoomed
								/ m_Factor;

					}
				}
				break;
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
		tBasicView.m_leftCustomView = tBasicView.m_leftZoomed - m_ViewPortPosF.x;
		tBasicView.m_topCustomView = tBasicView.m_topZoomed - m_ViewPortPosF.y;

		if (tBasicView instanceof CabinetViewObj) {
			m_ArrayCabinetViewList.add((CabinetViewObj) tBasicView);
		}

		if (tBasicView instanceof InterfaceViewObj) {
			m_ArrayInterfaceViewList.add((InterfaceViewObj) tBasicView);
		}

		if (tBasicView instanceof ChannelViewObj) {
			m_ArrayChanelViewsList.add((ChannelViewObj) tBasicView);
		}
	}

	public BasicViewObj findBasicViewByID(int tBasicViewID) {

		for (int i = 0; i < m_ArrayInterfaceViewList.size(); i++) {
			if (m_ArrayInterfaceViewList.get(i).getmBasicViewID() == tBasicViewID) {
				return m_ArrayInterfaceViewList.get(i);
			}

		}

		for (int i = 0; i < m_ArrayChanelViewsList.size(); i++) {
			if (m_ArrayChanelViewsList.get(i).getmBasicViewID() == tBasicViewID) {
				return m_ArrayChanelViewsList.get(i);
			}

		}

		return null;
	}

	public void SizeBasicViewMB(float tDeltaX, float tDeltaY) {
		if (m_CurSelBasicView == null) {
			return;
		}

		float tmpHeight = m_CurSelBasicView.m_HeightZoomed + tDeltaY;

		if (tmpHeight / m_Factor < 100) {
			return;
		}

		m_CurSelBasicView.m_HeightZoomed += tDeltaY;
		m_CurSelBasicView.m_height = m_CurSelBasicView.m_HeightZoomed
				/ m_Factor;

		invalidate();

	}

	public void SizeBasicViewRM(float tDeltaX, float tDeltaY) {
		if (m_CurSelBasicView == null) {
			return;
		}

		float tmpWidth = m_CurSelBasicView.m_HeightZoomed + tDeltaY;

		if (tmpWidth / m_Factor < 100) {
			return;
		}

		m_CurSelBasicView.m_WidthZoomed += tDeltaX;
		m_CurSelBasicView.m_width = m_CurSelBasicView.m_WidthZoomed / m_Factor;

		invalidate();
	}

	public void SizeBasicViewRB(float tDeltaX, float tDeltaY) {
		if (m_CurSelBasicView == null) {
			return;
		}

		float tmpWidth = m_CurSelBasicView.m_HeightZoomed + tDeltaY;

		if (tmpWidth / m_Factor < 100) {
			return;
		}

		float tmpHeight = m_CurSelBasicView.m_HeightZoomed + tDeltaY;
		if (tmpHeight / m_Factor < 100) {
			return;
		}

		float x1 = m_CurSelBasicView.m_leftCustomView;
		float y1 = m_CurSelBasicView.m_topCustomView;

		m_CurSelBasicView.m_WidthZoomed += tDeltaX;
		m_CurSelBasicView.m_width = m_CurSelBasicView.m_WidthZoomed / m_Factor;

		m_CurSelBasicView.m_HeightZoomed += tDeltaY;
		m_CurSelBasicView.m_height = m_CurSelBasicView.m_HeightZoomed / m_Factor;

		Log.i("SIZE_POSITION", x1 + "," + y1);

		invalidate();
	}

	public void MoveBasicView(float tDeltaX, float tDeltaY,
			BasicViewObj tBasicViewObj) {

		if (tBasicViewObj == null) {
			return;
		}

		float tLeftOrg = tBasicViewObj.m_leftOrg;
		float ttopOrg = tBasicViewObj.m_topOrg;

		float tDx1 = tDeltaX;
		float tDy1 = tDeltaY;

		tBasicViewObj.m_leftOrg += tDeltaX / m_Factor;
		tBasicViewObj.m_topOrg += tDeltaY / m_Factor;

		if (tBasicViewObj.m_leftOrg < 0) {
			tBasicViewObj.m_leftOrg = 0;
			tDx1 = 0 - tLeftOrg;
		}
		if (tBasicViewObj.m_topOrg < 0) {
			tBasicViewObj.m_topOrg = 0;
			tDy1 = 0 - ttopOrg;
		}

		tBasicViewObj.m_leftZoomed += tDx1;
		tBasicViewObj.m_topZoomed += tDy1;

		tBasicViewObj.m_leftCustomView += tDx1;
		tBasicViewObj.m_topCustomView += tDy1;

		invalidate();

	}

	public void MoveBasicView(float tDeltaX, float tDeltaY) {
		// 更新org坐标
		if (m_CurSelBasicView == null) {
			return;
		}

		BasicViewObj tBasicView = m_CurSelBasicView;

		float tLeftOrg = tBasicView.m_leftCustomView;
		float ttopOrg = tBasicView.m_topCustomView;

		float tDx1 = tDeltaX;
		float tDy1 = tDeltaY;

		tBasicView.m_leftOrg += tDeltaX / m_Factor;
		tBasicView.m_topOrg += tDeltaY / m_Factor;

		if (tBasicView.m_leftOrg < 0) {
			tDx1 = 0 - tLeftOrg;
			tBasicView.m_leftOrg = 0;

		}
		if (tBasicView.m_topOrg < 0) {
			tDy1 = 0 - ttopOrg;
			tBasicView.m_topOrg = 0;
		}

		tBasicView.m_leftZoomed = tBasicView.m_leftOrg * m_Factor;
		tBasicView.m_topZoomed = tBasicView.m_topOrg * m_Factor;

		tBasicView.m_leftCustomView = tBasicView.m_leftZoomed
				- m_ViewPortPosF.x;
		tBasicView.m_topCustomView = tBasicView.m_topZoomed - m_ViewPortPosF.y;

		if (tBasicView instanceof ChannelViewObj) {

			ChannelViewObj tChanObj = (ChannelViewObj) tBasicView;

			ArrayList<InterfaceViewObj> tArrInterfaceViewObjs = tChanObj
					.getM_ArrayChildViewObj();

			for (int i = 0; i < tArrInterfaceViewObjs.size(); i++) {

				InterfaceViewObj interfaceViewObj = tArrInterfaceViewObjs
						.get(i);

				interfaceViewObj.m_leftOrg = tChanObj.m_leftOrg
						+ interfaceViewObj.xMoveRela;
				interfaceViewObj.m_topOrg = tChanObj.m_topOrg
						+ interfaceViewObj.yMoveRela;

				interfaceViewObj.m_leftZoomed = interfaceViewObj.m_leftOrg
						* m_Factor;
				interfaceViewObj.m_topZoomed = interfaceViewObj.m_topOrg
						* m_Factor;

				interfaceViewObj.m_leftCustomView = interfaceViewObj.m_leftZoomed
						- m_ViewPortPosF.x;
				interfaceViewObj.m_topCustomView = interfaceViewObj.m_topZoomed
						- m_ViewPortPosF.y;

			}

		}

		invalidate();
	}

	public void SetZoomFactor(float fFactor) {

		m_Factor = fFactor;
		// m_ArrayObjListZoomed.clear();
		m_ViewPortPosF.x = m_ViewPortPosFOrg.x * fFactor;
		m_ViewPortPosF.y = m_ViewPortPosFOrg.y * fFactor;

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			BasicViewObj tBasicView = m_ArrayCabinetViewList.get(i);

			tBasicView.m_leftZoomed = tBasicView.m_leftOrg * fFactor;
			tBasicView.m_topZoomed = tBasicView.m_topOrg * fFactor;
			tBasicView.m_WidthZoomed = tBasicView.m_width * fFactor;
			tBasicView.m_HeightZoomed = tBasicView.m_height * fFactor;

			tBasicView.m_leftCustomView = tBasicView.m_leftOrg * fFactor - m_ViewPortPosF.x;
			tBasicView.m_topCustomView = tBasicView.m_topOrg * fFactor - m_ViewPortPosF.y;
		}

		for (int i = 0; i < m_ArrayInterfaceViewList.size(); i++) {

			BasicViewObj tBasicView = m_ArrayInterfaceViewList.get(i);

			tBasicView.m_leftZoomed = tBasicView.m_leftOrg * fFactor;
			tBasicView.m_topZoomed = tBasicView.m_topOrg * fFactor;
			tBasicView.m_WidthZoomed = tBasicView.m_width * fFactor;
			tBasicView.m_HeightZoomed = tBasicView.m_height * fFactor;

			tBasicView.m_leftCustomView = tBasicView.m_leftOrg * fFactor - m_ViewPortPosF.x;
			tBasicView.m_topCustomView = tBasicView.m_topOrg * fFactor - m_ViewPortPosF.y;
		}

		for (int i = 0; i < m_ArrayChanelViewsList.size(); i++) {

			BasicViewObj tBasicView = m_ArrayChanelViewsList.get(i);

			tBasicView.m_leftZoomed = tBasicView.m_leftOrg * fFactor;
			tBasicView.m_topZoomed = tBasicView.m_topOrg * fFactor;
			tBasicView.m_WidthZoomed = tBasicView.m_width * fFactor;
			tBasicView.m_HeightZoomed = tBasicView.m_height * fFactor;

			tBasicView.m_leftCustomView = tBasicView.m_leftOrg * fFactor - m_ViewPortPosF.x;
			tBasicView.m_topCustomView = tBasicView.m_topOrg * fFactor - m_ViewPortPosF.y;
		}

	}

	/*
	 * 从数据库中获取数据发送
	 */

	/*
	 * 采集卡 设置 1.使能，非使能 2.视频通道号
	 */

	/**
	 * 修改interface的视频源参数
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */

	public void IntfModifyCutParam(InterfaceViewObj interfaceObj) {

		short x = (short) UtilFun.f2i(interfaceObj.m_leftOrg
				- interfaceObj.getmParentChan().m_leftOrg);
		short y = (short) UtilFun.f2i(interfaceObj.m_topOrg
				- interfaceObj.getmParentChan().m_topOrg);
		short w = (short) UtilFun.f2i(interfaceObj.m_width);
		short h = (short) UtilFun.f2i(interfaceObj.m_height);
		int ncfg3d = interfaceObj.m_nCfg3d;

		// 相对channel的位置
		interfaceObj.xMoveRela = x;
		interfaceObj.yMoveRela = y;

		byte[] macaddress = interfaceObj.getmMacAddress();

		int Rj45num = interfaceObj.getmBasicViewID() % 1000;

		InterfaceComm.SetSendCardPortParam(x, y, w, h, ncfg3d, macaddress, Rj45num);

	}

	/**
	 * 修改这个对象的位置
	 * 
	 * @param interfaceObj
	 */
	public void IntfModifyDbParam(InterfaceViewObj interfaceObj) {

		int id = interfaceObj.getmBasicViewID();

		int x = UtilFun.f2i(interfaceObj.m_leftOrg);
		int y = UtilFun.f2i(interfaceObj.m_topOrg);
		int w = UtilFun.f2i(interfaceObj.m_width);
		int h = UtilFun.f2i(interfaceObj.m_height);

		InterfaceDB.UpdateInterfacePosParam(id, x, y, w, h, Ak10Application.GetLedId());
	}

	/**
	 * 修改这个对象的位置
	 * 
	 * @param interfaceObj
	 */
	public void ChanModifyDbParam(ChannelViewObj chanObj) {

		int id = chanObj.getmBasicViewID();

		int x = UtilFun.f2i(chanObj.m_leftOrg);
		int y = UtilFun.f2i(chanObj.m_topOrg);
		int w = UtilFun.f2i(chanObj.m_width);
		int h = UtilFun.f2i(chanObj.m_height);

		ChannelDB.UpdateChannelPosParam(id, x, y, w, h, Ak10Application.GetLedId());

	}

	// 更改视频源
	public void IntfModifyVideoSource(InterfaceViewObj interfaceObj,
			int nVideoPortId) {
		int videochport = interfaceObj.getmParentChan().getChPortId();
		byte[] macaddress = interfaceObj.getmMacAddress();
		int Rj45num = interfaceObj.getmBasicViewID() % 1000;

		// 视频通道更改
		InterfaceComm.SetSendCardChPortAndEnable(true, videochport, macaddress,
				Rj45num);

	}

	// 删除视频源
	public void Chan_DeleteVideoSource(ChannelViewObj chObj) {
		ChanComm.SetAcqCardPortNumAndEnable(false, chObj.getChPortId(), 5,
				Ak10Application.GetLedId());
	}

	/**
	 * 在初始化设置视频源
	 * 
	 * @param basicViewObj
	 */

	public void InitSetVideoSource(ChannelViewObj chObj) {
		
		int vSoureId = chObj.getmBasicViewID()%1000+chObj.getmBasicViewID()/1000;
		int interceNum = chObj.getmBasicViewID()%1000;
		
		ChanComm.SetAcqCardPortNumAndEnable(true,vSoureId,interceNum,Ak10Application.GetLedId());
	}

	/**
	 * 初始化设置各个网口参数
	 * 
	 * @param basicViewObj
	 */

	public void InitSetSendcardParams(InterfaceViewObj interfaceObj) {
		short x = (short) UtilFun.f2i(interfaceObj.m_leftOrg
				- interfaceObj.getmParentChan().m_leftOrg);
		short y = (short) UtilFun.f2i(interfaceObj.m_topOrg
				- interfaceObj.getmParentChan().m_topOrg);
		short w = (short) UtilFun.f2i(interfaceObj.m_width);
		short h = (short) UtilFun.f2i(interfaceObj.m_height);
		byte[] macaddress = interfaceObj.getmMacAddress();
		int Rj45num = interfaceObj.getmBasicViewID() % 1000;
		int videochport = interfaceObj.getmParentChan().getChPortId();
		int nCfg3d = UtilFun.f2i(interfaceObj.m_nCfg3d);

		// 设置videoport
		try {
			InterfaceComm.SetSendCardChPortAndEnable(true, videochport, macaddress, Rj45num);
			// 设置剪切数据
			InterfaceComm.SetSendCardPortParam(x, y, w, h, nCfg3d,macaddress, Rj45num);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ShowChan(boolean bShow) {

		bShowCHAN = bShow;
		invalidate();

	}

	public void ShowIntf(boolean bShow) {

		bShowINTF = bShow;
		invalidate();
	}

	public void ShowCbt(boolean bShow) {

		bShowCBT = bShow;
		invalidate();
	}

	public void ShowLable(boolean bShow) {

		bShowLABEL = bShow;

		invalidate();

	}
	
	public void GotoScaleSetActivity(LedConstructActivity tLedDispalyConfigActivity) {

		if (m_CurSelBasicView == null) {
			return;
		}

		Intent intentActivity = null;
		if (m_CurSelBasicView instanceof ChannelViewObj) {	
			
//			String strtReslution = null;
//			Bundle bundle = ((Intent) this.getParent()).getExtras(); 
//			int id = bundle.getInt("id");
//			ChnData tChanData =  ChannelDB.GetRecordById(id, Ak10Application.gLedid);
//			strtReslution=	ChanComm.GetAcqCardResolotion(tChanData.videosourceid,tChanData.Id%1000);
//			
			intentActivity = new Intent(getContext(), SetVideoScaleActivity.class);
			int nWidth = Integer.valueOf(AcqCardSetupActivity.GetReadAcqcardwidth());
			int nHeight = Integer.valueOf(AcqCardSetupActivity.GetReadAcqcardheight());
			String strReso;
			if (nWidth > 0 && nHeight > 0) {
				strReso = AcqCardSetupActivity.GetReadAcqcardwidth() + "X" + AcqCardSetupActivity.GetReadAcqcardheight();
			} else {
				strReso = m_CurSelBasicView.m_width+"X"+m_CurSelBasicView.m_height;
			}
			
			intentActivity.putExtra("resolution", strReso);
			intentActivity.putExtra("ChanId", String.valueOf(m_CurSelBasicView.getmBasicViewID()));
			tLedDispalyConfigActivity.startActivity(intentActivity);
		} 	
	}
	

	public void GotoSettingsAct(LedConstructActivity tLedDispalyConfigActivity) {

		if (m_CurSelBasicView == null) {
			return;
		}

		byte[] MacAddress = null;
		if (m_CurSelBasicView instanceof ChannelViewObj) {
			MacAddress = ((ChannelViewObj) m_CurSelBasicView).getmMacAddress();
		} else if (m_CurSelBasicView instanceof InterfaceViewObj) {
			MacAddress = ((InterfaceViewObj) m_CurSelBasicView).getmMacAddress();
		}

		Intent intentActivity = null;
		if (m_CurSelBasicView instanceof ChannelViewObj) {
			if (MacAddress[2] == 1) {
				intentActivity = new Intent(getContext(), SystemCardSetupActivity.class);
			} else {
				intentActivity = new Intent(getContext(), AcqCardSetupActivity.class);
			}
		} else if (m_CurSelBasicView instanceof InterfaceViewObj) {
			if (MacAddress == null) {
				Toast.makeText(m_LedConfigActivity, "Mac地址为空",	Toast.LENGTH_SHORT).show();
				return;
			}

			intentActivity = new Intent(getContext(), SendCardSetupActivity.class);
		}

		if (m_CurSelBasicView != null) {
			intentActivity.putExtra("id", m_CurSelBasicView.getmBasicViewID());
			tLedDispalyConfigActivity.startActivity(intentActivity);	
		}
	}

	public double getDistance(PointF p1, PointF p2) {
		double _x = Math.abs(p1.x - p2.x);
		double _y = Math.abs(p1.y - p2.y);
		return Math.sqrt(_x * _x + _y * _y);
	}

	public void DeleteCurSelView() {

		int nViewStyle = -1;
		if (m_CurSelBasicView instanceof ChannelViewObj) {
			nViewStyle = 0;
		} else {
			nViewStyle = 1;
		}

		// chan
		if (nViewStyle == 0) {
			ChannelViewObj tChannelView = (ChannelViewObj) m_CurSelBasicView;
			int tChPortId = tChannelView.getChPortId();
			int tChId = tChannelView.getmBasicViewID();

			// 向硬件中写入关闭视频源数据
			ChanComm.SetAcqCardPortNumAndEnable(false, tChPortId, tChId % 1000,	Ak10Application.GetLedId());
			// 数据库删除视频源
			ChannelDB.DeleteDataById(tChannelView.getmBasicViewID(), Ak10Application.GetLedId());

			// this.removeView(mCurrentView);
			ArrayList<InterfaceViewObj> arrayList = tChannelView.getM_ArrayChildViewObj();
			for (InterfaceViewObj interfaceView : arrayList) {
				// 数据库中删除intefaceView对应的数据
				InterfaceDB.DeleteDataById(interfaceView.getmBasicViewID(),	Ak10Application.GetLedId());
				InterfaceComm.SetSendCardChPortAndEnable(false, tChPortId,
						interfaceView.getmMacAddress(),
						interfaceView.getmBasicViewID() % 1000);
				DeleteInterfViewDbData(interfaceView);
				DeleteBasicView(interfaceView);
			}

			DeleteBasicView(m_CurSelBasicView);
			m_CurSelBasicView = null;

		} else if (nViewStyle == 1) {
			InterfaceViewObj tIntFView = (InterfaceViewObj) m_CurSelBasicView;
			int tChPortId = tIntFView.getmParentChan().getChPortId();
			InterfaceComm.SetSendCardChPortAndEnable(false, tChPortId,
					tIntFView.getmMacAddress(),
					tIntFView.getmBasicViewID() % 1000);

			DeleteInterfViewDbData(tIntFView);
			ChannelViewObj tChanView = tIntFView.getmParentChan();
			tChanView.getM_ArrayChildViewObj().remove(m_CurSelBasicView);
			DeleteBasicView(m_CurSelBasicView);
		}

	}

	public void DeleteInterfViewDbData(InterfaceViewObj tIntFView) {

		int id = tIntFView.getmBasicViewID();
		InterfaceDB.DeleteDataById(id, Ak10Application.GetLedId());
	}

	// 跳转到连线图

	public void GotoLineActivity() {
		if (m_CurSelBasicView instanceof InterfaceViewObj) {

			UpdateCabinetIntefId();
			// UpdateCabinetInterfIdToCurSel();

			Bundle bundleInterf = new Bundle();
			bundleInterf.putInt("intfId",
					UtilFun.f2i(m_CurSelBasicView.getmBasicViewID()));
			Intent tIntent = new Intent(m_LedConfigActivity,
					ConnChartActivity.class);
			tIntent.putExtras(bundleInterf);
			m_LedConfigActivity.startActivity(tIntent);
		}

	}

	/**
	 * 更新当前发送卡所带的接口的值
	 */
	public void UpdateCabinetInterfIdToCurSel() {
		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			CabinetViewObj tCbtObj = m_ArrayCabinetViewList.get(i);

			RECT tRect = new RECT();

			tRect.left = (int) tCbtObj.m_leftOrg;
			tRect.top = (int) tCbtObj.m_topOrg;
			tRect.right = (int) (tCbtObj.m_leftOrg + tCbtObj.m_width);
			tRect.bottom = (int) (tCbtObj.m_topOrg + tCbtObj.m_height);

			InterfaceViewObj tInterfaceViewObj = (InterfaceViewObj) m_CurSelBasicView;

			RECT tRectIntf = new RECT();

			tRectIntf.left = (int) tInterfaceViewObj.m_leftOrg;
			tRectIntf.top = (int) tInterfaceViewObj.m_topOrg;
			tRectIntf.right = (int) (tInterfaceViewObj.m_leftOrg + tInterfaceViewObj.m_width);
			tRectIntf.bottom = (int) (tInterfaceViewObj.m_topOrg + tInterfaceViewObj.m_height);

			tRectIntf.left -= 5;
			tRectIntf.top -= 5;
			tRectIntf.right += 5;
			tRectIntf.bottom += 5;

			if (tRectIntf.RectInRect(tRect)) {
				CabinetDB.UpdateInterfaceIdById(tCbtObj.getmBasicViewID(),
						tInterfaceViewObj.getmBasicViewID(),
						Ak10Application.GetLedId());
			} else {
				CabinetDB.UpdateInterfaceIdById(tCbtObj.getmBasicViewID(), -1,
						Ak10Application.GetLedId());// 初始状态，没有连接到任何发送卡接口
				CabinetDB.UpdateCbtAddressById(tCbtObj.getmBasicViewID(), -1,
						Ak10Application.GetLedId());
				m_ArrayCabinetViewList.get(i).setM_AddressId(-1);
			}

		}
	}

	public void UpdateCabinetIntefId() {

		for (int i = 0; i < m_ArrayCabinetViewList.size(); i++) {

			CabinetViewObj tCbtObj = m_ArrayCabinetViewList.get(i);

			RECT tRect = new RECT();

			tRect.left = (int) tCbtObj.m_leftOrg;
			tRect.top = (int) tCbtObj.m_topOrg;
			tRect.right = (int) (tCbtObj.m_leftOrg + tCbtObj.m_width);
			tRect.bottom = (int) (tCbtObj.m_topOrg + tCbtObj.m_height);

			int j = 0;
			for (; j < m_ArrayInterfaceViewList.size(); j++) {
				InterfaceViewObj tInterfaceViewObj = m_ArrayInterfaceViewList
						.get(j);

				RECT tRectIntf = new RECT();

				tRectIntf.left = (int) tInterfaceViewObj.m_leftOrg;
				tRectIntf.top = (int) tInterfaceViewObj.m_topOrg;
				tRectIntf.right = (int) (tInterfaceViewObj.m_leftOrg + tInterfaceViewObj.m_width);
				tRectIntf.bottom = (int) (tInterfaceViewObj.m_topOrg + tInterfaceViewObj.m_height);

				tRectIntf.left -= 5;
				tRectIntf.top -= 5;
				tRectIntf.right += 5;
				tRectIntf.bottom += 5;

				if (tRectIntf.RectInRect(tRect)) {
					CabinetDB.UpdateInterfaceIdById(tCbtObj.getmBasicViewID(),
							tInterfaceViewObj.getmBasicViewID(),
							Ak10Application.GetLedId());
					break;
				}

			}

			if (j == m_ArrayInterfaceViewList.size()) {
				CabinetDB.UpdateInterfaceIdById(tCbtObj.getmBasicViewID(), -1,
						Ak10Application.GetLedId());// 初始状态，没有连接到任何发送卡接口
				CabinetDB.UpdateCbtAddressById(tCbtObj.getmBasicViewID(), -1,
						Ak10Application.GetLedId());
				m_ArrayCabinetViewList.get(i).setM_AddressId(-1);
			}
		}

	}

	/**
	 * @return the bShowINTF
	 */
	public boolean isbShowINTF() {
		return bShowINTF;
	}

	/**
	 * @param bShowINTF
	 *            the bShowINTF to set
	 */
	public void setbShowINTF(boolean bShowINTF) {
		this.bShowINTF = bShowINTF;
	}

	/**
	 * @return the bShowCHAN
	 */
	public boolean isbShowCHAN() {
		return bShowCHAN;
	}

	/**
	 * @param bShowCHAN
	 *            the bShowCHAN to set
	 */
	public void setbShowCHAN(boolean bShowCHAN) {
		this.bShowCHAN = bShowCHAN;
	}

	/**
	 * @return the bShowLABEL
	 */
	public boolean isbShowLABEL() {
		return bShowLABEL;
	}

	/**
	 * @param bShowLABEL
	 *            the bShowLABEL to set
	 */
	public void setbShowLABEL(boolean bShowLABEL) {
		this.bShowLABEL = bShowLABEL;
	}

	/**
	 * @return the bShowCBT
	 */
	public boolean isbShowCBT() {
		return bShowCBT;
	}

	/**
	 * @param bShowCBT
	 *            the bShowCBT to set
	 */
	public void setbShowCBT(boolean bShowCBT) {
		this.bShowCBT = bShowCBT;
	}

	@Override
	public int onNetDataChangeNotify(int nType) {
		Message message = new Message();

		if (nType == 0) {
			message.what = MSGCHAN;
		}
		if (nType == 1) {
			message.what = MSGCHAN;
		}
		if (nType == 2) {
			message.what = MSGINTF;
		}
		if (nType == 3) {
			message.what = MSGCLEAR;
		}

		myHandler.sendMessage(message);
		return 0;
	}

	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSGCHAN:
				UpdateChnFromDb();
				invalidate();
				CustomToast.showToast(getContext(), "", Toast.LENGTH_SHORT);
				break;
			case MSGINTF:
				UpdateChnFromDb();
				UpdateIntfaceFromDb();
				invalidate();
				CustomToast.showToast(getContext(), "", Toast.LENGTH_SHORT);
				break;
			case MSGCLEAR:

				m_ArrayChanelViewsList.clear();
				m_ArrayInterfaceViewList.clear();

				invalidate();
			}

			invalidate();
			super.handleMessage(msg);
		}
	};

	private void UpdateIntfaceFromDb() {

		ArrayList<IntfData> arrayListIntfDatas = InterfaceDB.GetAllRecord(Ak10Application.GetLedId());

		for (int i = 0; i < arrayListIntfDatas.size(); i++) {

			IntfData tintfData = arrayListIntfDatas.get(i);

			int nID = tintfData.Id;
			int noffsetx = tintfData.offsetX;
			int noffsety = tintfData.offsetY;
			int nWidth = tintfData.width;
			int nHeight = tintfData.height;
			int nParentId = tintfData.channelid;

			int j = 0;

			for (; j < m_ArrayInterfaceViewList.size(); j++) {
				InterfaceViewObj tIntfViewObj = m_ArrayInterfaceViewList.get(j);

				if (tIntfViewObj.getmBasicViewID() == nID) {

					tIntfViewObj.setmBasicViewID(nID);
					tIntfViewObj.m_leftOrg = noffsetx;
					tIntfViewObj.m_topOrg = noffsety;
					tIntfViewObj.m_width = nWidth;
					tIntfViewObj.m_height = nHeight;

					tIntfViewObj.m_leftZoomed = tIntfViewObj.m_leftOrg
							* m_Factor;
					tIntfViewObj.m_topZoomed = tIntfViewObj.m_topOrg * m_Factor;
					tIntfViewObj.m_WidthZoomed = nWidth * m_Factor;
					tIntfViewObj.m_HeightZoomed = nHeight * m_Factor;

					// 相对采集卡的位置
					ChnData tChnData = ChannelDB.GetRecordById(nParentId,
							Ak10Application.GetLedId());
					tIntfViewObj.xMoveRela = noffsetx - tChnData.offsetX;
					tIntfViewObj.yMoveRela = noffsety - tChnData.offsetY;

					// view in custom
					tIntfViewObj.m_leftCustomView = tIntfViewObj.m_leftZoomed
							- m_ViewPortPosF.x;
					tIntfViewObj.m_topCustomView = tIntfViewObj.m_topZoomed
							- m_ViewPortPosF.y;

					break;

				}

			}

			// 添加
			if (j == m_ArrayInterfaceViewList.size()) {

				byte[] macaddress = tintfData.macaddress;

				InterfaceViewObj tIntfViewObj = new InterfaceViewObj();

				tIntfViewObj.setmBasicViewID(nID);
				tIntfViewObj.m_leftOrg = noffsetx;
				tIntfViewObj.m_topOrg = noffsety;
				tIntfViewObj.m_width = nWidth;
				tIntfViewObj.m_height = nHeight;
				tIntfViewObj.setmMacAddress(macaddress);
				tIntfViewObj.setmBackGroundColor(UtilFun.GetColorById(nID));

				ChnData tChnData = ChannelDB.GetRecordById(nParentId,
						Ak10Application.GetLedId());
				tIntfViewObj.xMoveRela = noffsetx - tChnData.offsetX;
				tIntfViewObj.yMoveRela = noffsety - tChnData.offsetY;

				AddBasicView(tIntfViewObj);

				// 设置parent video surce
				for (int k = 0; k < m_ArrayChanelViewsList.size(); k++) {

					if (m_ArrayChanelViewsList.get(k).getmBasicViewID() == nParentId) {
						tIntfViewObj.setmParentChan(m_ArrayChanelViewsList.get(k));
						m_ArrayChanelViewsList.get(k).getM_ArrayChildViewObj().add(tIntfViewObj);
						break;
					}
				}

				InitSetSendcardParams(tIntfViewObj);

			}

		}

	}

	/**
	 * 循环界面发送连线图
	 */
	public void SendConnChart() {
		int nArrayInterfaceViewListSize = m_ArrayInterfaceViewList.size(); 
		for (int i = 0; i < nArrayInterfaceViewListSize; i++) {

			InterfaceViewObj interfObj = m_ArrayInterfaceViewList.get(i);

			int interfId = interfObj.getmBasicViewID();

			ArrayList<CbtData> arrCbtData = CabinetDB.GetCbtRecordByIntfId(
					interfId, Ak10Application.GetLedId());

			if (arrCbtData == null || arrCbtData.size() == 0) {
				continue;
			}

			int j = 0;
			int narrCbtDataSize = arrCbtData.size();
			for (; j < narrCbtDataSize; j++) {
				if (arrCbtData.get(j).address == -1)
					break;
			}

			// 存的地址有未初始化的情况
			if (j != arrCbtData.size()) {
				continue;
			}

			// 经过过滤，存放的地址都没有问题
			ConnChartComm.SendConnChartFromDB(interfId, Ak10Application.GetLedId(),
					arrCbtData);

		}

	}
	/**
	 * 循环界面发送箱体参数
	 */
	public void SendAllConnParam() {
		int nArrayInterfaceViewListSize = m_ArrayInterfaceViewList.size();
		for (int i = 0; i < nArrayInterfaceViewListSize; i++) {
			
			InterfaceViewObj interfObj = m_ArrayInterfaceViewList.get(i);
			
			int interfId = interfObj.getmBasicViewID();
			
			ArrayList<CbtData> arrCbtData = CabinetDB.GetCbtRecordByIntfId(
					interfId, Ak10Application.GetLedId());
			
			if (arrCbtData == null || arrCbtData.size() == 0) {
				continue;
			}
			
			int j = 0;
			int narrCbtDataSize = arrCbtData.size();
			for (; j < narrCbtDataSize; j++) {
				if (arrCbtData.get(j).address == -1)
					break;
			}
			
			// 存的地址有未初始化的情况
			if (j != arrCbtData.size()) {
				continue;
			}
			
			// 经过过滤，存放的地址都没有问题
			ConnChartComm.SendCabinetParamDromDB(interfId, Ak10Application.GetLedId(),
					arrCbtData);
			
		}
		
	}

	private void UpdateChnFromDb() {

		ArrayList<ChnData> arrayListChanDatas = ChannelDB.GetAllRecord(Ak10Application.GetLedId());
		int narrayListChanDatasSize = arrayListChanDatas.size();
		for (int i = 0; i < narrayListChanDatasSize; i++) {

			ChnData tChnData = arrayListChanDatas.get(i);

			int nID = tChnData.Id;
			int noffsetx = tChnData.offsetX;
			int noffsety = tChnData.offsetY;
			int nWidth = tChnData.width;
			int nHeight = tChnData.height;

			int j = 0;
			int nArrayChanelViewsListSize = m_ArrayChanelViewsList.size();
			for (; j < nArrayChanelViewsListSize; j++) {

				ChannelViewObj tChnViewObj = m_ArrayChanelViewsList.get(j);

				if (tChnViewObj.getmBasicViewID() == nID) {
					tChnViewObj.m_leftOrg = noffsetx;
					tChnViewObj.m_topOrg = noffsety;
					tChnViewObj.m_width = nWidth;
					tChnViewObj.m_height = nHeight;

					tChnViewObj.m_leftZoomed = tChnViewObj.m_leftOrg * m_Factor;
					tChnViewObj.m_topZoomed = tChnViewObj.m_topOrg * m_Factor;
					tChnViewObj.m_WidthZoomed = nWidth * m_Factor;
					tChnViewObj.m_HeightZoomed = nHeight * m_Factor;

					// view in custom
					tChnViewObj.m_leftCustomView = tChnViewObj.m_leftZoomed
							- m_ViewPortPosF.x;
					tChnViewObj.m_topCustomView = tChnViewObj.m_topZoomed
							- m_ViewPortPosF.y;
					break;
				}
				//
			}

			if (j == nArrayChanelViewsListSize) {

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
				AddBasicView(tChnViewObj);
				InitSetVideoSource(tChnViewObj);
			}
		}
	}
	
	public BasicViewObj GetCurSelBasicView(){
		return m_CurSelBasicView;
	}
}
