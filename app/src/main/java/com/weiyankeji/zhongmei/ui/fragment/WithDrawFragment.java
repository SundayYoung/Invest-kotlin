package com.weiyankeji.zhongmei.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiyankeji.library.customview.passwordview.BottomPassWordDialog;
import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.event.LoginSuccessEvent;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.payment.AccountChargeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.payment.WithdrawCashRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.WithDrawPresenter;
import com.weiyankeji.zhongmei.ui.mview.WithDrawView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.StatisticalUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1：提现 和 2：充值
 * Created by liuhaiyang on 2017/9/4.
 */

public class WithDrawFragment extends BaseMvpFragment<WithDrawView, WithDrawPresenter> implements WithDrawView, BottomPassWordDialog.PassWordDialogListener {

    //bank info
    @BindView(R.id.iv_bank_logo)
    ImageView mIvBankLogo;
    @BindView(R.id.tv_bank_name)
    TextView mTvBankName;

    @BindView(R.id.tv_money_tip)
    TextView mTvMoneyTip;

    @BindView(R.id.et_withdraw_money)
    EditText mEtMoney;
    @BindView(R.id.line)
    View mLineView;
    @BindView(R.id.tv_withdraw_max)
    TextView mTvWithDrawMax;
    @BindView(R.id.tv_info_tip)
    TextView mTvInfoTip;
    @BindView(R.id.tv_withdraw_all)
    TextView mTvWithDrawAll;
    @BindView(R.id.bt_invest)
    Button mBtInvest;
    private BottomPassWordDialog mPassWordView;
    private int mType;
    private long mAvailable;
    private int mBankCardId;
    private double mCurrentMonkey;
    private long mOnceRechargeLimit;
    private long mOnceWithDrawLimit;


