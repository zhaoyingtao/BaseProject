package com.changdao.master.common.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fancy on 2016/5/30.
 * 自定义viewPager，设置是否可以滑动
 */
public class CustomViewPager extends ViewPager {
    private boolean slideEnable=true;
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return slideEnable?super.onTouchEvent(ev):false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return slideEnable?super.onInterceptTouchEvent(ev):false;
    }

    /**
     * 设置是否可以滑动
     * @param slideEnable
     */
    public void setSlideEnable(boolean slideEnable) {
        this.slideEnable = slideEnable;
    }

    @Override
    public void setCurrentItem(int item) {
        //false:remove animation
        super.setCurrentItem(item,false);
    }

}
