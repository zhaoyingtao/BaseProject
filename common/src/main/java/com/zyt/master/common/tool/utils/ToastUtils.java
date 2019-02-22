package com.zyt.master.common.tool.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.zyt.master.common.CommonLibConstant;
/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   : Toast工具类
 */

public class ToastUtils {
    private static ToastUtils mToastUtils;
    private Toast mToast;
    private int showPosition = Gravity.BOTTOM;

    public static ToastUtils getInstance() {
        if (mToastUtils == null) {
            synchronized (ToastUtils.class) {
                if (mToastUtils == null) {
                    mToastUtils = new ToastUtils();
                }
            }
        }
        return mToastUtils;
    }

    /**
     * 设置toast的位置
     *
     * @param showPosition Gravity.BOTTOM Gravity.CENTER
     */
    public void setToastPosition(int showPosition) {
        this.showPosition = showPosition;
    }

    /**
     * 显示Toast
     *
     * @param mess
     */
    public void showToast(String mess) {
        if (mToast == null) {
            mToast = Toast.makeText(CommonLibConstant.applicationContext, mess, Toast.LENGTH_LONG);
            if (showPosition != Gravity.BOTTOM ){
                mToast.setGravity(showPosition, 0, 0);
            }
        } else {
            mToast.setText(mess);
        }
        mToast.show();
    }

    /**
     * 显示Toast
     *
     * @param messResID R.string.xxx
     */
    public void showToast(int messResID) {
        String messStr = CommonLibConstant.applicationContext.getResources().getString(messResID);
        showToast(messStr);
    }
}
