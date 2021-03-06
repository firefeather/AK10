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
 * 类名：DragListAdapter
 * 功能：自定义拖拽排序适配器
 * 作者：zhangsj
 * 创建日期：2014年5月14日 
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
	 * 创建新 的 或 复用 View
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

			// 根据 文件 uuid 唯一值，判断 内存 图片列表中，是否存在（包含）该 文件的 缩略图
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
//				// 启动 异步任务 ，根据文件路径，读取Sd卡中 视频文件或图片文件 的 缩略图
////
////				if (viewHolder.imageAsyncTask != null) {
////
////					// 如果存在任务，要设置任务执行 取消逻辑方法，直接跳转到 处理完成逻辑方法
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

			// Log.e("异常--1", e.getMessage());

//			return convertView;
		}
		return convertView;
	}

	/**
	 * 设置 ImageView 控件 显示 多媒体文件 缩略图
	 * 
	 * @param imageView
	 *            图片控件对象
	 * @param videoPath
	 *            多媒体文件路径（视频/图片）
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
			// 根据 文件 uuid 唯一值，判断 内存 图片列表中，是否存在（包含）该 文件的 缩略图
			if (PlayerActivity.hashMapImage_Added.containsKey(imagePrimaryKey)) {

				tempBitmap = PlayerActivity.hashMapImage_Added
						.get(imagePrimaryKey);

				imageView.setImageBitmap(tempBitmap);
			} else {

				// 如果不存在，则 进行 读取，并添加到 内存 图片列表中
				// 该 视频文件或图片文件 存在
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
			// 布局文件中 的 图片控件，已经设置了 默认图片
			// imageView.setImageBitmap(HomePageActivity.bitmap);

			// 该 文件不存在，则设置 默认图片
			PlayerActivity.hashMapImage_Added.put(imagePrimaryKey,
					PlayerActivity.bitmap);
		}
	}
*/
	/**
	 * 设置 ImageView 控件 显示 多媒体文件 缩略图
	 * 
	 * @param imageView
	 *            图片控件对象
	 * @param videoPath
	 *            多媒体文件路径（视频/图片）
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

			// 根据 文件 uuid 唯一值，判断 内存 图片列表中，是否存在（包含）该 文件的 缩略图
			if (PlayerActivity.hashMapImage_Added.containsKey(imagePrimaryKey)) {

				tempBitmap = PlayerActivity.hashMapImage_Added
						.get(imagePrimaryKey);

			} else {

//				// 如果不存在，则 进行 读取，并添加到 内存 图片列表中
//				// 该 视频文件或图片文件 存在
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

			// 布局文件中 的 图片控件，已经设置了 默认图片
			// imageView.setImageBitmap(HomePageActivity.bitmap);

			// 该 文件不存在，则设置 默认图片
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
	 * 获取 缩略图 线程
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

				Log.e("异常--2", e.getMessage());
			}

		}
	}
*/
	private class ViewHolder {

//		ImageView imageView_ThumbnailImage;
		TextView drag_list_item_text1;

		/**
		 * 异步加载缩略图 类
		 */
//		ImageAsyncTask imageAsyncTask;

		/**
		 * 异步加载缩略图 类
		 * 
		 * @author Administrator
		 * 
		 */
//		imageView_ThumbnailImage.setBackgroundResource(R.drawable.video);
		
//		class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
//
//			/**
//			 * 开始处理
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
//			 * 处理完成
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
//					Log.e("异常--3", e.getMessage());
//				}
//
//			}
//
//		}
	}

}
