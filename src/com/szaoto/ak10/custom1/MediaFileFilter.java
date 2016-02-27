package com.szaoto.ak10.custom1;

import java.io.File;

/**
 * ������MediaFileFilter 
 * ���ܣ�ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� ��Ƶ�ļ� 
 * ���ߣ�zhangsj 
 * �������ڣ�2014��5��8��
 * �޸��ߣ��޸����ݣ��޸����� 
 * �޸��ߣ�zhangsj 
 * �޸����� ��2014��6��10�� 
 * �޸����ݣ������������ļ�ͼ�����ʾ
 */
public class MediaFileFilter {

	public static boolean isVideoFile(File file) {

		// ֧��ɸѡ�ļ���ʽ��MP4\MPEG\PNG\JPG\BMP

		return (file.getName().endsWith(".mp4")
				|| file.getName().endsWith(".MP4")
				|| file.getName().endsWith(".MPEG")
				|| file.getName().endsWith(".AVI")
				|| file.getName().endsWith(".avi")
				|| file.getName().endsWith(".mpg")
				|| file.getName().endsWith(".flv")
				|| file.getName().endsWith(".wma")
				|| file.getName().endsWith(".mov")
				|| file.getName().endsWith(".wmv")
				|| file.getName().endsWith(".rmvb")
				|| file.getName().endsWith(".rm")
				|| file.getName().endsWith(
				".3GP")

		);

	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� ��Ƶ�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isMusicFile(File file) {

		// ֧��ɸѡ�ļ���ʽ��MP4\MPEG\PNG\JPG\BMP

		return (file.getName().endsWith(".mp3")
				|| file.getName().endsWith(".MP3")
				|| file.getName().endsWith(".WAV")
				|| file.getName().endsWith(".wav")
				|| file.getName().endsWith(".MOD")
				|| file.getName().endsWith(".mod")
				|| file.getName().endsWith(".CD")
				|| file.getName().endsWith(".cd")
				|| file.getName().endsWith(".MID")
				|| file.getName().endsWith(".mid")
				|| file.getName().endsWith(".APE")
				|| file.getName().endsWith(".ape")
        		|| file.getName().endsWith(".MD")

		);

	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� ͼƬ�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(File file) {

		// ֧��ɸѡ�ļ���ʽ��MP4\MPEG\PNG\JPG\BMP

		return (file.getName().endsWith(".PNG")
				|| file.getName().endsWith(".png")
				|| file.getName().endsWith(".JPG")
				|| file.getName().endsWith(".jpg")
				|| file.getName().endsWith(".GIF")
				|| file.getName().endsWith(".gif")
				|| file.getName().endsWith(".BMP") 
				|| file.getName().endsWith(
				".bmp"));

	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� word�ļ�
	 * 
	 */
	public static boolean isDocFile(File file) {

		return (file.getName().endsWith(".doc") || file.getName().endsWith(
				".docx"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� java�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isJavaFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".JAVA") 
				|| file.getName().endsWith(".java"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� apk�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isApkFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".APK") || file.getName().endsWith(
				".apk"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� xls����ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isXlsFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".xls")
				|| file.getName().endsWith(".XLS")
				|| file.getName().endsWith(".xlsx") || file.getName().endsWith(
				".XLSX"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� txt�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTxtFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".txt") 
				|| file.getName().endsWith(
				".TXT"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� ѹ���ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isRarFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".rar") 
				|| file.getName().endsWith(
				".RAR"));
	}

	/**
	 * ͨ�� �ļ� ��׺�����й���,�ж� ���ļ� �Ƿ��� xml�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isXmlFile(File file) {
		// TODO Auto-generated method stub
		return (file.getName().endsWith(".xml") || file.getName().endsWith(
				".XML"));
	}

}
