package com.weiyankeji.zhongmei.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.KeyBoardUtil;
import com.weiyankeji.library.utils.ScreenUtil;
import com.weiyankeji.library.utils.StatusBarUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.event.LoginSuccessEvent;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mmodel.multi.LoginRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.LoginPresenter;
import com.weiyankeji.zhongmei.ui.mview.LoginView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @aythor: lilei
 * time: 2017/8/29  下午2:26
 * function:
 */

public class LoginFragment extends BaseMvpFragment<LoginView, LoginPresenter> implements LoginView, View.OnKeyListener {
    //修改密码
    public static final int TYPE_CHANGE_LOGIN_PW = 1000;
    //修改绑定手机
    public static final int TYPE_CHANGE_BIND_PHONE = 1002;

    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.iv_number_clear)
    ImageView mIvNumberClear;
    @BindView(R.id.et_password)
    EditText mEtPass;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindDimen(R.dimen.login_translate_height)
    int mTranslateHeight;

    @BindString(R.string.toast_login_mobile_pass_rule)
    String mPasswordErro;

    //密文
    boolean mIsDesensi = false;
    //键盘是否弹起
    boolean mIsMonitor = false;
    //谈起次数，避免重复动画
    int mTemp = 0;
    int mFirstHeightDiffrence;
    boolean mIsFirst = true;
    int mMoveHeight;

    LoginPresenter mLoginPresenter;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        StatusBarUtil.setWindowStatusBarColor(getActivity(), R.color.white, true);
        addBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.intentMainActivity(LoginFragment.this, mPresenter.mIntentType);
            }
        });
        setTitleBackground(R.color.white);
        setBackImage(R.drawable.login_icon_cancel);
        setMonitorKeyboard();
        if (getActivity().getIntent() != null) {
            mPresenter.mIntentType = getActivity().getIntent().getIntExtra(ConstantUtils.KEY_TYPE, 0);
        }
        checkListener(mEtMobile);
        checkListener(mEtPass);
        CommonUtils.setEditCallBack(mEtPass);
        mEtMobile.setOnKeyListener(this);
        mEtPass.setOnKeyListener(this);
        this.getView().setOnKeyListener(this);
    }


    /**
     * 监听edittext
     *
     * @param editext
     */
    public void checkListener(final EditText editext) {
        editext.addTextChangedListener(new TextWatcherAdapter() {
            public void afterTextChanged(Editable s) {
                String result = s.toString().trim();
                if (editext.equals(mEtMobile)) {
                    if (TextUtils.isEmpty(result) || TextUtils.isEmpty(mEtPass.getText().toString().trim())) {
                        mBtnLogin.setEnabled(false);
                    } else {
                        mBtnLogin.setEnabled(true);
                    }
                    mIvNumberClear.setVisibility(StringUtil.isNullOrEmpty(result) ? View.GONE : View.VISIBLE);

                } else if (editext.equals(mEtPass)) {
                    if (TextUtils.isEmpty(result) || TextUtils.isEmpty(mEtMobile.getText().toString().trim())) {
                        mBtnLogin.setEnabled(false);
                    } else {
                        mBtnLogin.setEnabled(true);
                    }
                }
            }
        });
    }


    @OnClick({R.id.ib_desensitization, R.id.btn_login, R.id.tv_regist, R.id.tv_forgetpass, R.id.iv_number_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_regist:
                startFragment(RegisterFragment.class, null);
                break;
            case R.id.ib_desensitization:
                mIsDesensi = CommonUtils.setIsDesensi(mEtPass, (ImageButton) view, mIsDesensi);
                break;
            case R.id.tv_forgetpass:
                startFragment(ForgetPasswordFragment.class, null);
                break;
            case R.id.iv_number_clear:
                mEtMobile.setText("");
                break;
            default:
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 登录
     */
    public void login() {
        String mMobile = mEtMobile.getText().toString().trim();
        String mPass = mEtPass.getText().toString().trim();
        if (ExtendUtil.isPhoneNumber(mMobile)) {
            if (CommonUtils.checkPass(mPass)) {
                mLoginPresenter.loadData(new LoginRequest(mMobile, mPass));
            } else {
                ToastUtil.showShortToast(mContext, getString(R.string.toast_password_erro_rule_login));
            }
        } else {
            ToastUtil.showShortToast(mContext, getString(R.string.toast_mobile_erro_rule));
        }
    }

    /**
     * 软键盘监听，和动画
     */
    public void setMonitorKeyboard() {
        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect mRect = new Rect();
                mParentView.getWindowVisibleDisplayFrame(mRect);
                int screenHeight = mParentView.getRootView().getHeight();
                int heightDifference = screenHeight - (mRect.bottom);
                int mBottom = 0;
                int mHeight = 0;
                if (mBtnLogin != null) {
                    mBottom = mBtnLogin.getBottom();
                    mHeight = mBtnLogin.getHeight();
                }
                int mMoveHeghtResult = 0;

                if (heightDifference > 200) {
                    mMoveHeight = heightDifference - (screenHeight - mBtnLogin.getBottom());
                }
                if (mIsFirst) {
                    mFirstHeightDiffrence = heightDifference;
                    mIsFirst = false;
                }
                AnimatorSet mAnimatorSet = new AnimatorSet();
                ObjectAnimator nTranslation;
                ObjectAnimator mAlpha;
                if (heightDifference > 200 && mTemp == 0) {
                    mMoveHeghtResult = screenHeight - ((mRect.bottom) / 2) - mBottom - (mHeight / 2) + ScreenUtil.getStatusHeight(getActivity());
                    nTranslation = ObjectAnimator.ofFloat(mLlRoot, "translationY", 0, -mMoveHeghtResult);
                    mAlpha = ObjectAnimator.ofFloat(mIvIcon, "alpha", 1, 0);
                    mAnimatorSet.play(nTranslation).with(mAlpha);
                    mTemp++;
                    mIsMonitor = true;
                } else if (heightDifference == mFirstHeightDiffrence && mIsMonitor) {
                    nTranslation = ObjectAnimator.ofFloat(mLlRoot, "translationY", -mMoveHeghtResult, 0);
                    mAlpha = ObjectAnimator.ofFloat(mIvIcon, "alpha", 0, 1);
                    mAnimatorSet.play(nTranslation).with(mAlpha);
                    mIsMonitor = false;
                    mTemp = 0;
                }
                mAnimatorSet.setDuration(200);
                mAnimatorSet.start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);

    }

    @Override
    public void setData(User mUser) {
        UserUtils.getInstance().saveUserObject(mUser);
        mPresenter.intentMainActivity(LoginFragment.this, mPresenter.mIntentType);
        EventBus.getDefault().post(new LoginSuccessEvent(mContext.getString(R.string.login)));
    }

    @Override
    public void onFailure(int code, String msg) {
        ToastUtil.showShortToast(mContext, msg);
    }

    @Override
    public LoginPresenter initPresenter() {
        mLoginPresenter = new LoginPresenter();
        return mLoginPresenter;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mIsMonitor) {
                KeyBoardUtil.closeKeybord(mEtMobile, mContext);
                return true;
            }
            mPresenter.intentMainActivity(LoginFragment.this, mPresenter.mIntentType);
        }
        return false;
    }

}
