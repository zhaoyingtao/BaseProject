package com.zyt.master.common.net.net;

import android.content.Context;

import com.zyt.master.common.CommonLibConstant;
import com.zyt.master.common.service.NetworkUtil;
import com.zyt.master.common.tool.utils.ToastUtils;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * Created by zyt on 2017/11/17.
 * 请求数据回调---
 */

public abstract class RequestApiCallBack<T> extends DisposableSubscriber<T> {
    protected Context mMContext;

    public RequestApiCallBack() {
    }

    public RequestApiCallBack(Context mContext) {
        this.mMContext = mContext;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!NetworkUtil.isNetworkAvailable(CommonLibConstant.applicationContext)) {
            ToastUtils.getInstance().showToast(CommonLibConstant.noNetWorkRemind);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(Throwable e);


}
