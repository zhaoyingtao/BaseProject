package com.zyt.master.common.tool.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.changdao.master.common.R;
import com.zyt.master.common.tool.interfaze.CommonInterfaceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dhf on 2017/3/17.
 * 滚轮选择器
 */

public class WheelsUtils {
    /**
     * 选择时间
     *
     * @param mContext
     * @param title
     * @param dateType              时间类型 1 显示当前以前日期   2 显示当前以后日期
     * @param commonInterfaceHelper  实现deliveryDateValue方法即可
     */
    public static void showSelectTimeWheel(Context mContext, String title, int dateType, final CommonInterfaceHelper commonInterfaceHelper) {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        if (dateType == 2) {
            endCalendar.set(startCalendar.get(Calendar.YEAR) + 100, startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
        } else {
            startCalendar.set(startCalendar.get(Calendar.YEAR) - 100, startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
        }
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                commonInterfaceHelper.deliveryDateValue(date);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(22)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(mContext.getResources().getColor(R.color.wheel_title))//标题文字颜色
                .setSubmitColor(mContext.getResources().getColor(R.color.tt_red))//确定按钮文字颜色
                .setCancelColor(mContext.getResources().getColor(R.color.tt_red))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
//                .setRange(startYear, endYear)//默认是1900-2100年
//                .setDate(calendar)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startCalendar, endCalendar)//起始终止年月日设定
//                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }
}
