package com.zyt.basics.twoframe;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zyt.appcommon.AppBaseActivity;
import com.zyt.appcommon.RouterList;
import com.zyt.basics.R;
import com.zyt.basics.databinding.ActTwoBinding;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/21
 * desc   :
 */
@Route(path = RouterList.ACT_TWO)
public class TwoActivity extends AppBaseActivity<ActTwoBinding> implements TwoModel.V {
    private TwoPresenter twoPresenter;
    private Unbinder unbinder;

    @Override
    protected int setContentViewId() {
        return R.layout.act_two;
    }

    @Override
    protected void firstInitView() {
        unbinder = ButterKnife.bind(this);
        twoPresenter = new TwoPresenter(this);
        twoPresenter.getTwoData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void secondInitData() {

    }

    @Override
    public void getDataSuccess(JSONObject object) {

    }

    @Override
    public void getDataFail() {

    }
}
