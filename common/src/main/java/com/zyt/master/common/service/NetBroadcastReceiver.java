package com.zyt.master.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;


import com.zyt.master.common.Listener.EventBus;

import java.text.SimpleDateFormat;

/**
 * Created by zyt on 2018/10/12.
 * 自定义检查手机网络状态是否切换的广播接受器
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    private static long WIFI_TIME = 0;
    private static long ETHERNET_TIME = 0;
    private static long NONE_TIME = 0;

    private static int LAST_TYPE = -3;
    public static final String NET_WORK_CHANGED = "networkChanged";//网络变化监听

    @Override
    public void onReceive(Context context, Intent intent) {
        //没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            long time = getTime();
            if (time != WIFI_TIME && time != ETHERNET_TIME && time != NONE_TIME) {
                final int netWorkState = NetworkUtil.getNetWorkType(context);
                if (netWorkState == 1 && LAST_TYPE != 1) {
                    WIFI_TIME = time;
                    LAST_TYPE = netWorkState;
                } else if (netWorkState == 2 && LAST_TYPE != 2) {
                    EventBus.getInstance().post(NET_WORK_CHANGED);
                    ETHERNET_TIME = time;
                    LAST_TYPE = netWorkState;
                } else if (netWorkState == 3 && LAST_TYPE != 3) {
                    EventBus.getInstance().post(NET_WORK_CHANGED);
                    ETHERNET_TIME = time;
                    LAST_TYPE = netWorkState;
                } else if (netWorkState == 4 && LAST_TYPE != 4) {
                    EventBus.getInstance().post(NET_WORK_CHANGED);
                    ETHERNET_TIME = time;
                    LAST_TYPE = netWorkState;
                } else if (netWorkState == 0 && LAST_TYPE != 0) {
                    NONE_TIME = time;
                    LAST_TYPE = netWorkState;
                }
            }
        }
    }

    public long getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        return Long.valueOf(date);
    }
}
