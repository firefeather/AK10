//package com.szaoto.ak10.util;
//
//import wseemann.media.FFmpegMediaMetadataRetriever;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.ThumbnailUtils;
//import android.util.Log;
//
///**
// * 类名：MediaImageTool
// * 功能：获取缩略图工具类
// * 作者：zhangsj
// * 创建日期：2014年5月8日
// * 修改者，修改内容，修改日期
// */
//public class MediaImageTool {
//
//	/**
//	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
//	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
//	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
//	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
//	 * 
//	 * @param imagePath
//	 *            图像的路径
//	 * @param width
//	 *            指定输出图像的宽度
//	 * @param height
//	 *            指定输出图像的高度
//	 * @return 生成的缩略图
//	 */
//	public static Bitmap getImageThumbnail(String imagePath, int width,
//			int height) throws Exception {
//		Bitmap bitmap = null;
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		// 获取这个图片的宽和高，注意此处的bitmap为null
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//		options.inJustDecodeBounds = false; // 设为 false
//		// 计算缩放比
//		int h = options.outHeight;
//		int w = options.outWidth;
//		int beWidth = w / width;
//		int beHeight = h / height;
//		int be = 1;
//		if (beWidth < beHeight) {
//			be = beWidth;
//		} else {
//			be = beHeight;
//		}
//		if (be <= 0) {
//			be = 1;
//		}
//		options.inSampleSize = be;
//		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		return bitmap;
//	}
//
//	/**
//	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
//	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
//	 * 
//	 * @param videoPath
//	 *            视频的路径
//	 * @param width
//	 *            指定输出视频缩略图的宽度
//	 * @param height
//	 *            指定输出视频缩略图的高度度
//	 * @param kind
//	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
//	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
//	 * @return 指定大小的视频缩略图
//	 */
//	public static Bitmap getVideoThumbnail(String videoPath, int width,
//			int height, int kind) throws Exception {
//		Bitmap bitmap = null;
//		// 获取视频的缩略图
////		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
////		// System.out.println("w" + bitmap.getWidth());
////		// System.out.println("h" + bitmap.getHeight());
////
////		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
////				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
////
////		if (bitmap == null) {
//
////			 FFmpegMediaMetadataRetriever fmmr = new
////			 FFmpegMediaMetadataRetriever();
//
//			 bitmap =
//			 getImageThumbnailByFFmpegMediaMetadataRetriever(videoPath,
//			 width, height);
//
////			bitmap = HomePageActivity.bitmap;
//
////		}
//
//		return bitmap;
//	}
//
//	/**
//	 * 根据 视频文件路径，通过 调用 C 获取 视频文件缩略图
//	 * 
//	 * @param videoPath
//	 * @return
//	 */
//	
//	private static Bitmap getImageThumbnailByFFmpegMediaMetadataRetriever(
//			String videoPath, int width, int height) {
//
//		FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
//		try {
//
//			fmmr.setDataSource(videoPath);
//
////			Bitmap b = fmmr.getFrameAtTime(4000000,
////					FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
//			Bitmap b = fmmr.getFrameAtTime();
//
//			if (b.getWidth() != width) {
//				b = ThumbnailUtils.extractThumbnail(b, width, height,
//						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//	 
//		}
//			if (b != null) {
//		
//				Log.i("视频缩略图", "缩略图提取 成功");
//
//				return b;
//			} else {
//				Log.e("视频缩略图", "缩略图提取 失败");
//			}
//		} catch (IllegalArgumentException ex) {
//			ex.printStackTrace();
//		} finally {
//			fmmr.release();
//		}
//		return null;
//	}
//	
//}
