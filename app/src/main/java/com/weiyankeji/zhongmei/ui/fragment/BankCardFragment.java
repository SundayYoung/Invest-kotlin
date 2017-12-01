package com.weiyankeji.zhongmei.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.ScreenUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.BankCardPresenter;
import com.weiyankeji.zhongmei.ui.mview.BankCardView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/8/28.
 */

public class BankCardFragment extends BaseMvpFragment<BankCardView, BankCardPresenter> implements BankCardView {

    @BindView(R.id.iv_bank_card_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_bank_card_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_bank_card_title)
    TextView mTvTitle;
    @BindView(R.id.tv_bank_card_num)
    TextView mTvNumber;

    private BankInfoResponse mBankInfo;

    @Override
    public BankCardPresenter initPresenter() {
        return new BankCardPresenter();
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_bank_card;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        setTitle(getString(R.string.mine_bank_card), true);
        mPresenter.loadData(this);
        ViewGroup.LayoutParams params = mIvBg.getLayoutParams();
        params.width = (int) (ScreenUtil.getScreenWidth(getActivity()) - getResources().getDimension(R.dimen.margin_big) * 2);
        params.height = (int) (params.width * 0.363637);
        mIvBg.setLayoutParams(params);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mPresenter.loadData(BankCardFragment.this);
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

    @Override
    public void request(BankInfoResponse response) {
        mBankInfo = response;
        if (mBankInfo == null) {
            return;
        }
        mTvTitle.setText(response.bank_name);
        ImageLoaderUtil.loadArcImageFromUrl(getActivity(), response.logo_back_small, mIvBg, 3);
        ImageLoaderUtil.loadImageFromUrl(getActivity(), response.logo_small, mIvIcon);
        String number = response.bank_card_no;
        if (!TextUtils.isEmpty(number) && number.length() >= 4) {
            number = number.substring(number.length() - 4, number.length());
            mTvNumber.setText("****  ****  ****  " + number);
        }
    }

    @Override
    public void changeRequest() {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, BankCardInfoFragment.TYPE_CHANGE);
        bundle.putInt(ConstantUtils.KEY_ID, mBankInfo.bank_card_id);
        startActivityForResult(FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), BankCardInfoFragment.class, bundle), 1);
    }

    @Override
    public void changeFailure(String msg) {
        DialogUtil.createHintDialogNoAction(mContext, msg, getString(R.string.common_ok));
    }

    @OnClick(R.id.bt_bank_change)
    public void onClick(View view) {
        if (mBankInfo == null) {
            ToastUtil.showShortToast(ZMApplication.getZMApplication(), getString(R.string.get_bank_info_fail_can_not_change_bank_card));
            return;
        }

        DialogUtil.createDialog(mContext, getString(R.string.hint), getString(R.string.sure_change_bank_card),
                getString(R.string.common_ok), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.checkBankIsCanChange();
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            mPresenter.loadData(this);
        }
    }
}
