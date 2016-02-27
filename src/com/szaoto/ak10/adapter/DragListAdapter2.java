package com.szaoto.ak10.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.R.id;
import com.szaoto.ak10.custom1.VideoListManager;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.player.PlayerActivity;
import com.szaoto.ak10.util.XmlTool;

/*
 * �������Ϸ�ɾ���ļ�������
 * ���ã����ڱ༭�����б�ʱ����ק��Ƶ�ļ����򣬼�ɾ���ļ�
 * ���ߣ� zhangsj
 * ��������: 2014��5��14��
 * 
 */
public class DragListAdapter2 extends BaseAdapter {
	private static final String TAG = "DragListAdapter";
	private ArrayList<String> arrayTitles;
	private ArrayList<VideoFile> videoFileList;
    public  static String videopath=HomePageActivity.CONFIG_PATH+"videofilelist.xml";
	private Context context;
	public boolean isHidden;

	VideoListManager videoListManager = new VideoListManager();

	public DragListAdapter2(Context context, ArrayList<String> arrayTitles,
			ArrayList<VideoFile> videoFileList) {
		this.context = context;
		this.arrayTitles = arrayTitles;
		// this.arrayDrawables = arrayDrawables;
		this.videoFileList = videoFileList;
	}

	public void showDropItem(boolean showItem) {
		this.ShowItem = showItem;
	}

	public void setInvisiblePosition(int position) {
		invisilePosition = position;
	}

	private XmlPullParser getXmlPullParser() throws XmlPullParserException,
			FileNotFoundException {

		File xmlFile = new File(videopath);
		if (!xmlFile.exists()) {
			XmlTool.CopyXmlFile(context);
			xmlFile = new File(videopath);

		}

		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance()
				.newPullParser();

		xmlPullParser.setInput(new FileInputStream(xmlFile), "utf-8");
		return xmlPullParser;
	}

	// �����򸲸�View
	/**
	 * �����򸲸�View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/***
		 * �����ﾡ����ÿ�ζ�����ʵ�����µģ���������קListView��ʱ�򲻻���ִ���.
		 * ����ԭ���������������������ԣ�Ŀǰû�з��ִ��ҡ���˵Ч�ʲ��ߣ���������קLisView�㹻�ˡ�
		 */
		convertView = LayoutInflater.from(context).inflate(
				R.layout.drag_list_item2, null);

		TextView textView = (TextView) convertView
				.findViewById(R.id.drag_list_item_text1);

