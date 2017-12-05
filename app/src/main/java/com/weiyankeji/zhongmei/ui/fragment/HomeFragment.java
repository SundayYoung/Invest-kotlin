package com.weiyankeji.zhongmei.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.library.utils.DensityUtil;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.ScreenUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.customview.CellLayout;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.TitleBarView;
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.BannerBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.GroupBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.MenuBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NewsBean;
import com.weiyankeji.zhongmei.ui.mmodel.bean.NoticeBean;
import com.weiyankeji.zhongmei.ui.mpresenter.HomePresenter;
import com.weiyankeji.zhongmei.ui.mview.HomeView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.StatisticalUtils;
import com.weiyankeji.zhongmei.utils.UserUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caiwancheng on 2017/8/15.
 */

public class HomeFragment extends BaseMvpFragment<HomeView, HomePresenter> implements HomeView, OnRefreshListener, ViewPager.OnPageChangeListener {
    public final float mBannerWHRate = 0.3611111f;

    @BindView(R.id.stl_home)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target)
    ScrollView mScrollView;

    @BindView(R.id.home_convenientBanner)
    ConvenientBanner mConvenientBanner;

    @BindView(R.id.rl_home_customized_financial)
    RelativeLayout mRlCostom;
    @BindView(R.id.rl_home_select)
    RelativeLayout mRlSelect;
    @BindView(R.id.rl_home_hot)
    RelativeLayout mRlHot;
    @BindView(R.id.rl_home_safe)
    RelativeLayout mRlAbout;
    @BindView(R.id.ll_home_select_content)
    LinearLayout mLlSelectContent;
    @BindView(R.id.cl_home_safe)
    CellLayout mClSafe;
    @BindView(R.id.ll_home_customize_financial)
    LinearLayout mLlCostomizeFinancial;
    @BindView(R.id.ll_home_news_content)
    LinearLayout mLlNews;
    @BindView(R.id.ll_home_new_message)
    LinearLayout mLlNewMessage;
    @BindView(R.id.ib_home_notice_cancel)
    ImageButton mIbNoticeCancel;
    @BindView(R.id.ib_home_notice_content)
    TextView mTvNoticeContent;

    private LayoutInflater mLInflater;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_home;
    }

    private NoticeBean mNoticeBean;

    @Override
    public void finishCreateView(Bundle bundle) {
        mLInflater = LayoutInflater.from(getActivity());
        initTitle();

        mConvenientBanner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenWidth(getActivity()) * mBannerWHRate)));
        mConvenientBanner.setCanLoop(false);
        mPresenter.initSafe(this, mRlAbout, mClSafe);
        mConvenientBanner.setOnPageChangeListener(this);
        showLoading();
        mPresenter.loadData(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mPresenter.loadData(HomeFragment.this);
            }
        });
        mTvNoticeContent.setSelected(true);
        mPresenter.checkUpdate(this);

        showContentView(false);
    }

    /**
     * 初始化众美标题
     */
    private void initTitle() {
        setTitle(R.string.app_name);
        setBaseTitleColor(R.color.white);
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        if (!mConvenientBanner.isTurning()) {
            mConvenientBanner.startTurning(3000);
        }

        removeBar();
        if (!UserUtils.isUserLogin()) {
            MenuBean menuBean = new MenuBean(getString(R.string.login));
            menuBean.setColorId(R.color.white);
            addBar(menuBean);
            setTitleBackground(R.color.blue_00);
            setOnItemBarListener(new TitleBarView.OnItemClickBarListener() {
                @Override
                public void onClick(View v, int postion) {
                    if (postion == 0) {
                        CommonUtils.intentLoginFragment(HomeFragment.this, -1);
                    }
                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        ////停止翻页
        mConvenientBanner.stopTurning();
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public HomePresenter initPresenter() {
        HomePresenter homePresenter = new HomePresenter(getActivity());
        return homePresenter;
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
        mSwipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void loadBanner(final List<BannerBean> banners) {
        if (ExtendUtil.listIsNullOrEmpty(banners)) {
            mConvenientBanner.setVisibility(View.GONE);
            mConvenientBanner.setCanLoop(false);
            return;
        }

        mConvenientBanner.setVisibility(View.VISIBLE);
        mConvenientBanner.setCanLoop(banners.size() == 1 ? false : true);
        mConvenientBanner.setPages(new CBViewHolderCreator<ImageHolderView>() {
            @Override
            public ImageHolderView createHolder() {
                return new ImageHolderView();
            }
        }, banners);

        if (banners.size() > 1) {
            mConvenientBanner.setPageIndicator(new int[]{R.drawable.guangbiao_tran, R.drawable.guangbiao})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        } else {
            mConvenientBanner.setPageIndicator(new int[]{-1, -1})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        }

        mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BannerBean bean = banners.get(position);
                if (TextUtils.isEmpty(bean.link_url)) {
                    return;
                }

                //add by v1.1
                if (bean.link_url.startsWith(getString(R.string.home_banner_head))) {
                    if (bean.is_need_login == 1 && !UserUtils.isUserLogin()) {
                        CommonUtils.intentLoginFragment(HomeFragment.this, -1);
                        return;
                    }
                    if (bean.link_url.endsWith(getString(R.string.home_banner_investlist))) { //标的列表
                        startFragment(InvestListFragment.class);
                    } else if (bean.link_url.endsWith(getString(R.string.home_banner_invite))) { //我的邀请
                        startFragment(MyInviteFragment.class);
                    } else if (bean.link_url.endsWith(getString(R.string.home_banner_regular))) { //我的定期
                        Bundle mBundle = new Bundle();
                        mBundle.putInt(ConstantUtils.SKEY_SIGN_TAG, ConstantUtils.INVEST_SKU_TYPE_STIPULATE);
                        startFragment(CustomFragment.class, mBundle);
                    } else if (bean.link_url.endsWith(getString(R.string.home_banner_card))) { //我的卡券
                        Bundle mBundle = new Bundle();
                        mBundle.putInt(ConstantUtils.KEY_TYPE, CardTicketFragment.TYPE_MY_TICKET);
                        startFragment(CardTicketFragment.class, mBundle);
                    }
                } else {
                    CommonUtils.intentWebView(HomeFragment.this, null, bean.link_url);
                }
            }
        });
    }

    @Override
    public void loadFinish() {
        mLlCostomizeFinancial.setVisibility(View.GONE);
        mRlCostom.setVisibility(View.GONE);
        mRlSelect.setVisibility(View.GONE);
        mLlSelectContent.setVisibility(View.GONE);
        mRlHot.setVisibility(View.GONE);
        mLlNews.setVisibility(View.GONE);
        showContentView(true);
    }

    public void loadCostomInvest(List<InvestDetailBean> itemBeans) {
        int size = itemBeans.size();
        if (size >= 3) {
            for (int i = 0; i < size; i++) {
                View view = mLInflater.inflate(R.layout.item_home_customize_financial_more, null);
                mLlCostomizeFinancial.addView(mPresenter.getCostomContent(this, view, itemBeans.get(i), itemBeans.size()));
            }
        } else if (size == 2) {
            int width = (int) (ScreenUtil.getScreenWidth(getActivity()) - mPresenter.mMarginLeft * 2 - mPresenter.mMarginMin) / 2;
            for (int i = 0; i < 2; i++) {
                View view = mLInflater.inflate(R.layout.item_home_customize_financial_more, null);
                LinearLayout llChild = ButterKnife.findById(view, R.id.item_home_customize_financial_child);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llChild.getLayoutParams();
                params.width = width;
                params.height = DensityUtil.dp2px(getActivity(), 134);
                llChild.setLayoutParams(params);
                mLlCostomizeFinancial.addView(mPresenter.getCostomContent(this, view, itemBeans.get(i), itemBeans.size()));
            }
        } else if (size == 1) {
            View view = mLInflater.inflate(R.layout.item_home_customize_financial_one, null);
            mLlCostomizeFinancial.addView(mPresenter.getCostomContent(this, view, itemBeans.get(0), itemBeans.size()));
        } else {
            mLlCostomizeFinancial.setVisibility(View.GONE);
            mRlCostom.setVisibility(View.GONE);
        }
    }

    public void loadSelectInvest(List<InvestDetailBean> itemBeans) {
        for (int i = 0; i < itemBeans.size(); i++) {
            final InvestDetailBean itemBean = itemBeans.get(i);
            mLlSelectContent.addView(mPresenter.getSelectView(this, itemBean, i));
        }
    }

    @Override
    public void loadInvest(GroupBean bean) {
        if (bean.type == ConstantUtils.TYPE_CUSTOM_INVEST) {
            mLlCostomizeFinancial.setVisibility(View.VISIBLE);
            mRlCostom.setVisibility(View.VISIBLE);
            mLlCostomizeFinancial.removeAllViews();
            mPresenter.initModule(this, mRlCostom, bean.title, getString(R.string.more_invest), 1);
            loadCostomInvest(bean.list);
        } else if (bean.type == ConstantUtils.TYPE_SELECT_INVEST) {
            mRlSelect.setVisibility(View.VISIBLE);
            mLlSelectContent.setVisibility(View.VISIBLE);
            mLlSelectContent.removeAllViews();
            mPresenter.initModule(this, mRlSelect, bean.title, null);
            loadSelectInvest(bean.list);
        }
    }


    @Override
    public void loadNews(List<NewsBean> itemBeans) {
        mPresenter.initModule(this, mRlHot, getString(R.string.hot_attention), getString(R.string.more_attention), 3);
        mRlHot.setVisibility(View.VISIBLE);
        mLlNews.removeAllViews();
        mLlNews.setVisibility(View.VISIBLE);
        int width = (int) (ScreenUtil.getScreenWidth(getActivity()) - mPresenter.mMarginLeft * 2 - mPresenter.mMarginMin) / 2;
        for (int i = 0; i < itemBeans.size(); i++) {
            final NewsBean itemBean = itemBeans.get(i);
            final View view = mLInflater.inflate(R.layout.include_home_hot, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, (int) (width * 0.4125));
            if (i != 0) {
                params.setMargins((int) mPresenter.mMarginMin, 0, 0, 0);
            }
            view.setLayoutParams(params);
            ImageView imageView = ButterKnife.findById(view, R.id.iv_home_hot_bg);
            ImageLoaderUtil.loadArcImage(mContext, itemBean.bg_img_url, imageView, 2, R.drawable.home_news_default);
            view.setId(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonUtils.intentWebView(HomeFragment.this, null, itemBean.link_url);
                    StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_NEWS, view.getId() + 1);
                }
            });
            mLlNews.addView(view);
        }
    }

    @Override
    public void loadNotice(NoticeBean bean) {
        if (bean == null || !mPresenter.isShowNotice(bean)) {
            mLlNewMessage.setVisibility(View.GONE);
        } else {
            mNoticeBean = bean;
            mLlNewMessage.setVisibility(View.VISIBLE);
            mTvNoticeContent.setText(bean.content);
        }
    }

    @Override
    public void showDialog(boolean isShow) {
        if (!isShow) {
            return;
        }
        final Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_home, null);
        //detail
        contentView.findViewById(R.id.tv_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtils.isUserLogin()) {
                    CommonUtils.intentLoginFragment(HomeFragment.this, -1);
                    return;
                }
                Bundle mBundle = new Bundle();
                mBundle.putInt(ConstantUtils.KEY_TYPE, CardTicketFragment.TYPE_MY_TICKET);
                startFragment(CardTicketFragment.class, mBundle);
                dialog.dismiss();
            }
        });
        //cancel
        contentView.findViewById(R.id.ib_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public class ImageHolderView implements Holder<BannerBean> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            mImageView = new ImageView(context);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return mImageView;
        }

        @Override
        public void UpdateUI(Context context, int position, BannerBean bean) {
            ImageLoaderUtil.loadImageFromUrl(context, bean.img_url, mImageView, R.drawable.home_banner_default);
        }
    }


    /**
     * 小红点显示
     *
     * @param view
     */
    @OnClick({R.id.ib_home_notice_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_home_notice_cancel:
                if (mNoticeBean != null) {
                    mLlNewMessage.setVisibility(View.GONE);
                    mPresenter.saveNoticeId(mNoticeBean.id);
                }
                break;
            default:
        }
    }

}
