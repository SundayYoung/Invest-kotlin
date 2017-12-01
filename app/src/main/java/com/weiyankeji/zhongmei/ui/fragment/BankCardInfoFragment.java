package com.weiyankeji.zhongmei.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiyankeji.library.customview.passwordview.BottomPassWordDialog;
import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mpresenter.BankCardCommonPresenter;
import com.weiyankeji.zhongmei.ui.mview.BankCardInfoView;
import com.weiyankeji.zhongmei.ui.mview.CountDownTimerView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CountDownBtnUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/8/28.
 */

public class BankCardInfoFragment extends BaseMvpFragment<BankCardInfoView, BankCardCommonPresenter> implements BankCardInfoView, CountDownTimerView, BottomPassWordDialog.PassWordDialogListener {
    public static final int TYPE_CHANGE = 0;
    public static final int TYPE_BIND = 1;

    @BindView(R.id.tv_bank_info_name)
    TextView mTvName;
    @BindView(R.id.et_bank_card_info_no)
    EditText mEtCardNo;
    @BindView(R.id.et_bank_card_info_phone)
    EditText mEtPhone;
    @BindView(R.id.et_bank_card_message_code)
    EditText mEtMsgCode;
    @BindView(R.id.bt_bank_card_info_account)
    TextView mTtAccount;
    @BindView(R.id.bt_bank_card_info_submit)
    Button mBtSubmit;
    @BindView(R.id.bt_bank_card_info_code)
    Button mBtCode;

    private TextWatcherAdapter mTextWatcherAdapter;

    private int mType = TYPE_CHANGE;

    private BottomPassWordDialog mDialog;

    public boolean mIsFirstInputPw = true;

    private BankInfoResponse mBankInfo;

