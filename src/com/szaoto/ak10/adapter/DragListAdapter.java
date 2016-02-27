package com.szaoto.ak10.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.szaoto.ak10.R;
import com.szaoto.ak10.entity.VideoFile;

/**
 * ������DragListAdapter
 * ���ܣ��Զ�����ק����������
 * ���ߣ�zhangsj
 * �������ڣ�2014��5��14�� 
 */
public class DragListAdapter extends BaseAdapter {
	
	//private static boolean flags = false;

	private ArrayList<VideoFile> arrayVideoFile;
	private Context context;
	public DragListAdapter(Context context, ArrayList<VideoFile> arrayVideoFile) {
		this.context = context;
		this.arrayVideoFile = arrayVideoFile;
	}
	/**
	 * ������ �� �� ���� View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			ViewHolder viewHolder = null;

			if (convertView == null) {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.drag_list_item, null);

				viewHolder = new ViewHolder();

				viewHolder.drag_list_item_text1 = (TextView) convertView
						.findViewById(R.id.itemmsg);

//				viewHolder.imageView_ThumbnailImage = (ImageView) convertView
//						.findViewById(R.id.imageview);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// ==========================

			VideoFile videoFile = arrayVideoFile.get(position);

			viewHolder.drag_list_item_text1.setText((position + 1) + "-"
					+ videoFile.getFileName());

			// viewHolder.imageView_ThumbnailImage = new ImageView(context);

//			String imagePrimaryKey = videoFile.getId();

//			String filePath = videoFile.getFilePath();

			// ���� �ļ� uuid Ψһֵ���ж� �ڴ� ͼƬ�б��У��Ƿ���ڣ��������� �ļ��� ����ͼ
//			if (HomePageActivity.hashMapImage_Added.containsKey(imagePrimaryKey) ) {
//
//				Bitmap tempBitmap = HomePageActivity.hashMapImage_Added
//						.get(imagePrimaryKey);
//
//				// int CacheImageCount =
//				// HomePageActivity.hashMapImage_Added.size();
//
//				viewHolder.imageView_ThumbnailImage.setImageBitmap(tempBitmap);
//
//			} else {
//				
//				// ���� �첽���� �������ļ�·������ȡSd���� ��Ƶ�ļ���ͼƬ�ļ� �� ����ͼ
////
////				if (viewHolder.imageAsyncTask != null) {
////
////					// �����������Ҫ��������ִ�� ȡ���߼�������ֱ����ת�� ��������߼�����
////					viewHolder.imageAsyncTask.cancel(false);
////				}
////
////				viewHolder.imageAsyncTask = viewHolder.new ImageAsyncTask();
////
////				viewHolder.imageAsyncTask.executeOnExecutor(
////						AsyncTask.THREAD_POOL_EXECUTOR, imagePrimaryKey,
////						filePath);
//			}

//			return convertView;

		} catch (Exception e) {

			e.printStackTrace();

			// Log.e("�쳣--1", e.getMessage());

//			return convertView;
		}
		return convertView;
	}

	/**
	 * ���� ImageView �ؼ� ��ʾ ��ý���ļ� ����ͼ
	 * 
	 * @param imageView
	 *            ͼƬ�ؼ�����
	 * @param videoPath
	 *            ��ý���ļ�·������Ƶ/ͼƬ��
	 * @param width
	 * @param height
	 * @throws Exception
	 */
	/*
	private void SetMediaFileThumbnail(ImageView imageView,
			String imagePrimaryKey, String filePath, int width, int height)
			throws Exception {

		File file = new File(filePath);

		Bitmap tempBitmap = null;

		if (file.exists()) {
			// ���� �ļ� uuid Ψһֵ���ж� �ڴ� ͼƬ�б��У��Ƿ���ڣ��������� �ļ��� ����ͼ
			if (PlayerActivity.hashMapImage_Added.containsKey(imagePrimaryKey)) {

				tempBitmap = PlayerActivity.hashMapImage_Added
						.get(imagePrimaryKey);

				imageView.setImageBitmap(tempBitmap);
			} else {

				// ��������ڣ��� ���� ��ȡ�������ӵ� �ڴ� ͼƬ�б���
				// �� ��Ƶ�ļ���ͼƬ�ļ� ����
//				if (MediaFileFilter.isVideoFile(file)) {
//
//					tempBitmap = MediaImageTool.getVideoThumbnail(
//							file.getPath(), width, height,
//							MediaStore.Images.Thumbnails.MICRO_KIND);
//				}
//
//				if (MediaFileFilter.isImageFile(file) && !flags) {
//
//					tempBitmap = MediaImageTool.getImageThumbnail(
//							file.getPath(), width, height);
//					flags = true;
//				}
//
//				imageView.setImageBitmap(tempBitmap);
//
//				HomePageActivity.hashMapImage_Added.put(imagePrimaryKey,
//						tempBitmap);
			}

		} else {
			// �����ļ��� �� ͼƬ�ؼ����Ѿ������� Ĭ��ͼƬ
			// imageView.setImageBitmap(HomePageActivity.bitmap);

			// �� �ļ������ڣ������� Ĭ��ͼƬ
			PlayerActivity.hashMapImage_Added.put(imagePrimaryKey,
					PlayerActivity.bitmap);
		}
	}
*/
	/**
	 * ���� ImageView �ؼ� ��ʾ ��ý���ļ� ����ͼ
	 * 
	 * @param imageView
	 *            ͼƬ�ؼ�����
	 * @param videoPath
	 *            ��ý���ļ�·������Ƶ/ͼƬ��
	 * @param width
	 * @param height
	 * @throws Exception
	 */
	/*
	private Bitmap SetMediaFileThumbnail2(String imagePrimaryKey,
			String filePath, int width, int height) throws Exception {

		File file = new File(filePath);

		Bitmap tempBitmap = null;

		if (file.exists()) {

			// ���� �ļ� uuid Ψһֵ���ж� �ڴ� ͼƬ�б��У��Ƿ���ڣ��������� �ļ��� ����ͼ
			if (PlayerActivity.hashMapImage_Added.containsKey(imagePrimaryKey)) {

				tempBitmap = PlayerActivity.hashMapImage_Added
						.get(imagePrimaryKey);

			} else {

//				// ��������ڣ��� ���� ��ȡ�������ӵ� �ڴ� ͼƬ�б���
//				// �� ��Ƶ�ļ���ͼƬ�ļ� ����
//				if (MediaFileFilter.isVideoFile(file)) {
//
//					tempBitmap = MediaImageTool.getVideoThumbnail(
//							file.getPath(), width, height,
//							MediaStore.Images.Thumbnails.MICRO_KIND);
//				}
//
//				if (MediaFileFilter.isImageFile(file)) {
//
//					tempBitmap = MediaImageTool.getImageThumbnail(
//							file.getPath(), width, height);
//				}

			}

		} else {

			// �����ļ��� �� ͼƬ�ؼ����Ѿ������� Ĭ��ͼƬ
			// imageView.setImageBitmap(HomePageActivity.bitmap);

			// �� �ļ������ڣ������� Ĭ��ͼƬ
			// HomePageActivity.hashMapImage_Added.put(imagePrimaryKey,
			// HomePageActivity.bitmap);

			tempBitmap = PlayerActivity.bitmap;
		}

		return tempBitmap;
	}
*/
	public Object getCopyItem(int position) {
		return arrayVideoFile.get(position);
	}

