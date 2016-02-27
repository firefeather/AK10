package com.szaoto.ak10.entity;

import java.io.Serializable;
/**
 * 类名：VideoFile
 * 功能：实现序列化的视频实体文件
 * 作者：zhangsj
 * 创建日期：2014年5月8日
 */
public class VideoFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String fileName;
	private String filePath;
	private String imagePath;
	private String duration;
	private String specialEffect;

	public VideoFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VideoFile(String id, String fileName, String filePath,
			String imagePath, String duration, String specialEffect) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.filePath = filePath;
		this.imagePath = imagePath;
		this.duration = duration;
		this.specialEffect = specialEffect;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSpecialEffect() {
		return specialEffect;
	}

	public void setSpecialEffect(String specialEffect) {
		this.specialEffect = specialEffect;
	}

}
