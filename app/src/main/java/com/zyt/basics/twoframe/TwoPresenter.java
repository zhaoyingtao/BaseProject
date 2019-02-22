package com.zyt.basics.twoframe;

import com.zyt.master.common.net.net.BasePresenter;
import com.zyt.master.common.net.net.RequestParams;
import com.zyt.master.common.net.net.RetrofitClientUtil;
import com.zyt.appcommon.net.RequestApiCallBackHelper;
import com.zyt.basics.MainModelApi;

import org.json.JSONObject;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */

public class TwoPresenter extends BasePresenter<TwoModel.V> implements TwoModel.P{
    public TwoPresenter(TwoModel.V mView) {
        super(mView);
    }

    @Override
    public void getTwoData() {
        RequestParams requestParams = new RequestParams();
        requestParams.addFormPart("type",4);
        requestParams.addFormPart("status","1");
        addSubscription(RetrofitClientUtil.initClient().create(MainModelApi.class).uploadUserTime(requestParams.create()),
                new RequestApiCallBackHelper() {
            @Override
            protected void dealSuccessData(boolean isSuccess, JSONObject rootJson) {
                mView.getDataSuccess(rootJson);
            }
        });
    }
}