	@Override
	public int getCount() {
		return arrayVideoFile.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayVideoFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ��ȡ ����ͼ �߳�
	 * 
	 * @author Administrator
	 * 
	 */
	/*
	private class GetThumbnailImageThreed extends HandlerThread {

		private int width;
		private int height;
		private String filePath;
		private String imagePrimaryKey;
		private ImageView imageView;

		public GetThumbnailImageThreed(ImageView imageView,
				String imagePrimaryKey, String filePath, int width, int height,
				String name) {
			super(name);

			this.width = width;
			this.height = height;
			this.filePath = filePath;
			this.imagePrimaryKey = imagePrimaryKey;
			this.imageView = imageView;
		}

		@Override
		public void run() {

			try {

				SetMediaFileThumbnail(imageView, imagePrimaryKey, filePath,
						width, height);

			} catch (Exception e) {				
				e.printStackTrace();

				Log.e("�쳣--2", e.getMessage());
			}

		}
	}
*/
	private class ViewHolder {

//		ImageView imageView_ThumbnailImage;
		TextView drag_list_item_text1;

		/**
		 * �첽��������ͼ ��
		 */
//		ImageAsyncTask imageAsyncTask;

		/**
		 * �첽��������ͼ ��
		 * 
		 * @author Administrator
		 * 
		 */
//		imageView_ThumbnailImage.setBackgroundResource(R.drawable.video);
		
//		class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
//
//			/**
//			 * ��ʼ����
//			 */
//			@Override
//			protected Bitmap doInBackground(String... params) {
//
//				try {
//					String imagePrimaryKey = params[0];
//					String filePath = params[1];
//
//					Bitmap tempBitmap = 
//							SetMediaFileThumbnail2(imagePrimaryKey,
//							filePath, HomePageActivity.imageDefaultWidth,
//							HomePageActivity.imageDefaultWidth);
//
//					HomePageActivity.hashMapImage_Added.put(imagePrimaryKey,
//							tempBitmap);
//
//					return tempBitmap;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//
//					return HomePageActivity.bitmap;
//				}
//			}
//
//			/**
//			 * �������
//			 * 
//			 * @param bm
//			 */
//			@Override
//			protected void onPostExecute(Bitmap bitmap) {
//
//				try {
//
////					imageView_ThumbnailImage.setImageBitmap(bitmap);
//					imageView_ThumbnailImage.setBackgroundResource(R.drawable.video);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//
//					Log.e("�쳣--3", e.getMessage());
//				}
//
//			}
//
//		}
	}

}