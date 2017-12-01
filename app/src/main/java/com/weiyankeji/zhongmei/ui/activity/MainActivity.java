package com.weiyankeji.zhongmei.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.animation.Animation;

import com.igexin.sdk.PushManager;
import com.weiyankeji.library.customview.bottomview.BottomNavigationItem;
import com.weiyankeji.library.customview.bottomview.BottomNavigationView;
import com.weiyankeji.library.customview.bottomview.BottomNavigationViewPager;
import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.PermissionsUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.event.LoginSuccessEvent;
import com.weiyankeji.zhongmei.service.WyIntentService;
import com.weiyankeji.zhongmei.service.WyPushService;
import com.weiyankeji.zhongmei.ui.adapter.FragmentPagerAdapter;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.fragment.HomeFragment;
import com.weiyankeji.zhongmei.ui.fragment.InvestFragment;
import com.weiyankeji.zhongmei.ui.fragment.LoginFragment;
import com.weiyankeji.zhongmei.ui.fragment.MineFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MessageNoticeBean;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindColor;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    //账户开通完成，跳首页投资
    public static final int TYPE_PAY_ACCOUNT_FINISH = 1001;
    //定期定制跳转投资
    public static final int TYPE_CUSTOM_FINISH = 1033;

    private static final int NOTIFICATION_POSITION = 2;
    private int mLastPosition;
    private FragmentPagerAdapter mFragmentAdapter;

    @BindView(R.id.bnv_main)
    BottomNavigationView mBottomView;
    @BindView(R.id.bvp_content)
    BottomNavigationViewPager mFragmentVp;
    @BindColor(R.color.blue_00)
    int mTitleColor;


    public int mType;
    public int mMinePositon = 0;

    HomeFragment mHomeFragment;
    InvestFragment mInvestFragment;
    MineFragment mMineFragment;

    private long mExitTime;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle bundle) {

        setStatusBarColor(mTitleColor);
        //getui init
        PushManager.getInstance().initialize(this.getApplicationContext(), WyPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), WyIntentService.class);

        mBottomView.setTitleState(BottomNavigationView.TitleState.ALWAYS_SHOW);
        BottomNavigationItem itemHome = new BottomNavigationItem(R.string.bottomview_main_home, R.drawable.common_bottombar_icon_home_normal, R.drawable.common_bottombar_icon_home_selected);
        BottomNavigationItem itemInvest = new BottomNavigationItem(R.string.bottomview_main_invest, R.drawable.common_bottombar_icon_invest_normal, R.drawable.common_bottombar_icon_invest_selected);
        BottomNavigationItem itemMine = new BottomNavigationItem(R.string.bottomview_main_mine, R.drawable.common_bottombar_icon_my_normal, R.drawable.common_bottombar_icon_my_selected);

        mBottomView.addItem(itemHome);
        mBottomView.addItem(itemInvest);
        mBottomView.addItem(itemMine);
        mBottomView.setItemForMenu(false);
        mFragmentVp.setOffscreenPageLimit(3);
        mFragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        mHomeFragment = new HomeFragment();
        mInvestFragment = new InvestFragment();
        mMineFragment = new MineFragment();
        mFragmentAdapter.addFragment(mHomeFragment);
        mFragmentAdapter.addFragment(mInvestFragment);
        mFragmentAdapter.addFragment(mMineFragment);
        mFragmentVp.setAdapter(mFragmentAdapter);
        mBottomView.setOnTabSelectedListener(new BottomNavigationView.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(final int position, boolean wasSelected) {
                if (wasSelected) {
                    return true;
                }
                if (mLastPosition != position) {
                    mFragmentVp.setCurrentItem(position, false);
                    BaseFragment currentFragment = mFragmentAdapter.getCurrentFragment();
                    currentFragment.willBeDisplayed(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //异步刷新首页数据
                            switch (position) {
                                case 0:
                                    if (mInvestFragment != null) {
                                        mInvestFragment.onRefresh();
                                    }
                                    break;
                                case 1:
                                    if (mHomeFragment != null) {
                                        mHomeFragment.onRefresh();
                                    }
                                    break;
                                default:
                                    break;
                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mLastPosition = position;
                }
                //小红点显示逻辑
                mMinePositon = position;
                netRedNotif();

                return true;
            }
        });

        //event 注册事件
        EventBus.getDefault().register(this);
        requestPermission();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMainEventBus(LoginSuccessEvent event) {
        if (event == null || StringUtil.isNullOrEmpty(event.msg)) {
            return;
        }
        if (event.msg.equals(getString(R.string.login))) {
            if (mHomeFragment != null) {
                mHomeFragment.onRefresh();
            }
            if (mInvestFragment != null) {
                mInvestFragment.onRefresh();
            }
        }
    }

    /**
     * 请求小红点网络,
     */
    public void netRedNotif() {
        if (!UserUtils.isUserLogin()) {
            return;
        }
        CommonUtils.requestData(UrlConfig.NOTICE_MESSAGE, new BaseRequest(), new RestBaseCallBack<MessageNoticeBean>() {
            @Override
            public void onResponse(MessageNoticeBean response) {
                if (response == null) {
                    return;
                }
                CommonUtils.messageNotificationOfActivity(MainActivity.this, response.datetime, mMinePositon, mFragmentAdapter.getCount() - 1, mMineFragment.getImageview());
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (code == ErrorMsgUtils.TOKEN_INVALID || code == ErrorMsgUtils.NEED_RELOGIN) {
                    CommonUtils.setNotificationPointOfActivity(MainActivity.this, SharedPreferencesUtil.getSharedPreferences(ZMApplication.getZMContext(), ConstantUtils.SHAREDPREFERENCES_NAME), mMinePositon, mFragmentAdapter.getCount() - 1, mMineFragment.getImageview());

                    //首页部分，需要重新登录 统一放到小红点接口返回处理，eventbus 通知去掉"定制理财"
                    ErrorMsgUtils.isNeedLogin(code);
                    ToastUtil.showShortToast(MainActivity.this, msg);
                    startActivity(FragmentContainerActivity.getFragmentContainerActivityIntent(MainActivity.this, LoginFragment.class));

                }
            }
        });
    }


    /**
     * 小红点请求
     */
    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setNotificationPointOfActivity(MainActivity.this, SharedPreferencesUtil.getSharedPreferences(this, ConstantUtils.SHAREDPREFERENCES_NAME), mMinePositon, mFragmentAdapter.getCount() - 1, mMineFragment.getImageview());
        netRedNotif();
        if (mMinePositon == mFragmentAdapter.getCount() - 1) {
            SharedPreferencesUtil.put(MainActivity.this, ConstantUtils.SHAREDPREFERENCES_NAME, ConstantUtils.SP_MESSAGE_MINE, false);
            hideNotification();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //event 注销事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示小红点
     */
    public void showNotification() {
        if (mLastPosition != NOTIFICATION_POSITION) {
            mBottomView.setNotification(NOTIFICATION_POSITION);
        }
    }

    /**
     * 隐藏小红点
     */
    public void hideNotification() {
        mBottomView.closeNotification(NOTIFICATION_POSITION);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mType = intent.getIntExtra(ConstantUtils.KEY_TYPE, 0);
        //如果是修改密码后登陆返回则跳 我的
        if (mType == LoginFragment.TYPE_CHANGE_LOGIN_PW || mType == LoginFragment.TYPE_CHANGE_BIND_PHONE) {
            mFragmentVp.setCurrentItem(2);
            mBottomView.setCurrentItem(2);
        } else if (mType == MainActivity.TYPE_PAY_ACCOUNT_FINISH || mType == MainActivity.TYPE_CUSTOM_FINISH) {
            mFragmentVp.setCurrentItem(1);
            mBottomView.setCurrentItem(1);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //moveTaskToBack(true);
    private void exitApp() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.showShortToast(this, getString(R.string.click_again_exit) + getString(R.string.app_name));
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void requestPermission() {
        PermissionsUtil.with(this).addPermission(Manifest.permission.READ_PHONE_STATE).setCallback(new PermissionsUtil.Callback() {
            @Override
            public void onPermissionGranted(String[] permissions) {
            }

            @Override
            public void onPermissionDenied(String[] permissions) {
            }
        }).request();
    }
}
