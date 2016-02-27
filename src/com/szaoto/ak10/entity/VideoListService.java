package com.szaoto.ak10.entity;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.szaoto.ak10.util.XmlTool;
/**
 * 类名：VideoListService
 * 功能：
 * 作者：zhangsj
 * 创建日期：2014年5月8日
 */
public class VideoListService {

	public List<Object> getObjectList(XmlPullParser xmlPullParser)
			throws Exception {

		// private int id;
		// private String fileName;
		// private String filePath;
		// private String imagePath;

		List<String> fields = new ArrayList<String>();

		fields.add("id");
		fields.add("fileName");
		fields.add("filePath");
		fields.add("imagePath");
		fields.add("duration");
		fields.add("specialEffect");

		List<String> elements = new ArrayList<String>();
		elements.add("id");
		elements.add("fileName");
		elements.add("filePath");
		elements.add("imagePath");
		elements.add("duration");
		elements.add("specialEffect");

		String itemElement = "VideoFile";

		return XmlTool.parseByXmlId(VideoFile.class, fields, elements,
				itemElement, xmlPullParser);

	}

	public VideoFile getVideoInfoByIndex(int currSelectedPosition,
			String filePath) throws Exception {

		return XmlTool.getVideoInfoByIndex(currSelectedPosition, filePath);
	}
}
