package com.weiyankeji.zhongmei.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.customview.passwordview.BottomPassWordDialog;
import com.weiyankeji.library.utils.DialogUtil;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.KeyBoardUtil;
import com.weiyankeji.library.utils.NumberUtil;
import com.weiyankeji.library.utils.StringUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.TextWatcherAdapter;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestDetailResponse;
import com.weiyankeji.zhongmei.ui.mmodel.TicketListResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestIncomeRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestIncomeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestMentRequest;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestMentResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestProductIntroductionResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestMentPresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestMentView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 立即投资
 * Created by liuhaiyang on 2017/8/30.
 */

public class InvestMentFragment extends BaseMvpFragment<InvestMentView, InvestMentPresenter> implements InvestMentView,
        BottomPassWordDialog.PassWordDialogListener {

    private static final int INTENT_REQUEST = 1000;
    private static final int INTENT_REQUEST_RECHARE = 1001;
    private static final int PROTOCOL_TIP_LENGTH = 12;

    @BindView(R.id.et_invest_money)
    EditText mEtMoney;
    @BindView(R.id.tv_income)
    TextView mTvInCome;
    @BindView(R.id.bt_invest)
    Button mBtInvest;
    //剩余可投
    @BindView(R.id.tv_last_money)
    TextView mTvLastMoney;
    //充值
    @BindView(R.id.tv_recharge)
    TextView mTvRecharge;
    //卡券
    @BindView(R.id.rl_invest_ment_ticket)
    RelativeLayout mRlTicket;
    @BindView(R.id.tv_ticket_tip)
    TextView mTvTicketTip;
    //可用余额
    @BindView(R.id.tv_use_money)
    TextView mTvUseMoney;
    //protocol
    @BindView(R.id.tv_protocol)
    TextView mTvProtocol;

    //支付密码view
    BottomPassWordDialog mPassWordView;

    private String mSkuId;
    private boolean mIsCanInvest;

    //选择的卡券
    private TicketListResponse mTicketItem;

    private InvestDetailResponse mDetailResponse;
    //账号可用余额
    private long mAvailable;
    //起投金额
    private long mStartMoney;

    //键盘是否弹起
    boolean mIsMonitor = false;
    //谈起次数，避免重复动画
    int mTemp = 0;
    int mFirstHeightDiffrence;
    boolean mIsFirst = true;


    @Override
    public int setContentLayout() {
        return R.layout.fragment_investment;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        if (bundle != null) {
            mDetailResponse = (InvestDetailResponse) bundle.getSerializable(ConstantUtils.KEY_DATA);
        }
        setTitle(getString(R.string.invest_home_invest_rightnow), true);

        if (mDetailResponse != null) {
            mSkuId = mDetailResponse.sku_id;
            //剩余可投
            long lastMoney = mDetailResponse.c_sku_amount;
            String strLast = MoneyFormatUtils.decimalFormatRuleOne(lastMoney);
            mStartMoney = mDetailResponse.min_invest_amount;
            mTvLastMoney.setText(getString(R.string.common_yuan, strLast));
            //如果剩余可投 < 起投金额 起=剩
            if (lastMoney < mDetailResponse.min_invest_amount) {
                mStartMoney = lastMoney;
            }
            //起投，递增
            String startInve = MoneyFormatUtils.lFormatYuanNoPoint(mDetailResponse.min_invest_amount);
            String raise = MoneyFormatUtils.lFormatYuanNoPoint(mDetailResponse.raise_amount);
            String strHint = getString(R.string.invest_start_money_hint, startInve, raise);
            mEtMoney.setHint(CommonUtils.changeStringSize(mContext, strHint, 14, startInve.length() + 1, strHint.length(), false));
            //卡券
            mTvTicketTip.setText(mDetailResponse.is_market_card == 1 ? getString(R.string.invest_choise) : getString(R.string.invest_unused_ticket));
            mRlTicket.setClickable(mDetailResponse.is_market_card == 1);

            mBtInvest.setClickable(false);
            mEtMoney.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {

                    String edText = s.toString();
                    setFontBold(edText);
                    if (StringUtil.isNullOrEmpty(edText)) {
                        return;
                    }

                    //开头不能以0开头
                    CommonUtils.twoDecimal(edText, mEtMoney);

                    long longText = (long) NumberUtil.getDouble(edText) * 100;

                    if (longText >= mStartMoney) {
                        mBtInvest.setBackgroundResource(R.drawable.common_long_button_state_style);
                        mBtInvest.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                        mBtInvest.setClickable(true);
                        mIsCanInvest = true;
                    } else {
                        mBtInvest.setBackgroundResource(R.drawable.shape_round_button_bg_gray_e1);
                        mBtInvest.setTextColor(ContextCompat.getColor(mContext, R.color.gray_c2));
                        mBtInvest.setClickable(false);
                        mIsCanInvest = false;
                    }

                    if (mTicketItem != null) {
                        mTvTicketTip.setText(getString(R.string.invest_choise));
                        mTicketItem = null;
                    }
                }
            });

            //invest protocol
            if (!ExtendUtil.listIsNullOrEmpty(mDetailResponse.agreement)) {
                StringBuilder proBuilder = new StringBuilder();
                proBuilder.append(getString(R.string.invest_agreement_tip));

                for (InvestProductIntroductionResponse response : mDetailResponse.agreement) {
                    proBuilder.append(response.getTitle());
                }

                Spannable spannable = new SpannableString(proBuilder);
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.gray_8a)), 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                int start = PROTOCOL_TIP_LENGTH;
                for (final InvestProductIntroductionResponse response : mDetailResponse.agreement) {

                    int vSize = response.getTitle().length();
                    int end = start + vSize;
                    spannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            CommonUtils.intentWebView(InvestMentFragment.this, getString(R.string.custom_details_invest_agreement), response.getLink());
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    start = end;
                }
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.gray_8a)), 0, PROTOCOL_TIP_LENGTH, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                mTvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
                mTvProtocol.setText(spannable);
            }
        }

        //获取可用余额
        mPresenter.loadAccountInfoData();
        setDefalutIncome();

        setMonitorKeyboard();

    }


    @Override
    public InvestMentPresenter initPresenter() {
        return new InvestMentPresenter();
    }

    @OnClick({R.id.bt_invest, R.id.rl_invest_ment_ticket, R.id.tv_recharge, R.id.tv_all_input, R.id.tv_income})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_invest:
                if (mDetailResponse == null) {
                    return;
                }
                if (!UserUtils.getInstance().isSetPayPw()) {
                    UserUtils.getInstance().checkUserInvidaType(this);
                    return;
                }
                double getMon = NumberUtil.getDouble(mEtMoney.getText().toString()) * 100; //输入金额
                long startMon = mDetailResponse.min_invest_amount; //起投金额
                long lastMon = mDetailResponse.sku_amount - mDetailResponse.invested_amount; //剩余可投
                long raise = mDetailResponse.raise_amount; //递增金额
                //金额 > 账户可用
                if (getMon > mAvailable) {
                    DialogUtil.createDialog(mContext, null, getString(R.string.invest_shortage_toast), getString(R.string.mine_recharge), getString(R.string.common_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Bundle parRec = new Bundle();
                                    parRec.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_RECHARGE);
                                    startFragmentForResult(WithDrawFragment.class, parRec, INTENT_REQUEST_RECHARE);
                                }
                            }, null).show();
                    return;
                }
                //剩余可投 < 起投 （最后一笔 投资金额必须=剩余可投）
                if (lastMon < startMon) {
                    if (getMon != lastMon) {
                        ToastUtil.showShortToast(mContext, getString(R.string.invest_lastonce_tip, lastMon));
                        return;
                    }
                } else {
                    //金额 < 起投
                    if (getMon < startMon) {
                        ToastUtil.showShortToast(mContext, getString(R.string.invest_money_toast, startMon));
                        return;
                    }
                    //金额 > 剩余可投
                    if (getMon > lastMon) {
                        ToastUtil.showShortToast(mContext, getString(R.string.invest_beyond_toast));
                        return;
                    }

                    //金额递增规则
                    if (getMon > startMon && (getMon - startMon) % raise != 0) {
                        ToastUtil.showShortToast(mContext, getString(R.string.invest_raise_toast, MoneyFormatUtils.lFormatYuanNoPoint(mDetailResponse.raise_amount)));
                        return;
                    }
                }

                setPasswordViewShow();
                KeyBoardUtil.closeKeybord(mEtMoney, mContext);
                break;
            case R.id.rl_invest_ment_ticket:
                String value = mEtMoney.getText().toString();
                if (StringUtil.isNullOrEmpty(value)) {
                    ToastUtil.showShortToast(mContext, getString(R.string.invest_choice_ticket_tip));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtils.KEY_TYPE, CardTicketFragment.TYPE_SELECT_TICKET);
                bundle.putSerializable(ConstantUtils.KEY_DATA, mTicketItem);
                bundle.putString(ConstantUtils.KEY_ID, mDetailResponse.sku_id);

                try {
                    long money = Long.parseLong(value) * 100;
                    bundle.putLong(ConstantUtils.KEY_VALUE, money);
                } catch (Exception e) {
                    bundle.putInt(ConstantUtils.KEY_VALUE, 0);
                }

                startFragmentForResult(CardTicketFragment.class, bundle, INTENT_REQUEST);
                break;
            case R.id.tv_recharge:
                if (!UserUtils.getInstance().isSetPayPw()) {
                    UserUtils.getInstance().checkUserInvidaType(this);
                    return;
                }
                Bundle parRec = new Bundle();
                parRec.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_RECHARGE);
                startFragmentForResult(WithDrawFragment.class, parRec, INTENT_REQUEST_RECHARE);
                break;
            case R.id.tv_all_input:
                if (mAvailable != 0) {
                    mEtMoney.setText(MoneyFormatUtils.doubleChangeF2Y(mAvailable));
                    loadIncome();
                }
                break;
            case R.id.tv_income:
                DialogUtil.createHintDialogNoAction(mContext, getString(R.string.invest_detail_income_toast), getString(R.string.common_ok));
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST && resultCode == ConstantUtils.INTENT_RESPONSE) {
            if (data != null) {
                mTicketItem = (TicketListResponse) data.getSerializableExtra(ConstantUtils.KEY_DATA);
                if (mTicketItem != null) {
                    String value = mTicketItem.market_card_type == ConstantUtils.MARKET_CARD_TYPE_VOUCHERS
                            ? getString(R.string.invest_ticket_use, mTicketItem.market_card_title, MoneyFormatUtils.lFormatYuanNoPoint(mTicketItem.market_card_value))
                            : getString(R.string.common_double_text, mTicketItem.market_card_title, getString(R.string.common_rate_percentage, MoneyFormatUtils.yieldPointRuleOne((int) mTicketItem.market_card_value)));
                    mTvTicketTip.setText(value);
                    loadIncome();
                }
            } else {
                if (mTicketItem != null) {
                    mTvTicketTip.setText(getString(R.string.invest_choise));
                    mTicketItem = null;
                }
            }
        } else if (requestCode == INTENT_REQUEST_RECHARE && resultCode == ConstantUtils.INTENT_RESPONSE) {
            mPresenter.loadAccountInfoData(); //充值成功后刷新可以余额
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    /**
     * 输入密码后去投资
     */
    @Override
    public void onInputComplete(String password) {
        InvestMentRequest request = new InvestMentRequest();
        request.sku_id = mSkuId;
        double iMoney = NumberUtil.getDouble(mEtMoney.getText().toString()) * 100; // 投资金额 单位：分
        request.invest_amount = String.valueOf((int) iMoney);
        request.pay_password = password;
        if (mTicketItem != null) {
            request.vouchers_id = mTicketItem.market_card_id;
        }
        mPresenter.postInvest(request);
    }

    /**
     * 支付loading动画结束后的回调(成功后)
     */
    @Override
    public void onLoadResult() {
        if (mPassWordView == null) {
            return;
        }
        if (getActivity() != null) {
            Intent intent = new Intent();
            getActivity().setResult(ConstantUtils.INTENT_REQUEST, intent);
            SystemClock.sleep(2000); //等待2s后页面消失
            getActivity().onBackPressed();
        }
    }

    @Override
    public void setAccountInfoData(AccountInfoBean bean) {
        mTvUseMoney.setText(getString(R.string.common_yuan, MoneyFormatUtils.lFormatS(bean.available)));
        mAvailable = bean.available;
    }

    /**
     * 投资成功回调
     */
    @Override
    public void postInvestComplete(InvestMentResponse response) {
        if (mPassWordView == null) {
            return;
        }
        mPassWordView.loadSuccess();

    }

    @Override
    public void postInvestError(int code, String errorMsg) {
        switch (code) {
            case ErrorMsgUtils.PAY_PASSWORD_ERROR:
                DialogUtil.createDialog(mContext, null, errorMsg, getString(R.string.common_again), getString(R.string.common_forget_password),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setPasswordViewShow();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
                            }
                        }).show();
                break;
            case ErrorMsgUtils.PAY_MONEN_ENOUTH:
                DialogUtil.createDialog(mContext, null, errorMsg, getString(R.string.mine_recharge), getString(R.string.common_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Bundle parRec = new Bundle();
                                parRec.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_RECHARGE);
                                startFragmentForResult(WithDrawFragment.class, parRec, INTENT_REQUEST_RECHARE);
                            }
                        }, null).show();
                break;
            default:
                DialogUtil.createHintDialogNoAction(mContext, errorMsg, getString(R.string.common_ok));
                break;
        }
        mPassWordView.dismiss();
    }

    /**
     * 预计收益
     */
    @Override
    public void setIncome(InvestIncomeResponse response) {
        String strIc = getString(R.string.invest_income_tip, MoneyFormatUtils.lFormatS(response.predict_income));
        mTvInCome.setText(CommonUtils.changeStringColor(strIc, ContextCompat.getColor(mContext, R.color.gray_8a), 0, 4));
    }

    @Override
    public void postAccountInfoError(int code, String errorMsg) {
        ErrorMsgUtils.showNetErrorToastOrLogin(this, code, errorMsg, true);
    }

    /**
     * 设置默认预计收益
     */
    private void setDefalutIncome() {
        String strIc = getString(R.string.invest_income_tip, "0");
        mTvInCome.setText(CommonUtils.changeStringColor(strIc, ContextCompat.getColor(mContext, R.color.gray_8a), 0, 4));
    }

    /**
     * 网络请求 获取预计收益
     */
    private void loadIncome() {
        InvestIncomeRequest request = new InvestIncomeRequest();
        request.sku_id = mSkuId;
        request.invest_amount = (long) NumberUtil.getDouble(mEtMoney.getText().toString()) * 100; // 投资金额 单位：分
        if (mTicketItem != null) {
            request.vouchers_id = mTicketItem.market_card_id;
        }
        mPresenter.getIncome(request);
    }

    /**
     * 软键盘监听
     */
    public void setMonitorKeyboard() {
        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect mRect = new Rect();
                mParentView.getWindowVisibleDisplayFrame(mRect);
                int screenHeight = mParentView.getRootView().getHeight();
                int heightDifference = screenHeight - (mRect.bottom);

                if (mIsFirst) {
                    mFirstHeightDiffrence = heightDifference;
                    mIsFirst = false;
                }

                if (heightDifference > 200 && mTemp == 0) { //弹出
                    mTemp++;
                    mIsMonitor = true;
                } else if (heightDifference == mFirstHeightDiffrence && mIsMonitor) {
                    mIsMonitor = false;
                    mTemp = 0;

                    if (mIsCanInvest) {
                        loadIncome();
                    } else {
                        setDefalutIncome();
                    }
                }
            }
        });
    }

    /**
     * 设置支付密码显示
     */
    private void setPasswordViewShow() {
        double getMon = NumberUtil.getDouble(mEtMoney.getText().toString()) * 100; //输入金额
        mPassWordView = BottomPassWordDialog.with(mContext);
        if (mTicketItem != null && mTicketItem.market_card_value != 0) {
            getMon = getMon - mTicketItem.market_card_value;
        }
        mPassWordView.setMoneyTip(getString(R.string.invest_money_tip, MoneyFormatUtils.lFormatS((long) getMon)))
                .setListener(this)
                .setShowForget(true)
                .setShowLoading(true)
                .setForgetPassWord(new BottomPassWordDialog.ForgetPassWordListener() {
                    @Override
                    public void onJumpSetPassWord() {
                        CommonUtils.intentMsgValida(getActivity(), CommonAuthFragment.TYPE_FORGET_PAY_PW);
                    }
                }).show();
    }

    public void setFontBold(String s) {
        if (!StringUtil.isNullOrEmpty(s)) {
            mEtMoney.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            mEtMoney.setTypeface(Typeface.DEFAULT);
        }
    }
}
