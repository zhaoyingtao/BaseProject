package com.zyt.basics.oneframe;

import com.zyt.master.common.net.net.RetrofitClientUtil;
import com.zyt.master.common.net.net.BasePresenter;
import com.zyt.appcommon.net.RequestApiCallBackHelper;
import com.zyt.basics.MainModelApi;

import org.json.JSONObject;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */

public class MainPresenter extends BasePresenter<MainIView> {
    public MainPresenter(MainIView mView) {
        super(mView);
    }

    public void getMainData() {
        addSubscription(RetrofitClientUtil.initClient().create(MainModelApi.class).getGrades(), new RequestApiCallBackHelper() {
            @Override
            protected void dealSuccessData(boolean isSuccess, JSONObject rootJson) {
                mView.getDataSuccess(rootJson);
            }
        });
    }
}
