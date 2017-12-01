package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.BankInfoResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.BankListPresenter;
import com.weiyankeji.zhongmei.ui.mview.BankListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 支持的银行卡列表
 * Created by caiwancheng on 2017/8/28.
 */

public class BankListFragment extends BaseMvpFragment<BankListView, BankListPresenter> implements BankListView {

    @BindView(R.id.rv_costom_recycler_view)
    RecyclerView mRvBank;

    public BankListAdapter mBankListAdapter;

    public List<BankInfoResponse> mItemBeans = new ArrayList<>();

    private String mBankName;

    @Override
    public BankListPresenter initPresenter() {
        return new BankListPresenter();
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
    public int setContentLayout() {
        return R.layout.costom_recyclerview;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        if (bundle != null) {
            mBankName = bundle.getString(ConstantUtils.KEY_TITLE);
        }
        setTitle(getString(R.string.bank_account_select), true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBank.setLayoutManager(layoutManager);
        mBankListAdapter = new BankListAdapter(R.layout.item_bank_list, mItemBeans);
        mPresenter.loadBankList(this);
        mRvBank.setAdapter(mBankListAdapter);
        mBankListAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.bank_list_header, null));
        mBankListAdapter.addFooterView(LayoutInflater.from(mContext).inflate(R.layout.bank_list_footer, null));
        mBankListAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(ConstantUtils.KEY_DATA, mItemBeans.get(position));
                getActivity().setResult(1, intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void requstBankList(List<BankInfoResponse> itemBeans) {
        mItemBeans.addAll(itemBeans);
        mBankListAdapter.notifyDataSetChanged();
    }

    public class BankListAdapter extends BaseRecycleViewAdapter<BankInfoResponse, BaseViewHolder> {

        public BankListAdapter(@LayoutRes int layoutResId, @Nullable List<BankInfoResponse> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankInfoResponse item) {
            helper.setText(R.id.tv_item_bank_list_title, item.bank_name);
            String once = MoneyFormatUtils.getThousandYuan(item.charge_once_limit);
            String limit = MoneyFormatUtils.getThousandYuan(item.charge_day_limit);
            String data = mContext.getString(R.string.bank_money);
            data = String.format(data, once, limit);
            helper.setText(R.id.tv_item_bank_list_hint, data);
            RadioButton button = helper.getView(R.id.rb_item_bank_list_select);
            if (!TextUtils.isEmpty(mBankName) && mBankName.equals(item.bank_name)) {
                button.setChecked(true);
            } else {
                button.setChecked(false);
            }
            ImageView icon = helper.getView(R.id.iv_item_bank_card_icon);
            ImageLoaderUtil.loadImageFromUrl(getContext(), item.logo_small, icon);
        }
    }
}
