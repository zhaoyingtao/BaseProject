package com.changdao.master.common.net.net;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;


/**
 * Created by zyt on 2018/8/24.
 * 在任何地方直接请求服务器
 */

public class DirectRequestApiManager {
    private static DirectRequestApiManager requestApiManager;

    public static DirectRequestApiManager init() {
        if (requestApiManager == null) {
            synchronized (DirectRequestApiManager.class) {
                if (requestApiManager == null) {
                    requestApiManager = new DirectRequestApiManager();
                }
            }
        }
        return requestApiManager;
    }

    /**
     * 注意传进来的前后参数类型一致
     *
     * @param observable
     * @param subscriber
     */
    public void addSubscription(Flowable observable, DisposableSubscriber subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .subscribeWith(subscriber);
    }
}
