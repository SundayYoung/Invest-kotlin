package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.StatusBarUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.multi.RegisterRequest;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.RegisterPresent;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.ui.mview.RegisterView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CountDownBtnUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @aythor: lilei
 * time: 2017/9/4  下午4:37
 * function:
 */

public class RegisterFragment extends BaseMvpFragment<RegisterView, RegisterPresent> implements RegisterView, CountDownTimerView {

    @BindView(R.id.et_password)
    EditText mEtPass;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.iv_number_clear)
    ImageView mIvNumberClear;
    @BindView(R.id.cb_agree)
    CheckBox mCbAgree;
    @BindView(R.id.btn_getcode)
    Button mBtnCode;
    @BindView(R.id.btn_register)
    Button mBtnRegist;
    @BindView(R.id.et_friend_mobile)
    EditText mEtFriendMobile;
    @BindView(R.id.et_vercode)
    EditText mEtVercode;
    @BindString(R.string.toast_mobile_erro_rule)
    String mMobileErro;
    @BindString(R.string.toast_vercode_erro_rule)
    String mVercodeErro;
    @BindString(R.string.toast_password_erro_rule)
    String mPasswordErro;
    @BindString(R.string.toast_agree_erro_rule)
    String mAgreeErro;
    @BindString(R.string.toast_regist_success)
    String mRegistSuccess;


    String mMobile;
    String mPass;
    String mFriendMobile;
    String mVercode;
    boolean mIsCheck;


    //密文
    boolean mIsDesensi = false;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_regist;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        StatusBarUtil.setWindowStatusBarColor(getActivity(), R.color.white, true);
        addBackListener();
        mCbAgree.setChecked(true);
        setBackImage(R.drawable.login_icon_arrow_left);
        setTitleBackground(R.color.white);
        checkRule();


    }

    /**
     * 判断规则
     */
    public void checkRule() {
        checkListener(mEtMobile);
        checkListener(mEtPass);
        checkListener(mEtVercode);
        CommonUtils.setEditCallBack(mEtPass);
        mCbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMobile = mEtMobile.getText().toString().trim();
                mPass = mEtPass.getText().toString().trim();
                mVercode = mEtVercode.getText().toString().trim();
                if (TextUtils.isEmpty(mMobile) || TextUtils.isEmpty(mPass) || TextUtils.isEmpty(mVercode)) {
                    mBtnRegist.setEnabled(false);
                } else {
                    mBtnRegist.setEnabled(true);
                }
            }
        });
    }

    /**
     * 监听edittext
     *
     * @param editext
     */
    public void checkListener(final EditText editext) {
        editext.addTextChangedListener(new TextWatcherAdapter() {
            public void afterTextChanged(Editable editable) {
                String mResultMobile = mEtMobile.getText().toString().trim();
                String mResultPassword = mEtPass.getText().toString().trim();
                String mResultCode = mEtVercode.getText().toString().trim();
                if (editext.equals(mEtMobile)) {
                    if (ExtendUtil.isPhoneNumber(mResultMobile)) {
                        if (mPresenter.isCountDownOver()) {
                            mBtnCode.setEnabled(true);
                        }
                    } else {
                        mBtnCode.setEnabled(false);
                    }
                    mIvNumberClear.setVisibility(StringUtil.isNullOrEmpty(editable.toString()) ? View.GONE : View.VISIBLE);
                }
                if (TextUtils.isEmpty(mResultMobile) || TextUtils.isEmpty(mResultPassword) || TextUtils.isEmpty(mResultCode)) {
                    mBtnRegist.setEnabled(false);
                } else {
                    mBtnRegist.setEnabled(true);
                }

            }
        });
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }


    @OnClick({R.id.ib_desensitization, R.id.btn_register, R.id.tv_agree_rule,R.id.tv_privacy_protocol, R.id.btn_getcode, R.id.iv_number_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_desensitization:
                mIsDesensi = CommonUtils.setIsDesensi(mEtPass, (ImageButton) view, mIsDesensi);
                break;
            case R.id.btn_getcode:
                getCode();
                break;
            case R.id.btn_register:
                regist();
                break;
            case R.id.tv_agree_rule:
                CommonUtils.intentWebView(RegisterFragment.this, getString(R.string.register_user_rule), UrlConfig.REGISTER_PROTOCOL);
                break;
            case R.id.tv_privacy_protocol:
                CommonUtils.intentWebView(RegisterFragment.this, getString(R.string.register_privacy_protocol), UrlConfig.PRIVACY_PROTOCOL);
                break;
            case R.id.iv_number_clear:
                mEtMobile.setText("");
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getCode() {
        mMobile = mEtMobile.getText().toString().trim();
        if (ExtendUtil.isPhoneNumber(mMobile)) {
            mPresenter.loadCode(new SendCodeRequest(mMobile, ConstantUtils.VERIFY_TYPE_REGI));
            CountDownBtnUtils.unAvailableState(mBtnCode);
            mPresenter.startCountDownTimer(this);
        } else {
            ToastUtil.showShortToast(mContext, mMobileErro);
        }

    }

    /**
     * 获取验证码成功
     */
    @Override
    public void loadCodeSuccess() {

    }

    /**
     * 获取验证码失败
     *
     * @param code
     * @param msg
     */
    @Override
    public void loadCodeFailure(int code, String msg) {
        ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
    }

    /**
     * 注册
     */
    public void regist() {
        mMobile = mEtMobile.getText().toString().trim();
        mIsCheck = mCbAgree.isChecked();
        mPass = mEtPass.getText().toString().trim();
        mFriendMobile = mEtFriendMobile.getText().toString().trim();
        mVercode = mEtVercode.getText().toString().trim();
        if (!mIsCheck) {
            ToastUtil.showShortToast(mContext, getString(R.string.toast_agree_erro_rule));
            return;
        }
        if (!ExtendUtil.isPhoneNumber(mMobile)) {
            ToastUtil.showShortToast(mContext, getString(R.string.toast_mobile_erro_rule));
            return;
        }
        if (!CommonUtils.checkPass(mPass)) {
            ToastUtil.showShortToast(mContext, mPasswordErro);
            return;
        }
        if (!(ExtendUtil.isPhoneNumber(mFriendMobile) || TextUtils.isEmpty(mFriendMobile))) {
            ToastUtil.showShortToast(mContext, getString(R.string.toast_invite_pass_erro));
            return;
        }
        mPresenter.registCode(new RegisterRequest(mMobile, mVercode, mPass, mFriendMobile));

    }

    /**
     * 注册成功
     */
    @Override
    public void registSuccess() {
        ToastUtil.showShortToast(getActivity(), mRegistSuccess);
        getActivity().finish();
    }

    /**
     * 注册失败
     *
     * @param code
     * @param msg
     */
    @Override
    public void registFailure(int code, String msg) {
        ToastUtil.showShortToast(mContext, msg);
    }


    @Override
    public RegisterPresent initPresenter() {
        return new RegisterPresent();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
    }


    @Override
    public void schedule(long time) {
        if (isAdded()) {
            mBtnCode.setText(time + getString(R.string.after_re_obtain));
        }
    }

    @Override
    public void countdownFinish() {
        CountDownBtnUtils.availableState(mBtnCode);
        if (!CommonUtils.checkPhoneNumber(mEtMobile.getText().toString())) {
            mBtnCode.setEnabled(false);
        }
    }

}
