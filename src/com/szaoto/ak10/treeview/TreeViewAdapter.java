package com.szaoto.ak10.treeview;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.szaoto.ak10.R;
/**
 * ������TreeViewAdapter
 * ���ܣ����ڵ�������
 * �������ڣ�2014��8��13��
 * �����ߣ�zhangshj
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class TreeViewAdapter extends BaseAdapter {
	class ViewHolder {
		ImageView icon;
		TextView title;
	}

	Context context;
	ViewHolder holder;
	LayoutInflater inflater;
	List<TreeElement> elements;

	public TreeViewAdapter(Context context, List<TreeElement> elements) {
		this.context = context;
		this.elements = elements;
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int position) {
		return elements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * ---------------------- get holder------------------------
		 */
		if (convertView == null) {
			if (inflater == null) {
				inflater = LayoutInflater.from(context);
			}
			holder = null;
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.tree_view_item_layout, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.tree_view_item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.tree_view_item_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/**
		 * ---------------------- set holder--------------------------
		 */
		if (elements.get(position).isHasChild()) {// ���ӽڵ㣬Ҫ��ʾͼ��
			if (elements.get(position).isFold()) {
				holder.icon.setImageResource(R.drawable.tree_view_icon_fold);
			} else if (!elements.get(position).isFold()) {
				holder.icon.setImageResource(R.drawable.tree_view_icon_unfold);
			}
			holder.icon.setVisibility(View.VISIBLE);
		} else {// û���ӽڵ㣬Ҫ����ͼ��
			holder.icon.setImageResource(R.drawable.tree_view_icon_fold);
			holder.icon.setVisibility(View.INVISIBLE);
		}
		holder.icon.setPadding(25 * (elements.get(position).getLevel()), 0, 0,
				0);// ���ݲ㼶��������
		holder.title.setText(elements.get(position).getTitle());
		holder.title.setTextSize(40 - elements.get(position).getLevel() * 5); // ���ݲ㼶���������С
		return convertView;
	}
}
