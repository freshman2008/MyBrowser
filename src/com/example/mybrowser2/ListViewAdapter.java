package com.example.mybrowser2;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context; // 运行上下文
	//private List<Map<String, Object>> listItems; // 商品信息集合
	List<HistoryItem> listItems;
	private LayoutInflater listContainer; // 视图容器
	private boolean[] hasChecked; // 记录商品选中状态

	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
		public TextView url;
	}

	public ListViewAdapter(Context context, List<HistoryItem> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		if (listItems != null) {
			return listItems.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (listItems != null) {
			return listItems.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.history_list_item,
					null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.imageItem);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.titleItem);
			listItemView.url = (TextView) convertView
					.findViewById(R.id.urlItem);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		//listItemView.image.setBackgroundResource((Integer) listItems.get(
		//		position).get("image"));
		listItemView.image.setBackgroundResource(R.drawable.ic_launcher);
		listItemView.title.setText((String) listItems.get(position).getTitle());
		listItemView.url.setText((String) listItems.get(position).getUrl());

		return convertView;
	}
}
