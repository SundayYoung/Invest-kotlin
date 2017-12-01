package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mpresenter.ChangePaymentPasswordThirdPresenter;
import com.weiyankeji.zhongmei.ui.mview.ChangePaymentPasswordThirdView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zff on 2017/9/5.
 */

public class ResetPayPwFragment extends BaseMvpFragment<ChangePaymentPasswordThirdView, ChangePaymentPasswordThirdPresenter>
        implements ChangePaymentPasswordThirdView {

    @BindView(R.id.tv_change_payment_new_payment_pw_third_id)
    EditText mEtIdCard;
    @BindView(R.id.tv_change_payment_new_payment_pw_third_new)
    EditText mEtNewPw;
    @BindView(R.id.tv_change_payment_new_payment_pw_third_comfirm_new)
    EditText mEtNewPwComfirm;
    @BindView(R.id.change_payment_new_payment_pw_third_submit)
    Button mBtCommit;

    private int mType;

    @Override
    public ChangePaymentPasswordThirdPresenter initPresenter() {
        return new ChangePaymentPasswordThirdPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_change_payment_password_third;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        mType = bundle.getInt(ConstantUtils.KEY_TYPE);
        if (mType == CommonAuthFragment.TYPE_FORGET_PAY_PW) {
            setTitle(getString(R.string.mine_password_forget), true);
        } else if (mType == CommonAuthFragment.TYPE_SET_PAY_PW) {
            setTitle(getString(R.string.set_pay_pw), true);
        }

        mEtIdCard.addTextChangedListener(new TextWatcherAdapter() {
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
        String idCard = mEtIdCard.getText().toString();
        String pw1 = mEtNewPw.getText().toString();
        String pw2 = mEtNewPwComfirm.getText().toString();

        if (pw1.length() != ConstantUtils.MAX_PAY_PW_LENGTH || pw2.length() != ConstantUtils.MAX_PAY_PW_LENGTH || !CommonUtils.checkIDCard(idCard)) {
            mBtCommit.setEnabled(false);
        } else {
            mBtCommit.setEnabled(true);
        }

    }

    @OnClick(R.id.change_payment_new_payment_pw_third_submit)
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
        ToastUtil.showShortToast(ZMApplication.getZMApplication(), getString(R.string.pw_reset_successful));
        if (mType == CommonAuthFragment.TYPE_SET_PAY_PW) {
            User user = UserUtils.getInstance().getUserObject();
            user.setValidate_type(ConstantUtils.ACCOUNT_ALL_VALIDA);
            UserUtils.getInstance().saveUserObject(user);
        }
        onFinishFragment();
    }

    private void commit() {
        String idCard = mEtIdCard.getText().toString();
        String newPassword = mEtNewPw.getText().toString();
        String newComfirmword = mEtNewPwComfirm.getText().toString();

        if (!newPassword.equals(newComfirmword)) {
            DialogUtil.createHintDialogNoAction(mContext, getString(R.string.comfirm_password_error), getString(R.string.common_ok));
            return;
        }

        boolean isInputLegal = mPresenter.validateInput(idCard, newPassword, newComfirmword);
        if (isInputLegal) {
            mPresenter.submitPaymentPasswordInfo(idCard, newPassword);
        }
    }

    @Override
    public void showCommitfail(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            if (code == ErrorMsgUtils.ID_CARD_NO_MATCH_FOR_AUTH) {
                DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
                return;
            }
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
        }
    }

}
