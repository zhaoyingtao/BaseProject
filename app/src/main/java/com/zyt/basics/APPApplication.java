package com.zyt.basics;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zyt.master.common.CommonLibConstant;
import com.zyt.master.common.net.net.AddCookiesInterceptor;
import com.zyt.master.common.net.net.RetrofitClientUtil;
import com.zyt.master.common.service.NetworkUtil;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2018/12/22
 * desc   :
 */

public class APPApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        //今日头条适配配置
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.NONE);
        if (AppConstant.is_debug) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        //初始化ARouter
        ARouter.init(this);
        //common依赖库的相关初始化
        CommonLibConstant.init()
                .setAppContext(this)
                .setIsDebug(true)
                .setNoNetWorkRemind("无网络")
                .setSharedPreferencesName("base_db")
                .setCrashSavePath(AppConstant.LOCAL_PATH)
                .setExternalNetworkIP();
        //网络请求初始化
        RetrofitClientUtil.initClient()
                .useCookiesInterceptor(new AddCookiesInterceptor(APPApplication.this))
                .setConnectionPoolNums(8)
                .setConnectionPoolKeepTime(15)
                .setRequestOutTime(20)
                .setRequestBaseUrl(AppConstant.base_url)//必须设置
                .build();//必须设置

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