    private int mOldBankId;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mPresenter.validaBackNo((String) msg.obj);
            }
        }
    };


    @Override
    public BankCardCommonPresenter initPresenter() {
        return new BankCardCommonPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_bank_card_info;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        mType = bundle.getInt(ConstantUtils.KEY_TYPE, TYPE_CHANGE);
        if (mType == TYPE_CHANGE) {
            setTitle(getString(R.string.change_bank_card), true);
            mOldBankId = bundle.getInt(ConstantUtils.KEY_ID);
        } else {
            setTitle(getString(R.string.bind_bank_card), true);
            mBtSubmit.setText(getString(R.string.next_step));
        }
        mTvName.setText(getString(R.string.cardholder) + UserUtils.getInstance().getUserObject().getRealname());

        mTextWatcherAdapter = new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String number = s.toString().replace(" ", "");
                if (!TextUtils.isEmpty(number) && (number.length() > 15 && number.length() < 20)) {
                    if (mHandler.hasMessages(0)) {
                        mHandler.removeMessages(0);
                    }
                    Message message = new Message();
                    message.what = 0;
                    message.obj = number;
                    mHandler.sendMessageDelayed(message, 500);
                }
                checkSubmit();

                String str = s.toString().trim().replace(" ", "");
                String result = "";
                if (str.length() >= 4) {
                    mEtCardNo.removeTextChangedListener(mTextWatcherAdapter);
                    for (int i = 0; i < str.length(); i++) {
                        result += str.charAt(i);
                        if ((i + 1) % 4 == 0) {
                            result += " ";
                        }
                    }
                    if (result.endsWith(" ")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    mEtCardNo.setText(result);
                    mEtCardNo.addTextChangedListener(mTextWatcherAdapter);
                    mEtCardNo.setSelection(mEtCardNo.getText().toString().length());
                }
            }
        };


        mEtCardNo.addTextChangedListener(mTextWatcherAdapter);

        mEtPhone.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkSubmit();
                if (CommonUtils.checkPhoneNumber(mEtPhone.getText().toString())) {
                    if (mPresenter.isCountDownOver()) {
                        mBtCode.setEnabled(true);
                    }
                } else {
                    mBtCode.setEnabled(false);
                }
            }
        });


        mEtMsgCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkSubmit();
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

    public void checkSubmit() {
        String phone = mEtPhone.getText().toString();
        String cradNo = mEtCardNo.getText().toString().trim();
        String msgCode = mEtMsgCode.getText().toString();
        if (mPresenter.checkCommitButtonState(mBankInfo, phone, cradNo, msgCode)) {
            mBtSubmit.setEnabled(true);
        } else {
            mBtSubmit.setEnabled(false);
        }
    }

    @Override
    public void request(BankInfoResponse response) {
        if (response == null) {
            mTtAccount.setTextColor(ContextCompat.getColor(mContext, R.color.gray_c2));
            mTtAccount.setText(mContext.getString(R.string.please_select_account));
            return;
        }

        mBankInfo = response;
        mTtAccount.setTextColor(ContextCompat.getColor(mContext, R.color.black_3e));
        mTtAccount.setText(response.bank_name);
    }

    @Override
    public void submitSuccessful() {
        if (mType == TYPE_CHANGE) {
            ToastUtil.showShortToast(mContext.getApplicationContext(), getString(R.string.bank_card_change_successful));
            getActivity().setResult(1);
            getActivity().finish();
        } else {
            ToastUtil.showShortToast(mContext.getApplicationContext(), getString(R.string.bank_bind_successful));
            User user = UserUtils.getInstance().getUserObject();
            user.setValidate_type(ConstantUtils.ACCOUNT_AUTH_AND_CARD);
            UserUtils.getInstance().saveUserObject(user);
            setPayPw();
        }
    }

    public void setPayPw() {
        mFristPw = null;
        mIsFirstInputPw = true;
        mDialog = BottomPassWordDialog.with(mContext);
        mDialog.setTitleName(getString(R.string.pay_pw_setting));
        mDialog.setListener(this);
        mDialog.show();
    }

    @Override
    public void submitFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
        }
    }

    @Override
    public void setPayPwSuccessful() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog.cancel();
            User user = UserUtils.getInstance().getUserObject();
            user.setValidate_type(ConstantUtils.ACCOUNT_ALL_VALIDA);
            UserUtils.getInstance().saveUserObject(user);
            startFragment(PayAccountFragment.class);
            getActivity().onBackPressed();
        }
    }

    @Override
    public void setPayPwFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(mContext, msg);
        }
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
        if (!CommonUtils.checkPhoneNumber(mEtPhone.getText().toString())) {
            mBtCode.setEnabled(false);
        }
    }

    @OnClick({R.id.bt_bank_card_info_account, R.id.bt_bank_card_info_submit, R.id.bt_bank_card_info_code})
    public void onClick(View view) {
        String phone = mEtPhone.getText().toString();
        String cradNo = mEtCardNo.getText().toString().trim();
        String msgCode = mEtMsgCode.getText().toString();
        switch (view.getId()) {
            case R.id.bt_bank_card_info_code:
                if (!mPresenter.checkRequestCode(mContext, mBankInfo, phone, cradNo)) {
                    return;
                }

                CountDownBtnUtils.unAvailableState(mBtCode);
                mPresenter.startCountDownTimer(this);

                mPresenter.loadMsgValidaCode(phone, mOldBankId, cradNo, mBankInfo.bank_code);

                break;
            case R.id.bt_bank_card_info_submit:
                if (!mPresenter.checkCommit(mContext, mBankInfo, phone, cradNo, msgCode)) {
                    return;
                }
                if (mType == TYPE_BIND) {
                    mPresenter.submitBankCardInfo(mBankInfo.bank_code, cradNo, phone, msgCode, 0);
                } else {
                    mPresenter.submitBankCardInfo(mBankInfo.bank_code, cradNo, phone, msgCode, mOldBankId);
                }
                break;
            case R.id.bt_bank_card_info_account:
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.KEY_TITLE, mTtAccount.getText().toString());
                Intent intent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), BankListFragment.class, bundle);
                startActivityForResult(intent, 1);
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == 1) {
            BankInfoResponse response = (BankInfoResponse) data.getSerializableExtra(ConstantUtils.KEY_DATA);
            if (response != null) {
                mBankInfo = response;
                mTtAccount.setTextColor(ContextCompat.getColor(mContext, R.color.gray_8a));
                mTtAccount.setText(response.bank_name);
                checkSubmit();
            }
        }
    }

    private String mFristPw;


    @Override
    public void onInputComplete(String number) {
        if (number.length() == 6) {
            if (mIsFirstInputPw) {
                if (mDialog != null && mDialog.isShowing()) {
                    mFristPw = number;
                    mDialog.dismiss();
                    mDialog.cancel();
                    mDialog = BottomPassWordDialog.with(mContext);
                    mDialog.setTitleName(getString(R.string.please_input_pay_pw_again));
                    mDialog.setListener(this);
                    mDialog.show();
                    mIsFirstInputPw = false;
                }
            } else {
                if (mFristPw.equals(number)) {
                    mPresenter.setPayPw(number);
                } else {
                    DialogUtil.createHintDialog(mContext, getString(R.string.comfirm_password_error), getString(R.string.common_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDialog.dismiss();
                            mDialog.cancel();
                            setPayPw();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onLoadResult() {

    }

    @Override
    public void requestCodeSuccessful() {

    }

    @Override
    public void requestCodeFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
        }
    }


    @Override
    public void validaPhoneCode() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
        if (mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }
    }
}
