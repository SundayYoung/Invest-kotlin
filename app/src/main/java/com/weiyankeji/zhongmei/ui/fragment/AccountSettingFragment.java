package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mpresenter.AccountSettingPresenter;
import com.weiyankeji.zhongmei.ui.mview.AccountSettingView;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/9/1.
 */

public class AccountSettingFragment extends BaseMvpFragment<AccountSettingView, AccountSettingPresenter> {
    @BindView(R.id.iv_account_setting_pay_status)
    ImageView mIvPay;
    @BindView(R.id.iv_account_setting_real_name)
    ImageView mIvAuth;

    @BindView(R.id.tv_account_setting_pay_status)
    TextView mTvAccountState;
    @BindView(R.id.tv_account_setting_real_name)
    TextView mTvAuthName;

    @Override
    public AccountSettingPresenter initPresenter() {
        return new AccountSettingPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_account_setting;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.mine_account_settings), true);
        initPayAccountLayout();
        initAuthLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.ll_account_setting_pay_account, R.id.rl_account_setting_auth, R.id.rl_account_setting_pay_pw, R.id.rl_account_setting_change_login_pw, R.id.rl_account_setting_auth_code})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_account_setting_pay_account:
                UserUtils.getInstance().checkUserInvidaType(this);
                break;
            case R.id.rl_account_setting_auth:
                if (!UserUtils.getInstance().isRealAuth()) {
                    startFragment(AuthNameFragment.class);
                }
                break;
            case R.id.rl_account_setting_pay_pw:
                if (UserUtils.getInstance().isSetPayPw()) {
                    startFragment(PayPassWordFragment.class);
                } else {
                    UserUtils.getInstance().checkUserInvidaType(this);
                }
                break;
            case R.id.rl_account_setting_change_login_pw:
                startFragment(ChangeLoginPwFragment.class);
                break;
            case R.id.rl_account_setting_auth_code:
                startFragment(AuthCodeCheckFragment.class);
                break;
            default:
        }
    }

    /**
     * 支付账户
     */
    public void initPayAccountLayout() {
        if (UserUtils.getInstance().isOpenAccount()) {
            mIvPay.setVisibility(View.INVISIBLE);
            mTvAccountState.setText(R.string.has_been_opened);
        } else {
            mIvPay.setVisibility(View.VISIBLE);
            mTvAccountState.setText(R.string.no_open);
        }
    }

    /**
     * 实名认证
     */
    public void initAuthLayout() {
        if (UserUtils.getInstance().isRealAuth()) {
            mIvAuth.setVisibility(View.INVISIBLE);
            mTvAuthName.setText(UserUtils.getInstance().getUserObject().getRealname());
        } else {
            mIvAuth.setVisibility(View.VISIBLE);
            mTvAuthName.setText("");
        }
    }

}
