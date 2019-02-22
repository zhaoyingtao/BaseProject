package com.zyt.appcommon;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zyt.master.common.base.BaseActivity;

/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/22
 * desc   : 项目中的baseActivity，可以自定义添加功能
 */

public abstract class AppBaseActivity<T extends ViewDataBinding> extends BaseActivity<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //若是不需要设置状态栏透明，此设置必须放置在super.onCreate之前处理
//        isStatusBar = false;
        super.onCreate(savedInstanceState);

    }
}
