package com.zyt.basics.oneframe;

import org.json.JSONObject;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */

public interface MainIView {
    void getDataSuccess(JSONObject object);
    void getDataFail();
}
