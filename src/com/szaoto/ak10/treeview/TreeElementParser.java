package com.szaoto.ak10.treeview;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 类名：TreeElementParser
 * 功能：树节点解析
 * 创建日期：2014年8月13日
 * 创建者：zhangshj
 * 修改者，修改日期，修改内容
 */
public class TreeElementParser {
	private static final String TAG = "TreeElementParser";
	/**
	 * 
	 * TreeElement的属性个数,可根据实际情况进行改动
	 */
	private static final int TREE_ELEMENT_ATTRIBUTE_NUM = 7;
	/**
	 * 把节点字符串信息集合解析成节点集合 这里的解析可根据实际情况进行改动
	 * @param list
	 * @return
	 */
	public static List<TreeElement> getTreeElements(List<String> list) {
		if (list == null) {
			Log.d(TAG,"the string list getted from solarterm.properties by ResManager.loadTextRes is null");
			return null;
		}
		int elementNum = list.size();
		List<TreeElement> treeElements = new ArrayList<TreeElement>();
		String info[] = new String[TREE_ELEMENT_ATTRIBUTE_NUM];
		for (int i = 0; i < elementNum; i++) {// 读取树形结构节点数
			if (treeElements == null) {
				treeElements = new ArrayList<TreeElement>();
			}
			info = list.get(i).split("=");
			TreeElement element = new TreeElement();
			element.setId(info[0]);
			element.setLevel(Integer.valueOf(info[1]));
			element.setTitle(info[2]);
			element.setFold(Boolean.valueOf(info[3]));
			element.setHasChild(Boolean.valueOf(info[4]));
			element.setHasParent(Boolean.valueOf(info[5]));
			element.setParentId(info[6]);
			Log.d(TAG, "add a TreeElement: " + element.toString());
			treeElements.add(element);
		}
		return treeElements;
	}
}
