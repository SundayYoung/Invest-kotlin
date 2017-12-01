package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.event.LoginSuccessEvent;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mpresenter.UserInfoPresenter;
import com.weiyankeji.zhongmei.ui.mview.UserInfoView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @aythor: lilei
 * time: 2017/9/1  下午2:22
 * function:
 */

public class UserInfoFragment extends BaseMvpFragment<UserInfoView, UserInfoPresenter> implements UserInfoView {
    @BindString(R.string.mine_information)
    String mTitle;
    @BindView(R.id.rl_icon)
    RelativeLayout mRlIcon;
    @BindView(R.id.rl_mobile)
    RelativeLayout mRlMobile;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.btn_logout)
    TextView mBtnLogout;

    User mUser;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_userinfo;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        addBackListener();
        setTitle(mTitle);
        mUser = UserUtils.getInstance().getUserObject();
        fullData();
        mPresenter.loadUserInfo(this, new BaseRequest());
    }

    /**
     * 填充信息
     */
    public void fullData() {
        if (TextUtils.isEmpty(mUser.avatar)) {
            ImageLoaderUtil.loadCircleImageFromLocalRes(mContext, R.drawable.login_logo, mIvIcon, 0);
        } else {
            ImageLoaderUtil.loadCircleImageFromUrl(mContext, mUser.avatar, mIvIcon, 0);
        }
        mTvMobile.setText(mUser.mobile);
    }


    @Override
    public void showLoading() {
        setLoading(true);

    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    /**
     * 退出成功
     */
    @Override
    public void onSuccess() {
        UserUtils.getInstance().deleteUser();
        SharedPreferencesUtil.clear(ZMApplication.getZMContext(), ConstantUtils.SHAREDPREFERENCES_NAME);
        onFinishFragment();
        EventBus.getDefault().post(new LoginSuccessEvent(mContext.getString(R.string.login)));
    }

    /**
     * 退出失败
     *
     * @param mCode
     * @param msg
     */
    @Override
    public void onFailure(int mCode, String msg) {
        UserUtils.getInstance().deleteUser();
        SharedPreferencesUtil.clear(ZMApplication.getZMContext(), ConstantUtils.SHAREDPREFERENCES_NAME);
        onFinishFragment();
    }

    /**
     * 请求之后填充数据
     *
     * @param respenseUser
     */
    @Override
    public void setData(User respenseUser) {
        mUser = UserUtils.getInstance().savaUserExcept(mUser, respenseUser);
        UserUtils.getInstance().saveUserObject(mUser);
        fullData();
    }

    @Override
    public UserInfoPresenter initPresenter() {
        return new UserInfoPresenter();
    }


    @OnClick({R.id.btn_logout, R.id.rl_mobile})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                mPresenter.logout(new BaseRequest());
                break;
            case R.id.rl_mobile:
                CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_CHANGE_BIND_MOLIBE);
                break;
            default:
                break;
        }
    }
}
