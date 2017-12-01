package com.weiyankeji.zhongmei.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mpresenter.ChangeBindPhonePresenter;
import com.weiyankeji.zhongmei.ui.mview.AccountValidaView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.CountDownBtnUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改绑定手机
 * Created by caiwancheng on 2017/9/4.
 */

public class ChangeBindPhoneFragment extends BaseMvpFragment<AccountValidaView, ChangeBindPhonePresenter> implements AccountValidaView, CountDownTimerView {

    @BindView(R.id.et_change_bind_phone)
    EditText mEtPhone;
    @BindView(R.id.et_change_bind_phone_code)
    EditText mEtCode;
    @BindView(R.id.bt_change_bind_phone_code)
    Button mBtCode;
    @BindView(R.id.tv_change_bind_phone_submit)
    Button mBtSubmit;

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public void schedule(long time) {
        if (isAdded()) {
            mBtCode.setText(time + getString(R.string.after_re_obtain));
        }
    }

    @Override
    public void countdownFinish() {
        CountDownBtnUtils.availableState(mBtCode);
        if (!CommonUtils.checkPhoneNumber(mEtPhone.getText().toString())){
           mBtCode.setEnabled(false);
        }
    }

    @Override
    public ChangeBindPhonePresenter initPresenter() {
        return new ChangeBindPhonePresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_change_bind_phone;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.change_bind_phone), true);
        mEtPhone.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    if (CommonUtils.checkPhoneNumber(text)) {
                        if (mPresenter.isCountDownOver()) {
                            mBtCode.setEnabled(true);
                        }
                    } else {
                        mBtCode.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void requestCodeFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
        }
    }

    @Override
    public void validaRequest() {
        ToastUtil.showShortToast(mContext.getApplicationContext(), getString(R.string.change_bind_phone_successful));
        CommonUtils.intentLoginFragment(this, LoginFragment.TYPE_CHANGE_BIND_PHONE);
        onFinishFragment();
    }

    @Override
    public void validaRequestFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            if (code == ErrorMsgUtils.MESSAGE_STATUS_INVALID) {
                DialogUtil.createHintDialog(mContext, msg, getString(R.string.common_again), null);
            } else {
                DialogUtil.createDialog(mContext, null, msg, getString(R.string.common_again), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_CHANGE_BIND_MOLIBE);
                    }
                }, null).show();
            }
        }
    }

    @Override
    public void requestCodeSuccessful() {
    }

    @OnClick({R.id.bt_change_bind_phone_code, R.id.tv_change_bind_phone_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change_bind_phone_code:
                CountDownBtnUtils.unAvailableState(mBtCode);
                mPresenter.startCountDownTimer(this);
                mPresenter.loadMsgValidaCode(mEtPhone.getText().toString());
                break;
            case R.id.tv_change_bind_phone_submit:
                String phone = mEtPhone.getText().toString();
                String code = mEtCode.getText().toString();
                if (!mPresenter.checkCommit(phone, code)) {
                    return;
                }
                mPresenter.submit(this, phone, code);
                break;
            default:
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
    }
}
