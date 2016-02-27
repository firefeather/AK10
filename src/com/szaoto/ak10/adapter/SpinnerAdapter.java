package com.szaoto.ak10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 
 * @author zhangsj
 *
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

	Context context;

	String[] items = new String[] {};

	public SpinnerAdapter(final Context context,

	final int textViewResourceId, final String[] objects) {

		super(context, textViewResourceId, objects);
		if(objects == null)
		{}
		else{
		this.items = objects;
		this.context = context;
		}

	}
	/*public SpinnerAdapter(final Context context,

			final int textViewResourceId) {

				super(context, textViewResourceId);

			//	this.items = objects;

				this.context = context;

			}*/

	
	/**
	 * 弹出的列表样式
	 * 
	 * 文本加单选按钮样式 ：android.R.layout.simple_spinner_dropdown_item
	 * 纯文本样式：android.R.layout.simple_spinner_item
	 * 还有很多......
	 * */

	@Override
	public View getDropDownView(int position, View convertView,

	ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent, false);
			
		}

		TextView tv = (TextView) convertView
				.findViewById(android.R.id.text1);
		if(items == null)
		{
			tv.setTextSize(30);
		}
		else{
		tv.setText(items[position]);
		// tv.setTextColor(Color.BLUE);
		tv.setTextSize(30);}
		return convertView;
	}

	/**
	 * Spinner的样式
	 * */
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
		}
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items[position]);
		//tv.setTextColor(Color.BLUE);
		tv.setTextSize(30);

		return convertView;

	}

}
