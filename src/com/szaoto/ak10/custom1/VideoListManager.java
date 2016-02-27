package com.szaoto.ak10.custom1;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.entity.VideoListService;
/**
 * ��Ƶ���Ϲ���
 * @author zhangsj
 *
 */
public class VideoListManager {

	static VideoListService videoListService = new VideoListService();

	public ArrayList<String> getObjectList(XmlPullParser xmlPullParser)
			throws Exception {

		ArrayList<String> dataList = new ArrayList<String>();

		List<Object> list = videoListService.getObjectList(xmlPullParser);

		VideoFile videoFile;

		int i = 0;

		for (Object object : list) {

			i++;

			videoFile = (VideoFile) object;

			dataList.add(i + "-" + videoFile.getFileName());
		}

		return dataList;
	}
   //����xml�ļ�����ӵ�������
	public static ArrayList<VideoFile> getObjectList2(XmlPullParser xmlPullParser)
			throws Exception {
		ArrayList<VideoFile> dataList = new ArrayList<VideoFile>();

		List<Object> list = videoListService.getObjectList(xmlPullParser);

		for (Object object : list) {

			dataList.add((VideoFile) object);

		}

		return dataList;
	}
    //ͨ��������ȡ��Ƶ��Ϣ
	public VideoFile getVideoInfoByIndex(int currSelectedPosition,
			String filePath) throws Exception {

		return videoListService.getVideoInfoByIndex(currSelectedPosition,
				filePath);
	}

}
