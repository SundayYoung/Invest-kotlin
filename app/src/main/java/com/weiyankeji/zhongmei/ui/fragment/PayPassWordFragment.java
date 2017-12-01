package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付密码
 * Created by liuhaiyang on 2017/9/4.
 */

public class PayPassWordFragment extends BaseFragment {

    @BindView(R.id.tv_edit_password)
    TextView mTvEdit;
    @BindView(R.id.tv_forget_password)
    TextView mTvForget;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_pay_password;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.pay_password), true);
    }


    @OnClick({R.id.tv_edit_password, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_password:
                CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_CHANGE_PAY_PW);
                break;
            case R.id.tv_forget_password:
                CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
                break;
            default:
                break;
        }
    }

}
