package com.changdao.master.common.tool.bar;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 沉浸式参数信息
 *
 * @author geyifeng
 * @date 2017/5/9
 */
public class BarParams implements Cloneable {
    /**
     * 状态栏颜色
     * The Status bar color.
     */
    @ColorInt
    int statusBarColor = Color.TRANSPARENT;
    /**
     * 导航栏颜色
     * The Navigation bar color.
     */
    @ColorInt
    int navigationBarColor = Color.BLACK;

    int defaultNavigationBarColor = Color.BLACK;
    /**
     * 状态栏透明度
     * The Status bar alpha.
     */
    @FloatRange(from = 0f, to = 1f)
    float statusBarAlpha = 0.0f;
    /**
     * 导航栏透明度
     * The Navigation bar alpha.
     */
    @FloatRange(from = 0f, to = 1f)
    float navigationBarAlpha = 0.0f;
    /**
     * 有导航栏的情况，全屏显示
     * The Full screen.
     */
    public boolean fullScreen = false;
    /**
     * 是否隐藏了导航栏
     * The Hide navigation bar.
     */
    boolean hideNavigationBar = false;
    /**
     * 隐藏Bar
     * The Bar hide.
     */
    public int barHide = FLAG_SHOW_BAR;
    /**
     * 状态栏字体深色与亮色标志位
     * The Dark font.
     */
    boolean darkFont = false;
    /**
     * 是否可以修改状态栏颜色
     * The Status bar flag.
     */
    boolean statusBarFlag = true;
    /**
     * 状态栏变换后的颜色
     * The Status bar color transform.
     */
    @ColorInt
    int statusBarColorTransform = Color.BLACK;
    /**
     * 导航栏变换后的颜色
     * The Navigation bar color transform.
     */
    @ColorInt
    int navigationBarColorTransform = Color.BLACK;
    /**
     * 支持view变色
     * The View map.
     */
    Map<View, Map<Integer, Integer>> viewMap = new HashMap<>();
    /**
     * The View alpha.
     */
    @FloatRange(from = 0f, to = 1f)
    float viewAlpha = 0.0f;
    /**
     * The Status bar color content view.
     */
    @ColorInt
    int contentColor = Color.TRANSPARENT;
    /**
     * The Status bar color content view transform.
     */
    @ColorInt
    int contentColorTransform = Color.BLACK;
    /**
     * The Status bar content view alpha.
     */
    @FloatRange(from = 0f, to = 1f)
    float contentAlpha = 0.0f;
    /**
     * The Navigation bar color temp.
     */
    int navigationBarColorTemp = navigationBarColor;
    /**
     * 解决标题栏与状态栏重叠问题
     * The Fits.
     */
    public boolean fits = false;
    /**
     * 解决标题栏与状态栏重叠问题
     * The Title bar view.
     */
    View titleBarView;
    /**
     * 解决标题栏与状态栏重叠问题
     * The Status bar view by height.
     */
    View statusBarView;
    /**
     * flymeOS状态栏字体变色
     * The Flyme os status bar font color.
     */
    @ColorInt
    int flymeOSStatusBarFontColor;
    /**
     * 结合actionBar使用
     * The Is support action bar.
     */
    boolean isSupportActionBar = false;
    /**
     * 解决软键盘与输入框冲突问题
     * The Keyboard enable.
     */
    public boolean keyboardEnable = false;
    /**
     * 软键盘属性
     * The Keyboard mode.
     */
    int keyboardMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    /**
     * 是否能修改导航栏颜色
     * The Navigation bar enable.
     */
    boolean navigationBarEnable = true;
    /**
     * 是否能修改4.4手机导航栏颜色
     * The Navigation bar with kitkat enable.
     */
    boolean navigationBarWithKitkatEnable = true;
    /**
     * 解决出现底部多余导航栏高度，默认为false，已废弃
     * The Fix margin at bottom.
     */
    @Deprecated
    boolean fixMarginAtBottom = false;
    /**
     * xml是否使用fitsSystemWindows属性
     * The System windows.
     */
    boolean systemWindows = false;
    /**
     * 软键盘监听类
     * The Keyboard patch.
     */
    KeyboardPatch keyboardPatch;
    /**
     * 软键盘监听类
     * The On keyboard listener.
     */
    OnKeyboardListener onKeyboardListener;
    /**
     * 隐藏状态栏
     * Flag hide status bar bar hide.
     */
    public static final int FLAG_HIDE_STATUS_BAR = 0;
    /**
     * 隐藏导航栏
     * Flag hide navigation bar bar hide.
     */
    public static final int FLAG_HIDE_NAVIGATION_BAR = 1;
    /**
     * 隐藏状态栏和导航栏
     * Flag hide bar bar hide.
     */
    public static final int FLAG_HIDE_BAR = 2;
    /**
     * 显示状态栏和导航栏
     * Flag show bar bar hide.
     */
    public static final int FLAG_SHOW_BAR = 3;

    @Override

    protected BarParams clone() {
        BarParams barParams = null;
        try {
            barParams = (BarParams) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return barParams;
    }
}
