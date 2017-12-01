package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.payment.ChangePaymentPwRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.ChangePaymentPasswordSecondPresenter;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordSecondView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zff on 2017/9/5.
 */

public class ChangePaymentPasswordSecondFragment extends BaseMvpFragment<ChangePaymentPasswordSecondView, ChangePaymentPasswordSecondPresenter>
        implements ChangePaymentPasswordSecondView {

    @BindView(R.id.tv_change_payment_new_payment_pw_second_old)
    EditText mEtOldPw;
    @BindView(R.id.tv_change_payment_new_payment_pw_second_new)
    EditText mEtNewPw;
    @BindView(R.id.tv_change_payment_new_payment_pw_second_new_comfirm)
    EditText mEtNewPwComfirm;
    @BindView(R.id.change_payment_new_payment_pw_second_submit)
    Button mBtSubmit;


    @Override
    public ChangePaymentPasswordSecondPresenter initPresenter() {
        return new ChangePaymentPasswordSecondPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_change_payment_password_second;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.change_pay_pw), true);
        mEtOldPw.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkCommitEnable();
            }
        });

        mEtNewPw.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkCommitEnable();
            }
        });

        mEtNewPwComfirm.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkCommitEnable();
            }
        });
    }

    public void checkCommitEnable() {
        String oldPw = mEtOldPw.getText().toString();
        String pw1 = mEtNewPw.getText().toString();
        String pw2 = mEtNewPwComfirm.getText().toString();

        if (pw1.length() != ConstantUtils.MAX_PAY_PW_LENGTH || pw2.length() != ConstantUtils.MAX_PAY_PW_LENGTH || oldPw.length() != ConstantUtils.MAX_PAY_PW_LENGTH) {
            mBtSubmit.setEnabled(false);
        } else {
            mBtSubmit.setEnabled(true);
        }

    }

    @OnClick(R.id.change_payment_new_payment_pw_second_submit)
    public void onClick(View view) {
        commit();
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
    public void showCommitSuccess() {
        ToastUtil.showShortToast(getActivity().getApplicationContext(), R.string.pw_change_successful);
        onFinishFragment();
    }

    @Override
    public void showCommitFail(String msg, int code) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
//            ToastUtil.showShortToast(mContext, msg);
        }
    }

    private void commit() {
        String oldPassword = mEtOldPw.getText().toString().trim();
        String newPassword = mEtNewPw.getText().toString().trim();
        String newComfirmword = mEtNewPwComfirm.getText().toString().trim();
        if (!newPassword.equals(newComfirmword)) {
            DialogUtil.createHintDialogNoAction(mContext, getString(R.string.comfirm_password_error), getString(R.string.common_ok));
            return;
        }

        boolean isInputLegal = mPresenter.validateInput(getActivity(), oldPassword, newPassword, newComfirmword);
        if (isInputLegal) {
            mPresenter.submitPaymentPasswordInfo(new ChangePaymentPwRequest(oldPassword, newPassword));
        }
    }


}
