package com.weiyankeji.zhongmei.ui.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.activity.MainActivity;
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.MineResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AccountInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.AssetInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.User;
import com.weiyankeji.zhongmei.ui.mpresenter.MinePresenter;
import com.weiyankeji.zhongmei.ui.mview.MineView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseMvpFragment<MineView, MinePresenter> implements MineView, OnRefreshListener {

    @BindView(R.id.ll_account_content)
    LinearLayout mLlAccountContent;
    @BindView(R.id.iv_notification)
    ImageView mIvNotification;
    @BindView(R.id.rl_available_amount)
    RelativeLayout mRlAvailable;
    @BindView(R.id.iv_user_icon)
    ImageView mIvUserIcon;
    @BindView(R.id.tv_user_phone)
    TextView mTvUserPhone;
    @BindView(R.id.rl_mine_custom)
    RelativeLayout mRlMineCustom;
    @BindView(R.id.tv_available)
    TextView mTvAvailable;
    @BindView(R.id.tv_withdraw)
    TextView mTvWithDraw;
    @BindView(R.id.tv_recharge)
    TextView mTvRecharge;
    @BindView(R.id.iv_image_line)
    ImageView mIvLine;
    @BindView(R.id.stl_mine)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @BindString(R.string.mine_unregistered)
    String mUnregisteredName;
    @BindString(R.string.mine_desensitization_result)
    String mDesensiResult;
    @BindDrawable(R.drawable.my_icon_message)
    Drawable mMessage;
    @BindDrawable(R.drawable.my_icon_message_remind)
    Drawable mMessageRemind;


    private TextView mTvTotalAsset;
    private TextView mTvTotalEarning;
    private TextView mTvPrincipal;
    private TextView mTvInteret;
    private ImageView mIvDesensi;

    private View mUnregistered;
    private View mUnaccout;
    private View mOpenAccout;
    LinearLayout.LayoutParams mLayoutParams;


    MinePresenter mMinePresenter;
    MineResponse mMineResponse;

    AccountInfoBean mAccountInfo;
    AssetInfoBean mAssetInfo;


    User mUser;
    boolean mIsLogin = false;
    //脱敏标识
    boolean mIsd;
    public static final String ACCOUNT_TAG = "ACCOUNT_TAG";
    SharedPreferences mShareP;
    long mMessageTime;      //时间
    MainActivity mMainActivity;


    @Override
    public void onRefresh() {
        mMainActivity.netRedNotif();
        mMinePresenter.loadData(new BaseRequest());
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_mine;

    }

    @Override
    public void finishCreateView(Bundle bundle) {
        mMainActivity = (MainActivity) getActivity();
        //导入布局
        mOpenAccout = getActivity().getLayoutInflater().inflate(R.layout.item_mine_accout, null);
        mUnaccout = getActivity().getLayoutInflater().inflate(R.layout.item_mine_unaccount, null);
        mUnregistered = getActivity().getLayoutInflater().inflate(R.layout.item_mine_unregistered, null);
        //支付账户相关view
        mTvTotalAsset = ButterKnife.findById(mOpenAccout, R.id.tv_total_asset);
        mTvTotalEarning = ButterKnife.findById(mOpenAccout, R.id.tv_total_earning);
        mTvPrincipal = ButterKnife.findById(mOpenAccout, R.id.tv_principal_received);
        mTvInteret = ButterKnife.findById(mOpenAccout, R.id.tv_interest_received);
        mIvDesensi = ButterKnife.findById(mOpenAccout, R.id.ib_desensi);

        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        setNotTitle();
        getUserInfo();
        setAccout();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserUtils.isUserLogin()) {
            mSwipeToLoadLayout.setRefreshEnabled(true);
        } else {
            mSwipeToLoadLayout.setRefreshEnabled(false);
        }
        CommonUtils.setNotificationPointOfActivity(mMainActivity, SharedPreferencesUtil.getSharedPreferences(mContext, ConstantUtils.SHAREDPREFERENCES_NAME), mMainActivity.mMinePositon, mMainActivity.mMinePositon, mIvNotification);
        getUserInfo();
        if (mIsLogin) {
            mMinePresenter.loadData(new BaseRequest());
            setAccout();
        } else {
            setAccout();
        }
    }

    /**
     * 获取用户信息，以及登录状态
     * mIsLogin、mUser
     */
    public void getUserInfo() {
        mIsd = (Boolean) SharedPreferencesUtil.get(getActivity(), ConstantUtils.SHAREDPREFERENCES_NAME, ConstantUtils.SP_MINE_DESENSITIZATION, false);
        mIsLogin = UserUtils.getInstance().isUserLogin();
        if (mIsLogin) {
            mUser = UserUtils.getInstance().getUserObject();
        }
    }

    /**
     * 切换到当前页面。重新请求，判断token是否失效
     *
     * @return
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            CommonUtils.setNotificationPointOfActivity(mMainActivity, SharedPreferencesUtil.getSharedPreferences(mContext, ConstantUtils.SHAREDPREFERENCES_NAME), mMainActivity.mMinePositon, mMainActivity.mMinePositon, mIvNotification);
            if (mIsLogin) {
                mMinePresenter.loadData(new BaseRequest());
                setAccout();
            } else {
                setAccout();
            }
        }
    }

    /**
     * 根据登录、支付状态显示不同view
     */
    public void setAccout() {
        mLlAccountContent.removeAllViews();
        mRlMineCustom.setVisibility(View.GONE); //我的定制
        mIvLine.setVisibility(View.GONE);
        if (mIsLogin) {
            if (TextUtils.isEmpty(mUser.avatar)) {
                ImageLoaderUtil.loadCircleImageFromLocalRes(mContext, R.drawable.login_logo, mIvUserIcon, 0);
            } else {
                ImageLoaderUtil.loadCircleImageFromUrl(mContext, mUser.avatar, mIvUserIcon, 0);
            }
            mTvUserPhone.setText(mUser.mobile);    //设置手机号
            if (mAccountInfo != null) {
                mTvAvailable.setText(MoneyFormatUtils.lFormatS(Long.valueOf(mAccountInfo.available)));
            }
            if (UserUtils.getInstance().isOpenAccount()) {
                mRlAvailable.setVisibility(View.VISIBLE);
                mLlAccountContent.addView(mOpenAccout, mLayoutParams);
                showAsset();
                mIvDesensi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mAssetInfo != null) {
                            mShareP = SharedPreferencesUtil.getSharedPreferences(getActivity(), ConstantUtils.SHAREDPREFERENCES_NAME);
                            if ((Boolean) SharedPreferencesUtil.get(mShareP, ConstantUtils.SP_MINE_DESENSITIZATION, false)) {
                                SharedPreferencesUtil.put(mShareP, ConstantUtils.SP_MINE_DESENSITIZATION, false);
                            } else {
                                SharedPreferencesUtil.put(mShareP, ConstantUtils.SP_MINE_DESENSITIZATION, true);
                            }
                        }
                        showAsset();
                    }
                });
                if (mUser.dz_auth == ConstantUtils.AUTHENTICATION_IN) {
                    mRlMineCustom.setVisibility(View.VISIBLE);
                    mIvLine.setVisibility(View.VISIBLE);
                }
            } else {
                mRlAvailable.setVisibility(View.GONE);
                mLlAccountContent.addView(mUnaccout, mLayoutParams);
            }
        } else {
            mIvUserIcon.setImageResource(R.drawable.my_icon_unregistered_user);
            mTvUserPhone.setText(mUnregisteredName);
            mRlAvailable.setVisibility(View.GONE);
            mLlAccountContent.addView(mUnregistered, mLayoutParams);
        }
    }

    /**
     * 脱敏显示
     */
    public void showAsset() {
        if (mAssetInfo != null) {
            if ((Boolean) SharedPreferencesUtil.get(getActivity(), ConstantUtils.SHAREDPREFERENCES_NAME, ConstantUtils.SP_MINE_DESENSITIZATION, false)) {
                mTvTotalAsset.setText(mDesensiResult);
                mTvTotalEarning.setText(mDesensiResult);
                mTvPrincipal.setText(mDesensiResult);
                mTvInteret.setText(mDesensiResult);
                mTvAvailable.setText(mDesensiResult);
                mIvDesensi.setImageResource(R.drawable.my_icon_eye_close);
            } else {
                mTvTotalAsset.setText(MoneyFormatUtils.lFormatS(mAssetInfo.total_asset));
                mTvTotalEarning.setText(MoneyFormatUtils.lFormatS(mAssetInfo.total_earning));
                mTvPrincipal.setText(MoneyFormatUtils.lFormatS(mAssetInfo.unback_principal));
                mTvInteret.setText(MoneyFormatUtils.lFormatS(mAssetInfo.unback_interest));
                mTvAvailable.setText(MoneyFormatUtils.lFormatS(mAccountInfo.available));
                mIvDesensi.setImageResource(R.drawable.my_icon_eye_open);
            }
        }
    }

    /**
     * 网络请求结果
     *
     * @param mineResponse
     */
    @Override
    public void setData(MineResponse mineResponse) {
        mMineResponse = mineResponse;
        User response_user = mineResponse.user_info;
        mUser = UserUtils.getInstance().savaUserExcept(mUser, response_user);
        mAssetInfo = mineResponse.asset_info;
        mAccountInfo = mineResponse.account_info;
        saveUserInfo();
        setAccout();


    }

    /**
     * token失效
     *
     * @param code
     * @param msg
     */
    @Override
    public void failureToken(int code, String msg) {
        switch (code) {
            case ErrorMsgUtils.TOKEN_INVALID:
            case ErrorMsgUtils.NEED_RELOGIN:
                mSwipeToLoadLayout.setRefreshing(false);
                mSwipeToLoadLayout.setRefreshEnabled(false);
                UserUtils.getInstance().deleteUser();
                mUser = null;
                getUserInfo();
                setAccout();
                break;
            default:
        }
    }

    /**
     * 更新用户信息，
     */
    public void saveUserInfo() {
        if (mUser != null) {
            UserUtils.getInstance().saveUserObject(mUser);
        }
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        mSwipeToLoadLayout.setRefreshing(false);
    }


    @Override
    public MinePresenter initPresenter() {
        mMinePresenter = new MinePresenter();
        return mMinePresenter;

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    @OnClick({R.id.ll_mine_transact_record, R.id.rl_mine_custom, R.id.ll_mine_bankcard, R.id.ll_mine_card_roll,
            R.id.iv_user_icon, R.id.tv_user_phone, R.id.ll_account_content, R.id.rl_payment, R.id.rl_help,
            R.id.tv_withdraw, R.id.tv_recharge, R.id.rl_regular, R.id.iv_notification, R.id.rl_about_us, R.id.rl_invite})

    public void onViewClick(View view) {
        Bundle mBundle;
        switch (view.getId()) {
            case R.id.ll_mine_transact_record:
                startFragmentCheckLogin(TransactRecordFragment.class, null);
                break;
            case R.id.rl_mine_custom:
                mBundle = new Bundle();
                mBundle.putInt(ConstantUtils.SKEY_SIGN_TAG, ConstantUtils.INVEST_SKU_TYPE_CUSTOM);
                startFragmentCheckLogin(CustomFragment.class, mBundle);
                break;
            case R.id.rl_regular:
                mBundle = new Bundle();
                mBundle.putInt(ConstantUtils.SKEY_SIGN_TAG, ConstantUtils.INVEST_SKU_TYPE_STIPULATE);
                startFragmentCheckLogin(CustomFragment.class, mBundle);
                break;
            case R.id.ll_mine_bankcard:
                if (mIsLogin) {
                    if (!UserUtils.getInstance().isBindBankCard()) {
                        UserUtils.getInstance().checkUserInvidaType(this);
                    } else {
                        startFragmentCheckLogin(BankCardFragment.class, null);
                    }
                } else {
                    startFragmentCheckLogin(LoginFragment.class, null);
                }
                break;
            case R.id.ll_mine_card_roll:
                mBundle = new Bundle();
                mBundle.putInt(ConstantUtils.KEY_TYPE, CardTicketFragment.TYPE_MY_TICKET);
                startFragmentCheckLogin(CardTicketFragment.class, mBundle);
                break;
            case R.id.iv_user_icon:
            case R.id.tv_user_phone:
                startFragmentCheckLogin(UserInfoFragment.class, null);
                break;
            case R.id.ll_account_content:
                if (mIsLogin) {
                    if (UserUtils.getInstance().isOpenAccount()) {
                        startFragmentCheckLogin(AssetsFragment.class, null);
                    } else {
                        UserUtils.getInstance().checkUserInvidaType(this);
                    }
                } else {
                    startFragmentCheckLogin(LoginFragment.class, null);
                }
                break;
            case R.id.rl_payment:
                startFragmentCheckLogin(AccountSettingFragment.class, null);
                break;
            case R.id.rl_help:
                CommonUtils.intentWebView(this, CommonWebViewFragment.TYPE_WEB_BACKFEEK, null, UrlConfig.H5_HELP);
                break;
            case R.id.tv_recharge:
                if (mIsLogin) {
                    if (UserUtils.getInstance().isSetPayPw()) {
                        Bundle parRec = new Bundle();
                        parRec.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_RECHARGE);
                        startFragmentCheckLogin(WithDrawFragment.class, parRec);
                    } else {
                        UserUtils.getInstance().checkUserInvidaType(this);
                    }
                } else {
                    startFragmentCheckLogin(LoginFragment.class, null);
                }
                break;
            case R.id.tv_withdraw:
                if (mIsLogin) {
                    if (UserUtils.getInstance().isSetPayPw()) {
                        Bundle parWit = new Bundle();
                        parWit.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_WITHDRAW);
                        startFragmentCheckLogin(WithDrawFragment.class, parWit);
                    } else {
                        UserUtils.getInstance().checkUserInvidaType(this);
                    }
                } else {
                    startFragmentCheckLogin(LoginFragment.class, null);
                }
                break;
            case R.id.iv_notification:
                startFragmentCheckLogin(PlatformNoticeListFragment.class, null);
                break;
            case R.id.rl_about_us:
                startFragment(AboutUsFragment.class);
                break;
            case R.id.rl_invite:
                startFragmentCheckLogin(InvitationFragment.class, null);
                break;
            default:
        }

    }


    public ImageView getImageview() {
        return mIvNotification;
    }

    /**
     * 页面跳转
     *
     * @param mClazz
     * @param bundle 无参传null
     */
    public void startFragmentCheckLogin(Class<? extends Fragment> mClazz, Bundle bundle) {
        Intent mIntent;
        if (!mIsLogin) {
            mIntent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), LoginFragment.class);
        } else {
            if (bundle == null) {
                mIntent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), mClazz);
            } else {
                mIntent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), mClazz, bundle);
            }
        }
        startActivity(mIntent);

    }

}
