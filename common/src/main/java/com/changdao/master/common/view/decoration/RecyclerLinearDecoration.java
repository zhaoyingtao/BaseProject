package com.changdao.master.common.view.decoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zyt on 2018/7/31.
 * RecyclerView使用Linear间距设置：
 */

public class RecyclerLinearDecoration extends RecyclerView.ItemDecoration {
    private int itemSpace;
    private int orientation = LinearLayoutManager.VERTICAL;
    private int spacePosition;

    /**
     * @param itemSpace   item间隔
     * @param orientation 每行item的个数
     */
    public RecyclerLinearDecoration(int itemSpace, int orientation) {
        this(itemSpace, orientation, 1);
    }

    /**
     * @param itemSpace     item间隔
     * @param orientation   每行item的个数
     * @param spacePosition 间隔添加位置 0 前面／左边 1 后面／右边 2 上下／左右都设置相同的间隔
     */
    public RecyclerLinearDecoration(int itemSpace, int orientation, int spacePosition) {
        this.itemSpace = itemSpace;
        this.orientation = orientation;
        this.spacePosition = spacePosition;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (orientation == LinearLayoutManager.VERTICAL) {
            if (spacePosition == 0) {
                outRect.top = itemSpace;
            } else if (spacePosition == 2) {
                outRect.top = itemSpace;
                outRect.bottom = itemSpace;
            } else {
                outRect.bottom = itemSpace;
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (spacePosition == 0) {
                outRect.left = itemSpace;
            } else if (spacePosition == 2) {
                outRect.left = itemSpace;
                outRect.right = itemSpace;
            } else {
                outRect.right = itemSpace;
            }
        }
    }
}