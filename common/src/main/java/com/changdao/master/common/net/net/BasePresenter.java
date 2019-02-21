package com.changdao.master.common.net.net;

import android.content.Context;


import io.reactivex.Flowable;
import io.reactivex.subscribers.DisposableSubscriber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by zyt on 2018/7/2.
 */

public abstract class BasePresenter<V> {
    protected V mView;
    protected Context mContext;
    public BasePresenter(V mView) {
        this.mView = mView;
    }

    public BasePresenter(V mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    protected void addSubscription(Flowable observable, DisposableSubscriber subscriber) {
        observable.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(mainThread())
                .subscribeWith(subscriber);
    }
}