    @Override
    public int setContentLayout() {
        return R.layout.fragment_withdraw;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        EventBus.getDefault().register(this);
        Bundle bde = getActivity().getIntent().getBundleExtra(FragmentContainerActivity.STAG_FRAGMENT_ARGUMENTS);
        if (bde != null) {
            mType = bde.getInt(ConstantUtils.KEY_TYPE);
        }

        if (mType == ConstantUtils.TYPE_RECHARGE) {
            setTitle(getString(R.string.mine_recharge), true);
            mTvMoneyTip.setText(getString(R.string.mine_recharge_money));
            mTvWithDrawAll.setVisibility(View.GONE);
            mEtMoney.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int count, int after) {
                    CommonUtils.twoDecimal(s.toString(), mEtMoney);
                    setFontBold(s);
                    if (isRechargeInputLegal(s) && mOnceRechargeLimit != 0) {

                        String input = s.toString().trim();
                        mCurrentMonkey = Double.parseDouble(input);
                        if (isRechargeMoneyLegal(mCurrentMonkey)) {
                            showBtnEnableState(true);
                            String tip = getString(R.string.mine_account_available, MoneyFormatUtils.lFormatS(mAvailable));
                            setNormalText(tip);
                        } else {
                            showBtnEnableState(false);
                            String tip = getString(R.string.mine_recharge_overmax, MoneyFormatUtils.doubleChangeF2Y(mOnceRechargeLimit));
                            setWarningText(tip);
                        }
                    } else {
                        showRechargeInputIllegal();
                    }
                }

            });
        } else {
            setTitle(getString(R.string.mine_withdrawals), true);
            mTvMoneyTip.setText(getString(R.string.mine_withdraw_money));
            mTvWithDrawAll.setVisibility(View.VISIBLE);
            mEtMoney.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    CommonUtils.twoDecimal(s.toString(), mEtMoney);
                    setFontBold(s);
                    if (isWithdrawInputLegal(s) && mOnceWithDrawLimit != 0) {
                        String input = s.toString().trim();
                        mCurrentMonkey = Double.parseDouble(input);

                        if (mAvailable < mOnceWithDrawLimit) {

                            if (isWithDrawMoneyLegal(mCurrentMonkey)) {
                                String canWithMax = getString(R.string.mine_withdraw_current_max, MoneyFormatUtils.lFormatS(mAvailable));
                                setNormalText(canWithMax);
                                showBtnEnableState(true);
                            } else {
                                setWarningText(getString(R.string.mine_withdraw_overmax));
                                showBtnEnableState(false);
                            }

                        } else {
                            if (isWithDrawMoneyLimitLegal(mCurrentMonkey)) {
                                String canWithMax = getString(R.string.mine_withdraw_current_max, MoneyFormatUtils.lFormatS(mAvailable));
                                setNormalText(canWithMax);
                                showBtnEnableState(true);

                            } else {
                                setWarningText(getString(R.string.mine_recharge_overmax, MoneyFormatUtils.doubleChangeF2Y(mOnceWithDrawLimit)));
                                showBtnEnableState(false);
                            }
                        }

                    } else {
                        showWithdrawInputIllegal();
                    }
                }
            });
        }
        mPresenter.loadData();
    }

    private void setNormalText(String text) {
        mTvWithDrawMax.setText(text);
        mTvWithDrawMax.setTextColor(ContextCompat.getColor(mContext, R.color.gray_8a));
    }

    private void setWarningText(String text) {
        mTvWithDrawMax.setText(text);
        mTvWithDrawMax.setTextColor(ContextCompat.getColor(mContext, R.color.red_f2));
    }

    private boolean isWithDrawMoneyLegal(double money) {

        double temp = money * 100;
        return money > 0 && temp <= mAvailable;

    }

    private boolean isWithDrawMoneyLimitLegal(double money) {

        double temp = money * 100;
        return money > 0 && temp <= mOnceWithDrawLimit;

    }

    private boolean isRechargeMoneyLegal(double money) {
        return money > 0 && money * 100 <= mOnceRechargeLimit;
    }

    private void showBtnEnableState(boolean enable) {
        if (enable) {
            mBtInvest.setEnabled(true);
            mBtInvest.setBackgroundResource(R.drawable.gradient_button_bg);
            mBtInvest.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            mBtInvest.setEnabled(false);
            mBtInvest.setBackgroundResource(R.drawable.shape_round_button_bg_gray_e1);
            mBtInvest.setTextColor(ContextCompat.getColor(mContext, R.color.gray_c3));
        }
    }

    private void showRechargeInputIllegal() {
        String tip = getString(R.string.mine_account_available, MoneyFormatUtils.lFormatS(mAvailable));
        setNormalText(tip);
        showBtnEnableState(false);
    }

    private void showWithdrawInputIllegal() {
        showBtnEnableState(false);
    }

    private boolean isWithdrawInputLegal(CharSequence s) {
        try {
            return s.length() > 0 && Double.parseDouble(s.toString().trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isRechargeInputLegal(CharSequence s) {
        try {
            return s.length() > 0 && Double.parseDouble(s.toString().trim()) >= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public WithDrawPresenter initPresenter() {
        return new WithDrawPresenter();
    }

    @OnClick({R.id.bt_invest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_invest:
                if (mType == ConstantUtils.TYPE_RECHARGE) {
                    submitNextRecharge();
                    StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_CLICK_CHARGE);
                } else {
                    submitNextWithDraw();
                    StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_CLICK_DRAWCASH);
                }
                break;
            default:
                break;
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
    public void onInputComplete(String password) {
        if (mType == ConstantUtils.TYPE_RECHARGE) {
            mPresenter.submitChargeInfo(new AccountChargeRequest((long) (mCurrentMonkey * 100), ConstantUtils.RECHARGE_TYPE, mBankCardId, password));
        } else {
            mPresenter.submitWithdrawInfo(new WithdrawCashRequest((long) (mCurrentMonkey * 100), password, mBankCardId));
        }
    }

    @Override
    public void onLoadResult() {
        if (mPassWordView == null) {
            return;
        }

        if (getActivity() != null) {
            Intent intent = new Intent();
            getActivity().setResult(ConstantUtils.INTENT_REQUEST, intent);
            getActivity().onBackPressed();
        }
    }

    @Override
    public void setBankData(BankInfoResponse response) {
        //bank info
        ImageLoaderUtil.loadImageFromUrl(mContext, response.logo_small, mIvBankLogo);
        mBankCardId = response.bank_card_id;
        String bankName = response.bank_name;
        String bankNum = response.bank_card_no;
        String bankSub = bankNum.substring(bankNum.length() - 4, bankNum.length());
        String bankInfo = getString(R.string.mine_withdraw_bank_info, bankName, getResources().getString(R.string.mine_recharge_end_number) + bankSub);
        Spannable set = CommonUtils.changeStringSize(mContext, bankInfo, 14, 0, 4, true);
        set.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.black_3e)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvBankName.setText(set);
        String onceLimit = getString(R.string.mine_recharge_limit, MoneyFormatUtils.doubleChangeF2Y(response.charge_once_limit),
                MoneyFormatUtils.doubleChangeF2Y(response.charge_day_limit));
        if (mType == ConstantUtils.TYPE_RECHARGE) {
            mTvInfoTip.setText(onceLimit);
            mOnceRechargeLimit = response.charge_once_limit;

        } else {
            mTvInfoTip.setText(getString(R.string.mine_withdraw_expect));
            mOnceWithDrawLimit = response.withdraw_once_limit;
        }
    }

    @Override
    public void setAccountData(AccountInfoBean response) {
        mAvailable = response.available;
        //提现 最多可提
        if (mType == ConstantUtils.TYPE_WITHDRAW) {
            String canWithMax = getString(R.string.mine_withdraw_current_max, MoneyFormatUtils.lFormatS(mAvailable));
            mTvWithDrawMax.setText(canWithMax);

        } else {
            String rechMin = getString(R.string.mine_recharge_min, getResources().getString(R.string.mine_recharge_hint_money));
            mEtMoney.setHint(CommonUtils.changeStringSize(mContext, rechMin, 12, 0, rechMin.length(), false));
            mTvWithDrawMax.setText(getString(R.string.mine_account_available, MoneyFormatUtils.lFormatS(mAvailable)));
        }

    }

    //全部提现
    @OnClick(R.id.tv_withdraw_all)
    public void onClick(View view) {
        String all = MoneyFormatUtils.lFormatS(mAvailable);
        mEtMoney.setText(all.replace(",", "").trim());
    }


    //充值
    private void submitNextRecharge() {


        mPassWordView = BottomPassWordDialog.with(mContext);
        mPassWordView.setMoneyTip(getString(R.string.mine_recharge_num, MoneyFormatUtils.lFormatS((long) (mCurrentMonkey * 100))))
                .setListener(this)
                .setShowForget(true)
                .setShowLoading(true)
                .setForgetPassWord(new BottomPassWordDialog.ForgetPassWordListener() {
                    @Override
                    public void onJumpSetPassWord() {
                        CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
                    }
                })
                .show();
    }


    //提现
    private void submitNextWithDraw() {

        mPassWordView = BottomPassWordDialog.with(mContext);
        mPassWordView.setMoneyTip(getString(R.string.mine_withdraw_num, MoneyFormatUtils.lFormatS((long) (mCurrentMonkey * 100))))
                .setListener(this)
                .setShowForget(true)
                .setShowLoading(true)
                .setForgetPassWord(new BottomPassWordDialog.ForgetPassWordListener() {
                    @Override
                    public void onJumpSetPassWord() {
                        CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
                    }
                })
                .show();


    }

    @Override
    public void showCommitFail(int code, String msg) {
        mPassWordView.dismiss();

        if (mType == ConstantUtils.TYPE_RECHARGE) {
            if (code != ErrorMsgUtils.PAY_PASSWORD_ERROR) {
                showHintDialog(msg, getString(R.string.with_draw_comfirm));
            } else {
                showDialog(null, msg, getString(R.string.retry), getString(R.string.forget_payment_pw));
            }
        } else {
            if (code != ErrorMsgUtils.PAY_PASSWORD_ERROR) {
                showHintDialog(msg, getString(R.string.with_draw_comfirm));
            } else {
                showDialog(null, msg, getString(R.string.retry), getString(R.string.forget_payment_pw));

            }
        }


    }

    //只有确定按钮的Dialog
    public void showHintDialog(String title, String button) {
        DialogUtil.createHintDialogNoAction(mContext, title, button);
    }

    //确定和取消的Dialog
    public void showDialog(String title, String msg, String positiveBtnName, String negativeBtnName) {

        DialogUtil.createDialog(mContext, title, msg, positiveBtnName, negativeBtnName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
            }
        }).show();
    }

    @Override
    public void showCommitSuccess() {
        if (mPassWordView == null) {
            return;
        }
        mPassWordView.loadSuccess();
    }

    @Override
    public void showInitDataFail(int code, String msg) {
        ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg);
    }


    public void setFontBold(CharSequence s) {
        if (s.length() > 0) {
            mEtMoney.setTypeface(Typeface.DEFAULT_BOLD);
            mEtMoney.setPadding(0, 0, 0, 0);
        } else {
            mEtMoney.setTypeface(Typeface.DEFAULT);
            mEtMoney.setPadding(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.margin_content_5));

        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMainEventBus(LoginSuccessEvent event) {
        if (event == null || StringUtil.isNullOrEmpty(event.msg)) {
            return;
        }
        if (event.msg.equals(getString(R.string.login))) {
            mPresenter.loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
