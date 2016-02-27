/*
   * �ļ��� TravelView.java
   * ���������б�com.szaoto.ak10.custom
   * �汾��Ϣ���汾��
   * ��������2013��11��11������9:52:41
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import com.szaoto.ak10.R;

/*
 * ����TravelView
 * ���� liangdb
 * ��Ҫ���� �ɹ����ĵ�������ͼ
 * ��������2013��11��11��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class TravelView extends SurfaceView implements SurfaceHolder.Callback {

	private OnTravelListener travelListener;
	// ͼƬ��Դ
	private Bitmap backgroundImg;
	private Bitmap[] itemImgs;
	private String[] itemNames;
	private Bitmap coverImg;
	private Bitmap leftArrow;
	private Bitmap rightArrow;

	// �ƶ����ĵ�ʱ��ms
	private int costTime = 100;

	// ��Χ��ܵĿ��ȣ��༴coverImg�Ŀ��ȣ���ΪcoverImgҪ����סitemImg��itemName��Ҳ�����ϸ������������ز�ͼ�Ĵ�С������
	private float frameWidth;
	// ��Χ��ܵĸ߶ȣ��༴backgroundImg�ĸ߶�
	private float frameHeight;
	// ÿһ��Ԫ�صĿ���
	private float itemWidth;
	// ÿһ��Ԫ�صĸ߶�
	private float itemHeight;
	// ��Ļ����
	private float screenWidth;

	// ���ڱ����SurfaceView��SurfaceHolder��ʹ����ΪSurfaceView���ӻص�����
	private SurfaceHolder holder;

	// ָʾ��SurfaceView�Ƿ��Ѿ���ʼ����ɣ�����ָʾ�Ƿ���Խ��л��Ʋ���
	private boolean isInited = false;
	// ָʾ��SurfaceView�Ƿ��Ѿ�ִ����oonSurfaceCreate�Ļص�����������ָʾ�Ƿ���Խ��л��Ʋ���
	private boolean isCreated = false;

	// SurfaceView��߽������Ļ��߽�ľ���,Ĭ��һ��ʼ��0
	private float offsetX;
	// ��ǰ��ָ���item������.Ĭ��0
	private int currentIndex;


	// ��ǰ��cover��߽��x����
	private float coverX;

	// item�Ƿ����ѡ���
	private boolean[] isSelectable;

	private Paint paint;
	private TextPaint textPaint;

	private GestureDetector gd;

	private float destionX;
	private float step;
	private Runnable coverRun;
	private boolean isPaused = false;
	private boolean isCoverRunning = false;
	private boolean isFollowWith;

	private float maxOffsetX;

	public TravelView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(15);
		textPaint.setColor(0xff000000);

		OnGestureListener gestureListener = new OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent e) {
				doOnSingleTapStuff(e.getX());
				return true;
			}

			public void onShowPress(MotionEvent e) {

			}

			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if (offsetX + distanceX > maxOffsetX) {
					offsetX = maxOffsetX;
				} else if (offsetX + distanceX < 0) {
					offsetX = 0;
				} else {
					offsetX += distanceX;
				}
				doDrawStuff();
				return true;
			}

			public void onLongPress(MotionEvent e) {

			}

			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				return false;
			}

			public boolean onDown(MotionEvent e) {
				return true;
			}
		};
		gd = new GestureDetector(gestureListener);

		coverRun = new Runnable() {
			public void run() {
				while (destionX != coverX) {
					long time1 = System.currentTimeMillis();
					// ��ͣ�Ժ��һֱ������
					if (isPaused || !isCreated || !isInited) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					coverX += step;
					// ���һ���
					if (step > 0 && coverX > destionX) {
						coverX = destionX;
					}
					// ���󻬶�
					if (step < 0 && coverX < destionX) {
						coverX = destionX;
					}

					if (isFollowWith) {
						doComputeOffsetX();
					}

					doDrawStuff();
					long time2 = System.currentTimeMillis();
					try {
						Thread.sleep(Math.max(0, 1000 / 24 - (time2 - time1)));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isCoverRunning = false;
				if (travelListener != null) {
					travelListener.onTravel(currentIndex);
				}

			}
		};

		holder = getHolder();
		holder.addCallback(this);
	}

	
	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		doChange();
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		doCreate();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		doDestroy();
	}

	// ��SurfaceChangeʱִ��
	private void doChange() {
		isPaused = false;

		doDrawStuff();
	}

	// ��surfaceCreateʱִ��
	private void doCreate() {
		if (!isCreated) {
			isCreated = true;
			screenWidth = getWidth();
			if (isInited) {
				maxOffsetX = itemImgs.length * frameWidth - screenWidth;
				doDrawStuff();
			}
		}
	}

	// ��surfaceDestroyִ��
	private void doDestroy() {
		isPaused = true;
	}

	public void initView(int backgroundID, int coverID, int[] itemIDs,
			String[] itemNames, boolean[] isSelectable) {
		Resources resources = getResources();
		backgroundImg = BitmapFactory.decodeResource(resources, backgroundID);
		coverImg = BitmapFactory.decodeResource(resources, coverID);
		leftArrow = BitmapFactory.decodeResource(resources,R.drawable.arrow_left);
		rightArrow = BitmapFactory.decodeResource(resources,R.drawable.arrow_right);
		itemImgs = null;
		itemImgs = new Bitmap[itemIDs.length];
		for (int i = 0; i < itemImgs.length; i++) {
			itemImgs[i] = BitmapFactory.decodeResource(resources, itemIDs[i]);
		}
		this.itemNames = itemNames;
		this.isSelectable = isSelectable;

		frameWidth = coverImg.getWidth();
		frameHeight = backgroundImg.getHeight();
		itemWidth = itemImgs[0].getWidth();
		itemHeight = itemImgs[0].getHeight();

		coverX = 0;
		offsetX = 0;

		ViewGroup.LayoutParams layoutParams = getLayoutParams();
		layoutParams.height = (int) frameHeight;
		setLayoutParams(layoutParams);

		isInited = true;

		if (isCreated) {
			maxOffsetX = itemIDs.length * frameWidth - screenWidth;
			doDrawStuff();
		}
	}
	// �������Ʋ���
	private void doDrawStuff() {
		synchronized (holder) {
			Canvas canvas = holder.lockCanvas();
			doDrawBackground(canvas);
			doDrawCover(canvas);
			doDrawItems(canvas);
			doDrawArrows(canvas);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	// ���Ƽ�ͷ
	private void doDrawArrows(Canvas canvas) {

		// ����һ��Ԫ�ؿ��ȣ���������ļ�ͷ
		if (offsetX > frameWidth) {

			canvas.drawBitmap(
					leftArrow,
					new Rect(0, 0, leftArrow.getWidth(), leftArrow.getHeight()),
					new RectF(0, (frameHeight - leftArrow.getHeight()) / 2,
							leftArrow.getWidth(), (frameHeight - leftArrow
									.getHeight()) / 2 + leftArrow.getHeight()),
					paint);

		}

		// �����һ��Ԫ�ص���߽������Ļ�ұ߽�ľ������0ʱ�򣬻������Ҽ�ͷ
		if (maxOffsetX - offsetX >= frameWidth) {

			canvas.drawBitmap(
					rightArrow,
					new Rect(0, 0, rightArrow.getWidth(), rightArrow
							.getHeight()),
					new RectF(screenWidth - rightArrow.getWidth(),
							(frameHeight - rightArrow.getHeight()) / 2,
							screenWidth - rightArrow.getWidth()
									+ rightArrow.getWidth(),
							(frameHeight - rightArrow.getHeight()) / 2
									+ rightArrow.getHeight()), paint);
		}

	}

	// ���Ʊ���
	private void doDrawBackground(Canvas canvas) {
		canvas.drawBitmap(
				backgroundImg,
				new Rect(0, 0, backgroundImg.getWidth(), backgroundImg
						.getHeight()),
				new RectF(0, 0, screenWidth, frameHeight), paint);
	}

	// ����cover
	private void doDrawCover(Canvas canvas) {
		canvas.drawBitmap(coverImg, new Rect(0, 0, coverImg.getWidth(),
				coverImg.getHeight()), new RectF(coverX - offsetX, 0, coverX
				- offsetX + frameWidth, frameHeight), paint);
	}

	// ����items
	private void doDrawItems(Canvas canvas) {

		float textHeight = textPaint.descent();

		for (int i = 0; i < itemImgs.length; i++) {
			canvas.drawBitmap(
					itemImgs[i],
					new Rect(0, 0, itemImgs[i].getWidth(), itemImgs[i]
							.getHeight()), new RectF(i * frameWidth
							+ (frameWidth - itemWidth) / 2 - offsetX,
							(frameHeight - textHeight - itemHeight) / 2, i
									* frameWidth + (frameWidth - itemWidth) / 2
									- offsetX + itemWidth, (frameHeight
									- textHeight - itemHeight)
									/ 2 + itemHeight), paint);
		}

		for (int i = 0; i < itemNames.length; i++) {
			float textWidth = textPaint.measureText(itemNames[i]);
			canvas.drawText(itemNames[i], i * frameWidth
					+ (frameWidth - textWidth) / 2 - offsetX, (frameHeight
					- textHeight - itemHeight)
					/ 2 + itemHeight + 10, textPaint);
		}

	}

	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	// �����������߼�����
	private void doOnSingleTapStuff(float x) {
		int clickIndex = (int) ((x + offsetX) / frameWidth);

		if (clickIndex>=15) {
			return;
		}
		// ����������͵�ǰ����λ�ò�һ���Ҹ������ǿ��Ե����
		if (currentIndex != clickIndex && isSelectable[clickIndex]) {
			// TODO ��cover�ƶ���ָ��������λ�ô���Ȼ���Ƿ������ü����¼�������еĻ���ִ�иü����¼����߼�
			this.currentIndex = clickIndex;
			if (isCoverRunning) {
				isFollowWith = false;
				doComputeMoveValues(clickIndex);
			} else {
				isFollowWith = false;
				doComputeMoveValues(clickIndex);
				startCoverRun();
			}
		}

	}

	// ����Ŀ�ĵ��Լ�����
	private void doComputeMoveValues(int index) {
		destionX = index * frameWidth;
		step = (destionX - coverX) / costTime * (1000 / 24);
	}

	// ��ת����indexλ�ô�
	private void startCoverRun() {
		isCoverRunning = true;
		new Thread(coverRun).start();
	}

	public interface OnTravelListener {
		public void onTravel(int index);
	}

	public void setOnTravelListener(OnTravelListener travelListener) {
		this.travelListener = travelListener;
	}

	// ��ת��ָ����index��,����ʵʱ����offsetX��ֵ,�༴��ͷ����cover�ƶ���Ч��
	public void moveToAndFollowWith(int index) {
		if (index != currentIndex && isSelectable[index]) {
			this.currentIndex = index;
			isFollowWith = true;
			doComputeMoveValues(index);
			doComputeOffsetX();

			// Tread��������
			if (!isCoverRunning) {
				isCoverRunning = true;
				new Thread(coverRun).start();
			}
		}
	}

	// ��ת��ָ����index������ͷ�������ƶ�
	public void moveTo(int index) {

		if (index != currentIndex && isSelectable[index]) {
			this.currentIndex = index;
			isFollowWith = false;
			doComputeMoveValues(index);
			// Tread��������
			if (!isCoverRunning) {
				isCoverRunning = true;
				new Thread(coverRun).start();
			}
		}
	}

	// ����ͷ����ʱ������ƫ��ֵ
	private void doComputeOffsetX() {
		float temp = coverX - screenWidth / 2;

		if (temp <= 0) {
			offsetX = 0;
		} else if (temp >= maxOffsetX) {
			offsetX = maxOffsetX;
		} else {
			offsetX = temp;
		}
	}

}