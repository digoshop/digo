package com.bjut.servicedog.servicedog.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bjut.servicedog.servicedog.R;


public class PopupMenu extends PopupWindow implements OnClickListener {

	private Activity activity;
	private View popView;
	private View v_item1,v_item2,v_item3;
	private int dimens270 = 0;


	private OnItemClickListener onItemClickListener;

	public enum MENUITEM {
		ITEM1, ITEM2,ITEM3,ITEM4
	}

	public PopupMenu(Activity activity) {
		super(activity);
		this.activity = activity;
		dimens270 = activity.getResources().getDimensionPixelOffset(R.dimen.base_dimen_270);
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.popup_menu, null);
		popView=v;
		this.setContentView(popView);// 把布局文件添加到popupwindow中
		this.setWidth(LayoutParams.MATCH_PARENT);// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
		this.setHeight(dimens270);
		this.setFocusable(true);// 获取焦点
		this.setTouchable(true); // 设置PopupWindow可触摸
		this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		// 获取选项卡
		v_item1 = popView.findViewById(R.id.ly_item1);
		v_item2 = popView.findViewById(R.id.ly_item2);
		v_item3 = popView.findViewById(R.id.ly_item3);
//		v_item4 = popView.findViewById(R.id.ly_item4);
		// 添加监听
		v_item1.setOnClickListener(this);
		v_item2.setOnClickListener(this);
		v_item3.setOnClickListener(this);
		//v_item4.setOnClickListener(this);

	}
	public PopupMenu(Activity activity,String all,String a,String b) {
		super(activity);
		//this.activity = activity;
//		LayoutInflater inflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflater.inflate(R.layout.popup_menu, null);
//		popView=v;
//		this.setContentView(popView);// 把布局文件添加到popupwindow中
//		this.setWidth(LayoutParams.MATCH_PARENT);// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
//		this.setHeight(dimens270);
//		this.setFocusable(true);// 获取焦点
//		this.setTouchable(true); // 设置PopupWindow可触摸
//		this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
//		ColorDrawable dw = new ColorDrawable(0x00000000);
//		this.setBackgroundDrawable(dw);
//		// 获取选项卡
//		v_item1 = popView.findViewById(R.id.ly_item1);
//		v_item2 = popView.findViewById(R.id.ly_item2);
//		v_item3 = popView.findViewById(R.id.ly_item3);
//		//v_item4 = popView.findViewById(R.id.ly_item4);
//
//		TextView first=(TextView)popView.findViewById(R.id.first);
//		first.setText(all);
//		TextView textView1=(TextView)popView.findViewById(R.id.second);
//		textView1.setText(a);
//		TextView textView=(TextView)popView.findViewById(R.id.third);
//		textView.setText(b);
//		// 添加监听
//		v_item1.setOnClickListener(this);
//		v_item2.setOnClickListener(this);
//		v_item3.setOnClickListener(this);
//		//v_item4.setVisibility(View.GONE);


		this.activity = activity;
		dimens270 = activity.getResources().getDimensionPixelOffset(R.dimen.base_dimen_270);
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popup_menu, null);
		this.setContentView(view);// 把布局文件添加到popupwindow中
		this.setWidth(LayoutParams.MATCH_PARENT);// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
		this.setHeight(dimens270);
		this.setFocusable(true);// 获取焦点
		this.setTouchable(true); // 设置PopupWindow可触摸
		this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);


		TextView first=(TextView)view.findViewById(R.id.first);
		first.setText(all);
		TextView textView1=(TextView)view.findViewById(R.id.second);
		textView1.setText(a);
		TextView textView=(TextView)view.findViewById(R.id.third);
		textView.setText(b);
		// 获取选项卡
		v_item1 = view.findViewById(R.id.ly_item1);
		v_item2 = view.findViewById(R.id.ly_item2);
		v_item3 = view.findViewById(R.id.ly_item3);
//		v_item4 = view.findViewById(R.id.ly_item4);
		// 添加监听
		v_item1.setOnClickListener(this);
		v_item2.setOnClickListener(this);
		v_item3.setOnClickListener(this);
		//v_item4.setOnClickListener(this);

	}

	/**
	 * 设置显示的位置
	 * 
	 * @param resourId
	 *            这里的x,y值自己调整可以
	 */
	public void showLocation(int resourId) {
		showAsDropDown(activity.findViewById(resourId), dip2px(activity, 0),
				dip2px(activity, 10));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MENUITEM menuitem = null;
		if (v == v_item1) {
			menuitem = MENUITEM.ITEM1;
		} else if (v == v_item2) {
			menuitem = MENUITEM.ITEM2;
		}else if (v == v_item3) {
			menuitem = MENUITEM.ITEM3;
		}
// else if (v == v_item4) {
//			menuitem = MENUITEM.ITEM4;
//		}
		if (onItemClickListener != null) {
			onItemClickListener.onClick(menuitem);
		}
		dismiss();
	}



	// dip转换为px
	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// 点击监听接口
	public interface OnItemClickListener {
		public void onClick(MENUITEM item );
	}

	// 设置监听
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
