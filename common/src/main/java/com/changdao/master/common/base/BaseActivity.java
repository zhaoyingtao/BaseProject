package com.changdao.master.common.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.changdao.master.common.R;
import com.changdao.master.common.tool.bar.StatusNavBar;



/**
 * author : zyt
 * e-mail : 632105276@qq.com
 * date   : 2019/2/20
 * desc   : 基础类，被继承用的
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected boolean isStatusBar = true;
    protected T mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contentViewId = setContentViewId();
        if (contentViewId != 0) {
            mBinding = DataBindingUtil.setContentView(this, contentViewId);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        //设置状态栏透明度
        if (isStatusBar) {
            StatusNavBar.with(this).statusBarColor(R.color.transparent)
                    .statusBarDarkFont(true).navigationBarColor(R.color.black_degree_50).init();
        }
        //所有的活动都要注入此操作，否则ARouter将无法跳转
        ARouter.getInstance().inject(this);
        //管理活动
        ActivityManager.getInstance().addActivity(this);
        firstInitView();
        secondInitData();
    }

    public Activity getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(Fragment fragment, @IdRes int frameId) {
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(Fragment fragment, @IdRes int frameId) {
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment
     */
    protected void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public void closeSoftInput() {
        //有软键盘就关闭软键盘，没有就算了
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        closeSoftInput();
    }

    protected abstract int setContentViewId();

    protected abstract void firstInitView();

    protected abstract void secondInitData();


}
