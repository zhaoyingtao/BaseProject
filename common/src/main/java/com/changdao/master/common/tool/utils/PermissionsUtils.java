package com.changdao.master.common.tool.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Created by zyt on 2018/7/4.
 * 权限公共类
 */

public class PermissionsUtils {
    public static final int REQUEST_CODE = 10001;
    private static String[] PERMISSIONS_ARRAY = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
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
        /*******below android 6.0*******/
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        // Check if we have write permission
        int camera = ActivityCompat.checkSelfPermission(activity, permission);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证某一项权限,然后返回 true false
     *
     * @param activity
     * @param permission
     * @return
     */
    public boolean verifyOnePermissionsIsOk(Activity activity, String permission) {
        /*******below android 6.0*******/
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        // Check if we have write permission
        int camera = ActivityCompat.checkSelfPermission(activity, permission);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证读取sd卡的权限、手机权限
     *
     * @param activity
     */
    public boolean verifyMustHasPermissions(Activity activity) {
        /*******below android 6.0*******/
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int readPhoneState = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
//        int recordPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
//        int locationPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED || readPhoneState != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_ARRAY, REQUEST_CODE);
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
                callBack.finishCurrentActivity();
                dialog.dismiss();
//                setResult(PERMISSIONS_DENIED);
//                finish();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppInfoSettings(mContext);
//                jumpPermissionSettingPage(mContext);
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
     * 应用信息界面
     *
     * @param activity
     */
    public void startAppInfoSettings(Activity activity, final int code) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, code);
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
    /******************************以下内容只做保留***********************************************/
    /**
     * 跳转的是权限管理页面
     *
     * @param mContext
     */
    public void jumpPermissionSettingPage(Context mContext) {
        String name = Build.MANUFACTURER;
        switch (name) {
            case "HUAWEI":
                goHuaWeiMainager(mContext);
                break;
            case "vivo":
                goVivoMainager(mContext);
                break;
            case "OPPO":
                goOppoMainager(mContext);
                break;
            case "Coolpad":
                goCoolpadMainager(mContext);
                break;
            case "Meizu":
                goMeizuMainager(mContext);
                break;
            case "Xiaomi":
                goXiaoMiMainager(mContext);
                break;
            case "samsung":
                goSangXinMainager(mContext);
                break;
            case "Sony":
                goSonyMainager(mContext);
                break;
            case "LG":
                goLGMainager(mContext);
                break;
            default:
                goIntentSetting(mContext);
                break;
        }
    }

    private void goLGMainager(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(mContext);
        }
    }

    private void goSonyMainager(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(mContext);
        }
    }

    private void goHuaWeiMainager(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(mContext);
        }
    }

    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private void goXiaoMiMainager(Context mContext) {
        String rom = getMiuiVersion();
        Intent intent = new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", mContext.getPackageName());
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", mContext.getPackageName());
        } else {
            goIntentSetting(mContext);
        }
        mContext.startActivity(intent);
    }

    private void goMeizuMainager(Context mContext) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", mContext.getPackageName());
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting(mContext);
        }
    }

    private void goSangXinMainager(Context mContext) {
        //三星4.3可以直接跳转
        goIntentSetting(mContext);
    }

    private void goIntentSetting(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goOppoMainager(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.coloros.safecenter");
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private void goCoolpadMainager(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private void goVivoMainager(Context mContext) {
        doStartApplicationWithPackageName(mContext, "com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    private void doStartApplicationWithPackageName(Context mContext, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e("PermissionPageManager", resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                goIntentSetting(mContext);
                e.printStackTrace();
            }
        }
    }
}
