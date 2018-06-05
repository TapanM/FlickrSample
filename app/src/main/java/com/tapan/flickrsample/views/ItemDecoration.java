package com.tapan.flickrsample.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int pos = parent.getChildLayoutPosition(view);
        outRect.left = space;
        outRect.right = space;

        if (pos == 0||pos == 1) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}