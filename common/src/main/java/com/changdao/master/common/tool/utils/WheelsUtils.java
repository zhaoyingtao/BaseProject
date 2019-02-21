package com.changdao.master.common.tool.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.changdao.master.common.R;
import com.changdao.master.common.tool.interfaze.CommonInterfaceHelper;

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
     * @param commonInterfaceHelper
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
                commonInterfaceHelper.deliveryStringValue(date);
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

    /**
     * 选择地址的滚轮器
     *
     * @param mContext
     * @param commonInterfaceHelper
     * @param hasThird              是否显示三级选择
     */
    public static void showSelectAddress(Context mContext, final CommonInterfaceHelper commonInterfaceHelper, final boolean hasThird) {
        dealWithProvincialCityJson(mContext);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = mProvinceDatas.get(options1) + " " + mCitisDatasMap.get(options1).get(option2);
                commonInterfaceHelper.deliveryStringValue(mProvinceDatas.get(options1), mCitisDatasMap.get(options1).get(option2), mDistrictDatasMap.get(options1).get(option2).get(options3));

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setSubCalSize(22)//确定和取消文字大小
                .setTitleSize(24)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                .setLabels("", "", "")//设置选择的三级单位
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
        if (hasThird) {
            pvOptions.setPicker(mProvinceDatas, mCitisDatasMap, mDistrictDatasMap);//添加数据源
        } else {
            pvOptions.setPicker(mProvinceDatas, mCitisDatasMap);//添加数据源
        }
        pvOptions.show();
    }

    /**
     * 所有省
     */
    protected static List<String> mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected static List<List<String>> mCitisDatasMap = new ArrayList<>();
    /**
     * key - 市 values - 区
     */
    protected static List<List<List<String>>> mDistrictDatasMap = new ArrayList<>();

    /**
     * 处理json数据
     */
    private static void dealWithProvincialCityJson(Context mContext) {
        mProvinceDatas = new ArrayList<>();
        String jsonString = getJsonFromFile(mContext, "provincial_city.json");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray provincials = jsonObject.optJSONArray("allCitys");
            if (provincials != null) {
                for (int i = 0; i < provincials.length(); i++) {
                    JSONObject provincial = (JSONObject) provincials.get(i);
                    mProvinceDatas.add(provincial.optString("name"));
                    JSONArray citys = provincial.optJSONArray("city");
                    List<String> allCitys = new ArrayList<>();
                    if (citys != null) {
                        List<List<String>> distrinctNameArray = new ArrayList<>();
                        for (int j = 0; j < citys.length(); j++) {
                            JSONObject city = (JSONObject) citys.get(j);
                            allCitys.add(city.optString("name"));
                            JSONArray areas = city.optJSONArray("area");
                            if (areas != null) {
                                List<String> allAreaList = new ArrayList<>();
                                for (int k = 0; k < areas.length(); k++) {
                                    allAreaList.add((String) areas.get(k));
                                }
                                distrinctNameArray.add(allAreaList);
                            }
                        }
                        mDistrictDatasMap.add(distrinctNameArray);
                    }
                    mCitisDatasMap.add(allCitys);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将json文件转化为jsonString
     *
     * @param mContext
     * @param fileName
     * @return
     */
    public static String getJsonFromFile(Context mContext, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = mContext.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
