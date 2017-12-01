package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.weiyankeji.library.customview.swipetoloadlayout.OnLoadMoreListener;
import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.BonusDetailsAdapter;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.RecyclerViewDecoration;
import com.weiyankeji.zhongmei.ui.mpresenter.BonusDetailsPresenter;
import com.weiyankeji.zhongmei.ui.mview.BonusDetailsView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zff on 2017/9/26.
 */

public class BonusDetailsFragment extends BaseMvpFragment<BonusDetailsView, BonusDetailsPresenter> implements BonusDetailsView, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.tv_bonus_details_bigtv)
    TextView mTvBonusTitle;
    @BindView(R.id.tv_bonus_details_smalltv)
    TextView mTvBonusMoney;
    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout mSwipeRefresh;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    public RecyclerViewDecoration mRecyclerViewDecoration;
    private BonusDetailsAdapter mAdapter;
    private List mListItemBeans = new ArrayList<>();
    private int mPage = 1;
    private long mDay;
    private String mInviteeUid;
    private int mStatus;

    @Override
    public void finishCreateView(Bundle bundle) {
        initData();
        mAdapter = new BonusDetailsAdapter(mContext, mListItemBeans);
        mRecyclerView.setAdapter(mAdapter);
        mStatus = bundle.getInt(ConstantUtils.KEY_TYPE);
        if (mStatus == ConstantUtils.BOUNS_DETAILS_DAYS) {
            mDay = bundle.getLong(ConstantUtils.KEY_TITLE);
            mTvBonusTitle.setText(TimeUtil.getYearMonthDayFormatStr(mDay));
            mTvBonusMoney.setText(getString(R.string.invite_total_reward_detail, MoneyFormatUtils.doubleChangeF2Y(bundle.getInt(ConstantUtils.KEY_DATA))));

            mPresenter.loadBonusDayData(mDay, mPage, true);
        } else {
            mTvBonusTitle.setText(getString(R.string.invite_total_invite_user_title, bundle.getString(ConstantUtils.KEY_TITLE)));
            mTvBonusMoney.setText(getString(R.string.invite_total_reward_detail, MoneyFormatUtils.doubleChangeF2Y(bundle.getInt(ConstantUtils.KEY_DATA))));
            mInviteeUid = bundle.getString(ConstantUtils.KEY_ID);

            mPresenter.loadBonusInviteeData(mInviteeUid, mPage, true);
        }

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                setLoading(true);
                onRefresh();

            }
        });
    }

    @Override
    public int setContentLayout() {
        return R.layout.fragment_bonus_details;
    }

    @Override
    public BonusDetailsPresenter initPresenter() {
        return new BonusDetailsPresenter();
    }


    @Override
    public void showLoading() {
        setLoading(true);

    }

    @Override
    public void hideLoading() {
        setLoading(false);
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        if (mSwipeRefresh.isLoadingMore()) {
            mSwipeRefresh.setLoadingMore(false);
        }

    }

    @Override
    public void onRefresh() {
        if (mListItemBeans != null) {
            mListItemBeans.clear();
        }
        mPage = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadData();
    }


    @Override
    public void setListData(List itemBeans) {
        showErrorPage(false);
        if (!ExtendUtil.listIsNullOrEmpty(itemBeans)) {
            if (itemBeans.size() < ConstantUtils.PAGE_SIZE) {
                mSwipeRefresh.setLoadMoreEnabled(false);
            }
            mListItemBeans.addAll(itemBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            mSwipeRefresh.setLoadMoreEnabled(false);
        }

    }

    @Override
    public void showLoadFial(int code, String msg) {
        ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg);
    }

    private void initData() {
        setTitle(R.string.bonus_datails_title);
        addBackListener();
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setOnLoadMoreListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerViewDecoration = new RecyclerViewDecoration(mContext);
        mRecyclerView.addItemDecoration(mRecyclerViewDecoration);
    }


    private void loadData() {
        if (mStatus == ConstantUtils.BOUNS_DETAILS_DAYS) {
            mPresenter.loadBonusDayData(mDay, mPage, false);
        } else {
            mPresenter.loadBonusInviteeData(mInviteeUid, mPage, false);
        }
    }
}
