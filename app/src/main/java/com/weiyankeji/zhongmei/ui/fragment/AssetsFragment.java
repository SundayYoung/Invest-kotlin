package com.weiyankeji.zhongmei.ui.fragment;


import android.os.Bundle;
import android.widget.TextView;

import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.customview.PecentView;
import com.weiyankeji.zhongmei.ui.mmodel.CircularPecent;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomAssetInfoBean;
import com.weiyankeji.zhongmei.ui.mmodel.multi.SkuTypeRequest;
import com.weiyankeji.zhongmei.ui.mpresenter.AssetPresenter;
import com.weiyankeji.zhongmei.ui.mview.CustomView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.MoneyFormatUtils;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;

/**
 * @aythor: lilei
 * time: 2017/8/31  下午5:24
 * function:
 */

public class AssetsFragment extends BaseMvpFragment<CustomView, AssetPresenter> implements CustomView {
    @BindString(R.string.assets_title)
    String mTitle;
    @BindView(R.id.pv_ring)
    PecentView mPVRing;
    @BindView(R.id.tv_total_assets)
    TextView mTVTotalAssets;

    @BindView(R.id.tv_assets_availble)
    TextView mTVAvailble;
    @BindView(R.id.tv_assets_transit)
    TextView mTVTransit;
    @BindView(R.id.tv_assets_principal)
    TextView mTVPrincipal;
    @BindView(R.id.tv_assets_interest)
    TextView mTVInterest;

    @BindView(R.id.tv_assets_availble_precent)
    TextView mTVAvailblePrecent;
    @BindView(R.id.tv_assets_transit_percent)
    TextView mTVTransitPrecent;
    @BindView(R.id.tv_assets_principal_percent)
    TextView mTVPrincipalPrecent;
    @BindView(R.id.tv_assets_interest_percent)
    TextView mTVInterestPrecent;


    @BindDimen(R.dimen.assets_circular_maxradius)
    int mMaxRadius;
    @BindDimen(R.dimen.assets_circular_minradius)
    int mMinRadius;
    @BindColor(R.color.red_f2)
    int mRedColor;
    @BindColor(R.color.blue_7e)
    int mBlueColor7E;
    @BindColor(R.color.blue_63)
    int mBlueColor63;
    @BindColor(R.color.yellow_ff)
    int mYellowColor;

    AssetPresenter mAssetPresenter;
    ArrayList<CircularPecent> mList;

    SkuTypeRequest mSkuTypeRequest;

    @Override
    public int setContentLayout() {
        return R.layout.fragment_assets;
    }

    @Override
    public void finishCreateView(Bundle bundle) {
        addBackListener();
        setTitle(mTitle);
        mSkuTypeRequest = new SkuTypeRequest(ConstantUtils.ASSETS_ALL);
        mAssetPresenter.loadData(this, mSkuTypeRequest);
        getErrorPageView().setOnErrorFunctionClickListener(new ErrorPageView.OnErrorFunctionClickListener() {
            @Override
            public void onClick() {
                mAssetPresenter.loadData(AssetsFragment.this, mSkuTypeRequest);
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
    }

    @Override
    public void setData(CustomAssetInfoBean mCustomAssetInfoBean) {
        if (mCustomAssetInfoBean.total_asset != 0) {
            fillData(mCustomAssetInfoBean);
            drawCicleData(mCustomAssetInfoBean);
        }
    }

    public void fillData(CustomAssetInfoBean mCustomAssetInfoBean) {
        showErrorPage(false);
        if (mCustomAssetInfoBean.total_asset == 0) {
            return;
        }
        mTVTotalAssets.setText(MoneyFormatUtils.lFormatS(mCustomAssetInfoBean.total_asset));
        mTVAvailble.setText(MoneyFormatUtils.lFormatS(mCustomAssetInfoBean.available));
        mTVTransit.setText(MoneyFormatUtils.lFormatS(mCustomAssetInfoBean.road_asset));
        mTVPrincipal.setText(MoneyFormatUtils.lFormatS(mCustomAssetInfoBean.unback_principal));
        mTVInterest.setText(MoneyFormatUtils.lFormatS(mCustomAssetInfoBean.unback_interest));
        double availablePercent = ((mCustomAssetInfoBean.available * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0) * 100);
        double transitPercent = ((mCustomAssetInfoBean.road_asset * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0) * 100);
        double principalPercent = ((mCustomAssetInfoBean.unback_principal * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0) * 100);
        double interestPercent = ((mCustomAssetInfoBean.unback_interest * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0) * 100);
        mTVAvailblePrecent.setText(MoneyFormatUtils.moneyPoint(availablePercent, 2) + "%");
        mTVTransitPrecent.setText(MoneyFormatUtils.moneyPoint(transitPercent, 2) + "%");
        mTVPrincipalPrecent.setText(MoneyFormatUtils.moneyPoint(principalPercent, 2) + "%");
        mTVInterestPrecent.setText(MoneyFormatUtils.moneyPoint(interestPercent, 2) + "%");
    }


    @Override
    public AssetPresenter initPresenter() {
        mAssetPresenter = new AssetPresenter();
        return mAssetPresenter;
    }

    public void drawCicleData(CustomAssetInfoBean mCustomAssetInfoBean) {
        mList = new ArrayList<>();
        mList.add(new CircularPecent(mRedColor, (float) ((mCustomAssetInfoBean.available * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0))));
        mList.add(new CircularPecent(mBlueColor7E, (float) ((mCustomAssetInfoBean.road_asset * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0))));
        mList.add(new CircularPecent(mBlueColor63, (float) ((mCustomAssetInfoBean.unback_principal * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0))));
        mList.add(new CircularPecent(mYellowColor, (float) ((mCustomAssetInfoBean.unback_interest * 1.0) / (mCustomAssetInfoBean.total_asset * 1.0))));
        mPVRing.setData(mList);

    }
}
