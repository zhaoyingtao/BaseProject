package com.zyt.master.common.tool.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by zyt on 2018/7/4.
 * 权限公共类
 */

public class PermissionsUtils {
    //可以单独使用https://github.com/ZeroBrain/AndroidPermissions的依赖库
    public static final int REQUEST_CODE = 10001;
    private static PermissionsUtils mPermissionsUtils;

    public static PermissionsUtils getInstance() {
        if (mPermissionsUtils == null) {
            synchronized (PermissionsUtils.class) {
                if (mPermissionsUtils == null) {
                    mPermissionsUtils = new PermissionsUtils();
                }
            }
        }
        return mPermissionsUtils;
    }

    /**
     * 验证某一项权限，没有授权就去动态请求授权
     *
     * @param activity
     * @param permission 传入这样的格式 Manifest.permission.CAMERA
     * @return
     */
    public boolean verifyOnePermissions(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permissionStatus = ActivityCompat.checkSelfPermission(activity, permission);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    /**
     * 显示缺失权限提示
     *
     * @param mContext
     * @param permission 缺少的权限名
     * @param callBack
     */
    public void showMissingPermissionDialog(final Context mContext, String permission, final MissingPermissionCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("帮助");
        if ("android.permission.READ_EXTERNAL_STORAGE".equals(permission)) {//存储
            builder.setMessage("当前应用缺少必要权限。\n请点击下方“设置”，进入页面后点击”权限“，打开“存储”权限。");
        } else if ("android.permission.READ_PHONE_STATE".equals(permission)) {//电话
            builder.setMessage("当前应用缺少必要权限。\n请点击下方“设置”，进入页面后点击”权限“，打开“电话”权限。");
        } else if ("android.permission.CAMERA".equals(permission)) {//拍照
            builder.setMessage("当前应用缺少必要权限。\n请点击下方“设置”，进入页面后点击”权限“，打开“相机”权限。");
        } else {
            builder.setMessage("当前应用缺少必要权限。\n请点击“设置”-“权限”-打开所需权限");
        }
        builder.setCancelable(false);
        // 拒绝, 退出应用
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openApplicationMarket(mContext);
                if (callBack != null)
                    callBack.finishCurrentActivity();
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppInfoSettings(mContext);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @FunctionalInterface
    public interface MissingPermissionCallBack {
        void finishCurrentActivity();
    }

    /**
     * 应用信息界面
     *
     * @param mContext
     */
    public void startAppInfoSettings(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }


    /**
     * 检测应用通知是否打开
     *
     * @param context
     * @return
     */
    public boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {//适配8。0除部分国产机以外的大部分机型
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        } else {
            String CHECK_OP_NO_THROW = "checkOpNoThrow";
            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null;
            /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 通过包名 在应用商店打开应用
     *
     * @param mContext
     */
    public void openApplicationMarket(Context mContext) {
        try {
            String brand = Build.BRAND;
            String str = "market://details?id=";
            if (!TextUtils.isEmpty(brand)) {
                if ("samsung".equals(brand.toLowerCase())) {//三星
                    str = "http://www.samsungapps.com/appquery/appDetail.as?appId=";
                } else if ("sony".equals(brand.toLowerCase())) {//索尼
                    str = "http://m.sonyselect.cn/";
                }
            } else {
                str = "market://details?id=";
            }
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setData(Uri.parse(str + mContext.getPackageName()));
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(localIntent);
        } catch (Exception e) {
            // 打开应用商店失败 可能是没有手机没有安装应用市场
            e.printStackTrace();
            Toast.makeText(mContext, "打开应用商店失败", Toast.LENGTH_LONG).show();
            // 调用系统浏览器进入商城
            String url = "http://app.mi.com/detail/163525?ref=search";
            openLinkBySystem(mContext, url);
        }
    }

    /**
     * 调用系统浏览器打开网页
     *
     * @param url 地址
     */
    private void openLinkBySystem(Context mContext, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
    }
}
