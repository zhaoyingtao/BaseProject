package com.changdao.master.common.tool.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.changdao.master.common.tool.rewrite.CornerTransform;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by zyt on 2017/11/20.
 */

public class ImageUtil {
    /**
     * 使用Glide加载图片
     *
     * @param mContext
     * @param url       图片地址
     * @param imageView 显示的ImageView
     */
    public static void imageLoad(Context mContext, String url, ImageView imageView) {
        imageLoad(mContext, url, imageView, false, -1);
    }

    /**
     * 使用Glide加载图片
     *
     * @param mContext
     * @param url        图片地址
     * @param imageView  显示的ImageView
     * @param isUseCache 是否使用缓存
     */
    public static void imageLoad(Context mContext, String url, ImageView imageView, boolean isUseCache) {
        imageLoad(mContext, url, imageView, isUseCache, -1);
    }

    /**
     * 使用Glide加载图片
     *
     * @param mContext
     * @param url        图片地址
     * @param imageView  显示的ImageView
     * @param defaultPic 默认图片
     */
    public static void imageLoad(Context mContext, String url, ImageView imageView, int defaultPic) {
        imageLoad(mContext, url, imageView, false, defaultPic);
    }

    /**
     * 使用Glide加载图片
     *
     * @param mContext
     * @param url        图片地址
     * @param imageView  显示的ImageView
     * @param isUseCache 是否使用缓存
     * @param defaultPic 默认图片
     */
    public static void imageLoad(Context mContext, String url, ImageView imageView, boolean isUseCache, int defaultPic) {
        if (imageView == null || mContext == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .placeholder(defaultPic)
                .error(defaultPic)
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        if (isUseCache) {
            Glide.with(mContext).load(url).apply(options).into(imageView);
        } else {
            Glide.with(mContext).load(url).apply(options).into(imageView);
        }
    }

    /**
     * 使用Glide加载圆形图片
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param defaultPic
     */
    public static void imageLoadCircle(Context mContext, String url, ImageView imageView, int defaultPic) {
        if (imageView == null || mContext == null) {
            return;
        }
        RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop())
                .placeholder(defaultPic)
                .error(defaultPic)
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        Glide.with(mContext).load(url).apply(options).into(imageView);
    }

    /**
     * 加载本地图片
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void imageLoadLoaction(Context mContext, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url) || mContext == null) {
            return;
        }
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext).load(new File(url)).apply(options).into(imageView);
    }

    /**
     * 加载圆角图片 ====建议直接使用这个
     */
    public static void imageLoadFillet(Context mContext, String url, int filletSize,
                                       ImageView imageView) {
        imageLoadFillet(mContext, url, filletSize, 0, imageView, -1);
    }

    /**
     * 加载圆角图片 ====建议直接使用这个
     *
     * @param mContext
     * @param url
     * @param filletSize 圆角大小 dp
     * @param direction  圆角方向 超过这几种也是圆角 0 四个圆角 1 左圆角 2 上圆角 3 右圆角 4 下圆角 5 四个直角
     * @param imageView
     * @param defaultPic
     */
    public static void imageLoadFillet(Context mContext, String url, int filletSize, int direction,
                                       ImageView imageView, int defaultPic) {
        if (imageView == null || mContext == null) {
            return;
        }
        CornerTransform transformation = new CornerTransform(mContext, filletSize);
        if (direction == 1) {
            transformation.setExceptCorner(false, true, false, true);
        } else if (direction == 2) {
            transformation.setExceptCorner(false, false, true, true);
        } else if (direction == 3) {
            transformation.setExceptCorner(true, false, true, false);
        } else if (direction == 4) {
            transformation.setExceptCorner(true, true, false, false);
        } else if (direction == 5) {
            transformation.setExceptCorner(true, true, true, true);
        } else {
            transformation.setExceptCorner(false, false, false, false);
        }
        RequestOptions options = RequestOptions.bitmapTransform(transformation)
                .placeholder(defaultPic)
                .error(defaultPic)
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        Glide.with(mContext).load(url).apply(options).into(imageView);
    }
    /**
     * 将图片url转化为Drawable
     * 注意----------不能再主线程中使用此方法
     *
     * @param imageUrl
     * @return
     */
    public static Drawable changeImageUrlToDrawable(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }

    /**
     * 旋转图片方向
     *
     * @param bitmap
     * @param imgPath
     * @return
     */
    private static Bitmap rotatingImage(Bitmap bitmap, String imgPath) {
        ExifInterface exifInterface = null;
        Matrix matrix = null;
        try {
            exifInterface = new ExifInterface(imgPath);
            if (exifInterface == null) return bitmap;
            matrix = new Matrix();
            int angle = 0;
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
            matrix.postRotate(angle);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 根据uri获取图片的绝对路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri || context == null) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
