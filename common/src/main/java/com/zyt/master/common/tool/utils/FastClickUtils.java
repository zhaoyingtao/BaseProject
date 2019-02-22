package com.zyt.master.common.tool.utils;

/**
 * Created by zyt on 2018/11/05.
 * 防止快速点击操作
 */

public class FastClickUtils {
    private static FastClickUtils fastClickUtils;
    private long minTimeLimit = 1000;

    public static FastClickUtils init() {
        if (fastClickUtils == null) {
            synchronized (FastClickUtils.class) {
                if (fastClickUtils == null) {
                    fastClickUtils = new FastClickUtils();
                }
            }
        }
        return fastClickUtils;
    }

    private long startTime;

    /**
     * 快速点击两秒以内算快速
     *
     * @return
     */
    public boolean isClickFast() {
//        LogUtil.e("System.currentTimeMillis() - startTime====" + (System.currentTimeMillis() - startTime));
        if (System.currentTimeMillis() - startTime < minTimeLimit) {
            startTime = System.currentTimeMillis();
            return true;
        }
        startTime = System.currentTimeMillis();
        return false;
    }

    public void setMinTimeLimit(long minTimeLimit) {
        this.minTimeLimit = minTimeLimit;
    }
}
