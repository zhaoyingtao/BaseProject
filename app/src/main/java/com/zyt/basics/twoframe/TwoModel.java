package com.zyt.basics.twoframe;

import org.json.JSONObject;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */

public interface TwoModel {
    interface V {
        void getDataSuccess(JSONObject object);

        void getDataFail();
    }
    interface P{
        void getTwoData();
    }
}
