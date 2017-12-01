package com.weiyankeji.zhongmei.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.ImageLoaderUtil;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.HotNewsResponse;

import java.util.List;

/**
 * Created by zff on 2017/9/7.
 */
public class HotNewsListAdapter extends BaseRecycleViewAdapter<HotNewsResponse, BaseViewHolder> {
    private Context mContext;

    public HotNewsListAdapter(Context context, List<HotNewsResponse> beans) {
        super(R.layout.item_news_list, beans);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotNewsResponse item) {
        if (item != null) {
            helper.setText(R.id.tv_hot_news_title, item.title);
            helper.setText(R.id.tv_hot_news_date, TimeUtil.getYearMonthDayFormatStr(item.created_at));
            ImageView icon = helper.getView(R.id.iv_hot_news_img);
            if (TextUtils.isEmpty(item.app_img_url)) {
                icon.setVisibility(View.GONE);
            } else {
                icon.setVisibility(View.VISIBLE);
                ImageLoaderUtil.loadImageFromUrl(mContext, item.app_img_url, icon);
            }


        }
    }
}
