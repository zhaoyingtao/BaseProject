package com.changdao.master.common.tool.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.changdao.master.common.service.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by zyt on 2018/7/18.
 * 应用的一些工具类
 */

public class AppUtils {
    private static AppUtils appUtils;

    public static AppUtils init() {
        if (appUtils == null) {
            synchronized (AppUtils.class) {
                if (appUtils == null) {
                    appUtils = new AppUtils();
                }
            }
        }
        return appUtils;
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

    /**
     * 获取手机的设备信息==惊悚格式
     *
     * @param context
     * @return
     */
    public String getPhoneDeviceJsonInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client-source", "app");
            jsonObject.put("client-version", AppUtils.init().getAppVersionName(context));
            jsonObject.put("client-platform", "android");
            jsonObject.put("client-network", NetworkUtil.getNetWorkStatus(context));
            jsonObject.put("client-platform_version", Build.VERSION.RELEASE);
            jsonObject.put("client-deviceid", SystemUtil.getDeviceUniqueIndicationCode(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * app的版本名
     *
     * @param context
     * @return
     */
    public String getAppVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * app的版本号
     *
     * @param context
     * @return
     */
    public int getAppVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 关闭键盘
     */
    public void closeInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null && imm.isActive() && activity.getCurrentFocus() != null) {
//            IBinder binder = activity.getCurrentFocus().getWindowToken();
//            if (null != binder) {
//                imm.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
    }


    /**
     * 隐藏键盘
     */
    public void closeInputMethod(Activity activity, EditText v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     * 调用不起作用时===可以延迟调用，让view.requestFocus(),获取焦点的方法需要做线程之外调用否则会报错
     */
    public void showSoftInputMethod(View view, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 修改Manifest中的meta-data的值
     *
     * @param mContext
     * @param metaKey   meta-data的name
     * @param metaValue
     */
    public void modifyManifestMetaDataValue(Context mContext, String metaKey, String metaValue) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            //这里不要使用getString获取，值可能是其他类型如boolean、long、int等获取就会为null
//            String msg = String.valueOf(appInfo.metaData.get(metaKey));
//            Log.e("zyt", "before: " + msg);
            appInfo.metaData.putString(metaKey, metaValue);
//            String msg = appInfo.metaData.getString(metaKey);
//            Log.e("zyt", "after: " + msg);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
