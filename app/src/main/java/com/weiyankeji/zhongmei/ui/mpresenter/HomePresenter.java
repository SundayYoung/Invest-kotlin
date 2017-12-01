package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.library.utils.DensityUtil;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ScreenUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.customview.CellLayout;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.fragment.CommonWebViewFragment;
import com.weiyankeji.zhongmei.ui.fragment.HotNewsListFragment;
import com.weiyankeji.zhongmei.ui.fragment.InvestListFragment;
import com.weiyankeji.zhongmei.ui.fragment.InvestProductDetailFragment;
import com.weiyankeji.zhongmei.ui.mmodel.BaseRequest;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NoticeBean;
import com.weiyankeji.zhongmei.ui.mmodel.home.HomeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.bean.GroupBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NewsBean;
import com.weiyankeji.zhongmei.ui.mmodel.multi.CheckUpdateResponse;
import com.weiyankeji.zhongmei.ui.mview.HomeView;
import com.weiyankeji.zhongmei.utils.CheckUpdateUtils;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;
import com.weiyankeji.zhongmei.utils.StatisticalUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by caiwancheng on 2017/8/18.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    public SharedPreferences mSharedPreferences;
    public float mMarginLeft;
    public float mMarginMin;
    private boolean mAlreadyLoad;


    private LayoutInflater mLInflater;

    private String mNoticeKey = ConstantUtils.SP_HOME_NOTICE_ID;

    public HomePresenter(Context context) {
        mLInflater = LayoutInflater.from(context);
        mMarginLeft = context.getResources().getDimension(R.dimen.margin_big);
        mMarginMin = context.getResources().getDimension(R.dimen.margin_middle);
        mSharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, ConstantUtils.SHAREDPREFERENCES_NAME_CONFIG);
    }

    /**
     * 是否显示小黄条
     *
     * @return
     */
    public boolean isShowNotice(NoticeBean bean) {
        if (UserUtils.getInstance().getUser() != null) {
            mNoticeKey = UserUtils.getInstance().getUser().getUuid() + ConstantUtils.SP_HOME_NOTICE_ID;
        }
        String noticeId = (String) SharedPreferencesUtil.get(mSharedPreferences, mNoticeKey, "");
        if (bean == null) {
            return false;
        } else {
            if (bean.id.equals(noticeId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 储存小黄条ID
     *
     * @param id
     */
    public void saveNoticeId(String id) {
        if (UserUtils.getInstance().getUser() != null) {
            mNoticeKey = UserUtils.getInstance().getUser().getUuid() + ConstantUtils.SP_HOME_NOTICE_ID;
        }
        SharedPreferencesUtil.put(mSharedPreferences, mNoticeKey, id);
    }

    /**
     * 初始化模块标题
     */
    public void initModule(final BaseFragment fragment, RelativeLayout layout, final String title, String moreHint) {
        initModule(fragment, layout, title, moreHint, 0);
    }

    /**
     * 初始化模块标题
     */
    public void initModule(final BaseFragment fragment, RelativeLayout layout, final String title, String moreHint, final int index) {
        TextView tvTitle = ButterKnife.findById(layout, R.id.tv_module_title);
        tvTitle.setText(title);

        LinearLayout llMore = ButterKnife.findById(layout, R.id.ll_module_mode);
        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (index) {
                    case 1: //定制理财
                        Bundle bundle = new Bundle();
                        bundle.putString(ConstantUtils.KEY_TITLE, title);
                        bundle.putInt(ConstantUtils.KEY_TYPE, ConstantUtils.TYPE_CUSTOM_INVEST);
                        Intent intent = FragmentContainerActivity.getFragmentContainerActivityIntent(fragment.getActivity(), InvestListFragment.class, bundle);
                        fragment.getActivity().startActivity(intent);
                        break;
                    case 3: //热点关注
                        fragment.startFragment(HotNewsListFragment.class);
                        break;
                    default:
                }
            }
        });

        TextView tvMore = ButterKnife.findById(layout, R.id.tv_module_more);
        if (!TextUtils.isEmpty(moreHint)) {
            tvMore.setText(moreHint);
            llMore.setVisibility(View.VISIBLE);
        } else {
            llMore.setVisibility(View.GONE);
        }
    }

    /**
     * 检测更新
     *
     * @param fragment
     */
    public void checkUpdate(final BaseFragment fragment) {
        if (!CheckUpdateUtils.checkUpdateConfig()) {
            return;
        }
        mCall = mRestService.postData(UrlConfig.CHECK_UPDATE, new BaseRequest());
        mCall.enqueue(new RestBaseCallBack<CheckUpdateResponse>() {
            @Override
            public void onResponse(CheckUpdateResponse response) {
                if (mView != null) {
                    if (response != null) {
                        CheckUpdateUtils.showUpdateDialog(fragment, response, true);
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, int code, String msg) {
            }
        });
    }


    /**
     * 获取首页数据
     *
     * @param fragment
     */
    public void loadData(final BaseFragment fragment) {
        if (mView != null) {
            mCall = mRestService.postData(UrlConfig.HOME_INDEX, new BaseRequest());
            mCall.enqueue(new RestBaseCallBack<HomeResponse>() {
                @Override
                public void onResponse(HomeResponse response) {
                    mView.hideLoading();
                    if (response == null) {
                        return;
                    }

                    mAlreadyLoad = true;
                    mView.loadFinish();
                    mView.loadBanner(response.banners);
                    List<GroupBean> groupBeans = response.group;
                    if (!ExtendUtil.listIsNullOrEmpty(groupBeans)) {
                        for (GroupBean bean : groupBeans) {
                            mView.loadInvest(bean);
                        }
                    }

                    List<NewsBean> newsBeans = response.news;
                    if (!ExtendUtil.listIsNullOrEmpty(newsBeans)) {
                        mView.loadNews(newsBeans);
                    }

                    NoticeBean noticeBean = response.notice;
                    mView.loadNotice(noticeBean);

                    mView.showDialog(response.is_first_login == 1);
                }

                @Override
                public void onFailure(Throwable error, int code, String msg) {
                    if (mView == null) {
                        return;
                    }

                    if (!mAlreadyLoad) {
                        if (!ErrorMsgUtils.showNetErrorPageOrLogin(fragment, code, msg)) {
                            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                        }
                    } else {
                        if (!ErrorMsgUtils.showNetErrorToastOrLogin(fragment, code, msg)) {
                            ToastUtil.showShortToast(ZMApplication.getZMApplication(), msg);
                        }
                    }
                    mView.hideLoading();
                }
            });
        }
    }

    /**
     * 初始化安全模块
     *
     * @param fragment
     * @param titleLayout 大标题
     * @param cellLayout
     */
    public void initSafe(final BaseFragment fragment, RelativeLayout titleLayout, CellLayout cellLayout) {
        initModule(fragment, titleLayout, fragment.getString(R.string.about_us), null);
        String[] titles = fragment.getResources().getStringArray(R.array.home_safe_title);
        String[] contents = fragment.getResources().getStringArray(R.array.home_safe_content);
        int size = 2;
        cellLayout.setColSum(size);
        cellLayout.setIsSquare(false);
        cellLayout.setSpacing((int) mMarginMin);

        int[] icons = new int[]{R.drawable.home_security_icon_manage, R.drawable.home_security_icon_aboutus};

        for (int i = 0; i < size; i++) {
            View view = mLInflater.inflate(R.layout.item_home_safe, null);
            ImageView ivIcon = ButterKnife.findById(view, R.id.iv_item_invest_record_amount);
            ivIcon.setBackgroundResource(icons[i]);
            TextView tvTitle = ButterKnife.findById(view, R.id.tv_item_home_safe_title);
            tvTitle.setText(titles[i]);
            TextView tvContent = ButterKnife.findById(view, R.id.tv_item_home_safe_content);
            tvContent.setText(contents[i]);
            cellLayout.addView(view);
        }

        cellLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    CommonUtils.intentWebView(fragment, null, UrlConfig.H5_ABOUT);
                } else {
                    CommonUtils.intentWebView(fragment, CommonWebViewFragment.TYPE_WEB_HOME_SAFE, null, UrlConfig.H5_SECURITY);
                    StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LAB_SAFE);
                }
            }
        });
    }

    public View getCostomContent(final BaseFragment fragment, View view, final InvestDetailBean itemBean, int size) {
        LinearLayout llChild = ButterKnife.findById(view, R.id.item_home_customize_financial_child);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llChild.getLayoutParams();
        params.height = DensityUtil.dp2px(fragment.mContext, 134);
        TextView tvMoney = ButterKnife.findById(view, R.id.tv_item_home_financila_money);
        TextView tvDate = ButterKnife.findById(view, R.id.tv_item_home_financila_date);
        if (size == 1) {
            params.width = (int) (ScreenUtil.getScreenWidth(fragment.mContext) - mMarginLeft * 2);
            String investMoney = fragment.getString(R.string.start_invest_money);
            String money = investMoney + MoneyFormatUtils.getThousandYuan(itemBean.min_invest_amount) + fragment.getString(R.string.ten_thousand_yuan);
            SpannableString moneySpan = new SpannableString(money);
            moneySpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(fragment.mContext, R.color.black_3e)), money.indexOf(investMoney) + investMoney.length(), money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMoney.setText(moneySpan);

            String limit = fragment.getString(R.string.project_time_limit);
            String date = limit + itemBean.sku_period;
            SpannableString span = new SpannableString(date);
            span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(fragment.mContext, R.color.black_3e)), date.indexOf(limit) + limit.length(), date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvDate.setText(span);

            Button btInvest = ButterKnife.findById(view, R.id.bt_home_costom_invest);
            btInvest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startInvestProductDetailFragment(fragment, itemBean.sku_id);
                }
            });
        } else if (size > 1) {
            if (size == 2) {
                params.width = (int) (ScreenUtil.getScreenWidth(fragment.mContext) - mMarginLeft * 2 - mMarginMin) / 2;
            }

            tvDate.setText(fragment.getString(R.string.invest_home_period_costom) + itemBean.sku_period);
            tvMoney.setText(MoneyFormatUtils.getThousandYuan(itemBean.min_invest_amount) + fragment.getString(R.string.ten_thousand_yuan) + fragment.getString(R.string.start_invest));
        }
        llChild.setLayoutParams(params);

        String rate = MoneyFormatUtils.yieldPointDouble(itemBean.sku_rate);
        ((TextView) ButterKnife.findById(view, R.id.tv_item_home_financila_yield)).setText(rate);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInvestProductDetailFragment(fragment, itemBean.sku_id);
            }
        });

        return view;
    }

    public View getSelectView(final BaseFragment fragment, final InvestDetailBean itemBean, int i) {
        String[] investType = fragment.getResources().getStringArray(R.array.invest_type);

        View view = mLInflater.inflate(R.layout.include_home_module_content, null);
        LinearLayout llTag = ButterKnife.findById(view, R.id.ll_select_tags);

        TextView tvYield = ButterKnife.findById(view, R.id.tv_select_yield);
        String rate = MoneyFormatUtils.yieldPointDouble(itemBean.sku_rate);
        int sIndex = rate.length() - 1;
        //add 1.1 加息
        if (itemBean.is_hike_rate == 1 && itemBean.hike_rate != 0) {
            String hike_rate = fragment.getString(R.string.invest_list_hike_rate, MoneyFormatUtils.yieldPointRuleOne(itemBean.hike_rate));
            rate = rate + hike_rate;
            sIndex = rate.length() - (hike_rate.length() + 1);

        }
        SpannableString ss = new SpannableString(rate);
        ss.setSpan(new AbsoluteSizeSpan(18, true), sIndex, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvYield.setText(ss);

        TextView tvDate = ButterKnife.findById(view, R.id.tv_select_date);
        SpannableString ssDate = new SpannableString(itemBean.sku_period);
        ssDate.setSpan(new AbsoluteSizeSpan(16, true), 0, ssDate.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDate.setText(ssDate);

        TextView tvMoney = ButterKnife.findById(view, R.id.tv_select_money);
        SpannableString ssMoney = new SpannableString(MoneyFormatUtils.lFormatNoPoint(itemBean.c_sku_amount));
        String unit = MoneyFormatUtils.getMoneyUnit(itemBean.c_sku_amount);
        ssMoney.setSpan(new AbsoluteSizeSpan(16, true), 0, ssMoney.length() - unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMoney.setText(ssMoney);

        TextView tvTitle = ButterKnife.findById(view, R.id.tv_select_title);
        tvTitle.setText(fragment.getString(R.string.good) + investType[itemBean.sku_type]);

        ImageView ivState = ButterKnife.findById(view, R.id.iv_home_module_content_bg);
        switch (itemBean.sku_status) {
            case ConstantUtils.SKU_TYPE_ING://立即投资
                CommonUtils.buildLabelIcons(fragment.mContext, llTag, itemBean.sku_tags);
                ivState.setVisibility(View.GONE);
                break;
            default://抢光 和募集结束
                int color = ContextCompat.getColor(fragment.mContext, R.color.gray_8a);
                TextView dataTitle = ButterKnife.findById(view, R.id.tv_select_date_title);
                TextView moneyTitle = ButterKnife.findById(view, R.id.tv_select_money_title);
                ivState.setVisibility(View.VISIBLE);
                tvYield.setTextColor(ContextCompat.getColor(fragment.mContext, R.color.gray_8a));
                if (itemBean.sku_status == ConstantUtils.SKU_TYPE_END) {
                    ivState.setBackgroundResource(R.drawable.common_icon_invest_end);
                } else {
                    ivState.setBackgroundResource(R.drawable.common_icon_invest_sale_out);
                }
                dataTitle.setTextColor(color);
                moneyTitle.setTextColor(color);
                tvTitle.setTextColor(color);
                tvMoney.setTextColor(color);
                tvDate.setTextColor(color);
                CommonUtils.buildLabelIcons(fragment.mContext, llTag, itemBean.sku_tags, R.color.gray_8a);
                break;
        }

        view.setId(i);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_SPECIAL_INVEST, view.getId() + 1);
                startInvestProductDetailFragment(fragment, itemBean.sku_id);
            }
        });

        return view;
    }

    /**
     * 跳转标的详情
     */
    public void startInvestProductDetailFragment(BaseFragment fragment, String skuId) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_ID, skuId);
        fragment.startFragment(InvestProductDetailFragment.class, bundle);
    }

}
