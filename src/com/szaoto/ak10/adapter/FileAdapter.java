package com.szaoto.ak10.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.szaoto.ak10.R.drawable;
import com.szaoto.ak10.R.id;
import com.szaoto.ak10.R.layout;
import com.szaoto.ak10.custom1.MediaFileFilter;
import com.szaoto.ak10.entity.VideoFile;
import com.szaoto.ak10.player.PlayerActivity;

/**
 * ����:FileAdapter 
 * ����:�ļ����������� 
 * ���ߣ�zhangsj 
 * �������ڣ�2014��5��14�� 
 * �޸��ߣ��޸����ݣ��޸�����
 * 
 */
public class FileAdapter extends BaseAdapter {

	List<File> fileList;
	Context context;
	/**
	 * 
	 * ��� ��ѡ�� ���� �ļ�ѡ��ѡ���ļ������ӵ� PlayerActivity.VideoFileList_Added �У�
	 * 
	 * ȡ��ѡ�У����� PlayerActivity.VideoFileList_Added �У�ɾ����Ӧ��VideoFile����
	 * 
	 * @author Administrator
	 * 
	 */
	private class SelectFile implements OnCheckedChangeListener {

		File file;

		public SelectFile(File file) {
			super();
			this.file = file;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				if (!isAddToHomePageActivityAddList(file)) {

					VideoFile videoFile = new VideoFile();
					videoFile.setId("");
					// ��ȡʱ��
					videoFile.setDuration("10");
					videoFile.setFileName(file.getName());
					videoFile.setFilePath(file.getPath());
					videoFile.setImagePath(file.getPath());
					videoFile.setSpecialEffect(" 1");

					PlayerActivity.VideoFileList_Added.add(videoFile);
				}

			} else {

				for (int i = 0; i < PlayerActivity.VideoFileList_Added.size(); i++) {
					if (PlayerActivity.VideoFileList_Added.get(i)
							.getFilePath().equals(file.getPath())) {
						PlayerActivity.VideoFileList_Added.remove(i);
					}
				}
			}
		}
	}

	public FileAdapter(List<File> fileList, Context context) {
		super();
		this.fileList = fileList;
		this.context = context;
	}

	@Override
	public int getCount() {

		return fileList.size();
	}

	@Override
	public Object getItem(int location) {

		return fileList.get(location);
	}

	@Override
	public long getItemId(int location) {

		return fileList.indexOf(getItem(location));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		File file = fileList.get(position);

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					layout.add_play_list_item, null);

			viewHolder = new ViewHolder();

			viewHolder.icon = (ImageView) convertView.findViewById(id.icon);
			viewHolder.name = (TextView) convertView.findViewById(id.name);
			viewHolder.checkBox_SelectedFile2 = (CheckBox) convertView
					.findViewById(id.checkBox_SelectedFile2);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.checkBox_SelectedFile2
				.setOnCheckedChangeListener(new SelectFile(file));

		// ===========��ʼ��ֵ========

		viewHolder.name.setText(file.getName());

		// VideoFile videoFile = new VideoFile();
		// videoFile.setFileName(file.getName());

		// viewHolder.checkBox_SelectedFile2.setChecked(false);
		// visibility�����ֵ 0��4�ֱ������ʾ�Ͳ���ʾ

		if (file.isDirectory()) {
			// ���ļ���
			viewHolder.icon.setImageResource(drawable.folder);
			viewHolder.checkBox_SelectedFile2.setVisibility(4);

		} else {
			if (MediaFileFilter.isVideoFile(file)) {
				// ����Ƶ�ļ�
				viewHolder.icon.setImageResource(drawable.video);

				viewHolder.checkBox_SelectedFile2.setVisibility(0);
				viewHolder.checkBox_SelectedFile2
						.setChecked(isAddToHomePageActivityAddList(file));
			} else if (MediaFileFilter.isImageFile(file)) {
				// ��ͼƬ�ļ�
				viewHolder.icon.setImageResource(drawable.jpeg);

				viewHolder.checkBox_SelectedFile2.setVisibility(0);

				viewHolder.checkBox_SelectedFile2
						.setChecked(isAddToHomePageActivityAddList(file));

			} else if (MediaFileFilter.isMusicFile(file)) {

				viewHolder.icon.setImageResource(drawable.music);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);

			} else if (MediaFileFilter.isXlsFile(file)) {

				viewHolder.icon.setImageResource(drawable.xls);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);

			} else if (MediaFileFilter.isDocFile(file)) {

				viewHolder.icon.setImageResource(drawable.doc);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);

			} else if (MediaFileFilter.isJavaFile(file)) {

				viewHolder.icon.setImageResource(drawable.java);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);

			} else if (MediaFileFilter.isApkFile(file)) {

				viewHolder.icon.setImageResource(drawable.apk);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);

			} else if (MediaFileFilter.isTxtFile(file)) {

				viewHolder.icon.setImageResource(drawable.txt);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);
			} else if (MediaFileFilter.isRarFile(file)) {

				viewHolder.icon.setImageResource(drawable.rar);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);
			}else if (MediaFileFilter.isXmlFile(file)) {

				viewHolder.icon.setImageResource(drawable.xml);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);
			}
			else {
				// �����ļ�
				viewHolder.icon.setImageResource(drawable.file);
				// VISIBLE = 0;
				// INVISIBLE = 4;
				viewHolder.checkBox_SelectedFile2.setVisibility(4);
			}

		}
		return convertView;
	}
	/**
	 * �ж� �Ƿ��Ѿ����ӵ� �ڴ�� ��ѡ�б���
	 * 
	 * @param file
	 * @return
	 */
	private boolean isAddToHomePageActivityAddList(File file) {

		boolean isadd = false;

		for (VideoFile videoFile : PlayerActivity.VideoFileList_Added) {

			if (videoFile.getFilePath().equals(file.getPath())) {
				isadd = true;
			}
		}
		return isadd;
	}

	private class ViewHolder {
		ImageView icon;

		TextView name;

		CheckBox checkBox_SelectedFile2;

	}
}