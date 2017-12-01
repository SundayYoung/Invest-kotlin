package com.weiyankeji.zhongmei.ui.adapter;

import android.content.Context;

import com.weiyankeji.library.customview.adapter.BaseRecycleViewAdapter;
import com.weiyankeji.library.customview.adapter.BaseViewHolder;
import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeResponse;
import java.util.List;




/**
 * Created by zff on 2017/9/1.
 */
public class PlatformNoticeAdapter extends BaseRecycleViewAdapter<PlatformNoticeResponse, BaseViewHolder> {


    public PlatformNoticeAdapter(Context context, List<PlatformNoticeResponse> beans) {
        super(R.layout.item_platform_notice, beans);
    }


    @Override
    protected void convert(BaseViewHolder helper, PlatformNoticeResponse item) {
        if (item != null) {
            helper.setText(R.id.tv_platform_nl_title, item.title);
            helper.setText(R.id.tv_platform_nl_date, TimeUtil.getMonthDayFormatStr(item.created_at));
            helper.setText(R.id.tv_platform_nl_content, item.content);
        }
    }


}
