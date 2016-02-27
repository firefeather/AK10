//package com.szaoto.ak10.util;
//
//import wseemann.media.FFmpegMediaMetadataRetriever;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.ThumbnailUtils;
//import android.util.Log;
//
///**
// * ������MediaImageTool
// * ���ܣ���ȡ����ͼ������
// * ���ߣ�zhangsj
// * �������ڣ�2014��5��8��
// * �޸��ߣ��޸����ݣ��޸�����
// */
//public class MediaImageTool {
//
//	/**
//	 * ����ָ����ͼ��·���ʹ�С����ȡ����ͼ �˷���������ô��� 1.
//	 * ʹ�ý�С���ڴ�ռ䣬��һ�λ�ȡ��bitmapʵ����Ϊnull��ֻ��Ϊ�˶�ȡ��Ⱥ͸߶ȣ�
//	 * �ڶ��ζ�ȡ��bitmap�Ǹ��ݱ���ѹ������ͼ�񣬵����ζ�ȡ��bitmap����Ҫ������ͼ�� 2.
//	 * ����ͼ����ԭͼ������û�����죬����ʹ����2.2�汾���¹���ThumbnailUtils��ʹ ������������ɵ�ͼ�񲻻ᱻ���졣
//	 * 
//	 * @param imagePath
//	 *            ͼ���·��
//	 * @param width
//	 *            ָ�����ͼ��Ŀ��
//	 * @param height
//	 *            ָ�����ͼ��ĸ߶�
//	 * @return ���ɵ�����ͼ
//	 */
//	public static Bitmap getImageThumbnail(String imagePath, int width,
//			int height) throws Exception {
//		Bitmap bitmap = null;
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//		options.inJustDecodeBounds = false; // ��Ϊ false
//		// �������ű�
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
//		// ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//		// ����ThumbnailUtils����������ͼ������Ҫָ��Ҫ�����ĸ�Bitmap����
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		return bitmap;
//	}
//
//	/**
//	 * ��ȡ��Ƶ������ͼ ��ͨ��ThumbnailUtils������һ����Ƶ������ͼ��Ȼ��������ThumbnailUtils������ָ����С������ͼ��
//	 * �����Ҫ������ͼ�Ŀ�͸߶�С��MICRO_KIND��������Ҫʹ��MICRO_KIND��Ϊkind��ֵ���������ʡ�ڴ档
//	 * 
//	 * @param videoPath
//	 *            ��Ƶ��·��
//	 * @param width
//	 *            ָ�������Ƶ����ͼ�Ŀ��
//	 * @param height
//	 *            ָ�������Ƶ����ͼ�ĸ߶ȶ�
//	 * @param kind
//	 *            ����MediaStore.Images.Thumbnails���еĳ���MINI_KIND��MICRO_KIND��
//	 *            ���У�MINI_KIND: 512 x 384��MICRO_KIND: 96 x 96
//	 * @return ָ����С����Ƶ����ͼ
//	 */
//	public static Bitmap getVideoThumbnail(String videoPath, int width,
//			int height, int kind) throws Exception {
//		Bitmap bitmap = null;
//		// ��ȡ��Ƶ������ͼ
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
//	 * ���� ��Ƶ�ļ�·����ͨ�� ���� C ��ȡ ��Ƶ�ļ�����ͼ
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
//				Log.i("��Ƶ����ͼ", "����ͼ��ȡ �ɹ�");
//
//				return b;
//			} else {
//				Log.e("��Ƶ����ͼ", "����ͼ��ȡ ʧ��");
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
