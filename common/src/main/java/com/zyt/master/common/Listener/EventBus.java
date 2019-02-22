package com.zyt.master.common.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   :EventBus 发送通知和接收通知
 */
public class EventBus {

    private static volatile EventBus mInstance;

    private final FlowableProcessor<Object> mBus;

    private Map<Object, List<Disposable>> mSubscriptions = new HashMap<>();

    public EventBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    public static EventBus getInstance() {
        if (mInstance == null) {
            synchronized (EventBus.class) {
                if (mInstance == null) {
                    mInstance = new EventBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送消息
     *
     * @param object
     */
    public void post(Object object) {
        mBus.onNext(object);
    }


    /**
     * 订阅消息
     *
     * @param object    页面对象，Activity 或 Fragment，但本质上是取其类名作为一个 key，传唯一实例的自定义对象也可以
     * @param eventType 事件类型
     * @param consumer  订阅者
     * @param <T>       事件类型
     * @return Subscription，可以主动unsubscribe，也可在页面退出时统一取消订阅
     */
    public <T> Disposable subscribe(Object object, Class<T> eventType, io.reactivex.functions.Consumer<T> consumer) {
        Disposable disposable = mBus.ofType(eventType).subscribe(consumer);
        if (object != null) {
            List<Disposable> subscriptions = mSubscriptions.get(object);
            if (subscriptions == null) {
                subscriptions = new ArrayList<>();
            }
            subscriptions.add(disposable);
            mSubscriptions.put(object, subscriptions);
        }
        return disposable;
    }

    /**
     * 页面退出时务必取消订阅，否则引发内存泄漏
     *
     * @param object 注册事件所在的对象
     */
    public void unsubscribe(Object object) {
        if (object != null) {
            List<Disposable> subscriptions = mSubscriptions.get(object);
            if (subscriptions != null) {
                for (Disposable disposable : subscriptions) {
                    disposable.dispose();
                }
                mSubscriptions.remove(object);
            }
        }
    }
}
