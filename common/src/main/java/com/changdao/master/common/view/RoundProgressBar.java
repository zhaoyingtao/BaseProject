package com.changdao.master.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.changdao.master.common.R;


/**
 * Created by zyt on 2017/6/26.
 * 圆圈进度条  中间显示百分比
 */

public class RoundProgressBar extends View {
    private Paint mCirclePaint;//中心园的画笔
    private Paint mTextPaint;//中间文字的画笔
    private Paint mArcPaint;//外圆环的画笔
    private int mCircleBackground;//中心园的背景色
    private int mTextCorlor;//文字的颜色
    private int mRadius;//圆的半径
    private int mRingColor;//进度条的颜色

    //圆心位置
    private int mCircleX;
    private int mCircleY;

    private double mCurrentAngle;//当前角度
    private int mStartSweepValue = -90;//起始角度位置//圆环开始角度 -90° 正北方向
    private int mTextSize;//文字大小
    private int mCurrentPercent = 0;//当前百分比
    private float mTargetPercent = 0;//目标百分比
    private boolean isDirectArrival;//是否直接跑到指定值
    private float circle_width;//圆环的大小

    public RoundProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        mRadius = (int) typedArray.getDimension(R.styleable.RoundProgressBar_radius, 50);
        mCircleBackground = typedArray.getColor(R.styleable.RoundProgressBar_circle_bg, 0xffafb4db);
        mRingColor = typedArray.getColor(R.styleable.RoundProgressBar_arc_color, 0xff6950a1);
        mTextCorlor = typedArray.getColor(R.styleable.RoundProgressBar_text_color, 0xffffffff);
        circle_width = typedArray.getDimension(R.styleable.RoundProgressBar_circle_width, (float) (0.075 * mRadius));
        typedArray.recycle();
        initPaint();
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        //设置中心园的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBackground);
        //样式一
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(circle_width);
        //样式二
//        mCirclePaint.setStyle(Paint.Style.FILL);
        //设置文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextCorlor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mRadius / 2);
        mTextPaint.setStrokeWidth((float) (0.025 * mRadius));
        //设置外圆环的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(circle_width);
        mArcPaint.setColor(mRingColor);
        //获得文字的字号 因为要设置文字在圆的中心位置
        mTextSize = (int) mTextPaint.getTextSize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(widthMeasureSpec));
    }

    // 当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
    private int measure(int measureSpec) {
        int result = 0;
        //1、先获取测量模式 和 测量大小
        //2、如果测量模式是MatchParent 或者精确值，则宽为测量的宽
        //3、如果测量模式是WrapContent ，则宽为 直径值 与 测量宽中的较小值；否则当直径大于测量宽时，会绘制到屏幕之外；
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //result = 2*mRadius;
            //result =(int) (1.075*mRadius*2);
            result = (int) (mRadius * 2 + mArcPaint.getStrokeWidth() * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //需要将计算放在绘制之前
        if (isDirectArrival) {
            if (mCurrentPercent < mTargetPercent) {
                //当前百分比+1
                mCurrentPercent += 1;
                //当前角度+360
                mCurrentAngle += 3.6;
                //每10ms重画一次
                postInvalidateDelayed(10);
            }
        } else {
            //当前角度+360
            if (mCurrentPercent <= 100) {
                mCurrentAngle = 3.6 * mCurrentPercent;
            }
        }

        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;
        //1、画中间背景圆
        //2、画文字
        //3、画圆环
        //4、判断进度，重新绘制
        canvas.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint);
        canvas.drawText(String.valueOf(mCurrentPercent) + "%", mCircleX, mCircleY + mTextSize / 4, mTextPaint);
        RectF mArcRectF = new RectF(mCircleX - mRadius, mCircleY - mRadius, mCircleX + mRadius, mCircleY + mRadius);
        canvas.drawArc(mArcRectF, mStartSweepValue, (float) mCurrentAngle, false, mArcPaint);

    }

    public void setTargetPercent(float targetPercent) {
        mTargetPercent = targetPercent;
        isDirectArrival = true;
        invalidate();
    }

    public void setProgress(int progress) {
        mCurrentPercent = progress;
        isDirectArrival = false;
        postInvalidate();
    }
}
