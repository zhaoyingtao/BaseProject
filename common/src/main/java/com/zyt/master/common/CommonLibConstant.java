package com.zyt.master.common;

import android.content.Context;
import android.os.Environment;

import com.zyt.master.common.crash.CrashHandler;
import com.zyt.master.common.db.AppSharedPreHelper;
import com.zyt.master.common.service.NetworkUtil;


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

    /**
     * 设置应用的Context，必须先设置，必须设置
     * @param appContext
     * @return
     */
    public CommonLibConstant setAppContext(Context appContext) {
        applicationContext = appContext;
        //日志收集
        CrashHandler.getInstance().init(appContext, LOCAL_PATH + appContext.getPackageName() + "/");
        return this;
    }

    /**
     * 设置是不是Debug模式
     * @param isDebug
     * @return
     */
    public CommonLibConstant setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
        return this;
    }

    /**
     * 设置无网络提示语
     * @param noNetremind
     * @return
     */
    public CommonLibConstant setNoNetWorkRemind(String noNetremind) {
        noNetWorkRemind = noNetremind;
        return this;
    }

    /**
     * 设置SharedPreferences的存储名以及初始化
     * @param dbName
     * @return
     */
    public CommonLibConstant setSharedPreferencesName(String dbName) {
        AppSharedPreHelper.init().initDB(applicationContext, "base_db");
        return this;
    }

    /**
     * 设置崩溃日志的存储路径==默认在应用包下
     * @param crashSavePath
     * @return
     */
    public CommonLibConstant setCrashSavePath(String crashSavePath) {
        //日志收集
        CrashHandler.getInstance().init(applicationContext, crashSavePath);
        return this;
    }

    /**
     * 获取外网的IP并且存在了SharedPreferences，通过ExternalNetworkIpKey可以获取到
     * @return
     */
    public CommonLibConstant setExternalNetworkIP() {
        //获取网络IP
        NetworkUtil.getExternalNetworkIP(applicationContext);
        return this;
    }
}
