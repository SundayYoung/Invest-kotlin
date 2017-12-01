package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mpresenter.ChangeLoginPwPresenter;
import com.weiyankeji.zhongmei.ui.mview.ChangeLoginPwView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登陆密码
 * Created by caiwancheng on 2017/9/1.
 */

public class ChangeLoginPwFragment extends BaseMvpFragment<ChangeLoginPwView, ChangeLoginPwPresenter> implements ChangeLoginPwView {

    @BindView(R.id.et_change_pw_old_pw)
    EditText mEtOldPw;
    @BindView(R.id.et_change_pw_new_pw)
    EditText mEtNewPw;
    @BindView(R.id.bt_change_pw_submit)
    Button mBtChange;

    @Override
    public ChangeLoginPwPresenter initPresenter() {
        return new ChangeLoginPwPresenter(this);
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_change_login_pw;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.change_login_pw), true);
        CommonUtils.setEditCallBack(mEtNewPw);
        CommonUtils.setEditCallBack(mEtOldPw);
        mEtNewPw.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(mEtNewPw.getText().toString()) && !TextUtils.isEmpty(mEtNewPw.getText().toString())) {
                    mBtChange.setEnabled(true);
                } else {
                    mBtChange.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.bt_change_pw_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change_pw_submit:
                String oldPw = mEtOldPw.getText().toString();
                String newPw = mEtNewPw.getText().toString();
                if (!CommonUtils.checkPass(oldPw) || !CommonUtils.checkPass(newPw)) {
                    ToastUtil.showShortToast(mContext, getString(R.string.pw_error));
                    return;
                }
                mPresenter.changePw(oldPw, newPw);
                break;
            default:
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
    public void changeLoginPwResponse() {
        ToastUtil.showShortToast(mContext, getString(R.string.pw_change_succe));
        UserUtils.getInstance().deleteUser();
        CommonUtils.intentLoginFragment(this, LoginFragment.TYPE_CHANGE_LOGIN_PW);
        getActivity().finish();
    }

    @Override
    public void changeFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
        }
    }

}
