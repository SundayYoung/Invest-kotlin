package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.weiyankeji.library.customview.swipetoloadlayout.OnRefreshListener;
import com.weiyankeji.library.customview.swipetoloadlayout.SwipeToLoadLayout;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.adapter.InvestHomeAdapter;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.TwitterRefreshHeaderView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.InvestDetailBean;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeResponse;
import com.weiyankeji.zhongmei.ui.mmodel.invest.InvestHomeSkuList;
import com.weiyankeji.zhongmei.ui.mpresenter.InvestHomePresenter;
import com.weiyankeji.zhongmei.ui.mview.InvestHomeView;
import com.weiyankeji.zhongmei.utils.CommonUtils;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;
import com.weiyankeji.zhongmei.utils.StatisticalUtils;

import butterknife.BindView;

/**
 * Created by liuhaiyang on 2017/8/21.
 */

public class InvestFragment extends BaseMvpFragment<InvestHomeView, InvestHomePresenter> implements InvestHomeView, InvestHomeAdapter.OnItemButtonClickListener,
        OnRefreshListener {

    @BindView(R.id.stl_invest)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @BindView(R.id.swipe_target)
    ExpandableListView mElvList;

    private ImageView mIvBrannerImg;

    private boolean mIsFirst = true;
    private InvestHomeAdapter mAdapter;

    private float mHeiht;

    //是否加载过数据
    private boolean mAlreadyLoad;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_invest;
    }

    @Override
    public void finishCreateView(final Bundle bundle) {
        setTitle(R.string.bottomview_main_invest);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshFinalDragOffset(TwitterRefreshHeaderView.REFRESH_MAX_DRAG_OFFSET);

        mElvList.setGroupIndicator(null);
        mElvList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                InvestHomeSkuList group = mAdapter.getGroup(groupPosition);
                Bundle parm = new Bundle();
                parm.putString(ConstantUtils.KEY_TITLE, group.title);
                parm.putInt(ConstantUtils.KEY_TYPE, group.type);
                startFragment(InvestListFragment.class, parm);
                return true;
            }
        });

        mElvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPos, int childPos, long l) {
                InvestDetailBean child = mAdapter.getChild(groupPos, childPos);
                Bundle parm = new Bundle();
                parm.putString(ConstantUtils.KEY_ID, child.sku_id);
                startFragment(InvestProductDetailFragment.class, parm);
                statistical(child.sku_type, childPos + 1);
                return false;
            }
        });

        mAdapter = new InvestHomeAdapter(mContext, this);
        mElvList.setAdapter(mAdapter);

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mPresenter.loadData(true);
            }
        });

        mHeiht = CommonUtils.getHeighScale(mContext);

        showContentView(false);

    }

    public void statistical(int type, int index) {
        if (type == ConstantUtils.INVEST_SKU_TYPE_STIPULATE) {
            StatisticalUtils.trackCustomKVEvent(StatisticalUtils.LABLE_INVEST_REGULAR, index);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mPresenter != null && mIsFirst) {
            mPresenter.loadData(true);
            mIsFirst = false;
        }
    }

    @Override
    public InvestHomePresenter initPresenter() {
        return new InvestHomePresenter();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void setData(InvestHomeResponse response) {
        showContentView(true);
        mAlreadyLoad = true;
        if (!ExtendUtil.listIsNullOrEmpty(response.getGroup())) {
            mAdapter.updateAdapter(response.getGroup());
            expandAllGroup();
        }
        setBanner(response);
    }

    @Override
    public void netError(int code, String msg) {
        if (!mAlreadyLoad) {
            if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
                ToastUtil.showShortToast(mContext.getApplicationContext(), msg);
            }
        } else {
            if (!ErrorMsgUtils.showNetErrorToastOrLogin(this, code, msg)) {
                ToastUtil.showShortToast(mContext.getApplicationContext(), msg);
            }
        }

    }

    private void expandAllGroup() {
        for (int i = 0, size = mAdapter.getGroupCount(); i < size; i++) {
            mElvList.expandGroup(i);
        }
    }

    private void setBanner(final InvestHomeResponse response) {
        if (response == null || ExtendUtil.listIsNullOrEmpty(response.getBanners())) {
            return;
        }
        if (mIvBrannerImg == null) {
            mIvBrannerImg = new ImageView(mContext);
        }
        mIvBrannerImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageLoaderUtil.loadImageFromUrl(mContext, response.getBanners().get(0).img_url, mIvBrannerImg, R.drawable.invest_banner_default);
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, (int) mHeiht);
        mIvBrannerImg.setLayoutParams(params);
        if (mElvList.getHeaderViewsCount() == 0) {
            mElvList.addHeaderView(mIvBrannerImg);
        }

        mElvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                mIvBrannerImg.scrollTo(0, (int) (-getScrollY() * 0.5));
            }
        });
    }

    /**
     * expand list y value
     *
     * @return
     */
    public int getScrollY() {
        View c = mElvList.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mElvList.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public void onItemButtonClick(InvestDetailBean itemList) {
        if (itemList == null) {
            return;
        }
        Bundle param = new Bundle();
        param.putString(ConstantUtils.KEY_ID, itemList.sku_id);
        startFragment(InvestProductDetailFragment.class, param);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(false);
    }
}
