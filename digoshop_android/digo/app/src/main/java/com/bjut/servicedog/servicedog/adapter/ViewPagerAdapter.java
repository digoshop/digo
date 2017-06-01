package com.bjut.servicedog.servicedog.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by apple02 on 16/7/1.
 */
public class ViewPagerAdapter extends PagerAdapter {
    List<View> viewLists;
    private Context context;

    public ViewPagerAdapter(List<View> lists) {
        viewLists = lists;

    }

    //获得size

    public int getCount() {
        return viewLists.size();
    }


    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //销毁Item

    public void destroyItem(View view, int position, Object object)
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    //实例化Item

    public Object instantiateItem(View view, int position)
    {
        ((ViewPager) view).addView(viewLists.get(position), 0);
        return viewLists.get(position);
    }

}
