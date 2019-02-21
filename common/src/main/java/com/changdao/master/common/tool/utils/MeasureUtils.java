package com.changdao.master.common.tool.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MeasureUtils {
    private static MeasureUtils measureUtil;

    public static MeasureUtils init() {
        if (measureUtil == null) {
            synchronized (MeasureUtils.class) {
                if (measureUtil == null) {
                    measureUtil = new MeasureUtils();
                }
            }
        }
        return measureUtil;
    }
    /**
     * 设置GridView的高度
     */
    public void setGridViewHeight(GridView gridView, int columns) {
        int count = 0;
        int height;
        int maxHeight = 0;
        int lines = 0;
        if (gridView != null) {
            //切记--此处获得子view数需要使用adapter获得，直接使用gridView.getChildCount();获得为0
            count = gridView.getAdapter().getCount();
            //取行数，对小数进行向上取整
            lines = (int) Math.ceil((double) count / (double) columns);

        }
        for (int i = 0; i < count; i++) {
            //此处获得子View也得使用下面方法，使用gridView.getChildViewAt();报错
            View childView = gridView.getAdapter().getView(i, null, gridView);
            childView.measure(0, 0);
            height = childView.getMeasuredHeight();
            if (maxHeight <= height) {
                maxHeight = height;
            }
        }
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) gridView.getLayoutParams();
        layoutParams.height = lines * (maxHeight + gridView.getVerticalSpacing());
        // 设置margin----MarginLayoutParams
        ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(0, 30, 0, 30);
        gridView.setLayoutParams(layoutParams);
    }

    /**
     * 计算ListView的高度然后设置高度
     */
    public void setListViewHeight(ListView listView) {
        if (listView == null) {
            return;
        }
        int counts;
        int height = 0;
        Adapter adapter = listView.getAdapter();
        counts = adapter.getCount();
        for (int i = 0; i < counts; i++) {
            //adapter的父布局必须是LinearLayout,因为只有它才有measure方法
            View view = adapter.getView(i, null, listView);
            view.measure(0, 0);
            if (height < view.getMeasuredHeight()) {
                height = view.getMeasuredHeight();
            }
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        params.height = counts * height + (counts - 1) * listView.getDividerHeight();
        listView.setLayoutParams(params);
        listView.setSelection(0);
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     */
    public  int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    /**
     * 获得手机屏幕的宽
     *
     * @param mActivity
     * @return
     */
    public int getPhoneScreenWidth(Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获得手机屏幕的高
     *
     * @param mActivity
     * @return
     */
    public int getPhoneScreenHeight(Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获得手机屏幕和状态栏总高
     *
     * @param mActivity
     * @return
     */
    public int getPhoneScreenAndBarHeight(Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels + getStatusBarHeight(mActivity);
    }

    /**
     * 字符串长度---汉字两个长度，字母一个长度
     *
     * @param value
     * @return
     */
    public int stringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
