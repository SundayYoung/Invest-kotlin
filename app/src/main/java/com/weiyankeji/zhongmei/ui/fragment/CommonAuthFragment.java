package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mpresenter.ChangePaymentPasswordPresenter;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CountDownBtnUtils;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zff on 2017/9/6.
 */

public class CommonAuthFragment extends BaseMvpFragment<ChangePaymentPasswordView, ChangePaymentPasswordPresenter> implements ChangePaymentPasswordView, CountDownTimerView {

    public static final int TYPE_FORGET_PAY_PW = 0;
    public static final int TYPE_SET_PAY_PW = 1;
    public static final int TYPE_CHANGE_PAY_PW = 2;
    public static final int TYPE_CHANGE_BIND_MOLIBE = 3;


    @BindView(R.id.bt_valida_code_change_payment_password)
    Button mBtRequestCode;
    @BindView(R.id.bt_valida_next_step_change_payment_password)
    Button mBtNextStep;
    @BindView(R.id.et_valida_code_change_payment_password)
    EditText mEtPhoneCode;
    @BindView(R.id.tv_valida_bind_number_change_payment_password)
    TextView mTvPhone;
    @BindDimen(R.dimen.text_small)
    int mChangeSize;
    @BindDimen(R.dimen.text_default)
    int mDefaultSize;

    private int mType = TYPE_FORGET_PAY_PW;
    private String mMobile;

    @Override
    public ChangePaymentPasswordPresenter initPresenter() {
        return new ChangePaymentPasswordPresenter();
    }


    @Override
    public int setContentLayout() {
        return R.layout.fragment_change_payment_password;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        if (bundle != null) {
            mType = bundle.getInt(ConstantUtils.KEY_TYPE);
        }

        if (mType == TYPE_FORGET_PAY_PW) {
            setTitle(getString(R.string.mine_password_forget), true);
        } else if (mType == TYPE_SET_PAY_PW) {
            setTitle(getString(R.string.set_pay_pw), true);
        } else if (mType == TYPE_CHANGE_PAY_PW) {
            setTitle(getString(R.string.change_pay_pw), true);
        } else if (mType == TYPE_CHANGE_BIND_MOLIBE) {
            setTitle(getString(R.string.change_bind_phone), true);
        }

        mEtPhoneCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                String text = editable.toString();

                if (CommonUtils.checkMessageCode(text)) {
                    mBtNextStep.setEnabled(true);
                } else {
                    mBtNextStep.setEnabled(false);
                }
            }
        });
        mMobile = mPresenter.getUserMobile();
        mTvPhone.setText(getString(R.string.binded_phone) + mMobile);
    }

    @OnClick({R.id.bt_valida_code_change_payment_password, R.id.bt_valida_next_step_change_payment_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_valida_code_change_payment_password:

                if (mType == TYPE_CHANGE_PAY_PW) {
                    mPresenter.requestVerificationCode(ConstantUtils.VERIFY_TYPE_CHANGE_TRADE);
                } else if (mType == TYPE_FORGET_PAY_PW || mType == TYPE_SET_PAY_PW) {
                    mPresenter.requestVerificationCode(ConstantUtils.VERIFY_TYPE_FORGET_TRADE);
                } else if (mType == TYPE_CHANGE_BIND_MOLIBE) {
                    mPresenter.requestVerificationCode(ConstantUtils.VERIFY_TYPE_LOGIN_CHANGE_PASSWORD);
                }

                CountDownBtnUtils.unAvailableState(mBtRequestCode);
                mPresenter.startCountDownTimer(this);
                break;
            case R.id.bt_valida_next_step_change_payment_password:
                String code = mEtPhoneCode.getText().toString();
                if (!CommonUtils.checkMessageCode(code, true)) {
                    return;
                }

                if (mType == TYPE_CHANGE_PAY_PW) {
                    mPresenter.sendValidaCode(code, ConstantUtils.VERIFY_TYPE_CHANGE_TRADE);
                } else if (mType == TYPE_CHANGE_BIND_MOLIBE) {
                    mPresenter.sendValidaCode(code, ConstantUtils.VERIFY_TYPE_LOGIN_CHANGE_PASSWORD);
                } else if (mType == TYPE_FORGET_PAY_PW || mType == TYPE_SET_PAY_PW) {
                    mPresenter.sendValidaCode(code, ConstantUtils.VERIFY_TYPE_FORGET_TRADE);
                }
                break;
            default:
        }
    }


    @Override
    public void countdownFinish() {
        CountDownBtnUtils.availableState(mBtRequestCode);
    }

    @Override
    public void schedule(long time) {
        if (isAdded()) {
            mBtRequestCode.setText(time + getString(R.string.after_re_obtain));
        }
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
    public void validaRequest() {

        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, mType);
        if (mType == TYPE_CHANGE_PAY_PW) {
            startFragment(ChangePaymentPasswordSecondFragment.class, bundle);
        } else if (mType == TYPE_SET_PAY_PW || mType == TYPE_FORGET_PAY_PW) {
            bundle.putInt(ConstantUtils.KEY_TYPE, mType);
            startFragment(ResetPayPwFragment.class, bundle);
        } else if (mType == TYPE_CHANGE_BIND_MOLIBE) {
            startFragment(ChangeBindPhoneFragment.class);
        }
        onFinishFragment();
    }

    @Override
    public void requestCodeFail() {
        ToastUtil.showShortToast(ZMApplication.getZMApplication(), R.string.request_error);
    }

    @Override
    public void requestCodeSuccess() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtils.INTENT_REQUEST && resultCode == ConstantUtils.INTENT_RESPONSE) {
            getActivity().setResult(ConstantUtils.INTENT_REQUEST);
            getActivity().finish();
        }
    }
}
