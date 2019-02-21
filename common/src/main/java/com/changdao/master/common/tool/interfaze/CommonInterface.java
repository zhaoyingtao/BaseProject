package com.changdao.master.common.tool.interfaze;

/**
 * Created by dhf on 2017/3/2.
 * 公共接口
 */

public interface CommonInterface {
    void clickCurrentItem();
    void clickCurrentItem(int position);
    void deliveryStringValue(String value);
    void deliveryStringValue(Object obj);
    void deliveryStringValue(String var1, String var2, String var3);
}
