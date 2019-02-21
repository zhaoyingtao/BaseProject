package com.changdao.master.common;

import android.content.Context;
import android.os.Environment;

import com.changdao.master.common.crash.CrashHandler;
import com.changdao.master.common.db.AppSharedPreHelper;
import com.changdao.master.common.net.net.RetrofitClientUtil;


/**
 * Created by zyt on 2018/7/2.
 * common的常量类
 */

public class CommonLibConstant {
    private static final CommonLibConstant libConstant = new CommonLibConstant();

    public static CommonLibConstant init() {
        return libConstant;
    }

    public static Context applicationContext;
    public static String noNetWorkRemind = "网络连接已断开,请检查网络";
    public static String base_url = "";
    //是否是调试模式，默认是调试模式
    public static boolean IS_DEBUG = true;
    private String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/";

    public CommonLibConstant setAppContext(Context appContext) {
        applicationContext = appContext;
        //日志收集
        CrashHandler.getInstance().init(appContext, LOCAL_PATH + appContext.getPackageName() + "/");
        return this;
    }

    public CommonLibConstant setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
        return this;
    }

    public CommonLibConstant setNoNetWorkRemind(String noNetremind) {
        noNetWorkRemind = noNetremind;
        return this;
    }

    public CommonLibConstant setRequestBaseUrl(String baseUrl) {
        base_url = baseUrl;
        //初始化请求网络实体
        RetrofitClientUtil.initClient().changeApiBaseUrl(baseUrl);
        return this;
    }

    public CommonLibConstant setSharedPreferencesName(String dbName) {
        AppSharedPreHelper.init().initDB(applicationContext, "base_db");
        return this;
    }

    public CommonLibConstant setCrashSavePath(String crashSavePath) {
        //日志收集
        CrashHandler.getInstance().init(applicationContext, crashSavePath);
        return this;
    }
}
