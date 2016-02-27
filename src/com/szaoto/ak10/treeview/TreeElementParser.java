package com.szaoto.ak10.treeview;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * ������TreeElementParser
 * ���ܣ����ڵ����
 * �������ڣ�2014��8��13��
 * �����ߣ�zhangshj
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class TreeElementParser {
	private static final String TAG = "TreeElementParser";
	/**
	 * 
	 * TreeElement�����Ը���,�ɸ���ʵ��������иĶ�
	 */
	private static final int TREE_ELEMENT_ATTRIBUTE_NUM = 7;
	/**
	 * �ѽڵ��ַ�����Ϣ���Ͻ����ɽڵ㼯�� ����Ľ����ɸ���ʵ��������иĶ�
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
		for (int i = 0; i < elementNum; i++) {// ��ȡ���νṹ�ڵ���
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