package com.weiyankeji.zhongmei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.mmodel.TicketListResponse;
import com.weiyankeji.zhongmei.ui.mpresenter.TicketListPresenter;
import com.weiyankeji.zhongmei.ui.mview.TicketListView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡券
 * Created by caiwancheng on 2017/8/30.
 */


public class TicketListFragment extends BaseListFragment<TicketListView, TicketListPresenter> implements TicketListView {

    private int mType = ConstantUtils.TICKET_UNUSE;

    private TicketAdapter mTicketAdapter;

    private List<TicketListResponse> mItemBeans = new ArrayList<>();

    private TicketListResponse mItem;

    private boolean mIsFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mIsFirst) {
            mIsFirst = false;
            if (mType != ConstantUtils.TICKET_AVAILABLE && mType != ConstantUtils.TICKET_UNUSE) {
                loadUrl();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onRefreshCallback() {

    }

    @Override
    public void onLoadCallback() {
        ((TicketListPresenter) mPresenter).loadMyTicketData(TicketListFragment.this, mType + "");
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setNotTitle();
        setCannotRefresh();

        RecyclerView recyclerView = getRecyclerView();
        recyclerView.removeItemDecoration(mRecyclerViewDecoration);
        Bundle bundle1 = getArguments();
        mTicketAdapter = new TicketAdapter(R.layout.item_ticket_list, mItemBeans);
        mType = bundle1.getInt(ConstantUtils.KEY_TYPE);
        mItem = (TicketListResponse) bundle1.getSerializable(ConstantUtils.KEY_DATA);

        if (mType == ConstantUtils.TICKET_AVAILABLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_ticket_footer, null);
            LinearLayout linearLayout = view.findViewById(R.id.ll_card_ticket_footer);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().setResult(ConstantUtils.INTENT_REQUEST);
                    getActivity().onBackPressed();
                }
            });
            if (mItem == null) {
                RadioButton button = view.findViewById(R.id.rb_card_ticket_footer);
                button.setChecked(true);
            }
            mTicketAdapter.setFooterView(view);
        }

        if (mType == ConstantUtils.TICKET_AVAILABLE || mType == ConstantUtils.TICKET_UNUSE) {
            loadUrl();
        }

        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                if (mType == ConstantUtils.TICKET_AVAILABLE || mType == ConstantUtils.TICKET_UNAVAILABLE) {
                    loadInvestTicket(getArguments());
                } else {
                    ((TicketListPresenter) mPresenter).loadMyTicketData(TicketListFragment.this, mType + "");
                }
            }
        });
    }

    public void loadUrl() {
        if (mType == ConstantUtils.TICKET_AVAILABLE || mType == ConstantUtils.TICKET_UNAVAILABLE) {
            setCannotLoad();
            loadInvestTicket(getArguments());
            if (mType == ConstantUtils.TICKET_AVAILABLE) {
                mTicketAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseRecycleViewAdapter adapter, View view, int position) {
                        Intent intent = new Intent();
                        intent.putExtra(ConstantUtils.KEY_DATA, mItemBeans.get(position));
                        getActivity().setResult(ConstantUtils.INTENT_REQUEST, intent);
                        getActivity().onBackPressed();
                    }
                });
            }
        } else {
            ((TicketListPresenter) mPresenter).loadMyTicketData(this, mType + "");
        }
    }

    public void loadInvestTicket(Bundle bundle) {
        String skuId = bundle.getString(ConstantUtils.KEY_ID);
        long money = bundle.getLong(ConstantUtils.KEY_VALUE);
        if (mType == ConstantUtils.TICKET_AVAILABLE) {
            ((TicketListPresenter) mPresenter).loadInvestTicketDate(this, skuId, 1, money);
        } else {
            ((TicketListPresenter) mPresenter).loadInvestTicketDate(this, skuId, 0, money);
        }
    }

    @Override
    public BaseRecycleViewAdapter getAdapter() {
        return mTicketAdapter;
    }

    @Override
    public TicketListPresenter initPresenter() {
        return new TicketListPresenter();
    }

    @Override
    public void showLoading() {
        setLoading(true);
    }

    @Override
    public void hideLoading() {
        setLoading(false);
        stopLoadMore();
    }

    @Override
    public void setListData(List<TicketListResponse> response) {
        if (response.size() < ConstantUtils.PAGE_SIZE) {
            setCannotLoad();
        }
        mItemBeans.addAll(response);
        mTicketAdapter.notifyDataSetChanged();
    }

    public class TicketAdapter extends BaseRecycleViewAdapter<TicketListResponse, BaseViewHolder> {

        public TicketAdapter(@LayoutRes int layoutResId, @Nullable List<TicketListResponse> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TicketListResponse item) {
            RelativeLayout llBase = helper.getView(R.id.ll_item_ticket);
            TextView tvValue = helper.getView(R.id.tv_item_ticket_value);
            TextView tvType = helper.getView(R.id.tv_item_ticket_title);

            TextView tvUseDesc = helper.getView(R.id.tv_item_ticket_use_desc);
            TextView tvUseDate = helper.getView(R.id.tv_item_ticket_use_date);
            TextView tvLimit = helper.getView(R.id.tv_item_ticket_money_limit);
            String value = item.market_card_type == ConstantUtils.MARKET_CARD_TYPE_VOUCHERS
                    ? getString(R.string.common_yuan, MoneyFormatUtils.lFormatYuanNoPoint(item.market_card_value)) : getString(R.string.common_rate_percentage, MoneyFormatUtils.yieldPointRuleOne((int) item.market_card_value));
            SpannableString ssDate = new SpannableString(value);
            ssDate.setSpan(new AbsoluteSizeSpan(14, true), value.length() - 1, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (mType == ConstantUtils.TICKET_UNUSE || mType == ConstantUtils.TICKET_AVAILABLE) {
                int colorBlue = item.market_card_type == ConstantUtils.MARKET_CARD_TYPE_VOUCHERS ? ContextCompat.getColor(mContext, R.color.blue_00) : ContextCompat.getColor(mContext, R.color.orange_fc);
                tvValue.setTextColor(colorBlue);
                tvType.setTextColor(colorBlue);
                int colorGray = ContextCompat.getColor(mContext, R.color.gray_c3);
                tvLimit.setTextColor(colorGray);
                tvUseDesc.setTextColor(colorGray);
                tvUseDate.setTextColor(colorGray);
                llBase.setBackgroundResource(item.market_card_type == 0 ? R.drawable.common_ticket_jiaxiquan_normal : R.drawable.common_ticket_daijinquan_normal);
            }

            if (mType == ConstantUtils.TICKET_AVAILABLE) {
                if (mItem != null && mItem.market_card_id == item.market_card_id) {
                    llBase.setBackgroundResource(item.market_card_type == 0 ? R.drawable.common_ticket_jiaxiquan_selected : R.drawable.common_ticket_daijinquan_selected);
                } else {
                    llBase.setBackgroundResource(item.market_card_type == 0 ? R.drawable.common_ticket_jiaxiquan_normal : R.drawable.common_ticket_daijinquan_normal);
                }
            }

            String s1 = MoneyFormatUtils.lFormatYuanNoPoint(item.low_money);
            String money = item.market_card_type == ConstantUtils.MARKET_CARD_TYPE_VOUCHERS
                    ? getString(R.string.ticket_money_hint, s1) : getString(R.string.ticket_percentage_hint, s1);
            tvLimit.setText(money);

            String date = mContext.getString(R.string.ticket_date_hint, item.sku_duration + "");
            tvUseDate.setText(date);


            tvType.setText(item.market_card_title);
            tvValue.setText(ssDate);
            tvUseDesc.setText(item.sku_type);

            String time = TimeUtil.getYearMonthDayFormatStr(item.due_time) + getString(R.string.due_time);
            TextView tvTime = helper.getView(R.id.tv_item_ticket_time);
            tvTime.setText(time);
        }
    }
}
