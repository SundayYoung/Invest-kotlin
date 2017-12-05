package com.weiyankeji.zhongmei.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.Animation

import com.igexin.sdk.PushManager
import com.weiyankeji.library.customview.bottomview.BottomNavigationItem
import com.weiyankeji.library.customview.bottomview.BottomNavigationView
import com.weiyankeji.library.net.client.RestBaseCallBack
import com.weiyankeji.library.utils.PermissionsUtil
import com.weiyankeji.library.utils.SharedPreferencesUtil
import com.weiyankeji.library.utils.StringUtil
import com.weiyankeji.library.utils.ToastUtil
import com.weiyankeji.zhongmei.R
import com.weiyankeji.zhongmei.api.UrlConfig
import com.weiyankeji.zhongmei.application.ZMApplication
import com.weiyankeji.zhongmei.event.LoginSuccessEvent
import com.weiyankeji.zhongmei.service.WyIntentService
import com.weiyankeji.zhongmei.service.WyPushService
import com.weiyankeji.zhongmei.ui.adapter.FragmentPagerAdapter
import com.weiyankeji.zhongmei.ui.fragment.HomeFragment
import com.weiyankeji.zhongmei.ui.fragment.InvestFragment
import com.weiyankeji.zhongmei.ui.fragment.LoginFragment
import com.weiyankeji.zhongmei.ui.fragment.MineFragment
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest
import com.weiyankeji.zhongmei.ui.mmodel.bean.MessageNoticeBean
import com.weiyankeji.zhongmei.utils.CommonUtils
import com.weiyankeji.zhongmei.utils.ConstantUtils
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils
import com.weiyankeji.zhongmei.utils.UserUtils

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var mLastPosition: Int = 0
    private var mFragmentAdapter: FragmentPagerAdapter? = null

    var mType: Int = 0
    var mMinePositon = 0

//    internal var mHomeFragment: HomeFragment? = null
    internal var mInvestFragment: InvestFragment? = null
    internal var mMineFragment: InvestFragment? = null

    private var mExitTime: Long = 0

    override fun setContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData(bundle: Bundle?) {

        setStatusBarColor(R.color.blue_00)
        //getui init
        PushManager.getInstance().initialize(this.applicationContext, WyPushService::class.java)
        PushManager.getInstance().registerPushIntentService(this.applicationContext, WyIntentService::class.java)

        mBottomView!!.titleState = BottomNavigationView.TitleState.ALWAYS_SHOW
//        val itemHome = BottomNavigationItem(R.string.bottomview_main_home, R.drawable.common_bottombar_icon_home_normal, R.drawable.common_bottombar_icon_home_selected)
        val itemInvest = BottomNavigationItem(R.string.bottomview_main_invest, R.drawable.common_bottombar_icon_invest_normal, R.drawable.common_bottombar_icon_invest_selected)
        val itemMine = BottomNavigationItem(R.string.bottomview_main_mine, R.drawable.common_bottombar_icon_my_normal, R.drawable.common_bottombar_icon_my_selected)

//        mBottomView!!.addItem(itemHome)
        mBottomView!!.addItem(itemInvest)
        mBottomView!!.addItem(itemMine)
        mBottomView!!.setItemForMenu(false)
        mFragmentVp!!.offscreenPageLimit = 3
        mFragmentAdapter = FragmentPagerAdapter(supportFragmentManager)
//        mHomeFragment = HomeFragment()
        mInvestFragment = InvestFragment()
        mMineFragment = InvestFragment()
//        mFragmentAdapter!!.addFragment(mInvestFragment)
        mFragmentAdapter!!.addFragment(mInvestFragment)
        mFragmentAdapter!!.addFragment(mMineFragment)
        mFragmentVp!!.adapter = mFragmentAdapter
        mBottomView!!.setOnTabSelectedListener(BottomNavigationView.OnTabSelectedListener { position, wasSelected ->
            if (wasSelected) {
                return@OnTabSelectedListener true
            }
            if (mLastPosition != position) {
                mFragmentVp!!.setCurrentItem(position, false)
                val currentFragment = mFragmentAdapter!!.currentFragment
                currentFragment.willBeDisplayed(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        //异步刷新首页数据
//                        when (position) {
//                            0 -> if (mInvestFragment != null) {
//                                mInvestFragment!!.onRefresh()
//                            }
//                            1 -> if (mHomeFragment != null) {
//                                mHomeFragment!!.onRefresh()
//                            }
//                            else -> {
//                            }
//                        }

                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                mLastPosition = position
            }
            //小红点显示逻辑
            mMinePositon = position

            true
        })

        //event 注册事件
        EventBus.getDefault().register(this)
        requestPermission()
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onMainEventBus(event: LoginSuccessEvent?) {
        if (event == null || StringUtil.isNullOrEmpty(event.msg)) {
            return
        }
        if (event.msg == getString(R.string.login)) {
//            if (mHomeFragment != null) {
//                mHomeFragment!!.onRefresh()
//            }
            if (mInvestFragment != null) {
                mInvestFragment!!.onRefresh()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //event 注销事件
        EventBus.getDefault().unregister(this)
    }

    /**
     * 显示小红点
     */
    fun showNotification() {
        if (mLastPosition != NOTIFICATION_POSITION) {
            mBottomView!!.setNotification(NOTIFICATION_POSITION)
        }
    }

    /**
     * 隐藏小红点
     */
    fun hideNotification() {
        mBottomView!!.closeNotification(NOTIFICATION_POSITION)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mType = intent.getIntExtra(ConstantUtils.KEY_TYPE, 0)
        //如果是修改密码后登陆返回则跳 我的
        if (mType == LoginFragment.TYPE_CHANGE_LOGIN_PW || mType == LoginFragment.TYPE_CHANGE_BIND_PHONE) {
            mFragmentVp!!.currentItem = 2
            mBottomView!!.currentItem = 2
        } else if (mType == MainActivity.TYPE_PAY_ACCOUNT_FINISH || mType == MainActivity.TYPE_CUSTOM_FINISH) {
            mFragmentVp!!.currentItem = 1
            mBottomView!!.currentItem = 1
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //moveTaskToBack(true);
    private fun exitApp() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtil.showShortToast(this, getString(R.string.click_again_exit) + getString(R.string.app_name))
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    private fun requestPermission() {
        PermissionsUtil.with(this).addPermission(Manifest.permission.READ_PHONE_STATE).setCallback(object : PermissionsUtil.Callback {
            override fun onPermissionGranted(permissions: Array<String>) {}

            override fun onPermissionDenied(permissions: Array<String>) {}
        }).request()
    }

    companion object {
        //账户开通完成，跳首页投资
        val TYPE_PAY_ACCOUNT_FINISH = 1001
        //定期定制跳转投资
        val TYPE_CUSTOM_FINISH = 1033

        private val NOTIFICATION_POSITION = 1
    }
}
