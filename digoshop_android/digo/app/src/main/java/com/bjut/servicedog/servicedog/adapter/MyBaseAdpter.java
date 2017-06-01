package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseAdpter<T> extends BaseAdapter {

	private List<T> list;

	protected Context context;

	public MyBaseAdpter(Context context) {
		init(context, new ArrayList<T>());
	}

	public MyBaseAdpter(Context context, List<T> list) {
		init(context, list);
	}

	private void init(Context context, List<T> list) {
		this.list = list;
		this.context = context;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void removeItem(int position){
		this.list.remove(position);
		notifyDataSetChanged();
	}

	public void clear() {
		this.list.clear();
		notifyDataSetChanged();
	}

	public void addAll(List<T> list) {
		if (list != null) {
			this.list.addAll(list);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflate(getContentView());
		}
		onInitView(convertView, position);
		return convertView;
	}

	/** 加载布局 */
	private View inflate(int layoutResID) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(layoutResID, null);
		return view;
	}

	public abstract int getContentView();

	public abstract void onInitView(View view, int position);

	/**
	 * 
	 * @param view
	 *            converView
	 * @param id
	 *            控件的id
	 * @return 返回<t extends="" view="">
	 */
	@SuppressWarnings("unchecked")
	protected <E extends View> E get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (null == viewHolder) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (null == childView) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);

		}
		return (E) childView;
	}

}
