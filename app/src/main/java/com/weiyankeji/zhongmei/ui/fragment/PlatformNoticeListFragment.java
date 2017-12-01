package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.library.utils.SharedPreferencesUtil;
import com.weiyankeji.library.utils.ToastUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.adapter.PlatformNoticeAdapter;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.PlatformNoticeListPresenter;
import com.weiyankeji.zhongmei.ui.mview.PlatformNoticeListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * 平台公告列表页
 * Created by zff on 2017/9/1.
 */

public class PlatformNoticeListFragment extends BaseListFragment<PlatformNoticeListView, PlatformNoticeListPresenter>
        implements PlatformNoticeListView {
    private List<PlatformNoticeResponse> mItemBeans = new ArrayList<>();

    private PlatformNoticeAdapter mAdapter;

    private PlatformNoticeListPresenter mNoticeLPresenter;

    @Override
    public void onRefreshCallback() {
        mNoticeLPresenter.loadData(false);
    }

    @Override
    public void onLoadCallback() {
        mNoticeLPresenter.loadData(false);
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.platform_notice_tile), true);
        getRecyclerView().removeItemDecoration(mRecyclerViewDecoration);
        SharedPreferencesUtil.put(getActivity(), ConstantUtils.SHAREDPREFERENCES_NAME, ConstantUtils.SP_MESSAGE_MESSAGE, false);

        mAdapter = new PlatformNoticeAdapter(getActivity(), mItemBeans);
        mAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                startFragment(PlatformNoticeDetailFragment.class, getBundle(mItemBeans.get(position).id));

            }
        });
        mNoticeLPresenter = ((PlatformNoticeListPresenter) mPresenter);
        mNoticeLPresenter.loadData(true);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                setLoading(true);
                onRefreshCallback();
            }
        });

    }

    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public PlatformNoticeListPresenter initPresenter() {
        return new PlatformNoticeListPresenter();
    }

    @Override
    public void hideLoading() {
        stopRefresh();
        stopLoadMore();
        setLoading(false);
    }


    @Override
    public void showLoading() {
        setLoading(true);

    }


    @Override
    public void loadNotices(List<PlatformNoticeResponse> itemBeans) {
        showErrorPage(false);
        showErrorPageView(itemBeans, 0, null);
        if (!ExtendUtil.listIsNullOrEmpty(itemBeans)) {
            if (itemBeans.size() < ConstantUtils.PAGE_SIZE) {
                setCannotLoad();
            }
            mItemBeans.addAll(itemBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            setCannotLoad();
        }

    }

    private Bundle getBundle(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_ID, id);
        return bundle;
    }


    @Override
    public void showLoadFial(int code, String msg) {
        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            ToastUtil.showShortToast(ZMApplication.getZMContext(), msg);
        }
    }

    private void showErrorPageView(List<PlatformNoticeResponse> itemBeans, int code, String msg) {

        if (!ErrorMsgUtils.showNetErrorPageOrLogin(this, code, msg)) {
            if (ExtendUtil.listIsNullOrEmpty(itemBeans) && mItemBeans.isEmpty()) {
                setErrorPageView(R.drawable.common_img_norecord, getString(R.string.empty_error), null, null);
                return;
            }
        }
    }
}
