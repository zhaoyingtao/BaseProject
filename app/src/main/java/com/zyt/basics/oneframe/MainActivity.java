package com.zyt.basics.oneframe;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zyt.master.common.Listener.EventBus;
import com.zyt.master.common.Listener.OnAntiDoubleClickListener;
import com.zyt.master.common.tool.utils.PermissionsUtils;
import com.zyt.master.common.tool.utils.ToastUtils;
import com.zyt.appcommon.AppBaseActivity;
import com.zyt.appcommon.RouterList;
import com.zyt.basics.R;
import com.zyt.basics.databinding.ActivityMainBinding;
import com.zyt.master.common.tool.utils.WheelsUtils;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   :
 */
@Route(path = RouterList.ACT_MAIN)
public class MainActivity extends AppBaseActivity<ActivityMainBinding> implements MainIView {
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
        //监听EventBus的监听,一下两种方式选择一种使用，第二种需要支持Lambda表达式
        EventBus.getInstance().subscribe(this, String.class, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
        EventBus.getInstance().subscribe(this, String.class, value ->{

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除监听
        EventBus.getInstance().unsubscribe(this);
    }

    @Override
    public void getDataSuccess(JSONObject object) {
        ToastUtils.getInstance().showToast("请求数据成功");
    }

    @Override
    public void getDataFail() {

    }
}
