package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mpresenter.AuthCodeCheckPresenter;
import com.weiyankeji.zhongmei.ui.mview.AuthCodeCheckView;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/9/2.
 */

public class AuthCodeCheckFragment extends BaseMvpFragment<AuthCodeCheckView, AuthCodeCheckPresenter> implements AuthCodeCheckView {

    @BindView(R.id.et_auth_code_check)
    EditText mEtCode;

    @BindView(R.id.bt_auth_code_check)
    Button mBtCommit;

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public void respone() {
        ToastUtil.showShortToast(mContext, getString(R.string.auth_successful));
        onFinishFragment();
    }

    @Override
    public void responeFail(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            if (code == ErrorMsgUtils.PARAMS_SKU_CODE_ERROR || code == ErrorMsgUtils.PARAMS_SKU_CODE_ISBAND) {
                DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
            } else {
                DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_again));
            }
        }
    }

    @Override
    public AuthCodeCheckPresenter initPresenter() {
        return new AuthCodeCheckPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_auth_code_check;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.auth_code_check), true);
        mEtCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.toString().length() > 0) {
                    mBtCommit.setEnabled(true);
                } else {
                    mBtCommit.setEnabled(true);
                }
            }
        });
    }

    @OnClick(R.id.bt_auth_code_check)
    public void onClick() {
        String code = mEtCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShortToast(mContext, getString(R.string.auth_code_is_null));
            return;
        }
        mPresenter.request(code);
    }
}
