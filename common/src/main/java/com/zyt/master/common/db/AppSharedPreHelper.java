package com.zyt.master.common.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   : SharedPreferences数据存储获取数据的封装
 */

public class AppSharedPreHelper {
    private static SharedPreferences sharedPreferences;
    private static AppSharedPreHelper appDbHelper;

    public static AppSharedPreHelper init() {
        if (appDbHelper == null) {
            synchronized (AppSharedPreHelper.class) {
                if (appDbHelper == null) {
                    appDbHelper = new AppSharedPreHelper();
                }
            }
        }
        return appDbHelper;
    }

    private static Context mContext;

    public void initDB(Context context,String shareName) {
        mContext = context;
        if (TextUtils.isEmpty(shareName)){
            shareName = "app_shared_db";
        }
        sharedPreferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
    }

    /******************向数据库的存值***********************/
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    /******************取出数据库的值***********************/
    public String getString(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public long getLong(String key) {
        return getLong(key, -1l);
    }

    public long getLong(String key, long defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    public float getFloat(String key, float defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return false;
    }


    public void del(String key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }
    }
}
