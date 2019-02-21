package com.changdao.master.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.changdao.master.common.R;

/**
 * Created by zyt on 2017/11/25.
 * 设置圆型图片显示和自定义圆角图片显示
 */

public class RoundAngelImageView extends AppCompatImageView {
    private Matrix matrix;
    private Paint paint;

    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private final int BODER_RADIUS_DEFAULT = 10;
    private float borderRadius;

    private int width;
    private int radius;
    private RectF rectF;

    public RoundAngelImageView(Context context) {
        this(context, null);
    }

    public RoundAngelImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundAngelImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inital(context, attrs);
    }

    private void inital(Context context, AttributeSet attrs) {
        matrix = new Matrix();
        paint = new Paint();
        //无锯齿
        paint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundAngelImageView);
        borderRadius = array.getDimension(R.styleable.RoundAngelImageView_borderRadius, dp2px(BODER_RADIUS_DEFAULT));
        //如果没设置圆角的默认值，在这设置默认值为10dp
//        borderRadius = dp2px(BODER_RADIUS_DEFAULT);
        // 默认为Circle
        type = array.getInt(R.styleable.RoundAngelImageView_type, TYPE_CIRCLE);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果类型是圆形，则强制改变view的宽高一致，以小值为准
        if (type == TYPE_CIRCLE) {
//            width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            width = getMeasuredWidth();
            radius = width / 2;
            setMeasuredDimension(width, width);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND)
            rectF = new RectF(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);重绘图，不能使用原有绘图
        if (getDrawable() == null) {
            return;
        }
        try {
            setBitmapShader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(rectF, borderRadius, borderRadius, paint);
        } else {
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    private void setBitmapShader() throws Exception {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        //将drawable转化成bitmap对象
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        //由于更改了glide最新库，有所变动
//        else if (drawable instanceof GlideBitmapDrawable) {//使用glide会出现GlideBitmapDrawable格式
//            bitmap = ((GlideBitmapDrawable) drawable).getBitmap();
//        } else if (drawable instanceof SquaringDrawable) {//使用glide会出现SquaringDrawable格式
//            bitmap = ((GlideBitmapDrawable) drawable.getCurrent()).getBitmap();
//        }
        else {//使用glide会出现其他格式的drawable
            bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
        }
        if (bitmap == null) {
            return;
        }
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        int viewwidth = getWidth();
        int viewheight = getHeight();
        int drawablewidth = bitmap.getWidth();
        int drawableheight = bitmap.getHeight();
        float dx = 0, dy = 0;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = width * 1.0f / size;

        } else if (type == TYPE_ROUND) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例
            // 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight()
                    * 1.0f / bitmap.getHeight());
        }

        if (drawablewidth * viewheight > viewwidth * drawableheight) {
            dx = (viewwidth - drawablewidth * scale) * 0.5f;
        } else {
            dy = (viewheight - drawableheight * scale) * 0.5f;
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        // 设置变换矩阵
        bitmapShader.setLocalMatrix(matrix);
        // 设置shader
        paint.setShader(bitmapShader);
    }

    //修改圆角大小
    public void setBorderRadius(int borderRadius) {
        float px = dp2px(borderRadius);
        if (this.borderRadius != px) {
            this.borderRadius = px;
            invalidate();
        }
    }

    //修改type
    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }
}