		TextView tvItemIndex = (TextView) convertView
				.findViewById(R.id.tvItemIndex);
		TextView tvDuration = (TextView) convertView
				.findViewById(R.id.tvDuration);
		TextView tvFilePath = (TextView) convertView
				.findViewById(R.id.tvFilePath);
		TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
		TextView tvImagePath = (TextView) convertView
				.findViewById(R.id.tvImagePath);
		TextView tvspecialEffect = (TextView) convertView
				.findViewById(R.id.tvspecialEffect);

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.drag_list_item_image);

		textView.setText(arrayTitles.get(position));

		tvItemIndex.setText(arrayTitles.get(position).toString().split("-")[0]
				.trim());

		VideoFile videoFile = videoFileList.get(position);

		tvDuration.setText(videoFile.getDuration());
		tvFilePath.setText(videoFile.getFilePath());
		tvId.setText(videoFile.getId());
		tvImagePath.setText(videoFile.getImagePath());
		tvspecialEffect.setText(videoFile.getSpecialEffect());

		Button btnDele = (Button) convertView.findViewById(id.btnDelFile);
		btnDele.setOnClickListener(new BtnDeleClick(position, this));

		if (isChanged) {
			Log.i("wanggang", "position == " + position);
			Log.i("wanggang", "holdPosition == " + invisilePosition);
			if (position == invisilePosition) {
				if (!ShowItem) {
					convertView.findViewById(R.id.drag_list_item_text1)
							.setVisibility(View.INVISIBLE);
					convertView.findViewById(R.id.drag_list_item_image)
							.setVisibility(View.INVISIBLE);
					// convertView.findViewById(R.id.check_del).setVisibility(
					// View.INVISIBLE);
					// convertView.setVisibility(View.INVISIBLE);
				}
			}
			if (lastFlag != -1) {
				if (lastFlag == 1) {
					if (position > invisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, -height);
						convertView.startAnimation(animation);
					}
				} else if (lastFlag == 0) {
					if (position < invisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, height);
						convertView.startAnimation(animation);
					}
				}
			}

		}

		// convertView.setTag(position);

		return convertView;
	}

	/**
	 * ��� ɾ�� ��ť
	 */
	private class BtnDeleClick implements OnClickListener {

		private int pos;
		private DragListAdapter2 dragListAdapter;

		public BtnDeleClick(int position, DragListAdapter2 dragListAdapter) {
			super();
			this.pos = position;
			this.dragListAdapter = dragListAdapter;
		}

		@Override
		public void onClick(View v) {
			
			try {	
				//��ʾ�Ƿ�ɾ��
				new AlertDialog.Builder(v.getContext())
				/* �������ڵ�����ͷ���� */
				.setTitle("ɾ��")
				.setMessage("�Ƿ���б�ɾ����")
				/* ���õ������ڵ�ͼʽ */
				.setIcon(android.R.drawable.ic_dialog_info)
				/* ���õ������ڵ���Ϣ */
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						
						File xmlFile = new File(videopath);

						String filePath = xmlFile.getPath();

						VideoFile videoFile = videoFileList.get(pos);

						// ɾ��xml �ļ�
						try {
							XmlTool.deleteXml(filePath, videoFile.getId());
						} catch (Exception e) {
	
							e.printStackTrace();
						}

						try {
							arrayTitles = videoListManager
									.getObjectList(getXmlPullParser());
						} catch (FileNotFoundException e) {

							e.printStackTrace();
						} catch (XmlPullParserException e) {
		
							e.printStackTrace();
						} catch (Exception e) {
	
							e.printStackTrace();
						}

						try {
							videoFileList = VideoListManager.getObjectList2(getXmlPullParser());
						} catch (FileNotFoundException e) {

							e.printStackTrace();
						} catch (XmlPullParserException e) {

							e.printStackTrace();
						} catch (Exception e) {

							e.printStackTrace();
						}

						dragListAdapter.notifyDataSetChanged();

						
					}
				})
				.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
					public void onClick(DialogInterface dialoginterface, int i) {
						
					}
				}).show();
				
				
			

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/***
	 * ��̬�޸�ListVIiw�ķ�λ.
	 * 
	 * @param start
	 *            ����ƶ���position
	 * @param down
	 *            �ɿ�ʱ���position
	 */
	private int invisilePosition = -1;
	private boolean isChanged = true;
	private boolean ShowItem = false;

	public void exchange(int startPosition, int endPosition, VideoFile videoFile) {
		System.out.println(startPosition + "--" + endPosition);
		// holdPosition = endPosition;
		Object startObject = getItem(startPosition);
		System.out.println(startPosition + "========" + endPosition);
		Log.d("ON", "startPostion ==== " + startPosition);
		Log.d("ON", "endPosition ==== " + endPosition);
		if (startPosition < endPosition) {
			arrayTitles.add(endPosition + 1, (String) startObject);
			arrayTitles.remove(startPosition);

			// ==========
			videoFileList.add(endPosition + 1, videoFile);
			videoFileList.remove(startPosition);

		} else {
			arrayTitles.add(endPosition, (String) startObject);
			arrayTitles.remove(startPosition + 1);

			// ==========
			videoFileList.add(endPosition, videoFile);
			videoFileList.remove(startPosition + 1);
		}
		isChanged = true;
		// notifyDataSetChanged();
	}

	public void exchangeCopy(int startPosition, int endPosition) {
		System.out.println(startPosition + "--" + endPosition);
		// holdPosition = endPosition;
		Object startObject = getCopyItem(startPosition);

		VideoFile videoFile = getCopyVideoFile(startPosition);

		System.out.println(startPosition + "========" + endPosition);
		Log.d("ON", "startPostion ==== " + startPosition);
		Log.d("ON", "endPosition ==== " + endPosition);
		if (startPosition < endPosition) {
			mCopyList.add(endPosition + 1, (String) startObject);
			mCopyList.remove(startPosition);

			// ==========

			CopyVideoFileList.add(endPosition + 1, videoFile);
			CopyVideoFileList.remove(startPosition);

		} else {
			mCopyList.add(endPosition, (String) startObject);
			mCopyList.remove(startPosition + 1);

			// ==========
			CopyVideoFileList.add(endPosition, videoFile);
			CopyVideoFileList.remove(startPosition + 1);
		}

		isChanged = true;
		// notifyDataSetChanged();
	}

	public Object getCopyItem(int position) {
		return mCopyList.get(position);
	}

	public VideoFile getCopyVideoFile(int position) {

		return CopyVideoFileList.get(position);
	}

	@Override
	public int getCount() {
		return arrayTitles.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayTitles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addDragItem(int start, Object obj, VideoFile videoFile) {
		Log.i(TAG, "start" + start);
		String title = arrayTitles.get(start);
		arrayTitles.remove(start);// ɾ������
		arrayTitles.add(start, (String) obj);// ����ɾ����

		// ===========
		videoFileList.remove(start);
		videoFileList.add(start, videoFile);

	}

	private ArrayList<String> mCopyList = new ArrayList<String>();

	private ArrayList<VideoFile> CopyVideoFileList = new ArrayList<VideoFile>();

	public void copyList() {
		mCopyList.clear();
		for (String str : arrayTitles) {
			mCopyList.add(str);
		}

		// ======

		CopyVideoFileList.clear();

		for (VideoFile videoFile : videoFileList) {
			CopyVideoFileList.add(videoFile);
		}
	}

	public void pastList() {
		arrayTitles.clear();
		for (String str : mCopyList) {
			arrayTitles.add(str);
		}

		videoFileList.clear();
		for (VideoFile videoFile : CopyVideoFileList) {
			videoFileList.add(videoFile);
		}

		PlayerActivity.SetVideoFileList_Public(videoFileList);
	}

	private boolean isSameDragDirection = true;
	private int lastFlag = -1;
	private int height;
	private int dragPosition = -1;

	public void setIsSameDragDirection(boolean value) {
		isSameDragDirection = value;
	}

	public void setLastFlag(int flag) {
		lastFlag = flag;
	}

	public void setHeight(int value) {
		height = value;
	}

	public void setCurrentDragPosition(int position) {
		dragPosition = position;
	}

	public Animation getFromSelfAnimation(int x, int y) {
		TranslateAnimation go = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
		go.setInterpolator(new AccelerateDecelerateInterpolator());
		go.setFillAfter(true);
		go.setDuration(100);
		go.setInterpolator(new AccelerateInterpolator());
		return go;
	}

	public Animation getToSelfAnimation(int x, int y) {
		TranslateAnimation go = new TranslateAnimation(Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y,
				Animation.RELATIVE_TO_SELF, 0);
		go.setInterpolator(new AccelerateDecelerateInterpolator());
		go.setFillAfter(true);
		go.setDuration(100);
		go.setInterpolator(new AccelerateInterpolator());
		return go;
	}

}