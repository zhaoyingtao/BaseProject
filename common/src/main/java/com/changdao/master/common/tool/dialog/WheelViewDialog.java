package com.changdao.master.common.tool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.changdao.master.common.R;

import java.util.List;

/**
 * Created by bob on 2016/12/8.
 * 单行数据选择器
 */

public class WheelViewDialog extends Dialog implements View.OnClickListener {

    private Context context;
    protected WheelView wheelView;
    protected TextView tv_ok;
    private List<String> dataList;
    private WheelViewDialogCallBack callBack;
    private TextView tv_cancel;
    private TextView tv_title;
    private int positionState;//0 宽全屏 1 宽半屏剪掉导航栏靠左 2 宽半屏靠左
    private int marginLeft;//距离左边的距离

    public WheelViewDialog(Context context, List<String> dataList, WheelViewDialogCallBack callBack) {
        super(context, R.style.customerDialog);
        this.context = context;
        this.dataList = dataList;
        this.callBack = callBack;
        this.positionState = 0;
        this.marginLeft = 0;
        createDialog();
    }

    public WheelViewDialog(Context context, int marginLeft, int positionState, List<String> dataList, WheelViewDialogCallBack callBack) {
        super(context, R.style.customerDialog);
        this.context = context;
        this.dataList = dataList;
        this.callBack = callBack;
        this.positionState = positionState;
        this.marginLeft = marginLeft;
        createDialog();
    }

    public WheelViewDialog(@NonNull Context context) {
        super(context, R.style.customerDialog);
        this.context = context;
        createDialog();
    }

    private void createDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.select_wheels_layout, null);
        setContentView(view);
        initWidth();
        initView();
    }

    private void initView() {
        wheelView = (WheelView) findViewById(R.id.wheelView);
        wheelView.setCyclic(false);
        tv_ok = (TextView) findViewById(R.id.ok);
        tv_cancel = (TextView) findViewById(R.id.cancel);
        tv_title = (TextView) findViewById(R.id.title);
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    @Override
    public void show() {
        wheelView.setAdapter(new ArrayWheelAdapter(dataList));
        super.show();
    }

    public WheelViewDialog setTitleStr(String title) {
        tv_title.setText(title);
        return this;
    }

    public WheelViewDialog setCurrentItem(int position) {
        wheelView.setCurrentItem(position);
        return this;
    }

    /**
     * 设置设局和回调
     *
     * @param title
     * @param callBack
     */
    public void setWheelDialogData(String title, List<String> dataList, WheelViewDialogCallBack callBack) {
        tv_title.setText(title);
        this.callBack = callBack;
        this.dataList = dataList;
        show();
    }

    private void initWidth() {
        Window dialogWindow = this.getWindow();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams param = dialogWindow.getAttributes();
        if (positionState == 1) {
            if (marginLeft != 0)
                param.width = (display.getWidth() - marginLeft) / 2;
            else param.width = display.getWidth();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            param.x = marginLeft;
        } else {
            param.width = display.getWidth();
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
//        param.height = display.getHeight() / 3;
        dialogWindow.setAttributes(param);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            //                String selected = dataList.get(wheelView.getCurrentItem());
            if (callBack != null) {
                callBack.clickPosition(wheelView.getCurrentItem());
            }
        }
        dismiss();
    }

    public interface WheelViewDialogCallBack {
        void clickPosition(int position);
    }
}
