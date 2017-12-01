package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.StatusBarUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SendCodeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.payment.ForgetPasswordRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.ForgetPasswordPresenter;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.ui.mview.ForgetPasswordView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CountDownBtnUtils;



import butterknife.BindView;
import butterknife.OnClick;


/**
 * 忘记密码
 * Created by zff on 2017/9/4.
 */

public class ForgetPasswordFragment extends BaseMvpFragment<ForgetPasswordView, ForgetPasswordPresenter> implements ForgetPasswordView, CountDownTimerView {
    @BindView(R.id.et_forget_password_phone_number)
    EditText mEtPhoneNumber;
    @BindView(R.id.btn_forget_password_request_verification_code)
    Button mBtnRequestVerificationCode;
    @BindView(R.id.et_forget_password_verification_code)
    EditText mEtVerificationCode;
    @BindView(R.id.et_forget_password_password)
    EditText mEtPassword;
    @BindView(R.id.btn_forget_password_commit)
    Button mBtnSubmit;


    //密文
    boolean mIsDesensi = false;

    String mPhoneNumber;
    String mVerificationCode;
    String mPassword;

    @Override
    public ForgetPasswordPresenter initPresenter() {
        return new ForgetPasswordPresenter();
    }

    @Override
    public void requestCodeFail(String msg) {
        ToastUtil.show(getActivity(), R.string.request_error, Toast.LENGTH_SHORT);
    }

    @Override
    public void requestCodeSuccess() {

    }

    @Override
    public void countdownFinish() {
        CountDownBtnUtils.availableState(mBtnRequestVerificationCode);
        if (!CommonUtils.checkPhoneNumber(mEtPhoneNumber.getText().toString())){
            mBtnRequestVerificationCode.setEnabled(false);
        }
    }

    @Override
    public void showLoading() {
        setLoading(true);

    }

    @Override
    public void schedule(long time) {
        if (isAdded()) {
            mBtnRequestVerificationCode.setText(time + getString(R.string.after_re_obtain));
        }
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_forget_password;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        StatusBarUtil.setWindowStatusBarColor(getActivity(), R.color.white, true);
        addBackListener();
        setTitleBackground(R.color.white);
        setBackImage(R.drawable.login_icon_arrow_left);

        addEtListener(mEtPhoneNumber);
        addEtListener(mEtVerificationCode);
        addEtListener(mEtPassword);

        CommonUtils.setEditCallBack(mEtPassword);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
    }

    @OnClick({R.id.btn_forget_password_request_verification_code, R.id.btn_forget_password_commit, R.id.ib_desensitization_forget_password_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_password_request_verification_code:
                mPhoneNumber = mEtPhoneNumber.getText().toString().trim();
                mPresenter.requestVerificationCode(new SendCodeRequest(mPhoneNumber, ConstantUtils.VERIFY_TYPE_FORGO_TPASSWORD));
                CountDownBtnUtils.unAvailableState(mBtnRequestVerificationCode);
                mPresenter.startCountDownTimer(this);
                break;
            case R.id.btn_forget_password_commit:
                commit();
                break;
            case R.id.ib_desensitization_forget_password_password:
                mIsDesensi = CommonUtils.setIsDesensi(mEtPassword, (ImageButton) view, mIsDesensi);
                break;
            default:
        }
    }

    private void commit() {
        mPhoneNumber = mEtPhoneNumber.getText().toString().trim();
        mVerificationCode = mEtVerificationCode.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        mPresenter.submitForgetPasswordInfo(new ForgetPasswordRequest(mPhoneNumber, mVerificationCode, mPassword));

    }

    @Override
    public void showCommitSuccess() {
        ToastUtil.showShortToast(getActivity().getApplicationContext(), R.string.pw_change_successful);
        onFinishFragment();
    }

    @Override
    public void showCommitFail(String msg) {
        ToastUtil.showShortToast(getActivity().getApplicationContext(), msg);
    }


    private void addEtListener(final EditText editText) {


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString().trim();
                if (editText.equals(mEtPhoneNumber)) {

                    if (ExtendUtil.isPhoneNumber(editable.toString().trim())) {
                        if (mPresenter.isCountDownOver()) {
                            mBtnRequestVerificationCode.setEnabled(true);
                        }
                    } else {
                        mBtnRequestVerificationCode.setEnabled(false);
                    }

                    if (ExtendUtil.isPhoneNumber(text) && CommonUtils.checkMessageCode(mEtVerificationCode.getText().toString().trim())
                            && !TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
                        mBtnSubmit.setEnabled(true);
                    } else {
                        mBtnSubmit.setEnabled(false);
                    }

                } else if (editText.equals(mEtVerificationCode)) {

                    if (ExtendUtil.isPhoneNumber(mEtPhoneNumber.getText().toString().trim()) && CommonUtils.checkMessageCode(text)
                            && !TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
                        mBtnSubmit.setEnabled(true);
                    } else {
                        mBtnSubmit.setEnabled(false);
                    }

                } else if (editText.equals(mEtPassword)) {

                    if (ExtendUtil.isPhoneNumber(mEtPhoneNumber.getText().toString().trim()) &&
                            CommonUtils.checkMessageCode(mEtVerificationCode.getText().toString().trim()) && !TextUtils.isEmpty(text)) {
                        mBtnSubmit.setEnabled(true);
                    } else {
                        mBtnSubmit.setEnabled(false);
                    }
                }
            }
        });
    }


}
