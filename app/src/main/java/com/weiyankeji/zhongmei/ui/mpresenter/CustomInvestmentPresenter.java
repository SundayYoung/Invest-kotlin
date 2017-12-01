package com.weiyankeji.zhongmei.ui.mpresenter;

import android.content.Intent;

import com.weiyankeji.library.net.client.RestBaseCallBack;
import com.weiyankeji.zhongmei.R;
import com.weiyankeji.zhongmei.api.UrlConfig;
import com.weiyankeji.zhongmei.application.ZMApplication;
import com.weiyankeji.zhongmei.ui.activity.MainActivity;
import com.weiyankeji.zhongmei.ui.customview.ErrorPageView;
import com.weiyankeji.zhongmei.ui.fragment.BaseFragment;
import com.weiyankeji.zhongmei.ui.mmodel.bean.CustomInvestBean;
import com.weiyankeji.zhongmei.ui.mmodel.mine.MineInvestRecordRequest;
import com.weiyankeji.zhongmei.ui.mview.CustomInvestView;
import com.weiyankeji.zhongmei.utils.ConstantUtils;
import com.weiyankeji.zhongmei.utils.ErrorMsgUtils;

import java.util.ArrayList;

/**
 * @aythor: lilei
 * time: 2017/8/30  下午7:36
 * function:
 */

public class CustomInvestmentPresenter extends BasePresenter<CustomInvestView> {

    BaseFragment mFragment;

    public CustomInvestmentPresenter(BaseFragment mFragment) {
        this.mFragment = mFragment;
    }

    /**
     * 加载更多
     */
    public void loadMore(MineInvestRecordRequest mineInvestRecordRequest) {
        mineInvestRecordRequest.page += 1;
        loadData(mineInvestRecordRequest);
    }

    /**
     * 刷新
     */
    public void loadRefresh(MineInvestRecordRequest mineInvestRecordRequest) {
        mineInvestRecordRequest.page = 1;
        loadData(mineInvestRecordRequest);
    }

    public void loadData(final MineInvestRecordRequest request) {
        mView.showLoading();
        mCall = mRestService.postData(UrlConfig.MINE_USER_INVEST_RECORD, request);
        mCall.enqueue(new RestBaseCallBack<ArrayList<CustomInvestBean>>() {
            @Override
            public void onResponse(ArrayList<CustomInvestBean> response) {
                mView.hideLoading();
                if (response == null) {
                    return;
                }
                mView.setData(response);
                if (request.page == 1 && response.isEmpty()) {
                    mFragment.setErrorPageView(R.drawable.common_img_norecord, ZMApplication.getZMApplication().getString(R.string.you_not_have_available_custom), ZMApplication.getZMContext().getString(R.string.immediate_investment), new ErrorPageView.OnErrorFunctionClickListener() {
                        @Override
                        public void onClick() {
                            mFragment.getActivity().finish();
                            Intent intent = new Intent(mFragment.getActivity(), MainActivity.class);
                            intent.putExtra(ConstantUtils.KEY_TYPE, MainActivity.TYPE_CUSTOM_FINISH);
                            mFragment.startActivity(intent);

                        }
                    });
                }

            }


            @Override
            public void onFailure(Throwable error, int code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    if (request.page == 1) {
                        ErrorMsgUtils.showNetErrView(mFragment);
                    }
                }
            }
        });
    }
}