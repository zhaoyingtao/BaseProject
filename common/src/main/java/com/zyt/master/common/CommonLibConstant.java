package com.zyt.master.common;

import android.content.Context;
import android.os.Environment;

import com.zyt.master.common.crash.CrashHandler;
import com.zyt.master.common.db.AppSharedPreHelper;


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
