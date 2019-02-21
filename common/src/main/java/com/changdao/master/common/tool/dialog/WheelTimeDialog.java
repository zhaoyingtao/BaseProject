package com.changdao.master.common.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.changdao.master.common.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bob on 2016/12/8.
 * 选择时间的滚动轮
 */

public class WheelTimeDialog extends Dialog implements View.OnClickListener {

    private Context context;
    protected TextView tv_ok;
    private WheelViewDialogCallBack callBack;
    private TextView tv_cancel;
    private TextView tv_title;
    private int positionState;//0 宽全屏 1 宽半屏剪掉导航栏靠左 2 宽半屏靠左
    private int marginLeft;//距离左边的距离
    private List<Integer> yearsList;
    private List<Integer> monthList;
    private List<Integer> daysList;
    private WheelView wheelView01;
    private WheelView wheelView02;
    private WheelView wheelView03;
    private ArrayWheelAdapter daysAdapter;

    public WheelTimeDialog(Context context, WheelViewDialogCallBack callBack) {
        super(context, R.style.customerDialog);
        this.context = context;
        this.callBack = callBack;
        this.positionState = 0;
        this.marginLeft = 0;
        createDialog();
    }

    public WheelTimeDialog(Context context, int marginLeft, int positionState, WheelViewDialogCallBack callBack) {
        super(context, R.style.customerDialog);
        this.context = context;
        this.callBack = callBack;
        this.positionState = positionState;
        this.marginLeft = marginLeft;
        createDialog();
    }

    public WheelTimeDialog(@NonNull Context context) {
        super(context, R.style.customerDialog);
        this.context = context;
        createDialog();
    }

    private void createDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.select_wheels_time_layout, null);
        setContentView(view);
        initView(view);
        initYearsMonthDays();
        initWidth();

        wheelView01.setAdapter(new ArrayWheelAdapter(yearsList));
        wheelView02.setAdapter(new ArrayWheelAdapter(monthList));
        wheelView02.setCyclic(true);
//        daysAdapter = new ArrayWheelAdapter(daysList);
        wheelView03.setAdapter(new ArrayWheelAdapter(daysList));
        wheelView03.setCyclic(true);
        initListener();
        wheelView01.setCurrentItem(yearsList.size() - 1);
    }

    public WheelTimeDialog setTitleStr(String title) {
        tv_title.setText(title);
        return this;
    }

    private void initListener() {

        wheelView01.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                addDaysData();
            }
        });
        wheelView02.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                addDaysData();
            }
        });
    }

    public WheelTimeDialog setCurrentTimeItem(String timeStr) {
        String[] timeArr;
        if (!TextUtils.isEmpty(timeStr) && timeStr.contains("-")) {
            timeArr = timeStr.split("-");
        } else {
            timeArr = new String[3];
            Calendar calendar = Calendar.getInstance();
            timeArr[0] = String.valueOf(calendar.get(Calendar.YEAR));
            timeArr[1] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            timeArr[2] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }
        for (int i = 0; i < yearsList.size(); i++) {
            if (yearsList.get(i) == Integer.parseInt(timeArr[0])) {
                wheelView01.setCurrentItem(i);
                break;
            }
        }
        for (int i = 0; i < monthList.size(); i++) {
            if (monthList.get(i) == Integer.parseInt(timeArr[1])) {
                wheelView02.setCurrentItem(i);
                break;
            }
        }
        for (int i = 0; i < daysList.size(); i++) {
            if (daysList.get(i) == Integer.parseInt(timeArr[2])) {
                wheelView03.setCurrentItem(i);
                break;
            }
        }
        return this;
    }

    /**
     * 设置天数的数据
     */
    private void addDaysData() {
        daysList.clear();
        int fff = getMonthOfDay(yearsList.get(wheelView01.getCurrentItem()), monthList.get(wheelView02.getCurrentItem()));
        for (int i = 1; i < fff + 1; i++) {
            daysList.add(i);
        }
        wheelView03.setAdapter(new ArrayWheelAdapter(daysList));
        if (wheelView03.getCurrentItem() > daysList.size() - 1) {
            wheelView03.setCurrentItem(daysList.size() - 1);
        }
    }

    private void initYearsMonthDays() {
        yearsList = new ArrayList<>();
        monthList = new ArrayList<>();
        daysList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR) - 70;
        int endYear = calendar.get(Calendar.YEAR);
        for (int i = 1; i < 13; i++) {
            monthList.add(i);
        }
        for (int i = 0; i < endYear - startYear + 1; i++) {
            yearsList.add(startYear + i);
        }
        for (int i = 1; i < 32; i++) {
            daysList.add(i);
        }
    }

    private void initView(View view) {
        wheelView01 = (WheelView) view.findViewById(R.id.wheelView_01);
        wheelView01.setCyclic(false);
        wheelView02 = (WheelView) view.findViewById(R.id.wheelView_02);
        wheelView02.setCyclic(false);
        wheelView03 = (WheelView) view.findViewById(R.id.wheelView_03);
        wheelView03.setCyclic(false);
        tv_ok = (TextView) view.findViewById(R.id.ok);
        tv_cancel = (TextView) view.findViewById(R.id.cancel);
        tv_title = (TextView) view.findViewById(R.id.title);
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    @Override
    public void show() {
        super.show();
    }

    private void initWidth() {
        Window dialogWindow = this.getWindow();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams param = dialogWindow.getAttributes();
        if (positionState == 1) {
            if (marginLeft == 0)
                param.width = display.getWidth();
            else param.width = (display.getWidth() - marginLeft) / 2;
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            param.x = marginLeft;
        } else {
            param.width = display.getWidth();
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        dialogWindow.setAttributes(param);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            if (callBack != null) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(yearsList.get(wheelView01.getCurrentItem()));
                stringBuffer.append("-");
                stringBuffer.append(changeNum(monthList.get(wheelView02.getCurrentItem())));
                stringBuffer.append("-");
                stringBuffer.append(changeNum(daysList.get(wheelView03.getCurrentItem())));
                callBack.selectResultValue(stringBuffer.toString());
            }
        }
        dismiss();
    }

    private String changeNum(int num) {
        if (num < 9) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

    public interface WheelViewDialogCallBack {
        void selectResultValue(String value);
    }

    /**
     * 获取一月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public int getMonthOfDay(int year, int month) {
        int day = 0;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }
}
