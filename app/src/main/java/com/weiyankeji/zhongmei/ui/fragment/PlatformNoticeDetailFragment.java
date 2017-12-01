package com.weiyankeji.zhongmei.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.weiyankeji.library.utils.TimeUtil;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.mmodel.PlatformNoticeDetailResponse;
import com.weiyankeji.zhongmei.ui.mmodel.payment.PlatformNoticeDetailRequset;
import com.weiyankeji.zhongmei.ui.mpresenter.PlatformNoticeDetailPresenter;
import com.weiyankeji.zhongmei.ui.mview.PlatformNoticeDetailView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;

import butterknife.BindView;


/**
 * 平台公告详细界面
 * Created by zff on 2017/9/1.
 */

public class PlatformNoticeDetailFragment extends BaseMvpFragment<PlatformNoticeDetailView, PlatformNoticeDetailPresenter> implements PlatformNoticeDetailView {

    @BindView(R.id.tv_notice_detail_title)
    TextView mTvTitle;
    @BindView(R.id.tv_notice_detail_time)
    TextView mTvTime;
    @BindView(R.id.tv_notice_detail_content)
    TextView mTvContent;

    int mNoticeId;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_platform_notice_detail;
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
    public void finishCreateView(Bundle bundle) {
        mNoticeId = bundle.getInt(ConstantUtils.KEY_ID);
        setTitle(getString(R.string.platform_notice_tile), true);
        mPresenter.loadData(new PlatformNoticeDetailRequset(mNoticeId));
        addBackListener();
    }


    @Override
    public PlatformNoticeDetailPresenter initPresenter() {
        return new PlatformNoticeDetailPresenter();
    }


    @Override
    public void loadNotices(PlatformNoticeDetailResponse itemBean) {
        mTvTitle.setText(itemBean.title);
        mTvContent.setText(itemBean.content);
        mTvTime.setText(TimeUtil.getCostomFormatTime(itemBean.created_at, TimeUtil.FORMAT_YY_MM_DD_HH));
    }


}