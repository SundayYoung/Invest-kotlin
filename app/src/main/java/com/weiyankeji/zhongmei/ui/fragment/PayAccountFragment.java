package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.activity.MainActivity;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/9/4.
 */

public class PayAccountFragment extends BaseFragment {
    @Override
    public int setContentLayout() {
        return R.layout.fragment_pay_account;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.pay_account), true);
    }

    @OnClick(R.id.bt_pay_account)
    public void onClick() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(ConstantUtils.KEY_TYPE, MainActivity.Companion.getTYPE_PAY_ACCOUNT_FINISH());
        getActivity().startActivity(intent);
        getActivity().onBackPressed();
    }
}
