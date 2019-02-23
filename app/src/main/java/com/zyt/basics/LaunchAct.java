package com.zyt.basics;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zyt.appcommon.AppBaseActivity;
import com.zyt.appcommon.RouterList;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/23
 * desc   :
 */

public class LaunchAct extends AppBaseActivity {
    @Override
    protected int setContentViewId() {
        return R.layout.act_launch;
    }

    @Override
    protected void firstInitView() {
        ARouter.getInstance().build(RouterList.ACT_MAIN).navigation();
        finish();
    }

    @Override
    protected void secondInitData() {

    }
}
