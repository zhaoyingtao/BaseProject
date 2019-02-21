package com.zyt.appcommon.net;


import android.content.Context;

import com.changdao.master.common.net.dialog.LoadingDialog;
import com.changdao.master.common.net.net.RequestApiCallBack;
import com.changdao.master.common.tool.utils.ToastUtils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zyt on 2018/7/13.
 * 公共处理数据格式的类
 */

public abstract class RequestApiCallBackHelper extends RequestApiCallBack<JsonObject> {

    //这里可以更改为自己项目需要的加载提示Dialog
    private LoadingDialog loadingDialog;

    public RequestApiCallBackHelper() {
        super();
    }

    public RequestApiCallBackHelper(Context mContext) {
        super(mContext);
        initLoadingDialog();
    }

    @Override
    public void onStart() {
        super.onStart();

        //显示加载loading
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showLoading();
        }
    }

    /**
     * 处理返回数据
     *
     * @param isSuccess 只有true正常处理
     * @param rootJson
     */
    protected abstract void dealSuccessData(boolean isSuccess, JSONObject rootJson);

    @Override
    public void onSuccess(JsonObject rootData) {
        //在这里设置取消dialog就可以
        dissmissLoadDialog();
        try {
            JSONObject rootJson = new JSONObject(rootData.toString());
            int code = rootJson.optInt("code", -1);
            switch (code) {
                case 0://成功
                    dealSuccessData(true, rootJson);
                    break;
                case 10000://失败
                    dealSuccessData(false, rootJson);
                    ToastUtils.getInstance().showToast(rootJson.optString("msg"));
                    break;
                case -1://未登录
                    if (mMContext != null) {

                    }
                    break;
                case 10001://失败状态 不弹出Toast
                    dealSuccessData(false, rootJson);
                    break;
                default:
                    dealSuccessData(false, rootJson);
                    ToastUtils.getInstance().showToast(rootJson.optString("msg"));
                    break;
            }
        } catch (JSONException e) {
            ToastUtils.getInstance().showToast("解析出错");
            e.printStackTrace();
          }

    }

    @Override
    public void onFail(Throwable e) {
        //在这里设置取消dialog就可以
        dissmissLoadDialog();
        ToastUtils.getInstance().showToast(e.getMessage());
    }

    /**
     * 初始化LoadingDialog
     */
    private void initLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mMContext);
        }
    }

    /**
     * 关闭加载dialog
     */
    private void dissmissLoadDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
