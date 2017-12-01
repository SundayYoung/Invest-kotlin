package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mmodel.mine.AuthNameResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.AuthNamePresenter;
import com.weiyankeji.zhongmei.ui.mview.AuthNameView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/9/2.
 */

public class AuthNameFragment extends BaseMvpFragment<AuthNameView, AuthNamePresenter> implements AuthNameView {

    @BindView(R.id.et_auth_name_real)
    EditText mEtName;
    @BindView(R.id.et_auth_name_id_card)
    EditText mEtIDCard;
    @BindView(R.id.tv_auth_name_phone)
    TextView mTvBindPhone;
    @BindView(R.id.bt_auth_name_next_step)
    Button mBtNext;

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
    }

    @Override
    public void respone(AuthNameResponse respone) {
        User user = UserUtils.getInstance().getUserObject();
        user.setValidate_type(ConstantUtils.ACCOUNT_NAME_AUTH);
        user.setRealname(respone.realname);
        UserUtils.getInstance().saveUserObject(user);

        ToastUtil.showShortToast(mContext, getString(R.string.real_auth_successful));
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, BankCardInfoFragment.TYPE_BIND);
        startFragment(BankCardInfoFragment.class, bundle);
        getActivity().onBackPressed();
    }

    @Override
    public void requestFailure(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
            DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
        }
    }

    @Override
    public AuthNamePresenter initPresenter() {
        return new AuthNamePresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_auth_name;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.name_auth), true);
        mTvBindPhone.setText(getString(R.string.binded_phone) + UserUtils.getInstance().getUserObject().getMobile());
        mEtIDCard.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkAuthButtonState();
            }
        });

        mEtName.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                checkAuthButtonState();
            }
        });
    }

    public void checkAuthButtonState() {
        if (mPresenter.checkAuthButtonState(mEtName.getText().toString(), mEtIDCard.getText().toString())) {
            mBtNext.setEnabled(true);
        } else {
            mBtNext.setEnabled(false);
        }
    }

    @OnClick(R.id.bt_auth_name_next_step)
    public void onClick() {
        String name = mEtName.getText().toString();
        String idCard = mEtIDCard.getText().toString();

        if (!mPresenter.checkAuthNameCommit(mContext, name, idCard)) {
            return;
        }

        mPresenter.request(name, idCard);
    }


}
