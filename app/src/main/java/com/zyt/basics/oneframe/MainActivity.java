package com.zyt.basics.oneframe;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.changdao.master.common.Listener.OnAntiDoubleClickListener;
import com.changdao.master.common.base.BaseActivity;
import com.changdao.master.common.tool.utils.ToastUtils;
import com.zyt.appcommon.RouterList;
import com.zyt.basics.R;
import com.zyt.basics.databinding.ActivityMainBinding;

import org.json.JSONObject;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   :
 */
@Route(path = RouterList.ACT_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainIView {
    private MainPresenter mainPresenter;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void firstInitView() {
        mainPresenter = new MainPresenter(this);
        mainPresenter.getMainData();
    }

    @Override
    protected void secondInitData() {
        mBinding.skipBtn.setOnClickListener(new OnAntiDoubleClickListener() {
            @Override
            public void onAntiDoubleClick(View v) {
                ARouter.getInstance().build(RouterList.ACT_TWO).navigation();
            }
        });
    }

    @Override
    public void getDataSuccess(JSONObject object) {
        ToastUtils.getInstance().showToast("请求数据成功");
    }

    @Override
    public void getDataFail() {

    }
}
