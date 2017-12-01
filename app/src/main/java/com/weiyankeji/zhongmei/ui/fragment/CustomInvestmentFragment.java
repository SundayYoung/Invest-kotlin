package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.utils.ExtendUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.activity.FragmentContainerActivity;
import com.weiyankeji.zhongmei.ui.adapter.CustomInvestAdapter;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MineInvestRecordRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.BasePresenter;
import com.weiyankeji.zhongmei.ui.mpresenter.CustomInvestmentPresenter;
import com.weiyankeji.zhongmei.ui.mview.CustomInvestView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import java.util.ArrayList;


/**
 * @aythor: lilei
 * time: 2017/8/30  下午7:34
 * function:
 */

public class CustomInvestmentFragment extends BaseListFragment<CustomInvestView, CustomInvestmentPresenter> implements CustomInvestView {

    public static final String STYPE = "mtype_tag";

    CustomInvestmentPresenter mCustomInvestmentPresenter;
    CustomInvestAdapter mCustomAdatper;
    ArrayList<CustomInvestBean> mList = new ArrayList<>();
    MineInvestRecordRequest mMineInvestRecordRequest;

    //定期、定制、网贷
    int mType;
    //投资中、已结束
    int mTypeTag;

    public static CustomInvestmentFragment newInstance(int mType, int mTypeTag) {
        CustomInvestmentFragment fragment = new CustomInvestmentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantUtils.KEY_TYPE, mType);
        bundle.putInt(STYPE, mTypeTag);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        setNotTitle();
        setCannotRefresh();
        mRecyclerView.removeItemDecoration(mRecyclerViewDecoration);
        mType = getArguments().getInt(ConstantUtils.KEY_TYPE);
        mTypeTag = getArguments().getInt(STYPE);
        mMineInvestRecordRequest = new MineInvestRecordRequest(mType, mTypeTag);
        mCustomAdatper = new CustomInvestAdapter(R.layout.item_custom_list, mList, mTypeTag, getActivity(), mType);
        mCustomInvestmentPresenter.loadRefresh(mMineInvestRecordRequest);
        setLoading(true);
        mCustomAdatper.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                CustomInvestBean item = (CustomInvestBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantUtils.SKEY_CUSTOM_DETAILS, item);
                bundle.putInt(STYPE, mTypeTag);
                Intent intent = FragmentContainerActivity.getFragmentContainerActivityIntent(getActivity(), CustomDetailsFragment.class, bundle);
                startActivity(intent);
            }
        });
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mCustomInvestmentPresenter.loadRefresh(mMineInvestRecordRequest);
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
        stopRefresh();
        stopLoadMore();
    }

    @Override
    public void setData(ArrayList<CustomInvestBean> list) {
        mCustomAdatper.notifyDataSetChanged();
        if (!ExtendUtil.listIsNullOrEmpty(list)) {
            if (list.size() < ConstantUtils.PAGE_SIZE) {
                setCannotLoad();
            }
            mList.addAll(list);
            mCustomAdatper.notifyDataSetChanged();
        } else {
            showErrorPage(false);
            setCannotLoad();
        }
    }


    @Override
    public void onRefreshCallback() {

    }

    @Override
    public void onLoadCallback() {
        mCustomInvestmentPresenter.loadMore(mMineInvestRecordRequest);
    }

    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mCustomAdatper;
    }

    @Override
    public BasePresenter initPresenter() {
        mCustomInvestmentPresenter = new CustomInvestmentPresenter(this);
        return mCustomInvestmentPresenter;
    }
}
