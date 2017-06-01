package com.bjut.servicedog.servicedog.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by beibeizhu on 17/3/16.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceLeft;
    private int spaceRight;
    private int spaceTop;
    private int spaceBottom;

    public SpacesItemDecoration(int spaceTop, int spaceLeft, int spaceBottom, int spaceRight) {
        this.spaceLeft = spaceLeft;
        this.spaceRight = spaceRight;
        this.spaceTop = spaceTop;
        this.spaceBottom = spaceBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceLeft;
        outRect.right = spaceRight;
        outRect.bottom = spaceBottom;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = spaceTop;
    }
}